package envision.engine.windows.windowObjects.advancedObjects.menuBar;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class WindowMenuBar extends WindowObject {
    
    //========
    // Fields
    //========
    
    protected final EList<WindowMenuCategory> categories = EList.newList();
    protected final EList<WindowButton> catButtons = EList.newList();
    protected WindowMenuCategory currentlyOpenCategory = null;
    
    protected boolean focusCheck = false;
    
    //==============
    // Constructors
    //==============
    
    public WindowMenuBar(WindowObject parent) {
        this.setDimensions(parent.startX, parent.startY, parent.width, 26);
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public void initChildren() {
        buildCategoryButtons();
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawRect(EColors.black);
        drawRect(EColors.pdgray, 1);
        
        //super.drawObject(dt, mXIn, mYIn);
    }
    
    @Override
    public void onAdded() {
        getTopParent().registerListener(this);
        for (var c : categories) addObject(c);
    }
    
    @Override
    public void onClosed() {
        getTopParent().unregisterListener(this);
    }
    
    @Override
    public void onEvent(ObjectEvent e) {
        
    }
    
    @Override
    public void onFocusLost(EventFocus eventIn) {
        super.onFocusLost(eventIn);
        updateFocusCheck();
    }
    
    //=========
    // Methods
    //=========
    
    protected void updateFocusCheck() {
        focusCheck = hasFocus();
        for (var category : categories) {
            focusCheck |= category.hasFocus();
        }
        for (var catButton : catButtons) {
            focusCheck |= catButton.hasFocus();
        }
        if (!focusCheck && currentlyOpenCategory != null) {
            currentlyOpenCategory.setSelected(false);
            currentlyOpenCategory = null;
        }
    }
    
    /**
     * Creates a new menu category with the given category name and attempts to
     * add it to this menu bar. If a category already exists under then given
     * name, then the new category is not added and null is returned.
     * 
     * @param catNameIn The name of the category to create
     * 
     * @return The newly created and added category or null if one already
     *         existed under that name
     */
    public WindowMenuCategory addMenuCategory(String catNameIn) {
        WindowMenuCategory cat = new WindowMenuCategory(catNameIn);
        if (addMenuCategory(cat)) return cat;
        return null;
    }
    
    /**
     * Adds a new category to this menu bar. New categories cannot share names.
     * If an already existing category of the same name is in this menu bar,
     * then the new category is not added and 'false' is returned.
     * 
     * @param categoryIn The category to add
     * 
     * @return true if successfully added
     */
    public boolean addMenuCategory(WindowMenuCategory categoryIn) {
        if (categoryIn == null) return false;
        // don't add multiple categories of the same name
        if (getCategoryByName(categoryIn.getCategoryName()) != null) return false;
        categories.add(categoryIn);
        if (isInitialized()) {
            buildCategoryButtons();
            addObject(categoryIn);
        }
        return true;
    }
    
    protected void buildCategoryButtons() {
        synchronized (categories) {
            final int size = categories.size();
            double lastX = startX;
            
            for (int i = 0; i < size; i++) {
                final var cat = categories.get(i);
                final String name = cat.getCategoryName();
                WindowButton catButton = getOrCreateCategoryButton(name);
                final double w = strWidth(name) + 20;
                catButton.setDimensions(lastX, startY, w, height);
                catButton.setGenericObject(i); // annoying index transfer workaround
                catButton.setAction(() -> openCategory((int) catButton.getGenericObject()));
                cat.setPosition(lastX, endY);
                lastX += w - 1;
            }
        }
    }
    
    public WindowButton getCategoryButton(String catName) {
        if (catName == null) return null;
        synchronized (catButtons) {
            final int size = catButtons.size();
            for (int i = 0; i < size; i++) {
                final var cat = catButtons.get(i);
                if (catName.equals(cat.getObjectName())) return cat;
            }
        }
        return null;
    }
    
    public WindowMenuCategory getCategoryByName(String catName) {
        if (catName == null) return null;
        synchronized (categories) {
            final int len = categories.size();
            for (int i = 0; i < len; i++) {
                var cat = categories.get(i);
                if (catName.equals(cat.getCategoryName())) {
                    return cat;
                }
            }            
        }
        return null;
    }
    
    protected WindowButton getOrCreateCategoryButton(String catName) {
        if (catName == null) throw new IllegalArgumentException("Given category name cannot be null!");
        
        WindowButton catButton = getCategoryButton(catName);
        if (catButton != null) return catButton;
        
        catButton = new WindowButton(this, catName) {
            @Override
            public void drawObject(float dt, int mXIn, int mYIn) {
                super.drawObject(dt, mXIn, mYIn);
                
                if (currentlyOpenCategory == null) return;
                if (!focusCheck) return;
                
                boolean mouseIn = isMouseInside();
                if (mouseIn) {
                    var cat = getCategoryByName(catName);
                    if (cat != null) {
                        openCategory(cat);
                    }
                }
                
                drawStringC(mouseIn);
            }
        };
        
        catButton.setObjectName(catName);
        addObject(catButton);
        catButtons.add(catButton);
        
        return catButton;
    }
    
    public void removeCategory(String catName) {
        if (catName == null) return;
        synchronized (categories) {
            WindowMenuCategory category = getCategoryByName(catName);
            categories.remove(category);
        }
        synchronized (catButtons) {
            WindowButton catButton = getCategoryButton(catName);
            catButtons.remove(catButton);
            removeObject(catButton);
        }
        buildCategoryButtons();
    }
    
    protected void openCategory(int index) {
        if (index < 0 || index >= catButtons.size()) return;
        
        openCategory(categories.get(index));
    }
    
    protected void openCategory(WindowMenuCategory category) {
        if (category == null) return;
        if (categories.notContains(category)) return;
        if (category == currentlyOpenCategory) return;
        
        var old = currentlyOpenCategory;
        categories.forEach(o -> o.setSelected(false));
        
        currentlyOpenCategory = category;
        if (currentlyOpenCategory == old) {
            currentlyOpenCategory.setSelected(false);
            currentlyOpenCategory = null;
        }
        else currentlyOpenCategory.setSelected(true);
    }
    
}
