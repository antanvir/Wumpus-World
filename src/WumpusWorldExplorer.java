import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WumpusWorldExplorer {
	
	private EnvironmentSetup env;
	private BoardGUI gui;
	private BoardMaker maker;
	private int[][] boardMatrix, markedRoute, breeze_placement, pit_placement,
			glitter_placement, smell_placement, gold_placement, wumpus_placement;	
	private int[] parent, marked;	
	int count = 0;
	static final int BOARD_SIZE = 10;
	private int arrows = 2;
	private int goldNode;
	private int agent_row = 9, agent_col = 0;
	private boolean GAME_OVER = false;
	private int SLEEP = 500;	
	private int[][] pit_possibility, wumpus_possibility, cell_OK, visited, unvisited,
						nodesID, relationships;
	
	
	public WumpusWorldExplorer(BoardMaker boardMaker) {
		
		env = boardMaker.getEnvironment();
		
		boardMatrix = env.getBoard();
		breeze_placement = env.getBreezeMatrix();
		glitter_placement = env.getGlitterMatrix();
		smell_placement = env.getSmellMatrix();
		gold_placement = env.getGoldPlacement();
		pit_placement = env.getPitPlacement();	
		wumpus_placement = env.getWumpusPlacement();
	}
	
	
//	public void exploreWorld( )
//	{
//		
//		
//		System.out.println("HERE I AM!");
//		
//		
//		ArrayList<Integer> queue = new ArrayList<Integer>();
////		boolean solution_exists = false;
//		
//		int[][] marked = new int[BOARD_SIZE][BOARD_SIZE];
//		int[][] nodesID = new int[BOARD_SIZE][BOARD_SIZE];
//		int[][] relationships = new int[BOARD_SIZE*BOARD_SIZE][BOARD_SIZE*BOARD_SIZE];
//		parent = new int[BOARD_SIZE*BOARD_SIZE];
//		int node_count = 0;
//		
//		for ( int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++ )
//		{
//				parent[i] = -1;
//	
//		}
//		
//		for ( int row = 0; row < BOARD_SIZE; row++ )
//		{
//			for ( int col = 0; col < BOARD_SIZE; col++ )
//			{
//				marked[row][col] = 0;
//			}
//		}
//		
//		for ( int row = 0; row < BOARD_SIZE; row++ ) 
//		{
//			for ( int col = 0; col < BOARD_SIZE; col++ )
//			{
//				nodesID[row][col] = node_count;
//				node_count += 1;
//			}
//		}
//		
//		for ( int row = 0; row < BOARD_SIZE*BOARD_SIZE; row++ )
//		{
//			for ( int col = 0; col < BOARD_SIZE*BOARD_SIZE; col++ )
//			{
//				relationships[row][col] = 0;
//			}
//		}
//		
//		for ( int row = 0; row < BOARD_SIZE; row++ ) 
//		{
//			for ( int col = 0; col < BOARD_SIZE; col++ )
//			{
//				try
//				{
//					relationships[ nodesID[row][col] ][ nodesID[row][col-1]  ] = 1;
//				}
//				catch( ArrayIndexOutOfBoundsException e ){  }
//				
//				try 
//				{
//					relationships[ nodesID[row][col] ][ nodesID[row-1][col]  ] = 1;
//				}
//				catch( ArrayIndexOutOfBoundsException e ){  }
//				
//				try 
//				{
//					relationships[ nodesID[row][col] ][ nodesID[row][col+1]  ] = 1;
//				}
//				catch( ArrayIndexOutOfBoundsException e ){  }
//				
//				try 
//				{
//					relationships[ nodesID[row][col] ][ nodesID[row+1][col]  ] = 1;
//				}
//				catch( ArrayIndexOutOfBoundsException e ){  }				 
//			}
//		}
//		
//		queue.add( nodesID[BOARD_SIZE-1][0 ] );
//		marked[BOARD_SIZE-1][0 ] = 1;
//		
//		while( !queue.isEmpty() ) 
//		{
//			int node = queue.remove( 0 );
//			
//			if( gold_placement[ ( int ) node / 10 ][ (int) node % 10 ] == 1 ) 
//			{
////				 solution_exists = true;
//				goldNode = node;
//				break;
//			}
//			else 
//			{
//				for ( int i = 0 ; i < BOARD_SIZE*BOARD_SIZE ; i++ )
//				{
//					if ( relationships[node][i] == 1 && pit_placement[ ( int ) i / 10 ][ (int) i % 10 ] != 1 && marked[ ( int ) i / 10 ][ (int) i % 10 ] != 1 )
//					{
//						System.out.println("HERE I AM!");
//						queue.add( nodesID[ ( int ) i / 10 ][ (int) i % 10 ] );
//						marked[ ( int ) i / 10 ][ (int) i % 10 ] = 1;
//						parent[ nodesID[ ( int ) i / 10 ][ (int) i % 10 ]] = node;
//					}
//				}
//			}
//		}
//		
//		
//		printRoute();
//		
//		
//	}
	
	public void startExploring(){
		
		marked = new int[BOARD_SIZE*BOARD_SIZE];
		nodesID = new int[BOARD_SIZE][BOARD_SIZE];
		relationships = new int[BOARD_SIZE*BOARD_SIZE][4];
		parent = new int[BOARD_SIZE*BOARD_SIZE];
		
		int node_count = 0;
		int curNodeID = agent_row * 10 + agent_col;
		
		for ( int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++ )
		{
				parent[i] = -1;
	
		}
		
		
		for ( int row = 0; row < BOARD_SIZE; row++ ) 
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				nodesID[row][col] = node_count;
				node_count += 1;
			}
		}
		
		
		for ( int row = 0; row < BOARD_SIZE; row++ ) 
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				if(row -1 >= 0) {
					relationships[ nodesID[row][col] ][ 0 ] = nodesID[row -1][col];
				}
				else relationships[ nodesID[row][col] ][ 0 ] = -1;
				
				if(col -1 >= 0) {
					relationships[ nodesID[row][col] ][ 1 ] = nodesID[row][col-1];
				}
				else relationships[ nodesID[row][col] ][ 1 ] = -1;
				
				if(col +1 <= 9) {
					relationships[ nodesID[row][col] ][ 2 ] = nodesID[row][col+1];
				}
				else relationships[ nodesID[row][col] ][ 2 ] = -1;
				
				if(row +1 <= 9) {
					relationships[ nodesID[row][col] ][ 3 ] = nodesID[row+1][col];
				}
				else relationships[ nodesID[row][col] ][ 3 ] = -1;
				
				
