import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WumpusWorldExplorer {
	
	private EnvironmentSetup env;
	private BoardGUI gui;
	private BoardMaker maker;
	private int[][] boardMatrix, markedRoute, breeze_placement, pit_placement,
			glitter_placement, smell_placement, gold_placement;
	
	private int[] parent;
	
	static final int BOARD_SIZE = 10;
	private int arrows = 2;
	private int goldNode;
	
	public WumpusWorldExplorer(BoardMaker boardMaker) {
		

		env = boardMaker.getEnvironment();
		
		boardMatrix = env.getBoard();
		breeze_placement = env.getBreezeMatrix();
		glitter_placement = env.getGlitterMatrix();
		smell_placement = env.getSmellMatrix();
		gold_placement = env.getGoldPlacement();
		pit_placement = env.getPitPlacement();	
		
	}
	
	
	public void exploreWorld( )
	{
		
		int flip_flop = new Random().nextInt( 2 ) ; 
		
		ArrayList<Integer> stack = new ArrayList<Integer>();
		boolean solution_exists = false;
		
		int[][] marked = new int[BOARD_SIZE][BOARD_SIZE];
		int[][] nodes = new int[BOARD_SIZE][BOARD_SIZE];
		int[][] relationships = new int[BOARD_SIZE*BOARD_SIZE][BOARD_SIZE*BOARD_SIZE];
		
		parent = new int[BOARD_SIZE*BOARD_SIZE];
		
		int node_count = 0;	
		int sleep = 3000;
		
		for ( int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++ )
		{
			parent[i] = -1;

		}
		
		
		for ( int row = 0; row < BOARD_SIZE; row++ ) 
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				nodes[row][col] = node_count;
				node_count += 1;
			}
		}
		
		
		for ( int row = 0; row < BOARD_SIZE; row++ ) 
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				try 
				{
					relationships[ nodes[row][col] ][ nodes[row][col-1]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try 
				{
					relationships[ nodes[row][col] ][ nodes[row-1][col]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try 
				{
					relationships[ nodes[row][col] ][ nodes[row][col+1]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }
				
				try 
				{
					relationships[ nodes[row][col] ][ nodes[row+1][col]  ] = 1;
				}
				catch( ArrayIndexOutOfBoundsException e ){  }				 
			}
		}
		
		stack.add( 0, nodes[BOARD_SIZE-1][0 ] );
		marked[BOARD_SIZE-1][0 ] = 1;
		
		try
		{
			Thread.sleep( sleep );
		}
		catch( InterruptedException x ) {}
		
		

		
		while( !stack.isEmpty() ) 
		{
			int node = stack.get( 0 );					
			
			if( smell_placement[ ( int ) node / 10 ][ ( int ) node % 10 ] == 1 )
			{
				if ( arrows != 0 )
				{

				
					try
					{
						Thread.sleep( sleep );
					}
					catch( InterruptedException x ) {}
					
					arrows -= 1;
					if ( arrows < 0 ) arrows = 0;
				}
			}
			
			
			if(gold_placement[ ( int ) node / 10 ][ ( int ) node % 10 ] == 1 ) 
			{
				maker.printSense(arrows, "Something-gold");
				try
				{
					Thread.sleep( sleep );
				}
				catch( InterruptedException x ) {}
				goldNode = node;
				break;
			}
			else 
			{
				boolean added = false;
				
				if ( flip_flop == 0 ) 
				{
					for ( int i = 0 ; i < BOARD_SIZE*BOARD_SIZE ; i++ )
					{
						if ( relationships[node][i] == 1 && pit_placement[ ( int ) i / 10 ][ (int) i % 10 ] != 1 && marked[ ( int ) i / 10 ][ (int) i % 10 ] != 1 )
						{
							stack.add( 0,  nodes[ ( int ) i / 10 ][ (int) i % 10 ] );
							marked[ ( int ) i / 10 ][ (int) i % 10 ] = 1;
							added = true;
							parent[ nodes[ ( int ) i / 10 ][ (int) i % 10 ]] = node;
						}
					}
				}
				
				
				else 
				{
					for ( int i = ( BOARD_SIZE*BOARD_SIZE - 1 ) ; i >= 0 ; i-- )
					{
						if ( relationships[node][i] == 1 && pit_placement[ ( int ) i / 10 ][ (int) i % 10 ] != 1 && marked[ ( int ) i / 10 ][ (int) i % 10 ] != 1 )
						{
							stack.add( 0,  nodes[ ( int ) i / 10 ][ (int) i % 10 ] );
							marked[ ( int ) i / 10 ][ (int) i % 10 ] = 1;
							added = true;
							parent[ nodes[ ( int ) i / 10 ][ (int) i % 10 ]] = node;
						}
					}
				}
				
				if ( !added ) stack.remove( 0 );
			}
			
			node = stack.get( 0 );
			
			try
			{
				Thread.sleep( sleep );
			}
			catch( InterruptedException x ) {}		
			
		}
	}
	
	public void printRoute() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int node = goldNode;
		while(node != -1) {
			list.add(node);
			node = parent[node];
		}
		
		Collections.reverse(list);
		
		System.out.println(); 
		for(int i: list) {  
		    System.out.print(i + " ");  
		}  
	
		System.out.println(); 
	}
	
}
