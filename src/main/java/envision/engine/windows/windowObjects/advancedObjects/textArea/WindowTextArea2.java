package envision.engine.windows.windowObjects.advancedObjects.textArea;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.actionObjects.WindowScrollBar;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2i;
import eutil.math.ENumUtil;
import eutil.misc.ScreenLocation;
import eutil.strings.EStringBuilder;

public class WindowTextArea2 extends WindowObject {
    
    //========
    // Fields
    //========
    
    private boolean isEditing = false;
    private boolean drawLineNumbers = true;
    
    /**
     * The amount of time (in ms) since the last input event occurred.
     * Used to prevent cursor blinks while editing.
     */
    private long timeSinceLastInput = 0L;
    private boolean inputHappenedRecently = false;
    
    private int highlightStartLine = 0;
    private int highlightStartIndex = 0;
    private boolean activelyHighlighting = false;
    
    private long cursorBlinkRate = 800L;
    private long cursorBlinkDelta = 0L;
    private boolean cursorBlinkState = true;
    
    private final Point2i clickPoint = new Point2i();
    
    /** The document that is being displayed. */
    private TextDocument document;
    
    //----------------------------
    // Fields : Drawing Locations
    //----------------------------
    
    /** The px x coordinate of where the line number area starts. */
    private double lnStartX;
    /** The px y coordinate of where the line number area starts. */
    private double lnStartY;
    /** The px x coordinate of where the line number area ends. */
    private double lnEndX;
    /** The px y coordinate of where the line number area ends. */
    private double lnEndY;
    
    /** The px x location of where line number text starts. */
    private double lnTextStartX;
    /** The px y location of where line number text starts. */
    private double lnTextStartY;
    
    /** The px x coordinate of where text area content starts. */
    private double textAreaStartX;
    /** The px y coordinate of where text area content starts. */
    private double textAreaStartY;
    /** The px x coordinate of where text area content ends. */
    private double textAreaEndX;
    /** The px y coordinate of where text area content ends. */
    private double textAreaEndY;
    
    /** The px x location of where text area content starts. */
    private double textStartX;
    /** The px y location of where text area content starts. */
    private double textStartY;
    
    //-------------------
    // Fields : children
    //-------------------
    
    private WindowScrollBar vScroll;
    private WindowScrollBar hScroll;
    
    protected double scrollableHeight = 0;
    protected double scrollableWidth = 0;
    
    //==============
    // Constructors
    //==============
    
    public WindowTextArea2(IWindowObject parentIn, double x, double y, double w, double h) {
        this(parentIn, x, y, w, h, null);
    }
    
    public WindowTextArea2(IWindowObject parentIn, double x, double y, double w, double h, TextDocument documentIn) {
        init(parentIn, x, y, w, h);
        
        if (documentIn == null) document = new TextDocument();
        else document = documentIn;
        
        scrollableWidth = w - 2.0;
        scrollableHeight = h - 2.0;
        
        vScroll = new WindowScrollBar(this, ScreenLocation.RIGHT, 7);
        hScroll = new WindowScrollBar(this, ScreenLocation.BOT, 7);
        
        vScroll.setHighVal(scrollableHeight);
        hScroll.setHighVal(scrollableWidth);
        
        vScroll.setScrollRate(50);
        
        document.updateLineNumberOffset();
        determineTextAreaDimensions();
        determineScrollableDimensions();
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        addObject(vScroll, hScroll);
    }
    
