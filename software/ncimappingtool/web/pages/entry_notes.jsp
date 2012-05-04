<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>

<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.MappingData" %>


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
  <title>Vocabulary Hierarchy</title>
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
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();"><img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>


<%
HashMap mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");
if (mapping_hmap == null) {
    mapping_hmap = new HashMap();
    request.getSession().setAttribute("mapping_hmap", mapping_hmap);
} 
List list = (ArrayList) request.getSession().getAttribute("data");
String idx1_str = request.getParameter("idx1");
if (idx1_str == null) {
    idx1_str = (String) request.getSession().getAttribute("idx1");
}



int idx1 = Integer.parseInt(idx1_str);
String data_value = (String) list.get(idx1);

String idx2_str = request.getParameter("idx2");
if (idx2_str == null) {
    idx2_str = (String) request.getSession().getAttribute("idx2");
}
int idx2 = Integer.parseInt(idx2_str);

List selected_matches = null;
selected_matches = (ArrayList) mapping_hmap.get(data_value);

MappingData mappingData = (MappingData) selected_matches.get(idx2);
String comment = mappingData.getComment();
if (comment == null) comment = "";

String source_code = mappingData.getSourceCode();
String source_name = mappingData.getSourceName();
String source_namespace = mappingData.getSourceCodeNamespace();

String rel = mappingData.getRel();
if (DataUtils.isNull(rel)) rel = "";
String score = new Integer(mappingData.getScore()).toString();
String target_code = mappingData.getTargetCode();
String target_name = mappingData.getTargetName();
String target_namespace = mappingData.getTargetCodeNamespace();

String source_scheme = mappingData.getSourceCodingScheme();
String source_version = mappingData.getSourceCodingSchemeVersion();
String target_scheme = mappingData.getTargetCodingScheme();
String target_version = mappingData.getTargetCodingSchemeVersion();

source_scheme = DataUtils.getFormalName(source_scheme);        
target_scheme = DataUtils.getFormalName(target_scheme);        

String message = (String) request.getSession().getAttribute("message"); 
request.getSession().removeAttribute("message"); 

System.out.println("idx1_str: " + idx1_str);
System.out.println("idx2_str: " + idx2_str);
System.out.println("data_value: " + data_value);

System.out.println("selected_matches: " + selected_matches.size());
System.out.println("list: " + list.size());

System.out.println("source_scheme: " + source_scheme);
System.out.println("source_version: " + source_version);
System.out.println("target_scheme: " + target_scheme);
System.out.println("target_version: " + target_version);

            
%>

        <div id="popupContentArea">


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
                              <b>Concept</b>:&nbsp;<%=source_name%>&nbsp;(code: <%=source_code%>) 
                          </td>
                  
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>To</b>:&nbsp;<%=target_scheme%>&nbsp;(version: <%=target_version%>) 
                          </td>
                  </tr>
                  
                  <tr valign="top" align="left">
                          <td align="left" class="textbody" >
                              <b>Concept</b>:&nbsp;<%=target_name%>&nbsp;(code: <%=target_code%>) 
                          </td>
                  </tr>
                
         </table>       

        
          <p></p>
          <p class="dataTableHeader" >
          Add Comment:
          </p>
          
          
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
          

          
                  <h:form>        
			  <table border="0" width="700px">
		   
				<tr>
				     <td align="left" class="textbody">
				      <b>Comments</b>:
                                     </td>
				     <td>  
					 <textarea name="comment" cols="50" rows=10 tabindex="3"><%=comment%></textarea>
				     </td>
				</tr>
                          
                          
                          <tr><td>&nbsp;</td><td></td></tr>

			  <tr>
			   <td>
			    <h:commandButton id="save" value="save" action="#{mappingBean.saveCommentAction}"
			      image="#{basePath}/images/save.gif"
			      alt="Save"
			      tabindex="2">
			    </h:commandButton>
			    
&nbsp;
		    

		      <a href="javascript:refreshParent();">
			<img src="<%= request.getContextPath() %>/images/close.gif" alt="Close" border="0">
		      </a>  			    
			    
			    
			   </td>
			   &nbsp;
			  <td>
			  </td>
			  </tr>	

                          </table>
                          
                          
     <input type="hidden" name="idx1" id="idx1" value="<%=idx1_str%>" />
     <input type="hidden" name="idx2" id="idx2" value="<%=idx2_str%>" />
                         
		  </h:form>  
          
          </table>

        </div>
      </div>
    </div>
  </f:view>
</body>
</html>