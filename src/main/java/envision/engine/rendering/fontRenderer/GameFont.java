package envision.engine.rendering.fontRenderer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;

import envision.engine.rendering.textureSystem.GameTexture;
import eutil.datatypes.boxes.Box2;
import eutil.file.LineReader;
import qot.settings.QoTSettings;

/**
 * A font set which can be displayed in the Envision engine.
 * 
 * @implSpec can only be instantiated AFTER the textureSystem!
 * 
 * @author Hunter Bragg
 */
public class GameFont {
	
	private static final String fontDir = QoTSettings.getResourcesDir().toString() + "\\font\\";
	
	private String mappingPath;
	private String fontPath;
	private GameTexture fontImage;
	private Map<Character, Integer> mapping;
	private int width, height;
	private double scaleW, scaleH;
	private double scaleSpace;
	private boolean failed;
	
	//--------------
	// Constructors
	//--------------

	private GameFont(String mappingPathIn, String fontPathIn) {
		this(mappingPathIn, fontPathIn, GL11.GL_NEAREST, GL11.GL_NEAREST);
	}
	private GameFont(String mappingPathIn, String fontPathIn, int minFilter, int magFilter) {
		if (mappingPathIn == null || fontPathIn == null) {
			throw new RuntimeException("Invalid font maping path! " + mappingPathIn);
		}
		
		mapping = new HashMap<>();
		mappingPath = mappingPathIn;
		fontPath = fontPathIn;
		
		try (var reader = new LineReader(new File(mappingPathIn))) {
			//read font dimensional data
			if (reader.hasNextLine()) {
				String line = reader.nextLine();
				var dataParser = new Scanner(line);
				width = dataParser.nextInt();
				height = dataParser.nextInt();
				scaleW = dataParser.nextDouble();
				scaleH = dataParser.nextDouble();
				scaleSpace = dataParser.nextDouble();
				dataParser.close();
			}
			
			//read mapping
			int i = 0;
			while (reader.hasNextLine()) {
				mapping.put(reader.nextLine().charAt(0), i++);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			failed = true;
		}
		
		if (failed) {
			throw new RuntimeException("Failed to parse font mapping file! '" + mappingPathIn + "'");
		}
		
		fontImage = new GameTexture(fontPathIn, minFilter, magFilter);
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the x and y coordinates within the fontImage for the corresponding character. */
	public Box2<Integer, Integer> getCharImage(char charIn) {
		if (charIn == FontRenderer.ERROR_CHAR) return getErrorChar();
		if (charIn == FontRenderer.COPYRIGHT) return getCopyrightChar();
		int pos = mapping.getOrDefault(charIn, -1);
//		mapping.
//		for (int i = 0; i < mapping.size(); i++) {
//			if (mapping.get(i) == charIn) {
//				pos = i;
//				break;
//			}
//		}
		
		return (pos != -1) ? new Box2<>(pos % 16, pos / 16) : new Box2<>(-1, -1);
	}
	
	public Box2<Integer, Integer> getErrorChar() { return new Box2<>(14, 5); }
	public Box2<Integer, Integer> getCopyrightChar() { return new Box2<>(15, 5); }
	
	public GameTexture getFontTexture() { return fontImage; }
	public Map<Character, Integer> getMaping() { return mapping; }
	public boolean created() { return !failed; }
	
	public String getMapingFile() { return mappingPath; }
	public String getFontFile() { return fontPath; }
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public double getScaleW() { return scaleW; }
	public double getScaleH() { return scaleH; }
	public double getScaleSpace() { return scaleSpace; }
	
	//----------------
	// Static Methods
	//----------------
	
	public static GameFont createCustomFont(String mappingPathIn, String fontPathIn) {
		return new GameFont(mappingPathIn, fontPathIn);
	}
	
	public static GameFont createCustomFont(String mappingPathIn, String fontPathIn, int minFilter, int magFilter) {
		return new GameFont(mappingPathIn, fontPathIn, minFilter, magFilter);
	}
	
	public static GameFont createFont(String mappingPathIn, String fontPathIn) {
		return new GameFont(fontDir + mappingPathIn, fontDir + fontPathIn);
	}
	
	public static GameFont createFont(String mappingPathIn, String fontPathIn, int minFilter, int magFilter) {
		return new GameFont(fontDir + mappingPathIn, fontDir + fontPathIn, minFilter, magFilter);
	}
	
}
