package com.common.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;


public class LoggerHelper {


	static final String SYSTEM_LOGGER_RB_NAME = "sun.util.logging.resources.logging";
	CustomLogger logger = new CustomLogger("Test", SYSTEM_LOGGER_RB_NAME);
	FileHandler fh;

	public LoggerHelper(String testcasename) throws SecurityException, IOException, ParseException
	{
		String foldername = "Log - " + LocalDate.now().toString();
		new File("/Users/amruth/Documents/EclipseWorkspace/e2MobileTestScripts/Logs/" + foldername).mkdir();
		fh = new FileHandler("/Users/amruth/Documents/EclipseWorkspace/e2MobileTestScripts/Logs/"+foldername+"/"+testcasename+".log");
		logger.addHandler(fh);
		SimpleFormatter formatter1 = new SimpleFormatter();
		fh.setFormatter(formatter1);	
	}

	public CustomLogger getLogger()
	{
		return logger;
	}

	public void CloseHandler()
	{

		for(Handler h:logger.getHandlers())
		{
			h.close();   //must call h.close or a .LCK file will remain. 
		}
	}



}
