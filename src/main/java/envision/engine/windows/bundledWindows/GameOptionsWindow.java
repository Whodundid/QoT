package envision.engine.windows.bundledWindows;

import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.actionObjects.WindowSlider;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import qot.assets.textures.taskbar.TaskBarTextures;

public class GameOptionsWindow extends WindowParent {
	
	//========
	// Fields
	//========
	
	private WindowSlider lightSlider;
	private WindowButton toggleBatchRendering;
	
	//==============
	// Constructors
	//==============
	
	public GameOptionsWindow() {
		windowIcon = TaskBarTextures.settings;
	}
	
	//===========================
	// Overrides : IWindowParent
	//===========================
	
	@Override
	public void initWindow() {
		setSize(456, 527);
		setResizeable(true);
		setMinDims(148, 100);
		setMaximizable(true);
		setObjectName("Rendering Settings");
	}
	
	//===========================
	// Overrides : IWindowObject
	//===========================
	
	@Override
	public void initChildren() {
		defaultHeader();
		
		var lightText = new WindowLabel(this, startX + 10, startY + 10, "Underground Brightness");
		lightSlider = new WindowSlider(this, startX + 10, lightText.endY + 10, width - 20, 40, 0, 100, false);
		
		toggleBatchRendering = new WindowButton(this, startX + 10, lightSlider.endY + 20, 300, 30, "Toggle Batch");
		
		IActionObject.setActionReceiver(this, lightSlider, toggleBatchRendering);
		
		addObject(lightText, lightSlider);
		addObject(toggleBatchRendering);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		if (toggleBatchRendering != null) {
			String text = (BatchManager.isEnabled()) ? "True" : "False";
			int textColor = (BatchManager.isEnabled()) ? EColors.green.intVal : EColors.lred.intVal;
			drawString(text, toggleBatchRendering.endX + 10, toggleBatchRendering.midY - FontRenderer.FONT_HEIGHT * 0.5, textColor);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == lightSlider) onLightChange();
		if (object == toggleBatchRendering) toggleBatch();
	}
	
	protected void onLightChange() {
		
	}
	
	protected void toggleBatch() {
		if (BatchManager.isEnabled()) BatchManager.disable();
		else BatchManager.enable();
	}
	
}
