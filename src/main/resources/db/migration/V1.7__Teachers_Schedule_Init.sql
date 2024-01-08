DO $$

DECLARE
  var_teacher_id BIGINT;
  var_course_id BIGINT;
  var_schedule_id BIGINT := 1;
  var_study_day_id BIGINT := 1;
  var_day_date DATE := '2023-10-01';
  var_week_day VARCHAR;
  var_lesson_start_time TIME;
  var_lesson_end_time TIME;
  var_last_lesson_end_time TIME := '09:00:00';
BEGIN

  FOR var_teacher_id IN (SELECT user_id FROM teachers) LOOP
    INSERT INTO schedules DEFAULT VALUES;

    FOR i IN 1..31 LOOP
      var_week_day := TRIM(UPPER(TO_CHAR(var_day_date, 'Day')));
      INSERT INTO study_days (day_date, week_day, schedule_id)
      VALUES (var_day_date, var_week_day, var_schedule_id);

      IF var_week_day NOT IN('SATURDAY', 'SUNDAY') THEN
        FOR var_course_id IN (SELECT course_id FROM courses WHERE teacher_id = var_teacher_id) LOOP
          var_lesson_start_time = var_last_lesson_end_time + (RANDOM() * (5*60*60))::int * INTERVAL '1 second';
          var_lesson_end_time = var_lesson_start_time + (RANDOM() * (3*60*60))::int * INTERVAL '1 second';
        
          IF var_lesson_end_time > var_lesson_start_time AND RANDOM() < 0.8 THEN
            INSERT INTO lessons (lesson_start_time, lesson_end_time, course_id, study_day_id)
            VALUES ((var_day_date::TEXT || ' ' || var_lesson_start_time::TEXT)::TIMESTAMPTZ,
                   (var_day_date::TEXT || ' ' || var_lesson_end_time::TEXT)::TIMESTAMPTZ,
                    var_course_id, var_study_day_id);
          END IF;

          var_last_lesson_end_time := var_lesson_end_time;
        END LOOP;

      END IF;

      var_study_day_id = var_study_day_id + 1;
      var_day_date = var_day_date + 1;
      var_last_lesson_end_time := '07:00:00' + (RANDOM() * (5*60*60))::int * INTERVAL '1 second';
    END LOOP;

    UPDATE teachers SET schedule_id = var_schedule_id
    WHERE user_id = var_teacher_id;
    var_day_date := '2023-10-01';
    var_schedule_id = var_schedule_id + 1;
  END LOOP;

END $$;
