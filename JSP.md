<!-- GFM-TOC -->
* [一、通过MyEclipse工具编写一个JSP程序](#一通过myeclipse工具编写一个jsp程序)
* [二、JSP运行原理](#二jsp运行原理)
    * [分析由JSP生成的Servlet代码](#分析由jsp生成的servlet代码)
    * [JSP静态部分的转换](#jsp静态部分的转换)
    * [JSP动态部分的转换](#jsp动态部分的转换)
* [三、JSP基本语法](#三jsp基本语法)
    * [学习JSP表达式](#学习jsp表达式)
    * [实现在JSP中嵌入代码](#实现在jsp中嵌入代码)
    * [JSP表达式语言（EL）](#jsp表达式语言（el）)
* [四、JSP指令](#四jsp指令)
    * [JSP指令](#jsp指令)
    * [JSP指令page](#jsp指令page)
    * [JSP指令include](#jsp指令include)
* [五、JSP内置对象](#五jsp内置对象)
    * [内置对象out](#内置对象out)
    * [内置对象pageContext](#内置对象pagecontext)
    * [其他内置对象](#其他内置对象)
    * [application对象](#application对象)
* [六、JSP标签](#六jsp标签)
    * [包含标签<jsp：include>](#包含标签<jsp：include>)
    * [web资源的扩展名](#web资源的扩展名)
    * [处理不存在的web资源](#处理不存在的web资源)
    * [转发标签<jsp：forward>](#转发标签<jsp：forward>)
    * [传参标签<jsp：param>](#传参标签<jsp：param>)
    * [创建Bean标签<jsp：useBean>](#创建bean标签<jsp：usebean>)
    * [设置属性值标签<jsp：setProperty>](#设置属性值标签<jsp：setproperty>)
    * [获取属性值标签<jsp：getProperty>](#获取属性值标签<jsp：getproperty>)
* [七、JSP表征标签库](#七jsp表征标签库)
<!-- GFM-TOC -->
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
    <title>random</title>
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
            for(int i=0;i<10;i++）{
                out.print(rand.nextInt(1000)); //输出1000以内的随机整数
                out.print("<br>");
            }
        %>
    </body>
</html>
```
(1)<% ..%>中的每条语句后面必须使用分号(;)作为结束标记；

(2)一个JSP页面可以在任意位置使用<%..%>插入java代码，<%..%>可以有任意多个；

(3)每一个<%..%>中的内容和JSP页面中的一个或多个<%..%>中的内容组合起来必须完整（甚至可以将一条java语句分别写在多个<%..%>中）;

## JSP表达式语言（EL）

# 四、JSP指令
## JSP指令
JSP指令被引擎翻译成java代码；JSP指令只是设置Servlet引擎如何处理JSP页面，或是向Servlet源代码中添加一些其他的JSP元素无法生成的java代码。
JSP指令的语法格式如下：
```java
<%@ 指令 属性名=“值” %>
```
```java
下面两条page指令分别设置了contentTyep和pageEncoing属性。
<%@page contentTyep="text/html"%>
<%@page pageEncoding="UTF-8" %>
```
## JSP指令page
page指令用于设置JSP页面的全局属性。通常将page指令放到JSP页面的起始位置。
```java
<%@ page
[language="java"][extends="package.class"]
[import="{package.class|package.8}，..."]
[session="ture|false"]
[buffer="none|8kb|sizekb"][autoFlush="true|false"]
[isThreadSafe="true|false"][info="text"]
[errorPage="relativeURL"][isErrorPage="true|false"]
[contendType={mimeType[;charset=characterSet]|text/html;charset=ISO-8859-1}"]
[pageEncoding="{characterSet|ISO-8859-1}"]
[isELIgnored="true|false"]
```
每队方括号（[]）分别表示page指令的一个属性，属性值中用竖杠（|）分割的不同部分为该属性可以设置的值；import属性是唯一允许出现多次的属性。

### language属性
language属性设置JSP页面所使用的开发语言。
### extends属性
extends属性设置了由JSP页面生成的Servlet类所继承的父类。
### import属性
import属性指定在JSP页面被翻译成Servlet源代码后要导入的包或类。
```java
<%@ page import="java.util.*" import="java.io.*" import="java.util.Random"%>
```
还可以在一个import属性中使用逗号，分割不同的包或类。
```java
<%@ page import="java.util.*,java.io.*,java.util.Random"%>
```
### session属性
session属性用于指定在JSP页面中是否创建内置session对象。
当session属性设为false后，即使在JSP页面中使用getSession方法也只能获得一个空的HttpSession对象，如下所示：
```java
<%@page session="false" contendType="text/html"%>
<%
    HttpSession newSession = pageContext.getSession();//newSession为null
    System.out.println(newSession);     //在控制台输出null
%>
```
如果某个JSP页面不需要对客户端进行跟踪时，最好将session属性的值设为false，以避免过多的创建HttpSession对象。
### buffer属性
buffer属性用于设置JSP的内置对象out的缓冲区大小，默认值是8kb；如果将buffer设为none，out对象则不适用缓冲区。
### autoFlush属性
autoFlush属性用于设置当内置对象out的缓冲区已满时，是将缓冲区中的内容刷新到客户端（autoFlush的属性值为true），还是抛出缓冲区溢出的异常（autoFlush属性值为false)，该属性的默认值是true.
autoFlush属性的值实际上就是getPageContext方法的最后一个参数值。
### isTreadSafe属性
isThreadSafe属性用于设置JSP页面是线程安全的，默认值为true.
### info属性
info属性用于定义一个用于描述当前JSP页面的字符串信息。
```java
<%@ page info="输出info属性的值" contendType="text/html" pageEncoding="utf-8"%>
<%--输出info属性的值--%>
info属性的值：<%=getServletInfo()%>
```
### errorPage属性
errorPage属性用于设置如何处理JSP页面抛出的异常。如果JSP页面抛出了未被捕获的异常，就会自动跳转到errorPage属性所指定的页面。
### isErrorPage属性
isErrorPage属性用来指定当前JSP页面是否可用于处理其他JSP页面未捕获的异常，默认值为false；errorPage属性所指的异常处理页面必须将exception对象属性设为true;否则无法在异常处理页中引用内置对象exception；因此exception对象只有在isErrorPage属性设为true时才存在。
```java
<!--testError.jsp-->
<%@page errorPage="error.jsp" contentType="text/html" pageEncoding="UTF-8"%>
<%
    java.util.Random rand=null;
    out.println(rand.nextInt());  //抛出一个NullPointerException异常
%>
下面的error.jsp页面负责处理由testError.jsp抛出的异常
<!--error.jsp-->
<%@ page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8"%>
<%
    out.println("错误跟踪信息:<br>");
    //当isErrorPage的值为false时，无法使用exception对象
    exception.printStackTrace(new java.io.PrintWriter(out));
%>
```




### contentType属性
contentType属性用于设置响应正文的MIME类型和JSP页面中文字符集编码；contentType属性的默认MIME属性是text/html，默认字符集是ISO-8859-1;
```java
<!--设置编码格式维UTF-8，以支持中文-->
<%@page errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!--设置编码格式为GBK，以支持中文-->
<%@page errorPage="error.jsp" contentType="text/html;charset=GBK"%>
```
### pageEncoding属性
pageEncoding属性用于指定JSP页面中文内容的字符集编码格式；如果指定了pageEncoding属性，contentType属性中的charset就不具有指定JSP页面中文内容的字符集编码的作用了。
### isELignored属性
isELignored属性用于设置JSP页面是否支持EL。isELIgnored属性的默认值是false.

## JSP指令include
include指令用于将其他的文件静态的加入到当前的JSP页面中，语法格式如下：
```java
<%@ include file="relativeURL"%>
```
如果在当前的JSP页面中未通过page指令的contendType属性设置字符集编码，则在被引入的JSP程序中通过page指令设置的pageEncoding属性值将作为当前JSP页面内容的编码格式。
```java
<%@page pageEncoding="UTF-8"%>
<!--包含文件-->
<%@include file="included.myjsp"%>
小于10000的随机数:<%=new java.util.Random().nextInt(10000)%>

下面来编写included.myjsp页面
<%@ page import="java.util.*" contentType="text/html;charset=UTF-8"%>
included.myjsp中的内容<br>
```

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
在JSP规范中定义了一些标准的标签，这些标签也被称为标准动作，使用了XML格式进行描述。
## 包含标签<jsp：include>
<jsp:inclde>标签将另一个静态或动态的资源插入到当前的JSP页面中；语法格式如下：
```java
//第一种用法
<jsp:include page="relativeURL" flush="true|false"/>
//第二种用法
<jsp:include page="relativeURL" flush="true|false>
{<jsp:param...../>}*
</jsp:include>
```
page属性表示一个相对于当前页的相对路径；flush属性表示在插入资源之前是否清空out缓冲区，默认值是false;第二种语法格式可以向relativeURL所指的web资源传递参数。
### 引入资源的方式
include指令是静态引入web资源的；include方法和<jsp:include>标签是动态引入web资源的。
### HTTP响应头的改变
include指令可以改变响应状态码和HTTP响应头。
### web资源的路径
include方法和<jsp:include>标签的相对路径都是相对于页面的，而include指令的相对路径是相对于文件的。
```java
<%@ page pageEncoding="UTF-8">
include指令
<%@include file="included.jsp"%>
<br>
include标签
<jsp:include page="include.jsp"/>
<br>
include方法：
<% 
    pageContext.inclde("included.jsp");
%>
```
上面的三个URL都执行成功，如在web.xml中加入下面的配置代码，就将compareInclude.jsp页面映射到新的web路径。
```java
<!--定义JSP本身的属性-->
<servlet>
    <servlet-name>ComprareInclude</servlet-name>
    <jsp-file>/chapter5/compareInclude.jsp</jsp-file>
</servlet>
<!--定义JSP映射信息-->
<servlet-mapping>
    <servlet-name>CompareInclude</servlet-name>
    <url-pattern>/myspace/compareInclde.jsp</url-pattern>
</servlet-mapping>
```
include指令的相对路径是文件的相对路径；而include标签和include方法的相对路径是相对于web路径的。
## web资源的扩展名
include指令在引用JSP文件时，按照JSP页面来处理，而include方法和<jsp:include>标签所引用的JSP文件的扩展名必须是.jsp才可以。
## 处理不存在的web资源
当相对路径所指的web资源不存在时，include指令抛出异常，而include方法和<jsp:include>标签会向客户端输出一条提示信息后，继续执行后面的JSP代码。

## 转发标签<jsp：forward>
实现转发的标签——<jsp:forward>，主要用于将当前请求转发给其他的静态资源、JSP页面或Servlet，语法格式如下：
```java
格式一
<jsp:forward page="relativeURL|<%=expression %>"/>
格式二
<jsp:forward page="relativeURL|<%=expression %>">
{<jsp:param.../>}*
</jsp:forward>
```
<jsp:forward>标签的page属性不仅可以是相对路径，而且可以是JSP表达式返回的值；在使用<jsp:forward>标签时应该注意以下几点：

（1）当前JSP页面的out缓冲区中有数据，在forward之前会清空out缓冲区；

（2）在调用<jsp:forward>之前，out缓冲区已经被刷新，当调用<jsp:forward>时会抛出java.in.IOException异常；

（3）当<jsp:forward>标签前面的输出字符数量大于缓冲区的尺寸时，系统会抛出java.lang.IllegalStateException异常；

（4）当前页未使用out缓冲区，并且在<jsp:forward>标签前有任意字符，系统会抛出java.lang.IllegalStateException异常。

## 传参标签<jsp：param>
实现传递参数功能的标签——<jsp:param>,该标签主要用来作为其他JSP标签的子标签，以便向其他的标签传递参数。语法如下：
```java
<jsp:param name="name" value="value"|<%=expression%>"/>
```
<jsp:param>标签传递的是URL的参数。
```java
<jsp:include page="/chapter5/processParam.jsp?id=1234&age=30">
    <!--使用param标签为URL添加请求参数值-->
    <jsp:param name="name" value="bill">
    <jsp:param name="age" value="35">
    <jsp:param name="salary" value="<%=3000/1.2%>"/>
</jsp:include>
```
## 创建Bean标签<jsp：useBean>
一个创建JavaBean类对象功能的标签——<jsp:useBean>,用于在不使用java代码的前提下创建类的对象实例，语法如下：
```java
格式一
<jsp:useBean id="name" scope="page|request|session|application" typeSpec/>
格式二
<jsp:useBean id="name" scope="page|request|session|application" typeSpec>
标签体
</jsp:useBean>
typeSpec::=class="className"|
class="className" type="typeName|
beanName="beanName"|<%=expression%>|EL" type="typeName"
```
<jsp:userBean>标签的5个属性：

（1）id:表示对象实例名；

（2）scope：表示对象实例的有效范围；

（3）class:要实例化的类名；

（4）beanName:与Class类名类似，beanName属性也是要实例化的类名；

（5）type:要实例化的类的父类名；

### <jsp:userBean>有三种使用方法：
#### 最简单的方法

```java
<jsp:useBean id="myDate" class="java.util.Date">
相当于如下java代码
<%
    java.util.Date myDate=new java.util.Date();
%>
```
#### 使用class和type
```java
<jsp:useBean id ="myDate" class="java.util.Date" type="Object">
相当于如下java代码
<%
    Object myDate = new java.util.Date();
%>
```
#### 使用beanName和type
```java
<jsp:useBean id="myDate" type="Object" beanName="java.util.Date">
相当于如下java代码
<%
    Object myDate = (Object)java.beans.Beans.instantiate(this.getClass().getClassLoader("java.util.Date");
%>
```
注意：beanName属性和type必须成对出现，并且beanName属性和class属性不能同时使用。
<jsp:useBean>标签还可以有标签体;
```java
<!--testPageScope.jsp-->
<%@page pageEncoding="UTF-8"%>
<!--保存当前页面域里-->
<jsp:useBean id="myDate" scope="page" class="java.util.Date">
这是一个标签体
</jsp:useBean>
```
```java
<!--testSessionScope.jsp-->
<%@page pageEncoding="UTF-8"%>
<!--保存当前页面域里-->
<jsp:useBean id="myDate" scope="session"calss="java.util.Date">
这是一个标签体
</jsp:useBean>
```
将scope属性设为page时，<jsp:useBean>标签创建的对象只对当前页面有效，并且<jsp:useBean>标签在创建对象之前，会先在page域里查找是否存在和id属性值同名的对象。如果存在，就不会再创建新的对象实例。当scope属性的值page时，每次刷新页面都会输出标签体。但当scope属性的值为session时，由于刷新页面时仍然位于一个IE窗口，所以session是共享的。

## 设置属性值标签<jsp：setProperty>
<jsp:setProperty>标签用来设置对象实例的属性值，可以放在<jsp:useBean>的标签体中，也可以单独使用。
<jsp:setProperty>有四个属性：

（1）name:对象实例名；

（2）property:对象实例的属性名；

（3）param:URL中的参数名；

（4）value:指定某个属性的值；

```java
<%@page pageEncoding="UTF-8"%>
<%--创建MyClass类的对象实例--%>
<jsp:useBean id="myClass" class="chapter5.MyClass"/>
<%--
    //也可以这样创建MyClass类的对象实例
    <jsp:useBean id="myclass" beanName="chapter5.MyClass" type="chapter5.MyClass"/>
--%>
<%--直接设置属性值--%>
<jsp:setProperty property="name" name="myClass" value="bill"/>
<jsp:setProperty property="age" name="myClass" value="32"/>
<%--使用不同名为URL请求参数设置属性值，也就是说，URL的参数中必须有name和age--%>
<jsp:setProperty property="name" name="myClass" param="newName"/>
<jsp:setProperty property="age" name="myClass" param="newParam"/>
<%--使用URL请求参数自动设置所有的属性--%>
<jsp:setProperty property="*" name="myClass”/>
<%
    //获取域里名为"myClass"的值的name属性的值；
    String name=((chapter5.MyClass)pageContext.getAttribute("myClass")).getName();
    //获取域里名为"myClass"值的age属性的值
    int age=((chapter5.MyClass)pageContext.getAttribute("myClass")).getAge();
    out.println("name:"+name);//输出属性name的值
    out.println("<br>age:");
    out.println(age);      //输出属性age的值
%>
```
从代码中可以看出，使用java代码从pageContext对象中可以获得myClass类的对象实例  ，并输出相应的属性值。
如果使用URL请求参数设置属性值，且和属性同名的URL请求参数不存在时，就会使用param参数指定的请求参数来设置这个属性。

## 获取属性值标签<jsp：getProperty>
<jsp:getProperty>实现获取对象属性功能的标签，主要用于获得对象实例的实现值，语法格式如下：
```java
<jsp:getProperty name="name" property="propertyName"/>
```
```java
<!--创建MyClass类的对象实例-->
<jsp:useBean id="myClass" beanName="chapter5.MyClass" type="chapter5.MyClass"/>
<!--设置MyClass对象实例的name属性-->
<jsp:setProperty property="name" name="myClass" value="bill"/>
<!--设置MyClass对象实例的age属性-->
<jsp:setproperty property="age" name="myClass" value="32"/>
<!--输出MyClass对象实例的name属性值-->
name:<jsp:getProperty property="name" name="myClass"/>
<br>
<!--属性MyClass对象实例的age属性值-->
age:<jsp:getProperty property="age" name="myClass"/>
```


# 七、JSP表征标签库
