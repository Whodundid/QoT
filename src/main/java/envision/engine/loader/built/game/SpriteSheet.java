package envision.engine.loader.built.game;

import org.joml.Vector2f;

import envision.engine.loader.dtos.IEngineResource;
import envision.engine.loader.dtos.game.SpriteSheetDTO;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_i;
import eutil.random.ERandomUtil;

public class SpriteSheet implements IEngineResource {
    
    //========
    // Fields
    //========
    
    private GameTexture baseTexture;
    private final EList<Sprite> sprites;
    private final int spriteWidth;
    private final int spriteHeight;
    private final int numSprites;
    private final int spacing;
    
    //==============
    // Constructors
    //==============
    
    public SpriteSheet() {
        baseTexture = null;
        sprites = EList.newList();
        spriteWidth = 0;
        spriteHeight = 0;
        numSprites = 0;
        spacing = 0;
    }
    
    public SpriteSheet(GameTexture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        sprites = EList.newList();
        baseTexture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numSprites = numSprites;
        this.spacing = spacing;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public SpriteSheetDTO toDto() {
        return null;
    }
    
    //=========
    // Methods
    //=========
    
    public void initSheet() {
        sprites.clear();
        
        float curX = 0;
        float curY = 0;
        
        final float texWidth = baseTexture.getWidth();
        final float texHeight = baseTexture.getHeight();
        
        for (int i = 0; i < numSprites; i++) {
            float topY = (curY + spriteHeight) / texHeight;
            float rightX = (curX + spriteWidth) / texWidth;
            float leftX = curX / texWidth;
            float botY = curY / texHeight;
            
            Vector2f[] texCoords = {
                new Vector2f(rightX, topY),
                new Vector2f(rightX, botY),
                new Vector2f(leftX, botY),
                new Vector2f(leftX, topY)
            };
            
            Sprite s = new Sprite(baseTexture, texCoords, new Dimension_i(curX, curY, curX + texWidth, curY + texWidth));
            sprites.add(s);
            
            curX += spriteWidth + spacing;
            if (curX >= baseTexture.getWidth()) {
                curX = 0;
                curY += spriteHeight + spacing;
            }
        }
    }
    
    //=========
    // Getters
    //=========
    
    public Sprite getSprite(int index) {
        return sprites.get(index);
    }
    
    public Sprite getRandom() {
        return sprites.getRandom();
    }
    
    public Sprite getRandom(int start, int end) {
         return sprites.get(ERandomUtil.getRoll(start, end));
    }
    
    public GameTexture getBaseTexture() {
        return baseTexture;
    }
    
    public int getNumberOfSprites() { return numSprites; }
    
}
