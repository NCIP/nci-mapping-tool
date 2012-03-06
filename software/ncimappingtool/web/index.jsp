<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="gov.nih.nci.evs.browser.properties.*" %>

<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>

<html>
 <body>
 
<%
String _mode_of_operation = DataUtils.getModeOfOperation();

System.out.println("index.jsp _mode_of_operation: " + _mode_of_operation);


if (_mode_of_operation != null && _mode_of_operation.compareTo(NCImtBrowserProperties.INTERACTIVE_MODE_OF_OPERATION) == 0) {
%>
  <jsp:forward page="/pages/home.jsf" />
<%  
} else {
%>
  <jsp:forward page="/pages/start.jsf" />
<% 
}
%>  
 </body>
</html>
