package com.bbq.util.selenium;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;

import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSON;
import com.bbq.util.selenium.bean.HttpProxyBean;

public class HttpProxySearcherThread extends Thread{
	public static Queue<HttpProxyBean> queue = new ArrayBlockingQueue<HttpProxyBean>(100);
	
	public static void main(String[] args) throws Exception{
		
//		List<HttpProxyBean> proxyHostList = searchXiciHttpProxy(1);
//		String orderNo = "a36067644c76e5bf53fe32806b479db1";
//		List<HttpProxyBean> proxyHostList = HttpProxySearcherThread.searchWuyouHttpProxy(orderNo);
//		System.out.println(JSON.toJSONString(proxyHostList));
		
	}
	
	public static HttpProxyBean getOneProxy(){
		log("当前可用代理数queue.size="+queue.size());
		return queue.poll();
	}
	String orderNo = "";
	
	int maxSize = 1;
	int maxSleepCount = 3600;
	
	public HttpProxySearcherThread(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@Override
	public void run() {
		int curSleepCount = 1;
		int getIpTime= 0;
		loop:while(true){
			
			try {
				while(queue.size()>=maxSize){
					if(curSleepCount<maxSleepCount){
						if(curSleepCount % 100 == 0){
							log(curSleepCount+"/"+maxSleepCount+",等待1秒继续");
						}
						Thread.sleep(1000);
						curSleepCount++;
					}else{
						break loop;
					}
				}
				curSleepCount = 1;
				List<HttpProxyBean> ipList = getProxy(orderNo);
				if (ipList.size() > 0) {
					System.out.println("第" + ++getIpTime + "次获取动态IP " + ipList.size() + " 个："+JSON.toJSONString(ipList));
					for(HttpProxyBean proxyHost : ipList){
						HttpHost hh = new HttpHost(proxyHost.getIp(), Integer.parseInt(proxyHost.getPort()), "http");
						try {
							boolean checkResult = HttpClientProxy.checkProxy(hh);
							if(checkResult == false){
								log("代理验证失败,继续下一轮");
							}else{
								log("代理验证成功,开始设置代理");
								queue.add(proxyHost);
							}
						} catch (Exception e) {
							log("代理验证失败:"+e.getMessage());
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(">>>>>>>>>>>>>>获取IP出错");
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<HttpProxyBean> getProxy(String orderNo){
		List<HttpProxyBean> ipList = new ArrayList<HttpProxyBean>();
		try {
			java.net.URL url = new java.net.URL("http://api.ip.data5u.com/dynamic/get.html?order=" + orderNo + "&ttl");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(3000);
			connection = (HttpURLConnection)url.openConnection();
			
			InputStream raw = connection.getInputStream();  
			InputStream in = new BufferedInputStream(raw);  
			byte[] data = new byte[in.available()];
			int bytesRead = 0;  
			int offset = 0;  
			while(offset < data.length) {  
			    bytesRead = in.read(data, offset, data.length - offset);  
			    if(bytesRead == -1) {  
			        break;  
			    }  
			    offset += bytesRead;  
			}  
			in.close();  
			raw.close();
			String[] res = new String(data, "UTF-8").split("\n");
			
			for (String ip : res) {
				try {
					String[] parts = ip.split(",");
					if (Integer.parseInt(parts[1]) > 0) {
						HttpProxyBean hb = new HttpProxyBean();
						String[] ipport = parts[0].split(":");
						hb.setIp(ipport[0]);
						hb.setPort(ipport[1]);
						ipList.add(hb);
					}else{
						System.out.println("此ip有效期已过："+ip);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		if(ipList != null && ipList.size() > 0){
//			return ipList.get(0);
//		}
		return ipList;
	}
	static SimpleDateFormat fff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static void log(String s){
		System.out.println("HttpProxySearcherThread "+fff.format(new Date())+" "+s);
	}
	
}
