CREATE TABLE "user" (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL,
  application_id VARCHAR(30),
  update_date VARCHAR(100) NOT NULL,
  insert_date VARCHAR(30) NOT NULL,
  is_valid BOOLEAN NOT NULL,
  accepted_terms BOOLEAN NOT NULL
);