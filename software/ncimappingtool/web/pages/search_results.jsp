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
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>


<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>

<%@ page import="gov.nih.nci.evs.browser.bean.MappingIteratorBean" %>
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

<%
boolean show_rank_column = true;
MappingData mappingData = null;
String basePath = request.getContextPath(); 
String message = (String) request.getSession().getAttribute("message");

request.getSession().removeAttribute("message");
String type = (String) request.getSession().getAttribute("type");

String identifier = (String) request.getSession().getAttribute("identifier");
if (identifier != null && identifier.compareTo("null") == 0) {
    identifier = "";
} else if (identifier == null) {
    identifier = "";
}
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

String algorithm = (String) request.getSession().getAttribute("algorithm");
if (DataUtils.isNull(algorithm)) {
    algorithm = "";
}

String input_option = (String) request.getSession().getAttribute("input_option");
String input_option_label = input_option;
input_option_label = input_option_label.toLowerCase();
List input_list = (ArrayList) request.getSession().getAttribute("data");


String idx_str = (String) request.getParameter("idx");
String option = input_option;

System.out.println("(search_results.jsp *) type: " + type);
System.out.println("(search_results.jsp *) idx_str: " + idx_str);
System.out.println("(search_results.jsp *) option: " + option);




int idx = Integer.parseInt(idx_str);
String data = (String) input_list.get(idx-1);


System.out.println("type: " + type);
System.out.println("idx_str: " + idx_str);
System.out.println("option: " + option); 

System.out.println("data: " + data); 
 
 
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
     
         <table border="0" width="700px">

            <% if (message != null) { request.getSession().removeAttribute("message"); %>
              <tr class="textbodyred">
                <td>
                <p class="textbodyred">&nbsp;<%=message%></p>
                </td>              
                <td></td>
              </tr>
            <% } %>    
           
            
	  <tr>
	    <td><div class="texttitle-blue"><%=identifier%></div></td>
	  </tr>

	  <tr><td>&nbsp;</td></tr>
	  
	  <%  
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
		      <b>From</b>:&nbsp;<%=source_abbrev%>&nbsp;(<%=source_scheme%>)
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
          }  else {
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
          }
          %>
          
                <tr>
		  <td align="left" class="textbody">
		      <b>Input Option</b>:&nbsp;<%=input_option%> 
		  </td>
                          <td>
                      &nbsp;
                          </td>
                </tr>                





		<%     
		if (input_option.compareToIgnoreCase("Code") != 0) {
			       //MappingUtils util = new MappingUtils();
			       Vector algorithms = MappingUtils.getAllSupportedSearchTechniqueNames();
		%>    

				<tr>
				    <td align="left" class="textbody">
				      <b>Algorithm</b>:

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
				    </td>
				    <td>&nbsp;</td>                
				</tr>

		<%
		}
		%>


      </table>

<HR></HR>


<%
      List list = null;
      
      
      
      
      if (type.compareTo("valueset") != 0) {
      
          list = new MappingSearchUtils().simpleSearch(type,
                             input_option,
                             data,
                             ncim_version,
                             source_abbrev,
                             target_abbrev,
                             source_cs,
                             target_cs,
                             algorithm);
                         
      } else if (type.compareTo("valueset") == 0) { 
          // to be implemented
          list = new ArrayList();           
      
      }
                                        
%>
          <table class="datatable">

          <th class="dataTableHeader" scope="col" align="left">
                 &nbsp;
          </th>
          
          <th class="dataTableHeader" width="60px" scope="col" align="left">Source</th>


          <th class="dataTableHeader" scope="col" align="left">
                 Source Code
          </th>

          <th class="dataTableHeader" scope="col" align="left">
                 Source Name
          </th>

          <th class="dataTableHeader" width="30px" scope="col" align="left">
                 REL
          </th>

<%
if (show_rank_column) {
%>
          <th class="dataTableHeader" width="35px" scope="col" align="left">
                 Map Rank
          </th>
<%
}
%>

          <th class="dataTableHeader" width="60px" scope="col" align="left">Target</th>

          <th class="dataTableHeader" scope="col" align="left">
                 Target Code
          </th>

          <th class="dataTableHeader" scope="col" align="left">
                 Target Name
          </th>


            <%



 if (list == null) {
  System.out.println("list == null???");
 } else {
     request.getSession().setAttribute("search_results", list);

 
 if (list != null && list.size() == 1) {
 
      request.getSession().setAttribute("mappingData", (MappingData) list.get(0));
 }
 
 
 
  for (int lcv=0; lcv<list.size(); lcv++) {
        String item_label = new Integer(lcv).toString();
  
        mappingData = (MappingData) list.get(lcv);
        source_code = mappingData.getSourceCode();
        source_name = mappingData.getSourceName();
        source_namespace = mappingData.getSourceCodeNamespace();

        rel = mappingData.getRel();
        score = new Integer(mappingData.getScore()).toString();
        target_code = mappingData.getTargetCode();
        target_name = mappingData.getTargetName();
        target_namespace = mappingData.getTargetCodeNamespace();

   
        source_scheme = mappingData.getSourceCodingScheme();
        source_version = mappingData.getSourceCodingSchemeVersion();
        target_scheme = mappingData.getTargetCodingScheme();
        target_version = mappingData.getTargetCodingSchemeVersion();
       
                
source_scheme = DataUtils.getFormalName(source_scheme);        
target_scheme = DataUtils.getFormalName(target_scheme);        

            %>

<tr>
<td>
<input type="checkbox" name="selected_list" value="<%=item_label%>"/>

</td>

                    <td class="datacoldark"><%=source_namespace%></td>
        <td class="datacoldark">
<a href="#"
      onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=source_scheme%>&version=<%=source_version%>&code=<%=source_code%>'">
      <%=source_code%>
</a>


        </td>
        <td class="datacoldark"><%=source_name%></td>


        <td class="textbody"><%=rel%></td>
        
<%
if (show_rank_column) {
%>        
        <td class="textbody"><%=score%></td>
<%
}
%>


        <td class="datacoldark"><%=target_namespace%></td>
        <td class="datacoldark">

<a href="#"
      onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=target_scheme%>&version=<%=target_version%>&code=<%=target_code%>'">
      <%=target_code%>
</a>


                    </td>
        <td class="datacoldark"><%=target_name%></td>

</tr>


               <%
               }
      }
%>               
 
                <tr>
                <td>&nbsp;</td><td></td>
                </tr>


<%
if (list != null && list.size() > 0) {
%>

<tr><td>
<h:commandButton id="generate" value="generate" action="#{mappingBean.saveMappingAction}"
image="#{basePath}/images/save.gif"
alt="Save"
tabindex="2">
</h:commandButton>
</td>
<td></td>
</tr>

<%                 
} else {
%>

<tr><td class="textbody">
No match found.
</td>
<td></td>
</tr>

<%
}
%>



          </table>
          
          
          
     <input type="hidden" name="type" id="type" value="ncimeta">
     <input type="hidden" name="identifier" id="type" value="<%=identifier%>">
     <input type="hidden" name="ncim_version" id="type" value="<%=ncim_version%>">
     <input type="hidden" name="source_abbrev" id="type" value="<%=source_abbrev%>">
     <input type="hidden" name="target_abbrev" id="type" value="<%=target_abbrev%>">
     <input type="hidden" name="input_option" id="type" value="<%=input_option%>">
     <input type="hidden" name="input_value" id="type" value="<%=data%>">
     
     <input type="hidden" name="idx_str" id="idx_str" value="<%=idx%>">
     
     
                 
                  
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

