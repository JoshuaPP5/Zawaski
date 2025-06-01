package zawaski;

import java.util.HashMap;
import java.util.Map;

public class CardFactory {
    private static Map<String, Card> cardDefinitions = new HashMap<>();

    static {
        // Define cards with their effects using Character helper methods
        cardDefinitions.put("Fireball", new Card("Fireball", 3, (character, battleSystem) -> {
            battleSystem.getEnemy().takeDamage(30);  // Use helper method for damage
        }));

        cardDefinitions.put("Heal", new Card("Heal", 2, (character, battleSystem) -> {
            character.heal(20);  // Use helper method for healing
        }));

        cardDefinitions.put("AP Potion", new Card("AP Potion", 0, (character, battleSystem) -> {
            character.restoreAP(5);  // Use helper method for healing
        }));
        
        cardDefinitions.put("Drain", new Card("Drain", 3, (character, battleSystem) -> {
            character.heal(5);  // Use helper method for healing
            battleSystem.getEnemy().takeDamage(5);
        }));
        
        cardDefinitions.put("Draw Card", new Card("Draw Card", 5, (character, battleSystem) -> {
            battleSystem.drawCards(1);
        }));
        
        // Add more cards here...
    }

    public static Card createCard(String cardName) {
        Card card = cardDefinitions.get(cardName);
        if (card == null) {
            throw new IllegalArgumentException("Card not found: " + cardName);
        }
        return card;
    }
}