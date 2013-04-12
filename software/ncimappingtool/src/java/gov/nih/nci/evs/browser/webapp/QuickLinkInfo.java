/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.webapp;

import java.util.*;
import gov.nih.nci.evs.utils.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class QuickLinkInfo {
    private String _display = "";
    private String _url = "";
    
    public QuickLinkInfo(String display, String url) {
        _display = display;
        _url = url;
    }
    
    public String getDisplay() {
        return _display;
    }
    
    public String getUrl() {
        return _url;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("display=" + _display);
        buffer.append(", url=" + _url);
        return buffer.toString();
    }

    public static ArrayList<QuickLinkInfo> parse(String value) {
        ArrayList<QuickLinkInfo> list = new ArrayList<QuickLinkInfo>();
        String[] subValues = StringUtils.toStrings(value, ";", false);
        for (int i=0; i<subValues.length; i+=2) {
            String display = subValues[i];
            String url = i+1<subValues.length ? subValues[i+1] : "";
            list.add(new QuickLinkInfo(display, url));
        }
        return list;
    }
}