    @Override
    public void drawObject_i(long dt, int mXIn, int mYIn) {
        if (!Mouse.isLeftDown()) activelyHighlighting = false;
        if (activelyHighlighting && !clickPoint.compare(mXIn, mYIn)) determineHighlightEnd();
        
        updateBeforeNextDraw(mXIn, mYIn);
        determineTextAreaDimensions();
        
        drawRect(EColors.black);
        drawRect(EColors.pdgray, 1);
        
        scissor(startX + 1, startY + 1, endX - 1, endY - 1);
        handleTextDrawing(dt);
        endScissor();
        
        if (isHScrollDrawn()) drawRect(startX, hScroll.startY - 1, endX, endY, EColors.black);
        if (isVScrollDrawn()) drawRect(vScroll.startX - 1, startY, endX, endY, EColors.black);
        
        // draw children as normal (non scissored)
        for (var o : getChildren()) {
            if (!o.willBeDrawn()) continue;
            
            if (!o.hasFirstDraw()) o.onFirstDraw_i();
            o.drawObject_i(dt, mXIn, mYIn);
        }
    }
    
    @Override
    public void mousePressed(int mXIn, int mYIn, int button) {
        clickPoint.set(mXIn + 1, mYIn);
        
        // transform mouse x/y into coordinates relative to text area x/y
        double clickX = mXIn - textStartX;
        double clickY = mYIn - textStartY;
        
        // determine character position (by character size) within area from click position
        int clickLine = (int) (clickY / FontRenderer.FONT_HEIGHT);
        int clickLineIndex = (int) (clickX / FontRenderer.getCharWidth());
        
        if (clickLine >= document.getNumberOfLines() ||
            clickLine == document.getNumberOfLines() - 1 && clickLineIndex >= document.getLine(document.getNumberOfLines() - 1).length()) {
            document.clearHighlight();
        }
        
        document.setCurrentLineAndIndex(clickLine, clickLineIndex);
        
        highlightStartLine = document.getCurrentLine();
        highlightStartIndex = document.getCurrentLineIndex();
        activelyHighlighting = true;

        startInputTimer();
        
        super.mousePressed(mXIn, mYIn, button);
    }
    
    @Override
    public void mouseReleased(int mXIn, int mYIn, int button) {
        // temp
        if (activelyHighlighting) {
            determineHighlightEnd();
            DeveloperDesktop.setTerminalClipboard(document.getHighlightedText());
        }
        
        activelyHighlighting = false;
        
        super.mouseReleased(mXIn, mYIn, button);
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        if (Keyboard.isCtrlA(keyCode)) { document.setHighlightedArea(0, document.length()); }
        else if (Keyboard.isCtrlC(keyCode)) { Keyboard.setClipboard(document.getHighlightedText()); }
        else if (Keyboard.isCtrlV(keyCode) && isEnabled()) {
            document.insertString(Keyboard.getClipboard(), document.getCursorPos());
        }
        else if (Keyboard.isCtrlX(keyCode)) {
//            Keyboard.setClipboard(getSelectedText());
//            if (isEnabled() && parentTextArea.isEditable()) {
//                writeText("");
//            }
        }
        else if (Keyboard.isCtrlF(keyCode)) {
            document.searchForText("Ban Ban");
        }
        else {
            switch (keyCode) {
            case Keyboard.KEY_LEFT: document.advanceCursor(-1); break;
            case Keyboard.KEY_RIGHT: document.advanceCursor(1); break;
            case Keyboard.KEY_UP: document.selectPreviousLine(); break;
            case Keyboard.KEY_DOWN: document.selectNextLine(); break;
            case Keyboard.KEY_HOME: document.moveCursorToStartOfLine(); break;
            case Keyboard.KEY_END: document.moveCursorToEndOfLine(); break;
            case Keyboard.KEY_BACKSPACE: document.backspaceAtCursor(); break;
            case Keyboard.KEY_DELETE: document.deleteAtCursor(); break;
            case Keyboard.KEY_ENTER: document.newLineAtCursor(); break;
            case Keyboard.KEY_TAB: document.insertString("    "); break;
            default:
                if (Keyboard.isTypable(typedChar, keyCode)) {
                    typedChar = (Keyboard.isShiftDown()) ? Keyboard.getUppercase(keyCode) : typedChar;
                    document.insertChar(typedChar);
                }
            }
        }
        
//        int line = document.getCurrentLine();
//        int lineIndex = document.getCurrentLineIndex();
//        int cursorPos = document.getCursorPos();

//        String c = "END";
//        if (cursorPos < document.length()) {
//            c = document.charAt(cursorPos) + "";
//        }
//        getParent().getHeader().setTitle(line + " : " + lineIndex + " : " + cursorPos + " : " + c);
        
        startInputTimer();
        determineTextAreaDimensions();
        determineScrollableDimensions();
        
        super.keyPressed(typedChar, keyCode);
    }
    
