package com.testlink.utils;

import com.excel.utils.POIFileRead;

public class TestData {

	public String path;
	public POIFileRead excelRead;

	public TestData(String filePath) {
		this.path = filePath;
		this.excelRead = new POIFileRead(path);
	}


	public String[] testCaseName() {
		return excelRead.getTestCasedata("Test Case Name");
	}

	public String[] summary() {
		return excelRead.getTestCasedata("Summary");
	}

	public String[] preConditions() {
		return excelRead.getTestCasedata("Pre-conditions");
	}


	public String[] actions() {
		return excelRead.getTestCasedata("Test Steps");
	}


	public String[] expectedResults() {
		return excelRead.getTestCasedata("Expected Result");
	}

}
