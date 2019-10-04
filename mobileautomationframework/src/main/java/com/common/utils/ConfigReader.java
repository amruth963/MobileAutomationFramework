package com.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

	public String filePath;
	Properties pro = new Properties();
	

	public ConfigReader(String filePath) {
		this.filePath=filePath;
		try { 
			File src = new File(filePath);
			FileInputStream fis = new FileInputStream(src);
			pro.load(fis);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String getStaticProperty(String str)
	{
		if(pro == null) {
			System.out.println("pro is null");
		}
		return pro.getProperty(str);
	}

}
