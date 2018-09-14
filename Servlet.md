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
        <servlet-name>Helloworld</servlet-name>                                      //标签表示的是Servlet名；
        <!--指定Servlet类的全名-->
        <servlet-class>chapter4.Helloworld</servlet-class>                           //表示的是Servlet类的全名
    </servlet>
    <!--定义Servlet映射信息-->
    <servlet-mapping>
        <!--指定Servlet名-->
        <servlet-name>Helloworld</servlet-name>
        <!--指定在浏览器中访问的Servlet的URL-->
        <url-pattern>/servlet/helloworld</url-pattern>                              //标签表示的是在浏览器中访问当前Servlet的URL
    </servlet-mapping>
    <!--配置欢迎页-->
    <welcome-file-list>
        <!--指定index.jsp页面为系统默认的访问页面-->
        <welcom-file>index.jsp</welcom-file>
    </welcom-file-list>
</web-app>
```

# 二、学习Servlet技术
## 配置Tomcat7服务器的数据库连接池
Tomcat服务器提供了一种数据库连接优化的技术——数据库连接技术。数据库连接池负责分配、管理和释放数据库连接，它允许应用程序使用一个现有的数据库连接，而不是重新建立一个数据库连接。
在Tomcat中配置数据库连接有两种形式：配置全局数据库连接和配置局部数据库连接。
```java          //配置全局数据库
<Resource name = "jdbc/webdb" auth="Container"                //name:设置数据源名称，通常有“jdbc/xxx”格式 //auth:设置数据源的管理者
    type="javax.sql.DataSource"                               //type:设置数据源的管理者，其有两个可选择Container和Application.
    <!--指定MySql的JDBC驱动类名-->
    driverClassName="com.mysql.jdbc.Driver"
    <!--指定连接字符串-->
    url="jdbc:mysql://localhost:3306/webdb?characterEncoding=utf-8"
    <!--指定用户名-->
    username="root"
    <!--指定密码-->
    password="1234"
    maxActive="200"                                         //maxActive:连接池可以存储的最大连接数
    maxIdle="50"                                            //maxIdle:最大空闲连接数
    maxWait="3000"/>                                        //maxWait:暂时无法获得数据库连接的等待时间
```
```java       //配置局部数据库
<Context path="/webdemo" docBase="webdemo" debug="0"> //注意配置全局和局部变量Resource来面的代码一样！！！
     <Resorce name="jdbc/webdb" auto="container"
         type="javax.sql.DataSource"                              
         <!--指定MySql的JDBC驱动类名-->
         driverClassName="com.mysql.jdbc.Driver"
         <!--指定连接字符串-->
         url="jdbc:mysql://localhost:3306/webdb?characterEncoding=utf-8"
         <!--指定用户名-->
         username="root"
         <!--指定密码-->
         password="1234"
         maxActive="200"                                         
         maxIdle="50"                                            
         maxWait="3000"/>  
</Context>
```

## 实例：通过数据库连接池连接MySQL数据库
```java
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class ViewDictionary extends HttpServlet{
    //处理客户端的GET请求
    public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置HTTP响应头的Content-Type字段值
        rsponse.setContentType("text/html;charset=UTF-8");
        //获得用于输出消息的PrintWriter对象,获取字符输出流
        PrintWriter out = response.getWriter();
        try{
        //获得Context对象实例
        javax.naming.Context ctx = new javax.naming.InitialContext();
        //根据webdb数据源获得DataSource对象              //要从数据源中获得连接对象，需要使用javax.naming.Context的lookup方法。
        javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("java:/comp/env/jdbc/webdb");
        //获得Connection对象
        Connection conn = ds.getConnection();
        //根据Selection语句建立一个PreparedStatement对象实例
        PreparedStatement pstmt = conns.prepareStatement("SELECTION * FROM   t_dictionary");
        //执行SQL语句
        ResultSet rs = pstmt.executeQuery();
        StringBuilder table = new  StirngBuilder();
        //添加<table>标签的HTML代码
        table.append("<table border="1">);
        table.append("<tr><td>书名</td><td>价格</td></tr>");
        //生成查询结果
        while(rs.next()){
            //添加<table>标签的HTML代码
            table.append("<tr><td>"+rs.getString("english")+"</td><td>");
            table.append(rs.getString("chinese")+"</td></tr>");
        }
        table.append("</table");
        out.println(table.toStirng());//输出查询结果
        pstm.close();         //关闭PreparedStatement对象
    }
    catch(Exception e){
        out.println(e.getMessage());//输出错误消息
    }
  }
}
```
## 实例：处理客户端HTTP GET请求——doGet方法
doGet方法可以处理HTTP GET请求，doGet方法的定义如下：
protected void doGet(HttpServletRequest req,HttpServletResponse resp)thorws ServletException,IOException
```java
public class TestDoGet extends HttpServlet{
    //处理客户端的GET请求
    protected void doGet(HttpServletRequest request,HttpServletResponse response)thorws ServletException,IOException{
        response.setContentType("text/html;charset=UTF-8");//设置Context-Type字段
        PrintWriter out = response.getWriter();//获得PrintWriter对象
        out.println("处理HTTP GET请求");//向客户端输出消息
    }
}
```
```java         //TestDoGet类的配置代码
<!--定义Servlet的名称：TestDoGet-->
<servlet>
    <servlet-name>TestDoGet</servlet-name>
    <servlet-class>chapter4.TestDoGet</servlet-class>
</servlet>
<!--指定Servlet的映射路径-->
<servlet-maping>
    <servlet-name>TestDoGet</servlet-name>
    <url-pattern>/servlet/TestDoGet</url-pattern>
</srvlet-maping>
```
## 处理客户端HTTP POST请求——doPost方法
doPost方法用来处理客户端的HTTP POST请求；doPost方法的定义如下：
protected void doPost(HttpServletRequest req,HttpServletResponse resp)thorws ServletException,IOException)
```java
public class TestDoPost extends HttpServlet{
    //处理客户端的POST请求
    protected void doPosst(HttpServletRequest request,HttpServletResponse response)thorws ServletException,IOException{
        response.setContentType("text/html;charset=UTF-8");//设置Context-Type字段
        PrintWriter out = response.getWriter();//获得PrintWriter对象
        out.println("处理HTTP POST请求");//向客户端输出消息
    }
}
```
```java         //TestDoPost类的配置代码
<!--定义Servlet的名称：TestDoPost-->
<servlet>
    <servlet-name>TestDoPost</servlet-name>
    <servlet-class>chapter4.TestDoPost</servlet-class>
</servlet>
<!--指定Servlet的映射路径-->
<servlet-maping>
    <servlet-name>TestDoPost</servlet-name>
    <url-pattern>/servlet/TestDoPost</url-pattern>
</srvlet-maping>
```
为了正确的显示出TestDoPost类的输出文本——处理HTTP POST请求，需要创建访问页面，具体html代码如下：
```java
<!--使用<form>通过post方法访问TestDoPost-->
<form action=".../servlet/TestDoPost" method="post">
    <input type="text" name="name"/><p/>
    <input type="submit" value="提交"/>
</form>
```

# 三、掌握HttpServletResponse类


# 四、掌握HttpServletRequest类


# 五、处理Cookie





# 六、处理Session



# 七、解决Web开发中的乱码问题

