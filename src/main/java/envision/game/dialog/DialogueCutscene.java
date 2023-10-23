package envision.game.dialog;

import java.util.ArrayList;
import java.util.Collections;

import envision.engine.windows.windowTypes.WindowObject;
import envision.game.entities.Entity;
import qot.screens.gameplay.GamePlayScreen;

public class DialogueCutscene extends GamePlayScreen {
	private Entity playerCharacter;
	private Entity otherSpeaker;
	
	private ArrayList<EntityDialogue> allDialogue;
	private int cutsceneId;
	
	/**
	 * A cutscene is a Window Screen that takes place between the player and a source. It differs
	 * from normal dialogue since this pauses the world background and creates a conversation
	 * between 2 entities in their separate WindowObject.
	 */
	public DialogueCutscene(Entity playerCharacterIn, Entity otherSpeakerIn) {
		playerCharacter = playerCharacterIn;
		otherSpeaker = otherSpeakerIn;
		
		allDialogue = new ArrayList<EntityDialogue>();
	}
	
	public DialogueCutscene(Entity playerCharacterIn) {
		
	}
	
	public void start() {
		
	}
	
	public void arrangeDialogue() {
		Collections.sort(allDialogue);
	}
	
	// ----------------------
	// Getters
	// ----------------------
	public Entity getPlayerCharacter() { return playerCharacter; }
	public Entity getOtherSpeaker() { return otherSpeaker; }
	
	public ArrayList<EntityDialogue> getAllDialogue() { return allDialogue; }
	public EntityDialogue getDialogue(int index) { return allDialogue.get(index); }
	
	public int getCutsceneId() { return cutsceneId; }
	
}
