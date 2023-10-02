package envision.engine.windows.bundledWindows.fileExplorer;

import java.io.File;

import org.apache.commons.io.FileUtils;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.terminal.terminalUtil.FileType;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.developerDesktop.util.DesktopUtil;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2d;
import eutil.file.FileOpener;
import qot.assets.textures.window.WindowTextures;

public class FilePreview extends WindowObject {
    
    //--------
    // Fields
    //--------
    
    private FileExplorerWindow explorer;
    private File theFile;
    private GameTexture texture;
    private boolean isDir;
    private boolean isHighlighted = false;
    private FileType fileType;
    private int orderPos;
    private RightClickMenu fileRCM;
    private WindowTextField textField;
    private boolean isPressed = false;
    private Point2d pressPoint = new Point2d(-1, -1);
    private boolean createdMovingFile = false;
    private boolean isRenaming = false;
    
    //--------------
    // Constructors
    //--------------
    
    public FilePreview(FileExplorerWindow windowIn, File pathIn, int orderPosIn) {
        init(windowIn);
        explorer = windowIn;
        theFile = pathIn;
        orderPos = orderPosIn;
        isDir = theFile.isDirectory();
        fileType = FileType.getFileType(theFile);
        texture = FileType.getFileTexture(fileType);
        getTopParent().registerListener(this);
        
        if (fileType == FileType.PICTURE) {
            try {
                GameTexture preview = new GameTexture(theFile.toString());
                TextureSystem.getInstance().reg(preview);
                if (preview.hasBeenRegistered()) {
                    texture = preview;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    //-----------
    // Overrides
    //-----------
    
    @Override
    public String toString() {
        return String.valueOf(theFile.getName());
    }
    
    @Override
    public void initChildren() {
        double textH = FontRenderer.FONT_HEIGHT;
        double textW = strWidth(theFile.getName()) + 20;
        double textX = startX + 50;
        double textY = midY - textH / 2 + 2;
        
        textField = new WindowTextField(this, textX, textY, textW, textH) {
            @Override
            public void keyPressed(char typedChar, int keyCode) {
                if (keyCode == Keyboard.KEY_ENTER) FilePreview.this.finishRename();
                else if (keyCode == Keyboard.KEY_ESC) FilePreview.this.cancelRename();
                else super.keyPressed(typedChar, keyCode);
            }
        };
        textField.setEnableBackgroundDrawing(false);
        textField.setText(theFile.getName());
        textField.setScissoringEnabled(false);
        textField.setClickable(false);
        
        addObject(textField);
    }
    
    @Override
    public void drawObject(int mXIn, int mYIn) {
        super.drawObject(mXIn, mYIn);
        
        int color;
        if (DeveloperDesktop.checkFileForCut(theFile)) color = EColors.mc_lightpurple.intVal;
        else if (DeveloperDesktop.checkFileForCopy(theFile)) color = EColors.aquamarine.intVal;
        else color = EColors.white.intVal;
        
        //draw transparent highlight overlay
        if (isHighlighted) {
            drawRect(0x33ffffff);
        }
        
        textField.setTextColor(color);
        //drawTexture(texture, startX, midY - texture.getWidth() / 2.0, 40, 40);
        drawTexture(texture, startX, startY, 40, height);
        //drawString(theFile.getName(), startX + 50, midY - FontRenderer.FONT_HEIGHT / 2, color);
        
        //draw mouse hovering selection rect
        if (isMouseOver()) { drawHRect(EColors.lgray); }
        
        checkMouseMove();
    }
    
    @Override
    public void mousePressed(int mXIn, int mYIn, int button) {
        super.mousePressed(mXIn, mYIn, button);

        if (button == 0) {
            isPressed = true;
            createdMovingFile = false;
            pressPoint.set(mXIn, mYIn);
        }
    }
    
    @Override
    public void mouseReleased(int mXIn, int mYIn, int button) {
        super.mouseReleased(mXIn, mYIn, button);
        
        if (button == 0) {
            explorer.fileWasHighlighted(this, Keyboard.isCtrlDown(), Keyboard.isShiftDown());
        }
        else if (button == 1) {
            openFileRCM();
        }
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (Keyboard.isShiftDelete(keyCode)) deleteFile();
        else if (keyCode == Keyboard.KEY_DELETE) recycleFile();
        else if (Keyboard.isCtrlA(keyCode)) explorer.selectAll();
        else if (Keyboard.isCtrlC(keyCode)) copyFile();
        else if (Keyboard.isCtrlX(keyCode)) cutFile();
        else if (Keyboard.isCtrlV(keyCode)) explorer.performFilePaste();
        else if (keyCode == Keyboard.KEY_ENTER) {
            if (isRenaming) finishRename();
            else explorer.selectFile(this);
        }
    }
    
    @Override
    public void onDoubleClick() {
        if (isDir) explorer.setDir(theFile);
        else explorer.selectFile(this);
    }
    
    @Override
    public void onEvent(ObjectEvent e) {
        if (e instanceof EventMouse em) {
            if (em.getMouseType() != MouseType.RELEASED) return;
            isPressed = false;
            pressPoint.set(-1, -1);
        }
    }
    
    @Override
    public void onClosed() {
        if (fileType == FileType.PICTURE) {
            try {
                System.out.println("CLEARING");
                // clean up image preview when no longer needed
                TextureSystem.getInstance().destroyTexture(texture);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onDragAndDrop(DragAndDropObject objectBeingDropped) {
        if (!(objectBeingDropped instanceof MovingFileObject)) return;
        
        var mfo = (MovingFileObject) objectBeingDropped;
        var files = mfo.getFilesBeingMoved().map(f -> f.getFile());
        
        if (!theFile.isDirectory()) return;
        
        for (File f : files) {
            try {
                if (f.isDirectory()) FileUtils.moveDirectoryToDirectory(f, theFile, false);
                else FileUtils.moveFileToDirectory(f, theFile, false);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        DeveloperDesktop.reloadFileExplorers();
    }
    
    //==================
    // Internal Methods
    //==================
    
    private void checkMouseMove() {
        if (createdMovingFile) return;
        // don't care if not pressed or LMB isn't down
        if (!isPressed || !Mouse.isLeftDown()) return;
        // don't care if the mouse hasn't moved since being pressed
        if (pressPoint.distance(Mouse.getMx(), Mouse.getMy()) <= 10) return;
        
        MovingFileObject.create(explorer.getHighlightedFilePreviews());
        createdMovingFile = true;
    }
    
    private void enableRenameMode() {
        isRenaming = true;
        getTopParent().setEscapeStopper(this);
        textField.setEditable(true);
        textField.requestFocus();
    }
    
    private void finishRename() {
        try {
            String name = textField.getText();
            File parent = theFile.getParentFile();
            File rename = new File(parent, name);
            theFile.renameTo(rename);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        textField.setEditable(false);
        isRenaming = false;
        getTopParent().setEscapeStopper(null);
        DeveloperDesktop.reloadFileExplorers();
    }
    
    private void cancelRename() {
        textField.setEditable(false);
        isRenaming = false;
        getTopParent().setEscapeStopper(null);
    }
    
    private void openFileRCM() {
        if (fileRCM != null) {
            fileRCM.close();
            fileRCM = null;
        }
        
        fileRCM = new RightClickMenu("File Options");
        fileRCM.addOption("Open", WindowTextures.file_up, this::onDoubleClick);
        fileRCM.addOptionIf(theFile.isDirectory(), "Open in new window", WindowTextures.new_folder,() -> DeveloperDesktop.openFileExplorer(theFile));
        fileRCM.addOption("Open In OS", () -> FileOpener.openFile(theFile));
        fileRCM.addOption("Copy", this::copyFile);
        fileRCM.addOption("Cut", this::cutFile);
        fileRCM.addOptionIf(DeveloperDesktop.hasFilesToCopy(), "Paste", () -> explorer.performFilePaste());
        fileRCM.addOption("Rename", this::enableRenameMode);
        fileRCM.addOption("Delete", WindowTextures.red_x, this::recycleFile);
        fileRCM.addOption("Refresh", WindowTextures.refresh, () -> explorer.refresh());
        fileRCM.addOption("Show in Terminal", WindowTextures.terminal , this::showInTerminal);
        fileRCM.showOnCurrent();
    }
    
    //=========
    // Methods
    //=========
    
    public void recycleFile() {
        explorer.recycleFile(this);
    }
    public void deleteFile() {
        explorer.deleteFile(this);
    }
    public void copyFile() {
        DeveloperDesktop.setFilesToCopy(explorer.getHighlightedFiles());
    }
    public void cutFile() {
        DeveloperDesktop.setFilesToCut(explorer.getHighlightedFiles());
    }
    public void showInTerminal() {
        DesktopUtil.openInTerminal(theFile);
    }
    
    //=========
    // Getters
    //=========
    
    public FileExplorerWindow getFileExplorerWindow() { return explorer; }
    public File getFile() { return theFile; }
    public boolean isDir() { return isDir; }
    public FileType getFileType() { return fileType; }
    public int getOrderPos() { return orderPos; }
    public boolean isHighlighted() { return isHighlighted; }
    public GameTexture getFileIcon() { return texture; }
    
    //=========
    // Setters
    //=========
    
    public void setHighlighted(boolean val) { isHighlighted = val; }
    
}
