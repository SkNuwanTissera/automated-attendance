import { Component, OnInit } from '@angular/core';
import {StudentDto} from "../../shared/Models/StudentDto";
import {MarkAttendanceService} from "../mark-attendance/mark-attendance.service";

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.scss']
})
export class StudentListComponent implements OnInit {

    studentDetails: StudentDto[] = [];

    constructor(private markAttendanceService: MarkAttendanceService) {
    }

  ngOnInit() {
        this.getAllStudentList();
  }


  getAllStudentList(){
      this.markAttendanceService.getAllStudents().subscribe(
          (data: StudentDto[]) => {
              this.studentDetails = data;

          });

  }

}
