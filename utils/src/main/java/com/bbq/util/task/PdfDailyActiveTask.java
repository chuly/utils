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

public class PdfDailyActiveTask {
	
	private static final String[] excludeWords = {"头条","今日头条","www.toutiao.com"};
	private static final int[] authorArr = {17,18,19};//作者id
	
	public static void main(String[] args) {
		int maxCount = 3;
		String mysqlUrl="jdbc:mysql://localhost:3306/dzs?autoReconnect=true&useSSL=false&characterEncoding=UTF-8&user=root&password=root";
		if(args != null && args.length >= 2){
			System.out.println("自定义参数");
			maxCount = Integer.parseInt(args[0]);
			mysqlUrl = args[1];
		}
		System.out.println("maxCount="+maxCount);
		System.out.println("mysqlUrl="+mysqlUrl);
		try {
			update(mysqlUrl,maxCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void update(String mysqlUrl, int maxCount) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(mysqlUrl);
		Statement stmt = con.createStatement();
		//验证是否存在可激活的数据
		String exitsTopicSQL = "select count(1) c from t_dzs_info where valid is null or valid != 1";
		System.out.println(exitsTopicSQL);
		ResultSet rs = stmt.executeQuery(exitsTopicSQL);
		if(rs.next() && rs.getInt(1) <= 0){
			System.out.println("不存在可激活数据，返回");
			return;
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		//开始更新
		String updateSQL = "update t_dzs_info set valid=1,create_time_str='" + f.format(new Date())
				+ "' WHERE valid IS NULL OR valid != 1 LIMIT " + maxCount;
		System.out.println("updateSQL="+updateSQL);
		int updateCount = stmt.executeUpdate(updateSQL);
		System.out.println("应激活："+maxCount+"，实际激活："+updateCount);
	}
}
