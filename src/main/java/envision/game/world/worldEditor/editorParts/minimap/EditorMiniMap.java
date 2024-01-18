package envision.game.world.worldEditor.editorParts.minimap;

import java.awt.image.BufferedImage;

import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.world.IGameWorld;
import envision.game.world.worldEditor.MapEditorScreen;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.worldTiles.VoidTile;
import eutil.colors.EColors;
import eutil.math.ENumUtil;

public class EditorMiniMap extends WindowObject {
	
	EditorSidePanel panel;
	MapEditorScreen editor;
	
	private BufferedImage mapImage;
	private GameTexture mapTexture;
	
	private boolean shouldUpdate = false;
	private long timeSinceLastUpdate;
	
	int mapWidth;
	int mapHeight;
	double mapPxWidth;
	double mapPxHeight;
	double diffX;
    double diffY;
	
	/** Detail relates to the draw ratio.
	 *  A detail level of 1 is equivalent to a 1:1 draw ratio, as in the minimap will draw each pixel of the map.
	 *  Similarly, a detail level of 4 will produce a 4:1 draw ratio where the minimap will draw 1 pixel for every 4 pixels of the map. */
	int detail = 1;
	
	public EditorMiniMap(EditorSidePanel panelIn) {
		panel = panelIn;
		editor = panel.getEditor();
		init(panel);
	}
	
	@Override
	public void initChildren() {
		
	}
	
	@Override
	public void drawObject(float dt, int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.mgray, 1);
		drawRect(EColors.lgray, 3);
		drawRect(EColors.black, 4);
		
		if (shouldUpdate) {
		    if (System.currentTimeMillis() - timeSinceLastUpdate >= 200) {
		        updateMap();
		        timeSinceLastUpdate = 0;
		        shouldUpdate = false;
		    }
		}
		
		if (Mouse.isLeftDown() && isMouseInside()) {
		    panOnMinimap(mXIn, mYIn);
		}
		
		scissor(startX + 4, startY + 4, endX - 4, endY - 4);
		drawMap();
		drawCameraPos();
		endScissor();
	}
	
	@Override
	public void mousePressed(int mx, int my, int button) {
	    if (button == 0) {
	        panOnMinimap(mx, my);
	    }
	}
	
	protected void panOnMinimap(int mx, int my) {
	    if (editor.camera == null) return;
	    
	    double xOffset = (diffX != 0) ? Math.floor(diffX * mapPxWidth) : 0;
        double yOffset = (diffY != 0) ? Math.floor(diffY * mapPxHeight) : 0;
	    
	    // check if actually inside minimap
        if (mx >= startX + 4 + xOffset && mx < endX - 4 - xOffset &&
            my >= startY + 4 + yOffset && my <= endY - 4 - yOffset)
        {
            IGameWorld world = editor.getEditorWorld();
            double w = width - 8 - (xOffset * 2.0);
            double h = height - 8 - (yOffset * 2.0);
            
            double mcx = mx - startX - 4 - xOffset;
            double mcy = my - startY - 4 - yOffset;
            
            if (w == 0 || h == 0) return;
            
            // convert click point to world coord
            double x = (int) ((mcx * world.getWidth()) / w + 1);
            double y = (int) ((mcy * world.getHeight()) / h + 1);
            
            x = ENumUtil.clamp(x, 0, world.getWidth());
            y = ENumUtil.clamp(y, 0, world.getHeight());
            
            editor.camera.setFocusedPoint(x * editor.tileWidth, y * editor.tileHeight);
        }
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	private void drawMap() {
	    if (mapTexture == null) return;
	    
	    // the minimap image itself offset if dimensions are odd values
	    double msx = startX + 4 + ((diffX != 0 && (diffX * 2) % 2 != 0) ? mapPxWidth * 0.5 : 0);
	    double msy = startY + 4 + ((diffY != 0 && (diffY * 2) % 2 != 0) ? mapPxHeight * 0.5 : 0);
	    drawTexture(mapTexture, msx, msy, width - 8, height - 8);
	    
	    // map outline -- useful if the map is not square
	    double osx = startX + 3 + diffX * mapPxWidth;
        double osy = startY + 3 + diffY * mapPxHeight;
        double oex = startX + 5 + (mapWidth + diffX) * mapPxWidth;
        double oey = startY + 5 + (mapHeight + diffY) * mapPxHeight;
        drawHRect(osx, osy, oex, oey, 1, EColors.lgray);
	    
        // view box in map
	    double psx = startX + 4 + (editor.left + diffX) * mapPxWidth;
        double psy = startY + 4 + (editor.top + diffY) * mapPxHeight;
        double pex = startX + 4 + (editor.right + diffX) * mapPxWidth;
        double pey = startY + 4 + (editor.bot + diffY) * mapPxHeight;
        drawHRect(psx, psy, pex, pey, 1, EColors.yellow);
	    
        // the centered tile
	    int centerX = (int) editor.camera.getCameraCenterX() - 1;
	    int centerY = (int) editor.camera.getCameraCenterY() - 1;
	    double csx = startX + 4 + (centerX + diffX) * mapPxWidth;
	    double csy = startY + 4 + (centerY + diffY) * mapPxHeight;
	    double cex = csx + mapPxWidth;
	    double cey = csy + mapPxHeight;
	    drawHRect(csx, csy, cex, cey, 1, EColors.red);
	}
	
	private void createMapImage() throws Throwable {
	    IGameWorld world = editor.getEditorWorld();
		int w = world.getWidth();
		int h = world.getHeight();
		
		//maybe detail someday..
		detail = 1;
		
		int imgWidth = w;
		int imgHeight = h;
		mapWidth = w;
		mapHeight = h;
		    
		//make the map image always square
		//furthermore, use the larger dimension of the two
		if (imgWidth != imgHeight) {
			if (imgWidth > imgHeight) imgHeight = imgWidth;
			else imgWidth = imgHeight;
		}
		
		diffX = (imgWidth - w) * 0.5;
		diffY = (imgHeight - h) * 0.5;
		
        double dw = width - 8;
        double dh = height - 8;
        mapPxWidth = dw / imgWidth;
        mapPxHeight = dh / imgHeight;
		
		mapImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		int layer = editor.camera.getCurrentLayer();
		
		for (int i = 0; i <= layer; i++) {
		    for (int y = 0, iy = (imgHeight - h) / 2; y < h; y++, iy++) {
	            for (int x = 0, ix = (imgWidth - w) / 2; x < w; x++, ix++) {
	                var tile = world.getTileAt(i, x, y);
	                if (tile == null || tile == VoidTile.instance) continue;
	                
	                //System.out.println(ix + " : " + iy);
	                mapImage.setRGB(ix, iy, tile.getMapColor());
	            }
	        }
		}
		
		mapTexture = new GameTexture(mapImage);
		TextureSystem.getInstance().reg(mapTexture);
		
//		File minimapFile = new File(editor.getActualWorld().getWorldFile(), "minimap.png");
//		ImageIO.write(mapImage, "png", new FileOutputStream(minimapFile));
//		TextureLoader.loadTexture(null);
	}
	
	private void drawCameraPos() {
		
	}
	
	public void onWorldEdited() {
	    shouldUpdate = true;
	    
	    if (timeSinceLastUpdate == 0) {
	        timeSinceLastUpdate = System.currentTimeMillis();
	        updateMap();	        
	    }
	}
	
	public void updateMap() {
	    try {
	        if (mapTexture != null && mapTexture.hasBeenRegistered()) {
	            TextureSystem.getInstance().destroyTexture(mapTexture);
	            mapTexture = null;
	        }
	        
            createMapImage();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
	}
	
}
