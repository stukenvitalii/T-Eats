CREATE TABLE restaurant
(
    id           SERIAL PRIMARY KEY,    -- Unique identifier for each restaurant
    name         VARCHAR(100) NOT NULL, -- Name of the restaurant
    address      VARCHAR(255) NOT NULL, -- Address of the restaurant
    phone_number VARCHAR(15),           -- Phone number of the restaurant
    email        VARCHAR(100)           -- Email of the restaurant
);