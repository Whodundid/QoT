package gameSystems.fontRenderer;

import gameSystems.textureSystem.GameTexture;
import java.io.File;
import java.util.Scanner;
import storageUtil.EArrayList;
import storageUtil.StorageBox;

//can only be instantiated after the textureSystem
public class GameFont {
	
	private String mapingPath;
	private String fontPath;
	private GameTexture fontImage;
	private EArrayList<Character> maping;
	
	//---------------------
	//GameFont Constructors
	//---------------------
	
	private GameFont(String mapingPathIn, String fontPathIn) {
		if (mapingPathIn == null || fontPathIn == null) { throw new RuntimeException("Invalid font maping path! " + mapingPathIn); }
		
		maping = new EArrayList();
		mapingPath = mapingPathIn;
		fontPath = fontPathIn;
		
		try (Scanner reader = new Scanner(new File(mapingPathIn))) {
			while (reader.hasNext()) {
				maping.add(reader.next().charAt(0));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid font maping path! " + mapingPathIn);
		}
		
		fontImage = new GameTexture(fontPathIn);
	}
	
	//----------------
	//GameFont Getters
	//----------------
	
	/** Returns the x and y coordinates within the fontImage for the corresponding character. */
	public StorageBox<Integer, Integer> getCharImage(char charIn) {
		int pos = -1;
		for (int i = 0; i < maping.size(); i++) {
			if (maping.get(i) == charIn) { pos = i; break; }
		}
		
		return (pos != -1) ? new StorageBox(pos % 16, pos / 16) : new StorageBox(-1, -1);
	}
	
	public GameTexture getFontTexture() { return fontImage; }
	
	public EArrayList<Character> getMaping() { return maping; }
	
	public String getMapingFile() { return mapingPath; }
	public String getFontFile() { return fontPath; }
	
	//-----------------------
	//GameFont Static Methods
	//-----------------------
	
	public static GameFont createFont(String mapingPathIn, String fontPathIn) {
		return new GameFont(mapingPathIn, fontPathIn);
	}
	
}
