package envision.engine.loader.built.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import envision.Envision;
import envision.engine.EngineSettings;
import envision.engine.internal.rendering.textureSystem.TextureSystem;
import envision.engine.loader.dtos.IEngineResource;
import envision.engine.loader.dtos.game.TextureResourceDTO;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.random.ERandomUtil;

/**
 * 
 * 
 * @author Hunter
 */
public class GameTexture implements IEngineResource {
    
    @Deprecated
    public static final String rDir = EngineSettings.RESOURCES_DIR.toString();
    @Deprecated
    public static final String tDir = rDir + "/textures/";
    
    //========
    // Fields
    //========
    
    private String name;
    /** If this texture is a child variant, this is the parent for which it relates. If this value is null, there is no parent and this is not a child texture. */
    @Deprecated
    private GameTexture parentTexture;
    /** A collection of this texture's child variants. */
    @Deprecated
    protected BoxList<GameTexture, Integer> children = new BoxList<>();
    /** When calling for a random child ID, this percent will indicate the likely hood this specific child will be selected. */
    @Deprecated
    private int percent = -1;
    /** The internal use ID for this texture object. -1 indicates an unregistered texture ID. -- Assigned dynamically through texture registration. */
    private int textureID = -1;
    /** If this is a child texture for a set of textures, I.E. grass with its multiple variants, this child ID indicates which specific variant is being refered internally. */
    @Deprecated
    private int childID = 0;
    /** The file system path where this texture exists. */
    private String filePath;
    /** The texture's width in pixels. -- Assigned dynamically through texture registration. */
    private int width = -1;
    /** The texture's height in pixels. -- Assigned dynamically through texture registration. */
    private int height = -1;
    /** The texture's bytes. -- Assigned dynamically through texture registration. */
    private ByteBuffer imageBytes;
    /** True if this texture ID has been deleted through OpenGL. */
    private boolean destroyed = false;
    /** Min Filter property for when registering texture. */
    private int minFilter = GL11.GL_LINEAR;
    /** Mag Filter property for when registering texture. */
    private int magFilter = GL11.GL_NEAREST;
    
    private BufferedImage image;
    
    //==============
    // Constructors
    //==============
    
    @Deprecated
    public GameTexture(String filePathIn) { this(null, "", filePathIn); }
    @Deprecated
    public GameTexture(String basePath, String filePathIn) { this(null, basePath, filePathIn); }
    @Deprecated
    public GameTexture(GameTexture parent, String filePathIn) { this(parent, "", filePathIn); }
    @Deprecated
    public GameTexture(GameTexture parent, String basePath, String filePathIn) {
        parentTexture = parent;
        filePath = basePath + filePathIn;
    }
    
    @Deprecated
    public GameTexture(String filePathIn, int minFilterIn, int magFilterIn) { this(null, "", filePathIn, minFilterIn, magFilterIn); }
    @Deprecated
    public GameTexture(String basePath, String filePathIn, int minFilterIn, int magFilterIn) { this(null, basePath, filePathIn, minFilterIn, magFilterIn); }
    @Deprecated
    public GameTexture(GameTexture parent, String filePathIn, int minFilterIn, int magFilterIn) { this(parent, "", filePathIn, minFilterIn, magFilterIn); }
    @Deprecated
    public GameTexture(GameTexture parent, String basePath, String filePathIn, int minFilterIn, int magFilterIn) {
        parentTexture = parent;
        filePath = basePath + filePathIn;
        minFilter = minFilterIn;
        magFilter = magFilterIn;
    }
    
    @Deprecated
    public GameTexture(File fileIn) {
        filePath = fileIn.getAbsolutePath();
    }
    
    //----------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------
    
