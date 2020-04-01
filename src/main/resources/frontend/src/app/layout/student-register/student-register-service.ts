import {Injectable} from '@angular/core';
import {StudentDto} from '../../shared/Models/StudentDto';
import {SystemValueServices} from '../../shared/common/system-value.services';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class StudentRegisterService {

    public BASE_URL = SystemValueServices.BASE_URL;
    private headersJson = new HttpHeaders({
        'Content-Type': 'application/json; charset=utf-8'
    });
    constructor(private httpClient: HttpClient) {
    }

    saveStudent(formData: FormData) {
        alert(formData);
        return this.httpClient.post(
             'http://localhost:9090/api/student/saveStudent/',
            formData
        );
    }
}
