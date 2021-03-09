package Connect4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class GuiForConnect4 extends JFrame {
	
	JTextField txtInput = new JTextField();

	Color dBlue = new Color(11, 177, 186);
	Color salmon = new Color(255, 96, 96);
	Color deepSalmon = new Color(168, 45, 45);
	
	private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 90);
	private JPanel jpMain, scoreBoard;
	private JLabel turnDis;
	private JLabel Chat;
	C4Board jpBoard;
	private JButton[] topBttn;
	private String bttn[] = {"Drop"};
	
	private PlayerP otherPlayer;
	private PlayerP currentPlayer;
	private PlayerP player1;
	private PlayerP player2;	
	private String inARow;
	private int ConnectX;
	
	public GuiForConnect4() throws FileNotFoundException {
		
		
		PrintStream o = new PrintStream(new File("Connect4-Results.txt"));
		@SuppressWarnings("unused")
		PrintStream console = System.out;
		System.setOut(o);


				
		// player 1 info grab
	    player1 = new PlayerP(JOptionPane.showInputDialog(null, "Player 1! What's your name?"), null);
	    player1 = new PlayerP(player1.getName(), JOptionPane.showInputDialog(null, "Cool. What's your symbol?"));
		
		// player 2 info grab		
		player2 = new PlayerP(JOptionPane.showInputDialog(null, "Player 2! What's your name?"), null);
		while (player2.getName().equalsIgnoreCase(player1.getName())) {
			JOptionPane.showMessageDialog(null, "Name already in use, try again");
			player2 = new PlayerP(JOptionPane.showInputDialog(null, "Player 2! What's your name?"), null);
			}		
		player2 = new PlayerP(player2.getName(), JOptionPane.showInputDialog(null, "Sweet! What's yout symbol?"));
		while (player2.getSymbol().equalsIgnoreCase(player1.getSymbol())) {
			JOptionPane.showMessageDialog(null, "Symbol already in use, try again");
			player2 = new PlayerP(player2.getName(), JOptionPane.showInputDialog(null, "Sweet! What's yout symbol?"));	
		}
		
		while ((ConnectX < 3 || ConnectX > 7)) {	
			inARow = JOptionPane.showInputDialog(null, "How many in a row is considered a win?");
			ConnectX = Integer.parseInt(inARow);
			if (ConnectX < 3 || ConnectX > 7) {
				JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 3 and 7");
			}
		}
		
		
		currentPlayer = player1;
		otherPlayer = player2;
		System.out.println(inARow + " in a row are needed to win this game.");
		
		jpMain = new JPanel();
		jpMain.setLayout(new BorderLayout());

		jpBoard = new C4Board();
		
		scoreBoard = new JPanel();
		turnDis = new JLabel(player1.getName() + "'s score = " + player1.getWins());
		Chat = new JLabel("Let's play Connect " + inARow + "!");
		scoreBoard.add(Chat);
		scoreBoard.add(turnDis);
		
		add(scoreBoard, BorderLayout.NORTH);
		scoreBoard.setBackground(Color.pink);
		scoreBoard.setFont(font);
		scoreBoard.setLayout(new GridLayout(5,5));
		setVisible (true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		jpMain.add(BorderLayout.CENTER, jpBoard);
		add(jpMain);
		setSize (1000,1000);
		setVisible (true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	private class C4Board extends JPanel implements PlayerInterface, BoardInterface, ActionListener{

		private JLabel [][] board;
		private  final int ROWS = 6;
		private final int COLS = 7;
		private int [] tracker = {5,5,5,5,5,5,5};
		

		public C4Board() {
			
			topBttn = new JButton[7]; 
			for (int j= 0; j<topBttn.length; j++) {
				topBttn[j] = new JButton(bttn[0]);
				topBttn[j].addActionListener(this);
				add(topBttn[j]);
			}
			
			setLayout (new GridLayout(ROWS+1, COLS));
			board = new JLabel [ROWS][COLS];
			displayBoard();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
		 JButton btnClicked = (JButton) e.getSource();
		 
		 
		// how turns are taken
		 btnClicked.setEnabled(true);
		 
		 for (int b = 0; b<7; b++) {
			 if (btnClicked.equals(topBttn[b])) {
				 other();
				 Chat.setText("It's your turn: " + otherPlayer.getName());
			 }
		 }
		 

		 for(int j= 0; j< 7; j++) {
			 if ( topBttn[j]==e.getSource()) {
				 int rowToCol =tracker[j];
				 board[rowToCol][j].setText(currentPlayer.getSymbol());
				 tracker[j]--;
				 
				 if (tracker[j]<0) {
					 topBttn[j].setEnabled(false);
					 
				 }
			 } 
		 }
		 
		 // where the winner is determined or the game is a draw
		 int draw = 0;
		 if (isWinner()) {
			 JOptionPane.showMessageDialog(null, "You win, " + currentPlayer.getName() + "!");
			 currentPlayer.addWins();
				System.out.println("The round is over. " + currentPlayer.getName() + " wins.");

			 
			 // asks players to play again
			 while (isWinner()) {
				 int reply = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Do you want to play again?",  JOptionPane.YES_NO_OPTION);
				 if (reply == JOptionPane.YES_OPTION){
					 clearBoard();
				 }
				 else  {
					 if (player1.getWins() > player2.getWins()) {
						 JOptionPane.showMessageDialog(null, player1.getName() + " is victorious!");
					     JOptionPane.showMessageDialog(null, "Thanks for playing!");
						 System.out.println("The game is over. " + currentPlayer.getName() + " is the champion with " + currentPlayer.getWins() + " wins");
						 System.exit(0);
						 }
					 else if ((player1.getWins() < player2.getWins())) {
						 JOptionPane.showMessageDialog(null, player2.getName() + " is victorious!");
						 JOptionPane.showMessageDialog(null, "Thanks for playing!");
						 System.out.println("The game is over. " + currentPlayer.getName() + " is the champion with " + currentPlayer.getWins() + " wins");
					     System.exit(0);
					 }
					 else {
						 JOptionPane.showMessageDialog(null, "Scores are tied. No winner.");
						 JOptionPane.showMessageDialog(null, "Thanks for playing!");
						 System.out.println("The game was a tie. There is no winner.");
					     System.exit(0);
					 }
				 } 
			 }					 
		 }	 
		 
		 
		 else if (isFull()){
			 draw++;
			 JOptionPane.showMessageDialog(null, "The board is full, the game is a draw");
			 
			// asks players to play again
			 while (isFull()) {
				 int reply = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Do you want to play again?",  JOptionPane.YES_NO_OPTION);
				 if (reply == JOptionPane.YES_OPTION){
					 clearBoard();
				 }
				 else  {
					 if (player1.getWins() > player2.getWins()) {
						 JOptionPane.showMessageDialog(null, player1.getName() + " is victorious!");
					     JOptionPane.showMessageDialog(null, "Thanks for playing!");
						 System.out.println("The game is over. " + currentPlayer.getName() + " is the champion with " + currentPlayer.getWins() + " wins");

						 System.exit(0);
						 }
					 else if ((player1.getWins() < player2.getWins())) {
						 JOptionPane.showMessageDialog(null, player2.getName() + " is victorious!");
						 JOptionPane.showMessageDialog(null, "Thanks for playing!");
							System.out.println("The game is over. " + currentPlayer.getName() + " is the champion with " + currentPlayer.getWins() + " wins");

					     System.exit(0);
					 }
					 else {
						 JOptionPane.showMessageDialog(null, "Scores are tied. No winner.");
						 JOptionPane.showMessageDialog(null, "Thanks for playing!");
							System.out.println("The game was a tie. There is no winner.");

					     System.exit(0);
					 }
				 } 
				 
			 }
			 
			 
		 }		 
		 int totalGamesPlayed = player1.getGames() + player2.getGames()+ draw;
		 turnDis.setText(player1.getName() + "'s score = " + player1.getWins()+ "     " + player2.getName() + "'s score = " + player2.getWins() +"     " + "Total Games Played = " + totalGamesPlayed);
		 
		 
			takeTurn();
		}
		@Override
		public boolean isWinner() {
			if(isWinnerInRow() || isWinnerInCol() || isWinnerInMainDiag() || isWinnerInSecDiag()) {
				return true;
			}
			return false;
		}
		
		public boolean isWinnerInRow() {
			String symbol = currentPlayer.getSymbol();
			for (int row = 0; row< board.length; row++) {
				int numMatchesInRow = 0 ; // this will reset on the next row
				for( int col = 0; col< board[row].length; col++) {
					if (board [row][col].getText().trim().equalsIgnoreCase(symbol)) {
						numMatchesInRow++;
						if (numMatchesInRow == ConnectX) {
							return true;
						} 
					} 
					else {
						numMatchesInRow=0;
					}
				}
				
			}
			return false;
		} //This is where Winner in row ends
		
		public boolean isWinnerInCol() {
			String symbol = currentPlayer.getSymbol();
			for (int col =0; col<7; col++) {
				int numMatchesInCol = 0 ;
				for (int row = 0; row <6; row++) {
					if (board[row][col].getText().trim().equalsIgnoreCase(symbol)) { //this will scan for symbol in the grid
						numMatchesInCol++;
						if (numMatchesInCol == ConnectX) {
							return true;
					}
				}
					else {
						numMatchesInCol = 0;
					}
				}
			}
			return false;
		} //This is where the Win in Col ends
		
		public boolean isWinnerInMainDiag() {
			String symbol = currentPlayer.getSymbol();
			int matchesInMainDiag = 0;
			int row = board.length-1;
			int col = 0;
			for (int i = board.length-1; i >=0; i--) {
				row = i;
				col = 0;
				matchesInMainDiag = 0;
				while(row<board.length && col <board[row].length) {
					if( board[row][col].getText().trim().equalsIgnoreCase(symbol)) {
						matchesInMainDiag++;		
					}
					else {
						matchesInMainDiag = 0;
					}
					if(matchesInMainDiag == ConnectX) {
						return true;
					}
					col++;
					row--;
					if (row<0) {
						row = 0;
						break;
					}
				}
			}
			
			for (int j = 0; j<board[row].length; j++) {
				col = j;
				row = board.length-1;
				matchesInMainDiag = 0;
				while( row<board.length && col <board[row].length) {
					if(board[row][col].getText().trim().equalsIgnoreCase(symbol)) {
						matchesInMainDiag++;
					}
					else {
						matchesInMainDiag = 0;
					}
					if (matchesInMainDiag == ConnectX) {
						return true;
					}
					row--;
					col++;
					if(row<0) {
						row =0;
						break;
					}
				}
			}
			return false;
		} //this is where the matches in the main Diag ends
		
		
		public boolean isWinnerInSecDiag() {
			String symbol = currentPlayer.getSymbol();
			int matchesInSecDiag = 0;
			int row = 0;
			int col =0;
			for(int showRow = 0; showRow < board.length; showRow++) {
				row = showRow;
				col = 0;
				matchesInSecDiag = 0;
				while(row<board.length && col < board[row].length) {
					if(board[row][col].getText().trim().equalsIgnoreCase(symbol)) {
						matchesInSecDiag++;
					}else {
						matchesInSecDiag=0;
					}
					if(matchesInSecDiag == ConnectX) {
						return true;
					}
					row++;
					col++;
				}
				
			}
			row = 0;
			for(int showCol = 0; showCol < board[row].length; showCol++) {
				row = 0;
				col = showCol;
				matchesInSecDiag = 0;
				while(row< board.length && col < board[row].length) {
					if(board[row][col].getText().trim().equalsIgnoreCase(symbol)) {
						matchesInSecDiag++;
					}else {
						matchesInSecDiag =0;
					}
					if(matchesInSecDiag == ConnectX) {
						return true;
					}
					row++;
					col++;
					if(row>5) {
						row =5;
						break;
					}
				}
			}
			return false;
		}    //This is where winner in Second Diag will win at
		
		
		// determines current player
		@Override
		public void takeTurn() {
			if (currentPlayer.equals(player2)) {
				currentPlayer = player1;
			}
			else {
				currentPlayer = player2;
			}
		}
		@Override
		public void other() {
			if (currentPlayer.equals(player1)) {
				otherPlayer = player2;
			}
			else {
				otherPlayer = player1;
			}
		}
		@Override
		public void displayBoard() {
			
			Font bigFont = new Font (Font.SANS_SERIF, Font.BOLD, 60);
			Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD,45);
			Border coolBorder = BorderFactory.createLineBorder(deepSalmon);
			
		
		
			    for(int col = 0; col<topBttn.length; col++) {
			    	topBttn[col].setOpaque(true);
			    	topBttn[col].setBackground(deepSalmon);
			    	topBttn[col].setFont(buttonFont);
			    	add(topBttn[col]);
			    }
				
				for(int row= 0; row<board.length; row++) {
					for (int col= 0; col<board[row].length; col++) {
						board[row][col]= new JLabel();
						board[row][col].setOpaque(true);
						board[row][col].setBackground(salmon);
						board[row][col].setFont(bigFont);
						board[row][col].setBorder(coolBorder);
						board[row][col].setHorizontalAlignment((int) CENTER_ALIGNMENT);
					    add(board[row][col]);
						
					}
					
				}
				
				
				
			
			
		}
		@Override
		public void clearBoard() {
			for(int row = 0; row<board.length; row++) {
				for (int col = 0 ; col<board[row].length; col++) {
					board[row][col].setText("");
					board[row][col].setEnabled(true);
					
				}
			}
			for (int i =0; i<7; i++) {
				tracker[i] = 5;
				topBttn[i].setEnabled(true);
			}
			
		}
		@Override
		public boolean isEmpty() {
			return false;
		}
		@Override
		public boolean isFull() {
		for (int row = 0; row<board.length; row++) {
			for (int col = 0; col<board[row].length; col++) {
				String cellContent = board[row][col].getText().trim();
				if(cellContent.isEmpty()) {
					return false;
				}
			}
			
		}
			return true;
		}
		
		
	}
	
	

}
