package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.io.*;
import java.net.*;
import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Impl.*;

import gov.nih.nci.system.client.*;
import gov.nih.nci.evs.security.*;

import gov.nih.nci.evs.browser.bean.*;


import org.apache.log4j.*;

import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;


import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.util.PrintUtility;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.valueSets.CodingSchemeReference;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.Exceptions.LBException;

import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;


import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;


import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;


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

/**
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */



public class ValueSetUtils
{
	  public static AbsoluteCodingSchemeVersionReferenceList defaultAbsoluteCodingSchemeVersionReferenceList = null;
	  public static HashMap _valueSetDefinitionURI2VSD_map = null;
	  private static Logger _logger = Logger.getLogger(ValueSetUtils.class);


/**
* Resolve a value set definition provided using the supplied set of coding scheme versions.
* This method also takes in list of ValueSetDefinitions (referencedVSDs) that are referenced by the ValueSetDefinition (vsDef).
* If referencedVSDs list is provided, these ValueSetDefinitions will be used to resolve vsDef.
*
* @param valueSetDefinition - value set definition object
* @param csVersionList - list of coding scheme versions to use in resolution. IF the value set definition uses a version that isn't mentioned in this list,
* the resolve function will return the codingScheme and version that was used as a default for the resolution.
* @param versionTag - the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
* @param referencedVSDs - List of ValueSetDefinitions referenced by vsDef. If provided, these ValueSetDefinitions will be used to resolve vsDef.
* @param sortOptionList - List of sort options to apply during resolution. If supplied, the sort algorithms will be applied in the order provided. Any
* algorithms not valid to be applied in context of node set iteration, as specified in the sort extension description,
* will result in a parameter exception. Available algorithms can  be retrieved through the LexBIGService getSortExtensions()
* method after being defined to the LexBIGServiceManager extension registry.
* @return Resolved Value Domain Definition
* @throws LBException
*/
//public ResolvedValueSetDefinition resolveValueSetDefinition(ValueSetDefinition vsDef, AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag, HashMap<String, ValueSetDefinition> referencedVSDs, SortOptionList sortOptionList) throws LBException;


//////////////////////////////////////////////////////////////////////////////////

    public static ResolvedConceptReferencesIterator resolveValueSetDefinition(ValueSetDefinition vsd,
        String scheme, String version) {
        HashMap<String, ValueSetDefinition> referencedVSDs = null;
		ResolvedConceptReferencesIterator iterator = null;
		boolean failOnAllErrors = false;
		String csVersionTag = null;//"PRODUCTION";

		SortOptionList sortOptionList = null;

        AbsoluteCodingSchemeVersionReferenceList csVersionList = new AbsoluteCodingSchemeVersionReferenceList();
		String cs_uri = DataUtils.codingSchemeName2URI(scheme);

System.out.println("(***) resolveValueSetDefinition ...cs_uri " +  cs_uri);
System.out.println("resolveValueSetDefinition ...version " +  version);


		csVersionList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(cs_uri, version));

