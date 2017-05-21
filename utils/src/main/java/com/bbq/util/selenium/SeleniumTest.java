package com.bbq.util.selenium;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.bbq.util.constEnum.PCUserAgentEnum;
import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.selenium.thread.config.ConfigParam;
import com.bbq.util.selenium.util.DelayUtil;
import com.bbq.util.selenium.util.HttpClientProxy;
import com.bbq.util.selenium.util.HttpProxySearcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class SeleniumTest extends Thread{
	private static final Logger log = Logger.getLogger(SeleniumTest.class);
	private static Map<String,String> usedProxy = Maps.newHashMap();
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		try {
//			new HttpProxySearcherThread(orderNo).start();
//			new SeleniumTest().start();
			new SeleniumTest().testExecAutoQueryFireFox();
//			 start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTimme = System.currentTimeMillis();
		System.out.println((endTimme-startTime));
	}
	static int toutiaoSuccessCount1 = 0;
	static int toutiaoSuccessCount2 = 0;
	public void run(){
		int maxSuccessCount = 500;
		int curSuccessCount = 0;
		int curTotleCount = 0;
		int totlePage = 5000;
		loop:while(true){
			List<HttpProxyBean> list = null;
			while(list == null || list.size() == 0){
				try {
					Thread.sleep(1000);
					list = getProxyHost();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			int a= 0;
			for (HttpProxyBean proxyHost : list) {
				try {
					log("成功数/总数：" + curSuccessCount + "(头条：" + toutiaoSuccessCount1 + "-" + toutiaoSuccessCount2 + ")/" + ++curTotleCount
							+ ",本页代理[当前index/总zide]：" + ++a + "/" + list.size());
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
	int netPage = 1;
	private List<HttpProxyBean> getProxyHost() throws Exception{
		List<HttpProxyBean> retList = Lists.newArrayList();
		int minSecond = 0;
		int maxSecond = 60;
		while(retList == null || retList.size() == 0){
			minSecond++;
			int second = Math.min(minSecond, maxSecond);
			Thread.sleep(second * 1000);
			List<HttpProxyBean> list = getProxyHost(1);
			boolean isNetNew = false;//网站上的代理是否刷新
			for (HttpProxyBean httpProxyBean : list) {
				String key = httpProxyBean.getIp()+":"+httpProxyBean.getPort();
				if(!usedProxy.containsKey(key)){
					isNetNew = true;
					break;
				}
			}
			if(!isNetNew){
				log("代理没有刷新，从原来的页码（"+netPage+"）开始获取");
				Thread.sleep(second * 1000);
				list = getProxyHost(++netPage);
			}else{
				log("代理已刷新，从第一页开始获取");
				netPage=1;
			}
			for (HttpProxyBean httpProxyBean : list) {
				String key = httpProxyBean.getIp()+":"+httpProxyBean.getPort();
				if(usedProxy.containsKey(key)){
					log("此代理【"+key+"】已使用，忽略");
					continue;
				}else{
					retList.add(httpProxyBean);
					usedProxy.put(key, null);
				}
			}
		}
		minSecond = 0;
		return retList;
	}
	
	private List<HttpProxyBean> getProxyHost(int page){
		List<HttpProxyBean> list = Lists.newArrayList();
		try{
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
		list = HttpProxySearcher.searchKuaidailiHttpProxy(page++);
		
		// 从西祠代理取
//		list = HttpProxySearcher.searchXiciHttpProxy(page++);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list == null?Lists.newArrayList(new HttpProxyBean()):list;
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
			String adUrl = "http://www.toutiao.com/i6420379203010560513/";
			int maxH = 6000;
//			String adUrl = "http://www.toutiao.com/i6420732837464375809/";
//			int maxH = 4500;
//			String adUrl = "http://baijiahao.baidu.com/builder/preview/s?id=1567561791269268";
//			int maxH = 4500;
			dr.get(adUrl);
			delay(1000, 2000);
			toutiaoSuccessCount1++;
			if(dr instanceof JavascriptExecutor){
				int curH = 0;
				while(curH < maxH){
					curH += 200+new Random().nextInt(400);
					JavascriptExecutor driver_js= (JavascriptExecutor) dr;
					String js = "window.scrollTo(0,"+curH+")";
					log("执行js："+js);
					driver_js.executeScript(js);
					delay(1000, 2000);
				}
				toutiaoSuccessCount2++;
			}
			dr.get("http://www.biubiuq.cn/topic/803?t=c");
//			log("寻找..");
//			WebDriverWait wait = new WebDriverWait(dr,10);  
//	        wait.until(new ExpectedCondition<WebElement>(){  
//	            public WebElement apply(WebDriver d) {  
//	                return d.findElement(By.linkText("速度与激情8"));  
//	            }}).click();  
			dr.findElement(By.linkText("速度与激情8")).click();
//			log("已经点击..");
			delay(500, 1500);
			dr.findElement(By.id("free_down_link")).click();
			delay(500, 1500);
			dr.findElement(By.id("free_down_link")).click();
			log("下载完成");
			delay(500, 1500);
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
	public void testExecAutoQueryFireFox() throws Exception{
		System.setProperty ( "webdriver.gecko.driver" , ConfigParam.firefox_driver_file );
		//firefox修改user-agent，chrome无
		FirefoxProfile profile = new FirefoxProfile();
//		profile.addAdditionalPreference("general.useragent.override", "some UA string");
		profile.setPreference("general.useragent.override", "xxxxxdeliulanqi");
		FirefoxOptions option = new FirefoxOptions();
		option.addArguments("--user-agent=Apple Iphone 5");
//		WebDriver driver = new FirefoxDriver(profile);
//		WebDriver dr = new FirefoxDriver(option);
		
		System.setProperty("webdriver.chrome.driver", ConfigParam.chrome_driver_file);
	    //声明chromeoptions,主要是给chrome设置参数
	    ChromeOptions options = new ChromeOptions();
	    //设置user agent为iphone5
//	    options.addArguments("user-agent=clydeliulanqi");
	    options.addArguments("user-agent="+PCUserAgentEnum.getRandomUserAgent());
//	    options.addArguments("referer=http://www.cly.com");
//	    options.addArguments("accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8,xxxxxcly");
	    //实例化chrome对象，并加入选项
	    WebDriver dr = new ChromeDriver(options);
		try {
			dr.get("https://baijiahao.baidu.com/po/feed/share?context=%7B%22nid%22%3A%22news_3271511677168637775%22%2C%22sourceFrom%22%3A%22bjh%22%7D&fr=bjhauthor&type=news");
			delay(1000, 2000);
			Actions action = new Actions(dr);
//			action.moveToElement(dr.findElement(By.className("mth-wangmeng wangmeng-container relate-ad-show")));
			action.moveToElement(dr.findElement(By.cssSelector("div[class='mth-wangmeng wangmeng-container relate-ad-show']")));
			action.click();
//			dr.findElement(By.linkText("【新鲜事】浙江一对母女老乡下了迷药，不满16周岁的女儿被祸害了")).click();
//			if(dr instanceof JavascriptExecutor){
//				JavascriptExecutor driver_js= (JavascriptExecutor) dr;
//				String js = "window.scrollTo(0,"+curH+")";
//				log.info("执行js："+js);
//				driver_js.executeScript(js);
//				DelayUtil.delay(1000, 2000);
//			}
//			dr.get("http://www.biubiuq.cn");
//			delay(1000, 2000);
//			dr.findElement(By.linkText("【资源共享】电影都发在这里")).click();
//			delay(1000, 2000);
//			dr.findElement(By.linkText("速度与激情8")).click();
//			delay(2000, 3000);
//			dr.findElement(By.id("free_down_link")).click();
//			delay(1000, 2000);
//			dr.findElement(By.id("free_down_link")).click();
//			System.out.println("下载完成");
			delay(3000, 2000);
			System.out.println("退出");
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(dr != null){
				dr.quit();
			}
		}
		
	}
	
	private void delay(long t1,int randomT2) throws Exception{
		long sleepTime = t1 + new Random().nextInt(randomT2);
		System.out.println("睡眠ms："+sleepTime);
		Thread.sleep(sleepTime);
	}
	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private void log(String s){
		System.out.println("SeleniumTest "+f.format(new Date())+" "+s);
//		log.info(s);
	}
	
}
