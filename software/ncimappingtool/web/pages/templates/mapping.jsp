<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.beans.*" %>



<%@ page import="java.util.*" %>


<% String contextPath = request.getContextPath(); %>

<!--
	<table cellpadding="0" cellspacing="0" border="0" width="715">
-->

	<table>
	
	  <tr>
	    <td align="left"><font size="4"><b>WELCOME TO NCI EVS Mapping Tool</b></font></td>
	  </tr>
	</table>  
	<hr />
		      
             <%
             String warning_msg= (String) request.getAttribute("message");
             if (warning_msg != null && warning_msg.compareTo("null") != 0) {
                 request.removeAttribute("message");
             %>
                <p class="textbodyred"><%=warning_msg%></p>
             <%
             }
             %>		      


		      
	<h:form id="mapping">
		<table summary="" border="0">
			<tr>
				<td align="left"><font size="4"><b><Mapping List</b></font></td>
				<td align="right" valign="bottom">&#xA0;&#xA0;
				
				      <h:commandButton
					 value="create_mapping" action="#{mappingBean.createMappingAction}"
					 image="#{contextPath}/images/new.gif" alt="Create a new mapping" />
					&#xA0;&#xA0;
					
				      <h:commandButton
					value="clone_mapping" action="#{mappingBean.cloneMappingAction}"
					onclick="javascript:cursor_wait();"
					rendered="#{mappingBean.isNotEmpty}"
					image="#{contextPath}/images/clone.gif" alt="Clone a mapping" />

					&#xA0;&#xA0;
				      <h:commandButton
					value="delete_mapping" action="#{mappingBean.deleteMappingAction}"
					onclick="return confirm('Are you sure you want to delete?')"
					rendered="#{mappingBean.isNotEmpty}"
					image="#{contextPath}/images/delete.gif" alt="Delete a mapping" />
					
				</td>
			</tr>
		</table>
		
		<hr/>

		
<%
HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
if (mappings == null) {
    mappings = new HashMap();
}
Iterator it = mappings.keySet().iterator();
int lcv = 0;
%>		
		
		<table summary="" border="0" >
		
			<tr height="15px">
				<th class="dataTableHeader" scope="col" align="left" width="10px">&#xA0;</th>
				<th class="dataTableHeader" scope="col" align="left" width="200px">Mapping</th>
				
				<th class="dataTableHeader" scope="col" align="left" width="200px">Source</th>
				<th class="dataTableHeader" scope="col" align="left" width="200px">Target</th>
				
				<th class="dataTableHeader" scope="col" align="left" width="80px"></th>
			</tr>

<%
                        while (it.hasNext()) {
                            String key = (String) it.next();
                            MappingObject obj = (MappingObject) mappings.get(key);
                            String obj_id = obj.getKey();
                            String label = obj.getName() + " (" + obj.getVersion() + ")";
                            
                            
                            String from = obj.getFromCS();
                            if (obj.getFromVersion() != null) {
                                from = obj.getFromCS() + " (" + obj.getFromVersion() + ")";
                            }
                            String to = obj.getToCS();
                            if (obj.getToVersion() != null) {
                                to = obj.getToCS() + " (" + obj.getToVersion() + ")";
                            }
                            
                            if (obj.getType().compareTo("valueset") == 0) {
                                to = obj.getValueSetDefinitionName();
                            }
                            

				if (lcv %2 == 0) {
				%>    
				        <tr class="dataRowLight">
				<%        
				    } else {
				%>
				        <tr class="dataRowDark">
				<%        
				    }
				    lcv++;
				%>

	             <td>
                        <input type="radio" name="selected_mapping" value="<%=obj_id%>">
                     </td>
		     <td>
                        <%=label%>&nbsp;(created: <%=obj.getCreationDate()%>)
                     </td>
		     <td>
                        <%=from%>
                     </td>
		     <td>
                        <%=to%>
                     </td>
                     
	     
		     <td class="textbody10">
		        &#xA0;&#xA0;

              <a href="<%= request.getContextPath() %>/pages/batch_mapping_form.jsf?mode=readonly&id=<%=obj_id%>" >
                View
              </a>
                        &#xA0; 
              <a href="<%= request.getContextPath() %>/pages/batch_mapping_form.jsf?action=edit&id=<%=obj_id%>" >
                Edit
              </a>     
                     </td>
		</tr>
						
<%
}

if (mappings.keySet().size() == 0) {
%>


					<tr valign="top">
					        <td>&nbsp;</td>
						<td class="textbody" colspan="4">No mapping data is available.</td>
					</tr>
<%
}
%>

					
			
		</table>

		
		<input type="hidden" name="new" id="new" value="true" />
		
	</h:form>
	