package game.doodads;

import assets.textures.DoodadTextures;
import game.entities.Entity;

public class PineTree extends Entity {

	public PineTree() { this(0, 0); }
	public PineTree(int posX, int posY) {
		super("pine");
		init(posX, posY, 256, 256);
		sprite = DoodadTextures.pine_tree;
	}
	
	@Override
	public int getObjectID() {
		return 5;
	}
	
}
