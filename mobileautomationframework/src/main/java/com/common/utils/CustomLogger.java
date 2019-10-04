package com.common.utils;
//CustomLevel
import java.util.logging.Logger;

public class CustomLogger extends Logger {

	public CustomLogger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}

	public void error(String msg) {
		System.out.println("ERROR : " + msg);
		log(CustomLevel.ERROR, msg);
	}

	public void debug(String msg) {
		System.out.println("DEBUG :" + msg);
		log(CustomLevel.DEBUG,msg);
	}

	public void info(String msg) {
		System.out.println("INFO :" + msg);
		log(CustomLevel.INFO,msg);
	}
	
	public void warning(String msg) {
		System.out.println("WARNING :" + msg);
		log(CustomLevel.WARNING,msg);
	}

	public void testCasePass(String msg) {
		System.out.println("PASS :" + msg);
		log(CustomLevel.TESTCASEPASS,msg);
	}

	public void testCaseFail(String msg) {
		System.out.println("FAIL :" + msg);
		log(CustomLevel.TESTCASEFAIL,msg);
	}


}
