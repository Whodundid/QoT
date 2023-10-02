package envision.engine.terminal.window.termParts;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.terminal.window.TerminalRCM;
import envision.engine.windows.windowObjects.advancedObjects.textArea.TextAreaLine;
import envision.engine.windows.windowUtil.ObjectPosition;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import eutil.colors.EColors;

public class TerminalTextLine extends TextAreaLine {

	ETerminalWindow term;
	
	public TerminalTextLine(ETerminalWindow termIn, String text) { this(termIn, text, EColors.white, null, -1); }
	public TerminalTextLine(ETerminalWindow termIn, String text, int color) { this(termIn, text, color, null, -1); }
	public TerminalTextLine(ETerminalWindow termIn, String text, EColors color) { this(termIn, text, color.c(), null, -1); }
	public TerminalTextLine(ETerminalWindow termIn, String text, EColors color, Object objIn) { this(termIn, text, color.c(), objIn, -1); }
	public TerminalTextLine(ETerminalWindow termIn, String text, int color, Object objIn) { this(termIn, text, color, objIn, -1); }
	public TerminalTextLine(ETerminalWindow termIn, String text, EColors color, Object objIn, int lineNumber) { this(termIn, text, color.c(), objIn, lineNumber); }
	public TerminalTextLine(ETerminalWindow termIn, String text, int color, Object objIn, int lineNumber) {
		super(termIn.getTextArea(), text, color, objIn, lineNumber);
		term = termIn;
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 1) {
			getTopParent().displayWindow(new TerminalRCM(this), ObjectPosition.CURSOR_CORNER);
		}
		else {
			if (checkLinkClick(mXIn, mYIn, button)) { term.writeln("Opening Link..\n", EColors.seafoam); }
			if (term != null && term.getInputField() != null) { term.getInputField().requestFocus(); }
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		//super.keyPressed(typedChar, keyCode);
		if (Keyboard.isCtrlC(keyCode)) {
			String text = getText();
			if (text.startsWith("> ")) { text = text.substring(2); }
			//text = EChatUtil.removeFormattingCodes(text);
			Keyboard.setClipboard(text.trim());
		}
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		super.onFocusGained(eventIn);
		if (eventIn.getEventType() == EventType.MOUSE && eventIn.getActionCode() == 1) {
			getTopParent().displayWindow(new TerminalRCM(this), ObjectPosition.CURSOR_CORNER);
		}
	}
	
	@Override
	public void onDoubleClick() {
		if (term != null && term.getInputField() != null) {
			String text = getText();
			//text = text;
			text = text.trim();
			if (text.startsWith("> ")) { text = text.substring(2); }
			term.getInputField().writeText(text);
			term.getInputField().requestFocus();
		}
	}

	public TerminalTextLine setTerminal(ETerminalWindow termIn) { term = termIn; return this; }
	public ETerminalWindow getTerminal() { return term; }
}
