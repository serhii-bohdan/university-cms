INSERT INTO roles (role_name)
VALUES ('ADMIN'),        --1
       ('MANAGER'),      --2
       ('TEACHER'),      --3
       ('STUDENT');      --4

INSERT INTO permissions (permission_name)
VALUES ('ADMINS_CREATE'),            --1
       ('ADMINS_READ'),              --2
       ('ADMINS_UPDATE'),            --3
       ('ADMINS_DELETE'),            --4

       ('MANAGERS_CREATE'),          --5
       ('MANAGERS_READ'),            --6
       ('MANAGERS_UPDATE'),          --7
       ('MANAGERS_DELETE'),          --8

       ('TEACHERS_CREATE'),          --9
       ('TEACHERS_READ'),            --10
       ('TEACHERS_UPDATE'),          --11
       ('TEACHERS_DELETE'),          --12

       ('STUDENTS_CREATE'),          --13
       ('STUDENTS_READ'),            --14
       ('STUDENTS_UPDATE'),          --15
       ('STUDENTS_DELETE'),          --16

       ('GROUPS_CREATE'),            --17
       ('GROUPS_READ'),              --18
       ('GROUPS_UPDATE'),            --19
       ('GROUPS_DELETE'),            --20

       ('COURSES_CREATE'),           --21
       ('COURSES_READ'),             --22
       ('COURSES_UPDATE'),           --23
       ('COURSES_DELETE'),           --24

       ('TOPICS_CREATE'),            --25
       ('TOPICS_READ'),              --26
       ('TOPICS_UPDATE'),            --27
       ('TOPICS_DELETE'),            --28

       ('MARKS_CREATE'),             --29
       ('MARKS_READ'),               --30
       ('MARKS_UPDATE'),             --31
       ('MARKS_DELETE'),             --32

       ('SCHEDULE_CREATE'),          --33
       ('SCHEDULE_READ'),            --34
       ('SCHEDULE_UPDATE'),          --35
       ('SCHEDULE_DELETE'),          --36

       ('STUDY_DAYS_CREATE'),        --37
       ('STUDY_DAYS_READ'),          --38
       ('STUDY_DAYS_UPDATE'),        --39
       ('STUDY_DAYS_DELETE'),        --40

       ('LESSONS_CREATE'),           --41
       ('LESSONS_READ'),             --42
       ('LESSONS_UPDATE'),           --43
       ('LESSONS_DELETE');           --44

INSERT INTO roles_permissions (role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),

       (2, 6),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12),
       (2, 13),
       (2, 14),
       (2, 15),
       (2, 16),
       (2, 17),
       (2, 18),
       (2, 19),
       (2, 20),

       (3, 10),
       (3, 14),
       (3, 18),
       (3, 21),
       (3, 22),
       (3, 23),
       (3, 24),
       (3, 25),
       (3, 26),
       (3, 27),
       (3, 28),
       (3, 29),
       (3, 30),
       (3, 31),
       (3, 32),
       (3, 33),
       (3, 34),
       (3, 35),
       (3, 36),
       (3, 37),
       (3, 38),
       (3, 39),
       (3, 40),
       (3, 41),
       (3, 42),
       (3, 43),
       (3, 44),

       (4, 10),
       (4, 14),
       (4, 18),
       (4, 22),
       (4, 26),
       (4, 30),
       (4, 33),
       (4, 34),
       (4, 35),
       (4, 36),
       (4, 37),
       (4, 38),
       (4, 39),
       (4, 40),
       (4, 41),
       (4, 42),
       (4, 43),
       (4, 44);
