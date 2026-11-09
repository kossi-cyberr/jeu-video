package cm.motorush.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import cm.motorush.config.GameConfig;
import cm.motorush.model.Obstacle;
import cm.motorush.model.ObstacleType;

/**
 * Apparition, descente et recyclage des obstacles (R5, R8).
 *
 * R8 : un obstacle n'apparait que s'il reste au moins deux voies libres,
 * ce qui garantit qu'au plus deux voies sont bloquees et qu'une voie
 * reste toujours franchissable.
 */
public class Spawner {

    private static final ObstacleType[] TYPES = ObstacleType.values();

    private final List<Obstacle> obstacles = new ArrayList<>();
    private final Random random;
    /** Tableau reutilise d'une image a l'autre : aucune allocation par frame. */
    private final boolean[] blockedLanes = new boolean[GameConfig.LANE_COUNT];

    private float timer;

    public Spawner() {
        this(new Random());
    }

    public Spawner(Random random) {
        this.random = random;
    }

    public void update(float delta, float speed) {
        moveAndRemoveOffScreen(delta, speed);

        timer += delta;
        if (timer >= GameConfig.OBSTACLE_SPAWN_INTERVAL_INITIAL && trySpawn()) {
            timer = 0f;
        }
    }

    /** Fait descendre les obstacles et libere ceux qui sont sortis de l'ecran. */
    private void moveAndRemoveOffScreen(float delta, float speed) {
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            obstacle.update(delta, speed);
            if (obstacle.isOffScreen()) {
                obstacles.remove(i);
            }
        }
    }

    /** @return vrai si un obstacle a ete cree. */
    private boolean trySpawn() {
        Arrays.fill(blockedLanes, false);
        int blockedCount = 0;
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            int lane = obstacle.getLane();
            if (obstacle.isVisible() && !blockedLanes[lane]) {
                blockedLanes[lane] = true;
                blockedCount++;
            }
        }

        int freeLanes = GameConfig.LANE_COUNT - blockedCount;
        if (freeLanes < 2) {
            // R8 : bloquer cette voie fermerait toutes les voies.
            return false;
        }

        int target = random.nextInt(freeLanes);
        for (int lane = 0; lane < GameConfig.LANE_COUNT; lane++) {
            if (blockedLanes[lane]) {
                continue;
            }
            if (target == 0) {
                Obstacle obstacle = new Obstacle();
                obstacle.init(randomType(), lane);
                obstacles.add(obstacle);
                return true;
            }
            target--;
        }
        return false;
    }

    private ObstacleType randomType() {
        return TYPES[random.nextInt(TYPES.length)];
    }

    /** Liste vivante des obstacles, pour le rendu et les collisions. */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void reset() {
        obstacles.clear();
        timer = 0f;
    }
}
