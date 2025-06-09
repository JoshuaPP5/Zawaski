package zawaski;

public class Enemy extends GameEntity {
    private int level;
    private Status status;
    private int attackPower;
    private int exp;  // Experience dropped by the enemy
    private int gold; // Gold dropped by the enemy

 // Updated constructor with id, name, level, maxHp, maxAp, attackPower, exp, and gold parameters
    public Enemy(int id, String name, int level, int maxHp, int maxAp, int attackPower, int exp, int gold) {
        super(id, name);  // Call to GameEntity constructor
        this.level = level;
        this.status = new Status(maxHp, maxAp);
        this.attackPower = attackPower;
        this.exp = exp;
        this.gold = gold;
    }

    public int getExp() {
        return this.exp;
    }

    public int getGold() {
        return this.gold;
    }
    
    // Getter for enemy type
    public String getName() {
        return name;
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
}
