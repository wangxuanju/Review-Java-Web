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
## 实例：处理客户端各种请求——service方法
如果要同时处理HTTP GET 和HTTP POST请求，就必须在Servlet类中包含doGet方法和doPost方法，并且在其中一个方法编写实际的代码，而在另外一个方法中只需要简单的调用另一个方法就可以；
```java
public class TestGetAndPost extends HttpServlet{
    //处理客户端的GET请求
    protected void doGet(HttpServletRequest request,HttpServletResponse response)thorws ServletException,IOException{
        //编写实际的代码
    }
    //处理客服端的POST请求
    protected void doPosst(HttpServletRequest request,HttpServletResponse response)thorws ServletException,IOException{
        //处理代码和doGet一样，需要调用doGet方法
        doGet(request,response);
    }
}
```
可以通过覆盖HttpSrvlet类的service方法来使代码更简洁。当具体执行HttpServlet类的service方法时，会根据HTTP协议的请求方式调用不同的doXXX方法，例如如果为HTTP Get请求则调用doGet方法；如果为HTTP POST请求则调用doPost方法。
```java
//HttpServlet类中的service方法
protected void service(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
    String method = req.getMethod();//获得http请求方法
    //METHOD_GET表示HTTP_GET方法
    if(method.equals(METHOD_GET)){
        doGet(req,resp);
    }else if(method.equals(METHOD_POST)){          //METHOD_POST表示HTTP_POST方法
        doPost(req,resp);
    }else{
        //获得异常信息
        String errMsg = lStrings.getString("http.method_not_implemented");
        Object[] errArgs = new Object[1];
        errArgs[0] = method;
        errMsg = MessageFormat.format(errMsg,errArgs);
        //向客户端发送抛出异常信息的通知
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,errMsg);
    }
}
```
如果在Servlet子类中覆盖了service方法，doXXX方法就不会再被调用；如果想要覆盖后的service方法中仍然调用doXXX方法，可以在service方法中加入如下代码：super.service(request,response);

```java         //TestGetAndPost类演示了如何使用service方法处理HTTP的POST和GET请求
public class TestGetAndPost extends HttpServlet{
    //覆盖了HttpServlet类的service方法
    @OVerride       //为注释表示service方法时覆盖父类（HttpServlet）的同名、同参数、同返回类型的方法。
    protected void service(HttpServletResponse request,HttpServletResponse response)throws ServletException,IOException{
        response.setContendType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("处理所有的HTTP请求");//向客户端输出信息
    }
}
```
## 实例：初始化和销毁Servlet
当Servlet对象实例被Web服务器第一次创建时，会调用HttpServlet类的init方法。开发人员可以在这个方法里实现初始化Servlet的功能，当Web服务器销毁Servlet对象实例时，会调用HttpServlet类的destory方法，开发人员可以在这个方法中释放由Servlet所占用的各种资源；init方法和destory方法在Servlet的整个生命周期都只调用一次。
```java
public class TestInitDestory extends HttpServlet{
    //覆盖destory方法，该方法将在Web服务器销毁TestInitDestory实例时被调用
    @Override
    public void destory(){
        //输出提示信息，以表明Servlet被销毁
        System.out.println("Servlet被销毁");
    }
    //覆盖了init方法，该方法将在web服务器第一次实例化时被调用
    @Override
    public void init() throws ServletException{
        //输出提示信息，以表明Servlet对象实例化被创建第一个实例化被调用
        Sytem.out.println("初始化Servlet");
    }
    //覆盖了doGet方法
    @Override
    protected void doGet(HttpServletResponse request,HttpServletResponse response)throws ServletException,IOException{
        //为了使程序不至于出错，需要加上此方法
    }
}
```
TestInitDestory类的配置代码如下：
```java
<!--定义Servlet的名称：TestInitDestory-->
<servlet>
    <servlet-name>TestInitDestory</servlet-name>
    <servlet-class>chapter4.TestInitDestory</servlet-class>
</servlet>
<!--指定Servlet的映射路径-->
<servlet-mapping>
    <servlet-name>TestInitDestory</servlet-name>
    <url-pattern>/servlet/TestInitDestory</url-pattern>
</servlet-mapping>
```
## 实例：输出字符流响应消息——PrintWriter类
前面的例子均使用PrintWriter对象向客户端输出信息；通过HttpServletResponse类的getWriter方法获得PrintWriter类的对象实例，具体使用时注意：

（1）当通过HttpServletResponse类的getWriter方法获取PrintWriter类对象之前，需要使用setContendType方法设置Content-Type字段值。

（2）HttpServletResponse类的addHeader和setHeader方法用来进行相应头的设置，可以在调用getWriter方法前后被调用。

（3）虽然可以使用addHeader和setHeader方法在调用getWriter方法后设置Contend-Type字段的值，如果在调用getWriter方法之前使用setContendType方法设置相应消息的字段集编码，在客户端浏览器中的中文信息仍然会显示"?"乱码。
```java   //TestPrintWriter类演示如何使用PrintWriter对象向客户端输出信息
public class TestPrintWriter extends HttpSErvlet{
    //覆盖service方法
    @Override
    protected void service(HttpSErvletRequest request,HttpServletResponse response)throws ServletException,IOException{
        response.setHeader("myhead1","value1");           //添加一个新的HTTP头
        PrintWriter out = response.getWriter();
        
        response.setContendType("text/html;charset=utf-8");//设置响应消息头必须在调用getWriter方法之前进行（这样会产生乱码）
        
        response.setHeader("Contend-Type","text/html;charset=utf-8");//虽然可以成功设置Contend-Type字段的值，但输出却是“?”
        response.setHeader("myhead2”,"value2");//这条语句时有效的
        out.println("<b>响应消息</b>");        //向客户端输出消息   
    }
}
```
## 实例：输出字节流响应消息——ServletOutputStream类
如果想要向客户端输出文本信息，使用PrintWriter类；但向客户端输出字节消息，如图像、视频文件等，就必须使用ServletOutputStream类。可以通过HttpServletResponse类的getOutputStream方法获得ServletOutStream对象实例。
```java  //ShowImage类演示了如何使用ServletStream方法获得ServletOutputStream对象在客户端浏览器中显示图像
public class ShowImag extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置响应消息的类型为图像
        response.setContendType("image/jpeg");
        //获得SevletOutStream对象
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[8192];//每次从文件输入流中读取8k字节
        //获得name请求参数所指定的图像绝对路径
        String imageName = request.getParameter("name");
        //获取图像文件的输入流
        FileInputStream fis = new FileInputStream(imageName);
        int count = 0;
        //通过循环读取并传送name所指定的图像数据
        while(true){
            count = fis.read(buffer);//将字节读到buffer缓冲区
            //当文件输入流中的字节读完后，退出while循环
            if(count<=0)
                break;
            os.writer(buffer,0,count);
        }
        fis.close();
    }
}
```
在显示图像时，必须将Contend-Type字段值设为"image/jpeg";

ShowImage类使用了请求参数name来获得客户端指定的图像绝对路径；也可以使用相对路径，使用ServleContext接口的getRealPath方法将相对路径转换为绝对路径。

getWrier方法和getOutputStream方法不能同时使用。

## 实例：包含Web资源——RequestDispatcher.include方法

# 三、掌握HttpServletResponse类


# 四、掌握HttpServletRequest类


# 五、处理Cookie





# 六、处理Session



# 七、解决Web开发中的乱码问题

