/**
 * Enumeración que representa los diferentes estados del juego.
 * Se utiliza para controlar el flujo del juego y determinar qué lógica o pantalla mostrar.
 */
public enum GameState {
    MENU,           // Estado del menú principal, donde el jugador puede iniciar el juego.
    PLAYING,        // Estado en el que el juego está en curso y el jugador está jugando.
    PAUSED,         // Estado de pausa, donde el juego se detiene temporalmente.
    GAME_OVER,      // Estado de fin de juego, cuando el jugador pierde todas sus vidas.
    LEVEL_COMPLETE  // Estado que indica que el jugador ha completado el nivel actual.
}