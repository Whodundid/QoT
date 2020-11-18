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
	
	GameScreen prev;
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
		
		entA.setPosition(midX - 150 - entA.width, midY - 150 - entA.height / 2);
		entB.setPosition(midX + 150, midY - 150 - entB.height / 2);
		
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
				Game.displayScreen(new GameOverScreen());
			}
			else {
				endBattle();
			}
		}
		
		super.drawScreen(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == attack) {
			entB.hurt(entA.getDamage());
			endTurn();
		}
		
		if (object == flame) {
			if (entA instanceof Player) {
				((Player) entA).useFlame(entB);
			}
			endTurn();
		}
		
		if (object == heal) {
			if (entA instanceof Player) {
				entB.hurt(((Player) entA).flameDamage);
				((Player) entA).useHeal();
			}
			endTurn();
		}
		
		if (object == doNothing) {
			endTurn();
		}
		
		if (object == openInventory) {
			if (inventoryWindow == null) {
				openInventory();
			}
		}
	}
	
	private boolean shouldBattleEnd() {
		return entA.isDead() || entB.isDead();
	}
	
	private void endTurn() {
		if (!entB.isDead()) {
			entA.hurt(entB.getDamage());
		}
	}
	
	private void drawStats(Entity in) {
		drawStringC("HP: " + in.getHealth() + "  MP: " + in.getMana(), in.midX, in.startY - 30);
	}
	
	public void endBattle() {
		if (prev instanceof TestScreen) {
			Game.displayScreen(prev);
		}
	}
	
	public void openInventory() {
		Game.displayWindow(inventoryWindow = new InventoryWindow((Player) entA));
	}
	
	public boolean isInventoryOpen() {
		return Game.isEGuiOpen(InventoryWindow.class);
	}
	
	@Override
	public void onScreenClosed() {
		Songs.stopSong(currentSong);
		
		entA.setDimensions(oldA);
		entB.setDimensions(oldB);
	}
	
	@Override
	public void onWindowResize() {
		super.onWindowResize();
		reInitObjects();
	}
	
}
