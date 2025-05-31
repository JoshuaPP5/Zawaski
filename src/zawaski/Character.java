package zawaski;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Character extends GameEntity implements Combatant {
    private String characterName;
    private Status status;
    private String ownerUsername;  // Changed from User to String
    private int attackPower;
    private List<String> startingCardNames;

    private static final String CHARACTER_DATA_FILE = "characters.dat";

    // Constructor for character creation
    public Character(int id, String characterName, String ownerUsername) {
        super(id, characterName);
        this.characterName = characterName;
        this.ownerUsername = ownerUsername;
        this.status = new Status(); // Composition: Character owns Status
        this.startingCardNames = new ArrayList<>();
        saveCharacterData();
    }

    public List<String> getStartingCardNames() {
        return startingCardNames;
    }

    public void addStartingCard(String cardName) {
        startingCardNames.add(cardName);
    }
    
    // Constructor for loading from file (with status values)
    public Character(int id, String characterName, String ownerUsername, int hp, int ap, int level) {
        super(id, characterName);
        this.characterName = characterName;
        this.ownerUsername = ownerUsername;
        this.status = new Status(hp, ap, level);
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
            if (Files.exists(path)) {
                lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                // Remove old entry for this character
                lines.removeIf(line -> line.startsWith(this.id + ":"));
            }
            String dataLine = id + ":" + characterName + ":" + ownerUsername + ":" + status.getHp() + ":" + status.getAp() + ":" + status.getLevel();
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
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    String charName = parts[1];
                    String owner = parts[2];
                    int hp = Integer.parseInt(parts[3]);
                    int ap = Integer.parseInt(parts[4]);
                    int level = Integer.parseInt(parts[5]);
                    if (owner.equals(username)) {
                        characters.add(new Character(id, charName, owner, hp, ap, level));
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
        int maxHp = 100; // Should match Status.MAX_HP or be configurable
        if (newHp > maxHp) {
            newHp = maxHp;
        }
        status.setHp(newHp);
        saveCharacterData();
    }

    public void restoreAP(int amount) {
        if (amount < 0) {
            return; // Ignore negative AP restoration
        }
        int newAp = status.getAp() + amount;
        int maxAp = 50; // Should match Status.MAX_AP or be configurable
        if (newAp > maxAp) {
            newAp = maxAp;
        }
        status.setAp(newAp);
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
