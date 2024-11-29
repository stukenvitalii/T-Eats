CREATE TABLE dishes
(
    id            SERIAL PRIMARY KEY,                       -- Unique identifier for each dish
    name          VARCHAR(100)   NOT NULL,                  -- Name of the dish
    description   TEXT,                                     -- Description of the dish
    price         DECIMAL(10, 2) NOT NULL,                  -- Price of the dish
    restaurant_id BIGINT         NOT NULL,                  -- Identifier of the restaurant to which the dish belongs
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,      -- Timestamp for dish creation
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,      -- Timestamp for last update (set manually)
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) -- Foreign key constraint
);

INSERT INTO dishes (name, description, price, restaurant_id)
VALUES ('Dish 1',
        'Description 1',
        10.00,
        1);