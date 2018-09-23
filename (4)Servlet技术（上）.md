# 一、Servlet API
servlet是用java语言编写出来的类，可编写出能完成复杂任务的Servlet类。

Servlet对象由Servlet容器创建。

Servlet最常用的武器：

请求对象（Servlet和HttpServletRequest）:Servlet从该对象中获取来自客户端的请求信息。

响应对象（ServletResponse和HttpServletResponse）：Servlet通过该对象来生成响应结果。

Servlet配置对象（ServletConfig）:当容器初始化一个Servlet对象时，会向Servlet提供一个ServletConfig对象,Servlet通过该对象来获取初始化参数信息及ServletContext对象。

Servlet上下文对象（ServletContext）:Servlet通过该对象来访问容器为当前Web应用提供的各种资源。

## Servlet API 
Servlet API主要由两个包组成：javax.servlet和javax.servlet.http.

在javax.servlet包中定义了Servlet接口及相关的通用接口和类；

在javax.servlet.http包中与HTTP协议相关的HttpServlet类、HttpServletRequest接口和HttpServletResponse接口。
### Servlet接口
在Servlet接口中定义了5个方法：有三个方法有Servlet容器来调用，容器会在Servlet的 生命周期的不同阶段调用特定的方法。

init（ServletConfig config）方法：负责初始化Servlet对象，容器在创建好Servlet对象后，就会调用该方法。

service(ServletRequest req,ServletResponse res)方法：当容器接收到客户端要求访问特定Servlet对象的请求时，就会调用该Servlet对象的service()方法。

destory()方法：当Servlet对象结束生命周期时，容器会调用此方法。

Servlet接口还定义了两个返回Servlet的相关信息的方法。

getServletConfig()：返回一个ServletConfig对象，在该对象包含了Servlet的初始化参数信息。

getServeltInfo():返回一个字符串，在该字符串中包含了Servlet的创建者、版本和版权等信息。

在Servlet API中，javax.servlet.GenericServlet抽象类实现了Servlet接口，而javax.servlet.http.HttpServlet抽象类是GeneericServlet类的子类；所以当用户开发自己的Servlet类，可以选择扩展GenericServlet类或者HttpServlet类。
### GenericServlet抽象类
GenericServlet类实现了Servlet接口，还实现了ServletConfig接口和Serializable接口。

GenericServlet类实现了Servlet接口中的init(ServletConfig config)初始化方法。GenericServlet类有一个ServletConfig类型的私有变量config，当Servlet容器调用GenericServlet的init(ServletConfig config)方法时，该方法使得私有实例变量config引用由容器传入的ServletConfig对象，即使得GenericServlet对象与一个ServletConfig对象关联。



service()方法时GenericServlet类唯一的抽象方法，GenericServlet类的具体子类必须实现该方法，从而为特定的客户请求提供具体的服务。

GenericServlet类实现了Servlet接口中的destory()方法，GenericServlet类的具体子类可以覆盖该方法，从而为待销毁的当前Servllt对象释放所占用的各种资源。

此外，GenericServlet类实现了ServletConfig接口中的所有方法。
### HttpServlet抽象类
在开发JavaWeb应用时，自定义的servlet类一般都扩展HttpServlet类。
HTTP协议把客户请求分为get/post/put/delete等多种方式；HttpServlet类针对每一种请求方式都提供了相应的方法，如doGet()/doPost()/doPut/doDelete()等方法。

HttpServlet类实现了Servlet接口中的service(ServletRequest,ServletResponse res)方法，该方法实际上调用的是它的重载方法：
```java
service(HttpServlet  req,HttpServletResponse resp)
```
重载service()方法中，首先HttpServletRequest类型的req参数的getMethod()方法，从而获得客户端的请求方式，然后依据该请求方式来调用匹配的服务方法。如果为GET方式，则调用doGet()方法；如果为POST方式，则调用doPost()方法。


