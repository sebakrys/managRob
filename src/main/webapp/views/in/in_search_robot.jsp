<%--
  Created by IntelliJ IDEA.
  User: Hitman
  Date: 16.03.2022
  Time: 21:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_ROBPROG')">
<html>
<head>

    <c:if test="${!empty object}">
    <table class="table table-striped">
        <tr>
            <th><spring:message code="label.project"/></th>
            <th><spring:message code="label.station"/></th>
            <th><spring:message code="label.robot"/></th>
            <th>&nbsp;</th>
            <th>&nbsp;</th>
        </tr>
        <tr>
            <c:forEach items="${object}" var="objectL">
                <td>${objectL.nazwaProjektu}</td>
                <td>${objectL.nazwaStacji}</td>
                <td>${objectL.nazwaRobota}</td>
                <td><a ype="button" class="btn btn-secondary btn-sm" href="inRobotManag.html?fId=${objectL.numerIdRobota}">
                    <spring:message code="label.robotAndMedia"/></a></td>
                <tr></tr>
            </c:forEach>
        </tr>
    </table>
    </c:if>

</head>
<body>
</body>
</html>
</sec:authorize>