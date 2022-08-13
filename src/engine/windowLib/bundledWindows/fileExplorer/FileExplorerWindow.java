package engine.windowLib.bundledWindows.fileExplorer;

import engine.inputHandlers.Keyboard;
import engine.windowLib.bundledWindows.TextEditorWindow;
import engine.windowLib.bundledWindows.TextureDisplayer;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.actionObjects.WindowTextField;
import engine.windowLib.windowObjects.advancedObjects.WindowScrollList;
import engine.windowLib.windowTypes.ActionWindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.ObjectPosition;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.file.FileOpener;
import eutil.strings.StringUtil;

import java.io.File;

import assets.textures.window.WindowTextures;

public class FileExplorerWindow<E> extends ActionWindowParent<E> {
	
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
	
	private EArrayList<FilePreview> order = new EArrayList();
	private EArrayList<FilePreview> highlighted = new EArrayList();
	
	private boolean selectMode = false;
	
	private String text;
	private double vPos = 0;
	private EArrayList<Integer> prevHighlight;
	
	private EArrayList<Double> scrollBarPositionHistory = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public FileExplorerWindow(IWindowObject<?> parent) { this(parent, System.getProperty("user.dir"), false); }
	public FileExplorerWindow(IWindowObject<?> parent, String dirIn) { this(parent, new File(dirIn), false); }
	public FileExplorerWindow(IWindowObject<?> parent, File dirIn) { this(parent, dirIn, false); }
	public FileExplorerWindow(IWindowObject<?> parent, String dirIn, boolean selectModeIn) { this(parent, new File(dirIn), selectModeIn); }
	public FileExplorerWindow(IWindowObject<?> parent, File dirIn, boolean selectModeIn) {
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
		setSize(800, 483);
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
		dirField.setText("> " + curDir);
		
		IActionObject.setActionReceiver(this, backBtn, forwardBtn, fileUpBtn);
		IActionObject.setActionReceiver(this, dirField);
		
		backBtn.setHoverText("Back");
		forwardBtn.setHoverText("Forward");
		fileUpBtn.setHoverText("File Up");
		
		double faH = (((selectMode) ? cY : endY) - 2) - (fileUpBtn.endY + 8);
		fileArea = new WindowScrollList(this, startX + 2, fileUpBtn.endY + 8, width - 4, faH);
		fileArea.setBackgroundColor(EColors.pdgray);
		
		addChild(backBtn, forwardBtn, fileUpBtn);
		addChild(cancelBtn, selectBtn);
		addChild(dirField);
		addChild(fileArea);
		
		buildDir();
	}
	
	@Override
	public void preReInit() {
		text = dirField.getText();
		vPos = fileArea.getVScrollBar().getScrollPos();
		prevHighlight = new EArrayList();
		/*
		for (var f : highlighted) {
			System.out.println(f.getOrderPos());
		}
		System.out.println("NONONONONO");
		*/
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
		
		drawLine(fileUpBtn.endX + 6, startY, fileUpBtn.endX + 6, backBtn.endY + 6, EColors.black);
		drawLine(startX, backBtn.endY + 6, endX, backBtn.endY + 6, EColors.black);
		
		if (selectBtn != null) {
			selectBtn.setEnabled(highlighted.hasOne());
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == fileUpBtn) fileUp();
		if (object == selectBtn) select();
		if (object == cancelBtn) cancel();
		
		if (object == dirField) {
			String txt = dirField.getText();
			scrollBarPositionHistory.clear();
			if (txt.startsWith("> ")) setDir(txt.substring(2));
			else setDir(txt);
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
	
	public File getSelectedFile() {
		return selectedFile;
	}
	
	//---------
	// Setters
	//---------
	
	public void setDir(String dir) { setDir(new File(dir)); }
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
		if (isInit()) getHeader().setTitle(title);
		else titleToSet = title;
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void updateDir() {
		dirField.setText("> " + curDir);
		buildDir();
	}
	
	private void buildDir() {
		fileArea.clearList();
		order.clear();
		highlighted.clear();
		File[] dir = curDir.listFiles();
		
		if (dir == null) return;
		EArrayList<File> folders = new EArrayList();
		EArrayList<File> files = new EArrayList();
		
		for (File f : dir) {
			if (f.isDirectory()) folders.add(f);
			else files.add(f);
		}
		
		int orderPos = 0;
		int yPos = 2;
		
		for (File f : folders) {
			FilePreview preview = new FilePreview(this, f, orderPos++);
			preview.setDimensions(0, yPos, fileArea.width - fileArea.getVScrollBar().width, 60);
			yPos += 62;
			fileArea.addObjectToList(preview);
			order.add(preview);
		}
		
		for (File f : files) {
			FilePreview preview = new FilePreview(this, f, orderPos++);
			preview.setDimensions(0, yPos, fileArea.width - fileArea.getVScrollBar().width, 60);
			yPos += 62;
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
				for (var o : order) {
					if (o.isHighlighted() && o.getOrderPos() < low) {
						low = o.getOrderPos();
					}
				}
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
		if (!highlighted.hasOne()) return;
		
		if (actionReceiver == null) {
			openFile(f);
			return;
		}
		
		selectedFile = f.getFile();
		performAction(f.getFile());
		close();
	}
	
	protected void openFile(FilePreview f) {
		File dir = f.getFile();
		
		try {
			switch (f.getFileType()) {
			case PICTURE:
				getTopParent().displayWindow(new TextureDisplayer(dir), ObjectPosition.SCREEN_CENTER);
				break;
			case TEXT:
				TextEditorWindow txtWindow = new TextEditorWindow(f.getFile());
				getTopParent().displayWindow(txtWindow, ObjectPosition.SCREEN_CENTER);
				break;
			case FOLDER:
				setDir(f.getFile());
				break;
			case FILE:
				FileOpener.openFile(f.getFile());
				break;
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
		int index = StringUtil.findStartingIndex(curDir.toString(), "\\", true);
		if (scrollBarPositionHistory.isNotEmpty()) {
			double pos = scrollBarPositionHistory.pop();
			System.out.println(pos);
			fileArea.getVScrollBar().setScrollPos(pos);
		}
		setDir(new File(curDir.toString().substring(0, index)));
	}
	
	private void refresh() { setDir(curDir); }
	private void select() { if (highlighted.hasOne()) selectFile(highlighted.get(0)); }
	private void cancel() { close(); }
	
}
