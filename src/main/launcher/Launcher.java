package main.launcher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import eutil.date.EDateTime;
import eutil.strings.StringUtil;
import main.QoT;

public class Launcher extends JFrame {

	//---------------
	// Static Runner
	//---------------
	
	private static final String INSTALL_PATH_SETTING = "INSTALL_PATH: ";
	
	private static Launcher launcher;
	private static File launcherDir;
	private static File launcherSettingsFile;
	
	public static Launcher getLauncher() { return launcher; }
	public static File getLauncherDir() { return launcherDir; }
	
	/** The actual launcher runner. */
	public static void runLauncher() {
		//attempt to create launcher directory
		if (!setupLauncherDir()) return;
		
		launcher = new Launcher();
	}
	
	/**
	 * Attempts to create the launchers directory and settings file to keep track of install path.
	 * 
	 * @return
	 */
	private static boolean setupLauncherDir() {
		try {
			String dir = Installer.getDefaultInstallDir();
			launcherDir = new File(dir + "\\QoT Launcher");
			
			//check if launcher directory already exists
			if (!launcherDir.exists() && !launcherDir.mkdirs()) {
				logErrorWithDialogBox("Cannot create the QoT Launcher directory!", "Setup Error!");
				return false;
			}
		}
		catch (Exception e) {
			logErrorWithDialogBox(e, "Cannot create the QoT Launcher directory!", "Setup Error!");
			return false;
		}
		
		//check if launcher settings file exists and create default if not
		try {
			launcherSettingsFile = new File(launcherDir, "settings");
			
			//create default launcher settings file
			if (!launcherSettingsFile.exists()) {
				var defaultSettings = new LauncherSettings();
				defaultSettings.INSTALL_DIR = Installer.getDefaultQoTInstallDir();
				updateLauncherSettingsFile(defaultSettings);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logErrorWithDialogBox(e, "Cannot create the QoT Launcher directory!", "Setup Error!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates the launcher settings file using the given launcher settings.
	 * 
	 * @param settings
	 * @throws Exception
	 */
	private static void updateLauncherSettingsFile(LauncherSettings settings) throws Exception {
		//create settings file for which to store launcher specific setting in
		launcherSettingsFile = new File(launcherDir, "settings");
		try (var writer = new FileWriter(launcherSettingsFile, Charset.forName("UTF-8"))) {
			String version = "QoT Version: " + QoT.version;
			writer.write(StringUtil.repeatString("-", version.length()) + "\n");
			writer.write(version);
			writer.write("\n" + StringUtil.repeatString("-", version.length()) + "\n");
			
			File installDir = (settings != null) ? settings.INSTALL_DIR : null;
			String dirPath = (installDir != null) ? installDir.getAbsolutePath() : "";
			writer.write("\n" + INSTALL_PATH_SETTING + dirPath);
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Parses the launcher settings file for the saved installation directory.
	 * 
	 * @return The parsed installation directory file
	 */
	private static File getInstallDir() {
		if (launcherSettingsFile == null) return null;
		File dir = null;
		
		try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(launcherSettingsFile)))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(INSTALL_PATH_SETTING)) {
					String path = line.substring(INSTALL_PATH_SETTING.length());
					if (path.isBlank() || path.isEmpty()) return null;
					dir = new File(path);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Launcher.log(e);
		}
		
		return dir;
	}
	
	//--------
	// Fields
	//--------
	
	/**
	 * Keeps track of whether or not the game is actually installed to the current
	 * install dir.
	 */
	private boolean installed = false;
	
	/**
	 * The active working settings for which QoT will be installed to and run with.
	 */
	private LauncherSettings launcherSettings;
	
	// Launcher window stuff -- can probably be made better!
	private JPanel contentPane;
	private JPanel backgroundPanel;
	private JPanel mainSelectionPanel;
	
	private JButton runOrInstall;
	private JButton changeInstallDir;
	private JButton settings;
	private JButton settings_back;
	
	private JLabel qotLogo;
	private JLabel logoLabel;
	private JLabel runLabel;
	private JLabel lblSettings;
	private JLabel installDirMenuLabel;
	private JLabel backLabel;
	private JLabel installDirOutputLabel;
	
	private JFileChooser fileChooser;
	
	private BufferedImage background;
	private BufferedImage selectionsBackground;
	
	//--------------
	// Constructors
	//--------------
	
	private Launcher() {
		//create launcher settings and grab the install dir from launcher settings
		launcherSettings = new LauncherSettings();
		launcherSettings.INSTALL_DIR = getInstallDir();
		
		//populate with default path if null
		if (launcherSettings.INSTALL_DIR == null) {
			launcherSettings.INSTALL_DIR = Installer.getDefaultQoTInstallDir();
		}
		
		//check if QoT is actually installed at the given installation directory
		checkInstalled();
		
		//init
		try {
			initWindow();
			initObjects();
		}
		catch (Exception e) {
			e.printStackTrace();
			Launcher.log(e);
		}
		
		//show
		setVisible(true);
	}
	
	/**
	 * Determines if QoT is actually installed at the current installation directory
	 */
	private boolean checkInstalled() {
		//verify that the dir is valid -- to some extent..
		ensureInstallDirEndsWithQoT();
		
		//check that dir exists and whether or not it is actually installed
		if (Installer.doesInstallDirExist(launcherSettings.INSTALL_DIR)) {
			return installed = Installer.verifyActuallyInstalled(launcherSettings.INSTALL_DIR);
		}
		
		return false;
	}
	
	//--------------
	// Init Methods
	//--------------
	
	private void initWindow() throws Exception {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Launcher.class.getResource("/textures/entities/whodundid/whodundid_base.png")));
		setTitle("QoT Launcher");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		int width = 700;
		int height = 550;
		Rectangle res = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		setBounds(res.width / 2 - width / 2, res.height / 2 - height /2, width, height);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	private void initObjects() throws Exception {
		logoLabel = new JLabel();
		logoLabel.setForeground(Color.WHITE);
		logoLabel.setBackground(Color.LIGHT_GRAY);
		logoLabel.setIcon(new ImageIcon(Launcher.class.getResource("/textures/general/qot_logo.png")));
		logoLabel.setBounds(236, 56, 211, 154);
		contentPane.add(logoLabel);
		
		selectionsBackground = ImageIO.read(Launcher.class.getResource("/textures/world/dungeon/dungenFloorA.png"));
		mainSelectionPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(selectionsBackground, 0, 0, getWidth(), getHeight(), this);
			}
		};
		mainSelectionPanel.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0)), null));
		mainSelectionPanel.setBackground(Color.LIGHT_GRAY);
		mainSelectionPanel.setBounds(207, 253, 266, 210);
		contentPane.add(mainSelectionPanel);
		mainSelectionPanel.setLayout(null);
		
		changeInstallDir = new JButton();
		changeInstallDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()) {
					@Override
					protected JDialog createDialog(Component parent) throws HeadlessException {
						JDialog dialog = super.createDialog(parent);
						dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
						return dialog;
					}
				};
				fileChooser.setCurrentDirectory(new File(Installer.getDefaultInstallDir()));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setApproveButtonText("Select");
				fileChooser.setDialogTitle("Select Install Directory");
				fileChooser.showOpenDialog(Launcher.this);
				
				File file = fileChooser.getSelectedFile();
				if (file != null) {
					//append 'QoT' to the end of the path if it doesn't already have it
					if (file.getName().endsWith("QoT")) file = new File(file, "QoT");
					
					//set path as install dir
					launcherSettings.INSTALL_DIR = file;
					checkInstalled();
					installDirOutputLabel.setText(file.getAbsolutePath());
					
					//attempt to update settings file path
					try {
						updateLauncherSettingsFile(launcherSettings);
					}
					catch (Exception ee) {
						ee.printStackTrace();
						Launcher.log(ee);
					}
				}
			}
		});
		changeInstallDir.setIcon(new ImageIcon(Launcher.class.getResource("/textures/window/folder.png")));
		changeInstallDir.setPressedIcon(new ImageIcon(Launcher.class.getResource("/textures/window/folder_sel.png")));
		changeInstallDir.setOpaque(false);
		changeInstallDir.setFocusPainted(false);
		changeInstallDir.setContentAreaFilled(false);
		changeInstallDir.setBorderPainted(false);
		changeInstallDir.setBounds(20, 61, 40, 40);
		changeInstallDir.setVisible(false);
		mainSelectionPanel.add(changeInstallDir);
		
		installDirMenuLabel = new JLabel();
		installDirMenuLabel.setBounds(20, 15, 233, 29);
		installDirMenuLabel.setIcon(new ImageIcon(Launcher.class.getResource("/textures/launcher/install_dir.png")));
		installDirMenuLabel.setVisible(false);
		mainSelectionPanel.add(installDirMenuLabel);
		
		runOrInstall = new JButton();
		runOrInstall.setBounds(50, 61, 40, 40);
		runOrInstall.setOpaque(false);
		runOrInstall.setFocusPainted(false);
		runOrInstall.setContentAreaFilled(false);
		runOrInstall.setBorderPainted(false);
		runOrInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkInstalled()) {
					closeLauncher();
					QoT.startGame(launcherSettings);
				}
				else {
					try {
						switch (Installer.createInstallDir(launcherSettings.INSTALL_DIR)) {
						case SUCCESS:
							JOptionPane.showMessageDialog(Launcher.this, "Installation complete!");
							installed = true;
							runLabel.setIcon(new ImageIcon(Launcher.class.getResource("/textures/launcher/run.png")));
							break;
						case FAILED:
							JOptionPane.showMessageDialog(Launcher.this, "Failed to create game local directory!");
							break;
						default:
							break;
						}
					}
					catch (Exception ee) {
						logErrorWithDialogBox(ee,
											  "Something went wrong!",
											  "Installation Error",
											  "Check the error log at: '" + launcherDir + "'");
					}
				}
			}
		});
		runOrInstall.setIcon(new ImageIcon(Launcher.class.getResource("/textures/window/play.png")));
		runOrInstall.setPressedIcon(new ImageIcon(Launcher.class.getResource("/textures/window/play_sel.png")));
		mainSelectionPanel.add(runOrInstall);
		
		runLabel = new JLabel("");
		runLabel.setBounds(100, 61, 129, 40);
		if (installed) runLabel.setIcon(new ImageIcon(Launcher.class.getResource("/textures/launcher/run.png")));
		else runLabel.setIcon(new ImageIcon(Launcher.class.getResource("/textures/launcher/install.png")));
		runLabel.setForeground(Color.gray.brighter());
		mainSelectionPanel.add(runLabel);
		
		settings = new JButton();
		settings.setBounds(50, 124, 40, 40);
		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSettingsMenu(true);
			}
		});
		settings.setOpaque(false);
		settings.setFocusPainted(false);
		settings.setContentAreaFilled(false);
		settings.setBorderPainted(false);
		settings.setIcon(new ImageIcon(Launcher.class.getResource("/textures/window/settings.png")));
		settings.setPressedIcon(new ImageIcon(Launcher.class.getResource("/textures/window/settings_sel.png")));
		mainSelectionPanel.add(settings);
		
		lblSettings = new JLabel();
		lblSettings.setBounds(100, 124, 129, 40);
		lblSettings.setIcon(new ImageIcon(Launcher.class.getResource("/textures/launcher/settings.png")));
		lblSettings.setForeground(Color.gray.brighter());
		mainSelectionPanel.add(lblSettings);
		
		installDirOutputLabel = new JLabel(String.valueOf(launcherSettings.INSTALL_DIR));
		installDirOutputLabel.setForeground(Color.LIGHT_GRAY);
		installDirOutputLabel.setHorizontalAlignment(SwingConstants.LEFT);
		installDirOutputLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
		installDirOutputLabel.setBounds(70, 61, 183, 40);
		installDirOutputLabel.setVisible(false);
		mainSelectionPanel.add(installDirOutputLabel);
		
		settings_back = new JButton();
		settings_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSettingsMenu(false);
			}
		});
		settings_back.setOpaque(false);
		settings_back.setFocusPainted(false);
		settings_back.setContentAreaFilled(false);
		settings_back.setBorderPainted(false);
		settings_back.setBounds(50, 124, 40, 40);
		settings_back.setIcon(new ImageIcon(Launcher.class.getResource("/textures/window/back.png")));
		settings_back.setPressedIcon(new ImageIcon(Launcher.class.getResource("/textures/window/back_sel.png")));
		settings_back.setVisible(false);
		mainSelectionPanel.add(settings_back);
		
		backLabel = new JLabel();
		backLabel.setForeground(new Color(182, 182, 182));
		backLabel.setIcon(new ImageIcon(Launcher.class.getResource("/textures/launcher/back.png")));
		backLabel.setBounds(100, 124, 129, 40);
		backLabel.setVisible(false);
		mainSelectionPanel.add(backLabel);
		
		background = ImageIO.read(Launcher.class.getResource("/textures/world/dungeon/dungWall.png"));
		backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
			}
		};
		backgroundPanel.setBounds(0, 0, 684, 511);
		backgroundPanel.setLayout(null);
		contentPane.add(backgroundPanel);
	}
	
	/**
	 * Garbage way of 'changing' the current """screen""". Smile :)
	 */
	private void openSettingsMenu(boolean val) {
		runLabel.setVisible(!val);
		runOrInstall.setVisible(!val);
		settings.setVisible(!val);
		lblSettings.setVisible(!val);
		
		changeInstallDir.setVisible(val);
		installDirMenuLabel.setVisible(val);
		settings_back.setVisible(val);
		installDirOutputLabel.setVisible(val);
		backLabel.setVisible(val);
	}
	
	/**
	 * Makes several checks against the mapped installation directory to confirm
	 * that the chosen directory is actually valid and ends with 'QoT'.
	 */
	private void ensureInstallDirEndsWithQoT() {
		//if any of the following checks regarding the current installation directory
		//fail, then the install directory will be reset to the default one
		
		//grab the current directory
		File dir = launcherSettings.INSTALL_DIR;
		
		//ensure the directory actually exists
		if (dir == null) launcherSettings.INSTALL_DIR = Installer.getDefaultQoTInstallDir();
		
		//ensure that the directory is not actually empty
		String path = dir.getAbsolutePath();
		if (path.isBlank() || path.isEmpty()) {
			launcherSettings.INSTALL_DIR = Installer.getDefaultQoTInstallDir();
		}
		
		//verify that the given directory even exists
		if (dir.exists()) {
			launcherSettings.INSTALL_DIR = Installer.getDefaultQoTInstallDir();
		}
		
		//ensure that the directory is actually a directory
		if (!dir.isDirectory()) {
			launcherSettings.INSTALL_DIR = Installer.getDefaultQoTInstallDir();
		}
		
		//ensure that the directory is actually named 'QoT'
		if (!dir.getName().equals("QoT")) {
			launcherSettings.INSTALL_DIR = Installer.getDefaultQoTInstallDir();
		}
	}
	
	/**
	 * Closes the launcher.
	 */
	public static void closeLauncher() {
		if (launcher == null) return;
		log("Closing launcher! Have a nice day :)");
		launcher.dispatchEvent(new WindowEvent(launcher, WindowEvent.WINDOW_CLOSING));
		launcher.dispose();
		launcher = null;
	}
	
	//----------------------------------------------------------------
	// Log handlers for when not running in a development environment
	//----------------------------------------------------------------
	
	private static File logFile = null;
	
	private static File createLogFile() {
		if (logFile != null) return logFile;
		return logFile = new File(Launcher.getLauncherDir(), "LOG_" + EDateTime.getDate() +
															 "_" + EDateTime.getTime() + ".log");
	}
	
	public static void log(Object err) {
		File log = createLogFile();
		try (var fos = new FileOutputStream(log, true);
			 var str = new PrintStream(fos))
		{
			str.append(String.valueOf(err));
			str.append('\n');
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void log(Exception e) {
		File log = createLogFile();
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
	
	public static void logWithDialogBox(Object e, String boxTitle, Object... args) {
		String err = String.valueOf(e);
		String additionalArgs = StringUtil.toString(args, "\n");
		
		JOptionPane.showInternalInputDialog(null, err + "\n\n" + additionalArgs, boxTitle, JOptionPane.INFORMATION_MESSAGE);
		log(err);
	}
	
	public static void logWithDialogBox(Exception e, String message, String boxTitle, Object... args) {
		String err = String.valueOf(message);
		String additionalArgs = StringUtil.toString(args);
		
		JOptionPane.showInternalInputDialog(null, err + "\n\n" + additionalArgs, boxTitle, JOptionPane.INFORMATION_MESSAGE);
		log(e);
	}
	
	public static void logErrorWithDialogBox(Object e, String boxTitle, Object... args) {
		String err = String.valueOf(e);
		String additionalArgs = StringUtil.toString(args, "\n");
		
		JOptionPane.showInternalInputDialog(null, err + "\n\n" + additionalArgs, boxTitle, JOptionPane.ERROR_MESSAGE);
		log(err);
	}
	
	public static void logErrorWithDialogBox(Exception e, String message, String boxTitle, Object... args) {
		String err = String.valueOf(message);
		String additionalArgs = StringUtil.toString(args);
		
		JOptionPane.showInternalInputDialog(null, err + "\n\n" + additionalArgs, boxTitle, JOptionPane.ERROR_MESSAGE);
		log(e);
	}
	
}
