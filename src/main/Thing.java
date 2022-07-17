package main;

import java.lang.reflect.Field;

import eutil.datatypes.BoxList;
import eutil.reflection.EModifier;
import sun.misc.Unsafe;

public class Thing {
	
	public static void start() throws Exception {
		divByZero();
		
//		Unsafe unsafe = null;
//		
//		Field f = Unsafe.class.getDeclaredField("theUnsafe");
//		f.setAccessible(true);
//		unsafe = (Unsafe) f.get(null);
//		
//		Object o = unsafe.allocateInstance(Banana.class);
//		
//		Class<?> clazz = o.getClass();
//		System.out.println(o);
//		
//		BoxList<String, Long> addresses = new BoxList();
//		
//		for (Field field : clazz.getDeclaredFields()) {
//			if (!new EModifier(field).isStatic())
//			addresses.add(field.getName(), unsafe.objectFieldOffset(field));
//		}
//		
//		addresses = addresses.sort((a, b) -> a.getB().compareTo(b.getB()));
//		
//		//unsafe.putInt(o, aAddress, 2);
//		
////		String line = "";
////		for (var l : addresses) {
////			line += l + " : ";
////		}
////		line = line.substring(0, line.length() - 3);
//		
//		System.out.println(addresses);
		
	}
	
	static void divByZero() {
		double x = 10;
		int y = 0;
		
		try {
			double z = x / y;
			System.out.println(z);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static class Banana {
		long x;
		long y;
		
		public Banana() {}
		
		@Override
		public String toString() {
			return "";
		}
	}

}
