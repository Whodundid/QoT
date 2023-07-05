package envision.engine.windows.bundledWindows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.textArea.TextAreaLine;
import envision.engine.windows.windowObjects.advancedObjects.textArea.WindowTextArea;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox.DialogBoxTypes;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.assets.textures.taskbar.TaskBarTextures;
import qot.assets.textures.window.WindowTextures;

public class TextEditorWindow extends WindowParent {
	
	//--------
	// Fields
	//--------
	
	private File path = null;
	private WindowTextArea<String> document;
	private WindowButton save, cancel, reload;
	private boolean failed = false;
	private boolean newFile = false;
	
	private double vPos, hPos;
	private TextAreaLine<String> line;
	
	private volatile boolean loading = false;
	private volatile boolean loaded = false;
	private volatile EList<String> lines;
	private volatile EList<TextAreaLine> parsed;
	private boolean restored = false;
	
	//--------------
	// Constructors
	//--------------
	
	public TextEditorWindow(File pathIn) {
		super(Envision.getActiveTopParent());
		path = pathIn;
		windowIcon = TaskBarTextures.texteditor;
		
		new Thread(() -> loadFile()).start();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initWindow() {
		setGuiSize(600, 500);
		setMinDims(125, 125);
		setResizeable(true);
		setMaximizable(true);
		setObjectName("Editing: " + path != null ? path.getName() : "Unnamed File");
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		document = new WindowTextArea(this, startX + 2, startY + 2, width - 4, height - 50);
		document.setEditable(!failed);
		document.setBackgroundColor(EColors.steel.intVal);
		document.setResetDrawn(false);
		document.registerListener(this);
		
		if (!failed) document.setDrawLineNumbers(true);
		double w = ENumUtil.clamp((width - 30 - 24) / 2, 45, 200);
		double h = document.endY + (endY - document.endY) / 2 - 20;
		
		cancel = new WindowButton(this, midX - 25 - w, h, w, 40, "Cancel");
		save = new WindowButton(this, midX + 25, h, width % 2 == 1 ? w + 1 : w, 40, "Save");
		reload = new WindowButton(this, midX - 20, h, 40, 40);
		reload.setButtonTexture(WindowTextures.refresh);
		
		reload.setHoverText("Reload");
		
		addObject(document);
		addObject(save, cancel, reload);
	}
	
	@Override
	public void preReInit() {
		if (!restored) return;
		
		restored = false;
		vPos = document.getVScrollBar().getScrollPos();
		hPos = document.getHScrollBar().getScrollPos();
		line = document.getCurrentLine();
	}
	
	@Override
	public void postReInit() {
		/*
		System.out.println("REINIT");
		document.getVScrollBar().setScrollPos(vPos);
		document.getHScrollBar().setScrollPos(hPos);
		
		if (lines != null) {
			for (var l : lines) {
				document.addTextLine(l, EColors.lgray);
			}
		}
		if (line != null) {
			document.setSelectedLine(document.getLineWithTextAndObject(line.getText(), line.getGenericObject()));
		}
		
		restored = true;
		*/
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
		checkLoad();
		checkRestored();
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		//tap into 'ctrl + s'
		if (Keyboard.isCtrlS(keyCode)) saveFile();
		
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == save) saveFile();
		if (object == cancel) close();
		if (object == reload) reloadFile();
	}
	
	@Override
	public void onEvent(ObjectEvent e) {
		//System.out.println(e);
	}
	
	//---------
	// Methods
	//---------
	
	public void setFocusToLineIfEmpty() {
		if (newFile) document.addTextLine().requestFocus();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void checkLoad() {
		if (loading) {
			drawStringC("Loading...");
		}
		else if (!loaded) {
			for (var l : lines) {
				document.addTextLine(l, EColors.lgray);
			}
			
			loaded = true;
			restored = true;
		}
	}
	
	private void checkRestored() {
		if (isResizing()) return;
		if (!restored && !loading) {
			
			for (var l : lines) {
				document.addTextLine(l, EColors.lgray);
			}
			
			document.getVScrollBar().setScrollPos(vPos);
			document.getHScrollBar().setScrollPos(hPos);
			
			loaded = true;
			restored = true;
		}
	}
	
	private void loadFile() {
		loading = true;
		loaded = false;
		
		if (path == null) return;
		if (document == null) return;
		if (!path.exists()) newFile = true;
		
		//all parsed lines
		lines = EList.newList();
		
		if (path.exists())
		try (BufferedReader r = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = r.readLine()) != null) {
				lines.add(line);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			failed = true;
			document.addTextLine("Error: Cannot open file!", EColors.red);
			document.setEditable(false);
		}
		
		if (isInitialized()) {
			document.getVScrollBar().setScrollPos(0);
		}
		
		loading = false;
		loaded = true;
	}
	
	private void saveFile() {
	    if (path == null) return;
	    
        //create the required directories if they do not exist
        if (!path.getParentFile().exists()) path.getParentFile().mkdirs();
        
        //save the file
        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            
            if (document != null) {
                var lines = document.getTextDocument();
                
                for (var l : lines) {
                    writer.println(l.getText());
                }
            }
            
            openDialogue(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            openDialogue(false);
        }
		
		DeveloperDesktop.reloadFileExplorers();
	}
	
	private void reloadFile() {
		reInitChildren();
	}
	
	private void openDialogue(boolean pass) {
		var box = new WindowDialogueBox(this, DialogBoxTypes.OK);
		
		box.setTitle("Saving File");
		box.setTitleColor(EColors.lgray.intVal);
		box.setMessage(path.getName() + ": " + (pass ? "Saved!" : "Failed to save!"));
		box.setMessageColor(pass ? EColors.green : EColors.lred);
		
		getTopParent().displayWindow(box);
	}
	
	//---------
	// Getters
	//---------
	
	public WindowTextArea getTextArea() { return document; }

}
