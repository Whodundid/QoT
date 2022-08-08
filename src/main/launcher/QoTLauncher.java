package main.launcher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import eutil.swing.ActionPerformer;
import eutil.swing.LeftClick;
import main.Main;
import main.QoT;

public class QoTLauncher extends JFrame {
	
	private static QoTLauncher launcher;
	
	static boolean inJar = false;
	static String resourcePath = "";
	
	//---------------
	// Static Runner
	//---------------
	
	/** The actual launcher runner. */
	public static void runLauncher() {
		//this line is used to specifically grab the class system's file structure to determine
		//what kind of environment the game is being executed from (an IDE or a Jar)
		String path = Main.class.getResource(Main.class.getSimpleName() + ".class").getFile();
		
		//if path does not start with a '/' then it's very likely a jar file!
		if (!path.startsWith("/")) {
			inJar = true;
			resourcePath = "resources/";
		}
		
		//attempt to create launcher directory
		if (!LauncherDir.setupLauncherDir()) return;
		
		LauncherLogger.log("Starting QoT Launcher!");
		LauncherLogger.log("runLauncher=" + LauncherDir.runLauncher);
		
		if (LauncherDir.runLauncher) {
			launcher = new QoTLauncher();
		}
	}
	
	public static QoTLauncher getLauncher() { return launcher; }
	
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
	private JCheckBox forceReinstall;
	
	private JLabel qotLogo;
	private JLabel logoLabel;
	private JLabel runLabel;
	private JLabel lblSettings;
	private JLabel installDirMenuLabel;
	private JLabel backLabel;
	private JLabel installDirOutputLabel;
	
	private JFileChooser fileChooser;
	
	//-----------------
	// image resources
	private BufferedImage
		programIcon,
		logo,
		background,
		selectionsBackground,
	
		playButton,
		playButtonSel,
		runText,
		installText,
	
		settingsButton,
		settingsButtonSel,
		settingsText,
	
		folderButton,
		folderButtonSel,
		installDirText,
	
		backButton,
		backButtonSel,
		backText;
	//-----------------
	
	//--------------
	// Constructors
	//--------------
	
	private QoTLauncher() {
		//create launcher settings and grab the install dir from launcher settings
		launcherSettings = new LauncherSettings();
		launcherSettings.INSTALL_DIR = LauncherDir.getInstallDir();
		launcherSettings.IN_JAR = inJar;
		LauncherLogger.log("Parsed install dir of: '" + launcherSettings.INSTALL_DIR + "'");
		
		loadLauncherResources();
		
		//init launcher window
		init();
	}
	
	private QoTLauncher(LauncherSettings settings) {
		launcherSettings = settings;
		
		//init launcher window
		init();
	}
	
	//------------------
	// Resource Helpers
	//------------------
	
