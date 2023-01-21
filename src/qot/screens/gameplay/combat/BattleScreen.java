package game.screens.gameplay.combat;

import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.gameSystems.screens.GameScreen;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.misc.Rotation;

public class BattleScreen extends GameScreen {
	
	//the parties in combat with one another
	private Party A, B;
	
	//turn stuff
	private boolean isATurn = true;
	private boolean isOver = false;
	private int aTurn = 0;
	private int bTurn = 0;
	private Entity curAEntity;
	private Entity curBEntity;
	
	private WindowButton tempNextTurn;
	
	//--------------
	// Constructors
	//--------------
	
	public BattleScreen(Entity a, Entity b) {
		A = new Party(a);
		B = new Party(b);
		
		for (int i = 0; i < 4; i++) {
			Entity entA = A.getSlot(i);
			Entity entB = B.getSlot(i);
			if (entA != null) entA.setFacing(Rotation.RIGHT);
			if (entB != null) entB.setFacing(Rotation.LEFT);
		}
	}
	
	public BattleScreen(Party a, Party b) {
		A = a;
		B = b;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initScreen() {
		curAEntity = A.getSlot(aTurn);
		curBEntity = B.getSlot(bTurn);
	}
	
	@Override
	public void initChildren() {
		tempNextTurn = new WindowButton(this, midX - 100, endY - 100, 200, 40, "Next Turn");
		
		addObject(tempNextTurn);
		
		renderTeamA();
		renderTeamB();
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.pdgray);
		drawLine(startX, endY - 175, endX, endY - 174, EColors.black);
		drawRect(startX, endY - 174, endX, endY, EColors.vdgray);
	}
	
	private void renderTeamA() {
		double distX = width / 9;
		double distY = height / 40;
		double sXA = width / 42;
		double sYA = midY + height / 12;
		double dW = width / 10;
		double dH = dW;
		
		//draw party members from left to right with far left being the last party member
		for (int i = A.getPartySize() - 1, q = 0; i >= 0; i--, q++) {
			Entity e = A.getSlot(i);
			
			double x = sXA + (q * distX);
			double y = sYA - (q * distY);
			
			var b = new BattleCharacter(e, x, y, dW, dH, true);
			addObject(b);
		}
	}
	
	private void renderTeamB() {
		double distX = width / 9;
		double distY = height / 40;
		double sXB = endX - width / 42 - distX;
		double sYB = midY + height / 12;
		double dW = width / 10;
		double dH = dW;
		
		//draw party members from left to right with far left being the last party member
		for (int i = B.getPartySize() - 1, q = 0; i >= 0; i--, q++) {
			Entity e = B.getSlot(i);
			
			double x = sXB - (q * distX);
			double y = sYB - (q * distY);
			
			var b = new BattleCharacter(e, x, y, dW, dH, false);
			addObject(b);
		}
	}
	
	private class BattleCharacter extends WindowButton {
		private Entity ent;
		private boolean teamA;
		
		public BattleCharacter(Entity e, double x, double y, double w, double h, boolean isTeamA) {
			super(BattleScreen.this, x, y, w, h);
			ent = e;
			teamA = isTeamA;
		}
		
		@Override
		public void drawObject(int mXIn, int mYIn) {
			drawTexture(ent.getTexture(), teamA);
			if (isMouseInside()) drawHRect(EColors.yellow);
			
			drawStringC(ent.getName(), midX, startY - 40, (teamA) ? EColors.skyblue : EColors.lred);
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object ==  tempNextTurn) advanceTurn();
	}
	
	//---------
	// Methods
	//---------
	
	public void advanceTurn() {
		//first check if either team is completely dead
		if (A.isDead()) { onTeamVictory(false); return; }
		if (B.isDead()) { onTeamVictory(true); return; }
		
		curAEntity = A.getSlot(aTurn);
		curBEntity = B.getSlot(bTurn);
		
		if (isATurn) {
			//increment a's turn #
			aTurn++;
			//roll back around to the first entity if over party size
			if (aTurn >= A.getPartySize()) aTurn = 0;
		}
		else {
			//increment b's turn #
			bTurn++;
			//roll back around to the first entity if over party size
			if (bTurn >= B.getPartySize()) bTurn = 0;
		}
		
		isATurn = !isATurn;
	}
	
	/** Forcefully make it team A's turn. */
	public void setAsTurn() { isATurn = true; }
	/** Forcefully make it team B's turn. */
	public void setBsTurn() { isATurn = false; }
	
	/**
	 * Used to update the total number of characters in a party for turn
	 * purposes. Is also used to keep track of whether or not a team is
	 * still alive or not.
	 * 
	 * @param pos
	 * @param isTeamA
	 */
	public void characterDied(int pos, boolean isTeamA) {
		if (isTeamA) A.setSlot(pos, null);
		else B.setSlot(pos, null);
	}
	
	public void onTeamVictory(boolean isTeamA) {
		isOver = true; 
		
		if (isTeamA) System.out.println("You win!");
		else System.out.println("You lose!");
	}
	
}
