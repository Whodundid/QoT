package envision.windowLib.bundledWindows.fileExplorer;

import eutil.colors.EColors;

import java.io.File;

import envision.inputHandlers.Keyboard;
import envision.renderEngine.fontRenderer.FontRenderer;
import envision.renderEngine.textureSystem.GameTexture;
import envision.terminal.terminalUtil.FileType;
import envision.windowLib.windowTypes.WindowObject;

public class FilePreview<E extends File> extends WindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	private FileExplorerWindow<?> window;
	private E thePath;
	private GameTexture texture;
	private boolean isDir;
	private boolean isHighlighted = false;
	private FileType fileType;
	private int orderPos;
	
	//--------------
	// Constructors
	//--------------
	
	public FilePreview(FileExplorerWindow<?> windowIn, E pathIn, int orderPosIn) {
		window = windowIn;
		thePath = pathIn;
		orderPos = orderPosIn;
		isDir = thePath.isDirectory();
		fileType = FileType.getFileType(thePath);
		texture = FileType.getFileTexture(fileType);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		drawTexture(texture, startX, midY - texture.getWidth() / 2, 40, 40);
		drawString(thePath.getName(), startX + 50, midY - FontRenderer.FONT_HEIGHT / 2);
		
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
		window.fileWasHighlighted(this, Keyboard.isCtrlDown(), Keyboard.isShiftDown());
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void onDoubleClick() {
		if (isDir) window.setDir(thePath);
		else window.selectFile(this);
	}
	
	//---------
	// Getters
	//---------
	
	public FileExplorerWindow<?> getFileExplorerWindow() { return window; }
	public File getFile() { return thePath; }
	public boolean isDir() { return isDir; }
	public FileType getFileType() { return fileType; }
	public int getOrderPos() { return orderPos; }
	public boolean isHighlighted() { return isHighlighted; }
	
	//---------
	// Setters
	//---------
	
	public void setHighlighted(boolean val) { isHighlighted = val; }
	
}
