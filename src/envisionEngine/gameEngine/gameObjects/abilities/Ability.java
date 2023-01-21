package envision.gameEngine.gameObjects.abilities;

import envision.gameEngine.gameObjects.entity.Entity;
import envision.renderEngine.textureSystem.GameTexture;

public class Ability {
	
	private Entity ent;
	private String name;
	private GameTexture texture;
	
	public Ability(String nameIn, Entity entIn) { this(nameIn, entIn, null); }
	public Ability(String nameIn, Entity entIn, GameTexture textureIn) {
		name = nameIn;
		ent = entIn;
		texture = textureIn;
	}
	
	public Entity getEntity() { return ent; }
	public String getAbilityName() { return name; }
	public GameTexture getTexture() { return texture; }
	
}
