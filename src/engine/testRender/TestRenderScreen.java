package engine.testRender;

import engine.renderEngine.GLObject;
import engine.renderEngine.shaders.ShaderProgram;
import engine.renderEngine.shaders.Shaders;
import engine.renderEngine.textureSystem.GameTexture;
import engine.screens.screenUtil.GameScreen;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.Vec2f;
import eutil.math.Vec3f;
import game.worldTiles.WorldTile;
import main.QoT;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import world.GameWorld;

public class TestRenderScreen extends GameScreen {
	
	GameWorld theWorld;
	Mesh[] worldTiles;
	WindowButton back;
	ShaderProgram shader = Shaders.basic;
	boolean failed = false;
	boolean drawn = false;
	
	int w;
	int h;
	
	public TestRenderScreen(GameWorld world) {
		theWorld = world;
		buildMesh();
	}
	
	private void buildMesh() {
		if (theWorld.isFileLoaded()) {
			WorldTile[][] tiles = theWorld.getWorldData();
			worldTiles = new Mesh[theWorld.getWidth() * theWorld.getHeight()];
			
			w = theWorld.getTileWidth();
			h = theWorld.getTileHeight();
			
			int n = 0;
			for (int i = 0; i < tiles[0].length; i++) {
				for (int j = 0; j < tiles.length; j++) {
					WorldTile t = tiles[j][i];
					if (t != null)
					worldTiles[n++] = createMesh(j * w,
												 i * h,
												 w,
												 h,
												 0xffffffff,
												 t.getTexture());
				}
			}
		}
		else failed = true;
	}
	
	@Override public void initScreen() {}
	
	@Override
	public void initObjects() {
		back = new WindowButton(this, 25, endY - 60, 100, 45, "Back");
		
		addObject(back);
	}
	
	@Override public void onScreenClosed() {
		QoT.loadWorld(null);
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		if (!failed) {
			//if (!drawn) {
				for (int i = 0; i < worldTiles.length; i++) {
					Mesh m = worldTiles[i];
					if (m != null) renderMesh(m);
				}
				//drawn = true;
			//}
		}
		else {
			drawRect(midX - 100, midY - 30, midX + 100, midY + 30, EColors.mgray);
			drawRect(midX - 95, midY - 25, midX + 95, midY + 25, EColors.dgray);
			drawStringC("Failed :(", midX, midY - 10, EColors.red);
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) closeScreen();
	}
	
	//--------------------------------------------------
	//--------------------------------------------------
	
	public static Mesh createMesh(float sX, float sY, float width, float height, int colorIn, GameTexture texIn) {
		//float f = (colorIn >> 16 & 255) / 255.0F;
		//float f1 = (colorIn >> 8 & 255) / 255.0F;
		//float f2 = (colorIn & 255) / 255.0F;
		//float f3 = (colorIn >> 24 & 255) / 255.0F;
		
		Vertex[] verts = new Vertex[] {
			new Vertex(new Vec3f(GLObject.tdx(sX), GLObject.tdy(sY), 0), new Vec2f(0, 0)),
			new Vertex(new Vec3f(GLObject.tdx(sX + width), GLObject.tdy(sY), 0), new Vec2f(1, 0)),
			new Vertex(new Vec3f(GLObject.tdx(sX + width), GLObject.tdy(sY + height), 0), new Vec2f(1, 1)),
			new Vertex(new Vec3f(GLObject.tdx(sX), GLObject.tdy(sY + height), 0), new Vec2f(0, 1))
		};
		
		int[] indices = { 0, 1, 2, 	  0, 3, 2 };
		
		Mesh m = new Mesh(verts, indices, texIn);
		m.create();
		return m;
	}
	
	private void renderMesh(Mesh m) {
		//enable all of the rendering things
		GL30.glBindVertexArray(m.getVAO());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, m.getIBO());
		GL11.glEnable(GL11.GL_ALPHA & GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//the actual drawing thing
		if (m.isTexture()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL13.glBindTexture(GL11.GL_TEXTURE_2D, m.getTexture().getTextureID());
		}
		shader.bind();
		//shader.setUniform("model", obj.getTransformMatrix());
		//shader.setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		//shader.setUniform("projection", window.getProjection());
		shader.setUniform("isTexture", m.isTexture());
		GL11.glDrawElements(GL11.GL_TRIANGLES, m.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
		shader.unbind();
		
		//disable all of the rendering things
		GL11.glDisable(GL11.GL_ALPHA & GL11.GL_BLEND);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
}
