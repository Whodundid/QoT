package main;

import eutil.sys.TracingPrintStream;
import main.launcher.Installer;
import main.launcher.Launcher;
import main.launcher.LauncherSettings;

public class Main {

	//-------------------------------------
	// Runner Settings
	//-------------------------------------
	// Modify these values for game launch
	//-------------------------------------
	
	/**
	 * Runs the game's launcher instead of directly out of a dev environment.
	 */
	public static final boolean RUN_LAUNCHER = true;
	
	/** Uses the internal jar resources instead of the install dir's. */
	public static final boolean USE_INTERNAL_RESOURCES = true;
	
	/**
	 * Instructs the launcher/installer on whether or not the game's internal
	 * resources should be extracted to the install dir.
	 * <p>
	 * Right now, there are not a ton of resources so loading times are still pretty
	 * quick regardless, but this is likely to change in future builds as the game
	 * develops.
	 * <p>
	 * NOTE: If 'USE_INTERNAL_RESOURCES' is false then this value NEEDS to be true
	 * otherwise the game will break if not already installed!
	 */
	public static final boolean EXTRACT_RESOURCES = false;
	
	//------
	// Main
	//------
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		//setup print tracing
		TracingPrintStream.enableTrace();
		TracingPrintStream.setTracePrimitives(true);
		
		//-------------------------------------------
		
		//if opting to use the launcher
		if (RUN_LAUNCHER) {
			Launcher.runLauncher();
		}
		//otherwise if opting to run directly from a dev environment
		else {
			try {
				//create fake launcher settings to bind resource paths
				var settings = new LauncherSettings();
				settings.INSTALL_DIR = Installer.getDefaultQoTInstallDir();
				
				//creates the base install directory for settings/resources/worlds/etc.
				if (!Installer.verifyActuallyInstalled(settings.INSTALL_DIR)) {
					Installer.createInstallDir(settings.INSTALL_DIR, EXTRACT_RESOURCES);
				}
				
				settings.USE_INTERNAL_RESOURCES_PATH = USE_INTERNAL_RESOURCES;
				
				//start the game with the fake settings
				QoT.startGame(settings);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
