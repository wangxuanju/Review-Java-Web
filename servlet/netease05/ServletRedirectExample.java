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
