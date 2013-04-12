/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.servlet;

import org.json.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.bean.MappingObject;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import gov.nih.nci.evs.mapping.*;


/**
 * 
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history
 *     Initial implementation kim.ong@ngc.com
 *
 */

public final class UploadServlet extends HttpServlet {
    private static Logger _logger = Logger.getLogger(UploadServlet.class);
    /**
     * local constants
     */
    private static final long serialVersionUID = 1L;

    /**
     * Validates the Init and Context parameters, configures authentication URL
     *
     * @throws ServletException if the init parameters are invalid or any other
     *         problems occur during initialisation
     */
    public void init() throws ServletException {

    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a Servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }


	public String convertStreamToString(InputStream is, long size) throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[(int)size];
			try {
				Reader reader = new BufferedReader(
				new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}

			} finally {
				is.close();
			}
			return writer.toString();

		} else {
			return "";
		}

	}




    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Determine request by attributes
        String action = (String) request.getParameter("action");
        String type = (String) request.getParameter("type");


System.out.println("(*) UploadServlet ...action " + action);
        if (action == null) {
			action = "upload_data";
		}

		DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();
		/*
		 *Set the size threshold, above which content will be stored on disk.
		 */
		fileItemFactory.setSizeThreshold(1*1024*1024); //1 MB
		/*
		 * Set the temporary directory to store the uploaded files of size above threshold.
		 */
		//fileItemFactory.setRepository(tmpDir);

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		try {
			/*
			 * Parse the request
			 */
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();
			while(itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				/*
				 * Handle Form Fields.
				 */
				if(item.isFormField()) {
					System.out.println("File Name = "+item.getFieldName()+", Value = "+item.getString());
					//String s = convertStreamToString(item.getInputStream(), item.getSize());
					//System.out.println(s);


				} else {
					//Handle Uploaded files.
					System.out.println("Field Name = "+item.getFieldName()+
						", File Name = "+item.getName()+
						", Content type = "+item.getContentType()+
						", File Size = "+item.getSize());

					String s = convertStreamToString(item.getInputStream(), item.getSize());
					//System.out.println(s);

					request.getSession().setAttribute("action", action);

					if (action.compareTo("upload_data") == 0) {
						request.getSession().setAttribute("codes", s);
					} else {
						Mapping mapping = new Mapping().toMapping(s);

						System.out.println("Mapping " + mapping.getMappingName() + " uploaded.");
						System.out.println("Mapping version: " + mapping.getMappingVersion());

						MappingObject obj = mapping.toMappingObject();
						HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
						if (mappings == null) {
							mappings = new HashMap();
						}
						mappings.put(obj.getKey(), obj);
						request.getSession().setAttribute("mappings", mappings);
					}
				}
			}
		}catch(FileUploadException ex) {
			log("Error encountered while parsing the request",ex);
		} catch(Exception ex) {
			log("Error encountered while uploading file",ex);
		}


        //long ms = System.currentTimeMillis();

        if (action.compareTo("upload_data") == 0) {
			if (type.compareTo("codingscheme") == 0) {
        		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/pages/codingscheme_data.jsf"));
			} else if (type.compareTo("ncimeta") == 0) {
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/pages/ncimeta_data.jsf"));
			} else if (type.compareTo("valueset") == 0) {
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/pages/valueset_data.jsf"));
			}		} else {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/pages/home.jsf"));
		}

    }


}
