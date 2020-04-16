import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs/internal/Subject";
import {WebcamImage} from "ngx-webcam";
import {Observable} from "rxjs/internal/Observable";
import {StudentRegisterService} from "../student-register/student-register-service";
import {MarkAttendanceService} from "./mark-attendance.service";
import {StudentDto} from "../../shared/Models/StudentDto";

@Component({
  selector: 'app-mark-attendance',
  templateUrl: './mark-attendance.component.html',
  styleUrls: ['./mark-attendance.component.scss']
})
export class MarkAttendanceComponent implements OnInit {
    // webcam snapshot trigger
    // latest snapshot
    public webcamImage: WebcamImage = null;
    private trigger: Subject<void> = new Subject<void>();
    facefileDto : FaceFileDto;
    studentDetails: StudentDto;
    studentName : string;
    studentNic: string;

  constructor(private markAttendanceService: MarkAttendanceService) { }

  ngOnInit() {
  }

    dataURLtoFile(dataurl) {
       // const arr = dataurl.split(',');
        const arr = dataurl.toString().split(',');
        // const mime = arr[0].match(/:(.*?);/)[1];
        // const bstr = atob(arr[1]);
        // let n = bstr.length;
        // const u8arr = new Uint8Array(n);
        // while (n--) {
        //     u8arr[n] = bstr.charCodeAt(n);
        // }
        // fileSaverSave(new File([u8arr], filename, {type: format}));
    }
    // getImage(imageUrl: string): Observable<File> {
    //     return this.http
    //         .get(imageUrl, { responseType: ResponseContentType.Blob })
    //         .map((res: Response) => res.blob());
    // }

    public triggerSnapshot(): void {
        this.trigger.next();
        this.dataURLtoFile(this.webcamImage.imageAsDataUrl);
        const facefiledto = new FaceFileDto();
        let reader: FileReader = new FileReader();

        facefiledto.imagedataURl=this.webcamImage.imageAsDataUrl;

        this.markAttendanceService.passImage(facefiledto).subscribe(
            (data) => {

                this.studentDetails = data;
                this.studentName=this.studentDetails.fname;
                this.studentNic=this.studentDetails.nic;
        });

    }

    public get triggerObservable(): Observable<void> {
        return this.trigger.asObservable();
    }

    public handleImage(webcamImage: WebcamImage): void {
        console.info('received webcam image', webcamImage);
        this.webcamImage = webcamImage;
    }

}

export class FaceFileDto {

    imagedataURl: string;

}

