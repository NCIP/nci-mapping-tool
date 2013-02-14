package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.mapping.*;

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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;


    public class MappingObject implements Cloneable {

		private static String NULL_STRING = "NULL";
	    private static int NULL_STRING_HASH_CODE = NULL_STRING.hashCode();

	    private String _type = null;

	    private String _name = null;

        private String _version = null;
        private String _description = null;
        private String _from_cs = null;
        private String _from_version = null;
        private String _to_cs = null;
        private String _to_version = null;
        private String _from = null;
        private String _to = null;

        private String _ncim_version = null;

        private String _vsdURI = null;
        private String _valueSetDefinitionName = null;

        private int _uuid = 0;
        private String _key = null;

        private HtmlSelectBooleanCheckbox _checkbox = null;
		private String _status = null;
		private HashMap _mapping_hmap = new HashMap();
		private HashMap _status_hmap = new HashMap();
		private List _data = new ArrayList();

		private String _creation_date = null;


        /**
         * Constructor
         */
        public MappingObject() {

		}

        public void setNCIMVersion(String ncim_version) {
			this._ncim_version = ncim_version;
		}

		public String getNCIMVersion() {
			return this._ncim_version;
		}

        public void setType(String type) {
			this._type = type;
		}

		public String getType() {
			return this._type;
		}


        public void setCreationDate(String creationDate) {
			this._creation_date = creationDate;
		}

		public String getCreationDate() {
			return this._creation_date;
		}


        public void setVsdURI(String vsdURI) {
			this._vsdURI = vsdURI;
		}

		public String getVsdURI() {
			return this._vsdURI;
		}


        public void setValueSetDefinitionName(String valueSetDefinitionName) {
			this._valueSetDefinitionName = valueSetDefinitionName;
		}


        public String getValueSetDefinitionName() {
			return this._valueSetDefinitionName;
		}


		public void setMappingHashMap(HashMap mapping_hmap) {
			this._mapping_hmap = mapping_hmap;
		}

		public HashMap getMappingHashMap() {
			return this._mapping_hmap;
		}

		public void setStatusHashMap(HashMap status_hmap) {
			this._status_hmap = status_hmap;
		}

		public HashMap getStatusHashMap() {
			return this._status_hmap;
		}

		public void setData(List data) {
			this._data = data;
		}

		public List getData() {
			return this._data;
		}

		public static String computeKey(String name, String version) {
			int uuid = 0;

			if(name != null){
				uuid += name.hashCode();
			} else {
				uuid += NULL_STRING_HASH_CODE;
			}

			if(version != null){
				uuid += version.hashCode();
			} else {
				uuid += NULL_STRING_HASH_CODE;
			}

			String key = null;
			if (uuid < 0) {
				uuid = uuid * (-1);
				key = "MN" + Integer.valueOf(uuid).toString();
			} else {
			    key = "M" + Integer.valueOf(uuid).toString();
			}
			return key;

		}

		public void setKey(String key) {
			_key = key;
		}


		public void setKey() {
			_key = computeKey(_name, _version);

			/*
			_uuid = 0;

			if(_name != null){
				_uuid += _name.hashCode();
			} else {
				_uuid += NULL_STRING_HASH_CODE;
			}

			if(_version != null){
				_uuid += _version.hashCode();
			} else {
				_uuid += NULL_STRING_HASH_CODE;
			}
			_key = "M" + Integer.valueOf(_uuid).toString();
			*/

		}

		public String getKey() {
			return _key;
		}

		public String getDescription() {
			return _description;
		}

		public void setDescription(String description) {
			_description = description;
		}


		public String getStatus() {
			return _status;
		}

		public void setStatus(String status) {
			_status = status;
		}


        // Getters & setters
		public String getName() {
			return _name;
		}
		public void setName(String name) {
			_name = name;
		}

		public String getVersion() {
			return _version;
		}
		public void setVersion(String version) {
			_version = version;
		}

		public String getFromCS() {
			return _from_cs;
		}
		public void setFromCS(String from_cs) {
			_from_cs = from_cs;
		}

		public String getFromVersion() {
			return _from_version;
		}
		public void setFromVersion(String from_version) {
			_from_version = from_version;
		}

		public String getToCS() {
			return _to_cs;
		}
		public void setToCS(String to_cs) {
			_to_cs = to_cs;
		}

		public String getToVersion() {
			return _to_version;
		}
		public void setToVersion(String to_version) {
			_to_version = to_version;
		}


        // *** Private Methods ***

        private boolean getSelected() {
            return _checkbox.isSelected();
        }


        public List<String> cloneData() {
			List<String> cloned_list = new ArrayList<String>(_data.size());
			for (int i=0; i<_data.size(); i++) {
				String t = (String) _data.get(i);
				cloned_list.add(t);
			}
            return cloned_list;
        }


        public Object clone() {
            MappingObject obj = new MappingObject();
            obj.setType(this._type);

            obj.setName("copy of " + this._name);
            obj.setVersion(this._version);

            obj.setFromCS(this._from_cs);
            obj.setFromVersion(this._from_version);

            obj.setToCS(this._to_cs);
            obj.setToVersion(this._to_version);

            obj.setNCIMVersion(this._ncim_version);

            obj.setVsdURI(this._vsdURI);
            obj.setValueSetDefinitionName(this._valueSetDefinitionName);

            obj.setData(cloneData());


            obj.setMappingHashMap((HashMap) _mapping_hmap.clone());

           // to be modified

            return obj;
        }

        public String toXML() {
           String m_description = this._name + " (version: " + this._version + ")";

           List<MappingElement> mappingElements = new ArrayList();

           for (int k=0; k<_data.size(); k++) {
			   String input_data = (String) _data.get(k);

			   List<MappingEntry> entries = new ArrayList();
			   List selected_matches = (ArrayList) _mapping_hmap.get(input_data);

			   if (selected_matches != null) {
				   for (int lcv2=0; lcv2<selected_matches.size(); lcv2++) {
						 MappingData mappingData = (MappingData) selected_matches.get(lcv2);

						 String source_code = mappingData.getSourceCode();
						 String source_name = mappingData.getSourceName();
						 String source_namespace = mappingData.getSourceCodeNamespace();

                         String associationName = "mapsTo";
						 String rel = mappingData.getRel();
						 if (DataUtils.isNull(rel)) rel = "";
						 int score = mappingData.getScore();
						 String target_code = mappingData.getTargetCode();
						 String target_name = mappingData.getTargetName();
						 String target_namespace = mappingData.getTargetCodeNamespace();

						 String source_scheme = mappingData.getSourceCodingScheme();
						 String source_version = mappingData.getSourceCodingSchemeVersion();
						 String target_scheme = mappingData.getTargetCodingScheme();
						 String target_version = mappingData.getTargetCodingSchemeVersion();

						 source_scheme = DataUtils.getFormalName(source_scheme);
						 target_scheme = DataUtils.getFormalName(target_scheme);

						 MappingEntry mappingEntry = new MappingEntry(
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

                         mappingEntry.setComment(mappingData.getComment());
                         entries.add(mappingEntry);
					 }
				 }

                 String status = null;
                 if (_status_hmap.containsKey(input_data)) {
	                 status = (String) _status_hmap.get(input_data);
				 }
				 MappingElement element = new MappingElement(input_data, status, entries);
				 mappingElements.add(element);
			}


		    Mapping mapping = new Mapping(
				this._type,
				this._name,
				this._version,
				m_description,
				this._from_cs,
				this._from_version,
				this._to_cs,
				this._to_version,
				this._ncim_version,
				this._vsdURI,
				this._valueSetDefinitionName,
				this._creation_date,
				this._uuid,
				this._status,
				mappingElements);

			XStream xstream_xml = new XStream(new DomDriver());
			String xml = xstream_xml.toXML(mapping);
			return xml;
		}



        public String toXML(Vector entry_status_vec) {
		   if (entry_status_vec == null || entry_status_vec.size() == 0) {
			   return toXML();
		   }


           String m_description = this._name + " (version: " + this._version + ")";

           List<MappingElement> mappingElements = new ArrayList();

           for (int k=0; k<_data.size(); k++) {
			   String input_data = (String) _data.get(k);

			   List<MappingEntry> entries = new ArrayList();
			   List selected_matches = (ArrayList) _mapping_hmap.get(input_data);

			   if (selected_matches != null) {
				   for (int lcv2=0; lcv2<selected_matches.size(); lcv2++) {
						 MappingData mappingData = (MappingData) selected_matches.get(lcv2);

						 if (entry_status_vec.contains(mappingData.getStatus())) {

							 String source_code = mappingData.getSourceCode();
							 String source_name = mappingData.getSourceName();
							 String source_namespace = mappingData.getSourceCodeNamespace();

							 String associationName = "mapsTo";
							 String rel = mappingData.getRel();
							 if (DataUtils.isNull(rel)) rel = "";
							 int score = mappingData.getScore();
							 String target_code = mappingData.getTargetCode();
							 String target_name = mappingData.getTargetName();
							 String target_namespace = mappingData.getTargetCodeNamespace();

							 String source_scheme = mappingData.getSourceCodingScheme();
							 String source_version = mappingData.getSourceCodingSchemeVersion();
							 String target_scheme = mappingData.getTargetCodingScheme();
							 String target_version = mappingData.getTargetCodingSchemeVersion();

							 String status = mappingData.getStatus();
							 String comment = mappingData.getComment();
							 if (DataUtils.isNull(comment)) {
								 comment = "";
							 }

							 source_scheme = DataUtils.getFormalName(source_scheme);
							 target_scheme = DataUtils.getFormalName(target_scheme);

							 MappingEntry mappingEntry = new MappingEntry(
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
								target_namespace,
								status,
								comment);

							 mappingEntry.setComment(mappingData.getComment());
							 entries.add(mappingEntry);
					     }
					 }
				 }

                 String status = null;
                 if (_status_hmap.containsKey(input_data)) {
	                 status = (String) _status_hmap.get(input_data);
				 }
				 MappingElement element = new MappingElement(input_data, status, entries);
				 mappingElements.add(element);
			}


		    Mapping mapping = new Mapping(
				this._type,
				this._name,
				this._version,
				m_description,
				this._from_cs,
				this._from_version,
				this._to_cs,
				this._to_version,
				this._ncim_version,
				this._vsdURI,
				this._valueSetDefinitionName,
				this._creation_date,
				this._uuid,
				this._status,
				mappingElements);

			XStream xstream_xml = new XStream(new DomDriver());
			String xml = xstream_xml.toXML(mapping);
			return xml;
		}




        public List<MappingData> getMappingData(Vector options) {
            List list = new ArrayList();

            for (int k=0; k<_data.size(); k++) {
			    String input_data = (String) _data.get(k);

			    //List<MappingEntry> entries = new ArrayList();
			    List selected_matches = (ArrayList) _mapping_hmap.get(input_data);

			    if (selected_matches != null) {
				    for (int lcv2=0; lcv2<selected_matches.size(); lcv2++) {
						MappingData mappingData = (MappingData) selected_matches.get(lcv2);

						String status = mappingData.getStatus();
						if (options.contains(status)) {
						    list.add(mappingData);
						}
					}
			    }
		    }
		    return list;
	   }






        public Vector<MappingEntry> getMappingEntries(Vector options) {

            Vector<MappingEntry> w = new Vector();
            for (int k=0; k<_data.size(); k++) {
			   String input_data = (String) _data.get(k);

			   //List<MappingEntry> entries = new ArrayList();
			   List selected_matches = (ArrayList) _mapping_hmap.get(input_data);

			   if (selected_matches != null) {
				    for (int lcv2=0; lcv2<selected_matches.size(); lcv2++) {
						 MappingData mappingData = (MappingData) selected_matches.get(lcv2);

						 String status = mappingData.getStatus();
						 if (options.contains(status)) {

							 String source_code = mappingData.getSourceCode();
							 String source_name = mappingData.getSourceName();
							 String source_namespace = mappingData.getSourceCodeNamespace();

							 String associationName = "mapsTo";
							 String rel = mappingData.getRel();
							 if (DataUtils.isNull(rel)) rel = "";
							 int score = mappingData.getScore();
							 String target_code = mappingData.getTargetCode();
							 String target_name = mappingData.getTargetName();
							 String target_namespace = mappingData.getTargetCodeNamespace();

							 String source_scheme = mappingData.getSourceCodingScheme();
							 String source_version = mappingData.getSourceCodingSchemeVersion();
							 String target_scheme = mappingData.getTargetCodingScheme();
							 String target_version = mappingData.getTargetCodingSchemeVersion();

							 String comment = mappingData.getComment();
							 String entry_status = mappingData.getStatus();


							 source_scheme = DataUtils.getFormalName(source_scheme);
							 target_scheme = DataUtils.getFormalName(target_scheme);

							 MappingEntry mappingEntry = new MappingEntry(
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
								target_namespace,
								entry_status,
								comment);

							 //mappingEntry.setComment(mappingData.getComment());
							 w.add(mappingEntry);
						 }
					}
				}
			}
			return w;
		}

}