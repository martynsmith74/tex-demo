<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="com.texcommunity.web.MarketBean" %>
<%@ page import="java.util.List" %>
<%@ page import="com.texcommunity.core.UserRole" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>The Enterprise Xchange</title>

<link rel="stylesheet" href="/css/jquery/ui-lightness/jquery-ui-1.9.2.custom.min.css" />

<link href="/css/market.css" rel="stylesheet" type="text/css" /><!--[if IE]>
<style type="text/css"> 
/* place css fixes for all versions of IE in this conditional comment */
.thrColHybHdr #sidebar1, .thrColHybHdr #sidebar2 { padding-top: 30px; }
.thrColHybHdr #mainContent { zoom: 1; padding-top: 15px; }
/* the above proprietary zoom property gives IE the hasLayout it needs to avoid several bugs */
</style>
<![endif]-->
<script src="/javascript/jquery/jquery-1.8.3.js"></script>
<script src="/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>

<script type="text/javascript">

  $(function() {
    $( "#editRequest, #editOffer" ).dialog({
      autoOpen: false,
      show: "fade",
      hide: "fade",
      modal: true
    });
 
    $( "#editRequestButton" ).click(function() {
      $( "#editRequest" ).dialog( "open" );
      return false;
    });

    $( "#editOfferButton" ).click(function() {
      $( "#editOffer" ).dialog( "open" );
      return false;
    });

    var isMatched = false;
    <c:if test="${match != null}">
      isMatched = true;
    </c:if>

    $( "#matchNotification" ).dialog({
      autoOpen: isMatched,
      show: "fade",
      hide: "fade",
      modal: true,
      width: 600
    });
 
  });
</script>

</head>

<body class="thrColHybHdr">


<div id="container">

  <div id="header">
    <h1>The Enterprise Xchange</h1>
  </div> <!-- end #header -->
  
  <div id="sidebar1">
    &nbsp;
  </div> <!-- end #sidebar1 -->
  
  <div id="sidebar2">
    <h3>Admin privileges</h3>
    <p>
      Current role is ${userCredentials.userRoleDisplay}<br/>
      Switch to:
      <ul>
        <li><a href="/demo?userRole=ADMIN">Admin</a></li>
        <li><a href="/demo?userRole=INVESTOR">Investor</a></li>
        <li><a href="/demo?userRole=BORROWER&tier=1">Borrower, tier 1</a></li>
        <li><a href="/demo?userRole=BORROWER&tier=2">Borrower, tier 2</a></li>
        <li><a href="/demo?userRole=BORROWER&tier=3">Borrower, tier 3</a></li>
        <li><a href="/demo?userRole=BORROWER&tier=4">Borrower, tier 4</a></li>
      </ul>
    </p>
    <p>
      <a href="/demo/admin?action=reset&userRole=${userCredentials.userRole}">Reset market</a>
    </p>
  </div> <!-- end #sidebar2 -->
  
  <div id="mainContent">
	<p>
      <c:choose>
        <c:when test='${userCredentials.showAllMarket}'>
          <jsp:include page="/jsps/allmarket.jsp"/>
        </c:when>
        <c:when test='${userCredentials.showUserMarket}'>
          <jsp:include page="/jsps/investormarket.jsp"/>
        </c:when>
      </c:choose>
    </p>
    <p>
      <table border="0" cellpadding="20">
        <tr>
          <c:if test="${userCredentials.ableToEnterRequest}">
            <td valign="top"><button id="editRequestButton">Make Request</button><br/></td>
          </c:if>
          <c:if test="${userCredentials.ableToEnterOffer}">
            <td valign="top"><button id="editOfferButton">Make Offer</button></td>
          </c:if>
        </tr>
      </table>
    </p>
  </div> <!-- end #mainContent -->
  <!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats --><br class="clearfloat" />
    
  <div id="footer">
    <p>&nbsp;</p>
  </div> <!-- end #footer -->

</div> <!-- end #container -->

<div id="editRequest" class="modalDialog">
  <jsp:include page="/jsps/editrequest.jsp"/>
</div>

<div id="editOffer" class="modalDialog">
  <jsp:include page="/jsps/editoffer.jsp"/>
</div>

<div id="matchNotification" class="modalDialog">
  <jsp:include page="/jsps/matchnotification.jsp"/>
</div>

</body>
</html>