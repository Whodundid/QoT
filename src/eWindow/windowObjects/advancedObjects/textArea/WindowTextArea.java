package eWindow.windowObjects.advancedObjects.textArea;

import eWindow.windowObjects.advancedObjects.scrollList.WindowScrollList;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.eventUtil.FocusType;
import eWindow.windowUtil.windowEvents.eventUtil.MouseType;
import eWindow.windowUtil.windowEvents.events.EventMouse;
import input.Mouse;
import java.util.Iterator;
import openGL_Util.GLSettings;
import util.EUtil;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;
import util.storageUtil.EDimension;

//Author: Hunter Bragg

public class WindowTextArea extends WindowScrollList {
	
	EArrayList<TextAreaLine> textDocument;
	TextAreaLine currentLine, longestLine;
	protected boolean editable = true;
	protected boolean drawLineNumbers = false;
	protected boolean drawLineHighlight = true;
	protected boolean drawLineNumberSeparator = true;
	protected int maxWidth = Integer.MAX_VALUE;
	protected int lineHeight = 10;
	protected int lineNumberSeparatorColor = 0xff000000;
	
	//---------------------------
	//WindowTextArea Constructors
	//---------------------------
	
	public WindowTextArea(IWindowObject parentIn, double x, double y, double widthIn, double heightIn) {
		this(parentIn, x, y, widthIn, heightIn, false);
	}
	
	public WindowTextArea(IWindowObject parentIn, double x, double y, double widthIn, double heightIn, boolean editableIn) {
		this(parentIn, x, y, widthIn, heightIn, false, false);
	}
	
	public WindowTextArea(IWindowObject parentIn, double x, double y, double widthIn, double heightIn, boolean editableIn, boolean addLine) {
		super(parentIn, x, y, widthIn, heightIn);
		editable = editableIn;
		textDocument = new EArrayList();
		setBackgroundColor(0xff2d2d2d);
		setResetDrawn(true);
	}
	
	//----------------------
	//WindowObject Overrides
	//----------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawRect(startX, startY, endX, endY, borderColor);
		
		verticalScroll.setVisible(isVScrollDrawn());
		horizontalScroll.setVisible(isHScrollDrawn());
		reset.setVisible(isResetDrawn());
		
		int scale = 1;//res.getScaleFactor();
		try {
			if (checkDraw() && height > (isHScrollDrawn() ? 5 : 2) && width > (isVScrollDrawn() ? 5 : 2)) {
				GLSettings.pushMatrix();
				GLSettings.enableBlend();
				
				double sX = startX + 1 + getLineNumberOffset() - 2;
				
				//draw list contents scissored
				drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); //draw background
				scissor(sX, startY + 1, endX - (isVScrollDrawn() ? verticalScroll.width + 1 : 1), endY - (isHScrollDrawn() ? horizontalScroll.height + 2 : 1));
				if (drawListObjects) {
					
					//draw line highlight
					if (currentLine != null && drawLineHighlight) {
						double lX = currentLine.startX - 2 + (hasLineNumbers() ? getLineNumberOffset() - 1: 0);
						//drawRect(lX, currentLine.startY + 1, endX - 1, currentLine.endY, 0x39909090);
					}
					
					//only draw the objects that are actually in the viewable area
					for (IWindowObject o : drawnListObjects) {
						if (o.checkDraw()) {
							if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
							GLSettings.fullBright();
							EDimension d = o.getDimensions();
							o.drawObject(mXIn, mYIn);
						}
					}
				}
				endScissor();
				
				scissor(startX + 1, startY + 1, startX + 1 + getLineNumberOffset() - 1, endY - 1);
				if (hasLineNumbers() && drawLineNumberSeparator) {
					double eY = endY - 1 - (isHScrollDrawn() ? getHScrollBar().height : 0);
					drawRect(sX, startY + 1, sX + 1, eY, lineNumberSeparatorColor);
					
					for (TextAreaLine l : textDocument) {
						double nX = startX + getLineNumberOffset() - getStringWidth(String.valueOf(l.getLineNumber())) - 3;
						drawString(l.getLineNumber(), nX, l.startY + 2, l.lineNumberColor);
					}
				}
				endScissor();
				
				if (isVScrollDrawn()) { drawRect(endX - verticalScroll.width - 2, startY + 1, endX - 1, endY - 1, borderColor); }
				if (isHScrollDrawn()) { drawRect(startX + 1, endY - horizontalScroll.height - 2, endX - 1, endY - 1, borderColor); }
				
				//draw non list contents as normal (non scissored)
				for (IWindowObject o : windowObjects) {
					if (o.checkDraw() && listContents.notContains(o)) {
						if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
						GLSettings.fullBright();
	    	        	o.drawObject(mXIn, mYIn);
	    			}
				}
				
				GLSettings.popMatrix();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		postEvent(new EventMouse(this, mX, mY, button, MouseType.Pressed));
		listClick(mXIn, mYIn, button);
	}
	
