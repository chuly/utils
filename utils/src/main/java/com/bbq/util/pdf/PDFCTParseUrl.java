package com.bbq.util.pdf;

import java.io.BufferedReader;
import java.io.FileReader;

public class PDFCTParseUrl {

	static String sql = "INSERT INTO t_dzs_info (`title`, `text_summary`, `download_url`, `create_time_str`,read_count,valid) "
			+ "VALUES ('%s', '%s', '%s', '%s',%s,1);";
	static String sql2 = "INSERT INTO t_dzs_info (`title`, `text_summary`, `download_url`,read_count,valid) "
			+ "VALUES ('%s', '%s', '%s', '%s',0);";
	public static void main(String[] args) throws Exception{
		String filePath = "D:\\工作\\pdf\\51099\\ct-url\\1010-2000";
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String s = null;
		String createTimeStr = "2017-02-05";
		String readCount = "1";
		while((s=br.readLine())!=null){
			s = s.replace("【来源：www.biubiuq.online】", "");
			s = s.substring(s.indexOf("-")+1);
			int idx = s.indexOf(": https://page50.ctfile.com");
			String name = s.substring(0,idx);
			String url = s.substring(idx+2);
//			System.out.println(String.format(sql, name,name,url,createTimeStr,readCount));
			System.out.println(String.format(sql2,name,name,url,readCount));
		}
		
	}
}
