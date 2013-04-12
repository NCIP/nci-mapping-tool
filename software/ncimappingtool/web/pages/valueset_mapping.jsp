<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils"%>
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
String basePath = request.getContextPath(); 
String message = (String) request.getSession().getAttribute("message");
request.getSession().removeAttribute("message");

String identifier = (String) request.getSession().getAttribute("identifier");
if (identifier != null && identifier.compareTo("null") == 0) {
    identifier = "";
} else if (identifier == null) {
    identifier = "";
}

String mapping_version = (String) request.getSession().getAttribute("mapping_version");
if (DataUtils.isNull(mapping_version)) {
    mapping_version = "";
} 

String source_cs = (String) request.getSession().getAttribute("source_cs");
if (source_cs != null && source_cs.compareTo("null") == 0) {
    source_cs = "";
} else if (source_cs == null) {
    source_cs = "";
}

String target_cs = (String) request.getSession().getAttribute("target_cs");
if (target_cs != null && target_cs.compareTo("null") == 0) {
    target_cs = "";
} else if (target_cs == null) {
    target_cs = "";
}






String code_checked = "";
String name_checked = "";
String property_checked = "";

String input_option = (String) request.getSession().getAttribute("input_option");
if (input_option == null) {
    input_option = "Code";
    
} 
if (input_option.compareTo("Code") == 0) {
    code_checked = "checked";
} else if (input_option.compareTo("Name") == 0) {
    name_checked = "checked";
} else {
    property_checked = "checked";
}


String algorithm = (String) request.getSession().getAttribute("algorithm");
if (algorithm != null && algorithm.compareTo("null") == 0) {
    algorithm = "";
} else if (algorithm == null) {
    algorithm = "";
}


String property = (String) request.getSession().getAttribute("property");
if (property != null && property.compareTo("null") == 0) {
    property = "";
} else if (property == null) {
    property = "";
}

String LOCAL_DATA = Constants.LOCAL_DATA;

                Vector cs_label_vec = new Vector();
                List ontology_list = DataUtils.getOntologyList();
                int num_vocabularies = ontology_list.size();

                for (int i = 0; i < ontology_list.size(); i++) {
                       SelectItem item = (SelectItem) ontology_list.get(i);
                       String label = (String) item.getLabel();
                       String value = (String) item.getValue();
                       String scheme = DataUtils.key2CodingSchemeName(value);
                       String version = DataUtils.key2CodingSchemeVersion(value);
		       boolean isMapping = DataUtils.isMapping(scheme, version);
		       if (!isMapping) { 
		           cs_label_vec.add(label);
		       }
		}
		cs_label_vec = SortUtils.quickSort(cs_label_vec);


if ((DataUtils.isNull(source_cs) || source_cs.compareTo("") == 0) && cs_label_vec.size() > 0) {
    source_cs = (String) cs_label_vec.elementAt(0);
}
if ((DataUtils.isNull(target_cs) || target_cs.compareTo("") == 0) && cs_label_vec.size() > 0) {
    target_cs = (String) cs_label_vec.elementAt(0);
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
   
 
                  <tr valign="top" align="left">
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
                  
                  
                <tr>
		  <td align="left" class="textbody">
		      <b>From</b>:
		  </td>

                  <td class="textbody">
                    <select id="source_cs" name="source_cs" size="1" tabindex="4">
                       <%
                       if (cs_label_vec != null && cs_label_vec.size() > 0) {
                       %>
                       <option value="<%=LOCAL_DATA%>"><%=LOCAL_DATA%></option>
                       <%
                       }
                       %>                     
                    <%
                       String t = null;
		       for (int i=0; i<cs_label_vec.size(); i++) {
			    t = (String) cs_label_vec.elementAt(i);
			    if (t.compareTo(source_cs) == 0) {
			    %>
				   <option value="<%=t%>" selected><%=t%></option>
			    <%
			    } else {
			    %>
				   <option value="<%=t%>"><%=t%></option>
			    <%
			    }
                       }
                    %>
                    </select>
                  </td>
                </tr>
 
 
     <tr>
 
 	 <td align="left" class="textbody">
 	     <b>To</b>:
 	 </td>                   
 
 	 <td class="inputItem">
 	 <select id="vsdURI" name="vsdURI" size="1" tabindex="6">
 
 	   <%
	   
 	     Vector item_vec = DataUtils.getValueSetDefinitions();
 	     String vsdURI = (String) request.getAttribute("vsdURI");
 	     
 	     
 	     if (vsdURI == null) {
 		      SelectItem item = (SelectItem) item_vec.elementAt(0);
 		      vsdURI = (String) item.getLabel();
 	     }
	     
 	     if (item_vec != null) {
 		    for (int i=0; i<item_vec.size(); i++) {
 		      SelectItem item = (SelectItem) item_vec.elementAt(i);
 		      String key = (String) item.getLabel();
 		      String value = (String) item.getValue();
 		      if (value != null && value.compareTo(vsdURI) == 0) {
 		  %>
 			<option value="<%=value%>" selected><%=key%></option>
 		  <%  } else { %>
 			<option value="<%=value%>"><%=key%></option>
 		  <%
 		      }
 		    }
 	     }
 	   %>
 	   </select>
 
 	 </td>
      </tr>    
<!-- 

                  <tr valign="top" align="left">
                          <td align="left" class="textbody">
                              Input Option:
                          </td>
                          <td class="textbody">
                          
            <input type="radio" name="input_option" value="Code" alt="Code" <%=code_checked%> >Code&nbsp;              
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
          <input type="hidden" name="type" id="type" value="valueset">
          <input type="hidden" name="input_option" id="input_option" value="Code">
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
