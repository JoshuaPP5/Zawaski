package zawaski;

public class Status {
    private int hp;
    private int ap;
    private int level;

    public Status() {
        this.hp = 100;
        this.ap = 50;
        this.level = 1;
    }

    public Status(int hp, int ap, int level) {
    	this.hp = hp;
        this.ap = ap;
        this.level = level;
	}

	public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getAp() { return ap; }
    public void setAp(int ap) { this.ap = ap; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    @Override
    public String toString() {
        return "HP: " + hp + ", AP: " + ap + ", Level: " + level;
    }
}