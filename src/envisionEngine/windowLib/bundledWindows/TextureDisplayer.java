package envision.windowLib.bundledWindows;

import eutil.datatypes.EArrayList;
import eutil.file.FileOpener;
import eutil.math.ENumUtil;
import game.QoT;
import game.assets.textures.taskbar.TaskBarTextures;
import game.assets.textures.window.WindowTextures;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import envision.renderEngine.textureSystem.GameTexture;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowObjects.basicObjects.WindowImageBox;
import envision.windowLib.windowTypes.WindowParent;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;

public class TextureDisplayer extends WindowParent {
	
	//--------
	// Fields
	//--------
	
	private WindowButton<?> previous, next, open;
	private WindowImageBox<?> imageBox;
	private Path path;
	private File file;
	private EArrayList<Path> paths;
	private EArrayList<File> files;
	private int curPath = 0;
	private int curFile = 0;
	private boolean stretched = false;
	
	private GameTexture tex;
	private boolean centered = false;
	private boolean navigationDrawn = false;
	
	//--------------
	// Constructors
	//--------------
	
	public TextureDisplayer() { this((GameTexture) null); }
	public TextureDisplayer(Path pathIn) { path = pathIn; }
	public TextureDisplayer(File fileIn) { file = fileIn; }
	public TextureDisplayer(GameTexture textureIn) { tex = textureIn; }
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initWindow() {
		setSize(422, 250);
		setResizeable(true);
		setMaximizable(true);
		setMinDims(144, 100);
		setObjectName("Texture Viewer");
		
		windowIcon = TaskBarTextures.textureviewer;
		
		parseForImage();
	}
	
	@Override
	public void initChildren() {
		defaultHeader(this);
		
		double w = ENumUtil.clamp((width - 10 - (width / 6)) / 2, 50, 100);
		
		imageBox = new WindowImageBox(this, startX + 5, startY + 5, width - 10, navigationDrawn ? height - 30 : height - 10);
		previous = new WindowButton(this, midX - (width / 40) - w, imageBox.endY + 4, w, 16, "Previous");
		next = new WindowButton(this, midX + (width / 40), imageBox.endY + 4, w, 16, "Next");
		open = new WindowButton(this, midX - 8, imageBox.endY + 4, 16, 16);
		open.setTextures(WindowTextures.plus, WindowTextures.plus_sel);
		
		open.setHoverText("Open on Computer");
		
		if (previous.endX > (open.startX - 1)) previous.setDimensions(open.startX - 1 - w, previous.startY, w, previous.height);
		if (open.endX > (next.startX - 1)) next.setDimensions(open.endX + 1, next.startY, w, next.height);
		
		IWindowObject.setVisible(navigationDrawn, previous, next);
		open.setVisible(file != null ? files.get(curFile) != null : false);
		
		imageBox.setImage(tex);
		imageBox.setCenterImage(centered);
		
		addObject(imageBox, previous, next, open);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == previous) previousImage();
		if (object == next) nextImage();
		if (object == open) FileOpener.openFile(files.get(curFile));
	}
	
	@Override
	public void close() {
		if (tex != null) tex.destroy();
		super.close();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void parseForImage() {
		if (tex != null) {
			centered = (tex.getWidth() == tex.getHeight());
		}
		
		if (path != null) {
			try {
				setObjectName(path.toString());
				
				//attempt to parse into image
				tex = new GameTexture(path.toString());
				QoT.getTextureSystem().registerTexture(tex);
				if (tex.hasBeenRegistered()) {
					centered = (tex.getWidth() == tex.getHeight());
				}
				
				if (path.getParent() != null) {
					EArrayList<Path> unprocessed = Files.list(path.getParent()).collect(EArrayList.toEArrayList());
					paths = unprocessed.stream().filter(p -> isImage(p.toString())).collect(EArrayList.toEArrayList());
					
					if (paths.size() > 1) {
						navigationDrawn = true;
						
						int i = 0;
						for (Path p : paths) {
							if (p.equals(path)) break;
							i++;
						}
						if (i < paths.size()) curPath = i;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (file != null) {
			try {
				setObjectName(file.getName());
				
				tex = new GameTexture(file.toString());
				QoT.getTextureSystem().registerTexture(tex);
				if (tex.hasBeenRegistered()) {
					centered = (tex.getWidth() == tex.getHeight());
				}
				
				if (file.getParentFile() != null) {
					var unprocessed = new EArrayList<File>().addA(file.getParentFile().listFiles());
					files = unprocessed.filter(f -> isImage(f.getPath()));
					
					if (files.size() > 1) {
						navigationDrawn = true;
						
						int i = 0;
						for (File f : files) {
							if (f.getPath().equals(file.getPath())) break;
							i++;
						}
						if (i < files.size()) curFile = i;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void previousImage() {
		if (file != null) {
			if (tex != null) tex.destroy();
			curFile--;
			if (curFile < 0) curFile = files.size() - 1;
			loadImage(true);
		}
		else if (path != null) {
			if (tex != null) tex.destroy();
			curPath--;
			if (curPath < 0) curPath = paths.size() - 1;
			loadImage(false);
		}
	}
	
	private void nextImage() {
		if (file != null) {
			if (tex != null) tex.destroy();
			curFile++;
			if (curFile == files.size()) curFile = 0;
			loadImage(true);
		}
		else if (path != null) {
			if (tex != null) tex.destroy();
			curPath++;
			if (curPath == paths.size()) curPath = 0;
			loadImage(false);
		}
	}
	
	private void loadImage(boolean isFile) {
		if (isFile) {
			if (files != null) {
				try {
					File newFile = files.get(curFile);
					setObjectName(newFile.getName());
					if (getHeader() != null) getHeader().setTitle(newFile.getName());
					
					tex = new GameTexture(newFile.toString());
					QoT.getTextureSystem().registerTexture(tex);
					if (tex.hasBeenRegistered()) {
						centered = (tex.getWidth() == tex.getHeight());
						imageBox.setImage(tex);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else if (paths != null) {	
			try {
				Path newPath = paths.get(curPath);
				setObjectName(newPath.getFileName().toString());
				if (getHeader() != null) getHeader().setTitle(newPath.getFileName().toString());
				
				tex = new GameTexture(paths.get(curPath).toString());
				QoT.getTextureSystem().registerTexture(tex);
				if (tex.hasBeenRegistered()) {
					centered = (tex.getWidth() == tex.getHeight());
					imageBox.setImage(tex);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isImage(String path) {
		return path.endsWith(".png") ||
			   path.endsWith(".jpg") ||
			   path.endsWith(".gif") ||
			   path.endsWith(".tga") ||
			   path.endsWith(".bmp");
	}

}
