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
Tomcat自带一个Servlet
```java
<!--定义Servlet本身的 属性-->
<servlet>
    <servlet-name>jsp</servlet-name>
    <!--处理JSP页面的Servlet类-->
    <servlet-class>org.apache.jasper.servlet.JspServlet</servlet-class>
    ...
    <load-on-startup>3</load-on-startup>
</servlet>
```
从代码中发现，这个servlet实际上是一个专门处理JSP程序的Servlet类，类名为JSPServlet。
（所以扩展名为jsp的URL都会交给JSPServlet类处理，也就是交由JSP引擎进行处理）
JSP引擎分两步对JSP页面进行处理；首先将JSP页面生成一个Servlet源程序文件，然后将调用Java编译器将这个Servlet源程序文件编译成.class文件，并由Servlet引擎装载并执行这个.class文件。
（为了提高JSP的执行效率，并不是每次访问JSP都进行编译，只有在第一次访问JSP页面时才会执行这个翻译过程）
JSP页面从运行原理上看是在运行Servlet程序，所以习惯上将同时执行JSP和Servlet的程序称为Servlet容器。
虽然每次访问JSP页面时，JSP引擎都会检查JSP页面和由JSP页面翻译而成的Servlet源文件的生成时间，由于在发布时每次访问JSP页面都要做这些工作导致降低系统性能，因此一般在发布JSP程序时将检查生成时间的功能关闭。
```java
<servlet>
    <servlet-name>jsp</servlet-name>
    <servlet-class>org.apache.jasper.servlet.JspServlet</servlet-class>
    <!--将development参数值设为false，Servlet就不会再编译JSP程序了-->
    <init-param>
        <!--关闭检查生成时间的功能-->
        <param-name>development</param-name>
        <param-name>false</param-value>
    </init-parm>
</servlet>
```
## 分析由JSP生成的Servlet代码
JSP在第一次运行时被翻译为Servlet的源代码，然后又被翻译成.class文件，最终Servlet引擎装载并运行了这个.class文件来完成JSP的第一次运行。
```java
<%@ page language="java" contendType="text/html;charset=utf-8"
pageEncoding="UTF-8"%>
<!--导入相应包-->
<%@page import="java.util.Random"%>
<!--java代码-->
<%
    Random rand = new Random();
    int n = rand.nextInt(1000); //产生第一个0~1000的随机数
    int m = rand.nextInt(1000); //产生第二个
%>
<html>
    <head>
    <head>random</title>
    </head>
    <body>
    第一个随机数：<% out.println(n);%><!--输出第一个随机数-->
    <br>
    <input type="button" value="单击第二个随机数" onclick="javascript:alert('<%=m%>')"/>
    </body>
</html>
```
## JSP静态部分的转换
JSP的静态部分就是在客户端运行的代码，如HTML、JavaScript等，这些静态的内容在转换时都作为字符串，并通过write()方法按原样输出到客户端。
## JSP动态部分的转换
JSP动态部分就是被括在<%..%>中的内容分为三种形式：

(1)<%..%>形式：将<%..%>中的内容按原样插入到由JSP生成的Servlet源代码中。

(2)<%=...%>：通过print方法将“=”后面的内容输出到客户端。

(3)JSP指令：<%@page ...%>就是一个JSP的page指令。按照指令类型和它的属性翻译成相应的Java代码。



注意：静态内容使用write方法输出到客户端；动态内容使用print方法向客户端输出。区别是:write方法只能输出字符串、字符数组和int类型的数据；而print方法可以将任何类型的数据转换为字符串输出。

# 三、JSP基本语法
## 学习JSP表达式
JSP表达式可将java表达式返回的值发送到客户端；JSP表达式的语法
```java
<%=Java表达式%>
<%=...%>中的java表达式就相当于out.print(..)中print方法中的参数。
```
也就是说<%=new java.util.Random().nextInt(1000)%>就相当于在Servlet中输入如下的语句：
```java
out.print(new java.util.Random().nextInt(1000));
```
注意：<%=...%>中的java表达式后面不能加分号；
## 实现在JSP中嵌入代码
JSP表达式可以在JSP页面中嵌入java代码，但所嵌入的java代码只不过是print方法的一个参数，其实也就是一个值而已；另外一种可以在JSP页面中嵌入一切合法Java代码的方式，即java代码放在<%..%>语法中。
```java
<!--jspjava.jsp-->
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>在JSP中嵌入java代码</title>
    </head>
    <body>
        <!--java代码-->
        <%
            //定义一个Random对象
            java.util.Random rand = new java.util.Random();
            //循环生成10个随机数，新输出到客户端
            for(int i=0;i<10;i=+）{
                out.print(rand.nextInt(1000)); //输出1000以内的随机整数
                out.print("<br>");
            }
        %>
    </body>
</html>
```
(1)<% ..%>中的每条语句后面必须使用分号(;)作为结束标记；

(2)一个JSP页面可以在任意位置使用<%..%>插入java代码，<%..%>可以有任意多个；

(3)每一个<%..%>中的内容和JSP页面中的一个或多个<%..%>中的内容组合起来必须完整（甚至可以将一条java语句分别写在多个<%..%>中）；

## JSP表达式语言（EL）

# 四、JSP指令


