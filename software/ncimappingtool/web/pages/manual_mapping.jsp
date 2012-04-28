<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>

<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>


<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.LexGrid.concepts.*" %>
<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.*" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.*" %>

<%@ page import="org.LexGrid.concepts.Presentation" %>
<%@ page import="org.LexGrid.concepts.Definition" %>
<%@ page import="org.LexGrid.commonTypes.Source" %>
<%@ page import="org.LexGrid.commonTypes.Property" %>



<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/yahoo-min.js" ></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/event-min.js" ></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/dom-min.js" ></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/animation-min.js" ></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/container-min.js" ></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/connection-min.js" ></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/autocomplete-min.js" ></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/treeview-min.js" ></script>
<%
  String basePath = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
  <title>Manual Mapping</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/fonts.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/grids.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/code.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/tree.css" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>

  <script language="JavaScript">

	function refreshParent() {
	   window.opener.history.go(0); 
	   window.close();
	}


  </script>
</head>
<body>
  <f:view>
    <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
    <!-- End Skip Top Navigation --> 
    <div id="popupContainer">
      <!-- nci popup banner -->
      <div class="ncipopupbanner">
        <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute"><img src="<%=basePath%>/images/nci-banner-1.gif" width="440" height="39" border="0" alt="National Cancer Institute" /></a>
        <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute"><img src="<%=basePath%>/images/spacer.gif" width="48" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a>
      </div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
      
        <a name="evs-content" id="evs-content"></a>
        <table class="evsLogoBg" cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td valign="top">
            <a href="http://evs.nci.nih.gov/" target="_blank" alt="Enterprise Vocabulary Services">
              <img src="<%=basePath%>/images/evs-popup-logo.gif" width="213" height="26" alt="EVS: Enterprise Vocabulary Services" title="EVS: Enterprise Vocabulary Services" border="0" />
            </a>
          </td>
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();" onclick="window.opener.location.reload(true);" >
              <img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>


<%
HashMap mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");
if (mapping_hmap == null) {
    mapping_hmap = new HashMap();
    synchronized (request.getSession()) {
    	request.getSession().setAttribute("mapping_hmap", mapping_hmap);
    }
} 
List list = (ArrayList) request.getSession().getAttribute("data");
String type = (String) request.getSession().getAttribute("type");

System.out.println("manual_mapping type: " + type);

String idx1_str = request.getParameter("idx1");
if (!DataUtils.isNull(idx1_str)) {
    request.getSession().setAttribute("idx1_str", idx1_str);
} else {
    idx1_str = (String) request.getSession().getAttribute("idx1_str");
}


System.out.println("manual_mapping idx1_str: " + idx1_str);

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
//System.out.println("(*) new target_code: " + target_code);


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

String message = (String) request.getSession().getAttribute("message");

Entity target_concept = null;
String target_concept_name = null;
Presentation[] target_presentations = null;
Definition[] target_definitions = null;
Property[] target_properties = null;
ArrayList target_superconceptList = null;
ArrayList src_superconceptList = new ArrayList();
ArrayList src_subconceptList = new ArrayList();
ArrayList target_subconceptList = new ArrayList();


System.out.println("source_scheme: " + source_scheme);
System.out.println("source_version: " + source_version);
System.out.println("target_scheme: " + target_scheme);
System.out.println("target_version: " + target_version);


Entity src_concept = null;
String source_concept_name = null;
String sourceCodeNamespace = null;


Presentation[] src_presentations = null;
Definition[] src_definitions = null;
Property[] src_properties = null;

HashMap hmap_super = null;				

