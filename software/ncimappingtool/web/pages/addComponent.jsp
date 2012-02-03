<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>

<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>

<%@ page import="java.util.ResourceBundle"%>

<%@ page import="org.apache.log4j.*" %>
<%@ page import="javax.faces.model.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>NCI EVS Mapping Tool</title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>

  <script type="text/javascript"> 
      window.history.forward(1);
      function noBack() {
         window.history.forward(1);
      }
  </script> 

  <script type="text/javascript">
    function newPopup(url) {    	  
    	var centerWidth = (window.screen.width - 500) / 2;
        var centerHeight = (window.screen.height - 400) / 2;
        window.open(url,'_blank',
        	   'top=' + centerHeight +
        	   ',left=' + centerWidth +
        	   ', height=200, width=300, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');
    }
    
    
     function refresh() {

      var subsetType = "Property";
      var subsetTypeObj = document.forms["addComponentForm"].subsetType;
      for (var i=0; i<subsetTypeObj.length; i++) {
        if (subsetTypeObj[i].checked) {
          subsetType = subsetTypeObj[i].value;
        }
      }

      var type = "";
      if (document.forms["addComponentForm"].type != null) {
          type = document.forms["addComponentForm"].type.value;
      }  
      
      var dictionary = "";
      if (document.forms["addComponentForm"].dictionary != null) {
          dictionary = document.forms["addComponentForm"].dictionary.value;
      }   
 
      var version = "";
      if (document.forms["addComponentForm"].version != null) {
           version = document.forms["addComponentForm"].version.value;
      }  
      
      var algorithm = "exactMatch";
      if (subsetType == "Relationship" && document.forms["addComponentForm"].search_algorithm != null) {
           algorithm = document.forms["addComponentForm"].search_algorithm.value;
      }      
     
      var rel_search_association = "";
      if (subsetType == "Relationship" && document.forms["addComponentForm"].rel_search_association != null) {
          rel_search_association = document.forms["addComponentForm"].rel_search_association.value;
      }
      
      var selectProperty = "";
      if (subsetType == "Property" && document.forms["addComponentForm"].selectProperty != null) {
           selectProperty = document.forms["addComponentForm"].selectProperty.value;
      }

      var selectValueSetReference = "";
      if (subsetType == "ValueSetReference" && document.forms["addComponentForm"].selectValueSetReference != null) {
           selectValueSetReference = document.forms["addComponentForm"].selectValueSetReference.value;
      }
      
      var label = "";
      if (document.forms["addComponentForm"].Label != null) {
           label = document.forms["addComponentForm"].Label.value;
      }

      var description = "";
      if (document.forms["addComponentForm"].Description != null) {
           description = document.forms["addComponentForm"].Description.value;
      }      
 
      var text = "";
      if (document.forms["addComponentForm"].matchText != null) {
            text = document.forms["addComponentForm"].matchText.value;
      }

      var action = "";
      if (document.forms["addComponentForm"].action != null) {
            action = document.forms["addComponentForm"].action.value;
      }
      
      var dir = "Forward";
      var radioObj = document.forms["addComponentForm"].direction;
      if (radioObj != null) {
          if (radioObj[1].checked) dir = "Backward";
      }
       
      window.location.href="/ncimappingtool/pages/addComponent.jsf?refresh=1"
          + "&type="+ type
          + "&opt="+ subsetType
          + "&action="+ action
          + "&label="+ label
          + "&description="+ description
          + "&text="+ text
          + "&algorithm="+ algorithm
          + "&ref_uri="+ selectValueSetReference
          + "&prop="+ selectProperty
          + "&rel="+ rel_search_association
          + "&dir="+ dir
          + "&version="+ version
          + "&dictionary="+ dictionary;
    }
   
  </script>
</head>


<body> 


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





