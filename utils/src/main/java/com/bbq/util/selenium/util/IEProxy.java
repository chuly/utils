package com.bbq.util.selenium.util;

public class IEProxy {

	public static void main(String[] args) throws Exception{
//		String cmd = "cmd.exe /C start d:\\mybat\\test.bat 113.122.142.144:808";
//		String cmd = "cmd.exe /C start d:\\mybat\\setProxy.bat 113.122.142.144:808";
		String cmd = "cmd.exe /C start d:\\mybat\\cleanproxy.bat";
		execCmd(cmd);
		Thread.sleep(1000);
		closeCmdWindow();
	}
	
	public static void execCmd(String cmd) throws Exception{
		Runtime.getRuntime().exec(cmd);
	}
	
	public static void closeCmdWindow() throws Exception{
		Runtime.getRuntime().exec("cmd.exe /C start wmic process where name='cmd.exe' call terminate");
	}
}
