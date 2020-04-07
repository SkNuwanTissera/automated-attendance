package com.cs.aws.automated_attendance;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cs.aws.automated_attendance.config.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class S3uploader {

//    @Autowired
//    private S3Config s3Config;

    private String endpointUrl ="https://s3.us-east-2.amazonaws.com";
    private String faceBucket = "bucketsk1995";

    private AmazonS3 amazonS3;

    public S3uploader() {
        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
        }

        amazonS3 = AmazonS3ClientBuilder.standard().withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        System.out.println("\nAWS S3 service Initialized...");

    }


    private void uploadFileTos3bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(faceBucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        String fileName = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + faceBucket + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public String uploadFileWithIndexNumber(MultipartFile multipartFile, String indexNumber){
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileUrl = endpointUrl + "/" + faceBucket + "/" + indexNumber;
            uploadFileTos3bucket(indexNumber, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * TODO : Improve file name creation logic
     * @param multiPart
     * @return
     */
    private String generateFileName(MultipartFile multiPart) {
//        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
        return multiPart.getOriginalFilename();
    }

    /**
     * Read S3 bucket
     * TODO : Modify the function
     * @param s3BucketName
     * @return
     */
    private ObjectListing readFiles(String s3BucketName){
        ObjectListing objectListing = amazonS3.listObjects(s3BucketName);
        return objectListing;
    }

}
