package envision.engine.notifications.window;

import envision.Envision;
import envision.engine.notifications.util.NotificationType;
import envision.engine.windows.windowObjects.action.WindowButton;
import envision.engine.windows.windowObjects.action.WindowCheckBox;
import envision.engine.windows.windowObjects.advanced.WindowScrollList;
import envision.engine.windows.windowObjects.basic.WindowLabel;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.dimensions.Dimension_d;

public class NoteSettingContainer extends WindowObject {
    
    private WindowScrollList parent;
    private NotificationType type;
    private double yPos = 0;
    private boolean drawOffset = false;
    private WindowCheckBox button;
    private WindowLabel label;
    
    public NoteSettingContainer(WindowScrollList parentIn, NotificationType typeIn, int yPosIn, boolean drawOffsetIn) {
        init(parentIn);
        parent = parentIn;
        type = typeIn;
        yPos = yPosIn;
        drawOffset = drawOffsetIn;
        build();
    }
    
    private void build() {
        Dimension_d d = parent.getListDimensions();
        
        button = new WindowCheckBox(parent, d.startX + 10, yPos, 20, 20);
        label = new WindowLabel(parent, button.endX + 8, button.startY + 6, type.getType()) {
            @Override
            public void mousePressed(int mXIn, int mYIn, int buttonIn) {
                if (buttonIn == 0) {
                    WindowButton.playPressSound();
                    Envision.getNotificationHandler().toggleNotificationEnabled(type, true);
                    button.setChecked(Envision.getNotificationHandler().isNotificationTypeEnabled(type));
                }
            }
        };
        
        label.setColor(EColors.lgray);
        label.setHoverText(type.getDescription() != null ? type.getDescription() : "No description");
        
        button.setChecked(Envision.getNotificationHandler().isNotificationTypeEnabled(type));
        button.setActionReceiver(this);
        
        parent.addObjectToList(button, label);
        
        yPos = button.height + 8;
    }
    
    @Override
    public void actionPerformed(IActionObject object, Object... args) {
        if (object == button) {
            Envision.getNotificationHandler().toggleNotificationEnabled(type, true);
            button.setChecked(Envision.getNotificationHandler().isNotificationTypeEnabled(type));
        }
    }
    
    public double getYPos() { return yPos; }

}
