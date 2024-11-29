CREATE TABLE restaurants
(
    id           SERIAL PRIMARY KEY,    -- Unique identifier for each restaurant
    name         VARCHAR(100) NOT NULL, -- Name of the restaurant
    address      VARCHAR(255) NOT NULL, -- Address of the restaurant
    phone_number VARCHAR(15),           -- Phone number of the restaurant
    email        VARCHAR(100)           -- Email of the restaurant
);

INSERT INTO restaurants (name, address, phone_number, email)
VALUES ('Restaurant 1',
        'Address 1',
        '1234567890',
        'restaurant@eats.com');