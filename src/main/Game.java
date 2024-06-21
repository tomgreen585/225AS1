package main;

import java.util.ArrayList; 
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import cards.Card;
import cards.CharacterCard;
import cards.Estate;
import cards.Weapon;
import moves.Moves;
import moves.SolveAttempt;

public class Game {
	private int numPlayers;
	private Board board;
	private Player[] players;
	
	private boolean gameWon = false;
	private Player WINNER;
	
	private static int playerIndex = 0;
	
	private List<Card> inPlayCards;
	
	private Weapon murdererWeapon;
	private Estate murdererEstate;
	private CharacterCard murdererCharacterCard;
	
	private List<CharacterCard> characterCards;
	private List<Estate> estateCards;
	private List<Weapon> weaponCards;
	
	private int currentPlayerMoves;
	
	Player currentPlayer;
	
	public Game() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Welcome to Hobby Detectives!\n");
		numPlayers = getPlayerChoice(sc, 2, 4, "Enter the number of players (2 to 4): ");
       
        //CREATE STARTING OBJECTS
        initClasses(sc);
        //START GAME LOOP
        startGameLoop(sc);
		
        //Display winner and congratulate
        System.out.println("\n\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
        System.out.println(WINNER.getName().toUpperCase() + " IS THE WINNER!!!\n");
        System.out.println("CONGRATULATIONS!!!\n");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        
        //CLOSE SCANNER
        sc.close();
	}

	private void initClasses(Scanner sc) {
		createPlayers(sc);
		makeCardDecks();
		board = new Board(this, estateCards);
		addWeaponsToEStates();
		makeMurdererCards();
		divideOutIntoHandCards();
		
		//TODO TESTING
		displayHands();
	}
	
	private void createPlayers(Scanner sc) {
		//LIST OF AVALIABLE PLAYERS
		List<CharacterCard> availableCharacters = new ArrayList<>(EnumSet.allOf(CharacterCard.class));
        players = new Player[4];
        
        //LOOP FOR ALL # OF PLAYING PLAYERS
        for(int i = 0; i < numPlayers; i++) {
        	System.out.println("\n--Select Character--");
        	displayCards(availableCharacters, "Available characters: ");
        	int choice = getPlayerChoice(sc, 1, availableCharacters.size(), "Player " + (i + 1) + ": ");
        	choice --;
        	CharacterCard characterChoice = availableCharacters.remove(choice);
        	
        	players[i] = new Player("Player " + (i+1), characterChoice, true);
        }
        
        //Make NPC players if necessary
        if(numPlayers == 2) {
        	CharacterCard characterChoice = availableCharacters.get(1);
        	players[2] = new Player("NPC " + 2, characterChoice, false);
    	}
        if(numPlayers < 4) {
        	CharacterCard characterChoice = availableCharacters.get(0);
        	players[3] = new Player("NPC " + 1, characterChoice, false);
        }
        
	}
	
	private void makeCardDecks() {
		characterCards = new ArrayList<>();
		characterCards.addAll(java.util.Arrays.asList(CharacterCard.values()));
		
		estateCards = new ArrayList<>();
		estateCards.addAll(java.util.Arrays.asList(Estate.values()));
		
		weaponCards = new ArrayList<>();
		weaponCards.addAll(java.util.Arrays.asList(Weapon.values()));	
	}
	
	private void addWeaponsToEStates() {
		for(int i = 0; i < weaponCards.size(); i++) 
			estateCards.get(i).addWeaponToEstate(weaponCards.get(i));
	}

	//Add random card from each deck to a murderer card
		private void makeMurdererCards() {
			Random random = new Random();
		
			murdererCharacterCard = (characterCards.get(random.nextInt(characterCards.size())));
			murdererEstate = (estateCards.get(random.nextInt(estateCards.size())));
			murdererWeapon = (weaponCards.get(random.nextInt(weaponCards.size())));
		}
		
		//divide cards into each players hand
		private void divideOutIntoHandCards() {
			inPlayCards = new ArrayList<>();
			
			inPlayCards.addAll(characterCards);
			inPlayCards.addAll(estateCards);
			inPlayCards.addAll(weaponCards);
			
			Collections.shuffle(inPlayCards);
			
			int currentPlayer = 0;
	        for (Card card : inPlayCards) {	
	        	if(card != murdererCharacterCard && card != murdererWeapon && card != murdererEstate) {
		            players[(currentPlayer)].addCard(card);
		            currentPlayer = (currentPlayer + 1) % numPlayers;
	        	}
	        }
		}
	
	private void displayHands() {
		for(Player p : players) {
			System.out.println(p.getName());
			for(Card c : p.getCards()) {
				System.out.println(c.getName());
			}
			System.out.println("*************");
		}
		System.out.println("murderer cards");
		System.out.println(murdererCharacterCard + " " + murdererEstate + " " + murdererWeapon);
	}

