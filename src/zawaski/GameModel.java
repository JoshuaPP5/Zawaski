package zawaski;

public class GameModel {
	private User currentUser;
    private Character currentCharacter;
    private BattleSystem battleSystem;
    private Shop shop;
    
	public GameModel() {
		this.shop = new Shop();
		this.currentCharacter = null;
		this.currentCharacter = null;
		this.battleSystem = new BattleSystem();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public Character getCurrentCharacter() {
		return currentCharacter;
	}

	public void setCurrentCharacter(Character currentCharacter) {
		this.currentCharacter = currentCharacter;
	}

	public BattleSystem getBattleSystem() {
		return battleSystem;
	}

	public void setBattleSystem(BattleSystem battleSystem) {
		this.battleSystem = battleSystem;
	}

	public Shop getShop() {
		return shop;
	}
}
