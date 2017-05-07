package test;

import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class Test {

	public static void main(String[] args) throws Exception{
		sendGetRequest();
//		tt();
	}
	
	public static void tt() throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String url = "www.weimob.com";//"httpbin.org";
			HttpHost target = new HttpHost(url, 443, "https");
			HttpGet request = new HttpGet("/");
			
			HttpHost proxy = getHttpHost();
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			request.setConfig(config);

			System.out.println("Executing request " + request.getRequestLine()
					+ " to " + target + " via " + proxy);

			CloseableHttpResponse response = httpclient
					.execute(target, request);
			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				System.out.println(EntityUtils.toString(response.getEntity()));
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	public static void sendGetRequest() throws Exception {
		String result = null;
		// POST的URL
//		String url1 = "dsp.weimob.com";
//		String url2 = "www.biubiuq.cn";
//		HttpHost target = new HttpHost(url2, 80, "http");
		String url = "www.biubiuq.cn";//"www.weimob.com";//"httpbin.org";
		HttpHost target = new HttpHost(url, 80, "http");
		HttpGet httpGet = new HttpGet("/");
		
		HttpHost proxy = getHttpHost();
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();  
		httpGet.setConfig(config);  
		// 建立HttpPost对象
		// 添加header
//		if (headerMap != null && headerMap.size() > 0) {
//			for (Map.Entry<String, String> en : headerMap.entrySet()) {
//				httpGet.setHeader(new BasicHeader(en.getKey(), en.getValue()));
//			}
//		}
		// 发送Post,并返回一个HttpResponse对象
		HttpResponse response = HttpClients.createDefault().execute(target,httpGet);
		int status = response.getStatusLine().getStatusCode();
		if (status == 200) {// 如果状态码为200,就是正常返回
			result = EntityUtils.toString(response.getEntity());
		} else {
			System.out.println("错误的状态 ： status=" + status);
		}
		System.out.println(result);
	}
	
	private static HttpHost getHttpHost(){
		return new HttpHost("222.187.227.40", 10000, "http");
	}

}
