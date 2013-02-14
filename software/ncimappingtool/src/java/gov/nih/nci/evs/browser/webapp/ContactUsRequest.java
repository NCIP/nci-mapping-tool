package gov.nih.nci.evs.browser.webapp;

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

public class ContactUsRequest extends FormRequest {
    // List of attribute name(s):
    private static final String SUBJECT = "subject";
    private static final String EMAIL_MSG = "email_msg";
    private static final String EMAIL_ADDRESS = "email_address";
    private static final String WARNING_TYPE = "warning_type";

    private static final String[] ALL_PARAMETERS = new String[] {
        SUBJECT, EMAIL_MSG, EMAIL_ADDRESS };
    private static final String[] MOST_PARAMETERS = new String[] {
        SUBJECT, EMAIL_MSG };
    private static final String[] SESSION_ATTRIBUTES = new String[] {
        EMAIL_ADDRESS };

    public ContactUsRequest() {
        setParameters(ALL_PARAMETERS);
    }

    public void clear() {
        super.clear();
        clearAttributes(new String[] { WARNING_TYPE });
        clearSessionAttributes(SESSION_ATTRIBUTES);
    }

    public String submitForm() {
        clearAttributes(FormRequest.ALL_PARAMETERS);
        clearAttributes(new String[] { WARNING_TYPE });
        updateAttributes();
        updateSessionAttributes(SESSION_ATTRIBUTES);
        AppProperties appProperties = AppProperties.getInstance();

        try {
            String mailServer = appProperties.getMailSmtpServer();
            String subject = _request.getParameter(SUBJECT);
            String emailMsg = _request.getParameter(EMAIL_MSG);
            String from = _request.getParameter(EMAIL_ADDRESS);
            String recipients = appProperties.getContactUrl();
            MailUtils.postMail(mailServer, from, recipients, subject, emailMsg, _isSendEmail);
        } catch (UserInputException e) {
            String warnings = e.getMessage();
            _request.setAttribute(WARNINGS, StringUtils.toHtml(warnings));
            _request.setAttribute(WARNING_TYPE, Prop.WarningType.User.name());
            return WARNING_STATE;
        } catch (Exception e) {
            String warnings = "System Error: Your message was not sent.\n";
            warnings += "    (If possible, please contact NCI systems team.)\n";
            warnings += "\n";
            warnings += e.getMessage();
            _request.setAttribute(WARNINGS, StringUtils.toHtml(warnings));
            _request.setAttribute(WARNING_TYPE, Prop.WarningType.System.name());
            e.printStackTrace();
            return WARNING_STATE;
        }

        clearAttributes(MOST_PARAMETERS);
        String msg = "Your message was successfully sent.";
        _request.setAttribute(MESSAGE, StringUtils.toHtml(msg));
        printSendEmailWarning();
        return SUCCESSFUL_STATE;
    }

    protected String printSendEmailWarning() {
        if (_isSendEmail)
            return "";

        String warning = super.printSendEmailWarning();
        StringBuffer buffer = new StringBuffer(warning);
        AppProperties appProperties = AppProperties.getInstance();
        String[] recipients = appProperties.getContactUsRecipients();
        String from = MailUtils.cleanAddresses(_request.getParameter(EMAIL_ADDRESS));
        buffer.append("Debug:\n");
        buffer.append("    * recipient(s): " + StringUtils.toString(recipients, " ; ") + "\n");
        buffer.append("    * Subject: " + _request.getParameter(SUBJECT) + "\n");
        buffer.append("    * Message: ");
        String emailMsg = _request.getParameter(EMAIL_MSG);
        emailMsg = INDENT + emailMsg.replaceAll("\\\n", "\n" + INDENT);
        buffer.append(emailMsg + "\n");
        buffer.append("    * Email: " + from + "\n");

        _request.setAttribute(WARNINGS, buffer.toString());
        return buffer.toString();
    }
}

