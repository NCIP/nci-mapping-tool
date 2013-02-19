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
MappingData mappingData = null;
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

List list = (ArrayList) request.getSession().getAttribute("data");
boolean show_rank_column = true;



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


//String data_value = (String) request.getSession().getAttribute("data_value");
String algorithm = (String) request.getSession().getAttribute("algorithm");
if (DataUtils.isNull(algorithm)) {
    algorithm = "";
}



List selected_matches = null;

System.out.println("\n===================== HashMap mapping_hmap =====================");

HashMap mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");
if (mapping_hmap == null) {
    mapping_hmap = new HashMap();
    request.getSession().setAttribute("mapping_hmap", mapping_hmap);
} 


Iterator it = mapping_hmap.keySet().iterator();


  source_scheme = DataUtils.getFormalName(source_abbrev);
  source_version = DataUtils.getVocabularyVersionByTag(source_scheme, null);

  target_scheme = DataUtils.getFormalName(target_abbrev);
  target_version = DataUtils.getVocabularyVersionByTag(target_scheme, null);
 
 
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
		  <td align="left" class="textbody">
		      <b>Input Option</b>:&nbsp;<%=input_option%> 
		  </td>
                          <td>
                      &nbsp;
                          </td>
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

		<%
		}
		%>


      </table>

<HR></HR>

      <table class="datatable">

  <tr><td>
  
	<h:commandButton id="save_all" value="save_all" action="#{mappingBean.saveAllMappingAction}"
	image="#{basePath}/images/save.gif"
	alt="Save"
	tabindex="3">
	</h:commandButton>  
  
        &nbsp;
        
	<h:commandButton
		id="Export_XML"
		value="Export_XML"
		image="#{subsetEditor_requestContextPath}/images/exportxml.gif"
		action="#{MappingBean.exportMappingToXMLAction}" 
		onclick="javascript:cursor_wait();"
		alt="Export to XML" >
	</h:commandButton>
	
  </td></tr>
  
