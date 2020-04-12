import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { StudentRegisterComponent } from './student-register/student-register.component';
import {PageHeaderModule} from '../shared/modules';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {StudentRegisterService} from './student-register/student-register-service';
import { MarkAttendanceComponent } from './mark-attendance/mark-attendance.component';
import {MarkAttendanceService} from "./mark-attendance/mark-attendance.service";
import {WebcamModule} from "ngx-webcam";
import { StudentAttendaceTableComponent } from './student-attendace-table/student-attendace-table.component';
import { StudentListComponent } from './student-list/student-list.component';





@NgModule({
    imports: [
        CommonModule,
        LayoutRoutingModule,
        PageHeaderModule,
        TranslateModule,
        NgbDropdownModule,
        FormsModule,
        ReactiveFormsModule,
        WebcamModule
    ],
    declarations: [LayoutComponent, SidebarComponent, HeaderComponent, StudentRegisterComponent, MarkAttendanceComponent, StudentAttendaceTableComponent, StudentListComponent],
    entryComponents: [StudentRegisterComponent],
    providers: [
        StudentRegisterService,
        MarkAttendanceService
    ]
})
export class LayoutModule {}
