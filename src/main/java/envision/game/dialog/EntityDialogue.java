package envision.game.dialog;

import envision.game.entities.Entity;

public abstract class EntityDialogue implements Comparable<EntityDialogue> {
	protected Entity entityDoingDialogue;
	protected String dialogueToSay;
	protected int id;
	
	protected EntityDialogue(Entity entityDoingDialogueIn, String dialogueToSayIn) {
		entityDoingDialogue = entityDoingDialogueIn;
		dialogueToSay = dialogueToSayIn;
	}

	public Entity getEntityDoingDialog() { return entityDoingDialogue; }
	public String getDialogToSay() { return dialogueToSay; }
	public int getId() { return id; }
	
	@Override public int compareTo(EntityDialogue e) {
		if (this.id > e.id) { return -1; }
		else if (this.id < e.id) { return 1; }
		return 0;
	}
}
