package gameScreens;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import entities.enemy.Enemy;
import entities.enemy.types.Goblin;
import entities.player.Player;
import gameSystems.gameRenderer.GameScreen;
import gameSystems.textureSystem.GameTexture;
import gameTextures.Textures;
import input.Keyboard;
import main.Game;
import org.lwjgl.glfw.GLFW;
import sound.Songs;
import util.mathUtil.NumUtil;
import util.storageUtil.EArrayList;

public class TestScreen extends GameScreen {
	
	Player mainCharacter;
	WindowButton damagePlayer, rebuildMap;
	GameTexture[][] mapTiles;
	
	EArrayList<Enemy> monsters = new EArrayList();
	
	float xf = 0;
	float yf = 0;
	
	long time = 0;
	int stage = 0;
	
	public TestScreen(Player playerIn) {
		mainCharacter = playerIn;
	}
	
	@Override
	public void initScreen() {
		Songs.playSong(Songs.theme);
		buildMap();
	}
	
	@Override
	public void initObjects() {
		if (mainCharacter == null) {
			mainCharacter = new Player("The Guy", Game.getWidth() / 2, Game.getHeight() / 2);
		}
		
		//mainCharacter.setDrawHitbox(true);
		mainCharacter.setPosition(Game.getWidth() / 2, Game.getHeight() / 2);
		
		addObject(mainCharacter);
		
		damagePlayer = new WindowButton(this, 5, Game.getHeight() - 60, 100, 55, "Damage");
		rebuildMap = new WindowButton(this, damagePlayer.endX + 20, Game.getHeight() - 60, 100, 55, "Map");
		
		IActionObject.setActionReceiver(this, damagePlayer, rebuildMap);
		
		addObject(damagePlayer, rebuildMap);
		
		randomMonsters(1);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		super.drawScreen(mXIn, mYIn);
		
		if (System.currentTimeMillis() - time >= 5000) {
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
		
		for (int i = 0; i < mapTiles.length; i++) {
			for (int j = 0; j < mapTiles[0].length; j++) {
				int offset = (i % 2 == 1) ? 64 : 0;
				
				if (i % 2 == 0 || j < mapTiles[0].length - 1) {
					//draw tiles
					drawTexture(-64 + (j * 128) + offset, 32 + (i * 32), 128, 64, mapTiles[i][j]);
				}
			}
		}
		
		drawString("FPS: " + Game.getFPS(), 50, 10);
		
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
		Songs.stopSong(Songs.theme);
	}
	
	private void checkIfBattle() {
		
		//check if the player is touching any of the other monster's hitboxes
		for (Enemy e : monsters) {
			if (mainCharacter.getDimensions().contains(e.getDimensions())) {
				
				System.out.println("TOUCHING: " + e);
				Game.displayScreen(new BattleScreen(this, mainCharacter, e));
			}
		}
		
	}
	
	private void randomMonsters(int number) {
		
		//remove old
		for (Enemy e : monsters) {
			removeObject(e);
		}
		monsters.clear();
		
		//make new ones
		for (int i = 0; i < number; i++) {
			int posX = NumUtil.getRoll(150, Game.getWidth() - 150);
			int posY = NumUtil.getRoll(150, Game.getHeight() - 150);
			
			Goblin g = new Goblin(posX, posY);
			monsters.add(g);
			//g.setDrawHitbox(true);
			
			addObject(g);
		}
		
		removeObject(mainCharacter);
		addObject(mainCharacter);
	}
	
	private void buildMap() {
		mapTiles = new GameTexture[Game.getWidth() / 64][Game.getHeight() / 32];
		
		for (int i = 0; i < mapTiles.length; i++) {
			for (int j = 0; j < mapTiles[0].length; j++) {
				GameTexture t = null;
				int rand = (int) (Math.random() * 100);
				
				t = Textures.farmland_1;
				
				
				if (rand >= 20) {
					switch (NumUtil.getRoll(0, 2)) {
					case 0: t = Textures.dirt_1; break;
					case 1: t = Textures.dirt_2; break;
					case 2: t = Textures.dirt_3; break;
					}
				}
				else if (rand >= 10) {
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
		buildMap();
	}
	
}
