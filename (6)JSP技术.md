<!-- GFM-TOC -->
* [一、比较HTML、Servlet和JSP](#一比较html、servlet和jsp)
    * [比较HTML、Servlet和JSP](#比较html、servlet和jsp)
* [二、JSP语法](#二jsp语法)
    * [JSP指令](#jsp指令)
    * [Java程序片段](#java程序片段)
    * [java表达式](#java表达式)
    * [隐含对象](#隐含对象)
* [三、JSP的生命周期](#三jsp的生命周期)
* [四、请求转发](#四请求转发)
* [五、包含](#五包含)
    * [静态包含](#静态包含)
    * [动态包含](#动态包含)
    * [混合使用静态包含和动态包含](#混合使用静态包含和动态包含)
* [六、JSP异常处理](#六jsp异常处理)
* [七、发布JSP](#七发布jsp)
* [八、预编译JSP](#八预编译jsp)
* [九、PageContext抽象类](#九pagecontext抽象类)
    * [向各种范围内存取属性的方法](#向各种范围内存取属性的方法)
    * [用于获得由Servelt容器提供的其他对象的引用的方法](#用于获得由servelt容器提供的其他对象的引用的方法)
    * [用于请求转发和包含的方法](#用于请求转发和包含的方法)
<!-- GFM-TOC -->
# 一、比较HTML、Servlet和JSP
JSP作用是简化网络创建过程和维护动态网站。
## 比较HTML、Servlet和JSP
### 用Servlet动态生成HTML文件
能根据客户端提供的username请求参数来动态生成与参数匹配的HTML文档。如果username请求参数为的值为"Tom"，那么HTML文档内容为"hello,Tom";
```java
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class HelloServlet extends HttpServlet {
  /** 响应客户请求*/
  public void doGet(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException {
    //获得username请求参数 
    String username=request.getParameter("username");
    
    /*字符编码转换。HTTP请求的默认字符编码为ISO-8859-1，如果请求中包含中文，需要把它转换为GB2312中文编码。*/
    if(username!=null)
      username=new String(username.getBytes("ISO-8859-1"),"GB2312");
          
    if(username==null){
      //仅仅为了演示response.sendError()的用法。
      response.sendError(response.SC_FORBIDDEN);
      return;
    }

    //设置HTTP响应的正文的MIME类型及字符编码
    response.setContentType("text/html;charset=GB2312");
   
    /*输出HTML文档*///开发人员必须以编写java程序代码的方式来生成HTML文档，更确切的说需要通过PrintWriter对象来一行行的打印HTML文档的内容。
    PrintWriter out = response.getWriter();
    out.println("<html><head><title>HelloServlet</TITLE></head>");
    out.println("<body>");
    out.println("你好: "+username);
    out.println("</body></html>");
     
    System.out.println("before close():"+response.isCommitted()); //false
    out.close(); //关闭PrintWriter
    System.out.println("after close():"+response.isCommitted());  //true
  }
}
```
对于内容庞大并且布局非常复杂的网页，开发人员用Servlet来生成这样的网页，编程工作非常繁琐。
### 用JSP动态生成HTML局面
在传统的HTML文件中加入java程序片段和JSP标记，就构成了JSP文件。
```java
<html>
<head>
    <title>helloapp</title>
</head>
<body>
    <b>Hello，<%=request.getParameter("username")%></b>//采用了JSP的语法，作用是向网页输出request.getParameter("username")方法的返回值。
</body>
</html>
```
当Servlet容器接收到客户端的要求访问特定JSP文件的请求时，容器按照以下流程来处理客户请求：

（1）查找与JSP文件对应的Servlet，如果已经存在，就调用它的服务方法。

（2）如果与JSP文件对应的Servlet还不存在，就解析文件系统中的JSP文件，把它翻译为Servlet源文件，接着把源文件编译成Servelt类，然后再初始化并运行
Servlet。（一般把JSP文件翻译成源文件及编译源文件的过程仅在客户端首次调用JSP文件时发生）
```java
    <%=request.getParameter("username")%>//用于向网页中输出request.getParamter("username"))方法的返回值，与源代码中对应是 
    
    out.print(request.getParameter("username"));
```
JSP技术的出现，使Web应用中HTML文档和业务逻辑代码有效分离成为可能；JSP负责动态生成HTML文档，业务逻辑由其他可重用的组件，如JavaBean或其他的Java程序来实现。

JSP本质上是Servlet,JSP可以访问Servelt API中的接口和类，此外JSP还可以访问JSP API中的接口和类。
# 二、JSP语法
JSP语法的特点实尽快能的用标记来取代JAVA代码，使整个JSP文件在形式上不像java代码，而像标记文档。
### servlet和JSP对比
```java
//引入类
import java.io.*;  //servlet用法
import java.util.Hashtable;
<%@ page import="java.io.*;java.util.Hashtable"%>//JSP用法
//在Servlet类中，通过以下方式设置响应头正文的类型
response.setContentType("text/html;charset=GB2312");//servlet用法
<%@ page contendType="text/html;charset=GB2312"%>//JSP用法
```
## JSP指令
JSP指令用来设置和整个网页相关的属性，一般语法格式如下：
```java
<%@ 指令 属性="值" %>
1 page指令——指定所使用的编程语言、与JSP对应的Servlet所实现的接口、所扩展的类及导入的软件包等，语法格式如下：
<%@ page 属性1="值1" 属性2="值2"%>
2 include指令
通过include指令来包含其他文件的内容，被包含的文件可以是JSP文件或HTML文件，提高了代码的重用性，避免重复代码，提供效率，语法如下：
<%@ include file="目录组件的绝对或相对URL"%>
3 JSP声明
JSP声明用于与JSP对应的Servlet类的成员变量和方法，语法如下：
<%! declaration;[declaration]..%>
```java
<%! int v1=0;%>
<%! int v2,v3,v4;%>      //实例变量
<%! String v5="hello";
    static int v6;       //静态变量
%>
<%!
    public String amethod(int i){  //实例方法
        if(i<3)return "i<3";
        else return "i>3";
   }
```
每个JSP声明只在当前JSP文件中有效，如果希望在多个JSP文件中都包含这些声明，可以把这些声明放到一个单独的JSP文件中，然后再其他JSP文件中用include指令把这个JSP文件包含进来。
## Java程序片段
在<%和%>标记间直接嵌入任何有效的java程序代码；如果在page指令中没有指定method属性，那么程序片段默认为与JSP对应的Servlet类的service()方法中的代码块。
```java  //if语句由三段“<%”和"%>"代码构成，分段的if语句可以控制网页的输出结果。
<%
    String gendar="female";
    if(gender.equals("female"){
%>
    she is a girl;
<%  }else{%>
    she is a girl too;
<% } %>
````
以上JSP代码等价于Servlet的service()方法：
```java
public void service(HttpServlet request,HttpServletResponse response)throws ServleetException,IOException{
    PrintWriter out=response.getWriter();
    String gendar="female"; //局部变量
    if(gender.equals("female")
        out.print("she is a girl");
    else
        out.print("he is a boy");
}
```
## java表达式
java表达式的标记为"<%="和"%>"；使用该标记，那么它能把表达式的值输出到网页上。表达式的int或float类型的值都自动转换成字符串再进行输出。
```java
<%=hitcount++%>
```
JSP中实例变量和局部变量的区别：

对应实例变量，每个JSP实例都有一个实例变量，由于在Servlet容器中，一个JSP文件只对应一个JSP实例。

局部变量在一个方法中定义，当Servlet容器每次调用JSP的服务方法时，Java虚拟机会为局部变量分配内存，从而创建一个新的局部变量；当这个方法执行完毕后，java虚拟机就会消耗这个局部变量。

java表达式除了可以直接插入到模板文本中，也可以作为某些JSP标签的属性的值：
```java
    <jsp:setProperty name="myPageBean" property="count" value="<%=myPageBean.getCount()+1 %>" />
```
## 隐含对象
Servlet可以访问由Servlet容器提供的ServletContext/ServletRequest/ServletResponse等对象；对于JSP程序片段，这些对象称为隐含对象，每个对象都被固定的引用变量引用。JSP不需要做任何变量声明，就可以直接通过固定的引用变量来引用这些对象。

例如：在JSP中可以直接通过request变量来获取HTTP请求中的请求参数。
```java
<%
    String username=request.getParameter("username");
    out.print(username);
%>
```
以上request和out变量分别引用HttpServletRequest和JspWriter隐含对象。
与以下代码是等价的：
```java
<%=username %>
```
例如:通过ServletContext隐含对象向Web应用范围内存放一个username属性，application变量引用ServletContext隐含对象：
```java
<%
    application.setAttribute("username","Tom");
%>
```
隐含对象的固定引用变量，是与JSP对应的Servlet类的服务方法中的方法参数或局部变量。
# 三、JSP的生命周期
Servlet容器必须先把JSP编译成Servlet类，然后才能运行它；JSP的生命周期包含以下阶段：
```java
解析阶段：Servlet容器解析JSP文件的代码；
翻译阶段;Servlet容器把JSP文件翻译成Servlet源文件；
编译阶段：Servlet容器编译Servlet源文件，生成Servlet类；
初始化阶段：加载与JSP对应的Servlet类，创建其实例，并调用它的初始化方法；
运行时阶段：调用与JSP对应的Servlet实例的服务方法；
销毁阶段：调用与JSP对应的Servlet实例的销毁方法，然后销毁Servlet实例。
```
在JSP的生命周期中，解析、翻译和编译是其特有的阶段，这三个阶段仅仅发生在以下场合：

(1)JSP文件被客户端首次请求访问；

（2）JSP文件被更新；

（3）与JSP文件对应的Servlet类的类文件被手工删除；

初始化、运行时和销毁阶段则是JSP和Servlet都具有的阶段。


在jspPage接口中定义了jspInit()和jspDestory()方法，它们的作用与Servlet接口的init()和destory()方法相同。
例子中life.jsp中定义了jspInit()和jspDestory()方法；
```java
<%@ page contentType="text/html; charset=GB2312" %>
<html><head><title>life.jsp</title></head><body>

<%! //life.jsp定义了三个用于跟踪JSP的生命周期的实例变量。
  private int initVar=0;   //统计jspIntit()方法被调用的次数，即JSP被初始化的次数。
  private int serviceVar=0; //统计jspService()方法被调用的次数，即JSP响应客户的请求的次数。
  private int destroyVar=0; //统计serviceDestory()被调用的 次数，即JSP被销毁的次数。
%>
  
<%!
  public void jspInit(){
    initVar++;
    System.out.println("jspInit(): JSP被初始化了"+initVar+"次");
  }
  public void jspDestroy(){
    destroyVar++;
    System.out.println("jspDestroy(): JSP被销毁了"+destroyVar+"次");
  }
%>

<%
  serviceVar++;
  System.out.println("_jspService(): JSP共响应了"+serviceVar+"次请求");

  String content1="初始化次数 : "+initVar;
  String content2="响应客户请求次数 : "+serviceVar;
  String content3="销毁次数 : "+destroyVar;
%>

<h1><%=content1 %></h1>
<h1><%=content2 %></h1>
<h1><%=content3 %></h1>

</body></html>

```

# 四、请求转发
JSP采用<jsp:forward>标签来实现请求转发的，转发的目标组件可以为HTML文件、JSP文件或者Servlet.<jsp:forward>的语法为
```java
 <jsp:forward page="转发的目标组件的绝对URL或相对URL"/>
```
JSP源文件和目标组件共享HttpServletRequest对象和HttpServletResponse对象；Servelt源组件调用RequestDispatcher.forward(request,response)方法进行请求转发之后的代码也会被执行。
```java  //sorce.jsp把请求转发给target.jsp.
<html><head><title>Source Page</title></head>
<body>
	<p>
		This is output of source.jsp before forward
	</p>
        <jsp:forward page="target.jsp" />
        <p>
		This is output of source.jsp after forward
	</p>
</body></html>


<html><head><title>Target Page</title></head>
<body>
	<p>
		hello, <%=request.getParameter("username")%>
	</p>
</body></html>
```
由于sorce.jsp将请求转发给了target.jsp，所以source.jsp中的所有数据输出都无效；此外target.jsp和source.jsp共享同一个HttpServletRequest对象，因此target.jsp可以通过request.getParameter("username")方法读取客户端提供的username请求参数。

<jsp:forward>标签中的page属性既可以为相对路径，也可以为绝对路径；

转发源文件还可以通过<jsp:param>标签来转发目标组件传递额外的请求参数。
```java
<html><head><title>Target Page</title></head>
<body>
	<jsp:forward page="target.jsp">
      <jsp:param name="username" value="Tom" />
      <jsp:param naem="password" value="1234"/>
  </jsp:forward>
</body></html>
```
在target.jsp中可以通过request.getParameter("username")的方式来读取source1.jsp传过来的username请求参数；

<jsp:param>标签除了可以嵌套在<jsp:forward>标签中，还可以嵌套在<jsp:include>标签中。
# 五、包含
用include指令来包含其他文件的方法，include指令的语法为：
```java
<%@ include file="被包含组件的绝对URL和相对的URL"%>
```
另外还可以用include标签来包含其他文件，include标签的语法为;
```java
<jsp:include page="被包含组件的URL的绝对URL或相对的URL"/>
```
include指令用于静态包含，而include标签用于动态包含；无论是静态包含还是动态包含，源组件和被包含的组件都共享请求范围内的共享数据。

## 静态包含
```java
sin.jsp is including content.jsp
<% int var=1;
    request.setAttribute("username","Tom");
%>
<%@ include file="contend.jsp" %>
<p>sin.jsp is doing something else.


<!--contend.jsp-->
<p>
Output from content.jsp:
<br>
var=<%=var %>
<br>
username=<%=request.getAttribute("username")%>
```
## 动态包含
动态包含发生在运行JSP源组件阶段，动态包含的目标组件可以为HTML文件、JSP文件或者Servlet；如果组件为JSP，Servlet容器会在运行JSP源组件的过程中，运行与JSP目标组件对应的Servlet的服务方法。JSP目标组件生成响应结果被包含到JSP源组件的响应结果中。

<jsp:include>标签还有一个flush属性，可选值为true和false；如果flush属性为true，就表示源组件在包含目标组件之前，先把已经生成的响应正文提交给客户。flush属性的默认值为false.
```java
    <jsp:include page="content.jsp" flush="true" />
```
## 混合使用静态包含和动态包含
静态包含通常用来包含不会发生变化的网页内容，而动态包含通常用来包含会发生变化的网页内容。
# 六、JSP异常处理
JSP在运行时也有可能抛出异常；在发生异常的场合，可以通过下面的指令将请求转发给另一个专门处理异常的网页。
<%@ page errorPage="errorpage.jsp"%>
以上errorpage.jsp是一个专门负责处理异常的网页；在这个处理异常的网页中，应该通过如下语句将该网页声明为异常处理网页：
<%@ page isErrorPage="true"%>
处理异常的网页可以直接访问exception隐含对象，获取当前异常的信息，例如：
```java
<p>
    错误原因为:<% exception.printStackTrace(new PrintWriter(out));%>
</p>
```
抛出异常的JSP文件与处理异常的JSP文件之间为请求转发关系，因此它们共享请求范围内的共享数据。
```java
<%@ page contentType="text/html; charset=GB2312" %>
<%@ page errorPage="errorpage.jsp" %>

<html><head><title>sum.jsp</title></head>
<body>
    <%!
            private int toInt(String num){
              return Integer.valueOf(num).intValue();
            }
    %>
    <%
        int num1=toInt(request.getParameter("num1"));
        int num2=toInt(request.getParameter("num2"));
    %>

    <p>
         运算结果为:<%=num1%>+<%=num2%>=<%=(num1+num2)%>
    </p>
</body></html>


<%@ page contentType="text/html; charset=GB2312" %>
<%@ page isErrorPage="true" %>
<%@ page import="java.io.PrintWriter" %>

<html><head><title>Error Page</title></head>
<body>

	<p>
		你输入的参数（num1=<%=request.getParameter("num1")%>,
                num2=<%=request.getParameter("num2")%>）有错误
	</p>
	<p>
		错误原因为：<% exception.printStackTrace(new PrintWriter(out));%>
	</p>
</body></html>
```
# 七、发布JSP
在发布Servelt时，必须在web.xml文件中加入<servlet>和<servelt-mapping>元素，其中<servlet-mapping>元素用来为Servlet映射URL；事实上可以为JSP配置<servlet>和<servlet-mapping>元素，从而为JSP映射URL。
```java
  <servlet>
      <servlet-name>hi</servlet-name>
  </servlet>
  <servlet-mapping>
      <servlet-name>hi</servlet-name>
      <url-pattern>/hi</url-pattern>
  </servlet-mapping> 
```

# 八、预编译JSP
当JSP文件被客户端第一次请求访问时，Servlet容器先把JSP文件编译为Servlet类后才能运行，这一过程延长客户端等待响应结果的时间；因此可以对JSP进行预编译。

JSP规范为JSP规定了一个特殊的请求参数jsp_precompile，它的取值可以为true或false.如果请求参数jsp_precompile的值为true，那么Servlet容器仅仅对客户端请求的JSP文件进行预编译，即把JSP文件转换为Servlet类，但不会进行Servlet.
# 九、PageContext抽象类
JSP APi提供了一个实用的类：javax.servlet.jsp.PageContext抽象类，它继承了javax.servlet.jsp.JspContext。
## 向各种范围内存取属性的方法
在PageContext类中提供了一组用于向各种范围内存取属性的方法。


## 用于获得由Servelt容器提供的其他对象的引用的方法
PageContext类的以下方法用于获得由Servelt容器提供的ServletContext/HttpSession/ServletRequest/ServletResponse等对象。
getPage()：返回当前JSP对应的Servlet实例；
getRequest()：返回ServletRequest对象；
getResponse():返回ServletRsonse对象；
getServletConfig()：返回ServletConfig对象；
getServletContext()：返回ServletContext对象；
getSession()：返回HttpSession对象；
getOut()：返回一个用于输出响应正文的JspWriter对象。
## 用于请求转发和包含的方法
PageContext类的以下方法用于请求转发和包含：
forward(String relativeUrlPath):用于把请求转发给其他web组件；
include(String relativeUrlPath):用于包含其他Web组件；
在JSP文件中可以用专门的JSP标记（<jsp:forward>和<jsp:include>）来进行请求转发和包含操作。
