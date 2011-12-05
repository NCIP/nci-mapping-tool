<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.net.URI"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>

<%@ page import="org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed" %>
<%@ page import="org.lexgrid.valuesets.LexEVSValueSetDefinitionServices" %>
<%@ page import="org.LexGrid.valueSets.ValueSetDefinition" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList" %>

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


String codes = (String) request.getSession().getAttribute("codes");
if (DataUtils.isNull(codes)) codes = "";

String source_cs = (String) request.getSession().getAttribute("source_cs");

String source_scheme = DataUtils.key2CodingSchemeName(source_cs);
String source_version = DataUtils.key2CodingSchemeVersion(source_cs);


String vsdURI = (String) request.getSession().getAttribute("vsdURI");

String input_option = (String) request.getSession().getAttribute("input_option");
if (input_option == null) {
    input_option = "Code";
}

String input_option_label = input_option;

String algorithm = (String) request.getSession().getAttribute("algorithm");
if (algorithm != null && algorithm.compareTo("null") == 0) {
    algorithm = "";
} else if (algorithm == null) {
    algorithm = "exactMatch";
}


String property = (String) request.getSession().getAttribute("property");
if (property != null && property.compareTo("null") == 0) {
    property = "";
} else if (property == null) {
    property = "";
}


LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
java.lang.String valueSetDefinitionRevisionId = null;
ValueSetDefinition vsd = vsd_service.getValueSetDefinition(new URI(vsdURI), valueSetDefinitionRevisionId); 
String valueSetDefinitionName =	vsd.getValueSetDefinitionName(); 

//AbsoluteCodingSchemeVersionReferenceList acsvrl = vsd_service.getCodingSchemesInValueSetDefinition(new URI(vsdURI));

