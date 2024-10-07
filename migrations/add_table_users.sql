CREATE TABLE users (
                       id SERIAL PRIMARY KEY,            -- Unique identifier for each user
                       username VARCHAR(50) NOT NULL UNIQUE, -- Unique username for the user
                       password VARCHAR(255) NOT NULL,   -- Password for the user (hashed)
                       email VARCHAR(100) NOT NULL UNIQUE,   -- Unique email for the user
                       first_name VARCHAR(50),           -- User's first name
                       last_name VARCHAR(50)             -- User's last name
);