package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.io.*;
import org.LexGrid.relations.Relations;

import org.LexGrid.LexBIG.Utility.Constructors;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;

import java.sql.DriverManager;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.lang.reflect.Array;
import java.text.ParsePosition;
import javax.swing.tree.DefaultMutableTreeNode;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.Impl.History.NCIThesaurusHistoryServiceImpl;
import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.*;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
//import org.LexGrid.codingSchemes.CodingSchemeVersion;
import org.LexGrid.versions.CodingSchemeVersion;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.DataModel.NCIHistory.*;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.versions.SystemRelease;
import org.LexGrid.LexBIG.DataModel.Core.*;
//import org.LexGrid.commonTypes.types.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.concepts.*;
import org.LexGrid.versions.*;
import org.LexGrid.naming.*;
//import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;


import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;


import org.LexGrid.LexBIG.Exceptions.LBException;

//import gov.nih.nci.lexbig.ext.DLBAdapter;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;


import gov.nih.nci.system.applicationservice.*;
import gov.nih.nci.system.client.*;
import gov.nih.nci.evs.security.SecurityToken;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;

//import gov.nih.nci.lexbig.ext.*;
//import gov.nih.nci.lexbig.ext.NavigationUtils;


import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;

import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

import org.LexGrid.naming.SupportedHierarchy;
//import org.LexGrid.codingSchemes.Mappings;
import org.LexGrid.naming.Mappings;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;

import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;


import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;

import org.LexGrid.naming.SupportedEntityType;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;

import org.LexGrid.relations.AssociationPredicate;

import java.util.StringTokenizer;

import org.apache.log4j.*;

import gov.nih.nci.evs.browser.bean.*;


public class MappingUtils {

    public static Vector<String> supportedSearchTechniqueNames;

    public static int MAX_RETURN = -1;

    private static Logger _logger = Logger.getLogger(MappingUtils.class);

	private OutputStream os;
	private File root;
	private File output;
	private String completeSource="";
	private OutputStreamWriter osWriter;
	private BufferedReader buff;

	private LexBIGService lbs;

	static String infilename;
	static String outfilename;

	private String searchText = "11: 107384855-107482789";
    private Vector searchText_vec = null;
    //private String codingSchemeName = "NCI Thesaurus";

    private String codingSchemeName = "HL7 Reference Information Model";// (version: V 02-25)
    private String codingSchemeVersion = "V 02-25"; //V 02-25

    //private String codingSchemeName = "NCI MetaThesaurus";
    private String algorithm = "exactMatch";

    private static Vector filler_vec = null;

    private void initializeTestCases(File inputfile) {
		searchText_vec = new Vector();
		File in = inputfile;
		try{
			BufferedReader buff= new BufferedReader(new FileReader(in));
			String line=buff.readLine();
			while(line != null){
				if (!line.startsWith("#")) {
                	searchText_vec.add(line);
			    }
			    line=buff.readLine();
			}
			buff.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }


    public static String removeFillers(String str) {
		String delim = getDelimiters();
		return removeFillers(str, delim);
	}

    public static String removeFillers(String str, String delim) {
		Vector fillers = getFillers();
		StringTokenizer st = new StringTokenizer(str, delim);
		int knt = 0;
		String retstr = "";
		while(st.hasMoreTokens()) {
			String val = st.nextToken();
			//System.out.println("\t" + val + " " + val.length());
			if (!fillers.contains(val)) {
			    retstr = retstr + " " + val;
			}
		}
		retstr = retstr.trim();
		return retstr;
	}


    public static Vector getFillers() {
		if (filler_vec != null) return filler_vec;
		filler_vec = new Vector();
		filler_vec.add("a");
		filler_vec.add("an");
		filler_vec.add("and");
		filler_vec.add("are");
		filler_vec.add("as");
		filler_vec.add("at");
		filler_vec.add("be");
		filler_vec.add("by");
		filler_vec.add("for");
		filler_vec.add("from");
		filler_vec.add("has");
		filler_vec.add("he");
		filler_vec.add("in");
		filler_vec.add("is");
		filler_vec.add("it");
		filler_vec.add("its");
		filler_vec.add("of");
		filler_vec.add("on");
		filler_vec.add("or");
		filler_vec.add("that");
		filler_vec.add("the");
		filler_vec.add("to");
		filler_vec.add("was");
		filler_vec.add("were");
		filler_vec.add("will");
		filler_vec.add("with");

		return filler_vec;

	}




    private void initializeTestCases() {
		searchText_vec = new Vector();
/*
searchText_vec.add("cis-3,3?,4?,5,7-pentahydroxyflavane 3-gallate");
searchText_vec.add("(10^-6m)3");

		searchText_vec.add("8p11.21");
		searchText_vec.add("Yp11.1-q11.1");
		searchText_vec.add("6q22.33-q24.1");
		searchText_vec.add("3' Flank");
		searchText_vec.add("(17beta)-17-(1-Oxopropoxy)androst-4-en-3-one");
		searchText_vec.add("{Application}");
		//searchText_vec.add("Anatibant (Code C79554)");
		searchText_vec.add("CYP2D6, g.100C>T");
		searchText_vec.add("CYP2D6*10");
		searchText_vec.add("A/He Mouse");
		searchText_vec.add("ELDERLY (> 65)");
		searchText_vec.add("F");
		searchText_vec.add("11: 107384855-107482789");
		searchText_vec.add("CYP2D6, P34S");
		searchText_vec.add("CYP2D6*10 Allele");
		searchText_vec.add("(17beta)-Estra-1,3,5(10)-triene-3,17-diol");
		searchText_vec.add("\"Blue Bone\" Formation");
		searchText_vec.add("D&C");
		searchText_vec.add("(2S)-N-(3-(4-Carbamimidoylbenzamido)propyl)-1-{2,4-dichloro-3-((2,4-dimethyl-8-quinolyloxy)methyl)phenylsulfonyl}pyrrolidine-2-carboxamide");
*/
searchText_vec.add("adverse");
	}

/*
            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
                //Constructors.createSortOptionList(new String[]{"matchToQuery"});
            boolean resolveConcepts = false;
            try {
                // resolve nothing unless sort_by_pt_only is set to false

                if (apply_sort_score && !sort_by_pt_only) resolveConcepts = true;
                try {
                    stopWatch.start();
					long ms = System.currentTimeMillis();
                    iterator = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);
 */


	public MappingUtils() throws Exception {
	    lbs = RemoteServerUtil.createLexBIGService();
	}


	public void lexicalMatchInBatch(File output, String codingSchemeName, String version) throws Exception {

        OutputStreamWriter osWriter = null;
		Vector algorithms = getSupportedSearchTechniqueNames();
		Vector excluded_algorithms = getExcludedAlgorithms();
		String algorithm = null;

		try {
			FileOutputStream os= new FileOutputStream(output);
			osWriter=new OutputStreamWriter(os);
		} catch (Exception ex) {
            System.out.println("\tWARNING: FileOutputStream exception thrown???");
            return;
		}

		try {
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			if (version != null) versionOrTag.setVersion(version);
            LexBIGService lbs = new RemoteServerUtil().createLexBIGService();
			for (int i=0; i<searchText_vec.size(); i++) {
				String t = (String) searchText_vec.elementAt(i);

osWriter.write("\n(******************) Attempting lexical match - " + t);

				int matched_algorithms = lexicalMatch(osWriter, codingSchemeName, version, t);

				if (matched_algorithms == 0) {
                    String t_wo_fillers = removeFillers(t, getDelimiters());

osWriter.write("\n(******************) Attempting match without fillers - " + t_wo_fillers);


                    matched_algorithms = lexicalMatch(osWriter, codingSchemeName, version, t_wo_fillers);
				}

				if (matched_algorithms == 0) {


                    String primaryToken = getPrimaryToken(t, "-");
                    if (primaryToken != null && primaryToken.compareToIgnoreCase(t) != 0) {

osWriter.write("\n(******************) Attempting primary token match - " + primaryToken);


                    	matched_algorithms = lexicalMatch(osWriter, codingSchemeName, version, primaryToken);
					}
				}

		    }
		    osWriter.flush();
		} catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\tWARNING: exception thrown??? " + algorithm);
		}

		osWriter.flush();
		osWriter.close();


        System.out.println("Output file " + outfilename + " generated.");
	}




	public int lexicalMatch(String outputfile, String codingSchemeName, String version, String vbt) throws Exception {

System.out.println("lexical matching outputfile: " + outputfile);
System.out.println("lexical matching codingSchemeName: " + codingSchemeName);

System.out.println("lexical matching version: " + version);

System.out.println("lexical matching vbt: " + vbt);


        OutputStreamWriter osWriter = null;
		try {
			FileOutputStream os = new FileOutputStream(new File(outputfile));
			osWriter = new OutputStreamWriter(os);
			return lexicalMatch(osWriter, codingSchemeName, version, vbt);

		} catch (Exception ex) {
            System.out.println("\tWARNING: FileOutputStream exception thrown???");
            return -1;
		}
	}


