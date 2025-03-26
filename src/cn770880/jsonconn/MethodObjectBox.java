package cn770880.jsonconn;

import java.lang.reflect.Method;
import java.util.HashMap;

import cn770880.jutil.string.StringUtil;

/**
 * 管理所有对外公布的方法，和对象
 * @author junehuang
 *
 */
public class MethodObjectBox {
	private static HashMap<String,Method> _methodMap = new HashMap<String,Method>();
	private static HashMap<String,Object> _objectMap = new HashMap<String,Object>();
	
	public static Method getMethod( String moduleName, String methodName ){
		String key = moduleName+"."+methodName;
		return _methodMap.get( key );
	}
	
	public static Object getObject( String moduleName ){
		return _objectMap.get( moduleName );
	}
	
	public static void init(String[][]  _classNameArray){
		try {
			//遍历mobi.wandan.api内所有的接口
			for (int i = 0; i < _classNameArray.length; i++) {
				//初始化_methodMap
				String moduleName = _classNameArray[i][0];
				Class apiClass = Class.forName(_classNameArray[i][0]);
				Method []methodList = apiClass.getMethods();
				for (int j = 0; j < methodList.length; j++) {
					Method method = methodList[j];

					String key = moduleName+"."+method.getName();
					if (_methodMap.get( key ) == null)
						_methodMap.put( key, methodList[j] );
				}
				//初始化_serviceMap
				Object service = Class.forName(_classNameArray[i][1]).newInstance();
				if (_objectMap.get( moduleName ) == null)
					_objectMap.put( moduleName, service );
			}
//			System.out.println( "===============_methodMap==============" );
//			System.out.println( _methodMap );
//			System.out.println( "===============_objectMap==============" );
//			System.out.println( _objectMap );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getSimpleModuleName( String className ){
		String[] arr = StringUtil.split(className, ".");
		return arr[arr.length-1];
	}
}
