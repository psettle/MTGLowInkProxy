package cards;

public class Planeswalker extends Card {

	protected String loyalty;
	public Planeswalker(String name) throws InvalidCardNameException {
		super(name);
		loyalty = data_dump.get("loyalty").toString();
		text = text.replaceAll("\\?", "-");
	}

	public String toString()
	{
		String toReturn = super.toString();
		toReturn += "\n                                       " + loyalty;
		return toReturn;
	}
}
