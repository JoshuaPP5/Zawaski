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


    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }


    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }


    public List<Enemy> getAllEnemies() {
        return new ArrayList<>(enemies);
    }


    public Enemy getRandomEnemy() {
        if (enemies.isEmpty()) {
            return null;
        }
        int index = random.nextInt(enemies.size());
        return enemies.get(index);
    }


    public List<Enemy> getRandomEnemies(int count) {
        List<Enemy> copy = new ArrayList<>(enemies);
        List<Enemy> selected = new ArrayList<>();

        if (count >= enemies.size()) {
            return copy;
        }

        for (int i = 0; i < count; i++) {
            int index = random.nextInt(copy.size());
            selected.add(copy.remove(index));
        }
        return selected;
    }
}