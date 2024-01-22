package envision.engine.creation;

import envision.engine.assets.WindowBuilderTextures;
import envision.engine.assets.WindowTextures;
import envision.engine.creation.block.BlockConnectionPoint;
import envision.engine.creation.block.CreatorBlock;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.textArea.TextDocument;
import envision.engine.windows.windowObjects.advancedObjects.textArea.WindowTextArea2;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision_lang._launch.EnvisionConsoleOutputReceiver;
import envision_lang._launch.EnvisionLangErrorCallBack;
import envision_lang._launch.EnvisionProgram;
import envision_lang._launch.EnvisionProgramRunner;
import envision_lang.lang.java.annotations.EField;
import envision_lang.lang.java.annotations.EFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import eutil.colors.EColors;

public class EnvisionCodeBlock extends CreatorBlock implements EnvisionConsoleOutputReceiver, EnvisionLangErrorCallBack {
    
    //========
    // Fields
    //========
    
    protected WindowTextArea2 codeArea;
    protected WindowTextArea2 consoleArea;
    protected WindowButton play, pause, stop;
    protected WindowButton clearConsole;
    
    protected EnvisionProgram program;
    protected EnvisionProgramRunner runner;
    
    protected int currentLine;
    protected int errorLine;
    
    @EField
    // inputs for script
    public final BlockConnectionPoint<Object> in0, in1, in2, in3, in4, in5, in6, in7, in8, in9;
    @EField
    // settable outputs
    public final BlockConnectionPoint<Object> out0, out1, out2, out3, out4, out5, out6, out7, out8, out9;
    
    protected TextDocument codeDocument;
    protected TextDocument consoleDocument;
    
    //==============
    // Constructors
    //==============
    
