package util.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CanOpener {

	public Object invokeHiddenMethod(Object targetObject, String targetMethod, Object... args) 
	throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> targetClass = targetObject.getClass();
		while (targetClass != null) {
			Method[] methods = targetClass.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(targetMethod)) {
					method.setAccessible(true);
					return method.invoke(targetObject, args);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		throw new IllegalArgumentException("Method \"" + targetMethod + 
				"\" does not exist on object \"" + targetObject.toString() + "\"");
	}
	
	public Object getHiddenField(Object targetObject, String targetField)
	throws IllegalArgumentException, IllegalAccessException {
		Class<?> targetClass = targetObject.getClass();
		while (targetClass != null) {
			Field[] fields = targetClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals(targetField)) {
					field.setAccessible(true);
					return field.get(targetObject);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		throw new IllegalArgumentException("Field \"" + targetField + 
				"\" does not exist on object \"" + targetObject.toString() + "\"");
	}
}
