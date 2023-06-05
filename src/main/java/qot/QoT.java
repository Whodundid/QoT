package qot;

import envision.Envision;
import envision.EnvisionGame;
import envision.debug.testGame.OpenGLTestingEnvironment;
import eutil.file.FileOpener;
import qot.assets.textures.entity.EntityTextures;
import qot.launcher.LauncherLogger;
import qot.launcher.LauncherSettings;
import qot.screens.main.MainMenuScreen;
import qot.settings.QoTSettings;

public class QoT implements EnvisionGame {
	
	//========
	// Fields
	//========
	
	public static final String version = "Quest of Thyrah: March 3rd, 2023";
	private static QoT instance;
	
	//==============
	// Constructors
	//==============
	
	private QoT() {
		instance = this;
	}
	
	public QoT instance() {
		return instance;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void onPostEngineLoad() {
		Envision.displayScreen(new MainMenuScreen());
	}
	
	//================
	// Static Methods
	//================
	
	public static void startGame(LauncherSettings settings) {
		try {
			QoTSettings.init(settings.INSTALL_DIR, settings.USE_INTERNAL_RESOURCES_PATH);
			
			if (Main.RUN_OPEN_GL_TESTING_ENVIRONMENT) {
				OpenGLTestingEnvironment.runTestingEnvironment(settings);
			}
			else {
				LauncherLogger.log("---------------------------\n");
				LauncherLogger.log("Running game with settings: " + settings);
				
				instance = new QoT();
				Envision.createGame(instance);
				Envision.getInstance().setIcon(EntityTextures.whodundid);
				Envision.setTargetFPS(240);
				Envision.setTargetUPS(150);
				Envision.startGame(settings);
			}
		}
		catch (Exception e) {
			LauncherLogger.logError(e);
			FileOpener.openFile(LauncherLogger.getLogFile());
			throw e;
		}
	}
	
}
