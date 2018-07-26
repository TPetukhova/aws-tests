package Aws;

import base.TestBase;
import com.sun.org.glassfish.gmbal.Description;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.DynamoDbHelper.*;
import static utils.LambdaHelper.*;
import static utils.S3helper.checkBucketExists;
import static utils.S3helper.getBucketLocation;

public class CheckAwsEntities extends TestBase {

    @Test
    @Description("Check parameters of S3 bucket, DynamoDB table and Lambda function")
    public void checkAwsParameters() {

        // 1 - Check parameters of S3 bucket
        checkBucketExists(bucketName);
        assertEquals(getBucketLocation(bucketName), "US", "S3 bucket location is incorrect.");

        // 2 - Check parameters of DynamoDB
        checkTableExists(region, tableName);
        assertEquals(getTableStatus(region, tableName), "ACTIVE", "DynamoDB table status is incorrect.");
        assertEquals(getTableKeyAttributes(region, tableName), Arrays.asList("packageId", "originTimeStamp"));
        assertEquals(getTableAttributes(region, tableName), Arrays.asList("filePath (S)", "fileType (S)", "originTimeStamp (N)", "packageId (S)"));

        // 3 - Check parameters of Lambda
        checkLambdaExists(lambdaName);
        assertEquals(getLambdaHandler(lambdaName), "com.ebsco.platform.infrastructure.inventoringlambda.IngestionLambda");
        assertTrue(getLambdaRole(lambdaName).endsWith("lambda-role"));
        assertTrue(getLambdaEnv(lambdaName).contains("{InventoryLambda_DYNAMODB_NAME=tpet-table}"));

    }

}



