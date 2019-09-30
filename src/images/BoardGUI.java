
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class BoardGUI extends JPanel {

	private Image wumpus;
	private Image dead_wumpus;
	private Image robot;
	private Image gold;
	private Image pit;
	private Image white;
	
	static final int BLANK    = 0;
	static final int PIT         = 1;
	static final int WUMPUS      = 2;
	static final int DEAD_WUMPUS = -2;	
	static final int GOLD        = 3;
	
	private Graphics2D g2D;
	private BufferedImage image;	
	
	private int boardWidth; 
	private int totalCell; 
	private final int cellLength;
	
	private EnvironmentSetup env;
	private int[][] board, markedRoute, breeze_placement, glitter_placement, smell_placement;
	
	String senses = " -nothing- ";
	int arrows    = 2;
	
	int robo_x = 0;
	int robo_y = 9;
	int lastState_Y = -1, lastState_X = -1;
	
	static final int HOME_X = 0;
	static final int HOME_Y = 9;
	
	boolean game_lost = false;
	boolean has_gold  = false;
	boolean reached_home = false;
	boolean game_won  = false;
	int wumpus_dead   = 0;
	
	int Num_Pit = -1, Num_Wumpus = -1;
	
	
	public BoardGUI(int boardWidth, int totalCell) {
		this.boardWidth = boardWidth;
		this.totalCell = totalCell;
		this.cellLength  = boardWidth / totalCell;
		
		env = new EnvironmentSetup(Num_Pit, Num_Wumpus);
		board = env.getBoard();
		breeze_placement = env.getBreezeMatrix();
		glitter_placement = env.getGlitterMatrix();
		smell_placement = env.getSmellMatrix();
		markedRoute = new int[10][10];
		
		image = new BufferedImage(boardWidth, boardWidth+80, BufferedImage.TYPE_INT_ARGB);		
		g2D = (Graphics2D)image.getGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                			 RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		
		ImageIcon y = new ImageIcon(this.getClass().getResource("images/monster1.png"));
		wumpus = y.getImage();
		
//		y = new ImageIcon(this.getClass().getResource("images/dead_wumpus.gif"));
//		dead_wumpus = y.getImage();
		
		ImageIcon z = new ImageIcon(this.getClass().getResource("images/shooter_Copy.png"));
		robot = z.getImage();
		
		
		ImageIcon w = new ImageIcon(this.getClass().getResource("images/gold1.png"));
		gold = w.getImage();
		
		ImageIcon v = new ImageIcon(this.getClass().getResource("images/pit1.png"));
		pit = v.getImage();
		
//		ImageIcon x = new ImageIcon(this.getClass().getResource("images/white1.jpg"));
//		white = x.getImage();
		
		drawGameGrid();
//		drawWumpusWorldEnvironment(robo_y, robo_x);
//		repaint();
		
		
	}
	
	public void drawGameGrid() {
		g2D.setColor(Color.LIGHT_GRAY);
		g2D.fillRect(0,80,boardWidth, boardWidth);
		
		g2D.setColor(Color.black);
		
		for(int i=1; i<=totalCell; i++) {		// Vertical Line
			g2D.drawLine(i*cellLength, 80, i*cellLength, boardWidth+80);
		}
		
		
		for(int i=0; i<=totalCell; i++) {		// Horizontal Line
			g2D.drawLine(0, 80+i*cellLength, boardWidth, 80+i*cellLength);
		}
		
		
		g2D.setColor( Color.DARK_GRAY );
		for ( int row = 0; row < 10; row++ ) 
		{
			for ( int col = 0; col < 10; col++ )
			{
				int robo_y = row;
				int robo_x = col;
				int x = robo_x *( cellLength) + 5;
				int y = robo_y *( cellLength) + 85;
				if(board[robo_y][robo_x] != PIT || board[robo_y][robo_x] != WUMPUS) {
					if ( breeze_placement[robo_y][robo_x] == 1 )   g2D.drawString( "B", x , y+8  );
					if ( smell_placement[robo_y][robo_x] == 1 )   g2D.drawString( "    S", x , y+8 );
					if ( glitter_placement[robo_y][robo_x] == 1 ) g2D.drawString( "        G", x , y+8 );
				}
				
				if ( board[robo_y][robo_x] == PIT ) g2D.drawImage( pit, x+8,  y+12, null );				
				if ( board[robo_y][robo_x] == WUMPUS ) g2D.drawImage( wumpus, x+8,  y+12, null );				
		//		if ( board[robo_y][robo_x] == DEAD_WUMPUS ) g2D.drawImage( dead_wumpus, x, y, null );			
				if ( board[robo_y][robo_x] == GOLD ) g2D.drawImage( gold, x+8,  y+10, null );
			}
		}
		
		g2D.drawImage( robot, robo_x+8,  robo_y+12, null );
		
	}
	
	public void drawWumpusWorldEnvironment(int robotPosY, int robotPosX) {
		int x, y = 0;
		robo_y = robotPosY;
		robo_x = robotPosX;
		markedRoute[robo_y][robo_x] = 1;
		
		x = robo_x *( cellLength) + 5;
		y = robo_y *( cellLength) + 85;
		
		
		
		g2D.setFont( new Font( "Verdana", Font.BOLD, 15 ) );
		
		if(lastState_X != -1 && lastState_Y != -1) {
			System.out.println(lastState_Y +" " + lastState_X);
			g2D.setColor(new Color(245, 245, 239));
			g2D.fillRect(lastState_X*( cellLength)+2, lastState_Y*( cellLength) + 80+2, cellLength-2, cellLength-2);
//			g2D.drawImage( white, x+8,  y+10, null );	
//			g2D.setColor( Color.DARK_GRAY );
//			System.out.println("-- HERE --\n"+ lastState_X*( cellLength) + 5 + " "+ lastState_Y*( cellLength) + 85 );
		}
		
//		g2D.setColor( Color.DARK_GRAY );
//		if(board[robo_y][robo_x] != PIT || board[robo_y][robo_x] != WUMPUS) {
//			if ( breeze_placement[robo_y][robo_x] == 1 )   g2D.drawString( "B", x , y+8  );
//			if ( smell_placement[robo_y][robo_x] == 1 )   g2D.drawString( "    S", x , y+8 );
//			if ( glitter_placement[robo_y][robo_x] == 1 ) g2D.drawString( "        G", x , y+8 );
//		}
//		
//		if ( board[robo_y][robo_x] == PIT ) g2D.drawImage( pit, x+8,  y+12, null );				
//		if ( board[robo_y][robo_x] == WUMPUS ) g2D.drawImage( wumpus, x+8,  y+12, null );				
////		if ( board[robo_y][robo_x] == DEAD_WUMPUS ) g2D.drawImage( dead_wumpus, x, y, null );			
//		if ( board[robo_y][robo_x] == GOLD ) g2D.drawImage( gold, x+8,  y+10, null );
//		
//		g2D.drawImage( robot, x+8,  y+12, null );
		
		g2D.drawImage( robot, x+8,  y+12, null );
		
		lastState_Y = robotPosY;
		lastState_X = robotPosX;
		
		
		if ( game_lost )
		{
			g2D.setFont( new Font( "Arial", Font.BOLD, 40 ) );
			g2D.setColor( Color.RED );
			g2D.drawString("** DEAD! GAME LOST! **", boardWidth/2 - 200, boardWidth/2 - 50 );
		}
		else if ( reached_home )
		{
			g2D.setFont( new Font( "Arial", Font.BOLD, 40 ) );
			g2D.setColor( Color.BLUE );
			g2D.drawString("GAME WON!", boardWidth/2 - 200, boardWidth/2 - 50 );
		}
		else if(has_gold){
			g2D.setFont( new Font( "Arial", Font.BOLD, 40 ) );
			g2D.setColor( Color.GREEN );
//			g2D.drawString("GOT THE JACKPOT!!", getWidth()/2 - 180, getHeight()/2 - 50 );
			g2D.drawString("GOT THE JACKPOT!!", boardWidth/2 - 200, boardWidth/2 - 50 );
		}
		repaint();
		
	}
	
	
	
	
	public void printSense(int arrows, String text) {
		g2D.setColor( Color.WHITE );
		g2D.fillRect(0, 0, boardWidth, 80);
		
		FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());				
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		
		g2D.setFont(new Font(g2D.getFont().getName(), Font.BOLD, 15));
//		g2D.setColor(Color.black);
		
//		int x = (boardWidth/2 - metrics.stringWidth(text));
		int x = 10;
		int y = 30;
		
		g2D.setColor( new Color(37, 37, 116));		
		g2D.drawString(text, x, y);
		
		g2D.setFont(new Font(g2D.getFont().getName(), Font.BOLD, 15));
		
		int x1 = boardWidth - 140;
		int y1 = 30;
		
		String arrowText = "Arrows Left: " + arrows;
		g2D.setColor( Color.black);		
		g2D.drawString(arrowText, x1, y1);
		
		
		
		repaint();
		
	}
	
	
	
	public EnvironmentSetup getEnvironment() {
		return env;
	}
	
	
	public void set_Pit_And_Wumpus(int pit, int wumpus) {
		Num_Pit = pit;
		Num_Wumpus = wumpus;
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g.create();
		
		// Draw the board
		g2D.drawImage(image, 0, 0, boardWidth, boardWidth+80, null);
		
		
		// Drawing border
		g2D.setColor(Color.black);
        g2D.drawRect(0, 0, boardWidth, boardWidth+80);
				
		
	}
	
	
	
//	public void attachListener(MouseListener listener) {
//		addMouseListener(listener);
//	}
	
	
	public void set_Game_WON(boolean flag) {
		game_won  = flag;
	}
	
	public void set_Game_LOST(boolean flag) {
		game_lost = flag;
	}
	
	public void set_Has_GOLD(boolean flag) {
		has_gold  = flag;
	}
	
	public void set_Reached_HOME(boolean flag) {
		reached_home = flag;		 
	}
	
	public boolean get_Has_GOLD() {	
		return has_gold;
	}
	

}