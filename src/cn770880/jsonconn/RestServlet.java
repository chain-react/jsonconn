package cn770880.jsonconn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn770880.jutil.data.RespData;
import cn770880.jutil.j4log.Logger;
import cn770880.jutil.string.StringUtil;
import cn770880.jutil.thread.ThreadLocalBox;

public class RestServlet extends HttpServlet{
	private static final long serialVersionUID = -2779918204907740817L;

	private static Logger debug = Logger.getLogger("restdebug");
	private static Logger flowLog = Logger.getLogger("rest");
	
	private TokenParser parser;
	private String[][] _classNameArray;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		//初始化_classNameArray
		try {
			String str = config.getInitParameter("ApiDef");
			str = StringUtil.removeAll(str, "\t");
			str = StringUtil.removeAll(str, "\r");
			str = StringUtil.removeAll(str, "\n");
			str = StringUtil.removeAll(str, " ");
			String[] arr = StringUtil.split(str, ";");
			_classNameArray = new String[arr.length][2];
			for (int i = 0; i < arr.length; i++) {
				String[] arr2 = StringUtil.split(arr[i], "#");
				_classNameArray[i][0] = arr2[0];
				_classNameArray[i][1] = arr2[1];
			}
			MethodObjectBox.init(_classNameArray);
		} catch (Exception e) {
			throw new ServletException("init TokenParser err !" + e.getMessage());
		}
		
		//初始化TokenParser
		try {
			String str = config.getInitParameter("TokenParser");
			Class parseClass = Class.forName(str);
			parser = (TokenParser)parseClass.newInstance();
		} catch (Exception e) {
			throw new ServletException("init TokenParser err !" + e.getMessage());
		}
		
		super.init(config);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		String host = req.getServerName();
		int port = req.getServerPort();
		//打印请求头
		ServletUtil.printHeaders( req );
		int len = req.getContentLength();
		String ip = ServletUtil.getRemoteIP( req );

		resp.setContentType("application/json; charset=utf-8");
		ByteArrayOutputStream bo = new ByteArrayOutputStream( 4096 );
		OutputStreamWriter out = new OutputStreamWriter( bo, StringUtil.UTF_8 );
		OutputStream servletOut = resp.getOutputStream();

		//先定义输出的结构体（缺省值是未知错误），后续就只要填充内容了
		RespData<Object> rspBean = new RespData<Object>();

		RequestBean reqBean = null;
		RequestHeadBean reqHeadBean = null;
		String methodName = null;
		String moduleName = null;
		long uid = 0;
		long subuid = 0;
		String edition = null;
		try {
			//获取模块名
			String uri = req.getRequestURI();
			String[] arr = StringUtil.split(uri, "/");
			moduleName = arr[arr.length-2];

			//post -> json -> ReqBean
			reqBean = ServletUtil.getReportReqBean( req );
			reqHeadBean = reqBean.getHead();
			EndInfo endInfo = EndInfo.createEndInfo(reqBean,host,port,ip,parser,req.getHeader("user-agent"));
			uid = endInfo.uid;
			subuid = endInfo.subuid;
			edition = endInfo.edition;
			if( StringUtil.isEmpty(edition) )
				edition = "-";
			ThreadLocalBox.getInstance().put("ENDINFO", endInfo);

			methodName = reqBean.getMethod();
			int id = reqBean.getId();

			rspBean.setId( id );

			//逻辑处理
			Object obj = new MainProcessor(reqBean,moduleName).process();
			rspBean.setObj(obj);
		} catch (InvocationTargetException e) {
			boolean isSvr = false;
			int code = SvrException.INVOKE_ERROR;
			String msg = "";
			Throwable t = e.getCause();
			
			if( t != null ){
				t.printStackTrace();
				Class cls = t.getClass();
				if (SvrException.class == cls) {
					SvrException svrE = (SvrException) t;
					code = svrE.getErrorCode();
					msg = svrE.getMessage();
				}else{
					code = SvrException.INVOKE_ERROR;
					msg = t.getMessage();
				}
			}else{
				e.printStackTrace();
				code = SvrException.INVOKE_ERROR;
				msg = e.getMessage();
			}
			rspBean.setErrCode( code );
			rspBean.setErrMsg( msg );
		} catch (SvrException e) {
			e.printStackTrace();
			rspBean.setErrCode(e.getErrorCode());
			rspBean.setErrMsg( e.getMessage() );
		} catch (Exception e) {
			rspBean.setErrCode(SvrException.UNKOWN_ERROR);
			rspBean.setErrMsg( "系统繁忙" );
			e.printStackTrace();
		} finally {
			ThreadLocalBox.getInstance().remove();
			try {
				// DisableCircularReferenceDetect来禁止循环引用检测
				JSON.writeJSONString( out, rspBean, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteEnumUsingToString );
				out.close();
				byte[] bbb = null;
				debug.debug( new String(bo.toByteArray(), StringUtil.UTF_8) );
				bbb = bo.toByteArray();
				servletOut.write( bbb );
				//打印流水日志
				StringBuilder sb = new StringBuilder();
				sb.append( ip );
				sb.append( "\t" + uid + "\t" + subuid + "\t" + edition );
				sb.append( "\t" + MethodObjectBox.getSimpleModuleName(moduleName) );
				sb.append( "\t" + methodName );
				if( reqHeadBean != null ){
					sb.append( "\t" + StringUtil.convertInt( reqHeadBean.getAppver(), 0 ) );
				}else{
					sb.append( "\t" + "-" );
				}
				sb.append( "\t" + len);
				sb.append( "\t" + rspBean.getErrCode() );
				sb.append( "\t" + (System.currentTimeMillis() - startTime) );
				if( bbb != null )
					sb.append( "\t" + bbb.length );
				else
					sb.append( "\t" + "-" );
				flowLog.info( sb.toString() );
			} catch (Exception e2) {
				e2.printStackTrace();
				flowLog.error( e2.getMessage() );
			}
		}
	}

}
