package envision.engine.terminal.terminalUtil;

import java.io.File;

import envision.engine.assets.WindowTextures;
import envision.engine.rendering.textureSystem.GameTexture;

public enum FileType {
	FILE,
	FOLDER,
	PICTURE,
	TEXT;
	
	//---------
	// Methods
	//---------
	
	public GameTexture getFileTexture() {
		switch (this) {
		case FILE: return WindowTextures.file;
		case FOLDER: return WindowTextures.file_folder;
		case PICTURE: return WindowTextures.file_pic;
		case TEXT: return WindowTextures.file_txt;
		default: return null;
		}
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static FileType getFileType(File f) {
		if (f == null) return null;
		if (f.isDirectory()) return FileType.FOLDER;
		String name = f.getName();
		
		if (name.endsWith(".c")) return FileType.TEXT;
		if (name.endsWith(".cpp")) return FileType.TEXT;
		if (name.endsWith(".txt")) return FileType.TEXT;
		if (name.endsWith(".log")) return FileType.TEXT;
		if (name.endsWith(".cfg")) return FileType.TEXT;
		if (name.endsWith(".java")) return FileType.TEXT;
		if (name.endsWith(".json")) return FileType.TEXT;
		if (name.endsWith(".text")) return FileType.TEXT;
		if (name.endsWith(".nvis")) return FileType.TEXT;
		if (name.endsWith(".twld")) return FileType.TEXT;
		if (name.endsWith(".classpath")) return FileType.TEXT;
		if (name.endsWith(".project")) return FileType.TEXT;
		if (name.endsWith(".readme")) return FileType.TEXT;
		
		if (name.endsWith(".jpg")) return FileType.PICTURE;
		if (name.endsWith(".png")) return FileType.PICTURE;
		if (name.endsWith(".gif")) return FileType.PICTURE;
		if (name.endsWith(".tga")) return FileType.PICTURE;
		if (name.endsWith(".bmp")) return FileType.PICTURE;
		return FileType.FILE;
	}
	
	public static GameTexture getFileTexture(FileType type) {
		switch (type) {
		case FILE: return WindowTextures.file;
		case FOLDER: return WindowTextures.file_folder;
		case PICTURE: return WindowTextures.file_pic;
		case TEXT: return WindowTextures.file_txt;
		default: return null;
		}
	}
}
