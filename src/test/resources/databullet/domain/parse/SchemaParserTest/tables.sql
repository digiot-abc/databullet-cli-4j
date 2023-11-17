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
    CONSTRAINT orders_foreign_key FOREIGN KEY (user_id) REFERENCES user_account (id)
);
