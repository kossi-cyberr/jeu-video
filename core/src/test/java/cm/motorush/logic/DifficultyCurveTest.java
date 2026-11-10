package cm.motorush.logic;

import org.junit.jupiter.api.Test;
import cm.motorush.config.GameConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Etape 10 (a) : la difficulte respecte le plafond de vitesse et le
 * plancher de l'intervalle d'apparition (R6, R7).
 */
class DifficultyCurveTest {

    private static final float TOLERANCE = 0.001f;

    @Test
    void startsAtConfiguredValues() {
        DifficultyCurve curve = new DifficultyCurve();

        assertEquals(GameConfig.ROAD_SPEED_INITIAL, curve.getRoadSpeed(), TOLERANCE);
        assertEquals(GameConfig.OBSTACLE_SPAWN_INTERVAL_INITIAL,
                curve.getSpawnInterval(), TOLERANCE);
    }

    @Test
    void increasesSpeedAndReducesInterval() {
        DifficultyCurve curve = new DifficultyCurve();
        curve.update(1f);

        assertEquals(GameConfig.ROAD_SPEED_INITIAL + GameConfig.ROAD_SPEED_INCREASE_PER_SECOND,
                curve.getRoadSpeed(), TOLERANCE);
        assertEquals(GameConfig.OBSTACLE_SPAWN_INTERVAL_INITIAL
                        - GameConfig.OBSTACLE_SPAWN_INTERVAL_DECREASE_PER_SECOND,
                curve.getSpawnInterval(), TOLERANCE);
    }

    @Test
    void roadSpeedNeverExceedsCeiling() {
        DifficultyCurve curve = new DifficultyCurve();
        curve.update(10_000f);

        assertEquals(GameConfig.ROAD_SPEED_MAX, curve.getRoadSpeed(), TOLERANCE);
    }

    @Test
    void spawnIntervalNeverDropsBelowFloor() {
        DifficultyCurve curve = new DifficultyCurve();
        curve.update(10_000f);

        assertEquals(GameConfig.OBSTACLE_SPAWN_INTERVAL_MIN,
                curve.getSpawnInterval(), TOLERANCE);
    }

    @Test
    void noRushHourAtTheStart() {
        DifficultyCurve curve = new DifficultyCurve();
        curve.update(GameConfig.RUSH_START_TIME - 0.1f);

        assertFalse(curve.isRushHour());
        assertEquals(0, curve.getRushCount());
        assertEquals(1f, curve.getSpeedMultiplier(), TOLERANCE);
        assertEquals(1f, curve.getSpawnMultiplier(), TOLERANCE);
    }

    @Test
    void rushHourAcceleratesAndDensifies() {
        DifficultyCurve curve = new DifficultyCurve();
        curve.update(GameConfig.RUSH_START_TIME + 1f);

        assertTrue(curve.isRushHour());
        assertEquals(1, curve.getRushCount());
        assertEquals(GameConfig.RUSH_SPEED_MULTIPLIER, curve.getSpeedMultiplier(), TOLERANCE);
        assertEquals(GameConfig.RUSH_SPAWN_MULTIPLIER, curve.getSpawnMultiplier(), TOLERANCE);
    }

    @Test
    void rushHourEndsBeforeNextCycle() {
        DifficultyCurve curve = new DifficultyCurve();
        curve.update(GameConfig.RUSH_START_TIME + GameConfig.RUSH_DURATION + 0.1f);

        assertFalse(curve.isRushHour());
        assertEquals(1f, curve.getSpeedMultiplier(), TOLERANCE);
        assertEquals(1f, curve.getSpawnMultiplier(), TOLERANCE);
    }

    @Test
    void rushIntensityGrowsEachCycleThenStopsAtCeiling() {
        DifficultyCurve curve = new DifficultyCurve();
        curve.update(GameConfig.RUSH_START_TIME + GameConfig.RUSH_CYCLE_DURATION + 1f);

        assertEquals(2, curve.getRushCount());
        assertEquals(GameConfig.RUSH_SPEED_MULTIPLIER + GameConfig.RUSH_INTENSITY_STEP,
                curve.getSpeedMultiplier(), TOLERANCE);

        // Bien plus tard, dans une heure de pointe : le plafond s'applique.
        curve.reset();
        curve.update(GameConfig.RUSH_START_TIME + 100f * GameConfig.RUSH_CYCLE_DURATION);
        assertTrue(curve.isRushHour());
        assertEquals(GameConfig.RUSH_SPEED_MULTIPLIER_MAX,
                curve.getSpeedMultiplier(), TOLERANCE);
    }

    @Test
    void resetBringsBackInitialValues() {
        DifficultyCurve curve = new DifficultyCurve();
        curve.update(500f);
        curve.reset();

        assertEquals(GameConfig.ROAD_SPEED_INITIAL, curve.getRoadSpeed(), TOLERANCE);
        assertEquals(GameConfig.OBSTACLE_SPAWN_INTERVAL_INITIAL,
                curve.getSpawnInterval(), TOLERANCE);
        assertFalse(curve.isRushHour());
        assertEquals(1f, curve.getSpeedMultiplier(), TOLERANCE);
    }
}
