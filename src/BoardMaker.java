import java.awt.event.MouseListener;
import java.util.ArrayList;


public class BoardMaker {
	
	private BoardGUI gui;
	private int[][] boardMatrix; // 0: Empty,  1: White,  2: Black
//	private EnvironmentSetup env;
//	private int[][] breeze_placement, glitter_placement, smell_placement;
	
	public BoardMaker() {
		
	}
	
	public BoardMaker(int boardWidth, int totalCell) {
		gui = new BoardGUI(boardWidth, totalCell);
		boardMatrix = new int[totalCell][totalCell];
//		env = new EnvironmentSetup();
	}
	
	public BoardMaker(BoardMaker board) {
//		breeze_placement = env.getBreezeMatrix();
//		glitter_placement = env.getGlitterMatrix();
//		smell_placement = env.getSmellMatrix();
		
		int[][] matrixToCopy = board.getBoardMatrix();		
		boardMatrix = new int[matrixToCopy.length][matrixToCopy.length];
				
		for(int i=0;i<matrixToCopy.length; i++) {
			for(int j=0; j<matrixToCopy.length; j++) {
				boardMatrix[i][j] = matrixToCopy[i][j];
			}
		}
		
	}
	
	
	public void startListening(MouseListener listener) {
		gui.attachListener(listener);
	}
	
	
	public void addStoneNoGUI(int posX, int posY, boolean human) {
		boardMatrix[posY][posX] = human ? 2 : 1;
	}
	
	
	public boolean addStone(int posX, int posY, boolean human) {
		
		if(boardMatrix[posY][posX] != 0) return false;
		
		gui.drawStone(posX, posY, human);
		boardMatrix[posY][posX] = human ? 2 : 1;
		return true;
		
	}
	

	public int getBoardSize() {
		return boardMatrix.length;
	}
	
	
	public int[][] getBoardMatrix() {
		return boardMatrix;
	}
	
	
	public BoardGUI getGUI() {
		return gui;
	}
	
	
	public EnvironmentSetup getEnvironment() {
		System.out.println(gui.getEnvironment().getBoard());
		return gui.getEnvironment();
	}
	
	public int getRelativePos(int x, boolean isWidth) {
		return gui.getRelativePos(x, isWidth);
	}
	
	
	public void printWinner(int winner, String text) {
		gui.printWinner(winner, text);
	}
	
	
	public void printSense(int arrows, String text) {
		gui.printSense(arrows, text);
	}
	
	
}