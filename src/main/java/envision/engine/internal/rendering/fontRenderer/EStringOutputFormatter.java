package envision.engine.internal.rendering.fontRenderer;

import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;

/** Used to concatenate 'EColors' color codes within drawn Strings. */
public class EStringOutputFormatter {
    
    /** The color of the shadow in the range of [0, 255]. */
    public static int shadowColor = 60;
    
    public static double drawString(String s, double x, double y, EColors color) {
        return drawString(s, x, y, 1.0, 1.0, color.intVal, false, false, false, false, false); }
    public static double drawString(String s, double x, double y, int color) {
        return drawString(s, x, y, 1.0, 1.0, color, false, false, false, false, false);
    }
    
    public static double drawString(String s, double x, double y, EColors color, boolean italic, boolean bold, boolean underline) {
        return drawString(s, x, y, 1.0, 1.0, color.intVal, false, false, italic, bold, underline); }
    public static double drawString(String s, double x, double y, int color, boolean italic, boolean bold, boolean underline) {
        return drawString(s, x, y, 1.0, 1.0, color, false, false, italic, bold, underline);
    }
    
    public static double drawString(String s, double x, double y, double scx, double scy, EColors color, boolean italic, boolean bold, boolean underline) {
        return drawString(s, x, y, scx, scy, color.intVal, false, false, italic, bold, underline); }
    public static double drawString(String s, double x, double y, double scX, double scY, int color, boolean italic, boolean bold, boolean underline) {
        return drawString(s, x, y, scX, scY, color, false, false, italic, bold, underline);
    }
    
    public static double drawString(String s, double x, double y, double scx, double scy, EColors color, boolean centered, boolean shadow) {
        return drawString(s, x, y, scx, scy, color.intVal, centered, shadow); }
    public static double drawString(String s, double x, double y, double scX, double scY, int color, boolean centered, boolean shadow) {
        return drawString(s, x, y, scX, scY, color, centered, shadow, false, false, false);
    }
    
    public static double drawString(String s, double x, double y, double scx, double scy, int color, boolean centered, boolean shadow, boolean italic, boolean bold, boolean underline) {
        if (s == null) return 0.0;
        
        double lastX = (centered) ? x - (FontRenderer.strWidth(s, scx) / 2) : x;
        int i = 0;
        String curString = "";
        int curColor = color;
        boolean hasCode = false;
        
        while (i < s.length()) {
            char c = s.charAt(i);
            
            // '8750' == the 'contour integral' character
            if (c == 8750 && i + 2 < s.length()) {
                if (hasCode) {
                    if (shadow) {
                        int br = EColors.changeBrightness(curColor, shadowColor);
                        FontRenderer.drawString(curString, lastX + 2, y + 2, br, scx, scy, italic, bold, underline);
                    }
                    lastX = (double) FontRenderer.drawString(curString, lastX, y, curColor, scx, scy, italic, bold, underline);
                    curString = "";
                }
                else {
                    if (shadow) {
                        int br = EColors.changeBrightness(curColor, shadowColor);
                        FontRenderer.drawString(curString, lastX + 2, y + 2, br, scx, scy, italic, bold, underline);
                    }
                    double val = (double) FontRenderer.drawString(curString, lastX, y, curColor, scx, scy, italic, bold, underline) - 1;
                    // duct tape fix
                    if (val != -1) lastX = val;
                    curString = "";
                }
                hasCode = true;
                
                try {
                    int code = Integer.parseInt(s.substring(i + 1, i + 3));
                    EColors col = EColors.byCode(code);
                    
                    curColor = (col != null) ? col.intVal : EColors.white.intVal;
                    
                    i += 2;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                curString += c;
            }
            
            i++;
        }
        
        if (!hasCode) {
            if (shadow) {
                int br = EColors.changeBrightness(color, shadowColor);
                FontRenderer.drawString(curString, lastX + 2, y + 2, br, scx, scy, italic, bold, underline);
            }
            lastX = FontRenderer.drawString(s, lastX, y, color, scx, scy, italic, bold, underline);
        }
        else {
            if (shadow) {
                int br = EColors.changeBrightness(color, shadowColor);
                FontRenderer.drawString(curString, lastX + 2, y + 2, br, scx, scy, italic, bold, underline);
            }
            lastX = FontRenderer.drawString(curString, lastX, y, curColor, scx, scy, italic, bold, underline);
        }
        
        return lastX;
    }
    
    /** Breaks a String into a list of smaller strings based on a set maximum line width. */
    @Broken(since="June 5, 2022")
    public static EList<String> createWordWrapString(String stringIn, double widthMax) {
        EList<String> lines = EList.newList();
        
        // don't care if string is null or empty
        if (stringIn == null || stringIn.isEmpty()) return lines;
        // don't care if the allotted width is less than the length of one character
        if (widthMax < 10) return lines;
        
        boolean shouldWrap = FontRenderer.strWidth(stringIn) > widthMax;
        
        try {
            
            // if there's no need to wrap, just return the line itself
            if (!shouldWrap) {
                lines.add(stringIn);
                return lines;
            }
            
            //CURRENTLY STUCK IN INFINITE LOOP HERE!
            
            String restOfString = stringIn;
            while (FontRenderer.strWidth(restOfString) > widthMax) {
                int i = 0;
                int iPos = 0;
                char end = Character.MIN_VALUE;
                String buildString = "";
                
                while (!(FontRenderer.strWidth(buildString) >= widthMax) && i < restOfString.length() - 1) {
                    buildString += restOfString.charAt(i);
                    i++;
                }
                
                while (i > 0 && end != ' ') {
                    iPos = i;
                    end = restOfString.charAt(i--);
                }
                
                if (i <= 0) {
                    lines.add(restOfString.substring(0, buildString.length() - 1));
                    restOfString = restOfString.substring(buildString.length() - 1, restOfString.length());
                }
                else {
                    lines.add(restOfString.substring(0, iPos));
                    restOfString = restOfString.substring(iPos + 1, restOfString.length());
                }
                
            }
            
            lines.add(restOfString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return lines;
    }
    
}
