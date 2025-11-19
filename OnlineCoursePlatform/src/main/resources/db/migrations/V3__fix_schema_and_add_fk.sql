-- V3: Fix table names (singular -> plural) and add proper foreign key constraints

-- Rename tables to plural form
ALTER TABLE instructor RENAME TO instructors;
ALTER TABLE student RENAME TO students;

-- Add missing columns to instructors table
ALTER TABLE instructors
    ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20),
    ADD COLUMN IF NOT EXISTS office_location VARCHAR(255),
    ADD COLUMN IF NOT EXISTS hire_date DATE,
    ADD COLUMN IF NOT EXISTS biography TEXT,
    ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0;

-- Add missing columns to students table
ALTER TABLE students
    ADD COLUMN IF NOT EXISTS date_of_birth DATE,
    ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20),
    ADD COLUMN IF NOT EXISTS address VARCHAR(500),
    ADD COLUMN IF NOT EXISTS registration_date DATE DEFAULT CURRENT_DATE,
    ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0;

-- Update column constraints for instructors
ALTER TABLE instructors
    MODIFY COLUMN department VARCHAR(100) NOT NULL,
    MODIFY COLUMN email VARCHAR(100) NOT NULL,
    MODIFY COLUMN first_name VARCHAR(50) NOT NULL,
    MODIFY COLUMN last_name VARCHAR(50) NOT NULL;

-- Update column constraints for students
ALTER TABLE students
    MODIFY COLUMN email VARCHAR(100) NOT NULL,
    MODIFY COLUMN first_name VARCHAR(50) NOT NULL,
    MODIFY COLUMN last_name VARCHAR(50) NOT NULL,
    MODIFY COLUMN date_of_birth DATE NOT NULL;

-- Add foreign key constraint: courses.instructor_id -> instructors.id
ALTER TABLE courses
    ADD CONSTRAINT fk_course_instructor
        FOREIGN KEY (instructor_id)
            REFERENCES instructors(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- Add foreign key constraint: enrollments.student_id -> students.id
ALTER TABLE enrollments
    ADD CONSTRAINT fk_enrollment_student
        FOREIGN KEY (student_id)
            REFERENCES students(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- Add foreign key constraint: enrollments.course_id -> courses.id
ALTER TABLE enrollments
    ADD CONSTRAINT fk_enrollment_course
        FOREIGN KEY (course_id)
            REFERENCES courses(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- Add unique constraint for instructor email
ALTER TABLE instructors
    ADD CONSTRAINT uk_instructor_email UNIQUE (email);

-- Add unique constraint for student email
ALTER TABLE students
    ADD CONSTRAINT uk_student_email UNIQUE (email);
