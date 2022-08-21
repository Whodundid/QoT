package envision.renderEngine.fontRenderer;

import java.io.File;
import java.util.Scanner;

import envision.renderEngine.textureSystem.GameTexture;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;

//can only be instantiated after the textureSystem
public class GameFont {
	
	private String mapingPath;
	private String fontPath;
	private GameTexture fontImage;
	private EArrayList<Character> mapping;
	private int width, height;
	private double scaleW, scaleH;
	private double scaleSpace;
	private boolean failed;
	
	//---------------------
	//GameFont Constructors
	//---------------------
	
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
			//throw new RuntimeException("Invalid font maping path! " + mapingPathIn);
		}
		
		if (!failed) fontImage = new GameTexture(fontPathIn);
	}
	
	//----------------
	//GameFont Getters
	//----------------
	
	/** Returns the x and y coordinates within the fontImage for the corresponding character. */
	public Box2<Integer, Integer> getCharImage(char charIn) {
		int pos = -1;
		for (int i = 0; i < mapping.size(); i++) {
			if (mapping.get(i) == charIn) { pos = i; break; }
		}
		
		return (pos != -1) ? new Box2(pos % 16, pos / 16) : new Box2(-1, -1);
	}
	
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
	
	//-----------------------
	//GameFont Static Methods
	//-----------------------
	
	public static GameFont createFont(String mapingPathIn, String fontPathIn) {
		return new GameFont(mapingPathIn, fontPathIn);
	}
	
}
