package envision.engine.resourceLoaders;

import java.io.File;

import eutil.file.EFileUtil;

public class TextureSheet {
	
	public String sheetName;
	public final File theTexture;
	public final File mappingFile;
	public final int tileWidth;
	public final int tileHeight;
	private TextureSheetParsingOrder parsingOrder;
	
	public TextureSheet(String sheetNameIn, File textureFileIn, File mappingFileIn, int tileWidthIn, int tileHeightIn) {
		sheetName = sheetNameIn;
		theTexture = textureFileIn;
		mappingFile = mappingFileIn;
		tileWidth = tileWidthIn;
		tileHeight = tileHeightIn;
		
		parseMappingFile();
	}
	
	private void parseMappingFile() {
		// make sure the mapping file is not null
		if (mappingFile == null) {
			throw fail("has a null mapping file!");
		}
		// make sure that the mapping file is the correct file type
		if (!EFileUtil.checkExtension(mappingFile, ".json")) {
			throw fail("Invalid texture mapping file type! Expected a '.json' file!");
		}
		
		
		
	}
	
	private RuntimeException fail(String reason) {
		return new IllegalArgumentException("TextureSheet: '" + sheetName + "' " + reason);
	}
	
}
