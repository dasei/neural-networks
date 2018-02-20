package org.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.activationFunctions.ActivationFunction;
import org.network.Network;

import com.sun.glass.events.KeyEvent;

public class GUI extends JFrame {

	private Network network;
	private File networkDataFile;
	private File lastCreatedFile;

	// -- JAVA SWING things --
	private JPanel pMainCard;

	// 'create new file' panel (CNF = create new file)
	private JLabel lCNFHeader;
	private JTextField txtCNFName;
	private JLabel lCNFNameError;
	private JTextField txtCNFLayout;
	private JLabel lCNFLayoutError;
	private JComboBox<String> cbCNFActivationFunction;
	private JLabel lCNFMainError;

	// 'import file' panel (IF = import file)
	private JTextField txtIFName;

	// 'export file' panel (EF = export file)
	private JTextField txtEFPath;

	public static final String CARDTYPE_CREATENEWFILE = "createnewfile";
	public static final String CARDTYPE_IMPORTFILE = "importfile";
	public static final String CARDTYPE_EXPORTFILE = "exportfile";

	private static final int DEFAULT_RIGID_AREA_SIZE = 15;

	public GUI() {
		// @formatter:off
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		// top menu bar
		JMenuBar menuBar = new JMenuBar();
			// first (leftmost) menu
			JMenu menuFile = new JMenu("File");
			menuFile.setMnemonic(KeyEvent.VK_F);
			menuFile.getAccessibleContext().setAccessibleDescription("ex/import file");
			
				menuFile.addSeparator();
			
				// JMenuItems (create new file, import, export, etc.)
				
				JMenuItem menuItemCreateNewFile = new JMenuItem("create new file", new ImageIcon("icons/CreateNewFile.jpg"));
				menuItemCreateNewFile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onMenuClickCNF();
					}
				});
				menuFile.add(menuItemCreateNewFile);
				
				JMenuItem menuItemImport = new JMenuItem("import file");
				menuItemImport.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onMenuClickIF();
					}
				});
				menuFile.add(menuItemImport);
				
				JMenuItem menuItemExport = new JMenuItem("export file");
				menuItemExport.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onMenuClickEF();
					}
				});
				menuFile.add(menuItemExport);
				
				menuFile.addSeparator();
				
			menuBar.add(menuFile);
		this.setJMenuBar(menuBar);
		
		
		// main JPanel
		pMainCard = new JPanel();
		pMainCard.setLayout(new CardLayout());
		//pMainCard.setPreferredSize(new Dimension(480,270));
		pMainCard.setPreferredSize(new Dimension(400,300));
		this.getContentPane().add(pMainCard, BorderLayout.CENTER);
		
		
		// create new file panel (CNF)
		JPanel pCNF = new JPanel();
		pCNF.setLayout(new BoxLayout( pCNF, BoxLayout.Y_AXIS));
// -- 			components of 'create new file' panel:
				
		
		
		
			lCNFHeader = new JLabel("Create a new Neural Network");
			lCNFHeader.setAlignmentX(Container.CENTER_ALIGNMENT);
			lCNFHeader.setFont(new Font("arial", Font.BOLD, 25));		
			pCNF.add(lCNFHeader);
						
			
			
			
			pCNF.add(Box.createRigidArea(new Dimension(0, DEFAULT_RIGID_AREA_SIZE)));
				
		
		
		
			JPanel pCNFName = new JPanel();
				JLabel lMarkCNFName = new JLabel("Name:");
				pCNFName.add(lMarkCNFName);
			
				txtCNFName = new JTextField(15);
//				txtCNFName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				pCNFName.add(txtCNFName);
				
				JLabel lMarkCNFTxt = new JLabel(".txt");
				pCNFName.add(lMarkCNFTxt);
			pCNF.add(pCNFName);
			
			// CNF name error label
			lCNFNameError = new JLabel("name invalid!");
			lCNFNameError.setAlignmentX(Container.CENTER_ALIGNMENT);
			lCNFNameError.setForeground(Color.red);
			lCNFNameError.setVisible(false);
			pCNF.add(lCNFNameError);
			
			
			
			
			pCNF.add(Box.createRigidArea(new Dimension(0,DEFAULT_RIGID_AREA_SIZE)));
			
			
			
			
			JPanel pCNFLayout = new JPanel();				
				JLabel lMarkCNFLayout = new JLabel("Layout:");
				pCNFLayout.add(lMarkCNFLayout);
				
				txtCNFLayout = new JTextField(15);
