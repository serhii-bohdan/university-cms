DO $$

DECLARE
  num_courses INTEGER;
  var_student_id BIGINT;
  var_course_id BIGINT;
BEGIN

  FOR var_student_id IN (SELECT user_id FROM students) LOOP
    num_courses := FLOOR(RANDOM() * 4) + 2;
    
    FOR i IN 1..num_courses LOOP

      LOOP
        var_course_id := (SELECT course_id FROM courses ORDER BY RANDOM() LIMIT 1);
        EXIT WHEN NOT EXISTS (SELECT 1 FROM students_courses 
                              WHERE student_id = var_student_id 
                              AND course_id = var_course_id);
      END LOOP;

      INSERT INTO students_courses (student_id, course_id)
      VALUES (var_student_id, var_course_id);
    END LOOP;

  END LOOP;

END $$;


DO $$

DECLARE
  var_student_id BIGINT;
  var_topic_id BIGINT;
  var_mark_value INTEGER;
BEGIN

  FOR var_student_id IN (SELECT user_id FROM students) LOOP

    FOR var_topic_id IN (SELECT topic_id FROM students
                         JOIN students_courses ON students_courses.student_id = students.user_id
                         JOIN courses ON students_courses.course_id = courses.course_id
                         JOIN topics ON courses.course_id = topics.course_id
                         WHERE students.user_id = var_student_id) LOOP

      var_mark_value := (RANDOM() * 100)::INTEGER;
      INSERT INTO marks (mark_value, topic_id, student_id)
      VALUES (var_mark_value, var_topic_id, var_student_id);
      END LOOP;

  END LOOP;

END $$;
