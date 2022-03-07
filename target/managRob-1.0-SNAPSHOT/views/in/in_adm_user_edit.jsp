<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 03.11.2021
  Time: 15:20
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
        <th><h3><spring:message code="label.user"/></h3></th>
        <th><h3><spring:message code="label.role"/></h3></th>
    </tr>

    <tr>
        <td>
<form:form method="post" action="inAdmUserEdit.html" modelAttribute="admUserEdit"><!--modelAttribute="userApp" musi sie pokrywać z tym co jest w AddUserControl.java attributeName "userApp" -->
<table>
    <tr>
        <td>
            <form:hidden path="id"/>
            <form:hidden path="password"/>
            <form:hidden path="confirmPassword"/>
            <!--<form:hidden path="verifyToken.verifyToken"/>-->
        </td>
    </tr>

    <tr>
        <td><form:label path="pesel"><spring:message code="label.pesel"/></form:label></td>
        <td><form:input path="pesel" /></td>
        <td><form:errors path="pesel" /></td>
    </tr>


    <tr>
        <td><form:label path="firstName"><spring:message code="label.firstName"/>:</form:label></td>
        <td><form:input path="firstName"/></td>
        <td><p style="font-size:10px; color:red"><form:errors path="firstName"/></p></td>
    </tr>
    <tr>
        <td><form:label path="lastName"><spring:message code="label.lastName"/>:</form:label></td>
        <td><form:input path="lastName"/></td>
        <td><p style="font-size:10px; color:red"><form:errors path="lastName"/></p></td>
    </tr>
    <tr>
        <td><form:label path="email"><spring:message code="label.email"/>:</form:label></td>
        <td><form:input path="email"/></td>
        <td><p style="font-size:10px; color:red"><form:errors path="email"/></p></td>
    </tr>
    <tr>
        <td><form:label path="telephone"><spring:message code="label.mobile"/>:</form:label></td>
        <td><form:input path="telephone"/></td>
        <td><p style="font-size:10px; color:red"><form:errors path="telephone"/></p></td>
    </tr>

    <!--todo-->

    <!--todo-->


    <td colspan="2">
        <input type="submit" class="btn btn-primary" value="<spring:message code="button.editAppUser"/>"/>
    </td>
</table>
</form:form>
        </td>
    <td>


        <form:form method="POST" action="inAdmRoleUserEdit.html" modelAttribute="admUserRoleEdit">
            <form:hidden path="id"/>
            <form:hidden path="pesel"/>
            <input type="checkbox" id="admin" name="ROLE_ADMIN" value="true"
            <c:forEach items="${admUserEdit.userRole}" var="userRole">
            <c:if test="${userRole.getRole()=='ROLE_ADMIN'}">
                   checked
            </c:if>
            </c:forEach>
            >
            <label for="admin">ADMIN</label><br>
            <input type="checkbox" id="zarzadca" name="ROLE_ZARZADCA" value="true"
            <c:forEach items="${admUserEdit.userRole}" var="userRole">
            <c:if test="${userRole.getRole()=='ROLE_ZARZADCA'}">
                   checked
            </c:if>
            </c:forEach>
            >
            <label for="zarzadca">ZARZADCA</label><br>
            <input type="checkbox" id="mieszkaniec" name="ROLE_MIESZKANIEC" value="true"
            <c:forEach items="${admUserEdit.userRole}" var="userRole">
            <c:if test="${userRole.getRole()=='ROLE_MIESZKANIEC'}">
                   checked
            </c:if>
            </c:forEach>
            >
            <label for="mieszkaniec">MIESZKANIEC</label><br><br>
            <input type="submit" class="btn btn-primary" value="<spring:message code="button.submit"/>">
        </form:form>




    </td>

    </tr>
</table>


</body>
</html>
