-- ==============================================
-- 1. 创建数据库（不存在则创建）
-- ==============================================
CREATE DATABASE IF NOT EXISTS library_db 
DEFAULT CHARACTER SET utf8 
DEFAULT COLLATE utf8_general_ci;

-- ==============================================
-- 2. 选择要操作的数据库
-- ==============================================
USE library_db;

-- ==============================================
-- 3. 图书表（book）+ 触发器
-- ==============================================
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `book_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图书唯一ID',
  `isbn` VARCHAR(20) NOT NULL COMMENT 'ISBN编号',
  `book_name` VARCHAR(100) NOT NULL COMMENT '图书名称',
  `author` VARCHAR(50) NOT NULL COMMENT '作者',
  `publisher` VARCHAR(50) NOT NULL COMMENT '出版社',
  `publish_date` DATE NOT NULL COMMENT '出版日期',
  `location` VARCHAR(30) NOT NULL COMMENT '馆藏位置',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-在馆，2-下架',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `idx_isbn` (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图书信息表';

-- 图书表：插入时自动填充create_time
DELIMITER //
CREATE TRIGGER tr_book_insert BEFORE INSERT ON `book`
FOR EACH ROW
BEGIN
  SET NEW.create_time = NOW();
END //
DELIMITER ;

-- 图书表：更新时自动填充update_time
DELIMITER //
CREATE TRIGGER tr_book_update BEFORE UPDATE ON `book`
FOR EACH ROW
BEGIN
  SET NEW.update_time = NOW();
END //
DELIMITER ;

-- ==============================================
-- 4. 用户表（user）
-- ==============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
  `user_name` VARCHAR(20) NOT NULL COMMENT '用户姓名',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号（登录账号）',
  `password` VARCHAR(60) NOT NULL COMMENT 'MD5加密密码',
  `role` TINYINT NOT NULL DEFAULT 2 COMMENT '角色：1-管理员，2-普通用户',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，2-黑名单',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ==============================================
-- 5. 借阅记录表（borrow_record）+ 触发器
-- ==============================================
DROP TABLE IF EXISTS `borrow_record`;
CREATE TABLE `borrow_record` (
  `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '借阅记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `book_id` BIGINT NOT NULL COMMENT '图书ID',
  `borrow_date` DATETIME NULL COMMENT '借阅日期',
  `due_date` DATETIME NOT NULL COMMENT '应还日期',
  `return_date` DATETIME DEFAULT NULL COMMENT '实际还书日期',
  PRIMARY KEY (`record_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借阅记录表';

-- 借阅记录表：插入时自动填充borrow_date
DELIMITER //
CREATE TRIGGER tr_borrow_record_insert BEFORE INSERT ON `borrow_record`
FOR EACH ROW
BEGIN
  SET NEW.borrow_date = NOW();
END //
DELIMITER ;

-- ==============================================
-- 6. 插入测试数据
-- ==============================================
INSERT INTO `user` (`user_name`, `phone`, `password`, `role`) 
VALUES ('管理员', '13800138000', 'e10adc3949ba59abbe56e057f20f883e', 1); -- 密码123456

INSERT INTO `user` (`user_name`, `phone`, `password`, `role`) 
VALUES ('普通用户', '13800138001', 'e10adc3949ba59abbe56e057f20f883e', 2); -- 密码123456

INSERT INTO `book` (`isbn`, `book_name`, `author`, `publisher`, `publish_date`, `location`, `stock`)
VALUES ('9787115428028', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', '2018-01-01', 'A区1架', 5);

USE library_db;

-- 为借阅记录表添加罚款字段（适配还书逻辑）
ALTER TABLE `borrow_record` 
ADD COLUMN `fine_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '罚款金额',
ADD COLUMN `fine_status` TINYINT NOT NULL DEFAULT 0 COMMENT '罚款状态：0-未缴/1-已缴';