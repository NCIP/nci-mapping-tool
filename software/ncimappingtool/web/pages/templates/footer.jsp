<%@ page import="gov.nih.nci.evs.utils.*" %>
<%
  String imagesPath = FormUtils.getImagesPath(request);
  String pagesPath = FormUtils.getPagesPath(request);
%>
<div class="footer">
  <ul>
    <li><a href="http://www.cancer.gov" target="_blank">NCI Home</a></li>
    <li> | <a href="<%=pagesPath%>/main/contact_us.jsf">Contact Us</a></li>
    <li> | <a href="http://www.cancer.gov/policies" target="_blank">Policies</a></li>
    <li> | <a href="http://www.cancer.gov/global/web/policies/page3" target="_blank">Accessibility</a></li>
    <li> | <a href="http://www.cancer.gov/global/web/policies/page6" target="_blank">FOIA</a></li>
  </ul>

  <p>
    A Service of the National Cancer Institute<br />
    <img src="<%=imagesPath%>/external-footer-logos.gif"
      alt="External Footer Logos" width="238" height="34" border="0"
      usemap="#external-footer" />
  </p>

  <map id="external-footer" name="external-footer">
    <area shape="rect" coords="0,0,46,34" 
      href="http://www.cancer.gov" target="_blank"
      alt="National Cancer Institute" />
    <area shape="rect" coords="55,1,99,32"
      href="http://www.hhs.gov/" target="_blank"
      alt="U.S. Health &amp; Human Services" />
    <area shape="rect" coords="103,1,147,31"
      href="http://www.nih.gov/" target="_blank"
      alt="National Institutes of Health" />
    <area shape="rect" coords="148,1,235,33"
      href="http://www.usa.gov/" target="_blank"
      alt="USA.gov" />
  </map>
</div>
