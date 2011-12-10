package gov.nih.nci.evs.mapping;


import java.util.*;
import java.net.URI;
import java.io.*;


import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;


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
* Copyright 2008,2009 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

/**
  * @author EVS Team
  * @version 1.0
  *
  * Modification history
  *     Initial implementation kim.ong@ngc.com
  *
 */

    public class Mapping {

	    private String type = null;
	    private String mappingName = null;
        private String mappingVersion = null;

        private String description = null;

        private String sourceCodingSchemeName = null;
        private String sourceCodingSchemeVersion = null;

        private String targetCodingSchemeName = null;
        private String targetCodingSchemeVersion = null;

        private String NCImVersion = null;

        private String valueSetDefinitionURI = null;
        private String valueSetDefinitionName = null;

        private String creationDate = null;
        private int UUID = 0;
		private String status = null;

        //private List data = new ArrayList();
		private List<MappingElement> mappingElements = new ArrayList();

        /**
         * Constructor
         */
        public Mapping() {

		}

        public Mapping(
			String type,
			String mappingName,
			String mappingVersion,
			String description,
			String sourceCodingSchemeName,
			String sourceCodingSchemeVersion,
			String targetCodingSchemeName,
			String targetCodingSchemeVersion,
			String NCImVersion,
			String valueSetDefinitionURI,
			String valueSetDefinitionName,
			String creationDate,
			int UUID,
			String status,
			List<MappingElement> mappingElements) {

			this.type = type;
			this.mappingName = mappingName;
			this.mappingVersion = mappingVersion;
			this.description = description;
			this.sourceCodingSchemeName = sourceCodingSchemeName;
			this.sourceCodingSchemeVersion = sourceCodingSchemeVersion;
			this.targetCodingSchemeName = targetCodingSchemeName;
			this.targetCodingSchemeVersion = targetCodingSchemeVersion;
			this.NCImVersion = NCImVersion;
			this.valueSetDefinitionURI = valueSetDefinitionURI;
			this.valueSetDefinitionName = valueSetDefinitionName;
			this.creationDate = creationDate;
			this.UUID = UUID;
			this.status = status;
			this.mappingElements = mappingElements;
		}


        public void setNCIMVersion(String NCImVersion) {
			this.NCImVersion = NCImVersion;
		}

		public String getNCIMVersion() {
			return this.NCImVersion;
		}

        public void setType(String type) {
			this.type = type;
		}

		public String getType() {
			return this.type;
		}


        public void setCreationDate(String creationDate) {
			this.creationDate = creationDate;
		}

		public String getCreationDate() {
			return this.creationDate;
		}


        public void setValueSetDefinitionURI(String valueSetDefinitionURI) {
			this.valueSetDefinitionURI = valueSetDefinitionURI;
		}

		public String getValueSetDefinitionURI() {
			return this.valueSetDefinitionURI;
		}


        public void setValueSetDefinitionName(String valueSetDefinitionName) {
			this.valueSetDefinitionName = valueSetDefinitionName;
		}

        public String getValueSetDefinitionName() {
			return this.valueSetDefinitionName;
		}

		public void setMappingElements(List<MappingElement> mappingElements) {
			this.mappingElements = mappingElements;
		}

		public List<MappingElement> getMappingElements() {
			return this.mappingElements;
		}

/*
		public void setData(List data) {
			this.data = data;
		}

		public List getData() {
			return this.data;
		}
*/

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			status = status;
		}


		public String getMappingName() {
			return mappingName;
		}
		public void setMappingName(String mappingName) {
			mappingName = mappingName;
		}

		public String getMappingVersion() {
			return mappingVersion;
		}
		public void setMappingVersion(String mappingVersion) {
			mappingVersion = mappingVersion;
		}

		public String getSourceCodingSchemeName() {
			return sourceCodingSchemeName;
		}
		public void setFromCS(String from_cs) {
			sourceCodingSchemeName = from_cs;
		}

		public String getSourceCodingSchemeVersion() {
			return sourceCodingSchemeVersion;
		}
		public void setSourceCodingSchemeVersion(String sourceCodingSchemeVersion) {
			sourceCodingSchemeVersion = sourceCodingSchemeVersion;
		}

		public String getTargetCodingSchemeName() {
			return targetCodingSchemeName;
		}
		public void setTargetCodingSchemeName(String targetCodingSchemeName) {
			targetCodingSchemeName = targetCodingSchemeName;
		}

		public String getTargetCodingSchemeVersion() {
			return targetCodingSchemeVersion;
		}
		public void setTargetCodingSchemeVersion(String targetCodingSchemeVersion) {
			targetCodingSchemeVersion = targetCodingSchemeVersion;
		}

    }