package envision.engine.loader.dtos;

import java.util.ArrayList;
import java.util.List;

import eutil.strings.EToStringBuilder;

public class WorldTileDTO {
    
    //========
    // Fields
    //========
    
    private String tileName;
    private String spriteSheetName;
    private List<String> passiveEffectNames = new ArrayList<>();
    private String materialName;
    private String rotation;
    private boolean drawFlipped;
    private boolean isAnimated;
    private boolean isSideAnimated;
    private boolean blocksMovement;
    private boolean blocksLight;
    private boolean isWall;
    private boolean randomizeWallHeight;
    private boolean randomizeRotation;
    private boolean randomizeDrawFlipped;
    private int spriteStartIndex;
    private int spriteEndIndex;
    private int sideSpriteStartIndex;
    private int sideSpriteEndIndex;
    private int spriteAnimationInterval;
    private int sideSpriteAnimationInterval;
    private int minimapColor;
    private int lightLevel;
    private float wallHeight;
    private float minWallHeight;
    private float maxWallHeight;
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        EToStringBuilder sb = new EToStringBuilder(this);
        sb.a("tileName", tileName);
        sb.a("spriteSheetName", spriteSheetName);
        sb.a("passiveEffectNames", passiveEffectNames);
        sb.a("materialName", materialName);
        sb.a("rotation", rotation);
        sb.a("drawFlipped", drawFlipped);
        sb.a("isAnimated", isAnimated);
        sb.a("isSideAnimated", isSideAnimated);
        sb.a("blocksMovement", blocksMovement);
        sb.a("blocksLight", blocksLight);
        sb.a("isWall", isWall);
        sb.a("randomizWallHeight", randomizeWallHeight);
        sb.a("randomizeRotation", randomizeRotation);
        sb.a("randomizeDrawFlipped", randomizeDrawFlipped);
        sb.a("spriteStartIndex", spriteStartIndex);
        sb.a("spriteEndIndex", spriteEndIndex);
        sb.a("sideSpriteStartIndex", sideSpriteStartIndex);
        sb.a("sideSpriteEndIndex", sideSpriteEndIndex);
        sb.a("spriteAnimationInterval", spriteAnimationInterval);
        sb.a("sideSpriteAnimationInterval", sideSpriteAnimationInterval);
        sb.a("minimapColor", minimapColor);
        sb.a("lightLevel", lightLevel);
        sb.a("wallHeight", wallHeight);
        sb.a("minWallHeight", minWallHeight);
        sb.a("maxWallHeight", maxWallHeight);
        return sb.toString();
    }
    
    //=========
    // Getters
    //=========

    public String getTileName() { return tileName; }
    public String getSpriteSheetName() { return spriteSheetName; }
    public List<String> getPassiveEffectNames() { return passiveEffectNames; }
    public String getMaterialName() { return materialName; }
    public String getRotation() { return rotation; }
    public boolean drawFlipped() { return drawFlipped; }
    public boolean isAnimated() { return isAnimated; }
    public boolean isSideAnimated() { return isSideAnimated; }
    public boolean isBlocksMovement() { return blocksMovement; }
    public boolean isBlocksLight() { return blocksLight; }
    public boolean isWall() { return isWall; }
    public boolean isRandomizeWallHeight() { return randomizeWallHeight; }
    public boolean isRandomizeRotation() { return randomizeRotation; }
    public boolean isRandomizeDrawFlipped() { return randomizeDrawFlipped; }
    public int getSpriteStartIndex() { return spriteStartIndex; }
    public int getSpriteEndIndex() { return spriteEndIndex; }
    public int getSideSpriteStartIndex() { return sideSpriteStartIndex; }
    public int getSideSpriteEndIndex() { return sideSpriteEndIndex; }
    public int getSpriteAnimationInterval() { return spriteAnimationInterval; }
    public int getSideSpriteAnimationInterval() { return sideSpriteAnimationInterval; }
    public int getMinimapColor() { return minimapColor; }
    public int getLightLevel() { return lightLevel; }
    public float getWallHeight() { return wallHeight; }
    public float getMinWallHeight() { return minWallHeight; }
    public float getMaxWallHeight() { return maxWallHeight; }
    
    //=========
    // Setters
    //=========
    
    public void setTileName(String tileName) { this.tileName = tileName; }
    public void setSpriteSheetName(String spriteSheetName) { this.spriteSheetName = spriteSheetName; }
    public void setPassiveEffectNames(List<String> effectNamesIn) { this.passiveEffectNames = (effectNamesIn != null) ? new ArrayList<>(effectNamesIn) : new ArrayList<>(); }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public void setRotation(String rotation) { this.rotation = rotation; }
    public void setDrawFlipped(boolean drawFlipped) { this.drawFlipped = drawFlipped; }
    public void setAnimated(boolean isAnimated) { this.isAnimated = isAnimated; }
    public void setSideAnimated(boolean isSideAnimated) { this.isSideAnimated = isSideAnimated; }
    public void setBlocksMovement(boolean blocksMovement) { this.blocksMovement = blocksMovement; }
    public void setBlocksLight(boolean blocksLight) { this.blocksLight = blocksLight; }
    public void setWall(boolean isWall) { this.isWall = isWall; }
    public void setRandomizeWallHeight(boolean randomizeWallHeight) { this.randomizeWallHeight = randomizeWallHeight; }
    public void setRandomizeRotation(boolean randomizeRotation) { this.randomizeRotation = randomizeRotation; }
    public void setRandomizeDrawFlipped(boolean randomizeDrawFlipped) { this.randomizeDrawFlipped = randomizeDrawFlipped; }
    public void setSpriteStartIndex(int spriteStartIndex) { this.spriteStartIndex = spriteStartIndex; }
    public void setSpriteEndIndex(int spriteEndIndex) { this.spriteEndIndex = spriteEndIndex; }
    public void setSideSpriteStartIndex(int sideSpriteStartIndex) { this.sideSpriteStartIndex = sideSpriteStartIndex; }
    public void setSideSpriteEndIndex(int sideSpriteEndIndex) { this.sideSpriteEndIndex = sideSpriteEndIndex; }
    public void setSpriteAnimationInterval(int spriteAnimationInterval) { this.spriteAnimationInterval = spriteAnimationInterval; }
    public void setSideSpriteAnimationInterval(int interval) { this.sideSpriteAnimationInterval = interval; }
    public void setMinimapColor(int minimapColor) { this.minimapColor = minimapColor; }
    public void setLightLevel(int lightLevel) { this.lightLevel = lightLevel; }
    public void setWallHeight(float wallHeight) { this.wallHeight = wallHeight; }
    public void setMinWallHeight(float minWallHeight) { this.minWallHeight = minWallHeight; }
    public void setMaxWallHeight(float maxWallHeight) { this.maxWallHeight = maxWallHeight; }
    
}
