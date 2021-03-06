/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

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
import gov.nih.nci.evs.browser.utils.*;



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


//	   var url = "/ncimappingtool/mapping?action=export&key=" + key_str + "&format=" + export_format + "&entries=" + entries;

   public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

		String action = HTTPUtils.cleanXSS((String) request.getParameter("action"));
		String format = HTTPUtils.cleanXSS((String) request.getParameter("format"));

		//String key = (String) request.getParameter("key");
		String entries = HTTPUtils.cleanXSS((String) request.getParameter("entries"));

		if (action.compareTo("export") == 0 && (format.compareTo("draft_xml") == 0 || format.compareTo("final_xml") == 0)) {
		System.out.println("(*) exportMappingToXMLAction: " + entries);

			 exportMappingToXMLAction(request, response);
		} else if (action.compareTo("export") == 0 && (format.compareTo("draft_xlsx") == 0 || format.compareTo("final_xlsx") == 0)) {
		System.out.println("(*) exportMappingToExcelAction: " + entries);

			 exportMappingToExcelAction(request, response);
		} else if (action.compareTo("export") == 0 && format.compareTo("final_lexgrid_xml") == 0) {
		System.out.println("(*) exportMappingToLexGridAction: " + entries);

			 exportMappingToLexGridAction(request, response);
		}

   }








    public void exportMappingToExcelAction(HttpServletRequest request, HttpServletResponse response ) {

System.out.println("exportMappingToExcelAction ...");

        //new MappingBean().updateMapping(request);
		//String type = (String) request.getParameter("type");

		String entries = HTTPUtils.cleanXSS((String) request.getParameter("entries"));
		if (entries.startsWith("ALL")) {
			entries = "Valid_Invalid_Pending";
		}

System.out.println("(*) Options: " + entries);


        try {

			long ms = System.currentTimeMillis();

String key = HTTPUtils.cleanXSS((String) request.getParameter("key"));
System.out.println("key: " + key);


            //String[] entry_status = request.getParameterValues("entry_status");


			HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
			if (mappings == null) {
				System.out.println("exportMappingToMSExcelAction mappings == null ???");

				mappings = new HashMap();
				request.getSession().setAttribute("mappings", mappings);
			}

			//HashMap status_hmap = (HashMap) request.getSession().getAttribute("status_hmap");
			MappingObject obj = (MappingObject) mappings.get(key);
			PrintWriter out = response.getWriter();

			String identifier = null;
			if (obj == null) {
				  String t = "mapping obj cannot be found for key : " + key;
				  System.out.println(t);
				  out.println(t);
				  out.flush();
				  out.close();
				  return;

			} else {
				identifier = obj.getName();
		    }
			//String identifier = (String) request.getParameter("identifier");
			if (identifier == null) {
				identifier = "mapping";
			}

String format = (String) request.getParameter("format");
boolean isDraft = true;
if (!format.startsWith("draft")) {
	isDraft = false;
}

//String ext = format.replace("_xlsx", ".xlsx");

String ext = format.replace("_xlsx", ".xls");

			identifier = identifier + "_" + ext;

			//identifier = identifier + ".xls";//.xlsx

System.out.println("mapping file name: " + identifier);

			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename="
						+ identifier);


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

			  if (isDraft) {
				  out.println("   <th class=\"dataTableHeader\" scope=\"col\" align=\"left\">");
				  out.println("	  Status");
				  out.println("   </th>");

				  out.println("   <th class=\"dataTableHeader\" scope=\"col\" align=\"left\">");
				  out.println("	  Comment");
				  out.println("   </th>");
			  }


              Vector options = new Vector();
              //entries
              if (entries.indexOf("Valid") != -1) options.add("Valid");
              if (entries.indexOf("Invalid") != -1) options.add("Invalid");
              if (entries.indexOf("Pending") != -1) options.add("Pending");

              //options.add("Valid");

			  // Add mapping entries based on selected entry categories here -- to be implemented.
              List<MappingData> list = obj.getMappingData(options);

              for (int i=0; i<list.size(); i++) {

				  MappingData md = (MappingData) list.get(i);
				  String sourceCode = md.getSourceCode();
				  String sourceName = md.getSourceName();
				  //String sourceCodingScheme,
				  //String sourceCodingSchemeVersion,
				  String sourceCodeNamespace = md.getSourceCodeNamespace();
				  //String associationName,
				  String rel = md.getRel();

				  int score = md.getScore();
				  String score_obj = Integer.valueOf(score).toString();
				  String targetCode = md.getTargetCode();
				  String targetName = md.getTargetName();
				  //String targetCodingScheme,
				  //String targetCodingSchemeVersion,
				  String targetCodeNamespace = md.getTargetCodeNamespace();
				  String status = md.getStatus();
				  String comment = md.getComment();
				  if (DataUtils.isNull(comment)) {
					  comment = "";
				  }


      out.println("			      <tr>");
      out.println("			          <td>" + sourceCodeNamespace + "</td>");
      out.println("			          <td>" + sourceCode + "</td>");
      out.println("			          <td>" + sourceName + "</td>");
      out.println("			          <td>" + rel + "</td>");
      out.println("			          <td>" + score_obj + "</td>");
      out.println("			          <td>" + targetCodeNamespace + "</td>");
      out.println("			          <td>" + targetCode + "</td>");
      out.println("			          <td>" + targetName + "</td>");


      if (isDraft && entries.indexOf(status) != -1) {
		  out.println("			          <td>" + status + "</td>");
		  out.println("			          <td>" + comment + "</td>");
	  }


      out.println("			      </tr>");



			  }
			  out.println("</table>");
			  out.flush();
			  out.close();


		} catch (Exception ex) {
			ex.printStackTrace();
		}

  }






    public void exportMappingToXMLAction(HttpServletRequest request, HttpServletResponse response ) {
        //new MappingBean().updateMapping(request);
		//String type = (String) request.getParameter("type");

		String entries = (String) request.getParameter("entries");
		if (entries.startsWith("ALL")) {
			entries = "Valid_Invalid_Pending";
		}

System.out.println("(*) Options: " + entries);

        try {
        	//String xml = null;
			StringBuffer sb = null;
			response.setContentType("text/xml");

String key = (String) request.getParameter("key");
System.out.println("key: " + key);

String format = (String) request.getParameter("format");

            //String[] entry_status = request.getParameterValues("entry_status");


            //Vector options = toVector(entry_status);

            Vector options = DataUtils.parseData(entries, "_");

			HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
			if (mappings == null) {
				System.out.println("exportMappingToXMLAction mappings == null ???");

				mappings = new HashMap();
				request.getSession().setAttribute("mappings", mappings);
			}

			HashMap status_hmap = (HashMap) request.getSession().getAttribute("status_hmap");
			MappingObject obj = (MappingObject) mappings.get(key);

			String mapping_name = null;
			String mapping_version = null;

			if (obj == null) {
				System.out.println("mapping obj cannot be found for key : " + key);
			} else {
				mapping_name = obj.getName();
				//mapping_version = obj.getVersion();
			}
			String xml = "";

			if (obj != null) {
				obj.setStatusHashMap(status_hmap);
				sb = new StringBuffer(xml);
				sb = sb.append(obj.toXML(options));
			}

			if (mapping_name == null) {
				mapping_name = "unknown_mapping";
			} else {
				mapping_name = mapping_name.replaceAll(" ", "_");
			}

			String ext = format.replace("_xml", ".xml");
			mapping_name = mapping_name + "_" + ext;

			response.setHeader("Content-Disposition", "attachment; filename="
					+ mapping_name);

//System.out.println("mapping file name : " + mapping_name);
//System.out.println("mapping sb.length() : " + sb.length());

            if (sb == null) {
				sb = new StringBuffer();
				sb.append("Content not available.");
			}

			response.setContentLength(sb.length());

			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}



    private Vector toVector(String[] entry_status) {
		Vector options = new Vector();
		if (entry_status != null) {
			for (int i = 0; i < entry_status.length; ++i) {
				String t = (String) entry_status[i];
				options.add(t);
			}
		}
		return options;
	}



    public void exportMappingToLexGridAction(HttpServletRequest request, HttpServletResponse response ) {
        //new MappingBean().updateMapping(request);
		//String type = (String) request.getParameter("type");

        try {
        	//String xml = null;
			StringBuffer sb = null;
			response.setContentType("text/xml");

String key = (String) request.getParameter("key");
System.out.println("key: " + key);

String format = (String) request.getParameter("format");

			HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
			if (mappings == null) {
				System.out.println("exportMappingToXMLAction mappings == null ???");

				mappings = new HashMap();
				request.getSession().setAttribute("mappings", mappings);
			}

			//HashMap status_hmap = (HashMap) request.getSession().getAttribute("status_hmap");
			MappingObject obj = (MappingObject) mappings.get(key);

			String mapping_name = obj.getName();
			//String mapping_version = obj.getVersion();

			mapping_name = mapping_name.replaceAll(" ", "_");
			//mapping_name = mapping_name + ".xml";
			String ext = format.replace("_xml", ".xml");
			mapping_name = mapping_name + "_" + ext;

System.out.println("mapping file name : " + mapping_name);

			response.setHeader("Content-Disposition", "attachment; filename="
					+ mapping_name);

            PrintWriter out = response.getWriter();

            String source_cs_uri = DataUtils.codingSchemeName2URI(obj.getFromCS());//"source_cs_uri_to_be_determined";
            String target_cs_uri = DataUtils.codingSchemeName2URI(obj.getToCS());//"target_cs_uri_to_be_determined";

  			LexGridXMLGenerator.writeHeader(out, obj.getName(), obj.getVersion(),
                                       		obj.getFromCS(), source_cs_uri,
                                       		obj.getToCS(), target_cs_uri);

            String[] entry_status = request.getParameterValues("entry_status");
            Vector options = toVector(entry_status);

System.out.println("options size: " + options.size());
if (options.size() == 0) {
	options.add("Valid");
}

            //options.add("Valid");
            //options.add("Invalid");
            List<MappingData> list = obj.getMappingData(options);
            LexGridXMLGenerator.writeContent(out, obj.getName(),
                                             obj.getFromCS(), obj.getToCS(), list);

			out.flush();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}


