package zawaski;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

public class GameController {
	private GameModel gm;
	private Scanner scanner = new Scanner(System.in);
    
	public GameController() {
		this.gm = new GameModel();
	}
	
	public void start() {

        System.out.println("===================================");
        System.out.println(" Welcome to the Zawaski Card Game! ");
        System.out.println("===================================\n");

        boolean exit = false;
        while (!exit) {
            printMainMenu();
            System.out.print("Select an option: ");
            String input = scanner.nextLine().trim();
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    handleRegister();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                	handleLeaderboard();
                	break;
                case 0:
                    System.out.println("Exiting game. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        }
        scanner.close();
	}
	
	private void printMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1) Register new account");
        System.out.println("2) Login to existing account");
        System.out.println("3) Leaderboard");
        System.out.println("0) Exit game");
    }
	
	private void handleLogin() {
        System.out.println("\n=== Login ===");
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User user = new User(username, password);

            if (user.login(password)) {
                this.gm.setCurrentUser(user);
                System.out.println("Login successful! Welcome, " + this.gm.getCurrentUser().getUsername() + ".");
                handlePostLoginMenu();
            } else {
                System.out.println("Login failed. Invalid username or password.");
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }
	
	private void handleRegister() {
        System.out.println("\n=== Register New Account ===");
        try {
            System.out.print("Enter desired username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter desired password: ");
            String password = scanner.nextLine();

            User newUser = new User(username, password);
            if (newUser.register()) {
                System.out.println("Registration successful! You can now login.");
            } else {
                System.out.println("Username already exists. Please try a different username.");
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }
	
	private void handleLeaderboard() {
        final int PAGE_SIZE = 5;
        int currentPage = 0; // zero-based for Leaderboard class
        boolean exit = false;
        String sortBy = "level"; // default sorting

        while (!exit) {
            System.out.println("\n=== Leaderboard Menu ===");
            System.out.println("Select sorting criteria:");
            System.out.println("1) Sort by Level");
            System.out.println("2) Sort by Wealth (Gold)");
            System.out.println("0) Exit Leaderboard");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    sortBy = "level";
                    currentPage = 0;
                    break;
                case "2":
                    sortBy = "gold";
                    currentPage = 0;
                    break;
                case "0":
                    exit = true;
                    continue;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 0.");
                    continue;
            }

            while (!exit) {
                List<Character> entries = Leaderboard.generateLeaderboard(sortBy, currentPage, PAGE_SIZE);

                if (entries.isEmpty()) {
                    System.out.println("\nNo entries to display on this page.");
                    break;
                }

                System.out.println("\n=== Leaderboard (Page " + (currentPage + 1) + ") ===");
                System.out.printf("%-15s %-20s %-10s %-10s%n", "Username", "Character Name", "Level", "Gold");
                System.out.println("-------------------------------------------------------------");

                for (Character c : entries) {
                    System.out.printf("%-15s %-20s %-10d %-10d%n",
                            c.getName(),          // Owner username
                            c.getCharacterName(), // Character name
                            c.getLevel(),
                            c.getGold());
                }

                System.out.println("\nOptions:");
                System.out.println("N - Next page");
                System.out.println("P - Previous page");
                System.out.println("C - Change sorting criteria");
                System.out.println("E - Exit leaderboard");
                System.out.print("Enter choice (N/P/C/E): ");

                String navChoice = scanner.nextLine().trim().toUpperCase();

                switch (navChoice) {
                    case "N":
                        currentPage++;
                        // Check if next page has entries
                        if (Leaderboard.generateLeaderboard(sortBy, currentPage, PAGE_SIZE).isEmpty()) {
                            System.out.println("You are on the last page.");
                            currentPage--;
                        }
                        break;
                    case "P":
                        if (currentPage > 0) {
                            currentPage--;
                        } else {
                            System.out.println("You are on the first page.");
                        }
                        break;
                    case "C":
                        // Break inner loop to go back to sorting menu
                        break;
                    case "E":
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter N, P, C, or E.");
                }

                if (navChoice.equals("C")) {
                    // Break to outer loop to select sorting again
                    break;
                }
            }
        }

        System.out.println("Exiting Leaderboard. Returning to main menu...");
    }
    
    private void handlePostLoginMenu() {
        boolean logout = false;
        while (!logout) {
        	System.out.println("\n=== User Menu ===");
        	System.out.println("Selected Character: " + 
        		    (this.gm.getCurrentCharacter() != null && this.gm.getCurrentCharacter().getCharacterName() != null 
        		        ? this.gm.getCurrentCharacter().getCharacterName() 
        		        : "None"));
        	if (this.gm.getCurrentCharacter() != null) {
        		System.out.println("Level: " + this.gm.getCurrentCharacter().getStatus().getLevel() + " | Exp: " + this.gm.getCurrentCharacter().getStatus().getExp() + "/" + this.gm.getCurrentCharacter().getXpThresholdForNextLevel() + " | Gold: " + this.gm.getCurrentCharacter().getStatus().getGold());
        		System.out.println("HP: " + this.gm.getCurrentCharacter().getStatus().getHp() + "/" + this.gm.getCurrentCharacter().getStatus().getMaxHp() + " | AP: " + this.gm.getCurrentCharacter().getStatus().getAp() + "/" + this.gm.getCurrentCharacter().getStatus().getMaxAp());
        	}
            System.out.println("1) Manage account");
            System.out.println("2) Manage characters");
            System.out.println("3) Start battle");
            System.out.println("4) Inventory");
            System.out.println("5) Shop");
            System.out.println("0) Logout");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
	            case "0":
	            	System.out.println("Logging out...");
	            	this.gm.setCurrentCharacter(null);
	            	this.gm.setCurrentUser(null);
	            	logout = true;
	            	break;
                case "1":
                    handleManageAccountMenu();
                    break;
                case "2":
                    handleManageCharactersMenu();
                    break;
                case "3":
                    handleStartBattle();
                    break;
                case "4":
                	if (this.gm.getCurrentCharacter() != null) {
                		this.gm.getBattleSystem().printPlayerInventory();
                	} else {
                		System.out.println("No character selected. Please create or select a character first.");
                	}
                    break;
                case "5":
                    handleShopMenu();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleManageCharactersMenu() {
        boolean backToUserMenu = false;
        while (!backToUserMenu) {
        	System.out.println("\n=== Characters Menu ===");
        	System.out.println("Selected Character: " + 
        		    (this.gm.getCurrentCharacter() != null && this.gm.getCurrentCharacter().getCharacterName() != null 
        		        ? this.gm.getCurrentCharacter().getCharacterName() 
        		        : "None"));
        	if (this.gm.getCurrentCharacter() != null) {
        		System.out.println("Level: " + this.gm.getCurrentCharacter().getStatus().getLevel() + " | Exp: " + this.gm.getCurrentCharacter().getStatus().getExp() + "/" + this.gm.getCurrentCharacter().getXpThresholdForNextLevel() + " | Gold: " + this.gm.getCurrentCharacter().getStatus().getGold());
        		System.out.println("HP: " + this.gm.getCurrentCharacter().getStatus().getHp() + "/" + this.gm.getCurrentCharacter().getStatus().getMaxHp() + " | AP: " + this.gm.getCurrentCharacter().getStatus().getAp() + "/" + this.gm.getCurrentCharacter().getStatus().getMaxAp());
        	}
            System.out.println("1) Select character");
            System.out.println("2) Create new character");
            System.out.println("3) Change character's name");
            System.out.println("4) Delete character");
            System.out.println("0) Return to user menu");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
	            case "1":
	            	if (this.gm.getCurrentUser() != null) {
	                    // Fetch all characters for the current user
	                    List<Character> userCharacters = Character.loadCharactersByUser(this.gm.getCurrentUser().getUsername());

	                    if (userCharacters.isEmpty()) {
	                        System.out.println("You have no characters. Please create one first.");
	                        break;
	                    }

	                    System.out.println("Select a character:");
	                    for (int i = 0; i < userCharacters.size(); i++) {
	                        System.out.println((i + 1) + ") " + userCharacters.get(i).getCharacterName());
	                    }
	                    System.out.print("Enter the number of the character to select: ");
	                    String input = scanner.nextLine().trim();

	                    try {
	                        int selectedIndex = Integer.parseInt(input) - 1;
	                        if (selectedIndex >= 0 && selectedIndex < userCharacters.size()) {
	                            this.gm.setCurrentCharacter(userCharacters.get(selectedIndex));
	                            System.out.println("Character '" + this.gm.getCurrentCharacter().getCharacterName() + "' selected.");
	                            this.gm.getBattleSystem().setPlayer(this.gm.getCurrentCharacter());
	                        } else {
	                            System.out.println("Invalid selection. Please try again.");
	                        }
	                    } catch (NumberFormatException e) {
	                        System.out.println("Invalid input. Please enter a number.");
	                    }
	                } else {
	                    System.out.println("No user logged in.");
	                }
	                break;
	            case "2":
	                if (this.gm.getCurrentUser() != null) {
	                    // Check how many characters the user already has
	                    List<Character> userCharacters = Character.loadCharactersByUser(this.gm.getCurrentUser().getUsername());
	                    if (userCharacters.size() >= 3) {
	                        System.out.println("You already have the maximum of 3 characters. Delete one before creating a new character.");
	                        break;
	                    }

	                    System.out.print("Enter new character name: ");
	                    String newCharName = scanner.nextLine().trim();
	                    if (newCharName.isEmpty()) {
	                        System.out.println("Character name cannot be empty.");
	                        break;
	                    }
	                    int newId = Character.generateNewId();
	                    Character newCharacter = new Character(newId, newCharName, this.gm.getCurrentUser().getUsername());
	                    this.gm.setCurrentCharacter(newCharacter);
	                    this.gm.getCurrentCharacter().addStartingCard();
	                    this.gm.getBattleSystem().setPlayer(this.gm.getCurrentCharacter());
	                    System.out.println("Character '" + newCharName + "' created successfully with ID " + newId + ".");
	                } else {
	                    System.out.println("No user logged in.");
	                }
	                break;
                case "3":
                    if (this.gm.getCurrentCharacter() != null) {
                        System.out.print("Enter new name for character '" + this.gm.getCurrentCharacter().getCharacterName() + "': ");
                        String updatedName = scanner.nextLine().trim();
                        if (updatedName.isEmpty()) {
                            System.out.println("Character name cannot be empty.");
                            break;
                        }
                        this.gm.getCurrentCharacter().setCharacterName(updatedName);
                        System.out.println("Character name changed successfully to '" + updatedName + "'.");
                    } else {
                        System.out.println("No character selected. Please create or select a character first.");
                    }
                    break;
                case "4":
                    if (this.gm.getCurrentCharacter() != null) {
                        System.out.print("Are you sure you want to delete character '" + this.gm.getCurrentCharacter().getCharacterName() + "'? (yes/no): ");
                        String confirmDelete = scanner.nextLine().trim().toLowerCase();
                        if (confirmDelete.equals("yes")) {
                        	this.gm.getCurrentCharacter().deleteCharacter();
                            System.out.println("Character '" + this.gm.getCurrentCharacter().getCharacterName() + "' deleted successfully.");
                            this.gm.setCurrentCharacter(null);
                        } else {
                            System.out.println("Character deletion cancelled.");
                        }
                    } else {
                        System.out.println("No character selected. Please create or select a character first.");
                    }
                    break;
                case "0":
                    backToUserMenu = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleManageAccountMenu() {
        boolean backToUserMenu = false;
        while (!backToUserMenu) {
        	System.out.println("\n=== Account Menu ===");
            System.out.println("1) Change username");
            System.out.println("2) Change password");
            System.out.println("3) Delete account");
            System.out.println("0) Return to user menu");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
	                case "1":
	                    if (this.gm.getCurrentUser() != null) {
	                        System.out.print("Enter new user name: ");
	                        String newUserName = scanner.nextLine().trim();
	                        if (newUserName.isEmpty()) {
	                            System.out.println("User name cannot be empty.");
	                            break;
	                        }
	                        this.gm.getCurrentUser().setUsername(newUserName);
	                        System.out.println("Your User Name has been changed into '" + newUserName + "' successfully");
	                    } else {
	                        System.out.println("No user logged in.");
	                    }
	                    break;

                    case "2":
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        this.gm.getCurrentUser().changePassword(newPassword);
                        System.out.println("Password changed successfully.");
                        break;
                    case "3":
                        System.out.print("Are you sure you want to delete your account? (yes/no): ");
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        if (confirm.equals("yes")) {
                        	this.gm.getCurrentUser().deleteAccount();
                            System.out.println("Account deleted. Logging out.");
                            this.gm.setCurrentCharacter(null);
                            this.gm.setCurrentUser(null);
                            backToUserMenu = true;
                        } else {
                            System.out.println("Account deletion cancelled.");
                        }
                        break;
                    case "0":
                        backToUserMenu = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (IOException | NoSuchAlgorithmException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleStartBattle() {
        if (this.gm.getCurrentCharacter() == null) {
            System.out.println("No character selected. Please create or select a character first.");
            return;
        }
        
        // Initialize battle system
        this.gm.setBattleSystem(new BattleSystem());
        this.gm.getBattleSystem().setPlayer(this.gm.getCurrentCharacter());
        this.gm.getBattleSystem().initializeBattle(this.gm.getCurrentCharacter());
        this.gm.getBattleSystem().startBattle();

        Combatant enemy = this.gm.getBattleSystem().getEnemy();
        
        // Battle loop
        while (!this.gm.getBattleSystem().isBattleOver()) {
            System.out.println("Your turn.");
            System.out.println(this.gm.getCurrentCharacter().getCharacterName() + "'s HP: " + this.gm.getCurrentCharacter().getStatus().getHp() + "/" + this.gm.getCurrentCharacter().getStatus().getMaxHp() + " | " + this.gm.getCurrentCharacter().getCharacterName() + "'s AP: " + this.gm.getCurrentCharacter().getStatus().getAp() + "/" + this.gm.getCurrentCharacter().getStatus().getMaxAp());
            System.out.println(enemy.getName() + "'s HP: " + enemy.getStatus().getHp() + "/" + enemy.getStatus().getMaxHp());
            this.gm.getBattleSystem().showPlayerHand();

            System.out.print("Enter card number to play or 'end' to end turn: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("end")) {
            	this.gm.getBattleSystem().endPlayerTurn();
            } else {
                try {
                    int cardIndex = Integer.parseInt(input) - 1;
                    this.gm.getBattleSystem().playCardFromHand(cardIndex, this.gm.getCurrentCharacter());
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Invalid input. Please enter a valid card number or 'end'.");
                }
            }
        }

        if (this.gm.getBattleSystem().isPlayerWinner()) { 
        	this.gm.getBattleSystem().awardRewards(this.gm.getCurrentCharacter()); 
        }
        this.gm.getCurrentCharacter().addExp(1000);
        this.gm.getBattleSystem().restoreAll(this.gm.getCurrentCharacter(), enemy);
    }

    private void handleShopMenu() {
        if (this.gm.getCurrentCharacter() == null) {
            System.out.println("No character selected. Please create or select a character first.");
            return;
        }

        Inventory<Card> playerInventory = this.gm.getCurrentCharacter().getInventory();

        boolean exitShop = false;

        while (!exitShop) {
            // Display Shop Menu Header and Character Status
            System.out.println("\n=== Shop Menu ===");
            System.out.println("Selected Character: " + 
                (this.gm.getCurrentCharacter().getCharacterName() != null 
                    ? this.gm.getCurrentCharacter().getCharacterName() 
                    : "None"));

            System.out.println("Level: " + this.gm.getCurrentCharacter().getStatus().getLevel() + 
                " | Exp: " + this.gm.getCurrentCharacter().getStatus().getExp() + "/" + this.gm.getCurrentCharacter().getXpThresholdForNextLevel() + 
                " | Gold: " + this.gm.getCurrentCharacter().getStatus().getGold());

            System.out.println("HP: " + this.gm.getCurrentCharacter().getStatus().getHp() + "/" + this.gm.getCurrentCharacter().getStatus().getMaxHp() + 
                " | AP: " + this.gm.getCurrentCharacter().getStatus().getAp() + "/" + this.gm.getCurrentCharacter().getStatus().getMaxAp());

            // Display Shop Options
            System.out.println("1) Buy Cards");
            System.out.println("2) Sell Cards");
            System.out.println("3) Gacha Draw");
            System.out.println("0) Return to user menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0":
                    exitShop = true;
                    break;

                case "1": // Buy Cards
                    if (this.gm.getShop().getCardInventory().getItems().isEmpty()) {
                        System.out.println("Shop has no cards available for purchase.");
                        break;
                    }

                    System.out.println("Available cards to buy:");
                    int index = 1;
                    for (Card card : this.gm.getShop().getCardInventory().getItems()) {
                        System.out.println(index + ") " + card.getCardName() + " (Price: " + card.getPrice() + " Gold)");
                        index++;
                    }

                    System.out.print("Enter the number of the card to buy or 0 to cancel: ");
                    String buyInput = scanner.nextLine().trim();

                    try {
                        int buyChoice = Integer.parseInt(buyInput);

                        if (buyChoice == 0) {
                            System.out.println("Cancelled buying.");
                            break;
                        }

                        if (buyChoice < 1 || buyChoice > this.gm.getShop().getCardInventory().getItems().size()) {
                            System.out.println("Invalid selection.");
                            break;
                        }

                        Card selectedCard = this.gm.getShop().getCardInventory().getItems().get(buyChoice - 1);
                        int cardPrice = selectedCard.getPrice();

                        if (this.gm.getCurrentCharacter().getStatus().getGold() < cardPrice) {
                            System.out.println("Not enough gold to buy this card.");
                            break;
                        }

                        // Use the shop's buyCard method which handles gold deduction and inventory update
                        boolean purchaseSuccess = this.gm.getShop().buyCard(selectedCard, playerInventory, this.gm.getCurrentCharacter());
                        if (!purchaseSuccess) {
                            System.out.println("Purchase failed.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                    break;

                case "2": // Sell Cards
                    if (playerInventory.getItems().isEmpty()) {
                        System.out.println("You have no cards to sell.");
                        break;
                    }

                    System.out.println("Your cards:");
                    index = 1;
                    for (Card card : playerInventory.getItems()) {
                        System.out.println(index + ") " + card.getCardName() + " (Sell Price: " + (card.getPrice() / 2) + " Gold)");
                        index++;
                    }

                    System.out.print("Enter the number of the card to sell or 0 to cancel: ");
                    String sellInput = scanner.nextLine().trim();

                    try {
                        int sellChoice = Integer.parseInt(sellInput);

                        if (sellChoice == 0) {
                            System.out.println("Cancelled selling.");
                            break;
                        }

                        if (sellChoice < 1 || sellChoice > playerInventory.getItems().size()) {
                            System.out.println("Invalid selection.");
                            break;
                        }

                        Card selectedCard = playerInventory.getItems().get(sellChoice - 1);

                        // Use the shop's sellCard method which handles gold addition and inventory update
                        boolean sellSuccess = this.gm.getShop().sellCard(selectedCard, playerInventory, this.gm.getCurrentCharacter());
                        if (!sellSuccess) {
                            System.out.println("Selling failed.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                    break;

                case "3": // Gacha Draw
                    final int gachaCost = 150;
                    int currentGold = this.gm.getCurrentCharacter().getStatus().getGold();

                    if (currentGold < gachaCost) {
                        System.out.println("You do not have enough gold for a Gacha Draw. (Cost: " + gachaCost + " gold)");
                        break;
                    }

                    // Deduct gold first
                    this.gm.getCurrentCharacter().deductGold(gachaCost);

                    // Perform the gacha draw
                    Card drawnCard = this.gm.getShop().gachaDraw();

                    if (drawnCard == null) {
                        // Shop is out of cards, refund gold
                    	this.gm.getCurrentCharacter().getStatus().setGold(currentGold);
                        System.out.println("Gacha draw failed: Shop is out of cards. Your gold has been refunded.");
                    } else {
                        // Add the drawn card to player's inventory
                        playerInventory.addItem(drawnCard);
                        System.out.println("You spent " + gachaCost + " gold and received: " + drawnCard.getCardName());
                        System.out.println("Remaining Gold: " + this.gm.getCurrentCharacter().getStatus().getGold());
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please select a valid menu number.");
                    break;
            }
        }
    }
}
