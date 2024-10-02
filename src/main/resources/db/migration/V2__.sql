ALTER TABLE user_roles
DROP
CONSTRAINT fk_user_roles_on_user;

DROP TABLE user_roles CASCADE;