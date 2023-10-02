package envision.engine.resourceLoaders.textures;

import java.util.List;

import eutil.datatypes.util.EList;

public record TextureSheet(String textureSheetName, List<ExportableTexture> textures) {
    
    public TextureSheet(String textureSheetName, EList<ExportableTexture> textures) {
        this(textureSheetName, textures.toArrayList());
    }
    
}
