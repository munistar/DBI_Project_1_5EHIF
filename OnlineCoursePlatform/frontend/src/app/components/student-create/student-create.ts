import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Student } from '../../models/student';

@Component({
  selector: 'app-student-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './student-create.html',
  styleUrl: './student-create.css'
})
export class StudentCreate {
  private apiService = inject(ApiService);
  private router = inject(Router);

  student: Student = {
    firstName: '',
    lastName: '',
    email: '',
    dateOfBirth: '',
    registrationDate: new Date().toISOString().split('T')[0]
  };

  onSubmit() {
    this.apiService.createStudent(this.student).subscribe({
      next: () => {
        this.router.navigate(['/students']);
      },
      error: (err) => {
        console.error('Error creating student', err);
        alert('Failed to create student');
      }
    });
  }
}
