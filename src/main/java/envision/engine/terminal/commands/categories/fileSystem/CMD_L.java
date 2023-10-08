package envision.engine.terminal.commands.categories.fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;
import eutil.sys.OSType;
import qot.settings.QoTSettings;

public class CMD_L extends AbstractFileCommand {
	
	public CMD_L() {
		setAcceptedModifiers("-a");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "ll"; }
	@Override public EList<String> getAliases() { return EList.of("l"); }
	@Override public String getHelpInfo(boolean runVisually) { return "List all files in a directory with more information."; }
	@Override public String getUsage() { return "ex: ll 'dir'"; }
	
	@Override
	public void runCommand() {
	    expectNoMoreThan(1);
	    
		//if empty -- ls current dir
		if (args().isEmpty()) {
			listDir(null);
			return;
		}
		
		File theFile = null;
		
		theFile = switch (firstArg()) {
		case "." -> term.getDir();
		case ".." -> term.getDir().getParentFile();
		case "~" -> USER_DIR;
		case "wdir" -> QoTSettings.getEditorWorldsDir();
		default -> null;
		};
		
		if (theFile == null) {
			String all = EStringUtil.combineAll(args(), " ");
			EList<String> allArgs = new EArrayList<>(args());
			allArgs.add(all);
			
			for (String s : allArgs) {
				theFile = new File(s);
				if (!theFile.exists()) {
					theFile = new File(dir(), s);
				}
			}
			
			if (firstArg().equals("~")) {
				theFile = EFileUtil.userDir();
			}
		}
		
		listDir(theFile);
	}
	
	private void listDir(File dir) {
		if (dir == null) dir = dir();
		
		//check if actually exists
		if (!EUtil.fileExists(dir)) {
			error("'" + dir.getAbsolutePath() + "' is not a vaild directory!");
			return;
		}
		
		//check if actually a directory
		if (!dir.isDirectory()) {
			error(dir.getName() + " is a file, not a directory!");
			return;
		}
		
		try {
			String colorPath = EColors.mc_aqua + dir.getName();
			boolean showHidden = hasModifier("-a");
			
			writeLink("Viewing Dir: " + colorPath, dir.getName(), dir, false, EColors.yellow);
			if (dir.list().length > 0) writeln();
			
			EList<File> allFiles = EList.newList();
			
			allFiles.addA(dir.listFiles());
			
			EList<FileProperties> fileNames = EList.newList();

			if (showHidden && OSType.isWindows()) {
				allFiles.add(dir());
				allFiles.add(parentDir());
			}
			
			int longestOwner = 0;
			int longestFileSize = 0;
			int longestDay = 0;
			int longestTime = 0;
			
			for (File f : allFiles) {
				//only show hidden files if '-a'
				if (f.isHidden() && !showHidden) continue;
				
				FileProperties props = null;
				
				if (dir().equals(f)) 						props = new FileProperties(f, ".");
				else if (parentDir().equals(f)) 			props = new FileProperties(f, "..");
				else 										props = new FileProperties(f);
				
				longestOwner = props.ownerWidth(longestOwner);
				longestFileSize = props.fileSizeWidth(longestFileSize);
				longestDay = props.dayWidth(longestDay);
				longestTime = props.timeWidth(longestTime);
				
				fileNames.add(props);
			}
			
			for (var f : fileNames) {
				var perms = f.perms;
				var owner = f.owner;
				var fSize = f.fileSize;
				var month = f.month;
				var day = f.day;
				var time = f.time;
				var name = "";
				
				if (f.isDirectory) name = EColors.blue + f.name + EColors.lgray + "/";
				else if (f.isSymbolicLink) {
					String link = "";
					try {
						link = Files.readSymbolicLink(f.theFile.toPath()).toString();
						link = EColors.lgray + " -> " + EColors.mc_darkred + link;
					}
					catch (Exception e) {}
					name = EColors.mc_darkred + f.name + link;
				}
				else name = EColors.green + f.name;
				
				owner = String.format("%" + longestOwner + "s", owner);
				fSize = String.format("%" + longestFileSize + "s", fSize);
				day   = String.format("%" + longestDay + "s", day);
				time  = String.format("%" + longestTime + "s", time);
				
				writeln(EColors.lgray, perms, " ", owner, " ", fSize, " ", month, " ", day, " ", time, " ", name);
			}
		}
		catch (Exception e) {
			error(e);
		}
	}
	