        try {
        	LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
			ResolvedValueSetDefinition rvsd = null;
			try {
				System.out.println("Calling vsd_service.resolveValueSetDefinition ...");

				rvsd = vsd_service.resolveValueSetDefinition(vsd, csVersionList, csVersionTag, referencedVSDs, sortOptionList);

				//rvsd = vsd_service.resolveValueSetDefinition(vsd, csVersionList, null, null);

				System.out.println("Exiting vsd_service.resolveValueSetDefinition ...");

				if (rvsd != null) {
					System.out.println("rvsd != null");
			    	iterator = rvsd.getResolvedConceptReferenceIterator();
			    	if (iterator == null) {
				    	System.out.println("rvsd.getResolvedConceptReferenceIterator() returns NULL???");
					} else {
						System.out.println("rvsd.getResolvedConceptReferenceIterator() returns iterator != null OK");
					}


			    	return iterator;
				} else {
					System.out.println("rvsd == null???");
				}


			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Error: vsd_service.resolveValueSetDefinition throws exception.");
			}

		} catch (Exception ex) {
			//ex.printStackTrace();
			System.out.println("Error: getLexEVSValueSetDefinitionServices throws exception.");
		}
		return null;
	}


	  public static ValueSetDefinition generateValueSetDefinition(ComponentObject ob) {
		ValueSetDefinition vsd = new ValueSetDefinition();
		String valueSetDefinitionURI = "myURI";
		vsd.setValueSetDefinitionURI(valueSetDefinitionURI);

		DefinitionEntry vDefinitionEntry = null;
		Long ruleOrderCount = new Long(0);
		if(ob.getType().compareTo("EnumerationOfCodes") == 0) {

			String codes = ob.getCodes();
			String lines[] = codes.split("\\n");
			for(int i = 0; i < lines.length; i++) {
				String code = lines[i];
				code = code.trim();
				DefinitionEntry entry = new DefinitionEntry();
				EntityReference entity_ref = new EntityReference();
				entity_ref.setEntityCode(code);

				String cs_name = DataUtils.getCodingSchemeName(ob.getVocabulary());
				entity_ref.setEntityCodeNamespace(cs_name);

				entry.setEntityReference(entity_ref);
				entry.setRuleOrder(ruleOrderCount);
				entry.setOperator(DefinitionOperator.OR);
				vsd.addDefinitionEntry(entry);
				ruleOrderCount++;
			}
		} else {

			DefinitionEntry entry = componentObject2DefinitionEntry(ob);
			entry.setRuleOrder(ruleOrderCount);
			entry.setOperator(DefinitionOperator.OR);
			vsd.addDefinitionEntry(entry);
		}
		return vsd;
	  }



    public static DefinitionEntry componentObject2DefinitionEntry(ComponentObject ob) {

		DefinitionEntry entry = new DefinitionEntry();
        //EntityReference entity_ref = new EntityReference();
        //entry.setEntityReference(entity_ref);


        String cs_name = ob.getVocabulary();
System.out.println("componentObject2DefinitionEntry  cs_name = ob.getVocabulary(): " + ob.getVocabulary());


		String type = ob.getType();
        if (type.compareTo("Code") == 0) {
			EntityReference entity_ref = new EntityReference();
			entity_ref.setEntityCode(ob.getFocusConceptCode());
			//cs_name = DataUtils.getCodingSchemeName(ob.getVocabulary());




			entity_ref.setEntityCodeNamespace(cs_name);
			entry.setEntityReference(entity_ref);

		} else if (type.compareTo("Property") == 0) {
			PropertyReference pr = new PropertyReference();
			//String cs_name = DataUtils.getCodingSchemeName(ob.getVocabulary());

			String codingschemename = DataUtils.uri2CodingSchemeName(cs_name);

			pr.setCodingScheme(codingschemename);

			pr.setPropertyName(ob.getPropertyName());

			PropertyMatchValue pmv = new PropertyMatchValue();
			pmv.setMatchAlgorithm(ob.getAlgorithm());
			pmv.setContent(ob.getMatchText());

			pr.setPropertyMatchValue(pmv);
			entry.setPropertyReference(pr);

		} else if (type.compareTo("Relationship") == 0) {

			EntityReference entity_ref = new EntityReference();
			entity_ref.setEntityCode(ob.getFocusConceptCode());


System.out.println("(DEBUG) FocusConceptCode: " + ob.getFocusConceptCode());

			//cs_name = ob.getVocabulary();//DataUtils.getCodingSchemeName(ob.getVocabulary());

			String codingschemename = DataUtils.uri2CodingSchemeName(cs_name);

			entity_ref.setEntityCodeNamespace(codingschemename);
System.out.println("(DEBUG) cs_name: " + cs_name);

System.out.println("(DEBUG) codingschemename: " + codingschemename);


            if (ob.getInclude_focus_node() != null && ob.getInclude_focus_node().compareToIgnoreCase("true") == 0) {
				entity_ref.setLeafOnly(Boolean.FALSE);

System.out.println("(DEBUG) setLeafOnly to Boolean.FALSE: ");


			} else {
				entity_ref.setLeafOnly(Boolean.TRUE);

System.out.println("(DEBUG) setLeafOnly to Boolean.TRUE: ");
			}




			entity_ref.setReferenceAssociation(ob.getRel_search_association());

System.out.println("(DEBUG) Rel_search_association: " + ob.getRel_search_association());


            if (ob.getTransitivity() != null && ob.getTransitivity().compareToIgnoreCase("true") == 0) {
				entity_ref.setTransitiveClosure(Boolean.TRUE);

System.out.println("(DEBUG) setTransitiveClosure Boolean.TRUE ");


			} else {
				entity_ref.setTransitiveClosure(Boolean.FALSE);


System.out.println("(DEBUG) setTransitiveClosure Boolean.FALSE ");

			}



            if (ob.getSelectedDirection() != null && ob.getSelectedDirection().compareToIgnoreCase("forward") == 0) {
				entity_ref.setTargetToSource(Boolean.FALSE);

System.out.println("(DEBUG) setTargetToSource Boolean.FALSE ");


			} else {
				entity_ref.setTargetToSource(Boolean.TRUE);

System.out.println("(DEBUG) setTargetToSource Boolean.TRUE ");

			}
			entry.setEntityReference(entity_ref);

		} else if (type.compareTo("EntireVocabulary") == 0) {
            CodingSchemeReference codingSchemeReference = new CodingSchemeReference();
            codingSchemeReference.setCodingScheme(ob.getCodingSchemeReference());
            entry.setCodingSchemeReference(codingSchemeReference);

		} else if (type.compareTo("ValueSetReference") == 0) {
            ValueSetDefinitionReference valueSetDefinitionReference = new ValueSetDefinitionReference();
            valueSetDefinitionReference.setValueSetDefinitionURI (ob.getValueSetReference());

            System.out.println("(* testing VSD URI *) ValueSetReference: " + ob.getValueSetReference());

            entry.setValueSetDefinitionReference(valueSetDefinitionReference);
        }

		return entry;
	}

      public static ValueSetDefinition getValueSetDefinitionByURI(String valueSetDefinitionURI) {
		  ValueSetDefinition vsd = null;
		  try {
			  LexEVSValueSetDefinitionServices vds = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
			  String valueSetDefinitionRevisionId = null;
			  vsd = vds.getValueSetDefinition(new URI(valueSetDefinitionURI), valueSetDefinitionRevisionId);
		  } catch (Exception ex) {
			  ex.printStackTrace();
		  }
		  return vsd;
	  }
}