package cm.motorush.render;

import cm.motorush.config.GameConfig;

/**
 * Projection pseudo-3D du jeu (etape 14).
 *
 * La camera est placee derriere la moto, a hauteur fixe, et regarde la route
 * vers un point de fuite situe sur la ligne d'horizon. Les formules restent
 * en 1 / distance : un point deux fois plus loin parait deux fois plus petit
 * et deux fois plus proche de l'horizon. Aucune vraie camera 3D n'est
 * necessaire, ce qui laisse intacte toute la logique de jeu (voies,
 * collisions, score) et ses tests.
 */
public final class Perspective {

    /** Distance du plan de la camera a la moto, le long de la route. */
    public static final float NEAR_DEPTH = GameConfig.CAMERA_BEHIND;

    /** Abscisse monde de la camera : elle suit la moto avec un temps de retard. */
    private float cameraX = GameConfig.WORLD_WIDTH / 2f;

    /** Replace la camera, par exemple au debut d'une partie. */
    public void reset(float worldX) {
        cameraX = worldX;
    }

    /**
     * Recale la camera sur la moto. Le retard donne l'impression que la moto
     * se decale dans l'axe de la route lors d'un changement de voie.
     */
    public void follow(float worldX, float delta) {
        float t = Math.min(1f, delta * GameConfig.CAMERA_FOLLOW_RATE);
        if (t > 0f) {
            cameraX += (worldX - cameraX) * t;
        }
    }

    /**
     * Distance d'un point au plan de la camera.
     * La camera est reculee de CAMERA_BEHIND derriere la moto.
     */
    public static float depth(float worldY) {
        return worldY - (GameConfig.BIKE_Y - GameConfig.CAMERA_BEHIND);
    }

    /** Vrai si le point est devant la camera et assez loin pour etre dessine. */
    public static boolean isVisible(float worldY) {
        return depth(worldY) > GameConfig.CAMERA_CULL_DEPTH;
    }

    /**
     * Ordonnee a l'ecran d'un point du sol situe a la profondeur donnee.
     * La moto tombe exactement sur NEAR_Y, l'horizon reste asymptotique.
     */
    public static float screenYForDepth(float depth) {
        if (depth <= GameConfig.CAMERA_CULL_DEPTH) {
            return GameConfig.HORIZON_Y;
        }
        return GameConfig.HORIZON_Y
                - (GameConfig.HORIZON_Y - GameConfig.NEAR_Y) * NEAR_DEPTH / depth;
    }

    /** Echelle (pixels par unite de monde) a la profondeur donnee. */
    public static float scaleForDepth(float depth) {
        if (depth <= GameConfig.CAMERA_CULL_DEPTH) {
            return 0f;
        }
        return GameConfig.SCALE_NEAR * NEAR_DEPTH / depth;
    }

    /** Abscisse a l'ecran d'un point du sol vu par la camera. */
    public float screenX(float worldX, float depth) {
        return GameConfig.WORLD_WIDTH / 2f + (worldX - cameraX) * scaleForDepth(depth);
    }

    public float getCameraX() {
        return cameraX;
    }
}
