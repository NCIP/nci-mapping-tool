<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>

<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>

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

String mapping_version = (String) request.getSession().getAttribute("mapping_version");
if (mapping_version != null && mapping_version.compareTo("null") == 0) {
    mapping_version = "";
} else if (mapping_version == null) {
    mapping_version = "";
}



String ncim_version = (String) request.getSession().getAttribute("ncim_version");
if (ncim_version != null && ncim_version.compareTo("null") == 0) {
    ncim_version = "";
} else if (ncim_version == null) {
    ncim_version = "";
}

String source_abbrev = (String) request.getSession().getAttribute("source_abbrev");
String target_abbrev = (String) request.getSession().getAttribute("target_abbrev");
String input_option = (String) request.getSession().getAttribute("input_option");

String input_option_label = input_option;
input_option_label = input_option_label.toLowerCase();

String algorithm = (String) request.getSession().getAttribute("algorithm");
if (algorithm != null && algorithm.compareTo("null") == 0) {
    algorithm = "";
} else if (algorithm == null) {
    algorithm = "exactMatch";
}


String source_scheme = null;
String source_version = null;

if (!DataUtils.isNull(source_abbrev)) {
	source_scheme = DataUtils.getFormalName(source_abbrev);
	source_version = DataUtils.getVocabularyVersionByTag(source_scheme, null);
}

String target_scheme = DataUtils.getFormalName(target_abbrev);
String target_version = DataUtils.getVocabularyVersionByTag(target_scheme, null);


String codes = "";

String action = (String) request.getSession().getAttribute("action");
if (action != null && action.compareTo("upload_data") == 0) {
   codes = (String) request.getSession().getAttribute("codes");
   
   
	if (codes != null && codes.compareTo("null") == 0) {
	    codes = "";
	} else if (codes == null) {
	    codes = "";
	}   
   
}

else {

	ResolvedConceptReferencesIterator iterator = null;
	iterator = (ResolvedConceptReferencesIterator) request.getSession().getAttribute("rcr_iterator");
	if (iterator != null) {
	   request.getSession().removeAttribute("rcr_iterator");
	   while (iterator.hasNext()) {
	       ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
	       codes = codes + rcr.getConceptCode() + "\n";
	   }
	}
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
 
             <% if (message != null) { request.getSession().removeAttribute("message"); %>
                 <p class="textbodyred">&nbsp;<%=message%></p>
            <% } %>   
      
  <p class="texttitle-blue"><%=identifier%>&nbsp;(<%=mapping_version%>)</p>    
      
      
  <table border="0" width="700px">
  

                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>NCI Metathesaurus Version</b>:&nbsp;<%=ncim_version%> 
                          </td>
                          <td>
                      &nbsp;
                          </td>
                  </tr>
                  
                <tr>
                <%
                if (source_scheme != null) {
                %>
		  <td align="left" class="textbody">
		      <b>From</b>:&nbsp;<%=source_abbrev%>&nbsp;(<%=source_scheme%>)
		  </td>
		<%  
		} else {
		    input_option = "Name";
		    input_option_label = input_option;
		    input_option_label = input_option_label.toLowerCase();		    
		    
		%>
		  <td align="left" class="textbody">
		      <b>From</b>:&nbsp;UNSPECIFIED
		  </td>
		<%
		}
		%>
		
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

     </table>
     
    
     <table>
<%     
if (input_option.compareTo("Name") == 0) {
    MappingUtils util = new MappingUtils();
    Vector algorithms = util.getSupportedSearchTechniqueNames();
%>    
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

<%
}
%>
     
     
 
      <tr>               
                <%
                if (source_scheme != null) {
                %>
                
                          <td align="left" class="textbody">
                              <b><%=source_abbrev%>&nbsp;<%=input_option_label%>&nbsp;data:</b>
                          </td>
                          
		<%
		} else {
		%> 
		
                          <td align="left" class="textbody">
                              <b>Data:</b>
                          </td>		
		
		<%
		}
		%>          
                           
                           
      <td> 
              <table>
              <tr>
 		     <td valign=top>    
 			     <textarea name="codes" cols="50" rows=10 tabindex="3"><%=codes%></textarea>
 		     </td>
 		     
 		     <td>
 		         <table>
 			    <tr><td>
 			    <h:commandButton id="upload" value="upload" action="#{mappingBean.uploadDataAction}"
 			      image="#{basePath}/images/upload.gif"
 			      alt="upload"
 			      tabindex="2">
 			    </h:commandButton> 
 			    </td></tr>
 			 </table>    
 		     </td>
              </tr>
              </table>
      </td>
     </tr>
     
     
     
<tr><td>&nbsp;</td></tr>
	
                  <tr><td>
                    <h:commandButton id="generate" value="generate" action="#{mappingBean.showBatchFormAction}"
                      image="#{basePath}/images/continue.gif"
                      alt="Continue"
                      tabindex="2">
                    </h:commandButton>
                  </td>
                  <td></td>
                  </tr>	
	
     </table> 

     <input type="hidden" name="type" id="type" value="ncimeta">
     
     <input type="hidden" name="identifier" id="identifier" value="<%=identifier%>">
     <input type="hidden" name="mapping_version" id="mapping_version" value="<%=mapping_version%>">
     
     <input type="hidden" name="ncim_version" id="type" value="<%=ncim_version%>">
     <input type="hidden" name="source_abbrev" id="type" value="<%=source_abbrev%>">
     <input type="hidden" name="target_abbrev" id="type" value="<%=target_abbrev%>">
     <input type="hidden" name="input_option" id="type" value="<%=input_option%>">


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
