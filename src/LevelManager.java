import java.awt.*;
import java.util.ArrayList;

public class LevelManager {
    private static LevelManager instance;
    private ArrayList<Level> levels;
    private int currentLevelIndex;

    private LevelManager() {
        levels = new ArrayList<>();
        currentLevelIndex = 0;
    }

    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    public Level getCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex);
        }
        return null;
    }

    public void nextLevel() {
        currentLevelIndex++;
        if (currentLevelIndex >= levels.size()) {
            currentLevelIndex = 0; // Volver al primer nivel
        }
    }

    public void resetLevels() {
        currentLevelIndex = 0;
    }

    public void createDefaultLevels() {
        // Nivel 1
        Level level1 = new Level("Nivel 1", new Point(100, 400));
        level1.addPlatform(new Platform(0, 450, 800, 50, true));
        level1.addPlatform(new Platform(100, 350, 100, 20, false));
        level1.addPlatform(new Platform(300, 300, 100, 20, false));
        level1.addPlatform(new Platform(500, 250, 100, 20, false));
        
        // Añadir enemigos
        level1.addEnemy(new Enemy(200, 400, 30, 30));
        level1.addEnemy(new Enemy(400, 400, 30, 30));
        
        // Añadir coleccionables
        level1.addCollectible(new Collectible(150, 300, 20, 20, Collectible.CollectibleType.COIN, 100));
        level1.addCollectible(new Collectible(350, 250, 20, 20, Collectible.CollectibleType.COIN, 100));
        level1.addCollectible(new Collectible(550, 200, 20, 20, Collectible.CollectibleType.POWER_UP, 200));
        
        addLevel(level1);

        // Nivel 2
        Level level2 = new Level("Nivel 2", new Point(100, 400));
        level2.addPlatform(new Platform(0, 450, 800, 50, true));
        level2.addPlatform(new Platform(150, 350, 100, 20, false));
        level2.addPlatform(new Platform(350, 300, 100, 20, false));
        level2.addPlatform(new Platform(550, 250, 100, 20, false));
        level2.addPlatform(new Platform(250, 200, 100, 20, false));
        
        // Añadir enemigos
        level2.addEnemy(new Enemy(200, 400, 30, 30));
        level2.addEnemy(new Enemy(400, 400, 30, 30));
        level2.addEnemy(new Enemy(600, 400, 30, 30));
        
        // Añadir coleccionables
        level2.addCollectible(new Collectible(200, 300, 20, 20, Collectible.CollectibleType.COIN, 100));
        level2.addCollectible(new Collectible(400, 250, 20, 20, Collectible.CollectibleType.COIN, 100));
        level2.addCollectible(new Collectible(600, 200, 20, 20, Collectible.CollectibleType.HEALTH, 0));
        
        addLevel(level2);
    }

    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    public int getTotalLevels() {
        return levels.size();
    }
} 