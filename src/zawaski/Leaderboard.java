package zawaski;

import java.util.*;
import java.util.stream.Collectors;

public class Leaderboard {
    /**
     * Generate the leaderboard sorted by the specified attribute ("level" or "gold").
     * Limits each user to their top 3 characters.
     * @param sortBy The attribute to sort by ("level" or "gold").
     * @return List of characters representing the leaderboard.
     */
    public static List<Character> generateLeaderboard(String sortBy, int page, int pageSize) {
        List<Character> allCharacters = FileController.loadAllCharacters("characters.dat");

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