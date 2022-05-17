package engine.windowLib.bundledWindows.fileExplorer;

import assets.textures.WindowTextures;
import engine.windowLib.bundledWindows.TextEditorWindow;
import engine.windowLib.bundledWindows.TextureDisplayer;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.actionObjects.WindowTextField;
import engine.windowLib.windowObjects.advancedObjects.scrollList.WindowScrollList;
import engine.windowLib.windowTypes.ActionWindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.ObjectPosition;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.misc.FileOpener;
import eutil.strings.StringUtil;

import java.io.File;

public class FileExplorerWindow<E> extends ActionWindowParent<E> {
	
	private File curDir;
	
	private WindowButton backBtn, forwardBtn, fileUpBtn, refreshBtn;
	private WindowTextField dirField, searchField;
	private WindowButton cancelBtn, selectBtn;
	private WindowScrollList fileArea;
	
	private EArrayList<FilePreview> order = new EArrayList();
	private EArrayList<FilePreview> highlighted = new EArrayList();
	
	public FileExplorerWindow(IWindowObject<?> parent) { this(parent, System.getProperty("user.dir")); }
	public FileExplorerWindow(IWindowObject<?> parent, String dirIn) {
		super(parent);
		curDir = new File(dirIn);
	}
	
	@Override
	public void initWindow() {
		setObjectName("File Explorer");
		setSize(800, 483);
		setResizeable(true);
		setMaximizable(true);
		setMinDims(400, 200);
	}
	
	@Override
	public void initObjects() {
		defaultHeader();
		
		double bW = 32;
		double topY = startY + 6;
		
		backBtn = new WindowButton(this, startX + 6, topY, bW, bW);
		forwardBtn = new WindowButton(this, backBtn.endX + 6, topY, bW, bW);
		fileUpBtn = new WindowButton(this, forwardBtn.endX + 6, topY, bW, bW);
		
		backBtn.setTextures(WindowTextures.back, WindowTextures.back_sel);
		forwardBtn.setTextures(WindowTextures.forward, WindowTextures.forward_sel);
		fileUpBtn.setTextures(WindowTextures.file_up, WindowTextures.file_up_sel);
		
		double dirWidth = (endX - 7) - (fileUpBtn.endX + 12);
		dirField = new WindowTextField(this, fileUpBtn.endX + 12, topY, dirWidth, bW);
		dirField.setMaxStringLength(1024);
		dirField.setText("> " + curDir);
		
		IActionObject.setActionReceiver(this, backBtn, forwardBtn, fileUpBtn);
		IActionObject.setActionReceiver(this, dirField);
		
		backBtn.setHoverText("Back");
		forwardBtn.setHoverText("Forward");
		fileUpBtn.setHoverText("File Up");
		
		double faH = (endY - 2) - (fileUpBtn.endY + 8);
		fileArea = new WindowScrollList(this, startX + 2, fileUpBtn.endY + 8, width - 4, faH);
		fileArea.setBackgroundColor(EColors.pdgray);
		
		addObject(backBtn, forwardBtn, fileUpBtn);
		addObject(dirField);
		addObject(fileArea);
		
		buildDir();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawLine(fileUpBtn.endX + 6, startY, fileUpBtn.endX + 6, backBtn.endY + 6, EColors.black);
		drawLine(startX, backBtn.endY + 6, endX, backBtn.endY + 6, EColors.black);
		
		super.drawObject(mXIn, mYIn);
	}
	
	//---------
	// Methods
	//---------
	
	protected void selectFile(FilePreview f) {
		if (this.actionReceiver != null) {
			performAction(f.getFile());
			close();
		}
		else {
			openFile(f);
		}
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
	
	public void setDir(String dir) { setDir(new File(dir)); }
	public void setDir(File dir) {
		curDir = dir;
		updateDir();
	}
	
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
				highlighted.add(o);
				o.setHighlighted(true);
			}
		}
		else {
			clearHighlighted();
			highlighted.add(f);
			f.setHighlighted(true);
		}
	}
	
	public void clearHighlighted() {
		highlighted.forEach(f -> f.setHighlighted(false));
		highlighted.clear();
	}
	
	//----------------------------
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == fileUpBtn) fileUp();
		if (object == dirField) {
			String txt = dirField.getText();
			if (txt.startsWith("> ")) setDir(txt.substring(2));
			else setDir(txt);
		}
	}
	
	//----------------------------
	
	private void back() {}
	private void forward() {}
	private void fileUp() {
		//System.out.println(curDir);
		int index = StringUtil.findStartingIndex(curDir.toString(), "\\", true);
		setDir(new File(curDir.toString().substring(0, index)));
	}
	private void refresh() {
		setDir(curDir);
	}
	private void select() {}
	private void cancel() {}
	
}
