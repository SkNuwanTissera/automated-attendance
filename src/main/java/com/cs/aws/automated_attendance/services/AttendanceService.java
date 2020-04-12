package com.cs.aws.automated_attendance.services;

import com.cs.aws.automated_attendance.dto.StudentDto;

import java.util.List;


public interface AttendanceService {
    public StudentDto markAttendace(String faceFileDto)throws Exception;
    public List<StudentDto> getAllAttendance();
}
