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
			String key = "M" + new Integer(uuid).toString();
			return key;

		}



		public void setKey() {
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
			_key = "M" + new Integer(_uuid).toString();

		}

		public String getKey() {
			return _key;
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



    }