<%@ page isELIgnored="false" %>
<%@ page import="com.texcommunity.web.MarketBean" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<div id="investorMarketTable">
  <table width="100%" border="0" cellpadding="5" cellspacing="0" class="markettable">
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="20%"/>
    <col width="20%"/>
    <col width="10%"/>
    <col width="10%"/>
    <col width="10%"/>

    <tr class="tableheadergrey">
      <td colspan="8">1 Year Lending Period (all sectors)</td>
    </tr>

    <tr class="tableheaderblack">
      <td colspan="4" align="left">REQUESTED</td>
      <td colspan="4" align="right">OFFERED</td>
    </tr>

    <tr class="row0">
      <c:forEach var="req" items="${invViewBean.requestList}">
        <c:set var="priceClass" value=""/>
        <c:if test="${req.best}">
          <c:set var="priceClass" value="largeText blueText"/>
        </c:if>
        <td class="${priceClass}">
          <c:if test="${req.amount != 0}">
            ${req.displayPrice}
          </c:if>&nbsp;
        </td>
      </c:forEach>

      <c:forEach var="offer" items="${invViewBean.offerList}">
        <c:set var="priceClass" value=""/>
        <c:if test="${offer.best}">
          <c:set var="priceClass" value="largeText redText"/>
        </c:if>
        <td class="${priceClass}">
          <c:if test="${offer.amount != 0}">
            ${offer.displayPrice}
          </c:if>&nbsp;
        </td>
      </c:forEach>
    </tr>

    <tr class="row1">
        <c:forEach var="req" items="${invViewBean.requestList}">
          <c:set var="amountClass" value=""/>
          <c:if test="${req.best}">
            <c:set var="amountClass" value="largeText"/>
          </c:if>
          <td class="${amountClass}">
            <c:if test="${req.amount != 0}">
              ${req.amount}
            </c:if>&nbsp;
          </td>
        </c:forEach>

        <c:forEach var="offer" items="${invViewBean.offerList}">
          <c:set var="amountClass" value=""/>
          <c:if test="${offer.best}">
            <c:set var="amountClass" value="largeText"/>
          </c:if>
          <td class="${amountClass}">
            <c:if test="${offer.amount != 0}">
              ${offer.amount}
            </c:if>&nbsp;
          </td>
        </c:forEach>
    </tr>

  </table>
    
<!-- end #investorMarketTable --></div>
