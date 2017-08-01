package com.pigthinkingtec.framework.log;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import com.pigthinkingtec.framework.databean.Parameter;
import com.pigthinkingtec.framework.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ログを階層構造にして出力する
 * 
 * @author yizhou
 * 
 * 
 */
@SuppressWarnings("rawtypes")
public class LogParamOutUtil {

	private Log log;
	
	private String debugLevel = null; 
	
	private final String DEBUG_LEVEL_DEBUG 	= "debug";
	private final String DEBUG_LEVEL_INFO 	= "info";
	private final String DEBUG_LEVEL_WARN 	= "warn";
	private final String DEBUG_LEVEL_ERROR 	= "error";
	private final String DEBUG_LEVEL_FATAL 	= "fatal";
	private final String DEBUG_LEVEL_TRACE 	= "trace";
	
	public LogParamOutUtil()
	{
		log = LogFactory.getLog(getClass().getName());
	}
	
	public LogParamOutUtil(String debugLevel)
	{
		this.log = LogFactory.getLog(getClass().getName());
		this.debugLevel = debugLevel;
	}
	
	/**
	 * 
	 * main 処理
	 * 
	 * @param obj
	 * @param strTitle
	 * @throws Exception
	 */
	public void invoke(Object obj, String strTitle){
		
		
		try{
			
			if(obj != null){
				putDataBean(obj, strTitle);
				
			}else{
				this.getLog(strTitle + "null");
				 
			}
			
		} catch (Exception e){
			//エラー発生によりスルー
		}
	}
	
