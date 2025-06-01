package zawaski;

import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;

public class Main {

    private static User currentUser = null;
    private static Character currentCharacter = null;
    private static BattleSystem battleSystem = new BattleSystem();
    private static Shop shop = new Shop();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
                    handleRegister(scanner);
                    break;
                case 2:
                    handleLogin(scanner);
                    break;
                case 3:
                	handleLeaderboard(scanner);
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

    private static void printMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1) Register new account");
        System.out.println("2) Login to existing account");
        System.out.println("3) Leaderboard");
        System.out.println("0) Exit game");
    }

    private static void handleRegister(Scanner scanner) {
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

    private static void handleLogin(Scanner scanner) {
        System.out.println("\n=== Login ===");
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User user = new User(username, password);

            if (user.login(password)) {
                currentUser = user;
                System.out.println("Login successful! Welcome, " + currentUser.getUsername() + ".");
                handlePostLoginMenu(scanner);
            } else {
                System.out.println("Login failed. Invalid username or password.");
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    private static void handleLeaderboard(Scanner scanner) {
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
    
    private static void handlePostLoginMenu(Scanner scanner) {
        boolean logout = false;
        while (!logout) {
        	System.out.println("\n=== User Menu ===");
        	System.out.println("Selected Character: " + 
        		    (currentCharacter != null && currentCharacter.getCharacterName() != null 
        		        ? currentCharacter.getCharacterName() 
        		        : "None"));
        	if (currentCharacter != null) {
        		System.out.println("Level: " + currentCharacter.getStatus().getLevel() + " | Exp: " + currentCharacter.getStatus().getExp() + "/" + currentCharacter.getXpThresholdForNextLevel() + " | Gold: " + currentCharacter.getStatus().getGold());
        		System.out.println("HP: " + currentCharacter.getStatus().getHp() + "/" + currentCharacter.getStatus().getMaxHp() + " | AP: " + currentCharacter.getStatus().getAp() + "/" + currentCharacter.getStatus().getMaxAp());
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
	            	currentUser = null;
	            	currentCharacter = null;
	            	logout = true;
	            	break;
                case "1":
                    handleManageAccountMenu(scanner);
                    break;
                case "2":
                    handleManageCharactersMenu(scanner);
                    break;
                case "3":
                    handleStartBattle(scanner);
                    break;
                case "4":
                	if (currentCharacter != null) {
                		battleSystem.printPlayerInventory();
                	} else {
                		System.out.println("No character selected. Please create or select a character first.");
                	}
                    break;
                case "5":
                    handleShopMenu(scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleManageCharactersMenu(Scanner scanner) {
        boolean backToUserMenu = false;
        while (!backToUserMenu) {
        	System.out.println("\n=== Characters Menu ===");
        	System.out.println("Selected Character: " + 
        		    (currentCharacter != null && currentCharacter.getCharacterName() != null 
        		        ? currentCharacter.getCharacterName() 
        		        : "None"));
        	if (currentCharacter != null) {
        		System.out.println("Level: " + currentCharacter.getStatus().getLevel() + " | Exp: " + currentCharacter.getStatus().getExp() + "/" + currentCharacter.getXpThresholdForNextLevel() + " | Gold: " + currentCharacter.getStatus().getGold());
        		System.out.println("HP: " + currentCharacter.getStatus().getHp() + "/" + currentCharacter.getStatus().getMaxHp() + " | AP: " + currentCharacter.getStatus().getAp() + "/" + currentCharacter.getStatus().getMaxAp());
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
	            	if (currentUser != null) {
	                    // Fetch all characters for the current user
	                    List<Character> userCharacters = Character.loadCharactersByUser(currentUser.getUsername());

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
	                            currentCharacter = userCharacters.get(selectedIndex);
	                            System.out.println("Character '" + currentCharacter.getCharacterName() + "' selected.");
	                            battleSystem.setPlayer(currentCharacter);
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
	                if (currentUser != null) {
	                    // Check how many characters the user already has
	                    List<Character> userCharacters = Character.loadCharactersByUser(currentUser.getUsername());
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
	                    Character newCharacter = new Character(newId, newCharName, currentUser.getUsername());
	                    currentCharacter = newCharacter;
	                    currentCharacter.addStartingCard();
	                    battleSystem.setPlayer(currentCharacter);
	                    System.out.println("Character '" + newCharName + "' created successfully with ID " + newId + ".");
	                } else {
	                    System.out.println("No user logged in.");
	                }
	                break;
                case "3":
                    if (currentCharacter != null) {
                        System.out.print("Enter new name for character '" + currentCharacter.getCharacterName() + "': ");
                        String updatedName = scanner.nextLine().trim();
                        if (updatedName.isEmpty()) {
                            System.out.println("Character name cannot be empty.");
                            break;
                        }
                        currentCharacter.setCharacterName(updatedName);
                        System.out.println("Character name changed successfully to '" + updatedName + "'.");
                    } else {
                        System.out.println("No character selected. Please create or select a character first.");
                    }
                    break;
                case "4":
                    if (currentCharacter != null) {
                        System.out.print("Are you sure you want to delete character '" + currentCharacter.getCharacterName() + "'? (yes/no): ");
                        String confirmDelete = scanner.nextLine().trim().toLowerCase();
                        if (confirmDelete.equals("yes")) {
                            currentCharacter.deleteCharacter();
                            System.out.println("Character '" + currentCharacter.getCharacterName() + "' deleted successfully.");
                            currentCharacter = null;
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

    private static void handleManageAccountMenu(Scanner scanner) {
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
	                    if (currentUser != null) {
	                        if (currentUser.getCharacters().size() >= 3) {
	                            System.out.println("You can't have more than 3 characters.");
	                            break;
	                        }
	                        System.out.print("Enter new character name: ");
	                        String newCharName = scanner.nextLine().trim();
	                        if (newCharName.isEmpty()) {
	                            System.out.println("Character name cannot be empty.");
	                            break;
	                        }
	                        int newId = Character.generateNewId();
	                        Character newCharacter = new Character(newId, newCharName, currentUser.getUsername());
	                        currentCharacter = newCharacter;
	                        System.out.println("Character '" + newCharName + "' created successfully with ID " + newId + ".");
	                    } else {
	                        System.out.println("No user logged in.");
	                    }
	                    break;

                    case "2":
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        currentUser.changePassword(newPassword);
                        System.out.println("Password changed successfully.");
                        break;
                    case "3":
                        System.out.print("Are you sure you want to delete your account? (yes/no): ");
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        if (confirm.equals("yes")) {
                            currentUser.deleteAccount();
                            System.out.println("Account deleted. Logging out.");
                            currentUser = null;
                            currentCharacter = null;
                            backToUserMenu = true; // Exit manage account menu
                            return; // Exit immediately to logout
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

    private static void handleStartBattle(Scanner scanner) {
        if (currentCharacter == null) {
            System.out.println("No character selected. Please create or select a character first.");
            return;
        }
        
        // Initialize battle system
        battleSystem = new BattleSystem();
        battleSystem.setPlayer(currentCharacter);
        battleSystem.initializeBattle(currentCharacter);
        battleSystem.startBattle();

        Combatant enemy = battleSystem.getEnemy();
        
        // Battle loop
        while (!battleSystem.isBattleOver()) {
            System.out.println("Your turn.");
            System.out.println(currentCharacter.getCharacterName() + "'s HP: " + currentCharacter.getStatus().getHp() + "/" + currentCharacter.getStatus().getMaxHp() + " | " + currentCharacter.getCharacterName() + "'s AP: " + currentCharacter.getStatus().getAp() + "/" + currentCharacter.getStatus().getMaxAp());
            System.out.println(enemy.getName() + "'s HP: " + enemy.getStatus().getHp() + "/" + enemy.getStatus().getMaxHp());
            battleSystem.showPlayerHand();

            System.out.print("Enter card number to play or 'end' to end turn: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("end")) {
                battleSystem.endPlayerTurn();
            } else {
                try {
                    int cardIndex = Integer.parseInt(input) - 1;
                    battleSystem.playCardFromHand(cardIndex, currentCharacter);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Invalid input. Please enter a valid card number or 'end'.");
                }
            }
        }

        if (battleSystem.isPlayerWinner()) { battleSystem.awardRewards(currentCharacter); }
        currentCharacter.addExp(1000);
        battleSystem.restoreAll(currentCharacter, enemy);
    }

    private static void handleShopMenu(Scanner scanner) {
        if (currentCharacter == null) {
            System.out.println("No character selected. Please create or select a character first.");
            return;
        }

        Inventory<Card> playerInventory = currentCharacter.getInventory();

        boolean exitShop = false;

        while (!exitShop) {
            // Display Shop Menu Header and Character Status
            System.out.println("\n=== Shop Menu ===");
            System.out.println("Selected Character: " + 
                (currentCharacter.getCharacterName() != null 
                    ? currentCharacter.getCharacterName() 
                    : "None"));

            System.out.println("Level: " + currentCharacter.getStatus().getLevel() + 
                " | Exp: " + currentCharacter.getStatus().getExp() + "/" + currentCharacter.getXpThresholdForNextLevel() + 
                " | Gold: " + currentCharacter.getStatus().getGold());

            System.out.println("HP: " + currentCharacter.getStatus().getHp() + "/" + currentCharacter.getStatus().getMaxHp() + 
                " | AP: " + currentCharacter.getStatus().getAp() + "/" + currentCharacter.getStatus().getMaxAp());

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
                    if (shop.getCardInventory().getItems().isEmpty()) {
                        System.out.println("Shop has no cards available for purchase.");
                        break;
                    }

                    System.out.println("Available cards to buy:");
                    int index = 1;
                    for (Card card : shop.getCardInventory().getItems()) {
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

                        if (buyChoice < 1 || buyChoice > shop.getCardInventory().getItems().size()) {
                            System.out.println("Invalid selection.");
                            break;
                        }

                        Card selectedCard = shop.getCardInventory().getItems().get(buyChoice - 1);
                        int cardPrice = selectedCard.getPrice();

                        if (currentCharacter.getStatus().getGold() < cardPrice) {
                            System.out.println("Not enough gold to buy this card.");
                            break;
                        }

                        // Use the shop's buyCard method which handles gold deduction and inventory update
                        boolean purchaseSuccess = shop.buyCard(selectedCard, playerInventory, currentCharacter);
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
                        boolean sellSuccess = shop.sellCard(selectedCard, playerInventory, currentCharacter);
                        if (!sellSuccess) {
                            System.out.println("Selling failed.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                    break;

                case "3": // Gacha Draw
                    final int gachaCost = 150;
                    int currentGold = currentCharacter.getStatus().getGold();

                    if (currentGold < gachaCost) {
                        System.out.println("You do not have enough gold for a Gacha Draw. (Cost: " + gachaCost + " gold)");
                        break;
                    }

                    // Deduct gold first
                    currentCharacter.deductGold(gachaCost);

                    // Perform the gacha draw
                    Card drawnCard = shop.gachaDraw();

                    if (drawnCard == null) {
                        // Shop is out of cards, refund gold
                        currentCharacter.getStatus().setGold(currentGold);
                        System.out.println("Gacha draw failed: Shop is out of cards. Your gold has been refunded.");
                    } else {
                        // Add the drawn card to player's inventory
                        playerInventory.addItem(drawnCard);
                        System.out.println("You spent " + gachaCost + " gold and received: " + drawnCard.getCardName());
                        System.out.println("Remaining Gold: " + currentCharacter.getStatus().getGold());
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please select a valid menu number.");
                    break;
            }
        }
    }

}