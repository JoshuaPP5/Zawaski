package zawaski;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class AccountMenuController {
	private final GameModel gameModel;
	private final Scanner scanner;
	
	public AccountMenuController(GameModel gameModel, Scanner scanner) {
		this.scanner = scanner;
		this.gameModel = gameModel;
	}
	
	public void changeUsername() throws NoSuchAlgorithmException, IOException{
		if (!this.isUserLoggedIn()) {
            return;
		}
		
        System.out.print("Enter new user name: ");
        String newUsername = scanner.nextLine().trim();
        if (newUsername.isEmpty()) {
            System.out.println("User name cannot be empty.");
            return;
        }
        this.gameModel.getCurrentUser().changeUsername(newUsername);
        System.out.println("Your User Name has been changed into '" + newUsername + "' successfully");
	}
	
	public void changePassword() throws NoSuchAlgorithmException, IOException{
		System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        this.gameModel.getCurrentUser().changePassword(newPassword);
        System.out.println("Password changed successfully.");
	}
	
	public void deleteAccount() throws IOException {
		System.out.print("Are you sure you want to delete your account? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes")) {
        	this.gameModel.getCurrentUser().deleteAccount();
            System.out.println("Account deleted. Logging out.");
            this.gameModel.setCurrentCharacter(null);
            this.gameModel.setCurrentUser(null);
        } else {
            System.out.println("Account deletion cancelled.");
        }
	}
	
	private boolean isUserLoggedIn() {
        if (gameModel.getCurrentUser() == null) {
            System.out.println("No user logged in.");
            return false;
        }
        return true;
    }
}
