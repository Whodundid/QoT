package util.miscUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import util.EUtil;

public class ReflectionHelper {
	
	/** Attempts to set the value of a field of the given name from the given object's class hierarchy. */
	public static void setField(Object obj, String fieldName, Object value) throws Exception {
		Field f = findField(obj.getClass(), fieldName);
		f.setAccessible(true);
		f.set(obj, value);
		f.setAccessible(false);
	}
	
	/** Attempts to return a value of a field of the given name from the given object's class hierarchy. */
	public static <E> E getValue(Object obj, String fieldName, Class<E> asClass) throws Exception {
		Field f = findField(obj.getClass(), fieldName);
		f.setAccessible(true);
		Object o = f.get(obj);
		f.setAccessible(false);
		return (E) o;
	}
	
	/** Attempts to return a corresponding constructor from the given name and class parameters from the given object's class hierarchy. */
	public static Constructor getConstructor(Object obj, Class... parameters) throws Exception {
		Constructor c = findConstructor(obj.getClass(), parameters);
		return c;
	}
	
	/** Attempts to set the value of a field of the given name from the object's immediate fields. */
	public static void setDeclaredField(Object obj, String fieldName, Object value) throws Exception {
		Field f = obj.getClass().getDeclaredField(fieldName);
		f.setAccessible(true);
		f.set(obj, value);
		f.setAccessible(false);
	}
	
	/** Attempts to return the value of a field of the given name from the object's immediate fields. 
	 *  If no matching field was found, null is returned instead. */
	public static <E> E getDeclaredValue(Object obj, String fieldName, Class<E> asClass) throws Exception {
		Field f = obj.getClass().getDeclaredField(fieldName);
		f.setAccessible(true);
		Object o = f.get(obj);
		f.setAccessible(false);
		return (E) o;
	}
	
	/** Attempts to return a corresponding constructor from the given name and class parameters from the given object's immediate constructors. */
	public static Constructor getDeclaredConstructor(Object obj, String constructorName, Class... parameters) throws Exception {
		Constructor c = obj.getClass().getDeclaredConstructor(parameters);
		return c;
	}
	
	/** Returns true if the given object actually has a field of the given name in its class hierarchy. */
	public static boolean doesFieldExist(Object obj, String fieldName) {
		try {
			Field f = findField(obj.getClass(), fieldName);
			return f != null;
		}
		catch (NoSuchFieldException e) {}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/** Returns true if the given object actually has a constructor of the given name with the given parameters in its class hierarchy. */
	public static boolean doesConstructorExist(Object obj, Class... parameters) {
		try {
			Constructor c = findConstructor(obj.getClass(), parameters);
			return c != null;
		}
		catch (NullPointerException e) {}
		catch (NoSuchMethodException e) {}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//-------------------
	// Protected Methods
	//-------------------
	
	/** Attempts to find a field of the given name by recursively checking the declared fields of each super class of its object hierarchy. */
	protected static Field findField(Class c, String fieldName) throws Exception {
		Field f = null;
		
		while (c != null && f == null) {
			try {
				f = c.getDeclaredField(fieldName);
			}
			//Ignore this exception as this could be inherently true for multiple class layers
			catch (NoSuchFieldException e) {}
			//In the event of a much more serious error, propagate the error upwards
			catch (Exception e) { throw e; }
			c = c.getSuperclass();
		}
		
		//If the field does not actually exist anywhere, throw a NoSuchFieldException
		if (f == null) { throw new NoSuchFieldException(fieldName); }
		
		//Otherwise return the field
		return f;
	}
	
	
	protected static Constructor findConstructor(Class c, Class... parameters) throws Exception {
		if (c == null) { throw new NullPointerException("The original class is null!"); }
		Class original = c;
		Constructor f = null;
		
		while (c != null && f == null) {
			try {
				f = c.getDeclaredConstructor(parameters);
			}
			catch (NoSuchMethodException e) {}
			catch (Exception e) { throw e; }
			c = c.getSuperclass();
		}
		
		if (f == null) {
			if (c != null) { throw new NoSuchMethodException(c.getSimpleName()); }
			else { throw new NullPointerException("The class '" + original + "' does not have a constructor with the given parameters: [" + EUtil.toList(parameters) + "]!"); }
		}
		
		return f;
	}
	
}