    public EnvisionCodeBlock() { this("Code Block"); }
    public EnvisionCodeBlock(String blockName) {
        super(blockName);
        
        in0 = createInputPoint("I0");
        in1 = createInputPoint("I1");
        in2 = createInputPoint("I2");
        in3 = createInputPoint("I3");
        in4 = createInputPoint("I4");
        in5 = createInputPoint("I5");
        in6 = createInputPoint("I6");
        in7 = createInputPoint("I7");
        in8 = createInputPoint("I8");
        in9 = createInputPoint("I9");
        
        out0 = createOutputPoint("O0");
        out1 = createOutputPoint("O1");
        out2 = createOutputPoint("O2");
        out3 = createOutputPoint("O3");
        out4 = createOutputPoint("O4");
        out5 = createOutputPoint("O5");
        out6 = createOutputPoint("O6");
        out7 = createOutputPoint("O7");
        out8 = createOutputPoint("O8");
        out9 = createOutputPoint("O9");
        
        setSize(700, 500);
        setResizeable(true);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void initChildren() {
        super.initChildren();
        
        double sideGap = 14;
        //double halfX = midX + 30;
        //double w = (endX - sideGap - halfX);
        double bWidth = 30;
        double bGap = 5;
        codeArea = new WindowTextArea2(this, startX + sideGap, startY + sideGap, width - sideGap * 2, height * 0.6, codeDocument);
        
        //double cw = ((endX - sideGap) - (codeArea.endX + sideGap));
        consoleArea = new WindowTextArea2(this, startX + sideGap, codeArea.endY + bGap, width - sideGap * 2,
                                          (endY - bGap - bWidth - bGap) - (codeArea.endY + bGap), consoleDocument);
        
        codeDocument = codeArea.getDocument();
        consoleDocument = consoleArea.getDocument();
        
        codeDocument.clearListeners();
        consoleDocument.clearListeners();
        
        codeDocument.registerListener(codeArea);
        consoleDocument.registerListener(consoleArea);
        
        addObject(codeArea, consoleArea);
        
        double mx = consoleArea.midX;
        double y = endY - bWidth - bGap;
        
        play = new WindowButton(this, mx - bGap - bWidth * 1.5, y, bWidth, bWidth);
        pause = new WindowButton(this, mx - bWidth * 0.5, y, bWidth, bWidth);
        stop = new WindowButton(this, mx + bGap + bWidth * 0.5, y, bWidth, bWidth);
        
        play.setButtonTexture(WindowBuilderTextures.creator_play);
        pause.setButtonTexture(WindowTextures.pin);
        stop.setButtonTexture(WindowBuilderTextures.creator_stop);
        
        play.setAction(this::playScript);
        pause.setAction(this::pauseScript);
        stop.setAction(this::stopScript);
        
        addObject(play, pause, stop);
        
        clearConsole = new WindowButton(this, stop.endX + bGap + bWidth, y, bWidth, bWidth);
        clearConsole.setTextures(WindowTextures.terminal, WindowTextures.terminal_sel);
        clearConsole.setAction(this::clearConsole);
        
        addObject(clearConsole);
    }
    
    @Override
    public void drawObject(float dt, int mXIn, int mYIn) {
        drawRect(EColors.black);
        drawRect(EColors.mgray, 1);
        
        IWindowObject.setEnabled(true, play, pause, stop);
        
        if (runner != null) {
            if (runner.isBlocked()) {
                pause.setEnabled(false);
                pause.drawRect(EColors.yellow, -2);
            }
            if (!runner.isRunning()) {
                stop.setEnabled(false);
                stop.drawRect(EColors.lred, -2);
            }
            else if (!runner.isBlocked()) {
                play.setEnabled(false);
                play.drawRect(EColors.lgreen, -2);
            }
            
            if (runner.hasError()) {
                codeArea.drawRect(EColors.red, -2);
            }
        }
        else {
            pause.setEnabled(false);
            stop.setEnabled(false);
        }
    }
    
    @Override
    public void onEnvisionError(EnvisionLangError e) {
        consoleDocument.addLine(e.toString());
        e.printStackTrace();
    }
    @Override
    public void onJavaException(Exception e) {
        consoleDocument.addLine(e.toString());
        e.printStackTrace();
    }
    @Override
    public void onEnvisionPrint(String line) {
        consoleDocument.insertString(line);
    }
    @Override
    public void onEnvisionPrintln(String line) {
        consoleDocument.addLine(line);
    }
    
    //=========
    // Methods
    //=========
    
    public void playScript() {
        try {
            if (runner == null || runner.hasFinished()) buildScript();
        
            if (!runner.isRunning()) runner.start();
            else if (runner.isBlocked()) runner.executeNextInstruction();
        }
        catch (Exception e) {
            e.printStackTrace();
            consoleDocument.insertString(e.toString() + '\n');
        }
    }
    
    public void pauseScript() {
        //if (runner != null) runner.
    }
    
    public void stopScript() {
        if (runner != null) runner.terminate();
    }
    
    @EFunction
    public void clearConsole() {
        consoleArea.getDocument().setDocumentText("");
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    protected void buildScript() throws Exception {
        clearConsole();
        String script = codeDocument.getInternalDocument().toString();
        program = new EnvisionProgram("Script", script);
        
        program.setEnableBlockStatements(true);
        program.setEnableBlockStatementParsing(true);
        program.setConsoleReceiver(this);
        program.setErrorCallback(this);
        
        program.addJavaObjectToProgram("block", this);
        
        runner = new EnvisionProgramRunner(program);
    }
    
    @EFunction public synchronized long inInt(int point) { return ((Number) getInputValue(point)).longValue(); }
    @EFunction public synchronized double inDouble(int point) { return ((Number) getInputValue(point)).doubleValue(); }
    @EFunction public synchronized String inString(int point) { return String.valueOf(getInputValue(point)); }
    
    public Object getInputValue(int point) {
        return switch (point) {
        case 0 -> in0.getValue();
        case 1 -> in1.getValue();
        case 2 -> in2.getValue();
        case 3 -> in3.getValue();
        case 4 -> in4.getValue();
        case 5 -> in5.getValue();
        case 6 -> in6.getValue();
        case 7 -> in7.getValue();
        case 8 -> in8.getValue();
        case 9 -> in9.getValue();
        default -> null;
        };
    }
    
    @EFunction
    public synchronized void out(int point, Object value) {
        switch (point) {
        case 0: out0.setValue(value); break;
        case 1: out1.setValue(value); break;
        case 2: out2.setValue(value); break;
        case 3: out3.setValue(value); break;
        case 4: out4.setValue(value); break;
        case 5: out5.setValue(value); break;
        case 6: out6.setValue(value); break;
        case 7: out7.setValue(value); break;
        case 8: out8.setValue(value); break;
        case 9: out9.setValue(value); break;
        default: break;
        }
    }
    
}
