CREATE TABLE employee
(
    id         INT          NOT NULL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    manager_id INT,
    CONSTRAINT employee_manager_fk FOREIGN KEY (manager_id) REFERENCES employee (id)
);
