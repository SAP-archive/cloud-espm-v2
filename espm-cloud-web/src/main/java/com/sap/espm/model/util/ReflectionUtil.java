/**
 * 
 */
package com.sap.espm.model.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utility class for Generating sales order headers and data
 *
 */
public final class ReflectionUtil {

	private static final String DELIMITER = "|";
	
	private static SimpleDateFormat formatter=new SimpleDateFormat("yyyy MM dd");

	public static <T> String generateEntityHeaders(Class clazz, Class annotationClass) {
		String headers = "|";
		if (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				for (Field field : fields) {
					Annotation annotation = field.getAnnotation(annotationClass);
					if (annotation != null) {
						headers += field.getName() + DELIMITER;
					}
				}
			}
		}
		return headers;
	}

	public static String generateEntityData(Object object, Class annotationClass)
			throws IllegalArgumentException, IllegalAccessException {
		String data = "|";
		if (object != null) {
			Field[] fields = object.getClass().getDeclaredFields();
			if (fields != null) {
				for (Field field : fields) {
					Annotation annotation = field.getAnnotation(annotationClass);
					if (annotation != null) {
						field.setAccessible(true);
						
						Object currentField = field.get(object);
						if(currentField!=null && currentField instanceof GregorianCalendar){
							//convert to string date.
							GregorianCalendar date = (GregorianCalendar) currentField;
							
							String dob = formatter.format(date.getTime());
							data += (dob != null) ? dob + DELIMITER
									: fixedLengthString("", field.getName().length()) + DELIMITER;
						}
						else{
							data += (field.get(object) != null) ? field.get(object) + DELIMITER
									: fixedLengthString("", field.getName().length()) + DELIMITER;
						}

						
					}
				}
			}
		}
		return data;
	}
	
	public static Map<String, String> generateReportData(Class clazz,
			Class annotationClass, Object object)
			throws IllegalArgumentException, IllegalAccessException {
		Map<String, String> reportData = new HashMap<>();
		if (clazz != null && object != null) {
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				for (Field field : fields) {
					Annotation annotation = field
							.getAnnotation(annotationClass);
					String name = field.getName();
					String data = "";
					if (annotation != null) {
						field.setAccessible(true);
						// headers += field.getName() + DELIMITER;
						Object currentField = field.get(object);
						if (currentField != null
								&& currentField instanceof GregorianCalendar) {
							// convert to string date.
							GregorianCalendar date = (GregorianCalendar) currentField;

							String dob = formatter.format(date.getTime());
							data += (dob != null) ? dob + "" : "";
						} else {
							data += (field.get(object) != null) ? field
									.get(object) + "" : "";
						}
						reportData.put(name, data);
					}
				}
			}
		}
		return reportData;
	}
	
	public static void processReportMap(Map<String,String> reportMap, StringBuffer reportBuffer){
		if(reportMap!=null && reportBuffer!=null){
			for(Entry<String,String> entry : reportMap.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				reportBuffer.append(key + " : " + value);
				reportBuffer.append("|");
			}
		}
	}


	public static String fixedLengthString(String string, int length) {
		return String.format("%1$" + length + "s", string);
	}
	
	public static String padString(String str, int leng) {
        for (int i = str.length(); i <= leng; i++)
            str += " ";
        return str;
    }

}
