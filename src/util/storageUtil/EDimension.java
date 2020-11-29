package util.storageUtil;

//Author: Hunter Bragg

public class EDimension {
	
	public double startX = 0, endX = 0;
	public double startY = 0, endY = 0;
	public double midX = 0, midY = 0;
	public double width = 0, height = 0;
	
	public EDimension() { this(0, 0, 0, 0); }
	public EDimension(double startXIn, double startYIn, double endXIn, double endYIn) {
		startX = startXIn;
		startY = startYIn;
		endX = endXIn;
		endY = endYIn;
		width = endXIn - startXIn;
		height = endYIn - startYIn;
		midX = getMidX();
		midY = getMidY();
	}
	
	public EDimension(EDimension dimIn) {
		startX = dimIn.startX;
		startY = dimIn.startY;
		endX = dimIn.endX;
		endY = dimIn.endY;
		width = endX - startX;
		height = endY - startY;
		midX = getMidX();
		midY = getMidY();
	}
	
	public EDimension move(double changeX, double changeY) {
		startX += changeX;
		startY += changeY;
		reDimension();
		return this;
	}
	
	public EDimension setPosition(double newX, double newY) {
		startX = newX;
		startY = newY;
		reDimension();
		return this;
	}
	
	public EDimension setWidth(double newWidth) {
		width = newWidth;
		reDimension();
		return this;
	}
	
	public EDimension setHeight(double newHeight) {
		height = newHeight;
		reDimension();
		return this;
	}
	
	private void reDimension() {
		endX = startX + width;
		endY = startY + height;
		midX = getMidX();
		midY = getMidY();
	}
	
	public EDimension expand(double amount) {
		EDimension d = new EDimension(this);
		d.startX -= amount;
		d.startY -= amount;
		d.endX += amount;
		d.endY += amount;
		d.width += (amount * 2);
		d.height += (amount * 2);
		return d;
	}
	
	public EDimension contract(double amount) {
		EDimension d = new EDimension(this);
		d.startX += amount;
		d.startY += amount;
		d.endX -= amount;
		d.endY -= amount;
		d.width -= (amount * 2);
		d.height -= (amount * 2);
		return d;
	}
	
	public boolean contains(double xIn, double yIn) { return xIn >= startX && xIn <= endX && yIn >= startY && yIn <= endY;}
	
	public double getMidX() { return startX + (width / 2); }
	public double getMidY() { return startY + (height / 2); }
	
	public EDimension translateHorizontal(double amount) { startX += amount; return this; }
	public EDimension translateVertical(double amount) { startY += amount; return this; }
	
	public boolean contains(EDimension dimIn) { return startX <= dimIn.endX && startY <= dimIn.endY && endX >= dimIn.startX && endY >= dimIn.startY; }
	public boolean fullyContains(EDimension dimIn) { return startX < dimIn.startX && startY < dimIn.startY && endX > dimIn.endX && endY > dimIn.endY; }
	public boolean isGreaterThan(EDimension dimIn) { return startX > dimIn.startX && startY > dimIn.startY && width > dimIn.width && height > dimIn.height; }
	public boolean isLessThan(EDimension dimIn) { return startX < dimIn.startX && startY < dimIn.startY && width < dimIn.width && height < dimIn.height; }
	public boolean isEqualTo(EDimension dimIn) { return startX == dimIn.startX && startY == dimIn.startY && width == dimIn.width && height == dimIn.height; }
	
	@Override public String toString() { return "[startX/Y: " + startX + ", " + startY + "; endX/Y: " + endX + ", " + endY + "; width/Height: " + width + ", " + height + "]"; }
	
	public static EDimension of(double startXIn, double startYIn, double endXIn, double endYIn) { return new EDimension(startXIn, startYIn, endXIn, endYIn); }
	public static EDimension of(EDimension in) { return new EDimension(in); }
	
}
