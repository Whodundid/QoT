package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

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
	
	private static final String base = "resources/textures/editor/";
	
	static {
		rectangle = new GameTexture("rectangle", base, "rectsel.png");
		move = new GameTexture("move", base, "move.png");
		magicWand = new GameTexture("magicWand", base, "magicwand.png");
		paintBucket = new GameTexture("paintBucket", base, "paintbucket.png");
		brush = new GameTexture("brush", base, "brush.png");
		pencil = new GameTexture("pencil", base, "pencil.png");
		eyeDropper = new GameTexture("eyeDropper", base, "eyedropper.png");
		eraser = new GameTexture("eraser", base, "eraser.png");
		line = new GameTexture("line", base, "line.png");
		select = new GameTexture("select", base, "select.png");
		shape = new GameTexture("shape", base, "shape.png");
		
		play = new GameTexture("play", base, "play.png");
		stop = new GameTexture("stop", base, "stop.png");
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
