package com.bbq.util.utils.freeHttpProxyParser;

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
public class KuaidailiHtmlParser  implements DailiHtmlParserItf{
	
	public List<HttpProxyBean> parseList(String html) {
		List<HttpProxyBean> list = new ArrayList();
		Document doc = Jsoup.parse(html);//connect("http://www.weather.com.cn/html/weather/101280101.shtml").get();
		Elements elements1 = doc.select("table[class=table table-bordered table-striped]").select("tr");
		if(elements1 != null && elements1.size() > 1){
			for (int i = 1; i < elements1.size(); i++) {//除去table头
				Element element = elements1.get(i);
				Elements tds = element.select("td");
				if(tds!= null && tds.size() == 7){
					String ip = tds.get(0).text();
					String port = tds.get(1).text();
//					String ip = tds.get(2).text();
//					String ip = tds.get(3).text();
					String addr = tds.get(4).text();
//					String ip = tds.get(5).text();
//					String ip = tds.get(6).text();
					HttpProxyBean hp = new HttpProxyBean();
					hp.setIp(ip);
					hp.setPort(port);
					hp.setAddr(addr);
					list.add(hp);
				}
			}
		}
		return list;
	}


	private void log(String s){
		System.out.println(s);
	}
}
