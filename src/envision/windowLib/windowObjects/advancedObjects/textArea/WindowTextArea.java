package envision.windowLib.windowObjects.advancedObjects.textArea;

import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;

import java.util.Iterator;

import envision.inputHandlers.Mouse;
import envision.renderEngine.GLSettings;
import envision.windowLib.windowObjects.advancedObjects.WindowScrollList;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.MouseType;
import envision.windowLib.windowUtil.windowEvents.events.EventMouse;

//Author: Hunter Bragg

public class WindowTextArea<E> extends WindowScrollList<E> {
	
	//--------
	// Fields
	//--------
	
	private EArrayList<TextAreaLine<E>> textDocument;
	private TextAreaLine<E> currentLine, longestLine;
	protected boolean editable = true;
	protected boolean drawLineNumbers = false;
	protected boolean drawLineHighlight = true;
	protected boolean drawLineNumberSeparator = true;
	protected int maxWidth = Integer.MAX_VALUE;
	protected int lineHeight = 10;
	protected int lineNumberSeparatorColor = 0xff000000;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowTextArea(IWindowObject<?> parentIn, double x, double y, double widthIn, double heightIn) {
		this(parentIn, x, y, widthIn, heightIn, false);
	}
	
	public WindowTextArea(IWindowObject<?> parentIn, double x, double y, double widthIn, double heightIn, boolean editableIn) {
		this(parentIn, x, y, widthIn, heightIn, false, false);
	}
	
	public WindowTextArea(IWindowObject<?> parentIn, double x, double y, double widthIn, double heightIn, boolean editableIn, boolean addLine) {
		super(parentIn, x, y, widthIn, heightIn);
		editable = editableIn;
		textDocument = new EArrayList<>();
		setBackgroundColor(0xff2d2d2d);
		setResetDrawn(true);
	}
	
	//---------------------------
	// Overrides : IWindowObject
	//---------------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawRect(startX, startY, endX, endY, borderColor);
		
		vScroll.setVisible(isVScrollDrawn());
		hScroll.setVisible(isHScrollDrawn());
		reset.setVisible(isResetDrawn());
		
		//int scale = 1;//res.getScaleFactor();
		try {
			if (willBeDrawn() && height > (isHScrollDrawn() ? 5 : 2) && width > (isVScrollDrawn() ? 5 : 2)) {
				GLSettings.pushMatrix();
				GLSettings.enableBlend();
				
				double sX = startX + 1 + getLineNumberOffset() - 2;
				
				//draw list contents scissored
				drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); //draw background
				scissor(sX, startY + 1, endX - (isVScrollDrawn() ? vScroll.width + 1 : 1), endY - (isHScrollDrawn() ? hScroll.height + 2 : 1));
				if (drawListObjects) {
					
					//draw line highlight
					if (currentLine != null && drawLineHighlight) {
						double lX = currentLine.startX - 2 + (hasLineNumbers() ? getLineNumberOffset() - 1: 0);
						drawRect(lX, currentLine.startY + 1, endX - 1, currentLine.endY, 0x39909090);
					}
					
					//only draw the objects that are actually in the viewable area
					for (var o : drawnListObjects) {
						if (o.willBeDrawn()) {
							if (!o.hasFirstDraw()) o.onFirstDraw();
							GLSettings.fullBright();
							o.drawObject(mXIn, mYIn);
						}
					}
				}
				endScissor();
				
				scissor(startX + 1, startY + 1, startX + 1 + getLineNumberOffset() - 1, endY - 1);
				if (hasLineNumbers() && drawLineNumberSeparator) {
					double eY = endY - 1 - (isHScrollDrawn() ? getHScrollBar().height : 0);
					drawRect(sX, startY + 1, sX + 1, eY, lineNumberSeparatorColor);
					
					for (var l : textDocument) {
						double nX = startX + getLineNumberOffset() - getStringWidth(String.valueOf(l.getLineNumber())) - 3;
						drawString(l.getLineNumber(), nX, l.startY + 2, l.lineNumberColor);
					}
				}
				endScissor();
				
				if (isVScrollDrawn()) drawRect(endX - vScroll.width - 2, startY + 1, endX - 1, endY - 1, borderColor);
				if (isHScrollDrawn()) drawRect(startX + 1, endY - hScroll.height - 2, endX - 1, endY - 1, borderColor);
				
				//draw non list contents as normal (non scissored)
				for (var o : getChildren()) {
					if (o.willBeDrawn() && listContents.notContains(o)) {
						if (!o.hasFirstDraw()) o.onFirstDraw();
						GLSettings.fullBright();
	    	        	o.drawObject(mXIn, mYIn);
	    			}
				}
				
				GLSettings.popMatrix();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		postEvent(new EventMouse(this, Mouse.getMx(), Mouse.getMy(), button, MouseType.PRESSED));
		listClick(mXIn, mYIn, button);
	}
	
