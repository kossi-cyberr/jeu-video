package cm.motorush.logic;

import cm.motorush.config.GameConfig;

/**
 * Difficulte progressive (R6, R7) : la route accelere et l'intervalle
 * entre deux apparitions diminue avec le temps de jeu, entre les valeurs
 * de depart et un plafond / plancher. Sans dependance graphique.
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

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void reset() {
        elapsedTime = 0f;
    }
}
