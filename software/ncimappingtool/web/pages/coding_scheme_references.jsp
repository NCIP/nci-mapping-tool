<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
L--%>

<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ComponentObject" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.beans.ValueSetBean.ValueSetObject" %>

<%@ page import="gov.nih.nci.evs.valueseteditor.properties.*" %>
<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>
<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator" %>
<%@ page import="org.LexGrid.naming.SupportedCodingScheme" %>

<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>


<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.apache.log4j.*" %>

<%@ page import="gov.nih.nci.evs.valueseteditor.utilities.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>NCI ValueSet Editor</title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
 
 <script type="text/javascript">
 function openQuickLinkSite(url) {
     if (url != "#")
     {
         window.open (url, "", "alwaysRaised,dependent,status,scrollbars,resizable,width=800,height=600"); 
     }
 }
 </script>
 
</head>
<body>

<%!
  private static Logger _logger = Utils.getJspLogger("coding_scheme_references.jsp");
%>
   
<f:view>
    <%@ include file="/pages/include/header.jsp" %>
    <div class="center-page">
        <%@ include file="/pages/include/subHeader.jsp" %>

        <div class="mainbox-top"><img src="<%= request.getContextPath() %>/images/mainbox-top.gif" width="745" height="5" alt="Mainbox Top" /></div>
	<div id="main-area">
	          <%@ include file="/pages/include/applicationBanner.jsp" %>
	          <%@ include file="/pages/include/quickLinks.jsp" %>
		  <div class="pagecontent">
		      <%@ include file="/pages/include/navBar.jsp" %>
		      

        
<%

String requestContextPath = request.getContextPath();

System.out.println("requestContextPath: " + requestContextPath);

String message = (String) request.getSession().getAttribute("message");  
request.getSession().removeAttribute("message");  


       	ValueSetBean vsb = (ValueSetBean)FacesContext.getCurrentInstance()
			     .getExternalContext().getSessionMap().get("ValueSetBean");
	
	String vsd_description = "";
	String isActive = "true";
	String organizations = "";
	
	String check_true = "";
	String check_false = "";
	
	String vsd_uri = null;
	
	if (vsb == null) {
	    System.out.println("ValueSetBean == null???");
	} else {
	    System.out.println("ValueSetBean != null");
	}
	
	
vsd_uri = vsb.getUri();
System.out.println("vsd_uri " + vsd_uri);

if (vsd_uri == null) {
    vsd_uri = (String) request.getAttribute("uri");
}

System.out.println("vsd_uri " + vsd_uri);

ValueSetObject vs_obj = vsb.getValueSet(vsd_uri);
Vector codingSchemeName_vec = vsb.findParticipatingCodingSchemes(vs_obj);

String codingSchemeNames = "";
if (codingSchemeName_vec != null) {
    for (int k=0; k<codingSchemeName_vec.size(); k++) {
        String nm = (String) codingSchemeName_vec.elementAt(k);
        if (k > 0) {
            codingSchemeNames = codingSchemeNames + "|";
        } 
        codingSchemeNames = codingSchemeNames + nm;
    }
}

Vector coding_scheme_ref_vec = DataUtils.getCodingSchemeReferencesInValueSetDefinition(codingSchemeName_vec);


System.out.println("coding_scheme_references.jsp coding_scheme_ref_vec.size() " + coding_scheme_ref_vec.size()) ;


String checked = "";
String prev_cs_urn = "";

%>
         
          
          <table>
            <tr>
            <td class="texttitle-blue">Resolve Value Set:&nbsp;<%=vsd_uri%></td>
            </tr>

            <% if (message != null)  { 
                request.getSession().removeAttribute("message");
            %>
            
        <tr class="textbodyred">
        <td>
            <p class="textbodyred">&nbsp;<%=message%></p>
        </td></tr>
            <% } %>


            <tr>
            <td>&nbsp;</td>
            </tr>
            

            <tr class="textbody"><td>

 <h:form id="resolveValueSetForm" styleClass="search-form">            
              
              <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
                <th class="dataTableHeader" scope="col" align="left">&nbsp;</th>
                <th class="dataTableHeader" scope="col" align="left">Coding Scheme</th>
                <th class="dataTableHeader" scope="col" align="left">Version</th>
                <th class="dataTableHeader" scope="col" align="left">Tag</th>
<%
if (coding_scheme_ref_vec != null) {
int k = -1;

            for (int i=0; i<coding_scheme_ref_vec.size(); i++) {
            
		    String coding_scheme_ref_str = (String) coding_scheme_ref_vec.elementAt(i);
int lcv = i+1;		    
System.out.println("(" + lcv + ")" + coding_scheme_ref_str);		    
		    
		    
		    String coding_scheme_name_version = coding_scheme_ref_str;
		    
		    Vector u = DataUtils.parseData(coding_scheme_ref_str);
		    String cs_name = (String) u.elementAt(0);
		    String displayed_cs_name = DataUtils.getCodingSchemeName(cs_name, null);//uri2CodingSchemeName(cs_name);
		    
		    //cs_name = DataUtils.uri2CodingSchemeName(cs_name); 
		    
		    
		    String cs_version = (String) u.elementAt(1);
		    
System.out.println("coding_scheme_references.jsp cs_name: " + cs_name);

System.out.println("coding_scheme_references.jsp cs_version: " + cs_version);
		    
		    String cs_tag = DataUtils.getVocabularyVersionTag(cs_name, cs_version);
		    if (cs_tag == null) cs_tag = "";
		    
		    if (cs_name.compareTo(prev_cs_urn) != 0) {
		       k++;
		       prev_cs_urn = cs_name;
		    }
		    
		    if (coding_scheme_ref_vec.size() == 1) {
		        checked = "checked";
		    } else if (cs_tag.compareToIgnoreCase("PRODUCTION") == 0) {
		        checked = "checked";
		    }
	    
        
		    if (k % 2 == 0) {
		    %>
		      <tr class="dataRowDark">
		    <%
			} else {
		    %>
		      <tr class="dataRowLight">
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
} else {
%>
<tr><td>
<p class="textbodyred">&nbsp;WARNING: Unable to retrieve coding scheme reference data from the server.</p>
</td></tr>
<%
}
             %>                 




              </table>

            <tr>
            <td>&nbsp;</td>
            </tr>
            
                  <tr>
                  <td>
                    <h:commandButton id="continue_resolve" value="continue_resolve" action="#{ValueSetBean.continueResolveValueSetAction}"
                      onclick="javascript:cursor_wait();"
                      image="#{requestContextPath}/images/continue.gif"
                      alt="Resolve"
                      tabindex="2">
                    </h:commandButton>

&nbsp; 


<h:commandButton
	id="Cancel"
	value="Cancel"
	action="#{ValueSetBean.cancelResolveValueSetAction}" 
	image="#{form_requestContextPath}/images/cancel.gif" alt="Cancel Resolve Value Set">
</h:commandButton>

                     
                  </td>
                  </tr>

              </td></tr>  
           </table>                
                  
              <input type="hidden" name="uri" id="uri" value="<%=vsd_uri%>"> 
              
              
              <input type="hidden" name="codingSchemeNames" id="codingSchemeNames" value="<%=codingSchemeNames%>">
              <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>">
</h:form>
           

           
               
		      <%@ include file="/pages/include/footer.jsp" %>
		  </div>
	</div>
        <div class="mainbox-bottom"><img src="<%= request.getContextPath() %>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    </div>
   <br/> 
</f:view>    
</body>
</html>