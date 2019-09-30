import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class GUI_Frame extends JPanel {

	
	private static final long serialVersionUID = 1L;
	
	private int difficulty;
	
	private JPanel boardPanel;
	private final JPanel setupPanel;
	private final JPanel selectionPanel;
	private final JPanel selectionPanel2;
	private final JPanel welcomePanel;
	private final JPanel startingPanel;
	private final JPanel inputPanel;
	private final JPanel inputPanel2;
	
	private final JButton buttonStart;
	private final JRadioButton rComputer;
	private final JRadioButton rHuman;
	private final JRadioButton rSpecified;
	private final JRadioButton rRandom;
	
	private final ButtonGroup bgDifficulty ;
	private final ButtonGroup bgEnvironment ;

	
	private final JLabel playWith;
	private final JLabel ChooseEnvironment;
	private final JLabel welcomeMsg;
	private final JLabel pitNumber;
	private final JLabel wumpusNumber;
	private JTextField pit, wumpus;

	private JFrame frame;

	
	public GUI_Frame(int width, int height, String title) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame(title);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBackground(Color.white);
	    frame.setSize(width,height);

			
		
		selectionPanel = new JPanel();
		selectionPanel.setBounds(0, height/4, width, height/4);
		
		selectionPanel2 = new JPanel();
		selectionPanel2.setBounds(0, 2*height/4, width, height/4);

		inputPanel = new JPanel();
		inputPanel.setBounds(0, 3*height/4, width, height/4);
		
		inputPanel2 = new JPanel();
		inputPanel2.setBounds(0, 4*height/4, width, height/4);
		
		welcomePanel = new JPanel();
		welcomePanel.setBounds(0, 0, width,  height/4);
		
		startingPanel = new JPanel();
		startingPanel.setBounds(0, 5*height/4, width,  height);

		
		welcomeMsg = new JLabel("    WELCOME TO WUMPUS WORLD   ");
		buttonStart = new JButton("READY SET GOOO!");
		
		rComputer = new JRadioButton(" Computer");
		rHuman = new JRadioButton(" Human");	
		
		rSpecified = new JRadioButton(" Specified Environment");
		rRandom = new JRadioButton(" Random Environment");		
		
		bgDifficulty = new ButtonGroup();
		bgDifficulty.add(rComputer);
		bgDifficulty.add(rHuman);
		rComputer.isSelected();
		
		playWith = new JLabel(" PLAY WITH :  ");
		
		bgEnvironment = new ButtonGroup();
		bgDifficulty.add(rSpecified);
		bgDifficulty.add(rRandom);
		rRandom.isSelected();
				
		ChooseEnvironment = new JLabel(" CHOOSE  ENVIRONMENT TYPE :  ");
		
		
		startingPanel.add(buttonStart);
		welcomePanel.add(welcomeMsg);
		
		selectionPanel.add(playWith);
		selectionPanel.add(rComputer);
		selectionPanel.add(rHuman);
		
		selectionPanel2.add(ChooseEnvironment);
		selectionPanel2.add(rSpecified);
		selectionPanel2.add(rRandom);
		
		pitNumber = new JLabel(" Number of Pits :  ");
		wumpusNumber = new JLabel(" Number of Wumpus :  ");
		

		inputPanel.add(pitNumber);
		inputPanel2.add(wumpusNumber);
	
		
		pit = new JTextField(20);	
		inputPanel.add(pit);
		
		wumpus = new JTextField(20);
		inputPanel2.add(wumpus);
	
		
		
		setupPanel = new JPanel();
		setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));
		
		setupPanel.add(welcomePanel);
		setupPanel.add(Box.createVerticalGlue());
		setupPanel.add(selectionPanel);
		setupPanel.add(Box.createVerticalGlue());
		setupPanel.add(selectionPanel2);
		setupPanel.add(Box.createVerticalGlue());
		setupPanel.add(inputPanel);
		setupPanel.add(Box.createVerticalGlue());
		setupPanel.add(inputPanel2);
		setupPanel.add(Box.createVerticalGlue());
		setupPanel.add(startingPanel);
		
		
		

		frame.getContentPane().add(setupPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	
	public boolean playWithComputer() {
		return rComputer.isSelected();
	}
	
	public boolean isRandomEnvironment() {
		return rRandom.isSelected();
	}
	
	public int No_Of_Wumpus() {
		return Integer.parseInt(wumpus.getText());
	}
	
	public int No_Of_Pit() {
		return Integer.parseInt(pit.getText());
	}
	
	
	public void listenGameStartButton(ActionListener listener) {
		buttonStart.addActionListener(listener);
	}
	
	
	public void attachBoard(JPanel board) {
		boardPanel = board;
	}
	
	
	public void showBoard() {
		frame.setContentPane(boardPanel);
		invalidate();
		validate();
		frame.pack();
	}
	

	
}