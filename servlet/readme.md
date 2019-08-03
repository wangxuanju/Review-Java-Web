## Servlet
一个Servlet就是一个Java类，并提供基于请求-响应模式的web服务。

## Servlet和Servlet容器的关系：
Servlet只有在Servlet容器中才能发挥作用。
### Servlet容器：
装载和管理Servlet（创建、执行和销毁）

Servlet就是一个服务端程序（负责把请求转发给Servlet，交由Servlet处理，处理完之后，由Servlet容器将结果返回给客户端）；
```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
```
```java
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


### Servlet的生命周期：
初始化（init方法）：一般在Servlet第一次请求才创建（整个Servlet生命周期中调用一次）

请求处理（service方法）

销毁（destroy方法）：在Servlet销毁之前调用
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


### Get方法与Post方法的区别：
#### 传输方式
HTTP header&HTTP boby

URL可见&URL不可见
#### 设计目的：
获取数据&发送数据
#### 安全性：
低（get）&高（post）
```java
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>get/post servlet</title>
</head>
<body>
	<form action="servlet/GetPostServlet" method="GET">
		用户名: <input type=text name=name1 value=""> 密码: <input
			type=password name=pw1 value=""><br> <input
			type="submit" value="使用GET提交"> <br>
	</form>
	-----------------------------------------------------------------------------
	<br></br>
	<form action="servlet/GetPostServlet" method="POST">
		用户名: <input type=text name=name2 value=""> 密码: <input
			type=password name=pw2 value=""><br> <input
			type="submit" value="使用POST提交">
	</form>
</body>
</html>
```

### Servlet配置参数
ServletConfig
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


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
Servlet初始化过程中，<init-param>参数将被封装到ServletConfig

每个Servlet支持设置一个或者多个<init-param>

以Servlet为单位，不是全局变量


### ServletContext（全局的配置信息）
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
```
ServletContext

通过配置文件共享全局配置信息

通过ServletContext属性实现不同Servlet之间的通信


如何读取外部资源配置文件信息？
ServletContext (1)getResource（2）getResourceAsStream（3）getRealPath


```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netease.server.example.util.GeneralUtil;

public class ServletContextServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		ServletContext ctx = this.getServletContext();
		String globalValue1 = ctx.getInitParameter("globalData1");
		String globalValue2 = ctx.getInitParameter("globalData2");
		System.out.println("servlet context global value1: " + globalValue1
				+ ", global value2: " + globalValue2);
		ctx.setAttribute("attribute1", "111");

		try {
			URL url = ctx.getResource("/WEB-INF/classes/log4j.properties");
			InputStream in = url.openStream();
			String propertyValue = GeneralUtil.getPropery("log4j.rootLogger",
					in);
			System.out.println("property value: " + propertyValue);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("=======================================");
		InputStream in2 = ctx
				.getResourceAsStream("/WEB-INF/classes/log4j.properties");
		String p2 = GeneralUtil.getPropery("log4j.rootLogger", in2);
		System.out.println("p2: " + p2);
		System.out.println("=======================================");
		String path = ctx.getRealPath("/WEB-INF/classes/log4j.properties");
		System.out.println("real path: " + path);
		File f = new File(path);
		try {
			InputStream in3 = new FileInputStream(f);
			String p3 = GeneralUtil.getPropery("log4j.rootLogger", in3);
			System.out.println("p3: " + p3);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		pw.print("ServletContext test");
		pw.close();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
```

### Servlet配置
Web.xml文件称为部署描述符

---Xml文件

---设置web应用程序的组件部署信息

---Servlet容器需要支持部署描述符的所有元素




如果需要在Servlet容器启动时执行操作？
<servlet>
    <load-on-startup>0</load-on-startup>//load-on-startup改变servlet默认初始时间
<servlet-name>ServletCtx</servlet-name>
<servlet-class>com.netease.example.ContextServlet</servlet-class>
</servlet>


### Cookie&Session
#### Cookie
会话数据保存在客户端（Key-Value形式数据）
#### Session
会话数据保存在服务器端

### Cookie生命周期：
默认结束后失效
setMaxAge设置cookie有效期
### Cookie缺陷：
大小和数量的限制

数据安全性的问题
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
	private static final long serialVersionUID = 4607606190625660785L;

	/** Logger for this class.*/
	private static Logger logger = Logger.getLogger(UserServlet.class);

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

### Session
HttpSession

Session生命周期

默认有效期30分钟

setMaxInactiveInterval设置有效期(设置接口的方式或设置描述符的方式来设置有效期，前者优先级大于后者)

部署描述符配置有效期

invalidate使Session失效

### Cookie&Session
数据存储：Cookie存客户端，Session存服务端；

使用安全：Cookie以明文方式存在客户端，安全性较弱，但是可以使用加密方式再进行存放；Session存在服务器端内存中安全性相对较强。

生命周期：Cookie的生命周期是累积时间的，到点失效，通过setMaxAge来设置有效期；Session的生命周期是间隔时间的，从最后一次访问开始计时，可以直接调用API使其失效。

