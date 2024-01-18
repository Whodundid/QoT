package envision.game.world.worldEditor;

import static envision.engine.inputHandlers.Keyboard.*;

import java.io.File;
import java.util.Collection;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.assets.EditorTextures;
import envision.engine.events.GameEvent;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.screens.GameScreen;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.component.ComponentType;
import envision.game.effects.sounds.SoundEngine;
import envision.game.entities.Entity;
import envision.game.entities.EntityRenderer;
import envision.game.manager.LevelManager;
import envision.game.util.InsertionSort;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import envision.game.world.WorldCamera;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.regionTool.RegionSidePanel;
import envision.game.world.worldEditor.editorParts.toolBox.EditorToolBox;
import envision.game.world.worldEditor.editorParts.topHeader.EditorScreenBotHeader;
import envision.game.world.worldEditor.editorParts.topHeader.EditorScreenTopHeader;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorToolType;
import envision.game.world.worldEditor.editorTools.ToolHandler;
import envision.game.world.worldTiles.VoidTile;
import envision.game.world.worldTiles.WorldTile;
import envision.game.world.worldTiles.WorldTileRenderer;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;

public class MapEditorScreen extends GameScreen {

	//================
	// Fields : World
	//================
	
	protected File mapFile;
	
	/** The actual game world (used to load from and save data to) */
	protected GameWorld actualWorld;
	
	/** The world as it exists in the map editor */
	private EditorWorld editorWorld;
	
	//=================
	// Fields : Editor
	//=================
	
	MapEditorSettings settings;
	ToolHandler toolHandler;
	
	EditorToolBox toolBox;
	EditorScreenTopHeader topHeader;
	EditorScreenBotHeader botHeader;
	EditorSidePanel sidePanel;
	
	public boolean firstPress = false;
	public boolean mouseInMap = false;
	public boolean mouseOver = false;
	public boolean drawingMousePos = false;
	
	/** the world coordinates at the center of the screen. */
	public int midDrawX, midDrawY;
	/** the width and height of the world that the renderer will draw out from the mid. */
	public int drawDistX = 20, drawDistY = 15;
	/** the world coordinates under the mouse. */
	public int mouseWorldX, mouseWorldY;
	/** The world pixel coordinates under the mouse. */
	public int mouseWorldPX, mouseWorldPY;
	public int oldWorldX, oldWorldY;
	public boolean updateMap = false;
	
	private boolean recentlySaved = false;
	private long saveStringTimeout = 0l;
	private String saveString = "Saved!";
	private EColors saveStringColor = EColors.lgreen;
	private boolean loading = false;
	private boolean firstDraw = false;
	private boolean hasBeenModified = false;
	private boolean hasSaved = false;
	private boolean failed = false;
	private Exception failedException;
	private WindowDialogueBox exitSaverBox = null;
	private boolean cancelExitClick = false;
	
	private final EList<EditorObject> selectedObjects = EList.newList();
	
	public WorldCamera camera;
	
	//---------------------
	public int left;
	public int top;
	public int right;
	public int bot;
	/** draw width */
	public int dw;
	/** draw height */
	public int dh;
	
	public double tileDrawWidth; //pixel width of each tile
	public double tileDrawHeight; //pixel height of each tile
	public double drawAreaMidX; //the middle x of the map draw area
	public double drawAreaMidY; //the middle y of the map draw area
	public double mapDrawStartX; //the left most x coordinate for map drawing
	public double mapDrawStartY; //the top most y coordinate for map drawing
	
	public double x = mapDrawStartX;
	public double y = mapDrawStartY;
	public double w = tileDrawWidth;
	public double h = tileDrawHeight;
	//----------------------
	
	public long timeSinceKey = 0l;
	public int tileWidth;
	public int tileHeight;
	
	public int curLayer = 0;
	
	//==============
    // Constructors
    //==============
	
	public MapEditorScreen(File mapFileIn) {
		mapFile = mapFileIn;
		setDefaultDims();
	}
	
	public MapEditorScreen(GameWorld worldIn) {
		actualWorld = worldIn;
		setDefaultDims();
	}
	
	//===========
    // Overrides
    //===========
	
	@Override
	public void initScreen() {
		settings = new MapEditorSettings(this);
		toolHandler = new ToolHandler(this);
		SoundEngine.stopAll();
	}
	
