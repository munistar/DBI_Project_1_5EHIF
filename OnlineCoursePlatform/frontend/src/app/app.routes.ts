import { Routes } from '@angular/router';
import { CourseList } from './components/course-list/course-list';
import { CourseDetail } from './components/course-detail/course-detail';
import { CourseCreate } from './components/course-create/course-create';
import { StudentList } from './components/student-list/student-list';
import { StudentCreate } from './components/student-create/student-create';
import { PerformanceTest } from './components/performance-test/performance-test';

export const routes: Routes = [
    { path: '', redirectTo: '/courses', pathMatch: 'full' },
    { path: 'courses', component: CourseList },
    { path: 'courses/create', component: CourseCreate },
    { path: 'courses/:id', component: CourseDetail },
    { path: 'students', component: StudentList },
    { path: 'students/create', component: StudentCreate },
    { path: 'performance', component: PerformanceTest }
];