对于HttpServlet类的具体子类，一般会针对客户端的特定请求方式，来覆盖HttpServlet父类中的相应doXXX()方法；为了使doXXX()方法能被Servlet容器访问，应该把访问权限设为public.
```java
public class HelloServlet extends HttpServlet{
    public void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        //提供具体的实现代码
    }
}
```
如果客户端会按照GET或POST请求访问HelloServlet，那么可以在HelloServlet类中重新实现doGet()方法，且让doPost()方法调用doGet()方法。
```java
public class HelloServlet extends HttpServlet{
    public void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        //提供具体的实现代码
    }
    public void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        doGet(req,resp);
    }
}
```
### ServletRequest接口
在Servlet接口中的service(ServletRequest req,ServletResponse res)方法中有一个ServletRequest类型的参数；ServletRequest类表示来自客户端的请求。

ServletRequest接口提供了一系列用于读取客户端的请求数据的方法。

### HttpServletRqsponse接口
HttpServletRequest接口时ServletRequest接口的子接口；HttpServlet类的重载service()方法及doGet()和doPost()等方法都有一个HttpServletRequest类型的参数：
```java
protected void service(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException
```
HttpServletRequest接口提供了用于读取HTTP请求中的相关信息的方法：

getContextPath():返回客户端所请求的web应用的URL入口。

getCookies()：返回HTTP请求中的所有Cookie。

getHeader(String name):返回HTTP请求头部的特定项。

getHeaderNames()：返回一个Enumeration对象，它包含了HTTP请求头部的所有项目名。

getMethods()：返回HTTP请求方式。

getRequestURI():返回HTTP请求的头部的第一行中的URI.

getQueryString()：返回HTTP请求中的查询字符串，即URL中的“？”后面的内容。

### ServletResponse接口
在Servlet接口的service(ServletRequest   req,ServletResponse res)方法中有一个ServletResonse类型的参数。Servlet通过ServletResponse对象生成响应结果。当Servlet容器接收到客户端要求访问特定的Servlet的请求时，容器会创建一个ServltResponse对象，并把它作为参数传递给Servlet的service()方法。

ServletResponse的getOutputStream()方法返回一个ServletRsponse()方法返回一个ServletOutputStream对象，Servlet可以利用ServletOutputStream来输出二进制的正文数据。ServletResponse的getWriter()返回一个PrintWriter对象，ServletResponse的getWriter()方法返回一个PrintWriter对象，Servlet可以利用PrintWriter来输出字符串形式的正文数据。

Servlet通过ServletResponse对象的setContentLength()、setContendType()和setCharacterEncoding()来分别设置响应正文的长度、MIME类型和字符编码。

如果要设置响应正文的MIME类型和字符编码，必须先调用ServletResponse对象的setContentType()和setCharacterEncoding()方法，然后再调用ServletResponse的getOutputStream()或getWriter()方法，或者提交缓冲区内的正文数据。只有满足这样的操作顺序，所做的设置才能生效。

### HttpServletResponse接口
HttpServletResponse接口是ServletResponse的子接口，HttpServlet类的重载service()方法及doGet()和doPost()等方法都有一个HttpServletResponsse类型的参数：
```java
    protected void service(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException
```
addHeader(int sc):向客户端发送一个代表特定错误的HTTP响应状态代码；

addHeader(int sc,String msg):向客户端发送一个代表特定错误的HTTP响应状态代码，并且发送具体的错误信息；
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
    
    /*字符编码转换。HTTP请求的默认字符编码为ISO-8859-1，如果请求中包含中文，需要把
    它转换为GB2312中文编码。*/
    if(username!=null)
      username=new String(username.getBytes("ISO-8859-1"),"GB2312");
          
    if(username==null){
      //仅仅为了演示response.sendError()的用法。
      response.sendError(response.SC_FORBIDDEN);
      return;
    }

    //设置HTTP响应的正文的MIME类型及字符编码
    response.setContentType("text/html;charset=GB2312");
   
    /*输出HTML文档*/
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
HelloServlet类的service()方法最后调用PrintWriter对象的close()方法关闭底层输出流，该方法在关闭输出流之前先把缓冲区内的数据提交到客户端。
```java   //以下三种设置HTTP响应正文的MIME类型及字符编码的方式是等价的
//方式1
response.setContentType("text/html;charset=GB2312");
//方式2
response.setContentType("text/html");
response.setCharacterEncoding("GB2312");
//方式3
response.setHeader("content-type","text/html;charset=GB2312");
```
### ServletConfig接口
Servlet接口的init(ServletConfig config)方法有一个ServletConfig类型的参数。