<%
    String basePath = request.getContextPath(); 


    String action = (String) request.getParameter("action"); //through restriction link, or refresh
    if (action == null) {
    	action = (String) request.getSession().getAttribute("action"); //import button
    }
   
    String type = (String) request.getParameter("type"); //through restriction link, or refresh
    if (type == null) {
    	type = (String) request.getSession().getAttribute("type"); 
    }
    
    System.out.println("(***) AddComponent.jsp type: " + type);
    
    
    String adv_search_vocabulary = request.getParameter("dictionary");
    String adv_search_version = request.getParameter("version");
    if (action.compareTo("import") == 0) {
	 adv_search_vocabulary = (String) request.getSession().getAttribute("dictionary");
	 adv_search_version = (String) request.getSession().getAttribute("version");
    } else {
	 adv_search_vocabulary = (String) request.getParameter("dictionary");
	 adv_search_version = (String) request.getParameter("version");
	 request.getSession().setAttribute("dictionary", adv_search_vocabulary);
	 request.getSession().setAttribute("version", adv_search_version);
    }
    if (adv_search_vocabulary == null) {
	 adv_search_vocabulary = (String) request.getSession().getAttribute("dictionary");
	 adv_search_version = (String) request.getSession().getAttribute("version");
    }
   
    
    String refresh = (String) request.getParameter("refresh");
    boolean refresh_page = false;
    if (refresh != null && refresh.compareTo("null") != 0) {
        refresh_page = true;
    }
    
    String form_requestContextPath = request.getContextPath();
    String advSearch_requestContextPath = request.getContextPath();
    advSearch_requestContextPath = advSearch_requestContextPath.replace("//ncitbrowser//ncitbrowser", "//ncitbrowser");
    
    String search_algorithm = null;
    String search_string = "";
    String selectValueSetReference = null;
    String selectProperty = null;
    String rel_search_association = null;
    String adv_search_source = null;
    String adv_search_type = null;
    String focusConceptCode = "";

    String t = null;
    
    String label = null;
    String description = null;
    String subsetType = null;
    String direction = null;
        
    String preview = (String) request.getSession().getAttribute("preview");
    
   
 String include_focus_node_checkbox = null;   
 String transitivity_checkbox = null; 
 
    if (preview != null && preview.compareTo("true") == 0) {
    
        adv_search_vocabulary = (String) request.getSession().getAttribute("preview_adv_search_vocabulary");
        subsetType = (String) request.getSession().getAttribute("preview_subsetType");
        label = (String) request.getSession().getAttribute("preview_label");
        
        action = (String) request.getSession().getAttribute("preview_action");
        description = (String) request.getSession().getAttribute("preview_description");
        search_string = (String) request.getSession().getAttribute("preview_search_string");
        search_algorithm = (String) request.getSession().getAttribute("preview_search_algorithm");
        adv_search_source = (String) request.getSession().getAttribute("preview_adv_search_source");
        rel_search_association = (String) request.getSession().getAttribute("preview_rel_search_association");
        selectProperty = (String) request.getSession().getAttribute("preview_selectProperty");
        selectValueSetReference = (String) request.getSession().getAttribute("preview_selectValueSetReference");
        direction = (String) request.getSession().getAttribute("preview_direction");

include_focus_node_checkbox = (String) request.getSession().getAttribute("preview_include_focus_node_checkbox");
transitivity_checkbox = (String) request.getSession().getAttribute("preview_transitivity_checkbox");
      
        focusConceptCode = (String) request.getSession().getAttribute("preview_focusConceptCode");
        if (focusConceptCode == null || focusConceptCode.compareTo("null") == 0) {
            focusConceptCode = "";
        }
       
    } 
    
	    if (refresh_page) {
	    
	    
System.out.println("********************** REFRESH PAGE");

                type = (String) request.getParameter("type");
                action = (String) request.getParameter("action");
		adv_search_vocabulary = (String) request.getParameter("dictionary");
		subsetType = (String) request.getParameter("opt");
		
		
		label = (String) request.getParameter("label");
		description = (String) request.getParameter("description");
		search_string = (String) request.getParameter("text");
		search_algorithm = (String) request.getParameter("algorithm");
		adv_search_source = (String) request.getParameter("sab");
		rel_search_association = (String) request.getParameter("rel");
		selectProperty = (String) request.getParameter("prop");
		selectValueSetReference = (String) request.getParameter("ref_uri");





	    } 

System.out.println("------------ addComponent.jsp type: " + type);



	    if (subsetType == null || subsetType.compareTo("null") == 0) {
		subsetType = "Property";
	    }



