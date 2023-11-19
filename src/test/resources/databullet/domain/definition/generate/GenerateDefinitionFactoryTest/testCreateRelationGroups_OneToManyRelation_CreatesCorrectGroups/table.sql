CREATE TABLE department
(
    id   INT          NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE employee_department
(
    employee_id   INT NOT NULL,
    department_id INT NOT NULL,
    PRIMARY KEY (employee_id, department_id),
    CONSTRAINT employee_fk FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT department_fk FOREIGN KEY (department_id) REFERENCES department (id)
);