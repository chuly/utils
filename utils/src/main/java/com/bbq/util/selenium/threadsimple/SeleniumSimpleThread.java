package com.bbq.util.selenium.threadsimple;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.bbq.util.constEnum.PCUserAgentEnum;
import com.bbq.util.selenium.thread.config.ConfigParam;


public class SeleniumSimpleThread extends Thread{
	private static final int maxThreadNum = 2;
	private static ExecutorService threadPool = Executors.newFixedThreadPool(maxThreadNum);
	public static final AtomicInteger success_start_count = new AtomicInteger(0);//进入页面数
	public static final AtomicInteger complete_start_count = new AtomicInteger(0);//成功完成数
	public SeleniumSimpleThread(String threadName){
		setName(threadName);
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < maxThreadNum; i++) {
			threadPool.submit(new SeleniumSimpleThread("SeleniumSimpleThread-"+i));
		}
		while(true){
			try {
				Thread.sleep(10 * 1000);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println("=======进入页面数:" + success_start_count + "，成功完成数：" + complete_start_count);
		}
	}
	public void run(){
		while(true){
			try {
				testExecAutoQuery();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void testExecAutoQuery() throws Exception{
		System.setProperty("webdriver.chrome.driver", ConfigParam.chrome_driver_file);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-agent="+PCUserAgentEnum.getRandomUserAgent());
		WebDriver dr = new ChromeDriver(options);
		dr.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			dr.get("https://page50.ctfile.com/fs/14115250-199528686");
			success_start_count.incrementAndGet();
			delay(2000, 3000);
			dr.findElement(By.id("free_down_link")).click();
			delay(1000, 2000);
			dr.findElement(By.id("free_down_link")).click();
			log("下载完成");
			delay(1000, 2000);
			log("退出");
			complete_start_count.incrementAndGet();
		} finally {
			if(dr != null){
				dr.quit();
			}
		}
		
	}
	private void delay(long t1,int randomT2) throws Exception{
		long sleepTime = t1 + new Random().nextInt(randomT2);
		log("睡眠ms："+sleepTime);
		Thread.sleep(sleepTime);
	}
	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private void log(String s){
		System.out.println(getName()+" "+f.format(new Date())+" "+s);
	}
	
}
