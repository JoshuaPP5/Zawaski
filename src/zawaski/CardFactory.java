package zawaski;

import java.util.HashMap;
import java.util.Map;

public class CardFactory {
    private static Map<String, Card> cardDefinitions = new HashMap<>();

    static {
        // Define cards with their effects using Character helper methods
        cardDefinitions.put("Fireball", new Card("Fireball", 5, 100, (character, battleSystem) -> {
            battleSystem.getEnemy().takeDamage(30);  // Use helper method for damage
        }));
        
        cardDefinitions.put("Ice Lance", new Card("Ice Lance", 5, 200, (character, battleSystem) -> {
            battleSystem.getEnemy().takeDamage(50);  // Use helper method for damage
        }));
        
        cardDefinitions.put("Thunder Strike", new Card("Thunder Strike", 5, 250, (character, battleSystem) -> {
            battleSystem.getEnemy().takeDamage(65);  // Use helper method for damage
        }));

        cardDefinitions.put("Heal", new Card("Heal", 5, 100, (character, battleSystem) -> {
            character.heal(20);  // Use helper method for healing
        }));

        cardDefinitions.put("AP Potion", new Card("AP Potion", 0, 125, (character, battleSystem) -> {
            character.restoreAP(5);  // Use helper method for healing
        }));
        
        cardDefinitions.put("Drain", new Card("Drain", 2, 125, (character, battleSystem) -> {
            character.heal(15);  // Use helper method for healing
            battleSystem.getEnemy().takeDamage(15);
        }));
        
        cardDefinitions.put("Draw Card", new Card("Draw Card", 5, 150, (character, battleSystem) -> {
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