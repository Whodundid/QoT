package gameSystems.fontRenderer;

import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

/** Used to concatenate EColors and EnumChatFormating objects within Strings. */
public class EStringBuilder {
	
	public static double drawString(String s, double x, double y, EColors colorIn, boolean centered, boolean shadow) { return drawString(s, x, y, colorIn.intVal, centered, shadow); }
	public static double drawString(String s, double x, double y, int colorIn, boolean centered, boolean shadow) {
		
		if (s != null) {
			double lastX = (centered) ? x - getStringWidth(s) / 2 : x;
			int i = 0;
			String curString = "";
			int curColor = 0xffffff;
			boolean hasCode = false;
			
			while (i < s.length()) {
				char c = s.charAt(i);
				
				if (c == 8750 && i + 2 < s.length()) { //the 'countour integral' character
					
					if (hasCode) {
						lastX = (double) FontRenderer.drawString(curString, lastX, y, curColor);
						curString = "";
					}
					else {
						lastX = (double) FontRenderer.drawString(curString, lastX, y, curColor) - 1;
						curString = "";
					}
					hasCode = true;
					
					try {
						int code = Integer.parseInt(s.substring(i + 1, i + 3));
						EColors color = EColors.getEColorByCode(code);
						
						curColor = (color != null) ? color.intVal : EColors.white.intVal;
						
						i += 2;
					}
					catch (Exception e) { e.printStackTrace(); }
				}
				else {
					curString += c;
				}
				
				i++;
			}
			
			if (!hasCode) {
				lastX = FontRenderer.drawString(s, lastX, y, colorIn);
			}
			else {
				lastX = FontRenderer.drawString(curString, lastX, y, curColor);
			}
			
			return lastX;
		}
		
		return -1;
	}
	
	public static int getStringWidth(String in) {
		if (in != null) {
			
			//remove EMC color codes
			String str = "";
			for (int i = 0, j = 0; i < in.length(); i++) {
				char c = in.charAt(i);
				
				if (j > 0) {
					if (++j >= 3) { j = 0; }
				}
				else if (c == 8750) { //the 'countour integral' character
					j = 1;
				}
				else { str += c; }
			}
			
			return FontRenderer.getStringWidth(str);
		}
		return -1;
	}
	
	/** Breaks a String into a list of smaller strings based on a set maximum line width. */
	public static EArrayList<String> createWordWrapString(String stringIn, double widthMax) {
		EArrayList<String> lines = new EArrayList();
		try {
			if (stringIn != null && !stringIn.isEmpty() && widthMax > 5 && getStringWidth(stringIn) > widthMax) {
				String restOfString = stringIn;
				while (getStringWidth(restOfString) > widthMax) {
					int i = 0;
					int iPos = 0;
					char end = Character.MIN_VALUE;
					String buildString = "";
					
					while (!(getStringWidth(buildString) >= widthMax) && i < restOfString.length() - 1) {
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
			else { lines.add(stringIn); }
		}
		catch (Exception e) { e.printStackTrace(); }
		return lines;
	}
	
}
