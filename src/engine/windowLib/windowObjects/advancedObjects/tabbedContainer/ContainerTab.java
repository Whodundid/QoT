package engine.windowLib.windowObjects.advancedObjects.tabbedContainer;

import engine.inputHandlers.Mouse;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.WindowObject;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.math.EDimension;

public class ContainerTab<E> extends WindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	protected TabbedContainer parentContainer;
	protected WindowButton tabButton;
	protected String name;
	
	protected int tabTextColor = EColors.white.intVal;
	protected int tabSelectedColor = EColors.mgray.intVal;
	protected int tabNotSelectedColor = EColors.dgray.intVal;
	protected int tabBackgroundColor = EColors.pdgray.intVal;
	
	protected double tabIndex = -1;
	protected double tabWidth;
	protected double tabHeight;
	
	//--------------
	// Constructors
	//--------------
	
	public ContainerTab(TabbedContainer parent) { this(parent, "New Tab"); }
	public ContainerTab(TabbedContainer parent, String nameIn) {
		init(parent);
		setDimensions(parent.getTabDims());
		parentContainer = parent;
		name = nameIn;
	}
	
	//----------
	// Tab Init
	//----------
	
	void initTab(int tabIndexIn, double tabWidthIn, double tabHeightIn) {
		//default values
		tabButton = new ContainerTabButton(this);
		
		IActionObject.setActionReceiver(this, tabButton);
		
		addObject(tabButton);
		
		double gap = parentContainer.getTabGap();
		tabIndex = tabIndexIn;
		tabWidth = tabWidthIn;
		tabHeight = tabHeightIn;
		
		//determine X placement of tab button
		double sX = parentContainer.startX + (tabIndex * tabWidth);
		double sY = parentContainer.startY + 2;
		
		if (tabIndex > 1) {
			sX += (tabIndex - 1) * gap;
		}
		
		tabButton.setDimensions(sX, sY, tabWidthIn, tabHeightIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (isSelected()) {
			drawRect(tabSelectedColor);
			drawRect(EColors.black, 7);
			drawRect(tabBackgroundColor, 8);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == tabButton) setSelected();
	}
	
	@Override
	public void addObject(IWindowObject<?>... objects) {
		boolean selected = isSelected();
		
		//translate each child down by the tabHeight
		for (var o : objects) {
			if (o == null) continue;
			if (o == tabButton) continue;
			
			var dims = o.getDimensions();
			o.setDimensions(dims.startX, dims.startY + tabHeight, dims.width, dims.height);
			
			o.setHidden(!selected);
		}
		
		super.addObject(objects);
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Called by the parent tab container on each tab each time a current
	 * tab selection changes.
	 */
	public void onTabChanged() {
		//if selected -- show all children
		if (isSelected()) {
			getCombinedChildren().forEach(o -> o.setHidden(false));
		}
		//otherwise hide all children (except tab button)
		else {
			for (IWindowObject<?> o : getCombinedChildren()) {
				if (o == tabButton) continue;
				o.setHidden(true);
			}
		}
	}
	
	public void setSelected() {
		parentContainer.setSelectedTab(this);
	}
	
	public boolean isSelected() {
		return parentContainer.getSelectedTab() == this;
	}
	
	//---------
	// Getters
	//---------
	
	public String getName() { return name; }
	public double getTabIndex() { return tabIndex; }
	public EDimension getTabDims() { return parentContainer.getTabDims().sub(8); }
	
	//---------
	// Setters
	//---------
	
	public void setName(String nameIn) { name = nameIn; }
	
	//-----------------------
	// Static Helper Classes
	//-----------------------
	
	public class ContainerTabButton extends WindowButton {
		
		private final ContainerTab tab;
		
		public ContainerTabButton(ContainerTab tabIn) {
			super(tabIn);
			tab = tabIn;
			setString(tab.getName());
		}
		
		@Override
		public void drawObject(int mXIn, int mYIn) {
			int o = (tab.isSelected()) ? 2 : 0;
			
			//println(this, getDimensions());
			drawRect(EColors.black);
			//drawRect(startX + 1, startY + 1, endX - 1, endY + o, EColors.mgray);
			drawRect(startX + 1, startY + 1, endX - 1, endY + o, (tab.isSelected()) ? tabSelectedColor : tabNotSelectedColor);
			if (!tab.isSelected()) {
				drawRect(startX, endY - 1, endX, endY, EColors.black);
			}
			
			boolean mouseHover = isClickable() && isMouseOver();
			boolean mouseCheck = mouseHover;
			int stringColor = isEnabled() ? (mouseCheck ? textHoverColor : tabTextColor) : (drawDisabledColor ? disabledColor : tabTextColor + 0xbbbbbb);
			drawStringC(displayString, midX, midY - FontRenderer.FONT_HEIGHT / 2 + 3, stringColor);
		}
		
	}
	
}
