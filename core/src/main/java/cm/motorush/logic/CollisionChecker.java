package cm.motorush.logic;

import java.util.List;
import com.badlogic.gdx.math.Rectangle;
import cm.motorush.model.Obstacle;

/**
 * Detection de collisions par rectangles (R9).
 * Deux rectangles se touchent des que leurs surfaces se recouvrent :
 * un simple contact de bord n'est pas une collision.
 */
public final class CollisionChecker {

    private CollisionChecker() {
    }

    public static boolean overlaps(Rectangle a, Rectangle b) {
        return a.x < b.x + b.width
                && a.x + a.width > b.x
                && a.y < b.y + b.height
                && a.y + a.height > b.y;
    }

    /** Vrai si la moto touche au moins un des obstacles. */
    public static boolean hitsAny(Rectangle moto, List<Obstacle> obstacles) {
        for (int i = 0; i < obstacles.size(); i++) {
            if (overlaps(moto, obstacles.get(i).getBounds())) {
                return true;
            }
        }
        return false;
    }
}
