<%@ page import="gov.nih.nci.evs.utils.*" %>
<%
  String imagesPath = FormUtils.getImagesPath(request);
%>
<div class="ncibanner">
  <a href="http://www.cancer.gov" target="_blank">
    <img src="<%=imagesPath%>/logotype.gif"
      width="440" height="39" border="0"
      alt="National Cancer Institute" />
  </a>
  <a href="http://www.cancer.gov" target="_blank">
    <img src="<%=imagesPath%>/spacer.gif"
      width="48" height="39" border="0" 
      alt="National Cancer Institute" class="print-header" /></a>
  <a href="http://www.nih.gov" target="_blank">
    <img src="<%=imagesPath%>/tagline_nologo.gif"
      width="173" height="39" border="0"
      alt="U.S. National Institutes of Health" />
  </a>
  <a href="http://www.cancer.gov" target="_blank">
    <img src="<%=imagesPath%>/cancer-gov.gif"
      width="99" height="39" border="0"
      alt="www.cancer.gov" />
  </a>
</div>
