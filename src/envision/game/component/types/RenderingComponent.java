package envision.game.component.types;

import envision.Envision;
import envision.game.component.ComponentType;
import envision.game.component.EntityComponent;
import envision.game.entities.ComponentBasedObject;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import eutil.colors.EColors;
import eutil.math.ENumUtil;

public abstract class RenderingComponent extends EntityComponent {
	
	//========
	// Fields
	//========
	
	protected double lastDrawX;
	protected double lastDrawY;
	protected double lastDrawW;
	protected double lastDrawH;
	protected int lastDrawBrightness;
	protected boolean lastDrawMouseOver;
	
	//==============
	// Constructors
	//==============
	
	public RenderingComponent(ComponentBasedObject objectIn) {
		super(objectIn, ComponentType.RENDERING);
	}
	
	//=========
	// Methods
	//=========
	
	public abstract void draw(IGameWorld world, WorldCamera camera,
							  int midDrawX, int midDrawY,
							  double midX, double midY,
							  int distX, int distY);
	
	//=================
	// Utility Methods
	//=================
	
	protected int calcBrightness() {
		return calcBrightness(theObject.worldX, theObject.worldY);
	}
	
	public int calcBrightness(int x, int y) {
		var world = Envision.theWorld;
		if (world == null || !world.isUnderground()) return 0xffffffff;
		
		int viewDist = 18;
		int lightx = x, lighty = y;
		var obj = world.getCamera().getFocusedObject();
		
		if (obj != null) {
			var p = obj;
			
			double cmx = p.collisionBox.midX; //collision mid x
			double cmy = p.collisionBox.midY; //collision mid y
			double tw = Envision.theWorld.getTileWidth(); //tile width
			double th = Envision.theWorld.getTileHeight(); //tile height
			
			//offset world coordinates to middle of collision box
			int mwcx = (int) Math.ceil(cmx / tw); //mid world coords x
			int mwcy = (int) Math.floor(cmy / th); //mid world coords y
			
			//light render position
			lightx = p.worldX + mwcx;
			lighty = p.worldY + mwcy;
		}
		else {
			lightx = world.getCamera().getWorldX();
			lighty = world.getCamera().getWorldY();
		}
		
		final int viewDistSquared = viewDist * viewDist;
		int distToPlayer = viewDistSquared - (int) (ENumUtil.distance_squared(x, y, lightx, lighty));
		distToPlayer = ENumUtil.clamp(distToPlayer, 0, distToPlayer);
		int ratio = (distToPlayer * 255) / viewDistSquared;
		return EColors.changeBrightness(0xffffffff, ratio);
	}
	
}
