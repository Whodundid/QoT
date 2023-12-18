package envision.engine.terminal.window;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Collection;

import envision.Envision;
import envision.engine.EngineSettings;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.termParts.TerminalTextField;
import envision.engine.terminal.window.termParts.TerminalTextLine;
import envision.engine.windows.bundledWindows.fileExplorer.MovingFileObject;
import envision.engine.windows.windowObjects.advancedObjects.textArea.TextAreaLine;
import envision.engine.windows.windowObjects.advancedObjects.textArea.WindowTextArea;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowUtil.EObjectGroup;
import envision.engine.windows.windowUtil.ObjectPosition;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.FocusType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.events.EventDragAndDrop;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import envision_lang._launch.EnvisionConsoleOutputReceiver;
import envision_lang._launch.EnvisionLangErrorCallBack;
import envision_lang.lang.java.annotations.EClass;
import envision_lang.lang.language_errors.EnvisionLangError;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.Box3;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.misc.ScreenLocation;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;
import qot.assets.textures.taskbar.TaskBarTextures;

//Author: Hunter Bragg

@EClass
public class ETerminalWindow extends WindowParent implements EnvisionConsoleOutputReceiver, EnvisionLangErrorCallBack {
	
	public static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();
	
	protected TerminalTextField inputField;
	protected WindowTextArea<String> history;
	protected boolean init = false;
	protected File dir;
	protected String textTabBegin = "";
	protected boolean tab1 = false;
	protected int tabPos = -1;
	protected int startArgPos = -1;
	protected String tabBase = "";
	protected EList<String> tabData = EList.newList();
	protected EList<TextAreaLine<String>> tabDisplayLines = EList.newList();
	protected boolean isCommand = false;
	
	protected String text = "";
	protected int textPos = 0;
	protected double vPos = 0, hPos = 0;
	protected EList<? extends TextAreaLine<String>> lines;
	
	public int historyLine = 0;
	public int lastUsed = 2;
	public String preservedInput = "";
	
	protected boolean requireConfirmation = false;
	protected String confirmationMessage = "";
	protected EList<String> previousArgs = null;
	protected boolean prevRunVisually = false;
	protected TerminalCommand confirmationCommand = null;
	
	protected boolean drawNewLineBetweenCommands = true;
	
	//==============
	// Constructors
	//==============
	
	public ETerminalWindow() {
		super();
		aliases.add("terminal", "console", "term");
		dir = new File(System.getProperty("user.dir"));
		windowIcon = TaskBarTextures.terminal;
	}
	
	//==========================
	// Envision Terminal Output
	//==========================
	
	@Override
	public void onEnvisionPrint(String line) {
		writeln(line);
	}
	
	@Override
	public void onEnvisionPrintln(String line) {
		writeln(line);
	}
	
	@Override
	public void onEnvisionError(EnvisionLangError error) {
	    error(error);
	}
	
	@Override
	public void onJavaException(Exception exception) {
	    javaError(exception);
	}
	
	//===========
	// Overrides
	//===========
	
    @Override
    public String getWindowName() {
        return "terminal";
    }
	
