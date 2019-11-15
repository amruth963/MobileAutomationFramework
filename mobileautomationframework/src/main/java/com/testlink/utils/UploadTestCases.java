package com.testlink.utils;

import java.util.ArrayList;
import java.util.List;
import br.eti.kinoshita.testlinkjavaapi.constants.ActionOnDuplicate;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.TestImportance;
import br.eti.kinoshita.testlinkjavaapi.model.Requirement;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;

public class UploadTestCases {

	public TestLinkSite testlink;
	public int count =0;
	public List<Integer> failedRows = new ArrayList<Integer>();

	String url = null;
	String devKey = null;

	public void getConnection(String url, String devKey) throws Exception {
		testlink = new TestLinkSite(url, devKey);
	}

	public void testCaseUpload(String devKey, String filePath, String testProjectName, String testSuiteName, String userDetails) {
		url = "http://132.145.181.135/testlink/lib/api/xmlrpc/v1/xmlrpc.php";
		//		devKey = "0879a997a9505b0722107f9dfd6e8b94";
		System.out.println("Connecting to TestLink");
		try {
			getConnection(url, devKey);
			System.out.println("Successfully conneted to TestLink server");

			//			String testProjectName = "E2 Platform";
			//			String testSuiteName = "E2_SignUp";
			int srs = 175;
			String [] requirements = {};

			TestData script = new TestData(filePath);
			String[] testCaseName = script.testCaseName();
			String[] summary = script.summary();
			String[] preCondition = script.preConditions();
			String[] actions = script.actions();
			String[] expectedResults = script.expectedResults();
			List<TestCaseStep> testLinkSteps = new ArrayList<TestCaseStep>();
			for (int i = 0; i < actions.length; i++) {
				testLinkSteps.clear();

				String testCaseNames = testCaseName[i];
				String userAutentication = userDetails;
				String summarys = summary[i];
				String preConditions = preCondition[i];
				String action = actions[i];
				String expectedResult = expectedResults[i];

				TestCaseStep testLinkStep = new TestCaseStep();
				testLinkStep.setNumber(i+1);
				testLinkStep.setActions(action);
				testLinkStep.setExpectedResults(expectedResult);
				testLinkStep.setExecutionType(ExecutionType.MANUAL);
				testLinkSteps.add(testLinkStep);

				TestProject testProject = testlink.getTestProject(testProjectName);
				if (testProject == null) {
					throw new RuntimeException("Could not find test project: " + testProjectName);
				}

				TestSuite testSuite = testlink.getTestSuite(testProject.getId(), testSuiteName);

				if (testSuite == null) {
					throw new RuntimeException("Could not find test suite: " + testSuiteName);
				}



				try {
					TestCase testCase = testlink.createTestCaseWithSteps(
							testCaseNames,// Test Case Name
							//							testCaseNumbers,
							testSuite.getId(), 
							testProject.getId(), 
							System.getProperty("testlink.author", userAutentication), 
							System.getProperty("testlink.summary", summarys), 
							testLinkSteps,
							System.getProperty("testlink.preconditions", preConditions),
							TestCaseStatus.DRAFT,
							TestImportance.MEDIUM, 
							ExecutionType.AUTOMATED,   
							null,// Order
							null,// Internal ID
							true,// Check Duplicated Names
							ActionOnDuplicate.GENERATE_NEW);// Replace old with new

					// Add requirements to the test case
					setRequirements(testCase, srs, requirements);

					count++;

				}catch(Exception e){
					System.out.println(e.getMessage());
					failedRows.add(i+2);
				}
			}
			System.out.println("Total number of test cases uploaded: " +count);

			if(failedRows.size()>0) {
				System.out.println("The failed rows is(are) : " +failedRows);
			}
			else{
				System.out.println("Failed rows : None");
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void setRequirements(TestCase testCase, int srsId, String[] requirementsId) {
		List<Requirement> requirements = new ArrayList<Requirement>();
		for (String requirementId : requirementsId) {
			int reqIdNumber = Integer.parseInt(requirementId);
			Requirement requirement = new Requirement();
			requirement.setId(reqIdNumber);
			requirement.setReqSpecId(srsId);
			requirements.add(requirement);
		}
		testlink.assignRequirements(testCase, requirements);
	}



}

