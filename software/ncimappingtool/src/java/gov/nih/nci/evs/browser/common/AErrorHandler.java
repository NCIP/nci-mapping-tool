/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.common;

import static gov.nih.nci.evs.browser.common.Constants.*;

import java.io.*;

import javax.faces.context.*;
import javax.faces.event.*;
import javax.servlet.http.*;

/**
 * 
 */

/**
 * @author garciawa2
 * 
 */
public class AErrorHandler {

    public static final String ERROR_PAGE = "/pages/error_handler.jsf";

    /**
     * @param _facesContext
     * @param _ex
     */
    public static void displayPhaseListenerException(
        FacesContext _facesContext, Exception _ex) {
        HttpServletResponse response =
            (HttpServletResponse) _facesContext.getExternalContext()
                .getResponse();
        HttpServletRequest request =
            (HttpServletRequest) _facesContext.getExternalContext()
                .getRequest();
        try {
            setPageErrorData(_ex, request);
            response.sendRedirect(request.getContextPath() + ERROR_PAGE);
            _facesContext.responseComplete();
            _ex.printStackTrace();
        } catch (IOException ex) {
            _ex.printStackTrace();
            ex.printStackTrace();
        }
        throw new AbortProcessingException("An Error has occurred.");
    }

    /**
     * @param _e
     * @param _request
     */
    public static void setPageErrorData(Throwable _e,
        HttpServletRequest _request) {
        _request.getSession().setAttribute(ERROR_MESSAGE, ERROR_UNEXPECTED);
    }

}
