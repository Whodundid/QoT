package envision.game.dialog;

import eutil.datatypes.EArrayList;

public class DialogueOption {
	private String dialogueHeader; // header for the text bubble option upon selection
	private String dialogueToSay; // actual dialogue associated to each index of dialogue headers
	
	private int id;
	
	public DialogueOption(String dialogueHeaderIn, String dialogueToSayIn, int idIn) {
		dialogueHeader = dialogueHeaderIn;
		dialogueToSay = dialogueToSayIn;
		
		id = idIn;
	}
	
	/**
	 * Gets the Dialogue Header to display text in the DiaogueBox for the player to respond to. This contains the basic title of 
	 * what the player's response could be.
	 * @return
	 */
	public String getDialogueHeader() {
		return dialogueHeader;
	}
	
	/**
	 * The Dialogue that the player actually says. This will appear above the player in a DialogueBox just like an Entity. This conveys
	 * speech for the player and triggers the call to EntityDialogue.
	 * @return
	 */
	public String getDialogueToSay() {
		return dialogueToSay;
	}
	
	/**
	 * Id to manage what order of people are able to speak in the current context.
	 * @return
	 */
	public int getId() {
		return id;
	}
}
