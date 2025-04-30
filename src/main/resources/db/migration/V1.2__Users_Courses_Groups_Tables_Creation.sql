CREATE TABLE admins (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password_hash VARCHAR NOT NULL,
  location_zone_offset VARCHAR NOT NULL,
  role_id BIGINT REFERENCES roles(id),
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
);

CREATE TABLE managers (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password_hash VARCHAR NOT NULL,
  location_zone_offset VARCHAR NOT NULL,
  role_id BIGINT REFERENCES roles(id),
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
);

CREATE TABLE groups (
  id BIGSERIAL PRIMARY KEY,
  group_name VARCHAR(5) NOT NULL,
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
);

CREATE TABLE schedules (
  id BIGSERIAL PRIMARY KEY
);

CREATE TABLE teachers (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password_hash VARCHAR NOT NULL,
  location_zone_offset VARCHAR NOT NULL,
  role_id BIGINT REFERENCES roles(id),
  is_active BOOLEAN NOT NULL,
  schedule_id BIGINT REFERENCES schedules(id),
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
);

CREATE TABLE students (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password_hash VARCHAR NOT NULL,
  location_zone_offset VARCHAR NOT NULL,
  role_id BIGINT REFERENCES roles(id),
  is_active BOOLEAN NOT NULL,
  group_id BIGINT REFERENCES groups(id) ON DELETE CASCADE NOT NULL,
  schedule_id BIGINT REFERENCES schedules(id),
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
);

CREATE TABLE courses (
  id BIGSERIAL PRIMARY KEY,
  course_name VARCHAR NOT NULL,
  course_description TEXT NOT NULL,
  teacher_id BIGINT REFERENCES teachers(id) ON DELETE CASCADE NOT NULL,
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
);

CREATE TABLE students_courses (
  student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
  course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
  PRIMARY KEY (student_id, course_id)
);

CREATE TABLE topics (
  id BIGSERIAL PRIMARY KEY,
  topic_name VARCHAR NOT NULL,
  topic_description TEXT NOT NULL,
  topic_order INT NOT NULL,
  course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE marks (
  id BIGSERIAL PRIMARY KEY,
  mark_value INTEGER NOT NULL,
  comment TEXT,
  student_id BIGINT REFERENCES students(id) ON DELETE CASCADE NOT NULL,
  topic_id BIGINT REFERENCES topics(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE lessons (
  id BIGSERIAL PRIMARY KEY,
  date DATE NOT NULL,
  lesson_start_time TIME NOT NULL,
  lesson_end_time TIME NOT NULL,
  zone_offset VARCHAR NOT NULL,
  course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE NOT NULL,
  schedule_id BIGINT REFERENCES schedules(id) ON DELETE CASCADE NOT NULL
);
