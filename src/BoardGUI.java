
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
	
	static final int HOME_X = 0;
	static final int HOME_Y = 9;
	
	boolean game_lost = false;
	boolean has_gold  = false;
	boolean gold_home = false;
	boolean game_won  = false;
	int wumpus_dead   = 0;
	
	
	public BoardGUI(int boardWidth, int totalCell) {
		this.boardWidth = boardWidth;
		this.totalCell = totalCell;
		this.cellLength  = boardWidth / totalCell;
		
		env = new EnvironmentSetup();
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
		
		drawGameGrid();
		drawWumpusWorldEnvironment(robo_y, robo_x);
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
	}
	
	public void drawWumpusWorldEnvironment(int robotPosY, int robotPosX) {
		int x, y = 0;
		
		robo_y = robotPosY;
		robo_x = robotPosX;
		markedRoute[robo_y][robo_x] = 1;
		
		get_senses();
		printSense(2, senses);

		if ( gold_home )
		{
			game_won = true;
		}
		
		for ( int i = 0 ; i < totalCell ; i++ )
		{
			for ( int j = 0 ; j < totalCell ; j++ )
			{
				x = j *( cellLength) + 5;
				y = i *( cellLength) + 85;
				
//				g2D.drawRect( x, y, 64, 64);
				
				g2D.setColor( Color.DARK_GRAY );
				g2D.setFont( new Font( "Verdana", Font.BOLD, 15 ) );
				
				if(board[i][j] != PIT || board[i][j] != WUMPUS) {
					if ( breeze_placement[i][j] == 1 )   g2D.drawString( "B", x , y+8  );
					if ( smell_placement[i][j] == 1 )   g2D.drawString( "    S", x , y+8 );
					if ( glitter_placement[i][j] == 1 ) g2D.drawString( "        G", x , y+8 );
				}
				
				g2D.setColor( Color.LIGHT_GRAY );
				
				if ( board[i][j] == PIT ) g2D.drawImage( pit, x+8,  y+12, Color.LIGHT_GRAY, null );				
				if ( board[i][j] == WUMPUS ) g2D.drawImage( wumpus, x+8,  y+12, null );				
//				if ( board[i][j] == DEAD_WUMPUS ) g2D.drawImage( dead_wumpus, x, y, null );			
				if ( board[i][j] == GOLD ) g2D.drawImage( gold, x+8,  y+10, null );
				if ( i == robo_y && j == robo_x ) g2D.drawImage( robot, x+8,  y+12, null );
											
			}
		}
		
//		if ( game_lost )
//		{
//			g2D.setFont( new Font( "Arial", Font.BOLD, 60 ) );
//			g2D.setColor( Color.RED );
//			g2D.drawString("GAME LOST!", getWidth()/2 - 180, getHeight()/2 - 50 );
//		}
//		else if ( game_won )
//		{
//			g2D.setFont( new Font( "Arial", Font.BOLD, 60 ) );
//			g2D.setColor( Color.BLUE );
//			g2D.drawString("GAME WON!", getWidth()/2 - 180, getHeight()/2 - 50 );
//		}
//		else
//		{
//			g2D.setColor( Color.BLACK );
//			g2D.drawString("ROBOT LCD:", 5, ( ( BOARD_SIZE  ) * TILE_SIZE ) + 30 );
//			g2D.drawString("Currently sensing:" + senses, 10, ( ( BOARD_SIZE  ) * TILE_SIZE ) + 50 );
//			g2D.drawString("Arrows Left:" + arrows, 10, ( ( BOARD_SIZE  ) * TILE_SIZE ) + 70 );
//			g2D.setColor( Color.LIGHT_GRAY );
//		}
	}
	
	
	public void get_senses( )
	{
		
		senses = "";
		
		if ( smell_placement[robo_y][robo_x] == 1  )
		{
			senses += "  -a horrible smell-  ";
		}
		
		if ( breeze_placement[robo_y][robo_x]  == 1  )
		{
			senses += "  -a breeze-  ";
		}
		
		if ( glitter_placement[robo_y][robo_x] == 1   )
		{
			senses += "  -glitter-  ";
		}
		
		
		if ( has_gold && !gold_home )
		{
//			senses = "";
			senses = "-walking for a safe departure-";
		}
		
		else {
			senses = " -nothing- ";
		}
		
	}
	
	
	public void printSense(int arrows, String text) {
		FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());				
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		
		g2D.setFont(new Font(g2D.getFont().getName(), Font.PLAIN, 18));
