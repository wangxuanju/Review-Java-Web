<!-- GFM-TOC -->
* [一、编写Java Web的HelloWord程序](#一编写javaweb的helloword程序)
* [用MyEclipse工具建立一个Servlet程序——Helloword](#用myeclipse工具建立一个servlet程序——helloword)
* [二、学习Servlet技术](#二学习servlet技术)
* [配置Tomcat7服务器的数据库连接池](#配置tomcat7服务器的数据库连接池)
* [实例：通过数据库连接池连接MySQL数据库](#实例：通过数据库连接池连接mysql数据库)
* [实例：处理客户端HTTP&nbsp;GET请求——doGet方法](#实例：处理客户端http&nbsp;get请求——doget方法)
* [处理客户端HTTP&nbsp;POST请求——doPost方法](#处理客户端http&nbsp;post请求——dopost方法)
* [实例：处理客户端各种请求——service方法](#实例：处理客户端各种请求——service方法)
* [实例：初始化和销毁Servlet](#实例：初始化和销毁servlet)
* [实例：输出字符流响应消息——PrintWriter类](#实例：输出字符流响应消息——printwriter类)
* [实例：输出字节流响应消息——ServletOutputStream类](#实例：输出字节流响应消息——servletoutputstream类)
* [实例：包含Web资源——RequestDispatcher.include方法](#实例：包含Web资源——requestdispatcher.include方法)
* [实例：转发Web资源——RequestDispatcher.forward方法](#实例：转发Web资源——requestdispatcher.forward方法)
* [三、掌握HttpServletResponse类](#三掌握httpservletresponse类)
* [四、掌握HttpServletRequest类](#四掌握httpservletrequest类)
* [获取请求行消息](#获取请求行消息)
* [获取网络连接消息](#获取网络连接消息)
* [获取请求头消息](#获取请求头消息)
* [五、处理Cookie](#五处理cookie)
* [认识操作Cookie的方法](#认识操作cookie的方法)
* [实例：通过Cookie技术读写客户端信息](#实例：通过cookie技术读写客户端信息)
* [实例：通过Cookie技术读写复杂数据](#实例：通过cookie技术读写复杂数据)
* [六、处理Session](#六处理session)
* [认识操作Session的方法](#认识操作session的方法)
* [创建Session对象](#创建session对象)
* [实例:通过Cookie跟踪Session](#实例:通过cookie跟踪session)
* [实例:通过重写URL跟踪Session](#实例:通过重写url跟踪session)
* [七、解决Web开发中的乱码问题](#七解决web开发中的乱码问题)
<!-- GFM-TOC -->


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
为了实现代码重用，需要将某些公用的代码和数据放到一个或多个Servlet中，以供其他的Servlet使用。RequestDispatcher.include方法。首先通过getServletContext方法获取ServletContext对象，然后通过ServletContext.getRequestDispatcher方法获取RequestDispatcher对象。
```java      //包含了一个Servlet:IncludedServlet，和一个html页面
public class IncludingServlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置Context-Type字段值，即类型为text/html，编码格式为gb2312
        response.setContendType("text/html;charset=gb2313");
        PrintWriter out = response.getWriter();
        out.println("中国<br/>");
        //向客户端输出请求URI
        out.println("IncludingServlet URI:" + request.getRequestURI() + "<p/>");
        //封装名为IncludedServlet的servlet
        RequestDiapatcher rd = this.getServletContext().getRequestDispatcher("/servlet/IncludedServlet");
        rd.include(request,response);      //包含IncludedServlet
        //封装IncludedHtml.html页面
        rd = getServletContext().getRequestDispatcher("/chapter4/IncludedHtml.html");
        rd.include(request,response);  //包含incluedHtml.html页面
    }
}
```
上面的程序包含了两个web资源，映射路径为"/servlet/IncludedServlet"的Servlet类和名为IncldedHtml.html的静态页面。

getRequestDispatcher方法的参数值必须以“/”开头。
```java
public class IncludedServlet extends HttpServlet{
    @Ovrride
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置Context-Type字段值，即类型为text/plain，编码格式为utf-8
        response.setContendType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<b>超人</b><br/>");  //输出带HTML代码的信息
        out.println("IncludedServlet URI:"+request.getRequestURI()+"<p/>");//向客户端输出请求URI
    }
}
```
(调用include方法就相当于Servlet引擎在IncludingServlet的service方法又调用了IncludedServlet的service方法，并将IncludingServlet的service方法的两个参数传入IncludedServlet的service方法，因此才会得到同样的URI值)
(在被调用者中设置的响应消息头将被忽略)

## 实例：转发Web资源——RequestDispatcher.forward方法
RequestDispatcher类的forward方法可以转发Web资源，forward方法与include方法有5点不同:

(1)在调用forward方法之前，输出缓冲区的数据会被清空。

(2)调用forward方法之前，已经将缓冲区中的数据发送到客户端，在调用forward方法时会抛出IllegalStateException异常。

(3)在调用者和被调用者中设置响应消息头都不会被忽略，而调用include方法时，只有在调用者中设置响应消息头才会生效。

(4)Servlet引擎会根据RequestDispatcher对象所包含的资源对HttpServletRequest对象中的请求路径和参数消息进行调整；而使用include方法时Servlet引擎并不调整这些消息。

(5）froward方法只能使用一次，否则会抛出IllegalStateException异常，而include方法可以多次使用。

```java
public class ForwardServlet extends HttpServlet{
    @Ovrride
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //RequestDispatcher对象封装的资源路径前必须加“/"
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/servlet/IncludingServlet");
        rd.forward(request,response); //转发到IncludingServlet
    }
}
```
在被转入的web资源（IncludingServlet）中改变了HttpServletRequest中的请求路径，因此会输出/webdemo/servlet/IncludingServlet的请求路径。

# 三、掌握HttpServletResponse类
HTTP协议的状态响应码
```java
public class ExploreResponseHeader extends HttpServlet{
    //处理客户端的GET请求
    public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置Context-Type字段的值
        response.setContextType("text/html;charset=UTF-8");
        response.setHeader("Context-Length","1234");
        //加一个NewField1字段
        response.addHeader("NewField1","value1");
        //获得Calendar对象
        Calendar cal = Calendar.getInstance();
        cal.set(2008,11,25); //月从0~11,1月用0表示
        //加一个MyDate字段
        response.addDateHeader("MyDate",cal.getTimeInMillis());
        //加一个NewField2字段
        response.setIntHeader("NewField2",3000;
    }
}
```
(1)添加和设置响应消息头——addHeader与setHeader方法

addHeader和setHeader方法用于设置HTTP响应消息头的所有字段，定义如下;
```java
public void addHeader(String name,String value);
public void setHeader(String name,String value);
```
name参数表示响应消息头的字段名，value表示响应消息头的字段值，这两个方法都向响应消息头增加一个字段。

（2）操作整数类型的响应消息头——addIntHeader与setIntHeader方法

HttpServletResponse提供两个专门设置整型字段值的方法，定义如下;
```java
public void addIntHeader(String name,int value);
public void setIntHeader(String name,int value);
```
这两个方法在设置整型字段值时避免将int类型转换为String类型值的麻烦。

（3）操作时间类型的响应消息头——addHeader与setDateHeader方法

HttpServletResponse提供了两个专门用于设置日期字段值的方法，定义如下：
```java
public void addDateHeader(String name,long date);
public void setDateHeader(String name,long date);
```
# 四、掌握HttpServletRequest类
在客户端请求某一个Servlet时，Servlet引擎为这个Servlet创建一个HttpServletRequest对象来存储客户端的请求消息，并在调用service方法时将HttpServletRequest对象作为参数传给了service方法。
## 获取请求行消息
HttpServletRequest接口中定义了若干的方法来获取请求行中各部分的消息。
## 获取网络连接消息
要运行Java Web消息，首先需要在服务端部署Java Web程序，然后才通过客户端连接服务器进行请求；在具体编写程序时，有时需要获取客户端与服务端网络连接的消息。
## 获取请求头消息
跟HttpServletResponse相似，类HttpServletRequest也定义了许多方法来操作请求消息中的请求头。HttpServletRequest接口中也定义了一些用于获得请求头消息的方法。
# 五、处理Cookie
Cookie是在浏览器访问某个Web资源时，由Web服务器在HTTP响应消息头中通过Set-Cookie字段发送给浏览器的一组消息。
一个cookie只能表示一个key-value对，由Cookie名和Cookie值组成。
## 认识操作Cookie的方法
在Servlet API中，使用java.servlet.http.Cookie类来封装一个Cookie消息；在HttpServletResponse接口中定义了一个addCookie方法来向浏览器发送Cookie消息，也定义了一个getCookies方法来读取浏览器传递过来的Cookie消息。Cookie类中定义了生成和获取Cookie消息的各个属性的方法。
Cookie类只有一个构造方法：public Cookie(String name,String vlaue)

Cookie中其他的常用方法：
getName方法：返回Cookie的名称；
setValue和getValue方法：设置和返回Cookie的值；
setMaxAge和getMaxAge方法：设置和返回Cookie在客户机的有效时间；
setPath和getPath方法：设置和返回当前Cookie的有效web路径；
setDomain和getDomain方法：设置和返回当前Cookie的有效域；
setComment和getComment方法：设置和返回当前Cookie的注释部分；
setVersion和getVersion方法：设置和返回当前Cookie的协议版本；
setSecure和getSecure方法;设置和返回当前Cookie是否只能使用安全的协议传输Cookie.
## 实例：通过Cookie技术读写客户端信息
```java       //SaveCookie类负责向客户端写入三种Cookie：永久Cookie、临时Cookie和有效时间为0的Cookie.
public class SaveCookie extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置Context-Type字段值
        response.setContendType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter(); //获取输出对象out
        Cookie tempCookie = new Cookie("temp","87654321");
        response.addCookie(tempCookie); //添加临时Cookie对象
        //设置MaxAge为0的Cookie
        Cookie cookie = new Cookie("cookie","6666");
        //建立超过时间为0的Cookie
        //MaxAge设为0，浏览器接收到Cookie后，Cookie立即被删除
        cookie.setMaxAge(0);
        response.addCookie(cookie); //添加超时时间为0的Cookie对象
        //获得请求参数user的值
        String user = request.getParameter(“user”);
        //如果请求url含有user参数，创建这个永久Cookie
        if(user!=null){
            //建立永久的Cookie对象
            Cookie userCookie = new Cookie("user",user);
            userCookie.setMaxAge(60*60*24);      //将MaxAge设为 1天
            //这个Cookie对站点中所有的目录下的访问路径都有效
            userCookie.setPath("/");
            response.addCookie(userCookie);     //添加永久Cookie对象
        }
        //转发到ReadCookie，并读出已经保存的Cookie值
        RequestDispatcher readCookie = getServletContext().getRequestDispatcher("/servlet/ReadCookie");
        readCookie.include(request,response);      //开始转变
    }
}
```
使用Cookie对象要经过三个步骤：首先通过Cookie类的构造函数创建一个包含保存信息的Cookie对象，然后通过setMaxAge方法设置Cookie对象在客户端的保存时间和通过setPath方法设置客户端访问什么路径传递Cookie对象，最后通过addCookie方法将Cookile对象传递给客户端。
```java        //在SaveCookie中使用了一个ReadCookie类，这个类负责读取被保存的Cookie值
public class ReadCookie extends HttpServlet{
    protected Cookie getCookieValue(Cookie[] cookie,String name){
        //如果有写入的Cookie，继续下面的操作
        if(cookie!=null){
            //在Cookie数组中查找指定的Cookie对象
            for(Cookie c:cookies){
                if(c.getName().equals(name))
                    return c;            //返回查到的Cookie对象
            }
        }
        return null;
    }
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置Context-Type字段值
        response.setContendType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter(); //获取输出对象out
        //调用getCookieValue方法获得临时Cookie,getCookies方法获得一个保存了消息头中所有的Cookie的数组
        Cookie tempCookie = getCookieValue(request.getCookie(),"temp");
        if(tempCookie!=null)
            out.println("临时Cookie值："+tempCookie.getValue()+"<br/>");
        else
            out.println("临时Cookie为设置<br/>");
        //这个Cookie永远不可能获得，因为它的MaxAge为0
        Cookie cookie = getCookieValue(request.getCookies(),"cookie");
        if(cookie!=null)
            out.println("cookie:"+cookie.getValue()+"<br/>");
        else
            out.println("cookie已经被删除了<br/>);
        //获得永久Coolie
        Cookie userCookie = getCookieValue(request.getCookies(),"user");
        if(userCookie!=null)
            out.println("user:"+userCookie.getValue());
        else
            out.println(“user未设置”);
    }
}
```
通过HttpServletRequest只能获得包含所有的Cookie的数组，要想获得某一个指定的Cookie，就必须在这个数组中查找。

## 实例：通过Cookie技术读写复杂数据
在Cookie中只能保存ISO-8859-1编码支持的字符，在Cookie中保存更复杂的数据，就必须对其进行编码，一般将复杂的数据以Base64格式进行编码。
演示如何在Cookie中保存一个被序列化的对象实例，并再次从Cookie中将其读出；SaveComplexCookie类负责将MyCookie类的对象实例写到Cookie中
```java
public void SaveComplexCookie extends HttpServlet{
    @Override
     protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
         //创建一个用于进行Base64编码的BASE64Encoder对象
         sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
         //创建一个用于接收被序列化的对象实例字节流的ByteArrayOutputStream对象
         ByteArrayOutputStream classBytes = new ByetArrayOutputStream();
         //创建一个用于向流中写入对象的ObjectOutputStream对象
         ObjectOutputStream oos = new ObjectOutputStream(classBytes);
         oos.writeObject(new MyCookie());      //写入MyCookie对象实例
         oos.close();     //关闭ObjectOutputStream对象
         //将被序列化的对象实例的字节流按Base64编码格式进行编码
         String classStr = bsee64Encoder.endcode(classBytes.toByteArray());
         Cookie cookie = new Cookie("mycookie",classStr);
         //将Base64编码写入Cookie
         cookie.setMaxAge(60*60*24）；       //Cookie的有效时间为1天
         response.addCookie(cookie);
         response.setContendType("text/html;charset=utf-8");
         PrintWriter out = response.getWriter();
         //输出提示消息
         out.println("MyCookie的对象实例已写入Cookie");
     }
}
```
首先获取类MyCookie的字节码数组，然后通过Base64编码将字节码解析成字符串classStr，最后通过Cookie将字符串classStr保存到客户端。
```java       //实现序列化的类MyCookie
public class MyCookie implements java.io.Serializable{
    //获取字符串
    public String getMsg(){
        retrun "MyCookie对象实例从Cookie获得的";
    }
}
```
MyCookie类需要被序列化，因此它必须实现java.io.Serizable接口，否则会抛出java.io.NotSerializableException异常;ReadComplexCookie类负责从Cookie中读取MyCookie的对象实例，并调用其中的getMsg方法.
```java          //继承类ReadCookie
public class ReadComplexCookie extends ReadCookie{
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
         try{
             //创建一个用于进行Base64编码的BASE64Encoder对象
             sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
             //通过调用方法getCookieValue获得名为mycookie的cookie
             Cookie cookie = getCookieValue(request.getCookies(),"mycookie");
             if(cookie==null)
                 return ;
             //取mycookir的值，也就是被序列化的MyCookie的类
             String classStr = cookie.getValue();
             //对classStr字符串进行Base64解码
             byte[] classBytes = base64Decoder.decoderBuffer(classStr);
             //根据序列化的字节流创建ObjectInputStream对象
             ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(classBytes));
             //得到MyCookie对象实例
             MyCookie myCookie  = (MyCookie)ois.readObject();
             response.setContentTyep("text/html;charset=utf-8");
             PrintWriter out = response.getWriter();
             out.println(myCookie.getMsg());         //调用getMsg方法
         }catch(Exception e){
         
         }
}
```
在ReadComplexCookie中使用了getCookieValue方法，因此这个类需要从ReadCookie类继承。

# 六、处理Session
## 认识操作Session的方法
一种将大量数据保存在服务端的技术，并使用SessonID对这些数据进行跟踪，就是Session技术。
在Servlet中使用HttpSession类来描述Sessin，一个HttpSession对象就是一个Session，使用HttpServletRquest接口的getSession方法可以获得一个HttpSession对象。


## 创建Session对象
一个请求只能属于一个Session，但一个Session也可以拥有多个请求。HttpServletRequest接口中定义了一些与Session相关的方法，其中getSessin()方法时HttpServletRequest接口的方法。这个方法用于返回与当前请求相关的HttpSession对象，该方法有两种重载形式：
```java
public HttpSession getSession();
public HttpSession getSession(boolean create);
```
调用第一种重载形式，如果在请求消息中含有SesionID,就根据这个SessionID返回一个HttpSession对象，如果在请求消息中不包含SessionID，就创建一个新的HttpSession对象，并返回它；在调用第二种重载形式时，如果create参数为true时,与第一种重载形式完全一样；如果create为false时，当请求消息中不包含SessionID时，并不创建一个新的HttpSession对象，而是直接返回null.
(方法getSession()或方法getSession(true)用来实现创建Session对象，而方法getSession(false)用来实现获取Session对象。）

## 实例：通过Cookie跟踪Session
客户端必须通过一个SessionID才能找到以前在服务端创建的某一个HttpSession对象，通过SessionID寻找HttpSession对象的过程也叫做Session跟踪。
通过客户端的SessionID通过HTTP请求消息头的Cookie字段发送给服务端，然后服务端通过getSession方法读取Cookie字段的值，已确定是需要新建一个HttpSession对象，还是获得一个已经存在的HttpSession对象，或者什么都不做，直接返回null.
```java
public class SessionServlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置Context-Type字段值
        response.setContendType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter(); //获取输出对象out
        //获取一个HttpSession对象
        HttpSession session = request.getSession();
        //设置Session的有效间隔为1天
        session.setMaxInactiveInterval(60*60*24);
        //如果是新建立的Session对象，保存属性值
        if(session.isNew()){
            //向Session中写入一个值
            session.setAttribute("session","宇宙");
            out.pirntln(“新会话已经建立”);  //输出提示信息
        }else{
            //如果是以前建立的会话，输出这个属性值
            out.println("会话属性值"+session.getAttribute("session"));
        }
}
```
## 实例:通过重写URL跟踪Session
通过URL发送SessionID,必须要重写URL;HttpServletResponse提供了如下两个方法用于重写URL：
```java
encodeURL方法;用于对所有内嵌在Servlet中的URL进行重写。
encodeRedirectURL方法：用于对sendRedirect方法所使用的URL进行重写。
```
如果HTTP请求消息头中没有Cookie字段，或是Cookie字段中没有名为JSESSIONID的Cookie，这两个方法就会在调用getSession获得SessinID后，将这个SessionID写到当前请求的URL后面。
```java
public class NewSessionServlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        //设置Context-Type字段值
        response.setContendType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter(); //获取输出对象out
        //获得指向SessionServlet的RequestDispatche对象
        RequestDiapatcher sessionServlet = getServletContext().getRequestDispatcher("/servlet/SessionServlet");
        sessionServlet.include(request,response);
        //开始包含SessionServlet
        //向客户端输出被重写的URL
        out.println("<br><a href='"+response.encodeURL("SessionServlet")+"'>SessionServlet</a>");
    }
}
```

# 七、解决Web开发中的乱码问题
开发人员使用了不支持中文或是错误的编码格式来对中文进行编码或解码，就会遇到乱码问题。
Java内部使用UCS2编码，是一种编码格式的世界语。国内一般采用UTF-8，不管采用哪种编码格式，Java在编译源代码时，都会将其转换成UCS2；
在编程中英文会远多于中文，所以用UTF-8编码格式对字符进行编码是比较合适的。
