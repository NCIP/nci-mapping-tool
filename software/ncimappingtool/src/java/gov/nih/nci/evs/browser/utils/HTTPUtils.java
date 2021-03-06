/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.*;

import javax.faces.context.*;
import javax.servlet.http.*;

import java.io.*;
import java.net.*;
import java.util.regex.*;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * HTTP Utility methods
 *
 * @author garciawa2
 *
 */
public class HTTPUtils {
    private static Logger _logger = Logger.getLogger(HTTPUtils.class);
    private static final String REFERER = "referer";
    private static final int MAX_FONT_SIZE = 29;
    private static final int MIN_FONT_SIZE = 22;
    private static final int MAX_STR_LEN = 18;
    public  static final int ABS_MAX_STR_LEN = 40;

/*
    public static String cleanXSS(String value) {

        if (value == null || value.length() < 1)
            return value;

        // Remove XSS attacks
        value = replaceAll(value, "<\\s*script\\s*>.*</\\s*script\\s*>", "");
        value = replaceAll(value, ".*<\\s*iframe.*>", "");
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value =
            replaceAll(value, "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
                "\"\"");
        value = value.replaceAll("\"", "&quot;");
        return value;

    }
*/

    public static String cleanXSS(String value) {

        if (value == null || value.length() < 1)
            return value;

        // Remove XSS attacks
        value = replaceAll(value, "<\\s*script\\s*>.*</\\s*script\\s*>", "");
        value = replaceAll(value, ".*<\\s*iframe.*>", "");
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        //value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value =
            replaceAll(value, "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
                "\"\"");
        value = value.replaceAll("\"", "&quot;");
        return value;

    }

/*
    public static String appendNCIT(String link) {
    	String nciturl = null;
    	if (link.contains("/ncitbrowser")) return link;
    	if (link.endsWith("/"))	link = nciturl + "ncitbrowser";
    	link = nciturl + "/ncitbrowser";
    	return link;
    }
*/

    public static int maxFontSize(String value) {
    	int size;
		if (value == null || value.length() == 0)
			size = MAX_FONT_SIZE;
		else if (value.length() >= MAX_STR_LEN)
			size = MIN_FONT_SIZE;
		else {
			// Calculate an intermediate font size
			/*
			size = MIN_FONT_SIZE
					+ Math.round((MAX_FONT_SIZE / MAX_STR_LEN)
							/ (MIN_FONT_SIZE / value.length()));
		    */
			size = MIN_FONT_SIZE
					+ Math.round(((float) MAX_FONT_SIZE / (float) MAX_STR_LEN)
							/ ((float) MIN_FONT_SIZE / (float) value.length()));
		}
    	return size;
    }
    /*
    public static int maxFontSize(String value) {
    	int size;
		if (value == null || value.length() == 0)
			size = MAX_FONT_SIZE;
		else if (value.length() >= MAX_STR_LEN)
			size = MIN_FONT_SIZE;
		else {
			// Calculate an intermediate font size
			size = MIN_FONT_SIZE
					+ Math.round((MAX_FONT_SIZE / MAX_STR_LEN)
							/ (MIN_FONT_SIZE / value.length()));
		}
    	return size;
    }
    */

    public static String replaceAll(String string, String regex,
        String replaceWith) {

        Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        string = myPattern.matcher(string).replaceAll(replaceWith);
        return string;

    }

    //--------------------------------------------------------------------------
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
    }

    public static void printRequestSessionAttributes(HttpServletRequest request) {
        _logger.debug(" ");
        _logger.debug(Utils.SEPARATOR);
        _logger.debug("Request Session Attribute(s):");

        try {
            HttpSession session = request.getSession();
            Enumeration<?> enumeration =
                SortUtils.sort(session.getAttributeNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = session.getAttribute(name);
                _logger.debug("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public static void printRequestSessionAttributes() {
        HttpServletRequest request = getRequest();
        printRequestSessionAttributes(request);
    }

    public static void printRequestAttributes(HttpServletRequest request) {
        _logger.debug(" ");
        _logger.debug(Utils.SEPARATOR);
        _logger.debug("Request Attribute(s):");

        try {
            Enumeration<?> enumeration =
                SortUtils.sort(request.getAttributeNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getAttribute(name);
                _logger.debug("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public static void printRequestAttributes() {
        HttpServletRequest request = getRequest();
        printRequestAttributes(request);
    }

    public static void printRequestParameters(HttpServletRequest request) {
        _logger.debug(" ");
        _logger.debug(Utils.SEPARATOR);
        _logger.debug("Request Parameter(s):");

        try {
            Enumeration<?> enumeration =
                SortUtils.sort(request.getParameterNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getParameter(name);
                _logger.debug("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public static void printRequestParameters() {
        HttpServletRequest request = getRequest();
        printRequestParameters(request);
    }

    public static HashMap<String, String> retrieveRequestParametersMap(HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            Enumeration<?> enumeration =
                SortUtils.sort(request.getParameterNames());
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                String value = request.getParameter(name);
                map.put(name, value);
            }
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return map;
    }

    public static void printAttributes() {
        printRequestSessionAttributes();
        printRequestAttributes();
        printRequestParameters();
        _logger.debug(" ");
    }

    //--------------------------------------------------------------------------
    public static String convertJSPString(String t) {
        // Convert problem characters to JavaScript Escaped values
        if (t == null) {
            return "";
        }

        if (t.compareTo("") == 0) {
            return "";
        }

        String sigleQuoteChar = "'";
        String doubleQuoteChar = "\"";

        String dq = "&quot;";

        t = t.replaceAll(sigleQuoteChar, "\\" + sigleQuoteChar);
        t = t.replaceAll(doubleQuoteChar, "\\" + dq);
        t = t.replaceAll("\r", "\\r"); // replace CR with \r;
        t = t.replaceAll("\n", "\\n"); // replace LF with \n;

        return cleanXSS(t);
    }

    /**
     * @param request
     * @return
     */
    public static String getRefererParmEncode(HttpServletRequest request) {
        String iref = request.getHeader(REFERER);
        String referer = "N/A";
        if (iref != null)
            try {
                referer = URLEncoder.encode(iref, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // return N/A if encoding is not supported.
            }
        return cleanXSS(referer);
    }

    /**
     * @param request
     * @return
     */
    public static String getRefererParmDecode(HttpServletRequest request) {
        String refurl = "N/A";
        try {
            String iref = request.getParameter(REFERER);
            if (iref != null)
                refurl =
                    URLDecoder.decode(request.getParameter(REFERER), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // return N/A if encoding is not supported.
        }
        return cleanXSS(refurl);
    }

    /**
     * @param request
     */
    public static void clearRefererParm(HttpServletRequest request) {
        request.setAttribute(REFERER, null);
    }
}