//				txtCNFLayout.setBorder(BorderFactory.createLineBorder(Color.black));
				pCNFLayout.add(txtCNFLayout);
			pCNF.add(pCNFLayout);
			
			// CNF layout error label
			lCNFLayoutError = new JLabel();
			lCNFLayoutError.setAlignmentX(Container.CENTER_ALIGNMENT);
			lCNFLayoutError.setForeground(Color.red);
			lCNFLayoutError.setVisible(false);
			pCNF.add(lCNFLayoutError);
			
			JLabel lMarkCNFLayoutEG = new JLabel("(e.g. '9, 7, 7, 4')");
			lMarkCNFLayoutEG.setAlignmentX(Container.CENTER_ALIGNMENT);
			pCNF.add(lMarkCNFLayoutEG);
			
			
			
			
			pCNF.add(Box.createRigidArea(new Dimension(0,DEFAULT_RIGID_AREA_SIZE)));
			
			
			
			
			JPanel pCNFActivationFunction = new JPanel();
				JLabel lCNFActivationFunction = new JLabel("Activation Function:");
				pCNFActivationFunction.add(lCNFActivationFunction);
				
				String[] activationFunctions = {"Sigmoid", "Linear", "ReLU", "LeakyReLU"};
				cbCNFActivationFunction = new JComboBox<String>(activationFunctions);
				pCNFActivationFunction.add(cbCNFActivationFunction);				
			pCNF.add(pCNFActivationFunction);			
			
			
			
			
			pCNF.add(Box.createRigidArea(new Dimension(0, DEFAULT_RIGID_AREA_SIZE)));
			
			
			
			//TODO add button that combines creation and import in one click
			JPanel pCNFCreate = new JPanel();
			pCNFCreate.setAlignmentX(Container.CENTER_ALIGNMENT);
				JButton bCNF = new JButton("create");				
				bCNF.addActionListener(new ActionListener() {				
					public void actionPerformed(ActionEvent e) {
						onButtonClickCNF();
					}
				});			
				pCNFCreate.add(bCNF);
				
				JButton bCNFAndImport = new JButton("create + import");				
				bCNFAndImport.addActionListener(new ActionListener() {				
					public void actionPerformed(ActionEvent e) {
						onButtonClickCNFAndImport();						
					}
				});			
				pCNFCreate.add(bCNFAndImport);
			pCNF.add(pCNFCreate);
			
			
			
			
			lCNFMainError = new JLabel("main error label");
			lCNFMainError.setForeground(Color.red);
			lCNFMainError.setVisible(false);
			lCNFMainError.setAlignmentX(Container.CENTER_ALIGNMENT);
			pCNF.add(lCNFMainError);
			
			
			
			
			pCNF.add(Box.createRigidArea(new Dimension(0,9000)));
// --
		pMainCard.add(pCNF, CARDTYPE_CREATENEWFILE);
		
		// import file panel (IF)
		JPanel pImport = new JPanel();
		pImport.setBackground(Color.red);
// --			components of 'import file' panel
			JLabel lMarkIFName = new JLabel("Name:");
			pImport.add(lMarkIFName);
		
			txtIFName = new JTextField();
			pImport.add(txtIFName);
			
			JLabel lMarkIFTxt = new JLabel(".txt");
			pImport.add(lMarkIFTxt);
			
			JButton bIF = new JButton("Import");
			bIF.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onButtonClickIF();
				}
			});
			pImport.add(bIF);
