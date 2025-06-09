package zawaski;

public abstract class GameEntity {
    protected int id;
    protected String name;

    public GameEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { 
    	return id; 
    }
    public void setId(int id) { 
    	this.id = id; 
    }

    public String getName() { 
    	return name; 
    }
    
    public void setName(String name) { 
    	this.name = name; 
    }

    public abstract Status getStatus();
    
    abstract void takeDamage(int amount);
    
    abstract void heal(int amount);
    
    abstract void maxHeal();
    
    abstract void restoreAP(int amount);
    
    abstract void maxAP();
    
    abstract boolean isAlive();
    
    abstract int getAttackPower();
    
    abstract int getExp();
}
