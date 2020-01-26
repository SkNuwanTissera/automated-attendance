package com.cs.aws.automated_attendance;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ManageCollection {
    AmazonRekognition rekognition = null;

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
            req.setCollectionId("cloudProject");
            CreateCollectionResult result = rekognition.createCollection(req);
            if(result.getStatusCode()==200)
                System.out.println("Collection created");
        } catch (ResourceAlreadyExistsException e) {
            System.out.println("Collection already created");
        }
    }

    /**
     * addFacesToCollection
     */
    public void addFacesToCollection() {
        try {
            File directory = new File("/Users/nuwantissera/Documents/GitHub/FaceDetector/imgs/faces/");
            File[] files = directory.listFiles();
            for (File targetImgFileName : files) {
                try {
                    ByteBuffer targetImageBytes = null;
                    InputStream inputStream = new FileInputStream(targetImgFileName);
                    targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
                    Image target = new Image().withBytes(targetImageBytes);

                    IndexFacesRequest req = new IndexFacesRequest();
                    req.setCollectionId("cloudProject");
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
     * searchFacesByImageResult
     */
    public String searchFacesByImageResult(Image image) {
        try {
            SearchFacesByImageRequest req = new SearchFacesByImageRequest();
            req.setCollectionId("cloudProject");
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
