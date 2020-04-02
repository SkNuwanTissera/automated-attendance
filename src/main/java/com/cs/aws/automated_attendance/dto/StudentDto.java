package com.cs.aws.automated_attendance.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.sql.DataSourceDefinition;

public class StudentDto {
    private Long id;
    private String fname;
    private String lname;
    private String dob;
    private String notes;
    private String email;
    private String nic;
    private MultipartFile imagefile;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public MultipartFile getImagefile() {
        return imagefile;
    }

    public void setImagefile(MultipartFile imagefile) {
        this.imagefile = imagefile;
    }
}
