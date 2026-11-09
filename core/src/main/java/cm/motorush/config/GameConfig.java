package cm.motorush.config;

/**
 * Toutes les valeurs de reglage du jeu.
 * Aucune constante de gameplay ne doit etre ecrite en dur ailleurs.
 */
public final class GameConfig {

    private GameConfig() {
    }

    // --- R1 : ecran logique en portrait, viewport fixe ---
    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 800f;

    // --- Fenetre desktop ---
    public static final String WINDOW_TITLE = "Moto-Taxi Rush";
    public static final int WINDOW_WIDTH = 480;
    public static final int WINDOW_HEIGHT = 800;

    // --- Couleurs (composantes RGB entre 0 et 1) ---
    /** Fond de la zone de jeu. */
    public static final float BG_RED = 0.15f;
    public static final float BG_GREEN = 0.15f;
    public static final float BG_BLUE = 0.18f;
    /** Bandes noires laissees par le FitViewport hors de la zone de jeu. */
    public static final float LETTERBOX_RED = 0.0f;
    public static final float LETTERBOX_GREEN = 0.0f;
    public static final float LETTERBOX_BLUE = 0.0f;
}
