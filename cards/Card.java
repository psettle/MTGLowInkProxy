package cards;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;


/**
 * Abstract magic card class, constructed by name
 * @author Patrick Settle
 */
public class Card {
	protected JsonObject data_dump;
	protected String name;
	protected ArrayList<String> types;
	protected ArrayList<String> subtypes;
	protected String cost;
	protected String text;
	
	public Card(String name) throws InvalidCardNameException
	{
		name = formatCardName(name);
		try {
			URL url = new URL("https://api.deckbrew.com/mtg/cards/" + name);
			InputStream input = url.openStream(); 
			JsonReader reader = Json.createReader(input);
			data_dump = reader.readObject();
			this.name = data_dump.get("name").toString();
			cost = data_dump.get("cost").toString();
			text = data_dump.get("text").toString();
			parseTypes();
			if(data_dump.containsKey("subtypes"))
			{
				parseSubTypes();
			}
			else
			{
				subtypes = new ArrayList<String>();
			}
			if(data_dump.containsKey("supertypes"))
			{
				parseSuperTypes();
			}
		} catch (MalformedURLException e) {
			throw new InvalidCardNameException();
		} catch (IOException e) {		
			throw new InvalidCardNameException();
		} catch (NullPointerException e) {
			throw new InvalidCardNameException();
		}
	}
	
	private static String formatCardName(String name)
	{
		name = name.replaceAll("\\s", "-");
		name = name.replaceAll("[',]", "");
		name = name.toLowerCase();
		return name;
	}
	
	/**
	 * Parses types out of data_dump
	 * @return
	 */
	private void parseTypes() throws NullPointerException
	{
		types = new ArrayList<String>();
		String types_raw = data_dump.get("types").toString();
		int first_index = -1;
		int last_index = -1;
		for(int i = 0; i < types_raw.length(); i++)
		{
			if((types_raw.charAt(i) != '[' && types_raw.charAt(i) != '"' && types_raw.charAt(i) != ' ' && types_raw.charAt(i) != '\n' && types_raw.charAt(i) != ',') && first_index == -1)
			{
				first_index = i;
			}
			if((types_raw.charAt(i) == '[' || types_raw.charAt(i) == '"') && last_index == -1 && first_index != -1)
			{
				last_index = i;
			}
			if(last_index != -1 && first_index != -1)
			{
				types.add(types_raw.substring(first_index, last_index));
				first_index = -1;
				last_index = -1;
			}
		}
		
		for(int i = 0; i < types.size(); i++)
		{		
			String capitalized = types.get(i);
			capitalized = capitalized.trim();
			if(capitalized.length() == 0) continue;
			capitalized = Character.toUpperCase(capitalized.charAt(0)) + capitalized.substring(1);
			types.set(i, capitalized);
		}
	}
	
	private void parseSubTypes() throws NullPointerException
	{
		subtypes = new ArrayList<String>();
		String types_raw = data_dump.get("subtypes").toString();
		int first_index = -1;
		int last_index = -1;
		for(int i = 0; i < types_raw.length(); i++)
		{
			if((types_raw.charAt(i) != '[' && types_raw.charAt(i) != '"' && types_raw.charAt(i) != ' ' && types_raw.charAt(i) != '\n' && types_raw.charAt(i) != ',') && first_index == -1)
			{
				first_index = i;
			}
			if((types_raw.charAt(i) == '[' || types_raw.charAt(i) == '"') && last_index == -1 && first_index != -1)
			{
				last_index = i;
			}
			if(last_index != -1 && first_index != -1)
			{
				subtypes.add(types_raw.substring(first_index, last_index));
				first_index = -1;
				last_index = -1;
			}
		}
		
		for(int i = 0; i < subtypes.size(); i++)
		{
			String capitalized = subtypes.get(i);
			capitalized = capitalized.trim();
			if(capitalized.length() == 0) continue;
			capitalized = Character.toUpperCase(capitalized.charAt(0)) + capitalized.substring(1);
			subtypes.set(i, capitalized);
		}
	}
	
	private void parseSuperTypes() throws NullPointerException
	{
		String types_raw = data_dump.get("supertypes").toString();
		int first_index = -1;
		int last_index = -1;
		for(int i = 0; i < types_raw.length(); i++)
		{
			if((types_raw.charAt(i) != '[' && types_raw.charAt(i) != '"' && types_raw.charAt(i) != ' ' && types_raw.charAt(i) != '\n' && types_raw.charAt(i) != ',') && first_index == -1)
			{
				first_index = i;
			}
			if((types_raw.charAt(i) == '[' || types_raw.charAt(i) == '"') && last_index == -1 && first_index != -1)
			{
				last_index = i;
			}
			if(last_index != -1 && first_index != -1)
			{
				types.add(0, types_raw.substring(first_index, last_index));
				first_index = -1;
				last_index = -1;
			}
		}
		
		for(int i = 0; i < types.size(); i++)
		{
			
			String capitalized = types.get(i);
			capitalized = capitalized.trim();
			if(capitalized.length() == 0) continue;
			capitalized = Character.toUpperCase(capitalized.charAt(0)) + capitalized.substring(1);
			types.set(i, capitalized);
		}
	}
	
	public String toString()
	{
		String toReturn = "";
		toReturn += name + " " + cost + "\n";
	    for(String type : types)
	    {
	    	toReturn += type + " ";
	    }
	    if(subtypes.size() != 0)
	    {
	    	toReturn += "- ";
	    }
	    for(String subtype : subtypes)
	    {
	    	toReturn += subtype + " ";
	    }
	    toReturn += "\n" + text;
	    return toReturn;
	}
	
	public ArrayList<String> getTypes()
	{
		return types;
	}
}
