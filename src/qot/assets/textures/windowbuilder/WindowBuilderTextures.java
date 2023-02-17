package qot.assets.textures.windowbuilder;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.settings.QoTSettings;

public class WindowBuilderTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final WindowBuilderTextures t = new WindowBuilderTextures();
	public static WindowBuilderTextures instance() { return t; }
	
	// Hide constructor
	private WindowBuilderTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\screen_creator\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	creator_btn 			= new GameTexture(textureDir, "creator_btn.png"),
	creator_btn3			= new GameTexture(textureDir, "creator_btn3.png"),
	creator_check			= new GameTexture(textureDir, "creator_check.png"),
	creator_circle 			= new GameTexture(textureDir, "creator_circle.png"),
	creator_container 		= new GameTexture(textureDir, "creator_container.png"),
	creator_dropdown 		= new GameTexture(textureDir, "creator_dropdown.png"),
	creator_eyedropper 		= new GameTexture(textureDir, "creator_eyedropper.png"),
	creator_header	 		= new GameTexture(textureDir, "creator_header.png"),
	creator_imgbox		 	= new GameTexture(textureDir, "creator_imgbox.png"),
	creator_label 			= new GameTexture(textureDir, "creator_label.png"),
	creator_line			= new GameTexture(textureDir, "creator_line.png"),
	creator_list			= new GameTexture(textureDir, "creator_list.png"),
	creator_move			= new GameTexture(textureDir, "creator_move.png"),
	creator_play			= new GameTexture(textureDir, "creator_play.png"),
	creator_progress		= new GameTexture(textureDir, "creator_progress.png"),
	creator_radio			= new GameTexture(textureDir, "creator_radio.png"),
	creator_resize			= new GameTexture(textureDir, "creator_resize.png"),
	creator_select			= new GameTexture(textureDir, "creator_select.png"),
	creator_slider			= new GameTexture(textureDir, "creator_slider.png"),
	creator_square			= new GameTexture(textureDir, "creator_square.png"),
	creator_stop			= new GameTexture(textureDir, "creator_stop.png"),
	creator_textbox			= new GameTexture(textureDir, "creator_textbox.png"),
	creator_tlist			= new GameTexture(textureDir, "creator_tlist.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, creator_btn);
		reg(sys, creator_btn3);
		reg(sys, creator_check);
		reg(sys, creator_circle);
		reg(sys, creator_container);
		reg(sys, creator_dropdown);
		reg(sys, creator_eyedropper);
		reg(sys, creator_header);
		reg(sys, creator_imgbox);
		reg(sys, creator_label);
		reg(sys, creator_line);
		reg(sys, creator_list);
		reg(sys, creator_move);
		reg(sys, creator_play);
		reg(sys, creator_progress);
		reg(sys, creator_radio);
		reg(sys, creator_resize);
		reg(sys, creator_select);
		reg(sys, creator_slider);
		reg(sys, creator_square);
		reg(sys, creator_stop);
		reg(sys, creator_textbox);
		reg(sys, creator_tlist);
	}
	
}
