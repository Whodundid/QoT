package util.miscUtil;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import org.lwjgl.opengl.GL11;
import util.EUtil;
import util.storageUtil.StorageBox;
import util.storageUtil.StorageBoxHolder;

/** Utility class which returns information on the current running system. */
public final class ESystemInfo {

	private static OperatingSystemMXBean bean;
	private static ESystemInfo instance;
	
	//private static Processor[] processors;
	//private static Processor cpu = null;
	//private static int coreNum = -1;
	//private static String cpuName = "null";
	
	private static long memTotal = 0l;
	private static long jvmMemTotal = 0l;
	
	//------------------------
	//ESystemInfo Constructors
	//------------------------
	
	//hide constructor
	@SuppressWarnings("deprecation")
	private ESystemInfo() {
		bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
		
		//processors = new SystemInfo().getHardware().getProcessors();
		//cpu = (processors.length > 0) ? processors[0] : null;
		//coreNum = processors.length;
        //cpuName = String.format("%dx %s", new Object[] {Integer.valueOf(processors.length), processors[0]}).replaceAll("\\s+", " ");
		
		memTotal = bean.getTotalPhysicalMemorySize();
		jvmMemTotal = Runtime.getRuntime().maxMemory();
	}
	
	//-----------
	//Initializer
	//-----------
	
	/** Used to initialize the static ESystemInfo instance and its parameters. */
	public static void init() {
		instance = (instance != null) ? instance : new ESystemInfo();
	}
	
	//-------------------
	//ESystemInfo Getters
	//-------------------
	
	/** Returns the static instance of ESystemInfo. */
	public static ESystemInfo instance() { return instance; }
	/** Returns an object which gathers certain system information. */
	public static OperatingSystemMXBean getInfoBean() { return bean; }
	
	//operating system
	
	/** Returns the name of the OS. */
	public static String getOS_Brand() { return System.getProperty("os.name"); }
	/** Returns the manufacturer name for the OS. */
	//public static String getOS_Manufacturer() { return new SystemInfo().getOperatingSystem().getManufacturer(); }
	/** Returns the version of the os. */
	public static String getOS_Version() { return System.getProperty("os.version"); }
	/** Returns the bit depth of the system (32-bit or 64-bit). */
	public static String getOS_Architecture() { return System.getProperty("sun.arch.data.model"); }
	
	//cpu
	
	/** Returns the first CPU object. */
	//public static Processor getCPU() { return cpu; }
	/** Returns an array of current CPUs in the system. */
	//public static Processor[] getCPUs() { return processors; }
	/** Returns the name of the CPU. */
	//public static String getCPU_Name() { return cpuName; }
	/** Returns the number of CPU cores. */
	//public static int getCPU_CoreNum() { return coreNum; }
	/** Returns the brand of the CPU. */
	public static String getCPU_Brand() { return System.getenv("PROCESSOR_IDENTIFIER"); }
	
	//gpu
	
	/** Returns the name of the GPU model. */
	public static String getGPU_Model() { return GL11.glGetString(GL11.GL_RENDERER); }
	/** Returns the name of the GPU vendor. */
	public static String getGPU_Vendor() { return GL11.glGetString(GL11.GL_VENDOR); }
	/** Returns the GPU version number. */
	public static String getGPU_Version() { return GL11.glGetString(GL11.GL_VERSION); }
	
	//ram
	
	/** Returns the total amount of system ram in bytes. */
	public static long getRAM_Total() { return memTotal; }
	/** Returns the total amount of free system ram in bytes. */
	@SuppressWarnings("deprecation")
	public static long getRAM_Free() { return bean.getFreePhysicalMemorySize(); }
	/** Returns the total amoutn of used system ram in bytes. */
	public static long getRAM_Used() { return memTotal - getRAM_Free(); }
	
	//jvm memory
	
	/** Returns the total amount of ram currently allocated to this active JVM instance in bytes. */
	public static long getRAM_JVM_Total() { return Runtime.getRuntime().maxMemory(); }
	/** Returns the total amount of free ram for this JVM instance. */
	public static long getRAM_JVM_Free() { return Runtime.getRuntime().freeMemory(); }
	/** Returns the total amount of used ram for this JVM instance. */
	public static long getRAM_JVM_Used() { return Runtime.getRuntime().totalMemory(); }
	
	//drives
	
	/** Returns the current drives on the system. Does not guarantee access. */
	public static File[] getDrives() { return File.listRoots(); }
	
	/** Returns the system drive that the OS is running off of. */
	public static File getPrimaryDrive() {
		for (File d : getDrives()) {
			try {
				String driveName = d.getAbsolutePath();
				boolean primary = false;
				
				primary = driveName.substring(0, driveName.length() - 1).equals(System.getenv("SystemDrive"));
				if (primary) { return d; }
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return null;
	}
	
	/** Returns a StorageBoxHolder containing each drive along with their current storage capacities (total, free). */
	public static StorageBoxHolder<File, StorageBox<Long, Long>> getDriveSizes() {
		StorageBoxHolder<File, StorageBox<Long, Long>> holder = new StorageBoxHolder();
		
		EUtil.forEach(getDrives(), o -> holder.add(o, new StorageBox(o.getTotalSpace(), o.getFreeSpace())));
		
		return holder;
	}
	
}
