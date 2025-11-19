import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Course } from '../../models/course';

@Component({
  selector: 'app-course-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './course-create.html',
  styleUrl: './course-create.css'
})
export class CourseCreate {
  private apiService = inject(ApiService);
  private router = inject(Router);

  course: Course = {
    name: '',
    description: ''
  };

  onSubmit() {
    this.apiService.createCourse(this.course).subscribe({
      next: () => {
        this.router.navigate(['/courses']);
      },
      error: (err) => {
        console.error('Error creating course', err);
        alert('Failed to create course');
      }
    });
  }
}
