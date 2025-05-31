package zawaski;

public interface Combatant {
    Status getStatus();
    void takeDamage(int amount);
    void heal(int amount);
    void restoreAP(int amount);
    String getName();

    boolean isAlive();
    int getAttackPower();
}