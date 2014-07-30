package eis;


public class Utilities {
	public static Object instantiateClass(String strClassName) {
		try {
			Class<?> classImpl = Class.forName(strClassName);
			return classImpl.newInstance();
		} catch (java.lang.ClassNotFoundException e) {
			System.out.println("Exception, could not find class " + strClassName);
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (java.lang.InstantiationException e) {
			System.out.println("Exception, could not instantiate class " + strClassName);
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Throwable t) {
			System.out.println("Unexpected exception, could not instantiate class " + strClassName);
			t.printStackTrace();
			return null;
		}
	}
}
