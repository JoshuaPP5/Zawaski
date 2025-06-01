package zawaski;

import java.util.ArrayList;
import java.util.List;

public class BattleSystem {
    private Combatant player;
    private Combatant enemy;
    private boolean battleStarted;
    private PlayerHand playerHand;             // Cards currently in hand
    private java.util.Random random;           // For random card drawing
    private List<Enemy> enemies = new ArrayList<>();

    // Method to award experience and gold to the player
    public void awardRewards(Character character) {
        player.addExp(enemy.getExp());  // Add experience to player
        player.addGold(enemy.getGold());  // Add gold to player
        System.out.println("Rewards available: " + enemy.getExp() + " EXP, " + enemy.getGold() + " Gold");
    }
    
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }
    
    // Enemy list management methods can be removed or kept if you want external control
    // public void addEnemy(Enemy enemy) { enemies.add(enemy); }
    // public void removeEnemy(Enemy enemy) { enemies.remove(enemy); }

    // Initialize enemies if empty, then select random enemy
    public Enemy selectRandomEnemy() {
        if (enemies.isEmpty()) {
            // Initialize enemy list here
            enemies.add(new Enemy(1, "Goblin", 1, 50, 10, 15, 10, 10));
            enemies.add(new Enemy(2, "Orc", 2, 70, 12, 18, 15, 15));
            enemies.add(new Enemy(3, "Troll", 3, 80, 15, 22, 20, 20));
        }
        return enemies.get(random.nextInt(enemies.size()));
    }


    public Combatant getEnemy() {
		return enemy;
	}
    
	public Combatant getPlayer() {
		return player;
	}

	public void setPlayer(Character character) {
		this.player = character;
	}

	// Start the battle
    public void startBattle() {
        battleStarted = true;
        playerHand.clearHand();
        drawCards(3);
    }
    
    public void restoreAll(Character character, Combatant enemy) {
    	player.maxHeal();
        player.maxAP();
        enemy.maxHeal();
        enemy.maxAP();
    }
    
 // Initialize battle with a player character
    public void initializeBattle(Character character) {
        if (character == null) {
            throw new IllegalArgumentException("Character cannot be null");
        }
        this.player = character;
        this.enemy = selectRandomEnemy();  // Randomly select enemy here
        restoreAll(character, enemy);
        System.out.println("Battle initialized between " + player.getName() + " and " + enemy.getName());
    }

    
    public void populatePlayerInventory() {
    	Inventory<Card> inventory = player.getInventory();
        inventory.addItem(CardFactory.createCard("Fireball"));
        inventory.addItem(CardFactory.createCard("Heal"));
        // Add more cards as needed
    }
    
    public void rewardCard() {
    	Inventory<Card> inventory = player.getInventory();
        inventory.addItem(CardFactory.createCard("Drain"));
        // Add more cards as needed
    }

    public void drawCards(int n) {
        Inventory<Card> inventory = player.getInventory();
        java.util.List<Card> availableCards = inventory.getItems();
        java.util.List<Card> shuffled = new java.util.ArrayList<>(availableCards);
        java.util.Collections.shuffle(shuffled, random);

        int totalCards = shuffled.size();

        // If no cards are available, just return early
        if (totalCards == 0) {
            return;
        }

        for (int i = 0; i < n; i++) {
            // Use modulo to cycle through the shuffled list repeatedly
            Card card = shuffled.get(i % totalCards);
            playerHand.addCard(card);
        }
    }
    
    public void printPlayerInventory() {
    	Inventory<Card> inventory = player.getInventory();
        System.out.println("Player Inventory contains:");
        for (Card card : inventory.getItems()) {
            System.out.println("- " + card.getCardName() + " (AP: " + card.getApCost() + ")");
        }
    }
    
    public BattleSystem(Combatant player, Combatant enemy) {
        this.player = player;
        this.enemy = enemy;
    }
    
    public void playCardFromHand(int cardIndex, Character character) {
        java.util.List<Card> handCards = playerHand.getHandCards().getItems();
        if (cardIndex < 0 || cardIndex >= handCards.size()) {
            System.out.println("Invalid card number. Please select a valid card.");
            return;
        }

        Card cardToPlay = handCards.get(cardIndex);
        int currentAp = character.getStatus().getAp();
        int cardApCost = cardToPlay.getApCost();

        if (currentAp < cardApCost) {
            System.out.println("Not enough Action Points (AP) to play card: " + cardToPlay.getCardName() + 
                               ". Required: " + cardApCost + ", Available: " + currentAp);
            return;
        }

        character.getStatus().setAp(currentAp - cardApCost);
        System.out.println("Playing card: " + cardToPlay.getCardName() + " (Cost: " + cardApCost + " AP)");
        cardToPlay.effect(character, this);
        playerHand.removeCard(cardToPlay);
        System.out.println("Card '" + cardToPlay.getCardName() + "' removed from hand.");
    }

    public void showPlayerHand() {
        java.util.List<Card> handCards = playerHand.getHandCards().getItems();
        if (handCards.isEmpty()) {
            System.out.println("Your hand is empty.");
            return;
        }
        System.out.println("Cards in hand:");
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            System.out.println((i + 1) + ". " + card.getCardName() + " (AP Cost: " + card.getApCost() + ")");
        }
    }

    public void endPlayerTurn() {
        System.out.println("Player's turn ended.");
        enemyTurn();
    }

    public void enemyTurn() {
        if (isBattleOver()) { return; }

        System.out.println("Enemy's turn:");
        // Simple enemy attack logic
        player.takeDamage(enemy.getAttackPower());
        System.out.println(enemy.getName() + " attacks " + player.getName() + " for " + enemy.getAttackPower() + " damage.");

        if (player.isAlive()) {
            player.restoreAP(5);;
            System.out.println(player.getName() + "'s turn begins. AP increased by 5 to " + player.getStatus().getAp() + ".");
            drawCards(1);
        }
    }

    public BattleSystem() {
        this.battleStarted = false;
        this.setPlayerHand(new PlayerHand());
        this.setRandom(new java.util.Random());
    }

    private String getHandCardNames() {
        StringBuilder sb = new StringBuilder();
        for (Card card : playerHand.getHandCards().getItems()) {
            sb.append(card.getCardName()).append(", ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "No cards";
    }

    // Execute one turn of the battle
    public void executeTurn() {
        if (!battleStarted) {
            System.out.println("Battle has not started yet.");
            return;
        }

        // Player attacks enemy
        enemy.takeDamage(player.getAttackPower());
        System.out.println(player.getName() + " attacks " + enemy.getName() + " for " + player.getAttackPower() + " damage.");

        // Check if enemy is defeated
        if (!enemy.isAlive()) {
//            System.out.println(enemy.getName() + " has been defeated!");
            return;
        }

        // Enemy attacks player
        player.takeDamage(enemy.getAttackPower());
        System.out.println(enemy.getName() + " attacks " + player.getName() + " for " + enemy.getAttackPower() + " damage.");
    }

    // Check if the battle is over
    public boolean isBattleOver() {
        if (!battleStarted) {
            return false;
        }
        
        if (!player.isAlive()) {
            System.out.println(player.getName() + " has been defeated. Game Over.");
            return true;
        }
        
        if (!enemy.isAlive()) {
            System.out.println(enemy.getName() + " has been defeated. You win!");
            return true;
        }
        
        return false;
    }

    // Play a card during battle
    public void playCard(Card card, Character character) {
        if (!battleStarted) {
            System.out.println("Battle has not started yet.");
            return;
        }

        int currentAp = character.getStatus().getAp();
        int cardApCost = card.getApCost();

        if (currentAp < cardApCost) {
            System.out.println("Not enough Action Points (AP) to play card: " + card.getCardName() + 
                               ". Required: " + cardApCost + ", Available: " + currentAp);
            return;
        }

        // Deduct AP cost
        character.getStatus().setAp(currentAp - cardApCost);

        System.out.println("Playing card: " + card.getCardName() + " (Cost: " + cardApCost + " AP)");

        // Apply the card effect
        card.effect(character, this);

        // Remove the card from the player's hand after playing
        if (playerHand.getHandCards().getItems().contains(card)) {
            playerHand.removeCard(card);
            System.out.println("Card '" + card.getCardName() + "' removed from hand.");
        } else {
            System.out.println("Warning: Card '" + card.getCardName() + "' was not found in hand.");
        }

        // Optionally, display the current hand after playing the card
        System.out.println("Current hand: " + getHandCardNames());
    }

    // Overloaded method for compatibility
    public void playCard(Card card) {
        System.out.println("playCard(Card) method called without character context.");
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

	public PlayerHand getPlayerHand() {
		return playerHand;
	}


	public void setPlayerHand(PlayerHand playerHand) {
		this.playerHand = playerHand;
	}


	public java.util.Random getRandom() {
		return random;
	}


	public void setRandom(java.util.Random random) {
		this.random = random;
	}

	public boolean isPlayerWinner() {
		if (!enemy.isAlive()) {
			return true;
		}
		return false;
	}
}
