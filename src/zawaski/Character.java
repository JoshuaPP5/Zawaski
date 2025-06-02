package zawaski;

import java.util.*;

public class Character extends GameEntity implements Combatant {
    private String characterName;
    private Status status;
    private String ownerUsername;  // Changed from User to String
    private int attackPower;
    private Inventory<Card> inventory;


    // Updated constructor for new character creation (gold defaults to 0)
    public Character(int id, String characterName, String ownerUsername) {
        super(id, characterName);
        this.characterName = characterName;
        this.ownerUsername = ownerUsername;
        this.inventory = new Inventory<>();
        this.status = new Status();  // gold initialized to 0 by default
        saveCharacterData();
    }

    // Updated constructor for loading from file (with gold)
    public Character(int id, String characterName, String ownerUsername, int hp, int maxHp, int ap, int maxAp, int level, int xp, int gold, List<String> inventoryItems) {
        super(id, characterName);
        this.characterName = characterName;
        this.ownerUsername = ownerUsername;
        this.inventory = new Inventory<>();
        this.status = new Status(hp, maxHp, ap, maxAp, level, xp, gold);
        for (String item : inventoryItems) {
            Card card = CardFactory.createCard(item);
            inventory.addItem(card);
        }
    }

    // Add starting cards to inventory
    public void addStartingCard() {
        try {
            inventory.addItem(CardFactory.createCard("Fireball"));
            inventory.addItem(CardFactory.createCard("Heal"));
            saveCharacterData();
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to add card: " + e.getMessage());
        }
    }

    // Upgrade status (called on level up)
    public void upgradeStatus() {
        status.setMaxHp(status.getMaxHp() + 10);
        status.setHp(status.getHp() + 10);
        status.setMaxAp(status.getMaxAp() + 5);
        status.setAp(status.getAp() + 5);
        status.setLevel(status.getLevel() + 1);
        saveCharacterData();
    }

    // Calculate XP threshold for next level (example: 100 XP per level)
    public int getXpThresholdForNextLevel() {
        // You can customize this formula as needed
        return 100 * status.getLevel();
    }

    // Delete character data from file
    public void deleteCharacter() {
    	List<Character> allCharacters = FileController.loadAllCharacters("characters.dat");

    	for(Character c : allCharacters) {
    		if(c.getId() == this.id) {
    			allCharacters.remove(c);
    			break;
    		}
    	}
    	
    	FileController.saveAllCharacters(allCharacters, "characters.dat");
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

 // Save character data including gold
    private void saveCharacterData() {
    	List<Character> allCharacters = FileController.loadAllCharacters("characters.dat");

    	for(Character c : allCharacters) {
    		if(c.getId() == this.id) {
    			allCharacters.remove(c);
    			allCharacters.add(this);
    			break;
    		}
    	}
    	
    	FileController.saveAllCharacters(allCharacters, "characters.dat");
    }

    // Getters and setters
    public String getCharacterName() { return characterName; }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
        saveCharacterData();
    }

    public String getOwnerUsername() { return ownerUsername; }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
        saveCharacterData();
    }

    public Inventory<Card> getInventory() {
        return inventory;
    }

    public void setInventory(Inventory<Card> inventory) {
        this.inventory = inventory;
    }

    public int getCurrentAP() {
        return status.getAp();
    }

    public boolean deductAP(int amount) {
        int currentAp = status.getAp();
        if (currentAp >= amount) {
            status.setAp(currentAp - amount);
            saveCharacterData();
            return true;
        }
        return false;
    }

    public void takeDamage(int amount) {
        if (amount < 0) return;
        int newHp = status.getHp() - amount;
        if (newHp < 0) newHp = 0;
        status.setHp(newHp);
        saveCharacterData();
    }

    public void heal(int amount) {
        if (amount < 0) return;
        status.setHp(status.getHp() + amount);
        saveCharacterData();
    }

    // Load characters by user with gold parsing
    public static List<Character> loadCharactersByUser(String username) {
        List<Character> allCharacters = FileController.loadAllCharacters("characters.dat");
        List<Character> characters = new ArrayList<>();

        for (Character c: allCharacters) {
            if (c.getOwnerUsername().equals(username)) {
            	characters.add(c);
            }
        }
        
        return characters;
    }

    // Static method to generate a new unique ID for characters
    public static int generateNewId() {
        return FileController.loadAllCharacters("characters.dat").size() + 1;
    }

    // Get current gold amount
    public int getGold() {
        return status.getGold();
    }

    // Deduct gold from character, return true if successful
    public boolean deductGold(int amount) {
        if (amount > 0 && status.deductGold(amount)) {
            saveCharacterData();
            return true;
        }
        return false;
    }
    
	@Override
	public void maxHeal() {
        int maxHp = status.getMaxHp();
        status.setHp(maxHp);
        saveCharacterData();
    }

	@Override
	public void restoreAP(int amount) {
        int newAp = status.getAp() + amount;
        status.setAp(Math.min(newAp, status.getMaxAp()));  // Clamp AP to maxAp
        saveCharacterData();
    }

	@Override
	public void maxAP() {
        int maxAp = status.getMaxAp();
        status.setAp(maxAp);
        saveCharacterData();
    }

	@Override
	public boolean isAlive() {
        return status.getHp() > 0;
    }

	@Override
	public int getAttackPower() {
        return attackPower;
    }

	@Override
	public int getExp() {
		return status.getExp();
	}

	@Override
	public void addGold(int amount) {
		if (amount > 0) {
            status.addGold(amount);
            saveCharacterData();
        }
	}

	public int getLevel() {
		return status.getLevel();
	}
	
	@Override
	public void addExp(int amount) {
		if (amount <= 0) return;  // Ignore non-positive XP

        status.setExp(status.getExp() + amount);

        // Check for level up
        while (status.getExp() >= getXpThresholdForNextLevel()) {
            status.setExp(status.getExp() - getXpThresholdForNextLevel());
            upgradeStatus();
        }

        saveCharacterData();
	}
}
