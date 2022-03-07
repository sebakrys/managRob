<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<table>
    <tr>
        <td>
<sec:authorize access="hasRole('ROLE_ADMIN')">
            <form:form method="post" action="inAddNewStation.html" modelAttribute="addStation"><!--modelAttribute="userApp" musi sie pokrywać z tym co jest w AddUserControl.java attributeName "userApp" -->
            <table>
                <tr>
                    <td>

                        <form:hidden path="id"/>
                    </td>
                </tr>

                <tr>
                    <td><form:label path="nazwa"><spring:message code="label.bName"/></form:label></td>
                    <td><form:input path="nazwa" /></td>
                    <td><form:errors path="nazwa" /></td>
                </tr>





                <tr>
                    <td><form:label path="stationNumber"><spring:message code="label.number"/>:</form:label></td>
                    <td><form:input path="stationNumber"/></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="stationNumber"/></p></td>
                </tr>
                <tr>
                    <td><form:label path="street"><spring:message code="label.street"/>:</form:label></td>
                    <td><form:input path="street"/></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="street"/></p></td>
                </tr>
                <tr>
                    <td><form:label path="postalCode"><spring:message code="label.PostalCode"/>:</form:label></td>
                    <td><form:input path="postalCode"/></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="postalCode"/></p></td>
                </tr>
                <tr>
                    <td><form:label path="city"><spring:message code="label.City"/>:</form:label></td>
                    <td><form:input path="city"/></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="city"/></p></td>
                </tr>


                <td colspan="2">
                    <input type="submit" class="btn btn-primary" value="<spring:message code="button.addStation"/>"/>
                </td>
            </table>
            </form:form>
</sec:authorize>
        </td>
    </tr>
    <tr colspan="2">
        <td>
            <h3>
                <spring:message code="label.stationsList"/>
            </h3>
            <c:if test="${!empty stationsList}">
                <table class="table table-striped">
                    <tr>
                        <th><spring:message code="label.bName"/></th>
                        <th><spring:message code="label.number"/></th>
                        <th><spring:message code="label.street"/></th>
                        <th><spring:message code="label.PostalCode"/></th>
                        <th><spring:message code="label.City"/></th>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <th>&nbsp;</th>
                        </sec:authorize>
                        <th>&nbsp;</th>

                    </tr><c:forEach items="${stationsList}" var="stationsL">
                    <tr>
                        <td>${stationsL.nazwa}</td>
                        <td>${stationsL.stationNumber}</td>
                        <td>${stationsL.street}</td>
                        <td>${stationsL.postalCode}</td>
                        <td>${stationsL.city}</td>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <td><a type="button" class="btn btn-danger btn-sm" href="deleteStation/${stationsL.id}.html"><spring:message code="label.delete"/></a></td>
                        </sec:authorize>

                        <td>

                            <a type="button" class="btn btn-<c:if test="${!stationsManagList.contains(stationsL.id)}">secondary</c:if><c:if test="${stationsManagList.contains(stationsL.id)}">primary</c:if> btn-sm" href="stationRobotsManag.html?bId=${stationsL.id}">
                                <spring:message code="label.robots"/>
                                <c:if test="${stationsManagList.contains(stationsL.id)}">
                                    <spring:message code="label.AndManag"/>
                                </c:if>
                            </a></td>

                    </tr>
                </c:forEach>
                </table>
            </c:if>
        </td>
    </tr>
</table>




</body>
</html>
