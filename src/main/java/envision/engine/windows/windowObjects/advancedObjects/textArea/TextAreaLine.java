package envision.engine.windows.windowObjects.advancedObjects.textArea;

import envision.Envision;
import envision.engine.inputHandlers.CursorHelper;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowTextField;
import envision.engine.windows.windowObjects.utilityObjects.LinkConfirmationWindow;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.FocusType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.boxes.Box3;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public class TextAreaLine<E> extends WindowTextField {
	
	//--------
	// Fields
	//--------
	
	protected WindowTextArea parentTextArea;
	protected IWindowObject focusRequester;
	public int lineNumberColor = 0xff555555;
	protected int lineNumber = 0;
	protected int drawnLineNumber = 0;
	protected double lineNumberWidth = 0;
	protected boolean lineEquals = false, drawCursor = false;
	protected String linkText = "";
	protected boolean webLink;
	protected Object linkObject;
	protected E genericObject;
	
	//--------------
	// Constructors
	//--------------
	
	public TextAreaLine(WindowTextArea<E> textAreaIn) { this(textAreaIn, "", 0xffffff, null, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn) { this(textAreaIn, textIn, 0xffffff, null, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn, int colorIn) { this(textAreaIn, textIn, colorIn, null, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn, E objectIn) { this(textAreaIn, textIn, 0xffffff, objectIn, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn, int colorIn, E objectIn) { this(textAreaIn, textIn, colorIn, objectIn, -1); }
	public TextAreaLine(WindowTextArea<E> textAreaIn, String textIn, int colorIn, E objectIn, int lineNumberIn) {
		init(textAreaIn, 0, 0, 0, 0);
		setMaxStringLength(1500);
		parentTextArea = textAreaIn;
		lineNumber = lineNumberIn;
		setText(textIn);
		textColor = colorIn;
		setGenericObject(objectIn);
		setDrawShadowed(false);
	}
	
	//--------------------
	// Overrides : Object
	//--------------------
	
	@Override public String toString() { return "[" + lineNumber + ": " + getText() + "]"; }
	
	//---------------------------
	// Overrides : IWindowObject
	//---------------------------
	
	@Override
	public void drawObject_i(long dt, int mXIn, int mYIn) {
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
		
		for (var o : getChildren()) {
			if (o.willBeDrawn()) {
				//GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        	o.drawObject_i(dt, mXIn, mYIn);
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
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (!hasFocus()) return;
		
		parentTextArea.keyPressed(typedChar, keyCode);
		
		if (Keyboard.isCtrlA(keyCode)) setCursorPositionEnd();
		else if (Keyboard.isCtrlC(keyCode)) Keyboard.setClipboard(getSelectedText());
		else if (Keyboard.isCtrlV(keyCode) && isEnabled()) writeText(Keyboard.getClipboard());
		else if (Keyboard.isCtrlX(keyCode)) {
			Keyboard.setClipboard(getSelectedText());
			if (isEnabled() && parentTextArea.isEditable()) {
				writeText("");
			}
		}
		else {
			switch (keyCode) {
			case Keyboard.KEY_ENTER: //enter
				if (parentTextArea.isEditable()) {
					parentTextArea.createNewLineAfter(this);
					setDimensions(startX, startY, strWidth(text), height);
				}
				break;
			case Keyboard.KEY_UP: //up
				parentTextArea.selectPreviousLine(this, getCursorPosition());
				break;
			case Keyboard.KEY_DOWN: //down
				parentTextArea.selectNextLine(this, getCursorPosition());
				break;
			case Keyboard.KEY_BACKSPACE: //backspace
				if (parentTextArea.isEditable()) {
					
					if (getText().isEmpty() || cursorPosition == 0) {
						TextAreaLine l = parentTextArea.deleteLineAndAddPrevious(this);
						if (l != null) {
							l.setDimensions(l.startX, l.startY, FontRenderer.strWidth(l.getText()), l.height);
						}
					}
					else if (Keyboard.isCtrlDown()) {
						if (isEnabled()) {
							deleteWords(-1);
							setDimensions(startX, startY, FontRenderer.strWidth(text), height);
						}
					}
					else if (isEnabled()) {
						deleteFromCursor(-1);
						setDimensions(startX, startY, FontRenderer.strWidth(text), height);
					}
					
					startTextTimer();
				}
				break;
			case Keyboard.KEY_HOME: //home
				if (Keyboard.isShiftDown()) setSelPos(0);
				else setCursorPosZero();
				break;
			case Keyboard.KEY_LEFT: //left
				if (Keyboard.isShiftDown()) {
					if (Keyboard.isCtrlDown()) setSelPos(getNthWordFromPos(-1, getSelEnd()));
					else setSelPos(getSelEnd() - 1);
				}
				else if (Keyboard.isCtrlDown()) setCursorPos(getNthWordFromCursor(-1));
				else moveCursorBy(-1);
				startTextTimer();
				break;
			case Keyboard.KEY_RIGHT: //right
				if (Keyboard.isShiftDown()) {
					if (Keyboard.isCtrlDown()) setSelPos(getNthWordFromPos(1, getSelEnd()));
					else setSelPos(getSelEnd() + 1);
				}
				else if (Keyboard.isCtrlDown()) setCursorPos(getNthWordFromCursor(1));
				else moveCursorBy(1);
				startTextTimer();
				break;
			case Keyboard.KEY_END: //end
				if (Keyboard.isShiftDown()) setSelPos(text.length());
				else setCursorPositionEnd();
				break;
			case Keyboard.KEY_DELETE: //delete
				if (parentTextArea.isEditable()) {
					if (Keyboard.isCtrlDown()) {
						if (isEnabled()) {
							deleteWords(1);
							startTextTimer();
						}
					}
					else if (isEnabled()) {
						deleteFromCursor(1);
						startTextTimer();
					}
				}
				break;
			default:
				if (parentTextArea.isEditable()) {
					if (isEnabled()) {
						if (Keyboard.isTypable(typedChar, keyCode)) {
							typedChar = (Keyboard.isShiftDown()) ? Keyboard.getUppercase(keyCode) : typedChar;
							writeText(Character.toString(typedChar));
							setDimensions(startX, startY, FontRenderer.strWidth(text), height);
							startTextTimer();
						}
					}
				}
			} //switch
		}
	}
	
	@Override 
	public void mousePressed(int mXIn, int mYIn, int button) {
		postEvent(new EventMouse(this, Mouse.getMx(), Mouse.getMy(), button, MouseType.PRESSED));
		
		int mX = mXIn;
		int mY = mYIn;
		int b = button;
		
		if (b == -1) {
			mX = Mouse.getMx();
			mY = Mouse.getMy();
			b = Mouse.getButton();
		}
		
        if (isMouseOver()) EUtil.nullDo(getWindowParent(), w -> w.bringToFront());
        if (b != 0) return;
        
        startTextTimer();
        
        if (isResizeable() && !getEdgeSideMouseIsOn().equals(ScreenLocation.OUT)) {
            getTopParent().setModifyingObject(this, ObjectModifyType.RESIZE);
            getTopParent().setResizingDir(getEdgeSideMouseIsOn());
            getTopParent().setModifyMousePos(mX, mY);
        }
        
        if (parentTextArea.isEditable()) {
            int i = (int) (mX - startX - parentTextArea.getLineNumberOffset());
            int cursorPos = FontRenderer.trimToWidth(text, i).length();
            setCursorPos(cursorPos);
            selectionEnd = cursorPosition;
            
            if (clickStartPos == -1) clickStartPos = cursorPos;
        }
        
        checkLinkClick(mX, mY, b);
	}
	
	/** Prevent cursor updates. */
	@Override
	public void updateCursorImage() {
	    
	}
	
	@Override
	public void mouseEntered(int mXIn, int mYIn) {
		super.mouseEntered(mXIn, mYIn);
		IWindowObject focused = getTopParent().getFocusedObject();
		boolean oneOf = focused == this || parentTextArea.isChildOf(focused);
		
		if (parentTextArea.isEditable() && (oneOf || !Mouse.isButtonDown(0))) {
			CursorHelper.setCursor(CursorHelper.ibeam);
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
		
		if (eventIn.getFocusType() == FocusType.MOUSE_PRESS) {
			startTextTimer();
			
			checkLinkClick(mX, mY, b);
			
			if (mX > endX) {
				setCursorPos(text.length() + 1);
				selectionEnd = cursorPosition;
				
				if (clickStartPos == -1) clickStartPos = text.length() + 1;
			}
			else if (mX >= startX) {
                int i = (int) (mX - startX - parentTextArea.getLineNumberOffset() + 3);
                int cursorPos = FontRenderer.trimToWidth(text, i).length();
                
                setCursorPos(cursorPos);
                selectionEnd = cursorPosition;
                
                if (clickStartPos == -1) { clickStartPos = cursorPos; }
			}
			else {
				setCursorPos(0);
				selectionEnd = cursorPosition;
				
				if (clickStartPos == -1) clickStartPos = 0;
			}
		}
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		clickStartPos = -1;
		super.onFocusLost(eventIn);
	}
	
	//---------
	// Methods
	//---------
	
	public void incrementLineNumber() { setLineNumber(lineNumber + 1); }
	public void decrementLineNumber() { setLineNumber(lineNumber - 1); }
	public void indent() { setText("    " + getText()); }
	
	//---------
	// Getters
	//---------
	
	public IWindowObject getFocusRequester() { return focusRequester; }
	public int getDrawnLineNumber() { return drawnLineNumber; }
	public int getLineNumber() { return lineNumber; }
	public Box3<String, Object, Boolean> getLink() { return new Box3<>(linkText, linkObject, webLink); }
	
	//---------
	// Setters
	//---------
	
	public void setParentTextArea(WindowTextArea area) {
		setParent(area);
		parentTextArea = area;
	}
	
	public void setLinkText(String textIn) { setLinkText(textIn, null, false); }
	public void setLinkText(String textIn, Object linkObjectIn) { setLinkText(textIn, linkObjectIn, false); }
	public void setLinkText(String textIn, boolean isWebLink) { setLinkText(textIn, null, isWebLink); }
	public void setLinkText(String textIn, Object linkObjectIn, boolean isWebLink) {
		linkText = textIn;
		linkObject = linkObjectIn;
		webLink = isWebLink;
	}
	
	public void setHighlighted(boolean val) {
		cursorPosition = 0;
		selectionEnd = val ? text.length() : 0;
	}
	
	public void setLineNumber(int numberIn) {
		lineNumber = numberIn;
		lineNumberWidth = FontRenderer.strWidth(String.valueOf(lineNumber));
	}
	
	public void setLineNumberColor(EColors colorIn) { lineNumberColor = colorIn.intVal; }
	public void setLineNumberColor(int colorIn) { lineNumberColor = colorIn; }
	public void setDrawnLineNumber(int numberIn) { drawnLineNumber = numberIn; }
	public void setFocusRequester(IWindowObject obj) { focusRequester = obj; }
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void updateValues() {
		if (parentTextArea != null && parentTextArea.getCurrentLine() != null) {
			lineEquals = parentTextArea.getCurrentLine().equals(this);
			drawCursor = parentTextArea.isEditable() && lineEquals && Envision.updateCounter / 60 % 2 == 0;
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
				catch (Exception e) {
					e.printStackTrace();
				}
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
		if (text != null && !text.isEmpty()) {
			if (drawShadowed) drawStringS(text, startX + parentTextArea.getLineNumberOffset(), startY + 5, textColor);
			else drawString(text, startX + parentTextArea.getLineNumberOffset(), startY + 5, textColor);
		}
		
		if (lineEquals && parentTextArea.isEditable()) {
			if (hasSel) { //draw highlight
				int start = selStart;
				int end = selEnd;
				
				//fix substring positions
				if (selStart > selEnd) {
					start = selEnd;
					end = selStart;
				}
				
				double xStart = startX + parentTextArea.getLineNumberOffset() + FontRenderer.strWidth(text.substring(0, start));
				double xEnd = xStart + FontRenderer.strWidth(text.substring(start, end));
				
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
				double textCursorPosLength = FontRenderer.strWidth(text.substring(0, cursorPosition));
				double sX = startX + parentTextArea.getLineNumberOffset() + textCursorPosLength;
				drawRect(sX - 1, startY + 1, sX, endY, 0xffffffff);
			}
		}
	}
	
	//================
	// Generic Object
	//================
	
	public void setGenericObject(E objectIn) { genericObject = objectIn; }
	public E getGenericObject() { return genericObject; }
	
}
