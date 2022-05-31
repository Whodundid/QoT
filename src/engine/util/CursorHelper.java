package engine.util;


import engine.input.Mouse;
import engine.renderEngine.textureSystem.GameTexture;
import main.QoT;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

public class CursorHelper {
	
	private static long curCursor;
	public static boolean isVisible = true;
	
	public static final long arrow = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
	public static final long ibeam = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
	public static final long crosshair = GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR);
	public static final long hand = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
	public static final long vresize = GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR);
	public static final long hresize = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
	
	public static void init() {
		curCursor = arrow;
	}
	
	/** Will only change the cursor if the current one is different than the one to be set. */
	public static void updateCursor(long cursorIn) {
		if (cursorIn != curCursor) setCursor(cursorIn);
	}
	
	/** Attempts to set the cursor to the one specified. */
	public static void setCursor(long cursorIn) {
		GLFW.glfwSetCursor(QoT.getWindowHandle(), curCursor = cursorIn);
	}
	
	/** Returns a new Cursor Object created from an EMC EResource. */
	public static long createCursorFromGameTexture(GameTexture resIn) { return createCursorFromGameTexture(resIn, -1, -1); }
	public static long createCursorFromGameTexture(GameTexture resIn, int xIn, int yIn) {
		try {
			if (resIn != null) {
				return createCursorFromFile(new File(resIn.getFilePath()), xIn, yIn);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		return arrow;
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
		return arrow;
	}
	
	/** Internal function used to map stitched image resources together to create a cursor of the same image. */
	private static long createCursor(BufferedImage imageIn, int xIn, int yIn) {
		try {
			int width = imageIn.getWidth();
			int height = imageIn.getHeight();
			
			int[] pixels = new int[width * height];
			imageIn.getRGB(0, 0, width, height, pixels, 0, width);

			//ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
			
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
			
			//long createdCursor = GLFW.glfwCreateCursor(img, width / 2, height / 2);
			long createdCursor = GLFW.glfwCreateCursor(img, xSpot, ySpot);
			return createdCursor;			
		}
		catch (Exception e) { e.printStackTrace(); }
		return 0;
	}
	
	
	/** Sets the cursor to be invisible. */
	public static void setInvisible() { setCursorVisibility(false); }
	
	/** Sets the cursor to be visible. */
	public static void setVisible() { setCursorVisibility(true); }
	
	/** Sets the cursor to be either visible or invisible. */
	public static void setCursorVisibility(boolean visible) {
		GLFW.glfwSetInputMode(QoT.getWindowHandle(), GLFW.GLFW_CURSOR, (isVisible = visible) ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_HIDDEN);
	}
	
	/** Returns the mouse location in terms of OpenGL. */
	public static Point getPosGL() { return MouseInfo.getPointerInfo() != null ? MouseInfo.getPointerInfo().getLocation() : new Point(0, 0); }
	
	/** Returns the mouse location in terms of the window itself. */
	public static Point getCursorPos() { return new Point(Mouse.getMx(), Mouse.getMy()); }
	
	/** Resets the cursor image back to default. */
	public static void reset() { setCursor(arrow); }
	public static void setIBeam() { setCursor(ibeam); }
	public static void setCrosshair() { setCursor(crosshair); }
	public static void setHand() { setCursor(hand); }
	public static void setHResize() { setCursor(hresize); }
	public static void setVResize() { setCursor(vresize); }
	
	public static boolean isArrow() { return curCursor == arrow; }
	
	/** Returns the current cursor. */
	public static long getCursor() { return curCursor; }
	
	/** Returns true if the cursor is visible. */
	public static boolean isCursorVisible() { return isVisible; }
	
}
