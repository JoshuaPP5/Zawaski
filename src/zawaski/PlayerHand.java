package zawaski;

public class PlayerHand {
    private Inventory<Card> handCards;

    public PlayerHand() {
        handCards = new Inventory<>();
    }

    public void addCard(Card card) {
        handCards.addItem(card);
    }

    public void removeCard(Card card) {
        handCards.removeItem(card);
    }

    public Inventory<Card> getHandCards() {
        return handCards;
    }

    public void clearHand() {
        handCards = new Inventory<>();
    }
}