	@Override
	public void initChildren() {
		addObject(topHeader = new EditorScreenTopHeader(this));
		addObject(toolBox = new EditorToolBox(this));
		addObject(sidePanel = new EditorSidePanel(this));
		addObject(botHeader = new EditorScreenBotHeader(this));
		
		sidePanel.setCurrentPanel(SidePanelType.TERRAIN);
		updateDrawDist();
	}
	
	@Override
	public void onPostInit() {
	    firstPress = !Mouse.isButtonDown(0);
        
        loading = true;
        Runnable loader = this::loadWorld;
        new Thread(loader).start();
        
        settings.currentTool = EditorToolType.PENCIL;
	}
	
    @Override
    public void drawScreen(float dt, int mXIn, int mYIn) {
        drawRect(EColors.vdgray);
        mouseOver = isMouseOver();
        
        if (loading) {
            drawStringC("Loading...!", midX, midY);
            return;
        }
        
        if (failed) {
            loading = false;
            drawStringC("Failed to load!", drawAreaMidX, drawAreaMidY);
            
            if (failedException != null) {
                drawString(failedException, 100, drawAreaMidY + 40, EColors.lred);
                
                var st = failedException.getStackTrace();
                for (int i = 0; i < st.length && i < 10; i++) {
                    drawString(failedException.getStackTrace()[i], 100, drawAreaMidY + 60 + (i * 20), EColors.lred);
                }
            }
        }
        else if (actualWorld == null || !actualWorld.isFileLoaded()) {
            drawStringC("Failed to load!", midX, midY);
            failed = true;
        }
        else {
            if (!firstDraw) {
                firstDraw = true;
                updateMinimap();
            }
            
            try {
                drawEditor(dt, mXIn, mYIn);
            }
            catch (Exception e) {
                e.printStackTrace();
                failed = true;
                failedException = e;
            }
        }
        
        if (exitSaverBox != null) {
            if (containsObject(exitSaverBox)) drawRect(EColors.vdgray.opacity(80));
            else exitSaverBox = null;
        }
        
//        		int sx = 0;
//              int sy = (int) topHeader.endY;
//              int ex = (int) (Envision.getWidth() - sidePanel.width);
//              int ey = (int) (Envision.getHeight() - botHeader.height);
//              int mx = sx + (ex - sx) / 2;
//              int my = sy + (ey - sy) / 2;
//        		drawRect(mx, sy, mx + 1, ey, EColors.green);
//        		drawRect(sx, my, ex, my + 1, EColors.green);
    }
	
	private void drawEditor(float dt, int mXIn, int mYIn) {
		tileDrawWidth = (editorWorld.getTileWidth() * Envision.levelManager.getCameraZoom()); //pixel width of each tile
		tileDrawHeight = (editorWorld.getTileHeight() * Envision.levelManager.getCameraZoom()); //pixel height of each tile
		drawAreaMidX = (Envision.getWidth() - sidePanel.width) / 2; //the middle x of the map draw area
		drawAreaMidY = topHeader.endY + (Envision.getHeight() - topHeader.height) / 2; //the middle y of the map draw area
		mapDrawStartX = (drawAreaMidX - (drawDistX * tileDrawWidth) - (tileDrawWidth / 2)); //the left most x coordinate for map drawing
		mapDrawStartY = (drawAreaMidY - (drawDistY * tileDrawHeight) - (tileDrawHeight / 2)); //the top most y coordinate for map drawing
		
		//converting to shorthand to reduce footprint
		x = mapDrawStartX;
		y = mapDrawStartY;
		w = tileDrawWidth;
		h = tileDrawHeight;
		
		camera.onRenderTick(dt);
		drawMap();
		if (settings.drawTileGrid) drawTileGrid();
		if (settings.drawEntities) renderEntities();
		if (settings.drawRegions) drawRegions();
		if (settings.drawCenterPositionBox) drawCenterPositionBox();
		
		if (!DeveloperDesktop.isOpen()) updateMousePos();
		drawingMousePos = mouseInMap;

		
		final var fh = FontRenderer.FONT_HEIGHT; // font height
		final var bot = (botHeader != null) ? botHeader.startY : 0;
		
		//displays the world coordinates of the hovered tile
		drawString("World: [" + mouseWorldX + "," + mouseWorldY + "]", 5, bot - fh * 4);
		//displays the world tile (world) coordinates directly at the middle of the screen
		drawString("Px: [" + mouseWorldPX + "," + mouseWorldPY + "]", 5, bot - fh * 3);
		//displays the world tile (world) coordinates directly at the middle of the screen
		drawString("Mid: [" + midDrawX + "," + midDrawY + "]", 5, bot - fh * 2);
		//displays the number of tiles that the renderer with draw out from the mid in x and y directions
		drawString("Dist: [" + drawDistX + "," + drawDistY + "]", 5, bot - fh);
		
        if (settings.drawMapBorders) drawMapBorders();
        if (recentlySaved) updateSaveString(drawAreaMidX, drawAreaMidY);
        if (exitSaverBox == null) {
            if (mouseInMap) {
                if (getCurrentSidePanel() == SidePanelType.TERRAIN) drawHoveredTileBox();
                toolHandler.drawCurrentTool(x, y, w, h);
            }
        }
		
		drawPlayerSpawn();
		
		drawSelectedEntities();
		
		oldWorldX = midDrawX;
		oldWorldY = midDrawY;
	}
	
