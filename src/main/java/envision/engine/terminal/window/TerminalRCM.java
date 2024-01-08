package envision.engine.terminal.window;

import envision.engine.assets.WindowTextures;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.window.termParts.TerminalTextField;
import envision.engine.terminal.window.termParts.TerminalTextLine;
import envision.engine.windows.windowObjects.utilityObjects.RightClickMenu;
import envision.engine.windows.windowTypes.interfaces.IActionObject;

public class TerminalRCM extends RightClickMenu {

	ETerminalWindow term;
	TerminalTextLine line;
	TerminalTextField inputField;
	
	public TerminalRCM(ETerminalWindow termIn) {
		super();
		term = termIn;
		build();
		
		//addOption("Search text..");
		if (Keyboard.getClipboard() != null) addOption("Paste");
		addOption("Clear");
		addOption("Clear History");
		addOption("Options", WindowTextures.settings);
	}
	
	public TerminalRCM(TerminalTextLine lineIn) {
		super();
		term = lineIn.getTerminal();
		line = lineIn;
		build();
		
		addOption("Copy");
		//addOption("Clear");
		//addOption("Clear History");
		//addOption("Options", EMCResources.guiSettingsButton);
	}
	
	public TerminalRCM(TerminalTextField fieldIn) {
		super();
		term = fieldIn.getTerminal();
		inputField = fieldIn;
		build();
		
		addOption("Copy");
		addOption("Paste");
		//addOption("Clear");
		//addOption("Clear History");
		//addOption("Options", EMCResources.guiSettingsButton);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public boolean isDebugWindow() {
		return true;
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object != this || args.length == 0) return;
		
		if (args[0] instanceof String s) {
			switch (s) {
			case "Copy": copy(); break;
			case "Paste": paste(); break;
			case "Search text..": search(); break;
			case "Clear": clear(); break;
			case "Clear History": clearHistory(); break;
			case "Options": openOptions(); break;
			default: break;
			}
		}
	}
	
	//===================
	// Internal Builders
	//===================
	
	private void build() {
		setRunActionOnPress(true);
		setActionReceiver(this);
		
		setTitle("Terminal");
	}
	
	private void copy() {
		//if it was for a textfield
		if (inputField != null) {
			String text = inputField.getText();
			//text = EChatUtil.removeFormattingCodes(text);
			text = text.trim();
			Keyboard.setClipboard(text);
		}
		//if it was for a textline
		else if (line != null) {
			String text = line.getText();
			if (text.startsWith("> ")) text = text.substring(2);
			//text = EChatUtil.removeFormattingCodes(text);
			text = text.trim();
			Keyboard.setClipboard(text);
		}
	}
	
	private void paste() {
		if (term == null) return;
		if (term.getInputField() == null) return;
		
		String toPaste = Keyboard.getClipboard();
		if (toPaste == null) return;
		
		term.getInputField().writeText(toPaste);
	}
	
	private void search() {
		// LOL
	}
	
	private void clear() {
		term.clear();
	}
	
	private void clearHistory() {
		TerminalCommandHandler.cmdHistory.clear();
		term.writeln("Terminal history cleared..", 0xff55ff55);
	}
	
	private void openOptions() {
		getTopParent().displayWindow(new TerminalOptionsWindow());
	}
	
}
