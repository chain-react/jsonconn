package cn770880.jsonconn;

public class SvrException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5905434796679137544L;
	
	//1001开始往后的错误，客户端统一提示系统繁忙，如果是1012错误，还要引导用户重新登录
	public static final int UNKOWN_ERROR = 1001;//未知错误
	public static final int JSON_FMT_ERROR = 1002;//json 格式错误
	public static final int CMD_ERROR = 1003;//未识别的cmd
	public static final int PARA_RANGE_ERROR = 1004;//参数取值范围错误
	public static final int DECODE_ERROR = 1005;//协议解密错误
	public static final int CONTENT_LEN_ERROR = 1006;//http请求post上来的数据超过了16K
	public static final int PARA_COUNT_ERROR = 1007;//参数个数错误
	public static final int PHP_SERVICE_ERROR = 1008;//PHP接口调用错误
	public static final int CHAT_SERVICE_ERROR = 1009;//私聊接口调用错误
	public static final int INVOKE_ERROR = 1011;//反射产生的InvocationTargetException
	public static final int TOKEN_INVALID_ERROR = 1012;//用户token无效，客户端遇到这个错误，需要重新登录
	
	private int code;
	public SvrException(int code, String msg){
		super(msg);
		this.code = code;
	}
	public int getErrorCode(){
		return code;
	}	
}
