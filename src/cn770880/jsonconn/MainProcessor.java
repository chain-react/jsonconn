package cn770880.jsonconn;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;

public class MainProcessor {
	private RequestBean reqBean = null;
	private RequestHeadBean reqHeadBean = null;
	private String methodName = null;
	private String moduleName = null;
	private String token = null;
	
	public MainProcessor(RequestBean reqBean,
			String moduleName
			){
		this.reqBean = reqBean;
		this.reqHeadBean = reqBean.getHead();
		this.moduleName = moduleName;
		this.methodName = reqBean.getMethod();
		this.token = reqBean.getToken();
	}
	
	public Object process() throws Exception{
		//反射出对象
		//反射出方法
		Method method = MethodObjectBox.getMethod(moduleName, methodName);
		if (method == null)
			throw new SvrException(SvrException.CMD_ERROR, "没有找到对应方法");
		Class<?> []args = method.getParameterTypes();
		if ( reqBean.getParams().size() != args.length )
			throw new SvrException(SvrException.PARA_COUNT_ERROR, "参数个数错误");
		//方法一：
//		Object []values = JSONArray.toJavaObject(reqBean.getParams(), Object [].class);
		//方法二：
		List<Object> objects = JSONObject.parseArray(reqBean.getParams().toJSONString(), args);
		Object[] values = objects.toArray();
		//调用
		Object service = MethodObjectBox.getObject(moduleName);
		return method.invoke(service, values);
	}
	
//	private static List<Object> parseArray(String text, Type[] types) {
//	    if (text == null) {
//	        return null;
//	    } else {
//	        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());
//	        Object[] objectArray = parser.parseArray(types);
//	        List list;
//	        if (objectArray == null) {
//	            list = null;
//	        } else {
//	            list = Arrays.asList(objectArray);
//	        }
//
//	        parser.handleResovleTask(list);
//	        parser.close();
//	        return list;
//	    }
//	}	
}
