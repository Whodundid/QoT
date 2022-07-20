package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import main.settings.QoTSettings;

public class EditorTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();

	public static final GameTexture rectangle;
	public static final GameTexture move;
	public static final GameTexture magicWand;
	public static final GameTexture paintBucket;
	public static final GameTexture brush;
	public static final GameTexture pencil;
	public static final GameTexture eyeDropper;
	public static final GameTexture eraser;
	public static final GameTexture line;
	public static final GameTexture select;
	public static final GameTexture shape;
	
	public static final GameTexture play;
	public static final GameTexture stop;
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\editor\\";
	
	static {
		rectangle = new GameTexture("rectangle", textureDir, "rectsel.png");
		move = new GameTexture("move", textureDir, "move.png");
		magicWand = new GameTexture("magicWand", textureDir, "magicwand.png");
		paintBucket = new GameTexture("paintBucket", textureDir, "paintbucket.png");
		brush = new GameTexture("brush", textureDir, "brush.png");
		pencil = new GameTexture("pencil", textureDir, "pencil.png");
		eyeDropper = new GameTexture("eyeDropper", textureDir, "eyedropper.png");
		eraser = new GameTexture("eraser", textureDir, "eraser.png");
		line = new GameTexture("line", textureDir, "line.png");
		select = new GameTexture("select", textureDir, "select.png");
		shape = new GameTexture("shape", textureDir, "shape.png");
		
		play = new GameTexture("play", textureDir, "play.png");
		stop = new GameTexture("stop", textureDir, "stop.png");
	}
	
	public static void registerTextures(TextureSystem ts) {
		textures.add(ts.registerTexture(rectangle));
		textures.add(ts.registerTexture(move));
		textures.add(ts.registerTexture(magicWand));
		textures.add(ts.registerTexture(paintBucket));
		textures.add(ts.registerTexture(brush));
		textures.add(ts.registerTexture(pencil));
		textures.add(ts.registerTexture(eyeDropper));
		textures.add(ts.registerTexture(eraser));
		textures.add(ts.registerTexture(line));
		textures.add(ts.registerTexture(select));
		textures.add(ts.registerTexture(shape));
		textures.add(ts.registerTexture(play));
		textures.add(ts.registerTexture(stop));
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
