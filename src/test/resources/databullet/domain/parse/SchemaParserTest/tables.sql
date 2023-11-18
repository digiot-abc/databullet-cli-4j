CREATE TABLE user_account
(
    id        VARCHAR(100) NOT NULL PRIMARY KEY,
    name      VARCHAR(20)  NOT NULL,
    birth_day DATE
);

CREATE TABLE orders
(
    id              INT NOT NULL PRIMARY KEY,
    user_id         VARCHAR(200),
    quantity        INT,
    bought_datetime TIMESTAMP,
    CONSTRAINT orders_fk FOREIGN KEY (user_id) REFERENCES user_account (id)
);

CREATE TABLE user_address
(
    id      VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    address TEXT,
    CONSTRAINT user_address_fk FOREIGN KEY (user_id) REFERENCES user_account (id)
);

CREATE TABLE order_delivery
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) NOT NULL,
    user_address_id VARCHAR(100) NOT NULL,
    transaction     TEXT,
    CONSTRAINT order_delivery_fk1 FOREIGN KEY (user_id) REFERENCES user_account (id),
    CONSTRAINT order_delivery_fk2 FOREIGN KEY (user_address_id) REFERENCES user_address (id)
);
