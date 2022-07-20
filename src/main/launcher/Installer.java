package main.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import engine.util.ESystemInfo;
import eutil.sys.OSType;
import main.QoT;

public class Installer {

	public static final String DEFAULT_WINDOWS_INSTALL_DIR = "\\AppData\\Roaming";
	
	//-------------
	// Helper Enum
	//-------------
	
	protected enum InstallerStatus { SUCCESS, EXISTS, FAILED; }

	//--------------
	// Constructors
	//--------------
	
	private Installer() {}
	
	//----------------
	// Static Methods
	//----------------
	
	/**
	 * Returns the file system path for where QoT would be
	 * installed to given the user's OS.
	 * 
	 * @return The installation path File
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
		
		return dir;
	}
	
	/**
	 * Attempts to create the QoT installation directory
	 * on this computer's file system.
	 * <p>
	 * If successful, InstallerStatus.SUCCESS will be returned.
	 * If the directory already exists, InstallerStatus.EXISTS will be returned.
	 * If the directory could not be created, InstallerStatus.FAILED will be returned.
	 * 
	 * @return The resulting status of the creation attempt
	 */
	public static InstallerStatus createInstallDir(File customDir) throws Exception {
		if (customDir == null) return createInstallDir((String) null);
		return createInstallDir(customDir.getAbsolutePath());
	}
	
	/**
	 * Attempts to create the QoT installation directory
	 * on this computer's file system.
	 * <p>
	 * If successful, InstallerStatus.SUCCESS will be returned.
	 * If the directory already exists, InstallerStatus.EXISTS will be returned.
	 * If the directory could not be created, InstallerStatus.FAILED will be returned.
	 * 
	 * @return The resulting status of the creation attempt
	 */
	public static InstallerStatus createInstallDir(String customDir) throws Exception {
		//determine installation path
		String path = null;
		if (customDir != null) {
			path = customDir;
		}
		else {
			var defaultPath = getDefaultInstallDir();
			if (defaultPath == null) return InstallerStatus.FAILED;
			path = defaultPath + "\\QoT";
		}
		
		//wrap path as file
		File dir = new File(path);
		
		//check if directory already exists
		//if (dir.exists()) return InstallerStatus.EXISTS;
		
		//setup local game directory
		if (!dir.exists() && !dir.mkdir()) {
			return InstallerStatus.FAILED;
		}
		
		//don't allow a non-empty directory to be installed to
		if (dir.isDirectory() && dir.list().length > 0) {
			System.out.println(dir.list().length);
			return InstallerStatus.FAILED;
		}
		
		//extract resources into installation directory
		setupResourcesDir(dir);
		
		//setup successful
		return InstallerStatus.SUCCESS;
	}
	
	/**
	 * Extracts texture/resources data into their respective folders within installation directory.
	 * 
	 * @param dir The installation directory
	 */
	private static void setupResourcesDir(File dir) throws Exception {
		//get mappings to internal resource directories to prepare for extraction
//		File internalFontDir = getInternalDir("font");
//		File internalSoundsDir = getInternalDir("sounds");
//		File internalTexturesDir = getInternalDir("textures");
//		File internalShaderDir = getInternalDir("shaders");
//		File internalMapsDir = getInternalDir("maps");
		
		//create output directory for resources within install dir
		File resourcesDir = new File(dir, "resources");
		if (!resourcesDir.exists()) resourcesDir.mkdirs();
		
		//create each internal directory mapping in install dir
//		File fontDir = new File(dir, "resources\\font");
//		File soundsDir = new File(dir, "resources\\sounds");
//		File texturesDir = new File(dir, "resources\\textures");
//		File shaderDir = new File(dir, "resources\\shaders");
//		//create game directories in install dir
		File profilesDir = new File(dir, "saves");
		//File editorWorldsDir = new File(dir, "editorWorlds");
//		
//		//create dir if it doesn't already exist
//		if (!fontDir.exists()) fontDir.mkdirs();
//		if (!soundsDir.exists()) soundsDir.mkdirs();
//		if (!texturesDir.exists()) texturesDir.mkdirs();
//		if (!shaderDir.exists()) shaderDir.mkdirs();
		if (!profilesDir.exists()) profilesDir.mkdirs();
		//if (!editorWorldsDir.exists()) editorWorldsDir.mkdirs();
		
		//extract data from each internal dir into installation dir
		try { extractDataToDir("font", resourcesDir); }
		catch (Exception e) { e.printStackTrace(); throw e; }
		try { extractDataToDir("sounds", resourcesDir); }
		catch (Exception e) { e.printStackTrace(); throw e; }
		try { extractDataToDir("textures", resourcesDir); }
		catch (Exception e) { e.printStackTrace(); throw e; }
		try { extractDataToDir("shaders", resourcesDir); }
		catch (Exception e) { e.printStackTrace(); throw e; }
		
		//copy bundled maps into install map dir
		try { extractDataToDir("editorWorlds", dir); }
		catch (Exception e) { e.printStackTrace(); throw e; }
	}
	
