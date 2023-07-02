package envision.engine.scripting.envisionMappings.qot_package.methods;

import java.io.File;

import envision.Envision;
import envision.game.entities.player.Player;
import envision.game.world.GameWorld;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.ArgLengthError;
import envision_lang.lang.language_errors.error_types.InvalidArgumentError;
import envision_lang.lang.natives.Primitives;
import qot.entities.player.QoT_Player;
import qot.screens.gameplay.GamePlayScreen;
import qot.settings.QoTSettings;

/** Creates a mapping between Envision and the QoT terminal. */
public class LoadWorld_ENV extends EnvisionFunction {
	
	public LoadWorld_ENV() {
		super(Primitives.VAR, "loadWorld");
	}
	
	@SuppressWarnings("unused")
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length == 0|| args.length > 1) throw new ArgLengthError(this, args.length, 1);
		
		if (args[0] instanceof EnvisionString env_str) {
			String worldName = env_str.get_i();
			GameWorld world = buildWorld(worldName);
			
			if (world.getWorldFile().exists()) {
				Player p = Envision.setPlayer(new QoT_Player("Test"));
				Envision.displayScreen(new GamePlayScreen());
				Envision.loadWorld(world);
				world.addObjectToWorld(p);
			}
			else throw new EnvisionLangError("The world '" + worldName + "' does not exist!");
		}
		else if (args[0] instanceof ClassInstance inst) {
			if (inst.getTypeString().equals("World")) {
				GameWorld world = null;//(GameWorld) inst.get("_iWorld_");
				
				if (world != null) {
					System.out.println("loading...");
					QoT_Player p = (QoT_Player) Envision.setPlayer(new QoT_Player("Test"));
					Envision.displayScreen(new GamePlayScreen());
					Envision.loadWorld(world);
					world.addObjectToWorld(p);
				}
			}
			else throw new InvalidArgumentError("Expected a World object!");
		}
	}
	
	private static GameWorld buildWorld(String worldName) {
		worldName = (worldName.endsWith(".twld")) ? worldName : worldName + ".twld";
		File f = new File(QoTSettings.getEditorWorldsDir(), worldName);
		return new GameWorld(f);
	}
	
}
