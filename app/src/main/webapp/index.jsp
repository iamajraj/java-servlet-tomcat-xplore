<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Query Params</title></head>
<body>
<h2>Query Parameters</h2>
<ul>
<%
    var params = request.getParameterMap();
    for (var key : params.keySet()) {
        out.println("<li>" + key + " = " + String.join(", ", params.get(key)) + "</li>");
    }
%>
</ul>
</body>
</html>