	/**
	 * Retrieves the internal resources dir pertaining to the given path.
	 * 
	 * @param path
	 * @return The File to the internal path
	 */
	private static File getInternalDir(String path) throws Exception {
//		try {
//			URL url = QoT.class.getResource(path);
//			File file = null;
//			
//			try {
//				file = new File(url.toURI());
//			}
//			catch (URISyntaxException e) {
//				file = new File(url.getPath());
//			}
//			
//			return file;
//		}
//		catch (IllegalArgumentException | NullPointerException e) {
			//attempt to access jar path directly

				
//				return tempDir;
//			}
//			catch (Exception ee) {
//				Launcher.logError(ee);
//			}
//			finally {
//				if (jarFile != null) jarFile.close();
//			}
//		}
			
		return null;
	}
	
	private static File getJarFile() {
		String path = QoT.class.getResource(QoT.class.getSimpleName() + ".class").getFile();
		if (path.startsWith("/")) return null;
		path = ClassLoader.getSystemClassLoader().getResource(path).getFile();
		path = path.substring(path.indexOf(':') + 1);
		path = path.substring(0, path.lastIndexOf('!'));
		return new File(path);
	}
	
	/**
	 * Copies the files from one directory to another.
	 * 
	 * @param fromDir
	 * @param toDir
	 * @throws Exception
	 */
	private static void extractDataToDir(String fromPath, File toDir) throws Exception {
		extractDataFromJarDir(fromPath, toDir);
		
//		File fromDir = getInternalDir(fromPath);
//		
//		if (fromDir == null) {
//			
//		}
//		else
//		
//		for (File f : fromDir.listFiles()) {
//			if (f.isDirectory()) {
//				File sub = new File(toDir.getAbsolutePath() + "\\" + f.getName());
//				sub.mkdir();
//				extractDataToDir(f, sub);
//			}
//			else {
//				Path internalPath = f.toPath();
//				Path copyPath = Paths.get(toDir.getAbsolutePath() + "\\" + f.getName());
//				Files.copy(internalPath, copyPath, StandardCopyOption.REPLACE_EXISTING);
//			}
//		}
	}
	
