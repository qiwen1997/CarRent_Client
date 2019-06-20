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
			//ҳ�������״ֵ̬���ֱ��У�200����ɹ���303�ض���400�������401δ��Ȩ��403��ֹ���ʡ�404�ļ�δ�ҵ���500����������
				result = EntityUtils.toString(response.getEntity());
				//���ص�����������������ʽд������Ӧ�����е����ݣ������ڷ���˵��õķ������Ϊ��
				//responseWriter.write("this is response body");
				//����httpclient�ͻ��� ����ͻ��ӡ��this is response body ��仰��
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
