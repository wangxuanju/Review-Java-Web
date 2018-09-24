# 一、JavaBean简介
JavaBean是一种可重复使用的且跨平台的软件组织；JavaBean可分为两种：一种是有用户界面，还有一种没有用户界面，注意负责表示业务数据或者处理事务（如数据运算、操纵数据库）的JavaBean；JSP常访问的是后一种JavaBean。

### JSP与JavaBean搭配使用的好处如下：

（1）使用HTML与Java程序分离；

（2）JSP侧重于动态生成网页，事务处理由JavaBean来完成，这样可以充分利用JavaBean组件的可重用性特点；

### 一个标准的JavaBean有以下几个特性：

（1）JavaBean是一个公共的类；

（2）是一个不带参数的构造方法；

（3）通过set方法设置属性，通过get方法获取属性；

（4）属性名和get方法名之间存在固定的对应关系；

（5）属性名和set方法名之间存在固定的对应关系；

（6）如果希望JavaBean能被持久化，那么可以时它实现java.io.Serializable接口；

（在JavaBean中除了定义get方法和set方法，也可以像普通java类那样定义其他完成特定功能的方法）

```java
public class CountBean{
    private int count=0; 
    public CountBean(){}
    public int getCount(){
        return count;
    }
    public void setCount(int count){
        this.count=count;
    }
}
```
# 二、JSP访问JavaBean的语法
在JSP网页中既可以通过程序代码来访问JavaBean，也可以通过特定的JSP标签来访问JavaBean。采用后一种方法，可以减少JSP网页中的程序代码；
## 导入JavaBean类
通过<%@ page import>指令引入JavaBean类
```java
    <%@ page import="mypack.CountBean"%>
```
## 声明JavaBean对象
<jsp:useBean>标签用来声明JavaBean对象：
```java
    <jsp:useBean id="myBean" class="mypack.CounterBean" scope="session" />
```
上述代码声明了一个名字为"myBean"的JavaBean对象；<jsp:useBean>标签具有以下属性：

id属性：JSP规范要求每个JavaBean对象都有唯一的ID；

class属性：用来指定JavaBean的类名；

