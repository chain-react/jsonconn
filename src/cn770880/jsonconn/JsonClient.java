package cn770880.jsonconn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import cn770880.jutil.data.RespData;
import cn770880.jutil.string.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonClient {
	public static Object invoke( String url, String method, String token,String appid,RequestHeadBean head,Object... params){
		Object obj = null;
		InputStream is = null;
		OutputStream out = null;
		HttpURLConnection conn = null;
		try{
			URL myFileUrl = new URL(url);
			conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.connect();

			
			//请求
			RequestBean req = new RequestBean();
			req.setMethod(method);
			req.setToken(token);
			req.setAppid(appid);
			req.setHead(head);
			req.setParams( new JSONArray( Arrays.asList(params) ) );
			out = conn.getOutputStream();
			String s2 = JSON.toJSONString( req );
//			System.out.println( "--------------JsonClient post------------------\n"+s2 );
			out.write( s2.getBytes( StringUtil.UTF_8 ) );
			out.flush();

			//判断status
			int status = conn.getResponseCode();
			if ( status != HttpURLConnection.HTTP_OK ) {
				return null;
			}
			
			//应答
			is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is,"UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				sb.append(s);
			}
			String s = sb.toString();
//			System.out.println( "--------------JsonClient recv------------------\n"+s );
			RespData<Object> resp = JSON.parseObject(s,RespData.class);


			if( resp.getErrCode()==0 ){
				obj = resp.getObj();
			}else{
				throw new SvrException( SvrException.UNKOWN_ERROR, resp.getErrMsg() );
			}
		} catch (Exception e){
			throw new SvrException( SvrException.UNKOWN_ERROR, e.getMessage() );
		}finally{
			if( is != null ){
				try {
					is.close();
				} catch (Exception e2) {
				}
			}
			if( out != null ){
				try {
					out.close();
				} catch (Exception e2) {
				}
			}
			if( conn != null ){
				try {
					conn.disconnect();
				} catch (Exception e2) {
				}
			}
		}
		return obj;
	}
	
	public static void main(String[] args) {
		Object obj = invoke("https://www.zjrvip.cn/api/v1/mobi.wandan.api.OnlineUser/queryPainterList", 
				"queryPainterList",
				"token",
				"appid",
				null,
				1,5);
		System.out.println(obj.getClass());
	}
}