	public void listClick(int mXIn, int mYIn, int button) {
		if (button == 0) {
			EUtil.nullDo(getWindowParent(), w -> w.bringToFront());
			
			if (isEditable() && textDocument.isEmpty()) {
				TextAreaLine l = addTextLine();
				setSelectedLine(l);
				l.requestFocus();
			}
			else if (textDocument.isNotEmpty()) {
				if (button == 0) {
					TextAreaLine l = getLineMouseIsOver();
					if (l != currentLine) {
						if (l != null) {
							setSelectedLine(l, FocusType.MousePress);
						}
						else {
							if (currentLine != null) { currentLine.setHighlighted(false); }
							setSelectedLine(null);
						}
					}
					else if (l != null) {
						if (!l.hasFocus()) { l.requestFocus(FocusType.MousePress); }
						else { l.mousePressed(mXIn, mYIn, button); }
					}
				}
			} //else if
		}
	}
	
	//override to prevent cursor updates
	@Override
	public void updateCursorImage() {
		if (!isEditable()) { super.updateCursorImage(); }
	}
	
	@Override
	public void mouseEntered(int mX, int mY) {
		super.mouseEntered(mX, mY);
		IWindowObject focused = getTopParent().getFocusedObject();
		boolean oneOf = focused == this || isChild(focused);
		
		if (isEditable() && (oneOf || !Mouse.isButtonDown(0))) {
			//CursorHelper.setCursor(EMCResources.cursorIBeam);
		}
	}
	
	@Override
	public void mouseExited(int mX, int mY) {
		super.mouseExited(mX, mY);
		if (getTopParent() != null) {
			IWindowObject o = getTopParent().getHighestZObjectUnderMouse();
			if (o != null && !o.isChild(this)) {
				//CursorHelper.reset();
			}
		}
		//else { CursorHelper.reset(); }
	}
	
	//----------------------
	//WindowTextArea Methods
	//----------------------
	
