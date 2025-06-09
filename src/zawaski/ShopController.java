package zawaski;

import java.util.Scanner;

public class ShopController {
	private final GameModel gameModel;
	
	private final Scanner scanner;
	
	private final int GACHA_COST = 150;
	
	public ShopController(GameModel gameModel, Scanner scanner) {
		this.scanner = scanner;
		this.gameModel = gameModel;
	}
	
	public void handleShopMenu() {
		Inventory<Card> playerInventory = this.gameModel.getCurrentCharacter().getInventory();
		Inventory<Card> shopInventory = this.gameModel.getShop().getCardInventory();
		
        boolean exitShop = false;
        
        while (!exitShop) {
            System.out.println("\n=== Shop Menu ===");
            CharacterView.displaySelectedCharacterInformation(this.gameModel.getCurrentCharacter());

            ShopView.displayMenu();

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0":
                    exitShop = true;
                    break;

                case "1": // Buy Cards
                    this.handleBuyCard(playerInventory, shopInventory);
                    break;

                case "2": // Sell Cards
                    this.handleSellCard(playerInventory);
                    break;

                case "3": // Gacha Draw
                    this.handleGachaCard(playerInventory);
                    break;

                default:
                    System.out.println("Invalid option. Please select a valid menu number.");
                    break;
            }
        }
	}
	
	private void handleBuyCard(Inventory<Card> playerInventory, Inventory<Card> shopInventory) {
		if (shopInventory.getItems().isEmpty()) {
            System.out.println("Shop has no cards available for purchase.");
            return;
        }

        ShopView.displayBuyMenu(this.gameModel.getShop().getCardInventory());

        String buyInput = scanner.nextLine().trim();

        try {
            int buyChoice = Integer.parseInt(buyInput);

            if (buyChoice == 0) {
                System.out.println("Cancelled buying.");
                return;
            }

            if (buyChoice < 1 || buyChoice > shopInventory.getItems().size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Card selectedCard = shopInventory.getItems().get(buyChoice - 1);
            int cardPrice = selectedCard.getPrice();

            if (this.gameModel.getCurrentCharacter().getStatus().getGold() < cardPrice) {
                System.out.println("Not enough gold to buy this card.");
                return;
            }

            // Use the shop's buyCard method which handles gold deduction and inventory update
            boolean purchaseSuccess = this.gameModel.getShop().buyCard(selectedCard, playerInventory, this.gameModel.getCurrentCharacter());
            
            if (!purchaseSuccess) {
                System.out.println("Purchase failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
	}
	
	public void handleSellCard(Inventory<Card> playerInventory) {
		if (playerInventory.getItems().isEmpty()) {
            System.out.println("You have no cards to sell.");
            return;
        }

        ShopView.displaySellMenu(playerInventory);
        
        String sellInput = scanner.nextLine().trim();

        try {
            int sellChoice = Integer.parseInt(sellInput);

            if (sellChoice == 0) {
                System.out.println("Cancelled selling.");
                return;
            }

            if (sellChoice < 1 || sellChoice > playerInventory.getItems().size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Card selectedCard = playerInventory.getItems().get(sellChoice - 1);

            // Use the shop's sellCard method which handles gold addition and inventory update
            boolean sellSuccess = this.gameModel.getShop().sellCard(selectedCard, playerInventory, this.gameModel.getCurrentCharacter());
            if (!sellSuccess) {
                System.out.println("Selling failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
	}
	
	private void handleGachaCard(Inventory<Card> playerInventory) {
        int currentGold = this.gameModel.getCurrentCharacter().getStatus().getGold();

        if (currentGold < GACHA_COST) {
            System.out.println("You do not have enough gold for a Gacha Draw. (Cost: " + GACHA_COST + " gold)");
            return;
        }

        // Deduct gold first
        this.gameModel.getCurrentCharacter().deductGold(GACHA_COST);

        // Perform the gacha draw
        Card drawnCard = this.gameModel.getShop().gachaDraw();

        if (drawnCard == null) {
            // Shop is out of cards, refund gold
        	this.gameModel.getCurrentCharacter().getStatus().setGold(currentGold);
            System.out.println("Gacha draw failed: Shop is out of cards. Your gold has been refunded.");
        } else {
            // Add the drawn card to player's inventory
            playerInventory.addItem(drawnCard);
            System.out.println("You spent " + GACHA_COST + " gold and received: " + drawnCard.getCardName());
            System.out.println("Remaining Gold: " + this.gameModel.getCurrentCharacter().getStatus().getGold());
        }
	}
}
