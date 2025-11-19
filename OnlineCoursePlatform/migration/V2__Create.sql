CREATE TABLE course
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(200)  NOT NULL,
    `description` VARCHAR(2000) NOT NULL,
    instructor_id BIGINT NULL,
    version       BIGINT NULL,
    CONSTRAINT pk_course PRIMARY KEY (id)
);

CREATE TABLE enrollment
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    student_id BIGINT NOT NULL,
    course_id  BIGINT NOT NULL,
    grade DOUBLE NULL,
    version    BIGINT NULL,
    CONSTRAINT pk_enrollment PRIMARY KEY (id)
);

CREATE TABLE instructor
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NULL,
    email      VARCHAR(255) NULL,
    department VARCHAR(255) NOT NULL,
    CONSTRAINT pk_instructor PRIMARY KEY (id)
);

CREATE TABLE student
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NULL,
    email      VARCHAR(255) NULL,
    CONSTRAINT pk_student PRIMARY KEY (id)
);