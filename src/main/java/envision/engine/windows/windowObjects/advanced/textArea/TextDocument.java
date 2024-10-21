package envision.engine.windows.windowObjects.advanced.textArea;

import java.util.Collection;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.fontRenderer.FontRenderer;
import eutil.colors.EColors;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.strings.EStringBuilder;

public class TextDocument {
    
    //========
    // Fields
    //========
    
    /** '8750' == the 'contour integral' character (so one more than that) */
    public static final char ESCAPE = 8751;
    /** The character to represent 'BOLD' blocks. */
    public static final char BOLD = 'b';
    /** The character to represent 'ITALICISED' blocks. */
    public static final char ITALIC = 'i';
    /** The character to represent 'UNDERLINED' blocks. */
    public static final char UNDERLINE = 'u';
    
    //---------------------
    // configurable values
    //---------------------
    
    /** The amount of spaces that a tab represents. */
    public int tabSpaceAmount = 4;
    
    /** A flag that will automatically indent newly created lines to the previous line's indentation level. */
    public boolean maintainIndentationOnNewLines = true;
    /** A flag that will automatically indent new lines when they end on a '{'. */
    public boolean indentOnNewLinesAfterOpeningBrace = true;
    /** Automatically removes a layer of indentation if closing an already open scope brace with a '}'. */
    public boolean autoUnindentOnClosingBrace = true;
    /** Automatically places a '}' after creating a new line that ended with a '{'. */
    public boolean autoInsertClosingBraceOnNewLine = true;
    /** Automatically places a ']' after a '['. */
    public boolean autoInsertClosingBracket = true;
    /** Automatically places a '\'' after a '\''. */
    public boolean autoInsertClosingSingleQuotes = true;
    /** Automatically places a '"' after a '"'. */
    public boolean autoInsertClosingDoubleQuotes = true;
    /** Automatically places a ')' after a '('. */
    public boolean autoInsertClosingParenthesis = true;
    /** Automatically removes a '[' if it is immediately followed by a ']'. */
    public boolean autoRemoveOpeningBracket = true;
    /** Automatically removes a '\'' if it is immediately followed by a '\''. */
    public boolean autoRemoveOpeningSingleQuotes = true;
    /** Automatically removes a '"' if it is immediately followed by a '"'. */
    public boolean autoRemoveOpeningDoubleQuotes = true;
    /** Automatically removes a '(' if it is immediately followed by a ')'. */
    public boolean autoRemoveOpeningParenthesis = true;
    /** Don't doubly insert a closing ')' if within a '(' block already. */
    public boolean autoDetectFunctionParenthesis = true;
    
    //--------------------------
    // internal document values
    //--------------------------
    
    /** The current position of the cursor. */
    private int cursorPos = 0;
    /** The internal cursor position that factors in escape codes. */
    private int internalCursorPos = 0;
    /** The currently selected line. */
    private int currentLine = 0;
    /** The current index of the cursor in the current line. */
    private int currentLineIndex = 0;
    /** The internal current line index of the cursor that factors in escape codes. */
    private int internalCurrentLineIndex = 0;
    /** The index of where the current line starts. */
    private int currentLineStartIndex = 0;
    
    /** The number of line breaks present within the document. */
    private int numberOfLines = 1;
    /** Keeps track of the physical width of how many characters long the highest line number is. */
    private int lineNumberCharsLength = 0;
    /** The actual number of pixels that the line numbers take to draw, all text should be offset by this amount. */
    private int curLineNumberPixelWidth = 0;
    
    /** The index of where highlighting begins. */
    private int highlightStartPos = 0;
    /** The index of where highlighting ends. */
    private int highlightEndPos = 0;
    private int highlightStartLine = 0;
    private int highlightStartLineIndex = 0;
    private int highlightEndLine = 0;
    private int highlightEndLineIndex = 0;
    
    /** Indicates whether or not this document can be edited. */
    private boolean isEditable = true;
    
    private int lineChangeIndex = 0;
    private boolean moveToIndex = false;
    
    /** An internal flag used to detect whenever the cursor is within a function's parenthesis. */
    private boolean stillWithinFunctionParenthesis = false;
    
    /**
     * The char that was last appended/inserted into this text document by
     * the user (not the last char in general).
     */
    private char lastUserEnteredChar;
    
    /** A list of document indicies used to keep track of whenever the previous font color changes. */
    public BoxList<Integer, Integer> colorChangeLocations = BoxList.newList();
    /** A list of document indicies used to keep track of all sections of bolded text. (START, END) */
    public BoxList<Integer, Integer> boldLocations = BoxList.newList();
    /** A list of document indicies used to keep track of all sections of italicised text. (START, END) */
    public BoxList<Integer, Integer> italicisedLocations = BoxList.newList();
    /** A list of document indicies used to keep track of all sections of underlined text. (START, END) */
    public BoxList<Integer, Integer> underlinedLocations = BoxList.newList();
    
    /**
     * When ctrl+f searching for text, this keeps track of the positions of all
     * instances of that text snippet within the document.
     */
    private final EList<HighlightedPosition> highlightedPositions = EList.newList();
    
    /** A list of all objects that will get notified whenever the contents of this document change. */
    private final EList<DocumentChangeListener> listeners = EList.newList();
    
    /** The actual internal document string. */
    private EStringBuilder document;
    
    //==============
    // Constructors
    //==============
    
    public TextDocument() {
        document = new EStringBuilder();
    }
    
    public TextDocument(String valueIn) {
        document = new EStringBuilder(valueIn);
    }
    
