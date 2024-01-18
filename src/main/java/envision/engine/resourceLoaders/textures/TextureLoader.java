package envision.engine.resourceLoaders.textures;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import envision.Envision;
import envision.engine.loader.dtos.GameTextureDTO;
import envision.engine.loader.dtos.SpriteDTO;
import envision.engine.loader.dtos.TextureSheetDTO;
import envision.engine.registry.IResourceManager;
import envision.engine.registry.types.Sprite;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import qot.settings.QoTSettings;

/**
 * Manages an active record file containing all of the textures
 * known to the engine.
 * 
 * @author Hunter Bragg
 */
public class TextureLoader implements IResourceManager {
	
    //========
    // Fields
    //========
    
	/** The active record of all loaded game textures. Maps name to texture. */
	private final Map<String, GameTexture> loadedTextures = new HashMap<>();
	/** Keeps track of textures that have been parsed but not yet loaded. */
	private final BoxList<String, GameTexture> toLoad = new BoxList<>();
	/** The path of the file that holds texture mapping data for the running game. */
	private File textureFile;
	
	private boolean isLoaded = false;
	
	//==============
	// Constructors
	//==============
	
	public TextureLoader(String textureFilePath) {
	    this(new File(textureFilePath));
	}
	
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
	public void save() throws Exception {
	    saveTextureFile();
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
	
	//=========
    // Methods
    //=========
	
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
		    var texSystem = TextureSystem.getInstance();
			loadTextureFile();
			
			var it = toLoad.iterator();
			while (it.hasNext()) {
				var box = it.next();
				var name = box.getA();
				var tex = box.getB();
				
				//if the name or texture is somehow null, don't add
				if (name != null && tex != null && !texSystem.isTextureRegistered(tex)) {
				    System.out.println("LOADING: " + tex);
					//register the texture through the engine's texture system
				    texSystem.registerTexture(tex);
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
		if (isLoaded) return;
		
		try {
		    ObjectMapper mapper = new ObjectMapper();
		    
		    TextureSheetDTO sheet = mapper.readValue(textureFile, TextureSheetDTO.class);
		    GameTextureDTO texture = sheet.texture();
		    List<SpriteDTO> sprites = sheet.sprites();
		    
		    toLoad.put(texture.name(), new GameTexture(QoTSettings.getResourcesDir() + "/" + texture.path()));
		    
		    
            //var parsedTextures = EList.of(mapper.readValue(textureFile, ExportableTexture[].class));
            
//            for (var t : sheet.textures()) {
//                GameTexture texture = new GameTexture(QoTSettings.getResourcesDir() + "/" + t.texturePath());
//                toLoad.put(t.textureName(), texture);
//            }
            
            isLoaded = true;
		}
        catch (StreamReadException e) {
            e.printStackTrace();
        }
        catch (DatabindException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void loadTextureFile(GameTextureDTO dto) {
	    
	}
	
	private EList<Sprite> parseSprites(List<SpriteDTO> dto) {
	    EList<Sprite> sprites = EList.newList();
	    
	    return sprites;
	}
	
	private void saveTextureFile() {
	    if (textureFile == null) return;
	    
	    try {
	        var texture = new GameTextureDTO("texture:baldric_walk_0", "textures/entities/walksheet.png");
	        List<SpriteDTO> sprites = EList.newList();
	        
	        for (int y = 0; y < 4; y++) {
	            int sy = y * 64;
                int ey = sy + 64;
                
	            for (int x = 0; x < 9; x++) {
	                int sx = x * 64;
	                int ex = sx + 64;
	                
	                switch (y) {
	                case 0: sprites.add(new SpriteDTO("sprite:baldric_walk_0_north_" + x, sx, sy, ex, ey)); break;
	                case 1: sprites.add(new SpriteDTO("sprite:baldric_walk_0_west_" + x, sx, sy, ex, ey)); break;
	                case 2: sprites.add(new SpriteDTO("sprite:baldric_walk_0_south_" + x, sx, sy, ex, ey)); break;
	                case 3: sprites.add(new SpriteDTO("sprite:baldric_walk_0_east_" + x, sx, sy, ex, ey)); break;
	                default:
	                    throw new RuntimeException("WhAT!");
	                }
	            }
	        }
	        
//            var textures = EList.of(new GameTextureDTO("Grass 0", "textures/world/nature/grass/grass_0.png"),
//                                    new GameTextureDTO("Leafy Grass 0", "textures/world/nature/grass/leafy_grass_0.png"));
            
//            List<ExportableTexture> textures = new ArrayList<>();
//            textures.add(new ExportableTexture("Grass 0", "textures/world/nature/grass/grass_0.png"));
//            textures.add(new ExportableTexture("Leafy Grass 0", "textures/world/nature/grass/leafy_grass_0.png"));
            
            TextureSheetDTO sheet = new TextureSheetDTO("sheet:baldric_walk_0", texture, sprites);
            
//            WorldTileDTO entity = new WorldTileDTO("The Cool Tile", sheet.name(), 1);
            
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(textureFile, sheet);
            //mapper.writerWithDefaultPrettyPrinter().writeValue(textureFile, sheet);
            //mapper.writerWithDefaultPrettyPrinter().writeValue(textureFile, textures);
            //Files.createFile(textureFile.toPath());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	//=========
	// Getters
	//=========
	
	public EList<GameTexture> getLoadedTextures() {
	    return EList.wrap(loadedTextures.values().stream().toList());
	}
	
	public EList<String> getLoadedTextureNames() {
	    return EList.wrap(loadedTextures.keySet().stream().toList());
	}
	
	public GameTexture getTexture(String name) {
	    return loadedTextures.get(name);
	}
	
	//=========
	// Setters
	//=========
	
	public void setTextureFile(String pathIn) { setTextureFile(pathIn); }
	public void setTextureFile(File textureFileIn) { textureFile = textureFileIn; }
	
}
