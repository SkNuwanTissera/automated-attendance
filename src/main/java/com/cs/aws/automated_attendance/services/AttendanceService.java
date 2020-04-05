package com.cs.aws.automated_attendance.services;

import com.cs.aws.automated_attendance.dto.FaceFileDto;
import com.cs.aws.automated_attendance.dto.StudentDto;
import org.springframework.web.multipart.MultipartFile;

public interface AttendanceService {
    public StudentDto markAttendace(String faceFileDto)throws Exception;
}
