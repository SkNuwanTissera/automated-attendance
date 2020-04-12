
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {FaceFileDto} from "./mark-attendance.component";

@Injectable()
export class MarkAttendanceService {

    private headersJson = new HttpHeaders({'Content-Type': 'application/json; charset=utf-8'});
    constructor(private httpClient: HttpClient) {
    }

    // passImage(formData: FormData) {
    //     console.log(formData);
    //     alert("methana sapaoi"+formData);
    //     return this.httpClient.post(
    //         'http://localhost:8080/api/face/getFaceImage/',
    //         formData
    //     );
    // }
    passImage(image: FaceFileDto):Observable<any> {
        //console.log(image);
        return this.httpClient.post('http://localhost:8080/face/getFaceImage',image,{headers: this.headersJson});
    }

    getAttendanceList(){
        return this.httpClient.get('http://localhost:8080/face/getAllAttendnaceList');
    }

    getAllStudents(){
        return this.httpClient.get('http://localhost:8080/api/student/getAllstudentList');

    }
}


