package qot.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import eutil.date.EDateTime;
import eutil.strings.EStringUtil;

public class LauncherLogger {
	
	private static final Logger logger = Logger.getLogger("Launcher");
	
	private static File logFile = null;

	private static File createLogFile() {
		if (logFile != null) return logFile;
		logFile = new File(LauncherDir.getLauncherDir(), "LOG_" + EDateTime.getDate() + "_" + EDateTime.getTime() + ".log");
		
		try (var fos = new FileOutputStream(logFile, true);
			 var str = new PrintStream(fos))
		{
			str.println("----------------");
			str.println(" QoT Output Log");
			str.println("----------------");
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return logFile;
	}
	
	public static File getLogFile() {
		if (logFile == null) createLogFile();
		return logFile;
	}
	
	//----------------------------------------------------------------
	// Log handlers for when not running in a development environment
	//----------------------------------------------------------------
	
	public static void log(Object obj) { log(LauncherLogLevel.DEBUG, obj); }
	public static void logError(Object obj) { log(LauncherLogLevel.ERROR, obj); }
	public static void logError(Exception e) { log(LauncherLogLevel.ERROR, e); }
	
	public static void log(LauncherLogLevel level, Object obj) {
		//check if logging to the log file
		if (level == LauncherLogLevel.DEBUG &&
			LauncherDir.logLevel == LogOutputLevel.ONLY_ERRORS) return;
		
		//logger.log(Level.SEVERE, "[LAUNCHER]: " + String.valueOf(obj));
		
		//prepare to log to file
		File log = getLogFile();
		try (var fos = new FileOutputStream(log, true);
			 var str = new PrintStream(fos))
		{
			str.append(String.valueOf(obj));
			str.append('\n');
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void log(LauncherLogLevel level, Exception e) {
		//check if logging to the log file
		if (level == LauncherLogLevel.DEBUG &&
			LauncherDir.logLevel == LogOutputLevel.ONLY_ERRORS) return;
		
		//logger.log(Level.SEVERE, "[LAUNCHER]: " + String.valueOf(e));
		
		//prepare to log to file
		File log = getLogFile();
		try (var fos = new FileOutputStream(log, true);
		     var str = new PrintStream(fos))
		{
			e.printStackTrace(str);
			str.append('\n');
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	static void logWithDialogBox(Object e, String boxTitle, Object... args) { logWithDialogBox(LauncherLogLevel.DEBUG, e, boxTitle, args); }
	static void logWithDialogBox(LauncherLogLevel level, Object e, String boxTitle, Object... args) {
		String msg = String.valueOf(e);
		String additionalArgs = EStringUtil.toString(args, "\n");
		String outMsg = msg + "\n\n" + additionalArgs;
		
		JOptionPane.showMessageDialog(null, outMsg, boxTitle, JOptionPane.INFORMATION_MESSAGE);
		log(level, msg);
	}
	
	static void logWithDialogBox(Exception e, String message, String boxTitle, Object... args) { logWithDialogBox(LauncherLogLevel.DEBUG, e, message, boxTitle, args); }
	static void logWithDialogBox(LauncherLogLevel level, Exception e, String message, String boxTitle, Object... args) {
		String msg = String.valueOf(message);
		String additionalArgs = EStringUtil.toString(args);
		String outMsg = msg + "\n\n" + additionalArgs;
		
		JOptionPane.showMessageDialog(null, outMsg, boxTitle, JOptionPane.INFORMATION_MESSAGE);
		log(level, e);
	}
	
	static void logErrorWithDialogBox(Object e, String boxTitle, Object... args) {
		String err = String.valueOf(e);
		String additionalArgs = EStringUtil.toString(args, "\n");
		
		JOptionPane.showMessageDialog(null, err + "\n\n" + additionalArgs, boxTitle, JOptionPane.ERROR_MESSAGE);
		logError(err);
	}
	
	static void logErrorWithDialogBox(Exception e, String message, String boxTitle, Object... args) {
		String err = String.valueOf(message);
		String additionalArgs = EStringUtil.toString(args);
		
		JOptionPane.showMessageDialog(null, err + "\n\n" + additionalArgs, boxTitle, JOptionPane.ERROR_MESSAGE);
		logError(e);
	}
	
}
