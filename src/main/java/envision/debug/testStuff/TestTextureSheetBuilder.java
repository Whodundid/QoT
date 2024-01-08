package envision.debug.testStuff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.terminal.terminalUtil.FileType;
import envision.engine.windows.bundledWindows.fileExplorer.MovingFileObject;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowImageBox;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.EObjectGroup;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.events.EventDragAndDrop;
import eutil.datatypes.Grid;
import eutil.datatypes.util.EList;

public class TestTextureSheetBuilder extends WindowParent {
    
    //========
    // Fields
    //========
    
    private WindowButton clearButton;
    private WindowButton saveSheet;
    private WindowButton editButton;
    
    private BufferedImage image;
    private GameTexture texture;
    private WindowImageBox textureDisplayer;
    private EList<BufferedImage> textureList = EList.newList();
    
    private boolean isEditMode = true;
    private Grid<WindowImageBox> spriteGrid = new Grid<>();
    
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
        
        clearButton = new WindowButton<>(this, midX - 160, endY - 50, 150, 40, "Clear");
        clearButton.setAction(this::clearImage);
        
        saveSheet = new WindowButton<>(this, midX + 10, endY - 50, 150, 40, "Save");
        saveSheet.setAction(this::saveSheet);
        
        double texEY = height - clearButton.height - 25;
        textureDisplayer = new WindowImageBox(this, startX + 5, startY + 5, width - 10, texEY);
        
        var group = new EObjectGroup(this);
        group.addObject(textureDisplayer, clearButton);
        setObjectGroup(group);
        textureDisplayer.setObjectGroup(group);
        clearButton.setObjectGroup(group);
        
        addObject(clearButton);
        addObject(saveSheet);
        addObject(textureDisplayer);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
    }
    
    @Override
    public void preReInit() {
        
    }
    
    @Override
    public void postReInit() {
        textureDisplayer.setImage(texture);
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
                findTextures(EList.of(new File(fileName)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        updateImage();
        updateTexture();
        requestFocus();
        bringToFront();
    }
    
    private void findTextures(EList<File> filesToProcess) {
        for (File file : filesToProcess) {
            try {
                FileType type = FileType.getFileType(file);
                if (type == FileType.FOLDER) {
                    var files = EList.of(file.listFiles());
                    findTextures(files);
                }
                else if (type == FileType.PICTURE) {
                    var img = ImageIO.read(file);
                    //if (img.getWidth() != 32 || img.getHeight() != 32) continue;
                    textureList.add(img);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onGroupNotification(ObjectEvent e) {
        if (e instanceof EventDragAndDrop d) {
            onDragAndDrop(d.getObjectBeingDropped());
        }
    }
    
    @Override
    public void onDragAndDrop(DragAndDropObject objectBeingDropped) {
        final MovingFileObject mfo = (MovingFileObject) objectBeingDropped;
        final var files = mfo.getFilesBeingMoved().map(f -> f.getFile().getAbsolutePath());
        onSystemDragAndDrop(files);
    }
    
    private void updateImage() {
        int w = 0, h = 0; // width/height
        int pw = 0, ph = 0; // pixel width/height
        
        int size = textureList.size();
        textureList.sort((a, b) -> {
            final boolean alphaA = a.getColorModel().hasAlpha();
            final boolean alphaB = b.getColorModel().hasAlpha();
            
            if (alphaA && !alphaB) {
                return 1;
            }
            else if (alphaB && !alphaA) {
                return -1;
            }
            return 0;
        });
        
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
        saveSheet.setEnabled(true);
    }
    
    private void clearImage() {
        if (texture != null && texture.hasBeenRegistered()) {
            TextureSystem.getInstance().destroyTexture(texture);
        }
        
        image = null;
        textureList.clear();
        saveSheet.setEnabled(false);
    }
    
    private void saveSheet() {
        if (image == null) return;
        
        String fileName = DeveloperDesktop.generateUniqueFileName(DeveloperDesktop.DESKTOP_DIR, "SPRITE_SHEET.png");
        File toCreate = new File(DeveloperDesktop.DESKTOP_DIR, fileName);
        try {
            ImageIO.write(image, "png", toCreate);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
