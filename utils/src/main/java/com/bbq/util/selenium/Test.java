package com.bbq.util.selenium;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class Test {

	public static void main(String[] args) throws Exception{
		String chromeDriver = "d:/chromedriver/v2.29/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDriver);
		WebDriver dr = new ChromeDriver();
		
		dr.get("http://www.biubiuq.cn");
		
//		dr.findElement(By.id("kw")).sendKeys("hello Selenium");
//		dr.findElement(By.id("su")).click();
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
		dr.quit();
	}
	
	private static void delay(long t1,long randomT2) throws Exception{
		Random r = new Random();
		long sleepTime = 2000 + r.nextInt(2000);
		System.out.println("睡眠ms："+sleepTime);
		Thread.sleep(sleepTime);
	}
	
}
