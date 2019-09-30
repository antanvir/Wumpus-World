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
	
	static  int WUMPUS_LIMIT = 2;
	static  int PIT_LIMIT    = 15;
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

	public EnvironmentSetup(int pit, int wumpus){	
		PIT_LIMIT = pit;
		WUMPUS_LIMIT = wumpus;
		if(pit != -1 && wumpus != -1) {
			generate_board();
		}
		else {
			
		}
	}
	

	

	public void generate_board( )
	{
		robot_placement = new int [10][10];
		robot_placement[9][0] = 1;
		
		board = new int [10][10];
		
		do{		
			Random rand = new Random();
			
			pit_placement = new int [10][10];
			wumpus_placement = new int [10][10];
			gold_placement = new int [10][10];
			
			breeze_placement = new int [10][10];
			smell_placement = new int [10][10];
			glitter_placement = new int [10][10];
			
			
			for ( int row = 0; row < BOARD_SIZE; row++) 
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
				
		
			for(int i = 0; i< PIT_LIMIT; i++) { 
				int x = rand.nextInt( 100 );
				int row = x / 10;
				int col = x % 10;
				if (  (row == BOARD_SIZE-1 && (col == 0 || col == 1)) || (row == BOARD_SIZE-2  && col == 0 )  ) {
					i --;
					continue;				
				}
				pit_placement[row][col] = 1;
			}

			
			for(int i = 0; i< WUMPUS_LIMIT; i++) { 
				int x = rand.nextInt( 100 );
				int row = x / 10;
				int col = x % 10;
				if (  (row == BOARD_SIZE-1 && (col == 0 || col == 1)) || (row == BOARD_SIZE-2  && col == 0 )  ){
					i --;
					continue;				
				}
				if ( pit_placement[row][col] == 1 ){
					i --;
					continue;				
				}
				wumpus_placement[row][col] = 1;
			}
					
			for(int i = 0; i< GOLD_LIMIT; i++) {
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

			}
			
		}while( !bfs( ) );
		
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
		
//		ClearClutterOfDanger();
//		PlaceBreeze();
//		PlaceSmell();
//		PlaceGlitter();
//		PlacePits();
//		PlaceWumpus();
		
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
				
							
					try
					{
						if( pit_placement[row][col-1] != 1 ) breeze_placement[row][col-1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
					{
						if( pit_placement[row-1][col] != 1 ) breeze_placement[row-1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
					{
						if( pit_placement[row][col+1] != 1 ) breeze_placement[row][col+1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
					{
						if( pit_placement[row+1][col] != 1 ) breeze_placement[row+1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }	
				}
				
				if ( wumpus_placement[row][col] == 1 )
				{
					
							
					try 
					{
						if( pit_placement[row][col-1] != 1 ) smell_placement[row][col-1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
					{
						if( pit_placement[row-1][col] != 1 ) smell_placement[row-1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
					{
						if( pit_placement[row][col+1] != 1 ) smell_placement[row][col+1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
					{
						if( pit_placement[row+1][col] != 1 ) smell_placement[row+1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }	
				}
				
				if ( gold_placement[row][col] == 1 )
				{
					
							
					try 
					{
						if ( pit_placement[row][col-1] != 1 ) glitter_placement[row][col-1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
					{
						if ( pit_placement[row-1][col] != 1 ) glitter_placement[row-1][col] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
					{
						if ( pit_placement[row][col+1] != 1 ) glitter_placement[row][col+1] = 1;
					}
					catch( ArrayIndexOutOfBoundsException e ){  }
		
					try 
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
		
		for ( int row = 0; row < BOARD_SIZE; row++ ) 
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
		
		for ( int row = 0; row < BOARD_SIZE; row++ ) 
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				try
				{
					relationships[ nodesID[row][col] ][ nodesID[row][col-1]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try 
				{
					relationships[ nodesID[row][col] ][ nodesID[row-1][col]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try 
				{
					relationships[ nodesID[row][col] ][ nodesID[row][col+1]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try 
				{
					relationships[ nodesID[row][col] ][ nodesID[row+1][col]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }				 
			}
		}
		
		queue.add( nodesID[BOARD_SIZE-1][0 ] );
		marked[BOARD_SIZE-1][0 ] = 1;
		
		while( !queue.isEmpty() ) 
		{
			int node = queue.remove( 0 );
			
			if( gold_placement[ ( int ) node / 10 ][ (int) node % 10 ] == 1 ) 
			{
				solution_exists = true;
				break;
			}
			else 
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
	
	public int[][] getWumpusPlacement(){
		return wumpus_placement;
	}

	
}
