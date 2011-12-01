package gov.nih.nci.evs.browser.bean;


import java.util.*;
import java.net.URI;
import java.io.*;


import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.common.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.*;
import org.apache.log4j.*;
import javax.faces.event.ValueChangeEvent;

import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import javax.servlet.ServletOutputStream;
import org.LexGrid.concepts.*;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.concepts.Definition;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Property;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;


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

public class MappingBean {
    private static Logger _logger = Logger.getLogger(MappingBean.class);
    private static List _rela_list = null;
    private static List _association_name_list = null;
    private static List _property_name_list = null;
    private static List _property_type_list = null;
    private static List _source_list = null;

    private static Vector _value_set_uri_vec = null;
    private static Vector _coding_scheme_vec = null;
    private static Vector _concept_domain_vec = null;

    private HashMap<String, MappingObject> _mappings = null;

    private String _name = null;

    private boolean _isNotEmpty = false;

    private String _status = "1";

	private static String NULL_STRING = "NULL";

	private static int NULL_STRING_HASH_CODE = NULL_STRING.hashCode();

    public void MappingBean() {

	}

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }


    public void setCurrentMapping(String name) {
		_name = name;
	}

    public boolean getIsNotEmpty() {
		if (getCount() == 0) {
			_isNotEmpty = false;
		} else {
			_isNotEmpty = true;
		}
		return _isNotEmpty;
	}


    private void _init() {
        if (_mappings == null) _mappings = new HashMap<String, MappingObject>();
    }



    public int getCount() {
        if (_mappings == null) {
			return 0;
		}

        return _mappings.keySet().size();
    }

    public Collection<MappingObject> getMappingList() {
        if (_mappings == null) _init();
        return _mappings.values();
    }

    public MappingObject getMapping(String key) {
        if (_mappings == null) {
        	_init();
        	return null;
        }
        if (key == null || key.length() < 1)
        	return null;
        return _mappings.get(key);
    }

    public boolean addMapping(MappingObject obj) {
        if (_mappings == null) {
        	_init();
        }
        if (obj == null) return false;
        String key = obj.getKey();//obj.getName() + "|" + obj.getVersion();
        if (key == null || key.length() < 1)
        	return false;
        _mappings.put(key, obj);
        return true;
    }



    public MappingObject getCurrentMapping() {
        if (_mappings == null) {
        	_init();
        	return null;
        }
        if (_mappings == null || _mappings.keySet().size() < 1)
        	return null;
        return _mappings.get(_name);
    }


    public String createMappingAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String type = (String) request.getParameter("type");

System.out.println("createMappingAction type: " + type);

        if (type == null) {

			String value = (String) request.getParameter("new");
			if (value != null && value.compareTo("true") == 0) return "new";

			String message = "Please specify the type of mapping by clicking on a radio button below.";
			request.getSession().setAttribute("message", message);
			return "message";
		} else if (type.compareTo("ncimeta") == 0) {
		    return "ncimeta";
		} else if (type.compareTo("codingscheme") == 0) {
		    return "codingscheme";
		} else if (type.compareTo("valueset") == 0) {
		    return "valueset";
		}
		return null;
	}


    public boolean codingSchemeSelected(String s) {
		if (s == null || s.compareTo("NOT SPECIFIED") == 0 || s.compareTo("") == 0 || s.compareTo("Select One") == 0) {
			return false;
		}
        return true;
	}



    public MappingObject createMappingObject(String type, String identifier, String version, String from_cs, String from_version, String to_cs,
                                             String to_version, List list) {

		MappingObject mapping_obj = getMapping(identifier + "|" + version);
		if (mapping_obj != null) return null;

        mapping_obj = new MappingObject();

        mapping_obj.setType(type);

        mapping_obj.setName(identifier);
        mapping_obj.setVersion(version);

        mapping_obj.setFromCS(from_cs);
        mapping_obj.setFromVersion(from_version);

        mapping_obj.setToCS(to_cs);

        if (type.compareTo("valueset") == 0) {
			mapping_obj.setValueSetDefinitionName(to_version);
		} else {
			mapping_obj.setToVersion(to_version);
		}

        mapping_obj.setStatus(_status);

        mapping_obj.setKey();

        mapping_obj.setData(list);

        return mapping_obj;
	}



    public String submitMetadataAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String message = null;
        String type = (String) request.getParameter("type");
