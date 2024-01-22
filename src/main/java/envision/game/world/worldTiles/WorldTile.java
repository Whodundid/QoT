package envision.game.world.worldTiles;

import java.util.List;

import envision.Envision;
import envision.engine.loader.dtos.WorldTileDTO;
import envision.engine.registry.types.Sprite;
import envision.engine.registry.types.SpriteSheet;
import envision.game.component.ComponentBasedObject;
import envision.game.effects.Effect;
import envision.game.entities.Entity;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.world_tiles.GlobalTileList;
import qot.world_tiles.TileIDs;

public class WorldTile extends ComponentBasedObject implements Comparable<WorldTile> {
    
    //========
    // Fields
    //========
    
    /**
     * The human-readable name of this tile.
     */
    public String tileName;
    public String spriteSheetName;
    public EList<String> passiveEffectNames = EList.newList();
    public String movementModifierName;
    public String materialName;
    public String rotation;
    /** Whether or not the tile will draw flipped. */
    public boolean drawFlipped = false;
    public boolean isAnimated;
    public boolean isSideAnimated;
    /**
     * True if this tile should prevent entities from being able to enter the
     * boundaries of this tile.
     */
    public boolean blocksMovement = false;
    /**
     * True if this tile has additional vertical depth.
     * <p>
     * Note: Wall tiles do not inherently block movement as that modifier must
     * be specified separately.
     */
    public boolean isWall = false;
    public boolean randomizeWallHeight;
    public boolean randomizeRotation;
    public boolean randomizeDrawFlipped;
    /** If true, light cannot move through. */
    public boolean blocksLight = true;
    public int spriteStartIndex;
    public int spriteEndIndex;
    public int sideSpriteStartIndex;
    public int sideSpriteEndIndex;
    public int spriteAnimationInterval;
    public int sideSpriteAnimationInterval;
    public int minimapColor = 0xff000000;
    /** 0 by default -- really dark. */
    public int lightLevel = 0;
    /**
     * Represents the physical height of this tile raised above (or below) the
     * standard terrain level of zero (0.0).
     * 
     * <li>Note: This value is ignored unless 'isWall' is true
     * <li>Note: Standard height ranges from [-1.0, 1.0]
     */
    public float wallHeight = 0.0f;
    public float minWallHeight = -1.0f;
    public float maxWallHeight = 1.0f;
    

    
    
    //--------------------------------------------
    
    
    /** The metadata id of this tile. */
    public int meta;
    
    /**
     * The id of this tile which is primarily used for saving/loading world
     * data.
     */
    protected TileIDs id;
    
    /**
     * The primary texture of this tile.
     */
    //protected GameTexture tex;
    
    /**
     * If this tile is a wall, this is the texture that is drawn either above
     * or below the primary texture in order to give additional depth to the
     * terrain.
     */
    public Sprite sideTex;
    
    /**
     * For tiles that can have multiple variations, I.E. Grass, this number
     * keeps track of the total number of variations there are.
     */
    public int numVariants = 1;
    
    /**
     * Used to denote whether or not this tile's texture can be randomly
     * swapped for any of its provided variants. For instance, Grass can
     * randomly decide which texture it will display based on its provided
     * number of variants.
     */
    public boolean wildCardTexture = false;
    

    

    
    /**
     * A tile's material is used to determine a variety of things ranging from:
     * 
     * <ul>
     * <li>Walking sound
     * <li>Particle effects while walking
     * <li>Active modifiers such as:
     * <ul>
     * <li>Entity movement speed
     * <li>General area effects
     * <li>Passive status effects (I.E. being wet)
     * </ul>
     * </ul>
     */
    public TileMaterial material = new TileMaterial();
    
    public boolean hasSideBrightness = false;
    public int sideBrightness = 255;
    
    /** This tile's rotation. */
    public Rotation rotationDir = Rotation.UP;
    
    
    // Resource Assigned Fields
    
    private SpriteSheet spriteSheet;
    private List<Effect> passiveEffects;
    private TileMaterial tileMaterial;
    
    //==============
    // Constructors
    //==============
    
    public WorldTile() {}
    protected WorldTile(TileIDs idIn) {
        this(idIn, -1);
    }
    protected WorldTile(TileIDs idIn, int metaIn) {
        id = idIn;
        meta = metaIn;
        tileName = id.name;
        
        addComponent(new WorldTileRenderer(this));
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return tileName;
    }
    
    @Override
    public int compareTo(WorldTile in) {
        return Integer.compare(id.tileID, in.id.tileID);
    }
    
    @Override
    public int getInternalSaveID() { return id.tileID; }
    
