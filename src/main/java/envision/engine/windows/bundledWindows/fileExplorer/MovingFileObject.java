package envision.engine.windows.bundledWindows.fileExplorer;

import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.assets.textures.window.WindowTextures;

public class MovingFileObject extends DragAndDropObject {
    
    private final EList<FilePreview> filesBeingMoved = EList.newList();
    private final GameTexture icon;
    private final int size;
    private final String sizeString;
    private final double sizeStringLen;
    
    //==============
    // Constructors
    //==============
    
    public static void create(FilePreview fileToMove) { new MovingFileObject(fileToMove); }
    public static void create(EList<FilePreview> filesToMove) { new MovingFileObject(filesToMove); }
    
    private MovingFileObject(FilePreview fileToMove) { this(EList.of(fileToMove)); }
    private MovingFileObject(EList<FilePreview> filesToMove) {
        filesBeingMoved.addAll(filesToMove);
        size = filesBeingMoved.size();
        sizeString = "" + size;
        sizeStringLen = strWidth(sizeString);
        if (filesBeingMoved.hasOne()) icon = filesBeingMoved.getFirst().getFileIcon();
        else icon = WindowTextures.file_txt;
        setSize(100, 100);
        bringToFront();
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void drawObject(int mXIn, int mYIn) {
        drawTexture(icon, mXIn - width * 0.5, mYIn - height * 0.5, width, height);
        drawString(sizeString, mXIn - sizeStringLen * 0.5, mYIn - FontRenderer.HALF_FH, EColors.lred);
    }
    
    //===============================
    // Overrides : DragAndDropObject
    //===============================
    
    @Override
    public void onDropped(IWindowObject target) {
        
    }
    
    //=========
    // Getters
    //=========
    
    public EList<FilePreview> getFilesBeingMoved() { return filesBeingMoved; }

    
}
