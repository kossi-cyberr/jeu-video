package cm.motorush.config;

/**
 * Toutes les valeurs de reglage du jeu (R1 a R13).
 * Aucune constante de gameplay ne doit etre ecrite en dur ailleurs.
 */
public final class GameConfig {

    private GameConfig() {
    }

    /** Couleur RGB, composantes entre 0 et 1. */
    public static final class Rgb {
        public final float r;
        public final float g;
        public final float b;

        public Rgb(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    // --- R1 : ecran logique en portrait, viewport fixe ---
    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 800f;

    // --- Fenetre desktop ---
    public static final String WINDOW_TITLE = "Moto-Taxi Rush";
    public static final int WINDOW_WIDTH = 480;
    public static final int WINDOW_HEIGHT = 800;

    // --- R2 : route a trois voies de largeur egale ---
    public static final int LANE_COUNT = 3;
    public static final float LANE_WIDTH = 160f;

    // --- R3 : moto 60 x 100, une voie a la fois ---
    public static final float BIKE_WIDTH = 60f;
    public static final float BIKE_HEIGHT = 100f;
    /** Ordonnee du bas de la moto (en bas de l'ecran). */
    public static final float BIKE_Y = 60f;
    /** Voie de depart : la voie centrale. */
    public static final int BIKE_START_LANE = 1;
    /** Duree du glissement d'une voie a l'autre, en secondes. */
    public static final float LANE_CHANGE_DURATION = 0.16f;

    // --- R5 : obstacles (voitures, camions, nids-de-poule) ---
    public static final float CAR_WIDTH = 70f;
    public static final float CAR_HEIGHT = 110f;
    public static final float TRUCK_WIDTH = 90f;
    public static final float TRUCK_HEIGHT = 180f;
    public static final float POTHOLE_WIDTH = 80f;
    public static final float POTHOLE_HEIGHT = 80f;
    /** R7 (valeur de depart) : intervalle entre deux apparitions, en secondes. */
    public static final float OBSTACLE_SPAWN_INTERVAL_INITIAL = 1.2f;

    // --- Defilement de la route ---
    /** R6 (valeur de depart) : vitesse de defilement en unites par seconde. */
    public static final float ROAD_SPEED_INITIAL = 250f;
    public static final float DASH_LENGTH = 40f;
    public static final float DASH_GAP = 40f;
    public static final float DASH_WIDTH = 6f;

    // --- Couleurs ---
    /** Bandes laissees par le FitViewport hors de la zone de jeu. */
    public static final Rgb LETTERBOX_COLOR = new Rgb(0f, 0f, 0f);
    /** Bitume de la route. */
    public static final Rgb ROAD_COLOR = new Rgb(0.20f, 0.20f, 0.23f);
    /** Pointilles de separation des voies. */
    public static final Rgb DASH_COLOR = new Rgb(0.85f, 0.85f, 0.80f);
    /** Moto du joueur. */
    public static final Rgb BIKE_COLOR = new Rgb(0.95f, 0.45f, 0.10f);
    public static final Rgb CAR_COLOR = new Rgb(0.80f, 0.22f, 0.22f);
    public static final Rgb TRUCK_COLOR = new Rgb(0.28f, 0.40f, 0.78f);
    public static final Rgb POTHOLE_COLOR = new Rgb(0.08f, 0.08f, 0.09f);
}
