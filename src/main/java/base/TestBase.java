package base;

import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.util.Properties;

public class TestBase {

    protected static String bucketName;
    protected static String tableName;
    protected static String lambdaName;
    protected static String region;
    protected static String filePath;

    @BeforeAll
    protected static void beforeAll() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("gradle.properties"));

        bucketName = properties.getProperty("bucketName");
        tableName = properties.getProperty("tableName");
        lambdaName = properties.getProperty("lambdaName");
        region = properties.getProperty("region");
        filePath = properties.getProperty("filePath");

        if (bucketName.isEmpty() || tableName.isEmpty() || lambdaName.isEmpty() || region.isEmpty() || filePath.isEmpty())
        {
            throw new Exception("AWS parameters are not setup properly"); // change to proper exception type
        }
        System.out.println("Properties:\nbucket name: " + bucketName +
                "\ntable name: " + tableName + "\nlambda name: " + lambdaName +
                "\nclient region: " + region + "\nfile to upload: " + filePath);
    }
}
