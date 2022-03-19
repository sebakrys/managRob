<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 14.10.2021
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link href="<c:url value="/resources/css/header.css" />" rel="stylesheet">


<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="#"><spring:message code="title.Sp"/></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <sec:authorize access="hasAnyRole('ROLE_ROBPROG', 'ROLE_MANAGER')">
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/inUser.html"><spring:message code="label.user"/></a>
                    </li>
                </sec:authorize>
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <li class="nav-item">
                        <a class="nav-link" href="/inAdmUsers.html"><spring:message code="label.users"/></a>
                    </li>
                </sec:authorize>
                <sec:authorize access="hasAnyRole('ROLE_ROBPROG', 'ROLE_MANAGER', 'ROLE_ADMIN')">
                    <li class="nav-item">
                        <a class="nav-link" href="/inStations.html"><spring:message code="label.stations"/></a>
                    </li>
                </sec:authorize>
                <sec:authorize access="hasAnyRole('ROLE_ROBPROG', 'ROLE_MANAGER', 'ROLE_ADMIN')">
                    <li class="nav-item">
                        <a class="nav-link" href="/inProjects.html"><spring:message code="label.projects"/></a>
                    </li>
                </sec:authorize>

            </ul>
<sec:authorize access="hasAnyRole('ROLE_ROBPROG', 'ROLE_MANAGER', 'ROLE_ADMIN')">
<form:form action="search" method="POST">
                <table>
                    <tr>
                        <td>Search:</td>
                        <td><input type="text" id="robotNumber" name="robotNumber" /></td>
                    </tr>
                </table>
            </form:form>
</sec:authorize>

            <form id="langForm" action="" method="GET" class="ms-auto">
                        <span style="...">
                            <select size="1" id="lang" name="lang" onchange="langChang()" class="form-select form-select-sm" aria-label=".form-select-sm" style="width:auto; border-radius:1.0rem;">
                                <option value="pl" <spring:message code="selected.selectedPL"/>>PL</option>
                                <option value="en" <spring:message code="selected.selectedEN"/>>EN</option>
                                <option value="de" <spring:message code="selected.selectedDE"/>>DE</option>
                            </select>
                        </span>
                <script>
                    function langChang(){


                        if(window.location.search.substr(1).length>0){
                            if(window.location.href.includes('lang')){
                                //alert(window.location.href.substring(0, window.location.href.indexOf('lang')+5))
                                window.location.href = window.location.href.substring(0, window.location.href.indexOf('lang')+5)+document.getElementById("lang").value;
                            }else{
                                window.location.href = window.location.href+"&lang="+document.getElementById("lang").value;
                            }

                        }else{
                            window.location.href = window.location.href+"?lang="+document.getElementById("lang").value;
                        }

                        //document.getElementById("lang").submit();
                    }
                </script>
            </form>
        </div>

    </div>
</nav>


<!--<span style="...">
    <a style="..." href="?lang=pl">pl</a> | <a style="..." href="?lang=en">en</a> | <a style="..." href="?lang=de">de</a>
</span>-->
<br>

