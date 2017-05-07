package com.bbq.util.selenium;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.bbq.util.selenium.bean.HttpProxyBean;


public class SeleniumTest {

	public static void main(String[] args) throws Exception{
//		start();
		execAutoQuery();
	}
	public static void start() throws Exception{
		int maxSuccessCount = 1;
		int curSuccessCount = 0;
		int curTotleCount = 0;
		int totlePage = 1851;
		int page = 1;
		loop : while(page <= totlePage){
			log("第"+page+"页代理，开始。。。");
			List<HttpProxyBean> proxyHostList = HttpProxySearcher.searchXiciHttpProxy(page++);
			if(proxyHostList != null && proxyHostList.size() > 0){
				for(HttpProxyBean proxyHost : proxyHostList){
					if(curSuccessCount >= maxSuccessCount){
						log("已达最大成功数，总数：" + curTotleCount + "，成功数：" + curSuccessCount + ",最大成功数：" + maxSuccessCount);
						break loop;
					}
					curTotleCount++;
					try {
						boolean r = doOne(curTotleCount, curSuccessCount, proxyHost);
						if(r){
							curSuccessCount++;
						}
					} catch (Exception e) {
					}
				}
			}else{
				log("代理数为0，proxyHostList="+proxyHostList);
			}
		}
		
	}
	
	private static boolean doOne(int curTotleCount, int curSuccessCount, HttpProxyBean proxyHost) throws Exception {
		log("验证代理有效性：" + proxyHost.getIp() + ":" + proxyHost.getPort() + "，总数：" + curTotleCount + "，成功数："
				+ curSuccessCount);
		HttpHost hh = new HttpHost(proxyHost.getIp(), Integer.parseInt(proxyHost.getPort()), "http");
		try {
			HttpClientProxy.checkProxy("http://s.ip-cdn.com/css/bootstrap.min.css",hh);
			log("代理链接成功,开始设置代理");
		} catch (Exception e) {
			log("代理链接失败:"+e.getMessage());
			throw e;
		}
		try {
			IEProxy.execCmd("cmd.exe /C start d:\\mybat\\setProxy.bat "+proxyHost.getIp()+":"+proxyHost.getPort());
			log("代理设置成功，开始执行......");
			execAutoQuery();
			log("执行完毕，清除代理");
			return true;
		} catch (Exception e) {
			log("执行失败:"+e.getMessage());
			throw e;
		} finally{
			IEProxy.execCmd("cmd.exe /C start d:\\mybat\\cleanproxy.bat");
			Thread.sleep(1000);
			IEProxy.closeCmdWindow();
		}
	}
	public static void execAutoQuery() throws Exception{
		String chromeDriver = "d:/chromedriver/v2.29/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDriver);
		WebDriver dr = new ChromeDriver();
		//方法用来设置页面完全加载的超时时间，完全加载即页面全部渲染，异步同步脚本都执行完成。
//		dr.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		
		//隐式等待使得WebDriver在查找一个Element或者Element数组时，每隔一段特定的时间就会轮询一次DOM，如果Element或数组没有马上被发现的话。
		//默认设置是0。一旦设置，这个隐式等待会在WebDriver对象实例的整个生命周期起作用。一劳永逸。
//		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			dr.get("http://www.biubiuq.cn");
			WebElement we = dr.findElement(By.linkText("【资源共享】电影都发在这里"));
			//.click();
			System.out.println(we);
			we.click();
			dr.findElement(By.linkText("速度与激情8")).click();
			Random r = new Random();
			delay(2000, 3000);
			dr.findElement(By.id("free_down_link")).click();
			delay(1000, 2000);
			dr.findElement(By.id("free_down_link")).click();
			System.out.println("下载完成");
			delay(1000, 2000);
			System.out.println("退出");
		} finally {
			if(dr != null){
				dr.quit();
			}
		}
		
	}
	
	private static void delay(long t1,long randomT2) throws Exception{
		Random r = new Random();
		long sleepTime = 2000 + r.nextInt(2000);
		System.out.println("睡眠ms："+sleepTime);
		Thread.sleep(sleepTime);
	}
	static SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static void log(String s){
		System.out.println("SeleniumTest "+f.format(new Date())+" "+s);
	}
	
}
