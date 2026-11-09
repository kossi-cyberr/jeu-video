package cm.motorush.model;

import com.badlogic.gdx.math.Rectangle;
import cm.motorush.config.GameConfig;

/**
 * Obstacle descendant la route (R5).
 * L'instance est reutilisable : init() la replace en haut de l'ecran,
 * ce qui evite d'allouer un objet a chaque apparition.
 */
public class Obstacle {

    private final Rectangle bounds = new Rectangle();
    private ObstacleType type;
    private int lane;

    /** Replace l'obstacle en haut de la voie demandee. */
    public void init(ObstacleType type, int lane) {
        this.type = type;
        this.lane = lane;
        float x = Moto.laneCenterX(lane) - type.getWidth() / 2f;
        bounds.set(x, GameConfig.WORLD_HEIGHT, type.getWidth(), type.getHeight());
    }

    /** Descend l'obstacle a la vitesse de defilement de la route. */
    public void update(float delta, float speed) {
        bounds.y -= speed * delta;
    }

    /** Vrai quand l'obstacle est entierement sorti par le bas. */
    public boolean isOffScreen() {
        return bounds.y + bounds.height < 0f;
    }

    /** Vrai quand l'obstacle est au moins partiellement visible. */
    public boolean isVisible() {
        return bounds.y < GameConfig.WORLD_HEIGHT && bounds.y + bounds.height > 0f;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public ObstacleType getType() {
        return type;
    }

    public int getLane() {
        return lane;
    }
}
