<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 14.10.2021
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><tiles:insertAttribute name="title" ignore="true"/></title>
    <style>
        html, body    {
            height: 100%;
        }
    </style>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="<c:url value="/resources/bootstrap-5.1.3-dist/js/bootstrap.min.js"/>"></script>
    <!-- Bootstrap core CSS -->
    <link href="<c:url value="/resources/bootstrap-5.1.3-dist/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/bootstrap-5.1.3-dist/css/bootstrap-grid.min.css"/>" rel="stylesheet">

</head>

<body>
<table border="0" cellpadding="2" cellspacing="2" align="center" width="100%" >
    <tr>
        <td colspan="2"><tiles:insertAttribute name="header" /></td>
    </tr>
    <tr>
        <td width="350"><tiles:insertAttribute name="menu" /></td>
        <td><tiles:insertAttribute name="body" /></td>

    </tr>
    <tr>
        <td height="30" colspan="2"><tiles:insertAttribute name="footer" />
        </td>
    </tr>

</table>

</body>
</html>
