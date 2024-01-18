package envision.game.world.worldEditor.editorTools.tools;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.game.entities.Entity;
import envision.game.world.Region;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorTool;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

public class Tool_Selector extends EditorTool {
	
	public Tool_Selector(MapEditorScreen in) {
		super(in);
	}

	@Override
	public void onPress() {
		//get current mode
		if (editor.getSidePanel().getCurrentPanelType() == SidePanelType.ASSET) {
			pressAssets();
		}
		else if (editor.getSidePanel().getCurrentPanelType() == SidePanelType.REGION) {
		    pressRegion();
		}
	}
	
	@Override
	public void onUpdate() {
		//get current mode
		if (editor.getSidePanel().getCurrentPanelType() == SidePanelType.ASSET) {
			handleMove();
		}
	}
	
	private void pressAssets() {
		// iterate through each editor object in the world and find ones that are under the mouse
		
		double mX = Mouse.getMx();
		double mY = Mouse.getMy();
		
		double zoom = Envision.levelManager.getCameraZoom();
		int drawDistX = editor.drawDistX;
		int drawDistY = editor.drawDistY;			
		int midDrawX = editor.midDrawX;
		int midDrawY = editor.midDrawY;
		
		double x = editor.x;
		double y = editor.y;
		double w = editor.w;
		double h = editor.h;
		
		EList<EditorObject> underMouse = new EArrayList<>();
		EditorObject toSelect = null;
		
		// get a list of all editor objects that are currently under the mouse
		for (var object : editor.getEditorWorld().getEditorEntities()) {
			Entity ent = object.getEntity();
			boolean under = camera().isMouseOverObject(ent);
			underMouse.addIf(under, object);
		}
		
		class Distance {
			double dist(Entity ent) {
				double drawX = x + w * (ent.worldX + drawDistX - midDrawX);
				double drawY = y + h * (ent.worldY + drawDistY - midDrawY);
				
				double drawW = ent.width * zoom;
				double drawH = ent.height * zoom;
				
				double sx = drawX;
				double sy = drawY;
				double midX = sx + drawW * 0.5;
				double midY = sy + drawH * 0.5;
				
				return ENumUtil.distance(mX, mY, midX, midY);
			}
		}
		
		// java is being silly so this is required ~
		Distance d = new Distance();
		
		// next sort the objects under the mouse by how close their center is to the cursor
		underMouse.sort((a, b) -> {
			Entity entA = a.getEntity();
			Entity entB = b.getEntity();
			
			double distA = d.dist(entA);
			double distB = d.dist(entB);
			
			return Double.compare(distA, distB);
		});
		
		toSelect = underMouse.getFirst();
		
		if (toSelect != null) {
	        if (Keyboard.isCtrlDown()) editor.addToSelected(toSelect);
	        else if (Keyboard.isAltDown()) editor.removeFromSelected(toSelect);
	        else editor.setSelectedObjects(toSelect);
		}
	}
	
	private void pressRegion() {
	    int x = editor.mouseWorldPX;
	    int y = editor.mouseWorldPY;
	    
	    EList<Region> underMouse = EList.newList();
	    
	    for (var region : editor.getEditorWorld().getRegionData()) {
	        double sx = region.startX;
	        double sy = region.startY;
	        double ex = region.endX;
	        double ey = region.endY;
	        if (x >= sx && x <= ex && y >= sy && y <= ey) {
	            underMouse.add(region);
	        }
	    }
	    
	    Region toSelect = null;
	    if (underMouse.size() > 1) {
	        underMouse.sort((a, b) -> {
                double distA = ENumUtil.distance(x, a.midX, y, a.midY);
                double distB = ENumUtil.distance(x, b.midX, y, b.midY);
                return Double.compare(distA, distB);
            });
	    }
	    
	    toSelect = underMouse.getFirst();
	    if (toSelect == null) {
	        
	    }
	    
	    System.out.println(toSelect);
	}
	
	private void handleMove() {
		//System.out.println(editor.worldPixelX + " : " + editor.worldPixelY);
	}
	
}