    @Override
    public void keyReleased(char typedChar, int keyCode) {
        super.keyReleased(typedChar, keyCode);
    }
    
    @Override
    public void mouseScrolled(int change) {
        if (Keyboard.isShiftDown()) {
            if (scrollableWidth - (width - 2) > 0) {
                hScroll.setScrollPos(hScroll.getScrollPos() - change * hScroll.getScrollRate());
            }
        }
        else if (scrollableHeight - (height - 2) > 0) {
            vScroll.setScrollPos(vScroll.getScrollPos() - change * vScroll.getScrollRate());
        }
        
        startInputTimer();
        
        super.mouseScrolled(change);
    }
    
    @Override
    public void onFocusGained(EventFocus eventIn) {
        if (document.isEditable()) isEditing = true;
        
        startInputTimer();
        
        super.onFocusGained(eventIn);
    }
    
    @Override
    public void onFocusLost(EventFocus eventIn) {
        isEditing = false;
        
        super.onFocusLost(eventIn);
    }
    
    //========================
    // Dimension Calculations
    //========================
    
    protected void determineTextAreaDimensions() {
        boolean isHScrollDrawn = isHScrollDrawn();
        boolean isVScrollDrawn = isVScrollDrawn();
        
        hScroll.setVisible(isHScrollDrawn);
        vScroll.setVisible(isVScrollDrawn);
        
        double hScrollPixelArea = ((isHScrollDrawn) ? hScroll.height + 1 : 0);
        double vScrollPixelArea = ((isVScrollDrawn) ? vScroll.width + 1 : 0);
        
        // line numbers area
        lnStartX = startX + 1;
        lnStartY = startY + 1;
        lnEndX = startX + 2 + document.getCurrentLineNumberPixelWidth() + 2;
        lnEndY = endY - 1 - hScrollPixelArea;
        
        // line number text position
        lnTextStartX = lnStartX + 1;
        lnTextStartY = lnStartY + 1;
        
        // text area
        textAreaStartX = ((drawLineNumbers) ? lnEndX + 2 : startX + 2);
        textAreaStartY = startY + 1;
        textAreaEndX = endX - 1 - vScrollPixelArea;
        textAreaEndY = endY - 1 - hScrollPixelArea;
        
        // position for where to start drawing text
        textStartX = textAreaStartX + 2;
        textStartY = textAreaStartY + 1;
        
        vScroll.setVisibleAmount(textAreaEndY - textAreaStartY);
        hScroll.setVisibleAmount(textAreaEndX - textAreaStartX);
        
        // apply scroll offsets to drawing
        double hScrollAmount = hScroll.getScrollPos() - hScroll.getVisibleAmount();
        double vScrollAmount = vScroll.getScrollPos() - vScroll.getVisibleAmount();
        
        lnTextStartY -= vScrollAmount;
        textStartX -= hScrollAmount;
        textStartY -= vScrollAmount;
    }
    
    protected void determineScrollableDimensions() {
        double longestLineWidth = FontRenderer.strWidth(document.getLongestLine()) + FontRenderer.getCharWidth() * 4;
        double totalLineHeight = document.getNumberOfLines() * FontRenderer.FONT_HEIGHT;
        
        scrollableWidth = ENumUtil.clamp(longestLineWidth, textAreaEndX - textAreaStartX, Integer.MAX_VALUE);
        scrollableHeight = ENumUtil.clamp(totalLineHeight, textAreaEndY - textAreaStartY, Integer.MAX_VALUE);
        
        vScroll.setHighVal(scrollableHeight);
        hScroll.setHighVal(scrollableWidth);
    }
    
