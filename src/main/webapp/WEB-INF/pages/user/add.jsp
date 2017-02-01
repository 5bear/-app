<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2017/1/22
  Time: 21:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加用户</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/User/add" method="post">
    <input name="username"  required  placeholder="用户名">
    <input name="password" required placeholder="密码">
    <input name="dcName"  required placeholder="仓库">
    <%--
        <input name="realCount" required type="number" placeholder="箱数">
    --%>
    <%--
        <input name="emptyCount" required type="number" placeholder="空白数">
    --%>
    <input type="submit">
</form>
</body>
</html>
