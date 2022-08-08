package main.launcher;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;

import eutil.strings.StringUtil;
import eutil.sys.LineReader;
import main.QoT;

/**
 * Manages the QoT Launcher dir that is installed to
 * 'Users/-/AppData/Roaming/QoT Launcher' by default.
 * <p>
 * This settings file keeps track of where QoT will actually be
 * installed to on the host computer's file system.
 * 
 * @author Hunter Bragg
 */
public class LauncherDir {
	
	//----------------------------------
	// Settings File - Launcher Options
	//----------------------------------
	
	private static final String INSTALL_PATH_SETTING = "INSTALL_PATH: ";
	private static final String LAUNCHER_LOG_LEVEL = "LAUNCHER_LOG_LEVEL [ALL, ONLY_ERRORS]: ";
	private static final String RUN_LAUNCHER_SETTING = "RUN_LAUNCHER: ";

	//---------------
	// Static Fields
	//---------------
	
	/** The path to the launcher dir. */
	private static File launcherDir;
	/** The path to the launcher settings file within the launcher dir. */
	private static File launcherSettingsFile;
	/** True if the launcher will actually be run -- THIS SHOULD ALMOST ALWAYS BE TRUE UNLESS AN ERROR OCCURED! */
	static boolean runLauncher = true;
	/** The file path that QoT is or will be installed to. 'Users/-/AppData/Roaming/QoT' by default. */
	static File installDir = null;
	/** The parsed log level from the launcher settings file -- defaults to 'ONLY_ERRORS'. */
	static LogOutputLevel logLevel = LogOutputLevel.ONLY_ERRORS;
	
	//----------------
	// Static Getters
	//----------------
	
	public static File getLauncherDir() { return launcherDir; }
	public static File getLauncherSettingsFile() { return launcherSettingsFile; }
	
	//----------------------------
	// Launcher Dir Setup Methods
	//----------------------------
	
