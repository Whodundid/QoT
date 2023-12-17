package envision.engine.windows.bundledWindows.fileExplorer;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.developerDesktop.util.DesktopUtil;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowObjects.utilityObjects.ConfirmationDialogBox;
import envision.engine.windows.windowObjects.utilityObjects.ErrorDialogBox;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowTypes.ActionWindowParent;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.EObjectGroup;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.events.EventDragAndDrop;
import envision.engine.windows.windowUtil.windowEvents.events.EventKeyboard;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
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
    private RightClickMenu folderRCM;
    
    private EList<FilePreview> order = EList.newList();
    private EList<FilePreview> highlighted = EList.newList();
    
    private boolean selectMode = false;
    
    private String text;
    private double vPos = 0;
    private EList<Integer> prevHighlight;
    
    private EList<Double> scrollBarPositionHistory = EList.newList();
    
    private WatchService watcher;
    private WatchKey watchKey;
    private WatchKey watchedDir;
    private Thread watcherThread;
    private volatile boolean updateDir = false;
    
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
        if (!curDir.isDirectory()) curDir = curDir.getParentFile();
        selectMode = selectModeIn;
        windowIcon = WindowTextures.file_folder;
    }
    
    @Override public String getWindowName() { return "file-explorer"; }
    
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
        
        try {
            watcher = FileSystems.getDefault().newWatchService();            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        fileArea.setVScrollRate(60);
        
        setObjectGroup(new EObjectGroup(this, fileArea, header, backBtn, forwardBtn, fileUpBtn));
        
        addObject(backBtn, forwardBtn, fileUpBtn);
        addObject(cancelBtn, selectBtn);
        addObject(dirField);
        addObject(fileArea);
        
        buildDir();
    }
    
    @Override
    public void preReInit() {
        refresh();
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
    public void drawObject(long dt, int mXIn, int mYIn) {
        drawDefaultBackground();
        
        drawVerticalLine(fileUpBtn.endX + 5, startY, backBtn.endY + 6, 1, EColors.black);
        drawHorizontalLine(startX, endX, backBtn.endY + 6, 1, EColors.black);
        
        if (selectBtn != null) { selectBtn.setEnabled(highlighted.hasOne()); }
        
        if (updateDir) {
            updateDir = false;
            buildDir();
        }
        
        super.drawObject(dt, mXIn, mYIn);
    }
    
    private void stopWatcher() {
        if (watcherThread == null) return;
        
        watcherThread.interrupt();
        watcherThread = null;
    }
    
    private void startWatcher() {
        if (watcherThread != null) {
            watcherThread.interrupt();
            watcherThread = null;
        }
        
        watcherThread = new Thread(() -> {
            while (true) {
                try {
                    watchKey = watcher.take();
                }
                catch (InterruptedException e) {
                    return;
                }
                
                if (watchedDir == null) return;
                
                var events = watchedDir.pollEvents();
                if (events != null && !events.isEmpty()) {
                    updateDir = true;
                }
            }
        });
        
        watcherThread.start();
    }
    
    @Override
    public void onGroupNotification(ObjectEvent e) {
        if (e instanceof EventMouse m) {
            final IWindowObject obj = e.getEventParent();
            if (obj != this && obj != fileArea) return;
            if (m.getMouseButton() == 1) openFolderRCM();
        }
        else if (e instanceof EventDragAndDrop d) {
            onDragAndDrop(d.getObjectBeingDropped());
        }
        else if (e instanceof EventKeyboard k) {
            keyPressed(k.getEventChar(), k.getEventKey());
        }
    }
    
    @Override
    public void mousePressed(int mXIn, int mYIn, int button) {
        super.mousePressed(mXIn, mYIn, button);
        if (button == 1) openFolderRCM();
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (Keyboard.isCtrlV(keyCode)) performFilePaste();
        else if (Keyboard.isShiftDelete(keyCode)) deleteHighlightedFiles();
        else if (keyCode == Keyboard.KEY_DELETE) recycleHighlightedFiles();
        else if (keyCode == Keyboard.KEY_ENTER) openHighlightedFiles();
        else if (Keyboard.isCtrlC(keyCode)) DeveloperDesktop.setFilesToCopy(getHighlightedFiles());
        else if (Keyboard.isCtrlX(keyCode)) DeveloperDesktop.setFilesToCut(getHighlightedFiles());
        else if (Keyboard.isCtrlA(keyCode)) selectAll();
        else if (Keyboard.isCtrlD(keyCode)) deselectAll();
        else if (keyCode == Keyboard.KEY_F5) refresh();
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
    
    @Override
    public void onDragAndDrop(DragAndDropObject objectBeingDropped) {
        if (!(objectBeingDropped instanceof MovingFileObject)) return;
        
        var mfo = (MovingFileObject) objectBeingDropped;
        var files = mfo.getFilesBeingMoved().map(f -> f.getFile());
        DeveloperDesktop.setFilesToCopy(files);
        DeveloperDesktop.performCut(curDir);
        DeveloperDesktop.reloadFileExplorers();
        
//        String text = EColors.yellow + "Move";
//        if (files.hasOne()) text += " the file: " + files.getFirst().getName();
//        else text += files.size() + " files";
//        text += " to " + curDir.getName() + "?";
//        ConfirmationDialogBox.showDialog(text, () -> {
//            DeveloperDesktop.performCut(curDir);
//            DeveloperDesktop.reloadFileExplorers();
//        });
    }
    
    @Override
    public void onClosed() {
        stopWatcher();
    }
    
    //---------
    // Methods
    //---------
    
    public void clearHighlighted() {
        highlighted.forEach(f -> f.setHighlighted(false));
        highlighted.clear();
    }
    
    private void openFolderRCM() {
        if (folderRCM != null) {
            folderRCM.close();
            folderRCM = null;
        }
        
        folderRCM = new RightClickMenu("Folder Options");
        folderRCM.addOption("New File", WindowTextures.file_txt, () -> createNewFile());
        folderRCM.addOption("New Folder", WindowTextures.new_folder, () -> createNewFolder());
        folderRCM.addOptionIf(DeveloperDesktop.hasFilesToCopy(), "Paste", () -> performFilePaste());
        folderRCM.addOption("New Window", WindowTextures.file_folder, () -> DeveloperDesktop.openFileExplorer(curDir));
        folderRCM.addOption("Refresh", WindowTextures.refresh, () -> refresh());
        folderRCM.addOption("Terminal Here", WindowTextures.terminal, () -> DesktopUtil.openInTerminal(curDir));
        folderRCM.showOnCurrent();
    }
    
    //---------
    // Getters
    //---------
    
    public File getSelectedFile() { return selectedFile; }
    
    /** Returns a list of all files that are currently highlighted in this file explorer. */
    public EList<FilePreview> getHighlightedFilePreviews() {
        return EList.of(highlighted);
    }
    
    /** Returns a list of all files that are currently highlighted in this file explorer. */
    public EList<File> getHighlightedFiles() {
        return EList.of(highlighted.map(f -> f.getFile()));
    }
    
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
        // stop watching old dir
        if (watchedDir != null) {
            watchedDir.cancel();
            stopWatcher();
        }
        
        // setup watcher for new dir
        try {
            watchedDir = curDir.toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY, OVERFLOW);
            startWatcher();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
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
        int gap = 1;
        
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
                DesktopUtil.openFile(f.getFile());
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
        if (!DeveloperDesktop.hasFilesToCopy()) return;
        if (DeveloperDesktop.performFileOperation(curDir)) {
            refresh();
            DeveloperDesktop.reloadFileExplorers();
        }
    }
    
    public void openHighlightedFiles() {
        for (var file : highlighted) {
            // if there are multiple files selected and one is a directory,
            // open the directory in a new file explorer window
            if (file.isDir() && highlighted.size() > 1) {
                DesktopUtil.openFile(file.getFile());
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
            DesktopUtil.recycleFile(filePreview.getFile());
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
            DesktopUtil.deleteFile(filePreview.getFile());
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
            DesktopUtil.recycleMultipleFiles(mappedFiles);
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
            DesktopUtil.deleteMultipleFiles(mappedFiles);
            refresh();
        });
    }
    
    public void createNewFile() {
        try {
            if (DeveloperDesktop.createFileInDir(curDir, "New File") != null) {
                refresh();
                return;
            }
            ErrorDialogBox.showDialog("Failed to create new file!");
        }
        catch (IOException e) {
            ErrorDialogBox.showDialog(e.toString());
        }
    }
    
    public void createNewFolder() {
        try {
            if (DeveloperDesktop.createDirectoryInDir(curDir, "New Folder") != null) {
                refresh();
                return;
            }
            ErrorDialogBox.showDialog("Failed to create new folder!");
        }
        catch (IOException e) {
            ErrorDialogBox.showDialog(e.toString());
        }
    }
    
}