    public GameTexture(TextureResourceDTO dto) {
        name = dto.name();
        filePath = dto.filePath();
        
        if (dto.minFilter() != null) {
            switch (dto.minFilter()) {
            case "GL_NEAREST": minFilter = GL11.GL_NEAREST; break;
            case "GL_LINEAR": minFilter = GL11.GL_LINEAR; break;
            case "GL_NEAREST_MIPMAP_NEAREST": minFilter = GL11.GL_NEAREST_MIPMAP_NEAREST; break;
            case "GL_LINEAR_MIPMAP_NEAREST": minFilter = GL11.GL_LINEAR_MIPMAP_NEAREST; break;
            case "GL_NEAREST_MIPMAP_LINEAR": minFilter = GL11.GL_NEAREST_MIPMAP_LINEAR; break;
            case "GL_LINEAR_MIPMAP_LINEAR": minFilter = GL11.GL_LINEAR_MIPMAP_LINEAR; break;
            default:
                throw new IllegalArgumentException("Invalid GL Min filter value! Expected one of: " +
                                                   "[GL_NEAREST, GL_LINEAR, GL_NEAREST_MIPMAP_NEAREST, GL_LINEAR_MIPMAP_NEAREST, " +
                                                   "GL_NEAREST_MIPMAP_LINEAR, GL_LINEAR_MIPMAP_LINEAR]");
            }
        }
        
        if (dto.magFilter() != null) {
            switch (dto.magFilter()) {
            case "GL_NEAREST": magFilter = GL11.GL_NEAREST; break;
            case "GL_LINEAR": magFilter = GL11.GL_LINEAR; break;
            default:
                throw new IllegalArgumentException("Invalid GL Mag filter value! Expected one of: " +
                                                   "[GL_NEAREST, GL_LINEAR]");
            }
        }
    }
    
    public GameTexture(BufferedImage imageIn) {
        image = imageIn;
    }
    
