package com.bbq.util.task;

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

import com.alibaba.fastjson.JSON;
import com.bbq.util.utils.HttpClientUtil;
import com.bbq.util.utils.ToutiaoHtmlParser;
import com.bbq.util.utils.bean.ToutiaoBean;

public class PybbsShehuiTask {
	
	private static final String[] excludeWords = {"头条","今日头条","www.toutiao.com"};
	private static final int[] authorArr = {17,18,19};//作者id
	
	public static void main(String[] args) {
		int maxCount = 5;
		String mysqlUrl="jdbc:mysql://localhost:3306/pybbs?autoReconnect=true&useSSL=false&characterEncoding=UTF-8&user=root&password=root";
		if(args != null && args.length >= 2){
			System.out.println("自定义参数");
			maxCount = Integer.parseInt(args[0]);
			mysqlUrl = args[1];
		}
		System.out.println("maxCount="+maxCount);
		System.out.println("mysqlUrl="+mysqlUrl);
		try {
			List<ToutiaoBean> list = queryHtml();
			insertToDB(list,mysqlUrl,maxCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static List<ToutiaoBean> queryHtml() throws Exception{
		Map<String, String> headMap = new HashMap();
		headMap.put("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
		headMap.put("Accept-Encoding", "gzip, deflate, sdch");
		headMap.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2");
		headMap.put("Connection", "keep-alive");
//		headMap.put("Cookie", "_qqq_uuid_=\"2|1:0|10:1488700163|10:_qqq_uuid_|56:ZDdjMTljZTRmZmZlMzZiMzA1NDI3ZGE3NTU1ODM3MGE2MmQ4NDM3Zg==|9bd0755c25e27aac82fa5c3b0a0aefca52f948137affe1ba6785bb37b4319b8c\"; Hm_lvt_18a964a3eb14176db6e70f1dd0a3e557=1488701485; _xsrf=2|9d7f9f6a|a883e1a963cdd88bb38665994182d6d6|1488701672; __cur_art_index=7801");
		headMap.put("Host", "www.toutiao.com");
		headMap.put("Referer", "http://www.toutiao.com/");
		headMap.put("Upgrade-Insecure-Requests", "1");
		headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		String html = HttpClientUtil.execGet("http://www.toutiao.com/api/pc/feed/?category=news_society&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A1D5E8BB0EAC929&cp=58BE4C79F2F90E1",headMap);
		Map m = JSON.parseObject(html, Map.class);
		List<Map> data = (List)m.get("data");
		List<ToutiaoBean> list = new ArrayList();
		if(data != null && data.size()>=2){
			for(int i = 1; i < data.size(); i++){
				System.out.println("第"+i+"个了哦哦");
				Map dataIdx = data.get(i);
				Object chinese_tag = dataIdx.get("chinese_tag");
				Object ad_label = dataIdx.get("ad_label");
				if(chinese_tag == null || "广告".equals(ad_label) || "情感".equals(chinese_tag)){
					System.out.println("忽略");
					continue;
				}
				String group_id = (String)dataIdx.get("group_id");
				String oriUrl = "http://www.toutiao.com/group/"+group_id;
				String html2 = HttpClientUtil.execGet(oriUrl,headMap);
				ToutiaoHtmlParser pa = new ToutiaoHtmlParser();
				ToutiaoBean mb = pa.parseDetail(html2);
				if(mb != null){
					mb.setOriUrl(oriUrl);
					list.add(mb);
				}else{
					System.out.println("解析失败");
				}
			}
			System.out.println(JSON.toJSONString(list));
			return list;
		}
		return null;
	}
	
	private static void insertToDB(List<ToutiaoBean> list, String mysqlUrl, int maxCount) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(mysqlUrl);
		Statement stmt = con.createStatement();
		//验证是否已经存在
		String exitsTopicSQL = "select content_hash from pybbs_topic where content_hash in (";
		for(ToutiaoBean qb : list){
			exitsTopicSQL += "'"+qb.getContentHash()+"',";
		}
		if(exitsTopicSQL.endsWith(",")){
			exitsTopicSQL = exitsTopicSQL.substring(0,exitsTopicSQL.length()-1);
		}
		exitsTopicSQL+=")";
		System.out.println(exitsTopicSQL);
		ResultSet rs = stmt.executeQuery(exitsTopicSQL);
		List<String> existContentList = new ArrayList();
		while (rs.next()) {
			existContentList.add(rs.getString(1));
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//开始插入
		String insertTopicSQL = "INSERT INTO `pybbs_topic` (`content`, `good`, `in_time`, `modity_time`, `reply_count`, "
				+ "`tab`, `title`, `top`, `up`, `up_ids`, `view`, `user_id`, `content_hash`) "
				+ "VALUES(?,'',?,NULL,'0','社会',?,'','0',NULL,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(insertTopicSQL);
		int i = 1;
		for(ToutiaoBean qb : list){
			if(i > maxCount){
				System.out.println("已达最大个数："+maxCount);
				break;
			}
			System.out.println("第" + i + "个");
			if(exist(existContentList, qb.getContentHash())){
				System.out.println("已存在，忽略："+JSON.toJSONString(qb));
				continue;
			}
			if(!valid(excludeWords, qb.getContent())){
				System.out.println("包含敏感词，忽略："+JSON.toJSONString(qb));
				continue;
			}
			String content = qb.getContent();
			String curDate = f.format(new Date());
			String title = "【新鲜事】"+qb.getTitle();
			int userId = authorArr[qb.hashCode() % authorArr.length];//2;
			String contentHash = qb.getContentHash();
			int randomLength = content.length() & 0xF;
			System.out.println("randomLength="+randomLength);
			int leng = 64;
			if(title!=null && title.length()>leng){
				title = title.substring(0,leng) + "...";
			}
			
			pstmt.setString(1, content);
			pstmt.setString(2, curDate);
			pstmt.setString(3, title);
			pstmt.setInt(4, new Random().nextInt(randomLength+1)+1);
			pstmt.setInt(5, userId);
			pstmt.setString(6, contentHash);
			int c = pstmt.executeUpdate();
			i++;
		}
	}
	private static boolean exist(List<String> list, String hash){
		for(String contentHash : list){
			if(hash.equals(contentHash)){
				return true;
			}
		}
		return false;
	}
	private static boolean valid(String[] excludeWords, String content){
		for(String word : excludeWords){
			if(content.indexOf(word)>=0){
				return false;
			}
		}
		return true;
	}

}
