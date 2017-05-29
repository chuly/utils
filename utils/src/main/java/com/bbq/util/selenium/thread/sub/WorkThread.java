package com.bbq.util.selenium.thread.sub;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbq.util.constEnum.PCUserAgentEnum;
import com.bbq.util.selenium.bean.HttpProxyBean;
import com.bbq.util.selenium.persistence.JdbcUtil;
import com.bbq.util.selenium.thread.config.ConfigParam;
import com.bbq.util.selenium.util.DelayUtil;

public class WorkThread extends Thread {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private HttpProxyBean proxyHost;
	public WorkThread(HttpProxyBean proxyHost){
		this.proxyHost = proxyHost;
	}

	@Override
	public void run() {
		setName("WorkThread-"+getName());
		log.info("工作线程启动。");
//		ConfigParam.running_work_thread.put(this, f.format(new Date()));
		try{
			execAutoQuery();
		}catch(Exception e){
			log.error("",e);
		}finally{
//			ConfigParam.running_work_thread.remove(this);
		}
	}

	public void execAutoQuery() throws Exception{
		log.info("使用代理：" + proxyHost.getIp() + ":" + proxyHost.getPort());
		System.setProperty("webdriver.chrome.driver", ConfigParam.chrome_driver_file);
		String proxyIpAndPort = proxyHost.getIp()+":"+proxyHost.getPort();
		
		DesiredCapabilities cap = new DesiredCapabilities();
		Proxy proxy=new Proxy();
		proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
		cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
		cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
		System.setProperty("http.nonProxyHosts", "localhost");
		cap.setCapability(CapabilityType.PROXY, proxy);
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("user-agent="+PCUserAgentEnum.getRandomUserAgent());
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver dr = new ChromeDriver(cap);
		dr.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
//			runToutiao(dr);
//			runChengtong(dr);
			runBaijia(dr);
		} finally {
			if(dr != null){
				dr.quit();
			}
		}
		
	}
	// 头条
	private void runToutiao(WebDriver dr) {
		String adUrl = "http://www.toutiao.com/i6420379203010560513/";
		int maxH = 6000;
		dr.get(adUrl);
		JdbcUtil.insert(proxyHost, 20);
		DelayUtil.delay(1000, 2000);
		ConfigParam.success_start_count.incrementAndGet();
		if(dr instanceof JavascriptExecutor){
			int curH = 0;
			while(curH < maxH){
				curH += 200+new Random().nextInt(400);
				JavascriptExecutor driver_js= (JavascriptExecutor) dr;
				String js = "window.scrollTo(0,"+curH+")";
				log.info("执行js："+js);
				driver_js.executeScript(js);
				DelayUtil.delay(1000, 2000);
			}
			ConfigParam.success_complete_count.incrementAndGet();
			JdbcUtil.insert(proxyHost, 30);
		}
		dr.get("http://www.biubiuq.cn/topic/803?th="+Thread.currentThread().getName());
		dr.findElement(By.linkText("速度与激情8")).click();
		DelayUtil.delay(500, 1500);
		dr.findElement(By.id("free_down_link")).click();
		DelayUtil.delay(500, 1500);
		dr.findElement(By.id("free_down_link")).click();
		log.info("下载完成");
		DelayUtil.delay(500, 1500);
		log.info("退出");
	}
	// 百家号
	private void runBaijia(WebDriver dr) {
		String adUrl = "https://baijiahao.baidu.com/po/feed/share?context=%7B%22nid%22%3A%22news_3271511677168637775%22%2C%22sourceFrom%22%3A%22bjh%22%7D&fr=bjhauthor&type=news";
		JdbcUtil.insert(proxyHost, 20);
		int maxH = 6000;
		dr.get(adUrl);
		DelayUtil.delay(1000, 2000);
		ConfigParam.success_start_count.incrementAndGet();
		if(dr instanceof JavascriptExecutor){
			int curH = 0;
			int scrollCount = 0;
			while(curH < maxH){
				curH += 200+new Random().nextInt(400);
				JavascriptExecutor driver_js= (JavascriptExecutor) dr;
				String js = "window.scrollTo(0,"+curH+")";
				log.info("执行js："+js);
				driver_js.executeScript(js);
				scrollCount++;
				DelayUtil.delay(1000, 2000);
			}
			if(scrollCount >= 2){
				ConfigParam.success_complete_count.incrementAndGet();
				JdbcUtil.insert(proxyHost, 30);
			}
		}
		DelayUtil.delay(500, 1500);
		log.info("退出");
	}
	// 诚通网盘
	private void runChengtong(WebDriver dr) {
//		String adUrl = "https://page50.ctfile.com/fs/14115250-199528686";
//		log.info("----11111------开始加载页面------");
//		dr.get(adUrl);
//		log.info("----22222------页面加载完成------");
//		WebDriverWait _wait = new WebDriverWait(dr, 10);
//		_wait.until(new ExpectedCondition<WebElement>() {
//			public WebElement apply(WebDriver d) {
//				log.info("-----33333-----查找元素------");
//				return d.findElement(By.id("free_down_link"));
//			}
//		}).click();
//		DelayUtil.delay(1000, 2000);
		
		dr.get("https://page50.ctfile.com/fs/14115250-199528686");
		ConfigParam.success_start_count.incrementAndGet();
		JdbcUtil.insert(proxyHost, 20);
		DelayUtil.delay(1500, 2000);
		dr.findElement(By.id("free_down_link")).click();
		DelayUtil.delay(500, 2000);
		dr.findElement(By.id("free_down_link")).click();
		DelayUtil.delay(500, 1000);
		ConfigParam.success_complete_count.incrementAndGet();
		JdbcUtil.insert(proxyHost, 30);
	}
	
}
