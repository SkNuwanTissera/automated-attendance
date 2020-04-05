package com.cs.aws.automated_attendance.services.servicesImpl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.cs.aws.automated_attendance.FaceComparer;
import com.cs.aws.automated_attendance.controllers.AmazonSES;
import com.cs.aws.automated_attendance.dto.SESEmail;
import com.cs.aws.automated_attendance.entity.Student;
import com.cs.aws.automated_attendance.S3uploader;
import com.cs.aws.automated_attendance.repository.StudentRepository;
import com.cs.aws.automated_attendance.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.util.ArrayList;
@Service
@Transactional
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void saveStudent(String student,MultipartFile file) throws Exception {

        Student student1 = studentRepository.save(this.getstudentObject(student));
        System.out.println("RDS Updated with new student "+ student1.getFname());
        S3uploader s3uploader = new S3uploader();
        FaceComparer faceComparer = new FaceComparer();
        String fileUrl = "";
        try{

           // file.getOriginalFilename().replace(file.getOriginalFilename(),student1.getFname().toLowerCase());
            fileUrl = s3uploader.uploadFile(file);

            //Note : Rename Image with First Name.

            faceComparer.indexUploadedImage(file.getOriginalFilename());
            System.out.println("File Uploaded Successfully !! "+fileUrl);

            AmazonSES amazonSES = new AmazonSES();
            amazonSES.sendEmail(this.getEmailSendObject(student));

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
       // return "Error in Uploading !!";
    }

    private Student getstudentObject(String student1) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();
        JSONObject jsonObject=new JSONObject(student1);

        Student student=new Student();
        for (int i = 0; i < jsonObject.length(); i++) {
            stringArray.add((String) jsonObject.get("fname"));
            stringArray.add((String) jsonObject.get("lname"));
            stringArray.add((String) jsonObject.get("dob"));
            stringArray.add((String) jsonObject.get("nic"));
            stringArray.add((String) jsonObject.get("email"));
        }


        student.setFname(stringArray.get(0));
        student.setLname(stringArray.get(1));
        student.setDob(stringArray.get(2));
        student.setNic(stringArray.get(3));
        student.setEmail(stringArray.get(4));

        return student;
    }

    private SESEmail getEmailSendObject(String student1) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();
        JSONObject jsonObject=new JSONObject(student1);

        SESEmail emailDetails = new SESEmail();
        for (int i = 0; i < jsonObject.length(); i++) {
            stringArray.add((String) jsonObject.get("fname"));
            stringArray.add((String) jsonObject.get("email"));
        }

        emailDetails.setTo(stringArray.get(1));
        emailDetails.setFrom("sliit.sk95@gmail.com");
        emailDetails.setSubject("Hello from ACAS");
        emailDetails.setText("Hi "+ stringArray.get(0));
        emailDetails.setHtmlcontent("<h4> Thanks for verification</h4>");

        return emailDetails;
    }
}
