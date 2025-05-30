package zawaski;

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
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleManageCharactersMenu(Scanner scanner) {
        boolean backToUserMenu = false;
        while (!backToUserMenu) {
            System.out.println("\nManage Characters:");
            System.out.println("1) Create new character");
            System.out.println("2) Change character's name");
            System.out.println("3) Delete character");
            System.out.println("4) Return to user menu");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (currentUser != null) {
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
                case "3":
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
                case "4":
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
        if (currentUser == null) {
            System.out.println("You must be logged in to start a battle.");
            return;
        }
        if (currentCharacter == null) {
            System.out.println("You must create or load a character before starting a battle.");
            return;
        }
        System.out.println("\n--- Starting Battle ---");
        battleSystem.startBattle();

        boolean battleOver = false;
        while (!battleOver) {
            System.out.println("\nBattle Menu:");
            System.out.println("1) Play a card");
            System.out.println("2) End turn");
            System.out.println("3) Quit battle");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter card name to play: ");
                    String cardName = scanner.nextLine().trim();
                    // For demo, create a dummy card with the given name and fixed AP cost
                    Card card = new Card(cardName, 1) {
                        @Override
                        public void effect(Character character, BattleSystem battleSystem) {
                            System.out.println("Effect of card '" + cardName + "' applied to character: " + character.getCharacterName());
                            // You can add more effect logic here
                        }
                    };
                    // Call playCard with both card and currentCharacter
                    battleSystem.playCard(card, currentCharacter);
                    break;
                case "2":
                    battleSystem.endTurn();
                    break;
                case "3":
                    System.out.println("Exiting battle...");
                    battleOver = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}