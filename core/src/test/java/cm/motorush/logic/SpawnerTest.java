package cm.motorush.logic;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import cm.motorush.config.GameConfig;
import cm.motorush.model.Obstacle;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Etape 10 (b) : le Spawner ne bloque jamais les trois voies (R8).
 */
class SpawnerTest {

    private static final float DELTA = 1f / 60f;
    private static final float SPEED = 600f;
    private static final float INTERVAL = 0.1f;
    private static final int FRAMES = 60 * 60;

    @Test
    void neverBlocksAllLanes() {
        for (int seed = 0; seed < 10; seed++) {
            Spawner spawner = new Spawner(new Random(seed));
            boolean sawObstacle = false;

            for (int frame = 0; frame < FRAMES; frame++) {
                spawner.update(DELTA, SPEED, INTERVAL);
                sawObstacle |= !spawner.getObstacles().isEmpty();

                int blockedLanes = countBlockedLanes(spawner.getObstacles());
                assertTrue(blockedLanes <= GameConfig.LANE_COUNT - 1,
                        "seed " + seed + ", image " + frame + " : "
                                + blockedLanes + " voies bloquees sur " + GameConfig.LANE_COUNT);
            }

            assertTrue(sawObstacle, "seed " + seed + " : aucun obstacle cree");
        }
    }

    /** Compte les voies occupees par un obstacle visible. */
    private static int countBlockedLanes(List<Obstacle> obstacles) {
        boolean[] blocked = new boolean[GameConfig.LANE_COUNT];
        int count = 0;
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if (obstacle.isVisible() && !blocked[obstacle.getLane()]) {
                blocked[obstacle.getLane()] = true;
                count++;
            }
        }
        return count;
    }
}
