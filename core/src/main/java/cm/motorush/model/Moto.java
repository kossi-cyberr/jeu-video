package cm.motorush.model;

import com.badlogic.gdx.math.Rectangle;
import cm.motorush.config.GameConfig;

/**
 * Moto du joueur (R3, R4).
 * Occupe une seule voie a la fois et glisse d'un cran vers la voie voisine.
 * Aucune dependance graphique (Rectangle est une classe mathematique).
 */
public class Moto {

    private final Rectangle bounds = new Rectangle();

    /** Voie courante, de 0 (gauche) a LANE_COUNT - 1 (droite). */
    private int lane;
    /** Abscisse du centre de la moto. */
    private float x;
    private float fromX;
    private float toX;
    /** Temps ecoule depuis le debut du glissement. */
    private float laneChangeTime;

    public Moto() {
        reset();
    }

    public void reset() {
        lane = GameConfig.BIKE_START_LANE;
        x = laneCenterX(lane);
        fromX = x;
        toX = x;
        laneChangeTime = GameConfig.LANE_CHANGE_DURATION;
        syncBounds();
    }

    /**
     * Deplace la moto d'une voie vers la gauche (-1) ou la droite (+1).
     * R4 : un deplacement hors de la route est ignore.
     */
    public void changeLane(int direction) {
        int target = lane + direction;
        if (target < 0 || target >= GameConfig.LANE_COUNT) {
            return;
        }
        lane = target;
        fromX = x;
        toX = laneCenterX(lane);
        laneChangeTime = 0f;
    }

    public void update(float delta) {
        if (laneChangeTime < GameConfig.LANE_CHANGE_DURATION) {
            laneChangeTime += delta;
            if (laneChangeTime >= GameConfig.LANE_CHANGE_DURATION) {
                laneChangeTime = GameConfig.LANE_CHANGE_DURATION;
                x = toX;
            } else {
                float t = laneChangeTime / GameConfig.LANE_CHANGE_DURATION;
                // Adoucissement (smoothstep) : demarrage et arrivee progressifs.
                float eased = t * t * (3f - 2f * t);
                x = fromX + (toX - fromX) * eased;
            }
            syncBounds();
        }
    }

    private void syncBounds() {
        bounds.set(x - GameConfig.BIKE_WIDTH / 2f, GameConfig.BIKE_Y,
                GameConfig.BIKE_WIDTH, GameConfig.BIKE_HEIGHT);
    }

    /** Abscisse du centre de la voie demandee. */
    public static float laneCenterX(int lane) {
        return lane * GameConfig.LANE_WIDTH + GameConfig.LANE_WIDTH / 2f;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return GameConfig.BIKE_Y;
    }

    public int getLane() {
        return lane;
    }

    public boolean isChangingLane() {
        return laneChangeTime < GameConfig.LANE_CHANGE_DURATION;
    }
}