当Servlet容器初始化一个Servlet对象时，会为这个Servlet对象创建一个ServletConfig对象。在ServletConfig对象包含了Servlet的初始化参数信息。Servlet容器在调用Servlet对象的init(ServletConfig config)方法时，会把ServletConfig对象作为参数传给Servlet对象，init(ServletConfig config)方法会使得当前Servlet对象与ServletConfig对象之间建立关联关系。

在ServletConfig接口中定义了以下方法：

getInitParameter(String name)：根据给定的初始化参数名，返回匹配的初始化参数值；

getInitParameterNames():返回一个Enumeration对象，里面包含了所有的初始化参数名；

在web.xml文件中设置一个Servlet时，可以通过<init-param>元素来设置初始化参数。
```java
<servlet>
    <servlet-name>Font</servlet-name>
    <servlet-class>mypack.FontServlet</servlet-class>
    <init-param>                 // <init-param>元素的<param-name>子元素设定参数名，<param-value>子元素设定参数值。
        <param-name>color</parm-name>
        <param-value>blue</param-value>
    </int-param>
    <init-param>
        <param-name>size</param-name>
        <param-value>15</param-value>
    </init-parm>
</servlet>
```
HttpServlet类继承GenericServlet类，而GenericServlet类实现了ServletConfig接口，因此HttpServlet类或GenericServlet类及子类都可以直接调用ServletCongfig接口中的方法。
```java          //演示了ServletConfig接口的用法
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class FontServlet extends HttpServlet {
  /** 响应客户请求*/
  public void doGet(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException {
    //获得word请求参数 
    String word=request.getParameter("word");
    if(word==null)word="Hello";
    
    //读取初始化参数
    String color=getInitParameter("color");
    String size=getInitParameter("size");
    System.out.println("servletName: "+getServletName());

    //设置HTTP响应的正文的MIME类型及字符编码
    response.setContentType("text/html;charset=GB2312");
   
    /*输出HTML文档*/
    PrintWriter out = response.getWriter();
    out.println("<html><head><title>FontServlet</TITLE></head>");
    out.println("<body>");
    out.println("<font size='"+size+"' color='"+color+"'>"+word+"</font>");
    out.println("</body></html>");
     
    out.close(); //关闭PrintWriter
  }
}
```
### ServletContext接口
ServletContext是Servlet与Servlet容器之间直接通信的接口；Servlet容器在启动一个Web应用的时候，会为它创建一个ServletContext对象；每个web应用都有唯一的ServletContext对象，可以把ServletContext对象形象的理解为web应用的总管家。

在ServletConfig接口中定义了getServletContext()方法；在HttpServlet类或GenericServlet类及子类中都可以直接调用getServletContext()方法，从而得到当前web应用的ServletContext对象。
```java
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ContextTesterServlet extends HttpServlet {
  /** 响应客户请求*/
  public void doGet(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException {
    //获得ServletContext对象
    ServletContext context=getServletContext(); 

    //设置HTTP响应的正文的MIME类型及字符编码
    response.setContentType("text/html;charset=GB2312");
   
    /*输出HTML文档*/
    PrintWriter out = response.getWriter();
    out.println("<html><head><title>FontServlet</TITLE></head>");
    out.println("<body>");
    out.println("<br>Email: "+context.getInitParameter("emailOfwebmaster"));//用于读取初始化参数的值；
    out.println("<br>Path: "+context.getRealPath("/WEB-INF"));
    out.println("<br>MimeType: "+context.getMimeType("/WEB-INF/web.xml"));
    out.println("<br>MajorVersion: "+context.getMajorVersion()); 
    out.println("<br>ServerInfo: "+context.getServerInfo()); 
    out.println("</body></html>");
    
    //输出日志
    context.log("这是ContextTesterServlet输出的日志。"); //调用context.log()来输出日志；
    out.close(); //关闭PrintWriter
  }
}
```
# 二、Servlet的生命周期
JavaWeb应用的生命周期由Servlet容器来控制；Servlet的生命周期可以分为三个阶段：初始化阶段、运行时阶段和销毁阶段；在javax.servlet.Servlet接口中定义了3个方法：init()、service()和destory()。

