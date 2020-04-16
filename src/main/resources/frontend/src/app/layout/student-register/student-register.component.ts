import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {StudentRegisterService} from './student-register-service';
import {StudentDto} from '../../shared/Models/StudentDto';
import Swal from 'sweetalert2'
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-student-register',
  templateUrl: './student-register.component.html',
  styleUrls: ['./student-register.component.scss']
})
export class StudentRegisterComponent implements OnInit {
    public studentRegister: FormGroup;
    public studentmgtDto = new StudentDto();
    InputFile: File;

  constructor(private studentRegisterService: StudentRegisterService) { }

  ngOnInit() {
      this.studentRegister = new FormGroup({
          fname: new FormControl(null, Validators.required),
          lname: new FormControl(null, Validators.required),
          nic: new FormControl(null, Validators.required),
          dob: new FormControl(null, Validators.required),
          email: new FormControl(null, Validators.required),
          InputFile: new FormControl(null, Validators.required)
      });
  }


  public onsubmit() {
      // alert('adasdsa');
      this.studentmgtDto.fname = this.studentRegister.value.fname;
      this.studentmgtDto.lname = this.studentRegister.value.lname;
      this.studentmgtDto.nic = this.studentRegister.value.nic;
      this.studentmgtDto.dob = this.studentRegister.value.dob;
      this.studentmgtDto.email = this.studentRegister.value.email;
      console.log(this.studentmgtDto);
      console.log(this.studentmgtDto);

      const formData = new FormData();
      formData.append('data', JSON.stringify(this.studentmgtDto));
      formData.append('file', this.studentRegister.get('InputFile').value);
      this.studentRegisterService.saveStudent(formData).subscribe((data) => {
         // alert("save Student successfully");
          Swal.fire(
              'Successfully Registered!',
              'Now you can mark you attendance!',
              'success'
          )
        console.log('success');
    });
  }

    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.studentRegister.get('InputFile').setValue(file);
        }
    }

}