	private void startGameLoop(Scanner sc) {
		while(!gameWon) {
			currentPlayer = players[playerIndex];
			
			if(currentPlayer.isPlaying()) {
				System.out.println("=================================");
				playTurn(currentPlayer, sc);	
			}
			
			playerIndex = (playerIndex + 1) % numPlayers; 
		}
	}

	
	private void playTurn(Player player, Scanner sc) {
		System.out.println(player.getName() + "'s turn as, " + player.getCharacter().name() + " (" + player.getCharacter().getName().charAt(0) +")\n");
		
		currentPlayerMoves = rollDice();
		
		System.out.println("You rolled: " + currentPlayerMoves);
		
		while(currentPlayerMoves > 0 && !gameWon) {
			if(gameWon) return;
			System.out.println("Moves Remaining: " + currentPlayerMoves + "\n");
			ArrayList<Moves> moves = new ArrayList<>();
			
			//check for where the player can move on the board, if they are not inside an estate
			if(player.getEstateOccupied() == null) 
				moves.addAll(board.checkDirections(player));
			
			moves.addAll(checkOtherMoves(player));
			displayChoices(moves, 0);
			
			int choice = getPlayerChoice(sc, 1, moves.size(), player.getName() + ": ");
			choice--;
			
			Moves moveChoice = moves.get(choice);
			if(moveChoice == Moves.UP || moveChoice == Moves.DOWN || moveChoice == Moves.LEFT || moveChoice ==  Moves.RIGHT) {
				currentPlayerMoves --;
			}
			
			else if(moveChoice == Moves.SKIP_TURN)
				currentPlayerMoves = 0;
			
			performPlayerMove(moveChoice, player, sc);
			moves.clear();
		}	
	}
	
	//Perform the moves made by a player
	private void performPlayerMove(Moves moveChoice, Player player, Scanner sc) {
		switch(moveChoice) {
		case UP:
		case DOWN:
		case LEFT:
		case RIGHT:
			board.replaceTile(player);
			player.movePlayer(moveChoice);
			board.updated();
			break;
		case ENTER_ESTATE:
			Estate estateOcc = board.checkForDoorsEstate(player);
			player.setEstateOccupied(estateOcc);
			board.replaceTile(player);
			board.updated();
			System.out.println("Player now occupies: " + estateOcc);
			break;
		case SOLVE_ATTEMPT:
			playerSolveAttempt(player, sc);
			player.setCanSolve(false);
			break;
		case GUESS:
			playerGuess(player, sc);
			board.updated();
			break;
		case EXIT_ESTATE_UPWARDS:
		case EXIT_ESTATE_DOWNWARDS:
		case EXIT_ESTATE_LEFT:
		case EXIT_ESTATE_RIGHT:
			Door door = getDoorOfDirection(player, moveChoice);
			player.setEstateOccupied(null);
			player.movePlayerOutsideDoor(door);
			board.updated();
			break;
		case SKIP_TURN:
			break;
		default:
			break;
		}
		
	}
	
	//perform guess
	private void playerGuess(Player player, Scanner sc) {
		Estate estateGuess;
		CharacterCard characterCardGuess;
		Weapon weaponGuess;
		
		displayCards(characterCards, "Choose a character for guess: ");
		int characterChoice = getPlayerChoice(sc,1,characterCards.size(), player.getName() + ": ");
		characterCardGuess = characterCards.get(characterChoice - 1);
		
		displayCards(weaponCards, "Choose a weapon for guess: ");
		int weaponChoice = getPlayerChoice(sc,1,weaponCards.size(), player.getName() + ": ");
		weaponGuess = weaponCards.get(weaponChoice - 1);
		
		estateGuess = player.getEstateOccupied();
		
		startRefuting(sc, player, estateGuess, characterCardGuess, weaponGuess);
		
	}
	
	
	//refuting
	private void startRefuting(Scanner sc, Player playingPlayer, Estate e, CharacterCard c, Weapon w) {
		Map<String, Card> refutationCards = new HashMap<>();
		
		
		//TELEPORT PLAYER AND THE WEAPON TO THE ESTATE
		Player teleport = null;
		for(Player p : players) {
			if(p.getCharacter() == c)
				teleport = p;
		}
		teleportPlayerAndWeapon(e, teleport, w);
		for(Player p : players) {
			if(p.getEstateOccupied() == e) System.out.println(p.getName() + "!!!!!!!");
			
		}
		
		for(Player p : players) {
			if(!p.equals(playingPlayer)) {
				List<Card> playerRefutations = new ArrayList<>();
				//weaponCard
				if(p.getCards().contains(w)) {
					playerRefutations.add(w);
				}
				//characterCard
				if(p.getCards().contains(c)) {
					playerRefutations.add(c);
				}
				//estateCard
				if(p.getCards().contains(e)) {
					playerRefutations.add(e);
				}
				
				
				if(playerRefutations.isEmpty() || playerRefutations == null) {
					refutationCards.put(p.getName() + " (" + p.getCharacter() + ") CANNOT REFUTE ", null);
				} 
				else if(playerRefutations.size() == 1) {
					refutationCards.put(p.getName() + " (" + p.getCharacter() + ") HAS THE: ", playerRefutations.get(0));
				}
				else {
					System.out.println("=============================");
					System.out.println(p.getName() + "'s refutation: ");
					displayCards(playerRefutations, "Choose a card to refute: ");
					int choice = (getPlayerChoice(sc, 0, playerRefutations.size(), null) - 1);
					refutationCards.put(p.getName() + " (" + p.getCharacter() + ") HAS THE: ", playerRefutations.get(choice));
				}
				
			}
		}
		
		//Print out refutation for player
		System.out.println("******REFUTATION CARDS FOR " + playingPlayer.getName() + " ******");
		for(Entry<String, Card>	entry : refutationCards.entrySet()) {
			String s = entry.getKey();
			Card card = entry .getValue();
			
			if(card == null)
				System.out.println(s);
			else
				System.out.println(s + "" + card);
			
		}
	}
	

