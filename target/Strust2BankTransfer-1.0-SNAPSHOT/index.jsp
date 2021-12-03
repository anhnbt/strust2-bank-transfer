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
        <title>Bank</title>
    </head>
    <body>
        <h1>Bank!</h1>
        <p><s:actionerror/></p>
        <s:form action="login" method="post">
            <s:textfield name="username" label="Your username"></s:textfield>
            <s:password name="password" label="Your password"></s:password>
            <s:submit value="Login"></s:submit>
        </s:form>
    </body>
</html>
