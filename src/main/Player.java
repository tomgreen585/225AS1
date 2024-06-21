package main;

import java.util.ArrayList;  
import java.util.List;

import cards.Card;
import cards.CharacterCard;
import cards.Estate;
import moves.Moves;


//The player class represents a player in the game, each player has a name and a character card
public class Player {

	//enums to represent the character position
	public enum CharacterPosition {
        LUCILLA(2, 12),
        BERT(10, 2),
        MALINA(23, 10),
        PERCY(15, 23);

        private final int row;
        private final int col;

        CharacterPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
	
	private String name;
	private CharacterCard characterPlaying;
	private boolean isPlaying;
	private boolean canSolve = true;
	private Estate estateOccupied = null;
	private List<Card> cards;
	public int row;
	public int col;	

	//Constructor to create a player with a given name, character card 
	public Player(String name, CharacterCard characterPlaying, boolean isPlaying) {
		this.name = name;
		this.isPlaying = isPlaying;
		this.characterPlaying = characterPlaying;
		this.cards = new ArrayList<>();
		setStartingPositions();
	}
	
	//private helper method to set the starting position based on the character card
	private void setStartingPositions() {
		//retrieve the starting position from the characterPosition enum
		CharacterPosition position = CharacterPosition.valueOf(characterPlaying.name().toUpperCase());
        row = position.getRow();
        col = position.getCol();
		
	}
	
	
	//Method to move the player to a specific position outside the door
	public void movePlayerOutsideDoor(Door exitDoor) {	
		int exitRow = exitDoor.exitRow();
		int exitCol = exitDoor.exitCol();
		
		//update the players position
		row = exitRow;
		col = exitCol;
	}
	
	
	//method to move the player in a specific direction
	public void movePlayer(Moves direction) {
		switch(direction) {
		case UP:
			row -= 1;
			break;
		case DOWN:
			row += 1;
			break;
		case RIGHT:
			col += 1;
			break;
		case LEFT:
			col -= 1;
			break;
		default:
			break;
		}
	}

	//Getter and setter methods for various player attributes 
	public boolean canSolve() {
		return canSolve;
	}

	public void setCanSolve(boolean canSolve) {
		this.canSolve = canSolve;
	}

	public Estate getEstateOccupied() {
		return estateOccupied;
	}

	public void setEstateOccupied(Estate estateOccupied) {
		this.estateOccupied = estateOccupied;
	}

	public void addCard(Card card) {
		cards.add(card);
	}
	
	public List<Card> getCards(){
		return cards;
	}

	
	public CharacterCard getCharacter() {
		return characterPlaying;
	}
	
	public void setPosition(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return name + " - " + characterPlaying;
	}

}
