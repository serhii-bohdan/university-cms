DO $$

DECLARE
  num_courses INTEGER;
  var_student_id BIGINT;
  var_course_id BIGINT;
BEGIN

  FOR var_student_id IN (SELECT id FROM students) LOOP
    num_courses := FLOOR(RANDOM() * 4) + 2;
    
    FOR i IN 1..num_courses LOOP

      LOOP
        var_course_id := (SELECT id FROM courses ORDER BY RANDOM() LIMIT 1);
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

  FOR var_student_id IN (SELECT id FROM students) LOOP

    FOR var_topic_id IN (SELECT topics.id FROM students
                         JOIN students_courses ON students_courses.student_id = students.id
                         JOIN courses ON students_courses.course_id = courses.id
                         JOIN topics ON courses.id = topics.course_id
                         WHERE students.id = var_student_id) LOOP

      var_mark_value := (RANDOM() * 100)::INTEGER;
      INSERT INTO marks (mark_value, topic_id, student_id)
      VALUES (var_mark_value, var_topic_id, var_student_id);
      END LOOP;

  END LOOP;

END $$;
