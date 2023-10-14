package com.basak.dalcom.config.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class DynamoDBConfig {

    private final BasicAWSCredentials awsCredentials;

    @Getter
    @Value("${aws.dynamodb.table.name}")
    private String defaultTableName;

//    @Bean
//    public TableNameOverride tableNameOverrider() {
//        return TableNameOverride.withTableNameReplacement(dynamoDBTableName);
//    }

    @Bean
    public String defaultTableName() {
        return defaultTableName;
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(awsCredentials);
        return AmazonDynamoDBClientBuilder.standard()
            .withCredentials(provider)
            .build();
    }
}
