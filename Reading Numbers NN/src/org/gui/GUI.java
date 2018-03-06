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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.activationFunctions.ActivationFunction;
import org.network.Network;
import org.trainingAlgorithms.TrainingAlgorithm_Addition;

import com.sun.glass.events.KeyEvent;

public class GUI extends JFrame {

	private volatile Network network;
	private GUITrainer trainer;
	private File networkDataFile;
	private File lastCreatedFile;

	// -- JAVA SWING things --
	private JPanel pMainCard;

	// menu bar
	private JMenuItem menuItemExport;
	private JMenuItem menuItemTrain;

	// 'create new file' panel (CNF = create new file)
	private JTextField txtCNFName;
	private JLabel lCNFNameError;
	private JTextField txtCNFLayout;
	private JLabel lCNFLayoutError;
	private JComboBox<String> cbCNFActivationFunction;
	private JLabel lCNFMainError;

	// 'import file' panel (IF = import file)
	private JTextField txtIFName;
	private JLabel lIFNameError;
	private JLabel lIFInspect;

	// 'export file' panel (EF = export file)
	private JTextField txtEFName;
	private JLabel lEFStatus;

	// 'train' panel (T = train)
	private JTextField txtTLearningRate;
	private JLabel lTLearningRateError;
	private JTextField txtTIterations;
	private JLabel lTIterationsError;
	private JLabel lTIterationInfo;
	private JLabel lTErrorInfo;
	private JLabel lTInfo;

	public static final String CARDTYPE_CREATENEWFILE = "createnewfile";
	public static final String CARDTYPE_IMPORTFILE = "importfile";
	public static final String CARDTYPE_EXPORTFILE = "exportfile";
	public static final String CARDTYPE_TRAIN = "train";

	private static final int DEFAULT_RIGID_AREA_SIZE = 15;

	// @formatter:off
	public GUI() {
		
		//create new object for training a network
		trainer = new GUITrainer(this);
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		
		Font fontHeaderBig = new Font("arial", Font.BOLD, 25);
		Font fontHeaderSmall = new Font("arial", Font.BOLD, 20);
		Font fontConsolasDefault = new Font("consolas", Font.PLAIN, 12);

// -------------------------------------------------------------------------------------------------MENU BAR----------------------------------
		
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
				
				menuItemExport = new JMenuItem("export file");
				menuItemExport.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onMenuClickEF();
					}
				});
				menuFile.add(menuItemExport);
				
				
				menuFile.addSeparator();
				
				
				menuItemTrain = new JMenuItem("train network");
				menuItemTrain.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onMenuClickT();
					}
				});
				menuFile.add(menuItemTrain);
				
//				menuFile.addSeparator();
				
			menuBar.add(menuFile);
		this.setJMenuBar(menuBar);
		
		
		// main JPanel
		pMainCard = new JPanel();
		pMainCard.setLayout(new CardLayout());
		//pMainCard.setPreferredSize(new Dimension(480,270));
		pMainCard.setPreferredSize(new Dimension(400,300));
		this.getContentPane().add(pMainCard, BorderLayout.CENTER);
		
		
// -------------------------------------------------------------CREATE NEW FILE-------------------------------------------------------------------
		
		
		// create new file panel (CNF)
		JPanel pCNF = new JPanel();
		pCNF.setLayout(new BoxLayout( pCNF, BoxLayout.Y_AXIS));
// -- 			components of 'create new file' panel:
				
		
		
		
			JLabel lCNFHeader = new JLabel("Create a new Neural Network");
			lCNFHeader.setAlignmentX(Container.CENTER_ALIGNMENT);
			lCNFHeader.setFont(fontHeaderBig);		
			pCNF.add(lCNFHeader);
						
			
			
			
			pCNF.add(Box.createRigidArea(new Dimension(0, DEFAULT_RIGID_AREA_SIZE)));
				
		
		
		
			JPanel pCNFName = new JPanel();
				JLabel lMarkCNFName = new JLabel("Name:");
				pCNFName.add(lMarkCNFName);
			
				txtCNFName = new JTextField(15);
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

		
		
