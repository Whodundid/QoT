package envision.debug.debugCommands;

import envision.Envision;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.resourceLoaders.textures.TextureLoader;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.TextureDisplayer;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.datatypes.util.EList;

@SuppressWarnings("unused")
public class Deb0 extends DebugCommand {
	
	@Override
	public void run(ETerminalWindow termIn, Object... args) throws Exception {
		TextureLoader loader = values.getOrCreate("loader", new TextureLoader("textures.json"));
		
		loader.unload();
		loader.save();
		loader.load();
//		EList<GameTexture> textures = loader.getLoadedTextures();
//		
//		for (GameTexture t : textures) {
//		    TextureDisplayer displayer = new TextureDisplayer(t);
//		    Envision.getActiveTopParent().displayWindow(displayer, ObjectPosition.EXISTING_OBJECT_INDENT);
//		}
	}

}
