package envision.game.world.worldEditor.editorTools.tools;

import envision.engine.inputHandlers.Mouse;
import envision.game.world.Region;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorTools.EditorTool;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2i;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;

public class Tool_Region extends EditorTool {

	
	/** The point that the mouse was initially clicked. */
	private Point2i clickPointWorld;
	/** The most recent point that the mouse is still being dragged to. */
	private Point2i dragPointWorld;
	
	private int color;
	
	public Tool_Region(MapEditorScreen in) {
		super(in);
	}

	@Override
	public void drawTool(double x, double y, double w, double h) {
		if (editor.getCurrentSidePanel() != SidePanelType.REGION) return;
		if (Mouse.isAnyButtonDown() && EUtil.notNull(clickPointWorld, dragPointWorld)) {
			//draw updated region box using world draw values
			//drawHRect()
			
			Dimension_d r = new Dimension_d(clickPointWorld.x, clickPointWorld.y, dragPointWorld.x, dragPointWorld.y);
			double midDrawX = editor.midDrawX;
			double midDrawY = editor.midDrawY;
			double drawDistX = editor.drawDistX;
			double drawDistY = editor.drawDistY;
			
			double z = editor.getActualWorld().getCameraZoom();
			double sX = (r.startX * z);
			double sY = (r.startY * z);
			double rw = (r.width * z);
			double rh = (r.height * z);
			double drawPosX = x + sX - ((midDrawX - drawDistX) * w);
			double drawPosY = y + sY - ((midDrawY - drawDistY) * h);
			double drawWidth = drawPosX + rw;
			double drawHeight = drawPosY + rh;
			double lineWidth = ENumUtil.clamp((3 * z), 1, 4);
			drawHRect(drawPosX, drawPosY, drawWidth, drawHeight, lineWidth, color);
			
			
		}
		else {
			int mX = Mouse.getMx();
			int mY = Mouse.getMy();
			drawRect(mX - 3, mY - 3, mX + 3, mY + 3, EColors.yellow);
		}
	}
	
	@Override
	public void onPress() {
		clickPointWorld = getHoverPixelCoords();
		dragPointWorld = getHoverPixelCoords();
		color = EColors.rainbow();
	}

	@Override
	public void onRelease() {
		if (EUtil.anyNull(clickPointWorld, dragPointWorld)) return;
		
		int totalRegions = editor.getEditorWorld().getRegionData().size();
		
		String regionName = "Region " + (totalRegions + 1);
		
		int sX = clickPointWorld.x;
		int sY = clickPointWorld.y;
		int eX = dragPointWorld.x;
		int eY = dragPointWorld.y;
		
		if (eX < sX) {
			int i = eX;
			eX = sX;
			sX = i;
		}
		
		if (eY < sY) {
			int i = eY;
			eY = sY;
			sY = i;
		}
		
		eY += 1;
		eX += 1;
		
		Region region = new Region(editor.getActualWorld(), regionName, sX, sY, eX, eY, color);
		editor.getEditorWorld().getRegionData().add(region);
		editor.updateRegionPanel();
		
		clickPointWorld = null;
		dragPointWorld = null;
	}

	@Override
	public void onUpdate() {
		if (clickPointWorld == null) return;
		dragPointWorld = getHoverPixelCoords();
	}
	
}
