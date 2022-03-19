<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<table width="100%">
    <tr>
        <th>
            <h3><table>
                <tr>
                    <td>${selectedProject.nazwa}</td>
                </tr>
                <tr>
                    <td>${selectedProject.country}, ${selectedProject.city}, ${selectedProject.standard}</td>
                </tr>

            </table></h3>

            </th>
        <th><div class="float-right">
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <h3><spring:message code="label.addManagers"/></h3>

            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_MANAGER') && !hasRole('ROLE_ADMIN')">
                <h3><spring:message code="label.Managers"/></h3>

            </sec:authorize>
        </div></th>
    </tr>
    <tr>
        <td>
            <sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')">
                <c:if test="${managerB}">



            <form:form method="post" action="inAddNewStation.html" modelAttribute="addStation"><!--modelAttribute="userApp" musi sie pokrywać z tym co jest w AddUserControl.java attributeName "userApp" -->
            <table class="data">
                <tr>
                    <td>

                        <form:hidden path="id"/>
                        <form:hidden path="project.id"/>

                    </td>
                </tr>

                <tr>
                    <td><form:label path="nazwa"><spring:message code="label.bName"/></form:label></td>
                    <td><form:input path="nazwa" /></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="nazwa" /></p></td>
                </tr>

                <tr>
                    <td><form:label path="hala"><spring:message code="label.Hala"/>:</form:label></td>
                    <td><form:input path="hala"/></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="hala"/></p></td>
                </tr>
                <tr>
                    <td><form:label path="linia"><spring:message code="label.Linia"/>:</form:label></td>
                    <td><form:input path="linia"/></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="linia"/></p></td>
                </tr>
                <tr>
                    <td><form:label path="sterownik"><spring:message code="label.Sterownik"/>:</form:label></td>
                    <td><form:input path="sterownik"/></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="sterownik"/></p></td>
                </tr>
                <td colspan="1">
                    <input class="btn btn-primary" type="submit" value="<spring:message code="button.addStation"/>"/>
                </td>
            </table>
            </form:form>
                </c:if>
</sec:authorize>
        </td>
        <td>
<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')">

                <form:form method="post" action="inProjectAddManager.html" modelAttribute="addManager">

            <div class="form-group">
                <form:hidden path="project.id" modelAttribute="addStation"/>
                <div class="overflow-auto" style="max-height: 260px;">
                <table class="table<sec:authorize access="hasRole('ROLE_ADMIN')"> table-hover</sec:authorize>" >


                    <c:forEach items="${managers}" var="zaUser">
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <tr><td><input type="checkbox" class="btn-check" autocomplete="off" id="btn-check-outlined ${zaUser.id}" name="ZaIds" value="${zaUser.id}"

                        <c:forEach items="${managersStacji}" var="managersStacja">

                            ${zaUser.id == managersStacja.id ? 'checked' : ''}

                        </c:forEach>

                        >
                            <label class="btn btn-outline-primary" for="btn-check-outlined ${zaUser.id}"> ${zaUser.pesel}<br>${zaUser.firstName} ${zaUser.lastName}</label></td></tr>

                        </sec:authorize>
                        <sec:authorize access="hasRole('ROLE_MANAGER')">
                            <sec:authorize access="!hasRole('ROLE_ADMIN')">
                            <c:forEach items="${managersStacji}" var="managersStacja">
                                <c:if test="${zaUser.id == managersStacja.id}">
                                    <tr><td><span class="badge rounded-pill bg-primary">${zaUser.email}<br>${zaUser.firstName} ${zaUser.lastName}</span></td></tr>
                                </c:if>


                            </c:forEach>
                            </sec:authorize>
                        </sec:authorize>
                    </c:forEach>


                </table>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
            </div>
                <input class="btn btn-primary" type="submit" value="<spring:message code="button.submit"/>"/>
            </div>
                    </sec:authorize>
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
                        <th><spring:message code="label.Hala"/></th>
                        <th><spring:message code="label.Linia"/></th>
                        <th><spring:message code="label.bName"/></th>
                        <th><spring:message code="label.Sterownik"/></th>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <th>&nbsp;</th>
                        </sec:authorize>
                        <th>&nbsp;</th>

                    </tr><c:forEach items="${stationsList}" var="stationsL">
                    <tr>
                        <td>${stationsL.hala}</td>
                        <td>${stationsL.linia}</td>
                        <td>${stationsL.nazwa}</td>
                        <td>${stationsL.sterownik}</td>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <td><a type="button" class="btn btn-danger btn-sm" href="deleteStation/${stationsL.id}.html?bId=${selectedProject.id}"><spring:message code="label.delete"/></a></td>
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
