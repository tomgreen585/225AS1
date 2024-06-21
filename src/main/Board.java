package main;

import java.util.ArrayList;   
import java.util.List;

import cards.Estate;
import cards.Weapon;
import moves.Moves;
 
public class Board {

	public static final int BOARD_SIZE = 26;
	public static final char EMPTY_SQUARE = '.';
	public static final char WALL = '#';
	
	private char[][] board; 
	private char[][] playerProgressLog; 
	
	private Game game;
	private List<Estate> estates;
	private List<Door> allDoors = new ArrayList<>();
	private ArrayList<Moves> moveableSpots = new ArrayList<>();
	
	public Board(Game game, List<Estate> estates) {
		this.estates = estates;
		this.game = game;
		
		playerProgressLog = new char[BOARD_SIZE][BOARD_SIZE];//TODO for player turn info
		board = new char[BOARD_SIZE][BOARD_SIZE];
		
		initialize();
		
	}

	
	private void initialize() {	
		drawStaticBoard();
		drawPlayerProgressLog(); //board on the right
		drawAllEstatesAndDoors();
		
		// draw all players in their starting positions
		for (Player p : game.getPlayers()) {
			drawPlayer(p);
		}
		
		render(); 
	}
	
	
	//DRAWING METHODS
	public void updated() {
		for (Player p : game.getPlayers())
			drawPlayer(p);
		render();
	}
	
	public void replaceTile(Player player) {
		board[player.row][player.col] = EMPTY_SQUARE;
	}
	
	//renders the board, prints out the board plus another board / log of info beside
	public void render() {
	    for (int row = 0; row < BOARD_SIZE; row++) {
	        // Printing the elements of the first board
	        for (int col = 0; col < BOARD_SIZE; col++) {
	            System.out.print(board[row][col] + " ");
	        }
	        // Add some spacing between the two boards
	        System.out.print("  ");
	        // Printing the elements of the second board
	        for (int col = 0; col < BOARD_SIZE; col++) {
	            System.out.print(playerProgressLog[row][col] + " ");
	        }
	        System.out.println("");
	    }
	}
	
	private void drawPlayerProgressLog() {
		// Fill board with empty squares
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
				playerProgressLog[row][col] = EMPTY_SQUARE;

		// Place game boarder
		for (int i = 0; i < BOARD_SIZE; i++) {
			playerProgressLog[0][i] = WALL; 
			playerProgressLog[i][0] = WALL;
			playerProgressLog[BOARD_SIZE - 1][i] = WALL; 
			playerProgressLog[i][BOARD_SIZE - 1] = WALL; 
		}

		drawGraySquares();
	}
	
	//draws the parts of the board which we dont care about e.g boarder and gray squares
	private void drawStaticBoard() {
		// Fill board with empty squares
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
				board[row][col] = EMPTY_SQUARE;

		// Place game boarder
		for (int i = 0; i < BOARD_SIZE; i++) {
			board[0][i] = WALL; 
			board[i][0] = WALL;
			board[BOARD_SIZE - 1][i] = WALL; 
			board[i][BOARD_SIZE - 1] = WALL; 
		}
		drawGraySquares();
	}

	// draws all Players on board
	private void drawPlayer(Player p) {	
		if (p.getEstateOccupied() != null)
			return; // if player is inside an estate, don't print
		char initial = p.getCharacter().getName().charAt(0);
		board[p.row][p.col] = initial;

	}
	
	//draw random gray squares
	public void drawGraySquares() {
		// Place those 4 gray squares
		drawWalls(12, 13, 6, 7, WALL); // Left
		drawWalls(12, 13, 18, 19, WALL); // Right
		drawWalls(6, 7, 12, 13, WALL); // Top
		drawWalls(18, 19, 12, 13, WALL); // Bottom
	}
	
	public void drawWalls(int startRow, int endRow, int startCol, int endCol, char c) {
		for (int row = startRow; row <= endRow; row++) {
			for (int col = startCol; col <= endCol; col++) {
				board[row][col] = c;
			}
		}
	}

	//Draw the estates and all the doors
	private void drawAllEstatesAndDoors() {
		//Draws the estates
		drawEstate(3, 7, 3, 7, Estate.HAUNTED_HOUSE);	
		drawEstate(3, 7, 18, 22, Estate.MANIC_MANOR);
		drawEstate(11, 14, 10, 15, Estate.VISITATION_VILLA);
		drawEstate(18, 22, 3, 7, Estate.CALAMITY_CASTLE);
		drawEstate(18, 22, 18, 22, Estate.PERIL_PALACE);
		
		//Draw the doors
		for(Estate e : estates)
			for(Door d : e.getDoors()) {
				board[d.getRow()][d.getCol()] = 'D';
				allDoors.add(d);
			}
	}
	//helper method to draw the estates
	public void drawEstate(int startRow, int endRow, int startCol, int endCol, Estate estate) {
		drawWalls(startRow, endRow, startCol, endCol, estate.getName().charAt(0));
		// Place walls for the top and bottom borders
		for (int col = startCol; col <= endCol; col++) {
			board[startRow][col] = WALL; // Top border
			board[endRow][col] = WALL; // Bottom border
		}

		// Place walls for the left and right borders, excluding the corners
		for (int row = startRow + 1; row < endRow; row++) {
			board[row][startCol] = WALL; // Left border
			board[row][endCol] = WALL; // Right border
		}
	}
	
	
	//OTHER HELPER METHODS
	
	//Checks all directions to see if there is an empty square
	public ArrayList<Moves> checkDirections(Player p) {
		moveableSpots.clear();
		int row = p.row;
		int col = p.col;
		
		if (board[row - 1][col] == EMPTY_SQUARE)
			moveableSpots.add(Moves.UP);
		if (board[row + 1][col] == EMPTY_SQUARE)
			moveableSpots.add(Moves.DOWN);
		if (board[row][col + 1] == EMPTY_SQUARE)
			moveableSpots.add(Moves.RIGHT);
		if (board[row][col - 1] == EMPTY_SQUARE)
			moveableSpots.add(Moves.LEFT);
		
		return moveableSpots;
	}
	
	
	public Estate checkForDoorsEstate(Player p) {
	    int[] dRowCol = lookForChar(p, p.row, p.col, 'D');
	    return dRowCol[0] != 0 || dRowCol[1] != 0 ? checkEstate(p.row + dRowCol[0], p.col + dRowCol[1]) : null;
	}
	
	private Estate checkEstate(int row, int col) {
		 return allDoors.stream().filter(d -> col == d.getCol() && row == d.getRow()).findFirst().map(d -> d.estate).orElse(null);
	}
	
	public int[] lookForChar(Player p, int row, int col, char ch) {
		int[] res = {0, 0};
		if(checkChar(p.row - 1, p.col) == ch) res[0] = -1; //UP
		else if(checkChar(p.row + 1, p.col) == ch) res[0] = 1; //DOWN
		if(checkChar(p.row, p.col + 1) == ch) res[1] = 1; //RIGHT
		else if(checkChar(p.row, p.col - 1) == ch) res[1] = -1; //LEFT
		return res;
	}
	
	//returns a char on the board
	private char checkChar(int row, int col) {
		return board[row][col];
	}
}
