import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WumpusWorldExplorer {
	
	private EnvironmentSetup env;
	private BoardGUI gui;
	private BoardMaker boardMaker;
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
		setupBoardMaker(boardMaker);
		env = boardMaker.getEnvironment();
		
		boardMatrix = env.getBoard();
		breeze_placement = env.getBreezeMatrix();
		glitter_placement = env.getGlitterMatrix();
		smell_placement = env.getSmellMatrix();
		gold_placement = env.getGoldPlacement();
		pit_placement = env.getPitPlacement();	
		wumpus_placement = env.getWumpusPlacement();
	}
	
	

	
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
//		count++;
		dfsExplore(curNodeID);
//		printRoute();
		boardMaker.set_Game_WON(true);
//		printSense(arrows, "[SENSE]: sense);
		
	}
	
	public void dfsExplore(int curNodeID) {
		String sense = "";
		System.out.println();
		if(GAME_OVER) {
			return;
		}
		if(pit_placement[curNodeID / 10][curNodeID % 10] == 1 && wumpus_placement[curNodeID / 10][curNodeID % 10] == 1) {
			sense = "** DEAD! GAME LOST! **";
			boardMaker.printSense(arrows, "[SENSE]: " + sense);
			System.out.println("Node: "+curNodeID+"\n ** DEAD! **");
			boardMaker.set_Game_LOST(true);
			GAME_OVER = true;
			return;
		}
		
		if(gold_placement[curNodeID / 10][curNodeID % 10] == 1) {
//			printSense(arrows, "[SENSE]: sense);
			sense = "** GOT GOLD **";
			boardMaker.printSense(arrows, "[SENSE]: " + sense);
			System.out.println("Node: "+curNodeID+"\n ** GOT GOLD **");
			boardMaker.set_Has_GOLD(true);
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
		
		if(BREEZE) sense += "-Breeze-";
		if(SMELL) sense += "-Breeze-";
		if(!BREEZE && !SMELL) sense += "-OK--No Risk-";
		
		System.out.println("Node: "+curNodeID+"\n[SENSE]: " +sense);
		boardMaker.printSense(arrows, "[SENSE]: " + sense);
		boardMaker.drawWumpusWorldEnvironment(curNodeID / 10, curNodeID % 10);
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ArrayList<Integer> safeList = new ArrayList<Integer>();
		ArrayList<Integer> unsafeList = new ArrayList<Integer>();
		int adjNodeID;
		
		for(int i = 0; i < 4; i++) {
			
			adjNodeID = relationships[curNodeID][i];
			if(adjNodeID == -1 || visited[adjNodeID / 10][adjNodeID % 10] == 1) continue;
			
			
			boolean PIT_ENTAILED_IN_THIS_MOVE = false, WUMPUS_ENTAILED_IN_THIS_MOVE = false;
			
			if(BREEZE && pit_possibility[adjNodeID / 10][adjNodeID % 10] != 0) {
				if(pit_possibility[adjNodeID / 10][adjNodeID % 10] == -1 && wumpus_possibility[adjNodeID / 10][adjNodeID % 10] == 1) {
//					pit_possibility[adjNodeID / 10][adjNodeID % 10] = -1;
//					wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = -1;
					pit_possibility[adjNodeID / 10][adjNodeID % 10] = 0;
					wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = 0;
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
//						pit_possibility[adjNodeID / 10][adjNodeID % 10] = -1;
//						wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = -1;
						pit_possibility[adjNodeID / 10][adjNodeID % 10] = 0;
						wumpus_possibility[adjNodeID / 10][adjNodeID % 10] = 0;
						
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
			for(int k = 0; k < NumOfSafeCell; k++) {
				parent[safeCell[k]] = curNodeID;
				visited[ safeCell[k] / 10 ][ safeCell[k] % 10 ] = 1;

//				if(count<=80)dfsExplore(safeCell[j]);
				dfsExplore(safeCell[k]);
//				count++;
//				if(count >= 3 && !GAME_OVER) {
//					count = 0;
//					if(NumOfUnsafeCell > 0) {
//						int riskyNodeID = -1;
//						for(int j = 0; j < NumOfUnsafeCell; j++) {
//							if(wumpus_possibility[ unsafeCell[j] / 10 ][ unsafeCell[j] % 10 ] == 2) {
//								riskyNodeID = unsafeCell[j];
//								String side = "";
//								if(riskyNodeID == curNodeID-1) side += "LEFT";
//								else if(riskyNodeID == curNodeID+1) side += "RIGHT";
//								else if(riskyNodeID == curNodeID-10) side += "UP";
//								else if(riskyNodeID == curNodeID+10) side += "DOWN";
////								arrows--;
////								printSense(arrows, "[DECISION]: WUMPUS should be in "+side+"-Throw Arrow-");
//								break;
//							}					
//						}
//						if(riskyNodeID == -1) {		
//							do {
//								Random rand = new Random();
//								int x = rand.nextInt( NumOfUnsafeCell );
//								riskyNodeID = unsafeCell[x];
//							}while(pit_possibility[ riskyNodeID / 10 ][ riskyNodeID % 10 ] == 2);
//							String side = "";
//							if(riskyNodeID == curNodeID-1) side += "LEFT";
//							else if(riskyNodeID == curNodeID+1) side += "RIGHT";
//							else if(riskyNodeID == curNodeID-10) side += "UP";
//							else if(riskyNodeID == curNodeID+10) side += "DOWN";
//							if(SMELL) {	
//								if(wumpus_placement[ riskyNodeID / 10 ][ riskyNodeID % 10 ] == 1) {
//									System.out.println("Node: "+riskyNodeID+"\n ** WUMPUS DEAD! **");
//								}
////								arrows--;
////								printSense(arrows, "[DECISION]: Moving "+side+"-Throwing Arrow for safety-");
////								try {
////								Thread.sleep(SLEEP - 200);
////								} catch (InterruptedException e) {
////									e.printStackTrace();
////								}
////								printSense(arrows, "[SENSE]: ** WUMPUS DEAD **");
//							}
//							else {
////								printSense(arrows, "[DECISION]: -Taking RISK- Moving "+side);
//							}
//							
//						}
//						parent[riskyNodeID] = curNodeID;
//						visited[ riskyNodeID / 10 ][ riskyNodeID % 10 ] = 1;
////						if(count<=20)dfsExplore(riskyNodeID);
//						dfsExplore(riskyNodeID);
//					}
//				}
				if(!GAME_OVER) {
					boardMaker.printSense(arrows, "[DECISION]: --Stepping Back--");
					boardMaker.drawWumpusWorldEnvironment(safeCell[k] / 10, safeCell[k] % 10);
					try {
						Thread.sleep(SLEEP);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		else if(NumOfUnsafeCell > 0) {
			int riskyNodeID = -1;
			for(int j = 0; j < NumOfUnsafeCell; j++) {
				if(wumpus_possibility[ unsafeCell[j] / 10 ][ unsafeCell[j] % 10 ] == 2) {
					riskyNodeID = unsafeCell[j];
					String side = "";
					if(riskyNodeID == curNodeID-1) side += "LEFT";
					else if(riskyNodeID == curNodeID+1) side += "RIGHT";
					else if(riskyNodeID == curNodeID-10) side += "UP";
					else if(riskyNodeID == curNodeID+10) side += "DOWN";
//					arrows--;
//					printSense(arrows, "[DECISION]: WUMPUS should be in "+side+"-Throw Arrow-");
					break;
				}					
			}
			if(riskyNodeID == -1) {		
				do {
					Random rand = new Random();
					int x = rand.nextInt( NumOfUnsafeCell );
					riskyNodeID = unsafeCell[x];
				}while(pit_possibility[ riskyNodeID / 10 ][ riskyNodeID % 10 ] == 2);
				String side = "";
				if(riskyNodeID == curNodeID-1) side += "LEFT";
				else if(riskyNodeID == curNodeID+1) side += "RIGHT";
				else if(riskyNodeID == curNodeID-10) side += "UP";
				else if(riskyNodeID == curNodeID+10) side += "DOWN";
				if(SMELL) {	
					arrows--;
					boardMaker.printSense(arrows, "[DECISION]: Moving "+side+"-Throwing Arrow for safety-");
					try {
						Thread.sleep(SLEEP - 200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					
					if(wumpus_placement[ riskyNodeID / 10 ][ riskyNodeID % 10 ] == 1) {
						System.out.println("Node: "+riskyNodeID+"\n ** WUMPUS DEAD! **");
						boardMaker.printSense(arrows, "[SENSE]: ** WUMPUS DEAD **");
					}
				}
				else {
					boardMaker.printSense(arrows, "[DECISION]: -Taking RISK- Moving "+side);
				}
				
			}
			try {
				Thread.sleep(SLEEP - 200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			parent[riskyNodeID] = curNodeID;
			visited[ riskyNodeID / 10 ][ riskyNodeID % 10 ] = 1;
//			if(count<=20)dfsExplore(riskyNodeID);
			dfsExplore(riskyNodeID);
		}
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
	
	public void setupBoardMaker(BoardMaker board) {
		this.boardMaker = board;
	}
	
}
