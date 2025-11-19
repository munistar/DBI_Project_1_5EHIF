import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Course } from '../../models/course';

@Component({
  selector: 'app-course-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './course-detail.html',
  styleUrl: './course-detail.css'
})
export class CourseDetail implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private apiService = inject(ApiService);

  course: Course | null = null;
  loading = true;
  isEditing = false;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadCourse(id);
    }
  }

  loadCourse(id: string) {
    this.loading = true;
    this.apiService.getCourseById(id).subscribe({
      next: (data) => {
        this.course = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading course', err);
        this.loading = false;
      }
    });
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  save() {
    if (this.course && this.course.id) {
      this.apiService.updateCourse(this.course.id, this.course).subscribe({
        next: (updated) => {
          this.course = updated;
          this.isEditing = false;
        },
        error: (err) => {
          console.error('Error updating course', err);
          alert('Failed to update course');
        }
      });
    }
  }

  delete() {
    if (this.course && this.course.id && confirm('Are you sure you want to delete this course?')) {
      this.apiService.deleteCourse(this.course.id).subscribe({
        next: () => {
          this.router.navigate(['/courses']);
        },
        error: (err) => {
          console.error('Error deleting course', err);
          alert('Failed to delete course');
        }
      });
    }
  }
}
