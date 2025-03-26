package cn770880.jsonconn;

public class RequestHeadBean {
	private int ostype;
	/**
	 * 小程序类型  101 画个头  102 奇迹暖暖
	 */
	private int littleostype;
	private String appver;
	private String oslevel;
	private String devicename;
	private String imei;
	private String cpuserial;
	private String mac;
	private String guid;
	private String imsi;
	private String nettype;
	private int width;
	private int height;
	
	public int getOstype() {
		return ostype;
	}
	public void setOstype(int ostype) {
		this.ostype = ostype;
	}
	public int getLittleostype() {
		return littleostype;
	}
	public void setLittleostype(int littleostype) {
		this.littleostype = littleostype;
	}
	public String getAppver() {
		return appver;
	}
	public void setAppver(String appver) {
		this.appver = appver;
	}
	public String getOslevel() {
		return oslevel;
	}
	public void setOslevel(String oslevel) {
		this.oslevel = oslevel;
	}
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getCpuserial() {
		return cpuserial;
	}
	public void setCpuserial(String cpuserial) {
		this.cpuserial = cpuserial;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getNettype() {
		return nettype;
	}
	public void setNettype(String nettype) {
		this.nettype = nettype;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
