<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
  <body>
  <f:view>
    <h:form>
      Submit:
      <h:commandButton
        id="submit"
        value="submit"
        action="#{userSessionBean.changeRequest}"
        image="/images/search.gif"
        alt="submit">
      </h:commandButton>
    </h:form>
  </f:view>
  </body>
</html>
