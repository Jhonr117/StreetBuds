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
    private int score;                   // Puntuación del juego
    private int lives;                   // Vidas del jugador
    private long startTime;              // Tiempo de inicio del juego
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
        this.keys = new boolean[256]; // Array para rastrear el estado de las teclas
        this.player = new Player(100, 400); // Crear el jugador en una posición inicial
        this.gameState = GameState.MENU; // El juego comienza en el menú principal
        this.score = 0; // Puntuación inicial
        this.lives = 3; // Vidas iniciales del jugador
        this.particleSystem = new ParticleSystem(); // Inicializar el sistema de partículas
        this.isPaused = false; // El juego no está pausado al inicio
        this.gameFont = new Font("Arial", Font.BOLD, 24); // Fuente para el texto del juego

        // Configuración del panel
        setPreferredSize(new Dimension(800, 600)); // Tamaño del panel
        setFocusable(true); // Permitir que el panel reciba eventos de teclado
        setBackground(Color.BLACK); // Fondo negro para el panel
        
        // Configuración del sistema de input
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true; // Marcar la tecla como presionada
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause(); // Alternar el estado de pausa si se presiona ESC
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false; // Marcar la tecla como liberada
            }
        });

        // Inicialización del bucle del juego
        timer = new Timer(DELAY, this); // Crear un temporizador con el retraso especificado
        timer.start(); // Iniciar el temporizador
        startTime = System.currentTimeMillis(); // Registrar el tiempo de inicio del juego
    }

    /**
     * Método de renderizado principal.
     * Se llama automáticamente cuando se necesita redibujar el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Dibujar diferentes pantallas según el estado del juego
        switch (gameState) {
            case MENU:
                drawMenu(g2d); // Dibujar el menú principal
                break;
            case PLAYING:
                drawGame(g2d); // Dibujar el juego en curso
                break;
            case PAUSED:
                drawGame(g2d); // Dibujar el juego en curso
                drawPauseScreen(g2d); // Superponer la pantalla de pausa
                break;
            case GAME_OVER:
                drawGameOver(g2d); // Dibujar la pantalla de fin de juego
                break;
            case LEVEL_COMPLETE:
                drawLevelComplete(g2d); // Dibujar la pantalla de nivel completado
                break;
        }
    }

    private void drawMenu(Graphics2D g2d) {
        // Dibujar el título y las instrucciones del menú principal
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(48f));
        g2d.drawString("Street Buds", 250, 200);
        g2d.setFont(gameFont);
        g2d.drawString("Presiona ESPACIO para comenzar", 200, 300);
    }

    private void drawGame(Graphics2D g2d) {
        // Dibujar el nivel actual, el jugador y el sistema de partículas
        if (currentLevel != null) {
            currentLevel.draw(g2d);
        }
        player.draw(g2d);
        particleSystem.draw(g2d);

        // Dibujar el HUD (puntuación, vidas y tiempo restante)
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont);
        g2d.drawString("Puntuación: " + score, 20, 30);
        g2d.drawString("Vidas: " + lives, 20, 60);
        
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime) / 1000; // Tiempo transcurrido en segundos
        long remainingTime = currentLevel != null ? currentLevel.getTimeLimit() - elapsedTime : 0;
        g2d.drawString("Tiempo: " + remainingTime, 20, 90);
    }

    private void drawPauseScreen(Graphics2D g2d) {
        // Dibujar una superposición semitransparente para la pausa
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(36f));
        g2d.drawString("PAUSA", 350, 250);
        g2d.setFont(gameFont);
        g2d.drawString("Presiona ESC para continuar", 250, 300);
    }

    private void drawGameOver(Graphics2D g2d) {
        // Dibujar la pantalla de fin de juego
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
        // Dibujar la pantalla de nivel completado
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
            update(); // Actualizar la lógica del juego
        }
        repaint(); // Redibujar el panel
    }

    /**
     * Actualiza la lógica del juego.
     * Se llama antes de cada frame.
     */
    private void update() {
        if (currentLevel != null) {
            currentLevel.update(); // Actualizar el nivel actual
            player.update(keys, currentLevel.getPlatforms()); // Actualizar el jugador
            
            // Verificar colisiones con enemigos
            for (Enemy enemy : currentLevel.getEnemies()) {
                if (enemy.isAlive() && player.getBounds().intersects(enemy.getBounds())) {
                    // Si el jugador está atacando (saltando sobre el enemigo)
                    if (player.isAttacking() && player.getBounds().getY() < enemy.getBounds().getY()) {
                        enemy.takeDamage(1, true); // El enemigo recibe daño y muere
                        player.jump(); // El jugador rebota
                        score += 100; // Incrementar la puntuación
                    } else if (!enemy.isStunned()) {
                        player.takeDamage(enemy.getDamage()); // El jugador recibe daño
                        if (player.getHealth() <= 0) {
                            lives--; // Reducir vidas
                            if (lives <= 0) {
                                gameState = GameState.GAME_OVER; // Fin del juego
                            } else {
                                player.respawn(); // Reaparecer al jugador
                            }
                        }
                    }
                }
            }

            // Verificar coleccionables
            for (Collectible collectible : currentLevel.getCollectibles()) {
                if (!collectible.isCollected() && player.getBounds().intersects(collectible.getBounds())) {
                    collectible.collect(); // Marcar el coleccionable como recogido
                    score += collectible.getValue(); // Incrementar la puntuación
                    particleSystem.createExplosion(
                        (int)collectible.getBounds().getCenterX(),
                        (int)collectible.getBounds().getCenterY(),
                        20,
                        Color.YELLOW
                    ); // Crear una explosión de partículas
                }
            }

            // Verificar si se completó el nivel
            if (score >= currentLevel.getScoreToComplete()) {
                gameState = GameState.LEVEL_COMPLETE; // Cambiar el estado del juego
            }
        }
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level; // Establecer el nivel actual
        this.gameState = GameState.PLAYING; // Cambiar el estado del juego a "jugando"
        this.startTime = System.currentTimeMillis(); // Registrar el tiempo de inicio
    }

    private void togglePause() {
        isPaused = !isPaused; // Alternar el estado de pausa
        if (isPaused) {
            timer.stop(); // Detener el temporizador si está pausado
        } else {
            timer.start(); // Reiniciar el temporizador si no está pausado
        }
    }
}