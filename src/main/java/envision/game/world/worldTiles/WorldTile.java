package envision.game.world.worldTiles;

import java.util.List;

import envision.Envision;
import envision.engine.loader.built.game.Effect;
import envision.engine.loader.built.game.Entity;
import envision.engine.loader.built.game.Sprite;
import envision.engine.loader.dtos.IEngineResource;
import envision.engine.loader.dtos.game.WorldTileDTO;
import envision.game.component.ComponentBasedObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.world_tiles.GlobalTileList;
import qot.world_tiles.TileIDs;

public class WorldTile extends ComponentBasedObject implements Comparable<WorldTile>, IEngineResource {
    
    //========
    // Fields
    //========
    
    /** The human-readable name of this tile. */
    public String name;
    public String tileMaterialName;
    public String rotation;
    
    /** Whether or not the tile will draw flipped. */
    public boolean drawFlipped = false;
    /**
     * True if this tile should prevent entities from being able to enter the
     * boundaries of this tile.
     */
    public boolean blocksMovement = false;
    public boolean randomizeStartSprite;
    public boolean randomizeStartSideSprite;
    public boolean randomizeTileHeight;
    public boolean randomizeRotation;
    public boolean randomizeDrawFlipped;
    
    public int minimapColor = 0xff000000;
    
    /**
     * Represents the physical height of this tile raised above (or below) the
     * standard terrain level of zero (0.0).
     * 
     * <li>Note: Standard height ranges from [-1.0, 1.0]
     */
    public float tileHeight = 0.0f;
    public float minTileHeight = -1.0f;
    public float maxTileHeight = 1.0f;

    public EList<String> sprites = EList.newList();
    public EList<String> sideSprites = EList.newList();
    public EList<Float> spriteFrameTimings = EList.newList();
    public EList<Float> sideSpriteFrameTimings = EList.newList();
    public EList<String> passiveEffectNames = EList.newList();
    
    
    
    //--------------------------------------------
    
    
    /** The metadata id of this tile. */
    @Deprecated
    public int meta;
    
    /**
     * The id of this tile which is primarily used for saving/loading world
     * data.
     */
    @Deprecated
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
    @Deprecated
    public Sprite sideTex;
    
    /**
     * For tiles that can have multiple variations, I.E. Grass, this number
     * keeps track of the total number of variations there are.
     */
    @Deprecated
    public int numVariants = 1;
    
    /**
     * Used to denote whether or not this tile's texture can be randomly
     * swapped for any of its provided variants. For instance, Grass can
     * randomly decide which texture it will display based on its provided
     * number of variants.
     */
    @Deprecated
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
    
    @Deprecated
    public boolean hasSideBrightness = false;
    @Deprecated
    public int sideBrightness = 255;
    
    /** This tile's rotation. */
    public Rotation rotationDir = Rotation.UP;
    
    
    // Resource Assigned Fields
    
    private List<Effect> passiveEffects;
    
    //==============
    // Constructors
    //==============
    
    public WorldTile() {}
    @Deprecated
    protected WorldTile(TileIDs idIn) {
        this(idIn, -1);
    }
    @Deprecated
    protected WorldTile(TileIDs idIn, int metaIn) {
        id = idIn;
        meta = metaIn;
        name = id.name;
        
        addComponent(new WorldTileRenderer(this));
    }
    
