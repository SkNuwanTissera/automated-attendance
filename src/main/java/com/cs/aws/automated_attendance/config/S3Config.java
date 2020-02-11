package com.cs.aws.automated_attendance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Configuration
//@EnableConfigurationProperties
//@ConfigurationProperties
public class S3Config {

    private String endpointUrl ="https://s3.us-east-2.amazonaws.com";
    private String faceBucket = "bucketsk1995";

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getFaceBucket() {
        return faceBucket;
    }

    public void setFaceBucket(String faceBucket) {
        this.faceBucket = faceBucket;
    }
}
