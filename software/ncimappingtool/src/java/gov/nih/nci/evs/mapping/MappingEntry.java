/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.mapping;

import java.io.*;
import java.util.*;

import gov.nih.nci.evs.browser.bean.MappingData;

/**
  * 
  */

/**
  * @author EVS Team
  * @version 1.0
  *
  * Modification history
  *     Initial implementation kim.ong@ngc.com
  *
 */

public class MappingEntry
{
    public static int COL_SOURCE_CODE = 1;
    public static int COL_SOURCE_NAME = 2;
    public static int COL_SOURCE_NAMESPACE = 3;
    public static int COL_REL = 4;
    public static int COL_SCORE = 5;
    public static int COL_TARGET_CODE = 6;
    public static int COL_TARGET_NAME = 7;
    public static int COL_TARGET_NAMESPACE = 8;


// Variable declaration
	private String sourceCode;
	private String sourceName;
	private String sourceCodingScheme;
	private String sourceCodingSchemeVersion;
	private String sourceCodeNamespace;
	private String associationName;
	private String rel;
	private int score;
	private String targetCode;
	private String targetName;
	private String targetCodingScheme;
	private String targetCodingSchemeVersion;
	private String targetCodeNamespace;

	private String comment;
	private String status;

// Default constructor
	public MappingEntry() {
	}

// Constructor
	public MappingEntry(
		String sourceCode,
		String sourceName,
		String sourceCodingScheme,
		String sourceCodingSchemeVersion,
		String sourceCodeNamespace,
		String associationName,
		String rel,
		int score,
		String targetCode,
		String targetName,
		String targetCodingScheme,
		String targetCodingSchemeVersion,
		String targetCodeNamespace,
		String status,
		String comment) {

		this.sourceCode = sourceCode;
		this.sourceName = sourceName;
		this.sourceCodingScheme = sourceCodingScheme;
		this.sourceCodingSchemeVersion = sourceCodingSchemeVersion;
		this.sourceCodeNamespace = sourceCodeNamespace;
		this.associationName = associationName;
		this.rel = rel;
		this.score = score;
		this.targetCode = targetCode;
		this.targetName = targetName;
		this.targetCodingScheme = targetCodingScheme;
		this.targetCodingSchemeVersion = targetCodingSchemeVersion;
		this.targetCodeNamespace = targetCodeNamespace;
		this.status = status;
		this.comment = comment;
	}

	public MappingEntry(
		String sourceCode,
		String sourceName,
		String sourceCodingScheme,
		String sourceCodingSchemeVersion,
		String sourceCodeNamespace,
		String associationName,
		String rel,
		int score,
		String targetCode,
		String targetName,
		String targetCodingScheme,
		String targetCodingSchemeVersion,
		String targetCodeNamespace) {

		this.sourceCode = sourceCode;
		this.sourceName = sourceName;
		this.sourceCodingScheme = sourceCodingScheme;
		this.sourceCodingSchemeVersion = sourceCodingSchemeVersion;
		this.sourceCodeNamespace = sourceCodeNamespace;
		this.associationName = associationName;
		this.rel = rel;
		this.score = score;
		this.targetCode = targetCode;
		this.targetName = targetName;
		this.targetCodingScheme = targetCodingScheme;
		this.targetCodingSchemeVersion = targetCodingSchemeVersion;
		this.targetCodeNamespace = targetCodeNamespace;
		this.status = "Pending";
		this.comment = "";
	}

// Set methods
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public void setSourceCodingScheme(String sourceCodingScheme) {
		this.sourceCodingScheme = sourceCodingScheme;
	}

	public void setSourceCodingSchemeVersion(String sourceCodingSchemeVersion) {
		this.sourceCodingSchemeVersion = sourceCodingSchemeVersion;
	}

	public void setSourceCodeNamespace(String sourceCodeNamespace) {
		this.sourceCodeNamespace = sourceCodeNamespace;
	}

	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public void setTargetCodingScheme(String targetCodingScheme) {
		this.targetCodingScheme = targetCodingScheme;
	}

	public void setTargetCodingSchemeVersion(String targetCodingSchemeVersion) {
		this.targetCodingSchemeVersion = targetCodingSchemeVersion;
	}

	public void setTargetCodeNamespace(String targetCodeNamespace) {
		this.targetCodeNamespace = targetCodeNamespace;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setStatus(String status) {
		this.status = status;
	}

// Get methods
	public String getSourceCode() {
		return this.sourceCode;
	}

	public String getSourceName() {
		return this.sourceName;
	}

	public String getSourceCodingScheme() {
		return this.sourceCodingScheme;
	}

	public String getSourceCodingSchemeVersion() {
		return this.sourceCodingSchemeVersion;
	}

	public String getSourceCodeNamespace() {
		return this.sourceCodeNamespace;
	}

	public String getAssociationName() {
		return this.associationName;
	}

	public String getRel() {
		return this.rel;
	}

	public int getScore() {
		return this.score;
	}

	public String getTargetCode() {
		return this.targetCode;
	}

	public String getTargetName() {
		return this.targetName;
	}

	public String getTargetCodingScheme() {
		return this.targetCodingScheme;
	}

	public String getTargetCodingSchemeVersion() {
		return this.targetCodingSchemeVersion;
	}

	public String getTargetCodeNamespace() {
		return this.targetCodeNamespace;
	}

	public String getKey() {
		return this.sourceCode + "|" + this.sourceCodeNamespace + "|" + this.targetCode + "|" + this.targetCodeNamespace;
	}

	public String getComment() {
		return this.comment;
	}

	public String getStatus() {
		return this.status;
	}

	public MappingData toMappingData() {
		MappingData obj = new MappingData(
			this.sourceCode,
			this.sourceName,
			this.sourceCodingScheme,
			this.sourceCodingSchemeVersion,
			this.sourceCodeNamespace,
			this.associationName,
			this.rel,
			this.score,
			this.targetCode,
			this.targetName,
			this.targetCodingScheme,
			this.targetCodingSchemeVersion,
			this.targetCodeNamespace,
			this.status,
			this.comment
			);

		obj.setComment(this.comment);
		return obj;
	}

}
