-- Drop tables if they exist
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS t_user;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS dessert;

-- Create Department Table
CREATE TABLE department(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  leader VARCHAR(100),
  phone VARCHAR(50),
  descp VARCHAR(500)
);

-- Create Employee Table
CREATE TABLE employee(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  gender VARCHAR(10),
  dept_id INT,
  phone VARCHAR(50),
  hire_date DATE,
  salary DOUBLE,
  descp VARCHAR(500)
);

-- Create User Table (mapped to t_user in UserMapper)
CREATE TABLE t_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  active INT DEFAULT 1
);

-- Create Category Table
CREATE TABLE category (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  descp VARCHAR(500)
);

-- Create Dessert Table
CREATE TABLE dessert (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  descp VARCHAR(500),
  photoUrl VARCHAR(255),
  price DOUBLE,
  release_date DATE,
  cat_id BIGINT
);

-- Initial Data: Department
INSERT INTO department(name, leader, phone, descp) VALUES 
('人力资源部', '王强', '010-88990001', '负责公司人力资源规划与员工发展'),
('研发部', '李敏', '010-88990002', '负责产品研发与技术支持'),
('财务部', '赵丽', '010-88990003', '负责公司财务预算和成本控制');

-- Initial Data: Employee
INSERT INTO employee(name, gender, dept_id, phone, hire_date, salary, descp) VALUES 
('张三', '男', 1, '010-88880001', '2024-03-01', 8000, '资深销售'),
('李四', '女', 2, '010-88880002', '2024-04-15', 12000, '研发工程师'),
('王五', '男', 3, '010-88880003', '2024-05-10', 9000, '财务专员');

-- Initial Data: User (password 123456)
INSERT INTO t_user (username, password, active) VALUES 
('admin', '123456', 1),
('user', '123456', 1);

-- Initial Data: Category
INSERT INTO category (name, descp) VALUES 
('蛋糕', '各式各样的美味蛋糕'),
('饼干', '酥脆可口的饼干'),
('饮品', '清凉解渴的饮品');

-- Initial Data: Dessert
INSERT INTO dessert (name, descp, photoUrl, price, release_date, cat_id) VALUES 
('草莓蛋糕', '新鲜草莓制作的蛋糕', 'https://via.placeholder.com/150', 25.0, '2024-01-01', 1),
('巧克力曲奇', '浓郁巧克力口味', 'https://via.placeholder.com/150', 10.0, '2024-01-02', 2),
('拿铁咖啡', '香醇浓郁的拿铁', 'https://via.placeholder.com/150', 15.0, '2024-01-03', 3);
