package envision.engine.loader.dtos.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import envision.engine.loader.dtos.IDataTransferObject;
import envision.game.world.worldTiles.WorldTile;
import eutil.assertions.Assertions;
import eutil.debug.Optional;
import eutil.strings.EToStringBuilder;

public record WorldTileDTO(
    String name,
    String tileMaterial,
    @Optional String rotation,
    boolean drawFlipped,
    boolean blocksMovement,
    boolean randStartSprite,
    boolean randStartSideSprite,
    boolean randTileHeight,
    boolean randRotation,
    boolean randDrawFlipped,
    int minimapColor,
    float tileHeight,
    float minTileHeight,
    float maxTileHeight,
    List<String> sprites,
    @Optional List<String> sideSprites,
    @Optional List<Float> spriteFrameTimings,
    @Optional List<Float> sideSpriteFrameTimings,
    @Optional List<String> passiveEffects
)
    implements IDataTransferObject<WorldTile>
{
    //==============
    // Constructors
    //==============
    
    /** Convenience constructor that doesn't require a list of sprites/frame timings. */
    public WorldTileDTO(
        String name,
        String tileMaterial,
        @Optional String rotation,
        boolean drawFlipped,
        boolean blocksMovement,
        boolean randStartSprite,
        boolean randStartSideSprite,
        boolean randTileHeight,
        boolean randRotation,
        boolean randDrawFlipped,
        int minimapColor,
        float tileHeight,
        float minTileHeight,
        float maxTileHeight,
        String sprite,
        @Optional String sideSprite,
        @Optional List<String> passiveEffects
    ){
        this(name,
             tileMaterial,
             rotation,
             drawFlipped,
             blocksMovement,
             randStartSprite,
             randStartSideSprite,
             randTileHeight,
             randRotation,
             randDrawFlipped,
             minimapColor,
             tileHeight,
             minTileHeight,
             maxTileHeight,
             wrap(sprite),
             wrap(sideSprite),
             EMPTY_LIST,
             EMPTY_LIST,
             passiveEffects
        );
    }
    
    public WorldTileDTO(
        String name,
        String tileMaterial,
        @Optional String rotation,
        boolean drawFlipped,
        boolean blocksMovement,
        boolean randStartSprite,
        boolean randStartSideSprite,
        boolean randTileHeight,
        boolean randRotation,
        boolean randDrawFlipped,
        int minimapColor,
        float tileHeight,
        float minTileHeight,
        float maxTileHeight,
        List<String> sprites,
        @Optional List<String> sideSprites,
        @Optional List<Float> spriteFrameTimings,
        @Optional List<Float> sideSpriteFrameTimings,
        @Optional List<String> passiveEffects
    ){
        // make sure every value has a non-null value
        Assertions.assertNotNull(name, tileMaterial);
        
        if (sprites == null) sprites = new ArrayList<>();
        if (sideSprites == null) sideSprites = new ArrayList<>();
        if (spriteFrameTimings == null) spriteFrameTimings = new ArrayList<>();
        if (sideSpriteFrameTimings == null) sideSpriteFrameTimings = new ArrayList<>();
        if (passiveEffects == null) passiveEffects = new ArrayList<>();
        
        // if the list values are not strictly ArrayList types, make them into ArrayLists
//        if (!(sprites instanceof ArrayList<String>)) sprites = new ArrayList<>(sprites);
//        if (!(sideSprites instanceof ))
        
        this.name = name;
        this.tileMaterial = tileMaterial;
        this.rotation = rotation;
        this.drawFlipped = drawFlipped;
        this.blocksMovement = blocksMovement;
        this.randStartSprite = randStartSprite;
        this.randStartSideSprite = randStartSideSprite;
        this.randTileHeight = randTileHeight;
        this.randRotation = randRotation;
        this.randDrawFlipped = randDrawFlipped;
        this.minimapColor = minimapColor;
        this.tileHeight = tileHeight;
        this.minTileHeight = minTileHeight;
        this.maxTileHeight = maxTileHeight;
        this.sprites = sprites;
        this.sideSprites = sideSprites;
        this.spriteFrameTimings = spriteFrameTimings;
        this.sideSpriteFrameTimings = sideSpriteFrameTimings;
        this.passiveEffects = passiveEffects;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        EToStringBuilder sb = new EToStringBuilder(this);
        sb.a("tileName", name);
        sb.a("tileMaterial", tileMaterial);
        sb.a("rotation", rotation);
        sb.a("drawFlipped", drawFlipped);
        sb.a("blocksMovement", blocksMovement);
        sb.a("randStartSprite", randStartSprite);
        sb.a("randStartSideSprite", randStartSideSprite);
        sb.a("randTileHeight", randTileHeight);
        sb.a("randRotation", randRotation);
        sb.a("randDrawFlipped", randDrawFlipped);
        sb.a("minimapColor", minimapColor);
        sb.a("tileHeight", tileHeight);
        sb.a("minTileHeight", minTileHeight);
        sb.a("maxTileHeight", maxTileHeight);
        sb.a("sprites", sprites);
        sb.a("sideSprites", sideSprites);
        sb.a("spriteFrameTimings", spriteFrameTimings);
        sb.a("sideSpriteFrameTimings", sideSpriteFrameTimings);
        sb.a("passiveEffects", passiveEffects);
        return sb.toString();
    }
    
    @Override
    public WorldTile fromDto() {
        return new WorldTile(this);
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    private static <E> List<E> wrap(E... vals) {
        List<E> list = new ArrayList<>(vals.length);
        for (int i = 0; i < vals.length; i++) {
            list.add(vals[i]);
        }
        return Collections.unmodifiableList(list);
    }
    
    private static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList());

}
