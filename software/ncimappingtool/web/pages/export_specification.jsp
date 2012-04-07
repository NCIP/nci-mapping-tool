<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>


<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>

<%@ page import="gov.nih.nci.evs.browser.bean.MappingIteratorBean" %>
<%@ page import="gov.nih.nci.evs.browser.bean.MappingBean" %>
<%@ page import="gov.nih.nci.evs.browser.bean.MappingData" %>

<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator" %>

<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>



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


  <script type="text/javascript">
    function refresh() {
      var stages = document.forms["exportForm"].stage;
      for (var i=0; i<stages.length; i++) {
        if (stages[i].checked) {
          stage = stages[i].value;
        }
      }
     
      window.location.href="/ncimappingtool/pages/export_specification.jsf?refresh=1"
          + "&stage="+ stage;
    }
    
    function back() {
        history.go(-1);
    }

    function exportToFile() {
    
        var key = document.getElementById("mappingKey");
        var key_str = key.value;
	var stages = document.exportForm.stage;
	var i;
	var j;
	var n;
	var formats;
	var export_format;
	
	var export_format = "";
        if (stages[0].checked) {
        
           if (document.getElementById('xml').checked == true) {
               export_format = "xml";
           } else if (document.getElementById('excel').checked == true) {
               export_format = "excel";
           } else {
               alert("Please specify a format.");
           }

        } else if (stages[1].checked) {
        
           if (document.getElementById('final_xml').checked == true) {
               export_format = "xml";
           } else if (document.getElementById('final_excel').checked == true) {
               export_format = "excel";
           } else if (document.getElementById('final_lexgrid_xml').checked == true) {
               export_format = "lexgrid_xml";               
           } else {
               alert("Please specify a format.");
           }
        } 
        
        if (export_format != "") {
	   var url = "/ncimappingtool/mapping?action=export&key=" + key_str + "&format=" + export_format;
	   window.open(url, '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');
        }
    }    
    
    
  </script>
  
  
<%

String source_scheme = null;
String source_version = null;
String source_namespace = null;
String target_scheme = null;
String target_version = null;

String source_code = null;
String source_name = null;
String rel = null;
String score = null;
String target_code = null;
String target_name = null;
String target_namespace = null;

String source_abbrev = null;
String target_abbrev = null;

String source_cs = null;
String target_cs = null;

String target_verson = null;

String vsdURI = null;
String valueSetDefinitionName = null;

String identifier = null;
String mapping_version = null;

String ncim_version = null;

String type = null;

HashMap mapping_hmap = null;

String id = null;


String basePath = request.getContextPath(); 
String message = (String) request.getSession().getAttribute("message");
request.getSession().removeAttribute("message");

String mappingKey = (String) request.getSession().getAttribute("mappingKey");
id = mappingKey;

HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
if (mappings == null) {
	mappings = new HashMap();
	request.getSession().setAttribute("mappings", mappings);
} 
MappingObject obj = (MappingObject) mappings.get(mappingKey);

		identifier = obj.getName();
		request.getSession().setAttribute("identifier", identifier);
		mapping_version = obj.getVersion();
		request.getSession().setAttribute("mapping_version", mapping_version);
	
	            type = obj.getType();
		    request.getSession().setAttribute("type", type);

                    ncim_version = obj.getNCIMVersion();
                    if (ncim_version != null) {
		    	request.getSession().setAttribute("ncim_version", ncim_version);
		    }
		    
		    source_abbrev = obj.getFromCS();
		    if (source_abbrev != null) {
		    	request.getSession().setAttribute("source_abbrev", source_abbrev);
		    }
		    
		    target_abbrev = obj.getToCS();
		    if (target_abbrev != null) {
		    	request.getSession().setAttribute("target_abbrev", target_abbrev);
		    }

                    source_scheme = obj.getFromCS();
		    if (source_scheme != null) {
		    	request.getSession().setAttribute("source_scheme", source_scheme);
		    }
		    
		    target_scheme = obj.getToCS();
		    if (target_scheme != null) {
		    	request.getSession().setAttribute("target_scheme", target_scheme);
		    }

                    source_version = obj.getFromVersion();
                    if (source_version != null) {
		        request.getSession().setAttribute("source_version", source_version);
		    }
		    
		    
		    target_version = obj.getToVersion();
		    if (target_version != null) {
		    	request.getSession().setAttribute("target_verson", target_version);
		    }	


