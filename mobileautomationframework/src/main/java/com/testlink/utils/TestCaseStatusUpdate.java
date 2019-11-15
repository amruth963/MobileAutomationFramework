package com.testlink.utils;


import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

public class TestCaseStatusUpdate {

	public String TESTLINK_DEVKEY = "0879a997a9505b0722107f9dfd6e8b94";
	public String TESTLINK_URL = "http://132.145.181.135/testlink/lib/api/xmlrpc/v1/xmlrpc.php";
	public String testProjectName = "E2 Platform";
	public String testPlanName = "EPT- Regression";
	public String buildName = "June Release 19";
	public ExecutionStatus testCasePass = ExecutionStatus.PASSED;
	public ExecutionStatus testCaseFail = ExecutionStatus.FAILED;
	public ExecutionStatus testCaseBlocked = ExecutionStatus.BLOCKED;


	public TestLinkSite testlink;

	public boolean getConnection(String url, String devKey) throws Exception {
		testlink = new TestLinkSite(url, devKey);
		return true;
	}

	public void updateResults(String testCaseName, String testSuiteName, String testCaseStatus) throws Exception {
		try {
			if(getConnection(TESTLINK_URL, TESTLINK_DEVKEY)) {
				TestProject testProject = testlink.getTestProject(testProjectName);
				Integer testProjectId = testProject.getId();
				TestPlan testPlan = testlink.getTestPlan(testPlanName, testProject.getId());
				TestSuite testSuite = testlink.getTestSuite(testProjectId, testSuiteName);
				Integer testSuiteId = testSuite.getId();
				Integer testPlanId = testPlan.getId();
				TestCase testCase = testlink.getTestCase(testSuiteId, testCaseName);
				Integer testCaseExternalId = testCase.getExternalId();
				Integer testCaseId = testCase.getId();
				testlink.updateTestCaseStatus(testCaseId,testCaseExternalId, testPlanId, status(testCaseStatus), buildName, false);
			}
		}catch(TestLinkAPIException te) {
			te.getMessage();
		} 
	}


	public ExecutionStatus status(String testCaseStatus) {
		if(testCaseStatus.equalsIgnoreCase("Pass")) {
			return testCasePass;
		}else if(testCaseStatus.equalsIgnoreCase("Fail")) {
			return testCaseFail;
		}else if(testCaseStatus.equalsIgnoreCase("Blocked")) {
			return testCaseBlocked;
		}else {
			return null;
		}
	}

}
