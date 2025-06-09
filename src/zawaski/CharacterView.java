package zawaski;

public class CharacterView {
	public static void displaySelectedCharacterInformation(CharacterModel character) {
		System.out.println("Selected Character: " + 
    		    (character != null && character.getName() != null 
    		        ? character.getName() 
    		        : "None"));
		
    	if (character != null) {
    		System.out.println("Level: " + character.getStatus().getLevel() + " | Exp: " + character.getStatus().getExp() + "/" + character.getXpThresholdForNextLevel() + " | Gold: " + character.getStatus().getGold());
    		System.out.println("HP: " + character.getStatus().getHp() + "/" + character.getStatus().getMaxHp() + " | AP: " + character.getStatus().getAp() + "/" + character.getStatus().getMaxAp());
    	}
	}
	
	public static void displayCharacterGeneralInformation(CharacterModel character) {
		System.out.printf("%-20s %-6d %-6d%n",
                character.getName(),
                character.getLevel(),
                character.getGold());
	}
}
