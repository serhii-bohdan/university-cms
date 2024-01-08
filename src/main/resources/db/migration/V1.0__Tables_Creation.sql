CREATE TABLE administrators (
  user_id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password VARCHAR CHECK (CHAR_LENGTH(password) >= 10) NOT NULL,
  is_active BOOLEAN NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE groups (
  group_id BIGSERIAL PRIMARY KEY,
  group_name VARCHAR(5) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE schedules (
  schedule_id BIGSERIAL PRIMARY KEY
);

CREATE TABLE teachers (
  user_id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password VARCHAR CHECK (CHAR_LENGTH(password) >= 10) NOT NULL,
  is_active BOOLEAN NOT NULL,
  schedule_id BIGINT REFERENCES schedules(schedule_id),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE students (
  user_id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password VARCHAR CHECK (CHAR_LENGTH(password) >= 10) NOT NULL,
  is_active BOOLEAN NOT NULL,
  group_id BIGINT REFERENCES groups(group_id) ON DELETE CASCADE NOT NULL,
  schedule_id BIGINT REFERENCES schedules(schedule_id),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE courses (
  course_id BIGSERIAL PRIMARY KEY,
  course_name VARCHAR NOT NULL,
  course_description TEXT NOT NULL,
  teacher_id BIGINT REFERENCES teachers(user_id) ON DELETE CASCADE NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE students_courses (
  student_id BIGINT REFERENCES students(user_id) ON DELETE CASCADE,
  course_id BIGINT REFERENCES courses(course_id) ON DELETE CASCADE,
  PRIMARY KEY (student_id, course_id)
);

CREATE TABLE topics (
  topic_id BIGSERIAL PRIMARY KEY,
  topic_name VARCHAR NOT NULL,
  topic_description TEXT NOT NULL,
  course_id BIGINT REFERENCES courses(course_id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE marks (
  mark_id BIGSERIAL PRIMARY KEY,
  mark_value INTEGER NOT NULL,
  student_id BIGINT REFERENCES students(user_id) ON DELETE CASCADE NOT NULL,
  topic_id BIGINT REFERENCES topics(topic_id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE study_days (
  study_day_id BIGSERIAL PRIMARY KEY,
  day_date DATE NOT NULL,
  week_day VARCHAR NOT NULL,
  schedule_id BIGINT REFERENCES schedules(schedule_id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE lessons (
  lesson_id BIGSERIAL PRIMARY KEY,
  lesson_start_time TIMESTAMPTZ NOT NULL,
  lesson_end_time TIMESTAMPTZ NOT NULL,
  course_id BIGINT REFERENCES courses(course_id) ON DELETE CASCADE NOT NULL,
  study_day_id BIGINT REFERENCES study_days(study_day_id) ON DELETE CASCADE NOT NULL
);
