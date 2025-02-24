package envision.debug;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import eutil.EUtil;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class Profiler {
    
    //================
    // Static Methods
    //================
    
    public static final Map<String, Profiler> STATIC_PROFILERS = new HashMap<>();
    
    public static Profiler getAndStartProfiler(String name) {
        var p = STATIC_PROFILERS.computeIfAbsent(name, n -> new Profiler(n));
        p.startProfiler();
        return p;
    }
    
    public static Profiler getProfiler(String name) {
        return STATIC_PROFILERS.get(name);
    }
    
    public static void clearProfilers() { STATIC_PROFILERS.clear(); }
    
    //========
    // Fields
    //========
    
    public final String profilerName;
    public final ProfilerSection rootSection;
    public ProfilerSection currentSection;
    public boolean showNanos = false;
    public boolean started = false;
    
    //==================
    // Internal Classes
    //==================
    
    public class ProfilerSection {
        public ProfilerSection parent;
        public String sectionName;
        public long sectionStartTime;
        public long sectionEndTime;
        public long sectionDuration;
        public boolean closed = false;
        public final EList<ProfilerSection> subSections = EList.newList();
        
        public ProfilerSection(String nameIn) { this(nameIn, null); }
        public ProfilerSection(String nameIn, ProfilerSection parentIn) {
            sectionName = nameIn;
            parent = parentIn;
            sectionStartTime = System.nanoTime();
        }
        
        @Override
        public String toString() {
//            var sb = new EStringBuilder();
//            EList<ProfilerSection> parentTree = EList.newList(this);
//            var p = this;
//            while (p.parent != null) {
//                parentTree.add(p.parent);
//                p = p.parent;
//            }
//            parentTree.reverseInPlace();
//            for (var s : parentTree) {
//                sb.a(s.sectionName, " : ");
//            }
//            sb.setSubstring(0, sb.length() - 3);
//            return sb.toString();
            return sectionName;
        }
        
        public void end() {
            if (closed) return;
            sectionEndTime = System.nanoTime();
            sectionDuration = sectionEndTime - sectionStartTime;
            closed = true;
            for (var s : subSections) {
                s.end();
            }
        }
        
        public String generateString(int depth) {
            var sb = new EStringBuilder();
            int d = depth - 1;
            String pre = (d > 0) ? "| ".repeat(d) : "";
            if (d >= 0) pre += "|-";
            sb.println(pre, sectionName, " ", generateTimestamp());
            for (var s : subSections) {
                sb.println(s.generateString(depth + 1));
            }
            return sb.toString();
        }
        
        public String generateTimestamp() {
            String ts = "(" + TimeUnit.NANOSECONDS.toMillis(sectionDuration) + "ms";
            if (showNanos) ts += "|" + sectionDuration + "ns";
            float durF = sectionDuration;
            float pDur = (parent != null) ? parent.sectionDuration : 1.0f;
            float percent = durF / pDur;
            percent *= 100.0f;
            var df = new DecimalFormat("0");
            if (parent == null) percent = 100.0f;
            String percentS = df.format(percent);
            ts += "|" + percentS + "%";
            ts += ")";
            return ts;
        }
    }
    
    //==============
    // Constructors
    //==============
    
    public Profiler(String nameIn) {
        this.profilerName = nameIn;
        rootSection = new ProfilerSection(nameIn);
        currentSection = rootSection;
    }
    
    //=========
    // Methods
    //=========
    
    public void startProfiler() {
        if (!started) started = true;
        else System.err.println("Profiler: '" + profilerName + "' has already been started and has yet to be stopped!");
        
        resetProfiler();
        rootSection.sectionStartTime = System.nanoTime();
    }
    
    public void stopProfiler() {
        rootSection.end();
        started = false;
    }
    
    public void resetProfiler() {
        rootSection.closed = false;
        rootSection.subSections.clear();
        rootSection.sectionStartTime = 0L;
        rootSection.sectionEndTime = 0L;
    }
    
    public void startSection(String sectionName) {
        if (EStringUtil.isNotPopulated(sectionName)) return;
        
        var section = new ProfilerSection(sectionName, currentSection);
        currentSection.subSections.add(section);
        currentSection = section;
    }
    
    public void endSection() {
        if (currentSection == null) return;
        currentSection.end();
        currentSection = currentSection.parent;
    }
    
    /**
     * Works from current section backwards to find matchinbg section by name.
     * 
     * @param sectionName
     */
    public void endSection(String sectionName) {
        if (currentSection == null) return;
        if (EStringUtil.isNotPopulated(sectionName)) return;
        var cur = currentSection;
        while (cur != null && EUtil.isNotEqual(sectionName, cur.sectionName)) {
            cur = cur.parent;
        }
        if (cur == null) {
            throw new RuntimeException("No path to section named: '" + sectionName + "' found from: '" + currentSection.sectionName + "'");
        }
        cur.end();
        currentSection = cur.parent;
    }
    
    public String createProfilerResultString() {
        return rootSection.generateString(0);
    }
    
    //=========
    // Setters
    //=========
    
    public void setShowNanos(boolean val) {
        showNanos = val;
    }
    
    /*
    "Profiler Name" (X ms)
    |- "Setup" (X ms)
    |  |- "Create Directory If It Doesn't Exist" (X ms)
    |  |- "Load Settings Files"
    |  |  |- "Load Settings File A"
    |  |  |- "Load Settings File B"
    |  |  |- "Load Settings File C"
    |  |- "Finish Setup"
    |- Section "Setup Input Handlers"
    |- Section "Create Backend Handlers"
    |- "Load Project Resources"
    |  |- "Discover Project Resources"
    |  |  |- "Parse Project Directories"
    |  |  |- "Find Resource JSON File"
    |  |  |- ...
     */
}
