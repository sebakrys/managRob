<%--
  Created by IntelliJ IDEA.
  User: diron
  Date: 14.10.2021
  Time: 20:46
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<meta charset="UTF-8">
<div class="container">
    <div class="row row-cols-auto">
<div class="border col-auto rounded-3 bg-ligh">
    <c:if test="${pageContext.request.userPrincipal.name != null}">

            <a href="/inUser.html" class="text-decoration-none link-dark">${pageContext.request.userPrincipal.name}</a>
            <button type="button" class="btn btn-outline-danger" onclick="javascript:formSubmit()">Logout</button>

    </c:if>
</div>
    </div>
</div>



<sec:authorize access="isAnonymous()">
    <button type="button" class="btn btn-outline-success" onclick="window.location.href='/login.html';"><spring:message code="label.login"/></button>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADMIN')">

    <button type="button" class="btn btn-labeled btn-success" disabled>
        <span class="btn-label"><i class="fa fa-check"></i></span><spring:message code="rol.admin"/></button><br>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ZARZADCA')">
    <button type="button" class="btn btn-labeled btn-success" disabled>
        <span class="btn-label"><i class="fa fa-check"></i></span><spring:message code="rol.zarzadca"/></button><br>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_MIESZKANIEC')">
    <button type="button" class="btn btn-labeled btn-success" disabled>
        <span class="btn-label"><i class="fa fa-check"></i></span><spring:message code="rol.mieszkaniec"/></button><br>
</sec:authorize>


<script>
    function formSubmit(){
        document.getElementById("logoutForm").submit();
    }
</script>
<!--csrf for log out-->
<form action="/logout" method="post" id="logoutForm">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>
<br/>
