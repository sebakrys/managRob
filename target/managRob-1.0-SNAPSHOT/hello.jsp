<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 13.10.2021
  Time: 17:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="title.hello"/></title>
</head>
<body>
<h3><spring:message code="label.helloWorld"/></h3>
<a href="/appUsers"><spring:message code="button.addAppUser"/></a>
</body>
</html>
