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
<form:form method="post" action="addAppUSR.html" modelAttribute="userApp"><!--modelAttribute="userApp" musi sie pokrywać z tym co jest w AddUserControl.java attributeName "userApp" -->

    <table>
        <tr>
            <td>
                <form:hidden path="id"/>
            </td>
        </tr>

        <tr>
            <td><form:label path="login"><spring:message code="label.login"/></form:label></td>
            <td><form:input path="login" /></td>
            <td><form:errors path="login" /></td>
        </tr>
        <tr>
            <td><form:label path="password"><spring:message code="label.password"/></form:label></td>
            <td><form:input path="password" type="password" /></td>
            <td><form:errors path="password" /></td>
        </tr>
        <tr>
            <td><form:label path="enabled"><spring:message code="label.enabled"/></form:label></td>
            <td><form:checkbox path="enabled" /></td>
            <td><form:errors path="enabled" /></td>
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
        <tr>
            <td><form:label path="age"><spring:message code="label.age"/>:</form:label></td>
            <td><form:input path="age" /></td>
            <td><p style="font-size:10px; color:red"><form:errors path="age"/></p></td>
        </tr>
        <tr>
            <td><form:label path="pesel.PESEL"><spring:message code="label.pesel"/>:</form:label></td>
            <td><form:input path="pesel.PESEL" /></td>
            <td><p style="font-size:10px; color:red"><form:errors path="pesel"/></p></td>
        </tr>
        <tr>
            <td><form:label path="address"><spring:message code="label.address"/>:</form:label></td>
            <td><form:select path="address" >
                <c:forEach items="${addressesList}" var="address">
                    <option value="${address.id}" ${address.id == selectedAddress ? 'selected="selected"' : ''}>${address.street}</option>
                </c:forEach>
            </form:select>
            </td>
            <td><p style="font-size:10px; color:red"><form:errors path="address"/></p></td>
        </tr>
        <tr>
            <td><form:label path="userRole"><spring:message code="label.role"/>:</form:label></td>
            <td><form:select path="userRole" multiple="true">
                <form:options items="${userRoleList}" itemValue="id" itemLabel="role" />
            </form:select></td>
            <td><p style="font-size:10px; color:red"><form:errors path="pesel"/></p></td>
        </tr>
        <tr>
            <td colspan="3">
                <div class="g-recaptcha" data-sitekey="6Ld8j_EcAAAAACnInrwh8xGi2YJ7X49FaxjQaa_L"></div>
            </td>
        </tr>

        <td colspan="2">
            <c:if test="${userApp.id==0}">
                <input type="submit" value="<spring:message code="button.addAppUser"/>"/>
            </c:if>
            <c:if test="${userApp.id!=0}">
                <input type="submit" value="<spring:message code="button.editAppUser"/>"/>
            </c:if>
        </td>
    </table>

</form:form>
<h3>
    <spring:message code="label.userList"/>
</h3>
<c:if test="${!empty userAppList}">
    <table class="data">
        <tr>
            <th><spring:message code="label.firstName"/></th>
            <th><spring:message code="label.lastName"/></th>
            <th><spring:message code="label.email"/></th>
            <th><spring:message code="label.mobile"/></th>
            <th><spring:message code="label.age"/></th>
            <th>&nbsp;</th>
            <th>&nbsp;</th>
        </tr><c:forEach items="${userAppList}" var="userApp">
        <tr>
            <td>${userApp.firstName}</td>
            <td>${userApp.lastName}</td>
            <td>${userApp.email}</td>
            <td>${userApp.telephone}</td>
            <td>${userApp.age}</td>
            <td><a href="delete/${userApp.id}.html">delete</a></td>
            <td><a href="appUsers.html?userAppId=${userApp.id}">edit</a></td>
            <td><a href="generatePdf-${userApp.id}">pdf</a></td>
        </tr>
        </c:forEach>
    </table>
</c:if>


</body>
</html>
