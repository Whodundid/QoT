package envision.engine.windows.bundledWindows.fileExplorer;

import java.io.File;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.terminal.terminalUtil.FileType;
import envision.engine.windows.developerDesktop.EnvisionDeveloperDesktop;
import envision.engine.windows.developerDesktop.util.EnvisionDesktopUtil;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowTypes.WindowObject;
import eutil.colors.EColors;
import eutil.file.FileOpener;

public class FilePreview extends WindowObject {
	
	//--------
	// Fields
	//--------
	
	private FileExplorerWindow window;
	private File theFile;
	private GameTexture texture;
	private boolean isDir;
	private boolean isHighlighted = false;
	private FileType fileType;
	private int orderPos;
	private RightClickMenu fileRCM;
	
	//--------------
	// Constructors
	//--------------
	
	public FilePreview(FileExplorerWindow windowIn, File pathIn, int orderPosIn) {
		window = windowIn;
		theFile = pathIn;
		orderPos = orderPosIn;
		isDir = theFile.isDirectory();
		fileType = FileType.getFileType(theFile);
		texture = FileType.getFileTexture(fileType);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
	    return String.valueOf(theFile.getName());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		boolean isCutFile = EnvisionDeveloperDesktop.checkFileForCut(theFile);
		int color = (isCutFile) ? EColors.mc_lightpurple.intVal : EColors.white.intVal;
		
		drawTexture(texture, startX, midY - texture.getWidth() / 2, 40, 40);
		drawString(theFile.getName(), startX + 50, midY - FontRenderer.FONT_HEIGHT / 2, color);
		
		//draw transparent highlight overlay
		if (isHighlighted) {
			drawRect(0x33ffffff);
		}
		
		//draw mouse hovering selection rect
		if (isMouseOver()) {
			drawHRect(EColors.lgray);
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
	    super.mousePressed(mXIn, mYIn, button);
	    
		if (button == 0) window.fileWasHighlighted(this, Keyboard.isCtrlDown(), Keyboard.isShiftDown());
		if (button == 1) openFileRCM();
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
	    if (Keyboard.isShiftDelete(keyCode)) deleteFile();
	    else if (keyCode == Keyboard.KEY_DELETE) recycleFile();
	    else if (keyCode == Keyboard.KEY_ENTER) window.selectFile(this);
	    else if (Keyboard.isCtrlC(keyCode)) copyFile();
	    else if (Keyboard.isCtrlX(keyCode)) cutFile();
	    else if (Keyboard.isCtrlV(keyCode)) window.performFilePaste();
	}
	
	@Override
	public void onDoubleClick() {
		if (isDir) window.setDir(theFile);
		else window.selectFile(this);
	}
	
	//==================
	// Internal Methods
	//==================
	
    private void openFileRCM() {
        if (fileRCM != null) {
            fileRCM.close();
            fileRCM = null;
        }
        
        fileRCM = new RightClickMenu("File Options");
        fileRCM.addOption("Open", () -> onDoubleClick());
        fileRCM.addOption("Open In OS", () -> FileOpener.openFile(theFile));
        fileRCM.addOption("Copy", () -> copyFile());
        fileRCM.addOption("Cut", () -> cutFile());
        if (EnvisionDeveloperDesktop.getWorkingFile() != null) {
            fileRCM.addOption("Paste", () -> window.performFilePaste());
        }
        fileRCM.addOption("Rename");
        fileRCM.addOption("Delete", () -> recycleFile());
        fileRCM.addOption("Show in Terminal", () -> showInTerminal());
        fileRCM.showOnCurrent();
    }
	
	//=========
	// Methods
	//=========
	
	public void recycleFile() { window.recycleFile(this); }
	public void deleteFile() { window.deleteFile(this); }
	public void copyFile() { EnvisionDeveloperDesktop.setFileToCopy(theFile); }
	public void cutFile() { EnvisionDeveloperDesktop.setFileToCut(theFile); }
	public void showInTerminal() { EnvisionDesktopUtil.openInTerminal(theFile); }
	
	//=========
	// Getters
	//=========
	
	public FileExplorerWindow getFileExplorerWindow() { return window; }
	public File getFile() { return theFile; }
	public boolean isDir() { return isDir; }
	public FileType getFileType() { return fileType; }
	public int getOrderPos() { return orderPos; }
	public boolean isHighlighted() { return isHighlighted; }
	
	//=========
	// Setters
	//=========
	
	public void setHighlighted(boolean val) { isHighlighted = val; }
	
}
