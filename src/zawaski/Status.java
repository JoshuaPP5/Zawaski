package zawaski;

public class Status {
    private int hp;
    private int ap;
    private int level;
    private int exp;
    private int gold;

    private static final int MAX_HP = 100;
    private static final int MAX_AP = 50;

    /**
     * Default constructor initializes HP and AP to max, level to 1, and exp to 0.
     */
    public Status() {
        this.hp = MAX_HP;
        this.ap = MAX_AP;
        this.level = 1;
        this.exp = 0;
        this.gold = 0;
    }

    /**
     * Constructor with HP and AP, level defaults to 1, exp to 0.
     */
    public Status(int hp, int ap) {
        this(hp, ap, 1, 0, 0);
    }

    /**
     * Constructor with HP, AP, Level, exp, an gold.
     */
    public Status(int hp, int ap, int level, int exp, int gold) {
        setHp(hp);
        setAp(ap);
        setLevel(level);
        setExp(exp);
        setGold(gold);
    }

    // Getter and setter for gold
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    // Add gold
    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
        }
    }

    // Deduct gold, return true if successful
    public boolean deductGold(int amount) {
        if (amount > 0 && this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }
    
    // Getter for HP
    public int getHp() {
        return hp;
    }

    // Setter for HP with clamping between 0 and max HP
    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else if (hp > getMaxHp()) {
            this.hp = getMaxHp();
        } else {
            this.hp = hp;
        }
    }

    // Getter for AP
    public int getAp() {
        return ap;
    }

    // Setter for AP with clamping between 0 and max AP
    public void setAp(int ap) {
        if (ap < 0) {
            this.ap = 0;
        } else if (ap > getMaxAp()) {
            this.ap = getMaxAp();
        } else {
            this.ap = ap;
        }
    }

    // Getter for Level
    public int getLevel() {
        return level;
    }

    // Setter for Level
    public void setLevel(int level) {
        if (level < 1) {
            this.level = 1;
        } else {
            this.level = level;
        }
    }

    // Getter for exp (XP)
    public int getExp() {
        return exp;
    }

    // Setter for exp (XP)
    public void setExp(int exp) {
        if (exp < 0) {
            this.exp = 0;
        } else {
            this.exp = exp;
        }
    }

    /**
     * Calculates the maximum HP based on the current level.
     * Example scaling: +10 HP per level above 1.
     */
    public int getMaxHp() {
        return MAX_HP + (level - 1) * 10;
    }

    /**
     * Calculates the maximum AP based on the current level.
     * Example scaling: +5 AP per level above 1.
     */
    public int getMaxAp() {
        return MAX_AP + (level - 1) * 5;
    }

    /**
     * Calculates the XP threshold required to level up.
     * Example formula: 100 XP * current level.
     * You can customize this formula as needed.
     */
    public int getXpThreshold() {
        return 100 * level;
    }

    @Override
    public String toString() {
        return "HP: " + hp + "/" + getMaxHp() +
               ", AP: " + ap + "/" + getMaxAp() +
               ", Level: " + level +
               ", XP: " + exp + "/" + getXpThreshold();
    }
}