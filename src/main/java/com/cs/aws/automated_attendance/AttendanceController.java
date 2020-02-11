package com.cs.aws.automated_attendance;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@RestController
@RequestMapping("/face")
public class AttendanceController {

    @PostMapping("/match")
    public String match(@RequestPart(value = "path") String path){

        FaceComparer faceComparer = new FaceComparer();

        ByteBuffer sourceImageBytes = null;
        try {
            sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(new FileInputStream(new File(path))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image source = new Image().withBytes(sourceImageBytes);

        String name = faceComparer.compare(source);
        System.out.printf(name);

        return name;
    }

    @PostMapping("/upload")
    public String upload(@RequestPart(value = "file") MultipartFile file){

        S3uploader s3uploader = new S3uploader();
        try{
            // Upload a text string as a new object.
            return s3uploader.uploadFile(file);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return "Error in Uploading !!";
    }

    @GetMapping("/")
    public String index(){
        return "<h1>Automated Attendance System</h1>";
    }

    @GetMapping("/trigger")
    public Integer triggerLambda(){

        LambdaTrigger lambdaTrigger = new LambdaTrigger();

        try {
            return lambdaTrigger.invoke().getStatusCode();

        }catch (SdkClientException e){
            e.printStackTrace();
        }
        return 404;
    }

}
