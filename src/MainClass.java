import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class MainClass {
	
	public static void main(String[] args) {
		
		final int width = 600;
//		final int height = 800;

		final GUI_Frame gui = new GUI_Frame(width, width, "-- WUMPUS WORLD --");
		
		BoardMaker board = new BoardMaker(width, 10);
				
//		final TwoPlayerGame humanGame = new TwoPlayerGame(board);
//		final AI_Game game = new AI_Game(board);
			

		gui.listenGameStartButton(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				

				boolean computerStarts = false;
				int depth = 4;
				boolean playingWithComputer = gui.playWithComputer();
//				System.out.println(playingWithComputer);
//				
//				System.out.println("Depth: " + depth + " AI Makes the first move: " + computerStarts );
				
				gui.attachBoard(board.getGUI());			
				gui.showBoard();
				
				WumpusWorldExplorer game = new WumpusWorldExplorer(board);
				game.exploreWorld();
				game.printRoute();
				
//				if(playingWithComputer) {
//
//					game.setAIDepth(depth);
//					game.setAIStarts(computerStarts);
//					
//					game.start();
//
//				}
//				else {									
//
//					humanGame.start();
//				}
				
			}
			
		});
		
		
		
		
		
		
	}
}
