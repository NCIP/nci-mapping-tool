/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.sql.*;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.commonTypes.*;

import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.common.*;

import org.apache.commons.codec.language.*;
import org.apache.log4j.*;
import org.LexGrid.relations.Relations;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping.SearchContext;

import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;

import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOptionName;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.QualifierSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

import static gov.nih.nci.evs.browser.common.Constants.*;
import gov.nih.nci.evs.browser.bean.*;


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

public class MappingSearchUtils {
    private static Logger _logger = Logger.getLogger(MappingSearchUtils.class);

    public MappingSearchUtils() {

    }


    public static String getMappingRelationsContainerName(String scheme, String version) {
		CodingSchemeVersionOrTag versionOrTag =
			new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}

		String relationsContainerName = null;
        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
			if (cs == null) return null;

			java.util.Enumeration<? extends Relations> relations = cs.enumerateRelations();
			while (relations.hasMoreElements()) {
				Relations relation = (Relations) relations.nextElement();
				Boolean isMapping = relation.getIsMapping();
				System.out.println("isMapping: " + isMapping);
				if (isMapping != null && isMapping.equals(Boolean.TRUE)) {
 					relationsContainerName = relation.getContainerName();
					break;
				}
			}

			if (relationsContainerName == null) {
				System.out.println("WARNING: Mapping container not found in " + scheme);
				return null;
			} else {
				System.out.println("relationsContainerName " + relationsContainerName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return relationsContainerName;
	}



    private CodedNodeSet.PropertyType[] getAllNonPresentationPropertyTypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[3];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        return propertyTypes;
    }



    public ResolvedConceptReferencesIteratorWrapper searchByCode(
        String scheme, String version, String matchText,
        String matchAlgorithm, int maxToReturn) {



		Vector schemes = new Vector();
		schemes.add(scheme);
		Vector versions = new Vector();
		versions.add(version);
		return searchByCode(schemes, versions, matchText, matchAlgorithm, SearchContext.SOURCE_OR_TARGET_CODES, maxToReturn);
	}


    public ResolvedConceptReferencesIteratorWrapper searchByCode(
        Vector schemes, Vector versions, String matchText,
        String matchAlgorithm, int maxToReturn) {



        return searchByCode(
         schemes, versions, matchText,
         matchAlgorithm, SearchContext.SOURCE_OR_TARGET_CODES, maxToReturn);
    }


    public ResolvedConceptReferencesIteratorWrapper searchByCode(
        String scheme, String version, String matchText,
        String matchAlgorithm, SearchContext searchContext, int maxToReturn) {
		Vector schemes = new Vector();
		schemes.add(scheme);
		Vector versions = new Vector();
		versions.add(version);
		return searchByCode(schemes, versions, matchText, matchAlgorithm, searchContext, maxToReturn);
	}


