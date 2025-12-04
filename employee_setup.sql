USE desserts;
DROP TABLE IF EXISTS employee;
CREATE TABLE employee(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  gender VARCHAR(10),
  dept_id INT,
  phone VARCHAR(50),
  hire_date DATE,
  salary DOUBLE,
  descp VARCHAR(500)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;

INSERT INTO employee(name, gender, dept_id, phone, hire_date, salary, descp)
  VALUES ('张三', '男', 1, '010-88880001', '2024-03-01', 8000, '资深销售'),
         ('李四', '女', 2, '010-88880002', '2024-04-15', 12000, '研发工程师'),
         ('王五', '男', 3, '010-88880003', '2024-05-10', 9000, '财务专员');
