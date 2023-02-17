package envision.engine.windows.windowBuilder;

import envision.engine.rendering.textureSystem.GameTexture;
import qot.assets.textures.windowbuilder.WindowBuilderTextures;

public enum WindowBuilderToolType {
	
	SELECT_AND_MOVE("Select and Move", WindowBuilderTextures.creator_select),
	COLOR_EYEDROPPER("Color Eyedropper", WindowBuilderTextures.creator_eyedropper),
	LINE("Line Tool", WindowBuilderTextures.creator_line),
	RECTANGLE("Rectangle Tool", WindowBuilderTextures.creator_square),
	CIRCLE("Circle Tool", WindowBuilderTextures.creator_circle),
	LABEL("Text Label", WindowBuilderTextures.creator_label),
	CONTAINER("Container", WindowBuilderTextures.creator_container),
	BUTTON("Button", WindowBuilderTextures.creator_btn),
	CHECK_BOX("Checkbox", WindowBuilderTextures.creator_check),
	RADIO_BUTTON("Radio Button", WindowBuilderTextures.creator_radio),
	DROP_DOWN_SELECTION("Dropdown Selection Box", WindowBuilderTextures.creator_dropdown),
	SLIDER("Slider", WindowBuilderTextures.creator_slider),
	PROGRESS_BAR("Progress Bar", WindowBuilderTextures.creator_progress),
	HEADER("Header", WindowBuilderTextures.creator_header),
	SCROLL_LIST("Scroll List", WindowBuilderTextures.creator_list),
	TEXT_BOX("Text Box", WindowBuilderTextures.creator_textbox),
	TEXT_AREA("Text Area", WindowBuilderTextures.creator_tlist),
	IMAGE_BOX("Image Box", WindowBuilderTextures.creator_imgbox);
	
	public final String description;
	public final GameTexture toolTexture;
	
	private WindowBuilderToolType(String descriptionIn, GameTexture textureIn) {
		description = descriptionIn;
		toolTexture = textureIn;
	}
	
}
