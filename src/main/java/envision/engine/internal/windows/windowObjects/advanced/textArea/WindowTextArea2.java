package envision.engine.internal.windows.windowObjects.advanced.textArea;

import envision.engine.internal.inputHandlers.Keyboard;
import envision.engine.internal.inputHandlers.Mouse;
import envision.engine.internal.kernel.developerDesktop.DeveloperDesktop;
import envision.engine.internal.rendering.fontRenderer.EStringOutputFormatter;
import envision.engine.internal.rendering.fontRenderer.FontRenderer;
import envision.engine.internal.rendering.fontRenderer.GameFont;
import envision.engine.internal.windows.windowObjects.action.WindowScrollBar;
import envision.engine.internal.windows.windowTypes.WindowObject;
import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.events.EventFocus;
import envision_lang.tokenizer.EnvisionTokenizer;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import eutil.colors.EColors;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.misc.ScreenLocation;
import eutil.strings.EStringBuilder;

public class WindowTextArea2 extends WindowObject implements DocumentChangeListener {
    
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
    
    private long cursorBlinkRate = 1200L;
    private long cursorBlinkDelta = 0L;
    private boolean cursorBlinkState = true;
    
    private int fontColor = EColors.white.intVal;
    private int textAreaBackground = EColors.black.intVal;
    private int lineNumbersColor = EColors.lgray.intVal;
    private int lineNumbersAreaBackground = EColors.dgray.intVal;
    private int lineNumbersSeparatorBackground = EColors.lsteel.intVal;
    private int currentLineHighlightColor = EColors.steel.intVal;
    private int highlightForegroundColor = EColors.chalk.intVal;
    private int highlightBackgroundColor = EColors.blue.brightness(200);
    private int cursorColor = EColors.white.intVal;
    
    private boolean clickedInsdieTextArea = false;
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
    /** An amount of pixels to additionally pad out the width of the line number area with. */
    private double lnWidthPadding = 20;
    /** The number of pixels to insert as a border between line numbers and the document. */
    private double lnGap = 12;
    
    /** The px x coordinate of where text area content starts. */
    private double textAreaStartX;
    /** The px y coordinate of where text area content starts. */
    private double textAreaStartY;
    /** The px x coordinate of where text area content ends. */
    private double textAreaEndX;
    /** The px y coordinate of where text area content ends. */
    private double textAreaEndY;
    /** The width of the text area. */
    private double textAreaWidth;
    /** The height of the text area. */
    private double textAreaHeight;
    
    /** The px x location of where text area content starts. */
    private double textStartX;
    /** The px y location of where text area content starts. */
    private double textStartY;
    
    /** An amount of pixels to offset the start of text drawing by in the x axis. */
    private double textStartOffsetX = 1;
    /** An amount of pixels to offset the start of text drawing by in the y axis. */
    private double textStartOffsetY = 2;
    
    private final EStringBuilder internalLineBuffer = new EStringBuilder();
    
    //-------------------
    // Fields : children
    //-------------------
    
    private WindowScrollBar vScroll;
    private WindowScrollBar hScroll;
    
    protected double lastVScrollPos = 0;
    protected double lastHScrollPos = 0;
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
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        final double vScrollPos = vScroll.getScrollPos();
        final double hScrollPos = hScroll.getScrollPos();
        
        if (isMouseInTextArea(mXIn, mYIn)) {
            if (!Mouse.isLeftDown()) {
                activelyHighlighting = false;
                clickedInsdieTextArea = false;
            }
        }
        
        if (clickedInsdieTextArea && !clickPoint.compare(mXIn, mYIn)) {
            activelyHighlighting = true;
            determineHighlightEnd();
        }
        
        updateBeforeNextDraw(mXIn, mYIn);
        if (vScrollPos != lastVScrollPos || hScrollPos != lastHScrollPos) {
            determineTextAreaDimensions();
        }
        
        drawRect(EColors.black);
        drawRect(textAreaBackground, 1);
        