// --
		pMainCard.add(pImport, CARDTYPE_IMPORTFILE);
		
		// export file panel
		JPanel pExport = new JPanel();
		pExport.setBackground(Color.blue);
		pMainCard.add(pExport, CARDTYPE_EXPORTFILE);
		
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		// @formatter:on
	}

	private void switchMainPanelTo(String cardType) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((CardLayout) pMainCard.getLayout()).show(pMainCard, cardType);
			}
		});
	}

	private void onMenuClickCNF() {
		switchMainPanelTo(CARDTYPE_CREATENEWFILE);
	}

	private void onMenuClickIF() {
		switchMainPanelTo(CARDTYPE_IMPORTFILE);
	}

	private void onMenuClickEF() {
		switchMainPanelTo(CARDTYPE_EXPORTFILE);
	}

	private void onButtonClickCNFAndImport() {
		if (onButtonClickCNF()) {
			importNetwork(lastCreatedFile);
		}
	}

	private boolean onButtonClickCNF() {
		// firstly reset all error displays
		lCNFNameError.setVisible(false);
		lCNFLayoutError.setVisible(false);
		lCNFMainError.setVisible(false);

		String name = txtCNFName.getText();

		////////
		//// check if NAME is valid
		char[] notSupportedChars = { '|', '<', '>', '/', '\\', ':', '*', '?', '"' };
		for (char cStr : name.toCharArray())
			for (char c : notSupportedChars)
				if (cStr == c) {
					lCNFNameError.setText("invalid character: '" + cStr + "'");
					lCNFNameError.setVisible(true);
					return false;
				}
		if (name.equals("")) {
			lCNFNameError.setText("name mustn't be empty");
			lCNFNameError.setVisible(true);
			return false;
		}

		////////
		//// check if LAYOUT is valid
		String layoutStrRaw = txtCNFLayout.getText();

		if (layoutStrRaw.equals("")) {
			System.out.println("something seems to be wrong with the entered layout");
			lCNFLayoutError.setText("layout mustn't be empty");
			lCNFLayoutError.setVisible(true);
			return false;
		}

		char[] supportedCharacters = { ',', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		boolean charOK = false;
		for (char cRaw : layoutStrRaw.toCharArray()) {
			charOK = false;
			for (char c : supportedCharacters) {
				if (cRaw == c) {
					charOK = true;
					break;
				}
			}
			if (!charOK) {
				lCNFLayoutError.setText("invalid character: '" + cRaw + "'");
				lCNFLayoutError.setVisible(true);
				// System.out.println("something seems to be wrong with the
				// entered layout");
				return false;
			}
		}
		// check syntax of layout
		// check if there's a comma at front or back
		if (layoutStrRaw.startsWith(",")) {
			lCNFLayoutError.setText("syntax error at beginning of String");
			lCNFLayoutError.setVisible(true);
			return false;
		} else if (layoutStrRaw.endsWith(",")) {
			lCNFLayoutError.setText("syntax error at end of String");
			lCNFLayoutError.setVisible(true);
			return false;
		}
		// (one comma after another comma)
		char cBefore = 'x';
		for (char c : layoutStrRaw.toCharArray()) {
			if (cBefore == ',' && c == ',') {
				lCNFLayoutError.setText("syntax error");
				lCNFLayoutError.setVisible(true);
				return false;
			}
			cBefore = c;
		}

		////////
		//// CONVERT LAYOUT STRING to int[]
		String[] layoutStr = layoutStrRaw.split(",");

		int[] layout = new int[layoutStr.length];
		try {
			for (int i = 0; i < layoutStr.length; i++) {
				layout[i] = Integer.parseInt(layoutStr[i]);
			}
		} catch (Exception e) {
			lCNFMainError.setText("error while trying to process entered layout");
			lCNFMainError.setVisible(true);
			e.printStackTrace();
			return false;
		}

		// check if layout has at least a length of 2
		if (layout.length < 2) {
			lCNFLayoutError.setText("layout too small");
			lCNFLayoutError.setVisible(true);
			lCNFMainError.setText("network must have at least two layers");
			lCNFMainError.setVisible(true);
			return false;
		}

		// check if layout has no empty layers
		for (int i : layout)
			if (i <= 0) {
				lCNFMainError.setText("network can't have empty layers");
				lCNFMainError.setVisible(true);
				return false;
			}

		// create (export a new netork object)
		try {
			Network network = new Network(layout,
					(ActivationFunction) Class
							.forName((String) ("org.activationFunctions." + cbCNFActivationFunction.getSelectedItem()))
							.getConstructor().newInstance());

			File exportFile = new File(name + ".txt");
			exportNetwork(network, exportFile);

			lastCreatedFile = exportFile;
			System.out.println("created new network! '" + name + "'");
			return true;
		} catch (Exception e) {
			lCNFMainError.setText("error while trying to create network");
			lCNFMainError.setVisible(true);
			e.printStackTrace();
			return false;
		}

	}

	private void onButtonClickIF() {
		if (txtIFName.getText().equals("")) {
			// TODO alles :3 (inputs auswerten und errors anzeigen und so)
			return;
		}

		importNetwork(new File(txtIFName.getText() + ".txt"));

	}

	private void onButtonClickEF() {
		if (txtEFPath.getText().equals("")) {
			// TODO alles :3
			return;
		}

		Network.exportToFile(network, new File(txtEFPath.getText() + ".txt"));
	}

	private void importNetwork(File file) {
		try {
			network = Network.loadNetworkFromFile(file);
			networkDataFile = file;

			this.setTitle("NN: '" + networkDataFile.getName() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exportNetwork(Network network, File exportFile) {
		Network.exportToFile(network, exportFile);
	}

}