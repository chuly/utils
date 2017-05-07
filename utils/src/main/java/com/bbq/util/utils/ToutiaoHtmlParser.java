package com.bbq.util.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.bbq.util.utils.bean.ToutiaoBean;

/**
 * 解析http://www.toutiao.com
 */
public class ToutiaoHtmlParser {
	
	public ToutiaoBean parseDetail(String html) {
		Document doc = Jsoup.parse(html);//connect("http://www.weather.com.cn/html/weather/101280101.shtml").get();
		// 获取目标HTML代码
		Elements elements1 = doc.select("[class=article-content]");
		Elements elementsTitle = doc.select("[class=article-title]");
		// 今天
		if(elements1 != null && elements1.size() > 0){
			ToutiaoBean mb = new ToutiaoBean();
			String title = elementsTitle.get(0).text();
			String content = elements1.get(0).toString();
			mb.setContent(content);
			mb.setTitle(title);
			mb.setContentHash(MD5Util.getMd5(content));
			return mb;
		}
		return null;
	}
}