source_cs = source_scheme + " (version: " + source_version + ")";
request.getSession().setAttribute("source_cs", source_cs);

if (DataUtils.isNull(source_version)) {
		    source_cs = source_scheme + " (version: unspecified" + ")";
} 
	    

		    target_cs = target_scheme + " (version: " + target_version + ")";
		    request.getSession().setAttribute("target_cs", target_cs);

                    vsdURI = obj.getVsdURI();
                    if (vsdURI != null) {
		    	request.getSession().setAttribute("vsdURI", vsdURI);
		    }
		    
		    valueSetDefinitionName = obj.getValueSetDefinitionName();
		    if (valueSetDefinitionName != null) {
		    	request.getSession().setAttribute("valueSetDefinitionName", valueSetDefinitionName);
                    }
                    
		    request.getSession().setAttribute("data", obj.getData());
		    
		    mapping_hmap = obj.getMappingHashMap();
		    request.getSession().setAttribute("mapping_hmap", mapping_hmap);

		    request.getSession().setAttribute("algorithm", "exactMatch");
		    request.getSession().setAttribute("input_option", "Code");

		    request.getSession().setAttribute("id", id);
		    request.getSession().setAttribute("status_hmap", obj.getStatusHashMap()); 
    
    
%>
<f:view>

  <!-- Begin Skip Top Navigation -->
  <!--
    <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
  -->  
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
        


<p class="texttitle-blue"><%=identifier%>&nbsp;(<%=mapping_version%>)</p>

<table> 

	  <%  
	  type = obj.getType();
	  if (type.compareTo("ncimeta") == 0) {
      
          %>
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>NCI Metathesaurus Version</b>:&nbsp;<%=ncim_version%> 
                          </td>
                          <td>
                      &nbsp;
                          </td>
                  </tr>
                  
                <tr>
		  <td align="left" class="textbody">
			<%	  
			if (source_abbrev.compareTo("") != 0) {
			%>
				      <b>From</b>:&nbsp;<%=source_abbrev%>&nbsp;(<%=source_scheme%>)
			<%
			} else {
			%>
				      <b>From</b>:&nbsp;UNSPECIFIED
			<%              
			}
			%>
		      
		  </td>
                          <td>
                      &nbsp;
                          </td>
                </tr>
                
                <tr>
		  <td align="left" class="textbody">
		      <b>To</b>:&nbsp;<%=target_abbrev%>&nbsp;(<%=target_scheme%>) 
		  </td>
                          <td>
                      &nbsp;
                          </td>
                </tr>
                 
          <%        
          }  else if (type.compareTo("codingscheme") == 0) {
          %>
                  
                <tr>
		  <td align="left" class="textbody">
		      <b>From</b>:&nbsp;<%=source_cs%>
		  </td>
                          <td>
                      &nbsp;
                          </td>
                </tr>
                
                <tr>
		  <td align="left" class="textbody">
		      <b>To</b>:&nbsp;<%=target_cs%>
		  </td>
                          <td>
                      &nbsp;
                          </td>
                </tr>

          <%        
          }  else if (type.compareTo("valueset") == 0) {
          %>
                  
                <tr>
		  <td align="left" class="textbody">
		      <b>From</b>:&nbsp;<%=source_cs%> 
		  </td>
                          <td>
                      &nbsp;
                          </td>
                </tr>
                
                <tr>
		  <td align="left" class="textbody">
		      <b>To</b>:&nbsp;<%=valueSetDefinitionName%>&nbsp;(URI: <%=vsdURI%>) 
		  </td>
                          <td>
                      &nbsp;
                          </td>
                </tr>

                
          <%
          }
          %>


 
<%
    String stage = request.getParameter("stage");
    if (DataUtils.isNull(stage)) {
        stage = "draft";
    }
    
    String draft_checked = "";
    String final_checked = "";
    
    if (stage.compareTo("draft") == 0) {
        draft_checked = "checked";
    } else {
        final_checked = "checked";
    }
    

