package zawaski;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleSystem {
    private CharacterModel player;
    private Enemy enemy;
    private boolean battleStarted;
    private Inventory<Card> playerHandCards;
    private Random random;    
    private List<Enemy> enemies = new ArrayList<>();


    public void awardRewards() {
        player.addExp(enemy.getExp()); 
        player.addGold(enemy.getGold());  
        System.out.println("Rewards available: " + enemy.getExp() + " EXP, " + enemy.getGold() + " Gold");
    }
    
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }
    
    public Enemy selectRandomEnemy() {
        if (enemies.isEmpty()) {
            // Initialize enemy list here
            enemies.add(new Enemy(1, "Goblin", 1, 50, 10, 15, 10, 10));
            enemies.add(new Enemy(2, "Orc", 2, 70, 12, 18, 15, 15));
            enemies.add(new Enemy(3, "Troll", 3, 80, 15, 22, 20, 20));
        }
        return enemies.get(random.nextInt(enemies.size()));
    }


    public GameEntity getEnemy() {
		return enemy;
	}
    
	public GameEntity getPlayer() {
		return player;
	}
	

	public void setPlayer(CharacterModel character) {
		this.player = character;
	}

	public Inventory<Card> getPlayerHandCards(){
		return playerHandCards;
	}
	
    public void restoreAll() {
    	player.maxHeal();
        player.maxAP();
        enemy.maxHeal();
        enemy.maxAP();
    }
    
    
    public void initializeBattle(CharacterModel character) {
        if (character == null) {
            throw new IllegalArgumentException("Character cannot be null");
        }
        
        this.player = character;
        this.enemy = selectRandomEnemy();
        
        battleStarted = true;
        playerHandCards = new Inventory<Card>();
        drawCards(3);
        restoreAll();
        
        System.out.println("\nBattle initialized between " + player.getName() + " and " + enemy.getName() + "!");
    }
    
    public void rewardCard() {
    	Inventory<Card> inventory = player.getInventory();
        inventory.addItem(CardFactory.createCard("Drain"));
        // Add more cards as needed
    }

    public void drawCards(int n) {
        Inventory<Card> inventory = player.getInventory();
        List<Card> availableCards = inventory.getItems();
        List<Card> shuffled = new java.util.ArrayList<>(availableCards);
        java.util.Collections.shuffle(shuffled, random);

        int totalCards = shuffled.size();

        // If no cards are available, just return early
        if (totalCards == 0) {
            return;
        }

        for (int i = 0; i < n; i++) {
            // Use modulo to cycle through the shuffled list repeatedly
            Card card = shuffled.get(i % totalCards);
            playerHandCards.addItem(card);;
        }
    }
    
    public void printPlayerInventory() {
    	Inventory<Card> inventory = player.getInventory();
        System.out.println("Player Inventory contains:");
        for (Card card : inventory.getItems()) {
            System.out.println("- " + card.getCardName() + " (AP: " + card.getApCost() + ")");
        }
    }
    
    public BattleSystem(GameEntity player, GameEntity enemy) {
        this.player = (CharacterModel) player;
        this.enemy = (Enemy) enemy;
    }
    
    public void playCardFromHand(int cardIndex, CharacterModel character) {
        List<Card> handCards = playerHandCards.getItems();
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
        playerHandCards.removeItem(cardToPlay);
        System.out.println("Card '" + cardToPlay.getCardName() + "' removed from hand.");
    }

    public void endPlayerTurn() {
        System.out.println("Player's turn ended.");
        enemyTurn();
    }

    public void enemyTurn() {
        if (isBattleOver()) { 
        	return; 
        }

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
        this.playerHandCards = new Inventory<Card>();
        this.random = new Random();
    }

    private String getHandCardNames() {
        StringBuilder sb = new StringBuilder();
        for (Card card : playerHandCards.getItems()) {
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
    public void playCard(Card card, CharacterModel character) {
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
        if (playerHandCards.getItems().contains(card)) {
            playerHandCards.removeItem(card);
            System.out.println("Card '" + card.getCardName() + "' removed from hand.");
        } else {
            System.out.println("Warning: Card '" + card.getCardName() + "' was not found in hand.");
        }

        // Optionally, display the current hand after playing the card
        System.out.println("Current hand: " + getHandCardNames());
    }

    // End the current turn
    public void endTurn() {
        if (!battleStarted) {
            System.out.println("Battle has not started yet.");
            return;
        }
        System.out.println("Turn ended.");
    }

	public boolean isPlayerWinner() {
		if (!enemy.isAlive()) {
			return true;
		}
		return false;
	}
}