// -------------------------------------------------------------------IMPORT------------------------------------------------------------------ 				
		
		// import file panel (IF)
		JPanel pImport = new JPanel();
		pImport.setLayout(new BoxLayout(pImport, BoxLayout.Y_AXIS));
// --			components of 'import file' panel
		
			JLabel lIFHeader = new JLabel("Import existing Network");
			lIFHeader.setAlignmentX(Container.CENTER_ALIGNMENT);
			lIFHeader.setFont(fontHeaderBig);
			pImport.add(lIFHeader);
			
			pImport.add(Box.createRigidArea(new Dimension(0,DEFAULT_RIGID_AREA_SIZE)));
			
			JPanel pIFName = new JPanel();
			pIFName.setAlignmentX(Container.CENTER_ALIGNMENT);
				JLabel lMarkIFName = new JLabel("Name: ");
				pIFName.add(lMarkIFName);
			
				txtIFName = new JTextField(15);
				pIFName.add(txtIFName);
				
				JLabel lMarkIFTxt = new JLabel(".txt");
				pIFName.add(lMarkIFTxt);
			pImport.add(pIFName);
			
			lIFNameError = new JLabel();
			lIFNameError.setVisible(false);
			lIFNameError.setForeground(Color.red);
			lIFNameError.setAlignmentX(Container.CENTER_ALIGNMENT);
			pImport.add(lIFNameError);
			
			pImport.add(Box.createRigidArea(new Dimension(0,DEFAULT_RIGID_AREA_SIZE)));
			
			JPanel pIFButtons = new JPanel();
			pIFButtons.setAlignmentX(Container.CENTER_ALIGNMENT);
				JButton bIF = new JButton("Import");				
				bIF.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onButtonClickIF();
					}
				});
				pIFButtons.add(bIF);
				
				JButton bIFInspect = new JButton("Inspect");				
				bIFInspect.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onButtonClickIFInspect();
					}
				});
				pIFButtons.add(bIFInspect);
				
			pImport.add(pIFButtons);
			
			
			pImport.add(Box.createRigidArea(new Dimension(0, DEFAULT_RIGID_AREA_SIZE)));
			
			
			lIFInspect = new JLabel();
			lIFInspect.setVisible(false);
			lIFInspect.setForeground(Color.gray);
			lIFInspect.setAlignmentX(Container.CENTER_ALIGNMENT);
			pImport.add(lIFInspect);
			
			
			pImport.add(Box.createRigidArea(new Dimension(0,9000)));
// --	
		pMainCard.add(pImport, CARDTYPE_IMPORTFILE);
		
// -------------------------------------------------------------------EXPORT------------------------------------------------------------------ 		
		
		// export file panel (EF)
		JPanel pExport = new JPanel();
		pExport.setLayout(new BoxLayout(pExport, BoxLayout.Y_AXIS));
		
// --		components of 'export file' panel
			
			JLabel lEFHeader = new JLabel("Export Network");
			lEFHeader.setFont(fontHeaderBig);
			lEFHeader.setAlignmentX(Container.CENTER_ALIGNMENT);
			pExport.add(lEFHeader);
			
			
			pExport.add(Box.createRigidArea(new Dimension(0, DEFAULT_RIGID_AREA_SIZE)));
			
			
			JPanel pEFName = new JPanel();
			pEFName.setAlignmentX(Container.CENTER_ALIGNMENT);			
				JLabel lMarkEFName = new JLabel("Filename:");
				pEFName.add(lMarkEFName);
				
				txtEFName = new JTextField(15);
				pEFName.add(txtEFName);
				
				JLabel lMarkEFTxt = new JLabel(".txt");
				pEFName.add(lMarkEFTxt);
			
			pExport.add(pEFName);
			
			
			pExport.add(Box.createRigidArea(new Dimension(0, DEFAULT_RIGID_AREA_SIZE)));
			
			
			JPanel pEFButtons = new JPanel();
			pEFButtons.setAlignmentX(Container.CENTER_ALIGNMENT);
				
				JButton bEFExport = new JButton("export");
				bEFExport.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onButtonClickEF();
					}
				});
				pEFButtons.add(bEFExport);
			
			pExport.add(pEFButtons);
			
			
			
			pExport.add(Box.createRigidArea(new Dimension(0, DEFAULT_RIGID_AREA_SIZE/2)));
			
			
			
			lEFStatus = new JLabel();
			lEFStatus.setVisible(false);
			lEFStatus.setForeground(Color.gray);
			lEFStatus.setAlignmentX(Container.CENTER_ALIGNMENT);
			pExport.add(lEFStatus);
			
			
		
			pExport.add(Box.createRigidArea(new Dimension(0, 9000)));
