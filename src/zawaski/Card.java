package zawaski;

import java.util.function.BiConsumer;

public class Card {
    private String cardName;
    private int apCost;
    private BiConsumer<Character, BattleSystem> effectFunction;

    public Card(String cardName, int apCost, BiConsumer<Character, BattleSystem> effectFunction) {
        this.cardName = cardName;
        this.apCost = apCost;
        this.effectFunction = effectFunction;
    }

    public String getCardName() {
        return cardName;
    }

    public int getApCost() {
        return apCost;
    }

    public void effect(Character character, BattleSystem battleSystem) {
        effectFunction.accept(character, battleSystem);
    }
}