package zawaski;

import java.util.List;
import java.util.Scanner;

public class BattleSystemController {
	private final BattleSystem battleSystem;
	private final Scanner scanner;
	
	public BattleSystemController(BattleSystem battleSystem, Scanner scanner) {
		this.scanner = scanner;
		this.battleSystem = battleSystem;
	}
	
	public void startBattle() {
        // Battle loop
        while (!this.battleSystem.isBattleOver()) {
            BattleSystemView.showPlayerTurn(this.battleSystem);

            System.out.print("Enter card number to play or 'end' to end turn: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("end")) {
            	this.battleSystem.endPlayerTurn();
            } else {
                try {
                    int cardIndex = Integer.parseInt(input) - 1;
                    this.battleSystem.playCardFromHand(cardIndex, (CharacterModel)this.battleSystem.getPlayer());
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Invalid input. Please enter a valid card number or 'end'.");
                }
            }
        }

        if (this.battleSystem.isPlayerWinner()) { 
        	this.battleSystem.awardRewards(); 
        }
        ((CharacterModel) this.battleSystem.getPlayer()).addExp(1000);
        this.battleSystem.restoreAll();
        
//        Save character
        List<CharacterModel> allCharacters = FileController.loadAllCharacters();
        
        for(int i = 0; i < allCharacters.size(); i++) {
        	if(allCharacters.get(i).getName().equals(this.battleSystem.getPlayer().getName())) {
        		allCharacters.remove(i);        		
        	}
        }
        allCharacters.add((CharacterModel) this.battleSystem.getPlayer());
        
        
        FileController.saveAllCharacters(allCharacters);
	}
}
