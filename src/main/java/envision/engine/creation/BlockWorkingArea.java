package envision.engine.creation;

import java.io.File;

import envision.engine.creation.block.BlockRegistry;
import envision.engine.creation.block.CreatorBlock;
import envision.engine.internal.windows.windowObjects.action.WindowButton;
import envision.engine.internal.windows.windowObjects.advanced.textArea.WindowTextArea;
import envision.engine.internal.windows.windowObjects.basic.WindowPanel;
import envision.engine.internal.windows.windowTypes.WindowObject;
import envision.engine.internal.windows.windowUtil.layouts.WindowBorderLayout;
import eutil.colors.EColors;

public class BlockWorkingArea extends WindowObject {
    
    //========
    // Fields
    //========
    
    protected File workingFile;
    
    private WindowTextArea<Class<? extends CreatorBlock>> blockListPanel;
    private WindowPanel leftPanel;
    private WindowButton create, delete;
    private BlockAreaPanel blockArea;
    
    //==============
    // Constructors
    //==============
    
    public BlockWorkingArea() {
        this((File) null);
    }
    
    public BlockWorkingArea(File FileToLoad) {
        workingFile = FileToLoad;
    }

    public BlockWorkingArea(BlockWorkingArea areaIn) {
        workingFile = areaIn.workingFile;
        
//        areaSpace = new Dimension_i(areaIn.areaSpace);
//        currentView = new Dimension_i(areaIn.currentView);
        
//        blockList.addAll(areaIn.blockList);
//        
//        zoom = areaIn.zoom;
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        super.initChildren();
        
        setLayout(new WindowBorderLayout());
        
        double w = width / 5;

        leftPanel = new WindowPanel(new WindowBorderLayout());
        leftPanel.setBackground(EColors.black);
        leftPanel.setMinWidth(w);
        
        int bGap = 5;
        int botGap = 40;
        blockListPanel = new WindowTextArea(this, startX, startY, w, (endY - botGap) - startY);
        
        blockListPanel.setDrawLineNumbers(true);
        for (var b : BlockRegistry.getBlockTypeList()) {
            blockListPanel.addTextLine(b.getSimpleName(), b);
        }
        
        blockListPanel.fitItemsInList();
        
        double by = blockListPanel.endY + bGap;
        double bw = w / 2 - bGap * 2 + 3;
        double bh = botGap - (bGap * 2);
        create = new WindowButton(this, startX + bGap, by, bw, bh, "Create");
        delete = new WindowButton(this, create.endX + bGap, by, bw, bh, "Delete");
        
        create.setAction(this::createBlock);
        delete.setAction(this::deleteBlocks);
        
        create.setEnabled(false);
        delete.setEnabled(false);
        
        var topLeft = new WindowPanel();
        var botLeft = new WindowPanel();
        
        topLeft.setMinHeight(botGap);
        botLeft.setMinHeight(botGap);
        
        botLeft.addObject(create);
        botLeft.addObject(delete);
        //leftPanel.addObject(topLeft, WindowBorderLayout.NORTH);
        leftPanel.addObject(botLeft, WindowBorderLayout.SOUTH);
        leftPanel.addObject(blockListPanel, WindowBorderLayout.CENTER);
        
        blockArea = new BlockAreaPanel();
        
        addObject(blockArea, WindowBorderLayout.CENTER);
        addObject(leftPanel, WindowBorderLayout.WEST);
    }
    
    @Override
    public void preReInit() {
        super.preReInit();
    }
    
    @Override
    public void postReInit() {
        super.postReInit();
    }
    
    @Override
    public void drawObject_i(float dt, int mXIn, int mYIn) {
        super.drawObject_i(dt, mXIn, mYIn);
        
        create.setEnabled(blockListPanel.getCurrentLine() != null);
        delete.setEnabled(blockArea.currentSelection.isNotEmpty());
    }
    
    //=========
    // Methods
    //=========
    
    protected void createBlock() {
        try {
            var line = blockListPanel.getCurrentLine();
            if (line == null) return;
            
            Class<? extends CreatorBlock> c = line.getGenericObject();
            var con = c.getConstructor();
            
            var b = con.newInstance();
            blockArea.addBlock(b, blockArea.currentView.midX, blockArea.currentView.midY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void deleteBlocks() {
        blockArea.deleteSelectedBlocks();
    }
    
    public void undoAction() {}
    public void redoAction() {}
    public void copyBlocks() {}
    public void pasteBlocks() {}
    public void cutBlocks() {}
    
}
