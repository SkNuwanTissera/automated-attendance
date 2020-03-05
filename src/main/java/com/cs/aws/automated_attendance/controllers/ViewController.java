package com.cs.aws.automated_attendance.controllers;

import com.cs.aws.automated_attendance.FaceComparer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ViewController {
    /**
     * Frontend routes
     * @return
     */
    @GetMapping("/")
    public String index(){
        return "<h1>Automated Attendance System</h1>";
    }

}
