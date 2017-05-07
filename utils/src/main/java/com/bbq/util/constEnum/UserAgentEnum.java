package com.bbq.util.constEnum;

import java.util.Random;

public class UserAgentEnum {
	public static final String[][] user_agent_enum = {
		{"1","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3"}
		,{"1","Dalvik/1.6.0 (Linux; U; Android 4.4.2; Nexus 6 Build/KOT49H)"}
		,{"1","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"}
		,{"1","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0"}
		,{"1","Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50"}//safari 5.1 – MAC
		,{"1","Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50"}//safari 5.1 – Windows
		,{"1","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;"}//IE 9.0
		,{"1","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)"}//IE 8.0
		,{"1","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)"}//IE 7.0
		,{"1"," Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"}//IE 6.0
		,{"1","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1"}//
		,{"1","Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1"}//
		,{"1","Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11"}//
		,{"1","Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11"}//
		,{"1","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11"}//
		,{"1","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)"}//傲游（Maxthon）
		,{"1","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)"}//腾讯TT
		,{"1","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"}//世界之窗（The World） 2.x
		,{"1","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; The World)"}//世界之窗（The World） 3.x
		,{"1","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)"}//搜狗浏览器 1.x
		,{"1","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"}//360浏览器
//		,{"1","11111111111111111"}//
//		,{"1","11111111111111111"}//
//		,{"1","11111111111111111"}//
//		,{"1","11111111111111111"}//
//		,{"1","11111111111111111"}//
//		,{"1","11111111111111111"}//
//		,{"1","11111111111111111"}//
//		,{"1","11111111111111111"}//
	};
	
	private static Random r = new Random();
	
	public static String getRandomUserAgent(){
		return user_agent_enum[r.nextInt(user_agent_enum.length)][1];
	}
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) 
			System.out.println(getRandomUserAgent());
	}
}
