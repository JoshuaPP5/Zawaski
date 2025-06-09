package zawaski;

import java.util.function.BiConsumer;

public class Card {
    private String cardName;
    private int apCost;
    private int price;
    private BiConsumer<CharacterModel, BattleSystem> effectFunction;


    public Card(String cardName, int apCost, int price, BiConsumer<CharacterModel, BattleSystem> effectFunction) {
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
    
    public int getSellPrice() {
    	return (int) (price / 2);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void effect(CharacterModel character, BattleSystem battleSystem) {
        effectFunction.accept(character, battleSystem);
    }
}
