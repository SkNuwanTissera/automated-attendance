import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentAttendaceTableComponent } from './student-attendace-table.component';

describe('StudentAttendaceTableComponent', () => {
  let component: StudentAttendaceTableComponent;
  let fixture: ComponentFixture<StudentAttendaceTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentAttendaceTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentAttendaceTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
