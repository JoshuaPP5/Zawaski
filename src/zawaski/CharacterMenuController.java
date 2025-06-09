package zawaski;

import java.util.List;
import java.util.Scanner;

public class CharacterMenuController {
	private final GameModel gameModel;
	private final Scanner scanner;
	
	public CharacterMenuController(GameModel gameModel, Scanner scanner) {
		this.scanner = scanner;
		this.gameModel = gameModel;
	}
	
	public void selectCharacter() {
		if (!this.isUserLoggedIn()) {
            return;
		}
		
        if(this.isUserCharacterEmpty()) {
        	return;
        }

    	List<CharacterModel> userCharacters = CharacterModel.loadCharactersByUser(this.gameModel.getCurrentUser().getUsername());
        	
        System.out.println("Select a character:");
        for (int i = 0; i < userCharacters.size(); i++) {
            System.out.println((i + 1) + ") " + userCharacters.get(i).getName());
        }
        System.out.print("Enter the number of the character to select: ");
        String input = scanner.nextLine().trim();

        try {
            int selectedIndex = Integer.parseInt(input) - 1;
            if (selectedIndex >= 0 && selectedIndex < userCharacters.size()) {
                this.gameModel.setCurrentCharacter(userCharacters.get(selectedIndex));
                System.out.println("Character '" + this.gameModel.getCurrentCharacter().getName() + "' selected.");
                this.gameModel.getBattleSystem().setPlayer(this.gameModel.getCurrentCharacter());
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
	}
	
	public void createNewCharacter() {
		if (!this.isUserLoggedIn()) {
            return;
		}
		
		if(this.isCharacterLimitReached()) {
			return;
		}

        System.out.print("Enter new character name: ");
        String newCharName = scanner.nextLine().trim();
        if (newCharName.isEmpty()) {
            System.out.println("Character name cannot be empty.");
            return;
        }
        
        int newId = CharacterModel.generateNewId();
        CharacterModel newCharacter = new CharacterModel(newId, newCharName, this.gameModel.getCurrentUser().getUsername());
        this.gameModel.setCurrentCharacter(newCharacter);
        this.gameModel.getCurrentCharacter().addStartingCard();
        this.gameModel.getBattleSystem().setPlayer(this.gameModel.getCurrentCharacter());
        System.out.println("Character '" + newCharName + "' created successfully with ID " + newId + ".");
        
//        Save character
        List<CharacterModel> allCharacters = FileController.loadAllCharacters();
        allCharacters.add(newCharacter);
        
        FileController.saveAllCharacters(allCharacters);
	}
	
	public void changeCharacterName() {
		if(!this.isCharacterSelected()) {
            return;
		}
		
        System.out.print("Enter new name for character '" + this.gameModel.getCurrentCharacter().getName() + "': ");
        String updatedName = scanner.nextLine().trim();
        if (updatedName.isEmpty()) {
            System.out.println("Character name cannot be empty.");
            return;
        }
        
        this.gameModel.getCurrentCharacter().setName(updatedName);
        System.out.println("Character name changed successfully to '" + updatedName + "'.");
	}
	
	public void deleteCharacter() {
		if(!this.isCharacterSelected()) {
            return;
		}
		
        System.out.print("Are you sure you want to delete character '" + this.gameModel.getCurrentCharacter().getName() + "'? (yes/no): ");
        String confirmDelete = scanner.nextLine().trim().toLowerCase();
        if (confirmDelete.equals("yes")) {
        	this.gameModel.getCurrentCharacter().deleteCharacter();
            System.out.println("Character '" + this.gameModel.getCurrentCharacter().getName() + "' deleted successfully.");
            this.gameModel.setCurrentCharacter(null);
        } else {
            System.out.println("Character deletion cancelled.");
        }
	}
	
	private boolean isCharacterSelected() {
		if (gameModel.getCurrentCharacter() == null) {
            System.out.println("No character selected. Please create or select a character first.");
            return false;
        }
        return true;
	}
	
	private boolean isUserLoggedIn() {
        if (gameModel.getCurrentUser() == null) {
            System.out.println("No user logged in.");
            return false;
        }
        return true;
    }
	
	private boolean isCharacterLimitReached() {
		List<CharacterModel> userCharacters = CharacterModel.loadCharactersByUser(this.gameModel.getCurrentUser().getUsername());
        
		if (userCharacters.size() >= 3) {
            System.out.println("You already have the maximum of 3 characters. Delete one before creating a new character.");
            return true;
        }
        return false;
	}

	private boolean isUserCharacterEmpty() {
		List<CharacterModel> userCharacters = CharacterModel.loadCharactersByUser(this.gameModel.getCurrentUser().getUsername());

        if (userCharacters.isEmpty()) {
            System.out.println("You have no characters. Please create one first.");
            return true;
        }
        return false;
	}
	
}
