package cm.motorush.model;

import com.badlogic.gdx.math.Rectangle;
import cm.motorush.config.GameConfig;

/**
 * Passager a ramasser (R10, R11).
 * Apparait en haut d'une voie et descend a la vitesse de la route.
 * Sorti de l'ecran sans avoir ete ramasse, il est simplement perdu.
 * Sans dependance graphique : Rectangle est une classe mathematique.
 */
public class Passager {

    private final Rectangle bounds = new Rectangle();

    /** Place le passager en haut de la voie demandee. */
    public void init(int lane) {
        float x = Moto.laneCenterX(lane) - GameConfig.PASSENGER_WIDTH / 2f;
        bounds.set(x, GameConfig.WORLD_HEIGHT,
                GameConfig.PASSENGER_WIDTH, GameConfig.PASSENGER_HEIGHT);
    }

    /** Descend le passager a la vitesse de defilement de la route. */
    public void update(float delta, float speed) {
        bounds.y -= speed * delta;
    }

    /** Vrai quand le passager est entierement sorti par le bas (R11). */
    public boolean isOffScreen() {
        return bounds.y + bounds.height < 0f;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
