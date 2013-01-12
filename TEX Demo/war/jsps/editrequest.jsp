<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="/demo/request" method="post">
  <h2>Enter a REQUEST</h2>
  <p>Amount: <input name="amount"></p>
  <p>APR %: <input name="apr"></p>
  <c:choose>
    <c:when test="${userCredentials.ableToEnterRequestAnyTier}">
      <p>Enter as tier: <input name="tier"></p>
    </c:when>
    <c:otherwise>
      <input type="hidden" name="tier" value="${userCredentials.tier}"/>
    </c:otherwise>
  </c:choose>
  <p>Request details: <textarea name="details"></textarea></p>
  <input type="hidden" name="userRole" value="${userCredentials.userRole}"/>
  <input type="hidden" name="action" value="request"/>
  <p><input type="submit" value="Submit"></p>
</form>