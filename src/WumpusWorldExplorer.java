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
		
		
		System.out.println("HERE I AM!");
		
		
		ArrayList<Integer> queue = new ArrayList<Integer>();
//		boolean solution_exists = false;
		
		int[][] marked = new int[BOARD_SIZE][BOARD_SIZE];
		int[][] nodesID = new int[BOARD_SIZE][BOARD_SIZE];
		int[][] relationships = new int[BOARD_SIZE*BOARD_SIZE][BOARD_SIZE*BOARD_SIZE];
		parent = new int[BOARD_SIZE*BOARD_SIZE];
		int node_count = 0;
		
		for ( int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++ )
		{
				parent[i] = -1;
	
		}
		
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
//				 solution_exists = true;
				goldNode = node;
				break;
			}
			else 
			{
				for ( int i = 0 ; i < BOARD_SIZE*BOARD_SIZE ; i++ )
				{
					if ( relationships[node][i] == 1 && pit_placement[ ( int ) i / 10 ][ (int) i % 10 ] != 1 && marked[ ( int ) i / 10 ][ (int) i % 10 ] != 1 )
					{
						System.out.println("HERE I AM!");
						queue.add( nodesID[ ( int ) i / 10 ][ (int) i % 10 ] );
						marked[ ( int ) i / 10 ][ (int) i % 10 ] = 1;
						parent[ nodesID[ ( int ) i / 10 ][ (int) i % 10 ]] = node;
					}
				}
			}
		}
		
		
		printRoute();
		
		
	}
	
	public void printRoute() {
		System.out.println();
		System.out.println("HERE IN PRINT ROUTE");
		ArrayList<Integer> list = new ArrayList<Integer>();
		int node = goldNode;
		System.out.println(goldNode);
		while(node != -1) {
			list.add(node);
			node = parent[node];
			System.out.println("HERE!!!");
		}
		
		Collections.reverse(list);
		
		System.out.println(); 
		for(int i: list) {  
		    System.out.print(i + " ");  
		}  
	
		System.out.println(); 
	}
	
}
