package assets.textures;

import eutil.EUtil;
import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;
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
		shape = new GameTexture("shape", "bin/textures/editor/shape.png");
		
		play = new GameTexture("play", "bin/textures/editor/play.png");
		stop = new GameTexture("stop", "bin/textures/editor/stop.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(rectangle);
		systemIn.registerTexture(move);
		systemIn.registerTexture(magicWand);
		systemIn.registerTexture(paintBucket);
		systemIn.registerTexture(brush);
		systemIn.registerTexture(pencil);
		systemIn.registerTexture(eyeDropper);
		systemIn.registerTexture(eraser);
		systemIn.registerTexture(line);
		systemIn.registerTexture(shape);
		systemIn.registerTexture(play);
		systemIn.registerTexture(stop);
		
		textures.add(rectangle);
		textures.add(move);
		textures.add(magicWand);
		textures.add(paintBucket);
		textures.add(brush);
		textures.add(pencil);
		textures.add(eyeDropper);
		textures.add(eraser);
		textures.add(line);
		textures.add(shape);
		textures.add(play);
		textures.add(stop);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