    public TextDocument(EStringBuilder valueIn) {
        if (valueIn == null) document = new EStringBuilder();
        else document = valueIn;
    }
    
    public TextDocument(TextDocument documentIn) {
        cursorPos = documentIn.cursorPos;
        currentLine = documentIn.currentLine;
        currentLineIndex = documentIn.currentLineIndex;
        currentLineStartIndex = documentIn.currentLineStartIndex;
        
        numberOfLines = documentIn.numberOfLines;
        lineNumberCharsLength = documentIn.lineNumberCharsLength;
        curLineNumberPixelWidth = documentIn.curLineNumberPixelWidth;
        
        highlightStartPos = documentIn.highlightStartPos;
        highlightEndPos = documentIn.highlightEndPos;
        
        isEditable = documentIn.isEditable;
        lineChangeIndex = documentIn.lineChangeIndex;
        moveToIndex = documentIn.moveToIndex;
        
        if (documentIn.document == null) document = new EStringBuilder();
        else document = documentIn.document;
    }
    
    //==================
    // Internal Classes
    //==================
    
    /**
     * A position within this document that outlines starting/ending indices on
     * both a document and line basis.
     * <p>
     * Layout: [startIndex, endIndex, lineStart, lineStartIndex, lineEnd, lineEndIndex]
     * <p>
     * <li> startIndex = the index in the document where this highlight begins
     * <li> endIndex = the index in the document where this highlight ends
     * <li> lineStart = the line on which this highlight begins
     * <li> lineStartIndex = the index on the starting line where this highlight begins
     * <li> lineEnd = the line that this highlight ends
     * <li> lineEndIndex = the index on the ending line where this highlight ends
     */
    static record HighlightedPosition(int startIndex, int endIndex,
                                      int lineStart, int lineStartIndex,
                                      int lineEnd, int lineEndIndex)
    {
        @Override
        public String toString() {
            return "[(" + startIndex + ", " + endIndex + "), " +
                   "{" + lineStart + ":" + lineStartIndex + ", " + lineEnd + ":" + lineEndIndex + "}]";
        }
    }
    
    //=========
    // Methods
    //=========
    
    public void clearHighlight() {
        highlightStartPos = 0;
        highlightEndPos = 0;
    }
    
    public void deleteHighlightedText() {
        deleteSection(highlightStartPos, highlightEndPos);
        setCursorPos(highlightStartPos);
        clearHighlight();
    }
    
    public void replaceHighlightedText(String textToReplaceWith) {
        if (textToReplaceWith == null) return;
        deleteHighlightedText();
        pasteSection(textToReplaceWith, highlightStartPos);
        highlightEndPos = highlightStartPos + textToReplaceWith.length();
        updateHighlightedTextPositions();
    }
    
    public String getHighlightedText() {
        int end = ENumUtil.clamp(highlightEndPos, 0, length());
        int start = ENumUtil.clamp(highlightStartPos, 0, end);
        return document.sub(start, end);
    }
    
    public String getSection(int start, int end) {
        assertValidIndices(start, end);
        
        int s = start;
        int e = end;
        
        // reorder if necessary
        if (e < s) {
            int temp = s;
            s = e;
            e = temp;
        }
        
        return document.sub(s, e);
    }
    
    public void deleteSection(int start, int end) {
        if (!checkMultiplePositions(start, end)) return;
        
        int s = start;
        int e = end;
        
        // reorder if necessary
        if (e < s) {
            int temp = s;
            s = e;
            e = temp;
        }
        
        // if the highlighted portion was within the bounds that's
        // being deleted, trim the highlight's bounds around it
        if (highlightStartPos > s) {
            highlightStartPos = s;
        }
        if (highlightEndPos > e) {
            highlightEndPos -= (e - s);
        }
        
        String str = document.sub(s, e);
        document.delete(s, e);
        
        updateFormattingIndicies(s, s - e);
        
        updateLineNumberOffset();
        determineCurrentPosition();
        updateHighlightedTextPositions();
        
        listeners.forEach(l -> {
            l.onDocumentChanged();
            l.onStringDeleted(str, start);
        });
    }
    
    public void backspaceAtCursor() {
        if (cursorPos < 1) return;
        if (document.isEmpty()) return;
        
        if (length() > 1 && cursorPos < length()) {
            final char inFront = document.charAt(cursorPos);
            if (autoRemoveOpeningBracket && inFront == ']') document.deleteCharAt(cursorPos);
            else if (autoRemoveOpeningParenthesis && inFront == ')') {
                document.deleteCharAt(cursorPos);
                stillWithinFunctionParenthesis = false;
            }
            else if (autoRemoveOpeningSingleQuotes && inFront == '\'') document.deleteCharAt(cursorPos);
            else if (autoRemoveOpeningDoubleQuotes && inFront == '"') document.deleteCharAt(cursorPos);
        }
        
        int cPos = cursorPos - 1;
        char c = document.charAt(cPos);
        int amountDeleted = 1;
        
        if (highlightStartPos != highlightEndPos) {
            document.delete(highlightStartPos, highlightEndPos);
            if (cursorPos > highlightStartPos && cursorPos <= highlightEndPos) {
                amountDeleted = (cursorPos - highlightStartPos);                
            }
            clearHighlight();
        }
        else {
            document.deleteCharAt(cPos);
        }
        
        // adjust cursor position as well as all formatting code indicies
        updateFormattingIndicies(cursorPos, -amountDeleted);
        cursorPos -= amountDeleted;
        
        // check to see if we should still try to maintain function parenthesis rules
        if (autoDetectFunctionParenthesis && stillWithinFunctionParenthesis) {
            boolean foundStartingParen = false;
            for (int i = cursorPos; i >= currentLineStartIndex; i--) {
                final char cc = document.charAt(i);
                if (cc == '(') {
                    foundStartingParen = true;
                    break;
                }
            }
            stillWithinFunctionParenthesis = foundStartingParen;            
        }
        
        updateLineNumberOffset();
        determineCurrentPosition();
        updateHighlightedTextPositions();
        
        listeners.forEach(l -> {
            l.onDocumentChanged();
            l.onStringDeleted("" + c, cPos);
        });
    }
    
