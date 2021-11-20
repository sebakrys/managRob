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
                    <td>${selectedBuilding.nazwa}</td>
                </tr>
                <tr>
                    <td>Ul. ${selectedBuilding.street} ${selectedBuilding.buildingNumber}</td>
                </tr>
                <tr>
                    <td>${selectedBuilding.postalCode}, ${selectedBuilding.city}</td>
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
            <sec:authorize access="hasAnyRole('ROLE_ZARZADCA', 'ROLE_ADMIN')">
                <c:if test="${zarzadcaB}">



            <form:form method="post" action="inAddNewFlat.html" modelAttribute="addFlat"><!--modelAttribute="userApp" musi sie pokrywać z tym co jest w AddUserControl.java attributeName "userApp" -->
            <table class="data">
                <tr>
                    <td>

                        <form:hidden path="id"/>
                        <form:hidden path="building.id"/>

                    </td>
                </tr>

                <tr>
                    <td><form:label path="flatNumber"><spring:message code="label.newFlatNumber"/></form:label></td>
                    <td><form:input path="flatNumber" /></td>
                    <td><p style="font-size:10px; color:red"><c:if test="${flatExists}">
                        <spring:message code="error.alreadyExists"/>
                    </c:if></p>
                    </td>
                </tr>
                <td colspan="1">
                    <input class="btn btn-primary" type="submit" value="<spring:message code="button.addFlat"/>"/>
                </td>
            </table>
            </form:form>
                </c:if>
</sec:authorize>
        </td>
        <td>
<sec:authorize access="hasRole('ROLE_ADMIN')">
                <form:form method="post" action="inBuildingAddZarzadca.html" modelAttribute="addZarzadca">
            <div class="form-group">
                <form:hidden path="building.id" modelAttribute="addFlat"/>
                <div class="overflow-auto" style="max-height: 260px;">
                <table class="table table-hover" >


                    <c:forEach items="${zarzadcy}" var="zaUser">
                        <tr><td><input type="checkbox" class="btn-check" autocomplete="off" id="btn-check-outlined ${zaUser.id}" name="ZaIds" value="${zaUser.id}"

                        <c:forEach items="${zarzadcyBudynku}" var="zarzadcyBud">

                            ${zaUser.id == zarzadcyBud.id ? 'checked' : ''}

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
                <spring:message code="label.flatsList"/>
            </h3>
            <c:if test="${!empty flatsList}">
                <table class="table table-striped">
                    <tr>
                        <th><spring:message code="label.number"/></th>
                <c:if test="${zarzadcaB}">
                        <th>&nbsp;</th>
                </c:if>
                        <th>&nbsp;</th>
                    </tr><c:forEach items="${flatsList}" var="flatsL">
                    <tr>
                        <td>${flatsL.flatNumber}</td>
                        <c:if test="${zarzadcaB}">
                        <td><a type="button" class="btn btn-danger btn-sm" href="deleteFlat/${flatsL.id}.html?bId=${selectedBuilding.id}"><spring:message code="label.delete"/></a></td>
                        </c:if>
                        <td><a ype="button" class="btn btn-secondary btn-sm" href="inFlatManag.html?fId=${flatsL.id}"><spring:message code="label.flatAndMedia"/></a></td>

                    </tr>
                </c:forEach>
                </table>
            </c:if>
        </td>
    </tr>
</table>




</body>
</html>
