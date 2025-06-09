package zawaski;

import java.util.List;
import java.util.Scanner;

public class LeaderboardController {
	private LeaderboardModel<CharacterModel> leaderboard;
	private final Scanner scanner;
	
	public LeaderboardController(Scanner scanner) {
		this.initLeaderboard();
		this.scanner = scanner;
	}
	
	
	private void initLeaderboard() {
        List<CharacterModel> allCharacters = FileController.loadAllCharacters();
        leaderboard = new LeaderboardModel<>(allCharacters, 5);
        
        // Configure sorting methods
        leaderboard.addSortMethod("level", 
            LeaderboardModel.comparing(CharacterModel::getLevel, true));
        leaderboard.addSortMethod("gold", 
            LeaderboardModel.comparing(CharacterModel::getGold, true));
        leaderboard.addSortMethod("name", 
            LeaderboardModel.comparing(CharacterModel::getName, false));
    }
	
	public void handleLeaderboardMenu() {
		int currentPage = 0;
        boolean exit = false;
        
        leaderboard.setItems(FileController.loadAllCharacters());
        
        while (!exit) {
            LeaderboardView.displayLeaderboardMenu();
            
            String input = scanner.nextLine();
            
            switch (input) {
                case "1":
                    leaderboard.setSort("level");
                    currentPage = 0;
                    break;
                case "2":
                    leaderboard.setSort("gold");
                    currentPage = 0;
                    break;
                case "3":
                    leaderboard.setSort("name");
                    currentPage = 0;
                    break;
                case "4":
                    currentPage++;
                    break;
                case "5":
                    currentPage = Math.max(0, currentPage - 1);
                    break;
                case "0":
                    exit = true;
                    continue;
                default:
                    System.out.println("Invalid input!");
                    continue;
            }
            
            LeaderboardView.displayLeaderboard(leaderboard, currentPage);
        }
	}
}
