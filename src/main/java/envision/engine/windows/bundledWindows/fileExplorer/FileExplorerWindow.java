package envision.engine.windows.bundledWindows.fileExplorer;

import java.io.File;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.windows.bundledWindows.ConfirmationDialogBox;
import envision.engine.windows.developerDesktop.EnvisionDeveloperDesktop;
import envision.engine.windows.developerDesktop.util.EnvisionDesktopUtil;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowTypes.ActionWindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;
import qot.assets.textures.window.WindowTextures;

public class FileExplorerWindow extends ActionWindowParent {
    
    //--------
    // Fields
    //--------
    
    private File curDir;
    private File selectedFile;
    private String titleToSet;
    
    private WindowButton backBtn, forwardBtn, fileUpBtn, refreshBtn;
    private WindowTextField dirField, searchField;
    private WindowButton cancelBtn, selectBtn;
    private WindowScrollList fileArea;
    
    private EList<FilePreview> order = EList.newList();
    private EList<FilePreview> highlighted = EList.newList();
    
    private boolean selectMode = false;
    
    private String text;
    private double vPos = 0;
    private EList<Integer> prevHighlight;
    
    private EList<Double> scrollBarPositionHistory = EList.newList();
    
    //--------------
    // Constructors
    //--------------
    
    public FileExplorerWindow() { this(Envision.getActiveTopParent()); }
    public FileExplorerWindow(String dirIn) { this(Envision.getActiveTopParent(), new File(dirIn), false); }
    public FileExplorerWindow(File dirIn) { this(Envision.getActiveTopParent(), dirIn, false); }
    public FileExplorerWindow(IWindowObject parent) { this(parent, System.getProperty("user.dir"), false); }
    public FileExplorerWindow(IWindowObject parent, String dirIn) { this(parent, new File(dirIn), false); }
    public FileExplorerWindow(IWindowObject parent, File dirIn) { this(parent, dirIn, false); }
    public FileExplorerWindow(IWindowObject parent, String dirIn, boolean selectModeIn) { this(parent, new File(dirIn), selectModeIn); }
    public FileExplorerWindow(IWindowObject parent, File dirIn, boolean selectModeIn) {
        super(parent);
        curDir = dirIn;
        selectMode = selectModeIn;
        windowIcon = WindowTextures.file_folder;
    }
    
    //-----------
    // Overrides
    //-----------
    
    @Override
    public void initWindow() {
        setObjectName("File Explorer");
        setGuiSize(800, 483);
        setResizeable(true);
        setMaximizable(true);
        setMinDims(400, 200);
    }
    
    @Override
    public void initChildren() {
        defaultHeader();
        if (titleToSet != null) header.setTitle(titleToSet);
        
        double bW = 32;
        double topY = startY + 6;
        
        backBtn = new WindowButton(this, startX + 6, topY, bW, bW);
        forwardBtn = new WindowButton(this, backBtn.endX + 6, topY, bW, bW);
        fileUpBtn = new WindowButton(this, forwardBtn.endX + 6, topY, bW, bW);
        
        double cW = 160;
        double cX = endX - 4 - cW;
        double cY = endY - 4 - bW;
        
        if (selectMode) {
            cancelBtn = new WindowButton(this, cX, cY, cW, bW, "Cancel");
            selectBtn = new WindowButton(this, cX - 6 - cW, cY, cW, bW, "Select");
        }
        
        backBtn.setTextures(WindowTextures.back, WindowTextures.back_sel);
        forwardBtn.setTextures(WindowTextures.forward, WindowTextures.forward_sel);
        fileUpBtn.setTextures(WindowTextures.file_up, WindowTextures.file_up_sel);
        
        double dirWidth = (endX - 6) - (fileUpBtn.endX + 12);
        dirField = new WindowTextField(this, fileUpBtn.endX + 12, topY, dirWidth, bW);
        dirField.setMaxStringLength(1024);
        dirField.setText(curDir);
        
        IActionObject.setActionReceiver(this, backBtn, forwardBtn, fileUpBtn);
        IActionObject.setActionReceiver(this, dirField);
        
        backBtn.setHoverText("Back");
        forwardBtn.setHoverText("Forward");
        fileUpBtn.setHoverText("File Up");
        
        double faH = (((selectMode) ? cY : endY) - 2) - (fileUpBtn.endY + 8);
        fileArea = new WindowScrollList(this, startX + 2, fileUpBtn.endY + 8, width - 4, faH);
        fileArea.setBackgroundColor(EColors.pdgray);
        
        addObject(backBtn, forwardBtn, fileUpBtn);
        addObject(cancelBtn, selectBtn);
        addObject(dirField);
        addObject(fileArea);
        
        buildDir();
    }
    
    @Override
    public void preReInit() {
        text = dirField.getText();
        vPos = fileArea.getVScrollBar().getScrollPos();
        prevHighlight = EList.newList();
        highlighted.map(f -> f.getOrderPos()).forEach(prevHighlight::add);
    }
    
