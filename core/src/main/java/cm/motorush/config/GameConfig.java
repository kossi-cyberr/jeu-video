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

    // --- Bouton pause tactile du HUD (etape 13) ---
    public static final float PAUSE_BUTTON_SIZE = 44f;
    public static final float PAUSE_BUTTON_MARGIN = 12f;
    public static final float PAUSE_BAR_WIDTH = 7f;
    public static final float PAUSE_BAR_HEIGHT = 20f;
    public static final float PAUSE_BAR_GAP = 8f;

    /** Abscisse du bord gauche du bouton pause, centre en haut de l'ecran. */
    public static float pauseButtonX() {
        return (WORLD_WIDTH - PAUSE_BUTTON_SIZE) / 2f;
    }

    /** Ordonnee du bord bas du bouton pause. */
    public static float pauseButtonY() {
        return WORLD_HEIGHT - PAUSE_BUTTON_MARGIN - PAUSE_BUTTON_SIZE;
    }

    /** Vrai si le point donne (coordonnees du monde) touche le bouton pause. */
    public static boolean isPauseButtonHit(float x, float y) {
        float px = pauseButtonX();
        float py = pauseButtonY();
        return x >= px && x <= px + PAUSE_BUTTON_SIZE
                && y >= py && y <= py + PAUSE_BUTTON_SIZE;
    }

    // --- Affichage des vies (HUD) ---
    public static final float LIFE_ICON_WIDTH = 22f;
    public static final float LIFE_ICON_HEIGHT = 22f;
    public static final float LIFE_ICON_SPACING = 8f;
    public static final float LIFE_ICON_MARGIN = 16f;

    // --- Etape 9 : musique et bruitages ---
    public static final float MUSIC_VOLUME = 0.35f;
    public static final float SOUND_VOLUME = 0.8f;

    // --- Etape 7 : ecrans de menu ---
    /** Signature affichee en bas des menus. */
    public static final String AUTHOR_NAME = "Kossi";
    /** Distance minimale d'un glissement tactile pour changer d'option, en fraction de la hauteur de l'ecran. */
    public static final float MENU_TOUCH_SWIPE_FRACTION = 0.05f;
    public static final float MENU_TITLE_Y = 620f;
    /** Diametre et ordonnee du centre du medaillon photo, au-dessus du titre. */
    public static final float MENU_PORTRAIT_SIZE = 110f;
    public static final float MENU_PORTRAIT_Y = 730f;
    public static final float MENU_SUBTITLE_Y = 540f;
    /** Ordonnee du centre de la premiere option. */
    public static final float MENU_FIRST_OPTION_Y = 400f;
    public static final float MENU_OPTION_SPACING = 70f;
    public static final float MENU_TITLE_FONT_SCALE = 2.4f;
    public static final float MENU_FONT_SCALE = 1.6f;
    /** Ordonnee et echelle de la signature en bas des menus. */
    public static final float MENU_AUTHOR_Y = 56f;
    public static final float MENU_AUTHOR_FONT_SCALE = 1.2f;
    public static final float MENU_HIGHLIGHT_WIDTH = 300f;
    public static final float MENU_HIGHLIGHT_HEIGHT = 48f;

    // --- Retour sensoriel et combo (etape 12) ---
    /** Multiplicateur maximal du combo de passagers. */
    public static final int PASSENGER_COMBO_MAX = 5;
    /** Hauteur du texte du combo, au-dessus de la moto. */
    public static final float COMBO_Y = 300f;
    public static final float COMBO_FONT_SCALE = 1.2f;
    /** Duree et amplitude du tremblement d'ecran apres un choc. */
    public static final float SHAKE_DURATION = 0.35f;
    public static final float SHAKE_STRENGTH = 12f;
    /** Particules d'impact : quantite par evenement et duree de vie. */
    public static final int PARTICLE_MAX = 64;
    public static final int PARTICLE_COUNT = 14;
    public static final float PARTICLE_LIFETIME = 0.45f;
    public static final float PARTICLE_SPEED_MIN = 120f;
    public static final float PARTICLE_SPEED_MAX = 340f;
    public static final float PARTICLE_GRAVITY = 700f;
    public static final float PARTICLE_SIZE = 5f;
    /** Le bruitage de ramassage monte d'un demi-ton a chaque passager du combo. */
    public static final float PICKUP_PITCH_STEP = 0.08f;
    public static final float PICKUP_PITCH_MAX = 1.6f;

    // --- Heures de pointe : la difficulte continue d'evoluer apres une minute ---
    /** Temps de jeu avant la premiere heure de pointe, en secondes. */
    public static final float RUSH_START_TIME = 30f;
    /** Periode d'un cycle accalmie + heure de pointe, en secondes. */
    public static final float RUSH_CYCLE_DURATION = 30f;
    /** Duree de l'heure de pointe au debut de chaque cycle, en secondes. */
    public static final float RUSH_DURATION = 8f;
    /** Acceleration de la route pendant une heure de pointe (1 = aucune). */
    public static final float RUSH_SPEED_MULTIPLIER = 1.15f;
    /** Gain d'intensite a chaque heure de pointe suivante. */
    public static final float RUSH_INTENSITY_STEP = 0.05f;
    /** Plafond de l'acceleration d'une heure de pointe. */
    public static final float RUSH_SPEED_MULTIPLIER_MAX = 1.5f;
    /** Densification des apparitions pendant une heure de pointe. */
    public static final float RUSH_SPAWN_MULTIPLIER = 1.35f;
    /** Texte d'alerte affiche pendant une heure de pointe. */
    public static final String RUSH_BANNER_TEXT = "HEURE DE POINTE";
    public static final float RUSH_BANNER_Y = 700f;
    public static final float RUSH_BANNER_FONT_SCALE = 1.3f;

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

    // --- Etape 14 : rendu pseudo-3D (camera derriere la moto) ---
    /** Ordonnee de la ligne d'horizon, ou converge la route. */
    public static final float HORIZON_Y = 470f;
    /** Ordonnee de la base de la moto a l'ecran, au premier plan. */
    public static final float NEAR_Y = 150f;
    /** Echelle (pixels par unite de monde) au niveau de la moto. */
    public static final float SCALE_NEAR = 1.2f;
    /** Recul de la camera derriere la moto, en unites de monde. */
    public static final float CAMERA_BEHIND = 220f;
    /** Vitesse a laquelle la camera se recale sur la moto apres un changement de voie. */
    public static final float CAMERA_FOLLOW_RATE = 7f;
    /** Profondeur minimale sous laquelle un objet n'est plus dessine (derriere nous). */
    public static final float CAMERA_CULL_DEPTH = 40f;
    /** Distance maximale dessinee le long de la route, en unites de monde. */
    public static final float DRAW_DISTANCE = 1600f;
    /** Largeur de la bande blanche marquant le bord de la route. */
    public static final float ROAD_EDGE_WIDTH = 10f;
    /** Nombre de bandes du degrade de ciel. */
    public static final int SKY_BANDS = 24;
    /** Soleil couchant : centre, rayons et halo. */
    public static final float SUN_X = 330f;
    public static final float SUN_Y = 548f;
    public static final float SUN_RADIUS = 28f;
    public static final float SUN_HALO_RADIUS = 78f;
    /** Nombre de silhouettes d'immeubles a l'horizon. */
    public static final int SKYLINE_COUNT = 16;
    /** Facteur de parallaxe des immeubles quand la camera se decale. */
    public static final float SKYLINE_PARALLAX = 0.12f;
    /** Hauteur minimale et maximale des immeubles a l'horizon. */
    public static final float SKYLINE_MIN_HEIGHT = 30f;
    public static final float SKYLINE_MAX_HEIGHT = 100f;
    /** Grille de fenetres allumees sur les immeubles. */
    public static final int BUILDING_WINDOW_ROWS = 3;
    public static final int BUILDING_WINDOW_COLUMNS = 2;
    /** Espacement des lampadaires le long de la route, en unites de monde. */
    public static final float LAMP_SPACING = 200f;
    /** Position laterale des lampadaires, hors de la route. */
    public static final float LAMP_LATERAL = 300f;
    /** Hauteur et epaisseur d'un lampadaire, en unites de monde. */
    public static final float LAMP_HEIGHT = 230f;
    public static final float LAMP_WIDTH = 8f;
    public static final float LAMP_HEAD_RADIUS = 14f;
    /** Rayon du halo lumineux autour des ampoules. */
    public static final float LAMP_GLOW_RADIUS = 26f;
    /** Ecrasement vertical des nids-de-poule, vus au sol. */
    public static final float POTHOLE_FLATTEN = 0.35f;

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
    /** Alerte d'heure de pointe. */
    public static final Rgb RUSH_BANNER_COLOR = new Rgb(0.95f, 0.75f, 0.15f);
    /** Etincelles d'un choc. */
    public static final Rgb PARTICLE_CRASH_COLOR = new Rgb(1f, 0.55f, 0.15f);
    /** Etincelles d'un passager ramasse. */
    public static final Rgb PARTICLE_PICKUP_COLOR = new Rgb(1f, 0.88f, 0.30f);
    /** Texte du combo. */
    public static final Rgb COMBO_COLOR = new Rgb(1f, 0.85f, 0.25f);
    /** Ciel au-dessus de l'horizon et lueur du couchant. */
    public static final Rgb SKY_TOP_COLOR = new Rgb(0.05f, 0.06f, 0.13f);
    public static final Rgb SKY_HORIZON_COLOR = new Rgb(0.36f, 0.22f, 0.28f);
    /** Sol de chaque cote de la route. */
    public static final Rgb GROUND_COLOR = new Rgb(0.11f, 0.13f, 0.12f);
    /** Silhouettes d'immeubles a l'horizon. */
    public static final Rgb BUILDING_COLOR = new Rgb(0.16f, 0.16f, 0.22f);
    /** Bande blanche du bord de la route. */
    public static final Rgb ROAD_EDGE_COLOR = new Rgb(0.90f, 0.90f, 0.86f);
    /** Lampadaires et halo de leur ampoule. */
    public static final Rgb LAMP_COLOR = new Rgb(0.30f, 0.32f, 0.36f);
    public static final Rgb LAMP_GLOW_COLOR = new Rgb(1f, 0.86f, 0.55f);
    /** Soleil couchant et halo. */
    public static final Rgb SUN_COLOR = new Rgb(1f, 0.62f, 0.34f);
    /** Fenetres allumees dans les immeubles de l'horizon. */
    public static final Rgb BUILDING_WINDOW_COLOR = new Rgb(1f, 0.79f, 0.45f);
}
