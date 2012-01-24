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
  
  String ncit_url = new DataUtils().getNCItURL();
  
  session = request.getSession(true);
  boolean readonly = false;
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

<!--
<body onload="doLoad()">
-->

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


<script type="text/javascript" language="javascript"> 

        function isBlank(str) {     return (!str || /^\s*$/.test(str)); } 


	function doLoad()
	{
		var counter = '<%= request.getSession().getAttribute("counter") %>'; 
		var total = '<%= request.getSession().getAttribute("total") %>'; 
		//alert(counter); 
		
		if (isBlank(counter) || counter != total) {
			setTimeout( "refresh()", 10*1000 );
		}
	}

	function refresh()
	{
		//window.location.reload( false );
		window.document.forms[0].submit(); 
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

String batch_status = (String) request.getSession().getAttribute("batch_status");
MappingData mappingData = null;
String basePath = request.getContextPath(); 
String message = (String) request.getSession().getAttribute("message");

request.getSession().removeAttribute("message");
String type = (String) request.getSession().getAttribute("type");

String ncim_version = null;
HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");

List selected_matches = null;
HashMap code2name_hmap = null;
HashMap mapping_hmap = null;

String mode = (String) request.getParameter("mode");
if (mode != null && mode.compareTo("readonly") == 0) {
        readonly = true;
} else {
    mode = "edit";
}

String action = (String) request.getParameter("action");
String id = (String) request.getParameter("id");
identifier = (String) request.getSession().getAttribute("identifier");
mapping_version = (String) request.getSession().getAttribute("mapping_version");


MappingObject obj = null;
if (action != null && (action.compareTo("view") == 0 || action.compareTo("edit") == 0)) {
	identifier = (String) request.getParameter("identifier");
	mapping_version = (String) request.getParameter("version");
	mappings = (HashMap) request.getSession().getAttribute("mappings");
	obj = (MappingObject) mappings.get(id);
}


String target_codingscheme = (String) request.getParameter("target_scheme");
String target_codingschemeversion = (String) request.getParameter("target_version");	


if (id != null) {

    mappings = (HashMap) request.getSession().getAttribute("mappings");
    if (mappings == null) {
        mappings = new HashMap();
        request.getSession().setAttribute("mappings", mappings);
    } 
    obj = (MappingObject) mappings.get(id);

	if (obj == null) {
	    System.out.println("obj with id not found??? " + id);
	} else {
	    //System.out.println("obj with id found: " + id);
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
		    
	}

} else if (type.compareTo("ncimeta") == 0) {
	ncim_version = (String) request.getSession().getAttribute("ncim_version");
	if (ncim_version != null && ncim_version.compareTo("null") == 0) {
	    ncim_version = "";
	} else if (ncim_version == null) {
	    ncim_version = "";
	}
	
	source_abbrev = (String) request.getSession().getAttribute("source_abbrev");
	target_abbrev = (String) request.getSession().getAttribute("target_abbrev");
	
	source_scheme = "";
	source_version = "";
	
	try {
		if (source_abbrev != null && source_abbrev.compareTo("") != 0) {
			source_scheme = DataUtils.getFormalName(source_abbrev);
			if (source_scheme != null) {
			    source_version = DataUtils.getVocabularyVersionByTag(source_scheme, null);
			}
		}
	} catch (Exception ex) {
	
	}

        target_scheme = DataUtils.getFormalName(target_abbrev);
        target_version = DataUtils.getVocabularyVersionByTag(target_scheme, null);
	
	
	
} else if (type.compareTo("codingscheme") == 0) {

	source_cs = (String) request.getSession().getAttribute("source_cs");
	target_cs = (String) request.getSession().getAttribute("target_cs");	


	source_scheme = DataUtils.key2CodingSchemeName(source_cs);
	source_version = DataUtils.key2CodingSchemeVersion(source_cs);

	target_scheme = DataUtils.key2CodingSchemeName(target_cs);
	target_version = DataUtils.key2CodingSchemeVersion(target_cs);


	
} else if (type.compareTo("valueset") == 0) {
	source_cs = (String) request.getSession().getAttribute("source_cs");
	vsdURI = (String) request.getSession().getAttribute("vsdURI");
	valueSetDefinitionName = (String) request.getSession().getAttribute("valueSetDefinitionName");

	source_scheme = DataUtils.key2CodingSchemeName(source_cs);
	source_version = DataUtils.key2CodingSchemeVersion(source_cs);
	
	
	target_codingscheme = (String) request.getParameter("target_scheme");
	target_codingschemeversion = (String) request.getParameter("target_version");	
	
	
}  


String input_option = (String) request.getSession().getAttribute("input_option");
String input_option_label = input_option;
input_option_label = input_option_label.toLowerCase();
List list = (ArrayList) request.getSession().getAttribute("data");


boolean show_rank_column = true;

HashSet expanded_hset = (HashSet) request.getSession().getAttribute("expanded_hset");
if (expanded_hset == null) {
    expanded_hset = new HashSet();
    request.getSession().setAttribute("expanded_hset", expanded_hset);
} 


mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");


if (mapping_hmap == null) {
    mapping_hmap = new HashMap();
    request.getSession().setAttribute("mapping_hmap", mapping_hmap);
} 


if (list != null && list.size() > 0) {
    if (input_option.compareToIgnoreCase("Code") == 0) {
	    if (source_scheme != null && source_scheme.compareTo("") != 0) {
		    code2name_hmap = DataUtils.code2Name(source_scheme, source_version, list);
		    request.getSession().setAttribute("code2name_hmap", code2name_hmap);
	    }
    }
}
    

if (input_option.compareToIgnoreCase("Code") == 0) {
    code2name_hmap = (HashMap) request.getSession().getAttribute("code2name_hmap");
    if (code2name_hmap == null) {
	code2name_hmap =DataUtils.code2Name(source_scheme, source_version, list);
	request.getSession().setAttribute("code2name_hmap", code2name_hmap);
    }
}  


boolean hasContent = false;
if (mapping_hmap.keySet().size() > 0) {
   hasContent = true;
}


String algorithm = (String) request.getSession().getAttribute("algorithm");
if (DataUtils.isNull(algorithm)) {
    algorithm = "exactMatch";
}


String refresh_on = (String) request.getSession().getAttribute("refresh");



boolean show_refresh_button = true;
if (DataUtils.isNull(refresh_on)) {
    show_refresh_button = false;
}


if (show_refresh_button) {
    expanded_hset.clear();
    for (int m=0; m<list.size(); m++) {
	String idx1_str = new Integer(m).toString();
	String input_data = (String) list.get(m);
	int k = m+1;
	String item_label = new Integer(k).toString();
	expanded_hset.add(item_label);
    }
}


boolean collapse_all = false;
boolean expand_all = false;

if (!DataUtils.isNull(action) && action.compareTo("collapseAll") == 0) {
    collapse_all = true;
    expand_all = false;
    expanded_hset.clear();
    
} else if (!DataUtils.isNull(action) && action.compareTo("expandAll") == 0) {
    collapse_all = false;
    expand_all = true;
    
    expanded_hset.clear();
    for (int m=0; m<list.size(); m++) {
	String idx1_str = new Integer(m).toString();
	String input_data = (String) list.get(m);
	int k = m+1;
	String item_label = new Integer(k).toString();
	expanded_hset.add(item_label);
    }    
}


if(!hasContent) {
   expand_all = false;
   collapse_all = false;
   
   expanded_hset.clear();
   request.getSession().setAttribute("expanded_hset", expanded_hset);
}


String record_idx = null;


HashMap status_hmap = (HashMap) request.getSession().getAttribute("status_hmap");
if (status_hmap == null) {
	status_hmap = new HashMap();
	request.getSession().setAttribute("status_hmap", status_hmap);
}

if (action != null && action.compareTo("approve") == 0) {
    record_idx  = (String) request.getParameter("idx");
    int approved_idx = Integer.parseInt(record_idx);
    status_hmap.put(list.get(approved_idx), "approved"); 
    request.getSession().setAttribute("status_hmap", status_hmap);
}


if (action != null && action.compareTo("expand") == 0) {
    String expand_idx  = (String) request.getParameter("idx");
    expanded_hset.add(expand_idx);
    request.getSession().setAttribute("expanded_hset", expanded_hset);
} else if (action != null && action.compareTo("collapse") == 0) {
    String expand_idx  = (String) request.getParameter("idx");
    expanded_hset.remove(expand_idx);
    request.getSession().setAttribute("expanded_hset", expanded_hset);
}


if (!expand_all && !collapse_all && hasContent) {
   expand_all = true;
   collapse_all = false;
}


Iterator it = mapping_hmap.keySet().iterator();


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

<p class="texttitle-blue"><%=identifier%>&nbsp;(<%=mapping_version%>)</p>

            <% if (message != null) { request.getSession().removeAttribute("message"); %>
                <p class="textbodyred">&nbsp;<%=message%></p>
            <% } %>    
 
             <% if (batch_status != null) { %>
                 <p class="textbodyred">&nbsp;<%=batch_status%></p>
            <% } %> 
            
          
         <table> 
          
	  
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
				    
<%


if (type.compareTo("codingscheme") == 0) {				    
%>				    
				    <td align="right" class="textbody10">
				    
		<a class="textbody10" href="<%=request.getContextPath()%>/pages/advanced_search.jsf?" >
		   Advanced
		</a>			
		                    
				    </td>
<%				    
} else {
%>
                          <td>
                      &nbsp;
                          </td>
<%                          
}
%>
				</tr>

		<%
		//}
		%>
		
		
		


      </table>

<HR></HR>

      <table class="datatable">
        <tr><td>
<%
if (show_refresh_button) {
%>
  	<h:commandButton
  		id="Refresh_Form"
  		value="Refresh_Form"
  		image="#{basePath}/images/refresh.png"
  		action="#{mappingBean.refreshFormAction}" 
  		alt="Refresh" >
  	</h:commandButton>
  	
  &nbsp;
<%  
} else {
%>  

	<%
	if (collapse_all || expanded_hset.isEmpty()) {
	%>
		<a href="<%=request.getContextPath()%>/pages/batch_mapping_form.jsf?action=expandAll&mode=<%=mode%>">
		   <img src="<%= request.getContextPath() %>/images/expandAll.png" width="18" height="18" alt="Expand All" border="0">
		</a>
	<%  
	} else {
	%>
		<a href="<%=request.getContextPath()%>/pages/batch_mapping_form.jsf?action=collapseAll&mode=<%=mode%>">
		   <img src="<%= request.getContextPath() %>/images/collapseAll.png" width="18" height="18" alt="Collapse All" border="0">
		</a>
	<%  
	} 
	%>
	
<%	
if (!readonly) {	
%>	
        &nbsp;

  	<h:commandButton
  		id="Submit_Form"
  		value="Submit_Form"
  		image="#{basePath}/images/submit.gif"
  		action="#{mappingBean.submitBatchAction}" 
  		alt="Submit Batch" >
  	</h:commandButton>
  	&nbsp;
  	
	<h:commandButton id="save_all" value="save_all" action="#{mappingBean.saveAllMappingAction}"
	image="#{basePath}/images/save.gif"
	alt="Save"
	tabindex="3">
	</h:commandButton>  
  
        &nbsp;

<%
}
%>

	<h:commandButton
		id="Export_XML"
		value="Export_XML"
		image="#{basePath}/images/exportxml.gif"
		action="#{mappingBean.exportMappingToXMLAction}" 
		onclick="javascript:cursor_wait();"
		alt="Export to XML" >
	</h:commandButton>  
	

  	
<%
}  	
        
%>        

	
  </td></tr>
  
<%
  String idx1_str = null;
  String item_label = null;
  String input_data = null;
  int lcv=0;
  for (lcv=0; lcv<list.size(); lcv++) {
        idx1_str = new Integer(lcv).toString();
 
        input_data = (String) list.get(lcv);
        selected_matches = (ArrayList) mapping_hmap.get(input_data);
        
        int k = lcv+1;
        item_label = new Integer(k).toString();
        
        if (input_option.compareToIgnoreCase("Name") == 0) {
            source_name = input_data;
            source_code = "N/A";
        } else {
            source_code = input_data;
        }
        
        if (DataUtils.isNull(input_data)) input_data = "";
        if (DataUtils.isNull(source_code)) source_code = "";
        if (DataUtils.isNull(source_name)) source_name = "";
        
       

%>

		
			<tr> 
<%			
if (input_option.compareToIgnoreCase("Code") == 0) {
    String concept_name = null;
    if (code2name_hmap != null) {
        concept_name = (String) code2name_hmap.get(input_data);
	if (concept_name == null) {
	    concept_name = "WARNING: Unidentifiable code";
	}
    }
    if (concept_name != null) {
%>    
        <td class="textbody"><%=item_label%>. &nbsp;<%=input_data%>&nbsp;(<%=concept_name%>)
<%
    } else {
%>    
        <td class="textbody"><%=item_label%>. &nbsp;<%=input_data%>
<%
    }
} else {
%>			
    <td class="textbody"><%=item_label%>. &nbsp;<%=input_data%>
<%    
}
%>



<%	
if (!readonly) {
   if (type.compareTo("valueset") != 0) {

%>				 
				      <a href="<%=request.getContextPath()%>/pages/search_results.jsf?type=<%=type%>&opt=<%=input_option%>&idx=<%=item_label%>">
					<img src="<%= request.getContextPath() %>/images/search.png" width="15" height="15" alt="Search" border="0">
				      </a>      				 

                                      &nbsp;
   <%
   }  
   %>
         
<%
if (type.compareTo("codingscheme") == 0) {
%>

      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/manual_mapping.jsf?idx1=<%=idx1_str%>',
        '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="13">
        <img src="<%= request.getContextPath() %>/images/user.png" style="border: none">
      </a> 


<%    
}
%> 

<%    
}
%>      
				 
				<%     
				if (selected_matches != null && selected_matches.size()>0) {
				%>


<%	
if (!readonly) {	
%>

                                      &nbsp;
				      <a href="<%=request.getContextPath()%>/pages/batch_mapping_form.jsf?action=approve&idx=<%=lcv%>&mode=<%=mode%>">
					<img src="<%= request.getContextPath() %>/images/approve_details.gif" width="15" height="15" alt="Approve Details" border="0">
				      </a>  				

				      &nbsp; 
<%    
}
%>


<%	
if (!readonly) {	
%>
  	<h:commandButton
  		id="remove_mapping"
  		value="remove_mapping"
  		image="#{basePath}/images/trash_16x16.gif"
  		action="#{mappingBean.removeMappingAction}" 
  		alt="Remove" >
  	</h:commandButton>
  	
<%    
}
%>		
				

                                      <% 
                                      if (!expanded_hset.contains(item_label)) {
                                      %>
                                      &nbsp;	
				      <a href="<%=request.getContextPath()%>/pages/batch_mapping_form.jsf?action=expand&idx=<%=k%>&mode=<%=mode%>">
					   <img src="<%= request.getContextPath() %>/images/down.png" width="15" height="15" alt="Hide" border="0">
				      </a>
				      <%
				      }
				      %>
				      

				      
				
				<% 
				}
				
if (!readonly) {				
				
				     
				if (status_hmap.containsKey(input_data)) {     
				     
				     %>
					<img src="<%= request.getContextPath() %>/images/small_checkmark__16_red.png" width="15" height="15" alt="Approved" border="0">
				     <%
				     
				}
}				
				
				%>
				</td>
				
			</tr>        
		
 
 
		 <%
	         
	         
	         if (selected_matches != null && selected_matches.size()>0 && expanded_hset.contains(item_label)) {

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
			     

                   String checkbox_name = "checkbox" + idx1_str;
		   for (int lcv2=0; lcv2<selected_matches.size(); lcv2++) {
		   
		         String rel_id = "rel" + "_" + lcv + "_" + lcv2;
		         String score_id = "score" + "_" + lcv + "_" + lcv2;
		         
		   
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
			 
target_codingscheme = target_scheme;
target_codingschemeversion = target_version;
			 
			 
			     %>

		    <tr>
			 
			 <td>
			 <input type="checkbox" name="<%=checkbox_name%>" value="<%=idx2_str%>" />
			 </td>

			 <td class="datacoldark"><%=source_namespace%></td>
			 
			 
<%
if (source_code.compareTo("N/A") != 0) {

%>
                                 <td class="datacoldark">
				 <a href="#"
				       onclick="javascript:openNewWindow('<%=ncit_url%>ConceptReport.jsp?dictionary=<%=source_scheme%>&version=<%=source_version%>&code=<%=source_code%>')">
				       <%=source_code%>
				 </a>
				 </td>
<%				 
} else {
%>

                                 <td class="datacoldark"><%=source_code%></td>
<%    
}
%>
			 
			 <td class="datacoldark"><%=source_name%></td>

		         <td class="textbody">
			    <select id="<%=rel_id%>" name="<%=rel_id%>" size="1" tabindex="4">
			    <%

				    String[] rel_options = DataUtils.rel_options;
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
			    <select id="<%=score_id%>" name="<%=score_id%>" size="1" tabindex="4">
			    <%

				    String[] score_options = DataUtils.score_options;
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
				       onclick="javascript:openNewWindow('<%=ncit_url%>ConceptReport.jsp?dictionary=<%=target_scheme%>&version=<%=target_version%>&code=<%=target_code%>')">
				       <%=target_code%>
				 </a>


			 </td>
			 <td class="datacoldark"><%=target_name%>
			 &nbsp;

		
<%	
if (!readonly) {	
%>			
	
      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/entry_notes.jsf?idx1=<%=idx1_str%>&idx2=<%=idx2_str%>',
        '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="13">
        <img src="<%= request.getContextPath() %>/images/edit.png" style="border: none">
      </a> 
      
      
      &nbsp;

<%	
}



%>



      
      <a href="#" onclick="javascript:window.open('<%=request.getContextPath() %>/pages/concept_info.jsf?target_scheme=<%=target_codingscheme%>&target_version=<%=target_codingschemeversion%>&src_cd=<%=input_data%>&target_cd=<%=target_code%>',
        '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');" tabindex="13">
        <img src="<%= request.getContextPath() %>/images/Info.gif" style="border: none">
      </a>       
      
				
			 </td>	     
		    </tr>

		
		 <%    
		 }
		 %>



		        <% 
		        if (expanded_hset.contains(item_label)) {
		        %>

			<tr>
			<td>

			<a href="<%=request.getContextPath()%>/pages/batch_mapping_form.jsf?action=collapse&idx=<%=k%>&mode=<%=mode%>">
			   <img src="<%= request.getContextPath() %>/images/up.png" width="15" height="15" alt="Hide" border="0">
			</a>
			
			</td>
			</tr>
			
			<%    
			}
			%>
			
		 
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

<%
if (show_refresh_button) {
%>

  	<h:commandButton
  		id="RefreshForm"
  		value="RefreshForm"
  		image="#{basePath}/images/refresh.png"
  		action="#{mappingBean.refreshFormAction}" 
  		alt="Refresh" >
  	</h:commandButton>
  	
  &nbsp;
<%  
} else {
%> 

	<%
	
	
	if (collapse_all || expanded_hset.isEmpty()) {
	%>
		<a href="<%=request.getContextPath()%>/pages/batch_mapping_form.jsf?action=expandAll&mode=<%=mode%>">
		   <img src="<%= request.getContextPath() %>/images/expandAll.png" width="18" height="18" alt="Expand All" border="0">
		</a>
	<%  
	} else {
	%>
		<a href="<%=request.getContextPath()%>/pages/batch_mapping_form.jsf?action=collapseAll&mode=<%=mode%>">
		   <img src="<%= request.getContextPath() %>/images/collapseAll.png" width="18" height="18" alt="Collapse All" border="0">
		</a>
	<%  
	} 
	%>
	
	
<%	
if (!readonly) {	
%>	
	
        &nbsp;

  	<h:commandButton
  		id="SubmitForm"
  		value="SubmitForm"
  		image="#{basePath}/images/submit.gif"
  		action="#{mappingBean.submitBatchAction}" 
  		alt="Submit Batch" >
  	</h:commandButton>
  	&nbsp;
  	
	<h:commandButton id="save" value="save" action="#{mappingBean.saveAllMappingAction}"
	image="#{basePath}/images/save.gif"
	alt="Save"
	tabindex="3">
	</h:commandButton>  
  
        &nbsp;


<%  
} 
%>
	
	
	<h:commandButton
		id="Export"
		value="Export"
		image="#{basePath}/images/exportxml.gif"
		action="#{mappingBean.exportMappingToXMLAction}" 
		onclick="javascript:cursor_wait();"
		alt="Export to XML" >
	</h:commandButton>  	

        &nbsp;
	
	
<%
}  	
        
%>  




	
  </td></tr>
  
  
  </table>
  
     <input type="hidden" name="type" id="type" value="<%=type%>" />
     <input type="hidden" name="identifier" id="identifier" value="<%=identifier%>" />
     <input type="hidden" name="input_option" id="input_option" value="<%=input_option%>" />
                 
              
     <input type="hidden" name="remove_idx" id="remove_idx" value="" />  
     
<%     
if (type.compareTo("ncimeta") == 0) {
%>
     <input type="hidden" name="ncim_version" id="ncim_version" value="<%=ncim_version%>" />
     <input type="hidden" name="source_abbrev" id="source_abbrev" value="<%=source_abbrev%>" />
     <input type="hidden" name="target_abbrev" id="target_abbrev" value="<%=target_abbrev%>" />
<%     
} else if (type.compareTo("codingscheme") == 0) {


%>
     <input type="hidden" name="source_cs" id="source_cs" value="<%=source_cs%>" />
     <input type="hidden" name="target_cs" id="target_cs" value="<%=target_cs%>" />
<% 
} else if (type.compareTo("valueset") == 0) {


%>     
     <input type="hidden" name="source_cs" id="source_cs" value="<%=source_cs%>" />
     <input type="hidden" name="vsdURI" id="vsdURI" value="<%=vsdURI%>" />
     <input type="hidden" name="valueSetDefinitionName" id="valueSetDefinitionName" value="<%=valueSetDefinitionName%>" />
     
     
     <input type="hidden" name="target_scheme" id="target_scheme" value="<%=target_codingscheme%>" />
     <input type="hidden" name="target_version" id="target_version" value="<%=target_codingschemeversion%>" />
     
     
<%
}
%>


     <input type="hidden" name="identifier" id="identifier" value="<%=identifier%>" />
     <input type="hidden" name="mapping_version" id="mapping_version" value="<%=mapping_version%>" />

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

