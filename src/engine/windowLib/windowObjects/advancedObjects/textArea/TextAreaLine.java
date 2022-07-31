package engine.windowLib.windowObjects.advancedObjects.textArea;

import engine.inputHandlers.Keyboard;
import engine.inputHandlers.Mouse;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowObjects.actionObjects.WindowTextField;
import engine.windowLib.windowObjects.basicObjects.WindowLabel;
import engine.windowLib.windowObjects.utilityObjects.LinkConfirmationWindow;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowTypes.interfaces.IWindowParent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.MouseType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import engine.windowLib.windowUtil.windowEvents.events.EventFocus;
import engine.windowLib.windowUtil.windowEvents.events.EventMouse;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.Box3;
import eutil.misc.ScreenLocation;
import main.QoT;

//Author: Hunter Bragg

public class TextAreaLine<E> extends WindowTextField<E> {
	
	protected WindowTextArea<E> parentTextArea;
	protected WindowLabel numberLabel;
	protected IWindowObject focusRequester;
	public int lineNumberColor = 0xff555555;
	protected int lineNumber = 0;
	protected int drawnLineNumber = 0;
	protected int lineNumberWidth = 0;
	protected int maxVisibleLength = 3;
	protected boolean textRecentlyEntered = false;
	protected boolean deleting = false;
	protected boolean creating = false;
	protected boolean highlighted = false;
	protected boolean lineEquals = false, drawCursor = false;
	protected long startTime = 0l;
	protected long doubleClickTimer = 0l;
	protected long doubleClickThreshold = 500l;
	protected boolean clicked = false;
	protected String linkText = "";
	protected boolean webLink;
	protected Object linkObject;
	
	//-------------------------
	//TextAreaLine Constructors
	//-------------------------
	
