package cards;

public enum CharacterCard implements Card {
	LUCILLA("Lucilla"),
    BERT("Bert"),
    MALINA("Malina"),
    PERCY("Percy");
	
	private final String name;
	
	CharacterCard(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
		
	}
	
	
}
