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
    /** Vies restantes (R9). */
    private int lives;
    /** Temps d'invincibilite restant, en secondes (R9). */
    private float invincibleTime;

    public Moto() {
        reset();
    }

    public void reset() {
        lane = GameConfig.BIKE_START_LANE;
        x = laneCenterX(lane);
        fromX = x;
        toX = x;
        laneChangeTime = GameConfig.LANE_CHANGE_DURATION;
        lives = GameConfig.LIVES_START;
        invincibleTime = 0f;
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

    /**
     * Retire une vie et demarre l'invincibilite (R9).
     * Un choc pendant l'invincibilite, ou sans vie restante, est ignore :
     * plusieurs obstacles touches la meme image ne coutent qu'une vie.
     *
     * @return vrai si une vie a ete perdue.
     */
    public boolean hit() {
        if (invincibleTime > 0f || lives <= 0) {
            return false;
        }
        lives--;
        invincibleTime = GameConfig.INVINCIBILITY_DURATION;
        return true;
    }

    public void update(float delta) {
        if (invincibleTime > 0f) {
            invincibleTime = Math.max(0f, invincibleTime - delta);
        }
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

    public int getLives() {
        return lives;
    }

    public boolean isInvincible() {
        return invincibleTime > 0f;
    }

    public float getInvincibleTime() {
        return invincibleTime;
    }

    /**
     * Clignotement pendant l'invincibilite (R9) : la moto est alternee
     * visible / cachee. Hors invincibilite elle est toujours visible.
     */
    public boolean isBlinkOn() {
        if (invincibleTime <= 0f) {
            return true;
        }
        int phase = (int) (invincibleTime / GameConfig.INVINCIBILITY_BLINK_INTERVAL);
        return phase % 2 == 0;
    }
}
