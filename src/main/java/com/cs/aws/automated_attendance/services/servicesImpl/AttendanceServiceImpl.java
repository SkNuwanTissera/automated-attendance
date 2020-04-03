package com.cs.aws.automated_attendance.services.servicesImpl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.cs.aws.automated_attendance.FaceComparer;
import com.cs.aws.automated_attendance.S3uploader;
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
import java.util.ArrayList;

@Service
@Transactional
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public void markAttendace(String attendance, MultipartFile file) throws Exception {

    }
}
