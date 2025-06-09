package zawaski;

public class Status {
	private int hp;
    private int maxHp;
    private int ap;
    private int maxAp;
    private int level;
    private int exp;
    private int gold;

    private static final int MAX_HP = 100;
    private static final int MAX_AP = 50;


    public Status() {
    	this(MAX_HP, MAX_AP);
    }

    public Status(int maxHp, int maxAp) {
    	this(maxHp, maxHp, maxAp, maxAp, 1, 0, 0);
    }


    public Status(int hp, int maxHp, int ap, int maxAp, int level, int exp, int gold) {
        this.hp = hp;
        this.maxHp = maxHp;
        this.ap = ap;
        this.maxAp = maxAp;
        this.level = level;
        this.exp = exp;
        this.gold = gold;
    }


    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }


    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
        }
    }


    public boolean deductGold(int amount) {
        if (amount > 0 && this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }
    
    public int getHp() { 
    	return hp; 
    }
    
    public void setHp(int hp) { 
    	this.hp = Math.min(hp, maxHp); 
    }

    public int getMaxHp() { 
    	return maxHp; 
    }
    
    public void setMaxHp(int maxHp) { 
    	this.maxHp = maxHp; 
    }
    
    public int getAp() { 
    	return ap; 
    }
    
    public void setAp(int ap) { 
    	this.ap = Math.min(ap, maxAp);
    }
    
    public int getMaxAp() { 
    	return maxAp; 
    }
    
    public void setMaxAp(int maxAp) { 
    	this.maxAp = maxAp; 
    }


    public int getLevel() { 
    	return level; 
    }


    public void setLevel(int level) {
        if (level < 1) {
            this.level = 1;
        } else {
            this.level = level;
        }
    }


    public int getExp() { 
    	return exp; 
    }


    public void setExp(int exp) {
        if (exp < 0) {
            this.exp = 0;
        } else {
            this.exp = exp;
        }
    }

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