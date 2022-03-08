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
                    <td>${selectedRobot.robotNumber}</td>
                </tr>

            </table></h3>

            </th>
        <th><div class="float-right">
            <sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')">
                <c:if test="${managerB}">
            <h3><spring:message code="label.addRobprogs"/></h3>
            </c:if>
            </sec:authorize>
        </div></th>
    </tr>
    <tr>
        <td>
            <form:form method="post" action="inRobotAddStatus.html" modelAttribute="addStatus">
                <form:hidden path="id" modelAttribute="selectedRobot"/>
            <table>
                <tr>

                <th>
                    <spring:message code="label.tz"/>
                </th>
                <th>
                    <spring:message code="label.vel_pot"/>
                </th>
                    <th>
                        <spring:message code="label.vel_prod"/>
                    </th>
                    <th>
                        <spring:message code="label.pot"/>
                    </th>
                    <th>
                        <spring:message code="label.prod"/>
                    </th>
                    <th>
                        <spring:message code="label.accepted"/>
                    </th>
                    <th>
                        <spring:message code="label.comment"/>
                    </th>
                    </tr>
                <tr>
                    <td><form:input path="tz" /></td>
                    <td><form:input path="vel_pot" /></td>
                    <td><form:input path="vel_prod" /></td>
                    <td><input type="checkbox" name="potCheck" id="i1" value="true" ${addStatus.pot == true ? 'checked' : ''}/></td>
                    <td><input type="checkbox" name="prodCheck" id="i2" value="true" ${addStatus.prod == true ? 'checked' : ''}/></td>
                    <td><input type="checkbox" name="acceptedCheck" id="i3" value="true" ${addStatus.accepted == true ? 'checked' : ''}/></td>
                    <td><form:input path="comment" /></td>
                </tr>
                    <td>
                        <input class="btn btn-primary" type="submit" value="<spring:message code="button.submit"/>"/>
                    </td>
                </tr>
            </table>
            </form:form>
        </td>
        <td>

            <sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')">
                <c:if test="${managerB}">
                <form:form method="post" action="inRobotAddRobprog.html" modelAttribute="addRobprog">
            <div class="form-group">
                <form:hidden path="station.id" modelAttribute="selectedRobot"/>
                <form:hidden path="id" modelAttribute="selectedRobot"/>
                <div class="overflow-auto" style="max-height: 240px;">
                <table class="table table-hover" >


                    <c:forEach items="${robprogs}" var="zaUser">
                        <tr><td><input type="checkbox" class="btn-check" autocomplete="off" id="btn-check-outlined ${zaUser.id}" name="ZaIds" value="${zaUser.id}"

                        <c:forEach items="${robprogsRobot}" var="locRobot">

                            ${zaUser.id == locRobot.id ? 'checked' : ''}

                        </c:forEach>

                        >
                            <label class="btn btn-outline-primary" for="btn-check-outlined ${zaUser.id}"> ${zaUser.pesel}<br>${zaUser.firstName} ${zaUser.lastName}</label></td></tr>


                    </c:forEach>


                </table>
            </div>
                <input class="btn btn-primary" type="submit" value="<spring:message code="button.submit"/>"/>
            </div>
                </form:form>
                </c:if>
            </sec:authorize>

        </td>
    </tr>


    <tr colspan="2">
        <td>
            <h3>
                <spring:message code="label.statusList"/>
            </h3>
            <c:if test="${!empty robotStatusList}">
                <table class="table table-striped">
                    <tr>
                        <th>
                            <spring:message code="label.tz"/>
                        </th>
                        <th>
                            <spring:message code="label.vel_pot"/>
                        </th>
                        <th>
                            <spring:message code="label.vel_prod"/>
                        </th>
                        <th>
                            <spring:message code="label.pot"/>
                        </th>
                        <th>
                            <spring:message code="label.prod"/>
                        </th>
                        <th>
                            <spring:message code="label.accepted"/>
                        </th>
                        <th>
                            <spring:message code="label.comment"/>
                        </th>
                        <th>
                            <spring:message code="label.data"/>
                        </th>
                        <th>
                            <spring:message code="label.email"/>
                        </th>

                    </tr><c:forEach items="${robotStatusList}" var="statusL">
                    <tr>
                        <td>${statusL.tz}</td>
                        <td>${statusL.vel_pot}</td>
                        <td>${statusL.vel_prod}</td>
                        <td>${statusL.pot}</td>
                        <td>${statusL.prod}</td>
                        <td>${statusL.accepted}</td>
                        <td>${statusL.comment}</td>
                        <td>${statusL.data.getHours()}:${statusL.data.getMinutes()}:${statusL.data.getSeconds()} ${statusL.data.getDate()}.${statusL.data.getMonth()}.${statusL.data.getYear()}</td>
                        <td>${statusL.robotyk.getEmail()}</td>

                    </tr>
                </c:forEach>
                </table>
            </c:if>
        </td>
    </tr>


</table>




</body>
</html>