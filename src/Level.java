import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Clase que representa un nivel del juego.
 * Contiene información sobre plataformas, enemigos, coleccionables, y otros elementos del nivel.
 */
public class Level {
    private String name; // Nombre del nivel
    private BufferedImage background; // Imagen de fondo del nivel
    private ArrayList<Platform> platforms; // Lista de plataformas en el nivel
    private ArrayList<Enemy> enemies; // Lista de enemigos en el nivel
    private ArrayList<Collectible> collectibles; // Lista de coleccionables en el nivel
    private Point playerSpawnPoint; // Punto de aparición del jugador
    private int timeLimit; // Límite de tiempo para completar el nivel (en segundos)
    private int scoreToComplete; // Puntuación necesaria para completar el nivel

    /**
     * Constructor que inicializa un nivel con un nombre y un punto de aparición para el jugador.
     * @param name Nombre del nivel.
     * @param playerSpawnPoint Punto de aparición del jugador.
     */
    public Level(String name, Point playerSpawnPoint) {
        this.name = name;
        this.playerSpawnPoint = playerSpawnPoint;
        this.platforms = new ArrayList<>(); // Inicializar la lista de plataformas
        this.enemies = new ArrayList<>(); // Inicializar la lista de enemigos
        this.collectibles = new ArrayList<>(); // Inicializar la lista de coleccionables
        this.timeLimit = 300; // Límite de tiempo por defecto (5 minutos)
        this.scoreToComplete = 1000; // Puntuación necesaria por defecto
    }

    /**
     * Agrega una plataforma al nivel.
     * @param platform Plataforma a agregar.
     */
    public void addPlatform(Platform platform) {
        platforms.add(platform);
    }

    /**
     * Agrega un enemigo al nivel.
     * @param enemy Enemigo a agregar.
     */
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * Agrega un coleccionable al nivel.
     * @param collectible Coleccionable a agregar.
     */
    public void addCollectible(Collectible collectible) {
        collectibles.add(collectible);
    }

    /**
     * Obtiene la lista de plataformas del nivel.
     * @return Lista de plataformas.
     */
    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * Obtiene la lista de enemigos del nivel.
     * @return Lista de enemigos.
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Obtiene la lista de coleccionables del nivel.
     * @return Lista de coleccionables.
     */
    public ArrayList<Collectible> getCollectibles() {
        return collectibles;
    }

    /**
     * Obtiene la imagen de fondo del nivel.
     * @return Imagen de fondo.
     */
    public BufferedImage getBackground() {
        return background;
    }

    /**
     * Obtiene el punto de aparición del jugador.
     * @return Punto de aparición del jugador.
     */
    public Point getPlayerSpawnPoint() {
        return playerSpawnPoint;
    }

    /**
     * Obtiene el límite de tiempo del nivel.
     * @return Límite de tiempo en segundos.
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * Obtiene la puntuación necesaria para completar el nivel.
     * @return Puntuación necesaria.
     */
    public int getScoreToComplete() {
        return scoreToComplete;
    }

    /**
     * Establece la puntuación necesaria para completar el nivel.
     * @param score Puntuación necesaria.
     */
    public void setScoreToComplete(int score) {
        this.scoreToComplete = score;
    }

    /**
     * Obtiene el nombre del nivel.
     * @return Nombre del nivel.
     */
    public String getName() {
        return name;
    }

    /**
     * Actualiza la lógica de los enemigos en el nivel.
     * Se llama en cada tick del juego.
     */
    public void update() {
        for (Enemy enemy : enemies) {
            enemy.update(); // Actualizar cada enemigo
        }
    }

    /**
     * Dibuja todos los elementos del nivel en la pantalla.
     * @param g2d Objeto Graphics2D para dibujar.
     */
    public void draw(Graphics2D g2d) {
        // Dibujar el fondo si está disponible
        if (background != null) {
            g2d.drawImage(background, 0, 0, null);
        }

        // Dibujar todas las plataformas
        for (Platform platform : platforms) {
            platform.draw(g2d);
        }

        // Dibujar todos los enemigos
        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
        }

        // Dibujar todos los coleccionables
        for (Collectible collectible : collectibles) {
            collectible.draw(g2d);
        }
    }
}