package engine.terminal.window;

import java.io.File;

import assets.textures.TaskBarTextures;
import engine.terminal.TerminalHandler;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.termParts.TerminalTextField;
import engine.terminal.window.termParts.TerminalTextLine;
import engine.windowLib.windowObjects.advancedObjects.textArea.TextAreaLine;
import engine.windowLib.windowObjects.advancedObjects.textArea.WindowTextArea;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowUtil.EObjectGroup;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.MouseType;
import engine.windowLib.windowUtil.windowEvents.events.EventFocus;
import engine.windowLib.windowUtil.windowEvents.events.EventMouse;
import envision._launch.EnvisionConsoleReceiver;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.Box3;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;
import eutil.misc.ScreenLocation;
import eutil.strings.StringUtil;
import main.QoT;

//Author: Hunter Bragg

public class ETerminal<E> extends WindowParent<E> implements EnvisionConsoleReceiver {
	
	TerminalTextField inputField;
	WindowTextArea history;
	boolean init = false;
	public int historyLine = 0;
	public int lastUsed = 2;
	public String preservedInput = "";
	File dir;
	public String textTabBegin = "";
	public boolean tab1 = false;
	public int tabPos = -1;
	public int startArgPos = -1;
	public String tabBase = "";
	public EArrayList<String> tabData = new EArrayList();
	EArrayList<TextAreaLine> tabDisplayLines = new EArrayList();
	boolean isCommand = false;
	protected boolean isChat = false;
	
	private String text = "";
	private int textPos = 0;
	double vPos = 0, hPos = 0;
	EArrayList<TextAreaLine> lines;
	
	public boolean requireConfirmation = false;
	public String confirmationMessage = "";
	public EArrayList<String> previousArgs = null;
	public boolean prevRunVisually = false;
	public TerminalCommand confirmationCommand = null;
	
	public ETerminal() {
		super();
		aliases.add("terminal", "console", "term");
		dir = new File(System.getProperty("user.dir"));
		windowIcon = TaskBarTextures.terminal;
	}
	
	//--------------------------
	// Envision Terminal Output
	//--------------------------
	
	@Override
	public void onEnvisionPrint(String line) {
		writeln(line);
	}
	
	@Override
	public void onEnvisionPrintln(String line) {
		writeln(line);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initWindow() {
		setObjectName("Terminal");
		int w = NumberUtil.clamp(1150, 200, QoT.getWindowSize().getWidth());
		int h = NumberUtil.clamp(600, 200, QoT.getWindowSize().getHeight());
		setSize(w, h);
		setMinDims(480, 285);
		setResizeable(true);
		setMaximizable(true);
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		inputField = new TerminalTextField(this, startX + 1, endY - 31, width - 2, 30);
		history = new WindowTextArea(this, startX + 1, startY + 1, width - 2, height - 1 - inputField.height) {
			
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				super.mousePressed(mXIn, mYIn, button);
				EUtil.nullDo(getWindowParent(), w -> w.bringToFront());
				if (button == 1) {
					windowInstance.bringToFront();
					windowInstance.mousePressed(mXIn, mYIn, button);
					//Game.displayWindow(new TerminalRCM(ETerminal.this), CenterType.cursorCorner);
				}
				else {
					inputField.requestFocus();
				}
			}
		};
		
		inputField.setBackgroundColor(EColors.black);
		inputField.setBorderColor(0xff222222);
		inputField.setTextColor(EColors.lgray);
		inputField.setMaxStringLength(255);
		
		history.setBackgroundColor(0xff000000);
		history.setBorderColor(0xff222222);
		//history.setDrawLineNumbers(CoreApp.termLineNumbers.get());
		history.setLineNumberSeparatorColor(EColors.vdgray);
		history.setResetDrawn(false);
		
		var objectGroup = new EObjectGroup(this);
		objectGroup.addObject(header, history, inputField);
		setObjectGroup(objectGroup);
		header.setObjectGroup(objectGroup);
		history.setObjectGroup(objectGroup);
		inputField.setObjectGroup(objectGroup);
		
		addChild(inputField, history);
		
		if (!init) {
			//history.addTextLine("EMC Terminal initialized..", 0xffff00);
			//history.addTextLine();
			init = true;
		}
		
		if (getTopParent().getModifyingObject() != this) { inputField.requestFocus(); }
	}
	
