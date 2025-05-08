import java.awt.*;

public class Enemy {
    // Coordenadas del enemigo en el espacio 2D
    protected int x, y;
    // Dimensiones del enemigo
    protected int width, height;
    // Salud del enemigo
    protected int health;
    // Daño que el enemigo puede infligir
    protected int damage;
    // Estado de vida del enemigo
    protected boolean isAlive;
    // Velocidad de movimiento del enemigo
    protected int speed;
    // Dirección de movimiento: 1 para derecha, -1 para izquierda
    protected int direction;
    // Estado de aturdimiento del enemigo
    private boolean isStunned;
    // Tiempo en el que el enemigo fue aturdido
    private long stunTime;
    // Duración del aturdimiento en milisegundos
    private static final long STUN_DURATION = 1000; // 1 segundo de aturdimiento

    // Constructor para inicializar las propiedades del enemigo
    public Enemy(int x, int y, int width, int height) {
        this.x = x; // Posición inicial en X
        this.y = y; // Posición inicial en Y
        this.width = width; // Ancho del enemigo
        this.height = height; // Altura del enemigo
        this.health = 100; // Salud inicial
        this.damage = 100; // Daño base
        this.isAlive = true; // El enemigo comienza vivo
        this.speed = 2; // Velocidad inicial
        this.direction = 1; // Dirección inicial hacia la derecha
        this.isStunned = false; // No está aturdido al inicio
    }

    // Método para actualizar el estado del enemigo
    public void update() {
        // Si el enemigo está muerto o aturdido, no se actualiza
        if (!isAlive || isStunned) return;

        // Movimiento básico del enemigo en la dirección actual
        x += speed * direction;

        // Cambiar de dirección si el enemigo alcanza los límites del área
        if (x <= 0 || x >= 800 - width) {
            direction *= -1; // Invierte la dirección
        }

        // Verificar si el tiempo de aturdimiento ha terminado
        if (isStunned && System.currentTimeMillis() - stunTime > STUN_DURATION) {
            isStunned = false; // El enemigo deja de estar aturdido
        }
    }

    // Método para dibujar al enemigo en pantalla
    public void draw(Graphics2D g2d) {
        // Si el enemigo está muerto, no se dibuja
        if (!isAlive) return;

        // Cambiar el color según el estado del enemigo (aturdido o no)
        g2d.setColor(isStunned ? Color.GRAY : Color.RED);
        // Dibujar un rectángulo que representa al enemigo
        g2d.fillRect(x, y, width, height);
    }

    // Método para manejar el daño recibido por el enemigo
    public void takeDamage(int damage, boolean fromAbove) {
        if (fromAbove) {
            // Si el daño viene desde arriba, el enemigo muere
            isAlive = false;
        } else {
            // Si el daño viene de otro lado, el enemigo es aturdido
            isStunned = true;
            stunTime = System.currentTimeMillis(); // Registrar el tiempo de aturdimiento
        }
    }

    // Método para obtener los límites del enemigo como un rectángulo
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Método para verificar si el enemigo está vivo
    public boolean isAlive() {
        return isAlive;
    }

    // Método para obtener el daño que el enemigo puede infligir
    public int getDamage() {
        return damage;
    }

    // Método para verificar si el enemigo está aturdido
    public boolean isStunned() {
        return isStunned;
    }
}