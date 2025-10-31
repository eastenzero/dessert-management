create database desserts;
use desserts;
create table category(
  id int auto_increment primary key,
  name varchar(100) not null,
  descp varchar(500)
);
create table dessert(
  id int auto_increment primary key,
  name varchar(100) not null,
  photoUrl varchar(500),
  price double,
  descp varchar(500),
  release_date date,
  cat_id int references category(id)
);
create table department(
  id int auto_increment primary key,
  name varchar(200) not null,
  leader varchar(100),
  phone varchar(50),
  descp varchar(500)
);
create table t_user(
   id int auto_increment primary key,
   username varchar(200) not null, # 登录用户名
   password varchar(200) not null, # 登录密码，经BCrypt算法加密
   active int(1) default 1 # 1用户可用，0 用户不可用
);
create table t_role(
   id int auto_increment primary key,
   role varchar(200) # 角色名
);
create table t_user_role(
   id int auto_increment primary key,
   user_id int references t_user(id),  #引用用户
   role_id int references t_role(id)   #引用角色
);

insert into category(name, descp)
  values ('传统甜品','精致到不忍下嘴的中国传统甜品,个个都经典'),
        ('凉粉系列','创意凉粉系列,将凉粉这一种民间小吃融入菜肴'),
        ('雪山系列','造型高颜值,口感绵软,越嚼越有嚼劲');

insert into dessert (name, photoUrl, price, descp, release_date, cat_id)
  values ('生磨芝麻糊加黑糯米', '/photo/001.jpg', 15, '满满一碗芝麻糊中央放了一个圆圆的黑糯米球，入口醇香柔滑，满满一口，让人齿颊留香', '2024-02-01', 1),
        ('鲜杂果凉粉(椰汁芒汁)', '/photo/002.jpg', 20, '新鲜水果料足量多，吸引人眼球。如艺术品般令人赞叹的造型，味道也很赞。', '2024-02-03', 2),
        ('生磨芝麻糊加黑糯米', '/photo/003.jpg', 25, '满满一碗芝麻糊中央放了一个圆圆的黑糯米球，入口醇香柔滑，满满一口，让人齿颊留香', '2024-02-05', 1);
insert into dessert (name, photoUrl, price, descp, release_date, cat_id)
  values ('生磨芝麻糊加黑糯米Ⅱ', '/photo/001.jpg', 15, '满满一碗芝麻糊中央放了一个圆圆的黑糯米球，入口醇香柔滑，满满一口，让人齿颊留香', '2024-02-01', 1),
        ('鲜杂果凉粉(椰汁芒汁)Ⅱ', '/photo/002.jpg', 20, '新鲜水果料足量多，吸引人眼球。如艺术品般令人赞叹的造型，味道也很赞。', '2024-02-03', 2),
        ('生磨芝麻糊加黑糯米Ⅱ', '/photo/003.jpg', 25, '满满一碗芝麻糊中央放了一个圆圆的黑糯米球，入口醇香柔滑，满满一口，让人齿颊留香', '2024-02-05', 1);
insert into dessert (name, photoUrl, price, descp, release_date, cat_id)
  values ('生磨芝麻糊加黑糯米Ⅲ', '/photo/001.jpg', 15, '满满一碗芝麻糊中央放了一个圆圆的黑糯米球，入口醇香柔滑，满满一口，让人齿颊留香', '2024-02-01', 1),
        ('鲜杂果凉粉(椰汁芒汁)Ⅲ', '/photo/002.jpg', 20, '新鲜水果料足量多，吸引人眼球。如艺术品般令人赞叹的造型，味道也很赞。', '2024-02-03', 2),
        ('生磨芝麻糊加黑糯米Ⅲ', '/photo/003.jpg', 25, '满满一碗芝麻糊中央放了一个圆圆的黑糯米球，入口醇香柔滑，满满一口，让人齿颊留香', '2024-02-05', 1);
insert into department(name, leader, phone, descp)
  values ('人力资源部', '王强', '010-88990001', '负责公司人力资源规划与员工发展'),
        ('研发部', '李敏', '010-88990002', '负责产品研发与技术支持'),
        ('财务部', '赵丽', '010-88990003', '负责公司财务预算和成本控制');

insert into t_user(username,password) values('admin','12345'), ('bob','123');
insert into t_role(role) values('ROLE_admin'), ('ROLE_normal');
insert into t_user_role(user_id, role_id) VALUES (1,1), (2,2);
