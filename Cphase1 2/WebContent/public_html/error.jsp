<%@page contentType="text/html"  pageEncoding="UTF-8"
              isErrorPage="true"%>
<%@page import="java.io.PrintWriter"%>
<html>
<head><title>錯誤</title></head>
<body>
  <h1>網頁發生錯誤：</h1><%= exception %>
  <h2>顯示例外堆疊追蹤：</h2>
<%
    exception.printStackTrace(new PrintWriter(out));
%>
</body>
</html>