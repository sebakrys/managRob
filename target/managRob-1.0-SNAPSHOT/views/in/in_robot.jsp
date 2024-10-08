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
    <link rel="stylesheet" type="text/css" href="/resources/css/lightbox.css">
    <script src="/resources/js/jquery.min.js"></script>
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
                            <label class="btn btn-outline-primary" for="btn-check-outlined ${zaUser.id}"> ${zaUser.email}<br>${zaUser.firstName} ${zaUser.lastName}</label></td></tr>


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
            <sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')">
            <h3>
                <spring:message code="label.statusList"/>
            </h3>


            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <li class="page-item${pageNr>0 ? "" : " disabled"}">
                        <a class="page-link" href="inRobotManag.html?fId=${selectedRobot.id}&pg=${pageNr>0 ? pageNr-1 : 0}" aria-label="Previous">
                            🡄
                        </a>
                    </li>
                    <li class="page-item disabled"><a class="page-link" href="#"><spring:message code="label.Page"/>: ${pageNr+1}/${maxPageNr+1}</a></li>
                    <li class="page-item${pageNr<maxPageNr ? "" : " disabled"}">
                        <a class="page-link" href="inRobotManag.html?fId=${selectedRobot.id}&pg=${pageNr<maxPageNr ? pageNr+1 : maxPageNr}" aria-label="Next">
                            🡆
                        </a>
                    </li>
                </ul>
            </nav>


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
                        <td><input class="form-check-input" type="checkbox" value="" id="flexCheckCheckedDisabled" ${statusL.pot ? 'checked' : ''} disabled></td>
                        <td><input class="form-check-input" type="checkbox" value="" id="flexCheckCheckedDisabled" ${statusL.prod ? 'checked' : ''} disabled></td>
                        <td><input class="form-check-input" type="checkbox" value="" id="flexCheckCheckedDisabled" ${statusL.accepted ? 'checked' : ''} disabled></td>
                        <td>${statusL.comment}</td>
                        <td>${statusL.data.getHours()}:${statusL.data.getMinutes()}:${statusL.data.getSeconds()} ${statusL.data.getDate()}.${statusL.data.getMonth()}.${statusL.data.getYear()}</td>
                        <td>${statusL.robotyk.getEmail()}</td>

                    </tr>
                </c:forEach>
                </table>
            </c:if>
            </sec:authorize>


            <c:if test="${imgExists!=null}">


                <a class="ripple" style="height: auto; width: 50%; display: block;" href="/resources/uploads/${selectedRobot.id}.${imgExists}" data-lightbox="robot_image_1" data-title="robot ${selectedRobot.robotNumber}">
                <div class="bg-image hover-zoom hover-overlay">
                    <img src="/resources/uploads/${selectedRobot.id}.${imgExists}" class="img-fluid rounded float-start" >
                    <div class="mask" style="background-color: rgba(251, 251, 251, 0.35)"></div>
                </div>
                </a>

            </c:if>

        </td>
        <td>

            <spring:message code="label.uploadPhoto"/> :

            <form name="fileUpload" method="POST" action="uploadFile?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data">
                <div class="mb-3">
                <br/>
                <input type="hidden" id="robId" name="fId" value="${selectedRobot.id}">
                <input class="form-control" id="formFile" type="file" name="file" />
                    <input class="btn btn-primary" type="submit" name="submit" value="<spring:message code="label.upload"/>" />
                </div>
            </form>

        </td>
    </tr>






</table>




<script src="/resources/js/lightbox.min.js"></script>

</body>
</html>