	public TextAreaLine(WindowTextArea<E> textAreaIn) { this(textAreaIn, "", 0xffffff, null, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn) { this(textAreaIn, textIn, 0xffffff, null, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn, int colorIn) { this(textAreaIn, textIn, colorIn, null, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn, E objectIn) { this(textAreaIn, textIn, 0xffffff, objectIn, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn, int colorIn, E objectIn) { this(textAreaIn, textIn, colorIn, objectIn, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn, int colorIn, E objectIn, int lineNumberIn) {
		init(textAreaIn, 0, 0, 0, 0);
		setMaxStringLength(1500);
		parent = textAreaIn;
		parentTextArea = textAreaIn;
		lineNumber = lineNumberIn;
		setText(textIn);
		textColor = colorIn;
		setGenericObject(objectIn);
		setDrawShadowed(false);
	}
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return "[" + lineNumber + ": " + getText() + "]"; }
	
	//----------------------
	//WindowObject Overrides
	//----------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (willBeDrawn()) {
			updateBeforeNextDraw(mXIn, mYIn);
			updateValues();
			//boolean current = parentTextArea.getCurrentLine() == this;
			
			drawText();
			
			if (textRecentlyEntered == true) {
				if (System.currentTimeMillis() - startTime >= 600) {
					startTime = 0l;
					textRecentlyEntered = false;
				}
			}
			
			for (IWindowObject o : windowObjects) {
				if (o.willBeDrawn()) {
					//GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		        	o.drawObject(mXIn, mYIn);
				}
			}
			
			if (parentTextArea.isEditable()) {
				if (!Mouse.isButtonDown(0)) { clickStartPos = -1; }
				if (clickStartPos != -1) {
					//int i = (int) (mXIn - startX - parentTextArea.getLineNumberOffset() + 3);
					//int cursorPos = mc.fontRendererObj.trimToWidth(text, i).length();
					//setSelectionPos(cursorPos);
				}
			}
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		
		if (hasFocus()) {
			parentTextArea.keyPressed(typedChar, keyCode);
			
			if (Keyboard.isCtrlA(keyCode)) { setCursorPositionEnd(); }
			else if (Keyboard.isCtrlC(keyCode)) { Keyboard.setClipboard(getSelectedText()); }
			else if (Keyboard.isCtrlV(keyCode) && isEnabled()) { writeText(Keyboard.getClipboard()); }
			else if (Keyboard.isCtrlX(keyCode)) {
				Keyboard.setClipboard(getSelectedText());
				if (isEnabled() && parentTextArea.isEditable()) {
					writeText("");
				}
			}
			else {
				switch (keyCode) {
				case 257: //enter
					if (parentTextArea.isEditable()) {
						parentTextArea.createNewLineAfter(this);
						setDimensions(startX, startY, getStringWidth(text), height);
					}
					break;
				case 265: //up
					parentTextArea.selectPreviousLine(this, getCursorPosition());
					break;
				case 264: //down
					parentTextArea.selectNextLine(this, getCursorPosition());
					break;
				case 259: //backspace
					if (parentTextArea.isEditable()) {
						
						if (getText().isEmpty() || cursorPosition == 0) {
							TextAreaLine l = parentTextArea.deleteLineAndAddPrevious(this);
							if (l != null) {
								l.setDimensions(l.startX, l.startY, getStringWidth(l.getText()), l.height);
							}
						}
						else if (Keyboard.isCtrlDown()) {
							if (isEnabled()) {
								deleteWords(-1);
								setDimensions(startX, startY, getStringWidth(text), height);
							}
						}
						else if (isEnabled()) {
							deleteFromCursor(-1);
							setDimensions(startX, startY, getStringWidth(text), height);
						}
						
						startTextTimer();
					}
					break;
				case 268: //home
					if (Keyboard.isShiftDown()) { setSelPos(0); }
					else { setCursorPosZero(); }
					break;
				case 263: //left
					if (Keyboard.isShiftDown()) {
						if (Keyboard.isCtrlDown()) { setSelPos(getNthWordFromPos(-1, getSelEnd())); }
						else { setSelPos(getSelEnd() - 1); }
					}
					else if (Keyboard.isCtrlDown()) { setCursorPos(getNthWordFromCursor(-1)); }
					else { moveCursorBy(-1); }
					startTextTimer();
					break;
				case 262: //right
					if (Keyboard.isShiftDown()) {
						if (Keyboard.isCtrlDown()) { setSelPos(getNthWordFromPos(1, getSelEnd())); }
						else { setSelPos(getSelEnd() + 1); }
					}
					else if (Keyboard.isCtrlDown()) { setCursorPos(getNthWordFromCursor(1)); }
					else { moveCursorBy(1); }
					startTextTimer();
					break;
				case 269: //end
					if (Keyboard.isShiftDown()) { setSelPos(text.length()); }
					else { setCursorPositionEnd(); }
					break;
				case 261: //delete
					if (parentTextArea.isEditable()) {
						if (Keyboard.isCtrlDown()) {
							if (isEnabled()) { deleteWords(1); startTextTimer(); }
						}
						else if (isEnabled()) { deleteFromCursor(1); startTextTimer(); }
					}
					break;
				default:
					if (parentTextArea.isEditable()) {
						if (isEnabled()) {
							if (Keyboard.isTypable(typedChar, keyCode)) {
								typedChar = (Keyboard.isShiftDown()) ? Keyboard.getUppercase(keyCode) : typedChar;
								writeText(Character.toString(typedChar));
								setDimensions(startX, startY, getStringWidth(text), height);
								startTextTimer();
							}
						}
					}
				} //switch
				
			}
		}
	}
	
	@Override 
	public void mousePressed(int mXIn, int mYIn, int button) {
		postEvent(new EventMouse(this, mX, mY, button, MouseType.PRESSED));
		
		int mX = mXIn;
		int mY = mYIn;
		int b = button;
		
		if (b == -1) {
			mX = Mouse.getMx();
			mY = Mouse.getMy();
			b = Mouse.getButton();
		}
		
		try {
			if (isMouseOver()) { EUtil.nullDo(getWindowParent(), w -> w.bringToFront()); }
			if (b == 0) {
				startTextTimer();
				
				if (isResizeable() && !getEdgeSideMouseIsOn().equals(ScreenLocation.OUT)) {
					getTopParent().setModifyingObject(this, ObjectModifyType.Resize);
					getTopParent().setResizingDir(getEdgeSideMouseIsOn());
					getTopParent().setModifyMousePos(mX, mY);
				}
				if (parentTextArea.isEditable()) {
					int i = (int) (mX - startX - parentTextArea.getLineNumberOffset() + 3);
					int cursorPos = QoT.getFontRenderer().trimToWidth(text, i).length();
					
					setCursorPos(cursorPos);
					selectionEnd = cursorPosition;
					
					if (clickStartPos == -1) { clickStartPos = cursorPos; }
				}
				
				checkLinkClick(mX, mY, b);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	/** Prevent cursor updates. */
	@Override public void updateCursorImage() {}
	
	@Override
	public void mouseEntered(int mXIn, int mYIn) {
		super.mouseEntered(mXIn, mYIn);
		IWindowObject focused = getTopParent().getFocusedObject();
		boolean oneOf = focused == this || parentTextArea.isChildOf(focused);
		
		if (parentTextArea.isEditable() && (oneOf || !Mouse.isButtonDown(0))) {
			//CursorHelper.setCursor(EMCResources.cursorIBeam);
		}
	}
	
	@Override
	public void mouseExited(int mXIn, int mYIn) {
		super.mouseExited(mXIn, mYIn);
		//IWindowObject over = getTopParent().getHighestZObjectUnderMouse();
		//boolean inside = over != parentTextArea || !parentTextArea.isChildOf(over);
		//if (parentTextArea.isEditable() && !inside) { CursorHelper.reset(); }
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		parentTextArea.mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode());
		parentTextArea.setSelectedLine(this);
		
		double mX = eventIn.getMX();
		double mY = eventIn.getMY();
		int b = eventIn.getActionCode();
		
		if (b == -1) {
			mX = Mouse.getMx();
			mY = Mouse.getMy();
			b = Mouse.getButton();
		}
		
		if (focusRequester != null) {
			focusRequester.requestFocus();
		}
		
		if (eventIn.getFocusType() == FocusType.MousePress) {
			startTextTimer();
			
			checkLinkClick(mX, mY, b);
			
			if (mX > endX) {
				setCursorPos(text.length() + 1);
				selectionEnd = cursorPosition;
				
				if (clickStartPos == -1) { clickStartPos = text.length() + 1; }
			}
			else if (mX >= startX) {
				//int i = (int) (mX - startX - parentTextArea.getLineNumberOffset() + 3);
				//int cursorPos = Game.getFontRenderer().trimToWidth(text, i).length();
				
				//setCursorPosition(cursorPos);
				//selectionEnd = cursorPosition;
				
				//if (clickStartPos == -1) { clickStartPos = cursorPos; }
			}
			else {
				setCursorPos(0);
				selectionEnd = cursorPosition;
				
				if (clickStartPos == -1) { clickStartPos = 0; }
			}
		}
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		clickStartPos = -1;
		super.onFocusLost(eventIn);
	}
	
	//--------------------
	//TextAreaLine Methods
	//--------------------
	
	public TextAreaLine<E> incrementLineNumber() { setLineNumber(lineNumber + 1); return this; }
	public TextAreaLine<E> decrementLineNumber() { setLineNumber(lineNumber - 1); return this; }
	public TextAreaLine<E> indent() { setText("    " + getText()); return this; }
	
	//--------------------
	//TextAreaLine Getters
	//--------------------
	
	public IWindowObject<?> getFocusRequester() { return focusRequester; }
	public int getDrawnLineNumber() { return drawnLineNumber; }
	public int getLineNumber() { return lineNumber; }
	public long getDoubleClickThreshold() { return doubleClickThreshold; }
	public Box3<String, Object, Boolean> getLink() { return new Box3(linkText, linkObject, webLink); }
	
	//--------------------
	//TextAreaLine Setters
	//--------------------
	
	public void setParentTextArea(WindowTextArea<E> area) {
		setParent(area);
		parentTextArea = area;
	}
	
	public TextAreaLine<E> setLinkText(String textIn) { return setLinkText(textIn, null, false); }
	public TextAreaLine<E> setLinkText(String textIn, Object linkObjectIn) { return setLinkText(textIn, linkObjectIn, false); }
	public TextAreaLine<E> setLinkText(String textIn, boolean isWebLink) { return setLinkText(textIn, null, isWebLink); }
	public TextAreaLine<E> setLinkText(String textIn, Object linkObjectIn, boolean isWebLink) {
		linkText = textIn;
		linkObject = linkObjectIn;
		webLink = isWebLink;
		return this;
	}
	
	public TextAreaLine<E> setHighlighted(boolean val) {
		cursorPosition = 0;
		selectionEnd = val ? text.length() : 0;
		return this;
	}
	
	public TextAreaLine<E> setLineNumber(int numberIn) { lineNumber = numberIn; lineNumberWidth = getStringWidth(String.valueOf(lineNumber)); return this; }
	public TextAreaLine<E> setLineNumberColor(EColors colorIn) { lineNumberColor = colorIn.intVal; return this; }
	public TextAreaLine<E> setLineNumberColor(int colorIn) { lineNumberColor = colorIn; return this; }
	public TextAreaLine<E> setDrawnLineNumber(int numberIn) { drawnLineNumber = numberIn; return this; }
	public TextAreaLine<E> setDoubleClickThreshold(long timeIn) { doubleClickThreshold = timeIn; return this; }
	public TextAreaLine<E> setFocusRequester(IWindowObject obj) { focusRequester = obj; return this; }
	
	//------------------------------
	//TextAreaLine Protecetd Methods
	//------------------------------
	
	protected void updateValues() {
		if (clicked && System.currentTimeMillis() - doubleClickTimer >= doubleClickThreshold) { clicked = false; doubleClickTimer = 0l; }
		if (parentTextArea != null && parentTextArea.getCurrentLine() != null) {
			lineEquals = parentTextArea.getCurrentLine().equals(this);
			drawCursor = parentTextArea.isEditable() && lineEquals && QoT.updateCounter / 60 % 2 == 0;
		}
	}
	
	protected boolean checkLinkClick(double mXIn, double mYIn, int button) {
		if (linkText != null && !linkText.isEmpty()) {
			String uText = text;
			
			String test = "";
			int total = 0;
			double startPos = startX;
			double endPos = startX;
			int linkPos = 0;
			
			for (int i = 0; i < uText.length(); i++) {
				char c = uText.charAt(i);
				int cLen = FontRenderer.getCharWidth();
				
				total += cLen;
				
				if (test.equals(linkText)) {
					break;
				}
				
				if (c == linkText.charAt(linkPos)) {
					endPos += cLen;
					linkPos++;
					test += c;
				}
				else {
					startPos = startX + total;
					endPos = startPos;
					linkPos = 0;
					test = "";
				}
			}
			
			if (mXIn >= startPos && mXIn <= endPos) {
				try {
					WindowButton.playPressSound();
					if (webLink) {
						getTopParent().displayWindow(new LinkConfirmationWindow((String) linkObject));
						return true;
					}
					else if (linkObject != null) {
						//if (linkObject instanceof File) { EUtil.openFile((File) linkObject); return true; }
						if (linkObject instanceof IWindowParent) { getTopParent().displayWindow((IWindowParent) linkObject); return true; }
						//if (linkObject instanceof Keyboard) { GuiOpener.openGui(linkObject.getClass()); return true; }
					}
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			
		} //if null link
		
		return false;
	}
	
	protected void drawText() {
		int selStart = cursorPosition;
		int selEnd = selectionEnd;
		boolean hasSel = (selStart != selEnd);
		
		/*
		if (this.getLineNumber() == 1) {
			double x = midX;
			double y = midY;
			//System.out.println(startX + " : " + startY);
			this.drawCircle(midX, midY, 1, 3, EColors.skyblue);
			this.drawCircle(midX, midY, 50, 50, EColors.skyblue);
		}
		*/
		
		//draw text
		if (drawShadowed) drawStringS(text, startX + parentTextArea.getLineNumberOffset(), startY + 5, textColor);
		else drawString(text, startX + parentTextArea.getLineNumberOffset(), startY + 5, textColor);
		
		if (lineEquals && parentTextArea.isEditable()) {
			if (hasSel) { //draw highlight
				int start = selStart;
				int end = selEnd;
				
				//fix substring positions
				if (selStart > selEnd) {
					start = selEnd;
					end = selStart;
				}
				
				double xStart = startX + parentTextArea.getLineNumberOffset() + getStringWidth(text.substring(0, start));
				double xEnd = xStart + getStringWidth(text.substring(start, end));
				
				//fix highlight selection
				if (selStart > selEnd) {
					//int temp = xStart;
					//xStart = xEnd;
					//xEnd = temp;
				}
				
				//System.out.println(xStart + " " + xEnd);
				
				drawCursorVertical(xStart, startY + 1, xEnd - 1, startY + 1 + FontRenderer.FONT_HEIGHT);
			}
			else if ((textRecentlyEntered || drawCursor) && hasFocus()) { //draw vertical cursor
				int textCursorPosLength = getStringWidth(text.substring(0, cursorPosition));
				double sX = startX + parentTextArea.getLineNumberOffset() + textCursorPosLength;
				drawRect(sX - 1, startY + 1, sX, endY, 0xffffffff);
			}
		}
	}
	
}
