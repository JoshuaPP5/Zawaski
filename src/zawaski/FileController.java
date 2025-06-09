package zawaski;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class FileController {
	private static final String CHARACTER_DATA_FILE = "characters.dat";
	
	private static final String USER_DATA_FILE = "users.dat";

    public static Map<String, String> loadUsers() throws IOException {
        Path path = Paths.get(USER_DATA_FILE);
        if (!Files.exists(path)) return new HashMap<>();

        return Files.readAllLines(path).stream()
            .map(line -> line.split(":"))
            .filter(parts -> parts.length == 2)
            .collect(Collectors.toMap(
                parts -> parts[0], 
                parts -> parts[1]
            ));
    }

    public static void saveUsers(Map<String, String> users) throws IOException {
        List<String> lines = users.entrySet().stream()
            .map(e -> e.getKey() + ":" + e.getValue())
            .collect(Collectors.toList());
        
        Files.write(
            Paths.get(USER_DATA_FILE),
            lines,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }
    
	public static List<CharacterModel> loadAllCharacters() {
        List<CharacterModel> characters = new ArrayList<>();
        
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
                    characters.add(new CharacterModel(id, charName, owner, hp, maxHp, ap, maxAp, level, xp, gold, inventoryItems));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return characters;
    }
    
    public static void saveAllCharacters(List<CharacterModel> allCharacters) {
        try {
            List<String> lines = new ArrayList<>();
            Path path = Paths.get(CHARACTER_DATA_FILE);
            
            for(CharacterModel c: allCharacters) {
            	List<String> cardNames = c.getInventory().getItems().stream()
            			.map(Card::getCardName)
            			.collect(Collectors.toList());
            	
            	String inventoryData = String.join(",", cardNames);


            	String dataLine = c.getId() + ":" + c.getName() + ":" 
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