## 初始化阶段
Servlet的初始化阶段包括以下四个步骤：

（1）加载Servlet类，把它的Class文件中的数据读入到内存中；

（2）创建ServletConfig对象，ServletConfig对象包含了特定Servlet的初始化配置信息；

（3）创建Servlet对象；

（4）调用Servlet对象的init(ServletConfig config)方法。

在下面两种情况之一，Servlet会进入初始化阶段：

（1）当前web应用处于运行时阶段，特定Servlet被客户首次请求访问。

（2）如果在web.xml文件中为一个Servlet设置了<load-on-startup>元素，那么当Servlet容器启动Servlet所属的Web应用时，就会初始化这个Servlet.
```java
<servlet>  //其中Servlet1和Servlet2的<load-on-startup>的值为1和2；当Servlet容器启动当前web应用时,Servelt1被第一个初始化，Servlet2第二个初始化
<servlet-name>servlet1</servlet-name>
<servlet-class>Servlet1</servlet-class>
<load-on-startup>1</load-on-startup>    
</servlet>
<servlet>
<servlet-name>servlet2</servlet-name>
<servlet-class>Servlet2</servlet-class>
<load-on-startup>2</load-on-startup>
</servlet>
<servlet> //没有配置<load-on-startup>元素，当Servlet容器启动当前web应用时，Servlet不会被初始化，只有当客户端首次请求访问ServletX时，它才会初始化
<servlet-name>servletX</servlet-name>
<servlet-class>ServletX</servlet-class>
</servlet>
```
## 运行时阶段
这个阶段Servlet可以随时响应客户端的请求；当Servlet容器接收到要求访问特定Servlet的客户请求，Servlet容器会创建针对这个请求的ServeltRequest对象和ServletResponse对象，然后调用相应的Servlet对象的service()方法。service()方法从ServletRequest对象中获得客户请求信息并处理该请求，再通过ServletResponse对象生成响应结果。

## 销毁阶段
当web应用被终止时，Servlet容器会先调用web应用中所有的Servlet对象的destory()方法，然后再销毁这些Servlet对象。

此外容器还会销毁与Servlet对象关联的Servlet对象关联的ServletConfig对象。

## 演示Servlet的生命周期的范例
Servlet的生命周期中，Servlet的初始化和销毁都只会发生一次；因此init()方法和destory()方法只会被Servelt容器调用一次，而Service()方法可能会被Servelt容器调用多次，这取决于客户端请求访问Servlet的次数。
```java
import java.io.*;
import javax.servlet.*;

public class LifeServlet extends GenericServlet{
  private int initVar=0;        //统计init()方法被调用的次数
  private int serviceVar=0;    //统计service()方法被调用的次数
  private int destroyVar=0;    //统计destory()方法被调用的次数
  private String name;
  
  public void init (ServletConfig config)throws ServletException{
    super.init(config);
    name=config.getServletName();
    initVar++;
    System.out.println(name+">init(): Servlet被初始化了"+initVar+"次");
  }
  public void destroy(){
    destroyVar++;
    System.out.println(name+">destroy(): Servlet被销毁了"+destroyVar+"次");
  }
  public void service(ServletRequest request,
		ServletResponse response)
		throws IOException ,ServletException{
    serviceVar++;
    System.out.println(name+">service(): Servlet共响应了"+serviceVar+"次请求");

    String content1="初始化次数 : "+initVar;
    String content2="响应客户请求次数 : "+serviceVar;
    String content3="销毁次数 : "+destroyVar;

    response.setContentType("text/html;charset=GB2312");

    PrintWriter out = response.getWriter();
    out.print("<html><head><title>LifeServlet</title>");
    out.print("</head><body>");
    out.print("<h1>"+content1 +"</h1>");
    out.print("<h1>"+content2 +"</h1>");
    out.print("<h1>"+content3 +"</h1>");
    out.print("</body></html>");
    out.close();
  }
}
```
# 三、ServletContext与Web应用范围
Servlet容器在启动一个Web应用时，会为它创建唯一的ServletContext对象；当Servlet容器终止一个web应用时，就会销毁它的ServletContext对象。