	//=========================
	// Internal Helper Classes
	//=========================
	
	private static class FileProperties {
		
		public File theFile;
		public String perms;
		public String owner;
		public String fileSize;
		public String month;
		public String day;
		public String time;
		public String name;
		public boolean isDirectory;
		public boolean isSymbolicLink;
		
		public FileProperties(File f) throws IOException { this(f, null); }
		public FileProperties(File f, String overrideName) throws IOException {
			theFile = f;
			perms = getPermissions(f);
			owner = getOwner(f);
			fileSize = getFileSize(f);
			month = getMonth(f);
			day = getDay(f);
			time = getTime(f);
			name = (overrideName != null) ? overrideName : f.getName();
			isDirectory = f.isDirectory();
			isSymbolicLink = Files.isSymbolicLink(f.toPath());
		}
		
		/** Retrieves the string format of the file permissions for the given file. 
		 * @throws IOException */
		private static String getPermissions(File f) throws IOException {
			final Path path = f.toPath();
			final var sb = new EStringBuilder();
			if (Files.isSymbolicLink(path)) sb.a('l');
			else if (Files.isDirectory(path)) sb.a('d');
			else sb.a('-');
			try {
			    var set = Files.getPosixFilePermissions(path);
			    sb.a(PosixFilePermissions.toString(set));
			}
			catch (UnsupportedOperationException e) {
			    sb.a((Files.isReadable(path)) ? 'r' : '-');
	            sb.a((Files.isWritable(path)) ? 'w' : '-');
	            sb.a((Files.isExecutable(path)) ? 'x' : '-');
			}
			return sb.toString();
		}
		
		/** Retrieves the owner of the given file (if possible). */
		private static String getOwner(File f) {
			String owner = "";
			
			try { owner = Files.getOwner(f.toPath()).getName(); }
			catch (Exception e) {}
			
			owner = EStringUtil.subStringAfter(owner, "\\");
			return owner;
		}
		
		/** Retrieves the string format of the size of the given file in bytes. */
		private static String getFileSize(File f) {
			String size = "";
			try { size = "" + Files.size(f.toPath()); }
			catch (Exception e) {}
			return size;
		}
		
		/** Retrieves the string format for the month that the given file was last modified. */
		private static String getMonth(File f) {
			String date = "";
			try {
				String utcFormat = Files.getLastModifiedTime(f.toPath()).toString();
				date = Instant.parse(utcFormat)
							  .atOffset(ZoneOffset.UTC)
							  .format(DateTimeFormatter.ofPattern("MMM"));
			}
			catch (Exception e) {}
			return date;
		}
		
		/** Retrieves the string format for the day that the given file was last modified. */
		private static String getDay(File f) {
			String day = "";
			try {
				String utcFormat = Files.getLastModifiedTime(f.toPath()).toString();
				day = Instant.parse(utcFormat)
							 .atOffset(ZoneOffset.UTC)
							 .format(DateTimeFormatter.ofPattern("d"));
			}
			catch (Exception e) {}
			return day;
		}
		
		/** Retrieves the string format for the HH:mm time of last modified time for the given file. */
		private static String getTime(File f) {
			String time = "";
			try {
				String utcFormat = Files.getLastModifiedTime(f.toPath()).toString();
				time = Instant.parse(utcFormat)
							  .atOffset(ZoneOffset.UTC)
							  .format(DateTimeFormatter.ofPattern("HH:mm"));
			}
			catch (Exception e) {}
			return time;
		}
		
		//=========
		// Getters
		//=========
		
		public int ownerWidth(int in) { var w = owner.length(); return (w > in) ? w : in; }
		public int fileSizeWidth(int in) { var w = fileSize.length(); return (w > in) ? w : in; }
		public int dayWidth(int in) { var w = day.length(); return (w > in) ? w : in; }
		public int timeWidth(int in) { var w = time.length(); return (w > in) ? w : in; }
		
	}
	
}
