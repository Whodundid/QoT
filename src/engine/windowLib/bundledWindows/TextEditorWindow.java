package engine.windowLib.bundledWindows;

import assets.textures.WindowTextures;
import engine.QoT;
import engine.input.Keyboard;
import engine.windowLib.bundledWindows.utilityWindows.WindowDialogueBox;
import engine.windowLib.bundledWindows.utilityWindows.WindowDialogueBox.DialogueBoxTypes;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.advancedObjects.textArea.TextAreaLine;
import engine.windowLib.windowObjects.advancedObjects.textArea.WindowTextArea;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditorWindow extends WindowParent {
	
	File path = null;
	WindowTextArea document;
	WindowButton save, cancel, reload;
	boolean failed = false;
	boolean newFile = false;
	
	private double vPos, hPos;
	private TextAreaLine line;
	
	public TextEditorWindow(File pathIn) {
		super(QoT.getActiveTopParent());
		path = pathIn;
		//windowIcon = EMCResources.textEditorIcon;
	}
	
	@Override
	public void initWindow() {
		setSize(600, 500);
		setMinDims(125, 125);
		setResizeable(true);
		setMaximizable(true);
		setObjectName("Editing: " + path != null ? path.getName() : "Unnamed File");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		document = new WindowTextArea(this, startX + 2, startY + 2, width - 4, height - 50);
		document.setEditable(!failed);
		document.setBackgroundColor(EColors.steel.intVal);
		document.setResetDrawn(false);
		document.registerListener(this);
		if (!failed) document.setDrawLineNumbers(true);
		
		double w = NumberUtil.clamp((width - 30 - 24) / 2, 45, 200);
		double h = document.endY + (endY - document.endY) / 2 - 20;
		
		cancel = new WindowButton(this, midX - 25 - w, h, w, 40, "Cancel");
		save = new WindowButton(this, midX + 25, h, width % 2 == 1 ? w + 1 : w, 40, "Save");
		reload = new WindowButton(this, midX - 20, h, 40, 40);
		reload.setButtonTexture(WindowTextures.refresh);
		
		reload.setHoverText("Reload");
		
		addObject(cancel, save, cancel, reload);
		
		loadFile();
	}
	
	@Override
	public void preReInit() {
		vPos = document.getVScrollBar().getScrollPos();
		hPos = document.getHScrollBar().getScrollPos();
		line = document.getCurrentLine();
	}
	
	@Override
	public void postReInit() {
		document.getVScrollBar().setScrollPos(vPos);
		document.getHScrollBar().setScrollPos(hPos);
		if (line != null) {
			document.setSelectedLine(document.getLineWithTextAndObject(line.getText(), line.getGenericObject()));
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
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
	
	private void loadFile() {
		if (path != null && document != null) {
			if (path.exists()) {
				try (Scanner reader = new Scanner(path)) {
					while (reader.hasNext()) {
						document.addTextLine(reader.nextLine(), EColors.lgray);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					failed = true;
					document.addTextLine("Error: Cannot open file!", EColors.red);
					document.setEditable(false);
				}
				
				document.getVScrollBar().setScrollPos(0);
			}
			else newFile = true;
		}
	}
	
	private void saveFile() {
		if (path != null) {
			
			//create the required directories if they do not exist
			if (!path.getParentFile().exists()) path.getParentFile().mkdirs();
			
			//save the file
			try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
				
				if (document != null) {
					EArrayList<TextAreaLine> lines = document.getTextDocument();
					
					for (TextAreaLine l : lines) {
						writer.println(l.getText());
					}
				}
				
				openDialogue(true);
			}
			catch (Exception e) {
				e.printStackTrace();
				openDialogue(false);
			}
		}
	}
	
	private void reloadFile() {
		reInitObjects();
	}
	
	public WindowTextArea getTextArea() { return document; }
	
	public TextEditorWindow setFocusToLineIfEmpty() {
		if (newFile) document.addTextLine().requestFocus();
		return this;
	}
	
	private void openDialogue(boolean pass) {
		WindowDialogueBox box = new WindowDialogueBox(this, DialogueBoxTypes.ok);
		
		box.setTitle("Saving File");
		box.setTitleColor(EColors.lgray.intVal);
		box.setMessage(path.getName() + ": " + (pass ? "Saved!" : "Failed to save!"));
		box.setMessageColor(pass ? EColors.green : EColors.lred);
		
		getTopParent().displayWindow(box);
	}

}
