package com.bbq.util.selenium.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.selenium.thread.config.ConfigParam;

public class JdbcUtil {
	private static final Logger log = LoggerFactory.getLogger(JdbcUtil.class);

	private static Connection con;
	private static PreparedStatement pstmt;
	
	private static boolean needInit() {
		return con == null || pstmt == null;
	}
	public static void init(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(ConfigParam.mysql_url);
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
		if(needInit()){
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
	
	public static void loadUserProxyFromDB() throws Exception{
		if(!ConfigParam.load_used_proxy_from_db){
			return;
		}
		if(needInit()){
			init();
		}
		Statement st = con.createStatement();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date();
		curDate.setTime(curDate.getTime() - 24*3600*1000);
		String whereSql = " create_date>='"+f.format(curDate)+"'";
		ResultSet rs = st.executeQuery("select count(1) from t_http_proxy where check_result=10 and "+whereSql);
		int totleCount = 0;
		int n = 0;
		int curCount = 0;
		int limitCount = 100000;
		int pageSize = 1000;
		if(rs.next()){
			totleCount = rs.getInt(1);
		}
		if(totleCount <= limitCount){
			while (curCount<totleCount){
				String querySql = "select * from t_http_proxy where check_result=10 and "+whereSql+" limit "+curCount+","+pageSize;
				log.info("querySql="+querySql);
				rs = st.executeQuery(querySql);
				while(rs.next()){
					String key = rs.getString("ip")+":"+rs.getString("port");
					Date createDate = rs.getTime("create_date");
					ConfigParam.used_proxy.put(key, createDate);
					log.info("添加已用代理("+(++n)+"/"+totleCount+"):"+key+","+f.format(createDate));
				}
				curCount+=pageSize;
			}
		}else{
			log.info("已用代理数据量超过"+limitCount+"("+totleCount+")，不加载");
		}
	}
	
}
