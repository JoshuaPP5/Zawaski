package zawaski;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Leaderboard {

    private static final String CHARACTER_DATA_FILE = "characters.dat";

    /**
     * Load all characters from the characters.dat file.
     * @return List of all characters.
     */
    public static List<Character> loadAllCharacters() {
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

    /**
     * Generate the leaderboard sorted by the specified attribute ("level" or "gold").
     * Limits each user to their top 3 characters.
     * @param sortBy The attribute to sort by ("level" or "gold").
     * @return List of characters representing the leaderboard.
     */
    public static List<Character> generateLeaderboard(String sortBy, int page, int pageSize) {
        List<Character> allCharacters = loadAllCharacters();

        // Comparator based on sortBy parameter with descending order
        Comparator<Character> comparator;
        if ("gold".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingInt(Character::getGold)
                    .thenComparingInt(Character::getLevel)
                    .reversed();
        } else {
            comparator = Comparator.comparingInt(Character::getLevel)
                    .thenComparingInt(Character::getGold)
                    .reversed();
        }

        // Sort all characters globally
        List<Character> sortedCharacters = allCharacters.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        // Calculate pagination boundaries
        int start = page * pageSize;
        int end = Math.min(start + pageSize, sortedCharacters.size());

        if (start >= sortedCharacters.size()) {
            return Collections.emptyList(); // No more pages
        }

        // Return paginated sublist
        return sortedCharacters.subList(start, end);
    }

    public static void printLeaderboard(List<Character> leaderboard, int currentPage, int pageSize) {
        System.out.println("Leaderboard (Page " + (currentPage + 1) + "):");
        System.out.println("===================================");
        int rank = currentPage * pageSize + 1;
        for (Character c : leaderboard) {
            System.out.printf("%d. %s (Owner: %s) - Level: %d, Gold: %d%n",
                    rank++, c.getCharacterName(), c.getName(), c.getLevel(), c.getGold());
        }
        System.out.println("Use nextPage() and previousPage() methods to navigate.");
    }
}