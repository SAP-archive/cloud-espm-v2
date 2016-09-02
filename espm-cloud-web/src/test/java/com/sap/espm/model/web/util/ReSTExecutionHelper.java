package com.sap.espm.model.web.util;

import java.util.Map;
import org.slf4j.Logger;
import java.util.HashMap;
import java.io.IOException;
import java.io.BufferedReader;
import org.apache.http.Header;
import org.slf4j.LoggerFactory;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.CookieStore;
import org.apache.geronimo.mail.util.Base64;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.methods.CloseableHttpResponse;



public class ReSTExecutionHelper{
	private static Logger logger = LoggerFactory.getLogger(ReSTExecutionHelper.class);

	private static final String INTEGRATION_TEST_SERVER_URL = "integration.test.server.url";
	private static final String ESPM_SERVICE_BASE_URI = "/espm-cloud-web/espm.svc/";
	private static final String SERVICE_ROOT_URI = System
			.getProperty(INTEGRATION_TEST_SERVER_URL) + ESPM_SERVICE_BASE_URI;
	private static final String user = "ret";
	private static final String password = "123";
	private static final String AUTHORIZATION = "Basic "
			+ new String(Base64.encode((user + ":" + password).getBytes()));

	public static CloseableHttpResponse executePostRequest(String entityName,String payLoad, boolean secure) throws Exception {

		HttpClientContext context = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		context.setCookieStore(cookieStore);

		HttpGet get = new HttpGet(SERVICE_ROOT_URI+"secure/");
		get.addHeader("Authorization",AUTHORIZATION);
		get.addHeader("X-CSRF-TOKEN","Fetch");

		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = httpclient.execute(get, context);
		Header[] header = response.getAllHeaders();
		Map<String,String>heads = new HashMap<String,String>();
		for(Header head:header){
			heads.put(head.getName(), head.getValue());
		}		
		HttpEntity entity = new ByteArrayEntity(payLoad.getBytes("UTF-8"));
		HttpPost post = new HttpPost(SERVICE_ROOT_URI+"secure/"+entityName);
		post.setHeader("Content-Type","application/xml");
		post.setHeader("Accept","application/json");
		post.setEntity(entity);
		RequestAddCookies addCookies = new RequestAddCookies();
		addCookies.process(post, context);
		post.setHeader("X-CSRF-Token",heads.get("X-CSRF-Token"));

		response = httpclient.execute(post, context);

		return response;
	}

	public static CloseableHttpResponse executePutRequest(String serviceEndPoint,String payLoad, boolean secure) throws Exception {

		HttpClientContext context = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		context.setCookieStore(cookieStore);

		HttpGet get = new HttpGet(SERVICE_ROOT_URI+"secure/");
		get.addHeader("Authorization",AUTHORIZATION);
		get.addHeader("X-CSRF-TOKEN","Fetch");
		String url = (secure) ? SERVICE_ROOT_URI+"secure/"+ serviceEndPoint : SERVICE_ROOT_URI +serviceEndPoint;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = httpclient.execute(get, context);
		Header[] header = response.getAllHeaders();
		Map<String,String>heads = new HashMap<String,String>();
		for(Header head:header){
			heads.put(head.getName(), head.getValue());
		}		


		HttpPut put = new HttpPut(url);

		put.setHeader("Content-Type","application/xml");
		put.setHeader("Accept","application/json");
		HttpEntity entity = new ByteArrayEntity(payLoad.getBytes("UTF-8"));
		put.setEntity(entity);
		RequestAddCookies addCookies = new RequestAddCookies();
		addCookies.process(put, context);
		put.setHeader("X-CSRF-Token",heads.get("X-CSRF-Token"));

		response = httpclient.execute(put, context);

		return response;
	}

	public static CloseableHttpResponse executeDeleteRequest(String serviceEndPoint, boolean secure) throws Exception {

		HttpClientContext context = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		context.setCookieStore(cookieStore);

		HttpGet get = new HttpGet(SERVICE_ROOT_URI+"secure/");
		get.addHeader("Authorization",AUTHORIZATION);
		get.addHeader("X-CSRF-TOKEN","Fetch");
		String url = (secure) ? SERVICE_ROOT_URI+"secure/"+ serviceEndPoint : SERVICE_ROOT_URI +serviceEndPoint;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = httpclient.execute(get, context);
		Header[] header = response.getAllHeaders();
		Map<String,String>heads = new HashMap<String,String>();
		for(Header head:header){
			heads.put(head.getName(), head.getValue());
		}		

		HttpDelete delete = new HttpDelete(url);


		delete.setHeader("Accept","application/json");
		RequestAddCookies addCookies = new RequestAddCookies();
		addCookies.process(delete, context);
		delete.setHeader("X-CSRF-Token",heads.get("X-CSRF-Token"));

		response = httpclient.execute(delete, context);

		return response;
	}

	public static CloseableHttpResponse executeGetRequest(String serviceEndPoint,boolean secure) throws IOException {
		HttpClientContext context = HttpClientContext.create();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CookieStore cookieStore = new BasicCookieStore();
		context.setCookieStore(cookieStore);
		String url = (secure) ? SERVICE_ROOT_URI+"secure/"+ serviceEndPoint : SERVICE_ROOT_URI +serviceEndPoint;

		HttpGet get = new HttpGet(url);
		if(secure){
			get.addHeader("Authorization",AUTHORIZATION);
		}
		get.setHeader("Accept","application/json");
		CloseableHttpResponse response = httpclient.execute(get, context);
		return response;
	}

	public static String readResponse(CloseableHttpResponse response) 
			throws Exception {
		BufferedReader reader = null;
		String content = "";
		String line = null;
		HttpEntity entity = response.getEntity();

		reader = new BufferedReader(new InputStreamReader(entity.getContent())); 
		while ((line = reader.readLine()) != null) {
			content += line;
		}

		EntityUtils.consume(entity);
		return content;
	}


}
