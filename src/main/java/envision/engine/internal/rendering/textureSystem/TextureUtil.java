package envision.engine.internal.rendering.textureSystem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextureUtil {    
    //==============
    // Constructors
    //==============
    
    private TextureUtil() {}
    
    //=======================
    // Static Helper Methods
    //=======================
    
    public static boolean isFileTexture(File theFile) {
        if (theFile == null || !theFile.exists()) return false;
        
        try {
            BufferedImage image = ImageIO.read(theFile);
            return image != null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
}