scope属性：用来指定JavaBean对象的存放范围，可选择包括page(页面范围）/request(请求范围)/session(会话范围)/application（Web应用范围），scope的默认值为page;

###  <jsp:useBean>标签的处理流程如下：
（1）定义一个myBean的局部变量；
（2）从scope指定的会话范围内读取名为"myBean"的属性，并且使得myBean局部变量引用具体的属性值；
（3）如果在scope指定的会话范围内，名为"myBean”的属性不存在，那么通过CountBean类的默认构造方法创建一个CounterBean对象，并把它存放在会话范围内，令其属性名为"myBean";
```java       //以上<jsp:useBean>标签和以下java程序片段的作用是等价的；
    mypack.CounterBean mybean = null;
    myBean = (mypack.CounterBean) session.getAttribute("myBean");
    if (myBean==null){
        myBean = new mypack.CounterBean();
        session.setAttribute("myBean",myBean);
    }
```
## 访问JavaBean属性
JSP提供了访问JavaBean属性的标签，要将JavaBean的某个属性输出到网页上，可用<jsp:getProperty>标签：
```java
    <jsp:getProperty name="myBean" property="count"/>
```
以上<jsp:getProperty>标签根据name属性的值"myBean"找到<jsp:useBean>标签声明的ID为“myBean”的CountBean对象，然后打印它的属性。等价于以下java程序片段
```java
    <%=myBean.getCount() %>
```
Servlet容器在运行<jsp:getProperty>标签时，会根据property属性指定的属性名，自动调用JavaBean的相应的get方法。


如果想要给JavaBean的某个属性赋值，可以用<jsp:setProperty>标签：
```java
    <jsp:setProperty name="myBean" property="count" value="1" />
```
以上<jsp:setProperty>标签根据name属性的值"myBean",找到由<jsp:useBean>标签声明的ID位"myBean"的CounterBean对象，然后给它的count属性赋值；以上<jsp:setProperty>标签等价于以下代码：
```java
    <% myBean.setCount(1);%>
```
# 三、Java的范围
在<jsp:useBean>标签中可以设置JavaBean的scope属性，scope属性决定了JavaBean对象存在的范围。scope属性的默认值;scope的可选值包括以下几种：

page：是scope属性的默认值；

request:表示请求范围；

session：表示会话范围；

application：表示web应用范围；





# 四、在bookstore应用中访问
bookstore应用创建了两个JavaBean:BookDB类和ShoppingCart类；
## 访问BookDB类
BookDB类负责访问数据库、查询BOOKS表的数据以及购书事务；在common.jsp中声明了一个类型为BookDB类的JavaBean：
```java
    <jsp:useBean id="bookDB" scope="application" class="mypack.BookDB" />
```
以上<jsp:useBean>标签的scope属性为"application"，这意味着在整个web应用范围内只有一个名为"bookDB"的BookDB对象，所有通过<%@ include>指令包含了common.jsp的其他JSP文件都可以访问这个BookDB对象。

由于<jsp:useBean>标签会定义一个引用BookDB对象的bookDB局部变量，因此在JSP文件的java程序片段中可以直接通过bookDB局部变量来引用BookDB对象。
```java    //示例如下
<%@ page contentType="text/html;charset=GB2312" %>
<%@ include= file="coommon.jsp" %>
<%@ page import="java.util" %>

<html><head><title>TitleBookDescription</title></head>
<%@ include file="banner.jsp" %>
<br>

<%
//读取bookID
String bookID=request.getParameter("bookId");
if(bookId=null) bookId="201";
BookDetails book=bookDB.getBookDetails(bookId);
%>
.....
```
## 访问ShoppingCart类
ShoppingCart类代表了虚拟的购物车；在catalog.jsp、showcart.jsp、casher.jsp和receipt.jsp中均访问ShoppingCart对象；以下是声明ShoppingCart对象的代码：
```java
    <jsp:useBean id="cart" scope="session" class="mypack.ShoppingCart"/>
```
bookstore应用中的会话代表了客户的一次购物活动，从选购书开始，到付账结束。ShoppingCart对象保存在会话中，可以用来跟踪客户的购书信息。当客户付账时，服务端就可以根据ShoppingCart对象中的信息来计算客户应支付的金额；
### catalog.jsp访问ShoppingCart
catalog.jsp中如果对某本书选择“加入购物车”链接，catalog.jsp就会把这本书的信息加入到该客户的会话范围内的ShoppongCart对象中;
```java
    //向购物车内加入一本书
    String bookId = request.getParameter("Add");
    if(bookId!=null){
        BookDetails book = bookDB.getBookDetails(bookId);
        cart.add(bookId,book);
    }
```
### showcart.jsp访问ShoppingCart
showcart.jsp从ShoppingCart对象中读取所有的ShoppingCartItem对象，然后从ShoppingCartItem对象中读取BookDetails对象，并且将这些数据输出到网页上。
如果客户在showcart.jsp网页上选择“删除”链接就会执行如下操作：
```java
    String bookId = request.getParameter("Remove");
    if(bookId !=null){
        cart.remove(bookId);     //从购物车中删除一本书
        BookDetails book = bookDB.getBookDetails(bookId);
    }
```

### casher.jsp访问ShoppingCart
casher.jsp从ShoppingCart对象中获取客户购买书的总数量和总金额，然后输出到网页上，此外还提供了让客户输入银行卡账号的 表单。
```java
    <p>你一共购买了<%=cart.getNumberOfItems()%>本书<p>
    <p>你应支付的金额<%=cart.getTotal()%>元<p>
```
```java
<%@ page contentType="text/html;charset=GB2312" %>
<%@ include= file="coommon.jsp" %>
<%@ page import="java.util" %>

<html><head><title>TitleCashier</title></head>
<%@ include file="banner.jsp" %>
    <p>你一共购买了<%=cart.getNumberOfItems()%>本书<p>
    <p>你应支付的金额<%=cart.getTotal()%>元<p>
```
### receipt.jsp访问ShoppingCart
receipt.jsp把ShoppingCart对象作为参数传给BookDB对象的buyBooks()方法：
```java
    <jsp:useBean id="cart" scope="session" class="mypack.ShoppingCart" /?
    <%
        bookDB.buyBooks(cart);
        session.invalidate();
    %>
```
# 小结



















