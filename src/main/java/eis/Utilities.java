package eis;

public class Utilities {
	public static Object instantiateClass(final String strClassName) {
		try {
			final Class<?> classImpl = Class.forName(strClassName);
			return classImpl.getConstructor().newInstance();
		} catch (final java.lang.ClassNotFoundException e) {
			System.out.println("Exception, could not find class " + strClassName);
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (final java.lang.InstantiationException e) {
			System.out.println("Exception, could not instantiate class " + strClassName);
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (final Throwable t) {
			System.out.println("Unexpected exception, could not instantiate class " + strClassName);
			t.printStackTrace();
			return null;
		}
	}
}
