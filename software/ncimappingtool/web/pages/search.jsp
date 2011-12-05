<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>

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

String action = (String) request.getParameter("action");

request.getSession().removeAttribute("message");

String identifier = (String) request.getSession().getAttribute("identifier");
if (identifier != null && identifier.compareTo("null") == 0) {
    identifier = "";
} else if (identifier == null) {
    identifier = "";
}


String type = (String) request.getSession().getAttribute("type");

HashMap mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");
if (mapping_hmap == null) {
    mapping_hmap = new HashMap();
    synchronized (request.getSession()) {
    	request.getSession().setAttribute("mapping_hmap", mapping_hmap);
    }
} 
List list = (ArrayList) request.getSession().getAttribute("data");

String idx1_str = request.getParameter("idx1");
if (!DataUtils.isNull(idx1_str)) {
    request.getSession().setAttribute("idx1_str", idx1_str);
} else {
    idx1_str = (String) request.getSession().getAttribute("idx1_str");
}

int idx1 = Integer.parseInt(idx1_str);
String data_value = (String) list.get(idx1);

String source_code = null;
String source_name = null;

String input_option = input_option = (String) request.getSession().getAttribute("input_option");

if (input_option.compareToIgnoreCase("Code") == 0) {
    source_code = data_value;
    source_name = "";
} else {
    Vector data_vec = new Vector();
    data_vec = DataUtils.parseData(data_value);
    String s1 = (String) data_vec.elementAt(0);
    String s2 = (String) data_vec.elementAt(1);
    source_code = s1;
    source_name = s2;
}

String source_scheme = null;
String source_version = null;
String source_namespace = null;
String target_scheme = null;
String target_version = null;

String rel = null;
int score = 0;
String target_code = null;
String target_name = null;
String target_namespace = null;

String source_abbrev = null;
String target_abbrev = null;

String source_cs = null;
String target_cs = null;

String associationName = "mapsTo";
target_code = (String) request.getParameter("target_code");

String ncim_version = null;
if (type.compareTo("ncimeta") == 0) {
	ncim_version = (String) request.getSession().getAttribute("ncim_version");
	if (ncim_version != null && ncim_version.compareTo("null") == 0) {
	    ncim_version = "";
	} else if (ncim_version == null) {
	    ncim_version = "";
	}
	source_abbrev = (String) request.getSession().getAttribute("source_abbrev");
	target_abbrev = (String) request.getSession().getAttribute("target_abbrev");
	
        source_scheme = DataUtils.getFormalName(source_abbrev);
        source_version = DataUtils.getVocabularyVersionByTag(source_scheme, null);

        target_scheme = DataUtils.getFormalName(target_abbrev);
        target_version = DataUtils.getVocabularyVersionByTag(target_scheme, null);
	
	
	
} else if (type.compareTo("codingscheme") == 0) {

	source_cs = (String) request.getSession().getAttribute("source_cs");
	target_cs = (String) request.getSession().getAttribute("target_cs");	

	source_scheme = DataUtils.key2CodingSchemeName(source_cs);
	source_version = DataUtils.key2CodingSchemeVersion(source_cs);

	target_scheme = DataUtils.key2CodingSchemeName(target_cs);
	target_version = DataUtils.key2CodingSchemeVersion(target_cs);
	
} 

Entity src_concept = MappingUtils.getConceptByCode(source_scheme, source_version, null, source_code);
String source_concept_name = src_concept.getEntityDescription().getContent();

Vector target_properties = OntologyBean.getSupportedPropertyNames(target_scheme, target_version);


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

    <p class="texttitle-blue"><%=target_scheme%>&nbsp;(version: <%=target_version%>)</p>
  
  <table border="0" width="700px">
  
            <% if (message != null) { request.getSession().removeAttribute("message"); %>
              <tr class="textbodyred">
                <td>
                <p class="textbodyred">&nbsp;<%=message%></p>
                </td>              
                <td></td>
              </tr>
              <tr><td>&nbsp;</td></tr>
              
            <% } %>       
  

     </table>
     
    
     <table>
<%     
    MappingUtils util = new MappingUtils();
    Vector algorithms = util.getSupportedSearchTechniqueNames();
    String algorithm = (String) request.getSession().getAttribute("algorithm");
    String target_property = "";
    if (target_properties.contains("Preferred_Name")) {
        target_property = "Preferred_Name";
    } else if (target_properties.contains("textualPresentation")) {
        target_property = "textualPresentation";
    }    
%>    


                 <tr>
 		  <td align="left" class="textbody">
 		      <b>Search String</b>:&nbsp; 
 		  </td>
                   <td>
                       <input CLASS="searchbox-input" id="input" name="input" value="<%=source_concept_name%>" width="700px" tabindex="1"/>
                   </td>
                </tr> 	
                
                
                <tr>
		  <td align="left" class="textbody">
		      <b>Algorithm</b>:
		  </td>

                  <td class="textbody">
                    <select id="algorithm" name="algorithm" size="1" tabindex="4">
                    <%
                       if (algorithms != null) {
			    for (int i=0; i<algorithms.size(); i++) {
				 String t = (String) algorithms.elementAt(i);
				 if (t.compareTo(algorithm) == 0) {
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
		  <td align="left" class="textbody">
		      <b>Property</b>:
		  </td>

                  <td class="textbody">
                    <select id="target_property" name="target_property" size="1" tabindex="4">
                    <%
                       if (target_properties != null) {
			    for (int i=0; i<target_properties.size(); i++) {
				 String t = (String) target_properties.elementAt(i);
				 if (t.compareTo(target_property) == 0) {
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
 	
  	
                  <tr><td>

  	<h:commandButton
  		id="SubmitForm"
  		value="SubmitForm"
  		image="#{basePath}/images/search.gif"
  		action="#{mappingBean.searchAction}" 
  		alt="Submit Batch" >
  	</h:commandButton>

                  &nbsp;



      <a href="#" onclick="javascript:history.back();" >
        <img src="<%= request.getContextPath() %>/images/cancel.gif" style="border: none">
      </a> 
      
      
                  
                  </td>

                  <td>
                  
                  
                  
                  </td>
                  </tr>	
	
     </table> 

     <input type="hidden" name="type" id="type" value="<%=type%>">
     <input type="hidden" name="identifier" id="identifier" value="<%=identifier%>">
     <input type="hidden" name="source_cs" id="source_cs" value="<%=source_cs%>">
     <input type="hidden" name="target_cs" id="target_cs" value="<%=target_cs%>">
     
     <input type="hidden" name="refresh" id="refresh" value="true">
     <input type="hidden" name="input_option" id="input_option" value="Property">
      
     <input type="hidden" name="target_scheme" id="target_scheme" value="<%=target_scheme%>">
     <input type="hidden" name="target_version" id="target_version" value="<%=target_version%>">
      
      
<%
if (action != null) {
%>
<input type="hidden" name="action" id="action" value="<%=action%>">
<%
}
%>


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
