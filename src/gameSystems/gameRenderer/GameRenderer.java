package gameSystems.gameRenderer;

import eWindow.windowTypes.TopWindowParent;
import entities.Entity;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.worldTiles.WorldTile;
import input.Mouse;
import main.Game;

//Author: Hunter Bragg

public class GameRenderer extends TopWindowParent {
	
	public static GameRenderer instance;
	private GameWorld world;
	int distX = 15;
	int distY = 10;
	int tileWidth, tileHeight;
	int pixelWidth, pixelHeight;
	int x, y, w, h;
	private double zoomVal = 1.25;
	
	public static GameRenderer getInstance() {
		return instance == null ? instance = new GameRenderer() : instance;
	}
	
	private GameRenderer() {
		res = Game.getWindowSize();
		initObjects();
	}
	
	public void onRenderTick() {
		if (world != null && world.isFileLoaded()) { renderWorld(); }
		drawObject(Mouse.getMx(), Mouse.getMy());
	}
	
	private void renderWorld() {
		renderMap();
		renderEntities();
	}
	
	private void renderMap() {
		//int xPos = Game.thePlayer.midX;
		//int yPos = game.thePlayer.midY;
		
		int xPos = 0;
		int yPos = 0;
		
		xPos = (int) 0;
		yPos = (int) 0;
		
		//if (xPos < distX) { xPos = distX; }
		//if (yPos < distY) { yPos = distY; }
		//System.out.println(distX + " " + distY);
		
		WorldTile[][] tiles = world.getTilesAroundPoint(xPos, yPos, distX, distY);
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				WorldTile t = tiles[i][j];
				if (t != null) {
					
					int drawPosX = x;
					int drawPosY = y;
					
					//if (xPos < distX) { drawPosX += (distX - xPos) * w; }
					//if (yPos < distY) { drawPosY += (distY - yPos) * h; }
					
					//System.out.println(drawPosX + " " + drawPosY);
					
					if (t.hasTexture()) {
						drawTexture(drawPosX + (i * w), drawPosY + (j * h), w, h, t.getTexture());
					}
				}
			}
		}
		
		drawString(xPos + " " + yPos, 10, 40);
	}
	
	private void renderEntities() {
		for (Entity e : world.getEntitiesInWorld()) {
			
		}
	}

	@Override public void close() { System.out.println("FOOL! Dagoth Ur cannot be closed, I am a god!"); }
	
	
	
	public GameRenderer setWorld(GameWorld worldIn) {
		world = worldIn;
		onWindowResized();
		return this;
	}
	
	@Override
	public void onWindowResized() {
		res = Game.getWindowSize();
		setDimensions(0, 0, res.getWidth(), res.getHeight());
		
		if (world != null) {
			tileWidth = world.getTileWidth();
			tileHeight = world.getTileHeight();
			
			distX = (int) 5;
			distY = (int) 5;
			
			w = (int) (world.getTileWidth() * zoomVal);
			h = (int) (world.getTileHeight() * zoomVal);
			x = (int) midX - (distX / 2 * w) - w;
			y = (int) midY - (distY / 2 * h) - h;
		}
		
	}
	
}
