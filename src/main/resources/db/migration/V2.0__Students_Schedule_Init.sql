DO $$

DECLARE
  var_student_id BIGINT;
  var_teacher_id BIGINT;
  var_course_id BIGINT;
  var_schedule_id BIGINT := ((SELECT MAX(id) FROM schedules) + 1)::BIGINT;
  var_day_date DATE := CURRENT_DATE;
  var_week_day VARCHAR;
  var_lesson_start_time TIME;
  var_lesson_end_time TIME;
  var_zone_offset VARCHAR;
BEGIN

  FOR var_student_id IN (SELECT id FROM students) LOOP
    INSERT INTO schedules DEFAULT VALUES;

    FOR i IN 1..31 LOOP
      var_week_day := TRIM(UPPER(TO_CHAR(var_day_date, 'Day')));

      IF var_week_day NOT IN('SATURDAY', 'SUNDAY') THEN
        FOR var_teacher_id, var_course_id IN (SELECT teachers.id, courses.id FROM students
                                              JOIN students_courses ON students_courses.student_id = students.id
                                              JOIN courses ON courses.id = students_courses.course_id
                                              JOIN teachers ON teachers.id = courses.teacher_id
                                              WHERE students.id = var_student_id) LOOP

          FOR var_lesson_start_time, var_lesson_end_time, var_zone_offset IN (SELECT lessons.lesson_start_time, lessons.lesson_end_time, lessons.zone_offset
                                                                              FROM teachers JOIN schedules ON schedules.id = teachers.schedule_id
                                                                              JOIN lessons ON lessons.schedule_id = schedules.id
                                                                              JOIN courses ON courses.id = lessons.course_id
                                                                              WHERE teachers.id = var_teacher_id AND lessons.date = var_day_date
                                                                              AND courses.id = var_course_id) LOOP

            IF NOT EXISTS (SELECT 1 FROM lessons WHERE schedule_id = var_schedule_id AND date = var_day_date AND
                          (var_lesson_start_time, var_lesson_end_time) OVERLAPS (lesson_start_time, lesson_end_time)) THEN
              INSERT INTO lessons (date, lesson_start_time, lesson_end_time, zone_offset, course_id, schedule_id)
              VALUES (var_day_date, var_lesson_start_time, var_lesson_end_time, var_zone_offset, var_course_id, var_schedule_id);
            END IF;

          END LOOP;

        END LOOP;

      END IF;

      var_day_date = var_day_date + 1;
    END LOOP;

    UPDATE students SET schedule_id = var_schedule_id
    WHERE id = var_student_id;
    var_day_date := CURRENT_DATE;
    var_schedule_id = var_schedule_id + 1;
  END LOOP;

END $$;
