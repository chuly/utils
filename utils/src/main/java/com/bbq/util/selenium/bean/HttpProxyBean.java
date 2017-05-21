package com.bbq.util.selenium.bean;

import java.util.Date;

public class HttpProxyBean {

	private String ip;
	private String port;
	private String httpType = "http";// 协议类型 http https
	private String oriUrl;// 来源
	private int type;// 10：国内高匿代理，20：国内普通代理，30：国外高匿代理，40：国外普通代理
	private String addr;// 地址
	private Date createDate;// 创建日期

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getOriUrl() {
		return oriUrl;
	}

	public void setOriUrl(String oriUrl) {
		this.oriUrl = oriUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getHttpType() {
		return httpType;
	}

	public void setHttpType(String httpType) {
		this.httpType = httpType;
	}

}
