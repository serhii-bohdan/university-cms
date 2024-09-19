INSERT INTO roles (role_name)
VALUES ('TEACHER'),
       ('STUDENT');

INSERT INTO groups (group_name)
VALUES ('LS-11');

INSERT INTO schedules(id)
VALUES (1),
       (2);

INSERT INTO teachers (first_name, last_name, email, password_hash, role_id, is_active, schedule_id, created_at, updated_at)
VALUES ('FirstName1', 'LastName1', 'firstname1.lastname1@gmail.com', '$2b$12$xKJ/2R73oi5JwSWZk2cbDulyv.pR.wdP3Ixc9FBKPUFxyriBlTtPa', 1, true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO teachers (first_name, last_name, email, password_hash, role_id, is_active, created_at, updated_at)
VALUES ('FirstName2', 'LastName2', 'firstname2.lastname2@gmail.com', '$2b$12$e8IiFW/7rgsTJLkp11RqxezYJBSTK6VCeTgcTi/gfPHD6S4mAZUWW', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO students (first_name, last_name, email, password_hash, role_id, is_active, group_id, schedule_id, created_at, updated_at)
VALUES ('FirstName1', 'LastName1', 'firstname1.lastname1@gmail.com', '$2b$12$xKJ/2R73oi5JwSWZk2cbDulyv.pR.wdP3Ixc9FBKPUFxyriBlTtPa', 2, true, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO students (first_name, last_name, email, password_hash, role_id, is_active, group_id, created_at, updated_at)
VALUES ('FirstName2', 'LastName2', 'firstname2.lastname2@gmail.com', '$2b$12$e8IiFW/7rgsTJLkp11RqxezYJBSTK6VCeTgcTi/gfPHD6S4mAZUWW', 2, true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
