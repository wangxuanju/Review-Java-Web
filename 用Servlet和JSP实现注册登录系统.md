# 一、系统概述
## 系统功能简介
注册系统，就是向数据库的t_users表中添加一条记录；登录功能就是查询数据库中的t_users表的相关记录。
## 系统总体的结果
系统的注册系统和登录系统是相互独立的；每一个系统分别由一个处理业务的Servlet和若干JSP页面组成。
注册系统的Servelt类是Register类，另有一个用于显示用户注册信息页面的register.jsp文件和一个负责显示注册结果的result.jsp文件。
登录系统的Servelt类是Login类，有一个显示登录信息的login.jsp文件和一个表示用户已经成功登录的main.jsp文件。

# 二、设计数据库
建立t_users表的SQL语句如下
```java
create table if not exists webdb.t_users(
    user_name varchar(20) collate utf8_unicode_ci not null,
    password_md5 varchar(50) collate utf8_unicode_ci not null,
    email varchar(3) collate utf8_unicode_ci not null,
    primary_key (user_name)
)begine=InnoDB default charset=utf8 collate=utf8_unicode_ci;
```

# 三、实现系统的基础类
在注册系统中使用一些重要类，用于连接和操作数据库的DBServlet类、用于对字符串进行MD5加密的Encrypter类以及实现图形验证码的ValidatinCode类。
## 实现访问数据库的DBServlet类


# 四、实现注册系统

# 五、实现登录系统
