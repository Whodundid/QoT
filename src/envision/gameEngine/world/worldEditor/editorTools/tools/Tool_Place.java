package envision.gameEngine.world.worldEditor.editorTools.tools;

import envision.gameEngine.GameObject;
import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.world.worldEditor.MapEditorScreen;
import envision.gameEngine.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.gameEngine.world.worldEditor.editorParts.util.EditorObject;
import envision.gameEngine.world.worldEditor.editorTools.EditorTool;
import envision.inputHandlers.Mouse;
import envision.renderEngine.textureSystem.GameTexture;

public class Tool_Place extends EditorTool {
	
	private EditorObject object;
	
	//--------------
	// Constructors
	//--------------
	
	public Tool_Place(MapEditorScreen in) {
		super(in);
	}

	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawTool(double x, double y, double w, double h) {
		if (editor.getCurrentSidePanel() != SidePanelType.ASSET) return;
		
		//COMPLETELY HASHED TOGETHER :)
		EditorObject primary = settings.getPrimaryPalette();
		if (primary == null) return;
		GameTexture tex = primary.getTexture();
		double tW = primary.getGameObject().width;
		double tH = primary.getGameObject().height;
		double tDrawW = tW * editor.getActualWorld().getZoom();
		double tDrawH = tH * editor.getActualWorld().getZoom();
		double tmx = primary.getGameObject().collisionBox.midX * editor.getActualWorld().getZoom();
		double tmy = primary.getGameObject().collisionBox.midY * editor.getActualWorld().getZoom();
		
		drawTexture(tex, Mouse.getMx() - tmx, Mouse.getMy() - tmy, tDrawW, tDrawH, 0x99ffffff);
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
	
	//------------------
	// Internal Methods
	//------------------
	
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
		
		if (obj instanceof Entity ent) {
			//Entity ent = object.getEntity();
			Class<? extends Entity> objClass = ent.getClass();
			
			try {
				Entity newEnt = objClass.getConstructor().newInstance();
				addEntityToWorld(newEnt);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
