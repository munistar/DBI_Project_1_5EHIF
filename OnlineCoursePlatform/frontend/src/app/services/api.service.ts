import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course } from '../models/course';
import { Student } from '../models/student';
import { PerformanceResult } from '../models/performance-result';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    private baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) { }

    // Courses
    getAllCourses(): Observable<Course[]> {
        return this.http.get<Course[]>(`${this.baseUrl}/courses`);
    }

    getCourseById(id: string): Observable<Course> {
        return this.http.get<Course>(`${this.baseUrl}/courses/${id}`);
    }

    createCourse(course: Course): Observable<Course> {
        return this.http.post<Course>(`${this.baseUrl}/courses`, course);
    }

    updateCourse(id: string, course: Course): Observable<Course> {
        return this.http.put<Course>(`${this.baseUrl}/courses/${id}`, course);
    }

    deleteCourse(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/courses/${id}`);
    }

    // Students
    getAllStudents(): Observable<Student[]> {
        return this.http.get<Student[]>(`${this.baseUrl}/students`);
    }

    createStudent(student: Student): Observable<Student> {
        return this.http.post<Student>(`${this.baseUrl}/students`, student);
    }

    deleteStudent(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/students/${id}`);
    }

    // Performance
    runPerformanceTests(): Observable<PerformanceResult[]> {
        return this.http.post<PerformanceResult[]>(`${this.baseUrl}/performance/run`, {});
    }
}
