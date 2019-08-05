# HTTP协议
## HTTP协议:
用来规定浏览器与服务器之前需要遵守的规则. 
## HTTP协议的作用：
规范浏览器和服务器之间的数据传递.  
## HTTP协议的特点： 
* 基于请求和响应的模型. 
* 必须先有请求后有响应. 
* 请求和响应必须成对出现. 

## HTTP协议的版本: 
* 1.1 :现在使用. 不是每次响应后挂断,等待长时间以后没有请求会挂断

## 状态码 ：
* 200  ：成功
* 302  ：重定向
* 304  ：查找本地缓存
* 404  ：资源不存在
* 500  ：服务器内部错误

## 提交方式:
* 提交方式有很多,常用的 GET 和 POST:
* GET 和 POST 的区别:
* GET 的提交的参数会显示到地址栏上,而 POST 不显示.
* GET 往往是有大小限制的,而 POST 没有大小的限制.
* GET 没有请求体,而 POST 有请求体.


# Servlet
## 什么是 Servlet:
* 就是一个运行在 WEB 服务器上的小的 Java 程序,用来接收和响应从客户端发送过来的请求,通常使用 HTTP协议.
* 一个Servlet就是一个Java类，并提供基于请求-响应模式的web服务。
* Servlet就是一个服务端程序（负责把请求转发给Servlet，交由Servlet处理，处理完之后，由Servlet容器将结果返回给客户端）；
* Servlet 就是 SUN 公司提供的一个动态网页开发技术.
## 编写一个类继承 HttpServlet，重写 doGet 和 doPost 方法.
```java
public class TestServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("service method");
		super.service(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("doGet method");
		PrintWriter pw = resp.getWriter();
		pw.print("/hello");
		pw.close();
	}

}

//web.xml中配置文件
<servlet>
		<servlet-name>TestServlet</servlet-name>
		<servlet-class>com.netease.server.example.web.controller.TestServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>TestServlet</servlet-name>
		<url-pattern>/hello</url-pattern>//映射出去的路径
	</servlet-mapping>

```
http://localhost:8080/web_project_template/hello
浏览器中URL地址——Servlet容器根据URL地址通过配置文件web.xml找到对应的Servlet，同时将请求转发给Servlet对应的service方法。每当一个客户端请求httpServlet对象的时候，该对象service方法被调用，传给这个方法一个HttpServletRquest和HttpServletResponse对象作为参数。
（由于我们使用的是HTTP get方法访问的Servlet，service就会调用doGet方法处理（post方法的使用同理）。最后我们在doGet方法里通过HttpServletResponse对象把hello word返回给客户端。）


## Servlet 生命周期:Servlet 从创建到销毁的过程.
* 何时创建:用户第一次访问 Servlet 创建 Servlet 的实例
* 何时销毁:当项目从服务器中移除的时候，或者关闭服务器的时候.
  用户第一次访问Servlet 的时候, 服务器会 创建一个Servlet 的实例, 那么Servlet 中init 方法就会执行.

任何一次请求服务器都会创建一个新的线程访问 Servlet  中的 service  的方法.在 service方法内部根据请求的方式的不同调用 doXXX  的方法.(get  请求调用 doGet,post  请求调用 doPost). 当 Servlet  中服务器中移除掉, 或者关闭服务器,Servlet  的实例就会被销毁, 那么 destroy

