package envision.game.world.worldEditor.editorTools.tools;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.engine.registry.types.Sprite;
import envision.game.GameObject;
import envision.game.entities.Entity;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorTool;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;

public class Tool_Place extends EditorTool {
	
	private EditorObject object;
	
	//==============
    // Constructors
    //==============
	
	public Tool_Place(MapEditorScreen in) {
		super(in);
	}

	//===========
    // Overrides
    //===========
	
	@Override
	public void drawTool(double x, double y, double w, double h) {
		if (editor.getCurrentSidePanel() != SidePanelType.ASSET) return;
		
		//COMPLETELY HASHED TOGETHER :)
		EditorObject primary = settings.getPrimaryPalette();
		if (primary == null) return;
		var camera = Envision.levelManager.getCamera();
		var obj = primary.getGameObject();
		double zoom = camera.getZoom();
		Sprite tex = primary.getSprite();
		double tW = obj.width;
		double tH = obj.height;
		double tDrawW = tW * zoom;
		double tDrawH = tH * zoom;
		double tw = editor.tileWidth;
		double th = editor.tileHeight;
		double tmx = obj.collisionBox.midX;
		double tmy = obj.collisionBox.midY;
		
		double xOffset = (obj.collisionBox.midX == 0) ? tW * 0.5 : tmx;
		double yOffset = (obj.collisionBox.midY == 0) ? tH * 0.5 : tmy;
		
		//xOffset /= editor.tileWidth;
		//yOffset /= editor.tileHeight;
		
		double dsx;
		double dsy;
		
		if (editor.getSettings().lockToTileGrid) {
		    double offsetX = (tmx == 0) ? obj.width * 0.5 : tmx;
            double offsetY = (tmy == 0) ? obj.height * 0.5 : tmy;
		    dsx = camera.calculateDrawX((editor.mouseWorldX - (int) (offsetX / tw)) * editor.tileWidth);
		    dsy = camera.calculateDrawY((editor.mouseWorldY - (int) (offsetY / th)) * editor.tileHeight);
		}
		else {
		    dsx = camera.calculateDrawX(editor.mouseWorldPX - xOffset);
		    dsy = camera.calculateDrawY(editor.mouseWorldPY - yOffset);
		    
		}
		
		drawSprite(tex, dsx, dsy, tDrawW, tDrawH, 0x99ffffff);
		//drawTexture(tex, mX, mY, tDrawW, tDrawH, 0x99ffffff);
		//drawHRect(mX, mY, mX + tDrawW, mY + tDrawH, 1, EColors.blue);
		//drawTexture(tex, mX, mY, tDrawW, tDrawH);
	}
	
	@Override
	public void onPress() {
		object = getPrimary();
		if (object == null) return;
		if (object.isTile()) placeTile();
		else if (object.isEntity()) placeEntity();
		else placeObject();
	}
	
	//==================
    // Internal Methods
    //==================
	
	private void placeTile() {
		
	}
	
	private void placeEntity() {
		Entity ent = object.getEntity();
		Class<? extends Entity> objClass = ent.getClass();
		
		try {
			Entity newEnt = objClass.getConstructor().newInstance();
			addEntityToWorld(newEnt);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void placeObject() {
		GameObject obj = object.getGameObject();
		System.out.println("PLACE OBJECT: " + obj);
		
		if (obj instanceof PlayerSpawnPoint p) {
		    var placement = getPlacementPosition(obj);
			this.editor.getEditorWorld().setPlayerSpawn(placement[0], placement[1]);
		}
		else if (obj instanceof Entity e) {
			placeEntity();
		}
	}
	
}
