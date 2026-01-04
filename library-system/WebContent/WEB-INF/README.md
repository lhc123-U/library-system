\# 图书馆管理系统

基于Java Web（JSP+Servlet+MySQL）开发的轻量级图书馆管理系统，支持图书借阅、归还、管理员新增图书等核心功能。





\## 一、环境要求

\- JDK：1.8及以上

\- Web服务器：Tomcat 9.0

\- 数据库：MySQL 8.0

\- 开发工具：Eclipse（Mars版本）





\## 二、数据库初始化

1\. 打开MySQL客户端（如Navicat、MySQL Command Line），新建数据库`library\_db`；

2\. 执行项目根目录下`sql/library\_db.sql`脚本，自动创建表并插入测试数据：

&nbsp;  ```sql

&nbsp;  -- 1. 用户表（user）

&nbsp;  CREATE TABLE `user` (

&nbsp;    `user\_id` BIGINT PRIMARY KEY AUTO\_INCREMENT COMMENT '用户唯一ID',

&nbsp;    `phone` VARCHAR(20) UNIQUE NOT NULL COMMENT '登录手机号',

&nbsp;    `password` VARCHAR(50) NOT NULL COMMENT '登录密码（MD5加密）',

&nbsp;    `user\_name` VARCHAR(50) NOT NULL COMMENT '用户名',

&nbsp;    `role` INT NOT NULL DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员'

&nbsp;  );



&nbsp;  -- 2. 图书表（book）

&nbsp;  CREATE TABLE `book` (

&nbsp;    `book\_id` BIGINT PRIMARY KEY AUTO\_INCREMENT COMMENT '图书唯一ID',

&nbsp;    `isbn` VARCHAR(30) UNIQUE NOT NULL COMMENT 'ISBN编号',

&nbsp;    `book\_name` VARCHAR(100) NOT NULL COMMENT '图书名称',

&nbsp;    `author` VARCHAR(50) NOT NULL COMMENT '作者',

&nbsp;    `publisher` VARCHAR(100) NOT NULL COMMENT '出版社',

&nbsp;    `publish\_date` DATE NOT NULL COMMENT '出版日期',

&nbsp;    `location` VARCHAR(50) NOT NULL COMMENT '馆藏位置',

&nbsp;    `stock` INT NOT NULL DEFAULT 1 COMMENT '库存数量',

&nbsp;    `status` INT NOT NULL DEFAULT 1 COMMENT '状态：1-在馆，0-下架'

&nbsp;  );



&nbsp;  -- 3. 借阅记录表（borrow\_record）

&nbsp;  CREATE TABLE `borrow\_record` (

&nbsp;    `record\_id` BIGINT PRIMARY KEY AUTO\_INCREMENT COMMENT '记录唯一ID',

&nbsp;    `user\_id` BIGINT NOT NULL COMMENT '借阅用户ID',

&nbsp;    `book\_id` BIGINT NOT NULL COMMENT '借阅图书ID',

&nbsp;    `borrow\_date` DATE NOT NULL COMMENT '借阅日期',

&nbsp;    `due\_date` DATE NOT NULL COMMENT '应还日期',

&nbsp;    `return\_date` DATE NULL COMMENT '实际归还日期（NULL表示未归还）',

&nbsp;    `fine\_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '罚款金额',

&nbsp;    FOREIGN KEY (`user\_id`) REFERENCES `user`(`user\_id`),

&nbsp;    FOREIGN KEY (`book\_id`) REFERENCES `book`(`book\_id`)

&nbsp;  );



&nbsp;  -- 测试数据

&nbsp;  INSERT INTO `user` VALUES 

&nbsp;  (1, '13800138000', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 1),

&nbsp;  (2, '13800138001', 'e10adc3949ba59abbe56e057f20f883e', '普通用户', 0);



&nbsp;  INSERT INTO `book` VALUES 

&nbsp;  (1, '9787115428028', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', '2018-01-01', 'A区1架', 5, 1);

&nbsp;  ```





\## 三、部署步骤

1\. \*\*导入项目到Eclipse\*\*：

&nbsp;  - 打开Eclipse（Mars版本），点击「File → Import」；

&nbsp;  - 在弹出窗口中选择「General → Existing Projects into Workspace」，点击「Next」；

&nbsp;  - 点击「Browse」，选择本地项目根目录，勾选项目名称，点击「Finish」完成导入。



2\. \*\*配置Tomcat服务器\*\*：

&nbsp;  - 点击Eclipse菜单栏「Window → Preferences」，选择「Server → Runtime Environments」；

&nbsp;  - 点击「Add」，选择「Apache → Apache Tomcat v9.0」，点击「Next」；

&nbsp;  - 选择Tomcat 9.0的安装路径，点击「Finish → Apply and Close」；

&nbsp;  - 在Eclipse的「Servers」视图中，右键空白处选择「New → Server」，选择Tomcat 9.0，点击「Next」；

&nbsp;  - 勾选当前项目，点击「Add → Finish」完成项目部署。



3\. \*\*修改数据库连接\*\*：

&nbsp;  - 打开项目中`com.util.DBUtil.java`文件，替换数据库账号密码为你的MySQL信息：

&nbsp;    ```java

&nbsp;    private static final String URL = "jdbc:mysql://localhost:3306/library\_db?useSSL=false\&characterEncoding=utf8";

&nbsp;    private static final String USER = "你的MySQL用户名"; // 如root

&nbsp;    private static final String PWD = "你的MySQL密码";   // 如123456

&nbsp;    ```





\## 四、测试账号

\- 管理员：手机号`13800138000`，密码`123456`

\- 普通用户：手机号`13800138001`，密码`123456`

