<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 03.11.2021
  Time: 14:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h3>
    <spring:message code="label.userList"/>
</h3>
<c:if test="${!empty userAppList}">
    <table class="table table-striped">
        <tr>
            <th><spring:message code="label.firstName"/></th>
            <th><spring:message code="label.lastName"/></th>
            <th><spring:message code="label.email"/></th>
            <th><spring:message code="label.mobile"/></th>
            <th><spring:message code="label.activated"/></th>
            <th><spring:message code="label.role"/></th>
            <th>&nbsp;</th>
            <th>&nbsp;</th>
        </tr><c:forEach items="${userAppList}" var="userApp">
        <tr>
            <td>${userApp.firstName}</td>
            <td>${userApp.lastName}</td>
            <td>${userApp.email}</td>
            <td>${userApp.telephone}</td>
            <td><a type="button" class="
                    <c:if test="${userApp.enabled}">
                        btn btn-outline-info btn-sm
                    </c:if>
                    <c:if test="${!userApp.enabled}">
                        btn btn-outline-warning btn-sm
                    </c:if>"
                   href="inAdmActivateUser.html?userId=${userApp.id}&e=${userApp.enabled}">${userApp.enabled}</a></td>
            <td><c:forEach items="${userApp.userRole}" var="userRole">${userRole.getRole()}<br></c:forEach></td>
            <td><a type="button" class="btn btn-danger btn-sm" href="admUserDelete/${userApp.id}.html"><spring:message code="label.delete"/></a></td>
            <td><a type="button" class="btn btn-secondary btn-sm" href="inAdmEditUser.html?userId=${userApp.id}"><spring:message code="label.edit"/></a></td>
            <!--<td><a href="generatePdf-${userApp.id}">pdf</a></td>-->
        </tr>
    </c:forEach>
    </table>
</c:if>

</body>
</html>
