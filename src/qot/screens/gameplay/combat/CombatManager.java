package qot.screens.gameplay.combat;

import envision.game.objects.entities.Entity;
import eutil.datatypes.EArrayList;

/** Keeps track of fighting entities of two teams over the course of a fight. */
public class CombatManager {
	
	// Team A is the player's team. Team B is what they're fighting
	private EArrayList<Entity> teamA, teamB;
	
	// Boolean to keep track of which team's turn it is
	private boolean isATurn = true;
	// Keeps track of which entity is currently fighting
	private int turnA = 0, turnB = 0;
	// The currently fighting entities of either team
	private Entity entityA, entityB;
	
	// True if this combat phase has completed -- check if either aWon or bWon are true. (could be a tie)
	private boolean over = false;
	// True if teamA won.
	private boolean aWon = false;
	// True if teamB won.
	private boolean bWon = false;
	
	//--------------
	// Constructors
	//--------------
	
	public CombatManager(Entity aIn, Entity bIn) { this(new EArrayList<Entity>(aIn), new EArrayList<Entity>(bIn)); }
	public CombatManager(EArrayList<Entity> aIn, EArrayList<Entity> bIn) {
		teamA = aIn;
		teamB = bIn;
	}
	
	//---------
	// Methods
	//---------
	
	public void endTurn() {
		//switch turn
		isATurn = !isATurn;
		
		//load the current fighters for each team
		entityA = getNextA();
		entityB = getNextB();
		
		//it's over if either A or B aren't alive
		over = (entityA == null || entityB == null) || !(checkAlive_A() && checkAlive_B());
		
		//if it's over, determine victor (if any)
		if (over) {
			if (checkAlive_A()) aWon = true;
			else if (checkAlive_B()) bWon = true;
		}
	}
	
	public Entity getCurA() { return entityA; }
	public Entity getCurB() { return entityB; }
	
	public boolean aAlive() { return !entityA.isDead(); }
	public boolean bAlive() { return !entityB.isDead(); }
	
	public void damageCurA(int amount) { entityA.drainHealth(amount); }
	public void drainManaCurA(int amount) { entityA.drainMana(amount); }
	
	public void damageCurB(int amount) { entityB.drainHealth(amount); }
	public void drainManaCurB(int amount) { entityB.drainMana(amount); }
	
	//----------
	// Internal
	//----------
	
	/** Returns true if there are any alive on A. */
	private boolean checkAlive_A() { for (Entity e : teamA) { if (!e.isDead()) return true; } return false; }
	/** Returns true if there are any alive on B. */
	private boolean checkAlive_B() { for (Entity e : teamB) { if (!e.isDead()) return true; } return false; }
	
	/** Returns the entity up next on team A. */
	private Entity getNextA() {
		//only care if there are any alive
		if (teamA.isNotEmpty() && checkAlive_A()) {
			Entity a;
			while ((a = teamA.get(turnA = (turnA + 1 < teamA.size() - 1) ? turnA + 1 : 0)).isDead()); //ignore dead entities
			return a;
		}
		return null;
	}
	
	/** Returns the entity up next on team B. */
	private Entity getNextB() {
		//only care if there are any alive
		if (teamB.isNotEmpty() && checkAlive_B()) {
			Entity b;
			while ((b = teamB.get(turnB = (turnB + 1 < teamB.size() - 1) ? turnB + 1 : 0)).isDead()); //ignore dead entities
			return b;
		}
		return null;
	}
	
}
