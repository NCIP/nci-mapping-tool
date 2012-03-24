package gov.nih.nci.evs.browser.servlet;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class MappingServlet extends HttpServlet {

   public void init(ServletConfig config)
	           throws ServletException
   {
      super.init(config);
   }

   public void doGet(HttpServletRequest request,
                     HttpServletResponse response)
              throws IOException, ServletException
   {
        execute(request, response);
   }


   public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
              throws IOException, ServletException
   {
        execute(request, response);
   }

   public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        long ms = System.currentTimeMillis();

        String identifier = (String) request.getParameter("identifier");
        if (identifier == null) {
		    identifier = "mapping";
		}
		identifier = identifier + ".xls";

		response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename="
                    + identifier);

        PrintWriter out = response.getWriter();

      out.println("<table>");
      out.println("   <th class=\"dataTableHeader\" width=\"60px\" scope=\"col\" align=\"left\">Source</th>");
      out.println("   <th class=\"dataTableHeader\" scope=\"col\" align=\"left\">");
      out.println("	  Source Code");
      out.println("   </th>");
      out.println("   <th class=\"dataTableHeader\" scope=\"col\" align=\"left\">");
      out.println("	  Source Name");
      out.println("   </th>");
      out.println("   <th class=\"dataTableHeader\" width=\"30px\" scope=\"col\" align=\"left\">");
      out.println("	  REL");
      out.println("   </th>");
      out.println("   <th class=\"dataTableHeader\" width=\"35px\" scope=\"col\" align=\"left\">");
      out.println("	  Map Rank");
      out.println("   </th>");
      out.println("   <th class=\"dataTableHeader\" width=\"60px\" scope=\"col\" align=\"left\">Target</th>");
      out.println("   <th class=\"dataTableHeader\" scope=\"col\" align=\"left\">");
      out.println("	  Target Code");
      out.println("   </th>");
      out.println("   <th class=\"dataTableHeader\" scope=\"col\" align=\"left\">");
      out.println("	  Target Name");
      out.println("   </th>");
      out.println("</table>");


   }

}




/*
<<table>
   <th class="dataTableHeader" width="60px" scope="col" align="left">Source</th>
   <th class="dataTableHeader" scope="col" align="left">
	  Source Code
   </th>
   <th class="dataTableHeader" scope="col" align="left">
	  Source Name
   </th>
   <th class="dataTableHeader" width="30px" scope="col" align="left">
	  REL
   </th>
   <th class="dataTableHeader" width="35px" scope="col" align="left">
	  Map Rank
   </th>
   <th class="dataTableHeader" width="60px" scope="col" align="left">Target</th>
   <th class="dataTableHeader" scope="col" align="left">
	  Target Code
   </th>
   <th class="dataTableHeader" scope="col" align="left">
	  Target Name
   </th>
</table>


		   for (int lcv2=0; lcv2<selected_matches.size(); lcv2++) {
		         String rel_id = "rel" + "_" + lcv + "_" + lcv2;
		         String score_id = "score" + "_" + lcv + "_" + lcv2;
		         String idx2_str = new Integer(lcv2).toString();
				 mappingData = (MappingData) selected_matches.get(lcv2);
				 source_code = mappingData.getSourceCode();
				 source_name = mappingData.getSourceName();
				 source_namespace = mappingData.getSourceCodeNamespace();
				 rel = mappingData.getRel();
				 if (DataUtils.isNull(rel)) rel = "";
				 score = new Integer(mappingData.getScore()).toString();
				 target_code = mappingData.getTargetCode();
				 target_name = mappingData.getTargetName();
				 target_namespace = mappingData.getTargetCodeNamespace();
				 source_scheme = mappingData.getSourceCodingScheme();
				 source_version = mappingData.getSourceCodingSchemeVersion();
				 target_scheme = mappingData.getTargetCodingScheme();
				 target_version = mappingData.getTargetCodingSchemeVersion();
				 source_scheme = DataUtils.getFormalName(source_scheme);
				 target_scheme = DataUtils.getFormalName(target_scheme);
				 target_codingscheme = target_scheme;
				 target_codingschemeversion = target_version;

			 <tr>
			 	<td class="datacoldark"><%=source_namespace%></td>
			 	<td class="datacoldark"><%=source_namespace%></td>
			 	<td class="datacoldark"><%=source_namespace%></td>
			 	<td class="datacoldark"><%=source_namespace%></td>
			 	<td class="datacoldark"><%=source_namespace%></td>
			 	<td class="datacoldark"><%=source_namespace%></td>
			 </tr>
		   }


</table>
*/

