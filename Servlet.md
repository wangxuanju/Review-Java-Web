# 一、编写Java Web的HelloWord程序
##  用MyEclipse工具建立一个Servlet程序——Helloword
```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class Helloworld extends HttpServlet{
    public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置HTTP响应头的Content-Type字段值
        rsponse.setContentType("text/html");
        //获得用于输出消息的PrintWriter对象
        PrintWriter out = response.getWriter();
        out.println("<b>Hello world</B>");//向客户端输出Hello world
    }
}
```
（1）Servlet类必须从javax.servlet.http.HttpServlet类继承。

（2）由于本例向客户端浏览器输出的信息中含有HTML代码，必须使用setContendType()方法设置HTTP响应头字段Contend-Type的值，本例中Contend-Type属性的值是“text/html”;

（3）使用HttpServletResponse类的getWriter()方法获得一个PrintWriter对象，并使用PrintWriter类的println方法向客户端输出信息。

在建立Servlet的过程中，MyExclipse会自动的在web.xml文件中添加Servlet的配置代码，实例如下：
```java
<?xml version="1.0" encoding="UTF-8"?>
<!--配置文件的根标签，指定了命名空间、版本等信息-->
<web-app version="3.0"
    xlmns="http://java.sun.com/xml/ns/javaee"
    xlmns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">
    <!--定义Servlet本身的属性-->
    <servlet>
        <!--指定Servlet的名称-->
        <servlet-name>Helloworld</servlet-name>
        <!--指定Servlet类的全名-->
        <servlet-class>chapter4.Helloworld</servlet-class>
    </servlet>
    <!--定义Servlet映射信息-->
    <servlet-mapping>
        <!--指定Servlet名-->
        <servlet-name>Helloworld</servlet-name>
        <!--指定在浏览器中访问的Servlet的URL-->
        <url-pattern>/servlet/helloworld</url-pattern>
    </servlet-mapping>
    <!--配置欢迎页-->
    <welcome-file-list>
        <!--指定index.jsp页面为系统默认的访问页面-->
        <welcom-file>index.jsp</welcom-file>
    </welcom-file-list>
</web-app>
?
```

# 二、学习Servlet技术


# 三、掌握HttpServletResponse类


# 四、掌握HttpServletRequest类


# 五、处理Cookie





# 六、处理Session



# 七、解决Web开发中的乱码问题

