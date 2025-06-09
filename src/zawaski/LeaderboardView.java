package zawaski;

import java.util.List;

public class LeaderboardView {
	public static void displayLeaderboard(LeaderboardModel<CharacterModel> leaderboard, int page) {
        List<CharacterModel> entries = leaderboard.getPage(page);
        
        System.out.println("\nPage " + (page + 1));
        System.out.println("----------------------------");
        System.out.printf("%-20s %-6s %-6s%n", "Name", "Lvl", "Gold");
        
        for (CharacterModel c : entries) {
            CharacterView.displayCharacterGeneralInformation(c);
        }
        
        if (entries.isEmpty()) {
            System.out.println("End of leaderboard!");
        }
    }
	
	public static void displayLeaderboardMenu() {
		System.out.println("\n=== Leaderboard ===");
        System.out.println("1. Sort by Level");
        System.out.println("2. Sort by Gold");
        System.out.println("3. Sort by Name");
        System.out.println("4. Next Page");
        System.out.println("5. Prev Page");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
	}
}
