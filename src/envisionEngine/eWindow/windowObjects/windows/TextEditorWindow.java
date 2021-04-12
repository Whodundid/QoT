package envisionEngine.eWindow.windowObjects.windows;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.advancedObjects.textArea.TextAreaLine;
import envisionEngine.eWindow.windowObjects.advancedObjects.textArea.WindowTextArea;
import envisionEngine.eWindow.windowObjects.windows.WindowDialogueBox.DialogueBoxTypes;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import mathUtil.NumberUtil;
import renderUtil.EColors;
import storageUtil.EArrayList;

public class TextEditorWindow extends WindowParent {
	
	File path = null;
	WindowTextArea document;
	WindowButton save, cancel, reload;
	boolean failed = false;
	boolean newFile = false;
	
	private double vPos, hPos;
	private TextAreaLine line;
	
	public TextEditorWindow(File pathIn) {
		super();
		path = pathIn;
		//windowIcon = EMCResources.textEditorIcon;
	}
	
	@Override
	public void initWindow() {
		setDimensions(250, 250);
		setMinDims(125, 125);
		setResizeable(true);
		setMaximizable(true);
		setObjectName("Editing: " + path != null ? path.getName() : "Unnamed File");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		document = new WindowTextArea(this, startX + 2, startY + 2, width - 4, height - 31);
		document.setEditable(!failed);
		document.setBackgroundColor(EColors.steel.intVal);
		document.setResetDrawn(false);
		if (!failed) { document.setDrawLineNumbers(true); }
		
		double w = NumberUtil.clamp((width - 10 - 24) / 2, 45, 100);
		double h = document.endY + (endY - document.endY) / 2 - 10;
		
		cancel = new WindowButton(this, midX - 15 - w, h, w, 20, "Cancel");
		save = new WindowButton(this, midX + 15, h, width % 2 == 1 ? w + 1 : w, 20, "Save");
		reload = new WindowButton(this, midX - 10, h, 20, 20);
		//reload.setTextures(EMCResources.refresh, EMCResources.refreshSel);
		
		reload.setHoverText("Reload");
		
		addObject(document, save, cancel, reload);
		
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
		document.getVScrollBar().setScrollBarPos(vPos);
		document.getHScrollBar().setScrollBarPos(hPos);
		if (line != null) {
			document.setSelectedLine(document.getLineWithTextAndObject(line.getText(), line.getStoredObject()));
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == save) { saveFile(); }
		if (object == cancel) { close(); }
		if (object == reload) { reloadFile(); }
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
				
				document.getVScrollBar().setScrollBarPos(0);
			}
			else { newFile = true; }
		}
	}
	
	private void saveFile() {
		if (path != null) {
			
			//create the required directories if they do not exist
			if (!path.getParentFile().exists()) { path.getParentFile().mkdirs(); }
			
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
		if (newFile) { document.addTextLine().requestFocus(); }
		return this;
	}
	
	private void openDialogue(boolean pass) {
		WindowDialogueBox box = new WindowDialogueBox(DialogueBoxTypes.ok);
		
		box.setTitle("Saving File");
		box.setTitleColor(EColors.lgray.intVal);
		box.setMessage(path.getName() + ": " + (pass ? "Saved!" : "Failed to save!"));
		box.setMessageColor(pass ? EColors.green.intVal : EColors.lred.intVal);
		
		getTopParent().displayWindow(box);
	}

}
