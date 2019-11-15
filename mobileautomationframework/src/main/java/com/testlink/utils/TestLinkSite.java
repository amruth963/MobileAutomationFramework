/* 
 * The MIT License
 * 
 * Copyright (c) 2013 Matteo Castellarin, Bruno P. Kinoshita
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.testlink.utils;

import java.net.URL;
import java.util.List;
import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ActionOnDuplicate;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.TestImportance;
import br.eti.kinoshita.testlinkjavaapi.model.ReportTCResultResponse;
import br.eti.kinoshita.testlinkjavaapi.model.Requirement;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

/**
 * Wrapper class that represents a TestLink instance.
 * 
 * <p>
 * This class is not supposed to be used by clients of this API.
 * </p>
 * 
 * <p>
 * This class is thread safe and not serializable
 * </p>
 * 
 * @author mcaste00
 * @since 0.1
 */
final class TestLinkSite {

	TestLinkAPI api = null;
	Integer testCaseId;
	TestCase testCase;

	TestLinkSite(String url, String devKey) throws Exception{
		try {
			URL testlinkURL = new URL(url);
			api = new TestLinkAPI(testlinkURL, devKey);
		}catch(TestLinkAPIException te) {
			System.out.println("The dev key provided is not proper" +te.getMessage());
			//			te.printStackTrace(System.err);
		}
	}


	TestProject getTestProject(String testProjectName) {
		TestProject[] testProjects = api.getProjects();
		for (TestProject testProject : testProjects) {
			String name = testProject.getName();
			if (name.equals(testProjectName)) {
				return testProject;
			}
		}
		return null;
	}

	TestPlan getTestPlan(String testPlanName, Integer projectId) {
		TestPlan[] testPlans = api.getProjectTestPlans(projectId);
		for(TestPlan testPlan: testPlans) {
			String name = testPlan.getName();
			if(name.equals(testPlanName)) {
				return testPlan;
			}
		}
		return null;
	}

	TestSuite getTestSuite(Integer testProject, String testSuiteName) {
		TestSuite[] testSuites = api.getFirstLevelTestSuitesForTestProject(testProject);
		for (TestSuite testSuite : testSuites) {
			String name = testSuite.getName();
			if (name.equals(testSuiteName)) {
				return testSuite;
			}
		}
		return null;
	}

	TestCase getTestCase(Integer testSuiteId, String testCaseName) {
		TestCase[] testCases = api.getTestCasesForTestSuite(testSuiteId, true, TestCaseDetails.FULL);
		for(TestCase testCase : testCases) {
			String name = testCase.getName();
			if(name.equals(testCaseName)) {
				return testCase;
			}
		}
		return null;
	}

	TestCase createTestCaseWithSteps(String testCaseName, Integer testSuiteId, Integer testProjectId,
			String authorLogin, String summary, List<TestCaseStep> steps, String preconditions,TestCaseStatus status,
			TestImportance importance, ExecutionType executionType, Integer order, Integer internalId,
			boolean checkDuplicatedName, ActionOnDuplicate actionOnDuplicatedName) {
		TestCase testCase = api.createTestCase(testCaseName, testSuiteId, testProjectId, authorLogin, summary, steps,
				preconditions, status, importance, executionType, order, internalId, checkDuplicatedName,
				actionOnDuplicatedName);
		return testCase;
	}

	void assignRequirements(TestCase testCase, List<Requirement> requirements) {
		api.assignRequirements(testCase.getId(), testCase.getTestProjectId(), requirements);
	}


	Integer getTestCaseId(String testCaseName, String testSuiteName, String testProjectName, String testCasePathName) {
		return testCaseId = api.getTestCaseIDByName(testCaseName, testSuiteName, testProjectName, testCasePathName);
	}


	ReportTCResultResponse updateTestCaseStatus(Integer testCaseId, Integer testCaseExternalId, Integer testPlanId, ExecutionStatus status, String buildName, Boolean overwrite) {
		return api.reportTCResult(testCaseId, testCaseExternalId, testPlanId, status, null, null, buildName, null, null, null, null, null, null, null, overwrite, null, null);
	}


	


}