	//perform solve
	private void playerSolveAttempt(Player player, Scanner sc) {
		Estate estateGuess;
		CharacterCard characterCardGuess;
		Weapon weaponGuess;
		
		displayCards(characterCards, "Choose a character: ");
		int characterChoice = getPlayerChoice(sc, 0, characterCards.size(), player.getName() + ": ");
		characterCardGuess = characterCards.get(characterChoice - 1);
		
		displayCards(weaponCards, "Choose a weapon: ");
		int weaponChoice = getPlayerChoice(sc, 1, weaponCards.size(), player.getName() + ": ");
		weaponGuess = weaponCards.get(weaponChoice - 1);
		
		displayCards(estateCards, "Choose an estate: ");
		int estateChoice = getPlayerChoice(sc, 1, estateCards.size(), player.getName() + ": ");
		estateGuess = estateCards.get(estateChoice - 1);
		
		SolveAttempt solve = new SolveAttempt(estateGuess, characterCardGuess, weaponGuess);
		
		if(solve.isCorrectSolution(murdererEstate, murdererCharacterCard, murdererWeapon)) {
			WINNER = player;
			System.out.println("winner!");
			gameWon = true;
		} else System.out.println("NOT CORRECT, YOU ALSO YOU CANNOT SOLVE AGAIN!");
		
	}

	//check if the player can perform certain moves
	private ArrayList<Moves> checkOtherMoves(Player player) {
		ArrayList<Moves> moves = new ArrayList<>();
		
		if(player.canSolve())
			moves.add(Moves.SOLVE_ATTEMPT);
		//if player is inside an estate, player can guess and exit the estate, depending on the respected doors
		if(player.getEstateOccupied() != null) {
			moves.add(Moves.GUESS);
			//check what doors the player can exit from and add them to possible moves
			for(Door d : player.getEstateOccupied().getDoors()) {
				moves.add(d.getDirection());
			}
		}
		//if player is beside a door
		int[] dRowCol = board.lookForChar(player, player.row, player.col, 'D');
		
		if(((dRowCol[0] != 0) || dRowCol[1] != 0) && player.getEstateOccupied() == null)
			moves.add(Moves.ENTER_ESTATE);
		
		moves.add(Moves.SKIP_TURN);
		
		return moves;
		
	}
	
	public Door getDoorOfDirection(Player player, Moves direction) {
		for(Door d : player.getEstateOccupied().getDoors()) {
			if(d.getDirection() == direction)
				return d;
		}
		return null;
	}

	//FOR MAKING AN INTEGER DECISION CHOICES 
	private int getPlayerChoice(Scanner scanner, int min, int max, String question) {
        int choice;
        do {
            System.out.print(question);
            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < min || choice > max) {
            	if(min == max)
                	System.out.println("Invalid - You must choose option " + min);
            	else if((max - min) == 1)
            		System.out.println("Invalid - You must choose, either option" + min + " or " + max);
            	else
            		System.out.println("Invalid - You must choose between option " + min + " and " + max);
            }
        } while (choice < min || choice > max);

        return choice;
    }
	
	//DISPLAY CHOICES FOR MOVES
	private void displayChoices(ArrayList<Moves> choices, int choiceStartNum) {
		if(choices == null || choices.size() == 0) 
			return;
		
		for(int i = 0; i < choices.size(); i++)
			System.out.println(i+choiceStartNum+1 + " - " + choices.get(i)+"");
	}
	
	
	//DISPLAY THE AVALIABLE CARDS
	private void displayCards(List<? extends Card> cards, String message) {
        System.out.println(message);
        for(int j = 0; j < cards.size(); j++) 
    		System.out.println(j+1 + " - " + cards.get(j));
        System.out.println("");
    		
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public int rollDice() {
		int die1 = (int)(Math.random() * 6 + 1);
		int die2 = (int)(Math.random() * 6 + 1);
		
		return die1 + die2;
	}
	
	//teleport players and weapon to this estate 
		public void teleportPlayerAndWeapon(Estate e, Player p, Weapon w) {
			p.setEstateOccupied(e);
			board.replaceTile(p);
			for(Estate estate : estateCards) {
				if(estate.getWeaponsInEstate().contains(w))
					estate.removeWeaponFromEstate(w);
			}
			e.addWeaponToEstate(w);
			board.updated();
		}
}
