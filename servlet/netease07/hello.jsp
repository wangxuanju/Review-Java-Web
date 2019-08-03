<%@ page language="java" contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PBBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.com">
<html>
<head>
<meta http-equiv="Context-Type" context="text/html;charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%! String name;%>
request uri is<%=request.getRequestURL()%>
<br/>
<% 
name="abc";
out.println("name is "+ name);
%>

</body>
</html>
