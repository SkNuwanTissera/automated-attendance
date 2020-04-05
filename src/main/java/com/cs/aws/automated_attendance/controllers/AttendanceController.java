package com.cs.aws.automated_attendance.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.cs.aws.automated_attendance.FaceComparer;
import com.cs.aws.automated_attendance.LambdaTrigger;
import com.cs.aws.automated_attendance.S3uploader;
import com.cs.aws.automated_attendance.dto.FaceFileDto;
import com.cs.aws.automated_attendance.dto.StudentDto;
import com.cs.aws.automated_attendance.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;



@RestController
@RequestMapping("/face")
@CrossOrigin
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    /**
     * Match face in input image with trained classifier
     * @param path
     * @return
     */
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

    /**
     * Upload files to S3
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestPart(value = "file") MultipartFile file){

        S3uploader s3uploader = new S3uploader();
        FaceComparer faceComparer = new FaceComparer();
        String fileName = "";
        try{
            // Upload a text string as a new object.
            fileName = s3uploader.uploadFile(file);
            System.out.println("File Uploaded Successfully !! "+fileName);
            faceComparer.indexUploadedImage(fileName);
            return fileName;
            //faceComparer.loadTargetImages();

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

    /**
     * Index images in repository to train a classifier
     * @return
     */
    @GetMapping("/index")
    public String indexImages(){
        FaceComparer faceComparer = new FaceComparer();
        faceComparer.loadTargetImages();
        return "Successfully Indexed !!";
    }

    /**
     * Sample Lambda trigger
     * @return
     */
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


    @PostMapping("/getFaceImage")
    public StudentDto getFaceImage(@RequestBody FaceFileDto faceFileDto) throws Exception {
        System.out.println("kahuna matata"+faceFileDto.getImagedataURl());
        StudentDto studentDto=attendanceService.markAttendace(faceFileDto.getImagedataURl());
       return studentDto;
    }

}