	/**
	 * Attempts to create the launchers directory and settings file to keep track of install path.
	 * 
	 * @return
	 */
	static boolean setupLauncherDir() {
		try {
			String dir = QoTInstaller.getDefaultInstallDir();
			launcherDir = new File(dir + "\\QoT Launcher");
			
			//check if launcher directory already exists
			if (!launcherDir.exists() && !launcherDir.mkdirs()) {
				LauncherLogger.logErrorWithDialogBox("Cannot create the QoT Launcher directory!", "Setup Error!");
				return false;
			}
		}
		catch (Exception e) {
			LauncherLogger.logErrorWithDialogBox(e, "Cannot create the QoT Launcher directory!", "Setup Error!");
			return false;
		}
		
		//check if launcher settings file exists and create default if not
		try {
			launcherSettingsFile = new File(launcherDir, "settings.txt");
			
			//create default launcher settings file
			if (!launcherSettingsFile.exists()) {
				var defaultSettings = new LauncherSettings();
				defaultSettings.INSTALL_DIR = QoTInstaller.getDefaultQoTInstallDir();
				updateLauncherSettingsFile(defaultSettings);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			LauncherLogger.logErrorWithDialogBox(e, "Cannot create the QoT Launcher directory!", "Setup Error!");
			return false;
		}
		
		if (launcherSettingsFile != null && launcherSettingsFile.exists()) {
			//assume that the launcher should be run
			runLauncher = getConfigSetting(RUN_LAUNCHER_SETTING, Boolean.class, true);
			//assume that the log level will only log errors
			logLevel = getConfigSetting(LAUNCHER_LOG_LEVEL, LogOutputLevel.class, LogOutputLevel.ONLY_ERRORS);
		}
		
		return true;
	}
	
	/**
	 * Creates the launcher settings file using the given launcher settings.
	 * 
	 * @param settings
	 * @throws Exception
	 */
	static void updateLauncherSettingsFile(LauncherSettings settings) throws Exception {
		//create settings file for which to store launcher specific setting in
		launcherSettingsFile = new File(launcherDir, "settings.txt");
		
		try (var writer = new FileWriter(launcherSettingsFile, Charset.forName("UTF-8"))) {
			String version = "# QoT Version: " + QoT.version;
			String dashes = StringUtil.repeatString("-", version.length());
			writer.write("#" + dashes + "\n");
			writer.write(version);
			writer.write("\n#" + dashes + "\n");
			
			if (settings != null) LauncherLogger.log("Updating from launcher settings: " + settings);
			File installDir = (settings != null) ? settings.INSTALL_DIR : null;
			String dirPath = (installDir != null) ? installDir.getAbsolutePath() : "";
			LauncherLogger.log("Writing install path: " + dirPath);
			
			writer.write("\n# The directory QoT is or will be installed to");
			writer.write("\n" + INSTALL_PATH_SETTING + dirPath);
			writer.write("\n\n# Sets the level for logging -- if 'ONLY_ERRORS' then no debug outputs will be logged");
			writer.write("\n" + LAUNCHER_LOG_LEVEL + logLevel);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Parses the launcher settings file for the saved installation directory.
	 * 
	 * @return The parsed installation directory file
	 */
	static File getInstallDir() {
		return getConfigSetting(INSTALL_PATH_SETTING, File.class, null);
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	/**
	 * Attempts to parse a config value from the Launcher Settings Config
	 * File.
	 * <p>
	 * If the value attempting to be read doesn't actually exist, the
	 * default value is returned instead.
	 * 
	 * @param <E>          The datatype being worked with
	 * 
	 * @param valueName    The config setting attempting to be read from
	 * 
	 * @param asType       The datatype of the object attempting to be
	 *                     parsed from the config file
	 * 					
	 * @param defaultValue A value returned if the config value could not
	 *                     be successfully parsed
	 * 					
	 * @return The parsed config value's result or the provided default
	 *         value if failed
	 */
	private static <E> E getConfigSetting(String valueName, Class<E> asType, E defaultValue) {
		if (launcherSettingsFile == null) return defaultValue;
		if (valueName == null) return null;
		
		try (var br = new LineReader(launcherSettingsFile)) {
			while (br.hasNext()) {
				String line = br.nextLine();
				
				//ignore comments
				if (line.startsWith("#")) continue;
				//check for matching line
				if (line.startsWith(valueName)) {
					String value = line.substring(valueName.length());
					if (value.isBlank() || value.isEmpty()) return defaultValue;
					E r = null;
					
					//attempt to cast the parsed value string to the specified datatype
					try {
						if (asType.isAssignableFrom(Boolean.class)) r = asType.cast(Boolean.parseBoolean(value));
						if (asType.isAssignableFrom(Byte.class)) r = asType.cast(Byte.parseByte(value));
						if (asType.isAssignableFrom(Short.class)) r = asType.cast(Short.parseShort(value));
						if (asType.isAssignableFrom(Integer.class)) r = asType.cast(Integer.parseInt(value));
						if (asType.isAssignableFrom(Long.class)) r = asType.cast(Long.parseLong(value));
						if (asType.isAssignableFrom(Float.class)) r = asType.cast(Float.parseFloat(value));
						if (asType.isAssignableFrom(Double.class)) r = asType.cast(Double.parseDouble(value));
						if (asType.isAssignableFrom(String.class)) r = asType.cast(value);
						if (asType.isEnum()) r = (E) Enum.valueOf((Class) asType, value.toUpperCase());
						if (asType.isAssignableFrom(File.class)) r = (E) new File(value);
					}
					catch (Exception e) {
						e.printStackTrace();
						LauncherLogger.logError(e);
						return defaultValue;
					}
					
					//NOTE: the user could in theory type 'null' into the specified config value
					//I am not sure if this will actually fully be read in as a 'null' value
					//depending on the type -- as such: don't allow parsed 'null' values to
					//actually be returned
					if (r != null) return r;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			LauncherLogger.logError(e);
		}
		
		return defaultValue;
	}
	
}
