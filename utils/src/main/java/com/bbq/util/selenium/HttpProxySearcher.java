package com.bbq.util.selenium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.utils.HttpClientUtil;
import com.bbq.util.utils.KuaidailiHtmlParser;

public class HttpProxySearcher {

	public static void main(String[] args) {
		

	}

	private static List<HttpProxyBean> queryHtml(int page) throws Exception{
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
		headMap.put("Accept-Encoding", "gzip, deflate, sdch");
		headMap.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2");
		headMap.put("Connection", "keep-alive");
//		headMap.put("Cookie", "");
		headMap.put("Host", "www.kuaidaili.com");
		headMap.put("Referer", "http://www.kuaidaili.com/");
		headMap.put("Upgrade-Insecure-Requests", "1");
		headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		String html = HttpClientUtil.execGet("http://www.kuaidaili.com/free/inha/"+page+"/",headMap);
		if(html != null && !"Invalid Page".equals(html) && html.length()>0){
			KuaidailiHtmlParser pa = new KuaidailiHtmlParser();
			List<HttpProxyBean> list = pa.parseList(html);
			System.out.println(JSON.toJSONString(list));
			return list;
		}
		return null;
	}
}
