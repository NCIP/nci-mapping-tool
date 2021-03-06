<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
L--%>

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
  <title>Concept Details</title>
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
String source_code = request.getParameter("src_cd");
String target_code = request.getParameter("target_cd");


String identifier = (String) request.getSession().getAttribute("identifier");
if (identifier != null && identifier.compareTo("null") == 0) {
    identifier = "";
} else if (identifier == null) {
    identifier = "";
}

String source_abbrev = (String) request.getSession().getAttribute("source_abbrev");
String target_abbrev = (String) request.getSession().getAttribute("target_abbrev");

String source_scheme = DataUtils.getFormalName(source_abbrev);
String source_version = DataUtils.getVocabularyVersionByTag(source_scheme, null);

String target_scheme = DataUtils.getFormalName(target_abbrev);
String target_version = DataUtils.getVocabularyVersionByTag(target_scheme, null);

Entity src_concept    = MappingUtils.getConceptByCode(source_scheme, source_version, null, source_code);
Entity target_concept = MappingUtils.getConceptByCode(target_scheme, target_version, null, target_code);

String source_concept_name = src_concept.getEntityDescription().getContent();
String target_concept_name = target_concept.getEntityDescription().getContent();


Presentation[] src_presentations = null;
src_presentations = src_concept.getPresentation();

Definition[] src_definitions = null;
src_definitions = src_concept.getDefinition();


Presentation[] target_presentations = null;
target_presentations = target_concept.getPresentation();

Definition[] target_definitions = null;
target_definitions = target_concept.getDefinition();


Property[] src_properties = src_concept.getProperty();
Property[] target_properties = target_concept.getProperty();


ArrayList src_superconceptList = new ArrayList();
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
				
				
ArrayList target_superconceptList = new ArrayList();
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
  
%>

        <div id="popupContentArea">
        
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
    %>                
        
        
                   </table>
          
                 </td>
              </tr>           
              
              
          
          </table>

        </div>
      </div>
    </div>
  </f:view>
</body>
</html>