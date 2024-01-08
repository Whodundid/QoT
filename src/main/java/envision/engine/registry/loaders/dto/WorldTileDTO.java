package envision.engine.registry.loaders.dto;

public class WorldTileDTO {
    
    private String tileName;
    private String textureSheetName;
    private int textureIndex;
    
    public WorldTileDTO(String tileName, String textureSheetName, int textureIndex) {
        this.tileName = tileName;
        this.textureSheetName = textureSheetName;
        this.textureIndex = textureIndex;
    }
    
    public String getTileName() { return tileName; }
    public String getTextureSheetName() { return textureSheetName; }
    public int getTextureIndex() { return textureIndex; }
    
    public void setTileName(String value) { tileName = value; }
    public void setTextureSheetName(String value) { textureSheetName = value; }
    public void setTextureIndex(int value) { textureIndex = value; }
    
}
