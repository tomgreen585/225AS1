package moves;

/**
 * Represents possible moves and actions in the game.
 */
public enum Moves {
	
	//group of enums to move player around
    UP,
    DOWN,
    LEFT,
    RIGHT,

    //to enter an estate
    ENTER_ESTATE,

    // Group of EXIT_ESTATE enums
    EXIT_ESTATE_UPWARDS,
    EXIT_ESTATE_DOWNWARDS,
    EXIT_ESTATE_LEFT,
    EXIT_ESTATE_RIGHT,

    
    // enums for solving 
    SOLVE_ATTEMPT,
    GUESS,

    
    //use to skip a turn
    SKIP_TURN;
}
