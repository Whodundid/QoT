package envision.windowLib.windowObjects.advancedObjects.tabbedContainer;

import envision.windowLib.windowTypes.WindowObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;

public class TabbedContainer extends WindowObject {
	
	//--------
	// Fields
	//--------
	
	private EArrayList<ContainerTab> tabs = new EArrayList<>();
	private ContainerTab selectedTab = null;
	
	protected double tabHeight = 32; //Default height is 28 pixels
	protected double tabWidth; //Default will be equal to about 20 pixels wider than the tab's name
	protected double tabGap = 0; //Default gap is 0 pixels
	
	//The maximum width a tab can have
	protected double maxTabWidth = 200;
	//The minimum width a tab can have
	protected double minTabWidth = 80;
	
	//--------------
	// Constructors
	//--------------
	
	protected TabbedContainer() {}
	public TabbedContainer(IWindowObject<?> parent, double x, double y, double w, double h) {
		init(parent, x, y, w, h);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initChildren() {
		initTabs();
	}
	
	private void initTabs() {
		calcTabWidth();
		for (int i = 0; i < tabs.size(); i++) {
			var t = tabs.get(i);
			t.initTab(i, tabWidth, tabHeight);
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(startX, startY + 2 + tabHeight, endX, endY, EColors.black);
		//drawRect(EColors.pdgray, 1);
		drawRect(startX + 1, startY + 1 + tabHeight, endX - 1, startY + 2 + tabHeight, EColors.black);
		//drawRect(startX + 1, startY + 1, endX - 1, startY + 1 + tabHeight, EColors.dgray);
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Creates and adds a new tab to this container with the given tab
	 * name.
	 * 
	 * @param tabName
	 * 
	 * @return The created ContainerTab
	 */
	public ContainerTab addTab(String tabName) {
		var t = new ContainerTab(this, tabName);
		addObject(t);
		tabs.add(t);
		//select by default if there are no tabs currently present
		if (tabs.hasOne()) setSelectedTab(t);
		return t;
	}
	
	/**
	 * Adds the given tab to this header, does not add multiple times if
	 * already added.
	 * 
	 * @param tabIn The tab to add
	 * 
	 * @return The given ContainerTab
	 */
	public ContainerTab addTab(ContainerTab tabIn) {
		if (tabs.contains(tabIn)) return tabIn;
		addObject(tabIn);
		tabs.add(tabIn);
		//select by default if there are no tabs currently present
		if (tabs.hasOne()) setSelectedTab(tabIn);
		return tabIn;
	}
	
	/**
	 * Removes the tab at the specified index. If there are no tabs
	 * present, null is returned.
	 * 
	 * @param index The index of the tab to remove
	 * 
	 * @return The removed tab
	 */
	public ContainerTab removeTab(int index) {
		if (tabs.isEmpty()) {
			selectedTab = null;
			return null;
		}
		
		//don't allow bad indexes
		if (index < 0 || index >= tabs.size()) return null;
		
		//get tab and remove
		var tab = tabs.get(index);
		return (tab != null) ? removeTab(tab) : null;
	}
	
	/**
	 * Attempts to remove a tab with the given name. If there are no tabs
	 * present, null is returned.
	 * 
	 * @param tabName The name of the tab to remove
	 * @return The removed tab
	 */
	public ContainerTab removeTab(String tabName) {
		var tab = tabs.getFirst(t -> EUtil.isEqual(t.getName(), tabName));
		return (tab != null) ? removeTab(tab) : null;
	}
	
	/**
	 * Attempts to remove the given tab from this container. If the tab
	 * 
	 * @param tabIn
	 * @return
	 */
	public ContainerTab removeTab(ContainerTab tabIn) {
		if (tabIn == null) return tabIn;
		if (tabs.isEmpty()) {
			selectedTab = null;
			return tabIn;
		}
		if (tabs.notContains(tabIn)) return tabIn;
		
		tabs.remove(tabIn);
		setSelectedTab(0);
		removeObject(tabIn);
		updateTabs();
		
		return tabIn;
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void updateTabs() {
		calcTabWidth();
		for (int i = 0; i < tabs.size(); i++) {
			var t = tabs.get(i);
			if (t != null) t.initTab(i, tabWidth, tabHeight);
		}
	}
	
	protected void calcTabWidth() {
		tabWidth = 180;
	}
	
	//---------
	// Getters
	//---------
	
	public EArrayList<ContainerTab> getTabs() { return tabs; }
	
	public ContainerTab getSelectedTab() { return selectedTab; }
	public double getTabGap() { return tabGap; }
	public double getMinTabWidth() { return minTabWidth; }
	public double getMaxTabWidth() { return maxTabWidth; }
	
	public EDimension getTabDims() {
		return new EDimension(startX + 1, startY + 2 + tabHeight, endX - 1, endY - 1);
	}
	
	//---------
	// Setters
	//---------

	public void setSelectedTab(int index) {
		//if empty, always set selected tab to null
		if (tabs.isEmpty()) {
			selectedTab = null;
			return;
		}
		
		//don't allow bad indexes
		if (index < 0 || index >= tabs.size()) return;
		
		//get tab and set
		var tab = tabs.get(index);
		if (tab != null) setSelectedTab(tab);
	}
	
	public void setSelectedTab(String tabName) {
		var tab = tabs.getFirst(t -> EUtil.isEqual(t.getName(), tabName));
		if (tab != null) setSelectedTab(tab);
	}
	
	public void setSelectedTab(ContainerTab tab) {
		if (tab == null) return;
		
		//check to see if the new tab isn't already selected
		//if not, then a tab change has occurred
		boolean changed = false;
		if (tab != selectedTab) changed = true;
		
		//update selected
		selectedTab = tab;
		
		//notify all tabs on a tab change
		if (changed) tabs.forEach(t -> t.onTabChanged());
	}
	
}
