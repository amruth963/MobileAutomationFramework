package com.common.utils;

import java.util.logging.Level;

public class CustomLevel extends Level {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected CustomLevel(String name, int value, String resourceBundleName) {
		super(name, value, resourceBundleName);
	}
	private static final String defaultBundle = "sun.util.logging.resources.logging";
	public static final Level ERROR = new CustomLevel("ERROR",1000, defaultBundle);
	public static final Level DEBUG = new CustomLevel("DEBUG",1000, defaultBundle);
	public static final Level INFO = new CustomLevel("INFO",1000, defaultBundle);
	public static final Level WARNING = new CustomLevel("WARNING",1000, defaultBundle);
	public static final Level TESTCASEPASS = new CustomLevel("PASS", 1000, defaultBundle);
	public static final Level TESTCASEFAIL = new CustomLevel("FAIL", 1000, defaultBundle);
	
}
