package com.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetAvailableAndroidDevices {

	CommandPrompt cmd = new CommandPrompt();
	Map<String, String> devices = new HashMap<String, String>();

	public void startADB() throws Exception{
		String output = cmd.runCommand("start-server");
		String[] lines = output.split("\n");
		if(lines.length==1)
			System.out.println("adb service already started");
		else if(lines[1].equalsIgnoreCase("* daemon started successfully *"))
			System.out.println("adb service started");
		else if(lines[0].contains("internal or external command")){
			System.out.println("adb path not set in system varibale");
			System.exit(0);
		}
	}

	public void stopADB() throws Exception{
		cmd.runCommand("kill-server");
	}

	public static List<String> deviceIds = new ArrayList<String>(); 
	public Map<String, String> getDevices() throws Exception{

		startADB(); // start adb service
		String output = cmd.runCommand("devices");
		String[] lines = output.split("\n");

		if(lines.length<=1){
			System.out.println("No Device Connected");
			stopADB();
			System.exit(0);	// exit if no connected devices found
		}

		for(int i=1;i<lines.length;i++){
			lines[i]=lines[i].replaceAll("\\s+", "");

			if(lines[i].contains("device")){
				lines[i]=lines[i].replaceAll("device", "");
				String deviceID = lines[i];
				deviceIds.add(deviceID);

				String model = cmd.runCommand("-s "+deviceID+" shell getprop ro.product.model").replaceAll("\\s+", "");
				String brand = cmd.runCommand("-s "+deviceID+" shell getprop ro.product.brand").replaceAll("\\s+", "");
				String osVersion = cmd.runCommand("-s "+deviceID+" shell getprop ro.build.version.release").replaceAll("\\s+", "");
				String deviceName = brand+" "+model;

				devices.put("deviceID"+i, deviceID);
				devices.put("deviceName"+i, deviceName);
				devices.put("osVersion"+i, osVersion);

				System.out.println("Following device is connected");
				System.out.println(deviceID+" "+deviceName+" "+osVersion+"\n");
			}else if(lines[i].contains("unauthorized")){
				lines[i]=lines[i].replaceAll("unauthorized", "");
				String deviceID = lines[i];

				System.out.println("Following device is unauthorized");
				System.out.println(deviceID+"\n");
			}else if(lines[i].contains("offline")){
				lines[i]=lines[i].replaceAll("offline", "");
				String deviceID = lines[i];

				System.out.println("Following device is offline");
				System.out.println(deviceID+"\n");
			}
		}
		return devices;
	}

	public void getIOSDevicesUDID_Method1(){
		try {
			String[] arguments = { "/usr/local/bin/mobiledevice", "list_devices"};
			Process p =Runtime.getRuntime().exec(arguments);
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while((line=r.readLine()) != null)
			{
				System.out.println(line);

			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("NO CONNECTED IOS DEVICES FOUND");	     
		}
	}

	public void getIOSDevicesUDID_Method2(){
		try {
			String[] str = {"/bin/bash", "-c", "ios-deploy -c"};	
			Process p = Runtime.getRuntime().exec(str);
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while((line=r.readLine()) != null)
			{
				System.out.println(line);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("NO CONNECTED IOS DEVICES FOUND");	     
		}

	}


	public String getIOSDevicesUDID_Method3() throws Exception {
		String[] str = {"idevice_id -l"};
		Process p = Runtime.getRuntime().exec(str);
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line="";
		String allLine="";
		@SuppressWarnings("unused")
		int i=1;
		while((line=r.readLine()) != null){
			//		System.out.println(i+". "+line);
			allLine=allLine+""+line+"\n";
			if(line.contains("Console LogLevel: debug"))
				break;
			i++;
		}
		return allLine;

	}





}
