<%@ page isELIgnored="false" %>
<%@ page import="com.texcommunity.web.MarketBean" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<p>
<div id="marketTable">
  <table width="100%" border="0" cellpadding="5" cellspacing="0" class="markettable">
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="20%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>

    <tr class="tableheaderblack">
      <td colspan="4" align="left">REQUESTED</td>
      <td align="center">%</td>
      <td colspan="4" align="right">OFFERED</td>
    </tr>

    <c:set var="rowCount" value="0"/>
    <c:forEach var="marketPrice" items="${marketBean.adminPrices}">
      <tr class='row<c:out value="${rowCount % 2}"/>'>

        <c:forEach var="req" items="${marketPrice.requestList}">
          <td class="smalltext">
            <c:if test="${req.amount != 0}">
              Tier ${req.tier}<br/>
              Qty ${req.amount}<br/>
              Time ${req.displayTime}
            </c:if>&nbsp;
          </td>
        </c:forEach>

        <td align="center">${marketPrice.displayPrice}&nbsp;</td>

        <c:forEach var="offer" items="${marketPrice.offerList}">
          <td class="smalltext">
            <c:if test="${offer.amount != 0}">
              Qty ${offer.amount}<br/>
              Time ${offer.displayTime}
            </c:if>&nbsp;
          </td>
        </c:forEach>

      </tr>
      <c:set var="rowCount" value="${rowCount + 1}"/>
    </c:forEach>

  </table>
    
</div> <!-- end #marketTable -->
</p>

<p>
<div id="recentMatchTable">

  <table width="100%" border="0" cellpadding="5" cellspacing="0" class="markettable">
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>

    <tr class="tableheaderblack">
      <td colspan="10">Recent matches</td>
    </tr>
    <tr class="row1">
      <c:forEach var="match" items="${marketBean.matchList}">
        <td class="smalltext">
          <c:if test="${match.matchedAmount != 0}">
            Price ${match.displayPrice}<br/>
            Amount ${match.matchedAmount}<br/>
            Time ${match.displayTime}
          </c:if>&nbsp;
        </td>
      </c:forEach>
    </tr>
  </table>

</div> <!-- end #recentMatchTable -->
</p>