    @Override
    public void postReInit() {
        dirField.setText(text);
        fileArea.getVScrollBar().setScrollPos(vPos);
        prevHighlight.forEach(i -> order.get(i).setHighlighted(true));
    }
    
    @Override
    public void drawObject(int mXIn, int mYIn) {
        drawDefaultBackground();
        
        drawVerticalLine(fileUpBtn.endX + 5, startY, backBtn.endY + 6, 1, EColors.black);
        drawHorizontalLine(startX, endX, backBtn.endY + 6, 1, EColors.black);
        
        if (selectBtn != null) { selectBtn.setEnabled(highlighted.hasOne()); }
        
        super.drawObject(mXIn, mYIn);
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (Keyboard.isCtrlV(keyCode)) performFilePaste();
        else if (Keyboard.isShiftDelete(keyCode)) deleteHighlightedFiles();
        else if (keyCode == Keyboard.KEY_DELETE) recycleHighlightedFiles();
        else if (keyCode == Keyboard.KEY_ENTER) openHighlightedFiles();
        else if (Keyboard.isCtrlA(keyCode)) selectAll();
        else if (Keyboard.isCtrlD(keyCode)) deselectAll();
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        if (object == fileUpBtn) fileUp();
        if (object == selectBtn) select();
        if (object == cancelBtn) cancel();
        
        if (object == dirField) {
            String txt = dirField.getText();
            scrollBarPositionHistory.clear();
            setDir(txt);
        }
    }
    
    //---------
    // Methods
    //---------
    
    public void clearHighlighted() {
        highlighted.forEach(f -> f.setHighlighted(false));
        highlighted.clear();
    }
    
    //---------
    // Getters
    //---------
    
    public File getSelectedFile() { return selectedFile; }
    
    //---------
    // Setters
    //---------
    
    public void setDir(String dir) {
        setDir(new File(dir));
    }
    public void setDir(File dir) {
        curDir = dir;
        updateDir();
    }
    
    /**
     * Toggles between selection mode or regular explorer mode.
     * 
     * @param val True for selection mode
     */
    public void setSelectionMode(boolean val) {
        boolean prev = selectMode;
        selectMode = val;
        if (prev != val) reInitChildren();
    }
    
    public void setTitle(String title) {
        if (isInitialized()) getHeader().setTitle(title);
        else titleToSet = title;
    }
    
    //------------------
    // Internal Methods
    //------------------
    
    private void updateDir() {
        dirField.setText(curDir);
        buildDir();
    }
    
    private void buildDir() {
        fileArea.clearList();
        order.clear();
        highlighted.clear();
        File[] dir = curDir.listFiles();
        
        if (dir == null) return;
        EList<File> folders = EList.newList();
        EList<File> files = EList.newList();
        
        for (File f : dir) {
            if (f.isDirectory()) folders.add(f);
            else files.add(f);
        }
        
        int orderPos = 0;
        int yPos = 2;
        int entrySize = 40;
        int gap = 2;
        
        for (File f : folders) {
            FilePreview preview = new FilePreview(this, f, orderPos++);
            preview.setDimensions(0, yPos, fileArea.width - fileArea.getVScrollBar().width, entrySize);
            yPos += entrySize + gap;
            fileArea.addObjectToList(preview);
            order.add(preview);
        }
        
        for (File f : files) {
            FilePreview preview = new FilePreview(this, f, orderPos++);
            preview.setDimensions(0, yPos, fileArea.width - fileArea.getVScrollBar().width, entrySize);
            yPos += entrySize + gap;
            fileArea.addObjectToList(preview);
            order.add(preview);
        }
        
        fileArea.fitItemsInList(3, 5);
    }
    
    protected void fileWasHighlighted(FilePreview f, boolean ctrl, boolean shift) {
        // add/remove singleton files from selection
        if (ctrl && !shift) {
            var val = !f.isHighlighted();
            if (val) highlighted.add(f);
            else highlighted.remove(f);
            f.setHighlighted(val);
        }
        // add range of files
        else if (shift) {
            //start by finding the lowest index of all currently highlighted files
            int low = order.size() + 1;
            int high;
            
            if (highlighted.isEmpty()) low = 0;
            else {
                for (var o : order) { if (o.isHighlighted() && o.getOrderPos() < low) { low = o.getOrderPos(); } }
            }
            
            //determine actual low/high indexes
            if (f.getOrderPos() < low) {
                high = low;
                low = f.getOrderPos();
            }
            else {
                high = f.getOrderPos();
            }
            
            //highlight all within range
            for (int i = low; i <= high; i++) {
                var o = order.get(i);
                highlighted.addIfNotContains(o);
                o.setHighlighted(true);
            }
        }
        else {
            clearHighlighted();
            highlighted.add(f);
            f.setHighlighted(true);
        }
    }
    
