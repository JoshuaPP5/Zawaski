package zawaski;

public class BattleSystem {

    private boolean battleStarted;

    public BattleSystem() {
        this.battleStarted = false;
    }

    // Start the battle
    public void startBattle() {
        battleStarted = true;
        System.out.println("Battle started!");
        // Additional initialization logic can be added here
    }

    // Play a card during battle, triggering its effect
    public void playCard(Card card, Character character) {
        if (!battleStarted) {
            System.out.println("Battle has not started yet.");
            return;
        }
        System.out.println("Playing card: " + card.getCardName());
        card.effect(character, this);
    }

    // Overloaded method for compatibility if needed
    public void playCard(Card card) {
        System.out.println("playCard(Card) method called without character context.");
        // This can be left empty or throw an exception if not used
    }

    // End the current turn
    public void endTurn() {
        if (!battleStarted) {
            System.out.println("Battle has not started yet.");
            return;
        }
        System.out.println("Turn ended.");
        // Additional turn-end logic can be added here
    }
}