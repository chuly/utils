package com.bbq.util.selenium;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.selenium.util.HttpClientProxy;
import com.bbq.util.selenium.util.HttpProxySearcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class SeleniumTest extends Thread{
	private static Map<String,String> usedProxy = Maps.newHashMap();
	public static void main(String[] args) {
		try {
//			new HttpProxySearcherThread(orderNo).start();
			new SeleniumTest().start();
//			 start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void run(){
		int maxSuccessCount = 500;
		int curSuccessCount = 0;
		int curTotleCount = 0;
		int totlePage = 5000;
		loop:while(true){
			List<HttpProxyBean> list = null;
			try {
				list = getProxyHost();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			int a= 0;
			for (HttpProxyBean proxyHost : list) {
				try {
					log("成功数/总数：" + curSuccessCount + "/" + ++curTotleCount +",本页代理[当前index/总zide]："+ ++a +"/"+list.size());
					String key = proxyHost.getIp()+":"+proxyHost.getPort();
					if(usedProxy.containsKey(key)){
						log("此代理["+key+"]已使用过，忽略");
						continue;
					}
					usedProxy.put(key, null);
					boolean r = doOne(proxyHost);
					if(r){
						curSuccessCount++;
					}
					if(curSuccessCount >= maxSuccessCount){
						log("已达最大成功数，总数：" + curTotleCount + "，成功数：" + curSuccessCount + ",最大成功数：" + maxSuccessCount);
						break loop;
					}
				} catch (Exception e) {
				}
			}
			
		}
		
	}
	
	private List<HttpProxyBean> getProxyHost() throws Exception{
		List<HttpProxyBean> list = Lists.newArrayList();
//		// 实时从无忧收费代理去
//		HttpProxyBean proxyHost = HttpProxySearcherThread.getProxy("a36067644c76e5bf53fe32806b479db1").get(0);
//		while(proxyHost == null){
//			log("proxyHost为null，等待1秒继续获取");
//			Thread.sleep(1000);
//			proxyHost = HttpProxySearcherThread.getOneProxy();
//		}
////		proxyHost = HttpProxySearcherThread.getOneProxy();
//		list.add(proxyHost);
		
		// 从无忧免费代理取
//		list = HttpProxySearcher.searchWuyouHttpProxy();
		
		// 从快代理取
//		list = HttpProxySearcher.searchKuaidailiHttpProxy(1);
		
		// 从西祠代理取
		list = HttpProxySearcher.searchXiciHttpProxy(1);
		return list;
	}
	
	private boolean doOne(HttpProxyBean proxyHost) throws Exception {
		log("验证代理有效性：" + proxyHost.getIp() + ":" + proxyHost.getPort());
		HttpHost hh = new HttpHost(proxyHost.getIp(), Integer.parseInt(proxyHost.getPort()), "http");
		try {
			boolean checkResult = HttpClientProxy.checkProxy(hh);
			if(checkResult == false){
				log("代理验证失败,继续下一轮");
				return false;
			}else{
				log("代理验证成功,开始设置代理");
			}
		} catch (Exception e) {
			log("代理验证失败:"+e.getMessage());
			throw e;
		}
		try {
//			IEProxy.execCmd("cmd.exe /C start d:\\mybat\\setProxy.bat "+proxyHost.getIp()+":"+proxyHost.getPort());
//			log("代理设置成功，开始执行......");
//			Thread.sleep(1000);
			execAutoQuery(hh);
			log("执行完毕，清除代理");
			return true;
		} catch (Exception e) {
			log("执行失败:"+e.getMessage());
			throw e;
		} finally{
//			Thread.sleep(1000);
//			IEProxy.execCmd("cmd.exe /C start d:\\mybat\\cleanproxy.bat");
//			Thread.sleep(1000);
//			IEProxy.closeCmdWindow();
//			Thread.sleep(1000);
		}
	}
	public void execAutoQuery(HttpHost hh) throws Exception{
		String chromeDriver = "d:/chromedriver/v2.29/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDriver);
		
		//方法用来设置页面完全加载的超时时间，完全加载即页面全部渲染，异步同步脚本都执行完成。
//		dr.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		
		//隐式等待使得WebDriver在查找一个Element或者Element数组时，每隔一段特定的时间就会轮询一次DOM，如果Element或数组没有马上被发现的话。
		//默认设置是0。一旦设置，这个隐式等待会在WebDriver对象实例的整个生命周期起作用。一劳永逸。
//		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		String proxyIpAndPort = hh.getHostName()+":"+hh.getPort();
		DesiredCapabilities cap = new DesiredCapabilities();
		Proxy proxy=new Proxy();
		proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
		cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
		cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
		System.setProperty("http.nonProxyHosts", "localhost");
		cap.setCapability(CapabilityType.PROXY, proxy);
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver dr = new ChromeDriver(cap);
//		dr.manage().window().maximize(); //将浏览器设置为最大化的状态
		dr.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//firefox修改user-agent，chrome无
//		FirefoxProfile profile = new FirefoxProfile();
//		profile.addAdditionalPreference("general.useragent.override", "some UA string");
//		WebDriver driver = new FirefoxDriver(profile);
		
		try {
//			dr.get("http://1212.ip138.com/ic.asp");
//			Thread.sleep(1000);
//			dr.get("http://www.biubiuq.cn");
//			delay(1000, 2000);
//			dr.findElement(By.linkText("【资源共享】电影都发在这里")).click();
//			delay(1000, 2000);
			dr.get("http://www.biubiuq.cn/topic/803?t=c");
//			log("寻找..");
//			WebDriverWait wait = new WebDriverWait(dr,10);  
//	        wait.until(new ExpectedCondition<WebElement>(){  
//	            public WebElement apply(WebDriver d) {  
//	                return d.findElement(By.linkText("速度与激情8"));  
//	            }}).click();  
			dr.findElement(By.linkText("速度与激情8")).click();
//			log("已经点击..");
			delay(2000, 3000);
			dr.findElement(By.id("free_down_link")).click();
			delay(1000, 2000);
			dr.findElement(By.id("free_down_link")).click();
			log("下载完成");
			delay(1000, 2000);
			log("退出");
		} finally {
			if(dr != null){
				dr.quit();
			}
		}
		
	}
	public void testExecAutoQuery() throws Exception{
		String chromeDriver = "d:/chromedriver/v2.29/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDriver);
//		WebDriver dr = new ChromeDriver();
		File file = new File ("C:\\工具\\chrome扩展程序\\2.1.2_0.crx");
		ChromeOptions options = new ChromeOptions();
		FirefoxProfile ff = new FirefoxProfile();
		options.addExtensions(file);
//		options.setExperimentalOption("Referer", "ffff");
		WebDriver dr = new ChromeDriver(options);
		//方法用来设置页面完全加载的超时时间，完全加载即页面全部渲染，异步同步脚本都执行完成。
//		dr.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		
		//隐式等待使得WebDriver在查找一个Element或者Element数组时，每隔一段特定的时间就会轮询一次DOM，如果Element或数组没有马上被发现的话。
		//默认设置是0。一旦设置，这个隐式等待会在WebDriver对象实例的整个生命周期起作用。一劳永逸。
//		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			dr.get("http://www.biubiuq.cn");
			delay(1000, 2000);
			dr.findElement(By.linkText("【资源共享】电影都发在这里")).click();
			delay(1000, 2000);
			dr.findElement(By.linkText("速度与激情8")).click();
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
	
	private void delay(long t1,long randomT2) throws Exception{
		Random r = new Random();
		long sleepTime = 2000 + r.nextInt(2000);
		System.out.println("睡眠ms："+sleepTime);
		Thread.sleep(sleepTime);
	}
	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private void log(String s){
		System.out.println("SeleniumTest "+f.format(new Date())+" "+s);
	}
	
}
