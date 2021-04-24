package renderEngine;


import input.Mouse;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import main.QoT;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import renderEngine.textureSystem.GameTexture;

public class CursorHelper {
	
	private static long defaultCursor;
	public static long invisibleCursor;
	public static boolean isVisible = true;
	
	public static void init() {
		defaultCursor = GLFW.glfwGetCurrentContext();
	}
	
	/** Will only change the cursor if the current one is different than the one to be set. */
	public static void updateCursor(long cursorIn) {
		if (getCursor() == defaultCursor) {
			if (cursorIn != defaultCursor) { setCursor(cursorIn); }
		}
		else if (!(getCursor() == cursorIn)) {
			setCursor(cursorIn);
		}
	}
	
	/** Attempts to set the cursor to the one specified. */
	public static void setCursor(long cursorIn) {
		GLFW.glfwSetCursor(QoT.getWindowHandle(), cursorIn);
	}
	
	/** Returns a new Cursor Object created from an EMC EResource. */
	public static long createCursorFromGameTexture(GameTexture resIn) { return createCursorFromGameTexture(resIn, -1, -1); }
	public static long createCursorFromGameTexture(GameTexture resIn, int xIn, int yIn) {
		try {
			if (resIn != null) {
				return createCursor(resIn.GBI(), xIn, yIn);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		return defaultCursor;
	}
	
	/** Returns a new Cursor Object created from an image file. */
	public static long createCursorFromFile(File fileIn) { return createCursorFromFile(fileIn, -1, -1); }
	public static long createCursorFromFile(File fileIn, int xIn, int yIn) {
		try {
			InputStream stream = new FileInputStream(fileIn);
			BufferedImage image = ImageIO.read(stream);
			return createCursor(image, xIn, yIn);
		}
		catch (Exception e) { e.printStackTrace(); }
		return defaultCursor;
	}
	
	/** Internal function used to map stitched image resources together to create a cursor of the same image. */
	private static long createCursor(BufferedImage imageIn, int xIn, int yIn) {
		try {
			int width = imageIn.getWidth();
			int height = imageIn.getHeight();
			
			int[] pixels = new int[width * height];
			imageIn.getRGB(0, 0, width, height, pixels, 0, width);


			ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = pixels[y * width + x];
					
					buffer.put((byte) ((pixel >> 16) & 0xff));
		            buffer.put((byte) ((pixel >> 8) & 0xff));
		            buffer.put((byte) (pixel & 0xff));
		            buffer.put((byte) ((pixel >> 24) & 0xff));
				}
			}
			buffer.flip(); //flips image vertically
			
			GLFWImage img = GLFWImage.create();
			img.width(width);
			img.height(height);
			img.pixels(buffer);
			
			//ByteBuffer imageBuffer = GLFWImage.malloc(capacity);
			int xSpot = (xIn < 0) ? (width / 2) : xIn;
			int ySpot = (yIn < 0) ? (height / 2) : yIn;
			
			long createdCursor = GLFW.glfwCreateCursor(img, width / 2, height / 2);
			return createdCursor;			
		}
		catch (Exception e) { e.printStackTrace(); }
		return defaultCursor;
	}
	
	
	/** Sets the cursor to be invisible. */
	public static void setInvisible() { setCursorVisibility(false); }
	
	/** Sets the cursor to be visible. */
	public static void setVisible() { setCursorVisibility(true); }
	
	/** Sets the cursor to be either visible or invisible. */
	public static void setCursorVisibility(boolean visible) {
		if (isVisible != visible) {
			setCursor(visible ? defaultCursor : invisibleCursor);
			isVisible = visible;
		}
	}
	
	
	/** Returns the mouse location in terms of OpenGL. */
	public static Point getPosGL() { return MouseInfo.getPointerInfo() != null ? MouseInfo.getPointerInfo().getLocation() : new Point(0, 0); }
	
	/** Returns the mouse location in terms of the window itself. */
	public static Point getCursorPos() { return new Point(Mouse.getMx(), Mouse.getMy()); }
	
	/** Resets the cursor image back to default. */
	public static void reset() { setCursor(defaultCursor); }
	
	/** Returns true if the cursor is currently the default cursor. */
	public static boolean isNormalCursor() { return true; }
	
	/** Returns the current cursor. */
	public static long getCursor() { return GLFW.glfwGetCurrentContext(); }
	
	/** Returns true if the cursor is visible. */
	public static boolean isCursorVisible() { return isVisible; }
	
}
