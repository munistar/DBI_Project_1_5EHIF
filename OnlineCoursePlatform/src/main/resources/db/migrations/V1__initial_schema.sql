create table courses
(
    id            bigint auto_increment
        primary key,
    description   varchar(2000) not null,
    instructor_id bigint        null,
    name          varchar(200)  not null,
    version       bigint        null
);

create table enrollments
(
    id         bigint auto_increment
        primary key,
    course_id  bigint not null,
    grade      double null,
    student_id bigint not null,
    version    bigint null
);

create table instructor
(
    id         bigint auto_increment
        primary key,
    department varchar(255) not null,
    email      varchar(255) null,
    first_name varchar(255) not null,
    last_name  varchar(255) null
);

create table student
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) null,
    first_name varchar(255) not null,
    last_name  varchar(255) null
);

