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


# 四、JSP指令

# 五、JSP内置对象


# 六、JSP标签






# 七、JSP表征标签库
