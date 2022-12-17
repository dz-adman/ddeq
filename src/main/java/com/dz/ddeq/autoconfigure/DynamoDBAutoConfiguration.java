package com.dz.ddeq.autoconfigure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@AutoConfiguration
@EnableConfigurationProperties(DynamoDBProperties.class)
@ConditionalOnClass({AmazonDynamoDB.class, DynamoDB.class, DynamoDBMapper.class})
@ConditionalOnProperty(prefix = "dynamodb", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamoDBAutoConfiguration {

    @Autowired
    private DynamoDBProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        if (StringUtils.hasText(properties.getEndpoint())) {
            builder.setEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(properties.getEndpoint(), properties.getRegion()));
        } else if (StringUtils.hasText(properties.getRegion()))
            builder.setRegion(properties.getRegion());
        if (StringUtils.hasText(properties.getAccessKey()) && StringUtils.hasText(properties.getSecretKey())) {
            builder.setCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey())));
        }
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) { return new DynamoDB(amazonDynamoDB); }

    @Bean
    @ConditionalOnMissingBean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }
}
