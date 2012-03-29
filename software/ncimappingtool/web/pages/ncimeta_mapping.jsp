<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>


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
<body>
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

String basePath = request.getContextPath(); 
String message = (String) request.getSession().getAttribute("message");
request.getSession().removeAttribute("message");

String identifier = (String) request.getSession().getAttribute("identifier");
if (identifier != null && identifier.compareTo("null") == 0) {
    identifier = "";
} else if (identifier == null) {
    identifier = "";
}

String scheme = "NCI Metathesaurus";
String mapping_version = (String) request.getSession().getAttribute("mapping_version");
if (DataUtils.isNull(mapping_version)) {
    mapping_version = "";
} 

String ncim_version = (String) request.getSession().getAttribute("ncim_version");
if (ncim_version != null && ncim_version.compareTo("null") == 0) {
    ncim_version = "";
} else if (ncim_version == null) {
    ncim_version = DataUtils.getVocabularyVersionByTag(scheme, null);;
}



String adv_search_vocabulary = "NCI Metathesaurus";
String adv_search_version = null;
String adv_search_source = null;
String t = null;
Vector src_vec = OntologyBean.getSupportedSources(adv_search_vocabulary, adv_search_version);


String source_abbrev = (String)	request.getSession().getAttribute("source_abbrev");
if (source_abbrev != null && source_abbrev.compareTo("null") == 0) {
    source_abbrev = "";
} else if (source_abbrev == null) {
    source_abbrev = "NCI";
}

String target_abbrev = (String)	request.getSession().getAttribute("target_abbrev");
if (target_abbrev != null && target_abbrev.compareTo("null") == 0) {
    target_abbrev = "";
} else if (target_abbrev == null) {
    target_abbrev = "NCI";
}

String input_option = (String)	request.getSession().getAttribute("input_option");
if (input_option != null && input_option.compareTo("null") == 0) {
    input_option = "";
} else if (input_option == null) {
    input_option = "Source Code";
}


String source_code_checked = "";
if (input_option.compareTo("Source Code") == 0) {
    source_code_checked = "checked";
}

String name_checked = "";
if (input_option.compareTo("Name") == 0) {
    name_checked = "checked";
}


                      
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

        
<h:form>        
 
  <p class="texttitle-blue">Create Mapping</p>

             <% if (message != null) { request.getSession().removeAttribute("message"); %>
                 <p class="textbodyred">&nbsp;<%=message%></p>
            <% } %>  
            
            
  <table border="0" width="700px">

            



                  <tr valign="top" align="left" width="180">
                          <td align="left" class="textbody">
                              <b>Identifier</b>:
                          </td>
                          <td class="textbody">
                      <input CLASS="searchbox-input" id="identifier" name="identifier" value="<%=identifier%>" width="700px" tabindex="1"/>
                          </td>
                  </tr>


                  <tr valign="top" align="left" width="180">
                          <td align="left" class="textbody">
                              <b>Version</b>:
                          </td>
                          <td class="textbody">
                      <input CLASS="searchbox-input" id="mapping_version" name="mapping_version" value="<%=mapping_version%>" width="700px" tabindex="1"/>
                          </td>
                  </tr>
                  
                  
                  <tr valign="top" align="left" width="180">
                          <td align="left" class="textbody" >
                              <b>NCI Metathesaurus Version</b>:
                          </td>
                          <td class="textbody">
                      <%=ncim_version%> 
                          </td>
                  </tr>
                  
                  <!--
                  <tr valign="top" align="left" width="180">
                          <td align="left" class="textbody" >
                              <b>NCI Metathesaurus Version</b>:
                          </td>
                          <td>
                      <input CLASS="searchbox-input" id="ncim_version" name="ncim_version" value="<%=ncim_version%>" readonly="true" tabindex="2"/>
                          </td>
                  </tr>
                  -->
                  
                <tr>
		  <td align="left" class="textbody" width="180">
		      <b>From</b>:
		  </td>

                  <td class="textbody">
                    <select id="source_abbrev" name="source_abbrev" size="1" tabindex="4">
                    <%
                       if (src_vec != null) {
                                 t = "";
				 if (t.compareTo(source_abbrev) == 0) {
			    %>
				   <option value="<%=t%>" selected><%=t%></option>
			    <%
				 } else {
			    %>
				   <option value="<%=t%>"><%=t%></option>
			    <%
				 }                            
                            %>       

                            <%
			    for (int i=0; i<src_vec.size(); i++) {
				 t = (String) src_vec.elementAt(i);
				 if (t.compareTo(source_abbrev) == 0) {
			    %>
				   <option value="<%=t%>" selected><%=t%></option>
			    <%
				 } else {
			    %>
				   <option value="<%=t%>"><%=t%></option>
			    <%
				 }
			    }
                       }
                    %>
                    </select>
                </td></tr>
                
                <tr>
		  <td align="left" class="textbody" width="180">
		      <b>To</b>:
		  </td>
		  <td class="textbody">
		  <select id="target_abbrev" name="target_abbrev" size="1" tabindex="4">
                    <%
                       if (src_vec != null) {
			    for (int i=0; i<src_vec.size(); i++) {
				 t = (String) src_vec.elementAt(i);
				 if (t.compareTo(target_abbrev) == 0) {
			    %>
				   <option value="<%=t%>" selected><%=t%></option>
			    <%
				 } else {
			    %>
				   <option value="<%=t%>"><%=t%></option>
			    <%
				 }
			    }
                       }
                    %>
                    </select>
                </td></tr>
                

<!--

	<tr valign="top" align="left">
		  <td align="left" class="textbody" width="180">
		      <b>Input Option</b>:
		  </td>
		  <td class="textbody">
	    <input type="radio" name="input_option" value="Source Code" alt="Source Code" <%=source_code_checked%> >Code&nbsp;
	    <input type="radio" name="input_option" value="Name" alt="Name" <%=name_checked%> >Name&nbsp;
	    
	          </td>
	</tr>
	
-->	
<tr><td>&nbsp;</td></tr>


                  <tr><td>
                    <h:commandButton id="continue" value="continue" action="#{mappingBean.submitMetadataAction}"
                      image="#{basePath}/images/continue.gif"
                      alt="Submit"
                      tabindex="2">
                    </h:commandButton>
                  </td>
                  <td></td>
                  </tr>	
	
     </table> 

     <input type="hidden" name="type" id="type" value="ncimeta">
     <input type="hidden" name="ncim_version" id="type" value="<%=ncim_version%>">
     <input type="hidden" name="input_option" id="input_option" value="Code">
     
     
     <input type="hidden" name="ack" id="ack" value="ncimeta">
     

</h:form>
        
        
        
        <%@ include file="/pages/templates/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>
