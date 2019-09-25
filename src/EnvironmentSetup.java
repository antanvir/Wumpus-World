import java.awt.Image;
import java.util.*;
import java.util.ArrayList;
import java.lang.Integer;


public class EnvironmentSetup 
{
	Image question;
	Image wumpus;
	Image dead_wumpus;
	Image robot_up;
	Image robot_down;
	Image robot_left;
	Image robot_right;
	Image gold;
	Image pit;
	
	int[][] board, pit_placement, wumpus_placement, gold_placement, breeze_placement,
	smell_placement, glitter_placement, dead_wumpus_placement, robot_placement;
	
	
	static final int BLANK    = 0;
	static final int PIT         = 1;
	static final int WUMPUS      = 2;
	static final int DEAD_WUMPUS = -2;	
	static final int GOLD        = 3;
	
	static final int ROBO_LEFT   = 1;
	static final int ROBO_UP     = 2;
	static final int ROBO_RIGHT  = 3;
	static final int ROBO_DOWN   = 4;
	
	static final int WUMPUS_LIMIT = 2;
	static final int PIT_LIMIT    = 15;
	static final int GOLD_LIMIT   = 1;
	
	static final int BOARD_SIZE = 10;	
	static final int TOTAL_CELL  = 100;
	
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

	public EnvironmentSetup(){	
		
		generate_board();
	}
	

//	public static void main( String[] args )
//	{
//		new EnvironmentSetup( );
//	}
	

