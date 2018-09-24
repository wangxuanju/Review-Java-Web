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







# 三、Java的范围

# 四、在bookstore应用中访问