	//override to prevent cursor updates
	@Override
	public void updateCursorImage() {
		if (!isEditable()) super.updateCursorImage();
	}
	
	@Override
	public void mouseEntered(int mX, int mY) {
		super.mouseEntered(mX, mY);
		var focused = getTopParent().getFocusedObject();
		boolean oneOf = focused == this || isChildOf(focused);
		
		if (isEditable() && (oneOf || !Mouse.isButtonDown(0))) {
			//CursorHelper.setCursor(EMCResources.cursorIBeam);
		}
	}
	
	@Override
	public void mouseExited(int mX, int mY) {
		super.mouseExited(mX, mY);
		if (getTopParent() != null) {
			var o = getTopParent().getHighestZObjectUnderMouse();
			if (o != null && !o.isChildOf(this)) {
				//CursorHelper.reset();
			}
		}
		//else { CursorHelper.reset(); }
	}
	
	//---------
	// Methods
	//---------
	
	public void listClick(int mXIn, int mYIn, int button) {
		if (button == 0) {
			//EUtil.nullDo(getWindowParent(), w -> w.bringToFront());
			
			if (isEditable() && textDocument.isEmpty()) {
				var l = addTextLine();
				setSelectedLine(l);
				l.requestFocus();
			}
			else if (textDocument.isNotEmpty()) {
				if (button == 0) {
					var l = getLineMouseIsOver();
					if (l != currentLine) {
						if (l != null) {
							setSelectedLine(l, FocusType.MOUSE_PRESS);
						}
						else {
							if (currentLine != null) currentLine.setHighlighted(false);
							setSelectedLine(null);
						}
					}
					else if (l != null) {
						if (!l.hasFocus()) l.requestFocus(FocusType.MOUSE_PRESS);
						else l.mousePressed(mXIn, mYIn, button);
					}
				}
			} //else if
		}
	}
	
	public TextAreaLine<E> createTextLine() { return createTextLine("", EColors.white, null); }
	public TextAreaLine<E> createTextLine(String textIn) { return createTextLine(textIn, EColors.white, null); }
	public TextAreaLine<E> createTextLine(String textIn, EColors colorIn) { return createTextLine(textIn, colorIn.intVal, null); }
	public TextAreaLine<E> createTextLine(String textIn, int colorIn) { return createTextLine(textIn, colorIn, null); }
	public TextAreaLine<E> createTextLine(String textIn, EColors colorIn, E objectIn) { return createTextLine(textIn, colorIn.intVal, objectIn); }
	public TextAreaLine<E> createTextLine(String textIn, int colorIn, E objectIn) {
		var l = new TextAreaLine(this, textIn, colorIn, objectIn, 0);
		return l;
	}
	
