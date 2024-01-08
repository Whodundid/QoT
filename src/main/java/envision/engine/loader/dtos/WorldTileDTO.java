package envision.engine.loader.dtos;

public class WorldTileDTO {
    
    //========
    // Fields
    //========
    
    private String tileName;
    private String textureSheetName;
    private int textureIndex;
    
    //==============
    // Constructors
    //==============
    
    public WorldTileDTO(String tileName, String textureSheetName, int textureIndex) {
        this.tileName = tileName;
        this.textureSheetName = textureSheetName;
        this.textureIndex = textureIndex;
    }
    
    //=========
    // Getters
    //=========
    
    public String getTileName() { return tileName; }
    public String getTextureSheetName() { return textureSheetName; }
    public int getTextureIndex() { return textureIndex; }
    
    //=========
    // Setters
    //=========
    
    public void setTileName(String value) { tileName = value; }
    public void setTextureSheetName(String value) { textureSheetName = value; }
    public void setTextureIndex(int value) { textureIndex = value; }
    
}
