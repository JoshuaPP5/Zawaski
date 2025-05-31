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

    // Constructor for character creation
    public Character(int id, String characterName, String ownerUsername) {
        super(id, characterName);
        this.characterName = characterName;
        this.ownerUsername = ownerUsername;
        this.status = new Status();
        this.inventory = new Inventory<>();
        saveCharacterData();
    }

 // Constructor for loading from file (with status values)
    public Character(int id, String characterName, String ownerUsername, int hp, int ap, int level, List<String> inventoryItems) {
        super(id, characterName);
        this.characterName = characterName;
        this.ownerUsername = ownerUsername;
        this.status = new Status(hp, ap, level);
        this.inventory = new Inventory<>();
        for (String item : inventoryItems) {
        	Card card = CardFactory.createCard(item);  // Convert String to Card
        	inventory.addItem(card);                    // Add Card object
        }
    }
    
    public void addStartingCard() {
        try {
            inventory.addItem(CardFactory.createCard("Fireball")); // Add Card object to inventory
            inventory.addItem(CardFactory.createCard("Heal")); // Add Card object to inventory
            saveCharacterData();
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to add card: " + e.getMessage());
            // Optionally, handle the error more gracefully, e.g., notify user or log
        }
    }

    public void upgradeStatus() {
        status.setHp(status.getHp() + 10);
        status.setAp(status.getAp() + 5);
        status.setLevel(status.getLevel() + 1);
        saveCharacterData();
    }

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

    private void saveCharacterData() {
        try {
            List<String> lines = new ArrayList<>();
            Path path = Paths.get(CHARACTER_DATA_FILE);

            // Read existing lines if file exists, and remove lines for this character ID
            if (Files.exists(path)) {
                lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                lines.removeIf(line -> line.startsWith(this.id + ":"));
            }

            // Convert List<Card> to List<String> of card names
            List<String> cardNames = inventory.getItems().stream()
                .map(Card::getCardName)  // Use getCardName() method from Card class
                .collect(Collectors.toList());

            // Join card names into a comma-separated string
            String inventoryData = String.join(",", cardNames);

            // Construct the data line to save
            String dataLine = id + ":" + characterName + ":" + ownerUsername + ":" +
                              status.getHp() + ":" + status.getAp() + ":" + status.getLevel() + ":" +
                              inventoryData;

            // Add or update the character data line
            lines.add(dataLine);

            // Write all lines back to the file
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

    // Static method to load all characters for a given username
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
                if (parts.length >= 7) {
                    int id = Integer.parseInt(parts[0]);
                    String charName = parts[1];
                    String owner = parts[2];
                    int hp = Integer.parseInt(parts[3]);
                    int ap = Integer.parseInt(parts[4]);
                    int level = Integer.parseInt(parts[5]);
                    String inventoryData = parts[6];
                    List<String> inventoryItems = new ArrayList<>();
                    if (!inventoryData.isEmpty()) {
                        inventoryItems = Arrays.asList(inventoryData.split(","));
                    }
                    if (owner.equals(username)) {
                        characters.add(new Character(id, charName, owner, hp, ap, level, inventoryItems));
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
            saveCharacterData(); // Persist changes
            return true;
        }
        return false;
    }

    public void takeDamage(int amount) {
        if (amount < 0) {
            return; // Ignore negative damage
        }
        int newHp = status.getHp() - amount;
        if (newHp < 0) {
            newHp = 0;
        }
        status.setHp(newHp);
        saveCharacterData();
    }

    public void heal(int amount) {
        if (amount < 0) {
            return; // Ignore negative healing
        }
        int newHp = status.getHp() + amount;
        int maxHp = status.getMaxHp();
        if (newHp > maxHp) {
            newHp = maxHp;
        }
        status.setHp(newHp);
        saveCharacterData();
    }
    
    public void maxHeal() {
        int maxHp = status.getMaxHp();
        status.setHp(maxHp);
        saveCharacterData();
    }

    public void restoreAP(int amount) {
        if (amount < 0) {
            return; // Ignore negative AP restoration
        }
        int newAp = status.getAp() + amount;
        int maxAp = status.getMaxAp();
        if (newAp > maxAp) {
            newAp = maxAp;
        }
        status.setAp(newAp);
        saveCharacterData();
    }
    
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

}
