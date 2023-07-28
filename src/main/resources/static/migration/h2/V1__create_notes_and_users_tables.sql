CREATE TABLE users (
  id bigint NOT NULL AUTO_INCREMENT,
  login varchar(50) DEFAULT NULL,
  password varchar(100) DEFAULT NULL,
  role varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_sx468g52bpetvlad2j9y0lptc (login)
)
CREATE TABLE notes (
  id binary(16) NOT NULL,
  content varchar(10000) DEFAULT NULL,
  privacy varchar(255) DEFAULT NULL,
  title varchar(100) DEFAULT NULL,
  user_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKechaouoa6kus6k1dpix1u91c (user_id),
  CONSTRAINT FKechaouoa6kus6k1dpix1u91c FOREIGN KEY (user_id) REFERENCES users (id)
)