    public ResolvedConceptReferencesIteratorWrapper searchByCode(
        Vector schemes, Vector versions, String matchText,
        String matchAlgorithm, SearchContext searchContext, int maxToReturn) {

System.out.println("==============================  MappingSearchUtils searchByCode");


		if (matchText == null || matchText.trim().length() == 0)
			return null;

		matchText = matchText.trim();
		_logger.debug("searchByCode ... " + matchText);

		if (matchAlgorithm.compareToIgnoreCase("contains") == 0)
		{
		   matchAlgorithm = new SearchUtils().findBestContainsAlgorithm(matchText);
		}
		System.out.println("matchAlgorithm: " + matchAlgorithm);

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		MappingExtension mappingExtension = null;
		try {
			mappingExtension = (MappingExtension)lbSvc.getGenericExtension("MappingExtension");
		} catch (Exception ex) {
            ex.printStackTrace();
            return null;
		}

        ResolvedConceptReferencesIterator itr = null;
        int lcv = 0;
        String scheme = null;
        String version = null;
        int numberRemaining = -1;
        while (itr == null && numberRemaining == -1 && lcv < schemes.size()) {
            scheme = (String) schemes.elementAt(lcv);
            version = (String) versions.elementAt(lcv);

System.out.println(scheme + " (version: " +  version + ")");

			String containerName = getMappingRelationsContainerName(scheme, version);
System.out.println("\tcontainer name: " +  containerName);

			if (containerName != null) {
				try {

		CodingSchemeVersionOrTag versionOrTag =
			new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}

					Mapping mapping =
						mappingExtension.getMapping(scheme, versionOrTag, containerName);

					if (mapping != null) {

						ConceptReferenceList codeList = new ConceptReferenceList();
						ConceptReference ref = new ConceptReference();

						System.out.println("ref.setConceptCode: " + matchText);
						ref.setConceptCode(matchText);
 						codeList.addConceptReference(ref);

                        System.out.println("mapping.restrictToCodes: " + matchText);

                        mapping = mapping.restrictToCodes(codeList, searchContext);
						itr = mapping.resolveMapping();
						if (itr != null) {
							try {
								numberRemaining = itr.numberRemaining();
								System.out.println("\tsearchByCode matches: " + numberRemaining);

							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		    lcv++;
		}
		if (itr != null) {
			ResolvedConceptReferencesIteratorWrapper wrapper = new ResolvedConceptReferencesIteratorWrapper(itr);
			wrapper.setCodingSchemeName(scheme);
			wrapper.setCodingSchemeVersion(version);
			return wrapper;
		}
		return null;
    }



    public ResolvedConceptReferencesIteratorWrapper searchByName(
        String scheme, String version, String matchText,
        String matchAlgorithm, int maxToReturn) {
		Vector schemes = new Vector();
		schemes.add(scheme);
		Vector versions = new Vector();
		versions.add(version);
		return searchByName(schemes, versions, matchText, matchAlgorithm, maxToReturn);
	}


    public ResolvedConceptReferencesIteratorWrapper searchByName(
        Vector schemes, Vector versions, String matchText,
        String matchAlgorithm, int maxToReturn) {

		if (matchText == null || matchText.trim().length() == 0)
			return null;

		matchText = matchText.trim();
		_logger.debug("searchByName ... " + matchText);

		if (matchAlgorithm.compareToIgnoreCase("contains") == 0)
		{
		   matchAlgorithm = new SearchUtils().findBestContainsAlgorithm(matchText);
		}

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		MappingExtension mappingExtension = null;
		try {
			mappingExtension = (MappingExtension)lbSvc.getGenericExtension("MappingExtension");
		} catch (Exception ex) {
            ex.printStackTrace();
            return null;
		}

        ResolvedConceptReferencesIterator itr = null;
        int lcv = 0;
        String scheme = null;
        String version = null;
        int numberRemaining = -1;
        while (itr == null && numberRemaining == -1 && lcv < schemes.size()) {
            scheme = (String) schemes.elementAt(lcv);
            version = (String) versions.elementAt(lcv);
			String containerName = getMappingRelationsContainerName(scheme, version);
			if (containerName != null) {
				try {

		CodingSchemeVersionOrTag versionOrTag =
			new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}
					Mapping mapping =
						mappingExtension.getMapping(scheme, versionOrTag, containerName);

					if (mapping != null) {
						mapping = mapping.restrictToMatchingDesignations(
									matchText, SearchDesignationOption.ALL, matchAlgorithm, null, SearchContext.SOURCE_OR_TARGET_CODES
);

							//Finally, resolve the Mapping.
						itr = mapping.resolveMapping();
						try {
							numberRemaining = itr.numberRemaining();
							System.out.println("Number of matches: " + numberRemaining);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					//return null;
				}
		    }
		    lcv++;
		}
		if (itr != null) {
			ResolvedConceptReferencesIteratorWrapper wrapper = new ResolvedConceptReferencesIteratorWrapper(itr);
			wrapper.setCodingSchemeName(scheme);
			wrapper.setCodingSchemeVersion(version);
			return wrapper;
		}
		return null;
    }

    public ResolvedConceptReferencesIteratorWrapper searchByProperties(
        String scheme, String version, String matchText,
        String matchAlgorithm, int maxToReturn) {

System.out.println("searchByProperties scheme: " + scheme);
System.out.println("searchByProperties version: " + version);


		Vector schemes = new Vector();
		schemes.add(scheme);
		Vector versions = new Vector();
		versions.add(version);
		return searchByProperties(schemes, versions, matchText, matchAlgorithm, maxToReturn);
	}


    public ResolvedConceptReferencesIteratorWrapper searchByProperties(
        Vector schemes, Vector versions, String matchText,
        String matchAlgorithm, int maxToReturn) {

		if (matchText == null || matchText.trim().length() == 0)
			return null;

		matchText = matchText.trim();
		_logger.debug("searchByName ... " + matchText);

		if (matchAlgorithm.compareToIgnoreCase("contains") == 0)
		{
		   matchAlgorithm = new SearchUtils().findBestContainsAlgorithm(matchText);
		}

        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList propertyNames = null;
        LocalNameList sourceList = null;
        propertyTypes = getAllNonPresentationPropertyTypes();

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;
        String language = null;
        // to be modified
        SearchContext searchContext = SearchContext.SOURCE_OR_TARGET_CODES
;

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		MappingExtension mappingExtension = null;
		try {
			mappingExtension = (MappingExtension)lbSvc.getGenericExtension("MappingExtension");
		} catch (Exception ex) {
            ex.printStackTrace();
            return null;
		}

        ResolvedConceptReferencesIterator itr = null;
        int lcv = 0;
        String scheme = null;
        String version = null;

        System.out.println("schemes.size(): " + schemes.size() + " lcv: " + lcv);
        int numberRemaining = -1;
        while (itr == null && numberRemaining == -1 && lcv < schemes.size()) {

            scheme = (String) schemes.elementAt(lcv);
            version = (String) versions.elementAt(lcv);

			String containerName = getMappingRelationsContainerName(scheme, version);
			if (containerName != null) {
				try {

		CodingSchemeVersionOrTag versionOrTag =
			new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}

					Mapping mapping =
						mappingExtension.getMapping(scheme, versionOrTag, containerName);

					if (mapping != null) {
						mapping = mapping.restrictToMatchingProperties(
							   propertyNames,
							   propertyTypes,
							   sourceList,
							   contextList,
							   qualifierList,
							   matchAlgorithm,
							   language,
							   null,
							   searchContext);

							//Finally, resolve the Mapping.
						itr = mapping.resolveMapping();
						try {
							numberRemaining = itr.numberRemaining();
							System.out.println("Number of matches: " + numberRemaining);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					//return null;
				}
		    }
		    lcv++;
		}
		if (itr != null) {
			ResolvedConceptReferencesIteratorWrapper wrapper = new ResolvedConceptReferencesIteratorWrapper(itr);
			wrapper.setCodingSchemeName(scheme);
			wrapper.setCodingSchemeVersion(version);
			return wrapper;
		}
		return null;
    }

    public LocalNameList getSupportedAssociationNames(LexBIGService lbSvc, String scheme,
        String version, String containerName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        LocalNameList list = new LocalNameList();
        try {
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            Relations[] relations = cs.getRelations();
            for (int i = 0; i < relations.length; i++) {
                Relations relation = relations[i];

                _logger.debug("** getSupportedRoleNames containerName: "
                    + relation.getContainerName());

                if (relation.getContainerName().compareToIgnoreCase(containerName) == 0) {
                    org.LexGrid.relations.AssociationPredicate[] asso_array =
                        relation.getAssociationPredicate();
                    for (int j = 0; j < asso_array.length; j++) {
                        org.LexGrid.relations.AssociationPredicate association =
                            (org.LexGrid.relations.AssociationPredicate) asso_array[j];
                        // list.add(association.getAssociationName());
                        // KLO, 092209
                        //list.add(association.getForwardName());
                        list.addEntry(association.getAssociationName());
                    }
                }
            }
        } catch (Exception ex) {

        }
        return list;
    }


    public ResolvedConceptReferencesIteratorWrapper searchByRelationships(
        String scheme, String version, String matchText,
        String matchAlgorithm, int maxToReturn) {
		Vector schemes = new Vector();
		schemes.add(scheme);
		Vector versions = new Vector();
		versions.add(version);
		return searchByRelationships(schemes, versions, matchText, matchAlgorithm, maxToReturn);
	}


    public ResolvedConceptReferencesIteratorWrapper searchByRelationships(
        Vector schemes, Vector versions, String matchText,
        String matchAlgorithm, int maxToReturn) {

		if (matchText == null || matchText.trim().length() == 0)
			return null;

		matchText = matchText.trim();
		_logger.debug("searchByName ... " + matchText);

		if (matchAlgorithm.compareToIgnoreCase("contains") == 0)
		{
		   matchAlgorithm = new SearchUtils().findBestContainsAlgorithm(matchText);
		}

		SearchDesignationOption option = SearchDesignationOption.ALL;
		String language = null;


        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList propertyNames = null;
        LocalNameList sourceList = null;
        //propertyTypes = getAllNonPresentationPropertyTypes();

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		MappingExtension mappingExtension = null;
		try {
			mappingExtension = (MappingExtension)lbSvc.getGenericExtension("MappingExtension");
		} catch (Exception ex) {
            ex.printStackTrace();
            return null;
		}

        ResolvedConceptReferencesIterator itr = null;
        int lcv = 0;
        String scheme = null;
        String version = null;

        System.out.println("schemes.size(): " + schemes.size() + " lcv: " + lcv);
        int numberRemaining = -1;
        while (itr == null && numberRemaining == -1 && lcv < schemes.size()) {

            scheme = (String) schemes.elementAt(lcv);
            version = (String) versions.elementAt(lcv);

			String containerName = getMappingRelationsContainerName(scheme, version);
			if (containerName != null) {

				LocalNameList relationshipList = getSupportedAssociationNames(lbSvc, scheme, version, containerName);

				try {

		CodingSchemeVersionOrTag versionOrTag =
			new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}
					Mapping mapping =
						mappingExtension.getMapping(scheme, versionOrTag, containerName);

					if (mapping != null) {

					    mapping = mapping.restrictToRelationship(
							 matchText,
							 option,
							 matchAlgorithm,
							 language,
							 relationshipList);

							//Finally, resolve the Mapping.
						itr = mapping.resolveMapping();
						try {
							numberRemaining = itr.numberRemaining();
							System.out.println("Number of matches: " + numberRemaining);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					//return null;
				}
		    }
		    lcv++;
		}
		if (itr != null) {
			ResolvedConceptReferencesIteratorWrapper wrapper = new ResolvedConceptReferencesIteratorWrapper(itr);
			wrapper.setCodingSchemeName(scheme);
			wrapper.setCodingSchemeVersion(version);
			return wrapper;
		}
		return null;
    }




     public static ResolvedConceptReferencesIterator getRestrictedMappingDataIterator(String scheme, String version,
        List<MappingSortOption> sortOptionList, ResolvedConceptReferencesIterator searchResultsIterator) {
        return getRestrictedMappingDataIterator(scheme, version,
        sortOptionList, searchResultsIterator, SearchContext.SOURCE_OR_TARGET_CODES);

    }



    public static ResolvedConceptReferencesIterator getRestrictedMappingDataIterator(String scheme, String version,
        List<MappingSortOption> sortOptionList, ResolvedConceptReferencesIterator searchResultsIterator, SearchContext context) {

System.out.println("(***********) getRestrictedMappingDataIterator ...");
if (searchResultsIterator == null) return null;

		try {
			int numRemaining = searchResultsIterator.numberRemaining();
			System.out.println("(***********) searchResultsIterator passing number of matches: " + numRemaining);
		} catch (Exception e) {
			System.out.println("searchResultsIterator.numberRemaining() throws exception???");
			return null;
		}


		CodingSchemeVersionOrTag versionOrTag =
			new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}
		String relationsContainerName = null;

        LexBIGService distributed = RemoteServerUtil.createLexBIGService();
        try {
			CodingScheme cs = distributed.resolveCodingScheme(scheme, versionOrTag);
			if (cs == null) return null;

			java.util.Enumeration<? extends Relations> relations = cs.enumerateRelations();
			while (relations.hasMoreElements()) {
				Relations relation = (Relations) relations.nextElement();
				Boolean isMapping = relation.getIsMapping();
				System.out.println("isMapping: " + isMapping);
				if (isMapping != null && isMapping.equals(Boolean.TRUE)) {
 					relationsContainerName = relation.getContainerName();
					break;
				}
			}

			if (relationsContainerName == null) {
				System.out.println("WARNING: Mapping container not found in " + scheme);
				return null;
			} else {
				System.out.println("relationsContainerName " + relationsContainerName);
			}

			MappingExtension mappingExtension = (MappingExtension)
				distributed.getGenericExtension("MappingExtension");

		    Mapping mapping =
			    mappingExtension.getMapping(scheme, versionOrTag, relationsContainerName);

            //ConceptReferenceList codeList (to be derived based on ResolvedConceptReferencesIterator searchResultsIterator)
            ConceptReferenceList codeList = new ConceptReferenceList();

			//if (searchResultsIterator != null) {
				int lcv = 0;
				while(searchResultsIterator.hasNext()){
					ResolvedConceptReference[] refs = searchResultsIterator.next(100).getResolvedConceptReference();
					for(ResolvedConceptReference ref : refs){
						lcv++;
						System.out.println("(" + lcv + ") " + ref.getEntityDescription().getContent() + "(" + ref.getCode() + ")");
						codeList.addConceptReference((ConceptReference) ref);
					}
				}
				/*
			} else {
				System.out.println("resolved_value_set.jsp ResolvedConceptReferencesIterator == NULL???");
			}*/

            mapping = mapping.restrictToCodes(codeList, context);
            ResolvedConceptReferencesIterator itr = mapping.resolveMapping(sortOptionList);
			return itr;

		} catch (Exception ex) {
			//ex.printStackTrace();
			System.out.println("getRestrictedMappingDataIterator throws exceptions???");

		}
		return null;
	}

    public List getMappingRelationship(
        String scheme, String version, String code, int direction) {

		SearchContext searchContext = SearchContext.SOURCE_OR_TARGET_CODES;
		if (direction == 1) searchContext = SearchContext.SOURCE_CODES;
        else if (direction == -1) searchContext = SearchContext.TARGET_CODES;

        ResolvedConceptReferencesIteratorWrapper wrapper = searchByCode(
         scheme, version, code, "exactMatch", searchContext, -1);

        if (wrapper == null) return null;
        ResolvedConceptReferencesIterator iterator = wrapper.getIterator();
        if (iterator == null) return null;

		int numberRemaining = 0;
		try {
			numberRemaining = iterator.numberRemaining();
			if (numberRemaining == 0) {
                return null;
			}

		} catch (Exception ex) {
			//ex.printStackTrace();
			System.out.println("iterator.numberRemaining() throws exceptions???");
			return null;
		}

		System.out.println("(*) getMappingRelationship numberRemaining " + numberRemaining);


/*
		MappingIteratorBean mappingIteratorBean = new MappingIteratorBean(
		iterator,
		numberRemaining, // number remaining
		0,    // istart
		50,   // iend,
		//numberRemaining, // size,
		0,    // pageNumber,
		1);   // numberPages

		mappingIteratorBean.initialize(
		iterator,
		numberRemaining, // number remaining
		0,    // istart
		50,   // iend,
		numberRemaining, // size,
		0,    // pageNumber,
		1);   // numberPages
*/
        //mappingIteratorBean.initialize();


 		MappingIteratorBean mappingIteratorBean = new MappingIteratorBean(iterator);
		List list = mappingIteratorBean.getData(0, numberRemaining); // implement getAll
		if (mappingIteratorBean.getSize() != numberRemaining) {
			list = mappingIteratorBean.getData(0, mappingIteratorBean.getSize() );
		}
		return list;
    }

/*
    public static String TYPE_ROLE = "type_role";
    public static String TYPE_ASSOCIATION = "type_association";
    public static String TYPE_SUPERCONCEPT = "type_superconcept";
    public static String TYPE_SUBCONCEPT = "type_subconcept";
    public static String TYPE_INVERSE_ROLE = "type_inverse_role";
    public static String TYPE_INVERSE_ASSOCIATION = "type_inverse_association";

*/

    private String replaceAssociationNameByRela(AssociatedConcept ac,
        String associationName) {
        if (ac.getAssociationQualifiers() == null)
            return associationName;
        if (ac.getAssociationQualifiers().getNameAndValue() == null)
            return associationName;

        for (NameAndValue qual : ac.getAssociationQualifiers()
            .getNameAndValue()) {
            String qualifier_name = qual.getName();
            String qualifier_value = qual.getContent();
            if (qualifier_name.compareToIgnoreCase("rela") == 0) {
                return qualifier_value; // replace associationName by Rela value
            }
        }
        return associationName;
    }


    public HashMap getMappingRelationshipHashMap(String scheme, String version, String code) {
        HashMap hmap = new HashMap();
		HashMap map1 = getMappingRelationshipHashMap(scheme, version, code, 1);
        ArrayList list = (ArrayList) map1.get(TYPE_ASSOCIATION);
        if (list != null) {
			hmap.put(TYPE_ASSOCIATION, list);
		}
		HashMap map2 = getMappingRelationshipHashMap(scheme, version, code, -1);
        list = (ArrayList) map2.get(TYPE_INVERSE_ASSOCIATION);
        if (list != null) {
			hmap.put(TYPE_INVERSE_ASSOCIATION, list);
		}
		return hmap;
	}


    public HashMap getMappingRelationshipHashMap(
        String scheme, String version, String code, int direction) {

		SearchContext searchContext = SearchContext.SOURCE_OR_TARGET_CODES;
		if (direction == 1) searchContext = SearchContext.SOURCE_CODES;
        else if (direction == -1) searchContext = SearchContext.TARGET_CODES;

System.out.println("direction: " + direction);

        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        LexBIGServiceConvenienceMethods lbscm =
            new DataUtils().createLexBIGServiceConvenienceMethods(lbSvc);

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);


        ResolvedConceptReferencesIteratorWrapper wrapper = searchByCode(
         scheme, version, code, "exactMatch", searchContext, -1);

        if (wrapper == null) return null;
        ResolvedConceptReferencesIterator iterator = wrapper.getIterator();
        if (iterator == null) return null;

        HashMap hmap = new HashMap();
        ArrayList list = new ArrayList();
        try {
			while (iterator.hasNext()) {
				ResolvedConceptReference ref = (ResolvedConceptReference) iterator.next();

				AssociationList asso_of = ref.getSourceOf();
				//KLO, 030811
				if (direction == -1) asso_of = ref.getTargetOf();

				if (asso_of != null) {
					Association[] associations =
						asso_of.getAssociation();

					if (associations != null) {

						for (int i = 0; i < associations.length; i++) {
							Association assoc = associations[i];
							String associationName = null;
							try {
								associationName = lbscm
									.getAssociationNameFromAssociationCode(
										scheme, csvt, assoc
											.getAssociationName());
							} catch (Exception ex) {
								associationName = assoc.getAssociationName();
							}

							AssociatedConcept[] acl =
								assoc.getAssociatedConcepts()
									.getAssociatedConcept();

							for (int j = 0; j < acl.length; j++) {
								AssociatedConcept ac = acl[j];

								EntityDescription ed =
									ac.getEntityDescription();

								String name = "No Description";
								if (ed != null)
									name = ed.getContent();
								String pt = name;

								if (associationName
									.compareToIgnoreCase("equivalentClass") != 0
									&& ac.getConceptCode().indexOf("@") == -1) {

/*
									String relaValue =
										replaceAssociationNameByRela(
											ac, associationName);
*/
String relaValue = associationName;

System.out.println("relaValue: " + relaValue);

									String s =
										relaValue + "|" + pt + "|"
											 + ac.getConceptCode() + "|"
											 + ac.getCodingSchemeName();

                                    if (direction == -1) {

										EntityDescription ref_ed =
											ref.getEntityDescription();

										String ref_name = "No Description";
										if (ref_ed != null)
											ref_name = ref_ed.getContent();


										s = relaValue + "|" + ref_name + "|"
											 + ref.getCode() + "|"
											 + ref.getCodingSchemeName();

									}

									if (ac.getAssociationQualifiers() != null) {
										//String qualifiers = "";
										StringBuffer buf = new StringBuffer();
										for (NameAndValue qual : ac
												.getAssociationQualifiers()
												.getNameAndValue()) {
											String qualifier_name = qual.getName();
											String qualifier_value = qual.getContent();
											//qualifiers = qualifiers + (qualifier_name + ":" + qualifier_value) + "$";
											buf.append((qualifier_name + ":" + qualifier_value) + "$");
										}
										s = s + "|" + buf.toString();
									}


                                    if (direction == -1) {
									    s = s + "|" + ref.getCodeNamespace();
									} else {
										s = s + "|" + ac.getCodeNamespace();
									}
									list.add(s);

								}
							}
						}
					}
				}
			}
			if (list.size() > 0) {
				Collections.sort(list);
			}

			if (direction == 1) {
				hmap.put(TYPE_ASSOCIATION, list);
			} else {
				hmap.put(TYPE_INVERSE_ASSOCIATION, list);
			}
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return hmap;

	}


    public List simpleSearch(String type,
                             String input_option,
                             String input_value,

                             String ncim_version,
                             String source_abbrev,
                             String target_abbrev,

                             String source_cs,
                             String target_cs,

                             String algorithm) {

	         return simpleSearch(type,
	                             input_option,
	                             input_value,

	                             ncim_version,
	                             source_abbrev,
	                             target_abbrev,

	                             source_cs,
	                             target_cs,

	                             null,

				                 null,
				                 null,
				                 null,

				                 null,
				                 null,
				                 null,
				                 null,

	                             algorithm);

    }

    public List simpleSearch(String type,
                             String input_option,
                             String input_value,

                             String ncim_version,
                             String source_abbrev,
                             String target_abbrev,

                             String source_cs,
                             String target_cs,

                             CodedNodeSet vsd_cns,

			                 String property,
			                 String src_property,
			                 String target_property,

			                 String left_trim,
			                 String right_trim,
			                 String prefix,
			                 String suffix,

                             String algorithm

                            ) {

		List match_list = new ArrayList();
		if (type.compareTo("valueset") == 0) {

			String source_scheme = DataUtils.key2CodingSchemeName(source_cs);
			String source_version = DataUtils.key2CodingSchemeVersion(source_cs);
			String sourceCodingScheme = DataUtils.getFormalName(source_scheme);
			if (sourceCodingScheme == null) sourceCodingScheme = source_scheme;
			String sourceCodingSchemeVersion = null;
			sourceCodingSchemeVersion = source_version;

			//HashSet hset = new HashSet();
			String source = null;
			boolean ranking = false;

			int counter = 0;



			String sourceCode = null;
			String matchtext = null; //(String) values.elementAt(1);

			if (input_option.compareToIgnoreCase("Code") == 0) {
				sourceCode = input_value;

			} else {
				Vector values = DataUtils.parseData(input_value);
				sourceCode = (String) values.elementAt(0);
				matchtext = (String) values.elementAt(1);
			}


			ResolvedConceptReferencesIterator iterator = null;
			String sourceName = "";
			String sourceCodeNamespace = "";
			String rel = "";
			int score = 0;
			String associationName = "mapsTo";

			try {

				Entity source_entity = MappingUtils.getConceptByCode(sourceCodingScheme,
														sourceCodingSchemeVersion,
														null,
														sourceCode);

				if (source_entity == null) {
					//System.out.println("source_entity not found: " + sourceCode);
				} else {
					//System.out.println("source_entity found: " + sourceCode);
					//if (source_entity != null) {
						sourceName = source_entity.getEntityDescription().getContent();
						sourceCodeNamespace = source_entity.getEntityCodeNamespace();
					//}

					if(input_option.compareToIgnoreCase("Code") == 0) {
						System.out.println("calling getSynonyms: sourceCode " + sourceCode);

						Vector matchtext_vec = new Vector();
						Vector synonyms = DataUtils.getSynonyms(sourceCodingScheme, sourceCodingSchemeVersion, null, sourceCode);
						if (synonyms != null) {
							for (int k=0; k<synonyms.size(); k++) {
								String synonym_str = (String) synonyms.elementAt(k);
								Vector v = DataUtils.parseData(synonym_str);
								String synonym = (String) v.elementAt(0);
								matchtext_vec.add(synonym);
								System.out.println(sourceCode + " " + synonym);
							}
							iterator = new SearchUtils().searchByName(vsd_cns, matchtext_vec, algorithm);
						}

					} else if(input_option.compareTo("Name") == 0) {
						//iterator = MappingUtils.searchByName(targetCodingScheme, targetCodingSchemeVersion, matchtext, null, algorithm, false, 10);
						Vector matchtext_vec = new Vector();
						matchtext_vec.add(matchtext);
						iterator = new SearchUtils().searchByName(vsd_cns, matchtext_vec, algorithm);

					}

				}
				if (iterator != null) {
					try {
						 HashSet key_hset = new HashSet();
						 int numRemaining = iterator.numberRemaining();

						 while (iterator.hasNext()) {
							 ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();

							 String targetCodingScheme = rcr.getCodingSchemeName();
							 String targetCodingSchemeVersion = rcr.getCodingSchemeVersion();

							 Entity target_entity = MappingUtils.getConceptByCode(targetCodingScheme, targetCodingSchemeVersion, null, rcr.getConceptCode());

							 String targetCode = rcr.getConceptCode();
							 String targetName = "";
							 String targetCodeNamespace = "";
							 if (target_entity != null) {
								targetName = target_entity.getEntityDescription().getContent();
								targetCodeNamespace = target_entity.getEntityCodeNamespace();
							 }

							 String key = sourceCode + "|" + targetCode;
							 if (!key_hset.contains(key)) {
								 key_hset.add(key);

								 MappingData mappingData = new MappingData(
									sourceCode,
									sourceName,
									sourceCodingScheme,
									sourceCodingSchemeVersion,
									sourceCodeNamespace,
									associationName,
									rel,
									score,
									targetCode,
									targetName,
									targetCodingScheme,
									targetCodingSchemeVersion,
									targetCodeNamespace);
								 match_list.add(mappingData);
							 }
						 }
						 key_hset.clear();

					 } catch (Exception ex) {
						ex.printStackTrace();
					 }
				  }

			  } catch (Exception ex) {
				  ex.printStackTrace();
			  }

	    } else if (type.compareTo("ncimeta") == 0) {

			String source_scheme = null;
			String source_version = null;
			String source_namespace = null;
			String target_scheme = null;
			String target_version = null;

			String source_code = null;
			String source_name = null;
			String rel = null;
			String score = null;
			String target_code = null;
			String target_name = null;
			String target_namespace = null;

			MappingData mappingData = null;

			if (source_abbrev != null && source_abbrev.compareTo("") != 0) {
				source_scheme = DataUtils.getFormalName(source_abbrev);
				source_version = DataUtils.getVocabularyVersionByTag(source_scheme, null);
			} else {
				source_scheme = "UNSPECIFIED";
				source_version = "N/A";
			}

			target_scheme = DataUtils.getFormalName(target_abbrev);
			target_version = DataUtils.getVocabularyVersionByTag(target_scheme, null);

			int counter = 0;

			try {
				  match_list = MappingUtils.process_ncimeta_mapping(ncim_version,
													source_abbrev,
													target_abbrev,
													input_option,
													algorithm,
													input_value);

			} catch (Exception ex) {
				  ex.printStackTrace();
			}


		} else if (type.compareTo("codingscheme") == 0) {

			CodedNodeSet restriction = null;

			String source_scheme = DataUtils.key2CodingSchemeName(source_cs);
			String source_version = DataUtils.key2CodingSchemeVersion(source_cs);

			String target_scheme = DataUtils.key2CodingSchemeName(target_cs);
			String target_version = DataUtils.key2CodingSchemeVersion(target_cs);

			String sourceCodingScheme = DataUtils.getFormalName(source_scheme);
			if (sourceCodingScheme == null) sourceCodingScheme = source_scheme;

			String sourceCodingSchemeVersion = null;
			sourceCodingSchemeVersion = source_version;

			String targetCodingScheme = DataUtils.getFormalName(target_scheme);
			if (targetCodingScheme == null) targetCodingScheme = target_scheme;

			String targetCodingSchemeVersion = null;
			targetCodingSchemeVersion = target_version;

			//ArrayList list = new ArrayList();
			//HashSet hset = new HashSet();
			String source = null;
			boolean ranking = false;

			MappingUtils mapping_utils = null;//
			try {
				mapping_utils = new MappingUtils();
		    } catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}

			int counter = 0;
			String sourceCode = null;
			String matchtext = null;

			if (input_option.compareToIgnoreCase("Code") == 0) {
				sourceCode = input_value;

			} else if (input_option.compareToIgnoreCase("Name") == 0) {
				Vector values = DataUtils.parseData(input_value);
				sourceCode = (String) values.elementAt(0);
				matchtext = (String) values.elementAt(1);
			} else if (input_option.compareToIgnoreCase("Property") == 0) {
				sourceCode = input_value;
			}

			ResolvedConceptReferencesIterator iterator = null;
			String sourceName = "";
			String sourceCodeNamespace = "";
			String rel = "";
			int score = 0;
			String associationName = "mapsTo";

			try {

				Entity source_entity = null;
				if (sourceCodingScheme != null) {
					source_entity = MappingUtils.getConceptByCode(sourceCodingScheme,
														sourceCodingSchemeVersion,
														null,
														sourceCode);
				}
				if (source_entity != null) {
					if (source_entity.getEntityDescription() != null) {
						sourceName = source_entity.getEntityDescription().getContent();
						sourceCodeNamespace = source_entity.getEntityCodeNamespace();
					}
				} else {
						sourceName = matchtext;
						sourceCodeNamespace = "UNSPECIFIED";
						sourceCodingSchemeVersion = "N/A";
						sourceCodeNamespace = "N/A";
				}

				if(input_option.compareToIgnoreCase("Code") == 0) {
					Vector matchtext_vec = new Vector();
					Vector synonyms = DataUtils.getSynonyms(sourceCodingScheme, sourceCodingSchemeVersion, null, sourceCode);
					if (synonyms != null) {
						for (int k=0; k<synonyms.size(); k++) {
							String synonym_str = (String) synonyms.elementAt(k);
							Vector v = DataUtils.parseData(synonym_str);
							String synonym = (String) v.elementAt(0);
							matchtext_vec.add(synonym);
							System.out.println(sourceCode + " " + synonym);
						}
						iterator = new SearchUtils().searchByName(targetCodingScheme, targetCodingSchemeVersion, matchtext_vec, algorithm, restriction);
					}

				} else if(input_option.compareTo("Name") == 0) {
					iterator = mapping_utils.searchByName(targetCodingScheme, targetCodingSchemeVersion, matchtext, null, algorithm, false, -1);

				} else if (sourceCodingScheme != null) {
					Vector src_properties = mapping_utils.getPropertyValues(sourceCodingScheme, sourceCodingSchemeVersion,
																		   sourceCode, src_property);
					Vector matchText_vec = new Vector();
					for (int k=0; k<src_properties.size(); k++) {
						String value = (String) src_properties.elementAt(k);
						value = new SearchUtils().convertPropertyValue(value, right_trim, left_trim, prefix, suffix);
						System.out.println(value);
						matchText_vec.add(value);
					}

					iterator = new SearchUtils().searchByProperty(
							targetCodingScheme, targetCodingSchemeVersion, target_property, matchText_vec, algorithm, restriction);

				}


				if (iterator == null) {
					System.out.println("******************* search returns null???");
				} else {

					try {
						 HashSet key_hset = new HashSet();
						 int numRemaining = iterator.numberRemaining();
						 while (iterator.hasNext()) {
							 ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
							 Entity target_entity = MappingUtils.getConceptByCode(targetCodingScheme, targetCodingSchemeVersion, null, rcr.getConceptCode());

							 String targetCode = rcr.getConceptCode();
							 String targetName = "";
							 String targetCodeNamespace = "";
							 if (target_entity != null) {
								targetName = target_entity.getEntityDescription().getContent();
								targetCodeNamespace = target_entity.getEntityCodeNamespace();
							 }

							 String key = sourceCode + "|" + targetCode;
							 if (!key_hset.contains(key)) {
								 key_hset.add(key);

								 MappingData mappingData = new MappingData(
									sourceCode,
									sourceName,
									sourceCodingScheme,
									sourceCodingSchemeVersion,
									sourceCodeNamespace,
									associationName,
									rel,
									score,
									targetCode,
									targetName,
									targetCodingScheme,
									targetCodingSchemeVersion,
									targetCodeNamespace);
								 match_list.add(mappingData);
							 }
						 }
						 key_hset.clear();

					 } catch (Exception ex) {
						ex.printStackTrace();
					 }
				  }

			  } catch (Exception ex) {
				  ex.printStackTrace();
			  }

		}
		return match_list;
	}
}
