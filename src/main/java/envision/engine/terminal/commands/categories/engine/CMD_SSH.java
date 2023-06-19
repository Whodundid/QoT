//package envision.engine.terminal.commands.categories.engine;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.Session;
//
//import envision.engine.terminal.commands.TerminalCommand;
//import envision.engine.terminal.window.ETerminalWindow;
//import eutil.datatypes.util.EList;
//import eutil.strings.EStringUtil;
//
//// Author: Hunter Bragg
//
//public class CMD_SSH extends TerminalCommand {
//    
//    private static SSH_Reader cur_reader;
//    private static Thread readerThread;
//    
//    public CMD_SSH() {
//        setCategory("System");
//        expectedArgLength = 1;
//    }
//    
//    @Override public String getName() { return "ssh"; }
//    @Override public String getHelpInfo(boolean runVisually) { return "ssh."; }
//    @Override public String getUsage() { return "ex: ssh user@{hostname}"; }
//    
//    @Override
//    public void runCommand() throws Exception {
//        if (args().isEmpty()) {
//            errorUsage("Empty args!");
//            return;
//        }
//        
//        if (cur_reader == null) {
//            cur_reader = new SSH_Reader(term(), args());
//            readerThread = new Thread(cur_reader);
//            readerThread.start();
//        }
//        else {
//            cur_reader.sendInput(term(), args());
//        }
//    }
//    
//    private static class SSH_Reader implements Runnable {
//        ETerminalWindow term;
//        InputStream is;
//        OutputStream os;
//        InputStreamReader isr;
//        OutputStreamWriter osw;
//        BufferedReader reader;
//        BufferedWriter writer;
//        JSch jsch;
//        Session session;
//        Channel channel;
//        
//        private SSH_Reader(ETerminalWindow termIn, EList<String> args) throws Exception {
//            term = termIn;
//            
//            jsch = new JSch();
//            jsch.setKnownHosts("C:/Users/Hunter/.ssh/known_hosts");
//            session = jsch.getSession("hunter", "192.168.0.10", 22);
//            session.setPassword("Aringoings152");
//            session.connect(10000);
//            channel = session.openChannel("shell");
//            channel.setInputStream(System.in);
//            is = channel.getInputStream();
//            os = channel.getOutputStream();
//            isr = new InputStreamReader(is);
//            osw = new OutputStreamWriter(os);
//            reader = new BufferedReader(isr);
//            writer = new BufferedWriter(osw);
//            channel.connect(3 * 1000);
//        }
//        
//        public void sendInput(ETerminalWindow termIn, EList<String> input) {
//            if (writer == null) return;
//            
//            System.out.println(writer + " : " + System.in);
//            
//            try {
//                var args = EStringUtil.combineAll(input, " ");
//                writer.write(args);
//                writer.flush();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//                termIn.error(e);
//            }
//        }
//        
//        public void kill() throws IOException {
//            session.disconnect();
//            channel.disconnect();
//            
//            reader.close();
//            writer.close();
//            isr.close();
//            osw.close();
//            is.close();
//            os.close();
//        }
//        
//        @Override
//        public void run() {
//            final var header = term.getHeader();
//            final var oldHeader = header.getTitle();
//            
//            while (channel.isConnected()) {
//                header.setTitle(oldHeader + " ssh " + session.getHost());
//                
//                try {
//                    String n = null;
//                    while ((n = reader.readLine()) != null) {
//                        System.out.print(n);
//                        synchronized (term.getChildren()) {
//                            term.writeln(n);                            
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }
//            
//            header.setTitle(oldHeader);
//        }
//        
//    }
//    
//}