	private static void extractDataFromJarDir(String jarPath, File toDir) throws Exception {
		JarFile jarFile = null;
		
		try {
			jarFile = new JarFile(getJarFile());
			var it = jarFile.entries().asIterator();
			
			//create temporary directory to store extracted files into
			//File tempDir = Files.createTempDirectory("qot-"+path+"-").toFile();
			//FileUtils.forceDeleteOnExit(tempDir);
			
			String lastDir = "";
			boolean found = false;
			
			while (it.hasNext()) {
				var entry = it.next();
				var name = entry.getName();
				
				if (name.startsWith(jarPath)) {
					if (!found) found = true;
					
					//grab dir names
					if (name.endsWith("/")) {
						if (!name.startsWith(lastDir)) lastDir = name;
						else lastDir += name;
						continue;
					}
					
					//Launcher.logError(lastDir + "   :   " + StringUtil.subStringToString(name, "/", true));
					File newFile = new File(toDir, name);
					Path newFilePath = newFile.toPath();
					Launcher.logError("PARENT: " + newFilePath.getParent());
					if (!Files.exists(newFilePath)) Files.createDirectories(newFilePath.getParent());
					//if (!newFile.exists())
					
					Launcher.logError(newFile);
					
					byte[] bytes = QoT.class.getClassLoader().getResourceAsStream(name).readAllBytes();
					Launcher.logError("WRITING '" + bytes.length + "' to '" + newFile + "'!");
					//Files.write(newFilePath, bytes, StandardOpenOption.CREATE_NEW);
					
					try (var fos = new FileOutputStream(newFile)) {
						fos.write(bytes);
					}
					catch (Exception foserr) {
						Launcher.logError(foserr);
					}
					
					Launcher.logError(name);
				}
				else if (found) break;
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (jarFile != null) jarFile.close();
		}
	}
	
	private static void extractDataToDir(File fromDir, File toDir) throws IOException {
		for (File f : fromDir.listFiles()) {
			if (f.isDirectory()) {
				var sub = new File(toDir.getAbsolutePath() + "\\" + f.getName());
				sub.mkdir();
				extractDataToDir(f, sub);
			}
			else {
				Path internalPath = f.toPath();
				Path copyPath = Paths.get(toDir.getAbsolutePath() + "\\" + f.getName());
				Files.copy(internalPath, copyPath, StandardCopyOption.REPLACE_EXISTING);
			}
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
//	private static boolean verifyDir(File fromDir, File toDir) throws IOException {
//		for (File f : fromDir.listFiles()) {
//			if (f.isDirectory()) {
//				var sub = new File(toDir.getAbsolutePath() + "\\" + f.getName());
//				if (!sub.exists()) return false;
//				if (!verifyDir(f, sub)) return false;
//			}
//			else {
//				File checkPath = new File(toDir.getAbsolutePath() + "\\" + f.getName());
//				if (!checkPath.exists()) return false;
//			}
//		}
//		return true;
//	}
	
	/**
	 * Returns true if QoT has an installation dir already created
	 * on this computer's file system.
	 * 
	 * @return True if install directory exists on this computer
	 */
	public static boolean doesInstallDirExist(File dir) {
		if (dir == null) return false;
		return doesInstallDirExist(dir.getAbsolutePath());
	}
	
	/**
	 * Returns true if QoT has an installation dir already created
	 * on this computer's file system.
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
		if (dir == null) return false;
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
		if (dir == null) return false;
		
		//if dir doesn't exist, then QoT is not installed!
		if (!dir.exists()) return false;
		
		try {
			//get installation directory mapping for each internal dir
			File fontDir = new File(dir, "resources\\font");
			File soundsDir = new File(dir, "resources\\sounds");
			File texturesDir = new File(dir, "resources\\textures");
			File shaderDir = new File(dir, "resources\\shaders");
			
			//verify that each internal dir has been properly copied over into the installation directory
			if (!fontDir.exists() || fontDir.list().length == 0) return false;
			if (!soundsDir.exists() || soundsDir.list().length == 0) return false;
			if (!texturesDir.exists() || texturesDir.list().length == 0) return false;
			if (!shaderDir.exists() || shaderDir.list().length == 0) return false;
		}
		catch (Exception e) {
			Launcher.logError(e);
			JOptionPane.showMessageDialog(null, "Error reading install dir! " + e, "Setup Error!", JOptionPane.ERROR_MESSAGE);
		}
		
		//if this point is reached, then no essential internal files are missing!
		return true;
	}
	
}
