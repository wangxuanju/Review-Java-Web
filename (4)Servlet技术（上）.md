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




# 二、Java Web应用的生命周期

# 三、Servlet的生命周期

# 四、ServletContext与Web应用范围

# 五、Servlet的服务方法

# 六、防止页面被客户端访问
