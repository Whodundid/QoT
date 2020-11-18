package eWindow.windowObjects.actionObjects;

import eWindow.windowTypes.ActionObject;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.EObjectGroup;
import eWindow.windowUtil.windowEvents.events.EventFocus;
import gameSystems.fontRenderer.FontRenderer;
import input.Keyboard;
import main.Game;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;

//Author: Hunter Bragg

public class WindowTextField extends ActionObject {
	
	public String text = "", textWhenEmpty = "";
	public int maxStringLength = 32;
	public int textWhenEmptyColor = 0xb2b2b2;
	public int disabledColor = 7368816;
	public int textColor = 14737632;
	public int backgroundColor = 0xff000000;
	public int borderColor = -6250336;
	protected boolean editable = true;
	protected boolean enableBackgroundDrawing = true;
	protected boolean alwaysDrawCursor = false;
	protected boolean onlyAcceptLetters = false;
	protected boolean onlyAcceptNumbers = false;
	protected boolean allowClipboardPastes = true;
	protected boolean useObjectGroupForCursorDraw = false;
	protected boolean drawShadowed = true;
	protected boolean textRecentlyEntered = false;
	protected long startTime = 0l;
	protected int clickStartPos = -1;
	protected int cursorCounter;
	protected int lineScrollOffset;
	protected int cursorPosition;
	protected int selectionEnd;

	protected WindowTextField() {}
	public WindowTextField(IWindowObject parentIn, double x, double y, double widthIn, double heightIn) {
		init(parentIn, x, y, widthIn, heightIn);
		//Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		if (getEnableBackgroundDrawing()) {
			drawRect(startX - 1, startY - 1, endX + 3, endY + 1, borderColor);
			drawRect(startX, startY, endX + 2, endY, backgroundColor);
		}
		
		if (textRecentlyEntered == true) {
			if (System.currentTimeMillis() - startTime >= 300) {
				startTime = 0l;
				textRecentlyEntered = false;
			}
		}
		
		int drawColor = isEnabled() ? textColor : disabledColor;
		int selStart = cursorPosition - lineScrollOffset;
		int selEnd = selectionEnd - lineScrollOffset;
		String s = text.substring(lineScrollOffset);
		
		boolean hasSel = selStart >= 0 && selStart <= s.length();
		boolean drawCursor = false;
		boolean cursorCounter = (Game.updateCounter / 20 % 2 == 0 || textRecentlyEntered) && hasSel;
		
		if (alwaysDrawCursor) { drawCursor = cursorCounter; }
		else if (useObjectGroupForCursorDraw) {
			EObjectGroup g = getObjectGroup();
			if (g != null) { drawCursor = g.doAnyHaveFocus() && cursorCounter && (selStart == selEnd); }
		}
		else { drawCursor = hasFocus() && cursorCounter && isEnabled() && (selStart == selEnd); }
		
		double textStartX = enableBackgroundDrawing ? startX + 4 : startX + 2;
		double textStartY = enableBackgroundDrawing ? startY + (height - 6) / 2 : startY + height / 2 - 3;
		double xOffset = textStartX;
		
		if (selEnd > s.length()) { selEnd = s.length(); }
		if (s.length() > 0) {
			String s1 = hasSel ? s.substring(0, selStart) : s;
			if (drawShadowed) { xOffset = drawStringS(s1, textStartX, textStartY, drawColor); }
			else { xOffset = drawString(s1, textStartX, textStartY, drawColor); }
		}
		
		boolean flag2 = cursorPosition < text.length() || text.length() >= getMaxStringLength();
		double k1 = xOffset;
		
		if (!hasSel) { k1 = selStart > 0 ? textStartX + width : textStartX; } 
		else if (flag2) {
			k1 = xOffset - 1;
			xOffset--;
		}
		
		//xOffset++;
		
		if (s.length() > 0 && hasSel && selStart < s.length()) {
			if (drawShadowed) { xOffset = drawStringS(s.substring(selStart), xOffset, textStartY, drawColor); }
			else { xOffset = drawString(s.substring(selStart), xOffset + 1, textStartY, drawColor); }
		}
		
		if (drawCursor) {
			if (flag2) {
				drawRect(k1, textStartY - 2, k1 + 1, textStartY - 1 + FontRenderer.FONT_HEIGHT, -3092272);
			}
			else {
				if (drawShadowed) { drawStringS("_", k1, textStartY, drawColor); }
				else { drawString("_", k1, textStartY, drawColor); }
			}
		}
		
