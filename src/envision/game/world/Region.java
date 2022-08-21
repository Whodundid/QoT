package envision.game.world;

import envision.game.entity.Entity;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimensionI;

public class Region {
	
	protected GameWorld world;
	protected String name;
	public int startX, startY, endX, endY;
	public int midX, midY;
	public int width, height;
	protected int regionColor;
	protected boolean onlyRenderInEditor = true;
	
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
	
	public EArrayList<Entity> getEntitiesInside() {
		return null;
	}
	
	public void onEntityEntered(Entity in, int xPos, int yPos) {
		//world.entityEnteredRegion(this, in, xPos, yPos);
	}
	
	public void onEntityExited(Entity in, int xPos, int yPos) {
		//world.entityExitedRegion(this, in, xPos, yPos);
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
