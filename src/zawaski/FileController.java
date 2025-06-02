package zawaski;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileController {
	/**
     * Load all characters from the characters.dat file.
     * @return List of all characters.
     */
    public static List<Character> loadAllCharacters(String CHARACTER_DATA_FILE) {
        List<Character> characters = new ArrayList<>();
        Path path = Paths.get(CHARACTER_DATA_FILE);
        if (!Files.exists(path)) {
            System.out.println("Character data file not found: " + CHARACTER_DATA_FILE);
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
                    characters.add(new Character(id, charName, owner, hp, maxHp, ap, maxAp, level, xp, gold, inventoryItems));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return characters;
    }
    
    public static void saveAllCharacters(List<Character> allCharacters, String CHARACTER_DATA_FILE) {
        try {
            List<String> lines = new ArrayList<>();
            Path path = Paths.get(CHARACTER_DATA_FILE);
            
            for(Character c: allCharacters) {
            	List<String> cardNames = c.getInventory().getItems().stream()
            			.map(Card::getCardName)
            			.collect(Collectors.toList());
            	
            	String inventoryData = String.join(",", cardNames);
            	
            	// Added gold field after XP
            	String dataLine = c.getId() + ":" + c.getCharacterName() + ":" 
            			+ c.getOwnerUsername() + ":" +
            			c.getStatus().getHp() + ":" + c.getStatus().getMaxHp() + ":" +
            			c.getStatus().getAp() + ":" + c.getStatus().getMaxAp() + ":" +
            			c.getStatus().getLevel() + ":" + c.getStatus().getExp() + ":" +
            			c.getStatus().getGold() + ":" + inventoryData;
            	
            	lines.add(dataLine);
            }

            Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