在ServletContext接口中常用的用于存取共享数据的方法：
setAttribute(String name,Object object):向web应用范围内存入共享数据；
removeAttribute(String name)：根据参数给定的属性名，从web应用范围内删除匹配的共享数据；
getAttribute(String name)：根据参数给定的属性名，返回web应用范围内匹配的共享数据；
## 在web应用范围内存放共享数据的范例
介绍一个向web应用存放共享数据的范例；
```java
public class Counter{
  private int count; //计数值
  public Counter(){
    this(0);
  }
  public Counter(int count){
    this.count=count;
  }
  public void setCount(int count){
    this.count=count;
  }
  public int getCount(){
    return count;
  }
  
  public void add(int step){
    count+=step;
  }
}

//此类的作用实现了向应用范围内存取计数器的功能
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CounterServlet extends HttpServlet {
  public void doGet(HttpServletRequest request,
    HttpServletResponse response)throws ServletException, IOException {

    //获得ServletContext的引用
    ServletContext context = getServletContext();

    // 从ServletContext中读取counter属性
    Counter counter = (Counter)context.getAttribute("counter");

    // 如果ServletContext中没有counter属性，就创建counter属性
    if ( counter == null ) {
      counter = new Counter(1);
      context.setAttribute("counter", counter);
    }
    
    response.setContentType("text/html;charset=GB2312");
    PrintWriter out = response.getWriter();
    out.println("<html><head><title>CounterServlet</TITLE></head>");
    out.println("<body>");
    // 输出当前的counter属性
    out.println("<h1>欢迎光临本站。您是第 " + counter.getCount()+" 位访问者。</h1>");
    out.println("</body></html>");
  
    //将计数器递增1   
    counter.add(1);
    out.close();
  }
}
```
## 使用ServletContextListener监听器*
当web应用启动时，Servlet容器先调用MyServletContextListener的contextInitialized()方法，再调用lifeInit的init()方法；当web引用终止时，Servlet容器先调用lifeInit的destroy()方法，再调用MyServletListener的contextDestoryed()方法。

由此可见，在web应用的生命周期中，ServletContext对象是最早被创建，最晚被销毁。
# 四、Servlet的服务方法抛出异常
Servlet接口的service()方法完整定义如下：
```java
public void service(ServletRequest req,ServletResponse res)throws ServletException,java.io.IOException
```
ServletException:表示当前Servlet进行常规操作时出现的异常；
IOException：表示当Servlet进行IO操作时出现的异常；

Servlet的service()方法抛出的异常由Servlet容器捕获，Servlet容器在捕获到异常后，会向客户端发送相应的错误信息。
# 五、防止页面被客户端访问
浏览器端的缓存技术适用于保存服务器端的静态网页，以及不包含敏感数据的网页；当网页包含随时更新的动态内容和敏感数据，服务器不希望网页被客户端缓存。
服务器端的HttpServlet可以通过设置特定HTTP响应头来禁止客户端缓存网页，以下示范代码的response变量引用HttpServletResonse对象;
```java
response.addHeader("Pragma","no-cache");   //适用于HTTP1.0浏览器；
response.addHeader("Cache-Control","no-cache");//Cache-Control用来决定客户端是否可以缓存网页，no-cache的意思是不会把网页缓存到本地缓存中；
response.addHeader("Expires","0");//Expires选项用于设定网页过期的 时间，0表示立刻过期；
```
