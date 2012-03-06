<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils"%>

<%
  String ncit_build_info = new DataUtils().getNCITBuildInfo();
  String application_version = new DataUtils().getApplicationVersion();
  String anthill_build_tag_built = new DataUtils().getNCITAnthillBuildTagBuilt();
  String evs_service_url = new DataUtils().getEVSServiceURL();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
  <title>NCI EVS Concept Mapping Tool</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body onLoad="document.forms.searchTerm.matchText.focus();">
<!--
   Build info: <%=ncit_build_info%>
 Version info: <%=application_version%>
          Tag: <%=anthill_build_tag_built%>
   LexEVS URL: <%=evs_service_url%>          
  -->
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>

<%
String hm_basePath = request.getContextPath(); 

String reference_code = (String) request.getAttribute("reference_code");
String type = (String) request.getAttribute("type");

%>
<f:view>
  <!-- Begin Skip Top Navigation -->
    <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
  <!-- End Skip Top Navigation -->
  <%@ include file="/pages/templates/header.jsp" %>
  <div class="center-page">
    <%@ include file="/pages/templates/sub-header.jsp" %>
    <!-- Main box -->
    <div id="main-area">
      <%@ include file="/pages/templates/content-header.jsp" %>
      <!-- Page content -->
      <div class="pagecontent">
        <a name="evs-content" id="evs-content"></a>
        

<table>
<tr>
	<td align="left"><font size="4"><b>WELCOME TO NCI EVS Mapping Tool</b></font></td>
</tr>
<tr><td>
&nbsp;
</td></tr>
<tr><td class="textbody">
<p>
Thank you for submitting a mapping task.
A reference code 
<b><%=reference_code%></b> 
has been assigned to the task.
You may use this code to submit mapping data. 
Your data will be processed in the order they are received.
</p>
<p>
You may also use this code to check for the procesing status,  
perform manual mapping, add notes to mapping entries, assign mapping entry status, 
approve mapping entries, export mapping results to a Microsoft Excel file, or publish results in
LexGrid XML format.
</p>
</td></tr>

<tr><td>
&nbsp;
</td></tr>

<tr><td>
<h:form>

<center>

                    <h:commandButton id="continue" value="continue" action="#{mappingBean.mappingDataAction}"
                      image="#{basePath}/images/continue.gif"
                      alt="Submit"
                      tabindex="2">
                    </h:commandButton>
                    
                    &#xA0;&#xA0;
					
                    <h:commandButton id="close" value="close" action="#{mappingBean.returnToHomeAction}"
                      image="#{basePath}/images/close.gif"
                      alt="Close"
                      tabindex="2">
                    </h:commandButton>

</center>


     <input type="hidden" name="type" id="type" value="<%=type%>">

</h:form>
</td></tr>
</table>  	
	
        
        
        <%@ include file="/pages/templates/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=hm_basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>