	public TextAreaLine<E> addTextLine() { return addTextLine("", EColors.white, null, false); }
	public TextAreaLine<E> addTextLine(boolean moveDown) { return addTextLine("", EColors.white, null, moveDown); }
	public TextAreaLine<E> addTextLine(String textIn) { return addTextLine(textIn, EColors.white, null, false); }
	public TextAreaLine<E> addTextLine(String textIn, EColors colorIn) { return addTextLine(textIn, colorIn.intVal, null, false); }
	public TextAreaLine<E> addTextLine(String textIn, int colorIn) { return addTextLine(textIn, colorIn, null, false); }
	public TextAreaLine<E> addTextLine(String textIn, EColors colorIn, E objectIn) { return addTextLine(textIn, colorIn.intVal, objectIn, false); }
	public TextAreaLine<E> addTextLine(String textIn, int colorIn, E objectIn) { return addTextLine(textIn, colorIn, objectIn, false); }
	public TextAreaLine<E> addTextLine(String textIn, EColors colorIn, E objectIn, boolean moveDown) { return addTextLine(textIn, colorIn.intVal, objectIn, moveDown); }
	public TextAreaLine<E> addTextLine(String textIn, int colorIn, E objectIn, boolean moveDown) {
		return addTextLine(createTextLine(textIn, colorIn, objectIn), 0, moveDown);
	}
	
	public TextAreaLine<E> addTextLine(TextAreaLine<E> lineIn) { return addTextLine(lineIn, 0, false); }
	public TextAreaLine<E> addTextLine(TextAreaLine<E> lineIn, int offset) { return addTextLine(lineIn, offset, false); }
	public TextAreaLine<E> addTextLine(TextAreaLine<E> lineIn, int offset, boolean moveDown) {
		//int moveArg = moveDown ? 1 : 0;
		//EDimension ld = getListDimensions();
		lineIn.setDimensions(3, (textDocument.size() * 24) + offset, getStringWidth(lineIn.getText()), 24);
		textDocument.add(lineIn);
		addObjectToList(lineIn);
		
		fitItemsInList(3 + getLineNumberOffset(), 5);
		updateVisuals();
		lineIn.setLineNumber(textDocument.size());
		return lineIn;
	}
	
	public void deleteLine() { deleteLine(getCurrentLine()); }
	public void deleteLine(int lineNumber) { deleteLine(getTextLine(lineNumber)); }
	public void deleteLine(TextAreaLine<E> lineIn) {
		textDocument.remove(lineIn);
		removeObjectFromList(lineIn);
		fitItemsInList(3, 7);
	}
	
	public void setSelectedLine(TextAreaLine<E> lineIn) { setSelectedLine(lineIn, FocusType.TRANSFER); }
	public void setSelectedLine(TextAreaLine<E> lineIn, FocusType typeIn) {
		if (lineIn == null) { currentLine = null; }
		if (textDocument.contains(lineIn)) {
			currentLine = lineIn;
			if (!currentLine.hasFocus()) currentLine.requestFocus(typeIn);
		}
	}
	
	public TextAreaLine<E> selectPreviousLine(int numIn, int pos) { return selectPreviousLine(getTextLine(numIn), pos); }
	public TextAreaLine<E> selectPreviousLine(TextAreaLine<E> lineIn, int pos) {
		if (lineIn != null) {
			TextAreaLine<E> line = getTextLine(lineIn.getLineNumber() - 1);
			if (line != null) {
				setSelectedLine(line, FocusType.GAINED);
				line.setCursorPos(pos);
				line.startTextTimer();
			}
			return line;
		}
		return null;
	}
	
	public TextAreaLine<E> selectNextLine(int numIn, int pos) { return selectNextLine(getTextLine(numIn), pos); }
	public TextAreaLine<E> selectNextLine(TextAreaLine<E> lineIn, int pos) {
		if (lineIn != null) {
			TextAreaLine<E> line = getTextLine(lineIn.getLineNumber() + 1);
			if (line != null) {
				setSelectedLine(line, FocusType.GAINED);
				line.setCursorPos(pos);
				line.startTextTimer();
			}
			return line;
		}
		return null;
	}
	