System.out.println("------------ addComponent.jsp subsetType: " + subsetType);



     String warning_msg= (String) request.getSession().getAttribute("message");
     if (warning_msg != null && warning_msg.compareTo("null") != 0) {
	 request.getSession().removeAttribute("message");
	 
	 
     %>
	<p class="textbodyred"><%=warning_msg%></p>
     <%
     }

    
    // to be modified:
    String code_enumeration = "";


    adv_search_type = subsetType;

    if (selectProperty == null) selectProperty = "";
    if (search_string == null) search_string = "";
    if (search_algorithm == null) search_algorithm = "exactMatch";

    String check__e = "", check__b = "", check__s = "" , check__c ="";
    if (search_algorithm == null || search_algorithm.compareTo("exactMatch") == 0)
        check__e = "checked";
    else if (search_algorithm.compareTo("startsWith") == 0)
        check__s= "checked";
    else
        check__c = "checked";

    String check_n2 = "", check_c2 = "", check_p2 = "" , check_c3 = "" , check_cs = "" , check_r2 ="",  check_vs ="";

    if (subsetType == null || subsetType.compareTo("Name") == 0)
      check_n2 = "checked";
    else if (subsetType.compareTo("Code") == 0)
        check_c2 = "checked";
    else if (subsetType.compareTo("Property") == 0)
      check_p2 = "checked";
    else if (subsetType.compareTo("Relationship") == 0)
      check_r2 = "checked";
    else if (subsetType.compareTo("EnumerationOfCodes") == 0)
      check_c3 = "checked";   
    else if (subsetType.compareTo("EntireVocabulary") == 0)
      check_cs = "checked";    
    else if (subsetType.compareTo("ValueSetReference") == 0)
      check_vs = "checked"; 
      
    else check_n2 = "checked";

    String editAction = (String) request.getSession().getAttribute("editAction");
    
    if (label == null) label = "";
    if (description == null) description = "";
 
if (adv_search_vocabulary == null) {
    adv_search_vocabulary = (String) request.getSession().getAttribute("vocabulary");
}


%>

 <font size="3">
 
 <%
 if (action.compareTo("import") == 0) {
 %>
 <b>Import Concepts from Mapping Source</b></font><br/>
 <%
 } else {
 %>
 <b>Apply Restriction on Mapping Target</b></font><br/>
 <%
 }
 %>
  
 <h:form id="addComponentForm" styleClass="pagecontent">            
               
      <table border="0" width="80%">

                 <tr>
		                          <td align="right" class="textbody">
		                                  Vocabulary:&nbsp;
		                          </td>
		    			  <td class="textbody"> 
		    			        <%=adv_search_vocabulary%>&nbsp;(<%=adv_search_version%>)
		    			  </td>
                 </tr>
                    
                 <tr>
                     <td align="right" class="textbody">
                         <b>Type</b>:
                     </td>
 
                     <td class="textbody">
                     
 			<input type="radio" id="subsetType" name="subsetType" value="Property" alt="Property" <%=check_p2%> onclick="javascript:refresh()" tabindex="5">Property&nbsp;
 			<input type="radio" id="subsetType" name="subsetType" value="Relationship" alt="Relationship" <%=check_r2%> onclick="javascript:refresh()" tabindex="5">Relationship&nbsp;
 			<input type="radio" id="subsetType" name="subsetType" value="ValueSetReference" alt="Value Set" <%=check_vs%> onclick="javascript:refresh()" tabindex="5">Value Set&nbsp;
 			
 <%
 if (action.compareTo("import") != 0) {
 %> 			
 			<input type="radio" id="subsetType" name="subsetType" value="EnumerationOfCodes" alt="Enumeration of Codes" <%=check_c3%> onclick="javascript:refresh()" tabindex="5">Enumeration of Codes&nbsp;
 <%
 }
 %>
 
 
                     </td>
                </tr> 
                
                
 <%


     String match_text_label = "Match Text";
     if (subsetType.compareTo("Code") == 0) {
         match_text_label = "Code";
     }


