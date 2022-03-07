<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 14.10.2021
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="<c:url value="/resources/css/header.css" />" rel="stylesheet">

<div class="page-header">
    <h1><spring:message code="label.header"/></h1>
</div>

<table border="1" style="width:100%" >
    <tr>
        <th><h3></h3></th>
        <th>
<sec:authorize access="hasAnyRole('ROLE_ROBPROG', 'ROLE_MANAGER')">
    <a href="/inUser.html"><spring:message code="label.user"/></a>
</sec:authorize>
    </th>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <a href="/inAdmUsers.html"><spring:message code="label.users"/></a></th>
</sec:authorize>
<sec:authorize access="hasAnyRole('ROLE_ROBPROG', 'ROLE_MANAGER', 'ROLE_ADMIN')">
        <th><a href="/inStations.html"><spring:message code="label.stations"/></a></th>
</sec:authorize>
    </tr>
</table>

<!--<span style="...">
    <a style="..." href="?lang=pl">pl</a> | <a style="..." href="?lang=en">en</a> | <a style="..." href="?lang=de">de</a>
</span>-->
<br>

<form id="langForm" action="" method="get">
    <span style="...">
        <select size="1" name="lang" onchange="form.submit()">
            <option value="pl" <spring:message code="selected.selectedPL"/>>PL</option>
            <option value="en" <spring:message code="selected.selectedEN"/>>EN</option>
            <option value="de" <spring:message code="selected.selectedDE"/>>DE</option>
        </select>
    </span>
</form>