//				try
//				{
//					relationships[ nodesID[row][col] ][ 0 ] = nodesID[row -1][col];
//				}
//				catch( ArrayIndexOutOfBoundsException e ){  }
				
//				try
//				{
//					relationships[ nodesID[row][col] ][ 1 ] = nodesID[row][col-1];
//				}
//				catch( ArrayIndexOutOfBoundsException e ){  }
				
//				try
//				{
//					relationships[ nodesID[row][col] ][ 2 ] = nodesID[row][col+1];
//				}
//				catch( ArrayIndexOutOfBoundsException e ){  }
//				
//				try
//				{
//					relationships[ nodesID[row][col] ][ 3 ] = nodesID[row+1][col];
//				}
//				catch( ArrayIndexOutOfBoundsException e ){  }
			}
		}
		
		for ( int row = 0; row < BOARD_SIZE*BOARD_SIZE; row++ ) 
		{
			System.out.print("Node: " + row + "\t| ");
			for ( int col = 0; col < 4; col++ )
			{
				System.out.print(relationships[row][col] + " ");
			}
			System.out.println();
		}
		
		
		pit_possibility = new int[BOARD_SIZE][BOARD_SIZE];
		wumpus_possibility = new int[BOARD_SIZE][BOARD_SIZE];
		cell_OK = new int[BOARD_SIZE][BOARD_SIZE];
		visited = new int[BOARD_SIZE][BOARD_SIZE];
		int adjNodeID;
		
		
		for ( int row = 0; row < BOARD_SIZE; row++ )
		{
			for ( int col = 0; col < BOARD_SIZE; col++ )
			{
				pit_possibility[row][col] = -1;
				wumpus_possibility[row][col] = -1;
				visited[row][col] = 0;
			}
		}
		
		visited[9][0] = 1;
		count++;
		dfsExplore(curNodeID);
		
	}
	
	public void dfsExplore(int curNodeID) {
		if(GAME_OVER) {
			return;
		}
		if(pit_placement[curNodeID / 10][curNodeID % 10] == 1 && wumpus_placement[curNodeID / 10][curNodeID % 10] == 1) {
//			printSense(arrows, "[SENSE]: sense);
			System.out.println("Node: "+curNodeID+"\n ** GAME LOST! DEAD! **");
			GAME_OVER = true;
			return;
		}
		
		if(gold_placement[curNodeID / 10][curNodeID % 10] == 1) {
//			printSense(arrows, "[SENSE]: sense);
			System.out.println("Node: "+curNodeID+"\n ** GAME WON **");
			GAME_OVER = true;
			return;
		}
				
		boolean BREEZE = false, SMELL = false;
		if(breeze_placement[curNodeID / 10][curNodeID % 10] == 1) {
			BREEZE = true;
		}
		if(smell_placement[curNodeID / 10][curNodeID % 10] == 1) {
			SMELL = true;
		}
		
		String sense = "";
		if(BREEZE) sense += "-Breeze-";
		if(SMELL) sense += "-Breeze-";
		if(!BREEZE && !SMELL) sense += "-OK--No Risk-";
		
		System.out.println("Node: "+curNodeID+"\n[SENSE]: " +sense);
//		printSense(arrows, "[SENSE]: sense);
//		drawWumpusWorldEnvironment(safeCell[j] / 10, safeCell[j] % 10);
//		try {
//			Thread.sleep(SLEEP);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		ArrayList<Integer> safeList = new ArrayList<Integer>();
		ArrayList<Integer> unsafeList = new ArrayList<Integer>();
		int adjNodeID;
		
		for(int i = 0; i < 4; i++) {
			
			adjNodeID = relationships[curNodeID][i];
			if(adjNodeID == -1 || visited[adjNodeID / 10][adjNodeID % 10] == 1) continue;
			
			boolean PIT_ENTAILED_IN_THIS_MOVE = false, WUMPUS_ENTAILED_IN_THIS_MOVE = false;
			
			if(BREEZE && pit_possibility[adjNodeID / 10][adjNodeID % 10] != 0) {
				if(pit_possibility[adjNodeID / 10][adjNodeID % 10] == -1 && wumpus_possibility[adjNodeID / 10][adjNodeID % 10] == 1) {
					pit_possibility[adjNodeID / 10][adjNodeID % 10] = -1;
					wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = -1;
				}
				else {
					if (pit_possibility[adjNodeID / 10][adjNodeID % 10] == 1) PIT_ENTAILED_IN_THIS_MOVE = false;
					else PIT_ENTAILED_IN_THIS_MOVE = true;
					
					if(PIT_ENTAILED_IN_THIS_MOVE) pit_possibility[adjNodeID / 10][adjNodeID % 10] = 1;
					else pit_possibility[adjNodeID / 10][adjNodeID % 10] = 2;
				}
			}
			else {
				pit_possibility[adjNodeID / 10][adjNodeID % 10] = 0;
			}
			
			if(SMELL && wumpus_possibility[adjNodeID / 10][adjNodeID % 10] != 0) {
				if(wumpus_possibility[adjNodeID / 10][adjNodeID % 10] == -1 && pit_possibility[adjNodeID / 10][adjNodeID % 10] == 1) {
					
					if(PIT_ENTAILED_IN_THIS_MOVE) {
						wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = 1;
					}
					else {
						pit_possibility[adjNodeID / 10][adjNodeID % 10] = -1;
						wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = -1;
					}
				}
				else {
					if(wumpus_possibility[adjNodeID / 10][adjNodeID % 10] == 1) wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = 2;
					else wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = 1;
				}
			}
			else {
				wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = 0;
			}
			
			if((!BREEZE && !SMELL) || 
				(wumpus_possibility[adjNodeID / 10][adjNodeID % 10] <=0 && pit_possibility[adjNodeID / 10][adjNodeID % 10] <=0)) {
				safeList.add(adjNodeID);
			}
			else {
				unsafeList.add(adjNodeID);
			}				
		}
		
		int NumOfSafeCell = safeList.size();	int NumOfUnsafeCell = unsafeList.size();
		int[] safeCell = new int[NumOfSafeCell], unsafeCell = new int[NumOfUnsafeCell];
		
		for(int j = 0; j < NumOfSafeCell; j++) {
			safeCell[j] = safeList.get(j);
			System.out.println("safe: " +safeCell[j]);
		}
		
		for(int j = 0; j < NumOfUnsafeCell; j++) {
			unsafeCell[j] = unsafeList.get(j);
			System.out.println("unsafe: " +unsafeCell[j]);
		}		
		
		if(NumOfSafeCell > 0) {
			for(int j = 0; j < NumOfSafeCell; j++) {
				parent[safeCell[j]] = curNodeID;
				visited[ safeCell[j] / 10 ][ safeCell[j] % 10 ] = 1;
				count++;
				if(count<=80)dfsExplore(safeCell[j]);
//				dfsExplore(safeCell[j]);
				
//				if(!GAME_OVER) {
//					printSense(arrows, "[DECISION]: --Stepping Back--");
//					drawWumpusWorldEnvironment(safeCell[j] / 10, safeCell[j] % 10);
//					try {
//						Thread.sleep(SLEEP);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			}
		}
		
//		else {
//			int riskyNodeID = -1;
//			for(int j = 0; j < NumOfUnsafeCell; j++) {
//				if(wumpus_possibility[ unsafeCell[j] / 10 ][ unsafeCell[j] % 10 ] == 2) {
//					riskyNodeID = unsafeCell[j];
//					String side = "";
//					if(riskyNodeID == curNodeID-1) side += "LEFT";
//					else if(riskyNodeID == curNodeID+1) side += "RIGHT";
//					else if(riskyNodeID == curNodeID-10) side += "UP";
//					else if(riskyNodeID == curNodeID+10) side += "DOWN";
////					arrows--;
////					printSense(arrows, "[DECISION]: WUMPUS should be in "+side+"-Throw Arrow-");
//					break;
//				}					
//			}
//			if(riskyNodeID == -1) {		
//				do {
//					Random rand = new Random();
//					int x = rand.nextInt( NumOfUnsafeCell );
//					riskyNodeID = unsafeCell[x];
//				}while(pit_possibility[ riskyNodeID / 10 ][ riskyNodeID % 10 ] == 2);
//				String side = "";
//				if(riskyNodeID == curNodeID-1) side += "LEFT";
//				else if(riskyNodeID == curNodeID+1) side += "RIGHT";
//				else if(riskyNodeID == curNodeID-10) side += "UP";
//				else if(riskyNodeID == curNodeID+10) side += "DOWN";
//				if(SMELL) {	
//					if(wumpus_placement[ riskyNodeID / 10 ][ riskyNodeID % 10 ] == 1) {
//						System.out.println("Node: "+riskyNodeID+"\n ** WUMPUS DEAD! **");
//					}
////					arrows--;
////					printSense(arrows, "[DECISION]: Moving "+side+"-Throwing Arrow for safety-");
////					try {
////					Thread.sleep(SLEEP - 200);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
////					printSense(arrows, "[SENSE]: ** WUMPUS DEAD **");
//				}
//				else {
////					printSense(arrows, "[DECISION]: -Taking RISK- Moving "+side);
//				}
//				
//			}
//			parent[riskyNodeID] = curNodeID;
//			visited[ riskyNodeID / 10 ][ riskyNodeID % 10 ] = 1;
//			count++;
//			if(count<=20)dfsExplore(riskyNodeID);
////			dfsExplore(riskyNodeID);
//		}
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