	public void generate_board( )
	{
		robot_placement = new int [10][10];
		robot_placement[9][0] = 1;
		
		board = new int [10][10];
		
		do
		{
		
			Random rand = new Random();
//			int wumpus_placed = 0;
//			int pits_placed = 0;
//			int gold_placed = 0;	
//			int gold_placed_x = 0;
//			int gold_placed_y = 0;
			
			pit_placement = new int [10][10];
			wumpus_placement = new int [10][10];
			gold_placement = new int [10][10];
			
			breeze_placement = new int [10][10];
			smell_placement = new int [10][10];
			glitter_placement = new int [10][10];
			
			for ( int row = 0; row < BOARD_SIZE; row++) // CLEAR THEM ALL OUT
			{
				for ( int col = 0; col < BOARD_SIZE; col++)
				{
					pit_placement[row][col] = 0;
					wumpus_placement[row][col] = 0;
					gold_placement[row][col] = 0;
					
					breeze_placement[row][col] = 0;
					smell_placement[row][col] = 0;
					glitter_placement[row][col] = 0;
					
				}
			}	
		
			for(int i = 0; i< PIT_LIMIT; i++) { // PLACE THE PITS
				int x = rand.nextInt( 100 );
				int row = x / 10;
				int col = x % 10;
				if ( row == ( BOARD_SIZE - 1 ) && col == 0 ) {
					i --;
					continue;				
				}
				pit_placement[row][col] = 1;
			}

			
			for(int i = 0; i< WUMPUS_LIMIT; i++) { // PLACE THE WUMPUS'S
				int x = rand.nextInt( 100 );
				int row = x / 10;
				int col = x % 10;
				if ( row == ( BOARD_SIZE - 1 ) && col == 0 ){
					i --;
					continue;				
				}
				if ( pit_placement[row][col] == 1 ){
					i --;
					continue;				
				}
				wumpus_placement[row][col] = 1;
			}
					
			for(int i = 0; i< GOLD_LIMIT; i++) { // PLACE THE GOLD
				int x = rand.nextInt( 100 );
				int row = x / 10;
				int col = x % 10;
				if (row > 5 || ( row == ( BOARD_SIZE - 1 ) && col == 0) ){
					i --;
					continue;				
				}
				if ( pit_placement[row][col] == 1  || wumpus_placement[row][col] == 1) {
					i --;
					continue;				
				}

				gold_placement[row][col] = 1;				
//				gold_placed_x = col;
//				gold_placed_y = row;	
			}
			
		}
		while( !bfs( ) ); // DOES A SOLUTION EXIST?
		
		
		// Print the board
		for ( int row = 0; row < BOARD_SIZE; row++ )
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				if(pit_placement[row][col] == 1) System.out.print(1 + " ");
				else if(wumpus_placement[row][col] == 1) System.out.print(2 + " ");
				else if(gold_placement[row][col] == 1) System.out.print(3 + " ");
				else System.out.print(0 + " ");
				
			}
			System.out.println();
		}
		
		
		// Create the board
		for ( int row = 0; row < BOARD_SIZE; row++ )
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				if(pit_placement[row][col] == 1) board[row][col] = PIT;
				else if(wumpus_placement[row][col] == 1)  board[row][col] = WUMPUS;
				else if(gold_placement[row][col] == 1) board[row][col] = GOLD;
				else board[row][col] = BLANK;
				
			}			
		}
		
		
		for ( int row = 0; row < BOARD_SIZE; row++ )
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				if ( pit_placement[row][col] == 1 )
				{
					// PLACE THE BREEZES
							
					try // LEFT Neighbor
					{
						if( pit_placement[row][col-1] != 1 ) breeze_placement[row][col-1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // UP Neighbor
					{
						if( pit_placement[row-1][col] != 1 ) breeze_placement[row-1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // RIGHT Neighbor
					{
						if( pit_placement[row][col+1] != 1 ) breeze_placement[row][col+1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // DOWN Neighbor
					{
						if( pit_placement[row+1][col] != 1 ) breeze_placement[row+1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }	
				}
				
				if ( wumpus_placement[row][col] == 1 )
				{
					// PLACE THE SMELLIES
							
					try // LEFT Neighbor
					{
						if( pit_placement[row][col-1] != 1 ) smell_placement[row][col-1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // UP Neighbor
					{
						if( pit_placement[row-1][col] != 1 ) smell_placement[row-1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // RIGHT Neighbor
					{
						if( pit_placement[row][col+1] != 1 ) smell_placement[row][col+1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // DOWN Neighbor
					{
						if( pit_placement[row+1][col] != 1 ) smell_placement[row+1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }	
				}
				
				if ( gold_placement[row][col] == 1 )
				{
					// PLACE THE GLITTER
							
					try // LEFT Neighbor
					{
						if ( pit_placement[row][col-1] != 1 ) glitter_placement[row][col-1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // UP Neighbor
					{
						if ( pit_placement[row-1][col] != 1 ) glitter_placement[row-1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // RIGHT Neighbor
					{
						if ( pit_placement[row][col+1] != 1 ) glitter_placement[row][col+1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try // DOWN Neighbor
					{
						if ( pit_placement[row+1][col] != 1 ) glitter_placement[row+1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }	
				}
			}
		}		
	}
	
	public boolean bfs( )
	{
		ArrayList<Integer> queue = new ArrayList<Integer>();
		boolean solution_exists = false;
		
		int[][] marked = new int[BOARD_SIZE][BOARD_SIZE];
		int[][] nodesID = new int[BOARD_SIZE][BOARD_SIZE];
		int[][] relationships = new int[BOARD_SIZE*BOARD_SIZE][BOARD_SIZE*BOARD_SIZE];
		
		int node_count = 0;
		
		for ( int row = 0; row < BOARD_SIZE; row++ )
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				marked[row][col] = 0;
			}
		}
		
		for ( int row = 0; row < BOARD_SIZE; row++ ) // GENERATE UNIQUE NODE "ID"
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				nodesID[row][col] = node_count;
				node_count += 1;
			}
		}
		
		for ( int row = 0; row < BOARD_SIZE*BOARD_SIZE; row++ )
		{
			for ( int col = 0; col < BOARD_SIZE*BOARD_SIZE; col++ )
			{
				relationships[row][col] = 0;
			}
		}
		
		for ( int row = 0; row < BOARD_SIZE; row++ ) // GENERATE RELATIONSHIP MATRIX
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				try // LEFT Neighbor
				{
					relationships[ nodesID[row][col] ][ nodesID[row][col-1]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try // UP Neighbor
				{
					relationships[ nodesID[row][col] ][ nodesID[row-1][col]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try // RIGHT Neighbor
				{
					relationships[ nodesID[row][col] ][ nodesID[row][col+1]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try // DOWN Neighbor
				{
					relationships[ nodesID[row][col] ][ nodesID[row+1][col]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }				 
			}
		}
		
		queue.add( nodesID[BOARD_SIZE-1][0 ] );
		marked[BOARD_SIZE-1][0 ] = 1;
		
		while( !queue.isEmpty() ) // BFS ALGO BEGIN
		{
			int node = queue.remove( 0 );
			
			if( gold_placement[ ( int ) node / 10 ][ (int) node % 10 ] == 1 ) // GOLD?
			{
				solution_exists = true;
				break;
			}
			else // NO, SO ADD IT'S NEIGHBORS BUT ONLY IF THEY ARE NOT PITS AND HAVEN'T ALREADY BEEN MARKED
			{
				for ( int i = 0 ; i < BOARD_SIZE*BOARD_SIZE ; i++ )
				{
					if ( relationships[node][i] == 1 && pit_placement[ ( int ) i / 10 ][ (int) i % 10 ] != 1 && marked[ ( int ) i / 10 ][ (int) i % 10 ] != 1 )
					{
						queue.add( nodesID[ ( int ) i / 10 ][ (int) i % 10 ] );
						marked[ ( int ) i / 10 ][ (int) i % 10 ] = 1;
					}
				}
			}
		}
		
//		for ( int row = 0; row < BOARD_SIZE*BOARD_SIZE; row++ )
//		{
//			for ( int col = 0; col < BOARD_SIZE*BOARD_SIZE; col++ )
//			{
//				// System.out.print( relationships[row][col] + " " );
//			}
//			
//			// System.out.print( "\n " );
//		}
//		
		
		return solution_exists;
	}
	
	
	public int[][] getBoard(){
		return board;
	}
	
	public int[][] getGlitterMatrix(){
		return glitter_placement;
	}
	
	public int[][] getBreezeMatrix(){
		return breeze_placement;
	}
	
	public int[][] getSmellMatrix(){
		return smell_placement;
	}
	
	public int[][] getGoldPlacement(){
		return gold_placement;
	}
	
	public int[][] getPitPlacement(){
		return pit_placement;
	}
	
	
	
	
//	public void move_left( )
//	{
//		int next = 0;
//		
//		try
//		{
//			next = robot_placement[robo_y][robo_x-1];
//		}
//		catch( ArrayIndexOutOfBoundsException e ){ game_lost = true; repaint(); return;  }
//		
//		
//		if ( wumpus_placement[robo_y][robo_x-1] == WUMPUS )
//		{
//			game_lost = true;
//		}
//		else if ( pit_placement[robo_y][robo_x-1] == PIT )
//		{
//			game_lost = true;
//		}
//		else if ( gold_placement[robo_y][robo_x-1] == GOLD )
//		{
//			has_gold = true;
//		}
//		
//		robot_placement[robo_y][robo_x] = 0;
//		
//		robot_placement[robo_y][robo_x-1] = 1;
//		
//		robo_x = robo_x-1;	
//		
//		if ( has_gold )
//		{
//			for( int row = 0 ; row < BOARD_SIZE ; row++ )
//			{
//				for( int col = 0 ; col < BOARD_SIZE ; col++ )
//				{
//					gold_placement[row][col]= 0;
//				}
//			}
//			
//			gold_placement[robo_y][robo_x] = 1;
//		}
//		
//		if ( robo_x == HOME_X && robo_y == HOME_Y && has_gold )
//		{
//			game_won = true;
//		}
//		
//		repaint();		
//	}
//	
//	public void move_up( )
//	{
//		int next = 0;
//		
//		try
//		{
//			next = robot_placement[robo_y-1][robo_x];
//		}
//		catch( ArrayIndexOutOfBoundsException e ){ game_lost = true; repaint(); return;  }
//		
//		
//		if ( wumpus_placement[robo_y-1][robo_x] == WUMPUS )
//		{
//			game_lost = true;
//		}
//		else if ( pit_placement[robo_y-1][robo_x] == PIT )
//		{
//			game_lost = true;
//		}
//		else if ( gold_placement[robo_y-1][robo_x] == GOLD )
//		{
//			has_gold = true;
//		}
//				
//		robot_placement[robo_y][robo_x] = 0;
//		
//		robot_placement[robo_y-1][robo_x] = 2;
//		
//		robo_y = robo_y-1;	
//		
//		if ( has_gold )
//		{
//			for( int row = 0 ; row < BOARD_SIZE ; row++ )
//			{
//				for( int col = 0 ; col < BOARD_SIZE ; col++ )
//				{
//					gold_placement[row][col]= 0;
//				}
//			}
//			
//			gold_placement[robo_y][robo_x] = 1;
//		}
//		
//		if ( robo_x == HOME_X && robo_y == HOME_Y && has_gold )
//		{
//			game_won = true;
//		}
//		
//		repaint();	
//	}
//	
//	public void move_right( )
//	{
//		int next = 0;
//		
//		try
//		{
//			next = robot_placement[robo_y][robo_x+1];
//		}
//		catch( ArrayIndexOutOfBoundsException e ){ game_lost = true; repaint(); return;  }
//		
//		
//		if ( wumpus_placement[robo_y][robo_x+1] == WUMPUS )
//		{
//			game_lost = true;
//		}
//		else if ( pit_placement[robo_y][robo_x+1] == PIT )
//		{
//			game_lost = true;
//		}
//		else if ( gold_placement[robo_y][robo_x+1] == GOLD )
//		{
//			has_gold = true;
//		}
//				
//		robot_placement[robo_y][robo_x] = 0;
//		
//		robot_placement[robo_y][robo_x+1] = 3;
//		
//		robo_x = robo_x + 1;	
//		
//		if ( has_gold )
//		{
//			for( int row = 0 ; row < BOARD_SIZE ; row++ )
//			{
//				for( int col = 0 ; col < BOARD_SIZE ; col++ )
//				{
//					gold_placement[row][col]= 0;
//				}
//			}
//			
//			gold_placement[robo_y][robo_x] = 1;
//		}
//		
//		if ( robo_x == HOME_X && robo_y == HOME_Y && has_gold )
//		{
//			game_won = true;
//		}
//		
//		repaint();	
//	}
//	
//	public void move_down( )
//	{
//		int next = 0;
//		
//		try
//		{
//			next = robot_placement[robo_y+1][robo_x];
//		}
//		catch( ArrayIndexOutOfBoundsException e ){ game_lost = true; repaint(); return;  }
//		
//		
//		if ( wumpus_placement[robo_y+1][robo_x] == WUMPUS )
//		{
//			game_lost = true;
//		}
//		else if ( pit_placement[robo_y+1][robo_x] == PIT )
//		{
//			game_lost = true;
//		}
//		else if ( gold_placement[robo_y+1][robo_x] == GOLD )
//		{
//			has_gold = true;
//		}
//				
//		robot_placement[robo_y][robo_x] = 0;
//		
//		robot_placement[robo_y+1][robo_x] = 4;
//		
//		robo_y = robo_y + 1;	
//		
//		if ( has_gold )
//		{
//			for( int row = 0 ; row < BOARD_SIZE ; row++ )
//			{
//				for( int col = 0 ; col < BOARD_SIZE ; col++ )
//				{
//					gold_placement[row][col]= 0;
//				}
//			}
//			
//			gold_placement[robo_y][robo_x] = 1;
//		}
//		
//		if ( robo_x == HOME_X && robo_y == HOME_Y && has_gold )
//		{
//			game_won = true;
//		}
//		
//		repaint();	
//	}
//	
//	public void shoot( )
//	{
//		arrows -= 1;
//		
//		int next = 0;		
//		int next_x = 0;
//		int next_y = 0;
//		
//		if ( arrows < 0 )
//		{
//			arrows = 0;
//			repaint();
//			return;
//		}
//		
//		try
//		{
//			if ( robot_placement[robo_y][robo_x] == 1 && wumpus_placement[robo_y][robo_x-1] == 1 ) // FACING LEFT
//			{
//				wumpus_placement[robo_y][robo_x-1] = 0;
//				dead_wumpus_placement[robo_y][robo_x-1] = 1;
//			}
//			else if ( robot_placement[robo_y][robo_x] == 2 && wumpus_placement[robo_y-1][robo_x] == 1 ) // FACING UP
//			{
//				wumpus_placement[robo_y-1][robo_x] = 0;
//				dead_wumpus_placement[robo_y-1][robo_x] = 1;
//			}
//			else if ( robot_placement[robo_y][robo_x] == 3 && wumpus_placement[robo_y][robo_x+1] == 1 ) // FACING RIGHT
//			{
//				wumpus_placement[robo_y][robo_x+1] = 0;
//				dead_wumpus_placement[robo_y][robo_x+1] = 1;
//			}
//			else if ( robot_placement[robo_y][robo_x] == 4 && wumpus_placement[robo_y+1][robo_x] == 1 ) // FACING DOWN
//			{
//				wumpus_placement[robo_y+1][robo_x] = 0;
//				dead_wumpus_placement[robo_y+1][robo_x] = 1;
//			}
//
//		}
//		catch( ArrayIndexOutOfBoundsException e ){ /*System.out.println( "DEBUG: Out of bounds.");*/  }
//		
//		repaint();		
//	}
	
//	public void update( )
//	{
//		repaint();
//	}
	
	
}