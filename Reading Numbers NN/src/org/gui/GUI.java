package org.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.network.Network;

import com.sun.glass.events.KeyEvent;

public class GUI extends JFrame {

	private Network network;

	// -- JAVA SWING things --
	private JPanel pMainCard;

	// 'create new file' panel (CNF = create new file)
	private JTextField txtCNFName;
	private JTextField txtCNFLayout;
	private JComboBox cbCNFActivationFunction;
	private JLabel lCNFNameError;

	// 'import file' panel (IF = import file)
	private JTextField txtIFName;

	// 'export file' panel (EF = export file)
	private JTextField txtEFPath;

	public static final String CARDTYPE_CREATENEWFILE = "createnewfile";
	public static final String CARDTYPE_IMPORTFILE = "importfile";
	public static final String CARDTYPE_EXPORTFILE = "exportfile";

	public GUI() {
		// @formatter:off
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		pMainCard.setPreferredSize(new Dimension(480,270));
		this.getContentPane().add(pMainCard, BorderLayout.CENTER);
		
		
		// create new file panel (CNF)
		JPanel pCNF = new JPanel();
		pCNF.setLayout(new BoxLayout( pCNF, BoxLayout.Y_AXIS));
		////pCNF.setBackground(Color.green);		
// -- 			components of 'create new file' panel:
			JPanel pCNFName = new JPanel();
				JLabel lMarkCNFName = new JLabel("Name:");
				pCNFName.add(lMarkCNFName);
			
				txtCNFName = new JTextField(15);
//				txtCNFName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				pCNFName.add(txtCNFName);
				
				JLabel lMarkCNFTxt = new JLabel(".txt");
				pCNFName.add(lMarkCNFTxt);
			pCNF.add(pCNFName);
			
			// CNF error label
			lCNFNameError = new JLabel("name invalid!");
			lCNFNameError.setAlignmentX(Container.CENTER_ALIGNMENT);
			lCNFNameError.setForeground(Color.red);
			lCNFNameError.setVisible(false);
			pCNF.add(lCNFNameError);
			
			pCNF.add(Box.createRigidArea(new Dimension(0,15)));
			
			
			JPanel pCNFLayout = new JPanel();				
				JLabel lMarkCNFLayout = new JLabel("Layout:");
				pCNFLayout.add(lMarkCNFLayout);
				
				txtCNFLayout = new JTextField(15);
//				txtCNFLayout.setBorder(BorderFactory.createLineBorder(Color.black));
				pCNFLayout.add(txtCNFLayout);
			pCNF.add(pCNFLayout);
			
			JLabel lMarkCNFLayoutEG = new JLabel("(e.g. '9, 7, 7, 4')");
			lMarkCNFLayoutEG.setAlignmentX(Container.CENTER_ALIGNMENT);
			pCNF.add(lMarkCNFLayoutEG);
			
			
			JPanel pCNFActivationFunction = new JPanel();
				JLabel lCNFActivationFunction = new JLabel("Activation Function:");
				pCNFActivationFunction.add(lCNFActivationFunction);
				
				String[] activationFunctions = {"Sigmoid", "Linear", "ReLU", "LeakyReLU"};
				cbCNFActivationFunction = new JComboBox(activationFunctions);
				pCNFActivationFunction.add(cbCNFActivationFunction);				
			pCNF.add(pCNFActivationFunction);
			
			//TODO add activation function for entire network
			
			JButton bCNF = new JButton("create");
			bCNF.setAlignmentX(Container.CENTER_ALIGNMENT);
			bCNF.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					onFileCreateNew();
				}
			});			
			pCNF.add(bCNF);
			
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
					onFileImport();
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

	private void onFileImport() {
		if (txtIFName.getText().equals("")) {
			showDialog("invalid path");
			return;
		}
		try {
			network = Network.loadNetworkFromFile(txtIFName.getText() + ".txt");
		} catch (Exception exc) {

		}
	}

	private void onFileExport() {
		if (txtEFPath.getText().equals("")) {
			showDialog("invalid path");
			return;
		}

		Network.exportToFile(network, txtEFPath.getText() + ".txt");
	}

	private void onFileCreateNew() {
		String name = txtCNFName.getText();

		// check if entered name is valid
		char[] notSupportedChars = { '|', '<', '>', '/', '\\', ':', '*', '?', '"' };
		for (char cStr : name.toCharArray())
			for (char c : notSupportedChars)
				if (cStr == c) {
					lCNFNameError.setVisible(true);
					return;
				}
		if (name.equals("")) {
			lCNFNameError.setVisible(true);
			return;
		}
		lCNFNameError.setVisible(false);

		// check if layout is valid
		String layoutStrRaw = txtCNFLayout.getText();

		if (layoutStrRaw.equals("")) {
			// TODO show layout error label (textbox empty)
			System.out.println("something seems to be wrong with the entered layout");
			return;
		}

		// String[] layoutStr = txtCNFLayout.getText().split(",");
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
				// TODO show layout error label
				System.out.println("something seems to be wrong with the entered layout");
				return;
			}
		}

		return;
		//
		// try {
		// // get layout
		// int[] layout = new int[layoutStr.length];
		// for (int i = 0; i < layoutStr.length; i++) {
		// layout[i] = Integer.parseInt(layoutStr[i]);
		// }
		//
		// network = new Network(layout, (ActivationFunction) Class
		// .forName((String)
		// cbCNFActivationFunction.getSelectedItem()).getConstructor().newInstance());
		//
		// System.out.println("created new network!");
		// } catch (Exception exc) {
		// System.out.println("creating a new network failed");
		// exc.printStackTrace();
		// }
	}

	private static void showDialog(String msg) {
		JDialog d = new JDialog();
		d.setVisible(true);
	}
}
