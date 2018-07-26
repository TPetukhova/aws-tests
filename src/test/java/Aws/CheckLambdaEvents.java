package Aws;

import base.TestBase;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.sun.org.glassfish.gmbal.Description;
import org.junit.jupiter.api.Test;

import java.util.List;

import static utils.DynamoDbHelper.*;
import static utils.S3helper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CheckLambdaEvents extends TestBase {

    // to do - need to make test file as parameter

    @Test
    @Description("Check that lambda is triggered when file uploaded to S3 bucket")
    public void checkLambdaCalled() throws Exception {

        // 1 - Upload file to S3 bucket
        String keyName = uploadFileToS3(region, bucketName, filePath);

        // 2 - Check Lambda was triggered and new entry appeared in DynamoDB
        List<Item> entries = searchDynamoDbEntry(region, tableName, keyName);
        assertEquals(entries.size(), 1);
        checkEntryFormat(entries.get(0), getFileSize(filePath));

        // 3 - Clean up
        deleteItem(region,tableName,keyName); // not implemented yet

    }

    @Test
    @Description("Check that lambda is triggered when file uploaded to S3 bucket")
    public void checkLambdaCalledWithRemoval() throws Exception {

        // 1 - Upload file to S3 bucket
        String keyName = uploadFileToS3(region, bucketName, filePath);

        // 2 - Remove file from S3 bucket
        removeFileFromS3(region, bucketName, keyName);

        // 3 - Check Lambda was triggered and new entry appeared in DynamoDB
        List<Item> entries = searchDynamoDbEntry(region, tableName, keyName);
        assertEquals(entries.size(), 1);
        checkEntryFormat(entries.get(0), getFileSize(filePath));

        // 3 - Clean up
        deleteItem(region,tableName,keyName); // not implemented yet
    }

}
