package envision.debug.testStuff.testing;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

public class TextureLoader {
	
	/** Loads a texture in OpenGL. */
	public static int loadTexture(final String path) {
		var w = BufferUtils.createIntBuffer(1);
		var h = BufferUtils.createIntBuffer(1);
		var bits = BufferUtils.createIntBuffer(1);
		var textureID = BufferUtils.createIntBuffer(1);
		
		nstbi_set_flip_vertically_on_load(1);
		ByteBuffer pixels = stbi_load(path, w, h, bits, STBI_rgb);
		glCreateTextures(GL_TEXTURE_2D, textureID);
		glBindTexture(GL_TEXTURE_2D, textureID.get());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, w.get(), h.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, pixels);
		
		stbi_image_free(pixels);
		
		return textureID.get();
	}
	
}
