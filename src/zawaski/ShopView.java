package zawaski;

public class ShopView {
    public static void displayBuyMenu(Inventory<Card> cardInventory) {
    	int index = 1;
    	
        System.out.println("Available cards to buy:");
        for (Card card : cardInventory.getItems()) {
            System.out.println(index + ") " + card.getCardName() + " (Price: " + card.getPrice() + " Gold)");
            index++;
        }
        System.out.println("======================");
        
        System.out.print("Enter the number of the card to buy or 0 to cancel: ");
    }
    
    public static void displaySellMenu(Inventory<Card> playerInventory) {
        int index = 1;
        
        System.out.println("Your cards:");
        for (Card card : playerInventory.getItems()) {
            System.out.println(index + ") " + card.getCardName() + " (Sell Price: " + (card.getSellPrice()) + " Gold)");
            index++;
        }

        System.out.print("Enter the number of the card to sell or 0 to cancel: ");
    }
    
    public static void displayMenu() {
    	System.out.println("1) Buy Cards");
        System.out.println("2) Sell Cards");
        System.out.println("3) Gacha Draw");
        System.out.println("0) Return to user menu");
        System.out.print("Select an option: ");
    }
}
