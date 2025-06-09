package zawaski;

public class GameView {
	public static void displayWelcomeMessage() {
		System.out.println("===================================");
        System.out.println(" Welcome to the Zawaski Card Game! ");
        System.out.println("===================================\n");
	}
	
	public static void displayPreLoginMenu() {
		System.out.println("\n=== Main Menu ===");
        System.out.println("1) Register new account");
        System.out.println("2) Login to existing account");
        System.out.println("3) Leaderboard");
        System.out.println("0) Exit game");
        System.out.print("Select an option: ");
	}
	
	public static void displayPostLoginMenu() {
		System.out.println("1) Manage account");
        System.out.println("2) Manage characters");
        System.out.println("3) Start battle");
        System.out.println("4) Inventory");
        System.out.println("5) Shop");
        System.out.println("0) Logout");
        System.out.print("Select an option: ");
	}
	
	public static void displayManageCharacterMenu() {
		System.out.println("1) Select character");
        System.out.println("2) Create new character");
        System.out.println("3) Change character's name");
        System.out.println("4) Delete character");
        System.out.println("0) Return to user menu");
        System.out.print("Select an option: ");
	}
	
	public static void displayManageAccountMenu() {
        System.out.println("1) Change username");
        System.out.println("2) Change password");
        System.out.println("3) Delete account");
        System.out.println("0) Return to user menu");
        System.out.print("Select an option: ");
	}
}
