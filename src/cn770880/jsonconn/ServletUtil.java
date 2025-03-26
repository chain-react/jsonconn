package cn770880.jsonconn;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;

import cn770880.jutil.j4log.Logger;
import cn770880.jutil.string.StringUtil;

public class ServletUtil {
	private static Logger debug = Logger.getLogger("restdebug");
	
	private static final int MAX_REQUEST_CONTENT_LEN = 5*1024*1024;//最多post 5M的东西上来
	
	public static void printHeaders(HttpServletRequest httpReq){
		Enumeration<String> em = httpReq.getHeaderNames();
		while (em.hasMoreElements()) {
			String key = em.nextElement();
			debug.debug(key + ": " + httpReq.getHeader(key));
		}
		debug.debug("");
	}
	
	private static byte[] readBody(HttpServletRequest httpReq) throws IOException {
		int len = httpReq.getContentLength();
		if( len<0 || len > MAX_REQUEST_CONTENT_LEN )
			throw new SvrException(SvrException.CONTENT_LEN_ERROR, "Content-Length err:"+len);
		byte[] bs = new byte[ len ];
		readAll( httpReq.getInputStream(), bs, 0, bs.length );
		return bs;
	}
	
	private static void readAll(InputStream is,byte[] bs,int start,int end) throws IOException {
		while(start<end){
			int bytesRead = is.read(bs,start,end-start);
			if (bytesRead < 0)
				throw new IOException("close by peer.");
			start += bytesRead;
		}
	}
	
	//init RspBean
//	public static RspBean createRspBean(){
//		RspBean rspBean = new RspBean();
//		RspHeadBean rspHead = new RspHeadBean();
//		rspHead.setSvrcode( 0 );
//		rspHead.setErrmsg( "" );
//		rspHead.setCmd( "nocmd" );
//		rspBean.setRsphead( rspHead );
//		Map<String,Object> returnBody = new HashMap<String, Object>();
//		rspBean.setBody( returnBody );
//		return rspBean;
//	}
	
	//post -> json -> ReqBean
	public static RequestBean getReportReqBean( HttpServletRequest req ) throws IOException{
		//从post请求中获取数据
		byte[] bs = readBody(req);
		String jsonStr = new String( bs, StringUtil.UTF_8 );
		//打印请求体
		debug.debug( jsonStr );
		//json->bean
		RequestBean reqBean = null;
		try {
			reqBean = JSON.parseObject(jsonStr,RequestBean.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SvrException(SvrException.JSON_FMT_ERROR, "协议格式错误 ! ");
		}
		return reqBean;
	}
	
	public static String getRemoteIP(ServletRequest request)
    {
		HttpServletRequest r = (HttpServletRequest) request;
		String ip = r.getHeader("X-Real-IP");
		if(ip == null || ip.length() == 0)
        {
			ip = r.getRemoteAddr();
        }
		return ip;
	}
}
