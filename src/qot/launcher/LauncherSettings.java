package game.launcher;

import java.io.File;

public class LauncherSettings {

	/**
	 * The directory for which QoT is installed to.
	 */
	public File INSTALL_DIR = null;
	
	/**
	 * The path for the game's resources dir.
	 */
	public boolean USE_INTERNAL_RESOURCES_PATH = false;
	
	/**
	 * True if the game is being run out of a jar file.
	 */
	public boolean IN_JAR = false;
	
	//--------------
	// Constructors
	//--------------
	
	public LauncherSettings() {}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "INSTALL_DIR=" + INSTALL_DIR + " " +
			   "USE_INTERNAL_RESOURCE_PATH=" + USE_INTERNAL_RESOURCES_PATH + " " +
			   "IN_JAR=" + IN_JAR;
	}
	
}