    protected void selectFile(FilePreview f) {
        if (Keyboard.isCtrlDown() || Keyboard.isShiftDown()) return;
        
        if (!highlighted.hasOne()) {
            if (selectMode) return;
            openHighlightedFiles();
            return;
        }
        
        if (!selectMode) {
            openFile(f);
            return;
        }
        
        selectedFile = f.getFile();
        performAction(f.getFile());
        close();
    }
    
    protected void openFile(FilePreview f) {
        try {
            switch (f.getFileType()) {
            case FOLDER:
                setDir(f.getFile());
                break;
            default:
                EnvisionDesktopUtil.openFile(f.getFile());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //----------------------------
    
    private void back() {}
    private void forward() {}
    
    private void fileUp() {
        int index = EStringUtil.findStartingIndex(curDir.toString(), "\\", true);
        if (scrollBarPositionHistory.isNotEmpty()) {
            double pos = scrollBarPositionHistory.pop();
            System.out.println(pos);
            fileArea.getVScrollBar().setScrollPos(pos);
        }
        setDir(new File(curDir.toString().substring(0, index)));
    }
    
    public void refresh() {
        setDir(curDir);
    }
    private void select() {
        if (highlighted.hasOne()) selectFile(highlighted.get(0));
    }
    private void cancel() {
        close();
    }
    
    //================
    // Helper Methods
    //================
    
    public void selectAll() {
        highlighted.clear();
        for (var file : order) {
            highlighted.add(file);
            file.setHighlighted(true);
        }
    }
    
    public void deselectAll() {
        for (var file : highlighted) {
            file.setHighlighted(false);
        }
        highlighted.clear();
    }
    
    public void performFilePaste() {
        File theFile = EnvisionDeveloperDesktop.getWorkingFile();
        boolean isCut = EnvisionDeveloperDesktop.isCutOperation();
        
        if (theFile == null) return;
        
        if (isCut) EnvisionDeveloperDesktop.performCut(curDir);
        else EnvisionDeveloperDesktop.performCopy(curDir);
        
        refresh();
    }
    
    public void openHighlightedFiles() {
        for (var file : highlighted) {
            // if there are multiple files selected and one is a directory,
            // open the directory in a new file explorer window
            if (file.isDir() && highlighted.size() > 1) {
                EnvisionDesktopUtil.openFile(file.getFile());
            }
            else openFile(file);
        }
    }
    
    /**
     * There is some evil circular logic going on here and should be killed.
     * 
     * @param filePreview
     */
    public void recycleFile(FilePreview filePreview) {
        if (highlighted.size() > 1) {
            recycleHighlightedFiles();
            return;
        }
        
        String text = EColors.yellow + "Are you sure you want to recycle the file: " + EColors.white + filePreview;
        ConfirmationDialogBox.showDialog(text, () -> {
            EnvisionDesktopUtil.recycleFile(filePreview.getFile());
            refresh();
        });
    }
    
    /**
     * There is some evil circular logic going on here and should be killed.
     * 
     * @param filePreview
     */
    public void deleteFile(FilePreview filePreview) {
        if (highlighted.size() > 1) {
            deleteHighlightedFiles();
            return;
        }
        
        String text = EColors.yellow + "Are you sure you want to delete the file: " + EColors.white + filePreview;
        ConfirmationDialogBox.showDialog(text, () -> {
            EnvisionDesktopUtil.deleteFile(filePreview.getFile());
            refresh();
        });
    }
    
    public void recycleHighlightedFiles() {
        final var filesToRecycle = highlighted;
        final int size = filesToRecycle.size();
        
        // check if there is only one file to delete
        if (size == 1) {
            recycleFile(filesToRecycle.getFirst());
            return;
        }
        
        EList<File> mappedFiles = filesToRecycle.map(f -> f.getFile());
        
        String text = EColors.yellow + "Are you sure you want to delete the selected '"
            + EColors.mc_lightpurple + size + EColors.yellow + "' files?";
        
        ConfirmationDialogBox.showDialog(text, () -> {
            EnvisionDesktopUtil.recycleMultipleFiles(mappedFiles);
            refresh();
        });
    }
    
    public void deleteHighlightedFiles() {
        final var filesToDelete = highlighted;
        final int size = filesToDelete.size();
        
        // check if there is only one file to delete
        if (size == 1) {
            deleteFile(filesToDelete.getFirst());
            return;
        }
        
        EList<File> mappedFiles = filesToDelete.map(f -> f.getFile());
        
        String text = EColors.yellow + "Are you sure you want to delete the selected '"
            + EColors.mc_lightpurple + size + EColors.yellow + "' files?";
        
        ConfirmationDialogBox.showDialog(text, () -> {
            EnvisionDesktopUtil.deleteMultipleFiles(mappedFiles);
            refresh();
        });
    }
    
}
