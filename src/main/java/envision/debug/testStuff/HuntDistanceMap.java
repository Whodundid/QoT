package envision.debug.testStuff;

import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.basicObjects.WindowImageBox;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2d;
import eutil.math.ENumUtil;
import qot.assets.textures.general.GeneralTextures;

public class HuntDistanceMap extends GameScreen {
	
	WindowButton stillwater, lawson, desalle;
	WindowImageBox currentImage;
	Point2d pointA, pointB;
	Point2d mapA, mapB;
	boolean setA = true;
	
	double vGap = 550;
	double hGap = 550;
	
	public HuntDistanceMap() {
		super();
		aliases.add("huntmap", "hm");
	}
	
	@Override
	public void initScreen() {
		
	}
	
	@Override
	public void initChildren() {
		double screenWidth = width;
		double screenHeight = height;
		
		double workingWidth = screenWidth - hGap * 2;
		double workingHeight = screenHeight - vGap * 2;
		
		double sx, sy;
		
		if (workingWidth < workingHeight) {
			workingWidth = workingHeight;
		}
		else if (workingHeight < workingWidth) {
			workingHeight = workingWidth;
		}
		
		sx = midX - workingWidth / 2;
		sy = midY - workingHeight / 2;
		
		currentImage = new WindowImageBox(this, sx, sy, workingWidth, workingHeight) {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				super.mousePressed(mXIn, mYIn, button);
				
				if (setA) {
					setA = false;
					if (pointA == null) pointA = new Point2d(mXIn, mYIn);
					else pointA.set(mXIn, mYIn);
				}
				else {
					setA = true;
					if (pointB == null) pointB = new Point2d(mXIn, mYIn);
					else pointB.set(mXIn, mYIn);
				}
			}
		};
		
		currentImage.setImage(GeneralTextures.hunt_stillwater);
		
		stillwater = new WindowButton(this, midX - 400, endY - 50, 200, 30, "Stillwater");
		lawson = new WindowButton(this, midX - 100, endY - 50, 200, 30, "Lawson");
		desalle = new WindowButton(this, midX + 200, endY - 50, 200, 30, "DeSalle");
		
		addObject(currentImage);
		addObject(stillwater, lawson, desalle);
	}
	
	@Override
	public void drawObject_i(int mXIn, int mYIn) {
		super.drawObject_i(mXIn, mYIn);
		
		if (pointA != null && pointB != null) {
			RenderingManager.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, 5, EColors.red);
			
			double sx = (pointA.x * 1000.0) / currentImage.width;
			double sy = (pointA.y * 1000.0) / currentImage.height;
			double ex = (pointB.x * 1000.0) / currentImage.width;
			double ey = (pointB.y * 1000.0) / currentImage.height;
			
			String text = "" + ENumUtil.distance(sx, sy, ex, ey);
			double textX = (pointA.x + pointB.x) / 2;
			double textY = (pointA.y + pointB.y) / 2;
			double strlen = FontRenderer.strWidth(text);
			
			RenderingManager.drawString(text, textX - strlen / 2, textY);
		}
		
		if (pointA != null) {
			RenderingManager.drawFilledCircle(pointA.x, pointA.y, 4, 50, EColors.green);
		}
		
		if (pointB != null) {
			RenderingManager.drawFilledCircle(pointB.x, pointB.y, 4, 50, EColors.blue);
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == stillwater) currentImage.setImage(GeneralTextures.hunt_stillwater);
		if (object == lawson) currentImage.setImage(GeneralTextures.hunt_lawson);
		if (object == desalle) currentImage.setImage(GeneralTextures.hunt_desalle);
	}
	
}
