<%-- 
    Document   : index
    Created on : Nov 27, 2021, 7:12:09 PM
    Author     : Nguyen Ba Tuan Anh <anhnbt.it@gmail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Struts 2 - Login Application</title>
    </head>
    <body>
        <h1>Struts 2 - Login Application</h1>
        <p><s:actionerror/></p>
        <s:form action="login" method="post">
            <s:textfield name="username" key="label.username"></s:textfield>
            <s:password name="password" key="label.password"></s:password>
            <s:submit key="label.login" method="execute"></s:submit>
        </s:form>
    </body>
</html>
