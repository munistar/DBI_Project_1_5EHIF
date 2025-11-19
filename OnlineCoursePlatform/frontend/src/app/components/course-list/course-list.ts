import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Course } from '../../models/course';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './course-list.html',
  styleUrl: './course-list.css'
})
export class CourseList implements OnInit {
  private apiService = inject(ApiService);
  courses: Course[] = [];
  loading = true;

  ngOnInit() {
    this.loadCourses();
  }

  loadCourses() {
    this.loading = true;
    this.apiService.getAllCourses().subscribe({
      next: (data) => {
        this.courses = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching courses', err);
        this.loading = false;
      }
    });
  }

  deleteCourse(id: string) {
    if (confirm('Are you sure you want to delete this course?')) {
      this.apiService.deleteCourse(id).subscribe(() => {
        this.courses = this.courses.filter(c => c.id !== id);
      });
    }
  }
}
