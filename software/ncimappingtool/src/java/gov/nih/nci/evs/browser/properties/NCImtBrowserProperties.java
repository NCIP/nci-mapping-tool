/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.properties;

import java.util.*;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */

public class NCImtBrowserProperties {
    private static Logger _logger =
        Logger.getLogger(NCImtBrowserProperties.class);
    private static List _displayItemList;
    private static List _metadataElementList;
    private static List _defSourceMappingList;
    private static HashMap _defSourceMappingHashMap;
    private static List _securityTokenList;
    private static HashMap _securityTokenHashMap;
    private static HashMap _configurableItemMap;

    // KLO
    private static final String DEBUG_ON = "DEBUG_ON";
    public static final String EVS_SERVICE_URL = "EVS_SERVICE_URL";
    public static final String LG_CONFIG_FILE = "LG_CONFIG_FILE";
    public static final String MAXIMUM_RETURN = "MAXIMUM_RETURN";
    public static final String EHCACHE_XML_PATHNAME = "EHCACHE_XML_PATHNAME";
    public static final String SORT_BY_SCORE = "SORT_BY_SCORE";
    public static final String MAIL_SMTP_SERVER = "MAIL_SMTP_SERVER";
    public static final String NCICB_CONTACT_URL = "NCICB_CONTACT_URL";
    public static final String MAXIMUM_TREE_LEVEL = "MAXIMUM_TREE_LEVEL";
    public static final String TERMINOLOGY_SUBSET_DOWNLOAD_URL =
        "TERMINOLOGY_SUBSET_DOWNLOAD_URL";
    public static final String NCIT_BUILD_INFO = "NCIT_BUILD_INFO";
    public static final String NCIT_APP_VERSION = "APPLICATION_VERSION";
    public static final String ANTHILL_BUILD_TAG_BUILT =
        "ANTHILL_BUILD_TAG_BUILT";
    public static final String NCIM_URL = "NCIM_URL";
    public static final String NCIT_URL = "NCIT_URL";
    public static final String TERM_SUGGESTION_APPLICATION_URL =
        "TERM_SUGGESTION_APPLICATION_URL";
    public static final String LICENSE_PAGE_OPTION = "LICENSE_PAGE_OPTION";

    public static final String PAGINATION_TIME_OUT = "PAGINATION_TIME_OUT";
    public static final String MINIMUM_SEARCH_STRING_LENGTH =
        "MINIMUM_SEARCH_STRING_LENGTH";
    public static final String SLIDING_WINDOW_HALF_WIDTH =
        "SLIDING_WINDOW_HALF_WIDTH";
    public static final String STANDARD_FTP_REPORT_URL =
        "STANDARD_FTP_REPORT_URL";
    public static final String STANDARD_FTP_REPORT_INFO =
        "STANDARD_FTP_REPORT_INFO";
    public static final int STANDARD_FTP_REPORT_INFO_MAX = 20;

    public static final String MAPPING_DIR = "MAPPING_DIR";
    public static final String MODE_OF_OPERATION = "MODE_OF_OPERATION";

    public static final String BATCH_MODE_OF_OPERATION = "batch";
    public static final String INTERACTIVE_MODE_OF_OPERATION = "interactive";

    private static NCImtBrowserProperties _browserProperties = null;
    private static Properties _properties = new Properties();

    private static boolean _debugOn = false;
    private static int _maxToReturn = 1000;
    private static int _maxTreeLevel = 1000;
    private static String _service_url = null;
    private static String _lg_config_file = null;
    private static String _mapping_dir = null;
    private static String _mode_of_operation = null;

    private static String _sort_by_score = null;
    private static String _mail_smtp_server = null;
    private static String _ncicb_contact_url = null;
    private static String _terminology_subset_download_url = null;
    private static String _term_suggestion_application_url = null;

    private static String _license_page_option = null;
    private static String _ncim_url = null;
    private static String _ncit_url = null;
    private static int _pagination_time_out = 4;
    private static int _minimum_search_string_length = 1;

    private static int _sliding_window_half_width = 5;
    private static String _standard_ftp_report_url = "";
    private static Vector<StandardFtpReportInfo> _standard_ftp_report_info_list =
        new Vector<StandardFtpReportInfo>();

    /**
     * Private constructor for singleton pattern.
     */
    private NCImtBrowserProperties() {
    }



