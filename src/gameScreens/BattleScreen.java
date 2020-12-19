package gameScreens;

import assets.entities.Entity;
import assets.entities.player.Player;
import assets.sounds.Audio;
import assets.sounds.Songs;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import gameScreens.gameplay.GamePlayScreen;
import gameScreens.gameplay.StatusBar;
import gameSystems.screenSystem.GameScreen;
import gameWindows.InventoryWindow;
import main.Game;
import util.renderUtil.EColors;
import util.storageUtil.EDimension;

public class BattleScreen extends GameScreen {
	
	/** The previous screen that was shown before this one. */
	GameScreen prev;
	/** The song that is currently playing. */
	Audio currentSong;
	Entity entA, entB;
	
	EDimension oldA, oldB;
	
	WindowButton attack, flame, heal, doNothing, openInventory;
	StatusBar health, mana;
	
	String lastMessage = "Prepare to fight!";
	InventoryWindow inventoryWindow = null;
	
	public BattleScreen(GameScreen previous, Entity entAIn, Entity entBIn) {
		super();
		prev = previous;
		entA = entAIn;
		entB = entBIn;
	}
	
	@Override
	public void initScreen() {
		Songs.loop(currentSong = Songs.darkCave);
		
		//oldA = entA.getDimensions();
		//oldB = entB.getDimensions();
		
		setObjectName("Battle Screen");
	}
	
	@Override
	public void initObjects() {
		health = new StatusBar(this, 5, 5, 200, 30, 0, Game.getPlayer().getMaxHealth(), EColors.red);
		health.setCurrentValue(Game.getPlayer().getHealth());
		addObject(health);
		
		mana = new StatusBar(this, health.endX + 5, 5, 200, 30, 0, Game.getPlayer().getMaxMana(), EColors.blue);
		mana.setCurrentValue(Game.getPlayer().getMana());
		addObject(mana);
		
		attack = new WindowButton(this, startX + 50, endY - 350, 200, 50, "Attack");
		flame = new WindowButton(this, startX + 50, endY - 300, 200, 50, "Flame");
		heal = new WindowButton(this, startX + 50, endY - 250, 200, 50, "Heal");
		doNothing = new WindowButton(this, startX + 50, endY - 200, 200, 50, "Do Nothing");
		openInventory = new WindowButton(this, startX + 50, endY - 150, 200, 50, "Inventory");
		
		//addObject(entA, entB);
		
		//entA.setPosition(midX - 250 - entA.width, midY - 150 - entA.height / 2);
		//entB.setPosition(midX + 250, midY - 150 - entB.height / 2);
		
		addObject(attack, flame, heal, doNothing, openInventory);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.vdgray);
		
		if (!isInventoryOpen()) { inventoryWindow = null; }
		
		drawStringC(lastMessage, midX, 20);
		
		drawStats(entA);
		drawStats(entB);
		
		if (shouldBattleEnd()) {
			if (entA.isDead()) {
				Game.displayScreen(new GameOverScreen((entA instanceof Player p) ? p : null));
			}
			else {
				//entA.getStats().addKilled(1);
				endBattle();
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		//base attack
		if (object == attack) {
			double damage = entA.getDamage();
			entB.hurt(damage);
			endTurn();
		}
		
		//flame attack
		if (object == flame) {
			if (entA instanceof Player p) {
				double amount = p.flameCost;
				p.useFlame(entB);
				mana.decrementVal(amount);
			}
			endTurn();
		}
		
		//heal spell
		if (object == heal) {
			if (entA instanceof Player p) {
				double amount = p.flameCost;
				entB.hurt(p.flameDamage);
				p.useHeal();
				mana.decrementVal(amount);
				health.incrementVal(p.healAmount);
			}
			endTurn();
		}
		
		//lol
		if (object == doNothing) {
			endTurn();
		}
		
		//open inventory
		if (object == openInventory) {
			openInventory();
		}
	}

	@Override
	public void onScreenClosed() {
		Songs.stopSong(currentSong);
		
		//entA.setDimensions(oldA);
		//entB.setDimensions(oldB);
	}
	
	@Override
	public void onWindowResize() {
		super.onWindowResize();
		reInitObjects();
	}
	
	/** Allows the enemy to fight back if they are still alive. */
	private void endTurn() {
		if (!entB.isDead()) {
			double damage = entB.getDamage();
			entA.hurt(damage);
			health.decrementVal(damage);
		}
	}
	
	/** Returns true if either entity in the fight has died. */
	private boolean shouldBattleEnd() {
		return entA.isDead() || entB.isDead();
	}
	
	/** Displays info about the current entity in the fight above their head. */
	private void drawStats(Entity in) {
		drawStringC(in.getName(), in.midX, in.startY - 69);
		drawStringC("HP: " + in.getHealth() + "  MP: " + in.getMana(), in.midX, in.startY - 30);
	}
	
	/** Stops the battle and returns to the game world. */
	public void endBattle() {
		if (prev instanceof GamePlayScreen) {
			
			//close the inventory if it is open
			if (inventoryWindow != null) { inventoryWindow.close(); }
			
			Game.displayScreen(prev);
			Songs.stopSong(currentSong);
		}
	}
	
	/** Attempts to open the inventory screen. Note, Only one inventory window can be open at any time. */
	public void openInventory() {
		if (inventoryWindow == null) {
			displayWindow(inventoryWindow = new InventoryWindow((Player) entA));
		}
	}
	
	/** Returns true if there is an inventory window open. */
	public boolean isInventoryOpen() {
		return isWindowOpen(InventoryWindow.class);
	}
	
}