System.out.println("submitMetadataAction type: " + type);



        if (type == null) {
			message = "Please specify the type of mapping by clicking on a radio button below.";
			request.getSession().setAttribute("message", message);
			return "message";
		} else if (type.compareTo("ncimeta") == 0) {
			String identifier = (String) request.getParameter("identifier");
			identifier = identifier.trim();
			if (identifier.compareTo("") == 0) {
				message = "Please provide an identifier for the mapping.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String mapping_version = (String) request.getParameter("mapping_version");
			mapping_version = mapping_version.trim();
			if (mapping_version.compareTo("") == 0) {
				message = "Please provide a version for the mapping.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String ncim_version = (String) request.getParameter("ncim_version");
			ncim_version = ncim_version.trim();
			if (ncim_version.compareTo("") == 0) {
				message = "Please specify the version of the NCI Metathesaurus.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String source_abbrev = (String) request.getParameter("source_abbrev");
			source_abbrev = source_abbrev.trim();
			/*
			if (source_abbrev.compareTo("") == 0) {
				message = "Please select a source.";
				request.getSession().setAttribute("message", message);
				return "message";
			}
			*/

			String target_abbrev = (String) request.getParameter("target_abbrev");
			target_abbrev = target_abbrev.trim();
			if (target_abbrev.compareTo("") == 0) {
				message = "Please select a target.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			if (source_abbrev.compareTo(target_abbrev) == 0) {
				message = "Source and target cannot be the same.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String input_option = (String) request.getParameter("input_option");
			if (input_option == null || input_option.compareTo("") == 0) {
				message = "Please select an input option.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("mapping_version", mapping_version);
			request.getSession().setAttribute("ncim_version", ncim_version);
			request.getSession().setAttribute("source_abbrev", source_abbrev);
			request.getSession().setAttribute("target_abbrev", target_abbrev);
			request.getSession().setAttribute("input_option", input_option);

HashMap mapping_hmap = new HashMap();
request.getSession().setAttribute("mapping_hmap", mapping_hmap);
HashSet expanded_hset = new HashSet();
request.getSession().setAttribute("expanded_hset", expanded_hset);


		    return "ncimeta";
		} else if (type.compareTo("codingscheme") == 0) {

			String identifier = (String) request.getParameter("identifier");
			identifier = identifier.trim();
			if (identifier.compareTo("") == 0) {
				message = "Please provide an identifier for the mapping.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String mapping_version = (String) request.getParameter("mapping_version");
			mapping_version = mapping_version.trim();
			if (mapping_version.compareTo("") == 0) {
				message = "Please provide a version for the mapping.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String source_cs = (String) request.getParameter("source_cs");
			source_cs = source_cs.trim();
			if (!codingSchemeSelected(source_cs)) {
				message = "Please select a source terminology.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String target_cs = (String) request.getParameter("target_cs");
			target_cs = target_cs.trim();
			if (!codingSchemeSelected(target_cs)) {
				message = "Please select a target terminology.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String input_option = (String) request.getParameter("input_option");
			if (input_option == null || input_option.compareTo("") == 0) {
				message = "Please select an input option.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("mapping_version", mapping_version);
			request.getSession().setAttribute("source_cs", source_cs);
			request.getSession().setAttribute("target_cs", target_cs);
			request.getSession().setAttribute("input_option", input_option);

HashMap mapping_hmap = new HashMap();
request.getSession().setAttribute("mapping_hmap", mapping_hmap);
HashSet expanded_hset = new HashSet();
request.getSession().setAttribute("expanded_hset", expanded_hset);

		    return "codingscheme";
		} else if (type.compareTo("valueset") == 0) {

			String identifier = (String) request.getParameter("identifier");
			identifier = identifier.trim();
			if (identifier.compareTo("") == 0) {
				message = "Please provide an identifier for the mapping.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String mapping_version = (String) request.getParameter("mapping_version");
			mapping_version = mapping_version.trim();
			if (mapping_version.compareTo("") == 0) {
				message = "Please provide a version for the mapping.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String source_cs = (String) request.getParameter("source_cs");
			source_cs = source_cs.trim();
			if (!codingSchemeSelected(source_cs)) {
				message = "Please select a source terminology.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String input_option = (String) request.getParameter("input_option");
			if (input_option == null || input_option.compareTo("") == 0) {
				message = "Please select an input option.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String vsdURI = (String) request.getParameter("vsdURI");
			if (vsdURI == null || vsdURI.compareTo("") == 0) {
				message = "Please specify a value set.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("mapping_version", mapping_version);
			request.getSession().setAttribute("source_cs", source_cs);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("vsdURI", vsdURI);

HashMap mapping_hmap = new HashMap();
request.getSession().setAttribute("mapping_hmap", mapping_hmap);
HashSet expanded_hset = new HashSet();
request.getSession().setAttribute("expanded_hset", expanded_hset);

            //request.getSession().setAttribute("mappings", _mappings);

		    return "valueset";
		}
		return null;
	}

    public String generateAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String type = (String) request.getParameter("type");
System.out.println("submitMetadataAction type: " + type);

        if (type == null) {
			String message = "Please specify the type of mapping by clicking on a radio button below.";
			request.getSession().setAttribute("message", message);
			return "message";
		} else if (type.compareTo("ncimeta") == 0) {
			String identifier = (String) request.getParameter("identifier");
			String ncim_version = (String) request.getParameter("ncim_version");
			String source_abbrev = (String) request.getParameter("source_abbrev");
			String target_abbrev = (String) request.getParameter("target_abbrev");
			String input_option = (String) request.getParameter("input_option");

			String algorithm = (String) request.getParameter("algorithm");

			String codes = (String) request.getParameter("codes");
			if (codes == null) {
				codes = "";
			}
			codes = codes.trim();
			if (codes.compareTo("") == 0) {
				String message = "No data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String lines[] = codes.split("\\n");
			for(int i = 0; i < lines.length; i++) {
				String t = lines[i];
				System.out.println(t);
			}

            List list = null;
			list = MappingUtils.process_ncimeta_mapping(ncim_version,
                                        source_abbrev,
                                        target_abbrev,
                                        input_option,
                                        algorithm,
                                        lines);


			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("ncim_version", ncim_version);
			request.getSession().setAttribute("source_abbrev", source_abbrev);
			request.getSession().setAttribute("target_abbrev", target_abbrev);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("results", list);
			request.getSession().setAttribute("algorithm", algorithm);

			if (list == null) {
				String message = "No match";
				request.getSession().setAttribute("message", message);
				return "message";
			}


		    return "ncimeta";

		} else if (type.compareTo("codingscheme") == 0) {
			String identifier = (String) request.getParameter("identifier");
			String source_cs = (String) request.getParameter("source_cs");
			String target_cs = (String) request.getParameter("target_cs");
			String property = (String) request.getParameter("property");
			String input_option = (String) request.getParameter("input_option");

			String algorithm = (String) request.getParameter("algorithm");

			String codes = (String) request.getParameter("codes");
			if (codes == null) {
				codes = "";
			}
			codes = codes.trim();
			if (codes.compareTo("") == 0) {
				String message = "No data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String lines[] = codes.split("\\n");
			for(int i = 0; i < lines.length; i++) {
				String t = lines[i];
				if (t.indexOf("|") == -1) {
					String message = "Input format error -- each line should contains a code, a bar '|' delimiter, and a value";
					request.getSession().setAttribute("message", message);
					return "message";
				}
				System.out.println(t);
			}

            List list = null;
			list = MappingUtils.process_codingscheme_mapping(
                                        source_cs,
                                        target_cs,
                                        input_option,
                                        property,
                                        algorithm,
                                        lines);

			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("source_cs", source_cs);
			request.getSession().setAttribute("target_cs", target_cs);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("results", list);
			request.getSession().setAttribute("algorithm", algorithm);

			if (list == null) {
				String message = "No match";
				request.getSession().setAttribute("message", message);
				return "message";
			}


		    return "codingscheme";
		} else if (type.compareTo("valueset") == 0) {
		    return "valueset";
		}
		return null;
	}



    public String showFormAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String type = (String) request.getParameter("type");
System.out.println("submitMetadataAction type: " + type);

        if (type == null) {
			String message = "Please specify the type of mapping by clicking on a radio button below.";
			request.getSession().setAttribute("message", message);
			return "message";
		} else if (type.compareTo("ncimeta") == 0) {
			String identifier = (String) request.getParameter("identifier");
			String ncim_version = (String) request.getParameter("ncim_version");
			String source_abbrev = (String) request.getParameter("source_abbrev");
			String target_abbrev = (String) request.getParameter("target_abbrev");
			String input_option = (String) request.getParameter("input_option");

			String algorithm = (String) request.getParameter("algorithm");

			String codes = (String) request.getParameter("codes");
			if (codes == null) {
				codes = "";
			}
			codes = codes.trim();
			if (codes.compareTo("") == 0) {
				String message = "No data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			List list = new ArrayList();

			String lines[] = codes.split("\\n");
			for(int i = 0; i < lines.length; i++) {
				String t = lines[i];

				t = t.trim();

				//System.out.println("(DATA) " + t + " length: " + t.length());
				/*
				if (t.length() > 1 && i < lines.length-1) {
					t = t.substring(0, t.length()-1);
				}
				*/
				list.add(t);
			}

/*
            List list = null;
			list = MappingUtils.process_ncimeta_mapping(ncim_version,
                                        source_abbrev,
                                        target_abbrev,
                                        input_option,
                                        algorithm,
                                        lines);


			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("ncim_version", ncim_version);
			request.getSession().setAttribute("source_abbrev", source_abbrev);
			request.getSession().setAttribute("target_abbrev", target_abbrev);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("results", list);
			request.getSession().setAttribute("algorithm", algorithm);

			if (list == null) {
				String message = "No match";
				request.getSession().setAttribute("message", message);
				return "message";
			}

*/
			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("ncim_version", ncim_version);
			request.getSession().setAttribute("source_abbrev", source_abbrev);
			request.getSession().setAttribute("target_abbrev", target_abbrev);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("data", list);
			request.getSession().setAttribute("algorithm", algorithm);

		    return "ncimeta";

		} else if (type.compareTo("codingscheme") == 0) {
			String identifier = (String) request.getParameter("identifier");
			String source_cs = (String) request.getParameter("source_cs");
			String target_cs = (String) request.getParameter("target_cs");
			String property = (String) request.getParameter("property");
			String input_option = (String) request.getParameter("input_option");

			String algorithm = (String) request.getParameter("algorithm");

			String codes = (String) request.getParameter("codes");
			if (codes == null) {
				codes = "";
			}
			codes = codes.trim();
			if (codes.compareTo("") == 0) {
				String message = "No data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			String lines[] = codes.split("\\n");
			for(int i = 0; i < lines.length; i++) {
				String t = lines[i];
				if (t.indexOf("|") == -1) {
					String message = "Input format error -- each line should contains a code, a bar '|' delimiter, and a value";
					request.getSession().setAttribute("message", message);
					return "message";
				}
				System.out.println(t);
			}

            List list = null;
			list = MappingUtils.process_codingscheme_mapping(
                                        source_cs,
                                        target_cs,
                                        input_option,
                                        property,
                                        algorithm,
                                        lines);

			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("source_cs", source_cs);
			request.getSession().setAttribute("target_cs", target_cs);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("results", list);
			request.getSession().setAttribute("algorithm", algorithm);

			if (list == null) {
				String message = "No match";
				request.getSession().setAttribute("message", message);
				return "message";
			}


		    return "codingscheme";
		} else if (type.compareTo("valueset") == 0) {
		    return "valueset";
		}
		return null;
    }




    public String saveMappingAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		List input_list = (ArrayList) request.getSession().getAttribute("selection_list");
		List selected_matches = new ArrayList();

        String[] selected_list = request.getParameterValues("selected_list");

        if (selected_list == null) {
			selected_list = new String[] {"0"};
		}


        for (int i=0; i<selected_list.length; i++) {
			String s = selected_list[i];
			int k = Integer.parseInt(s);
			MappingData mappingData = (MappingData) input_list.get(k);
			selected_matches.add(mappingData);
		}

		System.out.println("saveMappingAction selected_matches: " + selected_matches.size());
		String data_value = (String) request.getParameter("data_value");
		System.out.println("saveMappingAction data_value: " + data_value);

		HashMap mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");
		if (mapping_hmap == null) {
			mapping_hmap = new HashMap();
			request.getSession().setAttribute("mapping_hmap", mapping_hmap);
		}
		if (data_value != null && selected_matches.size() > 0) {
			data_value = data_value.trim();
			System.out.println("*** saveMappingAction mapping_hmap.put " + data_value + " matches " + selected_matches.size() + " data_value.length(): " + data_value.length());
			mapping_hmap.put((String)data_value, selected_matches);
		}

		 //List selected_matches = null;
		 Iterator it = mapping_hmap.keySet().iterator();
		 while (it.hasNext()) {
			String key = (String) it.next();

			System.out.println("\n(***) saveMappingAction key: " + key);
			selected_matches = (ArrayList) mapping_hmap.get((String)key);

			if (selected_matches != null) {
				System.out.println("\n(***) saveMappingAction selected_matches.size(): " + selected_matches.size());
			}

		 }


		request.getSession().setAttribute("mapping_hmap", mapping_hmap);
        return "ncimeta";
	}

    public String saveCommentAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String message = "Comment saved successfully.";
		request.getSession().setAttribute("message", message);

        return "ncimeta";
	}

    public String ncimetaSearchAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();


        return "ncimeta";
	}






    public String showBatchFormAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String type = (String) request.getParameter("type");

        String mapping_version = (String) request.getParameter("mapping_version");


        request.getSession().setAttribute("type", type);

        if (type == null) {
			String message = "Please specify the type of mapping by clicking on a radio button below.";
			request.getSession().setAttribute("message", message);
			return "message";
		} else if (type.compareTo("ncimeta") == 0) {
			String identifier = (String) request.getParameter("identifier");
			//String mapping_version = (String) request.getParameter("mapping_version");
			String ncim_version = (String) request.getParameter("ncim_version");
			String source_abbrev = (String) request.getParameter("source_abbrev");
			String target_abbrev = (String) request.getParameter("target_abbrev");
			String input_option = (String) request.getParameter("input_option");

			String algorithm = (String) request.getParameter("algorithm");

			String codes = (String) request.getParameter("codes");
			if (codes == null) {
				codes = "";
			}
			codes = codes.trim();
			if (codes.compareTo("") == 0) {
				String message = "No data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

			List list = new ArrayList();

			String lines[] = codes.split("\\n");
			for(int i = 0; i < lines.length; i++) {
				String t = lines[i];
				t = t.trim();
				list.add(t);
			}

			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("ncim_version", ncim_version);
			request.getSession().setAttribute("source_abbrev", source_abbrev);
			request.getSession().setAttribute("target_abbrev", target_abbrev);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("data", list);
			request.getSession().setAttribute("algorithm", algorithm);
			request.getSession().setAttribute("mapping_version", mapping_version);


            MappingObject mappingObj = createMappingObject(type, identifier, mapping_version, source_abbrev, null, target_abbrev, null, list);
            if (mappingObj == null) {
				String message = "Unable to create mapping -- mapping with the same identifier and version already exists.";
				request.getSession().setAttribute("message", message);
				return "message";
			}
			addMapping(mappingObj);
			request.getSession().setAttribute("mappings", _mappings);

		    return type;//"ncimeta";

		} else if (type.compareTo("codingscheme") == 0) {
			String identifier = (String) request.getParameter("identifier");
			//String mapping_version = (String) request.getParameter("mapping_version");
			String source_cs = (String) request.getParameter("source_cs");
			String target_cs = (String) request.getParameter("target_cs");
			String src_property = (String) request.getParameter("src_property");
			String target_property = (String) request.getParameter("target_property");

			String left_trim = (String) request.getParameter("left_trim");
			String right_trim = (String) request.getParameter("right_trim");
			String prefix = (String) request.getParameter("prefix");
			String suffix = (String) request.getParameter("suffix");

			String input_option = (String) request.getParameter("input_option");

			String algorithm = (String) request.getParameter("algorithm");

			String codes = (String) request.getParameter("codes");
			if (codes == null) {
				codes = "";
			}
			codes = codes.trim();
			if (codes.compareTo("") == 0) {
				String message = "No data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}
			List list = new ArrayList();

			String lines[] = codes.split("\\n");
			for(int i = 0; i < lines.length; i++) {
				String t = lines[i];
				t = t.trim();
				if (input_option.compareToIgnoreCase("Code") != 0) {
					if (t.indexOf("|") == -1) {
						String message = "Input format error -- each line should contains a code, a bar '|' delimiter, and a value";
						request.getSession().setAttribute("message", message);
						return "message";
					}
			    }
				list.add(t);
			}

			if (input_option.compareToIgnoreCase("Code") == 0) {
				String source_scheme = DataUtils.key2CodingSchemeName(source_cs);
				String source_version = DataUtils.key2CodingSchemeVersion(source_cs);

				HashMap code2name_hmap = DataUtils.code2Name(source_scheme, source_version, list);
				request.getSession().setAttribute("code2name_hmap", code2name_hmap);
			}

			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("source_cs", source_cs);
			request.getSession().setAttribute("target_cs", target_cs);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("data", list);
			request.getSession().setAttribute("property", target_property);
			request.getSession().setAttribute("src_property", src_property);

			request.getSession().setAttribute("target_property", target_property);
			request.getSession().setAttribute("left_trim", left_trim);
			request.getSession().setAttribute("right_trim", right_trim);
			request.getSession().setAttribute("prefix", prefix);
			request.getSession().setAttribute("suffix", suffix);

			request.getSession().setAttribute("algorithm", algorithm);
			request.getSession().setAttribute("mapping_version", mapping_version);


			if (list == null) {
				String message = "No mapping data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}


String source_scheme = DataUtils.key2CodingSchemeName(source_cs);
String source_version = DataUtils.key2CodingSchemeVersion(source_cs);

String target_scheme = DataUtils.key2CodingSchemeName(target_cs);
String target_version = DataUtils.key2CodingSchemeVersion(target_cs);

System.out.println("Creating MappingObject ...codingscheme");

            MappingObject mappingObj = createMappingObject(type, identifier, mapping_version, source_scheme, source_version, target_scheme, target_version, list);
            if (mappingObj == null) {
				String message = "Unable to create mapping -- mapping with the same identifier and version already exists.";
				request.getSession().setAttribute("message", message);
				return "message";
			}
			addMapping(mappingObj);
			request.getSession().setAttribute("mappings", _mappings);

		    return "codingscheme";
		} else if (type.compareTo("valueset") == 0) {


			String valueSetDefinitionName = (String) request.getParameter("valueSetDefinitionName");


			String identifier = (String) request.getParameter("identifier");
			//String mapping_version = (String) request.getParameter("mapping_version");
			String source_cs = (String) request.getParameter("source_cs");
			String property = (String) request.getParameter("property");
			String vsdURI = (String) request.getParameter("vsdURI");
			String input_option = (String) request.getParameter("input_option");

			String algorithm = (String) request.getParameter("algorithm");

			String codes = (String) request.getParameter("codes");
			request.getSession().setAttribute("codes", codes);


			if (codes == null) {
				codes = "";
			}
			codes = codes.trim();
			if (codes.compareTo("") == 0) {
				String message = "No data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}
			List list = new ArrayList();

			String lines[] = codes.split("\\n");
			for(int i = 0; i < lines.length; i++) {
				String t = lines[i];
				t = t.trim();
				if (input_option.compareToIgnoreCase("Code") != 0) {
					if (t.indexOf("|") == -1) {
						String message = "Input format error -- each line should contains a code, a bar '|' delimiter, and a value";
						request.getSession().setAttribute("message", message);
						return "message";
					}
			    }
				list.add(t);
			}

			if (input_option.compareToIgnoreCase("Code") == 0) {
				String source_scheme = DataUtils.key2CodingSchemeName(source_cs);
				String source_version = DataUtils.key2CodingSchemeVersion(source_cs);

				HashMap code2name_hmap = DataUtils.code2Name(source_scheme, source_version, list);
				request.getSession().setAttribute("code2name_hmap", code2name_hmap);
			}



Vector cs_uri_vec = DataUtils.getCodingSchemesInValueSetDefinition(vsdURI);
if (cs_uri_vec == null) {
			String msg = "WARNING: No coding scheme version reference is available.";
			request.getSession().setAttribute("message", msg);
			return "message";
}

AbsoluteCodingSchemeVersionReferenceList acsvrl = new AbsoluteCodingSchemeVersionReferenceList();
Vector cs_name_vec = DataUtils.getCodingSchemeURNsInValueSetDefinition(vsdURI);
AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
Vector ref_vec = new Vector();
String key = vsdURI;


for (int i=0; i<cs_uri_vec.size(); i++) {
	String cs_uri = (String) cs_uri_vec.elementAt(i);
	String cs_name = DataUtils.uri2CodingSchemeName(cs_uri);
	String version = (String) request.getParameter(cs_name);

	System.out.println("cs_name: " + cs_name);
	System.out.println("version: " + version);

	if (version != null) {
		//acsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(cs_name, version));
		acsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(cs_uri, version));
		ref_vec.add(cs_name + "$" + version);
		key = key + "|" + cs_name + "$" + version;
	}
}
request.getSession().setAttribute("acsvrl", acsvrl);


			request.getSession().setAttribute("identifier", identifier);
			request.getSession().setAttribute("mapping_version", mapping_version);
			request.getSession().setAttribute("source_cs", source_cs);
			request.getSession().setAttribute("input_option", input_option);
			request.getSession().setAttribute("data", list);
			request.getSession().setAttribute("property", property);
			request.getSession().setAttribute("algorithm", algorithm);
			request.getSession().setAttribute("vsdURI", vsdURI);
			request.getSession().setAttribute("valueSetDefinitionName", valueSetDefinitionName);



			if (list == null) {
				String message = "No mapping data has been entered.";
				request.getSession().setAttribute("message", message);
				return "message";
			}

String source_scheme = DataUtils.key2CodingSchemeName(source_cs);
String source_version = DataUtils.key2CodingSchemeVersion(source_cs);

System.out.println("Creating MappingObject ... valueset ");

            MappingObject mappingObj = createMappingObject(type, identifier, mapping_version, source_scheme, source_version, vsdURI, valueSetDefinitionName, list);
            if (mappingObj == null) {
				String message = "Unable to create mapping -- mapping with the same identifier and version already exists.";
				request.getSession().setAttribute("message", message);
				return "message";
			}
			addMapping(mappingObj);
			request.getSession().setAttribute("mappings", _mappings);
		    return "valueset";
		}
		return null;
    }



    public String submitBatchAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String identifier = request.getParameter("identifier");
        String mapping_version = request.getParameter("mapping_version");

        String id = MappingObject.computeKey(identifier, mapping_version);
        System.out.println("MappingBean id = " + id);
        request.getSession().setAttribute("id", id);

		if (id != null) {
			MappingObject obj = (MappingObject) _mappings.get(id);
			if (obj != null) {
				List input_list = (ArrayList) request.getSession().getAttribute("data");
				obj.setData(input_list);
				_mappings.put(id, obj);
				request.getSession().setAttribute("mappings", _mappings);
			} else {
				System.out.println("MappingBean MappingObject with id NOT FOUND ???");
			}
		}

		String source_cs = (String) request.getParameter("source_cs");
		if (source_cs != null) {
			request.getSession().setAttribute("source_cs", source_cs);
		}

        String type = null;
        HashMap mapping_hmap = null;
        String algorithm = null;
        String advanced = null;

		String src_property = null;
		String target_property = null;
		String left_trim = null;
		String right_trim = null;
		String prefix = null;
		String suffix = null;
		String input_option = null;

		//String source_cs = null;

        synchronized (request.getSession()) {
			type = (String) request.getParameter("type");
			input_option = (String) request.getParameter("input_option");

			mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");
			if (mapping_hmap == null) {
				mapping_hmap = new HashMap();
				request.getSession().setAttribute("mapping_hmap", mapping_hmap);
			}

			algorithm = (String) request.getParameter("algorithm");
			if (algorithm != null) {
				request.getSession().setAttribute("algorithm", algorithm);
			}

			advanced = (String) request.getParameter("advanced");
			if (advanced != null) {
					src_property = (String) request.getParameter("src_property");
					target_property = (String) request.getParameter("target_property");
					left_trim = (String) request.getParameter("left_trim");
					right_trim = (String) request.getParameter("right_trim");
					prefix = (String) request.getParameter("prefix");
					suffix = (String) request.getParameter("suffix");
					request.getSession().setAttribute("src_property", src_property);
					request.getSession().setAttribute("target_property", target_property);
					request.getSession().setAttribute("left_trim", left_trim);
					request.getSession().setAttribute("right_trim", right_trim);
					request.getSession().setAttribute("prefix", prefix);
					request.getSession().setAttribute("suffix", suffix);
					request.getSession().setAttribute("input_option", input_option);
			}

		}

		Runnable runnable = null;

		if (advanced != null) {
			runnable = new MappingThread(request, mapping_hmap, type, true);
		} else {
			if (type.compareTo("valueset") == 0) {
				String vsdURI = (String) request.getParameter("vsdURI");

				System.out.println("submitBatchAction vsdURI " + vsdURI);

				String valueSetDefinitionRevisionId = null;
				AbsoluteCodingSchemeVersionReferenceList csVersionList = (AbsoluteCodingSchemeVersionReferenceList) request.getSession().getAttribute("acsvrl");
				String csVersionTag = null;

				runnable = new MappingThread(request, mapping_hmap, type, vsdURI, valueSetDefinitionRevisionId, csVersionList, csVersionTag);
			} else {
				runnable = new MappingThread(request, mapping_hmap, type);
			}
	    }

		// Create the thread supplying it with the runnable object
		Thread thread = new Thread(runnable);

		// Start the thread
		thread.start();
        //int error_code = processBatchSubmission(request);

        synchronized (request.getSession()) {
        	request.getSession().setAttribute("refresh", "on");
	    }

		return type;
	}


    public String saveAllMappingAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();


		return "ncimeta";
	}

    public String refreshFormAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String type = (String) request.getParameter("type");
        request.getSession().setAttribute("type", type);
		return type;
	}

    public String manualMappingAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String type = (String) request.getParameter("type");
		String target_code = (String) request.getParameter("target_code");

		request.getSession().setAttribute("target_code", target_code);

		return type;
	}

    public String saveManualMappingAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String type = (String) request.getParameter("type");

		String source_scheme = (String) request.getParameter("source_scheme");
		String source_version = (String) request.getParameter("source_version");
		String source_code = (String) request.getParameter("source_code");
		String source_name = (String) request.getParameter("source_name");

		String source_namespace = (String) request.getParameter("source_namespace");
		String target_scheme = (String) request.getParameter("target_scheme");
		String target_version = (String) request.getParameter("target_version");
		String target_code = (String) request.getParameter("target_code");
		String target_name = (String) request.getParameter("target_name");

		String target_namespace = (String) request.getParameter("target_namespace");

        String idx1_str = (String) request.getSession().getAttribute("idx1_str");

        String associationName = "mapsTo";
        String rel = null;
        int score = 0;

		MappingData mappingData = new MappingData(
			source_code,
			source_name,
			source_scheme,
			source_version,
			source_namespace,
			associationName,
			rel,
			score,
			target_code,
			target_name,
			target_scheme,
			target_version,
			target_namespace);


		synchronized (request.getSession()) {
			String message = null;
			List list = (ArrayList) request.getSession().getAttribute("data");
			int idx = Integer.parseInt(idx1_str);
			String input_data = (String) list.get(idx);

			HashMap mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");

			List selected_matches = (ArrayList) mapping_hmap.get(input_data);
			if (selected_matches == null) {
				selected_matches = new ArrayList();
			}

            boolean found = searchMappingData(selected_matches, mappingData.getKey());
			if (!found) {
				selected_matches.add(mappingData);
			    mapping_hmap.put(input_data, selected_matches);
			    request.getSession().setAttribute("mapping_hmap", mapping_hmap);
				message = "Mapping entry saved successfully.";
				request.getSession().setAttribute("message", message);

			} else {
				message = "Mapping entry already exists -- data not saved.";
				request.getSession().setAttribute("message", message);
			}
	    }
		return type;
	}


    public boolean searchMappingData(List match_list, String search_key) {
		if (match_list == null) return false;
		for (int i=0; i<match_list.size(); i++) {
			MappingData mappingData = (MappingData) match_list.get(i);
			String key = mappingData.getKey();
			if (search_key.compareTo(key) == 0) {
                return true;
			}
		}
		return false;
	}



    public String advancedSearchAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String type = (String) request.getParameter("type");
		return type;
	}


    public String removeMappingAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String type = (String) request.getParameter("type");

        List list = (ArrayList) request.getSession().getAttribute("data");
	    for (int lcv=0; lcv<list.size(); lcv++) {
			String idx1_str = new Integer(lcv).toString();
			String checkbox_name = "checkbox" + idx1_str;
			String[] selected_list = request.getParameterValues(checkbox_name);
			if (selected_list != null && selected_list.length > 0) {
				String input_data = (String) list.get(lcv);
				System.out.println("Deleting record for " + input_data);

				HashMap mapping_hmap = (HashMap) request.getSession().getAttribute("mapping_hmap");
                HashSet hset = new HashSet();
				for (int i=0; i<selected_list.length; i++) {
					String s = selected_list[i];
					System.out.println(s);
					hset.add(s);
				}
                List mapping_list = (ArrayList) mapping_hmap.get(input_data);
                List new_mapping_list = new ArrayList();
                if (mapping_list != null && mapping_list.size()>0) {
				    for (int lcv2=0; lcv2<mapping_list.size(); lcv2++) {
  					     String idx2_str = new Integer(lcv2).toString();
  					     if (!hset.contains(idx2_str)) {
							 MappingData mappingData = (MappingData) mapping_list.get(lcv2);
							 new_mapping_list.add(mappingData);
						 }
					}
					if (new_mapping_list.size() > 0) {
						 mapping_hmap.put(input_data, new_mapping_list);
					} else {
						 mapping_hmap.remove(input_data);
					}
				}
			}
		}
		return type;
	}


    public String searchAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String type = (String) request.getParameter("type");
		String targetCodingScheme = (String) request.getParameter("target_scheme");
		String targetCodingSchemeVersion = (String) request.getParameter("target_version");
		String algorithm = (String) request.getParameter("algorithm");
		String target_property = (String) request.getParameter("target_property");
		String matchText = (String) request.getParameter("input");


System.out.println("targetCodingScheme: " + targetCodingScheme);
System.out.println("targetCodingSchemeVersion: " + targetCodingSchemeVersion);
System.out.println("target_property: " + target_property);
System.out.println("algorithm: " + algorithm);
System.out.println("matchText: " + matchText);



		Vector matchText_vec = new Vector();
		matchText_vec.add(matchText);

        ResolvedConceptReferencesIterator iterator = new SearchUtils().searchByProperty(
							        targetCodingScheme, targetCodingSchemeVersion, target_property, matchText_vec, algorithm);

		if (iterator == null) {
			String msg = "No match";
			System.out.println("iterator == null??? ");
			request.getSession().setAttribute("message", msg);
			return "nomatch";
		}
		try {
			int numRemaining = iterator.numberRemaining();
			System.out.println("Number of matches: " + numRemaining);
			if (numRemaining == 0) {
				String msg = "No match";
				request.getSession().setAttribute("message", msg);
				return "nomatch";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		request.getSession().setAttribute("match_results", iterator);
		return type;
	}


    public String deleteMappingAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
        String mapping_id = (String) request.getParameter("selected_mapping");

        if (mapping_id == null) {
			System.out.println("deleteMappingAction mapping_id == null???: ");
			return "delete";
		}

        Iterator it = mappings.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			MappingObject obj = (MappingObject) mappings.get(key);
			String obj_id = obj.getKey();
			if (obj_id.compareTo(mapping_id) == 0) {
				mappings.remove(obj_id);
				request.getSession().setAttribute("mappings", mappings);
				break;
			}
		}

		return "delete";
	}

    public String cloneMappingAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        HashMap mappings = (HashMap) request.getSession().getAttribute("mappings");
        String mapping_id = (String) request.getParameter("selected_mapping");

        System.out.println("cloneMappingAction mapping_id: " + mapping_id);

        if (mapping_id == null) {
			System.out.println("cloneMappingAction mapping_id == null???: ");
			return "clone";
		}

        Iterator it = mappings.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			MappingObject obj = (MappingObject) mappings.get(key);
			String obj_id = obj.getKey();
			if (obj_id.compareTo(mapping_id) == 0) {
		    // to be implemented

System.out.println("cloneMappingAction cloning ..." + mapping_id);

				MappingObject clone = (MappingObject) obj.clone();
				clone.setName("copy of " + obj.getName());
				//String clone_key = MappingObject.computeKey(clone.getName(), clone.getVersion());
				clone.setKey();

System.out.println("cloneMappingAction clone.getKey() ..." + clone.getKey());

				mappings.put(clone.getKey(), clone);

				request.getSession().setAttribute("mappings", mappings);
				break;
			}
		}

System.out.println("cloneMappingAction exiting ...");

		return "clone";
	}



}
