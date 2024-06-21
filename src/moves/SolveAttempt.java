package moves;

import cards.Estate;
import cards.Weapon;
import cards.CharacterCard;

/**
 * The SolveAttempt class represents an attempt made by a player to solve the murder mystery in the game.
 * It contains the selected estate, character, and weapon as the potential murder circumstances guessed by the player.
 */

public class SolveAttempt {
    private Estate estate;
    private CharacterCard character;
    private Weapon weapon;


    //Constructs a SolveAttempt object with the provided estate, character, and weapon.
    public SolveAttempt(Estate estate, CharacterCard character, Weapon weapon) {
        this.estate = estate;
        this.character = character;
        this.weapon = weapon;
    }

    //Checks if the solve attempt exactly matches the actual murder circumstances.
    public boolean isCorrectSolution(Estate actualEstate, CharacterCard actualCharacter, Weapon actualWeapon) {
    	
        // Check if the solve attempt exactly matches the actual murder circumstances.  	
        return estate.equals(actualEstate) && character.equals(actualCharacter) && weapon.equals(actualWeapon);
    }
    
    
    
    @Override
    public String toString() {
        return "SolveAttempt{" +
                "estate=" + estate +
                ", character=" + character +
                ", weapon=" + weapon +
                '}';
    }
}