	public int lexicalMatch(OutputStreamWriter osWriter, String codingSchemeName, String version, String vbt) throws Exception {
		Vector algorithms = getSupportedSearchTechniqueNames();
		Vector excluded_algorithms = getExcludedAlgorithms();

        int matched_algorithms = 0;
		try {
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			if (version != null) versionOrTag.setVersion(version);

            LexBIGService lbs = new RemoteServerUtil().createLexBIGService();

			osWriter.write("\ncodingSchemeName: " + codingSchemeName);
			String t = vbt;
			osWriter.write("\nSearch string: " + t);

			for (int j=0; j<algorithms.size(); j++) {
				algorithm = (String) algorithms.elementAt(j);
				boolean hasMatch = false;
				osWriter.write("\nAlgorithm: " + algorithm);

				if (!excluded_algorithms.contains(algorithm)) {
					osWriter.write("\n");
					CodedNodeSet cns = null;
					try {
						cns = lbs.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
					} catch (Exception ex) {
						osWriter.write("\tWARNING: getCodingSchemeConcepts throws exception -- algorithm: " + algorithm);
						System.out.println("\tWARNING: getCodingSchemeConcepts throws exception -- algorithm: " + algorithm);

						osWriter.flush();
					}

					if (cns == null) {
						System.out.println("cns == null???");
					} else {
						cns = cns.restrictToMatchingDesignations(t, SearchDesignationOption.ALL, algorithm, null);
						CodedNodeSet.ActiveOption activeOption = CodedNodeSet.ActiveOption.ACTIVE_ONLY;
						cns = cns.restrictToStatus(activeOption, null);

						ResolvedConceptReferencesIterator itr = null;
						int knt = 0;


						LocalNameList restrictToProperties = new LocalNameList();
						SortOptionList sortCriteria = null;
						boolean resolveConcepts = false;

						try {
							itr = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);
							if (itr != null) {
							   while(itr.hasNext()){
									ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
									for(ResolvedConceptReference ref : refs){
										osWriter.write("\n");

										Vector synonyms = getSynonyms(codingSchemeName, ref.getConceptCode());
										boolean matched = validateMatch(t, synonyms, 2);

										String label = "\n\t";
										if (matched) {
											label = "\n\t(*) ";
										}
										displayRef(osWriter, label, ref);

										for (int k=0; k<synonyms.size(); k++) {
											String s = (String) synonyms.elementAt(k);
											try {
												osWriter.write("\n\t\t" + s);
											} catch (Exception ex) {

											}
										}

										knt++;
										if (matched) {
											hasMatch = true;
										}
									}
								}
							}

							if (knt == 0) {
								osWriter.write("\tNo match.");
							} else if (hasMatch) {
								matched_algorithms++;
							}

						} catch (Exception ex) {
							osWriter.write("\tWARNING: cns.resolve throws exception -- algorithm: " + algorithm);
							System.out.println("\tWARNING: cns.resolve throws exception -- algorithm: " + algorithm);
						}

						osWriter.write("\n\n");
						osWriter.flush();
					}
				}

			}

		    osWriter.flush();
		} catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\tWARNING: exception thrown??? " + algorithm);
		} finally {
			osWriter.flush();
			if (osWriter != null) {
				System.out.println("Closing osWriter");
				osWriter.close();
			} else {
				System.out.println("osWriter not open");
			}
		}
		return matched_algorithms;
	}



	public void lexicalMatch(String outputfile, String codingSchemeName, String version, Vector vbt_vec) throws Exception {

System.out.println("lexical matching outputfile: " + outputfile);
System.out.println("lexical matching codingSchemeName: " + codingSchemeName);
System.out.println("lexical matching version: " + version);

        OutputStreamWriter osWriter = null;
		try {
			FileOutputStream os= new FileOutputStream(new File(outputfile));
			osWriter = new OutputStreamWriter(os);
			lexicalMatch(osWriter, codingSchemeName, version, vbt_vec);

		} catch (Exception ex) {
            System.out.println("\tWARNING: FileOutputStream exception thrown???");
		}
System.out.println("Output file " + outputfile + " generated.");

	}


	public void lexicalMatch(OutputStreamWriter osWriter, String codingSchemeName, String version, Vector vbt_vec) throws Exception {
		if (vbt_vec == null) return;

		Vector algorithms = getSupportedSearchTechniqueNames();
		Vector excluded_algorithms = getExcludedAlgorithms();

        int matched_algorithms = 0;
		try {
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			if (version != null) versionOrTag.setVersion(version);

            LexBIGService lbs = new RemoteServerUtil().createLexBIGService();

			osWriter.write("\ncodingSchemeName: " + codingSchemeName);

			for (int lcv=0; lcv<vbt_vec.size(); lcv++) {
				String vbt = (String) vbt_vec.elementAt(lcv);
				String t = vbt.trim();

				osWriter.write("\n==============================================================");
				osWriter.write("\n==   Search string: " + t);
				osWriter.write("\n==============================================================");

				for (int j=0; j<algorithms.size(); j++) {
					algorithm = (String) algorithms.elementAt(j);
					boolean hasMatch = false;
					osWriter.write("\nAlgorithm: " + algorithm);

					if (!excluded_algorithms.contains(algorithm)) {
						osWriter.write("\n");
						CodedNodeSet cns = null;
						try {
							cns = lbs.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
						} catch (Exception ex) {
							osWriter.write("\tWARNING: getCodingSchemeConcepts throws exception -- algorithm: " + algorithm);
							System.out.println("\tWARNING: getCodingSchemeConcepts throws exception -- algorithm: " + algorithm);

							osWriter.flush();
						}

						if (cns == null) {
							System.out.println("cns == null???");
						} else {
							cns = cns.restrictToMatchingDesignations(t, SearchDesignationOption.ALL, algorithm, null);
							CodedNodeSet.ActiveOption activeOption = CodedNodeSet.ActiveOption.ACTIVE_ONLY;
							cns = cns.restrictToStatus(activeOption, null);

							ResolvedConceptReferencesIterator itr = null;
							int knt = 0;


							LocalNameList restrictToProperties = new LocalNameList();
							SortOptionList sortCriteria = null;
							boolean resolveConcepts = false;

							try {
								itr = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);
								if (itr != null) {
								   while(itr.hasNext()){
										ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
										for(ResolvedConceptReference ref : refs){
											osWriter.write("\n");

											Vector synonyms = getSynonyms(codingSchemeName, ref.getConceptCode());
											boolean matched = validateMatch(t, synonyms, 2);

											String label = "\n\t";
											if (matched) {
												label = "\n\t(*) ";
											}
											displayRef(osWriter, label, ref);

											for (int k=0; k<synonyms.size(); k++) {
												String s = (String) synonyms.elementAt(k);
												try {
													osWriter.write("\n\t\t" + s);
												} catch (Exception ex) {

												}
											}

											knt++;
											if (matched) {
												hasMatch = true;
											}
										}
									}
								}

								if (knt == 0) {
									osWriter.write("\tNo match.");
								} else if (hasMatch) {
									matched_algorithms++;
								}

							} catch (Exception ex) {
								osWriter.write("\tWARNING: cns.resolve throws exception -- algorithm: " + algorithm);
								System.out.println("\tWARNING: cns.resolve throws exception -- algorithm: " + algorithm);
							}

							osWriter.write("\n\n");
							osWriter.flush();
						}
					}
				}
				osWriter.flush();
			}
		} catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\tWARNING: exception thrown??? " + algorithm);

		} finally {
			osWriter.flush();
			if (osWriter != null) {
				System.out.println("Closing osWriter");
				osWriter.close();
			} else {
				System.out.println("osWriter not open");
			}
		}


		return;// matched_algorithms;
	}



	public void testList() throws Exception {
		LexBIGService lbs = RemoteServerUtil.createLexBIGService();//LexEVSServiceHolder.instance().getLexEVSAppService();
        for (int i=0; i<searchText_vec.size(); i++) {
			String t = (String) searchText_vec.elementAt(i);
			//System.out.println("Search string: " + t);

			CodedNodeSet cns = lbs.getCodingSchemeConcepts(codingSchemeName, null);
			cns = cns.restrictToMatchingDesignations(t, SearchDesignationOption.ALL, algorithm, null);
			ResolvedConceptReference[] list = null;
			int knt = 0;
			try {
				list = cns.resolveToList(null, null, null, 500).getResolvedConceptReference();

				for(ResolvedConceptReference ref : list){
					displayRef(ref);
					knt++;
				}
			} catch (Exception ex) {
				System.out.println("Exception thrown #2");
				ex.printStackTrace();
			}
			if (knt == 0) {
				//System.out.println("No match.");
			}
			System.out.println("\n\n");
		}
	}

	protected void displayRef(ResolvedConceptReference ref){
		System.out.println(ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
	}

	protected void displayRef(Entity ref){
		System.out.println(ref.getEntityCode() + ":" + ref.getEntityDescription().getContent());
	}

	protected void displayRef(OutputStreamWriter osWriter, ResolvedConceptReference ref){
		try {
			osWriter.write("\t" + ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
		} catch (Exception e) {

		}
	}

	protected void displayRef(OutputStreamWriter osWriter, String label, ResolvedConceptReference ref){
		try {
			osWriter.write(label + ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
			//System.out.println(label + ref.getConceptCode() + ":" + ref.getEntityDescription().getContent());
		} catch (Exception e) {

		}
	}



	protected void displayRef(OutputStreamWriter osWriter, Entity ref){
		try {
			osWriter.write("\t" + ref.getEntityCode() + ":" + ref.getEntityDescription().getContent());
		} catch (Exception e) {

		}
	}




///////////////////////////////////////////////////////////////////////////////////////////////////////////

 	public static Entity getConceptByCode(String codingSchemeName, String vers, String ltag, String code)
	{
        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			if (lbSvc == null)
			{
				System.out.println("lbSvc == null???");
				return null;
			}
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			versionOrTag.setVersion(vers);

			ConceptReferenceList crefs =
				createConceptReferenceList(
					new String[] {code}, codingSchemeName);

			CodedNodeSet cns = null;
			try {
				try {
					cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
				} catch (Exception ex) {
					System.out.println("WARNING: getCodingSchemeConcepts throws exception " + codingSchemeName + " " + vers);
					return null;
				}
				cns = cns.restrictToCodes(crefs);

				ResolvedConceptReferenceList matches = null;
				try {
					matches = cns.resolveToList(null, null, null, 1);
				} catch (Exception e) {
					System.out.println("cns.resolveToList failed???");
				}

				if (matches == null)
				{
					System.out.println("Concep not found.");
					return null;
				}
                int count = matches.getResolvedConceptReferenceCount();
				// Analyze the result ...
				if (count == 0) return null;
				if (count > 0) {
                    try {
					    ResolvedConceptReference ref =
							(ResolvedConceptReference) matches.enumerateResolvedConceptReference().nextElement();
						Entity entry = ref.getReferencedEntry();
						return entry;
					} catch (Exception ex1) {
						System.out.println("Exception entry == null");
						return null;
					}
				}
		    } catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		 } catch (Exception e) {
			 e.printStackTrace();
			 return null;
		 }
		 return null;
	}

    public static ConceptReferenceList createConceptReferenceList(String[] codes, String codingSchemeName)
    {
        if (codes == null)
        {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++)
        {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }
// HL7 Roots


    public Vector getExcludedAlgorithms() {
		Vector u = new Vector();
		u.add("contains");
		u.add("SpellingErrorTolerantSubStringSearch");
		u.add("literalContains");
		u.add("LuceneQuery");
		u.add("LeadingAndTrailingWildcard");
		u.add("StemmedLuceneQuery");


		return u;
	}

    public static ResolvedConceptReferencesIterator searchByName(String scheme, String version, String matchText, String source, String matchAlgorithm, boolean ranking, int maxToReturn) {
		String matchText0 = matchText;
		String matchAlgorithm0 = matchAlgorithm;
		matchText0 = matchText0.trim();
System.out.println("searchByName scheme: " + scheme);
System.out.println("searchByName version: " + version);
System.out.println("searchByName matchText: " + matchText);



		boolean preprocess = true;
        if (matchText == null || matchText.length() == 0)
        {
			return null;
		}

        matchText = matchText.trim();

        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) //p11.1-q11.1  /100{WBC}
		{
			matchAlgorithm = "nonLeadingWildcardLiteralSubString";//Constants.CONTAIN_SEARCH_ALGORITHM; // to be replace by literalSubString
		}

System.out.println("matchAlgorithm: " + matchAlgorithm);


        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null)
            {
                System.out.println("lbSvc = null");
                return null;
            }

            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (version != null) versionOrTag.setVersion(version);
            cns = lbSvc.getNodeSet(scheme, versionOrTag, null);

            if (cns == null)
            {
                System.out.println("cns = null");
                return null;
            }

            //LocalNameList contextList = null;
            try {
				cns = cns.restrictToMatchingDesignations(matchText, SearchDesignationOption.ALL, matchAlgorithm, null);
				//cns = restrictToSource(cns, source);
            } catch (Exception ex) {
                return null;
            }

            LocalNameList restrictToProperties = new LocalNameList();
            //boolean resolveConcepts = true;
            //if (!ranking) resolveConcepts = false;
            boolean resolveConcepts = false;

            SortOptionList sortCriteria = null;

		    if (ranking){
				sortCriteria = Constructors.createSortOptionList(new String[]{"matchToQuery"});

            } else {
                sortCriteria = Constructors.createSortOptionList(new String[] { "entityDescription" }); //code
                System.out.println("*** Sort alphabetically...");
                resolveConcepts = false;
			}
            try {
               try {
					long ms = System.currentTimeMillis(), delay = 0;
                    iterator = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);
					//Debug.println("cns.resolve delay ---- Run time (ms): " + (delay = System.currentTimeMillis() - ms) + " -- matchAlgorithm " + matchAlgorithm);
                    //DBG.debugDetails(delay, "cns.resolve", "searchByName, CodedNodeSet.resolve");

                }  catch (Exception e) {
                    System.out.println("ERROR: cns.resolve throws exceptions.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		/*

        if (iterator == null) {
			System.out.println("*** WARNING: iterator == null.." + scheme);
			iterator = matchConceptCode(scheme, version, matchText0, source, "LuceneQuery");
		} else {
			try {
				int size = iterator.numberRemaining();
				System.out.println("*** number of matches: " + size);
				if (size == 0) {
					iterator = matchConceptCode(scheme, version, matchText0, source, "LuceneQuery");
				}
			} catch (Exception e) {
                e.printStackTrace();
			}
		}
		*/
        return iterator;
    }


    public static CodedNodeSet restrictToSource(CodedNodeSet cns, String source) {
		if (cns == null) return cns;
		if (source == null || source.compareTo("*") == 0 || source.compareTo("") == 0 || source.compareTo("ALL") == 0) return cns;

		LocalNameList contextList = null;
		LocalNameList sourceLnL = null;
		NameAndValueList qualifierList = null;

		Vector<String> w2 = new Vector<String>();
		w2.add(source);
		sourceLnL = vector2LocalNameList(w2);
		LocalNameList propertyLnL = null;
		CodedNodeSet.PropertyType[] types = new CodedNodeSet.PropertyType[] {CodedNodeSet.PropertyType.PRESENTATION};
		try {
			cns = cns.restrictToProperties(propertyLnL, types, sourceLnL, contextList, qualifierList);
		} catch (Exception ex) {
			System.out.println("restrictToSource throws exceptions.");
			return null;
		}
		return cns;
	}

	public static LocalNameList vector2LocalNameList(Vector<String> v) {
		if (v == null)
			return null;
		LocalNameList list = new LocalNameList();
		for (int i = 0; i < v.size(); i++) {
			String vEntry = (String) v.elementAt(i);
			list.addEntry(vEntry);
		}
		return list;
	}

/*
	public ResolvedConceptReferencesIterator matchConceptCode(String scheme, String version, String matchText, String source, String matchAlgorithm) {
		LexBIGService lbs = RemoteServerUtil.createLexBIGService();
		Vector v = new Vector();
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) versionOrTag.setVersion(version);
		CodedNodeSet cns = null;
		ResolvedConceptReferencesIterator iterator = null;
		try {
			cns = lbs.getNodeSet(scheme, versionOrTag, null);
			if (source != null) cns = restrictToSource(cns, source);
			CodedNodeSet.PropertyType[] propertyTypes = null;
			LocalNameList sourceList = null;
			LocalNameList contextList = null;
			NameAndValueList qualifierList = null;
			cns = cns.restrictToMatchingProperties(ConvenienceMethods.createLocalNameList(new String[]{"conceptCode"}),
					  propertyTypes, sourceList, contextList,
					  qualifierList,matchText, matchAlgorithm, null);

            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
            try {
                boolean resolveConcepts = true;
                try {
					long ms = System.currentTimeMillis(), delay = 0;
                    iterator = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);

                    int size = iterator.numberRemaining();
                    System.out.println("cns.resolve size: " + size);

                }  catch (Exception e) {
                    System.out.println("ERROR: cns.resolve throws exceptions.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

		} catch (Exception ex) {
			System.out.println("WARNING: searchByCode throws exception.");
		}
        return iterator;
	}
*/


///////////////////////////////////////////////////////////////////////////////////////////////////////////

/*


    public static Entity getConceptByCode(String codingSchemeName, String vers, String ltag, String code) {

		//System.out.println("coding scheme: " + codingSchemeName);
		//System.out.println("coding scheme  version: " + vers);
		//System.out.println("code: " + code);

        try {
			if (code == null) {
				System.out.println("Input error in DataUtils.getConceptByCode -- code is null.");
				return null;
			}
			if (code.indexOf("@") != -1) return null; // anonymous class

			//String formalname = (String) localName2FormalNameHashMap.get(codingSchemeName);

            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                System.out.println("lbSvc == null???");
                return null;
            }
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (vers != null) versionOrTag.setVersion(vers);

            ConceptReferenceList crefs = createConceptReferenceList(
                    new String[] { code }, codingSchemeName);

            CodedNodeSet cns = null;
            try {
				try {
					//cns = lbSvc.getCodingSchemeConcepts(codingSchemeName,
					//		versionOrTag);

					cns = getNodeSet(lbSvc, codingSchemeName, versionOrTag);

				} catch (Exception e) {
					e.printStackTrace();
				}

                if (cns == null) {
					System.out.println("getConceptByCode getCodingSchemeConcepts returns null??? " + codingSchemeName);
					return null;
				}

                cns = cns.restrictToCodes(crefs);
                //ResolvedConceptReferenceList matches = cns.resolveToList(null, null, null, 1);
 				ResolvedConceptReferenceList matches = null;
				try {
					matches = cns.resolveToList(null, null, null, 1);
				} catch (Exception e) {
					System.out.println("cns.resolveToList failed???");
				}

                if (matches == null) {
                    System.out.println("Concept not found.");
                    return null;
                }
                int count = matches.getResolvedConceptReferenceCount();
                // Analyze the result ...
                if (count == 0)
                    return null;
                if (count > 0) {
                    try {
                        ResolvedConceptReference ref = (ResolvedConceptReference) matches
                                .enumerateResolvedConceptReference()
                                .nextElement();
                        Entity entry = ref.getReferencedEntry();
                        return entry;
                    } catch (Exception ex1) {
                        System.out.println("Exception entry == null");
                        return null;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
*/

	public static ResolvedConceptReferencesIterator matchConceptCode(String scheme, String version, String matchText, String source, String matchAlgorithm) {
		LexBIGService lbs = RemoteServerUtil.createLexBIGService();
		Vector v = new Vector();
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) versionOrTag.setVersion(version);
		CodedNodeSet cns = null;
		ResolvedConceptReferencesIterator iterator = null;
		try {
			cns = lbs.getNodeSet(scheme, versionOrTag, null);
			if (source != null) cns = restrictToSource(cns, source);
			CodedNodeSet.PropertyType[] propertyTypes = null;
			LocalNameList sourceList = null;
			LocalNameList contextList = null;
			NameAndValueList qualifierList = null;
			cns = cns.restrictToMatchingProperties(ConvenienceMethods.createLocalNameList(new String[]{"conceptCode"}),
					  propertyTypes, sourceList, contextList,
					  qualifierList,matchText, matchAlgorithm, null);

            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
            try {
                boolean resolveConcepts = true;
                try {
					long ms = System.currentTimeMillis(), delay = 0;
                    iterator = cns.resolve(sortCriteria, null, restrictToProperties, null, resolveConcepts);

                    int size = iterator.numberRemaining();
                }  catch (Exception e) {
                    System.out.println("Method: matchConceptCode");
                    System.out.println("* ERROR: cns.resolve throws exceptions.");
                    System.out.println("* " + e.getClass().getSimpleName() + ": " +
                        e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

		} catch (Exception ex) {
			System.out.println("WARNING: searchByCode throws exception.");
		}
        return iterator;
	}



	public void testExactMatch(String scheme, String version, String searchText, String algorithm) throws Exception {
System.out.println("\nscheme: " + scheme);
System.out.println("searchText: " + searchText);
System.out.println("algorithm: " + algorithm);

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(version);

		LexBIGService lbs = RemoteServerUtil.createLexBIGService();

		CodedNodeSet cns = lbs.getCodingSchemeConcepts(scheme, versionOrTag);
		cns = cns.restrictToMatchingDesignations(searchText, SearchDesignationOption.ALL, algorithm, null);
		ResolvedConceptReference[] list = null;
        int knt = 0;
		try {
		    list = cns.resolveToList(null, null, null, 500).getResolvedConceptReference();

			for(ResolvedConceptReference ref : list){
				displayRef(ref);
				knt++;
			}
		} catch (Exception ex) {
			System.out.println("Exception thrown #2");
		}
		if (knt == 0) System.out.println("No match.");
	}




    public Vector<String> getSupportedSearchTechniqueDescriptions() {
		Vector u = new Vector();
		Vector<ModuleDescription> v = getSupportedSearchTechniques();
		for (int i=0; i<v.size(); i++) {
			ModuleDescription md = (ModuleDescription) v.elementAt(i);
			u.add(md.getName() + "|" + md.getDescription());
		}

		return u;
	}


    public static Vector<String> getAllSupportedSearchTechniqueNames() {
		if (supportedSearchTechniqueNames != null) return supportedSearchTechniqueNames;

		supportedSearchTechniqueNames = new Vector();
		Vector<ModuleDescription> v = getAllSupportedSearchTechniques();
		for (int i=0; i<v.size(); i++) {
			ModuleDescription md = (ModuleDescription) v.elementAt(i);
			supportedSearchTechniqueNames.add(md.getName());
		}
		supportedSearchTechniqueNames = SortUtils.quickSort(supportedSearchTechniqueNames);
		return supportedSearchTechniqueNames;
	}


    public static Vector<ModuleDescription> getAllSupportedSearchTechniques()
	{
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		Vector<ModuleDescription> v = new Vector<ModuleDescription>();
		try {

			//ExtensionDescriptionList list = lbSvc.getMatchAlgorithms();
			ModuleDescriptionList list = lbSvc.getMatchAlgorithms();

			if (list == null)
			{
				System.out.println("WARNING: ModuleDescriptionList.getMatchAlgorithms returns null.");
			    return v;
			}

			//ExtensionDescription[] eda = list.getExtensionDescription();
			ModuleDescription[] eda = list.getModuleDescription();
			if (eda == null)
			{
				System.out.println("WARNING: ModuleDescriptionList.getModuleDescription returns null.");
			    return v;
			}

			for (int i=0; i<eda.length; i++)
			{
				//ExtensionDescription ed = (ExtensionDescription) eda[i];
				ModuleDescription ed = (ModuleDescription) eda[i];

				//v.add(ed.getExtensionName());
				v.add(ed);
			}
	    } catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return v;
    }



    public Vector<String> getSupportedSearchTechniqueNames() {
		Vector u = new Vector();
		Vector<ModuleDescription> v = getSupportedSearchTechniques();
		for (int i=0; i<v.size(); i++) {
			ModuleDescription md = (ModuleDescription) v.elementAt(i);
			u.add(md.getName());
		}
		return SortUtils.quickSort(u);
	}




    public Vector<ModuleDescription> getSupportedSearchTechniques()
	{
		LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
		if (lbSvc == null)
		{
			lbSvc = new RemoteServerUtil().createLexBIGService();
		}

		Vector<ModuleDescription> v = new Vector<ModuleDescription>();
		try {

			//ExtensionDescriptionList list = lbSvc.getMatchAlgorithms();
			ModuleDescriptionList list = lbSvc.getMatchAlgorithms();

			if (list == null)
			{
				System.out.println("WARNING: ModuleDescriptionList.getMatchAlgorithms returns null.");
			    return v;
			}

			//ExtensionDescription[] eda = list.getExtensionDescription();
			ModuleDescription[] eda = list.getModuleDescription();
			if (eda == null)
			{
				System.out.println("WARNING: ModuleDescriptionList.getModuleDescription returns null.");
			    return v;
			}

			for (int i=0; i<eda.length; i++)
			{
				//ExtensionDescription ed = (ExtensionDescription) eda[i];
				ModuleDescription ed = (ModuleDescription) eda[i];

				//v.add(ed.getExtensionName());
				v.add(ed);
			}
	    } catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return v;
    }


    public static String getDelimiters() {

		return "(-)";
	}





    public static void testStringTokenizer(String in, String delim) {
		Vector fillers = getFillers();
		//System.out.println("String: " + in);
		//System.out.println("delim: " + delim);

		StringTokenizer st = new StringTokenizer(in, delim);
		while(st.hasMoreTokens()) {
			String val = st.nextToken();
			//System.out.println("\t" + val);
		}
		//System.out.println("\n");
	}



	public static Vector getSynonyms(String scheme, String code) {
		return getSynonyms(scheme, null, null, code);
	}


	public static Vector getSynonyms(String scheme, String version, String tag, String code) {
		Vector v = new Vector();
		Entity concept = getConceptByCode(scheme, version, tag, code);
/*
		if (concept != null) {
			System.out.println(concept.getEntityDescription().getContent());
		}
*/
		// KLO, 091009
		//getSynonyms(concept);
		return getSynonyms(scheme, concept);
	}

	public static Vector getSynonyms(String scheme, Entity concept) {
		if (concept == null)
			return null;
		Vector v = new Vector();
		Presentation[] properties = concept.getPresentation();
		int n = 0;
		for (int i = 0; i < properties.length; i++) {
			Presentation p = properties[i];
			String term_name = p.getValue().getContent();
			//System.out.println("Presentation: " + term_name);

			String term_type = "null";
			String term_source = "null";
			String term_source_code = "null";

			PropertyQualifier[] qualifiers = p.getPropertyQualifier();
			if (qualifiers != null) {
				//System.out.println("\tqualifiers.length: " + qualifiers.length);
				for (int j = 0; j < qualifiers.length; j++) {
					PropertyQualifier q = qualifiers[j];
					String qualifier_name = q.getPropertyQualifierName();
					String qualifier_value = q.getValue().getContent();
					//System.out.println("\tqualifier_name: " + qualifier_name);
					//System.out.println("\tqualifier_value: " + qualifier_value);
					if (qualifier_name.compareTo("source-code") == 0) {
						term_source_code = qualifier_value;
						break;
					}
				}
			}
			term_type = p.getRepresentationalForm();
			Source[] sources = p.getSource();
			if (sources != null && sources.length > 0) {
				Source src = sources[0];
				term_source = src.getContent();
			}
			v.add(term_name + "|" + term_type + "|" + term_source + "|"
					+ term_source_code);
		}
		SortUtils.quickSort(v);
		return v;
	}


	public boolean outputSynonyms(OutputStreamWriter osWriter, String vbt, String scheme, String code) {
		Vector synonyms = getSynonyms(scheme, code);
		for (int i=0; i<synonyms.size(); i++) {
			String t = (String) synonyms.elementAt(i);
			try {
				osWriter.write("\n\t\t" + t);
			} catch (Exception ex) {

			}
		}
//validateMatch(String vbt, Vector synonyms, int matchLevel)
		boolean retval = validateMatch(vbt, synonyms, 2);
		if (retval) {
			try {
				osWriter.write("\n\t\t(*) Matched found.");
			} catch (Exception ex) {

			}
		} else {
			try {
				osWriter.write("\n\t\t(*) Matched not found.");
			} catch (Exception ex) {

			}
		}
		return retval;
	}


	public static Vector<String> parseData(String line) {
		String tab = "|";
		return parseData(line, tab);
	}

    public static Vector<String> parseData(String line, String tab) {
        Vector data_vec = new Vector();
        StringTokenizer st = new StringTokenizer(line, tab);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value.compareTo("null") == 0)
                value = " ";
            data_vec.add(value);
        }
        return data_vec;
    }



	public String getSynonym(String delimed_str) {
		Vector u = parseData(delimed_str) ;
		if (u == null) return null;
		return (String) u.elementAt(0);
	}



	public boolean validateMatch(String vbt, Vector synonyms) {
		boolean retval = false;
		for (int i=0; i<synonyms.size(); i++) {
		    String delimited_syn = (String) synonyms.elementAt(i);
		    String syn = getSynonym(delimited_syn);
		    retval = matchTokenSet(vbt, syn);
		    if (retval) return true;
	    }
	    return false;
	}


	public boolean validateMatch(String vbt, Vector synonyms, int matchLevel) {
		boolean retval = false;
		for (int i=0; i<synonyms.size(); i++) {
		    String delimited_syn = (String) synonyms.elementAt(i);
		    String syn = getSynonym(delimited_syn);
		    if (matchLevel == 1) {
				retval = matchTokenSequence(vbt, syn);
				if (retval) return true;
			} else {
				retval = matchTokenSet(vbt, syn);
				if (retval) return true;
			}
	    }
	    return false;
	}



	public static Vector getTokens(String vbt) {
		String t = removeFillers(vbt);
		Vector v = new Vector();
		StringTokenizer st = new StringTokenizer(t, " ");
		int knt = 0;
		String retstr = "";
		while(st.hasMoreTokens()) {
			String val = st.nextToken();
			val = val.toLowerCase();
			v.add(val);
		}

		return v;
	}


	public boolean matchTokenSet(String vbt, String matched_str) {
		Vector v = getTokens(vbt);
//System.out.println("\nvbt: " + vbt);
for (int i=0; i<v.size(); i++) {
	String s = (String) v.elementAt(i);
	int j=i+1;
	//System.out.println("(" + j + ") vbt token: " + s);
}


		Vector u = getTokens(matched_str);
//System.out.println("\nmatched_str: " + matched_str);
for (int i=0; i<u.size(); i++) {
	String s = (String) u.elementAt(i);
	int j=i+1;
	//System.out.println("(" + j + ") matched_str token: " + s);
}

		if (v.size() != u.size()) {
			//System.out.println("wrong length - no match");
			return false;
		}
		for (int i=0; i<v.size(); i++) {
			String s = (String) v.elementAt(i);
			if (!u.contains(s)) {
				//System.out.println(" -- token not found " + s);
				return false;
			}
		}
        return true;
	}


	public boolean matchTokenSequence(String vbt, String matched_str) {
		Vector v = getTokens(vbt);
//System.out.println("\nvbt: " + vbt);
for (int i=0; i<v.size(); i++) {
	String s = (String) v.elementAt(i);
	int j=i+1;
	//System.out.println("(" + j + ") vbt token: " + s);
}


		Vector u = getTokens(matched_str);
//System.out.println("\nmatched_str: " + matched_str);
for (int i=0; i<u.size(); i++) {
	String s = (String) u.elementAt(i);
	int j=i+1;
	//System.out.println("(" + j + ") matched_str token: " + s);
}



		if (v.size() != u.size()) {
			System.out.println("wrong length - no match");
			return false;
		}
		for (int i=0; i<v.size(); i++) {
			String s = (String) v.elementAt(i);
			String t = (String) u.elementAt(i);
			if (s.compareTo(t) != 0) {
				System.out.println(" -- token mismatch " + s + " " + t);
				return false;
			}
		}
        return true;
	}



    public static String getPrimaryToken(String in, String delim) {
		System.out.println("String: " + in);
		System.out.println("delim: " + delim);

        String primaryToken = "";
        int primaryTokenLength = 0;

		StringTokenizer st = new StringTokenizer(in, delim);
		while(st.hasMoreTokens()) {
			String val = st.nextToken();
			//System.out.println("\t" + val);
			if (val.length() > primaryTokenLength) {
				primaryToken = val;
				primaryTokenLength = val.length();
			}
		}
		if (primaryTokenLength == 0) return null;
		return primaryToken;
	}


    public static Vector getPropertyValues(Entity concept,
            String property_type, String property_name) {
        Vector v = new Vector();
        org.LexGrid.commonTypes.Property[] properties = null;

		if (property_type.compareToIgnoreCase("GENERIC") == 0) {
			properties = concept.getProperty();
		} else if (property_type.compareToIgnoreCase("PRESENTATION") == 0) {
			properties = concept.getPresentation();
		} else if (property_type.compareToIgnoreCase("COMMENT") == 0) {
			properties = concept.getComment();
		} else if (property_type.compareToIgnoreCase("DEFINITION") == 0) {
			properties = concept.getDefinition();
		} else {

            System.out.println("WARNING: property_type not found -- "
                    + property_type);
        }

		if (properties == null || properties.length == 0)
			return v;
		for (int i = 0; i < properties.length; i++) {
			Property p = (Property) properties[i];
			if (property_name.compareTo(p.getPropertyName()) == 0) {
				String t = p.getValue().getContent();

				Source[] sources = p.getSource();
				if (sources != null && sources.length > 0) {
					Source src = sources[0];
					t = t + "|" + src.getContent();

				}
				v.add(t);
			}
		}
		return v;
	}


//===================================================================================

    public Vector getSupportedPropertyNames(String codingschemename, String version) {
		try {
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			if (version != null) versionOrTag.setVersion(version);

            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

			CodingScheme cs = resolveCodingScheme(lbSvc, codingschemename, versionOrTag);
			if (cs == null) return null;
			return getSupportedPropertyNames(cs);

        } catch (Exception ex) {
            System.out.println("(*) Unable to resolveCodingScheme " + codingschemename);
            System.out.println("(*) \tMay require security token. ");

        }
        return null;
	}



    public static CodingScheme resolveCodingScheme(LexBIGService lbSvc,
        String codingschemename, CodingSchemeVersionOrTag versionOrTag) {
        try {
            CodingScheme cs =
                lbSvc.resolveCodingScheme(codingschemename, versionOrTag);
            return cs;
        } catch (Exception ex) {
            System.out.println("(*) Unable to resolveCodingScheme " + codingschemename);
            System.out.println("(*) \tMay require security token. ");

        }
        return null;
    }



    public Vector<SupportedProperty> getSupportedProperties(CodingScheme cs)
	{
		if (cs == null) return null;
        Vector<SupportedProperty> v = new Vector<SupportedProperty>();
	    SupportedProperty[] properties = cs.getMappings().getSupportedProperty();
		for (int i=0; i<properties.length; i++)
		{
		     SupportedProperty sp = (SupportedProperty) properties[i];
		     v.add(sp);
		}
        return v;
	}

    public Vector<String> getSupportedPropertyNames(CodingScheme cs)
	{
        Vector w = getSupportedProperties(cs);
		if (w == null) return null;

        Vector<String> v = new Vector<String>();
		for (int i=0; i<w.size(); i++)
		{
		     SupportedProperty sp = (SupportedProperty) w.elementAt(i);
		     /*
		     if (sp == null)
		     {
				 pw.println("SupportedProperty == null");
			 }
			 else if (sp.getLocalId() == null)
			 {
			     pw.println("SupportedProperty.getLocalId() == null");
			 }
			 */

		     v.add(sp.getLocalId());
		}
        return v;
	}

//===================================================================================

/*

    public static ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }
*/


/*
    public static Entity getConceptByCode(String scheme, String version, String code) {

        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            if (lbSvc == null) {
                //_logger
                //    .warn("WARNING: Unable to connect to instantiate LexBIGService ???");
                return null;
            }
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			if (version != null)
				versionOrTag.setVersion(version);
			CodedNodeSet cns = getNodeSet(lbSvc, scheme, versionOrTag);
			CodedNodeSet.PropertyType[] propertyTypes = null;
			ConceptReferenceList crefs =
				createConceptReferenceList(new String[] { code }, scheme);
			cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
			cns = cns.restrictToCodes(crefs);
			boolean resolveConcepts = false;
			ResolvedConceptReferencesIterator iterator = null;
			SortOptionList sortCriteria = null;
			iterator = cns.resolve(sortCriteria, null, null, null, resolveConcepts);
			if (iterator == null) return null;
			while (iterator.hasNext()) {
				ResolvedConceptReference[] refs =
					iterator.next(100).getResolvedConceptReference();
				for (ResolvedConceptReference ref : refs) {
					if (ref.getReferencedEntry().getEntityCode().equals(
						code)) {
						return ref.getReferencedEntry();
					}
				}
		    }
        } catch (Exception e) {
            //_logger.error("Method: SearchUtil.matchConceptByCode");
           // _logger.error("* ERROR: cns.resolve throws exceptions.");
           // _logger.error("* " + e.getClass().getSimpleName() + ": "
            //    + e.getMessage());
        }
        return null;
    }
*/
     public static CodedNodeSet getNodeSet(LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag versionOrTag)
        throws Exception {
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
			CodedNodeSet.AnonymousOption restrictToAnonymous = CodedNodeSet.AnonymousOption.NON_ANONYMOUS_ONLY;
			cns = cns.restrictToAnonymous(restrictToAnonymous);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}

 	    return cns;
    }


    public static ResolvedConceptReferencesIterator getConceptWithPropertyQualifierMatching(
		                                                         String scheme,
		                                                         String version,
                                                                 PropertyType propertyType,
                                                                 String sourceAbbr,
                                                                 String qualifiername,
                                                                 String qualifiervalue,
                                                                 int    maxToReturn,
                                                                 boolean searchInactive) {

        if (qualifiervalue == null)
            return null;
        ResolvedConceptReferencesIterator matchIterator = null;

        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        if (lbSvc == null) {
            _logger.warn("lbSvc = null");
            return null;
        }

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

        Vector<String> v = null;

        if (qualifiervalue != null && qualifiervalue.compareTo("") != 0) {
            qualifierList = new NameAndValueList();
            NameAndValue nv = new NameAndValue();
            nv.setName(qualifiername);
            nv.setContent(qualifiervalue);
            qualifierList.addNameAndValue(nv);
        }

        LocalNameList propertyLnL = null;
        // sourceLnL
        Vector<String> w2 = new Vector<String>();
        w2.add(sourceAbbr);
        LocalNameList sourceLnL = vector2LocalNameList(w2);
        if (sourceAbbr.compareTo("*") == 0
            || sourceAbbr.compareToIgnoreCase("ALL") == 0) {
            sourceLnL = null;
        }

        SortOptionList sortCriteria = null;// Constructors.createSortOptionList(new
                                           // String[]{"matchToQuery", "code"});
        try {

			CodedNodeSet cns = getNodeSet(lbSvc, scheme, versionOrTag);
           if (cns == null) {
                _logger.warn("lbSvc.getCodingSchemeConceptsd returns null");
                return null;
            }
            CodedNodeSet.PropertyType[] types =
                new PropertyType[] { propertyType };
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);

            if (cns != null) {
                boolean activeOnly = !searchInactive;
                cns = restrictToActiveStatus(cns, activeOnly);

                try {
                    matchIterator = cns.resolve(sortCriteria, null, null);
                } catch (Exception ex) {

                }
            }

        } catch (Exception e) {
            return null;
        }
        return matchIterator;
	}

    private static CodedNodeSet restrictToActiveStatus(CodedNodeSet cns,
        boolean activeOnly) {
        if (cns == null)
            return null;
        if (!activeOnly)
            return cns; // no restriction, do nothing
        try {
            cns =
                cns.restrictToStatus(CodedNodeSet.ActiveOption.ACTIVE_ONLY,
                    null);
            return cns;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
/*
    public static CodedNodeSet restrictToSource(CodedNodeSet cns, String source) {
        if (cns == null)
            return cns;
        if (source == null || source.compareTo("*") == 0
            || source.compareTo("") == 0 || source.compareTo("ALL") == 0)
            return cns;

        LocalNameList contextList = null;
        LocalNameList sourceLnL = null;
        NameAndValueList qualifierList = null;

        Vector<String> w2 = new Vector<String>();
        w2.add(source);
        sourceLnL = vector2LocalNameList(w2);
        LocalNameList propertyLnL = null;
        CodedNodeSet.PropertyType[] types =
            new CodedNodeSet.PropertyType[] { CodedNodeSet.PropertyType.PRESENTATION };
        try {
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);
        } catch (Exception ex) {
            _logger.error("restrictToSource throws exceptions.");
            return null;
        }
        return cns;
    }
*/
    private static CodedNodeSet.PropertyType[] getAllPropertyTypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[4];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        propertyTypes[3] = PropertyType.PRESENTATION;
        return propertyTypes;
    }

    private static CodedNodeSet.PropertyType[] getAllNonPresentationPropertyTypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[3];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        return propertyTypes;
    }

    public static String findBestContainsAlgorithm(String matchText) {
        if (matchText == null)
            return "nonLeadingWildcardLiteralSubString";
        matchText = matchText.trim();
        if (matchText.length() == 0)
            return "nonLeadingWildcardLiteralSubString"; // or null
        if (matchText.length() > 1)
            return "nonLeadingWildcardLiteralSubString";
        char ch = matchText.charAt(0);
        if (Character.isDigit(ch))
            return "literal";
        else if (Character.isLetter(ch))
            return "LuceneQuery";
        else
            return "literalContains";
    }


    public static ResolvedConceptReferencesIterator searchByProperties(
        String scheme, String version, String matchText,
        String[] property_types, String[] property_names, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean matchToQuerySort,
        int maxToReturn) {
        boolean restrictToProperties = false;
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList propertyNames = null;
        LocalNameList sourceList = null;
        if (property_types != null || property_names != null) {
            _logger
                .debug("Case #1 -- property_types != null || property_names != null");

            restrictToProperties = true;
            if (property_types != null)
                propertyTypes = createPropertyTypes(property_types);
            if (property_names != null)
                propertyNames =
                    ConvenienceMethods.createLocalNameList(property_names);
        } else {
            _logger.debug("Case #2 ");
            propertyNames = new LocalNameList();
            propertyTypes = getAllPropertyTypes();
            if (excludeDesignation) {
                _logger.debug("Case #3 ");
                propertyTypes = getAllNonPresentationPropertyTypes();
            }
        }
        _logger.debug("searchByProperties scheme: " + scheme);
        _logger.debug("searchByProperties matchAlgorithm: " + matchAlgorithm);
        _logger.debug("searchByProperties source: " + source);
        _logger.debug("searchByProperties excludeDesignation: "
            + excludeDesignation);

        if (source != null)
            sourceList =
                ConvenienceMethods.createLocalNameList(new String[] { source });
        NameAndValueList qualifierList = null;

        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();
        if (matchText == null || matchText.length() == 0) {
            return null;
        }
        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            cns = null;
            iterator = null;
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            try {
                if (lbSvc == null) {
                    _logger.warn("lbSvc = null");
                    return null;
                }

                cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
                if (cns != null) {
                    try {
                        String language = null;
                        try {
                            _logger
                                .debug("*** restrictToMatchingProperties ...matchText "
                                    + matchText
                                    + " matchAlgorithm "
                                    + matchAlgorithm);


                            cns =
                                cns.restrictToMatchingProperties(propertyNames,
                                    propertyTypes, matchText, matchAlgorithm,
                                    language);

                        } catch (Exception e) {
                            _logger
                                .debug("\t(*) restrictToMatchingProperties throws exceptions???: "
                                    + matchText
                                    + " matchAlgorithm: "
                                    + matchAlgorithm);
                            // e.printStackTrace();
                        }
                        try {
                            _logger.debug("restrictToSource ...source "
                                + source);
                            cns = restrictToSource(cns, source);
                        } catch (Exception e) {
                            _logger
                                .error("\t(*) restrictToSource throws exceptions???: "
                                    + matchText
                                    + " matchAlgorithm: "
                                    + matchAlgorithm);
                            // e.printStackTrace();
                        }

                    } catch (Exception ex) {
                        // ex.printStackTrace();
                        _logger
                            .error("\t(*) searchByProperties2 throws exceptions.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // return null;
            }
            iterator = null;
            if (cns == null) {
                return null;
            }
            // LocalNameList restrictToProperties = null;//new LocalNameList();
            boolean resolveConcepts = false;
            SortOptionList sortCriteria = null;
            if (matchToQuerySort) {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                _logger.debug("*** Sort alphabetically...");
                resolveConcepts = false;
            }
            try {
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    cns = restrictToSource(cns, source);
                    _logger.debug("cns.resolve ...");
                    iterator =
                        cns.resolve(sortCriteria, null, null, null,
                            resolveConcepts);

                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.searchByProperties");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return iterator;
    }

    public ResolvedConceptReferencesIterator searchByProperties(
        String scheme, String version, String matchText, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {
        return searchByProperties(scheme, version, matchText, null, null,
            source, matchAlgorithm, excludeDesignation, ranking, maxToReturn);

    }

    public static CodedNodeSet.PropertyType[] createPropertyTypes(String[] types) {
        if (types == null || types.length == 0)
            return null;
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[types.length];
        int knt = -1;
        for (int i = 0; i < types.length; i++) {
            String type = (String) types[i];
            if (type.compareToIgnoreCase("PRESENTATION") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.PRESENTATION;
            } else if (type.compareToIgnoreCase("DEFINITION") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.DEFINITION;
            } else if (type.compareToIgnoreCase("GENERIC") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.GENERIC;
            } else if (type.compareToIgnoreCase("COMMENT") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.COMMENT;
            }
        }
        return propertyTypes;
    }


    public void dumpIterator(ResolvedConceptReferencesIterator itr) {
        try {
            while (itr.hasNext()) {
                try {
                    ResolvedConceptReference[] refs =
                        itr.next(100).getResolvedConceptReference();
                    for (ResolvedConceptReference ref : refs) {
                        displayRef(ref);
                    }
                } catch (Exception ex) {
                    break;
                }
            }
        } catch (Exception ex) {

        }
    }

/*
    public void dumpConceptDetails(String scheme, String version, ResolvedConceptReferencesIterator itr) {
		dumpConceptDetails(scheme, version, itr, false);
	}

    public void dumpConceptDetails(String scheme, String version, ResolvedConceptReferencesIterator itr, boolean show_details) {
        try {
            while (itr.hasNext()) {
                try {
                    ResolvedConceptReference[] refs =
                        itr.next(100).getResolvedConceptReference();
                    for (ResolvedConceptReference ref : refs) {
						if (show_details) {
							new ConceptDetails().getConceptDetails(scheme, version, ref.getConceptCode());
						} else {
							displayRef(ref);
						}
                    }
                } catch (Exception ex) {
                    break;
                }
            }
        } catch (Exception ex) {

        }
    }
*/


// In the event of no match:
//     Choose the token with the least patial matches (contains search). (the most significant token rule)
//     returned the list of partial matches.



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// matching through NCI Metathesaurus
// <source abbr, source code> --> metathesuaurus CUI --> check if <target source abbr, target source code> exists in CUI

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// search against an existing mapping
// <source coding scheme, source concept code> --> <target coding scheme, target concept code> Check if exists in the mapping?

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
(15) NCI Metathesaurus

	RepresentsVersion: 201101
	Blood (CUI C0005767)
	NCI PT C12434
	RADLEX PT RID1543
	MGED DPT MO_409


*/


    public static ResolvedConceptReferencesIterator findConceptWithSourceCodeMatching(
        String scheme, String version, String sourceAbbr, String code,
        int maxToReturn, boolean searchInactive) {
        if (sourceAbbr == null || code == null)
            return null;
        ResolvedConceptReferencesIterator matchIterator = null;

        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        if (lbSvc == null) {
            _logger.warn("lbSvc = null");
            return null;
        }

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

        Vector<String> v = null;

        if (code != null && code.compareTo("") != 0) {
            qualifierList = new NameAndValueList();
            NameAndValue nv = new NameAndValue();
            nv.setName("source-code");
            nv.setContent(code);
            qualifierList.addNameAndValue(nv);
        }

        LocalNameList propertyLnL = null;
        // sourceLnL
        Vector<String> w2 = new Vector<String>();
        w2.add(sourceAbbr);
        LocalNameList sourceLnL = vector2LocalNameList(w2);
        if (sourceAbbr.compareTo("*") == 0
            || sourceAbbr.compareToIgnoreCase("ALL") == 0) {
            sourceLnL = null;
        }

        SortOptionList sortCriteria = null;// Constructors.createSortOptionList(new
                                           // String[]{"matchToQuery", "code"});
        try {
            CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, null);
            if (cns == null) {
                _logger.warn("lbSvc.getCodingSchemeConceptsd returns null");
                return null;
            }
            CodedNodeSet.PropertyType[] types =
                new PropertyType[] { PropertyType.PRESENTATION };
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);

            if (cns != null) {
                boolean activeOnly = !searchInactive;
                cns = restrictToActiveStatus(cns, activeOnly);

                try {
                    matchIterator = cns.resolve(sortCriteria, null, null);// ConvenienceMethods.createLocalNameList(getPropertyForCodingScheme(cs)),null);
                } catch (Exception ex) {

                }
            }

        } catch (Exception e) {
            return null;
        }
        return matchIterator;
    }


    public static List process_ncimeta_mapping(String ncim_version,
                                        String source_abbrev,
                                        String target_abbrev,
                                        String input_option,
                                        String algorithm,
                                        String t) {
		String[] data = new String[1];
		data[0] = t;
		return process_ncimeta_mapping(ncim_version,source_abbrev,target_abbrev,input_option,algorithm,data);
	}



    public static List process_ncimeta_mapping(String ncim_version,
                                        String source_abbrev,
                                        String target_abbrev,
                                        String input_option,
                                        String algorithm,
                                        String[] data) {
		if (data == null) return null;
		if (data.length == 0) return null;

		String sourceCodingScheme = "UNSPECIFIED";
		String sourceCodingSchemeVersion = "N/A";

		if (source_abbrev.compareTo("") != 0) {
		    sourceCodingScheme = DataUtils.getFormalName(source_abbrev);
		    sourceCodingSchemeVersion = DataUtils.getVocabularyVersionByTag(sourceCodingScheme, null);
		}


		String targetCodingScheme = DataUtils.getFormalName(target_abbrev);
		String targetCodingSchemeVersion = DataUtils.getVocabularyVersionByTag(targetCodingScheme, null);

		ArrayList list = new ArrayList();
		HashSet hset = new HashSet();
		String source = null;
		boolean ranking = false;

		for (int i=0; i<data.length; i++) {
            String code = (String) data[i];
            String name = (String) data[i];
            ResolvedConceptReferencesIterator iterator = null;

            if(input_option.compareToIgnoreCase("Name") == 0) {
				iterator = searchByName("NCI Metathesaurus", ncim_version, name, source, algorithm, ranking, MAX_RETURN);
				if (iterator == null) return null;
				HashSet source_code_hset = new HashSet();
				HashSet target_code_hset = new HashSet();

				try {
					 while (iterator.hasNext()) {
						 ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
						 Entity meta_entity = getConceptByCode("NCI Metathesaurus", ncim_version, null, rcr.getConceptCode());
						 Vector v = DataUtils.getSynonyms(meta_entity);
						 if (v != null) {
							 for (int j=0; j<v.size(); j++) {
								 String syn = (String) v.elementAt(j);
								 Vector u = DataUtils.parseData(syn);
								 //term_name + "|" + term_type + "|" + term_source + + term_source_code;
								 if (u.size() == 4) {
									 String term_name = (String) u.elementAt(0);
									 String term_type = (String) u.elementAt(1);
									 String term_source = (String) u.elementAt(2);
									 String term_source_code = (String) u.elementAt(3);
									 if (source_abbrev != null && source_abbrev.compareTo("") != 0) {
										 if (term_source.compareTo(source_abbrev) == 0 && !source_code_hset.contains(term_source_code))
										 {
											 source_code_hset.add(term_source_code);
										 }
									 }

									 if (term_source.compareTo(target_abbrev) == 0 && !target_code_hset.contains(term_source_code)) {
										 target_code_hset.add(term_source_code);
									 }
								 }
							 }
						 }

						 if (source_abbrev == null || source_abbrev.compareTo("") == 0) {
							 if (target_code_hset.size() > 0) {
								 String sourceCode = "N/A";
								 String sourceName = name;
								 String sourceCodeNamespace = "UNSPECIFIED";
								 sourceCodeNamespace = "N/A";
								 String associationName = "mapsTo";
								 String rel = "";
								 int score = 0;

								 Iterator it_target = target_code_hset.iterator();
								 while (it_target.hasNext()) {
									 String targetCode = (String) it_target.next();
									 Entity target_entity = getConceptByCode(targetCodingScheme,
																			targetCodingSchemeVersion,
																			null,
																			targetCode);
									 String targetName = "";
									 String targetCodeNamespace = "";
									 if (target_entity != null) {
										targetName = target_entity.getEntityDescription().getContent();
										targetCodeNamespace = target_entity.getEntityCodeNamespace();
									 }


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
									 list.add(mappingData);
						    	 }
							 }

						 } else if (source_abbrev != null && source_abbrev.compareTo("") != 0) {
							 if (source_code_hset.size() > 0 && target_code_hset.size() > 0) {
								 Iterator it_source = source_code_hset.iterator();
								 while (it_source.hasNext()) {
									 String sourceCode = (String) it_source.next();

									 Entity source_entity = getConceptByCode(sourceCodingScheme,
																			sourceCodingSchemeVersion,
																			null,
																			sourceCode);
									 String sourceName = "";
									 String sourceCodeNamespace = "";
									 if (source_entity != null) {
										sourceName = source_entity.getEntityDescription().getContent();
										sourceCodeNamespace = source_entity.getEntityCodeNamespace();
									 }
									 String associationName = "mapsTo";
									 String rel = "";
									 int score = 0;

									 Iterator it_target = target_code_hset.iterator();
									 while (it_target.hasNext()) {
										 String targetCode = (String) it_target.next();
										 Entity target_entity = getConceptByCode(targetCodingScheme,
																				targetCodingSchemeVersion,
																				null,
																				targetCode);
										 String targetName = "";
										 String targetCodeNamespace = "";
										 if (target_entity != null) {
											targetName = target_entity.getEntityDescription().getContent();
											targetCodeNamespace = target_entity.getEntityCodeNamespace();
										 }


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
										 list.add(mappingData);
									 }

								 }
							 }
						 }
					 }

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else {

				//System.out.println("Calling findConceptWithSourceCodeMatching ...");
				//System.out.println("\tncim_version ..." + ncim_version);
				//System.out.println("\tsource_abbrev ..." + source_abbrev);
				//System.out.println("\tcode ..." + code);

				try {
					iterator = findConceptWithSourceCodeMatching(
						"NCI Metathesaurus", ncim_version, source_abbrev, code, -1, true);
					if (iterator == null) return null;

					 while (iterator.hasNext()) {
						 ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
						 Entity meta_entity = getConceptByCode("NCI Metathesaurus", ncim_version, null, rcr.getConceptCode());
						 if (meta_entity == null) {
							 System.out.println("\tConcept not found??? ..." + code);
						 }
						 if (meta_entity != null) {
							 Vector v = DataUtils.getSynonyms(meta_entity);
							 if (v != null) {
								 for (int j=0; j<v.size(); j++) {
									 String syn = (String) v.elementAt(j);

									 Vector u = DataUtils.parseData(syn);
									 //term_name + "|" + term_type + "|" + term_source + + term_source_code;
									 if (u.size() == 4) {
										 String term_source = (String) u.elementAt(2);
										 String targetCode = (String) u.elementAt(3);
										 if (term_source.compareTo(target_abbrev) == 0) {
										 // match found:
											String sourceCode = code;//rcr.getConceptCode();

											//System.out.println("sourceCode: " + sourceCode);

											//System.out.println("sourceCodingScheme: " + sourceCodingScheme);


											//String sourceCodingSchemeVersion = DataUtils.getVocabularyVersionByTag(sourceCodingScheme, null);
											Entity source_entity = getConceptByCode(sourceCodingScheme,
																					sourceCodingSchemeVersion,
																					null,
																					sourceCode);
											if (source_entity != null) {
												String sourceName = "";
												String sourceCodeNamespace = "";
												if (source_entity != null) {
													sourceName = source_entity.getEntityDescription().getContent();
													sourceCodeNamespace = source_entity.getEntityCodeNamespace();
												}
												String associationName = "mapsTo";
												String rel = "";
												int score = 0;

												//String targetCodingScheme = DataUtils.getFormalName(target_abbrev);

												//System.out.println("targetCodingScheme: " + targetCodingScheme);

												if (!hset.contains(targetCode)) {
													hset.add(targetCode);

													//String targetCodingSchemeVersion = DataUtils.getVocabularyVersionByTag(targetCodingScheme, null);
													Entity target_entity = getConceptByCode(targetCodingScheme,
																							targetCodingSchemeVersion,
																							null,
																							targetCode);
													String targetName = "";
													String targetCodeNamespace = "";
													if (target_entity != null) {
														targetName = target_entity.getEntityDescription().getContent();
														targetCodeNamespace = target_entity.getEntityCodeNamespace();
													}


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
													list.add(mappingData);
												}
										    }

										 }
									 }
								 }
							 }
					     }
					 }


				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return list;

	}

    //CHEBI:10126|Zolpidem Tartrate
    //CHEBI:10126|99294-93-6
    public static List process_codingscheme_mapping(
                                        String source_cs,
                                        String target_cs,
                                        String input_option,
                                        String property,
                                        String algorithm,
                                        String[] data) {
		if (data == null) return null;
		if (data.length == 0) return null;

System.out.println("process_codingscheme_mapping input_option: " + input_option);
System.out.println("process_codingscheme_mapping source_cs: " + source_cs);
System.out.println("process_codingscheme_mapping target_cs: " + target_cs);
System.out.println("process_codingscheme_mapping property: " + property);
System.out.println("process_codingscheme_mapping algorithm: " + algorithm);

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

		ArrayList list = new ArrayList();
		HashSet hset = new HashSet();
		String source = null;
		boolean ranking = false;

		for (int i=0; i<data.length; i++) {
			String src_code_and_matchtext = (String) data[i];

			Vector values = DataUtils.parseData(src_code_and_matchtext);
			String sourceCode = (String) values.elementAt(0);
			String matchtext = (String) values.elementAt(1);

			Entity source_entity = getConceptByCode(sourceCodingScheme,
													sourceCodingSchemeVersion,
													null,
													sourceCode);


if (source_entity == null) {
	System.out.println("source_entity not found: " + sourceCode);
	return null;
}

			String sourceName = "";
			String sourceCodeNamespace = "";
			if (source_entity != null) {
				sourceName = source_entity.getEntityDescription().getContent();
				sourceCodeNamespace = source_entity.getEntityCodeNamespace();
			}
			String associationName = "mapsTo";
			String rel = "";
			int score = 0;

            ResolvedConceptReferencesIterator iterator = null;
            if(input_option.compareTo("Name") == 0) {
				iterator = searchByName(targetCodingScheme, targetCodingSchemeVersion, matchtext, null, algorithm, false, -1);
			} else {
				String[] property_types = null;
				String all_sources = null;
				String[] property_names = new String[1];
				property_names[0] = property;
				iterator = searchByProperties(
					          targetCodingScheme,
					          targetCodingSchemeVersion,
					          matchtext,
					          property_types,
					          property_names,
					          all_sources,
					          algorithm,
					          true,
					          true,
					          10);
			}
			if (iterator == null) {
				System.out.println("******************* searchByProperties returns null");
				return null;
			}
			try {
				 while (iterator.hasNext()) {
					 ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
					 Entity target_entity = getConceptByCode(targetCodingScheme, targetCodingSchemeVersion, null, rcr.getConceptCode());

                     String targetCode = rcr.getConceptCode();
					 String targetName = "";
					 String targetCodeNamespace = "";
					 if (target_entity != null) {
						targetName = target_entity.getEntityDescription().getContent();
						targetCodeNamespace = target_entity.getEntityCodeNamespace();
					 }


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
					 list.add(mappingData);
				 }

			 } catch (Exception ex) {
				ex.printStackTrace();
			 }
		}
		return list;
	}

    public Vector getPropertyValues(String scheme, String version, String code, String propertyName) {
		Vector v = new Vector();
		Entity concept = getConceptByCode(scheme, version, null, code);
		boolean found = false;

		Property[] properties = concept.getProperty();
        for (int i = 0; i < properties.length; i++) {
            Property p = properties[i];
            if (propertyName.compareTo(p.getPropertyName()) == 0) {
				v.add(p.getValue().getContent());
				found = true;
			}
        }
        if (found) return v;

		Presentation[] presentations = concept.getPresentation();
        for (int i = 0; i < presentations.length; i++) {
            Presentation p = presentations[i];
            if (propertyName.compareTo(p.getPropertyName()) == 0) {
				v.add(p.getValue().getContent());
				found = true;
			}
        }
        if (found) return v;

		Definition[] definitions = concept.getDefinition();
        for (int i = 0; i < definitions.length; i++) {
            Definition p = definitions[i];
            if (propertyName.compareTo(p.getPropertyName()) == 0) {
				v.add(p.getValue().getContent());
				found = true;
			}
        }
        if (found) return v;

		Comment[] comments = concept.getComment();
        for (int i = 0; i < comments.length; i++) {
            Comment p = comments[i];
            if (propertyName.compareTo(p.getPropertyName()) == 0) {
				v.add(p.getValue().getContent());
				found = true;
			}
        }
        return v;
	}


}