    public void deleteAtCursor() {
        if (document.isEmpty()) return;
        if (cursorPos >= length()) return;
        
        int cPos = cursorPos;
        char c = document.charAt(cursorPos);
        
        if (highlightStartPos != highlightEndPos) {
            document.delete(highlightStartPos, highlightEndPos);
            if (cursorPos > highlightStartPos && cursorPos <= highlightEndPos) {
                cursorPos -= (cursorPos - highlightStartPos);                
            }
            clearHighlight();
        }
        else {
            document.deleteCharAt(cPos);
        }
        
        updateLineNumberOffset();
        determineCurrentPosition();
        updateHighlightedTextPositions();
        updateFormattingIndicies(cursorPos + 1, -1);
        
        listeners.forEach(l -> {
            l.onStringDeleted("" + c, cPos);
            l.onDocumentChanged();
        });
    }
    
    public void newLineAtCursor() {
        boolean willIndent = false;
        int indentAmount = 0;
        
        // if indenting on '{' figure out what the line's ending char was
        if (indentOnNewLinesAfterOpeningBrace) {
            // check if the last character on the line was a '{'
            if (cursorPos != 0 && document.charAt(cursorPos - 1) == '{') {
                indentAmount += tabSpaceAmount;
                willIndent = true;
            }
        }
        
        // if maintaining indentation across new lines,
        // first find the current line's indent amount
        if (maintainIndentationOnNewLines) {
            // work from the start of the line
            final int len = length();
            for (int i = currentLineStartIndex + 1, n = 0; i < len; i++, n++) {
                char c = document.charAt(i);
                if (c != ' ') break;
                if (n >= currentLineIndex) continue;
                indentAmount++;
            }
        }
        
        // actually add the 'new line' character
        if (cursorPos == length()) document.a('\n');
        else document.insert(cursorPos, '\n');
        cursorPos++;
        
        // add any automaticly inserted intentation
        for (int i = 0; i < indentAmount; i++) {
            document.insert(cursorPos, ' ');
        }
        
        if (autoInsertClosingBraceOnNewLine && willIndent) {
            // check if the current scope is already closed -- don't want to add another
            int openScopes = 0;
            boolean alreadyClosed = false;
            // if we're at the end of the file already, then scope can't be already closed
            final int len = length();
            if ((cursorPos + 2) < len) {
                for (int i = cursorPos + 1 + indentAmount; i < len; i++) {
                    char c = document.charAt(i);
                    if (c == '{') openScopes++;
                    else if (c == '}') {
                        if (openScopes > 0) openScopes--;
                        else {
                            openScopes++;
                            break;
                        }
                    }
                }
                alreadyClosed = openScopes == 0;
            }
            // if the scope isn't already closed, close it automatically
            if (!alreadyClosed) {
                int currentIndent = 0;
                for (int i = cursorPos - 2; i >= 0; i--) {
                    char c = document.charAt(i);
                    if (c == '\n') break;
                    else if (c != ' ') currentIndent = 0;
                    else currentIndent++;
                }
                document.insert(cursorPos + indentAmount, '\n');
                for (int i = 0; i < currentIndent; i++) {
                    document.insert(cursorPos + indentAmount + 1, ' ');
                }
                document.insert(cursorPos + 1 + indentAmount + currentIndent, '}');
            }
        }
        
        cursorPos += indentAmount;
        currentLine++;
        currentLineIndex = indentAmount;
        updateLineNumberOffset();
        determineCurrentPosition();
        updateFormattingIndicies(cursorPos, 1 + indentAmount);
        stillWithinFunctionParenthesis = false;
    }
    
    public void append(String toAppend) {
        document.a(toAppend);
        updateLineNumberOffset();
        determineCurrentPosition();
    }
    
    public void addLine(String line) {
        document.a('\n', line);
        updateLineNumberOffset();
        determineCurrentPosition();
    }
    
    public void addLines(String... lines) {
        for (String l : lines) addLine(l);
    }
    
    public void addLines(Collection<String> lines) {
        for (String l : lines) addLine(l);
    }
    
    public void insertChar(char toInsert) {
        insertChar(toInsert, cursorPos);
    }
    
