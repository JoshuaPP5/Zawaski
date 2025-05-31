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
	Inventory<Card> getInventory();
}