<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="/demo/offer" method="post">
  <h2>Enter an OFFER</h2>
  <p>Amount: <input name="amount"></p>
  <p>Gross yield %: <input name="yield"></p>
  <input type="hidden" name="userRole" value="${userCredentials.userRole}"/>
  <input type="hidden" name="action" value="offer"/>
  <p><input type="submit" value="Submit"></p>
</form>