    public void insertChar(char toInsert, final int position) {
        int pos = clampCursorPos(position);
        int amountAdded = 1;
        
        // if there is a highlighted section, delete the highlighted section
        if (highlightStartPos != highlightEndPos) {
            document.delete(highlightStartPos, highlightEndPos);
            pos = cursorPos = highlightStartPos;
            currentLineIndex = highlightStartLineIndex;
            currentLine = highlightStartLine;
            clearHighlight();
        }
        
        // if we insert certain chars automatically, don't double up the same char if the user manually inserts it
        boolean add = true;
        if (autoInsertClosingParenthesis && lastUserEnteredChar == '(' && toInsert == ')') add = false;
        else if (autoInsertClosingBracket && lastUserEnteredChar == '[' && toInsert == ']') add = false;
        else if (autoInsertClosingSingleQuotes && lastUserEnteredChar == '\'' && toInsert == '\'') add = false;
        else if (autoInsertClosingDoubleQuotes && lastUserEnteredChar == '"' && toInsert == '"') add = false;
        // don't doubly add an extra ')' to close function parenthesis if we already added one
        if (autoInsertClosingParenthesis && autoDetectFunctionParenthesis && stillWithinFunctionParenthesis && toInsert == ')') {
            add = false;
            stillWithinFunctionParenthesis = false;
        }
        
        if (add) {
            if (pos == length()) {
                document.append(toInsert);
                // auto insert logic
                if (autoInsertClosingParenthesis && toInsert == '(') {
                    document.a(')');
                    stillWithinFunctionParenthesis = true;
                    amountAdded += 1;
                }
                else if (autoInsertClosingBracket && toInsert == '[') { document.a(']'); amountAdded += 1; }
                else if (autoInsertClosingSingleQuotes && toInsert == '\'') { document.a('\''); amountAdded += 1; }
                else if (autoInsertClosingDoubleQuotes && toInsert == '"') { document.a('"'); amountAdded += 1; }
            }
            else {
                document.insert(pos, toInsert);
                // auto insert logic
                if (autoInsertClosingParenthesis && toInsert == '(') {
                    document.insert(pos + 1, ')');
                    stillWithinFunctionParenthesis = true;
                    amountAdded += 1;
                }
                else if (autoInsertClosingBracket && toInsert == '[') { document.insert(pos + 1, ']'); amountAdded += 1; }
                else if (autoInsertClosingSingleQuotes && toInsert == '\'') { document.insert(pos, '\''); amountAdded += 1; }
                else if (autoInsertClosingDoubleQuotes && toInsert == '"') { document.insert(pos, '"'); amountAdded += 1; }
            }
        }
        
        if (autoUnindentOnClosingBrace && toInsert == '}') {
            unindentScopeBlock();
        }
        
        updateFormattingIndicies(cursorPos, amountAdded);
        
        cursorPos++;
        currentLineIndex++;
        moveToIndex = false;
        updateLineNumberOffset();
        updateHighlightedTextPositions();
        
        listeners.forEach(l -> {
            l.onStringInserted("" + toInsert, cursorPos);
            l.onDocumentChanged();
        });
        
        lastUserEnteredChar = toInsert;
    }
    
    /**
     * Finds a function scope's starting indent amount and unindents to the
     * same amount.
     */
    private void unindentScopeBlock() {
        int startingIndent = 0;
        boolean foundStartingBrace = false;
        int nestedScopes = 0;
        for (int i = cursorPos - 1; i >= 0; i--) {
            char c = document.charAt(i);
            if (c == '}') nestedScopes++;
            else if (c == '{') {
                if (nestedScopes > 0) nestedScopes--;
                else {
                    foundStartingBrace = true;
                    for (int x = i; x >= 0; x--) {
                        c = document.charAt(x);
                        if (c == '\n') break;
                        else if (c == ' ') startingIndent++;
                        else startingIndent = 0;
                    }
                    break;
                }
            }
        }
        if (foundStartingBrace) {
            int currentIndent = 0;
            boolean allBeforeAreSpaces = true;
            for (int i = cursorPos - 1; i >= 0; i--) {
                char c = document.charAt(i);
                if (c == '\n') break;
                if (c != ' ') {
                    allBeforeAreSpaces = false;
                    break;
                }
                currentIndent++;
            }
            if (allBeforeAreSpaces) {
                int start = (cursorPos - 1 - (currentIndent - startingIndent)) + 1;
                int end = cursorPos - 1 + 1;
                document.delete(start, end);
                cursorPos -= (currentIndent - startingIndent);
                currentLineIndex -= (currentIndent - startingIndent);
            }
        }
    }
    
    public void insertString(String toInsert) {
        insertString(toInsert, cursorPos);
    }
    
    public void insertString(String toInsert, int position) {
        if (toInsert == null || toInsert.isEmpty()) return;
        
        if (position == length()) {
            document.append(toInsert);
        }
        else {
            assertValidIndex(position);
            document.insert(position, toInsert);
        }
        cursorPos += toInsert.length();
        currentLineIndex += toInsert.length();
        moveToIndex = false;
        updateLineNumberOffset();
        determineCurrentPosition();
        updateHighlightedTextPositions();
        
        listeners.forEach(l -> {
            l.onStringInserted(toInsert, position);
            l.onDocumentChanged();
        });
    }
    
    public void pasteSection(String toPaste, int position) {
        if (toPaste == null || toPaste.isEmpty()) return;
        
        assertValidIndex(position);
        insertString(toPaste, position);
        
        listeners.forEach(l -> {
            l.onStringInserted(toPaste, position);
            l.onDocumentChanged();
        });
    }
    
    public void copySection(int start, int end) {
        assertValidIndices(start, end);
        final String section = getSection(start, end);
        Keyboard.setClipboard(section);
    }
    
    public void advanceCursor() { advanceCursor(1); }
    public void advanceCursor(int amount) {
        final int amountToAdvanceBy = ENumUtil.clamp(cursorPos + amount, 0, length());
        
        moveToIndex = false;
        
        if (highlightStartPos != highlightEndPos) {
            if (amount < 0) cursorPos = highlightStartPos;
            else cursorPos = highlightEndPos;
        }
        else {
            cursorPos = amountToAdvanceBy;
        }
        
        clearHighlight();
        determineCurrentPosition();
    }
    
