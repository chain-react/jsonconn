package cn770880.jsonconn;

import com.alibaba.fastjson.JSONArray;

public class RequestBean {
	private String method;
	private String token;
	private String subtoken;
	private String deviceid;
	private String edition;
	private String appid;
	private RequestHeadBean head;
	private JSONArray params;
	private int id;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSubtoken() {
		return subtoken;
	}
	public void setSubtoken(String subtoken) {
		this.subtoken = subtoken;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public RequestHeadBean getHead() {
		return head;
	}
	public void setHead(RequestHeadBean head) {
		this.head = head;
	}
	public JSONArray getParams() {
		return params;
	}
	public void setParams(JSONArray params) {
		this.params = params;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
