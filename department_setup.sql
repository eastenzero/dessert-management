USE desserts;
DROP TABLE IF EXISTS department;
CREATE TABLE department(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  leader VARCHAR(100),
  phone VARCHAR(50),
  descp VARCHAR(500)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
INSERT INTO department(name, leader, phone, descp)
  VALUES ('人力资源部', '王强', '010-88990001', '负责公司人力资源规划与员工发展'),
         ('研发部', '李敏', '010-88990002', '负责产品研发与技术支持'),
         ('财务部', '赵丽', '010-88990003', '负责公司财务预算和成本控制');
