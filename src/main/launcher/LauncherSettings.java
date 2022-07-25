package main.launcher;

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
			   "USE_INTERNAL_RESOURCE_PATH=" + USE_INTERNAL_RESOURCES_PATH;
	}
	
}
