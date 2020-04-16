package com.cs.aws.automated_attendance.controllers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

//@RestController
//@CrossOrigin
//@RequestMapping("/api/collections")
public class CollectionController {


    AmazonRekognition rekognition = AmazonRekognitionClientBuilder.standard().withRegion("us-east-1")
            .withCredentials(new AWSStaticCredentialsProvider(new ProfileCredentialsProvider("default").getCredentials())).build();

    /**
     * createCollection
     */
//    @PostMapping("/create")
    public void createCollection(@PathVariable String collectionName) {
        try {
            CreateCollectionRequest req = new CreateCollectionRequest();
            req.setCollectionId(collectionName);
            CreateCollectionResult result = rekognition.createCollection(req);
            if(result.getStatusCode()==200)
                System.out.println("Collection created");
        } catch (ResourceAlreadyExistsException e) {
            System.out.println("Collection already created");
        }
    }

    /**
     * delete Collection
     */
//    @DeleteMapping("/delete")
    public void deleteCollection(@PathVariable String collectionId){
        try {
            
            System.out.println("Deleting collections");

            DeleteCollectionRequest request = new DeleteCollectionRequest()
                    .withCollectionId(collectionId);
            DeleteCollectionResult deleteCollectionResult = rekognition.deleteCollection(request);

            System.out.println(collectionId + ": " + deleteCollectionResult.getStatusCode()
                    .toString());

        } catch (ResourceInUseException e){
            System.out.println("Collection is already is use !!");
        }
    }
}
