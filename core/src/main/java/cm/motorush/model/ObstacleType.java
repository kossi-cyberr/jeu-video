package cm.motorush.model;

import cm.motorush.config.GameConfig;

/**
 * Types d'obstacles (R5). Les tailles viennent toutes de GameConfig.
 */
public enum ObstacleType {

    VOITURE(GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT),
    CAMION(GameConfig.TRUCK_WIDTH, GameConfig.TRUCK_HEIGHT),
    NID_DE_POULE(GameConfig.POTHOLE_WIDTH, GameConfig.POTHOLE_HEIGHT);

    private final float width;
    private final float height;

    ObstacleType(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
