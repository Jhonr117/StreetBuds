import java.awt.*;

public class Enemy {
    protected int x, y;
    protected int width, height;
    protected int health;
    protected int damage;
    protected boolean isAlive;
    protected int speed;
    protected int direction; // 1 para derecha, -1 para izquierda
    private boolean isStunned;
    private long stunTime;
    private static final long STUN_DURATION = 1000; // 1 segundo de aturdimiento

    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = 100;
        this.damage = 10;
        this.isAlive = true;
        this.speed = 2;
        this.direction = 1;
        this.isStunned = false;
    }

    public void update() {
        if (!isAlive || isStunned) return;
        
        // Movimiento básico
        x += speed * direction;
        
        // Cambio de dirección al llegar a los límites
        if (x <= 0 || x >= 800 - width) {
            direction *= -1;
        }

        // Actualizar estado de aturdimiento
        if (isStunned && System.currentTimeMillis() - stunTime > STUN_DURATION) {
            isStunned = false;
        }
    }

    public void draw(Graphics2D g2d) {
        if (!isAlive) return;
        
        g2d.setColor(isStunned ? Color.GRAY : Color.RED);
        g2d.fillRect(x, y, width, height);
    }

    public void takeDamage(int damage, boolean fromAbove) {
        if (fromAbove) {
            // Si el daño viene desde arriba, el enemigo es derrotado
            isAlive = false;
        } else {
            // Si el daño viene de otro lado, el enemigo es aturdido
            isStunned = true;
            stunTime = System.currentTimeMillis();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isStunned() {
        return isStunned;
    }
} 