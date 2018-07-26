package utils;

import base.TestBase;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.FunctionConfiguration;
import com.amazonaws.services.lambda.model.GetFunctionRequest;
import com.amazonaws.services.lambda.model.GetFunctionResult;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

public class LambdaHelper extends TestBase {

    public static void checkLambdaExists(String lambdaName) {
        AWSLambda lambdaClient = AWSLambdaClientBuilder.standard().withRegion(region).build();

        List<FunctionConfiguration> functions = lambdaClient.listFunctions().getFunctions();
        assertTrue(functions.stream().map(FunctionConfiguration::getFunctionName).collect(Collectors.toList()).contains(lambdaName));
    }

    public static String getLambdaHandler(String lambdaName) {
        AWSLambda lambdaClient = AWSLambdaClientBuilder.standard().withRegion(region).build();

        GetFunctionRequest request = new GetFunctionRequest().withFunctionName(lambdaName);
        GetFunctionResult response = lambdaClient.getFunction(request);

        return response.getConfiguration().getHandler();

    }

    public static String getLambdaRole(String lambdaName) {
        AWSLambda lambdaClient = AWSLambdaClientBuilder.standard().withRegion(region).build();

        GetFunctionRequest request = new GetFunctionRequest().withFunctionName(lambdaName);
        GetFunctionResult response = lambdaClient.getFunction(request);

        return response.getConfiguration().getRole();
    }

    public static String getLambdaEnv(String lambdaName) {
        AWSLambda lambdaClient = AWSLambdaClientBuilder.standard().withRegion(region).build();

        GetFunctionRequest request = new GetFunctionRequest().withFunctionName(lambdaName);
        GetFunctionResult response = lambdaClient.getFunction(request);

        return response.getConfiguration().getEnvironment().toString();
    }

}