    @Override
    public double getSortPoint() {
        return (worldY + 1) * Envision.theWorld.getTileHeight();
    }
    
    //=========
    // Methods
    //=========
    
    public WorldTileDTO toDTO() {
        WorldTileDTO dto = new WorldTileDTO();
        
        dto.setTileName(tileName);
        dto.setSpriteSheetName(spriteSheetName);
        dto.setPassiveEffectNames(passiveEffectNames);
        dto.setMaterialName(materialName);
        dto.setRotation((rotationDir != null) ? rotationDir.toString() : rotation);
        dto.setDrawFlipped(drawFlipped);
        dto.setAnimated(isAnimated);
        dto.setSideAnimated(isSideAnimated);
        dto.setBlocksMovement(blocksMovement);
        dto.setWall(isWall);
        dto.setRandomizeWallHeight(randomizeWallHeight);
        dto.setRandomizeRotation(randomizeRotation);
        dto.setRandomizeDrawFlipped(randomizeDrawFlipped);
        dto.setBlocksLight(blocksLight);
        dto.setSpriteStartIndex(sideSpriteStartIndex);
        dto.setSpriteEndIndex(sideSpriteEndIndex);
        dto.setSideSpriteStartIndex(sideSpriteStartIndex);
        dto.setSideSpriteEndIndex(sideSpriteEndIndex);
        dto.setSpriteAnimationInterval(spriteAnimationInterval);
        dto.setSideSpriteAnimationInterval(sideSpriteAnimationInterval);
        dto.setMinimapColor(minimapColor);
        dto.setLightLevel(lightLevel);
        dto.setWallHeight(wallHeight);
        dto.setMinWallHeight(minWallHeight);
        dto.setMaxWallHeight(maxWallHeight);
        
        return dto;
    }
    
    public static WorldTile fromDTO(WorldTileDTO dto) {
        WorldTile tile = new WorldTile();
        
        tile.tileName = dto.getTileName();
        tile.spriteSheetName = dto.getSpriteSheetName();
        tile.passiveEffectNames = EList.of(dto.getPassiveEffectNames());
        tile.materialName = dto.getMaterialName();
        tile.rotation = dto.getRotation();
        tile.drawFlipped = dto.drawFlipped();
        tile.isAnimated = dto.isAnimated();
        tile.isSideAnimated = dto.isSideAnimated();
        tile.blocksMovement = dto.isBlocksMovement();
        tile.blocksLight = dto.isBlocksLight();
        tile.isWall = dto.isWall();
        tile.randomizeWallHeight = dto.isRandomizeWallHeight();
        tile.randomizeRotation = dto.isRandomizeRotation();
        tile.randomizeDrawFlipped = dto.isRandomizeDrawFlipped();
        tile.spriteStartIndex = dto.getSpriteStartIndex();
        tile.spriteEndIndex = dto.getSpriteEndIndex();
        tile.sideSpriteStartIndex = dto.getSideSpriteStartIndex();
        tile.sideSpriteEndIndex = dto.getSideSpriteEndIndex();
        tile.spriteAnimationInterval = dto.getSpriteAnimationInterval();
        tile.sideSpriteAnimationInterval = dto.getSideSpriteAnimationInterval();
        tile.minimapColor = dto.getMinimapColor();
        tile.lightLevel = dto.getLightLevel();
        tile.wallHeight = dto.getWallHeight();
        tile.minWallHeight = dto.getMinWallHeight();
        tile.maxWallHeight = dto.getMaxWallHeight();
        
        return tile;
    }
    
    /**
     * Called every time the world updates.
     */
    public void onWorldTick() {
        if (isAnimated) animationHandler.onRenderTick();
    }
    
    /**
     * Called whenever a specific entity click on this tile.
     * 
     * @param entity The entity performing the action
     * @param button The mouse button clicking
     */
    public void onTileClicked(Entity entity, int button) {}
    
    public void randomizeValues() {
        if (randomizeDrawFlipped) drawFlipped = ERandomUtil.randomBool();
        if (randomizeWallHeight) wallHeight = ERandomUtil.getRoll(minWallHeight, maxWallHeight);
        if (randomizeRotation) rotationDir = Rotation.random();
    }
    
    public boolean hasVariation() {
        return false;
    }
    
    //=========
    // Getters
    //=========
    
    public SpriteSheet getSpriteSheet() { return spriteSheet; }
    public List<Effect> getPassiveEffects() { return passiveEffects; }
    public TileMaterial getTileMaterial() { return tileMaterial; }
    
