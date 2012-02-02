package gov.nih.nci.evs.browser.bean;

import java.util.*;
import gov.nih.nci.evs.browser.utils.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.valueSets.ValueSetDefinition;




    public class ComponentObject {
        private String _label = null;
        private String _description = null;

        private String _vocabulary = null;

        private String _version = null;

        private String _type = null;

        private String _propertyName = null;

        private String _matchText = null;
        private String _algorithm = null;

        private boolean _checkbox = false;

        private String _codes = null;


		private String _focusConceptCode = null;
		private String _rel_search_association = null;
		private String _include_focus_node = null;
		private String _transitivity = null;
		private String _selectedDirection = null;
		private String _selectValueSetReference = null;
		private String _selectCodingSchemeReference = null;


        public ComponentObject() {

		}

        public String getValueSetReference() {
        	return _selectValueSetReference;
        }

        public void setValueSetReference(String selectValueSetReference) {
        	this._selectValueSetReference = selectValueSetReference;
        }

        public String getCodingSchemeReference() {
        	return _selectCodingSchemeReference;
        }

        public void setCodingSchemeReference(String selectCodingSchemeReference) {
        	this._selectCodingSchemeReference = selectCodingSchemeReference;
        }


        public String getCodes() {
        	return _codes;
        }

        public void setCodes(String codes) {
        	this._codes = codes;
        }

        public String getPropertyName() {
        	return _propertyName;
        }

        public void setPropertyName(String propertyName) {
        	this._propertyName = propertyName;
        }

        public String getFocusConceptCode() {
        	return _focusConceptCode;
        }

        public void setFocusConceptCode(String focusConceptCode) {
        	this._focusConceptCode = focusConceptCode;
        }

        public String getRel_search_association() {
         	return _rel_search_association;
        }

        public void setRel_search_association(String rel_search_association) {
        	this._rel_search_association = rel_search_association;
        }


        public String getInclude_focus_node() {
          	return _include_focus_node;
        }

        public void setInclude_focus_node(String include_focus_node) {
          	this._include_focus_node = include_focus_node;
        }


        public String getTransitivity() {
         	return _transitivity;
        }

        public void setTransitivity(String transitivity) {
         	this._transitivity = transitivity;
        }

        public String getSelectedDirection() {
        	return _selectedDirection;
        }

        public void setSelectedDirection(String selectedDirection) {
        	this._selectedDirection = selectedDirection;
        }




        // Getters & setters
        public boolean getCheckbox() {
        	return _checkbox;
        }

        public void getCheckbox(boolean checked) {
        	_checkbox = checked;
        }

        public String getLabel() {
        	return _label;
        }

        public void setLabel(String label) {
        	this._label = label;
        }

        public String getDescription() {
        	return _description;
        }

        public void setDescription(String description) {
        	this._description = description;
        }


        public String getVocabulary() {
        	return _vocabulary;
        }

        public void setVocabulary(String vocabulary) {
        	this._vocabulary = vocabulary;
        }

        public String getVersion() {
        	return _version;
        }

        public void setVersion(String version) {
        	this._version = version;
        }


        public String getType() {
        	return _type;
        }

        public void setType(String type) {
        	this._type = type;
        }

        public String getMatchText() {
        	return _matchText;
        }

        public void setMatchText(String matchText) {
        	this._matchText = matchText;
        }

        public String getAlgorithm() {
        	return _algorithm;
        }

        public void setAlgorithm(String algorithm) {
        	this._algorithm = algorithm;
        }


        public CodedNodeSet toCNS() {
			if (_type.compareTo("EnumerationOfCodes") == 0) {
				return codes2CodedNodeSet();
			}

			CodedNodeSet cns = null;
			CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
			if (this._version != null)
				csvt.setVersion(this._version);

			try {
				LexBIGService lbSvc = null;
				lbSvc = new RemoteServerUtil().createLexBIGService();

				LocalNameList entityTypes = new LocalNameList();
				entityTypes.addEntry("concept");

				cns = lbSvc.getNodeSet(this._vocabulary, csvt, entityTypes);
				ResolvedConceptReferencesIterator iterator = toIterator();
				ConceptReferenceList codeList = new ConceptReferenceList();
				while (iterator.hasNext()) {
					ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
					codeList.addConceptReference(rcr);
				}
				cns.restrictToCodes(codeList);
			} catch (Exception ex) {

			}
			return cns;
		}


		public CodedNodeSet codes2CodedNodeSet() {
			if (_codes == null) return null;
			CodedNodeSet cns = null;
			CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
			if (this._version != null)
				csvt.setVersion(this._version);

			try {
				LexBIGService lbSvc = null;
				lbSvc = new RemoteServerUtil().createLexBIGService();

				LocalNameList entityTypes = new LocalNameList();
				entityTypes.addEntry("concept");

				cns = lbSvc.getNodeSet(this._vocabulary, csvt, entityTypes);
				ResolvedConceptReferencesIterator iterator = toIterator();
				ConceptReferenceList codeList = new ConceptReferenceList();

				String codes = _codes;
				if (codes == null) {
					codes = "";
				}
				codes = codes.trim();
				if (codes.compareTo("") == 0) {
					return null;
				}

				String lines[] = codes.split("\\n");
				for(int i = 0; i < lines.length; i++) {
					String t = lines[i];
					ConceptReference cr = new ConceptReference();
					cr.setConceptCode(t);
					codeList.addConceptReference(cr);
				}
				cns.restrictToCodes(codeList);
			} catch (Exception ex) {

			}
			return cns;
		}

        public ResolvedConceptReferencesIterator toIterator() {
			Vector matchtext_vec = new Vector();
			ResolvedConceptReferencesIterator iterator = null;
			matchtext_vec.add(this._matchText);
			if (_type.compareTo("Property") == 0) {

				iterator = new SearchUtils().searchByProperty(
						this._vocabulary, this._version, this._propertyName, matchtext_vec, this._algorithm);

			} else if (_type.compareTo("Relationship") == 0) {

                boolean resolveForward = true;
                boolean resolveBackward = false;
                if (this._selectedDirection.compareTo("Backward") == 0) {
					resolveForward = false;
					resolveBackward = true;
				}

				int resolveAssociationDepth = 1;

				if (this._transitivity != null && this._transitivity.compareTo("true") == 0) {
					resolveAssociationDepth = -1;
				}

                iterator = new SearchUtils().searchByAssociation(
					    this._vocabulary, this._version, this._focusConceptCode, this._rel_search_association,
					    resolveForward, resolveBackward, resolveAssociationDepth, -1);

			} else if (_type.compareTo("ValueSetReference") == 0) {
                String coding_scheme_name = this._vocabulary;
                String coding_scheme_version = this._version;

                System.out.println("(*) _selectValueSetReference: " + _selectValueSetReference);


                ValueSetDefinition vsd = ValueSetUtils.getValueSetDefinitionByURI(_selectValueSetReference);
                if (vsd != null) {
					iterator = ValueSetUtils.resolveValueSetDefinition(vsd, this._vocabulary, this._version);
				} else {
					System.out.println("(*) vsd == null???");
				}
/*
				try {
					LexEVSDistributed distributed =
						(LexEVSDistributed)
						ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

					LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();


					AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
					csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "10.08e"));

					ResolvedValueSetDefinition rvsd = vds.resolveValueSetDefinition(def, csvList, null, null);

					ResolvedConceptReferencesIterator itr = rvsd.getResolvedConceptReferenceIterator();

					while(itr.hasNext()){
						PrintUtility.print(itr.next());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
*/



			} else {
				CodedNodeSet cns = codes2CodedNodeSet();
				try {
					iterator = cns.resolve(null, null, null, null, false);
				} catch (Exception ex) {

				}
			}

		    return iterator;
		}

        public String toString() {
			return _label;
		}

    } // End of ComponentObject
