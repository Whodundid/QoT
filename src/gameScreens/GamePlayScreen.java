package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import entities.Entity;
import entities.enemy.types.Goblin;
import entities.enemy.types.Thyrah;
import entities.enemy.types.TrollBoar;
import entities.enemy.types.Whodundid;
import entities.player.Player;
import gameSystems.gameRenderer.GameScreen;
import gameSystems.textureSystem.GameTexture;
import gameTextures.Textures;
import input.Keyboard;
import main.Game;
import screenParts.StatusBar;

import org.lwjgl.glfw.GLFW;

import sound.Songs;
import util.mathUtil.Direction;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;
import util.storageUtil.EDimension;

public class GamePlayScreen extends GameScreen {
	
	Player mainCharacter;
	WindowButton damagePlayer, rebuildMap;
	GameTexture[][] mapTiles;
	StatusBar health, mana;
	
	
	EArrayList<Entity> monsters = new EArrayList();
	
	long time = 0;
	int stage = 0;
	
	@Override
	public void initScreen() {
		mainCharacter = Game.getPlayer();
		
		Songs.loop(Songs.battleTheme);
		
		buildMap();
		setObjectName("Game Screen");
	}
	
	@Override
	public void initObjects() {
		windowObjects.clear();
		
		health = new StatusBar(this, 5, Game.getHeight() - 90, 200, 55, 0, mainCharacter.getMaxHealth(), EColors.green);
		health.setCurrentValue(mainCharacter.getHealth());
		addObject(health);
		
		mana = new StatusBar(this, health.endX + 5, Game.getHeight() - 90, 200, 55, 0, mainCharacter.getMaxMana(), EColors.blue);
		mana.setCurrentValue(mainCharacter.getMana());
		addObject(mana);
		
		
		if (Game.getPlayer() == null) {
			Game.setPlayer(mainCharacter = new Player("The Player", Game.getWidth() / 2, Game.getHeight() / 2));
		}
		else {
			mainCharacter.setPosition(Game.getWidth() / 2, Game.getHeight() / 2);
		}
		
		addObject(mainCharacter);
		
		//damagePlayer = new WindowButton(this, 5, Game.getHeight() - 150, 150, 55, "Damage");
		//rebuildMap = new WindowButton(this, damagePlayer.endX + 20, Game.getHeight() - 150, 100, 55, "Map");
		
		//addObject(damagePlayer, rebuildMap);
		
		randomMonsters(1);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		
		if (mainCharacter.isDead()) {
			Game.displayScreen(new GameOverScreen());
		}
		
		if (System.currentTimeMillis() - time >= 2000) {
			time = System.currentTimeMillis();
			if (stage < 4) {
				growTiles();
			}
			else {
				//buildMap();
				stage = 0;
			}
			stage++;
		}
		
		//draw tiles
		for (int i = 0; i < mapTiles.length; i++) {
			for (int j = 0; j < mapTiles[0].length; j++) {
				int offset = (i % 2 == 1) ? 64 : 0;
				
				if (i % 2 == 0 || j < mapTiles[0].length - 1) {
					drawTexture(-64 + (j * 128) + offset, -32 + (i * 32), 128, 64, mapTiles[i][j]);
				}
			}
		}
		
		drawString("HP: " + mainCharacter.getHealth() + "   MP: " + mainCharacter.getMana(), 10, 10);
		//drawString("FPS: " + Game.getFPS(), 10, 50);
		drawString("Enemies Killed: " + mainCharacter.getBackgroundStats().getEnemiesKilled(), 10, 100);
		
		for (Entity e : monsters) {
			Direction d = NumUtil.randomDir();
			e.move(d);
		}
		
		if (Keyboard.isWDown()) { mainCharacter.move(0, -1); }
		if (Keyboard.isADown()) { mainCharacter.move(-1, 0); }
		if (Keyboard.isSDown()) { mainCharacter.move(0, 1); }
		if (Keyboard.isDDown()) { mainCharacter.move(1, 0); }
		
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			Game.stopGame();
		}
		
