package cards;

public enum Weapon implements Card{
	BROOM("Broom"),
    SCISSORS("Scissors"),
    KNIFE("Knife"),
    SHOVEL("Shovel"),
    IPAD("iPad");
	
	private final String name;

	Weapon(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
}