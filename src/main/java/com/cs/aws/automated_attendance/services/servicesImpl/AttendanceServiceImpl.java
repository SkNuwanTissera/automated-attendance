package com.cs.aws.automated_attendance.services.servicesImpl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.cs.aws.automated_attendance.FaceComparer;
import com.cs.aws.automated_attendance.S3uploader;
import com.cs.aws.automated_attendance.dto.FaceFileDto;
import com.cs.aws.automated_attendance.dto.StudentDto;
import com.cs.aws.automated_attendance.entity.Student;
import com.cs.aws.automated_attendance.repository.AttendanceRepository;
import com.cs.aws.automated_attendance.repository.StudentRepository;
import com.cs.aws.automated_attendance.services.AttendanceService;
import com.cs.aws.automated_attendance.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;


    @Override
    public StudentDto markAttendace(String faceFileDto) throws Exception {

        String[] base64StringData = faceFileDto.split("base64,");
        byte[] decodedBytes = Base64.getDecoder().decode(base64StringData[base64StringData.length-1]);

        FaceComparer faceComparer = new FaceComparer();

        ByteBuffer sourceImageBytes = null;
        sourceImageBytes = ByteBuffer.wrap(decodedBytes);
        Image source = new Image().withBytes(sourceImageBytes);

        String studentId = faceComparer.compare(source);
        System.out.printf(studentId);

        Optional<Student> student= studentRepository.findById(Long.parseLong(studentId));
        StudentDto studentDto= new StudentDto();
        if(student!=null){
            studentDto.setId(student.get().getId());
            studentDto.setFname(student.get().getFname());
            studentDto.setLname(student.get().getFname());
            studentDto.setNic(student.get().getEmail());
        }


        return studentDto;
    }

}
