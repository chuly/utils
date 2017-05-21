package com.bbq.util.selenium.thread.sub;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.selenium.thread.config.ConfigParam;
import com.bbq.util.selenium.util.HttpClientProxy;
import com.bbq.util.selenium.util.HttpProxySearcher;
import com.google.common.collect.Lists;

public class ProxyCheckThread extends Thread {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<HttpProxyBean> proxyList;
	public ProxyCheckThread(List<HttpProxyBean> proxyList){
		this.proxyList = proxyList;
	}
	@Override
	public void run() {
		setName("ProxyCheckThread-"+getName());
		log.info("代理检查线程启动。");
		ConfigParam.running_check_thread.put(this, f.format(new Date()));
		if(proxyList != null){
			try{
				log.info("待检查代理数："+proxyList.size());
				checkAndStartWork(proxyList);
			}catch(Exception e){
				log.error("",e);
			}finally{
				ConfigParam.running_check_thread.remove(this);
			}
		}else{
			log.info("传入的代理列表为null：proxyList="+proxyList);
		}
	}
	
	//验证代理，并启动工作线程
	private void checkAndStartWork(List<HttpProxyBean> list) throws Exception{
		for (HttpProxyBean httpProxyBean : list) {
			while(ConfigParam.running_work_thread.size() >= ConfigParam.max_work_thread_num){
				log.info("当前工作线程数（"+ConfigParam.running_work_thread.size()+"）已达最大允许线程数（"+ConfigParam.max_work_thread_num+"）");
				Thread.sleep(5 * 1000);
			}
			HttpHost hh = new HttpHost(httpProxyBean.getIp(), Integer.parseInt(httpProxyBean.getPort()),
					httpProxyBean.getHttpType());
			try {
				boolean checkResult = HttpClientProxy.checkProxy(hh);
				if(checkResult == false){
					log.info("代理验证失败,继续下一轮");
				}else{
					log.info("代理验证成功,开始启动工作线程...");
					new WorkThread(httpProxyBean).start();
				}
			} catch (Exception e) {
				log.info("代理验证失败:"+e.getMessage());
				log.error("代理验证失败。"+e.getMessage());
			}
		}
	}
	
}
