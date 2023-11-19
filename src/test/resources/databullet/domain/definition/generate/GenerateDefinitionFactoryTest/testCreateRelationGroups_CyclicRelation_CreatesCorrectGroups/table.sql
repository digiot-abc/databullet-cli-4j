CREATE TABLE a
(
    id   INT NOT NULL PRIMARY KEY,
    b_id INT,
    CONSTRAINT a_b_fk FOREIGN KEY (b_id) REFERENCES b (id)
);

CREATE TABLE b
(
    id   INT NOT NULL PRIMARY KEY,
    a_id INT,
    CONSTRAINT b_a_fk FOREIGN KEY (a_id) REFERENCES a (id)
);