ResolvedConceptReferencesIterator iterator = null;
iterator = (ResolvedConceptReferencesIterator) request.getSession().getAttribute("rcr_iterator");
if (iterator != null) {
   codes = "";
   request.getSession().removeAttribute("rcr_iterator");
   while (iterator.hasNext()) {
       ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
       codes = codes + rcr.getConceptCode() + "\n";
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

    <p class="texttitle-blue"><b><%=identifier%>&nbsp;(<%=mapping_version%>)</b></p> 
  
             <% if (message != null) { request.getSession().removeAttribute("message"); %>
                 <p class="textbodyred">&nbsp;<%=message%></p>
            <% } %>  
            
  <table border="0" width="700px">

     
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
                
           

     </table>
     
    
     <table>
 

                 <tr>
 		  <td align="left" class="textbody">
 		      <b>Coding Scheme Reference</b>:
		  </td>
		  <td></td>
		 </tr>
		  
		  
		 <tr>
		 
		 <td colspan="2">
		  
 
               <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
                 <th class="dataTableHeader" scope="col" align="left">&nbsp;</th>
                 <th class="dataTableHeader" scope="col" align="left">Coding Scheme</th>
                 <th class="dataTableHeader" scope="col" align="left">Version</th>
                 <th class="dataTableHeader" scope="col" align="left">Tag</th>
 <%
 


Vector cs_uri_vec = DataUtils.getCodingSchemesInValueSetDefinition(vsdURI);
String cs_version = null;
String prev_cs_urn = "";
String checked = "";

 if (cs_uri_vec != null) {
 int k = -1;
 
             for (int i=0; i<cs_uri_vec.size(); i++) {
                    String cs_uri = (String) cs_uri_vec.elementAt(i);
                    String cs_name = DataUtils.uri2CodingSchemeName(cs_uri); 
                    String displayed_cs_name = cs_name;//DataUtils.getCodingSchemeName(cs_name, null); //uri2CodingSchemeName(cs_name);
                    
                    displayed_cs_name = DataUtils.getMetadataValue(DataUtils.getFormalName(cs_name), "display_name");
                    
		    Vector cs_version_vec = DataUtils.getCodingSchemeVersionsByURN(cs_uri);
		    
		    for (int j=0; j<cs_version_vec.size(); j++) {
		        cs_version = (String) cs_version_vec.elementAt(j);
 		    
 int lcv = i+1;		    
 		    
 System.out.println("coding_scheme_references.jsp cs_name: " + cs_name);
 
 System.out.println("coding_scheme_references.jsp cs_version: " + cs_version);
 		    
 		    String cs_tag = DataUtils.getVocabularyVersionTag(cs_name, cs_version);
 		    if (cs_tag == null) cs_tag = "";
 		    
 		    if (cs_name.compareTo(prev_cs_urn) != 0) {
 		       k++;
 		       prev_cs_urn = cs_name;
 		    }
 		    
 		    if (cs_uri_vec.size() == 1) {
 		        checked = "checked";
 		    } else if (cs_tag.compareToIgnoreCase("PRODUCTION") == 0) {
 		        checked = "checked";
 		    }
 	    
         
 		    if (k % 2 == 0) {
 		    %>
 		      <tr class="dataRowLight">
 		    <%
 			} else {
 		    %>
 		      <tr class="dataRowDark">
 		    <%
 			}
 		    %>    
 
 
 
 		<td>
 <input type="radio" name="<%=cs_name%>" value="<%=cs_version%>" <%=checked%> tabinex="1" />
 		</td>
 
 
 	
 		      <td class="dataCellText">
 			 <%=displayed_cs_name%>
 		      </td>
 		      <td class="dataCellText">
 			 <%=cs_version%>
 		      </td>
 		      <td class="dataCellText">
 			 <%=cs_tag%>
 		      </td>		      
 
         
 		      </tr>
               
              <%
                 }
                 }
 } else {
 %>
 <tr><td colspan="2">
 <p class="textbodyred">&nbsp;WARNING: Unable to retrieve coding scheme reference data from the server.</p>
 </td></tr>
 <%
 }
              %>                 
 
 
 <tr><td>&nbsp;</td></tr>
 
 
              </table>
 
             </td></tr>


<%     
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
                
 <!--               
                <tr>
		  <td align="left" class="textbody">
		      <b>Input Option</b>:&nbsp;<%=input_option%> 
		  </td>
                          <td>
                      &nbsp;
                          </td>
                </tr>     
                
                
                
     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
 -->
 
     <tr>               
                          <td align="left" class="textbody">
                              <b><%=input_option_label%>&nbsp;data:</b>
                          </td>
                          
     <td>  
             <table>
             <tr>
		     <td valign=top>    
			     <textarea name="codes" cols="50" rows=10 tabindex="3"><%=codes%></textarea>
			     &nbsp;
			    <h:commandButton id="import" value="import" action="#{mappingBean.importDataAction}"
			      image="#{basePath}/images/import.gif"
			      alt="Import"
			      tabindex="2">
			    </h:commandButton>             
		     </td>
             </tr>
             </table>
         
     </td>
     </tr>
     

	
                  <tr><td>
                    <h:commandButton id="continue" value="continue" action="#{mappingBean.showBatchFormAction}"
                      image="#{basePath}/images/continue.gif"
                      alt="Continue"
                      tabindex="2">
                    </h:commandButton>
                  </td>
                  <td></td>
                  </tr>	
	
     </table> 

     <input type="hidden" name="type" id="type" value="valueset">
     <input type="hidden" name="identifier" id="identifier" value="<%=identifier%>">
     <input type="hidden" name="mapping_version" id="mapping_version" value="<%=mapping_version%>">

     <input type="hidden" name="source_cs" id="source_cs" value="<%=source_cs%>">
     <input type="hidden" name="input_option" id="type" value="<%=input_option%>">
     <input type="hidden" name="vsdURI" id="type" value="<%=vsdURI%>">
     
     <input type="hidden" name="valueSetDefinitionName" id="type" value="<%=valueSetDefinitionName%>">
     
     <input type="hidden" name="source_scheme" id="source_scheme" value="<%=source_scheme%>">
     <input type="hidden" name="source_version" id="source_version" value="<%=source_version%>">
     
    
     
     <%
     if (input_option.compareTo("Property") == 0) {
     %>
         <input type="hidden" name="algorithm" id="algorithm" value="exactMatch">
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