使用原则：Cookie是有限制的，每个站点是20个Cookie,每个cookie大小4k以内；Session存放在服务器端，建议不要在Session中存过多过大的对象。


### 转发与重定向
#### 请求转发：
forward：
---将当前的request和response对象交给指定的web组件处理；
---一次请求，一次响应
#### 转发对象：
RequestDispatcher

通过HttpServletRequest获取

通过ServletContext获取
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
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletForwardExample extends HttpServlet {

	@Override
	public void init() throws ServletException {
		System.out.println("init method");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");

		String user = req.getParameter("user");
		PrintWriter writer = resp.getWriter();
		writer.println("<html>");
		writer.println("<head><title>转发示例</title></head>");
		writer.println("<body>");
		writer.println("<p>用户名：" + user + "</p>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();
	}
}
```
### 请求重定向
sendRedirect

通过response对象发送给浏览器一个新url地址，让其从新请求

两次请求，两次响应
```java
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


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRedirectExample extends HttpServlet {

	@Override
	public void init() throws ServletException {
		System.out.println("init method");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		
		String user = req.getParameter("user");

		PrintWriter writer = resp.getWriter();
		writer.println("<html>");
		writer.println("<head><title>转发示例</title></head>");
		writer.println("<body>");
		writer.println("<p>用户名：" + user + "</p>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();
	}
}
```
### 转发&重定向
浏览器地址栏的变化（请求转发地址栏不会发生变化；请求重定向，浏览器地址栏会发生变化）

请求范围（请求转发，只能在同一个web服务中进行转发；请求重定向，跨web应用和服务器，重定位资源）

请求过程（一次请求，一次响应；两次请求，两次响应）


### 过滤器和监听器
#### 过滤器
过滤请求与响应

自定义过滤规则

用于对拥护请求进行预处理，和对请求响应进行后处理的web应用组件

### 过滤器的生命周期：
```java
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("filter init method");
		String value = filterConfig.getInitParameter("filterParam");
		System.out.println("filter config param: " + value);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("filter doFilter method");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		if (session.getAttribute("userName") == null) {
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect("../index.html");
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		System.out.println("filter destroy method");
	}

}



<filter>
		<init-param>
			<param-name>filterParam</param-name>
			<param-value>111</param-value>
		</init-param>
		<filter-name>TestFilter</filter-name>
		<filter-class>com.netease.server.example.web.controller.filter.TestFilter</filter-class>
	</filter>
```
### 监听器
监听事件发生，在事件发生前后能够做出相应处理的web应用组件。
#### 监听器的分类：
Listener:

---ServletContext(监听应用程序环境)（ServletContextListener&ServletContextAttributeListener）

---ServletRequest（监听拥护请求对象）(ServletRequestListener&ServletRequestAttributeListener)

---HTTPSession（监听用户会话对象）(HttpSessionListener&HttpSessionAttributeListener&HttpSessionActivationListener&HttpSessionBindingListener)
#### 监听器的应用场景
应用统计

任务触发

业务需求

监听器的启动顺序（web.xml中）
```java
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class TestListener implements HttpSessionAttributeListener,
		ServletContextListener, ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		System.out.println("listener: request destroy");
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		System.out.println("listener: request init");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("listener: context init");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("listener: context destroy");
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		System.out.println("listener: session attribute added.");
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		System.out.println("listener: session attribute removed");
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		System.out.println("listener: session attribute replaced");
	}

}
```


### JSP
动态网页技术标准

简化的Servlet

JSP=Html+Java+JSP tag


#### JSP（页面表示，视图） vs Servlet（逻辑控制：）
#### JSP声明
一个声明语句可以声明一个或多个变量、方法，供后面的Java代码使用。

<%!声明语句 %>

<%! Int A,B,C %>
#### JSP表达式
表达式元素中可以包含任何符合Java语言规范的表达式，但是不能使用分号来结束表达式。
<%=表达式 %>
<p>Today`s date:<%=(new java.util.Date()).toLocaleString()%></p>
	
#### JSP脚本
脚本程序可以包含任意量的Java语句、变量、方法或表达式。
<% 代码片段%>
	
#### JSP注释
JSP注释主要为代码作注释以及将某段代码注释掉
<%-- ---%>
### JSP指令
	
#### page指令
定义页面的依赖属性，比如脚本语言、error页面、缓存需求等等。
	
#### Include指令
--包含其它文件
	
#### taglib指令
--引入标签库的定义
	
### JSP内置对象
rquest(HttpServletRequest类的实例)/response（HttpSevletResponse类的实例）/out（PrintWriter类的实例，用于把结果输出到网页上）/session（HttpSession类的实例）/application（ServletContext类的实例，与应用上下文有关）/config（ServletConfig类的实例）
	
#### JSP独有的对象
Page(类似于Java类中this关键字)/pageContext（PageContext类的实例，提供对JSP页面所有对象以及命名空间的访问）/Exception（Exception类的对象，代表发生错误的JSP页面中对应的异常对象）
