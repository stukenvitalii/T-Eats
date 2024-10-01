CREATE TABLE order_dishes (
                              order_id INT NOT NULL,            -- Foreign key referencing the orders table
                              dish_id INT NOT NULL,             -- Foreign key referencing the dishes table
                              quantity INT NOT NULL,            -- Quantity of the dish in the order
                              PRIMARY KEY (order_id, dish_id),
                              FOREIGN KEY (order_id) REFERENCES orders(id), -- Foreign key constraint for orders
                              FOREIGN KEY (dish_id) REFERENCES dishes(id)   -- Foreign key constraint for dishes
);