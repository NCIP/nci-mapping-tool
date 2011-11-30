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
String basePath = request.getContextPath(); 
String message = (String) request.getSession().getAttribute("message");

request.getSession().removeAttribute("message");

String identifier = (String) request.getSession().getAttribute("identifier");
if (identifier != null && identifier.compareTo("null") == 0) {
    identifier = "";
} else if (identifier == null) {
    identifier = "";
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

List input_list = (ArrayList) request.getSession().getAttribute("data");
boolean show_rank_column = true;

String algorithm = (String) request.getSession().getAttribute("algorithm");
String data_value = (String) request.getSession().getAttribute("data_value");

String input_value  = (String) request.getParameter("value");
if (!DataUtils.isNull(input_value)) {
    data_value = input_value;
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

MappingData mappingData = null;

               
                

  source_scheme = DataUtils.getFormalName(source_abbrev);
  source_version = DataUtils.getVocabularyVersionByTag(source_scheme, null);

  target_scheme = DataUtils.getFormalName(target_abbrev);
  target_version = DataUtils.getVocabularyVersionByTag(target_scheme, null);
 

String type = (String) request.getParameter("type");
String idx_str = (String) request.getParameter("idx");
String option = (String) request.getParameter("opt");
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


<h:form id="matchedTerms">        
     
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
                
                <tr>
                <td></td><td></td>
                </tr>
                
              



		<%     
		if (input_option.compareTo("Name") == 0) {
			       MappingUtils util = new MappingUtils();
			       Vector algorithms = util.getSupportedSearchTechniqueNames();
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

				<tr>
				  <td align="left" class="textbody14">
				      <b><%=data%></b>


				    <h:commandButton id="generate" value="generate" action="#{mappingBean.ncimetaSearchAction}"
				      image="#{basePath}/images/search16.png"
				      alt="search"
				      tabindex="2">
				    </h:commandButton>


				  </td>
					  <td>
				      &nbsp;
					  </td>
				</tr>  	

		<%
		}
		%>
		
	
		
     </table>
     

<hr></hr>


<%

      
      List list = MappingUtils.process_ncimeta_mapping(ncim_version,
                                        source_abbrev,
                                        target_abbrev,
                                        input_option,
                                        algorithm,
                                        data);
      request.getSession().setAttribute("selection_list", list); 
      request.getSession().setAttribute("data_value", data);
      
%>
          <table class="datatable">

          <th class="dataTableHeader" scope="col" align="left">
                 &nbsp;
          </th>
          

          <th class="dataTableHeader" scope="col" align="left">
                 Source Code
          </th>

          <th class="dataTableHeader" scope="col" align="left">
                 Source Name
          </th>


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

        <td class="datacoldark">
<a href="#"
      onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=source_scheme%>&version=<%=source_version%>&code=<%=source_code%>'">
      <%=source_code%>
</a>


        </td>
        <td class="datacoldark"><%=source_name%></td>


        <td class="datacoldark">

<a href="#"
      onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=target_scheme%>&version=<%=target_version%>&code=<%=target_code%>'">
      <%=target_code%>
</a>

                    </td>
        <td class="datacoldark">
        <%=target_name%>

      &nbsp;
      
      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/ncimeta_concept_info.jsf?src_cd=<%=source_code%>&target_cd=<%=target_code%>',
        '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="13">
        <img src="<%= request.getContextPath() %>/images/Info.gif" style="border: none">
      </a> 
        
        
        </td>

</tr>


               <%
               }
      }
%>               
 
 
                <tr>
                <td>&nbsp;</td><td></td>
                </tr>
                
</table>
<table>
<tr><td>
<%
if (list != null && list.size() > 1) {
%>
                  <img src="<%= request.getContextPath() %>/images/selectAll.gif"
                    name="selectAll" alt="selectAll"
                    onClick="checkAll(document.matchedTerms.selected_list)" />

                  &nbsp;&nbsp; 

                  <img src="<%= request.getContextPath() %>/images/clear.gif"
                    name="selectAll" alt="selectAll"
                    onClick="uncheckAll(document.matchedTerms.selected_list)" />

                  &nbsp;&nbsp; 
<%
}
%>

                  
<h:commandButton id="save" value="save" action="#{mappingBean.saveMappingAction}"
image="#{basePath}/images/save.gif"
alt="Save"
tabindex="3">
</h:commandButton>
</td></tr>	
                 
</table>
          
          
          
     <input type="hidden" name="type" id="type" value="ncimeta">
     <input type="hidden" name="identifier" id="type" value="<%=identifier%>">
     <input type="hidden" name="ncim_version" id="type" value="<%=ncim_version%>">
     <input type="hidden" name="source_abbrev" id="type" value="<%=source_abbrev%>">
     <input type="hidden" name="target_abbrev" id="type" value="<%=target_abbrev%>">
     <input type="hidden" name="input_option" id="type" value="<%=input_option%>">
     
     <input type="hidden" name="data_value" id="data_value" value="<%=data%>">
                 
                  
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

