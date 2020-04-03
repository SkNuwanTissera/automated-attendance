package com.cs.aws.automated_attendance.controllers;

import com.cs.aws.automated_attendance.exceptions.ResourceNotFoundException;
import com.cs.aws.automated_attendance.repository.StudentRepository;
import com.cs.aws.automated_attendance.entity.Student;
import com.cs.aws.automated_attendance.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/api/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Add new Student
     * @param fname
     * @param lname
     * @param email
     * @param notes
     * @param dob
     * @return
     */
    @PostMapping(path="/add")
    public @ResponseBody ResponseEntity<Student> addNewStudent (@RequestParam String fname,
                                           @RequestParam String lname,
                                           @RequestParam String email,
                                           @RequestParam String notes,
                                           @RequestParam String dob
                                               ) {
        Student n = new Student();
//        n.setId(Long.parseLong(id));
        n.setFname(fname);
        n.setLname(lname);
        n.setEmail(email);
        n.setNotes(notes);
        n.setDob(dob);
        studentRepository.save(n);
        return new ResponseEntity<Student>(n, HttpStatus.OK);
    }

    /**
     * Get all students
     * @return
     */
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Delete student by ID
     * @param id
     * @return
     */
    @DeleteMapping(path="/delete/{id}")
    public @ResponseBody ResponseEntity<?> deleteStudent(@PathVariable Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        studentRepository.delete(student);
        return ResponseEntity.ok().build();
    }

    /**
     * Update a student by ID
     * @param id
     * @param student
     * @return
     */
    @PutMapping(path="/update/{id}")
    public @ResponseBody ResponseEntity<Student> updateStudent(@PathVariable Long id,@Valid @RequestBody Student student){

        if (!studentRepository.findById(id).isPresent()) {
            System.out.println("Cannot find the student");
            ResponseEntity.badRequest().build();
        }

        Student s = studentRepository.findById(id).get();
        s.setFname(student.getFname());
        s.setLname(student.getLname());
        s.setDob(student.getDob());
        s.setNotes(student.getNotes());
        s.setEmail(student.getEmail());

        return ResponseEntity.ok(studentRepository.save(s));
    }

    @PostMapping("/saveStudent")
    public void saveDetails(@RequestParam("data") String model,@RequestPart(value = "file") MultipartFile file) throws Exception {
        //  ,@RequestParam("file") List<MultipartFile> multipartFile
        studentService.saveStudent(model,file);
    }


}
