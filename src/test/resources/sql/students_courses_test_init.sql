INSERT INTO roles (role_name)
VALUES ('TEACHER'),
       ('STUDENT');

INSERT INTO groups (group_name, created_at, updated_at)
VALUES ('MJ-90', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO schedules
VALUES (DEFAULT),
       (DEFAULT);

INSERT INTO students (first_name, last_name, email, password_hash, location_zone_offset, role_id, is_active, group_id, schedule_id, created_at, updated_at)
VALUES ('Russell', 'Carter', 'russell.carter@email.com', 'hashed_password', '+00:00', 2, TRUE, (SELECT MAX(id) FROM groups), 1,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO teachers (first_name, last_name, email, password_hash, location_zone_offset, role_id, is_active, schedule_id, created_at, updated_at)
VALUES ('Eugene', 'Rivera', 'eugene.rivera@email.com', 'hashed_password', '+00:00', 1, TRUE, 2, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO courses (course_name, course_description, teacher_id, created_at, updated_at)
VALUES ('FirstCourseName', 'FirstCourseDescription', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('SecondCourseName', 'SecondCourseDescription', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ThirdCourseName', 'ThirdCourseDescription', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO students_courses (student_id, course_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);
