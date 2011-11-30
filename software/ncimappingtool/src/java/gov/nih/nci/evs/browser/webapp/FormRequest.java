package gov.nih.nci.evs.browser.webapp;

import java.util.*;
import javax.servlet.http.*;
import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.utils.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class FormRequest extends BaseRequest implements IFormRequest {
    // List of attribute name(s):
    public static final String VERSION = "version";
    public static final String MESSAGE = "message";
    public static final String WARNINGS = "warnings";

    // Parameter list(s):
    public static final String[] ALL_PARAMETERS = 
        new String[] { MESSAGE, WARNINGS };
    
    // List of return state(s):
    public static final String SUCCESSFUL_STATE = "successful";
    public static final String MESSAGE_STATE = "message";
    public static final String WARNING_STATE = "warning";
    
    // List of member variable(s):
    protected String _vocabularyParameter = null;
    protected boolean _isSendEmail = AppProperties.getInstance().isSendEmail();
    
    public FormRequest() {
        super();
    }
    
    public FormRequest(String vocabularyParameter) {
        _vocabularyParameter = vocabularyParameter;
    }
    
    public void clear() {
        super.clear();
        clearAttributes(ALL_PARAMETERS);
    }
    
    public String clearForm() {
        clear();
        return WARNING_STATE;
    }
    
    public String submitForm() {
        _request.setAttribute(MESSAGE, "NewTermRequest.submitForm");
        return MESSAGE_STATE;
    }
    
    protected void setParameters(String[] parameters) {
        super.setParameters(parameters);

        if (_vocabularyParameter == null)
            return;
        // Hack: In content_new_concept.jsp page, the vocabulary comboBox
        // stores the value as the URL instead of the vocabulary name.
        // This is done so that the "Browse" button can display the
        // vocabulary's URL in a separate window kicked off by using
        // javascript displayLinkInNewWindow method. The following
        // lines manually replaces the URL with the vocabulary name.
        // Note: In order for this to work, the URL must be unique.
        String url = _parametersHashMap.get(_vocabularyParameter);
        if (url == null)
            return;
        
        HttpServletRequest request = HTTPUtils.getRequest();
        Prop.Version version = (Prop.Version) 
            request.getSession().getAttribute(FormRequest.VERSION);

        String name = AppProperties.getInstance().getVocabularyName(version, url);
        _parametersHashMap.put(_vocabularyParameter, name);
    }
    
    protected void validate(StringBuffer buffer, boolean validValue, String message) {
        if (validValue)
            return;
        if (buffer.length() > 0)
            buffer.append("\n");
        buffer.append(message);
    }

    protected void itemizeParameters(StringBuffer buffer, String header,
        HashMap<String, String> labels, String[] parameters) {
        buffer.append(header + "\n");
        for (int i = 0; i < parameters.length; ++i) {
            String parameter = parameters[i];
            String label = labels != null ? labels.get(parameter) : parameter;
            buffer_append(buffer, label, parameter);
        }
        buffer.append("\n");
    }

    protected void buffer_append(StringBuffer buffer, String label, 
        String parameter) {
        String value = _parametersHashMap.get(parameter);
        buffer.append("* " + label + ": " + value + "\n");
    }
    
    protected String printSendEmailWarning() {
        if (_isSendEmail)
            return "";
        StringBuffer buffer = new StringBuffer();
        buffer.append("Warning: Email was never sent:\n");
        buffer.append("    * send.email configuration flag = " + _isSendEmail + ".\n");
        buffer.append("    * This flag allows us to design and implement our web pages\n");
        buffer.append("      without having to send a bunch of bogus emails.\n");

        if (_vocabularyParameter == null || _vocabularyParameter.length() <= 0)
            return buffer.toString();
        String recipients = getRecipients();
        buffer.append("Debug:\n");
        buffer.append("    * recipient(s): " + recipients + "\n");
        _request.setAttribute(WARNINGS, buffer.toString());
        return buffer.toString();
    }
    
    protected String getRecipients() {
        return "";
    }
}
