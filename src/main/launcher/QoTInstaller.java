package main.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarFile;

import engine.terminal.terminalUtil.ESystemInfo;
import eutil.sys.OSType;
import main.Main;

public class QoTInstaller {

	public static final String DEFAULT_WINDOWS_INSTALL_DIR = "\\AppData\\Roaming";
	
	//-------------
	// Helper Enum
	//-------------
	
	protected enum InstallerStatus { SUCCESS, FAILED; }

	//--------------
	// Constructors
	//--------------
	
	private QoTInstaller() {}
	
	//----------------
	// Static Methods
	//----------------
	
	/**
	 * Returns the file system path for where QoT would be installed to given the
	 * user's OS.
	 * <p>
	 * Note: This only specifies the directory in which QoT will be installed to,
	 * not the actual 'QoT Installation Directory' itself.
	 * 
	 * @return The root installation path File
	 */
	public static String getDefaultInstallDir() {
		//determine user OS and get their home directory
		OSType os = ESystemInfo.getOS();
		String homeDir = System.getProperty("user.home");
		
		String dir = null;
		
		switch (os) {
		case WINDOWS: dir = homeDir + DEFAULT_WINDOWS_INSTALL_DIR; break;
		case MAC:
		case LINUX:
		case SOLARIS: //no idea how to handle yet
		default:
			//not sure how to setup config for other os's except windows
			return null;
		}
		
		if (dir != null) {
			try {
				dir = URLDecoder.decode(dir, "UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				LauncherLogger.log(LauncherLogLevel.ERROR, e);
			}
		}
		
		return dir;
	}
	
	/**
	 * Builds and returns the default 'QoT Installation Directory' given the type of
	 * operating system the game is being run from.
	 * 
	 * @return The file path to the default QoT installation location
	 */
	public static File getDefaultQoTInstallDir() {
		return new File(QoTInstaller.getDefaultInstallDir() + "\\QoT");
	}
	
	/**
	 * Attempts to create the QoT installation directory on this computer's file
	 * system.
	 * <p>
	 * If successful, InstallerStatus.SUCCESS will be returned. If the directory
	 * already exists, InstallerStatus.EXISTS will be returned. If the directory
	 * could not be created, InstallerStatus.FAILED will be returned.
	 * 
	 * @param customDir The directory to install to
	 * 
	 * @return The resulting status of the creation attempt
	 */
	public static InstallerStatus createInstallDir(File customDir) throws Exception {
		return createInstallDir(customDir, true);
	}
	
	/**
	 * Attempts to create the QoT installation directory on this computer's file
	 * system.
	 * <p>
	 * If successful, InstallerStatus.SUCCESS will be returned. If the directory
	 * already exists, InstallerStatus.EXISTS will be returned. If the directory
	 * could not be created, InstallerStatus.FAILED will be returned.
	 * 
	 * @param customDir The directory to install to
	 * 
	 * @return The resulting status of the creation attempt
	 */
	public static InstallerStatus createInstallDir(String customDir) throws Exception {
		return createInstallDir(customDir, true);
	}
	
	/**
	 * Attempts to create the QoT installation directory on this computer's file
	 * system.
	 * <p>
	 * If successful, InstallerStatus.SUCCESS will be returned. If the directory
	 * already exists, InstallerStatus.EXISTS will be returned. If the directory
	 * could not be created, InstallerStatus.FAILED will be returned.
	 * 
	 * @param customDir The directory to install to
	 * @param extractResources Extracts resources to install dir if true
	 * 
	 * @return The resulting status of the creation attempt
	 */
	public static InstallerStatus createInstallDir(File customDir, boolean extractResources) throws Exception {
		if (customDir == null) return createInstallDir((String) null, extractResources);
		return createInstallDir(customDir.getAbsolutePath(), extractResources);
	}
	
	/**
	 * Attempts to create the QoT installation directory on this computer's file
	 * system.
	 * <p>
	 * If successful, InstallerStatus.SUCCESS will be returned. If the directory
	 * already exists, InstallerStatus.EXISTS will be returned. If the directory
	 * could not be created, InstallerStatus.FAILED will be returned.
	 * 
	 * @param customDir The directory to install to
	 * @param extractResources Extracts resources to install dir if true
	 * 
	 * @return The resulting status of the creation attempt
	 */
	public static InstallerStatus createInstallDir(String customDir, boolean extractResources) throws Exception {
		//determine installation path
		String path = null;
		if (customDir != null) {
			path = customDir;
		}
		else {
			var defaultPath = getDefaultInstallDir();
			if (defaultPath == null) {
				LauncherLogger.logError("Error: Default install dir is somehow null!");
				return InstallerStatus.FAILED;
			}
			path = defaultPath + "\\QoT";
		}
		
		//wrap path as file
		File dir = new File(path);
		LauncherLogger.log("Starting install at target: '" + dir + "'");
		
		//setup local game directory
		if (!dir.exists() && !dir.mkdir()) {
			LauncherLogger.logError("Error: Failed to create install target dir!");
			return InstallerStatus.FAILED;
		}
		
		//don't allow a non-empty directory to be installed to
		if (!dir.isDirectory()) {
			LauncherLogger.logError("Error: target install dir is not a directory!");
			return InstallerStatus.FAILED;
		}
		
		//extract resources into installation directory
		setupResourcesDir(dir, extractResources);
		
		//setup successful
		return InstallerStatus.SUCCESS;
	}
	
	/**
	 * Extracts texture/resources data into their respective folders within
	 * installation directory.
	 * 
	 * @param dir The QoT installation Directory to install to
	 */
	private static void setupResourcesDir(File dir, boolean extractResources) throws Exception {
		//create output directory for resources within install dir
		File resourcesDir = new File(dir, "resources");
		
		//create game directories in install dir
		File profilesDir = new File(dir, "saves");
		File worldsDir = new File(dir, "editorWorlds");
		
		//create each dir if they don't already exist
		LauncherLogger.log("Creating extraction points in install dir!");
		if (!resourcesDir.exists()) resourcesDir.mkdirs();
		if (!profilesDir.exists()) profilesDir.mkdirs();
		if (!worldsDir.exists()) worldsDir.mkdirs();
		
		if (extractResources) {
			//extract data from each internal dir into installation dir
			try { extractDataToDir("font", resourcesDir); }
			catch (Exception e) { e.printStackTrace(); throw e; }
			try { extractDataToDir("sounds", resourcesDir); }
			catch (Exception e) { e.printStackTrace(); throw e; }
			try { extractDataToDir("textures", resourcesDir); }
			catch (Exception e) { e.printStackTrace(); throw e; }
			//try { extractDataToDir("shaders", resourcesDir); }
			//catch (Exception e) { e.printStackTrace(); throw e; }
			
			//copy bundled maps into install map dir
			try { extractDataToDir("editorWorlds", dir); }
			catch (Exception e) { e.printStackTrace(); throw e; }
		}
	}
	
	/**
	 * Copies the files from one directory to another.
	 * <p>
	 * Determines what type of file system methodology to pursue when trying to
	 * extract resources from internal directories as Jar paths VASTLY differ in
	 * complexity to a standard IDE environment.
	 * 
	 * @param fromDir The internal dir to get resource data from
	 * @param toDir   The external dir to put extracted data in
	 * 
	 * @throws Exception Thrown if any error occurs during extraction
	 */
	private static void extractDataToDir(String fromPath, File toDir) throws Exception {
		LauncherLogger.log("Attempting to extract data from '" + fromPath + "' -> '" + toDir + "'");
		
		//this line is used to specifically grab the class system's file structure to determine
		//what kind of environment the game is being executed from (an IDE or a Jar)
		String path = Main.class.getResource(Main.class.getSimpleName() + ".class").getFile();
		
		//if path does not start with a '/' then it's very likely a jar file!
		if (!path.startsWith("/")) {
			path = ClassLoader.getSystemClassLoader().getResource(path).getFile();
			path = path.substring(path.indexOf(':') + 1);
			path = path.substring(0, path.lastIndexOf('!'));
			
			//String orig = path;
			path = URLDecoder.decode(path, "UTF-8");
			//LauncherLogger.log("Modifying path from: '" + orig + "' to '" + path + "'!");
			
			//wrap as file and begin the attempt to extract data from jar file
			File jarPath = new File(path);
			extractDataFromJarDir(jarPath, QoTLauncher.resourcePath + fromPath, toDir);
			return;
		}
		else {
			//attempt to get path as resource from classpath
			var das_path = QoTLauncher.resourcePath + fromPath;
			var url = Main.class.getResource("/" + das_path); //append '/' to stop relative path
			File dir = null;
			
			//try to convert resource url to file
			try {
				dir = new File(url.getFile());
			}
			catch (Exception e) {
				try {
					dir = new File(url.getPath());
				}
				catch (Exception ee) {
					e.printStackTrace();
					LauncherLogger.logError(ee);
				}
			}
			
			//begin the attempt to extract data from development dir
			extractDataFromDevDir(dir, new File(toDir, fromPath));
		}
	}
	
	/**
	 * Attempts to extract data from a JarFile (probably this one) while running.
	 * <p>
	 * Java JarFiles are more structurally similar to a zip file rather than a
	 * standard file system and require more effort to extract data from.
	 * 
	 * @param jarPath    The path of the jar file being extracted from on the
	 *                   compueter's file system
	 * @param pathToFind The resource dir path to extract files from
	 * @param toDir      The path to extract files to
	 * 
	 * @throws Exception Thrown if any error occurs while extracting
	 */
	private static void extractDataFromJarDir(File jarPath, String pathToFind, File toDir) throws Exception {
		JarFile jarFile = null;
		LauncherLogger.log("Starting extraction of '" + pathToFind + "' -> '" + toDir + "' from jar file '" + jarPath + "'");
		
		try {
			//construct jar file and extract jar entries
			jarFile = new JarFile(jarPath);
			var it = jarFile.entries().asIterator();
			
			String lastDir = "";
			boolean found = false;
			
			//iterate across entries and attempt to find paths that match the given 
			while (it.hasNext()) {
				var entry = it.next();
				var name = entry.getName();
				//LauncherLogger.log("\t" + name + " : " + pathToFind + " : " + pathToFind.startsWith(name));
				
				//ignore the entry if it does not start with the path being searched for
				if (name.startsWith(pathToFind)) {
					if (!found) found = true;
					
					//dynamically build directory path
					if (name.endsWith("/")) {
						if (!name.startsWith(lastDir)) lastDir = name;
						else lastDir += name;
						continue;
					}
					
					//create base level directory(ies)
					String destName = (name.startsWith("resources/")) ? name.substring("resources/".length()) : name;
					File newFile = new File(toDir, destName);
					Path newFilePath = newFile.toPath();
					if (!Files.exists(newFilePath)) Files.createDirectories(newFilePath.getParent());
					
					//because this is extracting from a jar, the resource must be extracted byte-by-byte
					byte[] bytes = Main.class.getClassLoader().getResourceAsStream(name).readAllBytes();
					LauncherLogger.log("Extracting '" + bytes.length + "' bytes to '" + newFile + "'!");
					
					//copy file to installation path
					try (var fos = new FileOutputStream(newFile)) {
						fos.write(bytes);
					}
					catch (Exception foserr) {
						foserr.printStackTrace();
						LauncherLogger.logError(foserr);
						throw foserr;
					}
				}
				//because of the way jar files are laid out, it can be assumed that as
				//soon as the underlying entry name no longer matches the path being
				//searched for, the final resource from that given directory has been
				//found. This means the while loop can exit early to save on performance.
				else if (found) break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			if (jarFile != null) jarFile.close();
		}
	}
	
	/**
	 * Attempts to extract data from this development environment's resources dir
	 * while running.
	 * 
	 * @param fromDir The resources dir to extract data from
	 * @param toDir   The installation dir to extract that data to
	 * 
	 * @throws IOException Thrown if any error occurs while extracting
	 */
	private static void extractDataFromDevDir(File fromDir, File toDir) throws IOException {
		toDir.mkdir();
		
		for (File f : fromDir.listFiles()) {
			if (f.isDirectory()) {
				var sub = new File(toDir.getAbsolutePath() + "\\" + f.getName());
				sub.mkdir();
				extractDataFromDevDir(f, sub);
			}
			else {
				Path internalPath = f.toPath();
				Path copyPath = Paths.get(toDir.getAbsolutePath() + "\\" + f.getName());
				LauncherLogger.log("Extracting file from '" + internalPath + " to '" + copyPath + "'!");
				Files.copy(internalPath, copyPath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
	
	/**
	 * Determines whether or not the game is being run out of a Jar file or a
	 * development environment and beings checking for whether or not the game's
	 * files have successfully been installed or not.
	 * <p>
	 * Note: At this current stage of development, the term 'installed' quite
	 * literally just means: "Does the given file exist" because there is no
	 * verification of the actual integrity of these files from the original ones.
	 * (intentionally)
	 * 
	 * @param fromPath The internal dir path to get resource data from
	 * @param toDir    The external dir to put extracted data in
	 * 
	 * @return True if the expected files for the game exist at the proper
	 *         installation directories
	 * 
	 * @throws Exception Thrown if any error occurs during extraction
	 */
	private static boolean verifyDir(String fromPath, File toDir) throws IOException {
		//this line is used to specifically grab the class system's file structure to determine
		//what kind of environment the game is being executed from (an IDE or a Jar)
		String path = Main.class.getResource(Main.class.getSimpleName() + ".class").getFile();
		
		//if path does not start with a '/' then it's very likely a jar file!
		if (!path.startsWith("/")) {
			path = ClassLoader.getSystemClassLoader().getResource(path).getFile();
			path = path.substring(path.indexOf(':') + 1);
			path = path.substring(0, path.lastIndexOf('!'));
			
			//String orig = path;
			path = URLDecoder.decode(path, "UTF-8");
			
			//wrap as file and begin the attempt to extract data from jar file
			File jarPath = new File(path);
			return verifyJarDir(jarPath, QoTLauncher.resourcePath + fromPath, toDir);
		}
		else {
			//attempt to get path as resource from classpath
			var url = Main.class.getResource("/" + QoTLauncher.resourcePath + fromPath); //append '/' to stop relative path
			File dir = null;
			
			//try to convert resource url to file
			try {
				dir = new File(url.getFile());
			}
			catch (Exception e) {
				try {
					dir = new File(url.getPath());
				}
				catch (Exception ee) {
					ee.printStackTrace();
					LauncherLogger.logError(ee);
				}
			}
			
			//begin the attempt to extract data from development dir
			return verifyDevDir(dir, new File(toDir, fromPath));
		}
	}
	
	/**
	 * Verifies a singular directory by ensuring that every file (including sub directories)
	 * has been fully copied over into the target directory.
	 * 
	 * @param fromDir
	 * @param toDir
	 * @return True if each file from 'fromDir' exists in 'toDir'
	 * @throws IOException
	 */
	private static boolean verifyJarDir(File jarPath, String pathToFind, File toDir) throws IOException {
		JarFile jarFile = null;
		LauncherLogger.log("Verifying dir '" + pathToFind + "' in '" + toDir + "' from jar file '" + jarPath + "'");
		
		try {
			//construct jar file and extract jar entries
			jarFile = new JarFile(jarPath);
			var it = jarFile.entries().asIterator();
			
			String lastDir = "";
			boolean found = false;
			
			//iterate across entries and attempt to find paths that match the given 
			while (it.hasNext()) {
				var entry = it.next();
				var name = entry.getName();
				
				//ignore the entry if it does not start with the path being searched for
				if (name.startsWith(pathToFind)) {
					if (!found) found = true;
					
					//dynamically build directory path
					if (name.endsWith("/")) {
						if (!name.startsWith(lastDir)) lastDir = name;
						else lastDir += name;
						continue;
					}
					
					String destName = (name.startsWith("resources/")) ? name.substring("resources/".length()) : name;
					File filePath = new File(toDir, destName);
					LauncherLogger.log("Verifying file '" + destName + "'");
					
					//if the file path to the current resource being verified doesn't exist, then
					//the install directory is not completely complete and should be reinstalled to be
					//properly verified
					if (!filePath.exists()) {
						LauncherLogger.log(toDir + " : " + filePath + " : " + lastDir + " : " + destName);
						LauncherLogger.log("File '" + filePath + "' not found! cannot fully verify install dir!");
						return false;
					}
				}
				//because of the way jar files are laid out, it can be assumed that as
				//soon as the underlying entry name no longer matches the path being
				//searched for, the final resource from that given directory has been
				//found. This means the while loop can exit early to save on performance.
				else if (found) break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			if (jarFile != null) jarFile.close();
		}
		
		return true;
	}
	
	/**
	 * Verifies a singular directory by ensuring that every file (including sub
	 * directories) has been fully copied over into the target directory.
	 * 
	 * @param fromDir The internal data dir to check file paths against
	 * @param toDir   The installation dir that files are supposedly installed to
	 * 
	 * @return True if each file from 'fromDir' exists in 'toDir'
	 * 
	 * @throws IOException Thrown if any error occurs in reading from either
	 *                     directory
	 */
	private static boolean verifyDevDir(File fromDir, File toDir) throws IOException {
		for (File f : fromDir.listFiles()) {
			if (f.isDirectory()) {
				var sub = new File(toDir.getAbsolutePath() + "\\" + f.getName());
				
				//if the sub directory does not exist, the install directory cannot be fully verified
				if (!sub.exists()) {
					LauncherLogger.log("Dir '" + sub + "' not found! cannot fully verify install dir!");
					return false;
				}
				
				//recursively check the next inner directory
				if (!verifyDevDir(f, sub)) return false;
			}
			else {
				File checkPath = new File(toDir.getAbsolutePath() + "\\" + f.getName());
				LauncherLogger.log("Verifying file '" + f.getName() + "'");
				
				if (!checkPath.exists()) {
					LauncherLogger.log("File '" + f.getName() + "' not found! cannot fully verify install dir!");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Returns true if QoT has an installation dir already created on this
	 * computer's file system.
	 * 
	 * @return True if install directory exists on this computer
	 */
	public static boolean doesInstallDirExist(File dir) {
		if (dir == null) {
			LauncherLogger.log("No current QoT dir exists");
			return false;
		}
		return doesInstallDirExist(dir.getAbsolutePath());
	}
	
	/**
	 * Returns true if QoT has an installation dir already created on this
	 * computer's file system.
	 * 
	 * @return True if install directory exists on this computer
	 */
	public static boolean doesInstallDirExist(String dirPath) {
		File dir = new File(dirPath);
		return dir.exists();
	}
	
	/**
	 * Performs checks to verify that each internal dir has been properly copied
	 * over into the installation directory.
	 * 
	 * @param dirPath The installation dir
	 * @return True if installation is valid
	 */
	public static boolean verifyActuallyInstalled(String dir) {
		if (dir == null) {
			LauncherLogger.log("QoT dir null -- nothing to verify");
			return false;
		}
		return verifyActuallyInstalled(new File(dir));
	}
	
	/**
	 * Performs checks to verify that each internal dir has been properly copied
	 * over into the installation directory.
	 * 
	 * @param dirPath The installation dir
	 * @return True if installation is valid
	 */
	public static boolean verifyActuallyInstalled(File dir) {
		//if dir is null, then QoT is not installed!
		if (dir == null) {
			LauncherLogger.log("QoT dir null -- nothing to verify");
			return false;
		}
		
		//if dir doesn't exist, then QoT is not installed!
		if (!dir.exists()) {
			LauncherLogger.log("QoT dir does not actually exist -- nothing to verify");
			return false;
		}
		
		//create output directory for resources within install dir
		File resourcesDir = new File(dir, "resources");
		if (!resourcesDir.exists()) {
			LauncherLogger.log("QoT resources dir does not actually exist -- cannot verify!");
			return false;
		}
		
		//assuming true
		boolean verified = true;
		
		try {
			//verify that data from each internal dir exists within the installation dir
			try { verified &= verifyDir("font", resourcesDir); }
			catch (Exception e) { e.printStackTrace(); throw e; }
			try { verified &= verifyDir("sounds", resourcesDir); }
			catch (Exception e) { e.printStackTrace(); throw e; }
			try { verified &= verifyDir("textures", resourcesDir); }
			catch (Exception e) { e.printStackTrace(); throw e; }
			//try { verified &= verifyDir("shaders", resourcesDir); }
			//catch (Exception e) { e.printStackTrace(); throw e; }
			//copy bundled maps into install map dir
			try { verified &= verifyDir("editorWorlds", dir); }
			catch (Exception e) { e.printStackTrace(); throw e; }
		}
		catch (Exception e) {
			LauncherLogger.logErrorWithDialogBox(e, "Error reading install dir!", "Setup Error!");
		}
		
		//return verification status
		return verified;
	}
	
}
