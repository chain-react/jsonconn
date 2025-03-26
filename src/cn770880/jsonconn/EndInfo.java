package cn770880.jsonconn;

import cn770880.jutil.string.StringUtil;

/**
 * 客户端信息，将会存在于ThreadLocal中，随时任意地方取用
 * @author junehuang
 *
 */
public class EndInfo {
	public final long uid;
	public final long subuid;
	public final String token;//主账号
	public final String subtoken;//子账号
	public final String deviceid;//推送id
	public final String edition;//小程序首页版本类型
	public int appver;
	public final int ostype; // 0 unknown 1 、小程序  2、iOS 3、Android 4、h5
	public final int littleOsType;	//小程序类型  101 画个头  102 奇迹暖暖
	public final String appid;
	public final String domain;
	public final String ip;
	public final String ua;
	
	private EndInfo( long uid, long subuid, String token, String subtoken, String deviceid, String edition, int ostype, String appid,String domain, String ip, int littleOsType, int appver, String ua){
		this.uid = uid;
		this.subuid = subuid;
		this.token = token;
		this.subtoken = subtoken;
		this.deviceid = deviceid;
		this.edition = edition;
		this.ostype = ostype;
		this.appid = appid;
		this.domain = domain;
		this.ip = ip;
		this.littleOsType = littleOsType;
		this.appver = appver;
		this.ua = ua;
	}
	
	@Override
	public String toString() {
		return "endinfo: " + uid + "," + subuid + "," + token + "," + subtoken + "," + deviceid + "," + appid + "," + domain + "," + ip;
	}
	
	public static EndInfo createEndInfo( RequestBean reqBean, String host, int port, String ip, TokenParser parser,String ua ){
		RequestHeadBean reqHeadBean = reqBean.getHead();
		String token = reqBean.getToken();
		String subtoken = reqBean.getSubtoken();
		String deviceid = reqBean.getDeviceid();
		String edition = reqBean.getEdition();
		int ostype = reqHeadBean==null ? 0 : reqHeadBean.getOstype();
		int littleOsType = reqHeadBean == null ? 0 : reqHeadBean.getLittleostype();
		int appver = reqHeadBean == null ? 0 : ( StringUtil.convertInt( reqHeadBean.getAppver(), 0 ) );
		String appid = reqBean.getAppid();
		long uid = 0;
		long subuid = 0;
		String domain = host;
		if( port != 80 )
			domain += ":"+port;
		try {
			uid = parser.parseToken(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			subuid = parser.parseToken(subtoken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		EndInfo endInfo = new EndInfo(uid, subuid, token, subtoken, deviceid, edition, ostype, appid, domain, ip, littleOsType, appver,ua );
		return endInfo;
	}
}
