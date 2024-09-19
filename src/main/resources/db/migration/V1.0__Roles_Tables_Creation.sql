CREATE TABLE roles (
  id BIGSERIAL PRIMARY KEY,
  role_name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE permissions (
  id BIGSERIAL PRIMARY KEY,
  permission_name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE roles_permissions (
  role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
  permission_id BIGINT REFERENCES permissions(id) ON DELETE CASCADE,
  PRIMARY KEY (role_id, permission_id)
);
