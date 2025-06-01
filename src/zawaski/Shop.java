package zawaski;

import java.util.Random;

public class Shop {
    private Inventory<Card> cardInventory;

    public Shop() {
        cardInventory = new Inventory<>();
        // Initialize shop with some cards
    }

    public void buyCard(Card card, Inventory<Card> playerInventory) {
        if (cardInventory.getItems().contains(card)) {
            playerInventory.addItem(card);
//            cardInventory.removeItem(card);
            System.out.println("Card bought: " + card.getCardName());
        } else {
            System.out.println("Card not available in shop.");
        }
    }

    public void sellCard(Card card, Inventory<Card> playerInventory) {
        if (playerInventory.getItems().contains(card)) {
            playerInventory.removeItem(card);
            cardInventory.addItem(card);
            System.out.println("Card sold: " + card.getCardName());
        } else {
            System.out.println("You don't own this card.");
        }
    }

    public Card gachaDraw() {
        Random rand = new Random();
        if (cardInventory.getItems().isEmpty()) {
            System.out.println("Shop is out of cards.");
            return null;
        }
        int index = rand.nextInt(cardInventory.getItems().size());
        Card drawnCard = cardInventory.getItems().get(index);
        System.out.println("Gacha draw: " + drawnCard.getCardName());
        return drawnCard;
    }
}