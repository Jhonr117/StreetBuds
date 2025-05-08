import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase que representa al personaje jugable.
 * Maneja la física, movimiento y colisiones del personaje.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Constantes de dimensiones y física
    private static final int WIDTH = 40;      // Ancho del jugador
    private static final int HEIGHT = 60;     // Alto del jugador
    private static final int JUMP_SPEED = -15;// Velocidad inicial del salto (negativa para ir hacia arriba)
    private static final int MOVE_SPEED = 5;  // Velocidad de movimiento horizontal
    private static final int GRAVITY = 1;     // Fuerza de gravedad aplicada por frame
    
    // Variables de posición y movimiento
    private double x, y;           // Posición actual
    private double velX, velY;     // Velocidades en cada eje
    private boolean isJumping;     // Estado de salto
    private boolean facingRight;   // Dirección a la que mira el personaje
    private transient Rectangle2D bounds; // Hitbox para colisiones

    private int health;
    private int maxHealth;
    private Point spawnPoint;
    private boolean isInvulnerable;
    private long invulnerabilityTime;
    private static final long INVULNERABILITY_DURATION = 2000; // 2 segundos

    private boolean isAttacking;
    private long lastAttackTime;
    private static final long ATTACK_COOLDOWN = 500; // 0.5 segundos entre ataques

    /**
     * Constructor que inicializa al jugador en una posición específica.
     */
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.velX = 0;
        this.velY = 0;
        this.isJumping = false;
        this.facingRight = true;
        this.bounds = new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
        this.health = 100;
        this.maxHealth = 100;
        this.spawnPoint = new Point(x, y);
        this.isInvulnerable = false;
        this.isAttacking = false;
        this.lastAttackTime = 0;
    }

    /**
     * Actualiza la lógica del jugador cada frame.
     * @param keys Array que indica qué teclas están presionadas
     * @param platforms Lista de plataformas para verificar colisiones
     */
    public void update(boolean[] keys, ArrayList<Platform> platforms) {
        // Control de movimiento horizontal
        if (keys['A'] || keys[KeyEvent.VK_LEFT]) {
            velX = -MOVE_SPEED;
            facingRight = false;
        } else if (keys['D'] || keys[KeyEvent.VK_RIGHT]) {
            velX = MOVE_SPEED;
            facingRight = true;
        } else {
            velX = 0;
        }

        // Control de salto
        if ((keys['W'] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_SPACE]) && !isJumping) {
            velY = JUMP_SPEED;
            isJumping = true;
        }

        // Aplicar gravedad
        velY += GRAVITY;

        // Actualizar posición X y verificar colisiones horizontales
        x += velX;
        bounds.setRect(x, y, WIDTH, HEIGHT);
        handleHorizontalCollisions(platforms);

        // Actualizar posición Y y verificar colisiones verticales
        y += velY;
        bounds.setRect(x, y, WIDTH, HEIGHT);
        handleVerticalCollisions(platforms);

        // Verificar límite inferior de la pantalla
        if (y > 400) {
            y = 400;
            velY = 0;
            isJumping = false;
        }

        // Actualizar invulnerabilidad
        if (isInvulnerable && System.currentTimeMillis() - invulnerabilityTime > INVULNERABILITY_DURATION) {
            isInvulnerable = false;
        }

        // Verificar si el jugador está atacando (saltando sobre un enemigo)
        if (isJumping && velY > 0) {
            isAttacking = true;
        } else {
            isAttacking = false;
        }
    }

    /**
     * Maneja las colisiones horizontales con las plataformas.
     */
    private void handleHorizontalCollisions(ArrayList<Platform> platforms) {
        for (Platform platform : platforms) {
            if (bounds.intersects(platform.getBounds())) {
                if (velX > 0) { // Colisión moviéndose a la derecha
                    x = platform.getX() - WIDTH;
                } else if (velX < 0) { // Colisión moviéndose a la izquierda
                    x = platform.getX() + platform.getWidth();
                }
                bounds.setRect(x, y, WIDTH, HEIGHT);
            }
        }
    }

    /**
     * Maneja las colisiones verticales con las plataformas.
     */
    private void handleVerticalCollisions(ArrayList<Platform> platforms) {
        boolean onPlatform = false;
        for (Platform platform : platforms) {
            if (bounds.intersects(platform.getBounds())) {
                if (velY > 0) { // Colisión cayendo
                    y = platform.getY() - HEIGHT;
                    velY = 0;
                    isJumping = false;
                    onPlatform = true;
                } else if (velY < 0) { // Colisión saltando
                    y = platform.getY() + platform.getHeight();
                    velY = 0;
                }
                bounds.setRect(x, y, WIDTH, HEIGHT);
            }
        }

        // Activar estado de salto si no está en una plataforma
        if (!onPlatform && velY == 0) {
            isJumping = true;
        }
    }

    /**
     * Dibuja el personaje y sus detalles.
     */
    public void draw(Graphics2D g2d) {
        // Dibujar forma del jugador
        g2d.setColor(Color.BLUE);
        g2d.fillRect((int)x, (int)y, WIDTH, HEIGHT);
        
        // Dibujar detalles del jugador
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)x + 5, (int)y + 10, 10, 10); // Ojo izquierdo
        g2d.fillOval((int)x + 25, (int)y + 10, 10, 10); // Ojo derecho
        g2d.drawLine((int)x + 10, (int)y + 30, (int)x + 30, (int)y + 30); // Boca

        // Dibujar barra de salud
        drawHealthBar(g2d);

        // Dibujar indicador de ataque si está atacando
        if (isAttacking) {
            g2d.setColor(new Color(255, 255, 0, 128));
            g2d.fillOval((int)x - 5, (int)y - 5, WIDTH + 10, HEIGHT + 10);
        }
    }

    private void drawHealthBar(Graphics2D g2d) {
        int barWidth = 50;
        int barHeight = 5;
        int healthBarX = (int)x + (WIDTH - barWidth) / 2;
        int healthBarY = (int)y - 10;

        // Fondo de la barra
        g2d.setColor(Color.RED);
        g2d.fillRect(healthBarX, healthBarY, barWidth, barHeight);

        // Barra de salud actual
        float healthPercentage = (float) health / maxHealth;
        g2d.setColor(Color.GREEN);
        g2d.fillRect(healthBarX, healthBarY, (int) (barWidth * healthPercentage), barHeight);
    }

    /**
     * Retorna el rectángulo de colisión del jugador.
     */
    public Rectangle2D getBounds() {
        return bounds;
    }

    public void takeDamage(int damage) {
        if (!isInvulnerable) {
            health -= damage;
            isInvulnerable = true;
            invulnerabilityTime = System.currentTimeMillis();
        }
    }

    public int getHealth() {
        return health;
    }

    public void respawn() {
        x = spawnPoint.x;
        y = spawnPoint.y;
        velX = 0;
        velY = 0;
        health = maxHealth;
        isInvulnerable = true;
        invulnerabilityTime = System.currentTimeMillis();
    }

    public void jump() {
        if (!isJumping) {
            velY = JUMP_SPEED;
            isJumping = true;
        }
    }

    public void setSpawnPoint(Point spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public boolean isAttacking() {
        return isAttacking;
    }
} 