package envision.game.entities.combat;

import envision.Envision;
import envision.engine.rendering.RenderingManager;
import eutil.colors.EColors;
import eutil.strings.EStringBuilder;

public class CircularDirectionalAttack {
    
    public static boolean set = false;
    public static double lastX, lastY;
    public static double upperX, upperY;
    public static double upperPX, upperPY;
    public static double lowerX, lowerY;
    public static double lowerPX, lowerPY;
    public static double lastAngle;
    
    public static void attackAt(double startX, double startY, double targetX, double targetY, double sectorThetaDegrees) {
        sectorThetaDegrees -= 180.0;
        sectorThetaDegrees %= 360.0;
        
        // determine degrees from origin
        double y = startY - targetY;
        double x = startX - targetX;
        
        double angle = Math.abs(180.0 - Math.atan2(y, x) * (180.0 / Math.PI)) % 360.0;
        double angleR = angle * Math.PI / 180.0;
        
        set = true;
        lastAngle = angle;
        
        double sectorAngleStart = angle - (sectorThetaDegrees * 0.5);
        double sectorAngleStartR = sectorAngleStart * Math.PI / 180.0;
        double sectorAngleEnd = angle - (sectorThetaDegrees * 0.5) + sectorThetaDegrees;
        double sectorAngleEndR = sectorAngleEnd * Math.PI / 180.0;
        
        double rads = sectorThetaDegrees * Math.PI / 180.0;
        double dx = startX - targetX;
        double dy = startY - targetY;
        
        double upperCos = Math.cos(rads);
        double upperSin = Math.sin(rads);
        double lowerCos = Math.cos(-rads);
        double lowerSin = Math.sin(-rads);
        
        upperPX = dx * upperCos - dy * upperSin + startX;
        upperPY = dx * upperSin + dy * upperCos + startY;
        lowerPX = dx * lowerCos - dy * lowerSin + startX;
        lowerPY = dx * lowerSin + dy * lowerCos + startY;
        
        var upper = Envision.theWorld.getCamera().convertWorldPixelPosToScreenPos(upperPX, upperPY);
        var lower = Envision.theWorld.getCamera().convertWorldPixelPosToScreenPos(lowerPX, lowerPY);
        
        var sb = new EStringBuilder();
        sb.a((int) startX, ", ", (int) startY);
        sb.a(" | ");
        sb.a(EColors.red);
        sb.a((int) targetX, ", ", (int) targetY);
        sb.a(" | ");
        sb.a(EColors.yellow);
        sb.a((int) dx, ", ", (int) dy);
        sb.a(" | ");
        sb.a(EColors.seafoam);
        sb.a((int) upperPX, ", ", (int) upperPY);
        
        RenderingManager.drawString(sb, 100, 100);
        upperX = upper.x;
        upperY = upper.y;
        lowerX = lower.x;
        lowerY = lower.y;
        
        //System.out.println(upperX + " : " + upperY);
        
        //System.out.println(sectorAngleEnd + " : " + angle + " : " + sectorAngleStart);
    }
    
    static boolean isWithinRadius(double x, double y, double radiusSquared) {
        return x*x + y*y <= radiusSquared;
    }
    
    static boolean isClockwise(double x1, double y1, double x2, double y2) {
        return -x1*y2 + y1*x2 > 0.0;
    }
    
}
