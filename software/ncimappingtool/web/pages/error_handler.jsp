<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
L--%>

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
