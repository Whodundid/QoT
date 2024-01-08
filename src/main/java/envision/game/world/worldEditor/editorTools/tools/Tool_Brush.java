package envision.game.world.worldEditor.editorTools.tools;

import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorTool;
import envision.game.world.worldTiles.WorldTile;
import eutil.math.ENumUtil;

public class Tool_Brush extends EditorTool {
	
	public EditorObject item;
	public WorldTile tile;
	public int tempSize = 5;
	public boolean tempIsCircle = true;
	
	public Tool_Brush(MapEditorScreen in) {
		super(in);
	}
	
	@Override public void onPress() { doAction(); }
	@Override public void onUpdate() { doAction(); }
	
	private void doAction() {
		item = (button == 0) ? getPrimary() : getSecondary();
		
		if (item == null || !item.isTile()) return;
		tile = item.getTile();
		
		if (tempIsCircle) setCircle();
		else setRect();
	}
	
	private void set(int x, int y) {
		final int tx = ENumUtil.clamp(wx + x, 0, getWorldWidth() - 1);
		final int ty = ENumUtil.clamp(wy + y, 0, getWorldHeight() - 1);
		setTile(tx, ty, WorldTile.randVariant(tile));
	}
	
	//========================
	
	private void setCircle() {
		if (tempSize < 1) return;
		tempSize = 4;
		
		switch (tempSize) {
		case 1: set1Circle(); break;
		case 2: set2Circle(); break;
		case 3: set3Circle(); break;
		case 4: set4Circle(); break;
		case 5: set5Circle(); break;
		}
	}
	
	private void set1Circle() {
		set(+0, +0);
	}
	
	private void set2Circle() {
		set1Circle();
		set(-1, +0);
		set(+0, +1);
		set(+0, -1);
		set(+1, +0);
	}
	
	private void set3Circle() {
		set2Circle();
		set(-2, +0);
		set(-1, -1);
		set(+0, +2);
		set(+1, -1);
		set(+2, +0);
		set(+1, +1);
		set(+0, -2);
		set(-1, +1);
	}
	
	private void set4Circle() {
		set3Circle();
		set(-3, +0);
		set(-2, -1);
		set(-2, -2);
		set(-1, -2);
		set(+0, -3);
		set(+1, -2);
		set(+2, -2);
		set(+2, -1);
		set(+3, +0);
		set(+2, +1);
		set(+2, +2);
		set(+1, +2);
		set(+0, +3);
		set(-1, +2);
		set(-2, +2);
		set(-2, +1);
	}
	
	private void set5Circle() {
		set4Circle();
		
		//set(-4, +1);
		set(-4, +0);
		//set(-4, -1);
		
		set(-3, -1);
		set(-2, -2);
		set(-1, -3);
		set(-3, -2);
		set(-2, -3);
		
		//set(-1, -4);
		set(+0, -4);
		//set(+1, -4);
		
		set(+1, -3);
		set(+2, -2);
		set(+3, -1);
		set(+3, -2);
		set(+2, -3);
		
		//set(+4, -1);
		set(+4, +0);
		//set(+4, +1);
		
		set(+3, +1);
		set(+2, +2);
		set(+1, +3);
		set(+2, +3);
		set(+3, +2);
		
		//set(+1, +4);
		set(+0, +4);
		//set(-1, +4);
		
		set(-1, +3);
		set(-2, +2);
		set(-3, +1);
		set(-3, +2);
		set(-2, +3);
		
	}
	
	//========================
	
	private void setRect() {
		
	}
	
}
