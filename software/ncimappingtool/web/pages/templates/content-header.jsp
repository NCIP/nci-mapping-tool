<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils"%>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.JSPUtils" %>
<!-- Thesaurus, banner search area -->


<% String ch_basePath = request.getContextPath(); 
    
    String mapping_tool_label = "NCI Mapping Tool";


%>

<div class="bannerarea">

        <div class="banner">
          <a href="<%=ch_basePath%>/index.jsp"><img src="<%=ch_basePath%>/images/evs_mapping_tool_logo.gif" width="383" height="117" alt="EVS Mapping Tool Logo" border="0" /></a>
        </div>
      
	<div>
	<%@ include file="menuBar.jsp" %>
	</div>
	
</div>

<!-- Quick links bar -->
<%@ include file="quickLink.jsp"%>
<!-- end Quick links bar -->
