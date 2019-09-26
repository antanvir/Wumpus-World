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
	
	
	private final JButton buttonStart;
	private final JRadioButton rComputer;
	private final JRadioButton rHuman;
	private final JRadioButton rSpecified;
	private final JRadioButton rRandom;
	private final ButtonGroup bgDifficulty;
	private final ButtonGroup bgDifficulty2;

	
	private final JLabel playWith;
	private final JLabel playWith2;
	private final JLabel welcomeMsg;

	private JFrame frame;

	
	public GUI_Frame(int width, int height, String title) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame(title);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBackground(Color.white);
	    frame.setSize(762, 343);

			
		
		selectionPanel = new JPanel();
		selectionPanel.setBounds(0, height/3, width, height/3);

		selectionPanel2 = new JPanel();
		selectionPanel2.setBounds(0, height/5, width, height/5);
		
		welcomePanel = new JPanel();
		welcomePanel.setBounds(0, 0, width,  height/3);
		
		startingPanel = new JPanel();
		startingPanel.setBounds(0, 2*height/3, width,  height);

		
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
		
		bgDifficulty2 = new ButtonGroup();
		bgDifficulty2.add(rSpecified);
		bgDifficulty2.add(rRandom);
			
		playWith = new JLabel(" PLAY WITH :  ");
		
		
		startingPanel.add(buttonStart);
		welcomePanel.add(welcomeMsg);
		
		selectionPanel.add(playWith);
		selectionPanel.add(rComputer);
		selectionPanel.add(rHuman);
		
		playWith2 = new JLabel(" PLAY WITH :  ");
		
		
		selectionPanel.add(playWith2);
		selectionPanel.add(rSpecified);
		selectionPanel.add(rRandom);
		
		setupPanel = new JPanel();
		setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));
		
		setupPanel.add(welcomePanel);
		setupPanel.add(Box.createVerticalGlue());
		setupPanel.add(selectionPanel);
		setupPanel.add(Box.createVerticalGlue());
		setupPanel.add(selectionPanel2);
		setupPanel.add(Box.createVerticalGlue());
		setupPanel.add(startingPanel);
		
		
		

		frame.getContentPane().add(setupPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	
	public boolean playWithComputer() {
		return rComputer.isSelected();
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
