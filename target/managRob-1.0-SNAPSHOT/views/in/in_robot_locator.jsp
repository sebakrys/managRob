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
                    <td>${selectedRobot.robotNumber}</td>
                </tr>
                <tr>
                    <td>${selectedStation.nazwa}</td>
                </tr>
                <tr>
                    <td>${selectedStation.street}</td><td>${selectedStation.stationNumber}</td>
                </tr>
                <tr>
                    <td>${selectedStation.city}</td>
                </tr>

            </table></h3>

            </th>
        <th><div class="float-right">
            <h3><spring:message code="label.addRobprogs"/></h3>
        </div></th>
    </tr>
    <tr>
        <td>
            <form:form method="post" action="inRobotAddCharges.html" modelAttribute="addCharges">
                <form:hidden path="id" modelAttribute="selectedRobot"/>
            <table>
                <tr>
                <th colspan="1"><spring:message code="label.data"/>: ${addCharges.data.getMonth()} ${addCharges.data.getYear()}</th>
                <th>&nbsp;<spring:message code="label.accepted"/>: ${addCharges.accepted}

                </th>
                <th>&nbsp;<spring:message code="label.paid"/>: ${addCharges.zaplacone}

                </th>
                </tr>
                <tr>
                <td><form:label path="funduszRemontowy"><spring:message code="label.funduszRemontowy"/></form:label></td>
                <td><form:input path="funduszRemontowy" /></td>
                <!--<td><form:errors path="funduszRemontowy" /></td>-->
                <td colspan="2"><spring:message code="label.stawka"/>: ${addCharges.funduszRemontowy_stawka} zł/m<sup>2</sup></td>
        </tr><tr>
                <td><form:label path="gaz"><spring:message code="label.gaz"/></form:label></td>
                <td><form:input path="gaz" /></td>
                <!--<td><form:errors path="gaz" /></td>-->
                <td colspan="2"><spring:message code="label.stawka"/>: ${addCharges.gaz_stawka} zł/m<sup>3</sup></td>
                </tr>
                <tr>
                    <td><form:label path="ogrzewanie"><spring:message code="label.ogrzewanie"/></form:label></td>
                    <td><form:input path="ogrzewanie" /></td>
                    <!--<td><form:errors path="ogrzewanie" /></td>-->
                    <td colspan="2"><spring:message code="label.stawka"/>: ${addCharges.ogrzewanie_stawka} zł/kWh</td>
                </tr><tr>
                        <td><form:label path="prad"><spring:message code="label.prad"/></form:label></td>
                        <td><form:input path="prad" /></td>
                        <!--<td><form:errors path="prad" /></td>-->
                        <td colspan="2"><spring:message code="label.stawka"/>: ${addCharges.prad_stawka} zł/kWh</td>
                </tr><tr>
                            <td><form:label path="scieki"><spring:message code="label.scieki"/></form:label></td>
                            <td><form:input path="scieki" /></td>
                            <!--<td><form:errors path="scieki" /></td>-->
                            <td colspan="2"><spring:message code="label.stawka"/>: ${addCharges.scieki_stawka} zł/m<sup>3</sup></td>
            </tr><tr>
                                <td><form:label path="woda_ciepla"><spring:message code="label.wodaCiepla"/></form:label></td>
                                <td><form:input path="woda_ciepla" /></td>
                                <!--<td><form:errors path="woda_ciepla" /></td>-->
                                <td colspan="2"><spring:message code="label.stawka"/>: ${addCharges.woda_ciepla_stawka} zł/m<sup>3</sup></td>
            </tr><tr>
                                    <td><form:label path="woda_zimna"><spring:message code="label.wodaZimna"/></form:label></td>
                                    <td><form:input path="woda_zimna" /></td>
                                    <!--<td><form:errors path="woda_zimna" /></td>-->
                                    <td colspan="2"><spring:message code="label.stawka"/>: ${addCharges.woda_zimna_stawka} zł/m<sup>3</sup></td>
            </tr>
                    <tr>
                    <td>
                        <input class="btn btn-primary" type="submit" value="<spring:message code="button.submit"/>"/>
                    </td>
                </tr>
            </table>
            </form:form>
            <c:if test="${addCharges.accepted == true}">
            Do zapłaty: ${addCharges.zaplacone == true ? 0.00 : doZaplaty}
            </c:if>
        </td>
        <td>

        </td>
    </tr>
</table>




</body>
</html>