	private void putDataBean(Object obj, String strTitle) throws Exception{
		
		Class clazz = obj.getClass();
		
		String methodName = clazz.getName().toString();
		
		Method method;
		
		this.getLog(strTitle + methodName);
		
		Method[] methods = clazz.getMethods();
		
		strTitle = strTitle.replaceAll("━", "　");
		strTitle = strTitle.replaceAll("┗", "　");
		
		strTitle = strTitle + "┃━";
		
		//getメソッドの個数を算出
		int getCount=0;
		for(int i=0; i < methods.length; i++){
			if(methods[i].getName().indexOf("get",0) == 0
					&& !"getClass".equals(methods[i].getName())){
				getCount++;
			}
		}
		
		for (int i=0,h=0; i < methods.length; i++){
			
			method = methods[i];
			
			if(method.getName().indexOf("get",0) == 0
					&& !"getClass".equals(methods[i].getName())){
				
				if(h == getCount - 1){
					strTitle = strTitle.replaceAll("┃━", "┗━");
				}
				
				if (method.getReturnType().getName().equals(String.class.getName())){ 
					putLogString(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(boolean.class.getName())){
					putLogBoolean(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(char.class.getName())){
					putLogChar(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(byte.class.getName())){
					putLogByte(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(short.class.getName())){
					putLogShort(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(int.class.getName())){
					putLogInt(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(long.class.getName())){
					putLogLong(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(float.class.getName())){
					putLogFloat(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(double.class.getName())){
					putLogDouble(obj, method, strTitle);

					
					
				}else if(method.getReturnType().getName().equals(Boolean.class.getName())){
					putLogBooleanWrap(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(Character.class.getName())){
					putLogCharWrap(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(Byte.class.getName())){
					putLogByteWrap(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(Short.class.getName())){
					putLogShortWrap(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(Integer.class.getName())){
					putLogIntWrap(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(Long.class.getName())){
					putLogLongWrap(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(Float.class.getName())){
					putLogFloatWrap(obj, method, strTitle);
					
				}else if(method.getReturnType().getName().equals(Double.class.getName())){
					putLogDoubleWrap(obj, method, strTitle);
					
					
				
				}else if(method.getParameterTypes().length == 0 
						&&	(method.getReturnType().getInterfaces().length > 0 
								&&	Collection.class.getName().toString().equals(method.getReturnType().getInterfaces()[0].getName().toString()))){
//					putList(obj, method, strTitle);
					
				}else if(method.getParameterTypes().length == 0){
					if(method.invoke(obj, (Object[])null) != null){
						
						this.getLog(strTitle + method.getName().replaceAll("get",""));
						
//						putDataBean(method.invoke(obj, null), strTitle.replaceAll("━", "　").replaceAll("┗", "　") + "┗━");
						
					}else{
						this.getLog(strTitle + method.getName().replaceAll("get","") + "=" + "null");
						
					}
				}else {
					this.getLog(strTitle + method.getName().replaceAll("get","") + "=" + "?");
					
				}
		
				h++;				
			}
		}
	}
	
	private void putLogString(Object obj, Method method, String strTitle) throws Exception{
		
		String methodName = method.getName();
		
		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(String.class.getName())){ //getで始まるメソッド
			String tmpValue = (String)method.invoke(obj, (Object[])null);
			
			if (tmpValue != null){
				tmpValue = StringUtil.rtrim(tmpValue);
				
			}
				
			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogBoolean(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(boolean.class.getName())){ //getで始まるメソッド
			String tmpValue = ((Boolean)method.invoke(obj, (Object[])null)).toString();

			if (tmpValue != null){
				tmpValue = StringUtil.rtrim(tmpValue.toString());
			}
			
			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogChar(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(char.class.getName())){ //getで始まるメソッド
			String tmpValue = null;
			
			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogByte(Object obj, Method method, String strTitle) throws Exception {
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(byte.class.getName())){ //getで始まるメソッド
			String tmpValue = null;
			
			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
		
	
	private void putLogShort(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(short.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogInt(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(int.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogLong(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(long.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogFloat(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(float.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogDouble(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(double.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogBooleanWrap(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(Boolean.class.getName())){ //getで始まるメソッド
			String tmpValue = null;
				
			if((method.invoke(obj, (Object[])null)) != null){
				tmpValue =StringUtil.rtrim(((Boolean)method.invoke(obj, (Object[])null)).toString()); 
			}

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogCharWrap(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(Character.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			if((method.invoke(obj, (Object[])null)) != null){
				tmpValue =StringUtil.rtrim(((Character)method.invoke(obj, (Object[])null)).toString()); 
			}
			
			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogByteWrap(Object obj, Method method, String strTitle) throws Exception {
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(Byte.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			if((method.invoke(obj, (Object[])null)) != null){
				tmpValue =StringUtil.rtrim(((Byte)method.invoke(obj, (Object[])null)).toString()); 
			}
			
			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
		
	
	private void putLogShortWrap(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(Short.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			if((method.invoke(obj, (Object[])null)) != null){
				tmpValue =StringUtil.rtrim(((Short)method.invoke(obj, (Object[])null)).toString()); 
			}

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogIntWrap(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(Integer.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			if((method.invoke(obj, (Object[])null)) != null){
				tmpValue =StringUtil.rtrim(((Integer)method.invoke(obj, (Object[])null)).toString()); 
			}

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogLongWrap(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(Long.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			if((method.invoke(obj, (Object[])null)) != null){
				tmpValue =StringUtil.rtrim(((Long)method.invoke(obj, (Object[])null)).toString()); 
			}

			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogFloatWrap(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(Float.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			if((method.invoke(obj, (Object[])null)) != null){
				tmpValue =StringUtil.rtrim(((Float)method.invoke(obj, (Object[])null)).toString()); 
			}
			
			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putLogDoubleWrap(Object obj, Method method, String strTitle)  throws Exception{
		
		String methodName = method.getName();

		if (methodName.indexOf("get",0)==0 && method.getReturnType().getName().equals(Double.class.getName())){ //getで始まるメソッド
			String tmpValue = null;

			if((method.invoke(obj, (Object[])null)) != null){
				tmpValue =StringUtil.rtrim(((Double)method.invoke(obj, (Object[])null)).toString()); 
			}
			
			this.getLog(strTitle + methodName.replaceAll("get","") + "=" + tmpValue);
		}
	}
	
	private void putList(List list, String strTitle)  throws Exception{
		
		this.getLog(strTitle + list.getClass().getName().toString());
		
		if(list.size() > 0){
			putListDetail(list, strTitle, list.get(0).getClass().getName().toString());
			
		} else {
			strTitle = strTitle.replaceAll("━", "　");
			strTitle = strTitle.replaceAll("┗", "　");
			
			strTitle = strTitle + "┗━";
			
			this.getLog(strTitle + "0件");
		}

	}
	
	private void putListDetail(List list, String strTitle, String className) throws Exception{
		
		
		strTitle = strTitle.replaceAll("━", "　");
		strTitle = strTitle.replaceAll("┗", "　");
		
		strTitle = strTitle + "┃━";
		
		if(list.get(0).getClass().getInterfaces().length > 0 ){
			if(Parameter.class.getName().toString().equals(list.get(0).getClass().getInterfaces()[0].getName().toString())){
				for (int i=0; i < list.size(); i++){
					if(i == (list.size() - 1)){
						strTitle = strTitle.replaceAll("┃━", "┗━");
					}
					
					putDataBean(list.get(i), strTitle);
				}		
			
			}else if(Collection.class.getName().toString().equals(list.get(0).getClass().getInterfaces()[0].getName().toString())){
				for (int i=0; i < list.size(); i++){
					if(i == (list.size() - 1)){
						strTitle = strTitle.replaceAll("┃━", "┗━");
					}
					
					putList((List)list.get(i), strTitle);
				}			
			}else if(String.class.getName().toString().equals(list.get(0).getClass().getName().toString())){
				for (int i=0; i < list.size(); i++){
					if(i == (list.size() - 1)){
						strTitle = strTitle.replaceAll("┃━", "┗━");
					}
					this.getLog(strTitle + (String)list.get(i));
				}	
			}
		}else{
			if(list.get(0).getClass().toString().indexOf("Bean",0) != 0){ 
				for (int i=0; i < list.size(); i++){
					if(i == (list.size() - 1)){
						strTitle = strTitle.replaceAll("┃━", "┗━");
					}
					
					putDataBean(list.get(i), strTitle);
				}
			}else{
				for (int i=0; i < list.size(); i++){
					if(i == (list.size() - 1)){
						strTitle = strTitle.replaceAll("┃━", "┗━");
					}
					
					this.getLog(strTitle + (String)list.get(i));
	
				}
			}
		}
	}
	
	private void getLog(String str){
		log = LogFactory.getLog(getClass().getName());
		if(StringUtil.isBlank(debugLevel)
				||	DEBUG_LEVEL_DEBUG.equals(debugLevel)){
			log.debug(str);
		}else if(DEBUG_LEVEL_WARN.equals(debugLevel)){
			log.warn(str);
		}else if(DEBUG_LEVEL_INFO.equals(debugLevel)){
			log.info(str);
		}else if(DEBUG_LEVEL_ERROR.equals(debugLevel)){
			log.error(str);
		}else if(DEBUG_LEVEL_FATAL.equals(debugLevel)){
			log.fatal(str);
		}else if(DEBUG_LEVEL_TRACE.equals(debugLevel)){
			log.trace(str);
		}else{
			log.debug(str);
		}
	}
}
