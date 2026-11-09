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

    // --- R10, R11 : passagers ---
    public static final float PASSENGER_WIDTH = 50f;
    public static final float PASSENGER_HEIGHT = 70f;
    /** Une apparition sur quatre est un passager plutot qu'un obstacle (R10). */
    public static final int PASSENGER_SPAWN_ONE_IN = 4;

    // --- R5 : obstacles (voitures, camions, nids-de-poule) ---
    public static final float CAR_WIDTH = 70f;
    public static final float CAR_HEIGHT = 110f;
    public static final float TRUCK_WIDTH = 90f;
    public static final float TRUCK_HEIGHT = 180f;
    public static final float POTHOLE_WIDTH = 80f;
    public static final float POTHOLE_HEIGHT = 80f;
    /** R7 (valeur de depart) : intervalle entre deux apparitions, en secondes. */
    public static final float OBSTACLE_SPAWN_INTERVAL_INITIAL = 1.2f;
    /** R7 : intervalle minimal entre deux apparitions, en secondes. */
    public static final float OBSTACLE_SPAWN_INTERVAL_MIN = 0.45f;
    /** R7 : reduction de l'intervalle, en secondes par seconde de jeu. */
    public static final float OBSTACLE_SPAWN_INTERVAL_DECREASE_PER_SECOND = 0.0125f;

    // --- R9 : vies et invincibilite ---
    public static final int LIVES_START = 3;
    /** Duree d'invincibilite apres un choc, en secondes. */
    public static final float INVINCIBILITY_DURATION = 1.5f;
    /** Demi-periode du clignotement pendant l'invincibilite, en secondes. */
    public static final float INVINCIBILITY_BLINK_INTERVAL = 0.1f;

    // --- R12 : score ---
    public static final int SCORE_PER_SECOND = 10;
    public static final int SCORE_PER_PASSENGER = 50;
    /** Echelle du texte du score (police par defaut de libGDX). */
    public static final float SCORE_FONT_SCALE = 1.4f;
    public static final float SCORE_MARGIN = 16f;

    // --- Affichage des vies (HUD) ---
    public static final float LIFE_ICON_WIDTH = 22f;
    public static final float LIFE_ICON_HEIGHT = 22f;
    public static final float LIFE_ICON_SPACING = 8f;
    public static final float LIFE_ICON_MARGIN = 16f;

    // --- Etape 9 : musique et bruitages ---
    public static final float MUSIC_VOLUME = 0.35f;
    public static final float SOUND_VOLUME = 0.8f;

    // --- Etape 7 : ecrans de menu ---
    public static final float MENU_TITLE_Y = 620f;
    public static final float MENU_SUBTITLE_Y = 540f;
    /** Ordonnee du centre de la premiere option. */
    public static final float MENU_FIRST_OPTION_Y = 400f;
    public static final float MENU_OPTION_SPACING = 70f;
    public static final float MENU_TITLE_FONT_SCALE = 2.4f;
    public static final float MENU_FONT_SCALE = 1.6f;
    public static final float MENU_HIGHLIGHT_WIDTH = 300f;
    public static final float MENU_HIGHLIGHT_HEIGHT = 48f;

    // --- Defilement de la route ---
    /** R6 (valeur de depart) : vitesse de defilement en unites par seconde. */
    public static final float ROAD_SPEED_INITIAL = 250f;
    /** R6 : acceleration de la route, en unites par seconde et par seconde de jeu. */
    public static final float ROAD_SPEED_INCREASE_PER_SECOND = 12f;
    /** R6 : vitesse de defilement maximale, en unites par seconde. */
    public static final float ROAD_SPEED_MAX = 700f;
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
    /** Vie perdue dans le HUD (teinte du coeur). */
    public static final Rgb LIFE_LOST_COLOR = new Rgb(0.35f, 0.35f, 0.38f);
    /** Passager a ramasser (R10). */
    public static final Rgb PASSENGER_COLOR = new Rgb(0.95f, 0.80f, 0.15f);
    /** Score affiche en haut a droite. */
    public static final Rgb SCORE_COLOR = new Rgb(0.95f, 0.95f, 0.90f);
    /** Fond des ecrans de menu. */
    public static final Rgb MENU_BACKGROUND_COLOR = new Rgb(0.10f, 0.10f, 0.12f);
    /** Titre des ecrans de menu. */
    public static final Rgb MENU_TITLE_COLOR = new Rgb(0.95f, 0.45f, 0.10f);
    /** Texte des options non selectionnees. */
    public static final Rgb MENU_TEXT_COLOR = new Rgb(0.85f, 0.85f, 0.80f);
    /** Texte de l'option selectionnee, sur fond orange. */
    public static final Rgb MENU_SELECTED_TEXT_COLOR = new Rgb(0.10f, 0.10f, 0.12f);
    /** Surbrillance de l'option selectionnee. */
    public static final Rgb MENU_HIGHLIGHT_COLOR = new Rgb(0.95f, 0.45f, 0.10f);
}