    protected void determineHighlightEnd() {
        int mx = Mouse.getMx();
        int my = Mouse.getMy();
        
        clickPoint.set(mx, my);
        
        // transform mouse x/y into coordinates relative to text area x/y
        double clickX = mx - textStartX;
        double clickY = my - textStartY;
        
        // determine character position (by character size) within area from click position
        int clickLine = (int) (clickY / FontRenderer.FONT_HEIGHT);
        int clickLineIndex = (int) ((clickX + FontRenderer.getCharWidth() / 2) / FontRenderer.getCharWidth());
        
        document.setHighlightedArea(highlightStartLine, highlightStartIndex, clickLine, clickLineIndex);
        document.setCurrentLineAndIndex(clickLine, clickLineIndex);            
        
        startInputTimer();
    }
    
    //=========
    // Drawing
    //=========
    
    protected void handleTextDrawing(long dt) {
        // highlight current line
        drawCurrentLineBackground();
        
        // draw the highlighted portion of the document
        drawHighlightedText();
        
        // draw document lines
        drawDocumentText();
        
        // draw cursor
        drawCursor(dt);
        
        // draw lineNumber backdrop and separator
        if (drawLineNumbers) drawLineNumbers();
    }
    
    /** Draw this after everything to casually avoid dealing with scissoring issues. */
    private void drawLineNumbers() {
        // backdrop
        drawRect(lnStartX, lnStartY, lnEndX, lnEndY, EColors.lsteel);
        // separator
        drawRect(lnEndX, lnStartY, lnEndX + 1, lnEndY, EColors.dsteel);
        
        // draw line numbers
        for (int i = 0, n = 1; i < document.getNumberOfLines(); i++, n++) {
            double yPos = lnTextStartY + (FontRenderer.FONT_HEIGHT * i);
            
            int numLen = String.valueOf(i + 1).length();
            double xPos = lnTextStartX + (document.getLineNumberCharsLength() - numLen) * FontRenderer.getCharWidth();
            
            // draw line num string
            drawString(n, xPos, yPos, EColors.mgray);
        }
    }
    
    private void drawCurrentLineBackground() {
        // only draw if not currently highlighting text
        if (document.getHighlightStartIndex() != document.getHighlightEndIndex()) return;
        
        double x = textAreaStartX;
        double y = textAreaStartY + (FontRenderer.FH * document.getCurrentLine());
        
        y -= vScroll.getScrollPos() - vScroll.getVisibleAmount();
        
        drawRect(x, y, endX - 1, y + FontRenderer.FH - 1, EColors.dgray);
    }
    
    private void drawHighlightedText() {
        // only draw if currently highlighting text
        if (document.getHighlightStartIndex() == document.getHighlightEndIndex()) return;
        
        int highlightStartLine = document.getHighlightStartLine();
        int highlightStartLineIndex = document.getHighlightStartLineIndex();
        int highlightEndLine = document.getHighlightEndLine();
        int highlightEndLineIndex = document.getHighlightEndLineIndex();
        
        // draw start highlight line
        double sx = textAreaStartX + (FontRenderer.getCharWidth() * highlightStartLineIndex);
        double ex = textAreaStartX + (FontRenderer.getCharWidth() * highlightEndLineIndex);
        
        
        int highlightColor = EColors.blue.brightness(200);
        
        if (highlightStartLine == highlightEndLine) {
            double sy = textAreaStartY + (FontRenderer.FH * highlightStartLine);
            sy -= vScroll.getScrollPos() - vScroll.getVisibleAmount();
            drawRect(sx, sy, ex, sy + FontRenderer.FH, highlightColor);
            return;
        }
        
        // highlight multiple lines
        final int lines = document.getNumberOfLines();
        for (int i = highlightStartLine; i < lines; i++) {
            double y = textAreaStartY + (FontRenderer.FH * i);
            y -= vScroll.getScrollPos() - vScroll.getVisibleAmount();
            
            if (i == highlightStartLine) {
                drawRect(sx, y, endX, y + FontRenderer.FH, highlightColor);
            }
            else if (i == highlightEndLine) {
                drawRect(textAreaStartX, y, ex, y + FontRenderer.FH, highlightColor);
                break;
            }
            else {
                drawRect(textAreaStartX, y, endX, y + FontRenderer.FH, highlightColor);
            }
        }
    }
    
