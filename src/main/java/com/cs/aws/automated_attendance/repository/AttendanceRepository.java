package com.cs.aws.automated_attendance.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AttendanceRepository extends CrudRepository<Student, Long> {

}