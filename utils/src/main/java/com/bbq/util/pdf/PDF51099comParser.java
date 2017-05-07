package com.bbq.util.pdf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.bbq.util.utils.HttpClientUtil;
import com.bbq.util.utils.bean.ToutiaoBean;

//http://10000.51099.com/
public class PDF51099comParser {
	static String mysqlUrl="jdbc:mysql://localhost:3306/pdf?autoReconnect=true&useSSL=false&characterEncoding=UTF-8&user=root&password=root";
	public static void main(String[] args) {
		List<Bean> details = null;
		try {
			List<Bean> list = queryListHtml("http://10000.51099.com");
			System.out.println(list);
			details = new ArrayList();
			System.out.println("书类型总数："+list.size());
			int i = 1;
			for(Bean b : list){
				System.out.println("i="+(i++));
				try {
					List<Bean> listDetailTmp = queryDetailHtml(b);
					details.addAll(listDetailTmp);
				} catch (Exception e) {
					System.out.println("==============出错");
					e.printStackTrace();
				}
			}
			System.out.println("书总数："+details.size());
			insertToDB(details, mysqlUrl);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("失败====下面为details：");
			System.out.println(details);
		}
	}
	
	private static List<Bean> queryDetailHtml(Bean b) throws Exception{
		String html = httpGet(b.getTypeUrl());
		Document doc = Jsoup.parse(html);
		Elements elements1 = doc.select("table");
		List<Bean> list = new ArrayList();
		if(elements1 != null && elements1.size() > 0){
			System.out.println("table个数："+elements1.size());
			Element tableE = elements1.get(0);
			Elements trArr = tableE.select("tr");
			for (Element tr : trArr) {
				Elements tdArr = tr.select("td");
				Bean b1 = parseOneOfDetail(tdArr.get(0),b);
				Bean b2 = parseOneOfDetail(tdArr.get(1),b);
				list.add(b1);
				list.add(b2);
			}
		}
		
		return list;
	}
	
	private static String httpGet(String url)throws Exception{
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headMap.put("Accept-Encoding", "gzip, deflate, sdch");
		headMap.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2");
		headMap.put("Connection", "keep-alive");
//		headMap.put("Cookie", "UM_distinctid=15b866130dd2c8-06d57ab32e063e-5e4f2b18-144000-15b866130e0af; CNZZDATA167778=cnzz_eid%3D522262787-1492608158-%26ntime%3D1492608158");
		headMap.put("Host", "10000.51099.com");
		headMap.put("Referer", "http://10000.51099.com");
//		headMap.put("If-Modified-Since", "Mon, 25 Mar 2013 07:15:54 GMT");
		headMap.put("Upgrade-Insecure-Requests", "1");
		headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		String html = HttpClientUtil.execGet(url,headMap);
		String html2 = new String(html.getBytes("ISO-8859-1"),"gbk");
		return html2;
	}
	
	private static List<Bean> queryListHtml(String url) throws Exception{
		String html = httpGet(url);
		Document doc = Jsoup.parse(html);//connect("http://www.weather.com.cn/html/weather/101280101.shtml").get();
		Elements elements1 = doc.select("table").select("[width=800]");
		List<Bean> list = new ArrayList();
		if(elements1 != null && elements1.size() > 0){
			System.out.println("table个数："+elements1.size());
			Element tableE = elements1.get(0);
			Elements trArr = tableE.select("tr");
			for (Element tr : trArr) {
				Elements tdArr = tr.select("td");
				Bean b1 = parseOneOfList(tdArr.get(0));
				Bean b2 = parseOneOfList(tdArr.get(1));
				list.add(b1);
				list.add(b2);
			}
		}
		
		return list;
	}
	private static Bean parseOneOfList(Element td ){
		Elements aArr = td.select("a");
		String text = aArr.text();
		String url = aArr.attr("tppabs");
		return new Bean(text,url);
	}
	private static Bean parseOneOfDetail(Element td, Bean b){
		Elements aArr = td.select("a");
		String text = aArr.text();
		String url = aArr.attr("href");
		return new Bean(b.getTypeName(),b.getTypeUrl(),text,url);
	}
	static class Bean {
		private String typeName;
		private String typeUrl;
		private String bookName;
		private String bookUrl;
		private Date createTime; 
		public Bean(String typeName,String typeUrl){
			this.typeName = typeName;
			this.typeUrl =  typeUrl;
		}
		public Bean(String typeName,String typeUrl,String bookName,String bookUrl){
			this.typeName = typeName;
			this.typeUrl =  typeUrl;
			this.bookName = bookName;
			this.bookUrl =  bookUrl;
		}
		
		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeUrl() {
			return typeUrl;
		}

		public void setTypeUrl(String typeUrl) {
			this.typeUrl = typeUrl;
		}

		public String getBookName() {
			return bookName;
		}

		public void setBookName(String bookName) {
			this.bookName = bookName;
		}

		public String getBookUrl() {
			return bookUrl;
		}

		public void setBookUrl(String bookUrl) {
			this.bookUrl = bookUrl;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
		
	}
	
	private static void insertToDB(List<Bean> list, String mysqlUrl) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(mysqlUrl);
		Statement stmt = con.createStatement();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//开始插入
		String insertTopicSQL = "INSERT INTO t_51099 (type_name,type_url,book_name,book_url,create_time) "
				+ "VALUES(?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(insertTopicSQL);
		int i = 0;
		for(Bean b : list){
			i++;
			System.out.println("第" + i + "个");
			pstmt.setString(1, b.getTypeName());
			pstmt.setString(2, b.getTypeUrl());
			pstmt.setString(3, b.getBookName());
			pstmt.setString(4, b.getBookUrl());
			pstmt.setString(5, b.getCreateTime()==null?null:f.format(b.getCreateTime()));
			int c = pstmt.executeUpdate();
		}
	}
	
}
