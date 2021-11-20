<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 21.10.2021
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UserRole</title>
</head>
<body>
<form:form method="post" action="addUserRole.html" modelAttribute="userRole">
    <table>
        <tr>
            <td>
                <form:hidden path="id"/>
            </td>
        </tr>
        <tr>
            <td><form:label path="role"><spring:message code="label.role" /></form:label></td>
            <td><form:input path="role" /></td>
            <td><form:errors path="role" /></td>
        </tr>

        <tr>
            <td colspan="2">
                <input type="submit" value="<spring:message code="label.addUserRole"/>" />
            </td>
        </tr>
    </table>
</form:form>

</body>
</html>
