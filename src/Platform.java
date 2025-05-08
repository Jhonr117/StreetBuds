import java.awt.*;

/**
 * Clase que representa una plataforma en el juego.
 * Las plataformas son objetos sólidos sobre los que el jugador puede pararse y colisionar.
 */
public class Platform {
    // Propiedades de posición y tamaño
    private int x, y;           // Posición de la plataforma
    private int width, height;  // Dimensiones de la plataforma
    private boolean isSolid;    // Indica si es una plataforma sólida
    private Color color;         // Color de la plataforma

    /**
     * Constructor que crea una plataforma con posición y dimensiones específicas.
     * @param x Posición X de la plataforma
     * @param y Posición Y de la plataforma
     * @param width Ancho de la plataforma
     * @param height Alto de la plataforma
     * @param isSolid Indica si es una plataforma sólida
     */
    public Platform(int x, int y, int width, int height, boolean isSolid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isSolid = isSolid;
        this.color = new Color(139, 69, 19); // Color marrón para las plataformas
    }

    /**
     * Retorna el rectángulo de colisión de la plataforma.
     * @return Rectangle que representa el área de colisión
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Dibuja la plataforma con su color.
     * @param g2d Contexto gráfico para dibujar
     */
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
    }

    // Getters para acceder a las propiedades de la plataforma
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSolid() {
        return isSolid;
    }
} 