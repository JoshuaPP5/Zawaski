package zawaski;

public class Enemy extends GameEntity implements Combatant {
    private String enemyType;
    private int level;
    private Status status;
    private int attackPower;

    // Updated constructor with id and name parameters
    public Enemy(int id, String enemyType, int level, int maxHp, int maxAp, int attackPower) {
        super(id, enemyType);  // Call to GameEntity constructor
        this.enemyType = enemyType;
        this.level = level;
        this.status = new Status(maxHp, maxAp);
        this.attackPower = attackPower;
    }

    // Getter for enemy type
    public String getEnemyType() {
        return enemyType;
    }

    // Getter for level
    public int getLevel() {
        return level;
    }

    // Implement the abstract method from GameEntity
    @Override
    public Status getStatus() {
        return this.status;
    }

    // Helper method to take damage with validation
    public void takeDamage(int amount) {
        int newHp = status.getHp() - amount;
        status.setHp(Math.max(newHp, 0));  // Clamp HP to minimum 0
        // Optionally add logic for enemy death here
    }

    // Helper method to heal with validation
    public void heal(int amount) {
        int newHp = status.getHp() + amount;
        status.setHp(Math.min(newHp, status.getMaxHp()));  // Clamp HP to maxHp
    }

    public void maxHeal() {
        int maxHp = status.getMaxHp();
        status.setHp(maxHp);
    }
    
    // Helper method to restore AP with validation
    public void restoreAP(int amount) {
        int newAp = status.getAp() + amount;
        status.setAp(Math.min(newAp, status.getMaxAp()));  // Clamp AP to maxAp
    }

    public void maxAP() {
        int maxAp = status.getMaxAp();
        status.setAp(maxAp);
    }
    
	@Override
	public boolean isAlive() {
        return status.getHp() > 0;
    }

	@Override
	public int getAttackPower() {
        return attackPower;
    }

	@Override
	public Inventory<Card> getInventory() {
		return null;
	}
}
