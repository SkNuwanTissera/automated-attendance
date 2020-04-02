package com.cs.aws.automated_attendance.services;

import com.cs.aws.automated_attendance.entity.Student;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    public void saveStudent(String student,MultipartFile file)throws Exception;
}
