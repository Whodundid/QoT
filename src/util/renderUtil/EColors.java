package util.renderUtil;

import java.awt.Color;

//Author: Hunter Bragg

/** The color palette used by the Envision Game Engine.
 *  A much more 'vibrant' color palette as compared to vanilla Minecraft. */
public enum EColors {
	
	//Envision colors
	lred(0xffff5555, 10, "Light Red"),
	red(0xffff0000, 11, "Red"),
	maroon(0xff9e0012, 12, "Maroon"),
	brown(0xff7F3000, 13, "Brown"),
	dorange(0xffB24400, 14, "Dark Orange"),
	borange(0xffff6600, 15, "Bright Orange"),
	orange(0xffff9900, 16, "Orange"),
	lorange(0xffffaa00, 17, "Light Orange"),
	yellow(0xffffff00, 18, "Yellow"),
	lime(0xffaaff00, 19, "Lime"),
	green(0xff55ff55, 20, "Light Green"),
	seafoam(0xff00ff8c, 21, "Seafoam"),
	lgreen(0xff00ff00, 22, "Green"),
	dgreen(0xff00af00, 23, "Dark Green"),
	turquoise(0xff00c1ae, 24, "Turquoise"),
	aquamarine(0xff00ffdc, 25, "Aquamarine"),
	cyan(0xff00ffff, 26, "Cyan"),
	skyblue(0xff00baff, 27, "Sky Blue"),
	blue(0xff0065ff, 28, "Blue"),
	regal(0xff004EC4, 29, "Regal Blue"),
	navy(0xff0000ff, 30, "Navy"),
	grape(0xff4200ff, 31, "Grape"),
	violet(0xff430093, 32, "Violet"),
	eggplant(0xff772789, 33, "Eggplant"),
	purple(0xffdd49ff, 34, "Purple"),
	pink(0xfff872e9, 35, "Pink"),
	hotpink(0xffff00dc, 36, "Hot Pink"),
	magenta(0xffff0061, 37, "Magenta"),
	white(0xffffffff, 38, "White"),
	lgray(0xffb2b2b2, 39, "Light Gray"),
	gray(0xff8d8d8d, 40, "Gray"),
	mgray(0xff636363, 41, "Medium Gray"),
	dgray(0xff474747, 42, "Dark Gray"),
	pdgray(0xff303030, 43, "Pretty Dark Gray"),
	steel(0xff1f1f1f, 44, "Steel"),
	dsteel(0xff191919, 45, "Dark Steel"),
	vdgray(0xff111111, 46, "Very Dark Gray"),
	black(0xff000000, 47, "Black"),
	
	//MC colors
	mc_darkred(0xffaa0000, 48, "MC Dark Red"),
	mc_red(0xffff5555, 49, "MC Red"),
	mc_gold(0xffffaa00, 50, "MC Gold"),
	mc_yellow(0xffffff55, 51, "MC Yellow"),
	mc_darkgreen(0xff00aa00, 52, "MC Dark Green"),
	mc_green(0xff55ff55, 53, "MC Green"),
	mc_aqua(0xff55ffff, 54, "MC Aqua"),
	mc_darkaqua(0xff00aaaa, 55, "MC Dark Aqua"),
	mc_darkblue(0xff0000aa, 56, "MC Dark Blue"),
	mc_blue(0xff5555ff, 57, "MC Blue"),
	mc_lightpurple(0xffff55ff, 58, "MC Light Purple"),
	mc_darkpurple(0xffaa00aa, 59, "MC Dark Purple"),
	mc_white(0xffffffff, 60, "MC White"),
	mc_gray(0xffaaaaaa, 61, "MC Gray"),
	mc_darkgray(0xff555555, 62, "MC Dark Gray"),
	mc_black(0xff000000, 63, "MC Black");
	
	public int intVal;
	public int code;
	public String name;
	
	EColors(int colorIn, int codeIn, String nameIn) {
		intVal = colorIn;
		code = codeIn;
		name = nameIn;
	}
	
	@Override public String toString() { return "\u222e" + code; }

	/** Returns the color integer. */
	public int c() { return intVal; }
	
	/** Returns the color name. */
	public String n() { return name; }
	
	/** Returns an EColors with the corresponding integer color (if any). */
	public static EColors getEColor(int colorIn) {
		for (EColors c : values()) {
			if (c.intVal == colorIn) { return c; }
		}
		return null;
	}
	
	public static EColors getEColor(String nameIn) {
		for (EColors c : values()) {
			if (c.getClass().getSimpleName().equalsIgnoreCase(nameIn)) { return c; }
		}
		return null;
	}
	
	/** Returns an EColors with the corresponding code (if any). */
	public static EColors getEColorByCode(int codeIn) {
		for (EColors c : values()) {
			if (c.code == codeIn) { return c; }
		}
		return null;
	}
	
	/** Returns an EColors with the corresponding String name (if any). */
	public static EColors getEColorByName(String colorNameIn) {
		if (colorNameIn != null) {
			for (EColors c : values()) {
				if (c.name.toLowerCase().equals(colorNameIn.toLowerCase())) { return c; }
			}
		}
		return null;
	}
	
	/** Needs to be consistently called in order for any color change to occur. */
	public static int rainbow() {
		return Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 1f);
	}
	
	public static EColors bool(boolean val) { return (val) ? EColors.green : EColors.lred; }
	public static EColors bool(boolean val, EColors ifTrue, EColors ifFalse) { return (val) ? ifTrue : ifFalse; }
	
}