		checkIfBattle();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == damagePlayer) {
			if (!mainCharacter.isDead()) {
				mainCharacter.hurt(5);
			}
		}
		
		if (object == rebuildMap) {
			buildMap();
		}
	}
	
	@Override
	public void onScreenClosed() {
		Songs.stopAllMusic();
	}
	
	private void checkIfBattle() {
		
		//check if the player is touching any of the other monster's hitboxes
		for (Entity e : monsters) {
			if (mainCharacter.getDimensions().contains(e.getDimensions())) {
				Game.displayScreen(new BattleScreen(this, mainCharacter, e));
			}
		}
		
	}
	
	private void randomMonsters(int number) {
		
		//remove old
		for (Entity e : monsters) {
			removeObject(e);
		}
		monsters.clear();
		
		//make new ones
		for (int i = 0; i < number; i++) {
			int posX, posY;
			EDimension d;
			
			do {
				posX = NumUtil.getRoll(150, Game.getWidth() - 150);
				posY = NumUtil.getRoll(150, Game.getHeight() - 150);
				d = new EDimension(posX, posY, posX + 150, posY + 150);
			}
			while (mainCharacter.getDimensions().contains(d));
			
			int type = NumUtil.getRoll(0, 3);
			Entity e = null;
			
			switch (type) {
			case 0: e = new Goblin(posX, posY); break;
			case 1: e = new Whodundid(posX, posY); break;
			case 2: e = new TrollBoar(posX, posY); break;
			case 3: e = new Thyrah(posX, posY); break;
			//case 2: e = new WhodundidsBrother(posX, posY); break;
			}
			
			monsters.add(e);
			//g.setDrawHitbox(true);
			
			addObject(e);
		}
		
		removeObject(mainCharacter);
		addObject(mainCharacter);
	}
	
	private void buildMap() {
		mapTiles = new GameTexture[Game.getHeight() / 32 + 2][Game.getWidth() / 128 + 2];
		
		for (int i = 0; i < mapTiles.length; i++) {
			for (int j = 0; j < mapTiles[0].length; j++) {
				GameTexture t = null;
				int rand = (int) (Math.random() * 100);
				
				t = Textures.farmland_1;
				
				if (rand >= 30) {
					switch (NumUtil.getRoll(0, 2)) {
					case 0: t = Textures.dirt_1; break;
					case 1: t = Textures.dirt_2; break;
					case 2: t = Textures.dirt_3; break;
					}
				}
				else if (rand >= 25) {
					switch (NumUtil.getRoll(0, 2)) {
					case 0: t = Textures.dirt_grass_1; break;
					case 1: t = Textures.dirt_grass_2; break;
					case 2: t = Textures.dirt_grass_3; break;
					}
				}
				else if (rand >= 0) {
					int intVal = NumUtil.getRoll(0, 5);
					switch (intVal) {
					case 0: t = Textures.stump_1; break;
					case 1: t = Textures.rocks_1; break;
					case 2: t = Textures.farmland_1; break;
					case 3: t = Textures.potatoes_1_seed; break;
					case 4: t = Textures.potatoes_1_grow; break;
					case 5: t = Textures.potatoes_1_done; break;
					}
				}
				
				if (i % 2 == 0 || j < mapTiles[0].length - 1) {
					mapTiles[i][j] = t;
				}
			}
		}
	}
	
	private void growTiles() {
		for (int i = 0; i < mapTiles.length; i++) {
			for (int j = 0; j < mapTiles[0].length; j++) {
				GameTexture t = mapTiles[i][j];
				
				if (t != null) {
					if (t == Textures.farmland_1) { mapTiles[i][j] = Textures.potatoes_1_seed; } //farm -> seed
					else if (t == Textures.potatoes_1_seed) { mapTiles[i][j] = Textures.potatoes_1_grow; } //seed -> grow
					else if (t == Textures.potatoes_1_grow) { mapTiles[i][j] = Textures.potatoes_1_done; } //grow -> done
				}
				
			}
		}
	}
	
	@Override
	public void onWindowResize() {
		reInitObjects();
		buildMap();
	}
	
}