    public void selectPreviousLine() {
        if (currentLine == 0) return;
        currentLine -= 1;
        int lineIndex = lineChangeIndex;
        if (!moveToIndex) {
            lineIndex = lineChangeIndex = currentLineIndex;
            moveToIndex = true;
        }
        clearHighlight();
        determineCurrentLineIndex();
        moveCursorToLineIndex(lineIndex);
    }
    
    public void selectNextLine() {
        if (currentLine == numberOfLines - 1) return;
        currentLine += 1;
        int lineIndex = lineChangeIndex;
        if (!moveToIndex) {
            lineIndex = lineChangeIndex = currentLineIndex;
            moveToIndex = true;
        }
        clearHighlight();
        determineCurrentLineIndex();
        moveCursorToLineIndex(lineIndex);
    }
    
    public void moveCursorToStartOfLine() {
        cursorPos = currentLineStartIndex + 1;
        currentLineIndex = 0;
    }
    
    public void moveCursorToEndOfLine() {
        if (numberOfLines == 0) return;
        else if (numberOfLines == 1) cursorPos = length();
        else if (cursorPos == length()) return;
        
        int index = currentLineIndex;
        final int len = length();
        for (int i = cursorPos; i < len; i++) {
            final char c = document.charAt(i);
            if (c == '\n') {
                cursorPos = i;
                currentLineIndex = index;
                return;
            }
            index++;
        }
        
        cursorPos = length();
        currentLineIndex = index;
    }
    
    public void moveCursorToLineIndex(int indexIn) {
        if (numberOfLines == 0) return;
        else if (numberOfLines == 1) cursorPos = length();
        else if (cursorPos == length()) return;
        
        int index = currentLineIndex;
        final int len = length();
        
        for (int i = cursorPos; i < len; i++) {
            final char c = document.charAt(i);
            if (index == indexIn || c == '\n') {
                cursorPos = i;
                currentLineIndex = index;
                return;
            }
            index++;
        }
        
        cursorPos = len;
        currentLineIndex = index;
    }
    
    /**
     * Gets the index (starting from index 0) in the document string of where
     * the given line begins.
     * 
     * @param lineNum The number of the line to find where index 0 is the first line
     * 
     * @return The document index of where the line with given line number starts
     */
    protected int getDocumentIndexOfLineStart(int lineNum) {
        if (lineNum < 0 || lineNum > numberOfLines) return -1;
        
        // first line should always return index 0
        if (lineNum == 0) return 0;
        
        final int len = length();
        int lineStartIndex = 0;
        int curLine = 0;
        boolean wasNewLine = false;
        
        for (int i = 0; i < len; i++) {
            if (curLine == lineNum) break;
            
            final char c = document.charAt(i);
            if (wasNewLine || (i + 1 == len)) {
                lineStartIndex = i;
                curLine++;
            }
            wasNewLine = (c == '\n');
            if (wasNewLine && i + 1 == len) {
                lineStartIndex = i + 1;
            }
        }
        
        return lineStartIndex;
    }
    
    protected int getDocumentIndexOfLineEnd(int lineNum) {
        if (lineNum < 0 || lineNum > numberOfLines) return -1;
        
        int lineStartIndex = getDocumentIndexOfLineStart(lineNum);
        
        final int len = length();
        int index = lineStartIndex;
        
        for (int i = lineStartIndex; i < len; i++) {
            final char c = document.charAt(i);
            
            if (c == '\n') break;
            index++;
        }
        
        return index;
    }
    
    protected String getLine(int lineNum) {
        if (lineNum < 0 || lineNum > numberOfLines) return null;
        
        final int len = length();
        int start = getDocumentIndexOfLineStart(lineNum);        
        int end = start;
        
        for (int i = start; i < len; i++) {
            final char c = document.charAt(i);
            
            if (c == '\n') break;
            end++;
        }
        
        return document.substring(start, end);
    }
    
    public void setBold(int start, int end) {
        assertValidIndices(start, end);
        boldLocations.put(start, end);
        determineCurrentPosition();
    }
    
    public void setItalicised(int start, int end) {
        assertValidIndices(start, end);
        italicisedLocations.put(start, end);
        determineCurrentPosition();
    }
    
    public void setUnderlined(int start, int end) {
        assertValidIndices(start, end);
        underlinedLocations.put(start, end);
        determineCurrentPosition();
    }
    
    public void setSectionColor(EColors color, int start) { setSectionColor(color.intVal, start, -1); }
    public void setSectionColor(EColors color, int start, int end) { setSectionColor(color.intVal, start, end); }
    public void setSectionColor(int color, int start) { setSectionColor(color, start, -1); }
    public void setSectionColor(int color, int start, int end) {
        assertValidIndex(start);
        // override existing value or add new
        colorChangeLocations.put(start, color);
        if (end >= 0) {
            colorChangeLocations.put(end, EColors.white.intVal);
        }
//        if (end >= 0) {
//            EColors previousColor = EColors.white;
//            for (int i = 0; i < colorChangeLocations.size(); i++) {
//                var loc = colorChangeLocations.get(i);
//                int index = loc.getA();
//                if (index < start) previousColor = loc.getB();
//                else break;
//            }
//            colorChangeLocations.put(end, previousColor);
//        }
        determineCurrentPosition();
    }
    
    //===========
    // Listeners
    //===========
    
    public void registerListener(DocumentChangeListener listenerIn) {
        listeners.addIfNotNull(listenerIn);
    }
    
    public void unregisterListener(DocumentChangeListener listenerIn) {
        if (listenerIn == null) return;
        listeners.remove(listenerIn);
    }
    
