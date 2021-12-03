<%-- 
    Document   : transfer-money
    Created on : Nov 27, 2021, 7:20:15 PM
    Author     : Nguyen Ba Tuan Anh <anhnbt.it@gmail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Transfer money</title>
    </head>
    <body>
        <h1>Transfer money</h1>
        <p>Hello, <s:property value="username"></s:property>!</p>
        <p>Your amount: <s:property value="totalAmount"></s:property></p>
        <s:form action="transfer">
            <s:hidden name="accountIdRequest" value="%{id}"></s:hidden>
            <s:hidden name="totalAmount" value="%{totalAmount}"></s:hidden>
            <s:textfield name="accountIdReceiver" label="Người nhận"></s:textfield>
            <s:textfield name="amount"label="Số tiền"></s:textfield>
            <s:submit value="Submit"></s:submit>
        </s:form>
    </body>
</html>
