import { Component, OnInit } from '@angular/core';
import {StudentDto} from "../../shared/Models/StudentDto";
import {MarkAttendanceService} from "../mark-attendance/mark-attendance.service";

@Component({
  selector: 'app-student-attendace-table',
  templateUrl: './student-attendace-table.component.html',
  styleUrls: ['./student-attendace-table.component.scss']
})
export class StudentAttendaceTableComponent implements OnInit {
    studentDetails: StudentDto[] = [];

    constructor(private markAttendanceService: MarkAttendanceService) {
    }

    ngOnInit() {

        this.getAttendanceList();
    }


    getAttendanceList() {
        this.markAttendanceService.getAttendanceList().subscribe(
            (data: StudentDto[]) => {
                this.studentDetails = data;

            });

    }
}
