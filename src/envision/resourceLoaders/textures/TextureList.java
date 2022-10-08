package envision.resourceLoaders.textures;

import java.io.File;
import java.util.HashMap;

import envision.Envision;
import envision.renderEngine.textureSystem.GameTexture;
import envision.resourceLoaders.IResourceManager;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.file.EFileUtil;
import eutil.file.LineReader;
import game.QoT;

/**
 * Manages an active record file containing all of the textures
 * known to the engine.
 * 
 * @author Hunter Bragg
 */
public class TextureList implements IResourceManager {
	
	/** The active record of all loaded game textures. Maps name to texture. */
	private final HashMap<String, GameTexture> textures = new HashMap<>();
	/**
	 * Keeps track of textures that have been parsed but not yet loaded.
	 */
	private final EArrayList<Box2<String, GameTexture>> toLoad = new EArrayList<>();
	/**
	 * The path of the file that holds texture mapping data for the
	 * running game.
	 */
	private File textureFile;
	
	private boolean isLoaded = false;
	
	//---------
	// Methods
	//---------
	
	/** Deletes and removes each texture entry within this list. */
	private void unloadAll() {
		isLoaded = false;
		
		try {
			var it = textures.entrySet().iterator();
			while (it.hasNext()) {
				var e = it.next();
				var tex = e.getValue();
				tex.destroy(); //delete gl texture data
				it.remove();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//ensure 'textures' is empty
			if (!textures.isEmpty()) {
				//post a warning stating that the following were not removed (shouldn't be possible but you never know~)
				Envision.warn("The following textures could not be unloaded! " + textures.toString());
				textures.clear();
			}
		}
	}
	
	/** Registered and adds each texture to this list. */
	private void loadAll() {
		try {
			var it = toLoad.iterator();
			while (it.hasNext()) {
				var box = it.next();
				var name = box.getA();
				var tex = box.getB();
				//if the name or texture is somehow null, don't add
				if (name != null && tex != null) {
					//register the texture through the engine's texture system
					QoT.getTextureSystem().reg(tex);
					//add it to the game's texture map
					textures.put(name, tex);
				}
				it.remove();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//ensure 'toLoad' is empty
			if (toLoad.isNotEmpty()) {
				//post a warning stating that the following were not removed (shouldn't be possible but you never know~)
				Envision.warn("The following textures could not be loaded! " + toLoad.toString());
				toLoad.clear();
			}
		}
	}
	
	/**
	 * Begins loading the file containing the engine's texture
	 * mappings for the current game.
	 * 
	 * @throws Exception Thrown if any error occurs during reading,
	 *                   parsing, or linking
	 */
	@Override
	public void load() throws Exception {
		loadAll();
	}
	
	@Override
	public void unload() throws Exception {
		unloadAll();
	}
	
	private void loadFile() {
		if (!EFileUtil.fileExists(textureFile)) return;
		
		try (var reader = new LineReader(textureFile)) {
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean isLoaded() {
		return isLoaded;
	}
	
	@Override
	public File getResourceFile() {
		return textureFile;
	}
	
}
