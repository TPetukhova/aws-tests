package utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.xspec.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamoDbHelper {

    public static void checkTableExists(String region, String tableName) {

        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
        List<String> tables = dynamoDB.listTables().getTableNames();

        assertTrue(tables.contains(tableName), "Dynamo DB doesn't exist. Please check configuration.");
    }

    public static String getTableStatus(String region, String tableName) {

        String tableStatus = "";
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();

        try {
            tableStatus = dynamoDB.describeTable(tableName).getTable().getTableStatus();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        return tableStatus;
    }

    public static List<String> getTableKeyAttributes(String region, String tableName) {

        List<String> keyAttributes = new ArrayList<>();
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();

        try {
            TableDescription tableInfo = dynamoDB.describeTable(tableName).getTable();

            if (tableInfo != null) {
                for (KeySchemaElement element : tableInfo.getKeySchema()) {
                    keyAttributes.add(element.getAttributeName());
                }
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        return keyAttributes;
    }


    public static List<String> getTableAttributes(String region, String tableName) {

        List<String> attributes = new ArrayList<>();
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();

        try {
            TableDescription tableInfo = dynamoDB.describeTable(tableName).getTable();

            if (tableInfo != null) {
                for (AttributeDefinition definition : tableInfo.getAttributeDefinitions()) {
                    attributes.add(definition.getAttributeName() + " (" + definition.getAttributeType() + ")");
                }
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        return attributes;
    }

    public static List<Item> searchDynamoDbEntry(String region, String tableName, String keyName) throws InterruptedException {

        // to do a proper waiting
        Thread.sleep(5000);

        List<Item> entries = new ArrayList<>();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
        ScanExpressionSpec expression = new ExpressionSpecBuilder().withCondition(ExpressionSpecBuilder.S("filePath").eq(keyName)).buildForScan();

        System.out.println("Looking for an entry in the database.");
        ItemCollection<ScanOutcome> scanOutcome = (new DynamoDB(client).getTable(tableName)).scan(expression);
        Iterator<Item> iterator = scanOutcome.iterator();

        while (iterator.hasNext()) {
            Item item = iterator.next();
            entries.add(item);
        }

        return entries;
    }

    public static void checkEntryFormat(Item item, String size) {

        assertEquals(item.get("actionName"), "ObjectCreated:Put");
        assertEquals(item.get("fileSize").toString(), size);

    }

    public static void deleteItem(String region, String tableName, String keyName) {
        // to do
    }

    public static String getFileSize(String filePath) {
        return String.valueOf(new File(filePath).length());
    }
}
