# 一、通过MyEclipse工具编写一个JSP程序
当动态Java Web应用中静态内容远多于动态内容时，在Servlet中通过PrintWriter对象或OutputStream对象向客户端输出这些静态内容就显得十分麻烦；所以在Servlet的基础上推出了JSP。
JSP页面中动态内容都放在<%..%>之间，<%..%>之外的内容都被认为是静态的内容。
```java      //JSP小例子
<%@ page language="java" contendType="text/html;charset=utf-8"
pageEncoding="UTF-8"%>
<!--导入相应包-->
<%@page import="java.text.*,java.util.*"%>
<html>
    <head>
        <title>显示服务器的当前时间</title>
    </head>
    <body>
        现在服务器的时间是：
        <!--将时间部分设置成红色-->
        <font color="red">
        <%
            //设置格式化时间字符串
            SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            out.println(format.format(new Date());//输出被格式的时间
        %>
        </front>
    </body>
</html>
```
建立的JSP程序可以在web.xml文件中加入如下内容映射它的访问路径
```java
<!--定义Servlet本身的属性-->
<servlet>
    <servlet-name>servertime</servlet-name>
    <jsp-file>/chapter5/servertime.jsp</jsp-file>
</servlet>
<!--定义Servlet映射信息-->
<servlet-mapping>
    <servleet-name>servertime</servlet-name>
    <url-pattern>/servertime.html</url-pattern>
</servlet-mapping>
```
除了可以改变JSP的访问路径外，还可以直接将JSP的扩展名改成其他的名称，如“.html”.

# 二、JSP运行原理






# 三、JSP基本语法


# 四、JSP指令

# 五、JSP内置对象


# 六、JSP标签






# 七、JSP表征标签库
