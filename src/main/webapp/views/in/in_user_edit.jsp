<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 13.10.2021
  Time: 18:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>userPage</title>
    <!-- Captcha Google -->
    <script src="https://www.google.com/recaptcha/api.js"></script>
</head>
<body>
<h1><spring:message code="title.appUser"/></h1>
<table width="100%">
    <tr>
        <td>
            <form:form method="post" action="inUserEdit.html" modelAttribute="userEdit"><!--modelAttribute="userApp" musi sie pokrywać z tym co jest w AddUserControl.java attributeName "userApp" -->
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


                <td colspan="2">
                    <input type="submit" class="btn btn-primary" value="<spring:message code="button.editAppUser"/>"/>
                </td>
            </table>
            </form:form>
        </td>
        <td>
            <form:form method="post" action="inUserPassEdit.html" modelAttribute="userPassEdit"><!--modelAttribute="userApp" musi sie pokrywać z tym co jest w AddUserControl.java attributeName "userApp" -->
            <table>
                <tr>
                    <td>
                        <form:hidden path="id"/>
                        <form:hidden path="email"/>
                        <form:hidden path="telephone"/>
                        <form:hidden path="firstName"/>
                        <form:hidden path="lastName"/>
                        <!--<form:hidden path="verifyToken.verifyToken"/>-->
                    </td>
                </tr>

                <tr>
                    <td><form:label path="password"><spring:message code="label.password"/></form:label></td>
                    <td><form:input path="password" type="password" /></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="password" /></p></td>
                </tr>
                <tr>
                    <td><form:label path="confirmPassword" ><spring:message code="label.confirmPassword"/></form:label></td>
                    <td><form:input path="confirmPassword" type="password" /></td>
                    <td><p style="font-size:10px; color:red"><form:errors path="confirmPassword" /></p></td>
                </tr>


                <td colspan="2">
                    <input type="submit" class="btn btn-primary" value="<spring:message code="button.changePassword"/>"/>
                </td>
            </table>
            </form:form>
        </td>
    </tr>

</table>

</body>
</html>