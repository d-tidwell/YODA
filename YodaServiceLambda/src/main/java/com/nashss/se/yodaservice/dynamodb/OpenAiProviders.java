package com.nashss.se.yodaservice.dynamodb;

 import software.amazon.awssdk.regions.Region;
 import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
 import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
 import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
 import software.amazon.awssdk.services.secretsmanager.model.InvalidParameterException;
 import software.amazon.awssdk.services.secretsmanager.model.InvalidRequestException;
 import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;

public class OpenAiProviders {

    private static OpenAiService openAiService;

    public static OpenAiService getOpenAiService() {
        if (openAiService == null) {
            openAiService = new OpenAiService(OpenAiProviders.getSecret(), Duration.ofSeconds(120));
        }
        return openAiService;
    }


    public static String getSecret() {

        String secretName = "YODAMD";
        Region region = Region.of("us-east-2");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (ResourceNotFoundException | InvalidRequestException | InvalidParameterException e) {
            // For a list of exceptions thrown, see
            // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
            throw new RuntimeException("The requested secret was not found.", e);
        } catch (Exception e) {
            // Catch any other exceptions and handle them as necessary
            // Depending on your application requirements, you might want to log the exception or
            // convert it to a custom application exception
            throw new RuntimeException("The requested secret threw an uncaught error"+ e.getStackTrace().toString());
        }

        String secret = getSecretValueResponse.secretString();

        return secret;
    }
}