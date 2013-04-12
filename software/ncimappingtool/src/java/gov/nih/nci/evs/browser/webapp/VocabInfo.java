/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.webapp;

import java.util.*;
import org.apache.log4j.*;
import gov.nih.nci.evs.utils.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class VocabInfo {
    private static Logger _logger = Logger.getLogger(VocabInfo.class);
    private static final String DELIM = ";";
    private String _displayName = "";
    private String _name = "";
    private String _url = "";
    private ArrayList<String> _emails = new ArrayList<String>();

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("displayName=" + _displayName);
        buffer.append(", name=" + _name);
        buffer.append(", url=" + _url);
        buffer.append(", emails=" + StringUtils.toString(_emails, "; "));
        return buffer.toString();
    }

    public void setDisplayName(String name) {
        _displayName = name;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setUrl(String url) {
        _url = url;
    }

    public String getUrl() {
        return _url;
    }

    public void addEmail(String email) {
        if (!_emails.contains(email))
            _emails.add(email);
    }

    public ArrayList<String> getEmails() {
        return _emails;
    }

    public boolean isEmpty() {
        return _name.length() <= 0 && _url.length() <= 0 && _emails.size() <= 0;
    }

    public VocabInfo() {
    }

    public VocabInfo(String name) {
        setDisplayName(name);
        setName(name);
    }

    public static VocabInfo parse(String text) {
        if (text == null || text.trim().length() <= 0 || text.startsWith("@")
            || text.startsWith("${"))
            return null;

        StringTokenizer tokenizer = new StringTokenizer(text, DELIM, true);
        VocabInfo info = new VocabInfo();
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.equals(DELIM)) {
                ++i;
                continue;
            }
            if (i == 0)
                info.setDisplayName(token);
            else if (i == 1)
                info.setName(token);
            else if (i == 2)
                info.setUrl(token);
            else if (token.length() > 0)
                info.addEmail(token);
        }
        return !info.isEmpty() ? info : null;
    }

    public static void debug(VocabInfo list) {
        if (!_logger.isInfoEnabled())
            return;
        _logger.info("* Display Name: " + list.getDisplayName());
        _logger.info("  * Name: " + list.getName());
        _logger.info("  * Url: " + list.getUrl());
        ArrayList<String> emails = list.getEmails();
        Iterator<String> iterator = emails.iterator();
        while (iterator.hasNext()) {
            String email = iterator.next();
            _logger.info("  * Email: " + email);
        }
    }

    public static void debug(List<VocabInfo> list) {
        if (!_logger.isInfoEnabled())
            return;
        Iterator<VocabInfo> iterator = list.iterator();
        _logger.info(StringUtils.SEPARATOR);
        _logger.info("List of vocabularies:");
        while (iterator.hasNext())
            debug(iterator.next());
    }

    public static void main(String[] args) {
        String[] values =
            new String[] { "NCIt ; NCI Thesaurus ; http://ncit-qa.nci.nih.gov/; ncicb@pop.nci.nih.gov; NCIThesaurus@mail.nih.gov", };
        for (int i = 0; i < values.length; ++i) {
            _logger.info(StringUtils.SEPARATOR);
            String value = values[i];
            _logger.info("Value: \"" + value + "\"");
            VocabInfo vocab = VocabInfo.parse(value);
            debug(vocab);
            _logger.info("");
        }
    }
}
