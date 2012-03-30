package gov.nih.nci.evs.browser.servlet;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import gov.nih.nci.evs.browser.bean.*;


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



    public void exportMappingToXMLAction(HttpServletRequest request, HttpServletResponse response ) {
        new MappingBean().updateMapping(request);
		String type = (String) request.getParameter("type");

        try {
        	//String xml = null;
			StringBuffer sb = null;

			response.setContentType("text/xml");
/*
			String mapping_name = (String) request.getParameter("identifier");
			String mapping_version = (String) request.getParameter("mapping_version");

System.out.println("mapping_name: " + mapping_name);
System.out.println("mapping_version: " + mapping_version);


			String key = MappingObject.computeKey(mapping_name, mapping_version);
*/
String key = (String) request.getParameter("key");
System.out.println("key: " + key);


String format = (String) request.getParameter("format");

			HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
			if (mappings == null) {
				System.out.println("exportMappingToXMLAction mappings == null ???");

				mappings = new HashMap();
				request.getSession().setAttribute("mappings", mappings);
			}

			HashMap status_hmap = (HashMap) request.getSession().getAttribute("status_hmap");

			MappingObject obj = (MappingObject) mappings.get(key);

if (obj == null) {
	System.out.println("mapping obj cannot be found for key : " + key);
} else {
	System.out.println("obj.toXML() : " + obj.toXML());
}



			String mapping_name = obj.getName();
			String mapping_version = obj.getVersion();




			//xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			String xml = "";

			if (obj != null) {
				obj.setStatusHashMap(status_hmap);

				sb = new StringBuffer(xml);
				sb = sb.append(obj.toXML());
			}

			mapping_name = mapping_name.replaceAll(" ", "_");
			mapping_name = mapping_name + ".xml";

			response.setHeader("Content-Disposition", "attachment; filename="
					+ mapping_name);

System.out.println("mapping file name : " + mapping_name);

System.out.println("mapping sb.length() : " + sb.length());

			response.setContentLength(sb.length());

			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes(), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

        //FacesContext.getCurrentInstance().responseComplete();
		//return "export";
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

