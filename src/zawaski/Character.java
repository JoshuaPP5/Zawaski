package zawaski;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Character extends GameEntity implements Combatant {
    private String characterName;
    private Status status;
    private String ownerUsername;  // Changed from User to String
    private int attackPower;
    private Inventory<Card> inventory;

    private static final String CHARACTER_DATA_FILE = "characters.dat";

    // Updated constructor for new character creation (gold defaults to 0)
    public Character(int id, String characterName, String ownerUsername) {
        super(id, characterName);
        this.characterName = characterName;
        this.ownerUsername = ownerUsername;
        this.status = new Status();  // gold initialized to 0 by default
        this.inventory = new Inventory<>();
        saveCharacterData();
    }

    // Updated constructor for loading from file (with gold)
    public Character(int id, String characterName, String ownerUsername, int hp, int maxHp, int ap, int maxAp, int level, int xp, int gold, List<String> inventoryItems) {
        super(id, characterName);
        this.characterName = characterName;
        this.ownerUsername = ownerUsername;
        this.status = new Status(hp, maxHp, ap, maxAp, level, xp, gold);
        this.inventory = new Inventory<>();
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
        try {
            List<String> lines = Files.readAllLines(Paths.get(CHARACTER_DATA_FILE), StandardCharsets.UTF_8);
            List<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                if (!line.startsWith(this.id + ":")) {
                    updatedLines.add(line);
                }
            }
            Files.write(Paths.get(CHARACTER_DATA_FILE), updatedLines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

 // Save character data including gold
    private void saveCharacterData() {
        try {
            List<String> lines = new ArrayList<>();
            Path path = Paths.get(CHARACTER_DATA_FILE);

            if (Files.exists(path)) {
                lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                lines.removeIf(line -> line.startsWith(this.id + ":"));
            }

            List<String> cardNames = inventory.getItems().stream()
                .map(Card::getCardName)
                .collect(Collectors.toList());

            String inventoryData = String.join(",", cardNames);

            // Added gold field after XP
            String dataLine = id + ":" + characterName + ":" + ownerUsername + ":" +
                    status.getHp() + ":" + status.getMaxHp() + ":" +
                    status.getAp() + ":" + status.getMaxAp() + ":" +
                    status.getLevel() + ":" + status.getExp() + ":" +
                    status.getGold() + ":" + inventoryData;

            lines.add(dataLine);

            Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        List<Character> characters = new ArrayList<>();
        Path path = Paths.get(CHARACTER_DATA_FILE);
        if (!Files.exists(path)) {
            return characters;
        }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length >= 11) {
                    int id = Integer.parseInt(parts[0]);
                    String charName = parts[1];
                    String owner = parts[2];
                    int hp = Integer.parseInt(parts[3]);
                    int maxHp = Integer.parseInt(parts[4]);
                    int ap = Integer.parseInt(parts[5]);
                    int maxAp = Integer.parseInt(parts[6]);
                    int level = Integer.parseInt(parts[7]);
                    int xp = Integer.parseInt(parts[8]);
                    int gold = Integer.parseInt(parts[9]);
                    String inventoryData = parts[10];
                    List<String> inventoryItems = new ArrayList<>();
                    if (!inventoryData.isEmpty()) {
                        inventoryItems = Arrays.asList(inventoryData.split(","));
                    }
                    if (owner.equals(username)) {
                    	characters.add(new Character(id, charName, owner, hp, maxHp, ap, maxAp, level, xp, gold, inventoryItems));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return characters;
    }

    // Static method to generate a new unique ID for characters
    public static int generateNewId() {
        int maxId = 0;
        Path path = Paths.get(CHARACTER_DATA_FILE);
        if (!Files.exists(path)) {
            return 1;
        }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length > 0) {
                    int id = Integer.parseInt(parts[0]);
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxId + 1;
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
