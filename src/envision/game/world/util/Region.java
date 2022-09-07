package envision.game.world.util;

import envision.events.types.entity.EntityEnteredRegionEvent;
import envision.events.types.entity.EntityExitedRegionEvent;
import envision.game.GameObject;
import envision.game.entity.Entity;
import envision.game.world.gameWorld.GameWorld;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.math.EDimensionI;
import game.QoT;

public class Region {
	
	protected GameWorld world;
	protected String name;
	public int startX, startY, endX, endY;
	public int midX, midY;
	public int width, height;
	protected int regionColor;
	protected boolean onlyRenderInEditor = true;
	protected EArrayList<Entity> entitiesInside = new EArrayList<>();
	
	public Region(GameWorld worldIn, String nameIn) { this(worldIn, nameIn, 0, 0, 0, 0, 0xff55ff55); }
	public Region(GameWorld worldIn, String nameIn, EColors colorIn) { this(worldIn, nameIn, 0, 0, 0, 0, colorIn.intVal); }
	public Region(GameWorld worldIn, String nameIn, int colorIn) { this(worldIn, nameIn, 0, 0, 0, 0, colorIn); }
	public Region(GameWorld worldIn, String nameIn, int sX, int sY, int eX, int eY) { this(worldIn, nameIn, sX, sY, eX, eY, 0xff55ff55); }
	public Region(GameWorld worldIn, String nameIn, int sX, int sY, int eX, int eY, EColors colorIn) { this(worldIn, nameIn, sX, sY, eX, eY, colorIn.intVal); }
	public Region(GameWorld worldIn, String nameIn, int sX, int sY, int eX, int eY, int colorIn) {
		world = worldIn;
		name = nameIn;
		setDimensions(sX, sY, eX, eY);
		regionColor = colorIn;
	}
	
	@Override
	public String toString() { return toSaveString(); }
	
	public void updateRegion() {
		//check to see if the entities that are said to be inside are still actually inside
		var it = entitiesInside.iterator();
		while (it.hasNext()) {
			var ent = it.next();
			if (ent == null) it.remove();
			
			EDimension entDims = ent.getDimensions();
			if (!getDimensions().partiallyContains(entDims)) {
				onEntityExited(ent, ent.startX, ent.startY);
				it.remove();
			}
		}
	}
	
	public void setDimensions(int widthIn, int heightIn) { setDimensions(startX, startY, startX + widthIn, startY + heightIn); }
	public void setDimensions(int sX, int sY, int eX, int eY) {
		startX = sX;
		startY = sY;
		endX = eX;
		endY = eY;
		width = endX - startX;
		height = endY - startY;
		midX = startX + width / 2;
		midY = startY + height / 2;
	}
	
	public EDimensionI getDimensions() { return new EDimensionI(startX, startY, endX, endY); }
	
	public void moveRegion(int x, int y) {
		startX += x;
		startY += y;
		endX -= x;
		endY -= y;
		setDimensions(startX, startY, endX, endY);
	}
	
	public synchronized EArrayList<Entity> getEntitiesInside() {
		return new EArrayList<>(entitiesInside);
	}
	
	protected synchronized void onEntityEntered(Entity in, int xPos, int yPos) {
		QoT.getEventHandler().postEvent(new EntityEnteredRegionEvent(world, in, this, xPos, yPos));
	}
	protected synchronized void onEntityExited(Entity in, int xPos, int yPos) {
		QoT.getEventHandler().postEvent(new EntityExitedRegionEvent(world, in, this, xPos, yPos));
	}
	
	public synchronized void addEntity(Entity in) {
		if (in == null || entitiesInside.contains(in)) return;
		
		entitiesInside.add(in);
		onEntityEntered(in, in.startX, in.startY);
	}
	
	public synchronized void removeEntity(Entity in) {
		if (in == null || entitiesInside.notContains(in)) return;
		
		entitiesInside.remove(in);
		onEntityExited(in, in.startX, in.startY);
	}
	
	public boolean containsEntity(GameObject object) {
		return entitiesInside.contains(object);
	}
	
	public String getName() { return name; }
	public GameWorld getWorld() { return world; }
	public int getColor() { return regionColor; }
	public boolean onlyRenderInEditor() { return onlyRenderInEditor; }
	public Region setOnlyRenderInEditor(boolean val) { onlyRenderInEditor = val; return this; }
	
	public Region setName(String nameIn) { name = nameIn; return this; }
	public Region setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public Region setColor(int colorIn) { regionColor = colorIn; return this; }
	
	public static Region parseRegion(GameWorld world, String line) {
		try {
			String[] parts = line.split(" ");
			int i = (parts.length == 7) ? 1 : 0;
			String name = parts[i++];
			int color = Integer.valueOf(parts[i++]);
			int sX = Integer.valueOf(parts[i++]);
			int sY = Integer.valueOf(parts[i++]);
			int eX = Integer.valueOf(parts[i++]);
			int eY = Integer.valueOf(parts[i++]);
			return new Region(world, name, sX, sY, eX, eY, color);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String toSaveString() {
		return "r " + getName() + " " + getColor() + " " + startX + " " + startY + " " + endX + " " + endY;
	}
	
}
