package com.car.rent.util;




import java.io.IOException;




import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;



public class HttpUtil {
	//
	public static final String BASE_URL="http://47.95.212.214:8080/CarRent_Server/";
	//public static final String BASE_URL="http://10.0.2.2:8080/ParkingSystem/";
	public static HttpGet getHttpGet(String url){
		HttpGet request = new HttpGet(url);
		 return request;
	}
	
	public static HttpPost getHttpPost(String url){
		 HttpPost request = new HttpPost(url);
		 return request;
	}
	
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	
	public static String queryStringForPost(String url){
	
		HttpPost request = HttpUtil.getHttpPost(url);
		String result = null;
		try {
			
			HttpResponse response = HttpUtil.getHttpResponse(request);
			
			if(response.getStatusLine().getStatusCode()==200){
			//页面请求的状态值，分别有：200请求成功、303重定向、400请求错误、401未授权、403禁止访问、404文件未找到、500服务器错误
				result = EntityUtils.toString(response.getEntity());
				//返回的是你服务端以流的形式写出的响应正文中的内容，比如在服务端调用的方法最后为：
				//responseWriter.write("this is response body");
				//那在httpclient客户端 这里就会打印出this is response body 这句话。
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "";
			return result;
		}
        return null;
    }
	
	public static String queryStringForPost(HttpPost request){
		String result = null;
		try {
			
			HttpResponse response = HttpUtil.getHttpResponse(request);
			
			if(response.getStatusLine().getStatusCode()==200){
				
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "";
			return result;
		}
        return null;
    }
	
	public static  String queryStringForGet(String url){
	
		HttpGet request = HttpUtil.getHttpGet(url);
		request.addHeader("Content-Type", "text/html"); 
		request.addHeader("charset", "utf-8");        
		String result = null;
		try {
	
			HttpResponse response = HttpUtil.getHttpResponse(request);
		
			if(response.getStatusLine().getStatusCode()==200){
			
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "";
			return result;
		}
        return null;
    }
}
