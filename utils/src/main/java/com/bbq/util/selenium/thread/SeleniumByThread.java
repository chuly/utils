package com.bbq.util.selenium.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.bbq.util.selenium.thread.config.ConfigParam;
import com.bbq.util.selenium.thread.sub.ProxySearchThread;
import com.google.common.collect.Lists;


public class SeleniumByThread extends Thread{
	private static final Logger log = LoggerFactory.getLogger(SeleniumByThread.class);
	
	public static void main(String[] args) {
		log.info("主线程启动");
		new ProxySearchThread(1).start();
		new ProxySearchThread(2).start();
		new ProxySearchThread(3).start();
		new ProxySearchThread(100).start();
		while(true){
			try {
				Thread.sleep(60 * 1000);
			} catch (Exception e1) {
				log.error("",e1);
			}
			log.info("工作线程数:" + ConfigParam.running_work_thread.size() + "/" + ConfigParam.max_work_thread_num + "，检查线程数："
					+ ConfigParam.running_check_thread.size() + "/" + ConfigParam.max_check_thread_num);
			log.info("成功开始数:" + ConfigParam.success_start_count + "，成功结束数：" + ConfigParam.success_complete_count);
			checkRunningWork();
			checkRunningCheck();
			checkDeadWork();
			checkDeadCheck();
			
		}
	}
	//检查运行的工作线程，是否已经僵死
	private static void checkRunningWork(){
		try {
			log.info("正在运行的工作线程数目："+ConfigParam.running_work_thread.size()+",明细："+JSON.toJSONString(ConfigParam.running_work_thread));
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long curTime = System.currentTimeMillis();
			List<Thread> arr = Lists.newArrayList();
			for(Map.Entry<Thread,String> en : ConfigParam.running_work_thread.entrySet()){
				Date date = f1.parse(en.getValue());
				if(curTime - date.getTime() > 1000 * 60){//操作60秒还未完成，则当做僵死线程处理
					arr.add(en.getKey());
				}
			}
			for(Thread t : arr){
				String value = ConfigParam.running_work_thread.remove(t);
				ConfigParam.block_work_thread.put(t, value);
			}
		} catch (Exception e) {
			log.error("",e);
		}
	}
	//检查运行的代理检查线程，是否已经僵死
	private static void checkRunningCheck(){
		try {
			log.info("正在运行的代理检查线程数目："+ConfigParam.running_check_thread.size()+",明细："+JSON.toJSONString(ConfigParam.running_check_thread));
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long curTime = System.currentTimeMillis();
			List<Thread> arr = Lists.newArrayList();
			for(Map.Entry<Thread,String> en : ConfigParam.running_check_thread.entrySet()){
				Date date = f1.parse(en.getValue());
				if(curTime - date.getTime() > 1000 * 60 * 10){//操作10分钟还未完成，则当做僵死线程处理
					arr.add(en.getKey());
				}
			}
			for(Thread t : arr){
				String value = ConfigParam.running_check_thread.remove(t);
				ConfigParam.block_check_thread.put(t, value);
			}
		} catch (Exception e) {
			log.error("",e);
		}
	}
	//检查僵死的工作线程，是否已经执行完毕
	private static void checkDeadWork(){
		try {
			log.info("僵死工作线程数目："+ConfigParam.block_work_thread.size()+",明细："+JSON.toJSONString(ConfigParam.block_work_thread));
			long curTime = System.currentTimeMillis();
			List<Thread> arr = Lists.newArrayList();
			for(Map.Entry<Thread,String> en : ConfigParam.block_work_thread.entrySet()){
				if(en.getKey().isAlive() == false){//僵死的线程已经执行完毕
					arr.add(en.getKey());
				}
			}
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(Thread t : arr){
				String value = ConfigParam.block_work_thread.remove(t);
				Date date = f1.parse(value);
				log.info("工作线程耗时过长："+((curTime-date.getTime())/1000)+"秒，明细："+JSON.toJSONString(t));
			}
		} catch (Exception e) {
			log.error("",e);
		}
	}
	//检查僵死的代理检查线程，是否已经执行完毕
	private static void checkDeadCheck(){
		try {
			log.info("僵死代理检查线程数目："+ConfigParam.running_check_thread.size()+",明细："+JSON.toJSONString(ConfigParam.running_check_thread));
			long curTime = System.currentTimeMillis();
			List<Thread> arr = Lists.newArrayList();
			for(Map.Entry<Thread,String> en : ConfigParam.running_check_thread.entrySet()){
				if(en.getKey().isAlive() == false){//僵死的线程已经执行完毕
					arr.add(en.getKey());
				}
			}
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(Thread t : arr){
				String value = ConfigParam.running_check_thread.remove(t);
				Date date = f1.parse(value);
				log.info("代理检查线程耗时过长："+((curTime-date.getTime())/1000)+"秒，明细："+JSON.toJSONString(t));
			}
		} catch (Exception e) {
			log.error("",e);
		}
	}
	
}