    public boolean hasSprite() {
        return sprite != null;
    }
    public boolean blocksMovement() {
        return blocksMovement;
    }
    public boolean isWildCard() { return wildCardTexture; }
    public boolean isWall() { return isWall; }
    public double getWallHeight() { return wallHeight; }
    
    public int getID() { return id.tileID; }
    public String getName() { return tileName; }
    public TileMaterial getMaterial() { return material; }
    public int getMapColor() { return minimapColor; }
    public int getNumVariants() { return numVariants; }
    
    public String getAdditionalValues() {
        String r = "";
        
        r += (blocksMovement) ? "true " : "false ";
        r += (material != null) ? material.getMaterialName() : "";
        
        return r;
    }
    
    public int getLightLevel() { return lightLevel; }
    
    //=========
    // Setters
    //=========
    
    public void setSpriteSheet(SpriteSheet sheet) { spriteSheet = sheet; }
    public void setPassiveEffects(List<Effect> effects) { passiveEffects = effects; }
    public void setTileMaterial(TileMaterial material) { tileMaterial = material; }
    
    public WorldTile setSprite(Sprite texIn) {
        sprite = texIn;
        return this;
    }
    public WorldTile setSideSprite(Sprite texIn) {
        sideTex = texIn;
        return this;
    }
    public WorldTile setBlocksMovement(boolean val) {
        blocksMovement = val;
        return this;
    }
    public WorldTile setWall(boolean val) {
        isWall = val;
        return this;
    }
    public WorldTile setWildCard(boolean val) {
        wildCardTexture = val;
        return this;
    }
    
    public void setMiniMapColor(EColors colorIn) {
        setMiniMapColor(colorIn.intVal);
    }
    public void setMiniMapColor(int colorIn) { minimapColor = colorIn; }
    
    public void setWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public WorldTile setWorldPos(int x, int y) {
        worldX = x;
        worldY = y;
        startX = x * width;
        startY = y * height;
        endX = startX + width;
        endY = startY + height;
        midX = startX + width * 0.5;
        midY = startY + height * 0.5;
        return this;
    }
    
    public WorldTile setAdditional(String in) {
        if (in != null) {
            String[] values = in.split(" ");
            
            try {
                if (values.length >= 1) blocksMovement = Boolean.valueOf(values[0]);
                if (values.length >= 2) material = new TileMaterial();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }
    
    public void setLightLevel(int level) { lightLevel = level; }
    
    //================
    // Static Methods
    //================
    
    public static WorldTile getTileFromID(int id) {
        return getTileFromID(id, 0);
    }
    public static WorldTile getTileFromID(int id, int texNum) {
        return GlobalTileList.getTileFromID(id, texNum);
    }
    
    public static int getIDFromTile(WorldTile in) {
        return (in != null) ? in.getID() : -1;
    }
    
    public static WorldTile getTileFromName(String nameIn) {
        return getTileFromArgs(nameIn, null);
    }
    public static WorldTile getTileFromArgs(String nameIn, String additional) {
        if (nameIn != null) {
            WorldTile t = GlobalTileList.getTileFromName(nameIn);
            
            if (additional != null) {
                t.setAdditional(additional);
            }
            
            return t;
        }
        return null;
    }
    
    public static WorldTile randVariant(WorldTile in) {
        if (in == null) return null;
        try {
            WorldTile r = in.getClass().getConstructor().newInstance();
            //Sprite tex = in.getSprite();
            //			if (tex != null && tex.hasParent()) {
            //				r.setTexture(tex.getParent().getRandVariant());
            //			}
            return r;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }
    
    public WorldTile copy() {
        return null;
    }
    
    protected WorldTile copyFields(WorldTile from, WorldTile to) {
        to.id = from.id;
        to.tileName = from.tileName;
        to.sprite = from.sprite;
        to.sideTex = from.sideTex;
        to.numVariants = from.numVariants;
        to.blocksMovement = from.blocksMovement;
        to.wildCardTexture = from.wildCardTexture;
        to.isWall = from.isWall;
        to.wallHeight = from.wallHeight;
        to.material = from.material;
        to.worldX = from.worldX;
        to.worldY = from.worldY;
        to.hasSideBrightness = from.hasSideBrightness;
        to.sideBrightness = from.sideBrightness;
        //		to.entitiesOnTile = from.entitiesOnTile;
        //		to.entitiesAdding = from.entitiesAdding;
        //		to.entitiesRemoving = from.entitiesRemoving;
        to.rotationDir = from.rotationDir;
        to.drawFlipped = from.drawFlipped;
        
        return to;
    }
    
    public static WorldTile copy(WorldTile tile) {
        return tile.copy();
    }
    
}
