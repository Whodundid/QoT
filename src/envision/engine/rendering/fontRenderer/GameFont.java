package envision.engine.rendering.fontRenderer;

import java.io.File;
import java.util.Scanner;

import envision.engine.rendering.textureSystem.GameTexture;
import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.Box2;

/**
 * A font set which can be displayed in the Envision engine.
 * 
 * @implSpec can only be instantiated AFTER the textureSystem!
 * 
 * @author Hunter Bragg
 */
public class GameFont {
	
	private String mapingPath;
	private String fontPath;
	private GameTexture fontImage;
	private EArrayList<Character> mapping;
	private int width, height;
	private double scaleW, scaleH;
	private double scaleSpace;
	private boolean failed;
	
	//--------------
	// Constructors
	//--------------
	
	private GameFont(String mapingPathIn, String fontPathIn) {
		if (mapingPathIn == null || fontPathIn == null) { throw new RuntimeException("Invalid font maping path! " + mapingPathIn); }
		
		mapping = new EArrayList();
		mapingPath = mapingPathIn;
		fontPath = fontPathIn;
		
		try (Scanner reader = new Scanner(new File(mapingPathIn))) {
			//read font dimensional data
			width = reader.nextInt();
			height = reader.nextInt();
			scaleW = reader.nextDouble();
			scaleH = reader.nextDouble();
			scaleSpace = reader.nextDouble(); 
			//read mapping
			while (reader.hasNext()) {
				mapping.add(reader.next().charAt(0));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			failed = true;
			//throw new RuntimeException("Invalid font mapping path! " + mapingPathIn);
		}
		
		if (!failed) fontImage = new GameTexture(fontPathIn);
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the x and y coordinates within the fontImage for the corresponding character. */
	public Box2<Integer, Integer> getCharImage(char charIn) {
		int pos = -1;
		for (int i = 0; i < mapping.size(); i++) {
			if (charIn == FontRenderer.ERROR_CHAR) return getErrorChar();
			if (charIn == FontRenderer.COPYRIGHT) return getCopyrightChar();
			if (mapping.get(i) == charIn) {
				pos = i;
				break;
			}
		}
		
		return (pos != -1) ? new Box2(pos % 16, pos / 16) : new Box2(-1, -1);
	}
	
	public Box2<Integer, Integer> getErrorChar() { return new Box2<>(14, 5); }
	public Box2<Integer, Integer> getCopyrightChar() { return new Box2<>(15, 5); }
	
	public GameTexture getFontTexture() { return fontImage; }
	public EArrayList<Character> getMaping() { return mapping; }
	public boolean created() { return !failed; }
	
	public String getMapingFile() { return mapingPath; }
	public String getFontFile() { return fontPath; }
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public double getScaleW() { return scaleW; }
	public double getScaleH() { return scaleH; }
	public double getScaleSpace() { return scaleSpace; }
	
	//----------------
	// Static Methods
	//----------------
	
	public static GameFont createFont(String mapingPathIn, String fontPathIn) {
		return new GameFont(mapingPathIn, fontPathIn);
	}
	
}
