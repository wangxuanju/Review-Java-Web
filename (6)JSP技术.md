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
    <%=request.getParameter("username")%>//用于向网页中输出request.getParamter("username"))方法的返回值，与源代码中对应是        out.print(request.getParameter("username"));
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





# 三、JSP的生命周期

# 四、请求转发

# 五、包含

# 六、JSP异常处理

# 七、发布JSP

# 八、预编译JSP
# 九、PageContext抽象类

