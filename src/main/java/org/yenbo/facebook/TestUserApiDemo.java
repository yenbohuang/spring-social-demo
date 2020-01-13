package org.yenbo.facebook;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestUserApiDemo {

	private static final Logger log = LoggerFactory.getLogger(TestUserApiDemo.class);
	
	private TestUserApiDemo() {
	}
	
	public static void main(String[] args) {
		
		String appToken = System.getProperty("appToken");
		String userId = System.getProperty("userId");
		String appId = System.getProperty("appId");
		log.info("appToken={}, userId={}, appId={}", appToken, userId, appId);
		
		try {
			printUserInfo(getAccessToken(appToken, appId, userId));
		} catch (IOException | URISyntaxException ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	public static String getAccessToken(String appToken, String appId, String userId) throws IOException, URISyntaxException {
		
		String accessToken = null;
		String body = null;
		
		String uri = new URIBuilder(String.format("https://graph.facebook.com/v2.8/%s/accounts/test-users", appId))
				.addParameter("fields", "access_token")
				.addParameter("access_token", appToken)
				.build().toString();
		log.info("uri={}", uri);
		
		CloseableHttpClient httpClient = null;
		
		try {
			httpClient = HttpClients.createDefault();
			
			HttpGet httpGet = new HttpGet(uri);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			body = EntityUtils.toString(httpResponse.getEntity());
			log.info("body={}", body);
			
		} finally {
			
			if (null != httpClient) {
				httpClient.close();
			}
		}
		
		Gson gson = new GsonBuilder().create();
		TestUserResponse testUserResponse = gson.fromJson(body, TestUserResponse.class);
		
		for (TestUserItem testUserItem: testUserResponse.getData()) {
			
			if (userId.equals(testUserItem.getId())) {
				accessToken = testUserItem.getAccessToken();
				break;
			}
		}
		
		return accessToken;
	}
	
	public static void printUserInfo(String accessToken) {
		
		Facebook facebook = new FacebookTemplate(accessToken);
		String[] fields = {"id", "email", "first_name", "last_name", "locale", "location"};
		User user = facebook.fetchObject("me", User.class, fields);
		log.info("id={}, email={}, first_name={}, last_name={}, locale={}",
				user.getId(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getLocale());
		
		if (null != user.getLocation()) {
		
			log.info("location.extraData={}, location.id={}, location.name={}",
					user.getLocation().getExtraData(),
					user.getLocation().getId(),
					user.getLocation().getName());
		} else {
			log.info("location=null");;
		}
	}
}