//		g2D.setColor(Color.black);
		
//		int x = (boardWidth/2 - metrics.stringWidth(text));
		int x = 10;
		int y = 30;
		
		g2D.setColor( new Color(37, 37, 116));		
		g2D.drawString("SENSING => " + text,x,y);
		
		g2D.setFont(new Font(g2D.getFont().getName(), Font.PLAIN, 18));
		
		int x1 = boardWidth - 120;
		int y1 = 30;
		
		String arrowText = "Arrows Left: " + arrows;
		g2D.setColor( Color.black);		
		g2D.drawString(arrowText,x1,y1);
		
		
		
//		repaint();
		
	}
	
	public int getRelativePos(int x, boolean isWidth) {
		if(x >= boardWidth) x = boardWidth-1;
		
		if(isWidth)
			return (int) ( x * totalCell / boardWidth );
		else
			return (int) ( (x+80) * totalCell / boardWidth );
	}
	
	
	public Dimension getPreferredSize() {
		return new Dimension(boardWidth, boardWidth+100);
	}
	
	
	public void printWinner(int winner, String text) {
		FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());		
		
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
   			 				 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2D.setFont(new Font(g2D.getFont().getName(), Font.PLAIN, 48));
		
		g2D.setColor(Color.black);
		int x = (boardWidth/2 - metrics.stringWidth(text)*2);
		int y = boardWidth/2;
		

		
		g2D.setColor(winner == 2 ? Color.green : (winner == 1 ? Color.red : Color.blue));
		
		g2D.drawString(text,x,y);
		
		repaint();
		
	}
	
	
	public void drawStone(int posX, int posY, boolean human) {
		
		if(posX >= totalCell || posY >= totalCell) return;
		
		
		
		g2D.setColor(human ? Color.green : Color.orange);
		g2D.fillOval((int)(cellLength*(posX+0.2)), 
					 (int)(cellLength*(posY+0.2)), 
					 (int)(cellLength*0.7), 
					 (int)(cellLength*0.7));
		
		g2D.setColor(Color.blue);
		g2D.setStroke(new BasicStroke((float)1.5));
		g2D.drawOval((int)(cellLength*(posX+0.2)), 
					 (int)(cellLength*(posY+0.2)), 
					 (int)(cellLength*0.7), 
					 (int)(cellLength*0.7));
		
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		g2D.setColor(Color.black);
		g2D.setStroke(new BasicStroke(2));
		g2D.drawOval((int)(cellLength*(posX+0.2)), 
					 (int)(cellLength*(posY+0.2)), 
					 (int)(cellLength*0.7), 
					 (int)(cellLength*0.7));
		
		repaint();
	}
	
	
	public EnvironmentSetup getEnvironment() {
		return env;
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
        
        FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());				
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
   			 				 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		
//        String title = "Exploring Gold in Wumpus World!!";
//		g2D.setFont(new Font(g2D.getFont().getName(), Font.PLAIN, 20));
////		g2D.setColor(Color.black);
//		
//		int x1 = (boardWidth/2 - metrics.stringWidth(title)+40);
//		int y1 = 25;
//			
//		g2D.setColor(Color.DARK_GRAY);		
//		g2D.drawString(title,x1,y1);
//		
//		g2D.drawLine(100, 35, boardWidth-100, 35);
//		
		
	}
	
	
	
	public void attachListener(MouseListener listener) {
		addMouseListener(listener);
	}
	
	

}