	@Override
	public void mouseScrolled(int change) {
		double c = Math.signum(change);
		double z = 1.0;
		double zoom = Envision.levelManager.getCameraZoom();
		
		if (exitSaverBox != null) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		
		if (Keyboard.isCtrlDown()) {
			if (c > 0 && zoom == 0.25)     z = 0.05;     // if at 0.25 and zooming out -- 0.05x
			else if (zoom < 1.0)           z = c * 0.05; // if less than 1 zoom by 0.05x
			else if (zoom < 2.0)           z = c * 0.1;  // if less than 2 zoom by 0.1x
			else if (c > 0 && zoom == 1.0) z = c * 0.05; // if at 1.0 and zooming in -- 0.05x
			else if (c < 0 && zoom == 2.0) z = c * 0.1;  // if at 2.0 and zooming in -- 0.1x
			else                           z = c * 0.25; // otherwise always zoom by 0.25x
			
			z = ENumUtil.round(zoom + z, 2);
			camera.setZoom(z);
			
			updateDrawDist();
		}
		else if (Keyboard.isShiftDown()) camera.moveCameraByCoords(c * 2, 0);
		else if (Keyboard.isAltDown()) camera.moveCameraByCoords(0, -c * 2);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (exitSaverBox != null && !exitSaverBox.isMouseInside()) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		
		if (getTopParent().getModifyingObject() == null) {
			if (mouseInMap || settings.getCurrentTool() == EditorToolType.SELECTOR) {
				toolHandler.handleToolPress(button);
			}
		}
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		
		if (exitSaverBox != null && !exitSaverBox.isMouseInside()) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		else {
			cancelExitClick = false;
		}
		
		if (!firstPress && button == 0) {
			firstPress = true;
		}
		
		if (mouseInMap && getTopParent().getModifyingObject() == null) {
			toolHandler.handleToolRelease(button);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (exitSaverBox != null) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) camera.moveCameraByCoords(0, -1);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) camera.moveCameraByCoords(-1, 0);
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) camera.moveCameraByCoords(0, 1);
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) camera.moveCameraByCoords(1, 0);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_O)) DebugSettings.drawEntityHitboxes = !DebugSettings.drawEntityHitboxes;
        if (Keyboard.isKeyDown(Keyboard.KEY_H)) DebugSettings.drawEntityCollisionBoxes = !DebugSettings.drawEntityCollisionBoxes;
		
        if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
            settings.lockToTileGrid = !settings.lockToTileGrid;
            botHeader.update();
        }
        
		if (mouseOver) {
			if (Keyboard.isCtrlS(keyCode)) saveWorld();
			if (Keyboard.isCtrlR(keyCode)) {
				loadWorld();
				saveString = "Reloaded!";
				saveStringColor = EColors.lgreen;
				recentlySaved = true;
				saveStringTimeout = System.currentTimeMillis();
			}
			
			// select everything
			if (Keyboard.isCtrlA(keyCode)) {
				selectedObjects.addAll(editorWorld.getEditorEntities());
			}
			
			// deselect everything
			if (Keyboard.isCtrlDown() && Keyboard.isKeyDown(Keyboard.KEY_D)) {
				selectedObjects.clear();
			}
			
			if (keyCode == Keyboard.KEY_ESC) {
				boolean isAsset = getSidePanel().getCurrentPanelType() == SidePanelType.ASSET;
				
				if (isAsset && getSettings().getCurrentTool() == EditorToolType.PLACE) {
					getSettings().setCurrentTool(EditorToolType.SELECTOR);
				}
				else if (!hasSaved && hasBeenModified) {
					if (exitSaverBox == null || !containsObject(exitSaverBox)) {
						exitSaverBox = new WindowDialogueBox(this, WindowDialogueBox.DialogBoxTypes.YES_NO);
						exitSaverBox.setTitle("World not saved!");
						exitSaverBox.setMessage("Exit without saving?");
						exitSaverBox.setMinimizable(false);
						exitSaverBox.setActionReceiver(this);
						setFocusLockObject(exitSaverBox);
						displayWindow(exitSaverBox);
						cancelExitClick = true;
					}
				}
				else {
					if (!screenHistory.isEmpty()) closeScreen();
				}
			}
			
			if (keyCode == Keyboard.KEY_DELETE) {
				for (var e : selectedObjects) {
					editorWorld.getEditorEntities().remove(e);
				}
				selectedObjects.clear();
			}
		}
		else if (keyCode == Keyboard.KEY_DELETE) {
			for (var e : selectedObjects) {
				editorWorld.getEditorEntities().remove(e);
			}
			selectedObjects.clear();
		}
		else super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == exitSaverBox && args[0] instanceof String action) {
		    if (action.equals("y")) closeScreen();
            else exitSaverBox.close();
		}
	}
	
	@Override
	public void onGameTick(float partialTicks) {
	    if (DeveloperDesktop.isOpen()) return;
	    
	    updateMovement();
		if (exitSaverBox == null && !cancelExitClick) {
			toolHandler.handleToolUpdate();
		}
	}
	
	@Override
	public void onEvent(GameEvent e) {
		System.out.println("GAME EVENT: " + e);
	}
	
	@Override
	public void onScreenClosed() {
		SoundEngine.stopAll();
	}
	
	@Override
	public void onScreenResized() {
	    super.onScreenResized();
	    
        int sx = 0;
        int sy = (int) topHeader.endY;
        int ex = (int) (Envision.getWidth() - sidePanel.width);
        int ey = (int) (Envision.getHeight() - botHeader.height);
        camera.setDrawableAreaDimensions(sx, sy, ex, ey);
	    
	    updateMinimap();
	}
	
	//========================
	// Private Editor Methods
	//========================
	
	private void updateMovement() {
	    if (camera == null) return;
		if (System.currentTimeMillis() - timeSinceKey < 37) return;
		
		if (exitSaverBox != null && Keyboard.isAnyKeyDown(KEY_W, KEY_UP, KEY_A, KEY_LEFT, KEY_S, KEY_DOWN, KEY_D, KEY_RIGHT)) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		
		if (!Keyboard.isCtrlDown()) {
			if (Keyboard.isWDown()) camera.moveCameraByCoords(0, -1);
			if (Keyboard.isADown()) camera.moveCameraByCoords(-1, 0);
			if (Keyboard.isSDown()) camera.moveCameraByCoords(0, 1);
			if (Keyboard.isDDown()) camera.moveCameraByCoords(1, 0);
			
			timeSinceKey = System.currentTimeMillis();
		}
	}
	
	/** Draws the world tiles of the map. */
	private void drawMap() {
	    final int midX = (int) camera.getCameraCenterX();
	    final int midY = (int) camera.getCameraCenterY();
	    
	    left = ENumUtil.clamp(midX - drawDistX, 0, editorWorld.getWidth());
        top = ENumUtil.clamp(midY - drawDistY, 0, editorWorld.getHeight());
        right = ENumUtil.clamp(midX + drawDistX - 1, left, editorWorld.getWidth());
        bot = ENumUtil.clamp(midY + drawDistY - 1, top, editorWorld.getHeight());
        dw = right - left; //draw width
        dh = bot - top; //draw height
        
        for (int i = 0; i <= curLayer && i < editorWorld.worldLayers.size(); i++) {
            for (int y = top; y < bot; y++) {
                for (int x = left; x < right; x++) {
                    WorldTile t = editorWorld.getTileAt(i, x, y);
                    if (t == null) continue;
                    WorldTileRenderer renderer = t.getComponent(ComponentType.RENDERING);
                    if (renderer == null) continue;
                    renderer.draw(editorWorld, camera);
                }
            }
        }
	}
	
	private void drawTileGrid() {
	    final double constSX = camera.calculateDrawX(0);
	    final double constSY = camera.calculateDrawY(0);
        final double constEX = camera.calculateDrawX(editorWorld.getWidth() * tileDrawWidth);
	    final double constEY = camera.calculateDrawY(editorWorld.getHeight() * tileDrawHeight);
	    final int tw = editorWorld.getTileWidth();
	    final int th = editorWorld.getTileHeight();
	    
		for (int i = 0; i < editorWorld.getHeight(); i++) {
			final double y = camera.calculateDrawY(i * th);
			drawRect(constSX, y, constEX, y + 1, EColors.vdgray);
		}
		
		for (int i = 0; i < editorWorld.getWidth(); i++) {
            final double x = camera.calculateDrawX(i * tw);
            drawRect(x, constSY, x + 1, constEY, EColors.vdgray);
        }
	}
	
	private void renderEntities() {
		int size = editorWorld.getEditorEntities().size();
		InsertionSort.sort(editorWorld.getEditorEntities());
		
		for (int i = 0; i < size; i++) {
			EditorObject object = editorWorld.getEditorEntities().get(i);
			Entity ent = object.getEntity();
			
			EntityRenderer renderer = ent.getComponent(ComponentType.RENDERING);
			renderer.draw(editorWorld, camera);
		}
	}
	
	private void drawSelectedEntities() {
		for (var e : selectedObjects) {
			final var o = e.getGameObject();
			final double[] area = camera.calculateDrawDimensions(o.startX, o.startY, o.width, o.height);
			drawHRectDimsArray(area, 1, EColors.green);
		}
	}
	
	private void drawPlayerSpawn() {
		final double[] area = camera.calculateDrawDimensions(getEditorWorld().getPlayerSpawn());
		drawTextureDimsArray(EditorTextures.player_spawn, area);
	}
	
	   /** Highlights the tile at the center of the draw area. */
    private void drawCenterPositionBox() {
        drawHRectDimsArray(camera.getCenteredTileDrawDimensions(), 2, EColors.red);
    }
    
    /** Draws a border outlining the edge of the map's bounds. */
    private void drawMapBorders() {
        final double endX = editorWorld.getWidth() * editorWorld.getTileWidth();
        final double endY = editorWorld.getHeight() * editorWorld.getTileHeight();
        final double[] area = camera.convertWorldAreaToScreenArea(0, 0, endX, endY);
        drawHRect(area, 1, EColors.red);
    }
    
    private void drawRegions() {
        double lineWidth = ENumUtil.clamp((3 * camera.getZoom()), 1, 4);
        for (Region r : editorWorld.getRegionData()) {
            final double[] area = camera.convertWorldAreaToScreenArea(r.startX, r.startY, r.endX, r.endY);
            area[1] += curLayer * tileHeight * 0.75 * camera.getZoom();
            area[3] += curLayer * tileHeight * 0.75 * camera.getZoom();
            drawHRect(area, lineWidth, r.getColor());
        }
    }
    
    private void updateMousePos() {
        mouseWorldX = camera.getMouseTileX();
        mouseWorldY = camera.getMouseTileY();
        mouseWorldPX = (int) camera.getMousePixelX();
        mouseWorldPY = (int) camera.getMousePixelY();
        mouseInMap = mouseOver && camera.isMouseInWorld();
    }
    
    /**
     * Draws a box around the currently hovered tile.
     */
    private void drawHoveredTileBox() {
        final int tw = editorWorld.getTileWidth();
        final int th = editorWorld.getTileHeight();
        final int mx = mouseWorldX * tw;
        final int my = mouseWorldY * th;
        final double[] area = camera.convertWorldPxToScreenPx(mx, my);
        double x = area[0];
        double y = area[1];
        final double zoom = camera.getZoom();
        
        WorldTile tileAtMouse = editorWorld.getTileAt(camera.getCurrentLayer(), mouseWorldX, mouseWorldY);
        int camLayer = camera.getCurrentLayer();
        //drawString(camLayer, midX, midY);
        if ((tileAtMouse == null || tileAtMouse == VoidTile.instance) && camLayer > 0) {
            int curLayer = camLayer;
            WorldTile tileBelow = null;
            while ((tileBelow == null || tileBelow == VoidTile.instance) && curLayer >= 0) {
                tileBelow = editorWorld.getTileAt(curLayer - 1, mouseWorldX, mouseWorldY);
                if (curLayer > 0) curLayer--;
            }
            int diff = curLayer - camLayer;
            //System.out.println(diff + " : " + camLayer + " : " + curLayer + " : " + tileBelow);
            //double yy = y + th * zoom - 1;
            //y -= diff * tileHeight;
            double bsy = y + th * zoom - 1;
            double yy = y - diff * th * 0.75 * zoom;
            drawHRect(x, bsy, x + tw * zoom, yy + 1, 1, EColors.gray);
            drawHRect(x, yy, x + tw * zoom, yy + th * zoom, 1, EColors.gray);
            drawRect(x, yy, x + tw * zoom, yy + th * zoom, EColors.gray.opacity(60));
        }

        if (!settings.drawWallBox) drawHRect(x, y, x + tw * zoom, y + th * zoom, 1, EColors.chalk);
    }
    
    private void updateSaveString(double drawAreaMidX, double drawAreaMidY) {
        double sw = FontRenderer.strWidth(saveString);
        double hsw = (sw / 2);
        double sbsx = drawAreaMidX - hsw - 12.5;
        double sbsy = drawAreaMidY - (FontRenderer.FONT_HEIGHT / 2) - 12.5;
        double sbex = drawAreaMidX + hsw + 12.5;
        double sbey = drawAreaMidY + (FontRenderer.FONT_HEIGHT / 2) + 12.5;
        drawRect(sbsx, sbsy, sbex, sbey,  EColors.black);
        drawRect(sbsx + 2, sbsy + 2, sbex - 2, sbey - 2,  EColors.dgray);
        drawStringC(saveString, drawAreaMidX, drawAreaMidY - (FontRenderer.FONT_HEIGHT / 2), saveStringColor);
        if (System.currentTimeMillis() - saveStringTimeout > 600) recentlySaved = false;
    }
    
    private void updateDrawDist() {
        if (editorWorld == null) return;
        
        double w = (double) editorWorld.getTileWidth() * camera.getZoom();
        double h = (double) editorWorld.getTileHeight() * camera.getZoom();
        
        double dw = (((double) (Envision.getWidth() - sidePanel.width)) / w) * 0.5;
        double dh = ((Envision.getHeight() - topHeader.height) / h) * 0.5;
        
        drawDistX = (int) Math.ceil(dw);
        drawDistY = (int) Math.ceil(dh);
        
        updateMap = true;
    }
	
	//============
	// Load World
	//============
	
	public void loadWorld() {
		boolean center = actualWorld == null || !firstPress;
		double zoomToSet = 1;
		
		if (Envision.levelManager == null) {
		    Envision.levelManager = new LevelManager();
		}
		
		if (mapFile != null) {
		    double oldZoom = 1;
			if (actualWorld != null) oldZoom = Envision.levelManager.getCameraZoom();
			if (mapFile.isDirectory()) {
				String mapName = mapFile.getName().replace(".twld", "");
				String mapDir = mapFile.getParent();
				mapFile = new File(mapDir, mapName + "/" + mapName + ".twld");
			}
			actualWorld = new GameWorld(mapFile);
			zoomToSet = oldZoom;
		}
		else if (actualWorld != null) {
			double oldZoom = Envision.levelManager.getCameraZoom();
			mapFile = actualWorld.getWorldFile();
			zoomToSet = oldZoom;
		}
		
		//create editor world
		editorWorld = new EditorWorld(actualWorld);
		Envision.levelManager.setActiveWorld(editorWorld);
		camera = Envision.levelManager.getCamera();
		
		if (center && actualWorld != null) {
		    tileWidth = editorWorld.getTileWidth();
            tileHeight = editorWorld.getTileHeight();
            
            // make the middle tile centered on the screen
            // I have no idea why this is necessary..
            double offsetX = (editorWorld.getWidth() % 2 == 0) ? -tileWidth >> 1 : 0;
            double offsetY = (editorWorld.getHeight() % 2 == 0) ? -tileHeight >> 1 : 0;
            
			double midX = (editorWorld.getPixelWidth() + editorWorld.getTileWidth()) * 0.5;
			double midY = (editorWorld.getPixelHeight() + editorWorld.getTileHeight()) * 0.5;
			
		    camera.setFocusedPoint(midX + offsetX, midY + offsetY);
	        camera.setPixelOffset(tileWidth >> 1, tileHeight >> 1);
	        camera.setCurrentLayer(editorWorld.getNumberOfLayers() - 1);
	        curLayer = editorWorld.getNumberOfLayers() - 1;
		}
		
		camera.setEdgeLocked(false);
		camera.setMinZoom(0.25);
		camera.setMaxZoom(10);
		camera.setZoom(zoomToSet);
        
        int sx = 0;
        int sy = (int) topHeader.endY;
        int ex = (int) (Envision.getWidth() - sidePanel.width);
        int ey = (int) (Envision.getHeight() - botHeader.height);
        camera.setDrawableAreaDimensions(sx, sy, ex, ey);
        updateDrawDist();
        
		loading = false;
	}
	
	//============
	// Save World
	//============
	
	public void saveWorld() {
		if (editorWorld == null) return;
		
		if (editorWorld.saveWorldToFile()) {
			saveString = "Saved!";
			saveStringColor = EColors.lgreen;
			recentlySaved = true;
			hasSaved = true;
			hasBeenModified = false;
			saveStringTimeout = System.currentTimeMillis();
		}
		else {
			saveString = "Failed to Save!";
			saveStringColor = EColors.lred;
			recentlySaved = true;
			saveStringTimeout = System.currentTimeMillis();
		}
	}
	
	//=================================
	//         Public Getters
	//=================================
	
	public Dimension_d getMapDrawDims() {
		double sx = 0.0;
		double sy = this.topHeader.endY;
		double ex = this.sidePanel.startX;
		double ey = endY;
		return new Dimension_d(sx, sy, ex, ey);
	}
	
	public int getXPos() { return midDrawX; }
	public int getYPos() { return midDrawY; }
	public int getViewX() { return drawDistX; }
	public int getViewY() { return drawDistY; }
	public int getWorldMX() { return mouseWorldX; }
	public int getWorldMY() { return mouseWorldY; }
	
	public double getCameraZoom() { return camera.getZoom(); }
	
	public GameWorld getActualWorld() { return actualWorld; }
	public EditorWorld getEditorWorld() { return editorWorld; }
	public boolean isMouseInMap() { return mouseInMap; }
	
	public WorldTile getTileHoveringOver() {
		if (editorWorld == null) return null;
		return editorWorld.getTileAt(mouseWorldX, mouseWorldY);
	}
	
	public int getWorldWidth() { return editorWorld.getWidth(); }
	public int getWorldHeight() { return editorWorld.getHeight(); }
	public int getWorldPixelWidth() { return editorWorld.getPixelWidth(); }
	public int getWorldPixelHeight() { return editorWorld.getPixelHeight(); }
	public int getWorldTileWidth() { return editorWorld.getTileWidth(); }
	public int getWorldTileHeight() { return editorWorld.getTileHeight(); }
	
	public Point2i getHoverTileCoords() { return new Point2i(mouseWorldX, mouseWorldY); }
	public Point2i getHoverPixelCoords() { return new Point2i(mouseWorldPX, mouseWorldPY); }
	
	public MapEditorSettings getSettings() { return settings; }
	public EditorScreenTopHeader getTopHeader() { return topHeader; }
	public EditorScreenBotHeader getBotHeader() { return botHeader; }
	public EditorToolBox getToolBox() { return toolBox; }
	public EditorSidePanel getSidePanel() { return sidePanel; }
	public EditorToolType getCurrentTool() { return settings.getCurrentTool(); }
	public SidePanelType getCurrentSidePanel() { return sidePanel.getCurrentPanelType(); }
	
	public boolean shouldDrawMouse() { return drawingMousePos; }
	public boolean compareSidePanel(SidePanelType type) { return getCurrentSidePanel().equals(type); }
    
    public void updateRegionPanel() {
        if (getSidePanel().getCurrentPanel() instanceof RegionSidePanel regionPanel) {
            regionPanel.loadRegions();
        }
    }
    
    //=================================
    //         Public Setters
    //=================================
    
    public WorldTile setTileAt(int x, int y, WorldTile in) {
        editorWorld.setTileAt(in, curLayer, x, y);
        markUnsaved();
        return in;
    }
    
    public WorldTile setTileAtMouse(WorldTile in) {
        if (mouseInMap) {
            editorWorld.setTileAt(in, mouseWorldX, mouseWorldY);
            markUnsaved();
        }
        return in;
    }
    
    public MapEditorScreen setXPos(int pos) { midDrawX = ENumUtil.clamp(pos, 0, getWorldWidth()); return this; }
    public MapEditorScreen setYPos(int pos) { midDrawY = ENumUtil.clamp(pos, 0, getWorldHeight()); return this; }
    public MapEditorScreen setViewX(int dist) { drawDistX = ENumUtil.clamp(dist, 0, dist); return this; }
    public MapEditorScreen setViewY(int dist) { drawDistY = ENumUtil.clamp(dist, 0, dist); return this; }
    
	//==================
	// Selected Objects
	//==================
	
	public void clearSelected() {
		var it = selectedObjects.iterator();
		while (it.hasNext()) {
			var o = it.next();
			o.setSelected(false);
			it.remove();
		}
	}
	
	public EList<EditorObject> getSelectedObjects() { return selectedObjects; }
	
	public void addToSelected(EditorObject... objs) { addAllToSelected(objs); }
	public void addToSelected(Collection<EditorObject> objs) { addAllToSelected(objs); }
	public void removeFromSelected(EditorObject... objs) { removeAllFromSelected(objs); }
	public void removeFromSelected(Collection<EditorObject> objs) { removeAllFromSelected(objs); }
	
	public void setSelectedObjects(EditorObject... objs) {
		clearSelected();
		addAllToSelected(objs);
	}
	
	public void setSelectedObjects(Collection<EditorObject> objs) {
		clearSelected();
		addAllToSelected(objs);
	}
	
	private void addAllToSelected(EditorObject... objs) {
		for (var o : objs) {
			if (o == null) continue;
			o.setSelected(true);
			selectedObjects.add(o);
		}
	}
	
	private void addAllToSelected(Collection<EditorObject> objs) {
		for (var o : objs) {
			if (o == null) continue;
			o.setSelected(true);
			selectedObjects.add(o);
		}
	}
	
	private void removeAllFromSelected(EditorObject... objs) {
		for (var o : objs) {
			if (o == null) continue;
			o.setSelected(false);
			selectedObjects.remove(o);
		}
	}
	
	private void removeAllFromSelected(Collection<EditorObject> objs) {
		for (var o : objs) {
			if (o == null) continue;
			o.setSelected(false);
			selectedObjects.remove(o);
		}
	}
	
	public void addWorldLayerAbove() {
	    editorWorld.addLayerAbove(curLayer);
	    updateMinimap();
	}
	
	public void addWorldLayerBelow() {
	    editorWorld.addLayerBelow(curLayer);
	    updateMinimap();
	}
	
	public void removeCurrentLayer() {
	    editorWorld.removeLayer(curLayer);
	    curLayer--;
	    camera.setCurrentLayer(curLayer);
	    updateMinimap();
	}
	
	public void setWorldLayer(int layerIn) {
	    curLayer = layerIn;
	    updateMinimap();
	}
	
	public int moveDownLayer() {
	    if (curLayer > 0) curLayer--;
	    camera.setCurrentLayer(curLayer);
	    updateMinimap();
	    return curLayer;
	}
	
	public int moveUpLayer() {
	    if (curLayer < editorWorld.worldLayers.size() - 1) curLayer++;
	    camera.setCurrentLayer(curLayer);
	    updateMinimap();
	    return curLayer;
	}
	
	public int getCurrentWorldLayer() {
	    return curLayer;
	}
	
	// Util
	
	public void markUnsaved() {
	    hasBeenModified = true;
        hasSaved = false;
        
        updateMinimap();
	}
	
	public void updateMinimap() {
	    sidePanel.getMiniMap().onWorldEdited();
	}
	
}
