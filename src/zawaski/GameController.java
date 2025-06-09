package zawaski;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class GameController {
	private final GameModel gameModel;
	private final ShopController shopController;
	private final LeaderboardController leaderboardController;
	private final CharacterMenuController characterMenuController;
	private final AccountMenuController accountMenuController;
	private final BattleSystemController battleSystemController;
    
	private final Scanner scanner;
	
	private boolean isExit = false;
	private boolean isAccountMenu = false;;
	private boolean isCharacterMenu = false;

	public GameController() {
		this.gameModel = new GameModel();		
		this.scanner = new Scanner(System.in);
		
		this.leaderboardController = new LeaderboardController(this.scanner);
		this.shopController = new ShopController(this.gameModel, this.scanner);
		this.characterMenuController = new CharacterMenuController(this.gameModel, this.scanner);
		this.accountMenuController = new AccountMenuController(this.gameModel, this.scanner);
		this.battleSystemController = new BattleSystemController(this.gameModel.getBattleSystem(), this.scanner);
	}
	
	public void start() {
		GameView.displayWelcomeMessage();

        while (!isExit) {
        	if(isAccountMenu == true) {
        		handleManageAccountMenu();
        	} else if(isCharacterMenu == true) {
        		handleManageCharacterMenu();
        	} else if(this.gameModel.getCurrentUser() != null) {
        		handlePostLoginMenu();
        	} else {
        		handlePreLoginMenu();
        	}
        }
        scanner.close();
	}
	
	private void handlePreLoginMenu() {
		GameView.displayPreLoginMenu();
    	
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                handleRegister();
                break;
            case "2":
                handleLogin();
                break;
            case "3":
            	handleLeaderboard();
            	break;
            case "0":
                System.out.println("Exiting game. Goodbye!");
                isExit = true;
                break;
            default:
                System.out.println("Invalid option. Please try again.\n");
        }
	}

	private void handleLogin() {
        System.out.println("\n=== Login ===");
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User user = new User(username, password);

            if (user.login(password) != null) {
                this.gameModel.setCurrentUser(user);
                System.out.println("Login successful! Welcome, " + this.gameModel.getCurrentUser().getUsername() + ".");
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
        leaderboardController.handleLeaderboardMenu();
    }
    
    private void handlePostLoginMenu() {
    	System.out.println("\n=== User Menu ===");
    	
    	CharacterView.displaySelectedCharacterInformation(this.gameModel.getCurrentCharacter());
    	GameView.displayPostLoginMenu();
    	
        
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "0":
            	handleLogout();
            	break;
            case "1":
            	isAccountMenu = true;
                break;
            case "2":
            	isCharacterMenu = true;
                break;
            case "3":
                handleStartBattle();
                break;
            case "4":
            	handleInventory();
                break;
            case "5":
                handleShopMenu();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void handleManageCharacterMenu() {
    	System.out.println("\n=== Characters Menu ===");
    	CharacterView.displaySelectedCharacterInformation(this.gameModel.getCurrentCharacter());
    	
        GameView.displayManageCharacterMenu();
        
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
            	characterMenuController.selectCharacter();
                break;
            case "2":
                characterMenuController.createNewCharacter();
                break;
            case "3":
                characterMenuController.changeCharacterName();
                break;
            case "4":
                characterMenuController.deleteCharacter();
                break;
            case "0":
            	isCharacterMenu = false;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void handleManageAccountMenu() {
		System.out.println("\n=== Account Menu ===");
		
    	GameView.displayManageAccountMenu();
        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1":
                    accountMenuController.changeUsername();
                    break;
                case "2":
                    accountMenuController.changePassword();
                    break;
                case "3":
                    accountMenuController.deleteAccount();
                    isAccountMenu = false;
                    break;
                case "0":
                	isAccountMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (IOException | NoSuchAlgorithmException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleStartBattle() {
    	if (!this.isCharacterSelected()) {
            return;
        }

		// Initialize battle system
        this.gameModel.getBattleSystem().initializeBattle(this.gameModel.getCurrentCharacter());
        battleSystemController.startBattle();
    }

    private void handleShopMenu() {
        if (!this.isCharacterSelected()) {
            return;
        }

        shopController.handleShopMenu();
    }
    
    private void handleInventory() {
    	if (!this.isCharacterSelected()) {
            return;
        }
    	
		this.gameModel.getBattleSystem().printPlayerInventory();
    }
    
    private void handleLogout() {
    	System.out.println("Logging out...");
    	
    	this.gameModel.setCurrentCharacter(null);
    	this.gameModel.setCurrentUser(null);
    }
    
    private boolean isCharacterSelected() {
		if (gameModel.getCurrentCharacter() == null) {
            System.out.println("No character selected. Please create or select a character first.");
            return false;
        }
        return true;
	}
}
