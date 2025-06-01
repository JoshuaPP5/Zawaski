package zawaski;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyManager {
    private List<Enemy> enemies;
    private Random random;

    public EnemyManager() {
        this.enemies = new ArrayList<>();
        this.random = new Random();
    }

    // Add an enemy to the list
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    // Remove an enemy from the list
    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    // Get all enemies
    public List<Enemy> getAllEnemies() {
        return new ArrayList<>(enemies); // Return a copy to avoid external modification
    }

    // Get a random enemy from the list
    public Enemy getRandomEnemy() {
        if (enemies.isEmpty()) {
            return null; // Or throw an exception if preferred
        }
        int index = random.nextInt(enemies.size());
        return enemies.get(index);
    }

    // Get multiple random enemies (without duplicates)
    public List<Enemy> getRandomEnemies(int count) {
        List<Enemy> copy = new ArrayList<>(enemies);
        List<Enemy> selected = new ArrayList<>();

        if (count >= enemies.size()) {
            return copy; // Return all if count exceeds available enemies
        }

        for (int i = 0; i < count; i++) {
            int index = random.nextInt(copy.size());
            selected.add(copy.remove(index));
        }
        return selected;
    }
}