        scissor(startX + 1, startY + 1, endX - 1, endY - 1);
        GameFont curFont = FontRenderer.getCurrentFont();
        try {
            if (curFont != null && curFont.isDefaultBold()) FontRenderer.flipCurrentFontBold(false);
            handleTextDrawing(dt);
        }
        finally {
            if (curFont != null && curFont.isDefaultBold()) FontRenderer.flipCurrentFontBold(true);
        }
        endScissor();
        
        if (isHScrollDrawn()) drawRect(startX, hScroll.startY - 1, endX, endY, EColors.black);
        if (isVScrollDrawn()) drawRect(vScroll.startX - 1, startY, endX, endY, EColors.black);
        
        // draw children as normal (non scissored)
        for (var o : getChildren()) {
            if (!o.willBeDrawn()) continue;
            
            if (!o.hasFirstDraw()) o.onFirstDraw_i();
            o.drawObject_i(dt, mXIn, mYIn);
        }
        
        // debug
        {
            int line = document.getCurrentLine();
            int lineIndex = document.getCurrentLineIndex();
            int cursorPos = document.getCursorPos();

            String c = "END";
            if (cursorPos < document.length()) {
                c = document.charAt(cursorPos) + "";
            }
            
            var header = getParent().getHeader();
            if (header != null) {
                header.setTitle(line + " : " + lineIndex + " : " + cursorPos + " : " + c);
            }
            
//            drawString("colr: " + document.colorChangeLocations.getAVals(), startX, endY + 3);
//            drawString("ital: " + document.italicisedLocations, startX, endY + 23);
//            drawString("bold: " + document.boldLocations, startX, endY + 43);
//            drawString("undr: " + document.underlinedLocations, startX, endY + 63);
        }
        
