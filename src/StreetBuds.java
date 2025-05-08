import javax.swing.*;

/**
 * Clase principal del juego que crea la ventana y maneja la inicialización básica.
 * Extiende de JFrame para crear una ventana del sistema.
 */
public class StreetBuds {
    public static void main(String[] args) {
        // Crear la ventana principal
        JFrame frame = new JFrame("Street Buds");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Crear el panel del juego
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        // Inicializar el gestor de niveles
        LevelManager levelManager = LevelManager.getInstance();
        levelManager.createDefaultLevels();
        gamePanel.setCurrentLevel(levelManager.getCurrentLevel());

        // Configurar la ventana
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Iniciar el juego
        gamePanel.requestFocus();
    }
} 