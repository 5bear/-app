<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/12/29
  Time: 12:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h1>${message}</h1>
<form action="<%=request.getContextPath()%>/Goods/add" method="post">
    <input name="rgb"  required type="number" placeholder="颜色值">
    <input name="gType" required placeholder="产品类型缩写">
    <input name="gTypeInfo"  required placeholder="产品类型详细">
    <input name="pallets"  required type="number" placeholder="垛数">
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
