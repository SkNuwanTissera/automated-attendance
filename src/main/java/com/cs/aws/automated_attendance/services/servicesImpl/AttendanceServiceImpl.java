package com.cs.aws.automated_attendance.services.servicesImpl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.cs.aws.automated_attendance.FaceComparer;
import com.cs.aws.automated_attendance.S3uploader;
import com.cs.aws.automated_attendance.dto.StudentDto;
import com.cs.aws.automated_attendance.entity.Attendance;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private StudentRepository studentRepository;

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


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
        Attendance studentO= new Attendance();

        if(student!=null){
            studentO.setId(student.get().getId());
            studentO.setName(student.get().getFname() + " " + student.get().getLname());
            studentO.setLecture("ASAS Lecture 1");
            //Format LocalDateTime
            String formattedDateTime = LocalDateTime.now().format(formatter);
            studentO.setTime(formattedDateTime);
            attendanceRepository.save(studentO);
        }

        StudentDto studentDTO= new StudentDto();
        if(student!=null){
            studentDTO.setId(student.get().getId());
            studentDTO.setFname(student.get().getFname());
            studentDTO.setLname(student.get().getFname());
            studentDTO.setNic(student.get().getEmail());
        }

        return studentDTO;
    }

}