        lastVScrollPos = vScrollPos;
        lastHScrollPos = hScrollPos;
    }
    
    @Override
    public void move(double dX, double dY) {
        super.move(dX, dY);
        determineTextAreaDimensions();
    }
    
    @Override
    public void mousePressed(int mXIn, int mYIn, int button) {
        if (button != 0 || !isMouseInTextArea(mXIn, mYIn)) {
            super.mousePressed(mXIn, mYIn, button);
            return;
        }
        
        clickedInsdieTextArea = true;
        clickPoint.set(mXIn, mYIn);
        
        // transform mouse x/y into coordinates relative to text area x/y
        double clickX = mXIn - textStartX;
        double clickY = mYIn - textStartY;
        
        // determine character position (by character size) within area from click position
        int clickLine = (int) (clickY / FontRenderer.FONT_HEIGHT);
        int clickLineIndex = (int) ((clickX + FontRenderer.CW() / 2) / FontRenderer.CW());
        
        final int numLines = document.getNumberOfLines();
        
        document.clearHighlight();
        
        if (clickLine >= numLines ||
            (clickLine == numLines - 1 && clickLineIndex >= document.getLine(numLines - 1).length()))
        {
            activelyHighlighting = false;
        }
        
        document.setCurrentLineAndIndex(clickLine, clickLineIndex);
        highlightStartLine = document.getCurrentLine();
        highlightStartIndex = document.getCurrentLineIndex();
        
        startInputTimer();
        
        super.mousePressed(mXIn, mYIn, button);
    }
    
    @Override
    public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {
        super.mouseDragged(mX, mY, button, timeSinceLastClick);
    }
    
    @Override
    public void onDoubleClick() {
        super.onDoubleClick();
    }
    
    protected void determineHighlightEnd() {
        int mx = Mouse.getMx();
        int my = Mouse.getMy();
        
        clickPoint.set(mx, my);
        //activelyHighlighting = true;
        
        // transform mouse x/y into coordinates relative to text area x/y
        double clickX = mx - textStartX;
        double clickY = my - textStartY;
        
        // determine character position (by character size) within area from click position
        int clickLine = (int) (clickY / FontRenderer.FONT_HEIGHT);
        int clickLineIndex = (int) ((clickX + FontRenderer.CW() / 2) / FontRenderer.CW());
        
        document.setCurrentLineAndIndex(clickLine, clickLineIndex);
        document.setHighlightedArea(highlightStartLine, highlightStartIndex, clickLine, clickLineIndex);
        
        {
            final double FH = FontRenderer.FH;
            final double CW = FontRenderer.CW();
            
            double taw = textAreaEndX - textAreaStartX; // text area width
            double tah = textAreaEndY - textAreaStartY; // text area height
            
            // pad the sides a bit so text at the bottom and right edge isn't pushed up RIGHT against either
            double extraWidth = CW * 4;
            
            // figure out values that will help to keep the cursor line in the middle of the window
            int textLinesThatCouldBeVisible = (int) Math.ceil(tah / FH);
            int halfMaxPossible = textLinesThatCouldBeVisible / 2 - 1;
            double halfMaxPossiblePixels = halfMaxPossible * FH;
            
            String curLine = document.getLine(document.getCurrentLine());
            int linePos = ENumUtil.clamp(document.getCurrentLineIndex(), 0, curLine.length());
            String sub = (curLine.substring(0, linePos));
            double subLength = FontRenderer.strWidth(sub);
            
            double hDiff = (subLength - taw);
            double vDiff = document.getCurrentLine() * FH;
            
            hScroll.setScrollPos(taw + hDiff + extraWidth);
            vScroll.setScrollPos(tah + vDiff - halfMaxPossiblePixels);
        }
        
        startInputTimer();
    }
    
    @Override
    public void mouseReleased(int mXIn, int mYIn, int button) {
        // temp
        if (activelyHighlighting) {
            determineHighlightEnd();
            DeveloperDesktop.setTerminalClipboard(document.getHighlightedText());
        }
        
        activelyHighlighting = false;
        clickedInsdieTextArea = false;
        
        super.mouseReleased(mXIn, mYIn, button);
    }
    
    @Override
    public void keyPressed(char typedChar, int keyCode) {
        int documentStartLength = document.length();
        
        // ctrl A -- select all
        if (Keyboard.isCtrlA(keyCode)) {
            document.setHighlightedArea(0, document.length());
        }
        // ctrl C -- copy selection
        else if (Keyboard.isCtrlC(keyCode)) {
            Keyboard.setClipboard(document.getHighlightedText());
        }
        // ctrl V -- paste onto selection
        else if (Keyboard.isCtrlV(keyCode) && isEnabled()) {
            document.insertString(Keyboard.getClipboard(), document.getCursorPos());
        }
        // ctrl X -- cut selection
        else if (Keyboard.isCtrlX(keyCode)) {
            Keyboard.setClipboard(document.getHighlightedText());
            document.deleteHighlightedText();
        }
        // ctrl F -- find text in document
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
        
        //System.out.println(document.getCursorPos() + " : " + document.getCurrentLine() + " : " + document.getCurrentLineIndex());
        
        startInputTimer();
        
        if (documentStartLength != document.length()) {
            determineTextAreaDimensions();
            determineScrollableDimensions();
            formatDocumentForEnvision();
        }
        
        makeCursorVisible();
        
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
    
    @Override
    public void onDocumentChanged() {
        determineTextAreaDimensions();
        determineScrollableDimensions();
        formatDocumentForEnvision();
        makeCursorVisible();
    }
    
    //========================
    // Dimension Calculations
    //========================
    
    protected void determineTextAreaDimensions() {
        boolean isHScrollDrawn = isHScrollDrawn();
        boolean isVScrollDrawn = isVScrollDrawn();
        
        if (isHScrollDrawn && isVScrollDrawn) {
            hScroll.setDimensions(hScroll.startX, hScroll.startY, width - 3 - vScroll.width, hScroll.height);
        }
        else {
            hScroll.setDimensions(hScroll.startX, hScroll.startY, width - 3, hScroll.height);
        }
        
        hScroll.setVisible(isHScrollDrawn);
        vScroll.setVisible(isVScrollDrawn);
        
        double hScrollPixelArea = ((isHScrollDrawn) ? hScroll.height + 1 : 0);
        double vScrollPixelArea = ((isVScrollDrawn) ? vScroll.width + 1 : 0);
        
        // line numbers area
        lnStartX = startX + 1;
        lnStartY = startY + 1;
        lnEndX = startX + 2 + document.getCurrentLineNumberPixelWidth() + lnWidthPadding + lnGap;
        lnEndY = endY - 1 - hScrollPixelArea;
        
        // line number text position
        lnTextStartX = lnStartX + 1;
        lnTextStartY = lnStartY + textStartOffsetY;
        
        // text area
        textAreaStartX = ((drawLineNumbers) ? lnEndX + lnGap : startX + 2);
        textAreaStartY = startY + 1;
        textAreaEndX = endX - 1 - vScrollPixelArea;
        textAreaEndY = endY - 1 - hScrollPixelArea;
        
        // position for where to start drawing text
        textStartX = textAreaStartX + textStartOffsetX;
        textStartY = textAreaStartY + textStartOffsetY;
        
        vScroll.setVisibleAmount(textAreaEndY - textAreaStartY);
        hScroll.setVisibleAmount(textAreaEndX - textAreaStartX);
        
        // apply scroll offsets to drawing
        double hScrollAmount = hScroll.calcScrollOffset();
        double vScrollAmount = vScroll.calcScrollOffset();
        
        lnTextStartY -= vScrollAmount;
        textStartX -= hScrollAmount;
        textStartY -= vScrollAmount;
        
        textAreaWidth = textAreaEndX - textAreaStartX;
        textAreaHeight = textAreaEndY - textAreaStartY;
    }
    
    protected void determineScrollableDimensions() {
        double taw = textAreaEndX - textAreaStartX; // text area width
        double tah = textAreaEndY - textAreaStartY; // text area height

        // pad the sides a bit so text at the bottom and right edge isn't pushed up RIGHT against either
        double extraWidth = FontRenderer.CW() * 4;
        
        double longestLineWidth = FontRenderer.strWidth(document.getLongestLine()) + extraWidth;
        double totalLineHeight = document.getNumberOfLines() * FontRenderer.FONT_HEIGHT;
        
        scrollableWidth = ENumUtil.clamp(longestLineWidth, taw, Integer.MAX_VALUE);
        scrollableHeight = ENumUtil.clamp(totalLineHeight, tah, Integer.MAX_VALUE);
        
        hScroll.setHighVal(scrollableWidth);
        vScroll.setHighVal(scrollableHeight);
    }
    
    public void makeCursorVisible() {
        final double FH = FontRenderer.FH;
        final double CH = FontRenderer.CW();
        
        double taw = textAreaEndX - textAreaStartX; // text area width
        double tah = textAreaEndY - textAreaStartY; // text area height
        
        // pad the sides a bit so text at the bottom and right edge isn't pushed up RIGHT against either
        double extraWidth = CH * 4;
        
        // figure out values that will help to keep the cursor line in the middle of the window
        int textLinesThatCouldBeVisible = (int) Math.ceil(tah / FH);
        int halfMaxPossible = textLinesThatCouldBeVisible / 2 - 1;
        double halfMaxPossiblePixels = halfMaxPossible * FH;
        
        String curLine = document.getLine(document.getCurrentLine());
        int linePos = ENumUtil.clamp(document.getCurrentLineIndex(), 0, curLine.length());
        String sub = (curLine.substring(0, linePos));
        double subLength = FontRenderer.strWidth(sub);
        
        double hDiff = (subLength - taw);
        double vDiff = document.getCurrentLine() * FH;
        
        hScroll.setScrollPos(taw + hDiff + extraWidth);
        vScroll.setScrollPos(tah + vDiff - halfMaxPossiblePixels);
    }
    
    //=========
    // Drawing
    //=========
    
    protected void handleTextDrawing(float dt) {
        final double scrollOffset = vScroll.calcScrollOffset();
        final int firstVisibleLine = (int) (scrollOffset / FontRenderer.FONT_HEIGHT);
        final int lastVisibleLine = (int) (textAreaHeight / FontRenderer.FONT_HEIGHT) + firstVisibleLine + 2;
        
        // highlight current line
        drawCurrentLineBackground();
        
        // draw the highlighted portion of the document
        drawHighlightedText();
        
        // draw document lines
        drawDocumentText(firstVisibleLine, lastVisibleLine);
        
        // draw cursor
        if (hasFocus() || vScroll.hasFocus() || hScroll.hasFocus()) {
            drawCursor(dt);
        }
        
        // draw lineNumber backdrop and separator
        if (drawLineNumbers) drawLineNumbers(firstVisibleLine, lastVisibleLine);
    }
    
    private void drawCurrentLineBackground() {
        // only draw if not currently highlighting text
        if (document.getHighlightStartIndex() != document.getHighlightEndIndex()) return;
        
        double x = textAreaStartX - 1;
        double y = textAreaStartY + (FontRenderer.FH * document.getCurrentLine());
        
        y -= vScroll.getScrollPos() - vScroll.getVisibleAmount();
        
        drawRect(x, y, endX - 1, y + FontRenderer.FH - 1, currentLineHighlightColor);
    }
    
    private void drawHighlightedText() {
        // only draw if currently highlighting text
        if (document.getHighlightStartIndex() == document.getHighlightEndIndex()) return;
        
        int highlightStartLine = document.getHighlightStartLine();
        int highlightStartLineIndex = document.getHighlightStartLineIndex();
        int highlightEndLine = document.getHighlightEndLine();
        int highlightEndLineIndex = document.getHighlightEndLineIndex();
        
        // draw start highlight line
        double sx = textAreaStartX + (FontRenderer.getCharWidth() * highlightStartLineIndex) + textStartOffsetX;
        double ex = textAreaStartX + (FontRenderer.getCharWidth() * highlightEndLineIndex) + textStartOffsetX;
        
        double xOffset = hScroll.getScrollPos() - hScroll.getVisibleAmount();
        sx -= xOffset;
        ex -= xOffset;
        if (document.getHighlightStartLineIndex() == 0) sx = textAreaStartX;
        if (document.getHighlightEndLineIndex() == 0) ex = textAreaStartX;
        sx = ENumUtil.clamp(sx, textAreaStartX, textAreaEndX);
        ex = ENumUtil.clamp(ex, textAreaStartX, textAreaEndX);
        
        if (highlightStartLine == highlightEndLine) {
            double sy = textAreaStartY + (FontRenderer.FH * highlightStartLine);
            sy -= vScroll.getScrollPos() - vScroll.getVisibleAmount();
            drawRect(sx, sy, ex, sy + FontRenderer.FH, highlightBackgroundColor);
            return;
        }
        
        // highlight multiple lines
        final int lines = document.getNumberOfLines();
        for (int i = highlightStartLine; i < lines; i++) {
            double y = textAreaStartY + (FontRenderer.FH * i);
            y -= vScroll.getScrollPos() - vScroll.getVisibleAmount();
            
            if (i == highlightStartLine) {
                drawRect(sx, y, textAreaEndX, y + FontRenderer.FH, highlightBackgroundColor);
            }
            else if (i == highlightEndLine) {
                drawRect(textAreaStartX, y, ex, y + FontRenderer.FH, highlightBackgroundColor);
                break;
            }
            else {
                drawRect(textAreaStartX, y, textAreaEndX, y + FontRenderer.FH, highlightBackgroundColor);
            }
        }
    }
    
    /**
     * 
     * @param baseOffset
     * @return The current cursor line index
     */
    private void drawDocumentText(final int firstVisibleLine, final int lastVisibleLine) {
        internalLineBuffer.clear();
        // the current line number chars are being drawn to
        int lineNum = 0;
        int lineIndex = 0;
        double xPos, yPos;
        boolean inVision;
        boolean italic = false;
        boolean bold = false;
        boolean underline = false;
        boolean isHighlighted = false;
        char c;
        
        final int totalNumberOfLines = document.getNumberOfLines();
        final int docLength = document.length();
        final double CW = FontRenderer.getCharWidth();
        
        // color formatting
        final BoxList<Integer, Integer> colorLocations = document.colorChangeLocations;
        boolean hasColors = colorLocations.isNotEmpty();
        int curColorIndex = 0;
        int curColorDocIndex = 0;
        int curColor = fontColor;
        if (hasColors) {
            curColorDocIndex = colorLocations.getFirstA();
        }
        
        // format trackers
        final var italicTracker = new FormatTracker(document.italicisedLocations);
        final var boldTracker = new FormatTracker(document.boldLocations);
        final var underlineTracker = new FormatTracker(document.underlinedLocations);
        
        final int minVisibleLineIndex = (int) (hScroll.calcScrollOffset() / CW);
        final int maxVisibleLineIndex = (int) ((hScroll.calcScrollOffset() + textAreaWidth) / CW) + 1;
        
        // i = document index
        for (int i = 0; i < docLength; i++) {
            if (lineNum >= totalNumberOfLines || lineNum >= lastVisibleLine) break;
            
            inVision = lineNum >= firstVisibleLine;
            c = document.charAt(i);
            
            // color formatting
            if (hasColors && i == curColorDocIndex) {
                curColor = colorLocations.get(curColorIndex).getB();
                curColorIndex++;
                if (curColorIndex < colorLocations.size()) {
                    curColorDocIndex = colorLocations.get(curColorIndex).getA();
                }
            }
            
            // update format
            italic = italicTracker.update(i);
            bold = boldTracker.update(i);
            underline = underlineTracker.update(i);
            isHighlighted = (i >= document.getHighlightStartIndex() && i < document.getHighlightEndIndex());
            
            if (c != '\n') {
                xPos = textStartX + lineIndex++ * CW;
                yPos = textStartY + lineNum * FontRenderer.FONT_HEIGHT;
                inVision &= (lineIndex > minVisibleLineIndex && lineIndex <= maxVisibleLineIndex);
                if (!inVision) continue;
                int drawColor = (isHighlighted) ? highlightForegroundColor : curColor;
                EStringOutputFormatter.drawString(String.valueOf(c), xPos, yPos, drawColor, italic, bold, underline);
            }
            else {
                lineIndex = 0;
                lineNum++;
            }
        }
    }
    
    protected void drawCursor(float dt) {
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
            double sx = textStartX + (FontRenderer.getCharWidth() * document.getCurrentLineIndex());
            double sy = textAreaStartY + (FontRenderer.FONT_HEIGHT * document.getCurrentLine());
            
            double yOffset = vScroll.getScrollPos() - vScroll.getVisibleAmount();
            sy -= yOffset;
            
            drawRect(sx, sy, sx + 2, sy + FontRenderer.FONT_HEIGHT - 1, cursorColor);
        }
    }
    
    /** Draw this after everything to casually avoid dealing with scissoring issues. */
    private void drawLineNumbers(final int firstVisibleLine, final int lastVisibleLine) {
        // backdrop
        drawRect(lnStartX, lnStartY, lnEndX, lnEndY, lineNumbersAreaBackground);
        // separator
        drawRect(lnEndX, lnStartY, lnEndX + lnGap, lnEndY, lineNumbersSeparatorBackground);
        
        final int totalNumberOfLines = document.getNumberOfLines();
        
        // draw line numbers
        for (int i = 0, n = 1; i < totalNumberOfLines; i++, n++) {
            if (i < firstVisibleLine) continue;
            if (i >= totalNumberOfLines || i >= lastVisibleLine) break;
            
            double yPos = lnTextStartY + (FontRenderer.FONT_HEIGHT * i);
            
            int numLen = String.valueOf(i + 1).length();
            double xPos = lnTextStartX + (document.getLineNumberCharsLength() - numLen) * FontRenderer.getCharWidth();
            xPos += lnWidthPadding / 2 + FontRenderer.CW() / 2 - 3;
            
            // draw line num string
            drawString(n, xPos, yPos, lineNumbersColor);
        }
    }    
    //=========
    // Methods
    //=========
    
    public void startInputTimer() {
        timeSinceLastInput = System.currentTimeMillis();
        inputHappenedRecently = true;
    }
    
    /**
     * Returns true if the given mouse location is within the bounds of the
     * editable text area.
     * 
     * @param  mX X mouse position
     * @param  mY Y mouse position
     * 
     * @return    True if inside
     */
    public boolean isMouseInTextArea(int mX, int mY) {
        return mX >= textAreaStartX && mX <= textAreaEndX && mY >= textAreaStartY && mY <= textAreaEndY;
    }
    
    public void formatDocumentForEnvision() {
        document.colorChangeLocations.clear();
        document.boldLocations.clear();
        document.italicisedLocations.clear();
        document.underlinedLocations.clear();
        
        EList<Token<?>> tokens = null;
        
        String out = document.getInternalDocument().toString();
        for (int i = 0; i < out.length(); i++) {
            char c = out.charAt(i);
            System.out.println(i + ": " + ((c == '\n') ? "\\n" : c));
        }
        
        try {
            EnvisionTokenizer tokenizer = new EnvisionTokenizer();
            tokens = tokenizer.getTokens();
            tokenizer.tokenizeLine(document.getInternalDocument().toString());
        }
        catch (Exception e) {
            // do nothing
        }
        
        final int size = tokens.length();
        for (int i = 0; i < size; i++) {
            Token<?> t = tokens.get(i);
            int start = t.getCharacterIndex();
            int end = start + t.getLexeme().length();
            
            if (t.isReservedWord()) {
                ReservedWord w = t.asReservedWord();
                int color = EColors.borange.intVal;
                boolean bold = false;
                
                switch (w) {
                case NEWLINE:
                case EOF:
                    continue;
                case INT_LITERAL:
                case DOUBLE_LITERAL:
                    color = EColors.skyblue.intVal;
                    break;
                case STRING_LITERAL:
                case CHAR_LITERAL:
                    color = EColors.dgreen.intVal;
                    break;
                case IDENTIFIER:
                    color = 0xffffff88;
                    if ((i + 1) < size) {
                        Token testToken = tokens.get(i + 1);
                        if (!(testToken != null && testToken.getKeyword().isOperator() && testToken.asOperator() == Operator.PAREN_L)) {
                            break;
                        }
                        int j = i + 2;
                        int pStack = 1;
                        while (j < size) {
                            Token tt = tokens.get(j);
                            if (tt.isKeyword() && tt.getKeyword().isOperator()) {
                                Operator o = tt.asOperator();
                                if (o == Operator.PAREN_L) pStack++;
                                else if (o == Operator.PAREN_R) {
                                    pStack--;
                                    if (pStack == 0) {
                                        color = EColors.lime.intVal;
                                        break;
                                    }
                                }
                            }
                            j++;
                        }
                    }
                    break;
                default:
                    bold = true;
                    break;
                }
                if (w == ReservedWord.NEWLINE) continue;
                
                end = ENumUtil.clamp(end, 0, document.getInternalDocument().length());
//                if (end == document.getInternalDocument().length() - 1) {
//                    end += 1;
//                }
                
                document.setSectionColor(color, start, end);
                if (bold) document.setBold(start, end - 1);
            }
        }
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
    
    public void setTextAreaBackground(EColors color) { setTextAreaBackground(color.intVal); }
    public void setTextAreaBackground(int color) { textAreaBackground = color; }
    
    //==================
    // Internal Classes
    //==================
    
    private static class FormatTracker {
        BoxList<Integer, Integer> f;
        int n = -1, start = -1, end = -1;
        FormatTracker(BoxList<Integer, Integer> fIn) {
            f = fIn;
            if (f.isNotEmpty()) {
                n = 0;
                start = f.getA(n);
                end = f.getB(n);
            }
        }
        boolean update(int i) {
            boolean active = false;
            if (n >= 0) {
                active = (i >= start && i <= end);
                if (i == end) {
                    n++;
                    if (n < f.size()) {
                        start = f.getA(n);
                        end = f.getB(n);
                    }
                }
            }
            return active;
        }
    }
    
}
