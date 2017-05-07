package com.bbq.util.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.utils.bean.QiubaiBean;

/**
 * 解析http://www.qiushibaike.com
 * @author chuly
 *
 */
public class KuaidailiHtmlParser {
	
	public List<HttpProxyBean> parseList(String html) {
		List<HttpProxyBean> list = new ArrayList();
//		Document doc = Jsoup.parse(html);//connect("http://www.weather.com.cn/html/weather/101280101.shtml").get();
//		// 获取目标HTML代码
//		Elements elements1 = doc.select("[class=article block untagged mb15]");
//		// 今天
//		if(elements1 != null && elements1.size() > 0){
//			for (Element element : elements1) {
//					QiubaiBean mb = new QiubaiBean();
//					String content = element.select("[class=content]").select("span").get(0).text();
//					String goodeCountStr = element.select("[class=stats-vote]").select("i").get(0).text();
//					try {
//						mb.setGoodCount(Integer.parseInt(goodeCountStr));
//					} catch (Exception e) {
//						mb.setGoodCount(0);
//					}
//					mb.setContent(content);
//					mb.setContentHash(MD5Util.getMd5(content));
//					list.add(mb);
//			}
//		}
		return list;
	}


	private void log(String s){
		System.out.println(s);
	}
}
