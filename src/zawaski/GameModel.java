package zawaski;

public class GameModel {
	private User currentUser;
    private CharacterModel currentCharacter;
    private BattleSystem battleSystem;
    private ShopModel shop;
    
	public GameModel() {
		this.shop = new ShopModel();
		this.currentCharacter = null;
		this.currentUser = null;
		this.battleSystem = new BattleSystem();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public CharacterModel getCurrentCharacter() {
		return currentCharacter;
	}

	public void setCurrentCharacter(CharacterModel currentCharacter) {
		this.currentCharacter = currentCharacter;
	}

	public BattleSystem getBattleSystem() {
		return battleSystem;
	}

	public void setBattleSystem(BattleSystem battleSystem) {
		this.battleSystem = battleSystem;
	}

	public ShopModel getShop() {
		return shop;
	}
}
