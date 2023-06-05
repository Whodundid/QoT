package envision.game.world.worldEditor.editorParts.minimap;

import java.awt.image.BufferedImage;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.worldEditor.EditorWorld;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import eutil.colors.EColors;

public class EditorMiniMap extends WindowObject {
	
	EditorSidePanel panel;
	MapEditorScreen editor;
	EditorWorld world;
	
	private BufferedImage mapImage;
	private GameTexture mapTexture;
	
	/** Detail relates to the draw ratio.
	 *  A detail level of 1 is equivalent to a 1:1 draw ratio, as in the minimap will draw each pixel of the map.
	 *  Similarly, a detail level of 4 will produce a 4:1 draw ratio where the minimap will draw 1 pixel for every 4 pixels of the map. */
	int detail = 1;
	
	public EditorMiniMap(EditorSidePanel panelIn) {
		panel = panelIn;
		editor = panel.getEditor();
		world = editor.getEditorWorld();
		init(panel);
	}
	
	@Override
	public void initChildren() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.mgray, 1);
		drawRect(EColors.lgray, 3);
		drawRect(EColors.black, 4);
		
		//scissor(startX + 4, startY + 4, endX - 4, endY - 4);
		//drawMap();
		drawCameraPos();
		//endScissor();
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	private void drawMap() {

	}
	
	private void createMapImage() throws Throwable {
		int w = world.getWidth();
		int h = world.getHeight();
		
		//maybe detail someday..
		detail = 1;
		
		int imgWidth = w;
		int imgHeight = h;
		
		//make the map image always square
		//furthermore, use the larger dimension of the two
		if (imgWidth != imgHeight) {
			if (imgWidth > imgHeight) imgHeight = imgWidth;
			else imgWidth = imgHeight;
		}
		
		mapImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				var tile = world.getTileAt(i, j);
				if (tile == null) continue;
				
				mapImage.setRGB(j, i, tile.getMapColor());
			}
		}
		
//		File minimapFile = new File(editor.getActualWorld().getWorldFile(), "minimap.png");
//		ImageIO.write(mapImage, "png", new FileOutputStream(minimapFile));
//		TextureLoader.loadTexture(null);
	}
	
	private void drawCameraPos() {
		
	}
	
}
