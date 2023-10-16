package com.basak.dalcom.aws.lambda.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@Service
public class LambdaService {

    private final LambdaClient lambdaClient;

    public LambdaService() {
        this.lambdaClient = LambdaClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }

    public String invokeLambda(String functionName) {
        InvokeRequest invokeRequest = InvokeRequest.builder()
            .functionName(functionName)
            .build();

        try {
            InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
            return invokeResponse.payload().asUtf8String();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