// --
		pMainCard.add(pExport, CARDTYPE_EXPORTFILE);
		
// -------------------------------------------------------------------TRAIN-------------------------------------------------------------------
		
		// train panel
		JPanel pTrain = new JPanel();
		pTrain.setLayout(new BoxLayout( pTrain, BoxLayout.Y_AXIS));
// --		components of 'train' panel
		
			//header
			JLabel lTHeader = new JLabel("Train the Network");
			lTHeader.setFont(fontHeaderBig);
			lTHeader.setAlignmentX(Container.CENTER_ALIGNMENT);
			pTrain.add(lTHeader);
			
			
			
			pTrain.add(Box.createRigidArea(new Dimension(0,DEFAULT_RIGID_AREA_SIZE)));
			
			
		
			JPanel pTLearningRate = new JPanel();
				//learning rate label
				JLabel lMarkTLearningRate = new JLabel("Learning Rate: ");
				lMarkTLearningRate.setAlignmentX(Container.CENTER_ALIGNMENT);
				pTLearningRate.add(lMarkTLearningRate);
			
				//learning rate txtField
				txtTLearningRate = new JTextField(15);
				txtTLearningRate.setAlignmentX(Container.CENTER_ALIGNMENT);
				pTLearningRate.add(txtTLearningRate);
			pTrain.add(pTLearningRate);
			
			
			
			lTLearningRateError = new JLabel();
			lTLearningRateError.setVisible(false);
			lTLearningRateError.setAlignmentX(Container.CENTER_ALIGNMENT);
			lTLearningRateError.setForeground(Color.red);
			pTrain.add(lTLearningRateError);
			
			
			
			pTrain.add(Box.createRigidArea(new Dimension(0,DEFAULT_RIGID_AREA_SIZE)));
			
			
			
			JPanel pTButtons = new JPanel();
			pTButtons.setAlignmentX(Container.CENTER_ALIGNMENT);
				JButton bTOneCycle = new JButton("1 cycle");
				bTOneCycle.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onButtonClickTrainOneCycle();
					}
				});
				pTButtons.add(bTOneCycle);
				
				JButton bTThousandCycles = new JButton("1000 cycles");
				bTThousandCycles.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onButtonClickTrainThousandCycles();
					}
				});
				pTButtons.add(bTThousandCycles);
			pTrain.add(pTButtons);
			
			
			JPanel pTIterationButton = new JPanel();
			pTIterationButton.setAlignmentX(Container.CENTER_ALIGNMENT);
				txtTIterations = new JTextField(10);				
				pTIterationButton.add(txtTIterations);
				
				JButton bTIterations = new JButton("cycles");
				bTIterations.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onButtonClickTrainIterations();
					}
				});
				pTIterationButton.add(bTIterations);
			pTrain.add(pTIterationButton);
				
			
			lTIterationsError = new JLabel();
			lTIterationsError.setVisible(false);
			lTIterationsError.setForeground(Color.red);
			lTIterationsError.setAlignmentX(Container.CENTER_ALIGNMENT);
			pTrain.add(lTIterationsError);			
			
			
			pTrain.add(Box.createRigidArea(new Dimension(0,DEFAULT_RIGID_AREA_SIZE)));
			
			
			
			lTIterationInfo = new JLabel("");
			lTIterationInfo.setFont(fontConsolasDefault);
			lTIterationInfo.setAlignmentX(Container.CENTER_ALIGNMENT);
			pTrain.add(lTIterationInfo);
			
			lTErrorInfo = new JLabel("");
			lTErrorInfo.setFont(fontConsolasDefault);
			lTErrorInfo.setAlignmentX(Container.CENTER_ALIGNMENT);
			pTrain.add(lTErrorInfo);
			
			lTInfo = new JLabel("");
			lTInfo.setFont(fontConsolasDefault);
			lTInfo.setAlignmentX(Container.CENTER_ALIGNMENT);
			pTrain.add(lTInfo);
			
			
			
