package envision.engine.rendering.fontRenderer;

import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.debug.Broken;

/** Used to concatenate 'EColors' color codes within drawn Strings. */
public class EStringOutputFormatter {
	
	/** The color of the shadow in the range of [0, 255]. */
	public static int shadowColor = 60;
	
	public static double drawString(String s, double x, double y, double scaleX, double scaleY, EColors colorIn, boolean centered, boolean shadow) {
		return drawString(s, x, y, scaleX, scaleY, colorIn.intVal, centered, shadow, false);
	}
	
	public static double drawString(String s, double x, double y, double scaleX, double scaleY, EColors colorIn, boolean centered, boolean shadow, boolean batch) {
		return drawString(s, x, y, scaleX, scaleY, colorIn.intVal, centered, shadow, batch);
	}
	
	public static double drawString(String s, double x, double y, double scaleX, double scaleY, int colorIn, boolean centered, boolean shadow) {
		return drawString(s, x, y, scaleX, scaleY, colorIn, centered, shadow, false);
	}
	
	public static double drawString(String s, double x, double y, double scaleX, double scaleY, int colorIn, boolean centered, boolean shadow, boolean batch) {
		if (s == null) return -1.0;
		
		double lastX = (centered) ? x - (getStringWidth(s) / 2) : x;
		int i = 0;
		String curString = "";
		int curColor = colorIn;
		boolean hasCode = false;
		
		
		while (i < s.length()) {
			char c = s.charAt(i);
			
			// '8750' == the 'contour integral' character
			if (c == 8750 && i + 2 < s.length()) {
				if (hasCode) {
					if (shadow) {
						int br = EColors.changeBrightness(curColor, shadowColor);
						FontRenderer.drawString(curString, lastX - 2, y + 2, br, scaleX, scaleY, batch);
					}
					lastX = (double) FontRenderer.drawString(curString, lastX, y, curColor, scaleX, scaleY, batch);
					curString = "";
				}
				else {
					if (shadow) {
						int br = EColors.changeBrightness(curColor, shadowColor);
						FontRenderer.drawString(curString, lastX - 2, y + 2, br, scaleX, scaleY, batch);
					}
					double val = (double) FontRenderer.drawString(curString, lastX, y, curColor, scaleX, scaleY, batch) - 1;
					// duct tape fix
					if (val != -1) lastX = val;
					curString = "";
				}
				hasCode = true;
				
				try {
					int code = Integer.parseInt(s.substring(i + 1, i + 3));
					EColors color = EColors.byCode(code);
					
					curColor = (color != null) ? color.intVal : EColors.white.intVal;
					
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
				int br = EColors.changeBrightness(colorIn, shadowColor);
				FontRenderer.drawString(curString, lastX - 2, y + 2, br, scaleX, scaleY, batch);
			}
			lastX = FontRenderer.drawString(s, lastX, y, colorIn, scaleX, scaleY, batch);
		}
		else {
			if (shadow) {
				int br = EColors.changeBrightness(colorIn, shadowColor);
				FontRenderer.drawString(curString, lastX - 2, y + 2, br, scaleX, scaleY, batch);
			}
			lastX = FontRenderer.drawString(curString, lastX, y, curColor, scaleX, scaleY, batch);
		}
		
		return lastX;
	}
	
	public static int getStringWidth(String in) {
		if (in == null) return -1;
		
		//remove EMC color codes
		String str = "";
		for (int i = 0, j = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			if (j > 0) {
				if (++j >= 3) j = 0;
			}
			else if (c == 8750) { //the 'contour integral' character
				j = 1;
			}
			else str += c;
		}
		
		return FontRenderer.getStringWidth(str);
	}
	
	/** Breaks a String into a list of smaller strings based on a set maximum line width. */
	@Broken(since="June 5, 2022")
	public static EArrayList<String> createWordWrapString(String stringIn, double widthMax) {
		EArrayList<String> lines = new EArrayList();
		try {
			
			//CURRENTLY STUCK IN INFINITE LOOP HERE!
			
			if (stringIn != null && !stringIn.isEmpty() && widthMax > 5 && getStringWidth(stringIn) > widthMax) {
				String restOfString = stringIn;
				while (getStringWidth(restOfString) > widthMax) {
					int i = 0;
					int iPos = 0;
					char end = Character.MIN_VALUE;
					String buildString = "";
					//System.out.println("HERE 1");
					
					while (!(getStringWidth(buildString) >= widthMax) && i < restOfString.length() - 1) {
						buildString += restOfString.charAt(i);
						i++;
						//System.out.println("HERE 2");
					}
					
					while (i > 0 && end != ' ') {
						iPos = i;
						end = restOfString.charAt(i--);
						//System.out.println("HERE 3");
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
			else lines.add(stringIn);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
}
