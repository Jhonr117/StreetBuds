import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ParticleSystem {
    private ArrayList<Particle> particles;
    private Random random;

    public ParticleSystem() {
        particles = new ArrayList<>();
        random = new Random();
    }

    public void createExplosion(int x, int y, int count, Color color) {
        for (int i = 0; i < count; i++) {
            float angle = random.nextFloat() * 2 * (float) Math.PI;
            float speed = random.nextFloat() * 5 + 2;
            float vx = (float) Math.cos(angle) * speed;
            float vy = (float) Math.sin(angle) * speed;
            float life = random.nextFloat() * 1.0f + 0.5f;
            particles.add(new Particle(x, y, vx, vy, color, life));
        }
    }

    public void update(float deltaTime) {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update(deltaTime);
            if (p.isDead()) {
                particles.remove(i);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        for (Particle p : particles) {
            p.draw(g2d);
        }
    }

    private class Particle {
        private float x, y;
        private float vx, vy;
        private Color color;
        private float life;
        private float size;
        private float alpha;

        public Particle(float x, float y, float vx, float vy, Color color, float life) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
            this.life = life;
            this.size = random.nextFloat() * 5 + 2;
            this.alpha = 1.0f;
        }

        public void update(float deltaTime) {
            x += vx * deltaTime;
            y += vy * deltaTime;
            vy += 0.1f * deltaTime; // Gravedad
            life -= deltaTime;
            alpha = life;
            size *= 0.99f;
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)));
            g2d.fillOval((int)x, (int)y, (int)size, (int)size);
        }

        public boolean isDead() {
            return life <= 0;
        }
    }
} 