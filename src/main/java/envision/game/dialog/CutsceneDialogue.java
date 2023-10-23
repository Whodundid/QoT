package envision.game.dialog;

import envision.engine.windows.windowTypes.WindowObject;
import envision.game.entities.Entity;

public class CutsceneDialogue extends WindowObject {
	private Entity player;
	private Entity speaker;
	
	public CutsceneDialogue()  { }
	
	public Entity getPlayer() { return player; }
	public Entity getSpeaker() { return speaker; }
}
