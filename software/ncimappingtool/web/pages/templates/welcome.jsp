<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>


<% String contextPath = request.getContextPath(); %>


<%
String basePath = request.getContextPath(); 
String message = (String) request.getSession().getAttribute("message");
request.getSession().removeAttribute("message");

String option_1 = "Map from one NCI Metathesaurus source to another";
String option_2 = "Map from one terminology to another";
String option_3 = "Map from one terminology to an existing value set on the server";


%>



<div id="message" class="textbody">
  <table border="0" width="700px">
  <tr>
    <td><div class="texttitle-blue">Welcome</div></td>
  </tr>
  
  <tr><td>&nbsp;</td></tr>
  
  <tr>
    <td class="textbody">Mapping Tool Introduction ...</td>
  </tr>

<tr><td>&nbsp;</td></tr>
<tr><td class="textbody">Please select a type of mapping and press <b>Continue</b> to proceed.</td></tr>


  <tr>


<td>

<h:form> 

     <table>
     
     
            <% if (message != null) { request.getSession().removeAttribute("message"); %>
              <tr class="textbodyred"><td>
                <p class="textbodyred">&nbsp;<%=message%></p>
              </td></tr>
            <% } %>     
     
     
	<tr valign="top" align="left">
	  <td align="left" class="textbody">
	    <ul style="list-style: none;">
	    <li>
	    <input type="radio" name="type" value="ncimeta" alt="<%=option_1%>" checked><%=option_1%>&nbsp;
	    </li>
	    <li>
	    <input type="radio" name="type" value="codingscheme" alt="<%=option_2%>"><%=option_2%>&nbsp;
	    </li>
	    <li>	    
	    <input type="radio" name="type" value="valueset" alt="<%=option_3%>" ><%=option_3%>&nbsp;
	    </li>
	    </ul>
	  </td>
	</tr>

	
                  <tr><td>
                    <h:commandButton id="continue" value="continue" action="#{mappingBean.createMappingAction}"
                      image="#{basePath}/images/continue.gif"
                      alt="Resolve"
                      tabindex="2">
                    </h:commandButton>
                  </td></tr>	
	
     </table> 

</h:form>


</td>
    
  </tr>
  
  </table>
  <hr/>

</div>
