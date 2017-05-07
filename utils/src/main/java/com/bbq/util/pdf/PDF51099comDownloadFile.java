package com.bbq.util.pdf;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.bbq.util.utils.HttpClientUtil;

//http://10000.51099.com/
public class PDF51099comDownloadFile {
	static String mysqlUrl="jdbc:mysql://localhost:3306/pdf?autoReconnect=true&useSSL=false&characterEncoding=UTF-8&user=root&password=root";
	static String localPath = "d:/tmp/51099";
	public static void main(String[] args) {
		try {
			List<Bean> list = selectFromDB(mysqlUrl);
			System.out.println("书总数："+list.size());
			int i = 1;
			int repeatCount=1;
			int existFileCount=0;
			for(Bean b : list){
				System.out.println("i="+(i++));
				String secPath = localPath+"/"+b.getTypeName();
				File secPathF = new File(secPath);
				if(!secPathF.exists()){
					secPathF.mkdirs();
				}
				String localFileName = secPath+"/"+b.getBookName();
				File f = new File(localFileName);
				if(f.exists()){
					localFileName = localFileName+"_"+(repeatCount++);
				}
				localFileName = localFileName+getFileHouzhui(b.getBookUrl());
				if(new File(localFileName).exists()){
					existFileCount++;
					System.out.println("已存在，跳过，existFileCount："+existFileCount);
	                continue;
	            }
				try {
					HttpClientUtil.downLoad(b.getBookUrl(), b.getBookName(), localFileName);
				} catch (Exception e) {
					System.out.println("==============出错");
					e.printStackTrace();
				}
			}
			System.out.println("repeatCount="+repeatCount+",existFileCount="+existFileCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getFileHouzhui(String url){
		String[] arr = url.split("\\.");
		return "."+arr[arr.length-1];
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
	
	private static List<Bean> selectFromDB(String mysqlUrl) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(mysqlUrl);
		Statement stmt = con.createStatement();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String selectSQL = "SELECT type_name,type_url,book_name,book_url FROM t_51099";
		System.out.println(selectSQL);
		ResultSet rs = stmt.executeQuery(selectSQL);
		List<Bean> list = new ArrayList();
		while (rs.next()) {
			String typeName = rs.getString(1);
			String typeUrl =  rs.getString(2);
			String bookName = rs.getString(3);
			String bookUrl =  rs.getString(4);
			Bean b = new Bean(typeName, typeUrl, bookName, bookUrl);
			list.add(b);
		}
		return list;
	}
	
}
