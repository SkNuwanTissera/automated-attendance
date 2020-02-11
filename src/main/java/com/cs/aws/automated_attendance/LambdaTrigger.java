package com.cs.aws.automated_attendance;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class LambdaTrigger {

    private AWSLambda lambda;

    public LambdaTrigger(){

        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
        }

        lambda = AWSLambdaClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        System.out.println("\nAWS Lambda service Initialized...");
    }

    public InvokeResult invoke() {
        //Create an InvokeRequest with required parameters
        InvokeRequest req = new InvokeRequest()
                .withFunctionName("newImageTrigger");
//                .withPayload("{ ... }"); // optional
        //Invoke the function and capture response
        InvokeResult result = lambda.invoke(req);
        return result;
    }
}
