package com.cs.aws.automated_attendance.controllers;

import com.cs.aws.automated_attendance.repository.AttendanceRepository;
import com.cs.aws.automated_attendance.repository.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path="/student")
public class StudentController {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewStudent (@RequestParam String id,
                                               @RequestParam String fname,
                                               @RequestParam String lname,
                                               @RequestParam String email,
                                               @RequestParam String notes,
                                               @RequestParam String dob
                                               ) {
        Student n = new Student();
        n.setId(Long.parseLong(id));
        n.setFname(fname);
        n.setLname(lname);
        n.setEmail(email);
        n.setNotes(notes);
        n.setDob(dob);
        attendanceRepository.save(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Student> getAllStudents() {
        return attendanceRepository.findAll();
    }

    @PutMapping(path="/update/{id}")
    public @ResponseBody ResponseEntity<Student> updateStudent(@PathVariable Long id,@Valid @RequestBody Student student){

        if (!attendanceRepository.findById(id).isPresent()) {
            System.out.println("Cannot find the student");
            ResponseEntity.badRequest().build();
        }

        Student s = attendanceRepository.findById(id).get();
        s.setFname(student.getFname());
        s.setLname(student.getLname());
        s.setDob(student.getDob());
        s.setNotes(student.getNotes());
        s.setEmail(student.getEmail());

        return ResponseEntity.ok(attendanceRepository.save(s));
    }

}
