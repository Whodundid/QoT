package envision.engine.internal.rendering.fontRenderer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;

import envision.engine.EngineSettings;
import envision.engine.loader.built.game.GameTexture;
import eutil.datatypes.boxes.Box2;
import eutil.file.LineReader;

/**
 * A font set which can be displayed in the Envision engine.
 * 
 * @implSpec can only be instantiated AFTER the textureSystem!
 * 
 * @author Hunter Bragg
 */
public class GameFont {
    
    private static final String fontDir = EngineSettings.RESOURCES_DIR.toString() + "/font/";
    
    //========
    // Fields
    //========
    
    private String fontName;
    private String mappingPath;
    private String fontPath;
    private String boldFontPath;
    private GameTexture fontImage;
    private GameTexture boldFontImage;
    private Map<String, Integer> mapping;
    private Map<String, Double> characterWidth;
    private int width, height;
    private double scaleW, scaleH;
    private double scaleSpace;
    private boolean failed;
    private boolean flipBold;
    private boolean defaultBold;    
    //==============
    // Constructors
    //==============

    private GameFont(String nameIn, String mappingPathIn, String fontPathIn) {
        this(nameIn, mappingPathIn, fontPathIn, fontPathIn, GL11.GL_NEAREST, GL11.GL_NEAREST);
    }
    private GameFont(String nameIn, String mappingPathIn, String fontPathIn, int minFilter, int magFilter) {
        this(nameIn, mappingPathIn, fontPathIn, fontPathIn, GL11.GL_NEAREST, GL11.GL_NEAREST);
    }
    private GameFont(String nameIn, String mappingPathIn, String fontPathIn, String boldFontPathIn) {
        this(nameIn, mappingPathIn, fontPathIn, boldFontPathIn, GL11.GL_NEAREST, GL11.GL_NEAREST);
    }
    private GameFont(String nameIn, String mappingPathIn, String fontPathIn, String boldFontPathIn, int minFilter, int magFilter) {
        if (mappingPathIn == null || fontPathIn == null) {
            throw new RuntimeException("Invalid font maping path! " + mappingPathIn);
        }
        
        fontName = nameIn;
        
        mapping = new HashMap<>();
        characterWidth = new HashMap<>();
        mappingPath = mappingPathIn;
        fontPath = fontPathIn;
        boldFontPath = boldFontPathIn;
        
        loadMappingFile();
        
        fontImage = new GameTexture(fontPathIn, minFilter, magFilter);
        boldFontImage = new GameTexture(boldFontPathIn, minFilter, magFilter);
    }
    
    public void reloadMappingFile() {
        mapping.clear();
        characterWidth.clear();
        loadMappingFile();
    }
    
    private void loadMappingFile() {
        try (var reader = new LineReader(new File(mappingPath))) {
            //read font dimensional data
            if (reader.hasNextLine()) {
                String line = reader.nextLine();
                var dataParser = new Scanner(line);
                width = dataParser.nextInt();
                height = dataParser.nextInt();
                scaleW = dataParser.nextDouble();
                scaleH = dataParser.nextDouble();
                scaleSpace = dataParser.nextDouble();
                if (dataParser.hasNextBoolean()) {
                    defaultBold = dataParser.nextBoolean();
                }
                dataParser.close();
            }
            
            //read mapping
            int i = 0;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.equals(",") || !line.contains(",")) {
                    mapping.put(line, i++);
                    characterWidth.put(line, (double) width);
                }
                else {
                    String[] parts = line.split(",");
                    String theChar = parts[0];
                    double width = Double.parseDouble(parts[1]);
                    mapping.put(theChar, i++);
                    characterWidth.put(line, width);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            failed = true;
        }
        
        if (failed) {
            throw new RuntimeException("Failed to parse font mapping file! '" + mappingPath + "'");
        }
    }    
    //=========
    // Getters
    //=========
    
    /** Returns the x and y coordinates within the fontImage for the corresponding character. */
    public Box2<Integer, Integer> getCharImage(char charIn) {
        if (charIn == FontRenderer.ERROR_CHAR) return getErrorChar();
        if (charIn == FontRenderer.COPYRIGHT) return getCopyrightChar();
        int pos = mapping.getOrDefault(String.valueOf(charIn), -1);
        return (pos != -1) ? new Box2<>(pos % 16, pos / 16) : new Box2<>(-1, -1);
    }
    
    public Box2<Integer, Integer> getErrorChar() { return new Box2<>(14, 5); }
    public Box2<Integer, Integer> getCopyrightChar() { return new Box2<>(15, 5); }
    
    public GameTexture getFontTexture() { return fontImage; }
    public GameTexture getBoldFontTexture() { return boldFontImage; }
    public Map<String, Integer> getMaping() { return mapping; }
    public boolean created() { return !failed; }
    
    public String getMapingFile() { return mappingPath; }
    public String getFontFile() { return fontPath; }
    
    public String getFontName() { return fontName; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getScaleW() { return scaleW; }
    public double getScaleH() { return scaleH; }
    public double getScaleSpace() { return scaleSpace; }
    public boolean isFlipBold() { return flipBold; }
    public boolean isDefaultBold() { return defaultBold; }
    
    public double getCharWidth(String in) {
        return characterWidth.get(in);
    }    
    //=========
    // Setters
    //=========
    
    public void setScaleW(double val) { scaleW = val; }
    public void setScaleH(double val) { scaleH = val; }
    public void setFlipBold(boolean val) { flipBold = val; }    
    //================
    // Static Methods
    //================
    
    public static GameFont createCustomFont(String nameIn, String mappingPathIn, String fontPathIn) {
        return new GameFont(nameIn, mappingPathIn, fontPathIn);
    }
    public static GameFont createCustomFont(String nameIn, String mappingPathIn, String fontPathIn, int minFilter, int magFilter) {
        return new GameFont(nameIn, mappingPathIn, fontPathIn, minFilter, magFilter);
    }
    public static GameFont createCustomFont(String nameIn, String mappingPathIn, String fontPathIn, String boldFontPathIn) {
        return new GameFont(nameIn, mappingPathIn, fontPathIn, boldFontPathIn);
    }
    public static GameFont createCustomFont(String nameIn, String mappingPathIn, String fontPathIn, String boldFontPathIn, int minFilter, int magFilter) {
        return new GameFont(nameIn, mappingPathIn, fontPathIn, boldFontPathIn, minFilter, magFilter);
    }
    
    public static GameFont createFont(String nameIn, String mappingPathIn, String fontPathIn) {
        return new GameFont(nameIn, fontDir + mappingPathIn, fontDir + fontPathIn);
    }
    public static GameFont createFont(String nameIn, String mappingPathIn, String fontPathIn, int minFilter, int magFilter) {
        return new GameFont(nameIn, fontDir + mappingPathIn, fontDir + fontPathIn, minFilter, magFilter);
    }
    public static GameFont createFont(String nameIn, String mappingPathIn, String fontPathIn, String boldFontPathIn) {
        return new GameFont(nameIn, fontDir + mappingPathIn, fontDir + fontPathIn, fontDir + boldFontPathIn);
    }
    public static GameFont createFont(String nameIn, String mappingPathIn, String fontPathIn, String boldFontPathIn, int minFilter, int magFilter) {
        return new GameFont(nameIn, fontDir + mappingPathIn, fontDir + fontPathIn, fontDir + boldFontPathIn, minFilter, magFilter);
    }
    
}
