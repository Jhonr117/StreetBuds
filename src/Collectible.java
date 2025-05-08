import java.awt.*;

public class Collectible {
    public enum CollectibleType {
        COIN,
        POWER_UP,
        HEALTH
    }

    private int x, y;
    private int width, height;
    private CollectibleType type;
    private int value;
    private boolean collected;
    private Color color;

    public Collectible(int x, int y, int width, int height, CollectibleType type, int value) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.value = value;
        this.collected = false;
        
        // Asignar color según el tipo
        switch (type) {
            case COIN:
                this.color = Color.YELLOW;
                break;
            case POWER_UP:
                this.color = Color.MAGENTA;
                break;
            case HEALTH:
                this.color = Color.GREEN;
                break;
        }
    }

    public void draw(Graphics2D g2d) {
        if (!collected) {
            g2d.setColor(color);
            g2d.fillOval(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public int getValue() {
        return value;
    }

    public CollectibleType getType() {
        return type;
    }
} 