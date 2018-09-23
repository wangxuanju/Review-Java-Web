# 一、下载文件
下载文件是指把服务器端的文件发送到客户端，Servlet能够向客户端发送任意格式的文件数据。
```java
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class DownloadServlet extends HttpServlet {
  public void doGet(HttpServletRequest request,HttpServletResponse response)
         throws ServletException, IOException {
    OutputStream out; //输出响应正文的输出流
    InputStream in;  //读取本地文件的输入流
    //获得filename请求参数 
    String filename=request.getParameter("filename");
     
    if(filename==null){
      out=response.getOutputStream(); 
      out.write("Please input filename.".getBytes());
      out.close();
      return;
    }
    
    //创建读取本地文件的输入流
    in= getServletContext().getResourceAsStream("/store/"+filename);
    int length=in.available();
    //设置响应正文的MIME类型
    response.setContentType("application/force-download"); 
    response.setHeader("Content-Length",String.valueOf(length));
    response.setHeader("Content-Disposition", "attachment;filename=\""+filename +"\" "); 
    
    /** 把本地文件中的数据发送给客户 */
    out=response.getOutputStream(); 
    int bytesRead = 0;
    byte[] buffer = new byte[512];
    while ((bytesRead = in.read(buffer)) != -1){
      out.write(buffer, 0, bytesRead);
    } 
       
    in.close();
    out.close();
  }
}
```
# 二、上传文件
上传文件时把客户端的文件发送到服务器端。

# 三、动态生成图像
Servlet不仅能动态生成HTML文档，还能动态生成图像。
```java

```
# 四、读写Cookie

# 五、访问web应用的 工作目录


# 六、转发和包含


# 七、重定向


# 八、访问Servlet容器内的其他web应用


# 九、避免并发问题