if (!DataUtils.isNull(source_scheme) && source_scheme.compareTo("LOCAL DATA") != 0) {

   src_concept = MappingUtils.getConceptByCode(source_scheme, source_version, null, source_code);
   source_concept_name = src_concept.getEntityDescription().getContent();
   sourceCodeNamespace = src_concept.getEntityCodeNamespace();

	hmap_super = TreeUtils.getSuperconcepts(source_scheme, source_version, source_code);
	if (hmap_super != null) {
		TreeItem ti = (TreeItem) hmap_super.get(source_code);
		if (ti != null) {
			for (String association : ti._assocToChildMap.keySet()) {
				List<TreeItem> children =
					ti._assocToChildMap.get(association);
				for (TreeItem childItem : children) {
					src_superconceptList.add(childItem._text + "|"
						+ childItem._code);
				}
			}
		}
	}
	SortUtils.quickSort(src_superconceptList);

	src_presentations = src_concept.getPresentation();
	src_definitions = src_concept.getDefinition();
	src_properties = src_concept.getProperty();

	src_subconceptList =
	   TreeUtils.getSubconceptNamesAndCodes(source_scheme, source_version, source_code);
	SortUtils.quickSort(src_subconceptList);

}



String targetCodeNamespace = null;

if (DataUtils.isNull(target_code)) {
    target_code = "";
} else {

    target_concept = MappingUtils.getConceptByCode(target_scheme, target_version, null, target_code);
    target_concept_name = target_concept.getEntityDescription().getContent();
    
    target_presentations = target_concept.getPresentation();
    target_definitions = target_concept.getDefinition();
    target_properties = target_concept.getProperty();
    
    targetCodeNamespace = target_concept.getEntityCodeNamespace();

	target_superconceptList = new ArrayList();
	hmap_super = TreeUtils.getSuperconcepts(target_scheme, target_version, target_code);
	if (hmap_super != null) {
		TreeItem ti = (TreeItem) hmap_super.get(target_code);
		if (ti != null) {
			for (String association : ti._assocToChildMap.keySet()) {
				List<TreeItem> children =
					ti._assocToChildMap.get(association);
				for (TreeItem childItem : children) {
					target_superconceptList.add(childItem._text + "|"
						+ childItem._code);
				}
			}
		}
	}
	SortUtils.quickSort(target_superconceptList);	

}


if (data_value.indexOf("|") != -1) {
    Vector w = DataUtils.parseData(data_value);
    source_concept_name = (String) w.elementAt(1);
}


%>

        <div id="popupContentArea">

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
            


<tr valign="top" align="left">
           <td align="left" class="textbody" >      
<h:commandButton id="refresh" value="refresh" action="#{mappingBean.manualMappingAction}"
image="#{basePath}/images/refresh.png"
alt="Refresh"
tabindex="2">
</h:commandButton>
	  </td>
</tr>





            
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>Data</b>:&nbsp;<%=data_value%> 
                          </td>
                  </tr>
                  
                  
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                          <%
                          if (DataUtils.isNull(source_version)) {
                          %>
                             <b>From</b>:&nbsp;<%=source_scheme%>
                          <%
                          } else {
                          %>
                             <b>From</b>:&nbsp;<%=source_scheme%>&nbsp;(version: <%=source_version%>)  
                          
                          <%
                          }
                          %>
                          </td>
                  </tr>
              
                 
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>Concept</b>:&nbsp;<%=source_concept_name%>&nbsp;(code: <%=source_code%>) 
                          </td>
                  
              
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>To</b>:&nbsp;<%=target_scheme%>&nbsp;(version: <%=target_version%>) 
                          </td>
                  </tr>
                  
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>Target concept code</b>:&nbsp;<input name="target_code" id="target_code" value="<%=target_code%>" />


                       &nbsp; 
				 
		      <a href="<%=request.getContextPath()%>/pages/search.jsf?idx1=<%=idx1_str%>">
			<img src="<%= request.getContextPath() %>/images/search.png" width="15" height="15" alt="Search" border="0">
		      </a>   
				      
                              
                          </td>
                  </tr>

<%
if (target_code.compareTo("") != 0) {

%>
<tr><td>&nbsp;</td></tr>
<tr><td>


		    <h:commandButton id="save" value="save" action="#{mappingBean.saveManualMappingAction}"
		      image="#{basePath}/images/save.gif"
		      alt="Save"
		      tabindex="2">
		    </h:commandButton>

&nbsp;



		      <a href="javascript:refreshParent();">
			<img src="<%= request.getContextPath() %>/images/close.gif" alt="Close" border="0">
		      </a>   
</td></tr>

<%
}
%>
              
              
         </table>       


