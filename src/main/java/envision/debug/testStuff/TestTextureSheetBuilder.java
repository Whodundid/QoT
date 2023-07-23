package envision.debug.testStuff;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowImageBox;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import eutil.datatypes.util.EList;

public class TestTextureSheetBuilder extends WindowParent {
    
    //========
    // Fields
    //========
    
    private WindowButton clearButton;
    
    private BufferedImage image;
    private GameTexture texture;
    private WindowImageBox textureDisplayer;
    private EList<BufferedImage> textureList = EList.newList();
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setObjectName("Texture Sheet Builder");
        setGuiSize(400, 400);
        setMinDims(400, 400);
        setResizeable(true);
        setMaximizable(true);
        setMinimizable(true);
    }
    
    @Override
    public void initChildren() {
        defaultHeader();
        
        clearButton = new WindowButton(this, midX - 100, endY - 50, 200, 40, "Clear");
        clearButton.onPress(() -> clearImage());
        
        double texEY = height - clearButton.height - 25;
        textureDisplayer = new WindowImageBox(this, startX + 5, startY + 5, width - 10, texEY);
        
        addObject(clearButton);
        addObject(textureDisplayer);
    }
    
    @Override
    public void drawObject(int mXIn, int mYIn) {
        drawDefaultBackground();
        
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        
    }
    
    @Override
    public boolean allowsSystemDragAndDrop() {
        return true;
    }
    
    @Override
    public void onSystemDragAndDrop(EList<String> droppedFileNames) {
        for (String fileName : droppedFileNames) {
            try {
                var img = ImageIO.read(new File(fileName));
                textureList.add(img);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        updateImage();
        updateTexture();
    }
    
    private void updateImage() {
        int w = 0, h = 0; // width/height
        int pw = 0, ph = 0; // pixel width/height
        
        int size = textureList.size();
        
        int i = 1;
        while (i * i < size) i++;
        
        w = i;
        h = i;
        pw = i * textureList.getFirst().getWidth();
        ph = i * textureList.getFirst().getHeight();
        image = new BufferedImage(pw, ph, BufferedImage.TYPE_INT_ARGB);
        
        // set black background
        for (int y = 0; y < ph; y++) {
            for (int x = 0; x < pw; x++) {
                image.setRGB(x, y, 0xff000000);
            }
        }
        
        // copy image data for each tile
        for (int j = 0; j < size; j++) {
            int x = (j % w) * 32;
            int y = (j / h) * 32;
            
            var img = textureList.get(j);
            image.getGraphics().drawImage(img, x, y, x + 32, y + 32, 0, 0, 32, 32, null);
        }
    }
    
    private void updateTexture() {
        texture = new GameTexture(image);
        TextureSystem.getInstance().reg(texture);
        textureDisplayer.setImage(texture);
    }
    
    private void clearImage() {
        if (texture != null && texture.hasBeenRegistered()) {
            TextureSystem.getInstance().destroyTexture(texture);
        }
        
        image = null;
        textureList.clear();
    }
    
}
