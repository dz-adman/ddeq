package com.dz.dynamodb.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.backoff.BackoffStrategy;
import software.amazon.awssdk.core.retry.conditions.RetryCondition;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;
import java.time.Duration;

/**
 * Auto-configuration for Amazon DynamoDB, providing a bean for DynamoDbClient.
 * This configuration class reads properties from the application configuration
 * and sets up the DynamoDbClient with the specified settings.
 */
@Configuration
@EnableConfigurationProperties(DynamoDBProperties.class)
@ConditionalOnClass(DynamoDbClient.class)
@ConditionalOnProperty(prefix = "dynamodb", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamoDBAutoConfiguration {

    private final DynamoDBProperties properties;

    /**
     * Constructs a new DynamoDBAutoConfiguration instance with the given properties.
     *
     * @param properties the DynamoDBProperties instance containing the configuration settings
     */
    public DynamoDBAutoConfiguration(DynamoDBProperties properties) {
        this.properties = properties;
    }

    /**
     * Creates a new DynamoDbClient bean with the specified settings from the DynamoDBProperties.
     * If a bean of the same type already exists, this method will not be called.
     *
     * @return a new DynamoDbClient instance
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamoDbClient dynamoDbClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder();

        if (StringUtils.hasText(properties.getEndpoint())) {
            builder.endpointOverride(URI.create(properties.getEndpoint()));
        }

        if (StringUtils.hasText(properties.getRegion())) {
            builder.region(Region.of(properties.getRegion()));
        } else {
            builder.region(new DefaultAwsRegionProviderChain().getRegion());
        }

        if (StringUtils.hasText(properties.getAccessKey()) && StringUtils.hasText(properties.getSecretKey())) {
            builder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())));
        } else {
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        // Use UrlConnectionHttpClient for better compatibility with Java 17 (the default Apache Http client has been removed since Java 11)
        builder.httpClientBuilder(UrlConnectionHttpClient.builder());

        ClientOverrideConfiguration.Builder overrideConfigurationBuilder = ClientOverrideConfiguration.builder();

        if (properties.getApiCallTimeout() != null) {
            overrideConfigurationBuilder.apiCallTimeout(Duration.ofSeconds(properties.getApiCallTimeout()));
        }

        if (properties.getNumRetries() != null) {
            RetryPolicy.Builder retryPolicyBuilder = RetryPolicy.builder()
                    .numRetries(properties.getNumRetries())
                    .retryCondition(RetryCondition.defaultRetryCondition())
                    .backoffStrategy(BackoffStrategy.defaultThrottlingStrategy());
            overrideConfigurationBuilder.retryPolicy(retryPolicyBuilder.build());
        }

        builder.overrideConfiguration(overrideConfigurationBuilder.build());

        return builder.build();
    }
}
