import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Student } from '../../models/student';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './student-list.html',
  styleUrl: './student-list.css'
})
export class StudentList implements OnInit {
  private apiService = inject(ApiService);
  students: Student[] = [];
  loading = true;

  ngOnInit() {
    this.loadStudents();
  }

  loadStudents() {
    this.loading = true;
    this.apiService.getAllStudents().subscribe({
      next: (data) => {
        this.students = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching students', err);
        this.loading = false;
      }
    });
  }

  deleteStudent(id: string) {
    if (confirm('Are you sure you want to delete this student?')) {
      this.apiService.deleteStudent(id).subscribe(() => {
        this.students = this.students.filter(s => s.id !== id);
      });
    }
  }
}
