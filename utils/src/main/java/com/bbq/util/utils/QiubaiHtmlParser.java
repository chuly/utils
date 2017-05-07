package com.bbq.util.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.bbq.util.utils.bean.QiubaiBean;

/**
 * 解析http://www.qiushibaike.com
 * @author chuly
 *
 */
public class QiubaiHtmlParser {
	
	public List<QiubaiBean> parseList(String html) {
		List<QiubaiBean> list = new ArrayList();
		Document doc = Jsoup.parse(html);//connect("http://www.weather.com.cn/html/weather/101280101.shtml").get();
		// 获取目标HTML代码
		Elements elements1 = doc.select("[class=article block untagged mb15]");
		// 今天
		if(elements1 != null && elements1.size() > 0){
			for (Element element : elements1) {
					QiubaiBean mb = new QiubaiBean();
					String content = element.select("[class=content]").select("span").get(0).text();
					String goodeCountStr = element.select("[class=stats-vote]").select("i").get(0).text();
					try {
						mb.setGoodCount(Integer.parseInt(goodeCountStr));
					} catch (Exception e) {
						mb.setGoodCount(0);
					}
					mb.setContent(content);
					mb.setContentHash(MD5Util.getMd5(content));
					list.add(mb);
			}
		}
		return list;
	}


	public static void main(String[] args) throws Exception{
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headMap.put("Accept-Encoding", "gzip, deflate, sdch");
		headMap.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2");
		headMap.put("Connection", "keep-alive");
//		headMap.put("Cookie", "_qqq_uuid_=\"2|1:0|10:1488700163|10:_qqq_uuid_|56:ZDdjMTljZTRmZmZlMzZiMzA1NDI3ZGE3NTU1ODM3MGE2MmQ4NDM3Zg==|9bd0755c25e27aac82fa5c3b0a0aefca52f948137affe1ba6785bb37b4319b8c\"; Hm_lvt_18a964a3eb14176db6e70f1dd0a3e557=1488701485; _xsrf=2|9d7f9f6a|a883e1a963cdd88bb38665994182d6d6|1488701672; __cur_art_index=7801");
		headMap.put("Host", "www.qiushibaike.com");
		headMap.put("Referer", "http://www.qiushibaike.com/");
		headMap.put("Upgrade-Insecure-Requests", "1");
		headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		String html = HttpClientUtil.execGet("http://www.qiushibaike.com",headMap);///8hr/page/2/?s=4962379");
		System.out.println(html);
		QiubaiHtmlParser pa = new QiubaiHtmlParser();
		List<QiubaiBean> md = pa.parseList(html);
		System.out.println(JSON.toJSONString(md));
	}
	
	private void log(String s){
		System.out.println(s);
	}
}
