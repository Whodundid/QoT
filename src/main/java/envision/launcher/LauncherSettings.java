package envision.launcher;

import java.io.File;

import envision.game.EnvisionGameTemplate;
import eutil.datatypes.util.EList;

public class LauncherSettings {

    /**
     * The name of the game being installed.
     */
    public final EnvisionGameTemplate game;
    
	/**
	 * The directory for which the game will be installed to.
	 */
	public File INSTALL_DIR = null;
	
	/**
	 * The path for the game's resources dir.
	 */
	public boolean USE_INTERNAL_RESOURCES_PATH = false;
	
	/**
	 * True if the game will be run out of a jar file.
	 */
	public boolean IN_JAR = false;
	
	/**
	 * A list of all directories to extract/install from.
	 */
	private final EList<String> directoriesToExtract = EList.newList();
	private final EList<String> resourceDirectoriesToExtract = EList.newList();
	
	//==============
	// Constructors
	//==============
	
	public LauncherSettings(EnvisionGameTemplate gameIn) {
	    this.game = gameIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "INSTALL_DIR=" + INSTALL_DIR + " | " +
			   "USE_INTERNAL_RESOURCE_PATH=" + USE_INTERNAL_RESOURCES_PATH + " | " +
			   "IN_JAR=" + IN_JAR;
	}
	
	public EnvisionGameTemplate getGame() { return game; }
	public String getGameName() { return game.getGameName(); }
	public String getGameVersionString() { return game.getGameVersionString(); }
	
	public EList<String> getDirectoriesToExtract() { return EList.of(directoriesToExtract); }
	public EList<String> getResourceDirectoriesToExtract() { return EList.of(resourceDirectoriesToExtract); }
	
	public void setInstallationDir(File dirIn) { INSTALL_DIR = dirIn; }
	
	public void setAdditionalDirectoriesToExtract(String... directories) { directoriesToExtract.clearThenAdd(directories); }
	public void setAdditionalDirectoriesToExtract(EList<String> directories) { directoriesToExtract.clearThenAddAll(directories); }
	
	public void setResourceDirectoriesToExtract(String... directories) { resourceDirectoriesToExtract.clearThenAdd(directories); }
    public void setResourceDirectoriesToExtract(EList<String> directories) { resourceDirectoriesToExtract.clearThenAddAll(directories); }
	
}
