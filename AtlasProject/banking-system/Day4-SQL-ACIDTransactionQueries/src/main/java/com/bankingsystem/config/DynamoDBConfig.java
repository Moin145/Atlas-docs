package com.bankingsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Configuration
public class DynamoDBConfig {
    
    @Value("${aws.region:ap-south-1}")
    private String awsRegion;
    
    @Value("${aws.dynamodb.local.port:8003}")
    private int localPort;
    
    @Value("${aws.dynamodb.local.enabled:true}")
    private boolean localEnabled;
    
    @Bean
    public DynamoDbClient dynamoDbClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create());
        
        // Configure for local DynamoDB if enabled
        if (localEnabled) {
            builder.endpointOverride(java.net.URI.create("http://localhost:" + localPort));
        }
        
        return builder.build();
    }
    
}