    static {
		try {
			_browserProperties = new NCImtBrowserProperties();
			loadProperties();

			_debugOn = Boolean.parseBoolean(getProperty(DEBUG_ON));

			String max_str =
				_browserProperties
					.getProperty(_browserProperties.MAXIMUM_RETURN);
			_maxToReturn = Integer.parseInt(max_str);

			String max_tree_level_str =
				_browserProperties
					.getProperty(_browserProperties.MAXIMUM_TREE_LEVEL);
			_maxTreeLevel = Integer.parseInt(max_tree_level_str);

			_service_url =
				_browserProperties
					.getProperty(_browserProperties.EVS_SERVICE_URL);
			// _logger.info("EVS_SERVICE_URL: " + service_url);

			_lg_config_file =
				_browserProperties
					.getProperty(_browserProperties.LG_CONFIG_FILE);
			// _logger.info("LG_CONFIG_FILE: " + lg_config_file);

			_sort_by_score =
				_browserProperties
					.getProperty(_browserProperties.SORT_BY_SCORE);
			_ncicb_contact_url =
				_browserProperties
					.getProperty(_browserProperties.NCICB_CONTACT_URL);
			_mail_smtp_server =
				_browserProperties
					.getProperty(_browserProperties.MAIL_SMTP_SERVER);
			_terminology_subset_download_url =
				_browserProperties
					.getProperty(_browserProperties.TERMINOLOGY_SUBSET_DOWNLOAD_URL);
			_term_suggestion_application_url =
				_browserProperties
					.getProperty(_browserProperties.TERM_SUGGESTION_APPLICATION_URL);
			_license_page_option =
				_browserProperties
					.getProperty(_browserProperties.LICENSE_PAGE_OPTION);
			_ncim_url =
				_browserProperties
					.getProperty(_browserProperties.NCIM_URL);
			_ncit_url =
				_browserProperties
					.getProperty(_browserProperties.NCIT_URL);

			_mapping_dir =
				_browserProperties
					.getProperty(_browserProperties.MAPPING_DIR);

			_mode_of_operation =
				_browserProperties
					.getProperty(_browserProperties.MODE_OF_OPERATION);

			String pagination_time_out_str =
				_browserProperties
					.getProperty(_browserProperties.PAGINATION_TIME_OUT);
			if (pagination_time_out_str != null) {
				_pagination_time_out =
					Integer.parseInt(pagination_time_out_str);
			}

			String minimum_search_string_length_str =
				_browserProperties
					.getProperty(_browserProperties.MINIMUM_SEARCH_STRING_LENGTH);
			if (minimum_search_string_length_str != null) {
				int min_search_string_length =
					Integer.parseInt(minimum_search_string_length_str);
				if (min_search_string_length > 1) {
					_minimum_search_string_length =
						min_search_string_length;
				}
			}
			String sliding_window_half_width_str =
				_browserProperties
					.getProperty(_browserProperties.SLIDING_WINDOW_HALF_WIDTH);
			if (sliding_window_half_width_str != null) {
				int sliding_window_halfwidth =
					Integer.parseInt(sliding_window_half_width_str);
				if (sliding_window_halfwidth > 1) {
					_sliding_window_half_width =
						sliding_window_halfwidth;
				}
			}
			_standard_ftp_report_url = getProperty(STANDARD_FTP_REPORT_URL);
			_standard_ftp_report_info_list = StandardFtpReportInfo.parse(
				STANDARD_FTP_REPORT_INFO, STANDARD_FTP_REPORT_INFO_MAX);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static boolean get_debugOn() {
		return _debugOn;
	}
    /**
     * Gets the single instance of NCImtBrowserProperties.
     *
     * @return single instance of NCImtBrowserProperties
     *
     * @throws Exception the exception
     */
    public static NCImtBrowserProperties getInstance() {//throws Exception {
		/*
        if (_browserProperties == null) {
            synchronized (NCImtBrowserProperties.class) {

                if (_browserProperties == null) {
                    _browserProperties = new NCImtBrowserProperties();
                    loadProperties();

                    _debugOn = Boolean.parseBoolean(getProperty(DEBUG_ON));

                    String max_str =
                        _browserProperties
                            .getProperty(_browserProperties.MAXIMUM_RETURN);
                    _maxToReturn = Integer.parseInt(max_str);

                    String max_tree_level_str =
                        _browserProperties
                            .getProperty(_browserProperties.MAXIMUM_TREE_LEVEL);
                    _maxTreeLevel = Integer.parseInt(max_tree_level_str);

                    _service_url =
                        _browserProperties
                            .getProperty(_browserProperties.EVS_SERVICE_URL);
                    // _logger.info("EVS_SERVICE_URL: " + service_url);

                    _lg_config_file =
                        _browserProperties
                            .getProperty(_browserProperties.LG_CONFIG_FILE);
                    // _logger.info("LG_CONFIG_FILE: " + lg_config_file);

                    _sort_by_score =
                        _browserProperties
                            .getProperty(_browserProperties.SORT_BY_SCORE);
                    _ncicb_contact_url =
                        _browserProperties
                            .getProperty(_browserProperties.NCICB_CONTACT_URL);
                    _mail_smtp_server =
                        _browserProperties
                            .getProperty(_browserProperties.MAIL_SMTP_SERVER);
                    _terminology_subset_download_url =
                        _browserProperties
                            .getProperty(_browserProperties.TERMINOLOGY_SUBSET_DOWNLOAD_URL);
                    _term_suggestion_application_url =
                        _browserProperties
                            .getProperty(_browserProperties.TERM_SUGGESTION_APPLICATION_URL);
                    _license_page_option =
                        _browserProperties
                            .getProperty(_browserProperties.LICENSE_PAGE_OPTION);
                    _ncim_url =
                        _browserProperties
                            .getProperty(_browserProperties.NCIM_URL);
                    _ncit_url =
                        _browserProperties
                            .getProperty(_browserProperties.NCIT_URL);

                    _mapping_dir =
                        _browserProperties
                            .getProperty(_browserProperties.MAPPING_DIR);

                    _mode_of_operation =
                        _browserProperties
                            .getProperty(_browserProperties.MODE_OF_OPERATION);

                    String pagination_time_out_str =
                        _browserProperties
                            .getProperty(_browserProperties.PAGINATION_TIME_OUT);
                    if (pagination_time_out_str != null) {
                        _pagination_time_out =
                            Integer.parseInt(pagination_time_out_str);
                    }

                    String minimum_search_string_length_str =
                        _browserProperties
                            .getProperty(_browserProperties.MINIMUM_SEARCH_STRING_LENGTH);
                    if (minimum_search_string_length_str != null) {
                        int min_search_string_length =
                            Integer.parseInt(minimum_search_string_length_str);
                        if (min_search_string_length > 1) {
                            _minimum_search_string_length =
                                min_search_string_length;
                        }
                    }
                    String sliding_window_half_width_str =
                        _browserProperties
                            .getProperty(_browserProperties.SLIDING_WINDOW_HALF_WIDTH);
                    if (sliding_window_half_width_str != null) {
                        int sliding_window_halfwidth =
                            Integer.parseInt(sliding_window_half_width_str);
                        if (sliding_window_halfwidth > 1) {
                            _sliding_window_half_width =
                                sliding_window_halfwidth;
                        }
                    }
                    _standard_ftp_report_url = getProperty(STANDARD_FTP_REPORT_URL);
                    _standard_ftp_report_info_list = StandardFtpReportInfo.parse(
                        STANDARD_FTP_REPORT_INFO, STANDARD_FTP_REPORT_INFO_MAX);
                }
            }
        }
        */

        return _browserProperties;
    }

    // public String getProperty(String key) throws Exception{
    public static String getProperty(String key) throws Exception {
        // return properties.getProperty(key);
        String ret_str = (String) _configurableItemMap.get(key);
        if (ret_str == null)
            return null;
        if (ret_str.compareToIgnoreCase("null") == 0)
            return null;
        return ret_str;
    }

    public static List getDisplayItemList() {
        return _displayItemList;
    }

    public static List getMetadataElementList() {
        return _metadataElementList;
    }

    public static List getDefSourceMappingList() {
        return _defSourceMappingList;
    }

    public static HashMap getDefSourceMappingHashMap() {
        return _defSourceMappingHashMap;
    }

    public static List getSecurityTokenList() {
        return _securityTokenList;
    }

    public static HashMap getSecurityTokenHashMap() {
        return _securityTokenHashMap;
    }

    private static void loadProperties() throws Exception {
        String propertyFile =
            System.getProperty("NCImtProperties");
        _logger.info("NCImtBrowserProperties File Location= " + propertyFile);

System.out.println("propertyFile: " + propertyFile);


        PropertyFileParser parser = new PropertyFileParser(propertyFile);
        parser.run();

        _displayItemList = parser.getDisplayItemList();
        _metadataElementList = parser.getMetadataElementList();
        _defSourceMappingList = parser.getDefSourceMappingList();
        _defSourceMappingHashMap = parser.getDefSourceMappingHashMap();
        _securityTokenList = parser.getSecurityTokenList();
        _securityTokenHashMap = parser.getSecurityTokenHashMap();

        _configurableItemMap = parser.getConfigurableItemMap();

    }

    public static String getLicensePageOption() {
        return _license_page_option;
    }

    public static String getNCIM_URL() {
        return _ncim_url;
    }

    public static String getNCIT_URL() {
        return _ncit_url;
    }

    public static String getMappingDir() {
        return _mapping_dir;
    }

    public static String getModeOfOperation() {
        return _mode_of_operation;
    }

    public static int getPaginationTimeOut() {
        return _pagination_time_out;
    }

    public static int getMinimumSearchStringLength() {
        return _minimum_search_string_length;
    }

    public static int getSlidingWindowHalfWidth() {
        return _sliding_window_half_width;
    }

    public static String getStandardFtpReportUrl() {
        return _standard_ftp_report_url;
    }

    public static Vector<StandardFtpReportInfo> getStandardFtpReportInfoList() {
        return _standard_ftp_report_info_list;
    }
}