	public TextAreaLine addTextLine() { return addTextLine("", 0xffffff, null, false); }
	public TextAreaLine addTextLine(boolean moveDown) { return addTextLine("", 0xffffff, null, moveDown); }
	public TextAreaLine addTextLine(String textIn) { return addTextLine(textIn, 0xffffff, null, false); }
	public TextAreaLine addTextLine(String textIn, EColors colorIn) { return addTextLine(textIn, colorIn.intVal, null, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn) { return addTextLine(textIn, colorIn, null, false); }
	public TextAreaLine addTextLine(String textIn, EColors colorIn, Object objectIn) { return addTextLine(textIn, colorIn.intVal, objectIn, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn, Object objectIn) { return addTextLine(textIn, colorIn, objectIn, false); }
	public TextAreaLine addTextLine(String textIn, EColors colorIn, Object objectIn, boolean moveDown) { return addTextLine(textIn, colorIn.intVal, objectIn, moveDown); }
	public TextAreaLine addTextLine(String textIn, int colorIn, Object objectIn, boolean moveDown) {
		return addTextLine(new TextAreaLine(this, textIn, colorIn, objectIn, textDocument.size()), 0, moveDown);
	}
	
	public TextAreaLine addTextLine(TextAreaLine lineIn) { return addTextLine(lineIn, 0, false); }
	public TextAreaLine addTextLine(TextAreaLine lineIn, int offset) { return addTextLine(lineIn, offset, false); }
	public TextAreaLine addTextLine(TextAreaLine lineIn, int offset, boolean moveDown) {
		int moveArg = moveDown ? 1 : 0;
		EDimension ld = getListDimensions();
		lineIn.setDimensions(3, 1 + (textDocument.size() * 10) + offset, getStringWidth(lineIn.getText()), 10);
		textDocument.add(lineIn);
		addObjectToList(lineIn);
		fitItemsInList(3 + getLineNumberOffset(), 5);
		updateVisuals();
		lineIn.setLineNumber(textDocument.size());
		return lineIn;
	}
	
	public WindowTextArea deleteLine() { return deleteLine(getCurrentLine()); }
	public WindowTextArea deleteLine(int lineNumber) { return deleteLine(getTextLine(lineNumber)); }
	public WindowTextArea deleteLine(TextAreaLine lineIn) {
		textDocument.remove(lineIn);
		removeObjectFromList(lineIn);
		fitItemsInList(3, 7);
		return this;
	}
	
	public WindowTextArea setSelectedLine(TextAreaLine lineIn) { return setSelectedLine(lineIn, FocusType.Transfer); }
	public WindowTextArea setSelectedLine(TextAreaLine lineIn, FocusType typeIn) {
		if (lineIn == null) { currentLine = null; return this; }
		if (textDocument.contains(lineIn)) {
			currentLine = lineIn;
			if (!currentLine.hasFocus()) { currentLine.requestFocus(typeIn); }
		}
		return this;
	}
	
	public TextAreaLine selectPreviousLine(int numIn, int pos) { return selectPreviousLine(getTextLine(numIn), pos); }
	public TextAreaLine selectPreviousLine(TextAreaLine lineIn, int pos) {
		if (lineIn != null) {
			TextAreaLine line = getTextLine(lineIn.getLineNumber() - 1);
			if (line != null) {
				setSelectedLine(line, FocusType.Gained);
				line.setCursorPosition(pos);
				line.startTextTimer();
			}
			return line;
		}
		return null;
	}
	
	public TextAreaLine selectNextLine(int numIn, int pos) { return selectNextLine(getTextLine(numIn), pos); }
	public TextAreaLine selectNextLine(TextAreaLine lineIn, int pos) {
		if (lineIn != null) {
			TextAreaLine line = getTextLine(lineIn.getLineNumber() + 1);
			if (line != null) {
				setSelectedLine(line, FocusType.Gained);
				line.setCursorPosition(pos);
				line.startTextTimer();
			}
			return line;
		}
		return null;
	}
	
	/** Used when pressing enter. */
	public TextAreaLine createNewLineAfter(TextAreaLine theLine) {
		if (theLine != null) {
			String text = theLine.getText().substring(theLine.getCursorPosition());
			TextAreaLine newLine = new TextAreaLine(this, text, theLine.textColor, theLine.getStoredObj(), theLine.getLineNumber() + 1);
			
			EArrayList<TextAreaLine> linesAfter = new EArrayList();
			try {
				for (int i = theLine.getLineNumber() + 1; i < textDocument.size() + 1; i++) {
					TextAreaLine l = getTextLine(i);
					linesAfter.add(l);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
			
			for (TextAreaLine l : linesAfter) { deleteLine(l); }
			theLine.setText(theLine.getText().substring(0, theLine.getCursorPosition()));
			addTextLine(newLine);
			for (TextAreaLine l : linesAfter) { addTextLine(l); }
			
			setSelectedLine(newLine);
			newLine.setCursorPosition(0);
			
			return newLine;
		}
		return null;
	}
	
	public TextAreaLine deleteLineAndAddPrevious(TextAreaLine theLine) {
		if (theLine != null) {
			String text = theLine.getText();
			TextAreaLine prev = getTextLine(theLine.getLineNumber() - 1);
			
			if (prev != null) {
				EArrayList<TextAreaLine> linesAfter = new EArrayList();
				try {
					for (int i = theLine.getLineNumber() + 1; i < textDocument.size() + 1; i++) {
						linesAfter.add(getTextLine(i));
					}
				}
				catch (Exception e) { e.printStackTrace(); }
				
				deleteLine(theLine);
				
				prev.setText(prev.getText() + text);
				for (TextAreaLine l : linesAfter) { deleteLine(l); }
				for (TextAreaLine l : linesAfter) { addTextLine(l); }
				
				setSelectedLine(prev);
				int pos = NumUtil.clamp(prev.getText().length() - text.length(), 0, prev.getText().length());
				prev.setCursorPosition(pos);
				
				return prev;
			}
		}
		return null;
	}
	
	public WindowTextArea setLineNumberDrawn(int lineNumber) { return setLineNumberDrawn(getTextLine(lineNumber)); }
	public WindowTextArea setLineNumberDrawn(TextAreaLine lineIn) {
		if (lineIn != null) {
			double lineYPos = lineIn.endY + lineIn.height;
			double difference = lineYPos - startY;
			
			getVScrollBar().setScrollBarPos((int) difference);
		}
		return this;
	}
	
	public WindowTextArea clear() {
		setListWidth(width - 2);
		setListHeight(0);
		Iterator it = windowObjects.iterator();
		while (it.hasNext()) {
			if (it.next() instanceof TextAreaLine) { it.remove(); }
		}
		listContents.clear();
		textDocument.clear();
		currentLine = null;
		longestLine = null;
		return this;
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
	
	//----------------------
	//WindowTextArea Getters
	//----------------------
	
	public TextAreaLine getLineMouseIsOver() {
		double mPosY = mY - startY - 1;
		double scrollOffset = verticalScroll.getScrollPos() - verticalScroll.getVisibleAmount();
		double posY = mPosY + scrollOffset;
		
		int num = (int) ((posY / lineHeight) + 1);
		TextAreaLine l = getTextLine(num);
		
		return l;
	}
	
	public int getLongestLineLength() {
		longestLine = getLongestTextLine();
		return longestLine != null ? getStringWidth(longestLine.getText()) : - 1;
	}
	
	public TextAreaLine getTextLine(int numIn) {
		if (numIn > 0 && numIn <= textDocument.size()) {
			int first = 0;
			int last = textDocument.size();
			int mid = (first + last) / 2;
			while (first <= last && mid < textDocument.size()) {
				TextAreaLine l = textDocument.get(mid);
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
	
	public TextAreaLine getLineWithText(String textIn) {
		for (TextAreaLine l : textDocument) {
			if (l.getText().equals(textIn)) { return l; }
		}
		return null;
	}
	
	public TextAreaLine getLineWithObject(Object objectIn) {
		for (TextAreaLine l : textDocument) {
			if (l.getStoredObj() == null) {
				if (objectIn == null) { return l; }
			}
			else if (l.getStoredObj().equals(objectIn)) { return l; }
		}
		return null;
	}
	
	public TextAreaLine getLineWithTextAndObject(String textIn, Object objectIn) {
		for (TextAreaLine l : textDocument) {
			if (l.getStoredObj() == null ) {
				if (l.getText().equals(textIn) && objectIn == null) { return l; }
			}
			else if (l.getText().equals(textIn) && l.getStoredObj().equals(objectIn)) { return l; }
		}
		return null;
	}
	
	public TextAreaLine getLongestTextLine() {
		TextAreaLine longest = null;
		int longestLen = 0;
		for (TextAreaLine l : textDocument) {
			int len = getStringWidth(l.getText());
			if (len > longestLen) { longest = l; longestLen = len; }
		}
		return longest;
	}
	
	public int getLineHeight() { return lineHeight; }
	public int getLineCount() { return ((int) height - 2) / 10; }
	public boolean hasLineNumbers() { return drawLineNumbers; }
	public int getLineNumberOffset() { return hasLineNumbers() ? (6 + String.valueOf(textDocument.size()).length() * getStringWidth("0")) : 2; }
	public boolean getDrawLineHighlight() { return drawLineHighlight; }
	public boolean isEditable() { return editable; }
	public TextAreaLine getCurrentLine() { return currentLine; }
	public EArrayList<TextAreaLine> getTextDocument() { return textDocument; }
	
	//----------------------
	//WindowTextArea Setters
	//----------------------
	
	public WindowTextArea setTextDocument(EArrayList<TextAreaLine> docIn) {
		clear();
		for (TextAreaLine l : docIn) { addTextLine(l); }
		return this;
	}
	
	public WindowTextArea setLineHeight(int in) { lineHeight = in; return this; }
	public WindowTextArea setDrawLineNumbers(boolean valIn) { drawLineNumbers = valIn; return this; }
	public WindowTextArea setDrawLineHighlight(boolean valIn) { drawLineHighlight = valIn; return this; }
	public WindowTextArea setEditable(boolean valIn) { editable = valIn; return this; }
	public WindowTextArea setDrawLineNumberSeparator(boolean valIn) { drawLineNumberSeparator = valIn; return this; }
	public WindowTextArea setLineNumberSeparatorColor(EColors colorIn) { return setLineNumberSeparatorColor(colorIn.intVal); }
	public WindowTextArea setLineNumberSeparatorColor(int colorIn) { lineNumberSeparatorColor = colorIn; return this; }
	
}
