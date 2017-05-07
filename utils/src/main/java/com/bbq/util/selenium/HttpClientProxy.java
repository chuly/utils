package com.bbq.util.selenium;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpClientProxy {

	public static void main(String[] args) throws Exception {
//		checkProxy("http://s.ip-cdn.com/css/bootstrap.min.css",new HttpHost("113.251.126.71", 8998, "http"));
//		System.out.println("\b\b====================================================\b\b");
		String resp = sendGetRequest("http://s.ip-cdn.com/css/bootstrap.min.css");
		System.out.println(resp);
	}

	public static void checkProxy(String url,HttpHost proxyHost) throws Exception {
		String result = null;
		HttpHost target = new HttpHost(url, 80, "http");
		HttpGet httpGet = new HttpGet("/");

		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(10000)
				.setSocketTimeout(5000)
				.setProxy(proxyHost).build();
		httpGet.setConfig(config);
		HttpClient hc = HttpClients.createDefault();
//		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
//		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);
		
		HttpResponse response = hc.execute(target, httpGet);
		int status = response.getStatusLine().getStatusCode();
		if (status == 200) {// 如果状态码为200,就是正常返回
			result = EntityUtils.toString(response.getEntity());
		} else {
			System.out.println("错误的状态 ： status=" + status);
			throw new Exception("错误的状态 ： status=" + status);
		}
		System.out.println(result);
	}
	public static String sendGetRequest(String urlWithParam) throws Exception {
		String responseBody = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(urlWithParam);
			System.out.println("Executing request " + httpget.getRequestLine());
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						HttpEntity entity = response.getEntity();
						String ss = entity != null ? EntityUtils.toString(entity) : null;
						System.out.println(ss);
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			responseBody = httpclient.execute(httpget, responseHandler);
			System.out.println("----------------------------------------"+responseBody);
		} finally {
			httpclient.close();
		}
		return responseBody;
	}

	private static HttpHost getHttpHost() {
		return new HttpHost("222.187.227.40", 10000, "http");
	}

}
