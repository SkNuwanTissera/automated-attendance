package com.cs.aws.automated_attendance;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class ManageCollection {
    AmazonRekognition rekognition = null;

//    @Value("${rekognitionConfigs.collectionName}")
    private String collectionName = "cloudProject2";

//   @Value("${rekognitionConfigs.localPath}")
    private String localPath = "src/main/java/com/cs/aws/automated_attendance/images_c/";

//   @Value("${s3config.faceBucket}")
    private String s3BucketName = "bucketsk1995";
    /**
     * Constructors
     */
    public ManageCollection(AmazonRekognition amazonRekognition) {
        rekognition = amazonRekognition;
    }

    /**
     * createCollection
     */
    public void createCollection() {
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
     * addFacesToCollection using file in local machine
     */
    public void addFacesToCollection() {
        if(localPath!=null)
            processCollection(localPath);
        else
            System.out.println("No Local Path!!");
    }

    private void processCollection(String path) {
        try {

            File directory = new File(path);
            File[] files = directory.listFiles();
            for (File targetImgFileName : files) {
                try {
                    ByteBuffer targetImageBytes = null;
                    InputStream inputStream = new FileInputStream(targetImgFileName);
                    targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
                    Image target = new Image().withBytes(targetImageBytes);

                    IndexFacesRequest req = new IndexFacesRequest();
                    req.setCollectionId(collectionName);
                    req.setImage(target);
                    req.setExternalImageId(targetImgFileName.getName().toLowerCase().replace(".jpg", ""));

                    IndexFacesResult result = rekognition.indexFaces(req);
                    for(FaceRecord fr : result.getFaceRecords()) {
                        System.out.println(fr.getFace().getExternalImageId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(files.length+" faces indexed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * addFacesToCollection from s3
     */
    public void addFaceToCollectionFromS3(String name) {

        Image image = new Image()
                .withS3Object(new S3Object()
                        .withBucket(s3BucketName)
                        .withName(name));

        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(image)
                .withQualityFilter(QualityFilter.AUTO)
                .withMaxFaces(1)
                .withCollectionId(collectionName)
                .withExternalImageId(name)
                .withDetectionAttributes("DEFAULT");

        IndexFacesResult indexFacesResult = rekognition.indexFaces(indexFacesRequest);

        System.out.println("Results for " + name );
        System.out.println("Faces indexed:");
        List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
        for (FaceRecord faceRecord : faceRecords) {
            System.out.println("  Face ID: " + faceRecord.getFace().getFaceId());
            System.out.println("  Location:" + faceRecord.getFaceDetail().getBoundingBox().toString());
        }

        List<UnindexedFace> unindexedFaces = indexFacesResult.getUnindexedFaces();
        System.out.println("Faces not indexed:");
        for (UnindexedFace unindexedFace : unindexedFaces) {
            System.out.println("  Location:" + unindexedFace.getFaceDetail().getBoundingBox().toString());
            System.out.println("  Reasons:");
            for (String reason : unindexedFace.getReasons()) {
                System.out.println("   " + reason);
            }
        }
    }

    /**
     * searchFacesByImageResult
     */
    public String searchFacesByImageResult(Image image) {
        try {
            SearchFacesByImageRequest req = new SearchFacesByImageRequest();
            req.setCollectionId(collectionName);
            req.setImage(image);
            req.setFaceMatchThreshold(70F);
            req.withMaxFaces(1);
            SearchFacesByImageResult result = rekognition.searchFacesByImage(req);
            for (FaceMatch fm : result.getFaceMatches()) {
                return fm.getFace().getExternalImageId();
            }
        } catch (InvalidParameterException e) {
            System.out.println("Image quality is poor"+ e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
