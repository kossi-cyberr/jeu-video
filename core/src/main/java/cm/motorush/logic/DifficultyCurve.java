package cm.motorush.logic;

import cm.motorush.config.GameConfig;

/**
 * Difficulte progressive (R6, R7) : la route accelere et l'intervalle
 * entre deux apparitions diminue avec le temps de jeu, entre les valeurs
 * de depart et un plafond / plancher. Sans dependance graphique.
 *
 * S'y ajoutent les heures de pointe (etape 11) : apres RUSH_START_TIME,
 * chaque cycle alterne une accalmie et une phase intense qui accelere la
 * route et densifie les apparitions, de plus en plus fort a chaque cycle.
 * Le jeu continue donc de changer apres la premiere minute.
 */
public class DifficultyCurve {

    /** Temps de jeu ecoule, en secondes. */
    private float elapsedTime;

    public void update(float delta) {
        elapsedTime += delta;
    }

    /**
     * R6 : 250 unites/s au depart, +12 par seconde, plafonne a 700.
     */
    public float getRoadSpeed() {
        return Math.min(GameConfig.ROAD_SPEED_MAX,
                GameConfig.ROAD_SPEED_INITIAL
                        + GameConfig.ROAD_SPEED_INCREASE_PER_SECOND * elapsedTime);
    }

    /**
     * R7 : de 1,2 s vers 0,45 s, jamais sous le plancher.
     */
    public float getSpawnInterval() {
        return Math.max(GameConfig.OBSTACLE_SPAWN_INTERVAL_MIN,
                GameConfig.OBSTACLE_SPAWN_INTERVAL_INITIAL
                        - GameConfig.OBSTACLE_SPAWN_INTERVAL_DECREASE_PER_SECOND * elapsedTime);
    }

    /**
     * Vrai pendant l'heure de pointe : les RUSH_DURATION premieres secondes
     * de chaque cycle, a partir de RUSH_START_TIME.
     */
    public boolean isRushHour() {
        if (elapsedTime < GameConfig.RUSH_START_TIME) {
            return false;
        }
        float inCycle = (elapsedTime - GameConfig.RUSH_START_TIME)
                % GameConfig.RUSH_CYCLE_DURATION;
        return inCycle < GameConfig.RUSH_DURATION;
    }

    /** Numero de l'heure de pointe en cours (1 pour la premiere). */
    public int getRushCount() {
        if (elapsedTime < GameConfig.RUSH_START_TIME) {
            return 0;
        }
        return 1 + (int) ((elapsedTime - GameConfig.RUSH_START_TIME)
                / GameConfig.RUSH_CYCLE_DURATION);
    }

    /**
     * Facteur applique a la vitesse de la route : 1 hors pointe, de plus en
     * plus fort a chaque pointe jusqu'au plafond RUSH_SPEED_MULTIPLIER_MAX.
     */
    public float getSpeedMultiplier() {
        if (!isRushHour()) {
            return 1f;
        }
        float intensity = GameConfig.RUSH_SPEED_MULTIPLIER
                + GameConfig.RUSH_INTENSITY_STEP * (getRushCount() - 1);
        return Math.min(GameConfig.RUSH_SPEED_MULTIPLIER_MAX, intensity);
    }

    /** Facteur applique a l'intervalle d'apparition (1 hors pointe). */
    public float getSpawnMultiplier() {
        return isRushHour() ? GameConfig.RUSH_SPAWN_MULTIPLIER : 1f;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void reset() {
        elapsedTime = 0f;
    }
}