    public GameTexture(BufferedImage imageIn, int minFilterIn, int magFilterIn) {
        image = imageIn;
        minFilter = minFilterIn;
        magFilter = magFilterIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public TextureResourceDTO toDto() {
        String min = switch (minFilter) {
        case GL11.GL_NEAREST -> "GL_NEAREST";
        case GL11.GL_LINEAR -> "GL_LINEAR";
        case GL11.GL_NEAREST_MIPMAP_NEAREST -> "GL_NEAREST_MIPMAP_NEAREST";
        case GL11.GL_NEAREST_MIPMAP_LINEAR -> "GL_NEAREST_MIPMAP_LINEAR";
        case GL11.GL_LINEAR_MIPMAP_NEAREST -> "GL_LINEAR_MIPMAP_NEAREST";
        case GL11.GL_LINEAR_MIPMAP_LINEAR -> "GL_LINEAR_MIPMAP_LINEAR";
        
        default -> throw new IllegalArgumentException("Invalid GL Min filter value! Expected one of: " +
                                                      "[GL_NEAREST, GL_LINEAR, GL_NEAREST_MIPMAP_NEAREST, GL_LINEAR_MIPMAP_NEAREST, " +
                                                      "GL_NEAREST_MIPMAP_LINEAR, GL_LINEAR_MIPMAP_LINEAR]");
        };
        
        String mag = switch (minFilter) {
        case GL11.GL_NEAREST -> "GL_NEAREST";
        case GL11.GL_LINEAR -> "GL_LINEAR";
        
        default -> throw new IllegalArgumentException("Invalid GL Mag filter value! Expected one of: " +
                                                      "[GL_NEAREST, GL_LINEAR]");
        };
        
        return new TextureResourceDTO(name, filePath, min, mag);
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    public void registerChildTextures(TextureSystem systemIn) {
        for (GameTexture t : children.getAVals()) {
            systemIn.registerTexture(t);
        }
    }    
    //=========
    // Methods
    //=========
    
    @Deprecated
    public GameTexture setChildID(int id) { childID = id; return this; }
    @Deprecated
    public GameTexture addChild(String path) { return addChild(new GameTexture(path)); }
    @Deprecated
    public GameTexture addChild(String base, String path) { return addChild(new GameTexture(base, path)); }
    @Deprecated
    public GameTexture addChild(final GameTexture in) { return addChild(in, -1); }
    @Deprecated
    public GameTexture addChild(final GameTexture in, final int percentage) {
        if (in != null) {
            in.parentTexture = this;
            children.add(in, percentage);
            in.setChildID(children.size());
        }
        return in;
    }
    
    public boolean destroy() {
        if (textureID != -1) {
            STBImage.stbi_image_free(imageBytes);
            GL11.glDeleteTextures(textureID);
            textureID = -1;
            destroyed = true;
            return true;
        }
        return false;
    }
    
    public boolean hasBeenRegistered() {
        return textureID != -1;
    }    
    //=========
    // Getters
    //=========
    
    public String getName() { return name; }
    @Deprecated
    public GameTexture getParent() { return parentTexture; }
    @Deprecated
    public EList<GameTexture> getChildren() { return children.getAVals(); }
    public int getTextureID() { return textureID; }
    @Deprecated
    public int getChildID() { return childID; }
    public String getFilePath() { return filePath; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public ByteBuffer getImageBytes() { return imageBytes; }
    public int getMinFilter() { return minFilter; }
    public int getMagFilter() { return magFilter; }
    public boolean hasBeenDestroyed() { return destroyed; }
    @Deprecated
    public boolean hasParent() { return parentTexture != null; }
    public BufferedImage getBufferedImage() { return image; }
    
    @Deprecated
    public EList<GameTexture> getAllChildren() {
        EList<GameTexture> found = EList.newList();
        EList<GameTexture> withChildren = EList.newList();
        EList<GameTexture> workList = EList.newList();
        
        children.getAVals().forEach(c -> {
            found.add(c);
            if (c.children.isNotEmpty()) withChildren.add(c);
        });
        workList.addAll(withChildren);
        
        while (workList.isNotEmpty()) {
            found.addAll(workList);
            
            withChildren.clear();
            workList.filterForEach(c -> c.children.getAVals().isNotEmpty(), withChildren::add);
            
            workList.clear();
            withChildren.forEach(c -> workList.addAll(c.children.getAVals()));
        }
        
        return found;
    }
    
    @Deprecated
    private GameTexture getRandChild() {
        int pos = ERandomUtil.getRoll(0, children.size() - 1);
        return children.getA(pos);
    }
    
    @Deprecated
    public void initVariantPercents() {
        BoxList<GameTexture, Integer> total = new BoxList(children);
        total.add(this, percent);
        //for (Box2<GameTexture, Integer> t : total) {
            //int amount = t.getB();
        //}
    }
    
    @Deprecated
    public GameTexture getRandVariant() {
        return (children.isNotEmpty() && ERandomUtil.roll(0, 0, 1)) ? getRandChild() : this;
    }
    
    @Deprecated
    public GameTexture getChild(int id) {
        return children.getA(id);
    }
    
    public BufferedImage convertToBufferedImage() {
        return convertToBufferedImage(this);
    }
    
    public static BufferedImage convertToBufferedImage(GameTexture texture) {
        int tWidth = texture.getWidth();
        int tHeight = texture.getHeight();
        
        class Annoying {
            int format;
            
            int getFormat(GameTexture texture) {
                TextureSystem.getInstance().bind(texture);
                var context = Envision.getRenderEngine().getRenderingContext();
                context.call(() -> format = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT));
                return format;
            }
        }
        
        
        TextureSystem.getInstance().bind(texture);
        int format = new Annoying().getFormat(texture);
        int channels = (format == GL11.GL_RGB) ? 3 : 4;
        
        ByteBuffer buffer = BufferUtils.createByteBuffer(tWidth * tHeight * channels);
        BufferedImage image = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_INT_ARGB);
        
        var context = Envision.getRenderEngine().getRenderingContext();
        context.call(() -> {
            TextureSystem.getInstance().bind(texture);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, format, GL11.GL_UNSIGNED_BYTE, buffer);
        });
        
        for (int x = 0; x < tWidth; ++x) {
            for (int y = 0; y < tHeight; ++y) {
                int i = (x + y * tWidth) * channels;
                
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                int a = 255;
                if (channels == 4) a = buffer.get(i + 3) & 0xFF;
                
                image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        
        return image;
    }    
    //=========
    // Setters
    //=========
    
    public void setMinFilter(int filter) { minFilter = filter; }
    public void setMagFilter(int filter) { magFilter = filter; }
    
}
