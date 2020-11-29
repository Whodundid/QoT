package util.miscUtil;

public class SysUtil {
	
	public static OSType getOS() {
		
		String s = System.getProperty("os.name").toLowerCase();
		
		if (s.contains("windows")) { return OSType.WINDOWS; }
		if (s.contains("mac")) { return OSType.MAC; }
		if (s.contains("sunos")) { return OSType.SOLARIS; }
		if (s.contains("nix") || s.contains("nux") || s.contains("aix")) { return OSType.LINUX; }
		
		return OSType.UNKNOWN;
	}
	
}
