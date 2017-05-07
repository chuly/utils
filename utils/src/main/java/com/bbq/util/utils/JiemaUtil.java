package com.bbq.util.utils;

import com.alibaba.fastjson.JSON;

public class JiemaUtil {
	private static String baseUrl = "http://api.ema666.com";

	public static void main(String[] args) throws Exception{
		String userName = "cyaema2017";
		String pwd = "caocaonimabiema2017";
		String developer="0Tt%2frAbd4WmOmbnXlBqvDg%3d%3d";
		String[] tokenArr = login(userName, pwd, developer);
		String token = tokenArr[0];
		System.out.println(JSON.toJSONString(tokenArr));
		
//		String token = "hxIGXGzRYOHg8E9XxU4XuEOcPg5XoF21872203444";
//		//String itemId = "30224";//	发发联盟
//		String itemId = "411";//	 宝宝树注册 绑定
//		String itemId = "420";//420	妈妈圈
		String itemId = "38100";//38100	流量神器注册 流量币赠送
		String phone = getPhone(token, itemId, "1");
		System.out.println(phone);
		phone = phone.replace(";", "");
		
//		String phone ="15818344161";
		String msmText = getOneMessage(token, itemId, phone);
		System.out.println(msmText);
		while (msmText.startsWith("False")){
			Thread.sleep(5*1000);
			msmText = getOneMessage(token, itemId, phone);
			System.out.println(msmText);
		}
		
		
//		String releaseResult = releasePhone(token, "18123811144;");
//		System.out.println("releaseResult="+releaseResult);
		
//		String releaseAllResult = releaseAllPhone(token);
//		System.out.println("releaseAllResult="+releaseAllResult);
	}
	/**
	 * 登录
	 * @param userName
	 * @param pwd
	 * @param developer
	 * @return token(下面所有方法都要用的令牌)
			账户余额
			最大登录客户端个数
			最多获取号码数
			单个客户端最多获取号码数
			折扣
	 * @throws Exception
	 */
	public static String[] login(String userName,String pwd,String developer) throws Exception{
		String url = "/Api/userLogin?uName=%s&pWord=%s&Developer=%s";
		String resp = HttpClientUtil.execGet(baseUrl+String.format(url, userName,pwd,developer));
		String[] ret = resp.split("&");
		return ret;
	}
	/**
	 * 获取手机号码
	 * @param token
	 * @param itemId
	 * @param phoneType 0 [所有运营商]，1 [移动]，2 [联通]，3 [电信] 
	 * @return
	 * @throws Exception
	 */
	public static String getPhone(String token,String itemId,String phoneType) throws Exception{
		String url = "/Api/userGetPhone?token=%s&ItemId=%s&PhoneType=%s";
		String resp = HttpClientUtil.execGet(baseUrl+String.format(url, token,itemId,phoneType));
		return resp;
	}
	/**
	 * 获取短信内容
	 * @param token
	 * @param itemId
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public static String getOneMessage(String token,String itemId,String phone) throws Exception{
		String url = "/Api/userSingleGetMessage?token=%s&itemId=%s&phone=%s";
		String resp = HttpClientUtil.execGet(baseUrl+String.format(url, token,itemId,phone));
		return resp;
	}
	/**
	 * 释放手机号
	 * @param token
	 * @param phoneList phone-itemId;phone-itemId;
	 * @return
	 * @throws Exception
	 */
	public static String releasePhone(String token,String phoneList) throws Exception{
		String url = "/Api/userReleasePhone?token=%s&phoneList=%s";
		String resp = HttpClientUtil.execGet(baseUrl+String.format(url, token,phoneList));
		return resp;
	}
	/**
	 * 释放所有手机号
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static String releaseAllPhone(String token) throws Exception{
		String url = "/Api/userReleaseAllPhone?token=%s";
		String resp = HttpClientUtil.execGet(baseUrl+String.format(url, token));
		return resp;
	}
	
}
