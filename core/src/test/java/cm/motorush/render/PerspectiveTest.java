package cm.motorush.render;

import org.junit.jupiter.api.Test;
import cm.motorush.config.GameConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Etape 14 : la projection pseudo-3D place la moto au premier plan, fait
 * converger la route vers l'horizon et retrecit les objets avec la distance.
 */
class PerspectiveTest {

    private static final float TOLERANCE = 0.001f;

    @Test
    void bikeIsDrawnAtTheNearLineWithNearScale() {
        float depth = Perspective.depth(GameConfig.BIKE_Y);

        assertEquals(GameConfig.CAMERA_BEHIND, depth, TOLERANCE);
        assertEquals(GameConfig.NEAR_Y, Perspective.screenYForDepth(depth), TOLERANCE);
        assertEquals(GameConfig.SCALE_NEAR, Perspective.scaleForDepth(depth), TOLERANCE);
    }

    @Test
    void farObjectsRiseTowardsTheHorizonAndShrink() {
        float nearY = Perspective.screenYForDepth(Perspective.depth(GameConfig.BIKE_Y));
        float farY = Perspective.screenYForDepth(Perspective.depth(GameConfig.WORLD_HEIGHT));
        float veryFarY = Perspective.screenYForDepth(Perspective.depth(GameConfig.WORLD_HEIGHT) * 10f);

        assertTrue(farY > nearY, "un objet plus loin doit etre plus haut a l'ecran");
        assertTrue(veryFarY > farY);
        assertTrue(veryFarY < GameConfig.HORIZON_Y, "l'horizon n'est jamais depasse");
        assertTrue(GameConfig.SCALE_NEAR > Perspective.scaleForDepth(Perspective.depth(GameConfig.WORLD_HEIGHT)));
    }

    @Test
    void lanesAppearWiderCloseToTheCamera() {
        Perspective perspective = new Perspective();
        float nearDepth = Perspective.depth(GameConfig.BIKE_Y);
        float farDepth = Perspective.depth(GameConfig.WORLD_HEIGHT);

        float nearGap = perspective.screenX(GameConfig.LANE_WIDTH, nearDepth)
                - perspective.screenX(0f, nearDepth);
        float farGap = perspective.screenX(GameConfig.LANE_WIDTH, farDepth)
                - perspective.screenX(0f, farDepth);

        assertEquals(GameConfig.LANE_WIDTH * GameConfig.SCALE_NEAR, nearGap, TOLERANCE);
        assertTrue(nearGap > farGap);
    }

    @Test
    void roadIsCentredWhenTheCameraIsCentred() {
        Perspective perspective = new Perspective();
        float depth = Perspective.depth(GameConfig.BIKE_Y);

        assertEquals(GameConfig.WORLD_WIDTH / 2f,
                perspective.screenX(GameConfig.WORLD_WIDTH / 2f, depth), TOLERANCE);
    }

    @Test
    void cameraFollowsTheBikeButLagsBehind() {
        Perspective perspective = new Perspective();
        perspective.reset(GameConfig.WORLD_WIDTH / 2f);

        perspective.follow(0f, 0.1f);

        assertTrue(perspective.getCameraX() < GameConfig.WORLD_WIDTH / 2f,
                "la camera doit commencer a suivre");
        assertTrue(perspective.getCameraX() > 0f,
                "la camera ne doit pas sauter d'un coup sur la moto");
    }

    @Test
    void objectsBehindTheCameraAreNotDrawn() {
        assertFalse(Perspective.isVisible(GameConfig.BIKE_Y - GameConfig.CAMERA_BEHIND));
        assertTrue(Perspective.isVisible(GameConfig.BIKE_Y));
        assertTrue(Perspective.isVisible(GameConfig.WORLD_HEIGHT));
    }
}
