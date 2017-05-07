package com.bbq.util.msm;

import java.util.HashMap;
import java.util.Map;

import com.bbq.util.constEnum.UserAgentEnum;
import com.bbq.util.utils.HttpClientUtil;

public class MsmHz {

	public static void main(String[] args) {
		String phoneNum = "";
//		send_95504(phoneNum);//可用
//		send_tadu(phoneNum);
	}
	// http://lxbjs.baidu.com/cb/call
	public static void send_lxbjs_baidu(String phoneNum) {
		String urlWithParam = "http://lxbjs.baidu.com/cb/call?uid=748365&g=1929&tk=221A4C5D78F6167BBEA01EF9163BCA74EBF8973E3F5FC342EEF611C1F9FB38739F3B67A3D33BEA8A73A35E746F6BAC579D63257266290116737A292B98DB4A8F2E774C27F3FF41BD08D2612F2BEF92B66E40FBA6C60C7CD698205137F6EC6B4A9DEB75E1C50B499604A3B40934AAEF2D47BC83956474EDE0E2DBD5C53B6B2B4323149E624B0B46D6285858642059BCAE364143EEDD004EE7FA5325E2C4789D0968857A045875034007A7840078DB6A1DA2099A8E33EB35B5B82AD3EFB41F25F5CC82D3B857E5B321025C875984F950E6BB0820A9B3D34197C04247427455DD8BB23982043332940DDF3E3D89E453796A81536E3404BE9B65CF5D5DBD0CA75DA495D8E969D74CF992EAA520BBEEEF27D05C44456ABF754270131AC51B5155FE9EE2761854AC2FF6DF3D4F54B52CE544D359C1DB7B39D60A1268B2146235F57BF315062BFAB2B11D8C55382114054D1F4D825F4E74497BC5348388EE6C713CD43F0036E0AF8465A83A&vtel="
				+ phoneNum + "&bdcbid=c0f7dcbf-06d4-4e86-bc95-9c1bbeba456b&callback=_lxb_jsonp_1481886677679_";
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", "lxbjs.baidu.com");
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
		headMap.put("Content-Length", "0");
		headMap.put("Referer", "http://lxbjs.baidu.com");
		try {
			String s = HttpClientUtil.execGet(urlWithParam, headMap);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Referer: http://account.autohome.com.cn/AccountApi/CreateMobileCode
	public static void send_autohome(String phoneNum) {
		String urlWithParam = "http://account.autohome.com.cn/AccountApi/CreateMobileCode";
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", "account.autohome.com.cn");
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
//		headMap.put("Content-Length", "0");
		headMap.put("Referer", "http://account.autohome.com.cn");
		String paramStr = "UserName=&Password=&mobilehw=&isOverSea=0&Mobile="+phoneNum+"&ValidCode=&sex=1&phone="+phoneNum+"&validcodetype=1";
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Referer: http://wqb.jsz998.com/user/getmobilevcode
	public static void send_jsz998(String phoneNum) {
		String urlWithParam = "http://wqb.jsz998.com/user/getmobilevcode";
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", "wqb.jsz998.com");
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
//			headMap.put("Content-Length", "0");
		headMap.put("Referer", "http://wqb.jsz998.com");
		String paramStr = "action=1&mobile="+phoneNum+"&vcode=";
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Referer: http://m.guazi.com/misc/user/?act=register
	public static void send_guazi(String phoneNum) {
		String urlWithParam = "http://m.guazi.com/misc/user/?act=register";
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", "m.guazi.com");
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
//				headMap.put("Content-Length", "0");
		headMap.put("Referer", "http://m.guazi.com");
		String paramStr = "phone="+phoneNum;
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Referer: http://myapi.motherbuy.com/index.php?act=login&op=sms
	public static void send_motherbuy(String phoneNum) {
		String urlWithParam = "http://myapi.motherbuy.com/index.php?act=login&op=sms";
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", "myapi.motherbuy.com");
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
//					headMap.put("Content-Length", "0");
		headMap.put("Referer", "http://myapi.motherbuy.com");
		String paramStr = "mobile="+phoneNum+"&type=1";
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Referer: http://wx.95504.net/MicroSide/JFSCPhoneValidate.ashx
	public static void send_95504(String phoneNum) {
		String urlWithParam = "http://wx.95504.net/MicroSide/JFSCPhoneValidate.ashx";
		String referer = "http://wx.95504.net";
		String host = "wx.95504.net";
		String paramStr = "methodflag=setCode&phone="+phoneNum;
		
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", host);
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
		headMap.put("Referer", referer);
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Referer: http://kuaihongbao.com/api/get_login_code
	public static void send_kuaihongbao(String phoneNum) {
		String urlWithParam = "http://kuaihongbao.com/api/get_login_code";
		String referer = "http://kuaihongbao.com";
		String host = "kuaihongbao.com";
		String paramStr = "uid=&khbadrqd=yingyongbao&isfromkhbadr=true&phone="+phoneNum+"&version=3.1.1";
		
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", host);
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
		headMap.put("Referer", referer);
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Referer: http://www.wahuasuan.com/?m=api&a=vcode
	public static void send_wahuasuan(String phoneNum) {
		String urlWithParam = "http://www.wahuasuan.com/?m=api&a=vcode";
		String referer = "http://www.wahuasuan.com";
		String host = "www.wahuasuan.com";
		String paramStr = "phone="+phoneNum+"&type_pos=3&sms=0";
		
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", host);
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
		headMap.put("Referer", referer);
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Referer: http://wap.tadu.com/gencaptcha/
	public static void send_qyer(String phoneNum) {
		String urlWithParam = "http://open.qyer.com/qyer/user/active_code";
		String host = "open.qyer.com";
		String referer = "http://"+host;
		String paramStr = "client_id=qyer_android&client_secret=9fcaae8aefc4f9ac4915&v=1&track_deviceid=864394010002246&track_app_version=7.0.5&track_app_channel=baidu&track_device_info=L50t&track_os=Android4.4.2&app_installtime=1480747518929&country_code=86&mobile="+phoneNum+"";
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", host);
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
		headMap.put("Referer", referer);
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Referer: http://wap.tadu.com/gencaptcha/
	public static void send_tadu(String phoneNum) {
		String urlWithParam = "http://wap.tadu.com/gencaptcha/";
		String host = "wap.tadu.com";
		String referer = "http://"+host;
		String paramStr = "phone="+phoneNum+"&captchatype=3";
		
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept-Encoding", "gzip");
		headMap.put("User-Agent", UserAgentEnum.getRandomUserAgent());
		headMap.put("Host", host);
		headMap.put("Connection", "Keep-Alive");
		headMap.put("Content-Type", "application/x-www-form-urlencoded");
		headMap.put("Referer", referer);
		try {
			String s = HttpClientUtil.execPost(urlWithParam, headMap, paramStr);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * http://www.ttysq.com/member.php?mod=register
	 * 
	 */
}
