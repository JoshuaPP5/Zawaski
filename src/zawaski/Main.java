package zawaski;

import java.util.List;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;

public class Main {

    private static User currentUser = null;
    private static Character currentCharacter = null;
    private static BattleSystem battleSystem = new BattleSystem();

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
        System.out.println("\nMain Menu:");
        System.out.println("1) Register new account");
        System.out.println("2) Login to existing account");
        System.out.println("3) Exit game");
    }

    private static void handleRegister(Scanner scanner) {
        System.out.println("\n--- Register New Account ---");
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
        System.out.println("\n--- Login ---");
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

    private static void handlePostLoginMenu(Scanner scanner) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\nUser Menu:");
            System.out.println("1) Manage account");
            System.out.println("2) Manage characters");
            System.out.println("3) Start battle");
            System.out.println("4) Logout");
            System.out.println("5) Inventory");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
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
                    System.out.println("Logging out...");
                    currentUser = null;
                    currentCharacter = null;
                    logout = true;
                    break;
                case "5":
                    battleSystem.printPlayerInventory();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleManageCharactersMenu(Scanner scanner) {
        boolean backToUserMenu = false;
        while (!backToUserMenu) {
            System.out.println("\nManage Characters:");
            System.out.println("1) Select character");
            System.out.println("2) Create new character");
            System.out.println("3) Change character's name");
            System.out.println("4) Delete character");
            System.out.println("5) Return to user menu");
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
                case "5":
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
            System.out.println("\nManage Account:");
            System.out.println("1) Change username");
            System.out.println("2) Change password");
            System.out.println("3) Delete account");
            System.out.println("4) Return to user menu");
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
                    case "4":
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
        
        Enemy enemy = new Enemy(1, "Goblin", 1, 100, 50, 25);

        // Initialize battle system
        battleSystem = new BattleSystem();
        battleSystem.populatePlayerInventory();
        battleSystem.initializeBattle(currentCharacter, enemy);
        battleSystem.startBattle();

        System.out.println("Battle started between " + currentCharacter.getCharacterName() + " and " + enemy.getEnemyType() + "!");

        // Battle loop
        while (!battleSystem.isBattleOver()) {
            System.out.println("\nYour turn. " + "Current HP: " + currentCharacter.getStatus().getHp() + "/" + currentCharacter.getStatus().getMaxHp() + " | Current AP: " + currentCharacter.getStatus().getAp() + "/" + currentCharacter.getStatus().getMaxAp());
            System.out.println("Enemy HP: " + enemy.getStatus().getHp() + "/" + enemy.getStatus().getMaxHp() + " | Enemy AP: " + enemy.getStatus().getAp() + "/" + enemy.getStatus().getMaxAp());
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

        System.out.println("Battle ended. " + (battleSystem.isPlayerWinner() ? "You won!" : "You lost."));
    }

}