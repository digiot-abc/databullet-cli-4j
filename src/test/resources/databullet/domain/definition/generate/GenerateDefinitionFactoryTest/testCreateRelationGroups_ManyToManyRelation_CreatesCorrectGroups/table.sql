CREATE TABLE student
(
    id   INT          NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE course
(
    id    INT          NOT NULL PRIMARY KEY,
    title VARCHAR(100) NOT NULL
);

CREATE TABLE student_course
(
    student_id INT NOT NULL,
    course_id  INT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    CONSTRAINT student_fk FOREIGN KEY (student_id) REFERENCES student (id),
    CONSTRAINT course_fk FOREIGN KEY (course_id) REFERENCES course (id)
);
