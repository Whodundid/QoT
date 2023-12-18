package qot;

import envision.Envision;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.settings.config.EnvisionConfigFile;
import envision.game.AbstractWorldCreator;
import envision.game.EnvisionGameSettings;
import envision.game.EnvisionGameTemplate;
import envision.launcher.EnvisionGameLauncher;
import envision.launcher.LauncherSettings;
import qot.assets.textures.GameTextures;
import qot.assets.textures.entity.EntityTextures;
import qot.screens.main.MainMenuScreen;
import qot.settings.QoTSettings;

public class QoT implements EnvisionGameTemplate {
	
	//========
	// Fields
	//========
	
	public static final String version = "Quest of Thyrah: March 3rd, 2023";
	private static QoT instance;
	
	//==============
	// Constructors
	//==============
	
	private QoT() {
	    // nothing
	}
	
	public static QoT instance() {
	    if (instance == null) instance = new QoT();
		return instance;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void onPostEngineLoad() {
		Envision.displayScreen(new MainMenuScreen());
	}
	
    @Override
    public String getGameName() { return "Quest of Thyrah"; }

    @Override
    public String getGameVersionString() { return version; }

    @Override
    public EnvisionGameSettings getGameSettings() { return QoTSettings.instance(); }

    @Override
    public EnvisionConfigFile getConfigFile() { return QoTSettings.getGameConfig(); }

    @Override
    public AbstractWorldCreator getWorldCreator() { return null; }
	
    @Override
    public EnvisionGameLauncher createGameLauncher(LauncherSettings settings) {
        return new EnvisionGameLauncher(settings) {
            @Override
            protected void launchGame(LauncherSettings settings) {
                Envision.loadGame(settings);
                Envision.setWindowIcon(EntityTextures.whodundid);
                Envision.setTargetFPS(240);
                Envision.setTargetUPS(150);
                Envision.startGame();
            }
        };
    }
    
    @Override
    public void onRegisterInternalTextures(TextureSystem textureSystem) {
        GameTextures.instance().onRegister(textureSystem);
    }
    
	//================
	// Static Methods
	//================
	
//	public static void startGame(LauncherSettings settings) {
//		try {
//			//QoTSettings(settings.INSTALL_DIR, settings.USE_INTERNAL_RESOURCES_PATH);
//			
//			if (Main.RUN_OPEN_GL_TESTING_ENVIRONMENT) {
//				OpenGLTestingEnvironment.runTestingEnvironment(settings);
//			}
//			else {
//				LauncherLogger.log("---------------------------\n");
//				LauncherLogger.log("Running game with settings: " + settings);
//				
//				Envision.loadGame(settings);
//		        Envision.setWindowIcon(EngineTextures.noscreens);
//		        Envision.setTargetFPS(240);
//		        Envision.setTargetUPS(150);
//		        Envision.startGame();
//			}
//		}
//		catch (Exception e) {
//			LauncherLogger.logError(e);
//			FileOpener.openFile(LauncherLogger.getLogFile());
//			throw e;
//		}
//	}
	
}
