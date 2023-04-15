package com.dz.dynamodb.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dynamodb")
public class DynamoDBProperties {
    private String endpoint;
    private String region;
    private String accessKey;
    private String secretKey;
    private Integer apiCallTimeout;
    private Integer numRetries;

    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccessKey() {
        return accessKey;
    }
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getApiCallTimeout() { return apiCallTimeout; }
    public void setApiCallTimeout(Integer apiCallTimeout) { this.apiCallTimeout = apiCallTimeout; }

    public Integer getNumRetries() { return numRetries; }
    public void setNumRetries(Integer numRetries) { this.numRetries = numRetries; }
}