	/** Used when pressing enter. */
	public TextAreaLine<E> createNewLineAfter(TextAreaLine<E> theLine) {
		if (theLine != null) {
			String text = theLine.getText().substring(theLine.getCursorPosition());
			TextAreaLine<E> newLine = new TextAreaLine<>(this, text, theLine.textColor, theLine.getGenericObject(), theLine.getLineNumber() + 1);
			
			EArrayList<TextAreaLine<E>> linesAfter = new EArrayList<>();
			try {
				for (int i = theLine.getLineNumber() + 1; i < textDocument.size() + 1; i++) {
					var l = getTextLine(i);
					linesAfter.add(l);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			for (TextAreaLine<E> l : linesAfter) deleteLine(l);
			theLine.setText(theLine.getText().substring(0, theLine.getCursorPosition()));
			addTextLine(newLine);
			for (TextAreaLine<E> l : linesAfter) addTextLine(l); 
			
			setSelectedLine(newLine);
			newLine.setCursorPos(0);
			
			return newLine;
		}
		return null;
	}
	
	public TextAreaLine<E> deleteLineAndAddPrevious(TextAreaLine<E> theLine) {
		if (theLine != null) {
			String text = theLine.getText();
			var prev = getTextLine(theLine.getLineNumber() - 1);
			
			if (prev != null) {
				EArrayList<TextAreaLine<E>> linesAfter = new EArrayList<>();
				try {
					for (int i = theLine.getLineNumber() + 1; i < textDocument.size() + 1; i++) {
						linesAfter.add(getTextLine(i));
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				deleteLine(theLine);
				
				prev.setText(prev.getText() + text);
				for (var l : linesAfter) deleteLine(l);
				for (var l : linesAfter) addTextLine(l);
				
				setSelectedLine(prev);
				int pos = ENumUtil.clamp(prev.getText().length() - text.length(), 0, prev.getText().length());
				prev.setCursorPos(pos);
				
				return prev;
			}
		}
		return null;
	}
	
	public void setLineNumberDrawn(int lineNumber) { setLineNumberDrawn(getTextLine(lineNumber)); }
	public void setLineNumberDrawn(TextAreaLine<E> lineIn) {
		if (lineIn != null) {
			double lineYPos = lineIn.endY + lineIn.height;
			double difference = lineYPos - startY;
			
			getVScrollBar().setScrollPos((int) difference);
		}
	}
	
	public void clear() {
		setListWidth(width - 2);
		setListHeight(0);
		Iterator it = getChildren().iterator();
		while (it.hasNext()) {
			if (it.next() instanceof TextAreaLine) { it.remove(); }
		}
		listContents.clear();
		textDocument.clear();
		currentLine = null;
		longestLine = null;
	}
	
	/*
	
	public TextAreaLine insertTextLine() { return insertTextLine("", 0xffffff, -1); }
	public TextAreaLine insertTextLine(int atPos) { return insertTextLine("", 0xffffff, atPos); }
	public TextAreaLine insertTextLine(String textIn) { return insertTextLine(textIn, 0xffffff, -1); }
	public TextAreaLine insertTextLine(String textIn, int atPos) { return insertTextLine(textIn, 0xffffff, atPos); }
	public TextAreaLine insertTextLine(String textIn, int colorIn, int atPos) {
		if (atPos == -1) {
			if (currentLine == null) {
				System.out.println("pos: " + atPos);
				atPos = textDocument.size();
				
			}
		}
		return null;
	}
	
	public TextAreaLine insertTextLine(TextAreaLine lineIn, int atPos) {
		
		return lineIn;
	}
	
	//unfinished
	public WindowTextArea setMaxLineWidth(int widthIn) {
		return this;
	}
	
	//unfinished
	public WindowTextArea indentLine() { return indentLine(getCurrentLine()); }
	public WindowTextArea indentLine(int lineNumber) { return indentLine(getTextLine(lineNumber)); }
	public WindowTextArea indentLine(TextAreaLine lineIn) {
		return this;
	}
	
	*/
	
	//---------
	// Getters
	//---------
	
	public int getLineHeight() { return lineHeight; }
	public int getLineCount() { return ((int) height - 2) / 10; }
	public boolean hasLineNumbers() { return drawLineNumbers; }
	public int getLineNumberOffset() { return hasLineNumbers() ? (6 + String.valueOf(textDocument.size()).length() * getStringWidth("0")) : 2; }
	public boolean getDrawLineHighlight() { return drawLineHighlight; }
	public boolean isEditable() { return editable; }
	public TextAreaLine<E> getCurrentLine() { return currentLine; }
	public EArrayList<TextAreaLine<E>> getTextDocument() { return textDocument; }
	
	public TextAreaLine<E> getLineMouseIsOver() {
		double mPosY = Mouse.getMy() - startY - 1;
		double scrollOffset = vScroll.getScrollPos() - vScroll.getVisibleAmount();
		double posY = mPosY + scrollOffset;
		
		int num = (int) ((posY / lineHeight) + 1);
		var l = getTextLine(num);
		
		return l;
	}
	
	public int getLongestLineLength() {
		longestLine = getLongestTextLine();
		return longestLine != null ? getStringWidth(longestLine.getText()) : - 1;
	}
	
	public TextAreaLine<E> getTextLine(int numIn) {
		if (numIn > 0 && numIn <= textDocument.size()) {
			int first = 0;
			int last = textDocument.size();
			int mid = (first + last) / 2;
			while (first <= last && mid < textDocument.size()) {
				var l = textDocument.get(mid);
				if (l != null) {
					if (l.getLineNumber() == numIn) { return l; }
					else if (l.getLineNumber() < numIn) { first = mid + 1; }
					else { last = mid - 1; }
				}
				mid = (first + last) / 2;
			}
		}
		return null;
	}
	
	public TextAreaLine<E> getLineWithText(String textIn) {
		for (var l : textDocument) {
			if (l.getText().equals(textIn)) return l;
		}
		return null;
	}
	
	public TextAreaLine<E> getLineWithObject(E objectIn) {
		for (var l : textDocument) {
			if (l.getGenericObject() == null) if (objectIn == null) return l;
			else if (l.getGenericObject().equals(objectIn)) return l;
		}
		return null;
	}
	
	public TextAreaLine<E> getLineWithTextAndObject(String textIn, E objectIn) {
		for (TextAreaLine<E> l : textDocument) {
			if (l.getGenericObject() == null ) {
				if (l.getText().equals(textIn) && objectIn == null) return l;
			}
			else if (l.getText().equals(textIn) && l.getGenericObject().equals(objectIn)) return l;
		}
		return null;
	}
	
	public TextAreaLine<E> getLongestTextLine() {
		TextAreaLine<E> longest = null;
		int longestLen = 0;
		for (var l : textDocument) {
			int len = getStringWidth(l.getText());
			if (len > longestLen) {
				longest = l;
				longestLen = len;
			}
		}
		return longest;
	}
	
	//---------
	// Setters
	//---------
	
	public void setLineHeight(int in) { lineHeight = in; }
	public void setDrawLineNumbers(boolean valIn) { drawLineNumbers = valIn; }
	public void setDrawLineHighlight(boolean valIn) { drawLineHighlight = valIn; }
	public void setEditable(boolean valIn) { editable = valIn; }
	public void setDrawLineNumberSeparator(boolean valIn) { drawLineNumberSeparator = valIn; }
	public void setLineNumberSeparatorColor(EColors colorIn) { setLineNumberSeparatorColor(colorIn.intVal); }
	public void setLineNumberSeparatorColor(int colorIn) { lineNumberSeparatorColor = colorIn; }
	
	public void setTextDocument(EArrayList<TextAreaLine<E>> docIn) {
		clear();
		docIn.forEach(l -> addTextLine(l));
	}
	
}
