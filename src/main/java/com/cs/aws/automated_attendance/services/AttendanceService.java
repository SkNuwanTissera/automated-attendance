package com.cs.aws.automated_attendance.services;

import org.springframework.web.multipart.MultipartFile;

public interface AttendanceService {
    public void markAttendace(String attendance, MultipartFile file)throws Exception;
}
