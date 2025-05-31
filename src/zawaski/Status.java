package zawaski;

public class Status {
    private int hp;
    private int ap;
    private int level;

    private static final int MAX_HP = 100;
    private static final int MAX_AP = 50;

    // Default constructor sets HP and AP to max, level to 1
    public Status() {
        this.hp = MAX_HP;
        this.ap = MAX_AP;
        this.level = 1;
    }

    // New constructor accepting hp and ap, sets level to 1 by default
    public Status(int hp, int ap) {
        this(hp, ap, 1);  // Calls the three-parameter constructor
    }
    
    // Constructor with hp, ap, and level parameters
    public Status(int hp, int ap, int level) {
        setHp(hp);
        setAp(ap);
        this.level = level;
    }

    // Getter for HP
    public int getHp() { return hp; }

    // Setter for HP with clamping
    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else if (hp > MAX_HP) {
            this.hp = MAX_HP;
        } else {
            this.hp = hp;
        }
    }

    // Getter for AP
    public int getAp() { return ap; }

    // Setter for AP with clamping
    public void setAp(int ap) {
        if (ap < 0) {
            this.ap = 0;
        } else if (ap > MAX_AP) {
            this.ap = MAX_AP;
        } else {
            this.ap = ap;
        }
    }

    // Getter for Level
    public int getLevel() { return level; }

    // Setter for Level
    public void setLevel(int level) { this.level = level; }

    // New getter for maximum HP
    public int getMaxHp() {
        return MAX_HP + (level - 1) * 10; // Example scaling
    }

    public int getMaxAp() {
        return MAX_AP + (level - 1) * 5;  // Example scaling
    }

    @Override
    public String toString() {
        return "HP: " + hp + ", AP: " + ap + ", Level: " + level;
    }
}
