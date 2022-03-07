<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 20.10.2021
  Time: 21:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
    <style>
        .error{
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid transparent;
            border-radius: 4px;
            color: #f87305;
            background-color: #ecd4c7;
            border-color: #eab4bb;
        }
        .msg{
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid transparent;
            border-radius: 4px;
            color: #00ff96;
            background-color: rgba(199, 243, 221, 0.76);
            border-color: #7494f1;
        }
        .login-box{
            width: 300px;
            padding: 20px;
            margin: 10px auto;
            background: #dbe8e5;
            -webkit-border-radius: 2px;
            -moz-border-radius: 2px;
            border: 1px solid #14141c;
        }
    </style>
</head>
<body >
<h3><spring:message code="label.login.hello"/></h3>
REGISTERED

</body>
</html>