<%
  
 
  for (int lcv=0; lcv<list.size(); lcv++) {


        String idx1_str = new Integer(lcv).toString();
 
        String input_data = (String) list.get(lcv);
        selected_matches = (ArrayList) mapping_hmap.get(input_data);
        
        int k = lcv+1;
        String item_label = new Integer(k).toString();
        
        if (input_option.compareToIgnoreCase("Name") == 0) {
            source_name = input_data;
        } else {
            source_code = input_data;
        }
        
        if (DataUtils.isNull(input_data)) input_data = "";
        if (DataUtils.isNull(source_code)) source_code = "";
        if (DataUtils.isNull(source_name)) source_name = "";


%>

		
			<tr> 
				<td class="textbody"><%=item_label%>. &nbsp;<%=input_data%>
				 
				      <a href="<%=request.getContextPath()%>/pages/ncimeta_search_results.jsf?type=ncimeta&opt=<%=input_option%>&idx=<%=k%>">
					<img src="<%= request.getContextPath() %>/images/search.png" width="15" height="15" alt="Search" border="0">
				      </a>      				 
				 
				 
				<%     
				if (selected_matches != null && selected_matches.size()>0) {
				%>

				      <a href="<%=request.getContextPath()%>/pages/ncimeta_mapping_form.jsf?action=approve&idx=<%=k%>">
					<img src="<%= request.getContextPath() %>/images/approve_details.gif" width="15" height="15" alt="Approve Details" border="0">
				      </a>  				

				
					<h:commandButton id="remove" value="remove" action="#{mappingBean.removeMappingAction}"
					image="#{basePath}/images/trash_16x16.gif" 
					alt="Remove"
					tabindex="2">
					</h:commandButton>				
				
				
				<% 
				}
				
				String action = (String) request.getParameter("action");
				if (action != null && action.compareTo("approve") == 0) {
				     String record_idx  = (String) request.getParameter("idx");
				     if (Integer.parseInt(record_idx) == k) {
				     %>
					<img src="<%= request.getContextPath() %>/images/small_checkmark__16_red.png" width="15" height="15" alt="Approved" border="0">
				     <%
				     }
				}
				
				%>
				</td>
				
			</tr>        
		
 
 
		 <%
	         
	         
	         if (selected_matches != null && selected_matches.size()>0) {

		 %>     
			   <tr><td>

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
				   
				   <th class="dataTableHeader" width="35px" scope="col" align="left">
					  Map Rank
				   </th>


				   <th class="dataTableHeader" width="60px" scope="col" align="left">Target</th>

				   <th class="dataTableHeader" scope="col" align="left">
					  Target Code
				   </th>

				   <th class="dataTableHeader" scope="col" align="left">
					  Target Name
				   </th>


			     <%
			     


		   for (int lcv2=0; lcv2<selected_matches.size(); lcv2++) {
		   
		         String idx2_str = new Integer(lcv2).toString();

			 mappingData = (MappingData) selected_matches.get(lcv2);
			 source_code = mappingData.getSourceCode();
			 source_name = mappingData.getSourceName();
			 source_namespace = mappingData.getSourceCodeNamespace();

			 rel = mappingData.getRel();
			 if (DataUtils.isNull(rel)) rel = "";
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

		         <td class="textbody">
			    <select id="rel" name="rel" size="1" tabindex="4">
			    <%

				    String[] rel_options = DataUtils.get_rel_options();
				    for (int i=0; i<rel_options.length; i++) {
					 String t = rel_options[i];
					 if (t.compareTo(rel) == 0) {
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


		         <td class="textbody">
			    <select id="score" name="score" size="1" tabindex="4">
			    <%

				    String[] score_options = DataUtils.get_score_options();
				    for (int i=0; i<score_options.length; i++) {
					 String t = score_options[i];
					 if (t.compareTo(score) == 0) {
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


			 <td class="datacoldark"><%=target_namespace%></td>
			 
			 <td class="datacoldark">

				 <a href="#"
				       onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=target_scheme%>&version=<%=target_version%>&code=<%=target_code%>'">
				       <%=target_code%>
				 </a>


			 </td>
			 <td class="datacoldark"><%=target_name%>
			 &nbsp;

	
      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/ncimeta_mapping_entry_notes.jsf?idx1=<%=idx1_str%>&idx2=<%=idx2_str%>',
        '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="13">
        <img src="<%= request.getContextPath() %>/images/edit.png" style="border: none">
      </a> 
				
			 </td>	     
		    </tr>

		
		 <%    
		 }
		 %>


			<tr>
			<td>

			<a href="<%=request.getContextPath()%>/pages/ncimeta_mapping_form.jsf?action=collapse&idx=<%=k%>">
			   <img src="<%= request.getContextPath() %>/images/up.png" width="15" height="15" alt="Hide" border="0">
			</a>  				
			</td>
			</tr>
			
			
		 
	    <%	 
	    }
	    %>        
	    

			  </table>
			  
		    </td></tr>        
 
 
 	    <tr><td><hr></hr></td></tr>       

  <%
  }
  %>
  
  
  <tr><td>
  
	<h:commandButton id="save" value="save" action="#{mappingBean.saveAllMappingAction}"
	image="#{basePath}/images/save.gif"
	alt="Save"
	tabindex="3">
	</h:commandButton>  
  
        &nbsp;
        
	<h:commandButton
		id="Export"
		value="Export"
		image="#{subsetEditor_requestContextPath}/images/exportxml.gif"
		action="#{MappingBean.exportMappingToXMLAction}" 
		onclick="javascript:cursor_wait();"
		alt="Export to XML" >
	</h:commandButton>
	
  </td></tr>
  
  
  </table>
  
     <input type="hidden" name="type" id="type" value="ncimeta">
     <input type="hidden" name="identifier" id="type" value="<%=identifier%>">
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

