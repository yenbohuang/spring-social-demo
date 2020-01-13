package org.yenbo.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestUserItem {

	@Expose
	private String id;
	
	@Expose
	@SerializedName("login_url")
	private String loginUrl;
	
	@Expose
	@SerializedName("access_token")
	private String accessToken;
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLoginUrl() {
		return loginUrl;
	}
	
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
}