    /**
     * 
     * @param baseOffset
     * @return The current cursor line index
     */
    private void drawDocumentText() {
        // the current line number chars are being drawn to
        int lineNum = 0;
        // the line's current index
        int lineIndex = 0;
        // the index of the cursor in the current line
        //int cursorLineIndex = 0;
        
        final var charWidth = FontRenderer.getCharWidth();
        final var charHeight = FontRenderer.FONT_HEIGHT;
        
        final EStringBuilder sb = new EStringBuilder();
        double xPos;
        double yPos;
        
        // i = document index
        for (int i = 0; i < document.length(); i++) {
            char c = document.charAt(i);
            
            if (c == '\n') {
                xPos = textStartX;// + lineIndex * charWidth;
                yPos = textStartY + lineNum * charHeight;
                drawString(sb, xPos, yPos);
                
                lineNum++;
                lineIndex = 0;
                sb.clear();
            }
            else {
                sb.a(c);
                lineIndex++;
            }
        }
        
        if (sb.isNotEmpty()) {
            xPos = textStartX;
            yPos = textStartY + lineNum * charHeight;
            drawString(sb, xPos, yPos);            
        }
    }
    
    protected void drawCursor(long dt) {
        cursorBlinkDelta += dt;
        
        // if input happened recently, don't blink the cursor
        if (inputHappenedRecently && timeSinceLastInput >= 300) {
            timeSinceLastInput = 0l;
            cursorBlinkDelta = 0;
            cursorBlinkState = true;
            inputHappenedRecently = false;
        }
        
        if (cursorBlinkDelta >= cursorBlinkRate) {
            cursorBlinkDelta = 0L;
            cursorBlinkState = !cursorBlinkState;
        }
        
        if (inputHappenedRecently || cursorBlinkState) {
            double sx = textStartX + (FontRenderer.getCharWidth() * document.getCurrentLineIndex()) - 2;
            double sy = textStartY + (FontRenderer.FONT_HEIGHT * document.getCurrentLine());
            
            drawRect(sx, sy, sx + 2, sy + FontRenderer.FONT_HEIGHT - 1, EColors.chalk);
        }
    }
    
    //=========
    // Methods
    //=========
    
    public void startInputTimer() {
        timeSinceLastInput = System.currentTimeMillis();
        inputHappenedRecently = true;
    }
    
    //=========
    // Getters
    //=========
    
    boolean isEditable() { return document.isEditable(); }
    boolean areLineNumbersEnabled() { return drawLineNumbers; }
    
    public TextDocument getDocument() { return document; }
    public long getCursorBlinkRate() { return cursorBlinkRate; }
    
    public boolean isVScrollDrawn() { return vScroll.getHighVal() > vScroll.getVisibleAmount(); }
    public boolean isHScrollDrawn() { return hScroll.getHighVal() > hScroll.getVisibleAmount(); }
    
    //=========
    // Setters
    //=========
    
    public void setEditable(boolean val) { document.setEditable(val);}
    public void setDrawLineNumbers(boolean val) { drawLineNumbers = val; }
    
    public void setCursorBlinkRate(long timeInMS) {
        cursorBlinkRate = ENumUtil.clamp(timeInMS, 0, Integer.MAX_VALUE);
        cursorBlinkDelta = 0L;
    }
    
    public void setText(String textIn) {
        document.setDocumentText(textIn);
    }
    
    public void setDocument(TextDocument documentIn) {
        document = documentIn;
    }
    
}
