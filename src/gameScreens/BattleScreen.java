package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import entities.Entity;
import entities.player.Player;
import gameSystems.gameRenderer.GameScreen;
import gameWindows.InventoryWindow;
import main.Game;
import sound.Audio;
import sound.Songs;
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
		Songs.playSong(currentSong = Songs.darkCave).loop();
		
		oldA = entA.getDimensions();
		oldB = entB.getDimensions();
	}
	
	@Override
	public void initObjects() {
		attack = new WindowButton(this, startX + 50, endY - 350, 200, 50, "Attack");
		flame = new WindowButton(this, startX + 50, endY - 300, 200, 50, "Flame");
		heal = new WindowButton(this, startX + 50, endY - 250, 200, 50, "Heal");
		doNothing = new WindowButton(this, startX + 50, endY - 200, 200, 50, "Do Nothing");
		openInventory = new WindowButton(this, startX + 50, endY - 150, 200, 50, "Inventory");
		
		addObject(entA, entB);
		
		entA.setPosition(midX - 250 - entA.width, midY - 150 - entA.height / 2);
		entB.setPosition(midX + 250, midY - 150 - entB.height / 2);
		
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
				entA.getBackgroundStats().addKilled(1);
				endBattle();
			}
		}
		
		super.drawScreen(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		//base attack
		if (object == attack) {
			entB.hurt(entA.getDamage());
			endTurn();
		}
		
		//flame attack
		if (object == flame) {
			if (entA instanceof Player p) {
				p.useFlame(entB);
			}
			endTurn();
		}
		
		//heal spell
		if (object == heal) {
			if (entA instanceof Player p) {
				entB.hurt(p.flameDamage);
				p.useHeal();
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
	public void onClosed() {
		Songs.stopSong(currentSong);
		
		entA.setDimensions(oldA);
		entB.setDimensions(oldB);
	}
	
	@Override
	public void onWindowResize() {
		super.onWindowResize();
		reInitObjects();
	}
	
	/** Allows the enemy to fight back if they are still alive. */
	private void endTurn() {
		if (!entB.isDead()) {
			entA.hurt(entB.getDamage());
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
		if (prev instanceof TestScreen) {
			
			//close the inventory if it is open
			if (inventoryWindow != null) { inventoryWindow.close(); }
			
			Game.displayScreen(prev);
		}
	}
	
	/** Attempts to open the inventory screen. Note, Only one inventory window can be open at any time. */
	public void openInventory() {
		if (inventoryWindow == null) {
			Game.displayWindow(inventoryWindow = new InventoryWindow((Player) entA));
		}
	}
	
	/** Returns true if there is an inventory window open. */
	public boolean isInventoryOpen() {
		return Game.isEGuiOpen(InventoryWindow.class);
	}
	
}
