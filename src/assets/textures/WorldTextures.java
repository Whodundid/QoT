package assets.textures;

import assets.textures.world.Tex_Grass;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

public class WorldTextures {
	
	// Textures
	
	private static final String world = "resources/textures/world/";
	private static final String dung = "resources/textures/world/dungeon/";
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture grass = new Tex_Grass();
	public static final GameTexture dirt = new GameTexture("Dirt", world, "dirt.png");
	public static final GameTexture sand = new GameTexture("Sand", world, "sand.png");
	public static final GameTexture water = new GameTexture("Water", world, "water.png");
	public static final GameTexture stone = new GameTexture("Stone", world, "stone.png");
	public static final GameTexture wood = new GameTexture("Wood", world, "wood.png");
	public static final GameTexture bush0 = new GameTexture("Bush0", world, "bush0.png");
	public static final GameTexture farm0 = new GameTexture("Farm0", world, "farm0.png");
	public static final GameTexture farm1 = new GameTexture("Farm1", world, "farm1.png");
	public static final GameTexture farm2 = new GameTexture("Farm2", world, "farm2.png");
	public static final GameTexture farm3 = new GameTexture("Farm3", world, "farm3.png");
	
	public static final GameTexture dungWallA = new GameTexture("Dungeon Wall A", dung, "dungenFloorA.png");
	public static final GameTexture dungWallB = new GameTexture("Dungeon Wall B", dung, "dungenFloorB.png");
	public static final GameTexture dungOldWallA = new GameTexture("Dungeon Old Wall A", dung, "dungeonFloorA.png");
	public static final GameTexture dungOldWallB = new GameTexture("Dungeon Old Wall B", dung, "dungeonFloorB.png");
	public static final GameTexture dungFloor = new GameTexture("Dungeon Floor", dung, "dungWall.png");
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(grass);
		systemIn.registerTexture(dirt);
		systemIn.registerTexture(sand);
		systemIn.registerTexture(water);
		systemIn.registerTexture(stone);
		systemIn.registerTexture(wood);
		systemIn.registerTexture(bush0);
		systemIn.registerTexture(farm0);
		systemIn.registerTexture(farm1);
		systemIn.registerTexture(farm2);
		systemIn.registerTexture(farm3);
		
		systemIn.registerTexture(dungWallA);
		systemIn.registerTexture(dungWallB);
		systemIn.registerTexture(dungOldWallA);
		systemIn.registerTexture(dungOldWallB);
		systemIn.registerTexture(dungFloor);
		
		textures.add(grass);
		textures.add(dirt);
		textures.add(sand);
		textures.add(water);
		textures.add(stone);
		textures.add(wood);
		textures.add(bush0);
		
		textures.add(dungWallA);
		textures.add(dungWallB);
		textures.add(dungOldWallA);
		textures.add(dungOldWallB);
		textures.add(dungFloor);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
	/** Returns a copy of the textures in this holder class. */
	public static EArrayList<GameTexture> getTextures() { return new EArrayList(textures); }
	
}