    public void clearListeners() {
        listeners.clear();
    }
    
    //=========================
    // Cursor Position Helpers
    //=========================
    
    public boolean isCursorAtStartOfLine() {
        return cursorPos == currentLineIndex;
    }
    
    public boolean isCursorAtEndOfLine() {
        if (document.isEmpty()) return true;
        if (cursorPos == length()) return true;
        final char currentChar = document.charAt(cursorPos);
        return (cursorPos + 1 == length() || currentChar == '\n');
    }
    
    public boolean isCursorAtStartOfDocument() {
        return cursorPos == 0;
    }
    
    public boolean isCursorAtEndOfDocument() {
        return cursorPos == length();
    }
    
    public boolean isCurrentLineEmpty() {
        if (document.isEmpty()) return true;
        else if (document.length() == 1 && Character.isWhitespace(document.charAt(0))) return true;
        else if (cursorPos == 0) {
            final char currentChar = document.charAt(cursorPos);
            return currentChar == '\n';
        }
        else if (cursorPos == length()) {
            final char previousChar = document.charAt(cursorPos - 1);
            return previousChar == '\n';
        }
        
        final char previousChar = document.charAt(cursorPos - 1);
        final char currentChar = document.charAt(cursorPos);
        
        return previousChar == '\n' && currentChar == '\n';
    }
    
    public String getLongestLine() {
        final int len = length();
        
        int lineStart = 0;
        
        int longestStartIndex = 0;
        int longestEndIndex = 0;
        
        for (int i = 0; i < len; i++) {
            final char c = document.charAt(i);
            
            if (c == '\n' || (i + 1) == len) {
                // check if longer
                if (i - lineStart > (longestEndIndex - longestStartIndex)) {
                    longestStartIndex = lineStart;
                    longestEndIndex = i;
                }
                
                lineStart = i + 1;
            }
        }
        
        return document.sub(longestStartIndex, longestEndIndex);
    }
    
