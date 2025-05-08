import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Panel principal del juego donde se maneja la lógica del juego y el renderizado.
 * Implementa ActionListener para el bucle del juego y Serializable para persistencia.
 */
public class GamePanel extends JPanel implements ActionListener, Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DELAY = 16; // ~60 FPS (1000ms / 60 = 16.66ms)
    private Timer timer;                 // Timer para el bucle del juego
    private Player player;               // Jugador principal
    private boolean[] keys;              // Estado de las teclas presionadas
    private Level currentLevel;          // Nivel actual del juego
    private GameState gameState;         // Estado actual del juego
    private int score;                  // Puntuación del juego
    private int lives;                  // Vidas del jugador
    private long startTime;             // Tiempo de inicio del juego
    private ParticleSystem particleSystem; // Sistema de partículas del juego
    private boolean isPaused;            // Estado de pausa del juego
    private Font gameFont;               // Fuente del juego

    /**
     * Constructor que inicializa todos los componentes del juego.
     * Configura el panel, crea el jugador, las plataformas y el sistema de input.
     */
    public GamePanel() {
        super();
        // Inicialización de variables
        this.keys = new boolean[256];
        this.player = new Player(100, 400);
        this.gameState = GameState.MENU;
        this.score = 0;
        this.lives = 3;
        this.particleSystem = new ParticleSystem();
        this.isPaused = false;
        this.gameFont = new Font("Arial", Font.BOLD, 24);

        // Configuración del panel
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        setBackground(Color.BLACK);
        
        // Configuración del sistema de input
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });

        // Inicialización del bucle del juego
        timer = new Timer(DELAY, this);
        timer.start();
        startTime = System.currentTimeMillis();
    }

    /**
     * Método de renderizado principal.
     * Se llama automáticamente cuando se necesita redibujar el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        switch (gameState) {
            case MENU:
                drawMenu(g2d);
                break;
            case PLAYING:
                drawGame(g2d);
                break;
            case PAUSED:
                drawGame(g2d);
                drawPauseScreen(g2d);
                break;
            case GAME_OVER:
                drawGameOver(g2d);
                break;
            case LEVEL_COMPLETE:
                drawLevelComplete(g2d);
                break;
        }
    }

    private void drawMenu(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(48f));
        g2d.drawString("Street Buds", 250, 200);
        g2d.setFont(gameFont);
        g2d.drawString("Presiona ESPACIO para comenzar", 200, 300);
    }

    private void drawGame(Graphics2D g2d) {
        if (currentLevel != null) {
            currentLevel.draw(g2d);
        }
        player.draw(g2d);
        particleSystem.draw(g2d);

        // Dibujar HUD
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont);
        g2d.drawString("Puntuación: " + score, 20, 30);
        g2d.drawString("Vidas: " + lives, 20, 60);
        
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime) / 1000;
        long remainingTime = currentLevel != null ? currentLevel.getTimeLimit() - elapsedTime : 0;
        g2d.drawString("Tiempo: " + remainingTime, 20, 90);
    }

    private void drawPauseScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(36f));
        g2d.drawString("PAUSA", 350, 250);
        g2d.setFont(gameFont);
        g2d.drawString("Presiona ESC para continuar", 250, 300);
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(36f));
        g2d.drawString("GAME OVER", 300, 250);
        g2d.setFont(gameFont);
        g2d.drawString("Puntuación final: " + score, 300, 300);
        g2d.drawString("Presiona ESPACIO para reiniciar", 200, 350);
    }

    private void drawLevelComplete(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(36f));
        g2d.drawString("¡Nivel Completado!", 250, 250);
        g2d.setFont(gameFont);
        g2d.drawString("Puntuación: " + score, 300, 300);
        g2d.drawString("Presiona ESPACIO para continuar", 200, 350);
    }

    /**
     * Método que se llama en cada tick del juego.
     * Parte del bucle principal del juego.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused && gameState == GameState.PLAYING) {
            update();
        }
        repaint();
    }

    /**
     * Actualiza la lógica del juego.
     * Se llama antes de cada frame.
     */
    private void update() {
        if (currentLevel != null) {
            currentLevel.update();
            player.update(keys, currentLevel.getPlatforms());
            
            // Verificar colisiones con enemigos
            for (Enemy enemy : currentLevel.getEnemies()) {
                if (enemy.isAlive() && player.getBounds().intersects(enemy.getBounds())) {
                    // Verificar si el jugador está atacando (saltando sobre el enemigo)
                    if (player.isAttacking() && player.getBounds().getY() < enemy.getBounds().getY()) {
                        enemy.takeDamage(1, true);
                        // Rebote del jugador
                        player.jump();
                        score += 100; // Puntos por derrotar un enemigo
                    } else if (!enemy.isStunned()) {
                        player.takeDamage(enemy.getDamage());
                        if (player.getHealth() <= 0) {
                            lives--;
                            if (lives <= 0) {
                                gameState = GameState.GAME_OVER;
                            } else {
                                player.respawn();
                            }
                        }
                    }
                }
            }

            // Verificar coleccionables
            for (Collectible collectible : currentLevel.getCollectibles()) {
                if (!collectible.isCollected() && player.getBounds().intersects(collectible.getBounds())) {
                    collectible.collect();
                    score += collectible.getValue();
                    particleSystem.createExplosion(
                        (int)collectible.getBounds().getCenterX(),
                        (int)collectible.getBounds().getCenterY(),
                        20,
                        Color.YELLOW
                    );
                }
            }

            // Verificar si se completó el nivel
            if (score >= currentLevel.getScoreToComplete()) {
                gameState = GameState.LEVEL_COMPLETE;
            }
        }
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
        this.gameState = GameState.PLAYING;
        this.startTime = System.currentTimeMillis();
    }

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }
    }
}