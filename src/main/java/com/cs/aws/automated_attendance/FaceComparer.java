package com.cs.aws.automated_attendance;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;

public class FaceComparer {
    private AmazonRekognition rekognitionClient;
    private ManageCollection mc;
    private AmazonSNS snsClient;
    private AmazonS3 s3Client;

    @Value("${rekognitionConfigs.collectionName}")
    private String collectionName;

    @Value("${rekognitionConfigs.localPath}")
    private String localPath;

    public FaceComparer() {

        AWSCredentials credentials;
        /**
        https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html#cli-quick-configuration
         **/
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
        }

        rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        snsClient = AmazonSNSClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        s3Client = AmazonS3ClientBuilder.standard().withRegion("us-east-1").withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        System.out.println("\nAWS Rekognition Initialized...");

        mc = new ManageCollection(rekognitionClient);

      //  loadTargetImages();

    }

    /**
     * CREATE COLLECTION AND LOAD IMAGED FROM REPOSITORY
     */
    public void loadTargetImages() {
        try {
            mc.createCollection();
            mc.addFacesToCollection();
//            mc.addFaceToCollectionFromS3();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void indexUploadedImage(String name) {
        try {
            mc.createCollection();
            mc.addFaceToCollectionFromS3(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * COMPARE IMAGE WITH COLLECTION INDEXED IMAGES
     */
    public String compare(Image source){
        String photoName = mc.searchFacesByImageResult(source);
        /**
         * Remove .jpg part from name
         */
        String name = photoName.split("\\.")[0];
        return name;
    }
}
