CREATE TABLE orders
(
    id            BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, -- Уникальный идентификатор заказа, автоматически генерируемый
    user_id       BIGINT      NOT NULL,                            -- Ссылка на пользователя (идентификатор)
    order_date    TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,  -- Дата размещения заказа
    total_amount  INT         NOT NULL,                            -- Общая сумма заказа
    status        VARCHAR(20) NOT NULL DEFAULT 'Pending',          -- Статус заказа (например, Pending, Completed)
    updated_at    TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,  -- Время последнего обновления
    restaurant_id BIGINT      NOT NULL,                            -- Ссылка на ресторан (идентификатор)
    FOREIGN KEY (user_id) REFERENCES users (id),                   -- Связь с таблицей users
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)        -- Связь с таблицей restaurants
);
