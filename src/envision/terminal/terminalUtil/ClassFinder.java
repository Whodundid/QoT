package envision.terminal.terminalUtil;

import eutil.datatypes.EArrayList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassFinder {
	
	public static <E> EArrayList<Class<E>> findClassesOfType(String packageName, Class<E> type) {
		if (type != null) {
			Class[] classes = getClasses(packageName);
			EArrayList<Class<E>> casted = new EArrayList();
			
			for (Class<?> c : classes) {
				if (type.isAssignableFrom(c)) {
					casted.add((Class<E>) c);
				}
			}
			
			return casted;
		}
		return null;
	}
	
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     * @author Victor Tatai -- https://dzone.com/users/74061/vtatai.html
     */
	public static Class[] getClasses(String packageName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		try {
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			
			ArrayList<Class> classes = new ArrayList<Class>();
			
			for (File directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}
			
			return classes.toArray(new Class[classes.size()]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     * @author Victor Tatai -- https://dzone.com/users/74061/vtatai.html
     */
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		
		if (!directory.exists()) { return classes; }
		
		File[] files = directory.listFiles();
		
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			}
			else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		
		return classes;
	}
	
}
