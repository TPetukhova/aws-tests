package utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class S3helper {

    public static String uploadFileToS3(String clientRegion, String bucketName, String filePath) throws Exception {
        DefaultAWSCredentialsProviderChain providerChain = new DefaultAWSCredentialsProviderChain();

        String[] pathParts = filePath.split("\\.");

        String keyName = (pathParts.length > 1) ? System.currentTimeMillis() + "." + pathParts[pathParts.length - 1] : String.valueOf(System.currentTimeMillis());
        System.out.println("Keyname of uploaded file: " + keyName);

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(providerChain)
                    .build();
            TransferManager transferManager = TransferManagerBuilder.standard()
                    .withS3Client(s3Client)
                    .build();
            Upload upload = transferManager.upload(bucketName, keyName, new File(filePath));
            System.out.println("File upload has been started");
            upload.waitForCompletion();
            System.out.println("File has been successfully uploaded to S3 bucket '" + bucketName + "'");
            transferManager.shutdownNow();

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return keyName;
    }

    public static void removeFileFromS3(String clientRegion, String bucketName, String keyName) {
        DefaultAWSCredentialsProviderChain providerChain = new DefaultAWSCredentialsProviderChain();

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(providerChain)
                    .build();

            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            System.out.println("File has been successfully removed");

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public static void checkBucketExists(String bucketName) {
        DefaultAWSCredentialsProviderChain providerChain = new DefaultAWSCredentialsProviderChain();

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(providerChain)
                .build();

        assertTrue(s3Client.doesBucketExistV2(bucketName), "S3 bucket doesn't exist. Please check configuration.");

    }

    public static String getBucketLocation(String bucketName) {
        DefaultAWSCredentialsProviderChain providerChain = new DefaultAWSCredentialsProviderChain();

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(providerChain)
                .build();

        return s3Client.getBucketLocation(bucketName);
    }

}
