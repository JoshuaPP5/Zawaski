package zawaski;

import java.util.Random;

public class ShopModel {
    private Inventory<Card> cardInventory;

    public ShopModel() {
        cardInventory = new Inventory<>();
        initializeShopInventory();
    }

    private void initializeShopInventory() {
        cardInventory.addItem(CardFactory.createCard("Fireball"));
        cardInventory.addItem(CardFactory.createCard("Ice Lance"));
        cardInventory.addItem(CardFactory.createCard("Thunder Strike"));
        cardInventory.addItem(CardFactory.createCard("Heal"));
        cardInventory.addItem(CardFactory.createCard("AP Potion"));
        cardInventory.addItem(CardFactory.createCard("Drain"));
        cardInventory.addItem(CardFactory.createCard("Draw Card"));
        // Add more cards as needed
    }

    public boolean buyCard(Card card, Inventory<Card> playerInventory, CharacterModel player) {
        if (!cardInventory.getItems().contains(card)) {
            System.out.println("Card not available in shop.");
            return false;
        }

        int price = card.getPrice();
        if (player.getGold() < price) {
            System.out.println("Not enough Gold to buy " + card.getCardName() + ". You need " + price + " Gold.");
            return false;
        }

        player.deductGold(price);
        playerInventory.addItem(card);
        System.out.println("Card bought: " + card.getCardName() + " for " + price + " Gold.");
        System.out.println("Remaining Gold: " + player.getGold());
        return true;
    }

    public boolean sellCard(Card card, Inventory<Card> playerInventory, CharacterModel player) {
        if (!playerInventory.getItems().contains(card)) {
            System.out.println("You don't own this card.");
            return false;
        }

        int price = card.getPrice() / 2;
        playerInventory.removeItem(card);
        player.addGold(price);

        System.out.println("Card sold: " + card.getCardName() + " for " + price + " Gold.");
        System.out.println("Current Gold: " + player.getGold());
        return true;
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

    public Inventory<Card> getCardInventory() {
        return cardInventory;
    }
}
