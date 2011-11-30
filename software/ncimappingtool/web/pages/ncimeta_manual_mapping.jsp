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
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();"><img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>


<%
HashMap mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");
if (mapping_hmap == null) {
    mapping_hmap = new HashMap();
    request.getSession().setAttribute("mapping_hmap", mapping_hmap);
} 
String type = (String) request.getSession().getAttribute("type");
List list = (ArrayList) request.getSession().getAttribute("data");
String idx1_str = request.getParameter("idx1");
int idx1 = Integer.parseInt(idx1_str);
String data_value = (String) list.get(idx1);
String source_code = null;
String source_name = null;
String input_option = input_option = (String) request.getSession().getAttribute("input_option");

if (input_option.compareTo("Name") == 0) {
    source_name = data_value;
} else {
    source_code = data_value;
}


String source_abbrev = (String) request.getSession().getAttribute("source_abbrev");
String target_abbrev = (String) request.getSession().getAttribute("target_abbrev");

String source_scheme = DataUtils.getFormalName(source_abbrev);
String source_version = DataUtils.getVocabularyVersionByTag(source_scheme, null);

String target_scheme = DataUtils.getFormalName(target_abbrev);
String target_version = DataUtils.getVocabularyVersionByTag(target_scheme, null);


String message = (String) request.getSession().getAttribute("message"); 

Entity src_concept = MappingUtils.getConceptByCode(source_scheme, source_version, null, source_code);
String source_concept_name = src_concept.getEntityDescription().getContent();
Entity target_concept = null;
String target_concept_name = null;
Presentation[] target_presentations = null;
Definition[] target_definitions = null;
Property[] target_properties = null;
ArrayList target_superconceptList = null;
ArrayList src_superconceptList = new ArrayList();
ArrayList src_subconceptList = new ArrayList();

HashMap hmap_super = TreeUtils.getSuperconcepts(source_scheme, source_version, source_code);
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


/*
	subconceptList =
		TreeUtils.getSubconceptNamesAndCodes(scheme, version, code);
	//KLO
	//Collections.sort(subconceptList);
	SortUtils.quickSort(subconceptList);
				
*/

String target_code = (String) request.getSession().getAttribute("target_code");
if (DataUtils.isNull(target_code)) {
    target_code = "";
} else {
    target_concept = MappingUtils.getConceptByCode(target_scheme, target_version, null, target_code);
    target_concept_name = target_concept.getEntityDescription().getContent();
    
    target_presentations = target_concept.getPresentation();
    target_definitions = target_concept.getDefinition();
    target_properties = target_concept.getProperty();

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


Presentation[] src_presentations = null;
src_presentations = src_concept.getPresentation();

Definition[] src_definitions = null;
src_definitions = src_concept.getDefinition();
Property[] src_properties = src_concept.getProperty();
				


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
                              <b>Data</b>:&nbsp;<%=data_value%> 
                          </td>
                  </tr>
                  
                  
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>From</b>:&nbsp;<%=source_scheme%>&nbsp;(version: <%=source_version%>)  
                          </td>
                  </tr>
                 
                 
                 
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>Concept</b>:&nbsp;<%=source_concept_name%>&nbsp;(code: <%=source_code%>) 
                          </td>
                  
                  
                  <tr><td>&nbsp;</td></tr>
                  
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>To</b>:&nbsp;<%=target_scheme%>&nbsp;(version: <%=target_version%>) 
                          </td>
                  </tr>
                  
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>Target concept code</b>:&nbsp;<input name="target_code" id="target_code" value="<%=target_code%>" />
                          </td>
                  </tr>


<tr><td>&nbsp;</td></tr>
<tr><td>

		    <h:commandButton id="refresh" value="refresh" action="#{mappingBean.manualMappingAction}"
		      image="#{basePath}/images/refresh.png"
		      alt="Refresh"
		      tabindex="2">
		    </h:commandButton>

&nbsp;


		    <h:commandButton id="save" value="save" action="#{mappingBean.saveManualMappingAction}"
		      image="#{basePath}/images/save.gif"
		      alt="Refresh"
		      tabindex="2">
		    </h:commandButton>
</td></tr>
                  
         </table>       



          <table width="580px" cellpadding="3" cellspacing="0" border="0">
                  
                <tr>
		  <td align="left" class="textbody">
		      <b>Source</b>:&nbsp;<%=source_abbrev%>&nbsp;(<%=source_scheme%>)
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
              <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=source_scheme%><%=source_version%>&code=<%=cCode%>">
                <%=cName%>
              </a>
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
 if (target_code.compareTo("") != 0) {
 %>         
          <hr></hr>
          
          
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
                  
                <tr>
		  <td align="left" class="textbody">
		      <b>Target</b>:&nbsp;<%=target_abbrev%>&nbsp;(<%=target_scheme%>)
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
              <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=target_scheme%><%=target_version%>&code=<%=cCode%>">
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


     <input type="hidden" name="type" id="type" value="ncimeta">


 </h:form>  



        </div>
      </div>
    </div>
  </f:view>
</body>
</html>