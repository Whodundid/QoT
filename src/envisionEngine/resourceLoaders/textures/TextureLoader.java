package envisionEngine.resourceLoaders.textures;

import java.io.File;
import java.util.HashMap;

import envisionEngine.Envision;
import envisionEngine.renderEngine.textureSystem.GameTexture;
import envisionEngine.renderEngine.textureSystem.TextureSystem;
import envisionEngine.resourceLoaders.IResourceManager;
import eutil.datatypes.BoxList;
import eutil.file.EFileUtil;
import eutil.file.LineReader;

/**
 * Manages an active record file containing all of the textures
 * known to the engine.
 * 
 * @author Hunter Bragg
 */
public class TextureLoader implements IResourceManager {
	
	/** The active record of all loaded game textures. Maps name to texture. */
	private final HashMap<String, GameTexture> loadedTextures = new HashMap<>();
	/** Keeps track of textures that have been parsed but not yet loaded. */
	private final BoxList<String, GameTexture> toLoad = new BoxList<>();
	/** The path of the file that holds texture mapping data for the running game. */
	private File textureFile;
	
	private boolean isLoaded = false;
	
	//==============
	// Constructors
	//==============
	
	public TextureLoader(String textureFilePath) { this(new File(textureFilePath)); }
	public TextureLoader(File textureFileIn) {
		textureFile = textureFileIn;
	}
	
	//===========
	// Overrides
	//===========
	
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
	
	@Override
	public boolean isLoaded() {
		return isLoaded;
	}
	
	@Override
	public File getResourceFile() {
		return textureFile;
	}
	
	@Override
	public boolean reload() {
		try {
			unload();
			load();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//---------
	// Methods
	//---------
	
	/** Deletes and removes each texture entry within this list. */
	private void unloadAll() {
		isLoaded = false;
		
		try {
			var it = loadedTextures.entrySet().iterator();
			while (it.hasNext()) {
				var e = it.next();
				var tex = e.getValue();
				//delete gl texture data
				TextureSystem.getInstance().destroyTexture(tex);
				it.remove();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//ensure 'textures' is empty
			if (!loadedTextures.isEmpty()) {
				//post a warning stating that the following were not removed (shouldn't be possible but you never know~)
				Envision.warn("The following textures could not be unloaded! " + loadedTextures.toString());
				loadedTextures.clear();
			}
		}
	}
	
	/** Registered and adds each texture to this list. */
	private void loadAll() {
		try {
			loadTextureFile();
			
			var it = toLoad.iterator();
			while (it.hasNext()) {
				var box = it.next();
				var name = box.getA();
				var tex = box.getB();
				//if the name or texture is somehow null, don't add
				if (name != null && tex != null) {
					//register the texture through the engine's texture system
					TextureSystem.getInstance().registerTexture(tex);
					//add it to the game's texture map
					loadedTextures.put(name, tex);
				}
				it.remove();
			}
			
			isLoaded = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//ensure 'toLoad' is empty
			if (toLoad.isNotEmpty()) {
				//post a warning stating that the following were not removed (shouldn't be possible but you never know~)
				Envision.warnf("The following textures could not be loaded! {}", toLoad.getAVals());
				toLoad.clear();
			}
		}
	}
	
	private void loadTextureFile() {
		if (!EFileUtil.fileExists(textureFile)) return;
		
		try (var reader = new LineReader(textureFile)) {
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				
				String[] parts = line.split("|");
				for (int i = 0; i < parts.length; i++) {
					parts[i] = parts[i].trim();
				}
				
				if (parts.length < 2) {
					Envision.debugf("Failure to parse line: {}", line);
					continue;
				}
				
				String textureName = parts[0];
				String texturePath = parts[1];
				
				GameTexture toCreate = new GameTexture(texturePath);
				toLoad.add(textureName, toCreate);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//=========
	// Setters
	//=========
	
	public void setTextureFile(String pathIn) { setTextureFile(pathIn); }
	public void setTextureFile(File textureFileIn) { textureFile = textureFileIn; }
	
}