    public WorldTile(WorldTileDTO dto) {
        name = dto.name();
        tileMaterialName = dto.tileMaterial();
        rotation = dto.rotation();
        drawFlipped = dto.drawFlipped();
        blocksMovement = dto.blocksMovement();
        randomizeTileHeight = dto.randTileHeight();
        randomizeRotation = dto.randRotation();
        randomizeDrawFlipped = dto.randDrawFlipped();
        minimapColor = dto.minimapColor();
        tileHeight = dto.tileHeight();
        minTileHeight = dto.minTileHeight();
        maxTileHeight = dto.maxTileHeight();
        passiveEffectNames = EList.of(dto.passiveEffects());
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    @Deprecated
    public int compareTo(WorldTile in) {
        return Integer.compare(id.tileID, in.id.tileID);
    }
    
    @Override
    @Deprecated
    public int getInternalSaveID() { return id.tileID; }
    
    @Override
    public WorldTileDTO toDto() {
        return new WorldTileDTO(name, tileMaterialName, (rotationDir != null) ? rotationDir.toString() : rotation,
                                drawFlipped, blocksMovement, randomizeStartSprite, randomizeStartSideSprite,
                                randomizeTileHeight, randomizeRotation, randomizeDrawFlipped, minimapColor, tileHeight,
                                minTileHeight, maxTileHeight, sprites, sideSprites, spriteFrameTimings,
                                sideSpriteFrameTimings, passiveEffectNames);
    }
    
    @Override
    public double getSortPoint() {
        final double p = (worldY) * Envision.theWorld.getTileHeight();
        return p;
    }    
    //=========
    // Methods
    //=========
    
    /**
     * Called every time the world updates.
     */
    public void onWorldTick() {
        //if (isAnimated) animationHandler.onRenderTick((long) Envision.getDeltaTime());
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
        if (randomizeTileHeight) tileHeight = ERandomUtil.getRoll(minTileHeight, maxTileHeight);
        if (randomizeRotation) rotationDir = Rotation.random();
    }    
    public boolean hasSprite() { return sprite != null; }

    //=========
    // Getters
    //=========
    
    public List<Effect> getPassiveEffects() { return passiveEffects; }
    
    public boolean blocksMovement() { return blocksMovement; }
    @Deprecated
    public boolean isWildCard() { return wildCardTexture; }
    public double getWallHeight() { return tileHeight; }
    
    @Deprecated
    public int getID() { return id.tileID; }
    public String getName() { return name; }
    public TileMaterial getMaterial() { return material; }
    public int getMapColor() { return minimapColor; }
    @Deprecated
    public int getNumVariants() { return numVariants; }
    
    @Deprecated
    public String getAdditionalValues() {
        String r = "";
        
        r += (blocksMovement) ? "true " : "false ";
        r += (material != null) ? material.getMaterialName() : "";
        
        return r;
    }
    
    //=========
    // Setters
    //=========
    
    public void setPassiveEffects(List<Effect> effects) {
        passiveEffects = effects;
    }
    
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
    @Deprecated
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
    
    @Deprecated
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
    
    //================
    // Static Methods
    //================
    
    @Deprecated
    public static WorldTile getTileFromID(int id) {
        return getTileFromID(id, 0);
    }
    @Deprecated
    public static WorldTile getTileFromID(int id, int texNum) {
        return GlobalTileList.getTileFromID(id, texNum);
    }
    @Deprecated
    public static int getIDFromTile(WorldTile in) {
        return (in != null) ? in.getID() : -1;
    }
    @Deprecated
    public static WorldTile getTileFromName(String nameIn) {
        return getTileFromArgs(nameIn, null);
    }
    @Deprecated
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
    @Deprecated
    public static WorldTile randVariant(WorldTile in) {
        if (in == null) return null;
        try {
            WorldTile r = in.getClass().getConstructor().newInstance();
            //Sprite tex = in.getSprite();
            //            if (tex != null && tex.hasParent()) {
            //                r.setTexture(tex.getParent().getRandVariant());
            //            }
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
        to.name = from.name;
        to.sprite = from.sprite;
        to.sideTex = from.sideTex;
        to.numVariants = from.numVariants;
        to.blocksMovement = from.blocksMovement;
        to.wildCardTexture = from.wildCardTexture;
        //to.isWall = from.isWall;
        to.tileHeight = from.tileHeight;
        to.material = from.material;
        to.worldX = from.worldX;
        to.worldY = from.worldY;
        to.hasSideBrightness = from.hasSideBrightness;
        to.sideBrightness = from.sideBrightness;
        //        to.entitiesOnTile = from.entitiesOnTile;
        //        to.entitiesAdding = from.entitiesAdding;
        //        to.entitiesRemoving = from.entitiesRemoving;
        to.rotationDir = from.rotationDir;
        to.drawFlipped = from.drawFlipped;
        
        return to;
    }
    
    public static WorldTile copy(WorldTile tile) {
        return tile.copy();
    }
    
}