    /**
     * Searches this document for all occurrences of the given text. The result
     * will be returned in the form of a list of positions which include
     * various indices on each occurrences location within the document.
     * 
     * @see HighlightedPosition
     * 
     * @param textToSearchFor
     * 
     * @return
     */
    public EList<HighlightedPosition> searchForText(String textToSearchFor) {
        highlightedPositions.clear();
        
        // don't bother searching for non-existent or empty matches -- there aren't any
        if (textToSearchFor == null || textToSearchFor.isEmpty()) {
            return highlightedPositions;
        }
        
        // the document can't possibly contain text that is longer than itself
        if (textToSearchFor.length() > document.length()) {
            return highlightedPositions;
        }
        
        // used to store potential string matches while iterating the document
        var buffer = new EStringBuilder();
        // the start index of a potential match
        int start = 0;
        // the current index into the 'textToSearchFor' string we've matched
        int textIndex = 0;
        
        int line = 0;
        int lineIndex = 0;
        int lineStart = 0;
        int lineStartIndex = 0;
        boolean wasNewLine = false;
        
        final int len = length();
        for (int i = 0; i < len; i++) {
            final char c = document.charAt(i);
            
            if (buffer.isEmpty()) {
                // nest to short-ciruit logic
                if (textToSearchFor.startsWith("" + c)) {
                    start = i;
                    lineStart = line;
                    lineStartIndex = lineIndex;
                    buffer.a(c);
                    textIndex = 1;
                }
            }
            else {
                // nest to short-ciruit logic
                if (buffer.toString().equals(textToSearchFor)) {
                    var hp = new HighlightedPosition(start, i, lineStart, lineStartIndex, line, lineIndex);
                    highlightedPositions.add(hp);
                    buffer.clear();
                    textIndex = 0;
                    start = i;
                }
                
                if (c == textToSearchFor.charAt(textIndex)) {
                    buffer.a(c);
                    textIndex = ENumUtil.clamp(textIndex + 1, 0, textToSearchFor.length() - 1);
                }
                // for the case in when a match spans across multiple lines
                else if (c == '\n' && textToSearchFor.charAt(textIndex) == ' ') {
                    // do nothing
                }
                else {
                    buffer.clear();
                    textIndex = 0;
                }
            }
            
            // line indexing logic
            if (c == '\n') { line++; wasNewLine = true; }
            if (wasNewLine) { wasNewLine = false; lineIndex = 0; }
            else { lineIndex++; }
        }
        
        // check for a potential match that is still in the buffer
        if (buffer.toString().equals(textToSearchFor)) {
            var hp = new HighlightedPosition(start, len, lineStart, lineStartIndex, line, lineIndex);
            highlightedPositions.add(hp);
        }
        
        System.out.println(highlightedPositions);
        
        return highlightedPositions;
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    protected void assertValidIndices(int start, int end) {
        if (!checkPosition(start)) invalidIndex(start);
        if (!checkPosition(end)) invalidIndex(end);
    }
    
    protected void assertValidIndex(int index) {
        if (!checkPosition(index)) invalidIndex(index);
    }
    
    protected boolean checkPosition(int posIn) {
        return posIn >= 0 && posIn < document.length();
    }
    
    protected boolean checkMultiplePositions(int... positions) {
        for (int p : positions) {
            if (!checkPosition(p)) return false;
        }
        return true;
    }
    
    protected int clampCursorPos(int posIn) {
        return ENumUtil.clamp(posIn, 0, document.length());
    }
    
    protected int clampLineIndex(int lineIn) {
        return ENumUtil.clamp(lineIn, 0, numberOfLines);
    }
    
    protected void invalidIndex(int index) {
        throw new IndexOutOfBoundsException("The index '" + index
                                            + "' is outside of the bounds of this document! "
                                            + document.length());
    }
    
    /**
     * Calculates the current line and index into that line based off of the
     * current cursor position.
     */
    protected void determineCurrentPosition() {
        final int len = length();
        
        int line = 0;
        int lineIndex = 0;
        int lineStartIndex = 0;
        boolean wasNewLine = false;
        
        // count the number of '\n' that are found within the document up to the cursor pos
        // (IS THERE A BETTER WAY TO DO THIS???)
        for (int i = 0; i < len; i++) {
            final char c = document.charAt(i);
            if (i == cursorPos) break;
            if (c == '\n') {
                line++;
                wasNewLine = true;
                lineStartIndex = i;
            }
            
            if (wasNewLine) {
                wasNewLine = false;
                lineIndex = 0;
            }
            else {
                lineIndex++;
            }
        }
        
        currentLine = line;
        currentLineIndex = lineIndex;
        currentLineStartIndex = lineStartIndex;
    }
    
    /**
     * Calculates the line index and cursor position based off of the current
     * line number and current line index.
     */
    protected void determineCurrentLineIndex() {
        final int len = length();
        
        int line = 0;
        int lineIndex = 0;
        int lineStartIndex = 0;
        int currentPos = 0;
        
        for (int i = 0; i < len; i++) {
            final char c = document.charAt(i);
            if (line == currentLine) {
                currentPos = i;
                // if the current line is shorter than the current line index, break out
                if (c == '\n') break;
                if (lineIndex == currentLineIndex) break;
                lineIndex++;
            }
            else if (c == '\n') {
                currentPos = i + 1;
                line++;
                lineStartIndex = i;
            }
        }
        
        cursorPos = currentPos;
        currentLineIndex = lineIndex;
        currentLineStartIndex = lineStartIndex;
    }
    
    protected void updateLineNumberOffset() {
        int lines = 1;
        
        final int len = length();
        for (int i = 0; i < len; i++) {
            final char c = document.charAt(i);
            if (c == '\n') {
                lines++;
            }
        }
        
        numberOfLines = lines;
        lineNumberCharsLength = ENumUtil.clamp(String.valueOf(numberOfLines).length(), 1, Integer.MAX_VALUE);
        curLineNumberPixelWidth = FontRenderer.getCharWidth() * lineNumberCharsLength;
    }
    
    protected void updateHighlightedTextPositions() {
        // if the indices are the same, then there is no highlight
        if (highlightStartPos == highlightEndPos) return;
        
        int lineIndex = 0;
        int lineNum = 0;
        boolean wasNewLine = false;
        
        final int len = length();
        for (int i = 0; i < len; i++) {
            final char c = document.charAt(i);
            
            //System.out.println(highlightEndPos + " : " + (len - 1) + " : " + (i + 1));
            
            if (i == highlightStartPos) {
                highlightStartLine = lineNum;
                highlightStartLineIndex = lineIndex;
            }
            else if (highlightEndPos == len && (i + 1) == highlightEndPos) {
                highlightEndLine = lineNum;
                highlightEndLineIndex = lineIndex + 1;
            }
            else if (i == highlightEndPos) {
                highlightEndLine = lineNum;
                highlightEndLineIndex = lineIndex;
                break;
            }
            
            if (c == '\n') {
                lineNum++;
                wasNewLine = true;
            }
            
            if (wasNewLine) {
                wasNewLine = false;
                lineIndex = 0;
            }
            else {
                lineIndex++;
            }
        }
    }
    
    protected void updateFormattingIndicies(int changeIndex, int offset) {
//        System.out.println(changeIndex + " : " + offset);
        var it = colorChangeLocations.iterator();
        while (it.hasNext()) {
            var l = it.next();
            if (l.getA() < changeIndex) {
                continue;
            }
            else if (l.getA() == changeIndex && offset < 0) {
                it.remove();
            }
            else if (l.getA() == changeIndex && !it.hasNext()) {
                continue;
            }
            else {
                l.setA(l.getA() + offset);
            }
        }
//        System.out.println("ITAL");
        shiftFormatIndices(italicisedLocations, changeIndex, offset);
//        System.out.println("BOLD");
        shiftFormatIndices(boldLocations, changeIndex, offset);
//        System.out.println("UNDER");
        shiftFormatIndices(underlinedLocations, changeIndex, offset);
    }
    
    private void shiftFormatIndices(BoxList<Integer, Integer> list, int changeIndex, int offset) {
        var it = list.iterator();
        while (it.hasNext()) {
            var l = it.next();
            if (changeIndex == l.getB()) {
                if (offset > 0 && it.hasNext()) {
                    l.setB(l.getB() + offset);
                }
                else if (offset < 0) {
                    l.setB(l.getB() + offset);
                }
                if ((changeIndex + offset) == l.getA() && l.getA() == l.getB()) {
                    it.remove();
                }
            }
            else {
                if (changeIndex <= l.getA()) {
                    l.setA(l.getA() + offset);
                } 
                if (changeIndex <= l.getB()) {
                    l.setB(l.getB() + offset);
                }
                if ((changeIndex + offset) == l.getA() && l.getA() == l.getB()) {
                    it.remove();
                }
            }
        }
    }
    
    protected void formatDocument() {
        
    }
    
    //=========
    // Getters
    //=========
    
    public char charAt(int index) { return document.charAt(index); }
    
    boolean isEditable() { return isEditable; }
    
    /** Returns the length of the text in this document. */
    public int length() { return document.length(); }
    
    public int getCurrentLine() { return currentLine; }
    public int getCursorPos() { return cursorPos; }
    public int getCurrentLineIndex() { return currentLineIndex; }
    
    public int getHighlightStartIndex() { return highlightStartPos; }
    public int getHighlightEndIndex() { return highlightEndPos; }
    
    public int getHighlightStartLine() { return highlightStartLine; }
    public int getHighlightStartLineIndex() { return highlightStartLineIndex; }
    public int getHighlightEndLine() { return highlightEndLine; }
    public int getHighlightEndLineIndex() { return highlightEndLineIndex; }
    
    public int getNumberOfLines() { return numberOfLines; }
    
    public EStringBuilder getInternalDocument() { return document; }
    public EList<String> getDocumentLines() { return EList.of(document.toString().split("\n", 0)); }
    
    int getCurrentLineNumberPixelWidth() { return curLineNumberPixelWidth; }
    int getLineNumberCharsLength() { return lineNumberCharsLength; }
    
    //=========
    // Setters
    //=========
    
    public void setEditable(boolean val) { isEditable = val; }
    
    public void setHighlightedArea(int start, int end) {
        int s = ENumUtil.clamp(start, 0, length());
        int e = ENumUtil.clamp(end, 0, length());
        
        this.highlightStartPos = s;
        this.highlightEndPos = e;
        this.cursorPos = e;
        updateHighlightedTextPositions();
        determineCurrentPosition();
    }
    
    public void setHighlightedArea(int lineStart, int lineStartIndex, int lineEnd, int lineEndIndex) {
        int line_s = ENumUtil.clamp(lineStart, 0, numberOfLines - 1);
        int lineIndex_s = ENumUtil.clamp(lineStartIndex, 0, lineStartIndex);
        int line_e = ENumUtil.clamp(lineEnd, 0, numberOfLines - 1);
        int lineIndex_e = ENumUtil.clamp(lineEndIndex, 0, lineEndIndex);
        
        // if end line is less than start line, flip values to make math easier
        if (line_e < line_s) {
            int tempLine = line_s;
            int tempLineIndex = lineIndex_s;
            line_s = line_e;
            line_e = tempLine;
            lineIndex_s = lineIndex_e;
            lineIndex_e = tempLineIndex;
        }
        // if on the same line, but the end index is less than the start index, flip values
        else if (line_e == line_s && lineIndex_s > lineIndex_e) {
            int tempLineIndex = lineIndex_s;
            lineIndex_s = lineIndex_e;
            lineIndex_e = tempLineIndex;
        }
        
        
        
        int lineIndex = 0;
        int lineNum = 0;
        boolean wasNewLine = false;
        boolean foundStart = false;
        
        final int len = length();
        for (int i = 0; i < len; i++) {
            final char c = document.charAt(i);
            
            // check if the line index matches or if the end of the line is reached
            if (lineNum == line_s && !foundStart && (lineIndex == lineIndex_s || c == '\n')) {
                highlightStartPos = i;
                foundStart = true;
            }
            
            // check if the line index matches or if the end of the line is reached
            if (lineNum == line_e && (lineIndex == lineIndex_e || c == '\n')) {
                highlightEndPos = i;
                break;
            }
            
            // if on the last line and the last line is part of the highlight
            if (lineNum >= (numberOfLines - 1) && lineNum == line_e && lineIndex_e > lineIndex && (i + 1) == len) {
                if (!foundStart) highlightStartPos = len;
                highlightEndPos = len;
                break;
            }
            
            if (c == '\n') {
                lineNum++;
                wasNewLine = true;
            }
            
            if (wasNewLine) {
                wasNewLine = false;
                lineIndex = 0;
            }
            else {
                lineIndex++;
            }
        }
        
        updateHighlightedTextPositions();
    }
    
    /**
     * Sets the current line to the given line number and the current line
     * index to the line's start.
     * 
     * @param lineNum The line to set to
     */
    public void setCurrentLine(int lineNum) {
        lineNum = ENumUtil.clamp(lineNum, 0, numberOfLines - 1);
        currentLine = lineNum;
        currentLineIndex = 0;
        cursorPos = getDocumentIndexOfLineStart(currentLine);
    }
    
    public void setCurrentLineAndIndex(int lineNum, int lineIndex) {
        lineNum = ENumUtil.clamp(lineNum, 0, numberOfLines - 1);
        lineIndex = ENumUtil.clamp(lineIndex, 0, lineIndex);
        currentLine = lineNum;
        
        int start = getDocumentIndexOfLineStart(lineNum);
        cursorPos = start;
        
        String line = getLine(lineNum);
        final int lineLength = line.length();
        
        // if the given index is bigger than the actual length of the line:
        // -- clamp it at the line's end
        if (lineIndex >= lineLength) {
            currentLineIndex = lineLength;
            cursorPos = start + lineLength;
        }
        else {
            currentLineIndex = lineIndex;
            cursorPos = start + lineIndex;
        }
    }
    
    public void setCursorPos(int posIn) {
        assertValidIndex(posIn);
        cursorPos = posIn;
        determineCurrentPosition();
    }
    
    public void setDocumentText(String text) {
        document.clear(text);
        updateLineNumberOffset();
    }
    
    public void setDocumentObject(EStringBuilder documentIn) {
        if (documentIn == null) document = new EStringBuilder();
        else document = documentIn;
        updateLineNumberOffset();
    }
    
    public void setDocumentLines(Collection<String> lines) {
        document.clear();
        for (String l : lines) document.println(l);
        updateLineNumberOffset();
    }
    
}
