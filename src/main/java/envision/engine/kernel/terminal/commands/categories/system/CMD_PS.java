package envision.engine.kernel.terminal.commands.categories.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import envision.engine.kernel.EnvisionKernel;
import envision.engine.kernel.process.IEnvisionProcess;
import envision.engine.kernel.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

//Author: Hunter Bragg

public class CMD_PS extends TerminalCommand {
    
    public CMD_PS() {
        setCategory("System");
        setAcceptedModifiers("u", "t");
        expectedArgLength = 0;
    }

    @Override public String getName() { return "ps"; }
    @Override public String getHelpInfo(boolean runVisually) { return "Used to list active processes in the kernel - accepts '-u' & '-t'"; }
    @Override public String getUsage() { return "ex: ps -ut"; }
    @Override public byte requiredPermissionLevel() { return 2; }
    
    @Override
    public void runCommand() {
        expectNoArgs();
        
        int longestUser = "USER".length();
        int longestPid = "PID".length();
        int longestStartTime = "START".length();
        int longestTimeAlive = "UPTIME".length();
        
        final var sdf = new SimpleDateFormat("MMMd H:mm");
        
        final EList<IEnvisionProcess> processList = EnvisionKernel.getInstance().getActiveProcesses();
        for (IEnvisionProcess p : processList) {
            longestUser = longestCheck(longestUser, p.launchingUser());
            longestPid = longestCheck(longestPid, p.pid());
            longestStartTime = longestCheck(longestStartTime, sdf.format(new Date(p.startTime())));
            
            long uptime = p.timeAlive();
            long hours = TimeUnit.MILLISECONDS.toHours(uptime);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime);
            String uptimeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            longestTimeAlive = longestCheck(longestTimeAlive, uptimeString);
        }
        
        // create header
        {
            EList<String> args = EList.newList();
            var sb = new EStringBuilder();
            if (hasModifier("u")) {
                sb.a("%-", longestUser, "s "); args.add("USER");
            }
            sb.a("%", longestPid, "s "); args.add("PID");
            if (hasModifier("t")) {
                sb.a("%-", longestStartTime, "s "); args.add("START");
                sb.a("%-", longestTimeAlive, "s "); args.add("UPTIME");
            }
            sb.a("%s"); args.add("COMMAND");
            
            Object[] arr = new Object[args.size()];
            for (int i = 0; i < args.size(); i++) {
                arr[i] = args.get(i);
            }
            
            String header = String.format(sb.trim(), arr);
            writeln(header, EColors.lgray);
        }
        
        // write each process
        for (IEnvisionProcess p : processList) {
            long uptime = p.timeAlive();
            long hours = TimeUnit.MILLISECONDS.toHours(uptime);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime);
            String uptimeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            
            String user  = String.format("%-" + longestUser + "s", String.valueOf(p.launchingUser()));
            String pid   = String.format("%" + longestPid + "s", String.valueOf(p.pid()));
            String start = String.format("%-" + longestStartTime + "s", sdf.format(p.startTime()));
            String alive = String.format("%-" + longestTimeAlive + "s", uptimeString);
            String cmd   = p.launchCommand();
            
            var sb = new EStringBuilder();
            if (hasModifier("u")) sb.a(EColors.lgreen, user, " ");
            sb.a(EColors.white, pid, " ");
            if (hasModifier("t")) sb.a(EColors.mc_lightpurple, start, " ", EColors.magenta, alive, " ");
            sb.a(EColors.lime, cmd);
            
            writeln(sb);
        }
    }
    
    private int longestCheck(int current, Object toCheck) {
        int len = EStringUtil.strlen(toCheck);
        if (len > current) return len;
        return current;
    }
    
}
