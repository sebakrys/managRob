<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 13.10.2021
  Time: 21:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.userAdded"/></title>
</head>
<body>
<h1><spring:message code="label.yourData"/></h1>
<table>
    <tr>
        <td><spring:message code="label.firstName"/>:</td>
        <td>${firstname}</td>
    </tr>
    <tr>
        <td><spring:message code="label.lastName"/>Last Name:</td>
        <td>${lastname}</td>
    </tr>
    <tr>
        <td><spring:message code="label.email"/>:</td>
        <td>${email}</td>
    </tr>
    <tr>
        <td><spring:message code="label.mobile"/>:</td>
        <td>${tel}</td>
    </tr>
    <tr>
        <td><spring:message code="label.age"/>:</td>
        <td>${age}</td>
    </tr>
</table>
</body>
</html>
