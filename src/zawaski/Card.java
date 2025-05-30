package zawaski;

public abstract class Card {
    protected String cardName;
    protected int apCost;

    public Card(String cardName, int apCost) {
        this.cardName = cardName;
        this.apCost = apCost;
    }

    public String getCardName() {
        return cardName;
    }

    public int getApCost() {
        return apCost;
    }

    // Abstract method to apply the card's effect during battle
    public abstract void effect(Character character, BattleSystem battleSystem);
}