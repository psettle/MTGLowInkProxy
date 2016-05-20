package cards;

public class Creature extends Card {
	protected String power;
	protected String toughness;
	public Creature(String name) throws InvalidCardNameException {
		super(name);
		power = data_dump.get("power").toString();
		toughness = data_dump.get("toughness").toString();
	}
	
	public String toString()
	{
		String toReturn = super.toString();
		toReturn += "\n                                       " + power + "/" + toughness;
		return toReturn;
	}
}