	@Override
	public void initWindow() {
		setObjectName("Terminal");
		int w = ENumUtil.clamp(750, 200, Envision.getWidth());
		int h = ENumUtil.clamp(500, 200, Envision.getHeight());
		setGuiSize(w, h);
		setMinDims(480, 285);
		setResizeable(true);
		setMaximizable(true);
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		inputField = new TerminalTextField(this, startX + 1, endY - 31, width - 2, 30);
		history = new WindowTextArea<>(this, startX + 1, startY + 1, width - 2, height - 1 - inputField.height) {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				super.mousePressed(mXIn, mYIn, button);
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
		
		final boolean lineNumbers = EngineSettings.termLineNumbers.get();
		final int background = EngineSettings.termBackground.get();
		final int opacity = EngineSettings.termOpacity.get();
		final int c = EColors.changeOpacity(background, opacity);
		
		inputField.setBackgroundColor(c);
		inputField.setBorderColor(0xff222222);
		inputField.setTextColor(EColors.lgray);
		inputField.setMaxStringLength(255);
		
		history.setBackgroundColor(c);
		history.setBorderColor(0xff222222);
		history.setDrawLineNumbers(lineNumbers);
		history.setLineNumberSeparatorColor(EColors.vdgray);
		history.setResetDrawn(false);
		
		var objectGroup = new EObjectGroup(this);
		objectGroup.addObject(header, history, inputField);
		setObjectGroup(objectGroup);
		header.setObjectGroup(objectGroup);
		history.setObjectGroup(objectGroup);
		inputField.setObjectGroup(objectGroup);
		
		addObject(inputField, history);
		
		if (!init) {
			writeln("QoT Terminal initialized..", EColors.seafoam);
			writeln();
			init = true;
		}
		
		if (getTopParent().getModifyingObject() != this) {
			inputField.requestFocus();
		}
	}
	
	@Override
	public void preReInit() {
		text = inputField.getText();
		textPos = inputField.getCursorPosition();
		vPos = history.getVScrollBar().getScrollPos();
		hPos = history.getHScrollBar().getScrollPos();
		
		lines = EList.of(history.getTextDocument());
		clearTabCompletions();
		
		history.clear();
	}
	
	@Override
	public void postReInit() {
		for (TextAreaLine<String> l : lines) history.addTextLine(l);
		history.getVScrollBar().setScrollPos(vPos);
		history.getHScrollBar().setScrollPos(hPos);
		inputField.setText(text);
		inputField.setCursorPos(textPos);
	}
	
	@Override
	public void drawObject(long dt, int mXIn, int mYIn) {
		if (header != null) {
			header.setTitleColor(-EColors.rainbow() + 0xff222222);
			String dirS = "./" + dir.getName();
			header.setTitle("Terminal " + EColors.yellow + dirS);
		}
		
		var opacity = EngineSettings.termOpacity.get();
		var c = EColors.changeOpacity(0xff000000, opacity);
		drawRect(startX, inputField.startY - 1, endX, inputField.startY, EColors.black);
        drawHRect(EColors.black);
        drawRect(c, 1);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (inputField != null) inputField.requestFocus();
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (button == 1) getTopParent().displayWindow(new TerminalRCM(this), ObjectPosition.CURSOR_CORNER);
		else inputField.requestFocus();
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		inputField.requestFocus();
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MOUSE_PRESS)) {
			mousePressed(eventIn.mX, eventIn.mY, eventIn.actionCode);
		}
		else inputField.requestFocus();
	}
	
	@Override
	public void onGroupNotification(ObjectEvent e) {
	    if (e instanceof EventMouse m) {
	        var mt = m.getMouseType();
	        
	        if (mt == MouseType.PRESSED) {
	            bringToFront();
	            var p = e.getEventParent();
	            if (p instanceof WindowTextArea || p instanceof TextAreaLine && inputField != null) {
	                inputField.requestFocus();
	            }
	        }
	        else if (mt == MouseType.RELEASED && inputField != null) {
	            inputField.requestFocus();
	        }
	    }
        else if (e instanceof EventDragAndDrop d) {
            onDragAndDrop(d.getObjectBeingDropped());
        }
	}
	
    @Override
    public void onDragAndDrop(DragAndDropObject objectBeingDropped) {
        if (!(objectBeingDropped instanceof MovingFileObject)) return;
        
        final var mfo = (MovingFileObject) objectBeingDropped;
        final var files = mfo.getFilesBeingMoved().map(f -> f.getFile());
        
        if (files.size() > 1) return;
        final File theFile = files.getFirst();
        
        if (theFile.isDirectory()) {
            try {
                setDir(theFile.getCanonicalFile());
                String path = dir.getName();
                String colorPath = EColors.mc_aqua + path;
                writeLink("Current Dir: " + colorPath, path, dir, false, EColors.yellow);
                writeln();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	@Override
	public void resize(double xIn, double yIn, ScreenLocation areaIn) {
		if (getMaximizedPosition() == ScreenLocation.TOP) return;
		if (xIn == 0 || yIn == 0) return;
		
		try {
			String text = inputField.getText();
			double vPos = history.getVScrollBar().getScrollPos();
			double hPos = history.getHScrollBar().getScrollPos();
			
			EList<TextAreaLine<?>> lines = new EArrayList<>();
			for (TextAreaLine<?> l : history.getTextDocument()) {
				if (tabDisplayLines.notContains(l)) { lines.add(l); }
			}
			clearTabCompletions();
			
			history.clear();
			super.resize(xIn, yIn, areaIn);
			
			for (TextAreaLine<?> l : lines) {
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
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object != inputField) return;
		
		String cmd = inputField.getText();
		boolean isTab = false;
		
		for (var o : args) {
			if (o.equals("tab")) {
				isTab = true;
				break;
			}
		}
		
		try {
			if (isTab) {
				handleTab();
			}
			else if (requireConfirmation) {
				writeln(cmd);
				
				if (cmd.equals("y") || cmd.equals("n")) {
					confirmationCommand.onConfirmation(cmd);
					confirmationCommand.runCommand_i(this, previousArgs, prevRunVisually);
					clearConfirmationRequirement();
				}
				else {
					error("Invalid input! Type either 'y' or 'n'");
				}
				
				TerminalCommandHandler.cmdHistory.add(cmd);
				inputField.setText("");
				scrollToBottom();
			}
			else {
				boolean isClear = EStringUtil.equalsAny(cmd, "clear", "clr", "cls");
				
				if (!isClear) {
					writeln("> " + cmd, 0xffffffff);
				}
				
				runCommand(cmd);
				inputField.clear();
				tab1 = false;
				tabData.clear();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Executes the following command input in this terminal.
     * 
     * @param input The command to run
     */
    public void runCommand(String input) {
        TerminalCommandHandler.cmdHistory.add(input);
        TerminalCommandHandler.getInstance().executeCommand(this, input, false);
        scrollToBottom();
    }
    
    /**
     * Executes the following command input in this terminal.
     * 
     * @param input The command to run
     */
	public void runCommand(String command, EList<String> arguments) {
	    String argString = EStringUtil.toString(arguments);
	    TerminalCommandHandler.cmdHistory.add(command + ((!argString.isBlank()) ? " " + argString : ""));
	    TerminalCommandHandler.executeOnTerminal(this, command, arguments);
	    scrollToBottom();
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
			
			EList<TextAreaLine<String>> lines = EList.newList();
			for (TextAreaLine<String> l : EList.newList(history.getTextDocument())) {
				if (tabDisplayLines.notContains(l)) lines.add(l);
			}
			clearTabCompletions();
			
			history.clear();
			reInitChildren();
			
			for (TextAreaLine<String> l : lines) {
				TerminalTextLine n = new TerminalTextLine(this, l.getText(), l.textColor, l.getGenericObject(), l.getLineNumber());
				history.addTextLine(n);
			}
			
			history.getVScrollBar().setScrollPos(vPos);
			history.getHScrollBar().setScrollPos(hPos);
			inputField.setText(text);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void miniaturize() {
		setDimensions(getPreMax());
		
		String text = inputField.getText();
		double vPos = history.getVScrollBar().getScrollPos();
		//double hPos = history.getHScrollBar().getScrollPos();
		
		EList<TextAreaLine<String>> lines = EList.newList();
		for (var l : history.getTextDocument()) {
			if (tabDisplayLines.notContains(l)) { lines.add(l); }
		}
		clearTabCompletions();
		
		history.clear();
		reInitChildren();
		
		for (TextAreaLine<String> l : lines) {
			var n = new TerminalTextLine(this, l.getText(), l.textColor, l.getGenericObject(), l.getLineNumber());
			history.addTextLine(n);
		}
		
		history.getVScrollBar().setScrollPos(vPos);
		history.getHScrollBar().setScrollPos(0);
		inputField.setText(text);
	}
	
	@Override
	public boolean isDebugWindow() {
		return true;
	}
	
	//=========
	// Methods
	//=========
	
	public ETerminalWindow scrollToBottom() {
		history.getVScrollBar().setScrollPos(history.getVScrollBar().getHighVal());
		return this;
	}
	
	public ETerminalWindow scrollToTop() {
		history.getVScrollBar().setScrollPos(0);
		return this;
	}
	
	public void handleTab() {
		String input = inputField.getText();
		
		try {
			if (!input.isEmpty()) {
				//only test on a command if the starting input wasn't at arg 0 or if it was at -1
				if (!isCommand && getCurrentArg() >= 1) {
					TerminalCommandHandler.getInstance().executeCommand(this, input, true);
				}
                //build completions off of partial command input
                else if (startArgPos == -1 || getCurrentArg() == 0 && !tab1 && !isCommand) {
                    isCommand = true;
                    try {
                        EList<String> options = TerminalCommandHandler.buildTabCompleteCommandOptions(input);
                        buildTabCompletions(options);
                    }
                    catch (IndexOutOfBoundsException e) {}
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
				
				//set tab true only after parsing the first if condition
				if (!tab1) { tab1 = true; }
				
				//only run if there is tab data to iterate over
				if (tabPos >= 0 && tabData.isNotEmpty()) {
					
					if (isCommand) {
						inputField.setText(tabBase + tabData.get(tabPos));
					}
					//determine where we are getting tab completion data from
					else if (startArgPos >= 0) {
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
					EList<String> cnames = TerminalCommandHandler.getSortedCommandNames();
					//for (String s : cnames) { s = s.toLowerCase(); }
					
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
	
	public ETerminalWindow buildTabCompletions(String... dataIn) {
	    return buildTabCompletions(EList.of(dataIn));
	}
	
	public ETerminalWindow buildTabCompletions(Collection<String> dataIn) {
		clearTabCompletions();
		
		if (dataIn.isEmpty()) return this;
		
		if (!tab1) {
			tabData = EList.newList(dataIn);
			tabPos = 0;
			startArgPos = getCurrentArg();
		}
		
		int textWidth = 0;
		int maxData = 0;
		int spaceAmount = 3;
		double longest = 0;
		int longestLen = 0;
		
		for (String s : dataIn) {
			double len = FontRenderer.strWidth(s);
			if (len > longest) {
				longest = len;
				longestLen = s.length();
			}
		}
		
		//determine the maximum number of auto complete options that can fit on one line
		for (int i = 1; i < dataIn.size() + 1; i++) {
			textWidth += longest + FontRenderer.strWidth(EStringUtil.repeatString(" ", spaceAmount));
			if (textWidth < width) {
				maxData = i;
			}
			else break;
		}
		
		//System.out.println("maxData: " + maxData);
		maxData = ENumUtil.clamp(maxData, 1, Integer.MAX_VALUE);
		
		//position each auto complete option on one line up to the max line width
		int amount = dataIn.size();
		int cur = 1;
		var line = new EStringBuilder();
		
		var it = dataIn.iterator();
		while (amount > 0 && it.hasNext()) {
			line.a(it.next(), ", ");
			
			if (cur == maxData || amount == 1) {
				try {
					String format = EStringUtil.repeatString("%-" + longestLen + "s" + EStringUtil.repeatString(" ", spaceAmount), cur);
					String[] args = line.splitA(", ");
					line = new EStringBuilder(String.format(format, (Object[]) args));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				if (!line.isEmpty()) {
					var nl = new TextAreaLine<>(history, line.toString(), EColors.lgray.intVal);
					tabDisplayLines.add(nl);
				}
				line.setString("");
				cur = 0;
			}
			
			amount--;
			cur++;
		}
		
		//add each created line to the grid
		for (TextAreaLine<String> l : tabDisplayLines) {
			history.addTextLine(l);
		}
		
		scrollToBottom();
		
		return this;
	}
	
	public ETerminalWindow clearTabCompletions() {
		for (TextAreaLine<String> l : tabDisplayLines) {
			history.deleteLine(l);
		}
		
		tabDisplayLines.clear();
		scrollToBottom();
		
		return this;
	}
	
	public ETerminalWindow clear() { history.clear(); return this; }
	public ETerminalWindow clearTabData() { tabData.clear(); return this; }
	
	public int getCurrentArg() {
		int arg = 0;
		
		if (inputField.isNotEmpty()) {
			int spaces = EStringUtil.countSpaces(inputField.getText());
			arg = (spaces == 0) ? 0 : spaces;
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
	
	public ETerminalWindow writeln() { return writeln(0xffffffff, ""); }
	public ETerminalWindow writeln(Object objIn) { return writeln(objIn != null ? objIn.toString() : "null", 0xffffffff); }
	public ETerminalWindow writeln(Object objIn, EColors colorIn) { return writeln(objIn != null ? objIn.toString() : "null", colorIn.intVal); }
	public ETerminalWindow writeln(Object objIn, int colorIn) {
		parseText(colorIn, objIn);
		return this;
	}
	
	public ETerminalWindow writeln(EColors color, Object... arguments) {
		writeln(EStringBuilder.ofR(arguments), color.intVal);
		return this;
	}
	
	/** Object Integer to distinguish between writeln(color, arguments) and writeln(arguments). */
	public ETerminalWindow writeln(Integer color, Object... arguments) {
		writeln(EStringBuilder.ofR(arguments), color);
		return this;
	}
	
	public ETerminalWindow writeln(Object... arguments) {
		writeln(EStringBuilder.ofR(arguments));
		return this;
	}
	
	public ETerminalWindow errorUsage(String error, String usage) {
		error(error);
		info(usage);
		return this;
	}
	
	public ETerminalWindow info(Object... msgIn) { parseText(0xffffff00, msgIn); return this; }
	public ETerminalWindow warn(Object... msgIn) { parseText(EColors.orange.intVal, msgIn); return this; }
	public ETerminalWindow error(Object... msgIn) { parseText(0xffff5555, msgIn); return this; }
	public ETerminalWindow javaError(Object... msgIn) { parseText(0xffff0000, msgIn); return this; }
	
	private void parseText(int color, Object... msgIn) {
		parseText(EStringUtil.combineAll(msgIn, ""), color);
	}
	
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
	
	public ETerminalWindow writeLink(String msgIn, String linkTextIn, EColors colorIn) { return writeLink(msgIn, linkTextIn, null, false, colorIn.intVal); }
	public ETerminalWindow writeLink(String msgIn, String linkTextIn, int colorIn) { return writeLink(msgIn, linkTextIn, null, false, colorIn); }
	public ETerminalWindow writeLink(String msgIn, String linkTextIn, boolean isWebLink, EColors colorIn) { return writeLink(msgIn, linkTextIn, linkTextIn, isWebLink, colorIn.intVal); }
	public ETerminalWindow writeLink(String msgIn, String linkTextIn, boolean isWebLink, int colorIn) { return writeLink(msgIn, linkTextIn, linkTextIn, isWebLink, colorIn); }
	public ETerminalWindow writeLink(String msgIn, String linkTextIn, Object linkObjectIn, boolean isWebLink, EColors colorIn) { return writeLink(msgIn, linkTextIn, linkObjectIn, isWebLink, colorIn.intVal); }
	public ETerminalWindow writeLink(String msgIn, String linkTextIn, Object linkObjectIn, boolean isWebLink, int colorIn) {
		TerminalTextLine line = new TerminalTextLine(this, msgIn, colorIn);
		line.setLinkText(linkTextIn, linkObjectIn, isWebLink);
		line.setFocusRequester(this);
		line.setObjectGroup(getObjectGroup());
		history.addTextLine(line);
		return this;
	}
	
	//=========
	// Getters
	//=========
	
	public int getLastUsed() { return lastUsed; }
	public int getHistoryLine() { return historyLine; }
	public WindowTextArea<String> getTextArea() { return history; }
	public TerminalTextField getInputField() { return inputField; }
	public File getDir() { return dir; }
	public EList<TextAreaLine<String>> getInfoLines() { return tabDisplayLines; }
	public int getTabPos() { return tabPos; }
	public boolean getTab1() { return tab1; }
	public String getTextTabBegin() { return textTabBegin; }
	public int getTabArgStart() { return startArgPos; }
	public String getTabBase() { return tabBase; }
	public boolean getDrawNewLineBetweenCommands() { return drawNewLineBetweenCommands; }
	
	//=========
	// Setters
	//=========
	
	public ETerminalWindow setRequiresCommandConfirmation(TerminalCommand commandIn, String message, EList<String> args, boolean runVisually) {
		if (commandIn == null) return this;
		
		requireConfirmation = true;
		confirmationCommand = commandIn;
		previousArgs = args;
		prevRunVisually = runVisually;
		confirmationMessage = message != null && !message.isEmpty() ? message : "Are you sure you wish to continue? (Y, N)";
		
		warn(message);
		
		return this;
	}
	
	public ETerminalWindow clearConfirmationRequirement() {
		requireConfirmation = false;
		confirmationCommand = null;
		previousArgs = null;
		prevRunVisually = false;
		confirmationMessage = "";
		return this;
	}
	
	public ETerminalWindow setDir(File dirIn) { dir = dirIn; return this; }
	public ETerminalWindow setInputEnabled(boolean val) { inputField.setEnabled(val); return this; }
	public ETerminalWindow setLastUsed(int in) { lastUsed = in; return this; }
	public ETerminalWindow setHistoryLine(int in) { historyLine = in; return this; }
	public ETerminalWindow setTabPos(int in) { tabPos = in; return this; }
	public ETerminalWindow setTab1(boolean val) { tab1 = val; return this; }
	public ETerminalWindow setTextTabBeing(String in) { textTabBegin = in; return this; }
	public ETerminalWindow setTabBase(String in) { tabBase = in; return this; }
	public void setDrawsNewLineBetweenCommands(boolean val) { drawNewLineBetweenCommands = val; }
	
}
