package zawaski;

import java.util.HashMap;
import java.util.Map;

public class CardFactory {
    private static Map<String, Card> cardDefinitions = new HashMap<>();

    static {
        cardDefinitions.put("Fireball", new Card("Fireball", 5, 100, (character, battleSystem) -> {
            battleSystem.getEnemy().takeDamage(30);
        }));
        
        cardDefinitions.put("Ice Lance", new Card("Ice Lance", 5, 200, (character, battleSystem) -> {
            battleSystem.getEnemy().takeDamage(50);
        }));
        
        cardDefinitions.put("Thunder Strike", new Card("Thunder Strike", 5, 250, (character, battleSystem) -> {
            battleSystem.getEnemy().takeDamage(65);
        }));

        cardDefinitions.put("Heal", new Card("Heal", 5, 100, (character, battleSystem) -> {
            character.heal(20);
        }));

        cardDefinitions.put("AP Potion", new Card("AP Potion", 0, 125, (character, battleSystem) -> {
            character.restoreAP(5);
        }));
        
        cardDefinitions.put("Drain", new Card("Drain", 2, 125, (character, battleSystem) -> {
            character.heal(15);
            battleSystem.getEnemy().takeDamage(15);
        }));
        
        cardDefinitions.put("Draw Card", new Card("Draw Card", 5, 150, (character, battleSystem) -> {
            battleSystem.drawCards(1);
        }));
        
        // More cards
    }

    public static Card createCard(String cardName) {
        Card card = cardDefinitions.get(cardName);
        if (card == null) {
            throw new IllegalArgumentException("Card not found: " + cardName);
        }
        return card;
    }
}