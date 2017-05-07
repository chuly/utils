package com.bbq.util.selenium;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.utils.HttpClientUtil;
import com.bbq.util.utils.freeHttpProxyParser.DailiHtmlParserItf;
import com.bbq.util.utils.freeHttpProxyParser.KuaidailiHtmlParser;
import com.bbq.util.utils.freeHttpProxyParser.XicidailiHtmlParser;

public class HttpProxySearcher {

	public static void main(String[] args) throws Exception{
		
		List<HttpProxyBean> proxyHostList = searchXiciHttpProxy(1);
//		System.out.println(JSON.toJSONString(proxyHostList));
		
	}
	/**
	 * 快代理 www.kuaidaili.com
	 */
	public static List<HttpProxyBean> searchKuaidailiHttpProxy(int page) throws Exception{
		String html = searchHttpProxy("www.kuaidaili.com","http://www.kuaidaili.com/free/intr/"+page+"/");
		if(html != null && !"Invalid Page".equals(html) && html.length()>0){
			DailiHtmlParserItf pa = new KuaidailiHtmlParser();
			List<HttpProxyBean> list = pa.parseList(html);
			System.out.println(JSON.toJSONString(list));
			return list;
		}
		return null;
	}
	
	/**
	 * 西祠代理 http://www.xicidaili.com
	 */
	public static List<HttpProxyBean> searchXiciHttpProxy(int page) throws Exception{
		String html = searchHttpProxy("www.xicidaili.com","http://www.xicidaili.com/nn/"+page);
		if(html != null && !"Invalid Page".equals(html) && html.length()>0){
			DailiHtmlParserItf pa = new XicidailiHtmlParser();
			List<HttpProxyBean> list = pa.parseList(html);
			System.out.println(JSON.toJSONString(list));
			return list;
		}
		return null;
	}
	
	private static String searchHttpProxy(String host,String url) throws Exception{
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
		headMap.put("Accept-Encoding", "gzip, deflate, sdch");
		headMap.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2");
		headMap.put("Connection", "keep-alive");
		headMap.put("Host", host);
		headMap.put("Referer", "http://"+host+"/");
		headMap.put("Upgrade-Insecure-Requests", "1");
		headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		return HttpClientUtil.execGet(url,headMap);
	}
}