System.out.println("subsetType: " + subsetType);


     if (subsetType.equals("ValueSetReference")) {
     
%>

     <tr>
 
 	 <td align="left" class="textbody">
 	     <b>Value Set: &nbsp;</b>:
 	 </td>                   
 
 	 <td class="inputItem">
 	 <select id="vsdURI" name="vsdURI" size="1" tabindex="6">
 
 	   <%
	   
 	     Vector item_vec = DataUtils.getValueSetDefinitions();
 	     String vsdURI = (String) request.getAttribute("vsdURI");

if (item_vec == null) {
System.out.println("item_vec == null???");
} else if (item_vec.size() == 0) {
System.out.println("item_vec.size() == 0???");
}
 	     
 	     if (vsdURI == null && item_vec.size() > 0) {
 		      SelectItem item = (SelectItem) item_vec.elementAt(0);
 		      vsdURI = (String) item.getLabel();
 	     }
	     
 	     if (item_vec != null) {
 		    for (int i=0; i<item_vec.size(); i++) {
 		      SelectItem item = (SelectItem) item_vec.elementAt(i);
 		      String key = (String) item.getLabel();
 		      String value = (String) item.getValue();
 		      if (value != null && value.compareTo(vsdURI) == 0) {
 		  %>
 			<option value="<%=value%>" selected><%=key%></option>
 		  <%  } else { %>
 			<option value="<%=value%>"><%=key%></option>
 		  <%
 		      }
 		    }
 	     }
 	   %>
 	   </select>
 
 	 </td>
      </tr>    
      
      
 <%    


     } else if (subsetType.equals("Property")) {
     
     
    
	    MappingUtils util = new MappingUtils();
	    Vector algorithms = new Vector();
	    try {
		algorithms = util.getSupportedSearchTechniqueNames();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
  
     
     
     
     
     
     %>
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=rel_search_association%>">

                    
                    <tr>
		                              <td align="right" class="textbody">
		                                  <b><%=match_text_label%></b>:
		                              </td>
		    			  <td> 
		    			     <input name="matchText" value="<%=search_string%>" tabindex="3">
		    			  </td>
                    </tr>
     
     
                    <tr>
                    
                        <td align="right" class="textbody">
                            <b>Property</b>:
                        </td>                   
                    
                     
                        <td class="inputItem">
                        
                          <select id="selectProperty" name="selectProperty" size="1" tabindex="6">
                          <%
                            t = "";
                            if (t.compareTo(selectProperty) == 0) {
                          %>
                              <option value="<%=t%>" selected><%=t%></option>
                          <%} else {%>
                              <option value="<%=t%>"><%=t%></option>
                          <%}%>

                          <%
                 
                          
                            Vector property_vec = OntologyBean.getSupportedPropertyNames(adv_search_vocabulary, adv_search_version);
                            if (property_vec != null) {
				    for (int i=0; i<property_vec.size(); i++) {
				      t = (String) property_vec.elementAt(i);
				      if (t.compareTo(selectProperty) == 0) {
				  %>
					<option value="<%=t%>" selected><%=t%></option>
				  <%  } else { %>
					<option value="<%=t%>"><%=t%></option>
				  <%
				      }
				    }
                            }
                          %>
                          </select>
                        
                        </td>
                    </tr>


		<tr>
		    <td align="right" class="textbody">
		      <b>Algorithm</b>:
                        </td>
                
			<td>		      

			    <select id="search_algorithm" name="search_algorithm" size="1" tabindex="4">
			    <%
			       if (algorithms != null) {
				    for (int i=0; i<algorithms.size(); i++) {
					 String t0 = (String) algorithms.elementAt(i);
					 if (t0.compareTo(search_algorithm) == 0) {
				    %>
					   <option value="<%=t0%>" selected><%=t0%></option>
				    <%
					 } else {
				    %>
					   <option value="<%=t0%>"><%=t0%></option>
				    <%
					 }
				    }
			       }
			    %>
			    </select>
		        </td>
		 </tr>		    
				    
				    
				    

                  <% 
                  } else if (subsetType.equals("Relationship")) { 
                 %>
                  <input type="hidden" name="selectProperty" id="selectProperty" value="<%=selectProperty%>">
                  
                  
      <tr>               
                          <td align="right" class="textbody">
                              <b>Focus concept code</b>:
                          </td>
			  <td>                   
			     <input CLASS="textbody" name="focusConceptCode" value="<%=focusConceptCode%>" tabindex="3">
			     

                       &nbsp; 
				 
		      <a href="<%=request.getContextPath()%>/pages/search.jsf?action=<%=action%>">
			<img src="<%= request.getContextPath() %>/images/search.png" width="15" height="15" alt="Search" border="0">
		      </a>   			     
			     
			  </td>
     </tr>                 
                  
                  
                    <tr>

                        <td align="right" class="textbody">
                            <b>Association</b>:
                        </td> 
                        
                        <td class="inputItem">
                           <select id="rel_search_association" name="rel_search_association" size="1">
<%
String blank_str = "";
%>

<option value="<%=blank_str%>"><%=blank_str%></option>


                          <%

System.out.println("addComponent.jsp OntologyBean.getSupportedAssociationNames  adv_search_vocabulary: " + adv_search_vocabulary);
System.out.println("addComponent.jsp OntologyBean.getSupportedAssociationNames  version: " + adv_search_version);

                          
                            Vector association_vec = OntologyBean.getSupportedAssociationNames(adv_search_vocabulary, adv_search_version);
                            if (association_vec != null) { 
				    for (int i=0; i<association_vec.size(); i++) {
				      t = (String) association_vec.elementAt(i);
				      if (t.compareTo(rel_search_association) == 0) {
				  %>
					<option value="<%=t%>" selected><%=t%></option>
				  <%  } else { %>
					<option value="<%=t%>"><%=t%></option>
				  <%
				      }
				    }
                            }
                            
                          %>
                          </select>

                      </td>
                      
                   </tr>
 
                   <tr>
                   
                        <td align="right" class="textbody">
                            <b>Direction</b>:
                        </td> 
                                     

                    <td align="left" class="textbody">
                    <%
                          if (direction != null && direction.compareTo("Backward") == 0) {
		    %>
		                  <input type="radio" id="direction" name="direction" value="Forward" alt="Forward" tabindex="5">Forward&nbsp;
				  <input type="radio" id="direction" name="direction" value="Backward" alt="Backward" checked tabindex="6">Backward
		<%
		} else {
		%>	  
				  <input type="radio" id="direction" name="direction" value="Forward" alt="Forward" checked tabindex="5">Forward&nbsp;
				  <input type="radio" id="direction" name="direction" value="Backward" alt="Backward"  tabindex="6">Backward
		<%
		}
                %> 
                    </td>
                </tr> 
                

                         </td> 
                     </tr>         
 
 
                     <tr>
                    
                         <td align="right" class="textbody">
                             <b>Include focus concept</b>?
                         </td> 
 
                          <td>
<%
if (include_focus_node_checkbox != null && include_focus_node_checkbox.compareTo("true") == 0) {
%>
    <input type="checkbox" name="include_focus_node_checkbox" value="true" checked />
<%
} else {
%>
    <input type="checkbox" name="include_focus_node_checkbox" value="true" />
<%
}
%>
                         </td> 
                     </tr>    

                     <tr>
                    
                         <td align="right" class="textbody">
                             <b>Transitive closure</b>?
                         </td> 
 
                          <td>
                          
 <%
if (transitivity_checkbox != null && transitivity_checkbox.compareTo("true") == 0) {
%>
    <input type="checkbox" name="transitivity_checkbox" value="true" checked="yes" />
<%
} else {
%>
    <input type="checkbox" name="transitivity_checkbox" value="true" />
<%
}
%>                         
                          
                          </td> 
                     </tr>  
 
 
 

     
     
     
           
                     
                  <% } else { %>
                  
                  
                  
                  
                    <input type="hidden" name="selectProperty" id="selectProperty" value="<%=selectProperty%>">
                    <input type="hidden" name="rel_search_association" id="rel_search_association" value="<%=rel_search_association%>">
                    
 
                  <% }%>
                  
                  <% 
		  if (subsetType != null && subsetType.compareTo("EnumerationOfCodes") == 0) {
		  %>
		      <tr>               
					   <td align="right" class="textbody">
					       <b>Codes</b>:
					   </td>
		      <td>  
			  <textarea name="codes" cols="50" rows=10 tabindex="3" class="inputText" ></textarea>
		      </td>
		     </tr>
	     
	          <%}%>
	     
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>

<tr>

<td align="right" >&nbsp;</td>

<td>


<h:commandButton
	id="Save"
	value="Save"
	action="#{mappingBean.saveComponentSubsetAction}" 
	image="#{form_requestContextPath}/images/continue.gif" alt="Continue">
</h:commandButton>
&nbsp;

<!--
		      <a href="#" onclick="javascript:history.go(-1)">
			<img src="<%= request.getContextPath() %>/images/cancel.gif" alt="Cancel" border="0">
		      </a>   
-->		      
		      
<h:commandButton
	id="Cancel"
	value="Cancel"
	action="#{mappingBean.cancelComponentSubsetAction}" 
	onclick="javascript:history.go(-1)"
	image="#{form_requestContextPath}/images/cancel.gif" alt="Cancel">
</h:commandButton>


</td>


</tr>


              </table>
              
              <input type="hidden" name="referer" id="referer" value="HTTPUtils.getRefererParmEncode(request)" />
              
              <input type="hidden" name="dictionary" id="dictionary" value="<%=adv_search_vocabulary%>" />
              <input type="hidden" name="version" id="version" value="<%=adv_search_version%>" />

              <input type="hidden" name="adv_search_type" id="adv_search_type" value="<%=adv_search_type%>" />
            
              <input type="hidden" name="type" id="type" value="<%=type%>" />
              <input type="hidden" name="action" id="action" value="<%=action%>" />

              
         
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

