INSERT INTO roles (role_name)
VALUES ('TEACHER'),
       ('STUDENT');

INSERT INTO groups (group_name, created_at, updated_at)
VALUES ('MJ-90', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO schedules VALUES (DEFAULT);

INSERT INTO students (first_name, last_name, email, password_hash, location_zone_offset, role_id, is_active, group_id, schedule_id, created_at, updated_at)
VALUES ('Russell', 'Carter', 'russell.carter@email.com', 'hashed_password', '+00:00', 2, TRUE, (SELECT MAX(id) FROM groups), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO teachers (first_name, last_name, email, password_hash, location_zone_offset, role_id, is_active, schedule_id, created_at, updated_at)
VALUES ('Eugene', 'Rivera', 'eugene.rivera@email.com', 'hashed_password', '+00:00', 1, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO courses (course_name, course_description, teacher_id, created_at, updated_at)
VALUES ('Course1', 'This is a course description.', (SELECT MAX(id) FROM teachers), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO topics (topic_name, topic_description, topic_order, course_id)
VALUES ('Topic1', 'This is a topic description.', 1, (SELECT MAX(id) FROM courses)),
       ('Topic2', 'This is a topic description.', 2, (SELECT MAX(id) FROM courses)),
       ('Topic3', 'This is a topic description.', 3, (SELECT MAX(id) FROM courses)),
       ('Topic4', 'This is a topic description.', 4, (SELECT MAX(id) FROM courses)),
       ('Topic5', 'This is a topic description.', 5, (SELECT MAX(id) FROM courses)),
       ('Topic6', 'This is a topic description.', 6, (SELECT MAX(id) FROM courses));

INSERT INTO marks (mark_value, comment, student_id, topic_id)
VALUES (5, 'Great job!', (SELECT MAX(id) FROM students),
        (SELECT id FROM topics WHERE topic_order = 1 AND course_id = (SELECT MAX(id) FROM courses))),
       (4, 'Good job!', (SELECT MAX(id) FROM students),
        (SELECT id FROM topics WHERE topic_order = 2 AND course_id = (SELECT MAX(id) FROM courses))),
       (3, 'Average job.', (SELECT MAX(id) FROM students),
        (SELECT id FROM topics WHERE topic_order = 3 AND course_id = (SELECT MAX(id) FROM courses))),
       (2, 'Below average job.', (SELECT MAX(id) FROM students),
        (SELECT id FROM topics WHERE topic_order = 4 AND course_id = (SELECT MAX(id) FROM courses))),
       (1, 'Poor job.', (SELECT MAX(id) FROM students),
        (SELECT id FROM topics WHERE topic_order = 5 AND course_id = (SELECT MAX(id) FROM courses))),
       (0, 'No job.', (SELECT MAX(id) FROM students),
        (SELECT id FROM topics WHERE topic_order = 6 AND course_id = (SELECT MAX(id) FROM courses)));
