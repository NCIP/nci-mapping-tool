<%@ page import="gov.nih.nci.evs.common.*" %>
<html>
  <body>
	<%
      String errorMsg = EVSConstants.ERROR_MESSAGE;
      String message = (String) request.getSession().getAttribute(errorMsg); 
    %>
	<b><%=message%></b>
  </body>
</html>        