## Servlet 的生命周期：
* 第一次访问 Servlet 的时候,服务器创建一个 Servlet 的对象.init 方法就会执行.任何一次请求服务器都会创建一个新的线程执行service方法.service的方法内部根据请求方式调用doXXX方法.当服务器关闭的时候,servlet 就会被销毁.destroy 方法就会执行.
```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class TestServlet extends HttpServlet {

    @Override
	public void destroy() throws ServletException {
		System.out.println("destroy method");
	}

	@Override
	public void init() throws ServletException {
		System.out.println("init method");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("service method");
		super.service(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("doGet method");
		PrintWriter pw = resp.getWriter();
		pw.print("/hello");
		pw.close();
	}
}
```
#### ServletConfig
```java
<!-- servletconfig servlet -->
	<servlet>
		<init-param>
			<param-name>data1</param-name>
			<param-value>value1</param-value>
		</init-param>
		<init-param>
			<param-name>data2</param-name>
			<param-value>value2</param-value>
		</init-param>
		<servlet-name>ServletConfigServlet</servlet-name>
		<servlet-class>com.netease.server.example.web.controller.ServletConfigServlet</servlet-class>
	</servlet>
```
#### ServletContext
```java
<!-- servletcontext servlet -->
	<context-param>
		<param-name>globalData1</param-name>
		<param-value>123</param-value>
	</context-param>
	<context-param>
		<param-name>globalData2</param-name>
		<param-value>345</param-value>
	</context-param>
  //通过配置文件共享全局配置信息,通过ServletContext属性实现不同Servlet之间的通信
```
```java
public class ServletConfigServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		ServletConfig config = this.getServletConfig();
		String v1 = config.getInitParameter("data1");
		System.out.println("v1: " + v1);
		String v2 = config.getInitParameter("data2");
		System.out.println("v2: " + v2);

		ServletContext ctx = this.getServletContext();
		String globalValue1 = ctx.getInitParameter("globalData1");
		String globalValue2 = ctx.getInitParameter("globalData2");
		System.out.println("global value1: " + globalValue1
				+ ", global value2: " + globalValue2);

		String attribute = (String) ctx.getAttribute("attribute1");
		System.out.println("attribute: " + attribute);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		pw.print("ServletConfig test");
		pw.close();
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
```
## Get方法与Post方法的区别：
### 传输方式
HTTP header&HTTP boby
URL可见&URL不可见
### 设计目的：
获取数据&发送数据
### 安全性：
低（get）&高（post）





## 常见的会话技术:
* Cookie  :将数据保存到客户端浏览器.
* Session  :将数据保存到服务器端


## Cookie&Session
### Cookie
会话数据保存在客户端（Key-Value形式数据）
### Session
会话数据保存在服务器端
### Cookie生命周期：
默认结束后失效 setMaxAge设置cookie有效期
### Cookie缺陷：
大小和数量的限制
数据安全性的问题
### Session
HttpSession
### Session生命周期
默认有效期30分钟

setMaxInactiveInterval设置有效期(设置接口的方式或设置描述符的方式来设置有效期，前者优先级大于后者)

部署描述符配置有效期

invalidate使Session失效
## Cookie&Session
数据存储：Cookie存客户端，Session存服务端；

使用安全：Cookie以明文方式存在客户端，安全性较弱，但是可以使用加密方式再进行存放；Session存在服务器端内存中安全性相对较强。

生命周期：Cookie的生命周期是累积时间的，到点失效，通过setMaxAge来设置有效期；Session的生命周期是间隔时间的，从最后一次访问开始计时，可以直接调用API使其失效。

使用原则：Cookie是有限制的，每个站点是20个Cookie,每个cookie大小4k以内；Session存放在服务器端，建议不要在Session中存过多过大的对象。
```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class UserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("UserServlet post method is invoked.");
		response.setContentType("text/html;charset=UTF-8");
		process(request, response);
	}

	protected void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = null;
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("userPassword");

		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("userName");

		if (name != null) {
			System.out.println("second login: " + name);
		}

		session.setAttribute("userName", userName);

		Cookie userNameCookie = new Cookie("userName", userName);
		Cookie pwdCookie = new Cookie("pwd", userPassword);

		userNameCookie.setMaxAge(10 * 60);
		pwdCookie.setMaxAge(10 * 60);

		response.addCookie(userNameCookie);
		response.addCookie(pwdCookie);

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("userName")) {
					userName = cookie.getValue();
				}
				if (cookie.getName().equals("pwd")) {
					userPassword = cookie.getValue();
				}
			}
		}

		try {
			if (userName.equals("123") && userPassword.equals("123")) {
				PrintWriter writer = response.getWriter();
				writer.println("<html>");
				writer.println("<head><title>用户中心</title></head>");
				writer.println("<body>");
				writer.println("<p>用户名：" + userName + "</p>");
				writer.println("<p>用户密码：" + userPassword + "</p>");
				writer.println("</body>");
				writer.println("</html>");
				writer.close();
			} else {
				dispatcher = request.getRequestDispatcher("/error.html");
				dispatcher.forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			dispatcher = request.getRequestDispatcher("/error.html");
			dispatcher.forward(request, response);
		}

	}
}
```
### 转发和重定向的区别:
#### 转发
request.getRequestDispatcher(String path).forward(request,response);
#### 重定向
response.sendRedirect(String path); -- 完成重定向
#### 区别
1.转发的地址栏不变的.重定向的地址栏发生变化的.

