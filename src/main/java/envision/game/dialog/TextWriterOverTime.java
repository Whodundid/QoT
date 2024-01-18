package envision.game.dialog;

import eutil.math.ENumUtil;
import eutil.strings.EStringUtil;

public class TextWriterOverTime {
    
    //========
    // Fields
    //========
    
    private float curTime;
    private long timeToWrite;
    private String theStringToWrite;
    private String previouslyGenerated;
    private boolean done = false;
    private String lastChar;
    
    // double integral symbol
    public static final char PAUSE_CHAR = '\u222c';
    
    //==============
    // Constructors
    //==============
    
    public TextWriterOverTime(String theString, long writeTime) {
        theStringToWrite = theString;
        timeToWrite = writeTime;
        previouslyGenerated = "";
    }
    
    //=========
    // Methods
    //=========
    
    /**
     * Incrementally adds to the percentage of string visible
     * over time until done.
     * 
     * @param dt The change in time
     */
    public void update(float dt) {
        if (done) return;
        curTime += dt;
        done = curTime > timeToWrite;
    }
    
    /**
     * Resets this text writer back to a time of 0.0.
     */
    public void reset() {
        curTime = 0.0f;
        done = false;
    }
    
    //=========
    // Getters
    //=========
    
    /**
     * Returns this writer's string as a substring from 0% to 100% of the full
     * string's length based on the time spent writing.
     * 
     * @return This writer's string as a percentage over time
     */
    public String getStringOverTime() {
        double percentage = ENumUtil.clamp(curTime / timeToWrite, 0.0, 1.0);
        return getStringAtPercent(theStringToWrite, percentage);
    }
    
    public String getNextCharOverTime() {
        String c = "";
        if (!previouslyGenerated.equals(theStringToWrite)) {
            double percentage = ENumUtil.clamp(curTime / timeToWrite, 0.0, 1.0);
            c = getNextChar(theStringToWrite, previouslyGenerated, percentage);
            if (c != "") {
                previouslyGenerated += c;
                c = c.replace("" + PAUSE_CHAR, "");
                lastChar = c;
            }
        }
        return c;
    }
    
    public String getLastChar() {
        return lastChar;
    }
    
    /**
     * @return the full string of this writer regardless of time percentage.
     */
    public String getFullString() {
        return theStringToWrite;
    }
    
    public long getWriteTime() {
        return timeToWrite;
    }
    
    public boolean isDone() {
        return done;
    }
    
    //=========
    // Setters
    //=========
    
    public void setString(String stringIn) {
        theStringToWrite = stringIn;
        previouslyGenerated = "";
    }
    
    public void setWriteTime(long timeIn) {
        timeToWrite = timeIn;
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    /**
     * Returns the string of
     * @param percent
     * @return
     */
    public static String getStringAtPercent(String toWrite, double percent) {
        if (toWrite == null) return null;
        // if less than 0.0 percent, there's nothing to write
        if (percent < 0.0) return "";
        // wrap between 0.0 and 1.0
        if (percent > 1.0) percent = 1 - Math.pow(2, percent);
        
        double fullLen = toWrite.length();
        double percentLen = (int) (fullLen * percent);
        percentLen = Math.round(percentLen * 4.0) / 4.0;
        int len = (int) percentLen;
        
        String s = toWrite.substring(0, len);
        return s.replace("" + PAUSE_CHAR, "");
    }
    
    /**
     * Returns a string with the next character of the given string based on
     * percentage as its output.
     * <p>
     * This is returned as a string instead of a char so that empty values can
     * be returned at 0% values.
     * 
     * @param toWrite The string to write
     * @param percent The percent of the string to grab
     * 
     * @return A string containing the next char of the given 'toWrite' string
     *         based on the given 'percent'
     */
    public static String getNextChar(String toWrite, String previous, double percent) {
        if (toWrite == null) return null;
        // if less than 0.0 percent, there's nothing to write
        if (percent < 0.0) return "";
        // wrap between 0.0 and 1.0
        if (percent > 1.0) percent = 1 - Math.pow(2, percent);
        
        double fullLen = toWrite.length();
        double percentLen = (int) (fullLen * percent);
        percentLen = Math.round(percentLen * 4.0) / 4.0;
        int len = (int) percentLen;
        
        if (previous.length() >= len) return "";
        
        return toWrite.substring(previous.length(), len);
    }
    
    public static String addPause(int amount) {
        return EStringUtil.repeatString("" + PAUSE_CHAR, amount);
    }
    
}
