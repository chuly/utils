package com.bbq.util.selenium.thread.config;

import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import com.bbq.util.selenium.bean.HttpProxyBean;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class ConfigParam {
	public static final int max_work_thread_num = 20;//工作线程最大数
//	public static final int min_work_thread_num = 10;//工作线程最小数
	public static final int max_check_thread_num = 200;//代理有效性检查线程最大数
//	public static final int min_check_thread_num = 25;//代理有效性检查线程最小数
	
	public static final Map<Thread,String> running_work_thread = Maps.newConcurrentMap();//正在运行的工作线程
	public static final Map<Thread,String> block_work_thread = Maps.newConcurrentMap();//僵死的工作线程
	public static final Map<Thread,String> running_check_thread = Maps.newConcurrentMap();//正在运行的代理检查线程
	public static final Map<Thread,String> block_check_thread = Maps.newConcurrentMap();//僵死的代理检查线程
	
	public static final AtomicInteger success_start_count = new AtomicInteger(0);//成功数（进入页面）
	public static final AtomicInteger success_complete_count = new AtomicInteger(0);//成功数 （完成页面内所有操作）
	
	public static final Map<String,Date> used_proxy = Maps.newConcurrentMap();//已使用的代理
//	public static final Queue<HttpProxyBean> proxy = Queues.newArrayBlockingQueue(max_work_thread_num * 2);//待用的代理
	
	public static final String chrome_driver_file = "d:/selenium/chromedriver/v2.29/chromedriver.exe";//chrome驱动
	public static final String firefox_driver_file = "d:/selenium/firefoxdriver/v0.16.1/geckodriver.exe";//火狐驱动

}
