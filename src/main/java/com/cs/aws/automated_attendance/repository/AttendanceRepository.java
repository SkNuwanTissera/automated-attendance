package com.cs.aws.automated_attendance.repository;
import com.cs.aws.automated_attendance.entity.Student;
import org.springframework.data.repository.CrudRepository;

public interface AttendanceRepository extends CrudRepository<Student, Long> {

}