// --
			pTrain.add(Box.createRigidArea(new Dimension(0, 9000)));
		pMainCard.add(pTrain, CARDTYPE_TRAIN);
		
// -------------------------------------------------------------------------------------------------------------------------------------------
		
		//disable menu bar items (until a network gets imported) 
		menuItemExport.setEnabled(false);
		menuItemTrain.setEnabled(false);
		
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

	////////
	////////// onMenu clicks
	////////

	private void onMenuClickCNF() {
		switchMainPanelTo(CARDTYPE_CREATENEWFILE);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtCNFName.grabFocus();
			}
		});
	}

	private void onMenuClickIF() {
		switchMainPanelTo(CARDTYPE_IMPORTFILE);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtIFName.grabFocus();
			}
		});
	}

	private void onMenuClickEF() {
		switchMainPanelTo(CARDTYPE_EXPORTFILE);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						txtEFName.grabFocus();
					}
				});
			}
		});
	}

	private void onMenuClickT() {
		switchMainPanelTo(CARDTYPE_TRAIN);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtTLearningRate.grabFocus();
			}
		});
	}

	private void onButtonClickCNFAndImport() {
		if (onButtonClickCNF()) {
			importNetwork(lastCreatedFile);
		}
	}

	private void onButtonClickTrainOneCycle() {
		onButtonClickTrainINTERN(1);
	}

	private void onButtonClickTrainThousandCycles() {
		onButtonClickTrainINTERN(1000);
	}

	private void onButtonClickTrainIterations() {
		lTIterationsError.setVisible(false);
		long iterations = -1;
		try {
			iterations = Long.parseLong(txtTIterations.getText());
		} catch (Exception e) {
			lTIterationsError.setText("amount of iterations is invalid");
			lTIterationsError.setVisible(true);
			return;
		}

		if (iterations > 0) {
			onButtonClickTrainINTERN(iterations);
		}
	}

	private void onButtonClickTrainINTERN(long iterations) {
		lTLearningRateError.setVisible(false);

		// get learning rate
		double learningRate = -1;
		String learningRateStr = txtTLearningRate.getText();
		learningRateStr = learningRateStr.replaceAll(",", ".");

		try {
			learningRate = Double.parseDouble(learningRateStr);
		} catch (Exception e) {
		}

		if (learningRate == -1) {
			lTLearningRateError.setText("learning rate invalid");
			lTLearningRateError.setVisible(true);
			return;
		}

		network.setLearningRate(learningRate);
		// System.out.println("LR: " + learningRate);

		trainNetwork(iterations);
	}

	////////
	////////// onButton clicks
	////////

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
			File exportFile = new File(name + ".txt");

			// show warning if network with the same name already exists
			if (exportFile.exists()) {
				int result = JOptionPane.showOptionDialog(null,
						"A Network named '" + name + "' already exists, do you want to overwrite or import it?",
						"Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
						new String[] { "overwrite existing", "import existing", "no / cancel" }, "no / cancel");

				if (result == 1) {
					importNetwork(exportFile);
					return false;
				} else if (result == 2 || result == JOptionPane.CLOSED_OPTION) {
					return false;
				}
			}

			Network network = new Network(layout,
					(ActivationFunction) Class
							.forName((String) ("org.activationFunctions." + cbCNFActivationFunction.getSelectedItem()))
							.getConstructor().newInstance());

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
		lIFNameError.setVisible(false);

		String name = txtIFName.getText();
		if (name.equals("")) {
			lIFNameError.setText("name mustn't be empty");
			lIFNameError.setVisible(true);
			return;
		}

		File file = new File(name + ".txt");

		if (!file.exists()) {
			lIFNameError.setText("network with this name doesn't exist");
			lIFNameError.setVisible(true);
			return;
		}

		if (importNetwork(file)) {
			txtTLearningRate.setText(network.getLearningRate() + "");
		}
	}

	private void onButtonClickIFInspect() {
		lIFNameError.setVisible(false);
		lIFInspect.setVisible(false);

		String name = txtIFName.getText();
		if (name.equals("")) {
			lIFNameError.setText("name mustn't be empty");
			lIFNameError.setVisible(true);
			return;
		}

		File file = new File(name + ".txt");

		if (!file.exists()) {
			lIFNameError.setText("network with this name doesn't exist");
			lIFNameError.setVisible(true);
			return;
		}

		inspectNetwork(file);
	}

	private void onButtonClickEF() {
		if (txtEFName.getText().equals("")) {
			// TODO alles :3
			return;
		}

		if (Network.exportToFile(network, new File(txtEFName.getText() + ".txt"))) {
			lEFStatus.setText("exported successfully");
			lEFStatus.setVisible(true);
		}
	}

	private boolean importNetwork(File file) {
		try {
			network = Network.loadNetworkFromFile(file);
			networkDataFile = file;

			menuItemExport.setEnabled(true);
			menuItemTrain.setEnabled(true);
			this.setTitle("NN: '" + networkDataFile.getName() + "'");

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	private void inspectNetwork(File file) {
		try {
			Network net = Network.loadNetworkFromFile(file);

			// convert layout to str
			int[] layout = net.getLayout();
			String layoutStr = "";
			for (int l : layout)
				layoutStr += l + ",";
			layoutStr = layoutStr.substring(0, layoutStr.length() - 1);

			lIFInspect.setText("'" + file.getName() + "': " + layoutStr);
			lIFInspect.setVisible(true);

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private void exportNetwork(Network network, File exportFile) {
		Network.exportToFile(network, exportFile);
	}

	private void trainNetwork(long iterations) {

		trainer.startTraining(network, iterations, new TrainingAlgorithm_Addition());

	}

	public void updateTrainingProgress(long iterationsDone, long iterationsTotal, double currentError, String notePart1,
			String notePart2, String notePart3) {
		String iterationInfo = "iteration: ";
		String errorInfo = "error: ";
		String info = notePart1;

		String iterationValue = iterationsDone + "/" + iterationsTotal;
		String errorValue = currentError + "";
		String infoValue = notePart2;

		int maxStrLen = 30;
		int maxStrLenTriple = 45;
		int spaceLenIteration, spaceLenError, spaceLenInfo;

		spaceLenIteration = maxStrLen - (iterationInfo.length() + iterationValue.length());
		spaceLenError = maxStrLen - errorInfo.length() - errorValue.length();
		spaceLenInfo = maxStrLen - info.length() - infoValue.length();

		for (int i = 0; i < spaceLenIteration; i++)
			iterationInfo += " ";
		iterationInfo += iterationValue;

		for (int i = 0; i < spaceLenError; i++)
			errorInfo += " ";
		errorInfo += errorValue;

		for (int i = 0; i < spaceLenInfo; i++)
			info += " ";
		info += infoValue;

		// the third part
		iterationValue = ((int) (((double) iterationsDone / (double) iterationsTotal) * 1000000)) / 10000D + "%";
		errorValue = ((int) (currentError * 1000000)) / 10000D + "%";
		infoValue = notePart3;

		spaceLenIteration = maxStrLenTriple - (iterationInfo.length() + iterationValue.length());
		spaceLenError = maxStrLenTriple - errorInfo.length() - errorValue.length();
		spaceLenInfo = maxStrLenTriple - info.length() - infoValue.length();

		for (int i = 0; i < spaceLenIteration; i++)
			iterationInfo += " ";
		iterationInfo += iterationValue;

		for (int i = 0; i < spaceLenError; i++)
			errorInfo += " ";
		errorInfo += errorValue;

		for (int i = 0; i < spaceLenInfo; i++)
			info += " ";
		info += infoValue;

		// lTIterationInfo.setText("<html>iterations: " + iterationsDone + "/" +
		// iterationsTotal + "<br>error: "
		// + currentError + "<br>" + note + "</html>");

//		System.out.println(iterationInfo);
//		System.out.println(errorInfo);
//		System.out.println(info);
		lTIterationInfo.setText(iterationInfo);
		lTErrorInfo.setText(errorInfo);
		lTInfo.setText(info);

	}
}