<%
if (!DataUtils.isNull(source_scheme) && source_scheme.compareTo("LOCAL DATA") != 0) {
%>

<hr></hr>

          <table width="580px" cellpadding="3" cellspacing="0" border="0">
                  
                <tr>
		  <td align="left" class="textbody">
		      <b>Source</b>:&nbsp;<%=source_scheme%>&nbsp;(<%=source_version%>)
		  </td>
		</tr> 
                <tr>
		  <td align="left" class="textbody">
		      <b>Version</b>:&nbsp;<%=source_version%>
		  </td>
                </tr>		
                <tr>
		  <td align="left" class="textbody">
		      <b>Concept</b>:&nbsp;<%=source_concept_name%>&nbsp;(code: <%=source_code%>)
		  </td>
                </tr>

          </table>


          <table class="datatable">
               <tr>
                 <td>

		   <table class="datatable">

		   <th class="dataTableHeader" scope="col" align="left">
			  Synonyms:
		   </th>

		   <th class="dataTableHeader" scope="col" align="left">
			  &nbsp;
		   </th>
<%		   
         for (int i = 0; i < src_presentations.length; i++) {
             String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
             Presentation p = src_presentations[i];
%>             
             
                <tr class="<%=rowColor%>">
	     		  <td align="left" class="textbody">
	     		      <%=p.getPropertyName()%>
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      <%=p.getValue().getContent()%>
	     		  </td>	     		  
	     		
                </tr>
<%
         }
%>         
                   </table>
          
                 </td>
              </tr>
              
              
               <tr>
                 <td>

		   <table class="datatable">


		   <th class="dataTableHeader" scope="col" align="left">
			  Definitions:
		   </th>

		   <th class="dataTableHeader" scope="col" align="left">
			  &nbsp;
		   </th>
<%		   
         for (int i = 0; i < src_definitions.length; i++) {
             String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
             Definition p = src_definitions[i];
             
             Source source = p.getSource(0);
             String src = source.getContent();
%>             
             
                <tr class="<%=rowColor%>">
	     		  <td align="left" class="textbody">
	     		      <%=p.getPropertyName()%>
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      <%=p.getValue().getContent()%>&nbsp;[<%=src%>]
	     		  </td>	     		  
	     		
                </tr>
<%
         }
         if (src_definitions.length == 0) {

%>
                <tr>
	     		  <td align="left" class="textbody">
	     		      None
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      &nbsp;
	     		  </td>	     		  
	     		
                </tr>
<%                 
         }
         
%>               
        
                   </table>
          
                 </td>
              </tr>              



               <tr>
                 <td>

		   <table class="datatable">

		   <th class="dataTableHeader" scope="col" align="left">
			  Other Properties:
		   </th>

		   <th class="dataTableHeader" scope="col" align="left">
			  &nbsp;
		   </th>
        
        
<%
         for (int i = 0; i < src_properties.length; i++) {
             String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
             Property p = src_properties[i];

%>             
             
                <tr class="<%=rowColor%>">
	     		  <td align="left" class="textbody">
	     		      <%=p.getPropertyName()%>
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      <%=p.getValue().getContent()%>
	     		  </td>	     		  
	     		
                </tr>
<%
         }
         if (src_properties.length == 0) {

%>
                <tr>
	     		  <td align="left" class="textbody">
	     		      None
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      &nbsp;
	     		  </td>	     		  
	     		
                </tr>
<%                 
         }
         
%>               
        
                   </table>
          
                 </td>
              </tr>            
   

               <tr>
                 <td>

		   <table class="datatable">

		   <th class="dataTableHeader" scope="col" align="left">
			  Parents:
		   </th>

        
        
    <%
        for (int i=0; i<src_superconceptList.size(); i++) {
          String s = (String) src_superconceptList.get(i);
          Vector ret_vec = DataUtils.parseData(s, "|");
          String cName = (String) ret_vec.elementAt(0);
          String cCode = (String) ret_vec.elementAt(1);
          String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
    %>
          <tr class="<%=rowColor%>">
            <td class="dataCellText">
            
            <%=cName%>&nbsp;(<%=cCode%>)
            
            </td>
          </tr>
    <%
        }
    %>        
        
        
                   </table>
          
                 </td>
              </tr>         
              

               <tr>
                 <td>

		   <table class="datatable">

		   <th class="dataTableHeader" scope="col" align="left">
			  Children:
		   </th>

        
        
    <%
        for (int i=0; i<src_subconceptList.size(); i++) {
          String s = (String) src_subconceptList.get(i);
          Vector ret_vec = DataUtils.parseData(s, "|");
          String cName = (String) ret_vec.elementAt(0);
          String cCode = (String) ret_vec.elementAt(1);
          String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
    %>
          <tr class="<%=rowColor%>">
            <td class="dataCellText">
            
               <%=cName%>&nbsp;(<%=cCode%>)
              <!--
              <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=source_scheme%><%=source_version%>&code=<%=cCode%>">
                <%=cName%>
              </a>
              -->
              
              
            </td>
          </tr>
    <%
        }
    %>        
        
        
                   </table>
          
                 </td>
              </tr>      
              
              
          
          </table>
          
          
<%
}
%>       
    
    
          
 
 <%
 if (target_code.compareTo("") != 0) {
 target_subconceptList =
    TreeUtils.getSubconceptNamesAndCodes(target_scheme, target_version, target_code);
 SortUtils.quickSort(target_subconceptList);


 %>         
          <hr></hr>
          
          
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
                  
                <tr>
		  <td align="left" class="textbody">
		      <b>Target</b>:&nbsp;<%=target_scheme%>&nbsp;(<%=target_version%>)
		  </td>
		</tr> 
                <tr>
		  <td align="left" class="textbody">
		      <b>Version</b>:&nbsp;<%=target_version%>
		  </td>
                </tr>		
                <tr>
		  <td align="left" class="textbody">
		      <b>Concept</b>:&nbsp;<%=target_concept_name%>&nbsp;(code: <%=target_code%>)
		  </td>
                </tr>

          </table>


          <table class="datatable">
               <tr>
                 <td>

		   <table class="datatable">

		   <th class="dataTableHeader" scope="col" align="left">
			  Synonyms:
		   </th>

		   <th class="dataTableHeader" scope="col" align="left">
			  &nbsp;
		   </th>
<%	
         if (target_presentations != null) {
         for (int i = 0; i < target_presentations.length; i++) {
             Presentation p = target_presentations[i];
             String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
%>             
             
                <tr class="<%=rowColor%>">
	     		  <td align="left" class="textbody">
	     		      <%=p.getPropertyName()%>
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      <%=p.getValue().getContent()%>
	     		  </td>	     		  
	     		
                </tr>
<%
         }
         }
        
%>         
                   </table>
          
                 </td>
              </tr>
              
              
               <tr>
                 <td>

		   <table class="datatable">


		   <th class="dataTableHeader" scope="col" align="left">
			  Definitions:
		   </th>

		   <th class="dataTableHeader" scope="col" align="left">
			  &nbsp;
		   </th>
<%		
         if (target_definitions != null) {
         for (int i = 0; i < target_definitions.length; i++) {
             Definition p = target_definitions[i];
             String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
%>             
             
                <tr class="<%=rowColor%>">
	     		  <td align="left" class="textbody">
	     		      <%=p.getPropertyName()%>
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      <%=p.getValue().getContent()%>
	     		  </td>	     		  
	     		
                </tr>
<%
         }
         
         if (target_definitions.length == 0) {

%>
                <tr>
	     		  <td align="left" class="textbody">
	     		      None
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      &nbsp;
	     		  </td>	     		  
	     		
                </tr>
<%                 
         }
         }
         
%>         
                   </table>
          
                 </td>
              </tr>              


               <tr>
                 <td>

		   <table class="datatable">

		   <th class="dataTableHeader" scope="col" align="left">
			  Other Properties:
		   </th>

		   <th class="dataTableHeader" scope="col" align="left">
			  &nbsp;
		   </th>
        
        
<%
         if (target_properties != null) {
         for (int i = 0; i < target_properties.length; i++) {
             Property p = target_properties[i];
             String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
%>             
             
                <tr class="<%=rowColor%>">
	     		  <td align="left" class="textbody">
	     		      <%=p.getPropertyName()%>
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      <%=p.getValue().getContent()%>
	     		  </td>	     		  
	     		
                </tr>
<%
         }
         
         
         if (target_properties.length == 0) {

%>
                <tr>
	     		  <td align="left" class="textbody">
	     		      None
	     		  </td>
	     		  <td align="left" class="textbody">
	     		      &nbsp;
	     		  </td>	     		  
	     		
                </tr>
<%                 
         }
         }
         
%>               
        
                   </table>
          
                 </td>
              </tr>   
              
              

               <tr>
                 <td>

		   <table class="datatable">


		   <th class="dataTableHeader" scope="col" align="left">
			  Parents:
		   </th>

    <%
        if (target_superconceptList != null) {
        for (int i=0; i<target_superconceptList.size(); i++) {
          String s = (String) target_superconceptList.get(i);
          Vector ret_vec = DataUtils.parseData(s, "|");
          String cName = (String) ret_vec.elementAt(0);
          String cCode = (String) ret_vec.elementAt(1);
          String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
    %>
          <tr class="<%=rowColor%>">
            <td class="dataCellText">
            
              <a href="<%= request.getContextPath() %>/pages/manual_mapping.jsf?refresh=true&target_code=<%=cCode%>">
                <%=cName%>
              </a>
              
            </td>
          </tr>
    <%
        }
        }
    %>                
        
        
                   </table>
          
                 </td>
              </tr>           
              
 
             
              
                <tr>
                  <td>
 
 		   <table class="datatable">
 
 
 		   <th class="dataTableHeader" scope="col" align="left">
 			  Children:
 		   </th>
 
     <%
         if (target_subconceptList != null) {
         for (int i=0; i<target_subconceptList.size(); i++) {
           String s = (String) target_subconceptList.get(i);
           Vector ret_vec = DataUtils.parseData(s, "|");
           String cName = (String) ret_vec.elementAt(0);
           String cCode = (String) ret_vec.elementAt(1);
           String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
     %>
           <tr class="<%=rowColor%>">
             <td class="dataCellText">
             
               <a href="<%= request.getContextPath() %>/pages/manual_mapping.jsf?refresh=true&target_code=<%=cCode%>">
                 <%=cName%>
               </a>
               
             </td>
           </tr>
     <%
         }
         }
     %>                
         
         
                    </table>
           
                  </td>
              </tr>        
              
              
              
          
          </table>


 <%
 }
 %>         


     <input type="hidden" name="type" id="type" value="<%=type%>">
     <input type="hidden" name="idx1_str" id="idx1_str" value="<%=idx1_str%>">

     <input type="hidden" name="source_scheme" id="source_scheme" value="<%=source_scheme%>">
     <input type="hidden" name="source_version" id="source_version" value="<%=source_version%>">
     <input type="hidden" name="target_scheme" id="target_scheme" value="<%=target_scheme%>">
     <input type="hidden" name="target_version" id="target_version" value="<%=target_version%>">
     
     <input type="hidden" name="source_code" id="source_code" value="<%=source_code%>">
     
     <input type="hidden" name="source_name" id="source_name" value="<%=source_concept_name%>">
     <input type="hidden" name="target_name" id="target_name" value="<%=target_concept_name%>">

     <input type="hidden" name="source_namespace" id="source_namespace" value="<%=sourceCodeNamespace%>">
     <input type="hidden" name="target_namespace" id="target_namespace" value="<%=targetCodeNamespace%>">
     
     


 </h:form>  



        </div>
      </div>
    </div>
  </f:view>
</body>
</html>