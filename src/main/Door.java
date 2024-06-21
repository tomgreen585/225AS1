package main;

import cards.Estate;
import moves.Moves;

public class Door {
	
	Estate estate;
	int row, col;
	Moves direction;
	
	public Door(int row, int col, Moves direcion, Estate estate) {
		this.estate = estate;
		this.row = row;
		this.col = col;
		this.direction = direcion;
	}
	
	//get the exit position of the door
	public int exitCol() {
		if(getDirection() == Moves.EXIT_ESTATE_LEFT) 
			return (col - 1);
		if(getDirection() == Moves.EXIT_ESTATE_RIGHT)
			return (col + 1);
		return col;
	}
	public int exitRow() {
		if(getDirection() == Moves.EXIT_ESTATE_UPWARDS)
			return (row - 1);
		if(getDirection() == Moves.EXIT_ESTATE_DOWNWARDS)
			return (row + 1);
		return row;
	}
	
	public Door getDoor(Moves direction) {
		if(direction == this.direction) 
			return this;
		return null;
	}
	
	
	public Estate getEstate() {
		return estate;
	}
	
	public Moves getDirection() {
		return direction;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	@Override
    public String toString() {
        return "Door{" +
                "estate=" + estate +
                ", row=" + row +
                ", col=" + col +
                ", direction=" + direction +
                '}';
    }
	
	
}