		if (selEnd != selStart && hasFocus()) {
			double l1 = textStartX + getStringWidth(s.substring(0, selEnd));
			drawCursorVertical(k1 + (drawShadowed ? 0 : 1), textStartY - 1, l1 - 1, textStartY - 1 + FontRenderer.FONT_HEIGHT);
		}
		
		if (clickStartPos != -1) {
			int i = (int) (mX - startX + 2);
			if (enableBackgroundDrawing) { i -= 4; }
			//int cursorPos = Game.getFontRenderer().trimToWidth(text, i).length();
			int cursorPos = text.length();
			setSelectionPos(cursorPos);
		}
		
		super.drawObject(mX, mY);
	}
	
	@Override 
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (hasFocus() && button == 0) {
			int i = (int) (mX - startX + 2);
			if (enableBackgroundDrawing) { i -= 4; }
			
			//int cursorPos = Game.getFontRenderer().trimToWidth(text, i).length();
			int cursorPos = text.length();
			setCursorPosition(cursorPos);
			
			if (clickStartPos == -1) { clickStartPos = cursorPos; }
		}
		startTextTimer();
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		
		if (clickStartPos != -1) { clickStartPos = -1; }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (editable) {
			startTextTimer();
			if (Keyboard.isCtrlA(keyCode)) {
				setCursorPositionEnd();
				setSelectionPos(0);
			}
			//else if (isKeyComboCtrlC(keyCode)) { GuiScreen.setClipboardString(getSelectedText()); }
			//else if (isKeyComboCtrlV(keyCode) && isEnabled() && allowClipboardPastes) { writeText(GuiScreen.getClipboardString()); }
			//else if (isKeyComboCtrlX(keyCode)) {
			//	GuiScreen.setClipboardString(getSelectedText());
			//	if (isEnabled()) { writeText(""); }
			//}
			else {
				switch (keyCode) {
				case 259: //backspace
					if (isEnabled()) {
						if (Keyboard.isCtrlDown()) { deleteWords(-1); }
						else { deleteFromCursor(-1); }
					}
					break;
				case 257: //enter
					performAction();
					break;
				case 268: //home
					if (Keyboard.isShiftDown()) { setSelectionPos(0); } 
					else { setCursorPositionZero(); }
					break;
				case 263: //left
					startTextTimer();
					if (Keyboard.isShiftDown()) {
						if (Keyboard.isCtrlDown()) { setSelectionPos(getNthWordFromPos(-1, getSelectionEnd())); } 
						else { setSelectionPos(getSelectionEnd() - 1); }
					}
					else if (Keyboard.isCtrlDown()) { setCursorPosition(getNthWordFromCursor(-1)); } 
					else { moveCursorBy(-1); }
					break;
				case 262: //right
					startTextTimer();
					if (Keyboard.isShiftDown()) {
						if (Keyboard.isCtrlDown()) { setSelectionPos(getNthWordFromPos(1, getSelectionEnd())); }
						else { setSelectionPos(getSelectionEnd() + 1); }
					}
					else if (Keyboard.isCtrlDown()) { setCursorPosition(getNthWordFromCursor(1)); } 
					else { moveCursorBy(1); }
					break;
				case 269: //end
					if (Keyboard.isShiftDown()) { setSelectionPos(text.length()); } 
					else { setCursorPositionEnd(); }
					break;
				case 261: //delete
					startTextTimer();
					if (Keyboard.isCtrlDown()) {
						if (isEnabled()) { deleteWords(1); }
					}
					else if (isEnabled()) { deleteFromCursor(1); }
					break;
				default:
					if (isEnabled() && Keyboard.isTypable(typedChar)) {
						if (onlyAcceptLetters) { if (Character.isLetter(typedChar)) { writeText(Character.toString(typedChar)); } }
						else if (onlyAcceptNumbers) { if (Character.isDigit(typedChar)) { writeText(Character.toString(typedChar)); } }
						else { writeText(Character.toString(typedChar)); }
					}
					break;
				}
			} //else
		}
		
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		startTextTimer();
		if (!alwaysDrawCursor) { Game.updateCounter = 0; }
		if (text.equals(textWhenEmpty)) { text = ""; setTextColor(textColor); setCursorPosition(0); }
		super.onFocusGained(eventIn);
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		if (text.isEmpty()) { text = textWhenEmpty; setTextColor(textWhenEmptyColor); }
	}
	
	public void startTextTimer() {
		startTime = System.currentTimeMillis();
		textRecentlyEntered = true;
	}
	
	public void writeText(String textIn) {
		String s = "";
		String s1 = Keyboard.removeUntypables(textIn);
		int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
		int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
		int k = maxStringLength - text.length() - (i - j);
		int l = 0;
		
		if (text.length() > 0) { s = s + text.substring(0, i); }
		
		if (k < s1.length()) {
			s = s + s1.substring(0, k);
			l = k;
		}
		else {
			s = s + s1;
			l = s1.length();
		}
		
		if (text.length() > 0 && j < text.length()) { s = s + text.substring(j); }
		
		text = s;
		moveCursorBy(i - selectionEnd + l);
		startTextTimer();
	}

	/** Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of the cursor. */
	public void deleteWords(int numberOfWords) {
		if (text.length() != 0) {
			if (selectionEnd != cursorPosition) { writeText(""); }
			else { deleteFromCursor(getNthWordFromCursor(numberOfWords) - cursorPosition); }
		}
	}

	/** delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num */
	public void deleteFromCursor(int p_146175_1_) {
		if (text.length() != 0) {
			if (selectionEnd != cursorPosition) {
				writeText("");
			}
			else {
				boolean flag = p_146175_1_ < 0;
				int i = flag ? cursorPosition + p_146175_1_ : cursorPosition;
				int j = flag ? cursorPosition : cursorPosition + p_146175_1_;
				String s = "";
				if (i >= 0) { s = text.substring(0, i); }
				if (j < text.length()) { s = s + text.substring(j); }
				text = s;
				if (flag) { moveCursorBy(p_146175_1_); }
			}
		}
	}

	/** see @getNthWordFromPos() params: N, position */
	public int getNthWordFromCursor(int pos) { return getNthWordFromPos(pos, getCursorPosition()); }
	
	/** gets the position of the nth word. N may be negative, then it looks backwards. params: N, position */
	public int getNthWordFromPos(int posIn, int cursorPos) { return getNthWordFromPos(posIn, cursorPos, true); }
	public int getNthWordFromPos(int posIn, int cursorPos, boolean p_146197_3_) {
		int i = cursorPos;
		boolean flag = posIn < 0;
		int j = Math.abs(posIn);
		for (int k = 0; k < j; ++k) {
			if (!flag) {
				int l = text.length();
				i = text.indexOf(32, i);
				
				if (i == -1) { i = l; }
				else {
					while (p_146197_3_ && i < l && text.charAt(i) == 32) { ++i; }
				}
			}
			else {
				while (p_146197_3_ && i > 0 && text.charAt(i - 1) == 32) { --i; }
				while (i > 0 && text.charAt(i - 1) != 32) { --i; }
			}
		}
		return i;
	}

	/** draws the vertical line cursor in the textbox */
	protected void drawCursorVertical(double x, double y, double w, double h) {
		if (x < w) {
			double i = x;
			x = w;
			w = i;
		}
		if (y < h) {
			double j = y;
			y = h;
			h = j;
		}
		//if (w > startX + width) { w = startX + width; }
		//if (x > startX + width) { x = startX + width; }
		
		//Tessellator tessellator = Tessellator.getInstance();
		//WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		//GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		//GlStateManager.disableTexture2D();
		//GlStateManager.enableColorLogic();
		//GlStateManager.colorLogicOp(5387);
		//worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		//worldrenderer.pos(x, h, 0.0D).endVertex();
		//worldrenderer.pos(w, h, 0.0D).endVertex();
		//worldrenderer.pos(w, y, 0.0D).endVertex();
		//worldrenderer.pos(x, y, 0.0D).endVertex();
		//tessellator.draw();
		//GlStateManager.disableColorLogic();
		//GlStateManager.enableTexture2D();
	}
	
	public String getSelectedText() {
		int startPos = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
		int endPos = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
		return text.substring(startPos, endPos);
	}
	
	public WindowTextField setCursorPosition(int posIn) {
		cursorPosition = posIn;
		int i = text.length();
		cursorPosition = NumUtil.clamp(cursorPosition, 0, i);
		setSelectionPos(cursorPosition);
		return this;
	}

	public WindowTextField setMaxStringLength(int lengthIn) {
		maxStringLength = lengthIn;
		if (text.length() > lengthIn) { text = text.substring(0, lengthIn); }
		return this;
	}
	
	/** Sets the position of the selection anchor (i.e. position the selection was started at) */
	public WindowTextField setSelectionPos(int posIn) {
		int i = text.length();
		if (posIn > i) { posIn = i; }
		if (posIn < 0) { posIn = 0; }
		selectionEnd = posIn;
		
		if (Game.getFontRenderer() != null) {
			if (lineScrollOffset > i) { lineScrollOffset = i; }
			int j = (int) getWidth();
			//String s = Game.getFontRenderer().trimToWidth(text.substring(lineScrollOffset), j);
			String s = text.substring(lineScrollOffset);
			int k = s.length() + lineScrollOffset;
			//if (posIn == lineScrollOffset) { lineScrollOffset -= Game.getFontRenderer().trimToWidth(text, j, true).length(); }
			if (posIn == lineScrollOffset) { lineScrollOffset -= text.length(); }
			if (posIn > k) { lineScrollOffset += posIn - k; } 
			else if (posIn <= lineScrollOffset) { lineScrollOffset -= lineScrollOffset - posIn; }
			lineScrollOffset = NumUtil.clamp(lineScrollOffset, 0, i);
		}
		return this;
	}

	public WindowTextField setText(String textIn) {
		if (textIn != null) {
			if (textIn.isEmpty()) {
				text = textWhenEmpty;
				setTextColor(textWhenEmptyColor);
			}
			else {
				if (textIn.length() > maxStringLength) { text = textIn.substring(0, maxStringLength); } 
				else {
					String filtered = textIn.replaceAll("\t", "   ");
					text = filtered;
				}
				setTextColor(textColor);
			}
		}
		else { text = ""; }
		setCursorPositionEnd();
		return this;
	}
	
	public void updateCursorCounter() { cursorCounter++; }
	public int getMaxStringLength() { return maxStringLength; }
	public int getCursorPosition() { return cursorPosition; }
	public boolean getEnableBackgroundDrawing() { return enableBackgroundDrawing; }
	public boolean onlyAcceptsletters() { return onlyAcceptLetters; }
	public boolean onlyAcceptsNumbers() { return onlyAcceptNumbers; }
	public boolean drawsShadowed() { return drawShadowed; }
	public boolean allowsClipboardPastes() { return allowClipboardPastes; }
	public int getSelectionEnd() { return selectionEnd; }
	public double getWidth() { return getEnableBackgroundDrawing() ? width - 8 : width; }
	public String getText() { return text; }
	public boolean isEmpty() { return text != null ? text.isEmpty() : true; }
	public boolean isNotEmpty() { return text != null ? !text.isEmpty() : false; }
	public int getTextColor() { return textColor; }
	public int getTextEmptyColor() { return textWhenEmptyColor; }
	public int getBorderColor() { return borderColor; }
	
	public WindowTextField setCursorPositionZero() { setCursorPosition(0); return this; }
	public WindowTextField setCursorPositionEnd() { setCursorPosition(text.length()); return this; }
	public WindowTextField setEnableBackgroundDrawing(boolean val) { enableBackgroundDrawing = val; return this; }
	public WindowTextField setEditable(boolean val) { editable = val; return this; }
	public WindowTextField setTextColor(int colorIn) { textColor = colorIn; return this; }
	public WindowTextField setTextColor(EColors colorIn) { textColor = colorIn.intVal; return this; }
	public WindowTextField setDisabledTextColor(int colorIn) { disabledColor = colorIn; return this; }
	public WindowTextField setDisabledTextColor(EColors colorIn) { disabledColor = colorIn.intVal; return this; }
	public WindowTextField setAlwaysDrawCursor(boolean val) { alwaysDrawCursor = val; return this; }
	public WindowTextField setUseObjectGroupForCursorDraws(boolean val) { useObjectGroupForCursorDraw = val; return this; }
	public WindowTextField setOnlyAcceptLetters(boolean val) { onlyAcceptLetters = val; return this; }
	public WindowTextField setOnlyAcceptNumbers(boolean val) { onlyAcceptNumbers = val; return this; }
	public WindowTextField setAllowClipboardPastes(boolean val) { allowClipboardPastes = val; return this; }
	public WindowTextField setTextWhenEmpty(String textIn) { textWhenEmpty = textIn; text = textWhenEmpty; setTextColor(textWhenEmptyColor); return this; }
	public WindowTextField setTextWhenEmptyColor(int colorIn) { textWhenEmptyColor = colorIn; return this; }
	public WindowTextField setTextWhenEmptyColor(EColors colorIn) { textWhenEmptyColor = colorIn.intVal; return this; }
	public WindowTextField moveCursorBy(int moveAmount) { setCursorPosition(selectionEnd + moveAmount); return this; }
	public WindowTextField clear() { setText(""); return this; }
	public WindowTextField setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public WindowTextField setBackgroundColor(EColors colorIn) { backgroundColor = colorIn.intVal; return this; }
	public WindowTextField setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowTextField setBorderColor(EColors colorIn) { borderColor = colorIn.intVal; return this; }
	public WindowTextField setDrawShadowed(boolean val) { drawShadowed = val; return this; }
	
}