%>
 
 <h:form id="exportForm" styleClass="search-form" > 
    
     
            <% if (message != null) { request.getSession().removeAttribute("message"); %>
              <tr class="textbodyred"><td>
                <p class="textbodyred">&nbsp;<%=message%></p>
              </td>
              <td></td></tr>
            <% } %>     



	<tr valign="top" align="left">
	  <td align="left" class="textbody">

	    <input type="radio" name="stage" id="stage" value="draft" alt="Draft" <%=draft_checked%> onclick="javascript:refresh()">Draft&nbsp;
	    <input type="radio" name="stage" id="stage" value="final" alt="Final" <%=final_checked%> onclick="javascript:refresh()">Final

	  </td>
	  
	  <td></td>
	</tr>

<%
if (stage.compareTo("draft") == 0) {
%>

	<tr valign="top" align="left">
	  <td valign="top" class="textbody">
           
           &nbsp;&nbsp;&nbsp;Content:&nbsp; 

			    <select id="entry_status" name="entry_status" size="3" multiple tabindex="4">
			    <%

				    String[] status_options = DataUtils.export_options;
				    String default_status = DataUtils.default_export_option;
				    for (int i=0; i<status_options.length; i++) {
					 String t = status_options[i];
					 if (t.compareTo(default_status) == 0) {
				    %>
					   <option value="<%=t%>" selected="true"><%=t%></option>
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
	<td></td></tr>

     	<tr valign="top" align="left">
	  <td align="left" class="textbody">
	    <ul style="list-style: none;">
	    
	    <li>
	    <input type="radio" name="format" id="xml" value="xml" alt="XML" checked>XML
	    </li>	    
	    
	    <li>
	    <input type="radio" name="format" id="excel" value="excel" alt="Microsoft Excel" >Microsoft<sup>&#174;</sup> Excel&nbsp;
	    </li>

   
	    </ul>
	  </td>  
	  <td></td>
	</tr>
	
<%	
} else {
%>

<!--
        <input type="hidden" name="entry_status" id="entry_status" value="Valid" />
-->

     	<tr valign="top" align="left">
	  <td align="left" class="textbody">
	    <ul style="list-style: none;">
	  
	    <li>
	    <input type="radio" name="format2" id="final_xml" value="xml" alt="XML" >XML
	    </li>		  
	  
<%	  
if (!DataUtils.isNull(source_scheme) && source_scheme.compareTo("LOCAL DATA") != 0) {	  
%>	  
	    <li>
	    <input type="radio" name="format2" id="final_lexgrid_xml" value="lexgrid_xml" alt="LexGrid XML" checked>LexGrid XML
	    </li>	    
<%	    
}
%>
	    
	    <li>
	    <input type="radio" name="format2" id="final_excel" value="excel" alt="Microsoft Excel" >Microsoft<sup>&#174;</sup> Excel&nbsp;
	    </li>
	    </ul>
	  </td>
	  <td></td>
	</tr>
	
	
<%
}
%>
<tr><td>&nbsp;</td><td></td></tr>


                  <tr><td class="textbody">

<!--                  
	<h:commandButton
		id="Export_Mapping"
		value="Export_Mapping"
		image="#{basePath}/images/continue.gif"
		action="#{mappingBean.exportMappingAction}" 
		onclick="javascript:cursor_wait();"
		alt="Export Mapping" >
	</h:commandButton> 
-->

<a href="javascript:exportToFile();">
    <img src="<%= request.getContextPath() %>/images/continue.gif" alt="Continue" border="0">
</a>


	&nbsp;
	
<a href="javascript:back();">
    <img src="<%= request.getContextPath() %>/images/back.gif" alt="Back" border="0">
</a>	


                  </td>
                  <td></td>
                  
                  
                  
       </tr>	
	
     </table> 



     <input type="hidden" name="identifier" id="identifier" value="<%=identifier%>" />
     <input type="hidden" name="mapping_version" id="mapping_version" value="<%=mapping_version%>" />
     <input type="hidden" name="mappingKey" id="mappingKey" value="<%=mappingKey%>" />




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
