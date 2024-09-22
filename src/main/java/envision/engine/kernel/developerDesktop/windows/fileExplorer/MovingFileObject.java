package envision.engine.kernel.developerDesktop.windows.fileExplorer;

import java.io.File;

import envision.engine.assets.WindowTextures;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

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
        getTopParent().setEscapeStopper(this);
        setSize(100, 100);
        bringToFront();
    }
    
    //===========================
    // Overrides : IWindowObject
    //===========================
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
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
    
    //=======================
    // Static Helper Methods
    //=======================
    
    /**
     * Gets the file previews out of a DragAndDropObject assuming that it
     * is a moving file object.
     * <p>
     * If the object is not an instance of a MovingFileObject, an empy list
     * will be returned instead.
     * 
     * @param  object The object to potentially extract file previews from
     * @return        A list of file previews
     */
    public static EList<FilePreview> getFilePreviews(DragAndDropObject object) {
        EList<FilePreview> files = EList.newList();
        if (object instanceof MovingFileObject mfo) {
            files.addAll(mfo.filesBeingMoved);
        }
        return files;
    }
    
    /**
     * Gets the files out of a DragAndDropObject assuming that it is a
     * moving file object.
     * <p>
     * If the object is not an instance of a MovingFileObject, an empy list
     * will be returned instead.
     * 
     * @param  object The object to potentially extract files from
     * @return        A list of files
     */
    public static EList<File> getFiles(DragAndDropObject object) {
        EList<File> files = EList.newList();
        if (object instanceof MovingFileObject mfo) {
            for (FilePreview fp : mfo.filesBeingMoved) {
                files.add(fp.getFile());
            }
        }
        return files;
    }

    
}