# 五、JSP内置对象
## 内置对象out
out对象是JSP中最常用的内置对象，这个对象用于向客户端输出文本形式的数据。通过out对象向客户端输出数据一般需要经过两个缓冲区(JSPWriter对象提供的缓冲区和Servlet引擎提供的缓冲区）。
```java
<!--将JSP的缓冲区设为1kb-->
<%@ page buffer="1kb" import="java.io.*" contendType="text/html" pageEncoding="UTF-8"%>
<%
    out.clear(); //清除out对象缓冲区中的内容
    //根据请求参数count的值生成“1”，以使其占满缓冲区
    int count = Integer.parseInt(request.getParameter("count"));
    for(int i=0;i<count;i++){
        out.write("1");        //通过JspWrier对象将数据输出到客户端
    }
    //通过PrintWrier对象将数据输出到客户端
    PrintWriter servletOut = pageContext.getResponse().getWriter();
    servletOut.write("使用PrintWriter对象输出数据");
%>
```
## 内置对象pageContext
pageContext对象时javax.servlet.jsp.PageContext类的对象实例，pageContext对象封装了JSP页面的运行信息。通过这个对象的getXXX方法来获得其他8个JSP内置对象。
```java
abstract public Exception getException();        //返回exception内置低下
abstract public Object getPage();                //返回page内置对象
abstract public ServletRequest getRequest();     //返回request内置对象
abstract public ServletResponse getResponse();   //返回response内置对象
abstract public ServletConfig getServletConfig();//返回config内置对象
abstract public ServletContext getServletContext();//返回application内置对象
abstract public HttpSession getSession();        //返回session内置对象
abstract public JspWriter getOut();             //返回out内置对象
```
pageContext对象可以获得其他8个内置对象，当JSP页面调用普通的java类时，可只将pageContext对象作为参数传入相应的方法，这样在方法中就可以通过pageContext对象获取和操作其他的JSP内置对象了。
在pageContext类中引入了一个include方法来简化和替代RequestDispatcher.include方法；PageContext类的include方法有两种重载形式：
```java
abstract public void include(String relativeUrlPath)throws ServletException,IOException;
abstract public void include(String relativeUrlPath,boolean flush)throws ServletException,IOException;
```
PageContext类还提供了一个forward方法来简化和替代RequestDispatcher.forward方法的调用，定义如下：
```java
abstract public void forward(String relativeUrlPath)thorows ServletException,IOException;
```
## 其他内置对象
### request/response/session对象
这三个对象分别由PageContext类的getRequest/getResponse/getSession方法返回。
### page对象
page对象表示当前JSP页面所对应的Servlet类的对象实例
```java
<!--testPage.jsp-->
<%
    //输出Servlet的类名
    out.println(page.getClass().getName());
%>
```
### exception对象
只有page指令的isErrorPage属性值为true时，才会创建exception对象。
### config对象
从config对象中可以获得web.xml文件中与当前JSP页面相关的配置信息，如初始化参数和Servlet等。config对象由PageContext类的getServletConfig方法返回。
```java
<!--testConfig.jsp-->
<%@page contextType="text/html;charset=UTF-8"%>
Servelt名：
<!--输出Servlet类名-->
<%=config.getServletName()%>
<br>
初始化参数值：
<!--输出初始化参数值-->
<%=config.getInitParameter("jsparg")%>
```
web.xml文件的<web.app>标签中加入如下的子标签
```java
<servlet>
    <servlet-name>testConfigServletName</servelt-name>
    <jsp-file>/chapter5/testConfig.jsp</jsp-file>
    <!--设置初始化参数-->
    <init-param>
        <!--初始化参数名-->
        <param-name>jsparg</param-name>
        <!--初始化参数值-->
        <param-value>jspargvalue</param-vlaue>
    </init-parm>
</servlet>
<servlet-mapping>
    <servlet-name>testConfigServletName</servlet-name>
    <url-pattern>/chapter5/testConfig.jsp</url-pattern>
</servlet-mapping>
```
## application对象
application对象用于获得和当前web应用程序相关的信息。这个对象由PageContext类的getServletContext方法返回。application对象可用于获得全局的初始化参数，某个Web资源的绝对路径、Servlet引擎的版本号的等信息，也可以用于保存和获得全局的对象（使用setAttribute方法和getAttribute方法）
```java       //testApplication.jsp页面演示了appliction对象的使用
<%@ page contextType="text/html;charset=UTF-8"%>
全局初始化参数值：
<!--获取初始化参数globalArg的值-->
<%=application.getInitparameter("globalarg")%>
<br>
testAppliction.jsp的绝对路径
<!--获取路径-->
<%=application.getRealPath("testApplication.jsp"）%>
<br>
Servlet的版本号：
<!--获取Servlet版本号-->
<%=application.getMajorVersion()+"."+application.getMinorVersion()%>
<br>
web工程路径：
<!--获取应用路径-->
<%=application.getContextPath()%>
```
在web.xml的<web-app>节点中加入如下的子节点：
```java
<!--配置全局初始化参数globalArg-->
<context-param>
    <param-name>globalArg</param-name>
    <param-value>globalValue</param-value>
</context-param>
```
# 六、JSP标签






# 七、JSP表征标签库
