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
                    <td>${selectedStation.nazwa}</td>
                </tr>
                <tr>
                    <td>${selectedStation.hala}, ${selectedStation.sterownik}</td>
                </tr>

            </table></h3>

            </th>
        <th><div class="float-right">
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <h3><spring:message code="label.addManagers"/></h3>

            </sec:authorize>
        </div></th>
    </tr>
    <tr>
        <td>
            <sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')">
                <c:if test="${managerB}">



            <form:form method="post" action="inAddNewRobot.html" modelAttribute="addRobot"><!--modelAttribute="userApp" musi sie pokrywać z tym co jest w AddUserControl.java attributeName "userApp" -->
            <table class="data">
                <tr>
                    <td>

                        <form:hidden path="id"/>
                        <form:hidden path="station.id"/>

                    </td>
                </tr>

                <tr>
                    <td><form:label path="robotNumber"><spring:message code="label.newRobotNumber"/></form:label></td>
                    <td><form:input path="robotNumber" /></td>
                    <td><p style="font-size:10px; color:red"><c:if test="${robotExists}">
                        <spring:message code="error.alreadyExists"/>
                    </c:if></p>
                    </td>
                </tr>
                <td colspan="1">
                    <input class="btn btn-primary" type="submit" value="<spring:message code="button.addRobot"/>"/>
                </td>
            </table>
            </form:form>
                </c:if>
</sec:authorize>
        </td>
        <td>
<sec:authorize access="hasRole('ROLE_ADMIN')">
                <form:form method="post" action="inStationAddManager.html" modelAttribute="addManager">
            <div class="form-group">
                <form:hidden path="station.id" modelAttribute="addRobot"/>
                <div class="overflow-auto" style="max-height: 260px;">
                <table class="table table-hover" >


                    <c:forEach items="${managers}" var="zaUser">
                        <tr><td><input type="checkbox" class="btn-check" autocomplete="off" id="btn-check-outlined ${zaUser.id}" name="ZaIds" value="${zaUser.id}"

                        <c:forEach items="${managersStacji}" var="managersStacja">

                            ${zaUser.id == managersStacja.id ? 'checked' : ''}

                        </c:forEach>

                        >
                            <label class="btn btn-outline-primary" for="btn-check-outlined ${zaUser.id}"> ${zaUser.pesel}<br>${zaUser.firstName} ${zaUser.lastName}</label></td></tr>


                    </c:forEach>


                </table>
            </div>
                <input class="btn btn-primary" type="submit" value="<spring:message code="button.submit"/>"/>
            </div>
                </form:form>
</sec:authorize>

        </td>
    </tr>
    <tr colspan="2">
        <td>
            <h3>
                <spring:message code="label.robotsList"/>
            </h3>
            <c:if test="${!empty robotsList}">
                <table class="table table-striped">
                    <tr>
                        <th><spring:message code="label.number"/></th>
                <c:if test="${managerB}">
                        <th>&nbsp;</th>
                </c:if>
                        <th>&nbsp;</th>
                    </tr><c:forEach items="${robotsList}" var="robotsL">
                    <tr>
                        <td>${robotsL.robotNumber}</td>
                        <c:if test="${managerB}">
                        <td><a type="button" class="btn btn-danger btn-sm" href="deleteRobot/${robotsL.id}.html?bId=${selectedStation.id}"><spring:message code="label.delete"/></a></td>
                        </c:if>
                        <td><a ype="button" class="btn btn-secondary btn-sm" href="inRobotManag.html?fId=${robotsL.id}"><spring:message code="label.robotAndMedia"/></a></td>

                    </tr>
                </c:forEach>
                </table>
            </c:if>
        </td>
    </tr>
</table>




</body>
</html>
