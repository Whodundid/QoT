package envision.debug.testStuff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import envision.engine.creation.block.CreatorBlock;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.terminal.terminalUtil.FileType;
import envision.engine.windows.bundledWindows.fileExplorer.MovingFileObject;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.basicObjects.WindowImageBox;
import envision.engine.windows.windowObjects.utilityObjects.ErrorDialogBox;
import envision.engine.windows.windowObjects.utilityObjects.InfoDialogBox;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.EObjectGroup;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.events.EventDragAndDrop;
import eutil.datatypes.Grid;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class TestTextureSheetBuilder extends CreatorBlock {
    
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
    private WindowTextField saveNameField;
    private WindowTextField txWidthField, txHeightField;
    
    private boolean isEditMode = true;
    private Grid<WindowImageBox> spriteGrid = new Grid<>();
    
    private int tw = 32;
    private int th = 32;
    
    //===========================
    // Overrides : IWindowParent
    //===========================
    
    @Override
    public void initWindow() {
        setObjectName("Texture Sheet Builder");
        setGuiSize(500, 500);
        setMinDims(500, 500);
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
        
        double vgap = 5;
        double hgap = 10;
        double w = saveSheet.endX - clearButton.startX;
        double h = 30;
        double sx = clearButton.startX;
        double sy = clearButton.startY - h - vgap;
        saveNameField = new WindowTextField(this, sx, sy, w, h);
        saveNameField.setTextWhenEmpty("Save file name...");
        saveNameField.setAction(this::saveSheet);
        
        double ex = (sx + w) - (w / 2 - hgap * 2);
        txWidthField = new WindowTextField(this, sx, sy - vgap - h, w / 2 - hgap * 2, h);
        txHeightField = new WindowTextField(this, ex, sy - vgap - h, w / 2 - hgap * 2, h);
        txWidthField.setText(tw);
        txHeightField.setText(th);
        txWidthField.setTextWhenEmpty("width");
        txHeightField.setTextWhenEmpty("height");
        
        double texEY = (txWidthField.startY - vgap) - (startY + 5);
        textureDisplayer = new WindowImageBox(this, startX + 5, startY + 5, width - 10, texEY);
        
        var group = new EObjectGroup(this);
        group.addObject(textureDisplayer, clearButton);
        setObjectGroup(group);
        textureDisplayer.setObjectGroup(group);
        clearButton.setObjectGroup(group);
        
        addObject(clearButton);
        addObject(saveSheet);
        addObject(saveNameField);
        addObject(txWidthField, txHeightField);
        addObject(textureDisplayer);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        double x = txWidthField.endX + ((txHeightField.startX - txWidthField.endX) * 0.5);
        drawStringCS("x", x, txWidthField.midY - FontRenderer.HALF_FH + 1);
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
                var list = EList.of(new File(fileName));
                findTextures(list);
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
        
        tw = ENumUtil.parseInt(txWidthField.getText(), 32);
        th = ENumUtil.parseInt(txHeightField.getText(), 32);
        
        w = i;
        h = i;
        //pw = i * textureList.getFirst().getWidth();
        //ph = i * textureList.getFirst().getHeight();
        pw = i * tw;
        ph = i * th;
        image = new BufferedImage(pw, ph, BufferedImage.TYPE_INT_ARGB);
        
        // set black background
        for (int y = 0; y < ph; y++) {
            for (int x = 0; x < pw; x++) {
                image.setRGB(x, y, 0xff000000);
            }
        }
        
        // copy image data for each tile
        for (int j = 0; j < size; j++) {
            int x = (j % w) * tw;
            int y = (j / h) * th;
            
            var img = textureList.get(j);
            image.getGraphics().drawImage(img, x, y, x + tw, y + th, 0, 0, tw, th, null);
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
    
    private void saveSheet() { saveSheet(true); }
    private void saveSheet(boolean showDialog) {
        if (image == null) return;
        
        String name = saveNameField.getText();
        if (name.equals(saveNameField.textWhenEmpty)) name = "SPRITE_SHEET.png";
        else if (!name.endsWith(".png")) name += ".png";
        
        String fileName = DeveloperDesktop.generateUniqueFileName(DeveloperDesktop.DESKTOP_DIR, name);
        File toCreate = new File(DeveloperDesktop.DESKTOP_DIR, fileName);
        try {
            ImageIO.write(image, "png", toCreate);
            if (showDialog) InfoDialogBox.showDialog("Sprite Sheet Builder", "Sheet '" +
                                                     fileName + "' successfully saved!");
        }
        catch (IOException e) {
            e.printStackTrace();
            if (showDialog) ErrorDialogBox.showDialog("Failed to save sheet! " + e);
        }
    }
    
}