	@Override
	public void preReInit() {
		text = inputField.getText();
		textPos = inputField.getCursorPosition();
		vPos = history.getVScrollBar().getScrollPos();
		hPos = history.getHScrollBar().getScrollPos();
		
		lines = history.getTextDocument();
		clearTabCompletions();
		
		history.clear();
	}
	
	@Override
	public void postReInit() {
		for (TextAreaLine l : lines) history.addTextLine(l);
		history.getVScrollBar().setScrollPos(vPos);
		history.getHScrollBar().setScrollPos(hPos);
		inputField.setText(text);
		inputField.setCursorPos(textPos);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (header != null) header.setTitleColor(-EColors.rainbow() + 0xff222222);
		
		getHeader().setTitle("Terminal");
		
		drawRect(startX, startY, endX, endY, 0xff000000);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (inputField != null) { inputField.requestFocus(); }
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		//if (button == 1) getTopParent().displayWindow(new TerminalRCM(this), ObjectPosition.CURSOR_CORNER);
		//else inputField.requestFocus();
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		inputField.requestFocus();
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MOUSE_PRESS))
			mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode());
		else inputField.requestFocus();
	}
	
	@Override
	public void onGroupNotification(ObjectEvent e) {
		if (e instanceof EventMouse) {
			EventMouse m = (EventMouse) e;
			if (m.getMouseType() == MouseType.PRESSED) {
				bringToFront();
				if (e.getEventParent() instanceof WindowTextArea || e.getEventParent() instanceof TextAreaLine) {
					if (inputField != null) { inputField.requestFocus(); }
				}
			}
			if (m.getMouseType() == MouseType.RELEASED) {
				if (inputField != null) { inputField.requestFocus(); }
			}
		}
	}
	
	@Override
	public void resize(double xIn, double yIn, ScreenLocation areaIn) {
		try {
			if (getMaximizedPosition() != ScreenLocation.TOP && (xIn != 0 || yIn != 0)) {
				String text = inputField.getText();
				double vPos = history.getVScrollBar().getScrollPos();
				double hPos = history.getHScrollBar().getScrollPos();
				
				EArrayList<TextAreaLine> lines = new EArrayList();
				for (TextAreaLine l : new EArrayList<TextAreaLine>(history.getTextDocument())) {
					if (tabDisplayLines.notContains(l)) { lines.add(l); }
				}
				clearTabCompletions();
				
				history.clear();
				super.resize(xIn, yIn, areaIn);
				
				for (TextAreaLine l : lines) {
					TerminalTextLine n = new TerminalTextLine(this, l.getText(), l.textColor, l.getGenericObject(), l.getLineNumber());
					Box3<String, Object, Boolean> link = l.getLink();
					n.setLinkText(link.a, link.b, link.c);
					history.addTextLine(n);
				}
				
				history.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
				history.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
				inputField.setText(text);
				
				setPreMax(getDimensions());
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == inputField) {
			String cmd = inputField.getText();
			
			boolean isTab = false;
			for (Object o : args) {
				if (o.equals("tab")) { isTab = true; break; }
			}
			
			try {
				if (!cmd.isEmpty()) {
					if (requireConfirmation) {
						writeln(cmd);
						if (cmd.equals("y") || cmd.equals("n")) {
							confirmationCommand.onConfirmation(cmd);
							confirmationCommand.runCommand(this, previousArgs, prevRunVisually);
							clearConfirmationRequirement();
						}
						else {
							error("Invalid input! Type either 'y' or 'n'");
						}
						TerminalHandler.cmdHistory.add(cmd);
						inputField.setText("");
						scrollToBottom();
					}
					else {
						if (cmd.startsWith("/")) {
							TerminalHandler.cmdHistory.add(cmd);
							
							//if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, cmd) == 0) {
							//	mc.thePlayer.sendChatMessage(cmd);
							//}
							
							scrollToBottom();
							inputField.clear();
						}
						else if (isChat && cmd.startsWith("\\")) {
							TerminalHandler.cmdHistory.add(cmd);
							
							cmd = cmd.substring(1);
							
							boolean isClear = (cmd.equals("clear") || cmd.equals("clr") || cmd.equals("cls"));
							boolean isSetChat = (cmd.startsWith("setchat") || cmd.startsWith("chat"));
							
							if (!isClear && !isSetChat) {
								writeln("> " + cmd, 0xffffffff);
							}
							
							TerminalHandler.getInstance().executeCommand(this, cmd, false, !isSetChat);
							
							inputField.clear();
							scrollToBottom();
							tab1 = false;
							tabData.clear();
						}
						else if (isChat) {
							TerminalHandler.cmdHistory.add(cmd);
							//EChatUtil.sendLongerChatMessage(cmd);
							//mc.ingameGUI.getChatGUI().addToSentMessages(cmd);
							scrollToBottom();
							inputField.clear();
						}
						else if (isTab) { handleTab(); }
						else {
							boolean isClear = (cmd.equals("clear") || cmd.equals("clr") || cmd.equals("cls"));
							boolean isSetChat = (cmd.startsWith("setchat") || cmd.startsWith("chat"));
							
							if (!isClear && !isSetChat) {
								writeln("> " + cmd, 0xffffffff);
							}
							
							TerminalHandler.cmdHistory.add(cmd);
							TerminalHandler.getInstance().executeCommand(this, cmd, false, !isSetChat);
							inputField.clear();
							scrollToBottom();
							tab1 = false;
							tabData.clear();
						}
					}
				}
				else if (isTab) { handleTab(); }
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	@Override
	public void setPosition(double newX, double newY) {
		super.setPosition(newX, newY);
	}
	
	@Override
	public void maximize() {
		try {
			super.maximize();
			
			String text = inputField.getText();
			double vPos = history.getVScrollBar().getScrollPos();
			double hPos = history.getHScrollBar().getScrollPos();
			
			EArrayList<TextAreaLine> lines = new EArrayList();
			for (TextAreaLine l : new EArrayList<TextAreaLine>(history.getTextDocument())) {
				if (tabDisplayLines.notContains(l)) { lines.add(l); }
			}
			clearTabCompletions();
			
			history.clear();
			reInitChildren();
			
			for (TextAreaLine l : lines) {
				TerminalTextLine n = new TerminalTextLine(this, l.getText(), l.textColor, l.getGenericObject(), l.getLineNumber());
				history.addTextLine(n);
			}
			
			history.getVScrollBar().setScrollPos(vPos);
			history.getHScrollBar().setScrollPos(hPos);
			inputField.setText(text);
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void miniaturize() {
		setDimensions(getPreMax());
		
		String text = inputField.getText();
		double vPos = history.getVScrollBar().getScrollPos();
		//double hPos = history.getHScrollBar().getScrollPos();
		
		EArrayList<TextAreaLine> lines = new EArrayList();
		for (TextAreaLine l : new EArrayList<TextAreaLine>(history.getTextDocument())) {
			if (tabDisplayLines.notContains(l)) { lines.add(l); }
		}
		clearTabCompletions();
		
		history.clear();
		reInitChildren();
		
		for (TextAreaLine l : lines) {
			TerminalTextLine n = new TerminalTextLine(this, l.getText(), l.textColor, l.getGenericObject(), l.getLineNumber());
			history.addTextLine(n);
		}
		
		history.getVScrollBar().setScrollPos(vPos);
		history.getHScrollBar().setScrollPos(0);
		inputField.setText(text);
	}
	
	@Override public boolean isDebugWindow() { return true; }
	
	@Override
	public void close() {
		super.close();
		//if (isChat) { EnhancedMC.getEMCApp().unregisterChatTerminal(this); }
	}
	
	//-----------------
	//ETerminal Methods
	//-----------------
	
	/*
	public void onChat(TimedChatLine lineIn) {
		if (isChat && lineIn != null) {
			String msgIn = "[" + lineIn.getTimeStamp() + "] " + lineIn.getChatComponent().getFormattedText();
			
			String[] lines = msgIn.split("[\\r\\n]+", -1);
			
			boolean atBottom = history.getVScrollBar().getScrollPos() == history.getVScrollBar().getHighVal();
			
			for (String s : lines) {
				TerminalTextLine line = new TerminalTextLine(this, s, EColors.lgray);
				line.setFocusRequester(this).setObjectGroup(objectGroup);
				history.addTextLine(line);
			}
			
			if (atBottom) { scrollToBottom(); }
		}
	}
	*/
	
	public ETerminal scrollToBottom() {
		history.getVScrollBar().setScrollPos(history.getVScrollBar().getHighVal());
		return this;
	}
	
	public ETerminal scrollToTop() {
		history.getVScrollBar().setScrollPos(0);
		return this;
	}
	
	public void handleTab() {
		String input = inputField.getText();
		//String command = StringUtil.subStringToSpace(input, 0);
		
		//System.out.println("arg: " + startArgPos + " " + getCurrentArg() + " " + isCommand + " " + command + " " + tab1);
		
		try {
			if (!input.isEmpty()) {
				//only test on a command if the starting input wasn't at arg 0 or if it was at -1
				if (!isCommand && getCurrentArg() >= 1) {
					TerminalHandler.getInstance().executeCommand(this, input, true);
				}
				else if (startArgPos == -1 || getCurrentArg() == 0 && !tab1) { //build completions off of partial command input
					if (!isCommand) {
						isCommand = true;
						try {
							EArrayList<String> options = new EArrayList();
							
							for (String s : TerminalHandler.getSortedCommandNames()) {
								if (s.startsWith(input)) { options.add(s); }
							}
						
							buildTabCompletions(options);
						}
						catch (IndexOutOfBoundsException e) {}
						catch (Exception e) { e.printStackTrace(); }
					}
				}
				
				//set tab true only after parsing the first if condition
				if (!tab1) { tab1 = true; }
				
				//only run if there is tab data to iterate over
				if (tabPos >= 0 && tabData.isNotEmpty()) {
					
					if (isCommand) {
						inputField.setText(tabBase + tabData.get(tabPos));
					}
					else if (startArgPos >= 0) { //determine where we are getting tab completion data from
						
						//grab everything up to the argument being tabbed
						String f = "";
						for (int i = 0, spaces = 0; i < input.length(); i++) {
							if (spaces == startArgPos && i > 0) { f = input.substring(0, i - 1); break; }
							else if (input.charAt(i) == ' ') { spaces++; }
							
							if (i == input.length() - 1) { f = input.trim(); }
						}
						
						//append the next tab completion onto the previous string
						String tabCompletion = (startArgPos > 0) ? " " + tabBase + tabData.get(tabPos) : tabBase + tabData.get(tabPos);
						f += tabCompletion;
						
						//set the terminal's output to the original text with the new tab completion
						inputField.setText(f);
					}
					else {
						inputField.setText(tabBase + tabData.get(tabPos));
					}
					
					//update tab position
					if (tabPos == tabData.size() - 1) { tabPos = 0; }
					else { tabPos++; }
				}
			}
			else {
				if (!tab1) {
					EArrayList<String> cnames = TerminalHandler.getSortedCommandNames();
					for (String s : cnames) { s = s.toLowerCase(); }
					
					buildTabCompletions(cnames);
					inputField.setText(tabBase + tabData.get(tabPos));
					
					//update tab position
					if (tabPos == tabData.size() - 1) { tabPos = 0; }
					else { tabPos++; }
					
					tab1 = true;
				}
				else {
					System.out.println("c: " + startArgPos);
				}
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public ETerminal buildTabCompletions(String... dataIn) { return buildTabCompletions(new EArrayList().addA(dataIn)); }
	public ETerminal buildTabCompletions(EArrayList<String> dataIn) {
		clearTabCompletions();
		
		if (dataIn.isNotEmpty()) {
			if (!tab1) {
				tabData = new EArrayList(dataIn);
				tabPos = 0;
				startArgPos = getCurrentArg();
			}
			
			int textWidth = 0;
			int maxData = 0;
			int spaceAmount = 3;
			int longest = 0;
			int longestLen = 0;
			
			for (String s : dataIn) {
				int len = getStringWidth(s);
				if (len > longest) {
					longest = len;
					longestLen = s.length();
				}
			}
			
			//determine the maximum number of autocomplete options that can fit on one line
			for (int i = 1; i < dataIn.size() + 1; i++) {
				textWidth += longest + getStringWidth(StringUtil.repeatString(" ", spaceAmount));
				if (textWidth < width) {
					maxData = i;
				}
				else break;
			}
			
			//System.out.println("maxData: " + maxData);
			maxData = NumberUtil.clamp(maxData, 1, Integer.MAX_VALUE);
			
			//position each autocomplete option on one line up to the max line width
			int amount = dataIn.size();
			int i = 0;
			int cur = 1;
			String line = "";
			
			while (amount > 0) {
				//System.out.println(amount + " " + cur + " " + i + " " + maxData + " " + (dataIn.size() == i));
				line += dataIn.get(i) + ", ";
				
				//System.out.println(line);
				//System.out.println((cur == maxData) + " " + (amount == 1) + "\n");
				if (cur == maxData || amount == 1) {
					//if (amount == 0) { line += dataIn.get(i) + ", "; cur++; }
					try {
						String format = StringUtil.repeatString("%-" + longestLen + "s" + StringUtil.repeatString(" ", spaceAmount), cur);
						String[] args = line.split(", ");
						line = String.format(format, (Object[]) args);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					if (!line.isEmpty()) tabDisplayLines.add(new TextAreaLine(history, line, EColors.lgray.intVal));
					line = "";
					cur = 0;
				}
				
				amount--;
				cur++;
				i++;
			}
			
			//add each created line to the grid
			for (TextAreaLine l : tabDisplayLines) {
				history.addTextLine(l);
			}
			scrollToBottom();
		}
		return this;
	}
	
	public ETerminal clearTabCompletions() {
		for (TextAreaLine l : tabDisplayLines) {
			history.deleteLine(l);
		}
		
		tabDisplayLines.clear();
		
		return this;
	}
	
	public ETerminal clear() { history.clear(); return this; }
	public ETerminal clearTabData() { tabData.clear(); return this; }
	
	public int getCurrentArg() {
		int arg = 0;
		
		if (inputField.isNotEmpty()) {
			int spaces = StringUtil.countSpaces(inputField.getText());
			
			if (spaces == 0) { arg = 0; }
			else { arg = spaces; }
		}
		
		return arg;
	}
	
	public void resetTab() {
		double scroll = history.getVScrollBar().getScrollPos();
		clearTabCompletions();
		history.getVScrollBar().setScrollPos(scroll);
		
		tab1 = false;
		textTabBegin = "";
		tabBase = "";
		tabData.clear();
		startArgPos = isCommand ? 0 : -1;
		isCommand = false;
	}
	
	public ETerminal writeln() { return writeln("", 0xffffffff); }
	public ETerminal writeln(Object objIn) { return writeln(objIn != null ? objIn.toString() : "null", 0xffffffff); }
	public ETerminal writeln(Object objIn, EColors colorIn) { return writeln(objIn != null ? objIn.toString() : "null", colorIn.intVal); }
	public ETerminal writeln(Object objIn, int colorIn) { return writeln(objIn != null ? objIn.toString() : "null", colorIn); }
	public ETerminal writeln(String msgIn) { return writeln(msgIn, 0xffffffff); }
	public ETerminal writeln(String msgIn, EColors colorIn) { return writeln(msgIn, colorIn.intVal); }
	public ETerminal writeln(String msgIn, int colorIn) {
		parseText(msgIn, colorIn);
		return this;
	}
	
	public ETerminal info(String msgIn) { parseText(msgIn, 0xffffff00); return this; }
	public ETerminal warn(String msgIn) { parseText("Warning: " + msgIn, EColors.orange.intVal); return this; }
	public ETerminal error(String msgIn) { parseText(msgIn, 0xffff5555); return this; }
	public ETerminal javaError(String msgIn) { parseText(msgIn, 0xffff0000); return this; }
	
	private void parseText(String msgIn, int colorIn) {
		String[] lines = msgIn.split("[\\r\\n]+", -1);
		
		for (String s : lines) {
			TerminalTextLine line = new TerminalTextLine(this, s, colorIn);
			line.setFocusRequester(this);
			line.setObjectGroup(getObjectGroup());
			history.addTextLine(line);
			
		}
		
		scrollToBottom();
	}
	
	public ETerminal writeLink(String msgIn, String linkTextIn, EColors colorIn) { return writeLink(msgIn, linkTextIn, null, false, colorIn.intVal); }
	public ETerminal writeLink(String msgIn, String linkTextIn, int colorIn) { return writeLink(msgIn, linkTextIn, null, false, colorIn); }
	public ETerminal writeLink(String msgIn, String linkTextIn, boolean isWebLink, EColors colorIn) { return writeLink(msgIn, linkTextIn, linkTextIn, isWebLink, colorIn.intVal); }
	public ETerminal writeLink(String msgIn, String linkTextIn, boolean isWebLink, int colorIn) { return writeLink(msgIn, linkTextIn, linkTextIn, isWebLink, colorIn); }
	public ETerminal writeLink(String msgIn, String linkTextIn, Object linkObjectIn, boolean isWebLink, EColors colorIn) { return writeLink(msgIn, linkTextIn, linkObjectIn, isWebLink, colorIn.intVal); }
	public ETerminal writeLink(String msgIn, String linkTextIn, Object linkObjectIn, boolean isWebLink, int colorIn) {
		TerminalTextLine line = new TerminalTextLine(this, msgIn, colorIn);
		line.setLinkText(linkTextIn, linkObjectIn, isWebLink);
		line.setFocusRequester(this);
		line.setObjectGroup(getObjectGroup());
		history.addTextLine(line);
		return this;
	}
	
	//-----------------
	//ETerminal Getters
	//-----------------
	
	public int getLastUsed() { return lastUsed; }
	public int getHisLine() { return historyLine; }
	public WindowTextArea getTextArea() { return history; }
	public TerminalTextField getInputField() { return inputField; }
	public File getDir() { return dir; }
	public EArrayList<TextAreaLine> getInfoLines() { return tabDisplayLines; }
	public int getTabPos() { return tabPos; }
	public boolean getTab1() { return tab1; }
	public String getTextTabBegin() { return textTabBegin; }
	public int getTabArgStart() { return startArgPos; }
	public String getTabBase() { return tabBase; }
	
	//-----------------
	//ETerminal Setters
	//-----------------
	
	public ETerminal setRequiresCommandConfirmation(TerminalCommand commandIn, String message, EArrayList<String> args, boolean runVisually) {
		if (commandIn != null) {
			requireConfirmation = true;
			confirmationCommand = commandIn;
			previousArgs = args;
			prevRunVisually = runVisually;
			confirmationMessage = message != null && !message.isEmpty() ? message : "Are you sure you wish to continue? (Y, N)";
			
			warn(message);
		}
		return this;
	}
	
	public ETerminal clearConfirmationRequirement() {
		requireConfirmation = false;
		confirmationCommand = null;
		previousArgs = null;
		prevRunVisually = false;
		confirmationMessage = "";
		return this;
	}
	
	/*
	public ETerminal setChatTerminal(boolean val) {
		boolean old = isChat;
		isChat = val;
		
		if (isInit()) {
			//clear();
			
			WindowHeader header = getHeader();
			
			if (val && !old) {
				//EnhancedMC.getEMCApp().registerChatTerminal(this);
				
				if (header != null) {
					header.setTitle(getObjectName());
				}
				
				for (TimedChatLine l : EChatUtil.getChatHistory()) {
					writeln("[" + l.getTimeStamp() + "] " + l.getChatComponent().getFormattedText(), EColors.lgray);
				}
				
				inputField.setMaxStringLength(!mc.isSingleplayer() ? (EnhancedMC.isHypixel() ? 255 : 100) : 255);
			}
			else if (old && !val) {
				EnhancedMC.getEMCApp().unregisterChatTerminal(this);
				
				if (header != null) {
					header.setTitle(getObjectName());
				}
				
				writeln("EMC " + (EnhancedMC.isDevMode() ? "Dev " : "") + "Terminal v1.0 initialized..", 0xffff00);
				writeln();
				
				inputField.setMaxStringLength(100);
			}
		}
		
		return this;
	}
	*/
	
	public ETerminal setDir(File dirIn) { dir = dirIn; return this; }
	public synchronized ETerminal setInputEnabled(boolean val) { inputField.setEnabled(val); return this; }
	public ETerminal setLastUsed(int in) { lastUsed = in; return this; }
	public ETerminal setHistoryLine(int in) { historyLine = in; return this; }
	public ETerminal setTabPos(int in) { tabPos = in; return this; }
	public ETerminal setTab1(boolean val) { tab1 = val; return this; }
	public ETerminal setTextTabBeing(String in) { textTabBegin = in; return this; }
	public ETerminal setTabBase(String in) { tabBase = in; return this; }
	
	public boolean isChatTerminal() { return isChat; }
	
}
