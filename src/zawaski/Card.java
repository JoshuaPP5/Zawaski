package zawaski;

import java.util.function.BiConsumer;

public class Card {
    private String cardName;
    private int apCost;
    private int price;  // New field for price
    private BiConsumer<Character, BattleSystem> effectFunction;

    // Updated constructor to include price
    public Card(String cardName, int apCost, int price, BiConsumer<Character, BattleSystem> effectFunction) {
        this.cardName = cardName;
        this.apCost = apCost;
        this.price = price;
        this.effectFunction = effectFunction;
    }

    public String getCardName() {
        return cardName;
    }

    public int getApCost() {
        return apCost;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void effect(Character character, BattleSystem battleSystem) {
        effectFunction.accept(character, battleSystem);
    }
}
