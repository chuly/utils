package com.bbq.util.selenium.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.selenium.thread.config.ConfigParam;

public class JdbcUtil {
	private static final Logger log = LoggerFactory.getLogger(JdbcUtil.class);

	private static PreparedStatement pstmt;
	
	public static void init(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(ConfigParam.mysql_url);
			String insertTopicSQL = "INSERT INTO t_http_proxy (ip, port,ori_url,addr,check_result) "
					+ "VALUES(?,?,?,?,?)";
			pstmt = con.prepareStatement(insertTopicSQL);
		} catch (Exception e) {
			log.error("数据库连接失败", e);
		}
	}
	public static void insert(HttpProxyBean httpProxyBean,int checkResult){
		if(ConfigParam.save_to_db)
			insert(httpProxyBean.getIp(), httpProxyBean.getPort(), httpProxyBean.getOriUrl(), httpProxyBean.getAddr(), checkResult);
	}
	private static void insert(String ip,String port,String oriUrl,String addr,int checkResult){
		if(pstmt == null){
			init();
		}
		try {
			pstmt.setString(1, ip);
			pstmt.setString(2, port);
			pstmt.setString(3, oriUrl);
			pstmt.setString(4, addr);
			pstmt.setInt(5, checkResult);
			int c = pstmt.executeUpdate();
			log.info("插入DB条数："+c);
		} catch (Exception e) {
			log.warn("插入数据库失败，ip={},port={},oriUrl={},addr={},checkResult={}",ip,port,oriUrl,addr,checkResult);
		}
	}
	
}