2.转发是一次请求一次响应,重定向是两次请求两次响应.

3.request 域对象存取的值在转发中是有效的,在重定向无效的.

4.转发的路径不需要加工程名.重定向的路径需要加工程名.


### 转发和重定向区别?
* 转发是一次请求一次响应,重定向两次请求和两次响应.
* 转发地址栏不变,重定向会变化.
* 转发的路径不需要加工程名,重定向需要加工程名.
* 转发只能在本网站内部,重定向可以定向到任何网站.
```java
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletForward extends HttpServlet {

	@Override
	public void init() throws ServletException {
		System.out.println("init method");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/forwardExample");
		rd = this.getServletContext().getNamedDispatcher(
				"ServletForwardExample");
		rd = this.getServletContext().getRequestDispatcher("/forwardExample");
		rd.forward(req, resp);
	}
}

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRedirect extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		System.out.println("init method");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("redirectExample");
	}
}
```

# 什么是 JSP：
* Java Server Pages（Java 服务器端的页面）
## JSP 的脚本
  <%! %> :翻译成 Servlet 中的成员内容. 定义变量，方法，类. -- 不建议(因为是成员变量，会出现线程安全的问题).

  <% %> :翻译成 Servlet 中 service 方法内部的内容. 定义类,变量 （因为是局部变量，没有线程安全问题）

  <%= %> :翻译成 Servlet 中 service 方法中 out.print();
## JSP 的注释
  HTML 的注释 :<!-- 注释 --> 

  Java 代码的注释  :// 单行注释 /*多行注释*/ /** 文档注释 */

  JSP 的注释  :<%-- JSP 的注释 --%>
## JSP 的指令
  指令的语法:<%@ 指令名称 属性名称=”属性值” 属性名称=”属性值” ...%>

  JSP 中有三个指令:page 指令, include 指令, taglib 指令.

  JSP 中 中 page  指令:<%@ page %> --  设置 JSP  
## JSP 9大内置对象
request/response/session/application/page/config/out/exception/pageContext



# MVC的设计模式: 
M:Model:模型层 
V:View:视图层 
C:Controller:控制层

# JavaBean:就是一个满足了特定格式的 Java 类:
* 需要提供无参数的构造方法:
* 属性私有
* 对私有的属性提供 public 的 get/set 方法.

# 事务
## 什么是事务: 
* 事务指的是逻辑上的一组操作,组成这组操作的各个逻辑单元要么一起成功,要么一起失败. 
（MYSQL 数据库事务默认是自动提交的.Oracle 数据库事务默认是不自动提交.)

## 事务特性: 
原子性：强调事务的不可分割. 

一致性：强调的是事务的执行的前后，数据的完整性要保持一致. 

隔离性：一个事务的执行不应该受到其他事务的干扰. 

持久性：事务一旦结束(提交/回滚)数据就持久保持到了数据库.


## 如果不考虑事务的隔离性,引发一些安全性问题: 
### 读问题
* 脏读：一个事务读到另一个事务还没有提交的数据. 
* 不可重复读:一个事务读到了另一个事务已经提交的 update 的数据,导致在当前的事务中多次查询结果 
不一致. 
* 虚读/幻读:一个事务读到另一个事务已经提交的 insert 的数据,导致在当前的事务中多次的查询结果不一致. 


## 设置事务的隔离级别:
* read uncommitted : 未提交读. 脏读，不可重复读，虚读都可能发生.
* read committed  : 已提交读. 避免脏读. 但是不可重复读和虚读有可能发生.
* repeatable read  : 可重复读. 避免脏读, 不可重复读. 但是虚读有可能发生.
* serializable : 串行化的. 避免脏读，不可重复读，虚读的发生.

MYSQL 隔离级别：repeatable read      Oracle 隔离级别:read committed

## JDBC 的事务管理:
* 事务的概念：指的是逻辑上的一组操作,要么一起成功，要么一起失败.
### 事务的特性：
* 原子性：事务的不可分割
* 一致性：事务执行的前后,数据完整性保持一致.
* 隔离性：一个事务的执行不应该受到另一个事务的干扰
* 持久性：事务一旦结束，将会永久修改到数据库.
