package assets.textures;

import eutil.EUtil;
import renderEngine.textureSystem.GameTexture;
import renderEngine.textureSystem.TextureSystem;
import storageUtil.EArrayList;

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
	
	static {
		rectangle = new GameTexture("rectangle", "bin/textures/editor/rectsel.png");
		move = new GameTexture("move", "bin/textures/editor/move.png");
		magicWand = new GameTexture("magicWand", "bin/textures/editor/magicwand.png");
		paintBucket = new GameTexture("paintBucket", "bin/textures/editor/paintbucket.png");
		brush = new GameTexture("brush", "bin/textures/editor/brush.png");
		pencil = new GameTexture("pencil", "bin/textures/editor/pencil.png");
		eyeDropper = new GameTexture("eyeDropper", "bin/textures/editor/eyedropper.png");
		eraser = new GameTexture("eraser", "bin/textures/editor/eraser.png");
		line = new GameTexture("line", "bin/textures/editor/line.png");
		select = new GameTexture("select", "bin/textures/editor/select.png");
		shape = new GameTexture("shape", "bin/textures/editor/shape.png");
		
		play = new GameTexture("play", "bin/textures/editor/play.png");
		stop = new GameTexture("stop", "bin/textures/editor/stop.png");
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
