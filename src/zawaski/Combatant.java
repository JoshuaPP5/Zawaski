package zawaski;

public interface Combatant {
    Status getStatus();
    void takeDamage(int amount);
    void heal(int amount);
    void maxHeal();
    void restoreAP(int amount);
    void maxAP();
    String getName();
    boolean isAlive();
    int getAttackPower();
    int getExp();
    void addExp(int amount);
    int getGold();
    void addGold(int amount);
	Inventory<Card> getInventory();
}