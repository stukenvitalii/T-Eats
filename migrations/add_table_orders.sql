CREATE TABLE "order"
(
    id            SERIAL PRIMARY KEY,                                -- Unique identifier for each order
    user_id       BIGINT         NOT NULL,                           -- Foreign key referencing the users table
    order_date    TIMESTAMP               DEFAULT CURRENT_TIMESTAMP, -- Timestamp for order placement
    total_amount  DECIMAL(10, 2) NOT NULL,                           -- Total amount for the order
    status        VARCHAR(20)    NOT NULL DEFAULT 'Pending',         -- Status of the order (e.g., Pending, Completed)
    created_at    TIMESTAMP               DEFAULT CURRENT_TIMESTAMP, -- Timestamp for order creation
    updated_at    TIMESTAMP               DEFAULT CURRENT_TIMESTAMP, -- Timestamp for last update (set manually)
    restaurant_id BIGINT         NOT NULL,                           -- Foreign key referencing the restaurants table
    FOREIGN KEY (user_id) REFERENCES users (id),                     -- Foreign key constraint for users
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)          -- Foreign key constraint for restaurants
);