	private void loadLauncherResources() {
		try {
			LauncherLogger.log("Loading launcher resources...");
			var dir = "/" + resourcePath + "textures/launcher/";
			
			programIcon = loadResource(dir, "whodundid_base.png");
			logo = loadResource(dir, "qot_logo.png");
			background = loadResource(dir, "background.png");
			selectionsBackground = loadResource(dir, "options_back.png");
		
			playButton = loadResource(dir, "play.png");
			playButtonSel = loadResource(dir, "play_sel.png");
			runText = loadResource(dir, "run_text.png");
			installText = loadResource(dir, "install_text.png");
		
			settingsButton = loadResource(dir, "settings.png");
			settingsButtonSel = loadResource(dir, "settings_sel.png");
			settingsText = loadResource(dir, "settings_text.png");
		
			folderButton = loadResource(dir, "folder.png");
			folderButtonSel = loadResource(dir, "folder_sel.png");
			installDirText = loadResource(dir, "install_dir_text.png");
		
			backButton = loadResource(dir, "back.png");
			backButtonSel = loadResource(dir, "back_sel.png");
			backText = loadResource(dir, "back_text.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage loadResource(String base, String file) {
		try {
			if (base.endsWith("/") && file.startsWith("/")) file = file.substring(0, file.length() - 1);
			if (!base.endsWith("/") && !file.startsWith("/")) base += "/";
			return ImageIO.read(QoTLauncher.class.getResource(base + file));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//--------------
	// Init Methods
	//--------------
	
	private void init() {
		LauncherLogger.log("Initializing launcher...");
		
		//populate with default path if null
		if (launcherSettings.INSTALL_DIR == null) {
			launcherSettings.INSTALL_DIR = QoTInstaller.getDefaultQoTInstallDir();
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
			LauncherLogger.logError(e);
		}
		
		//show
		setVisible(true);
	}
	
	private void initWindow() throws Exception {
		setResizable(false);
		setIconImage(programIcon);
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
		logoLabel.setIcon(new ImageIcon(logo));
		logoLabel.setBounds(236, 56, 211, 154);
		contentPane.add(logoLabel);
		
		mainSelectionPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(selectionsBackground, 0, 0, getWidth(), getHeight(), this);
			}
		};
		mainSelectionPanel.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0)), null));
		mainSelectionPanel.setBounds(207, 253, 266, 210);
		contentPane.add(mainSelectionPanel);
		mainSelectionPanel.setLayout(null);
		
		changeInstallDir = new JButton();
		changeInstallDir.addActionListener(ActionPerformer.of(() -> openDirSelection()));
		changeInstallDir.setToolTipText("Modify installation directory");
		changeInstallDir.setIcon(new ImageIcon(folderButton));
		changeInstallDir.setPressedIcon(new ImageIcon(folderButtonSel));
		changeInstallDir.setOpaque(false);
		changeInstallDir.setFocusPainted(false);
		changeInstallDir.setContentAreaFilled(false);
		changeInstallDir.setBorderPainted(false);
		changeInstallDir.setBounds(20, 61, 40, 40);
		changeInstallDir.setVisible(false);
		mainSelectionPanel.add(changeInstallDir);
		
		installDirMenuLabel = new JLabel();
		installDirMenuLabel.setBounds(20, 15, 233, 29);
		installDirMenuLabel.setIcon(new ImageIcon(installDirText));
		installDirMenuLabel.setVisible(false);
		mainSelectionPanel.add(installDirMenuLabel);
		
		runOrInstall = new JButton();
		runOrInstall.addActionListener(ActionPerformer.of(() -> tryRunOrInstall()));
		runOrInstall.setBounds(50, 61, 40, 40);
		runOrInstall.setOpaque(false);
		runOrInstall.setFocusPainted(false);
		runOrInstall.setContentAreaFilled(false);
		runOrInstall.setBorderPainted(false);
		runOrInstall.setIcon(new ImageIcon(playButton));
		runOrInstall.setPressedIcon(new ImageIcon(playButtonSel));
		mainSelectionPanel.add(runOrInstall);
		
		runLabel = new JLabel();
		runLabel.addMouseListener(LeftClick.of(() -> tryRunOrInstall()));
		runLabel.setBounds(100, 61, 129, 40);
		if (installed) runLabel.setIcon(new ImageIcon(runText));
		else runLabel.setIcon(new ImageIcon(installText));
		runLabel.setForeground(Color.gray.brighter());
		mainSelectionPanel.add(runLabel);
		
		settings = new JButton();
		settings.addActionListener(ActionPerformer.of(() -> openSettingsMenu(true)));
		settings.setToolTipText("Modify installation properties");
		settings.setBounds(50, 124, 40, 40);
		settings.setOpaque(false);
		settings.setFocusPainted(false);
		settings.setContentAreaFilled(false);
		settings.setBorderPainted(false);
		settings.setIcon(new ImageIcon(settingsButton));
		settings.setPressedIcon(new ImageIcon(settingsButtonSel));
		mainSelectionPanel.add(settings);
		
		lblSettings = new JLabel();
		lblSettings.addMouseListener(LeftClick.of(() -> openSettingsMenu(true)));
		lblSettings.setToolTipText("Modify installation properties");
		lblSettings.setBounds(100, 124, 129, 40);
		lblSettings.setIcon(new ImageIcon(settingsText));
		lblSettings.setForeground(Color.gray.brighter());
		mainSelectionPanel.add(lblSettings);
		
		installDirOutputLabel = new JLabel(String.valueOf(launcherSettings.INSTALL_DIR));
		installDirOutputLabel.addMouseListener(LeftClick.of(() -> openDirSelection()));
		installDirOutputLabel.setToolTipText(String.valueOf(launcherSettings.INSTALL_DIR));
		installDirOutputLabel.setForeground(Color.LIGHT_GRAY);
		installDirOutputLabel.setHorizontalAlignment(SwingConstants.LEFT);
		installDirOutputLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
		installDirOutputLabel.setBounds(70, 61, 183, 40);
		installDirOutputLabel.setVisible(false);
		mainSelectionPanel.add(installDirOutputLabel);
		
		settings_back = new JButton();
		settings_back.addActionListener(ActionPerformer.of(() -> openSettingsMenu(false)));
		settings_back.setOpaque(false);
		settings_back.setFocusPainted(false);
		settings_back.setContentAreaFilled(false);
		settings_back.setBorderPainted(false);
		settings_back.setBounds(50, 150, 40, 40);
		settings_back.setIcon(new ImageIcon(backButton));
		settings_back.setPressedIcon(new ImageIcon(backButtonSel));
		settings_back.setVisible(false);
		mainSelectionPanel.add(settings_back);
		
		backLabel = new JLabel();
		backLabel.addMouseListener(LeftClick.of(() -> openSettingsMenu(false)));
		backLabel.setIcon(new ImageIcon(backText));
		backLabel.setBounds(100, 150, 129, 40);
		backLabel.setVisible(false);
		mainSelectionPanel.add(backLabel);
		
		forceReinstall = new JCheckBox("Force Re-Install");
		forceReinstall.setToolTipText("Overwrites any custom resources within the install path");
		forceReinstall.setBounds(70, 112, 199, 23);
		forceReinstall.setVisible(false);
		forceReinstall.setOpaque(false);
		forceReinstall.setForeground(Color.LIGHT_GRAY);
		mainSelectionPanel.add(forceReinstall);
		
		backgroundPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
			}
		};
		backgroundPanel.setBounds(0, 0, 684, 511);
		backgroundPanel.setLayout(null);
		contentPane.add(backgroundPanel);
		
		updateForceReinstallVisibility();
	}
	
	//-------------------------
	// Internal Window Methods
	//-------------------------
	
	/**
	 * Garbage way of 'changing' the current """screen""". Smile :)
	 * 
	 * @param val True if switching to the settings screen
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
		forceReinstall.setVisible(installed && val);
		backLabel.setVisible(val);
	}
	
	private void updateForceReinstallVisibility() {
		if (installed) {
			settings_back.setBounds(50, 150, 40, 40);
			backLabel.setBounds(100, 150, 129, 40);
		}
		else {
			settings_back.setBounds(50, 124, 40, 40);
			backLabel.setBounds(100, 124, 129, 40);
		}
	}
	
	private void openDirSelection() {
		File curDir = launcherSettings.INSTALL_DIR;
		
		//if the path already ends with 'QoT' use the parent directory instead
		if (launcherSettings.INSTALL_DIR.getName().endsWith("QoT")) {
			if (launcherSettings.INSTALL_DIR.getParentFile() != null) {
				curDir = launcherSettings.INSTALL_DIR.getParentFile();
			}
		}
		
		fileChooser = new JFileChooser(curDir) {
			@Override
			protected JDialog createDialog(Component parent) throws HeadlessException {
				JDialog dialog = super.createDialog(parent);
				dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
				return dialog;
			}
		};
		//fileChooser.setCurrentDirectory(new File(Installer.getDefaultInstallDir()));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setApproveButtonText("Select");
		fileChooser.setDialogTitle("Select Install Directory");
		fileChooser.showOpenDialog(QoTLauncher.this);
		
		File file = fileChooser.getSelectedFile();
		if (file != null) {
			//append 'QoT' to the end of the path if it doesn't already have it
			//System.out.println("FILE NAME: " + file.getName());
			//if (!file.getName().endsWith("QoT")) file = new File(file, "QoT");
			
			//set path as install dir
			LauncherLogger.log("Changing install path to: '" + file + "'");
			launcherSettings.INSTALL_DIR = file;
			checkInstalled();
			if (installed) runLabel.setIcon(new ImageIcon(runText));
			else runLabel.setIcon(new ImageIcon(installText));
			installDirOutputLabel.setText(file.getAbsolutePath());
			
			//update button positions and force reinstall checkbox visibility
			updateForceReinstallVisibility();
			
			//attempt to update settings file path
			try {
				LauncherDir.updateLauncherSettingsFile(launcherSettings);
			}
			catch (Exception ee) {
				ee.printStackTrace();
				LauncherLogger.logError(ee);
			}
		}
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void tryRunOrInstall() {
		if (forceReinstall != null && forceReinstall.isSelected()) {
			LauncherLogger.log("\nAttempting to reinstall and launch after...");
			tryInstall(true);
		}
		else if (!checkInstalled()) {
			LauncherLogger.log("\nAttempting to install...");
			tryInstall(false);
		}
		else {
			LauncherLogger.log("\nAttempting to launch game...");
			closeLauncher();
			QoT.startGame(launcherSettings);
		}
	}
	
	private void tryInstall(boolean runAfter) {
		try {
			switch (QoTInstaller.createInstallDir(launcherSettings.INSTALL_DIR)) {
			case SUCCESS:
				if (checkInstalled()) {
					LauncherLogger.logWithDialogBox("Installation success", "Installation complete!");
					installed = true;
					runLabel.setIcon(new ImageIcon(runText));
					
					if (runAfter) {
						closeLauncher();
						QoT.startGame(launcherSettings);
					}
				}
				else {
					LauncherLogger.logWithDialogBox("Something went wrong!", "Verify Error", "Was not actually able to verify install!");
				}
				break;
			case FAILED:
				LauncherLogger.logErrorWithDialogBox("Failed to create game local directory!", "Installation Error");
				break;
			default:
				break;
			}
		}
		catch (Exception ee) {
			LauncherLogger.logErrorWithDialogBox(ee,
								  "Something went wrong!",
								  "Installation Error",
								  "Check the error log at: '" + LauncherDir.getLauncherDir() + "'");
		}
		
		updateForceReinstallVisibility();
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
		if (dir == null) launcherSettings.INSTALL_DIR = QoTInstaller.getDefaultQoTInstallDir();
		
		//ensure that the directory is not actually empty
		String path = dir.getAbsolutePath();
		if (path.isBlank() || path.isEmpty()) {
			launcherSettings.INSTALL_DIR = QoTInstaller.getDefaultQoTInstallDir();
		}
	}
	
	/**
	 * Determines if QoT is actually installed at the current installation directory
	 */
	private boolean checkInstalled() {
		//verify that the dir is valid -- to some extent..
		ensureInstallDirEndsWithQoT();
		
		//check that dir exists and whether or not it is actually installed
		if (QoTInstaller.doesInstallDirExist(launcherSettings.INSTALL_DIR)) {
			return installed = QoTInstaller.verifyActuallyInstalled(launcherSettings.INSTALL_DIR);
		}
		
		return false;
	}
	
	//----------------
	// Static Methods
	//----------------
	
	/**
	 * Closes the launcher.
	 */
	public static void closeLauncher() {
		if (launcher == null) return;
		LauncherLogger.log("Closing launcher! Have a nice day :)\n");
		launcher.dispatchEvent(new WindowEvent(launcher, WindowEvent.WINDOW_CLOSING));
		launcher.dispose();
		launcher = null;
	}
	
}
