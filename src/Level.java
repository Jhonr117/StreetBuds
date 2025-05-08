import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {
    private String name;
    private BufferedImage background;
    private ArrayList<Platform> platforms;
    private ArrayList<Enemy> enemies;
    private ArrayList<Collectible> collectibles;
    private Point playerSpawnPoint;
    private int timeLimit;
    private int scoreToComplete;

    public Level(String name, Point playerSpawnPoint) {
        this.name = name;
        this.playerSpawnPoint = playerSpawnPoint;
        this.platforms = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.collectibles = new ArrayList<>();
        this.timeLimit = 300; // 5 minutos por defecto
        this.scoreToComplete = 1000;
    }

    public void addPlatform(Platform platform) {
        platforms.add(platform);
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void addCollectible(Collectible collectible) {
        collectibles.add(collectible);
    }

    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Collectible> getCollectibles() {
        return collectibles;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public Point getPlayerSpawnPoint() {
        return playerSpawnPoint;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getScoreToComplete() {
        return scoreToComplete;
    }

    public void setScoreToComplete(int score) {
        this.scoreToComplete = score;
    }

    public String getName() {
        return name;
    }

    public void update() {
        for (Enemy enemy : enemies) {
            enemy.update();
        }
    }

    public void draw(Graphics2D g2d) {
        if (background != null) {
            g2d.drawImage(background, 0, 0, null);
        }

        for (Platform platform : platforms) {
            platform.draw(g2d);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
        }

        for (Collectible collectible : collectibles) {
            collectible.draw(g2d);
        }
    }
} 