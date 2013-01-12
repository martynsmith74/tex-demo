<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="/demo/matchresponse" method="post">

<div style="text-align: left;">
  <h2>Match Notification</h2>

  <p>Total Matched: ${match.matchedAmount}</p>
  <p>Gross Yield %: ${match.displayPrice}</p>
  <p>Members matched to:</br>
    <table class="markettable">
      <col style="width:30px"/>
      <col colspan="5"/>
      <c:forEach var="req" items="${match.requestList}">
        <tr>
          <td style="width:10px;">Company name</td>      <%-- TODO sort out when name is too long --%>
          <td>${req.displayPrice}</td>
          <td>${req.amount}</td>
          <td><input type="submit" value="Acc" disabled/></td>
          <td><input type="submit" value="Chat" disabled/></td>
          <td><input type="submit" value="Dcln" disabled/></td>
        <tr>
      </c:forEach>
    </table>
  </p>

  <input type="hidden" name="userRole" value="${userCredentials.userRole}"/>
  <input type="hidden" name="action" value="matchresponse"/>

  <p>
    <input type="submit" name="acceptAll" value="Accept All">
    <input type="submit" name="declineAll" value="Decline All">
  </p>
</div>
</form>