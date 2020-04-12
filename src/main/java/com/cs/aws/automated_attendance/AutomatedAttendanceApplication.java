package com.cs.aws.automated_attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableCaching
@EnableScheduling
@PropertySource("classpath:application.properties")
//@ComponentScan("com.cs.aws.automated_attendance.services")
public class AutomatedAttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomatedAttendanceApplication.class, args);
    }

}
