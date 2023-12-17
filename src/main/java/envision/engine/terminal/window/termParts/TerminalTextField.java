package envision.engine.terminal.window.termParts;

import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.terminal.window.TerminalRCM;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowUtil.ObjectPosition;

//Author: Hunter Bragg

public class TerminalTextField extends WindowTextField {

	protected TerminalCommandHandler handler;
	protected ETerminalWindow term;
	protected boolean isTabCompleting = false;
	
	public TerminalTextField(ETerminalWindow termIn, double xIn, double yIn, double widthIn, double heightIn) {
		super(termIn, xIn, yIn, widthIn, heightIn);
		term = termIn;
		handler = TerminalCommandHandler.getInstance();
		setUseObjectGroupForCursorDraws(true);
		setDrawShadowed(false);
	}
	
	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		super.drawObject(dt, mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (button == 1) { getTopParent().displayWindow(new TerminalRCM(this), ObjectPosition.CURSOR_CORNER); }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 258) { //tab
			performAction("tab");
		}
		
		if (keyCode == 259) { //backspace
			term.resetTab();
			//if (term.getTab1()) { term.setTab1(false); }
			//term.setTextTabBeing("");
		}
		
		if (keyCode == 92 || keyCode == 47) { // backslash and slash
			term.resetTab();
		}
		
		if (keyCode == 257) { //enter
			term.historyLine = 0;
			term.preservedInput = "";
			term.resetTab();
		}
		
		if (keyCode == 32) { //space
			term.resetTab();
		}
		
		if (keyCode == 263) { //left
			term.resetTab();
		}
		
		if (keyCode == 262) { //right
			term.resetTab();
		}
		
		if (keyCode == 265) { //up
			if (term.historyLine < TerminalCommandHandler.cmdHistory.size()) {
				if (term.lastUsed == 0) {
					term.historyLine += 1;
					setText(getHistoryLine(term.historyLine));
					term.historyLine += 1;
				} else {
					setText(getHistoryLine(term.historyLine));
					term.historyLine += 1;
				}
			}
			term.lastUsed = 1;
			
			term.resetTab();
		}
		else if (keyCode == 264) { //down
			if (term.historyLine <= TerminalCommandHandler.cmdHistory.size() && term.historyLine > 1) {
				if (term.historyLine == TerminalCommandHandler.cmdHistory.size() || term.lastUsed == 1) { term.historyLine -= 2; }
				else { term.historyLine -= 1; }
				setText(getHistoryLine(term.historyLine));
				term.lastUsed = 0;
			} else if (term.historyLine <= 1) { 
				setText(term.preservedInput);
				term.lastUsed = 2;
			}
			
			term.resetTab();
		}
		else {
			super.keyPressed(typedChar, keyCode);
			if (term.historyLine <= 0) { term.preservedInput = getText(); }
		}
		
		//if (text.isEmpty()) { term.resetTab(); }
	}
	
	@Override
	public void onMiddleClick() {
	    super.onMiddleClick();
	}
	
	public ETerminalWindow getTerminal() { return term; }
	
	private String getHistoryLine(int lineNum) {
		if (!TerminalCommandHandler.cmdHistory.isEmpty()) {
			if (lineNum == 0) { return TerminalCommandHandler.cmdHistory.get(TerminalCommandHandler.cmdHistory.size() - 1); }
			else if (lineNum > 0) { return TerminalCommandHandler.cmdHistory.get(TerminalCommandHandler.cmdHistory.size() - (lineNum + 1)); }
		}
		return null;
	}
}
