package gov.nih.nci.evs.browser.bean;

    public class ComponentObject {
        private String _label = null;
        private String _description = null;

        private String _vocabulary = null;

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

        public String toString() {
			return _label;
		}

    } // End of ComponentObject
