<%-- 
    Document   : transfer-success
    Created on : Nov 27, 2021, 9:36:27 PM
    Author     : Nguyen Ba Tuan Anh <anhnbt.it@gmail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Transfer success!</h1>
        <p>Đã chuyển thành công số tiền: <s:property value="amount"></s:property> cho <s:property value="accountIdReceiver"></s:property></p>
    </body>
</html>
