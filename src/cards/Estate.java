package cards;

import java.util.ArrayList;
import java.util.List;

import main.Door;
import main.Player;
import moves.Moves;

public enum Estate implements Card {
	VISITATION_VILLA("Visitation Villa"), 
	HAUNTED_HOUSE("Haunted House"), 
	MANIC_MANOR("Manic Manor"),
	PERIL_PALACE("Peril Palace"), 
	CALAMITY_CASTLE("Calamity Castle");

	private String name;

	private List<Weapon> weaponsInEstate;
	private List<Door> doors;
	
	Estate(String name) {
		this.name = name;

		weaponsInEstate = new ArrayList<>();
	
		doors = new ArrayList<>();
		
		addDoors();
	}

	//add doors that connect to this estate, to get position of door. 
	//Depending on the move (e.g Moves.UP), will determine where the player gets moved out when exiting an estate
	private void addDoors() {
		switch (name.toLowerCase()) {
		case "visitation villa":
			doors.add(new Door(11, 13, Moves.EXIT_ESTATE_UPWARDS, this));
			doors.add(new Door(14, 12, Moves.EXIT_ESTATE_DOWNWARDS, this));
			doors.add(new Door(13, 10, Moves.EXIT_ESTATE_LEFT, this));
			doors.add(new Door(12, 15, Moves.EXIT_ESTATE_RIGHT, this));	
			break;
			
		case "haunted house":
			doors.add(new Door(4, 7, Moves.EXIT_ESTATE_RIGHT, this));
			doors.add(new Door(7, 6, Moves.EXIT_ESTATE_DOWNWARDS, this));
			break;

		case "manic manor":
			doors.add(new Door(6, 18, Moves.EXIT_ESTATE_LEFT, this));
			doors.add(new Door(7, 21, Moves.EXIT_ESTATE_DOWNWARDS, this));	
			break;
		
		case "peril palace":
			doors.add(new Door(18, 19, Moves.EXIT_ESTATE_UPWARDS, this));
			doors.add(new Door(21, 18, Moves.EXIT_ESTATE_LEFT, this));
			break;
			
		case "calamity castle":
			doors.add(new Door(18, 4, Moves.EXIT_ESTATE_UPWARDS, this));
			doors.add(new Door(19, 7, Moves.EXIT_ESTATE_RIGHT, this));
			break;
			
		default:
			throw new IllegalArgumentException("Unknown estate: " + name);
		}

	}
	
	public List<Door> getDoors() {
		return doors;
	}

	public List<Weapon> getWeaponsInEstate() {
		return weaponsInEstate;
	}

	public void addWeaponToEstate(Weapon weapon) {
		weaponsInEstate.add(weapon);
	}
	
	public void removeWeaponFromEstate(Weapon weapon) {
		weaponsInEstate.remove(weapon);
	}
	
	@Override
	public String getName() {
		return name;
	}

}
