package cm.motorush.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.math.Rectangle;
import cm.motorush.config.GameConfig;
import cm.motorush.model.Obstacle;
import cm.motorush.model.ObstacleType;
import cm.motorush.model.Passager;

/**
 * Apparition, descente et recyclage des obstacles (R5, R8) et des
 * passagers (R10, R11).
 *
 * R8 : un obstacle n'apparait que s'il reste au moins deux voies libres,
 * ce qui garantit qu'au plus deux voies sont bloquees et qu'une voie
 * reste toujours franchissable.
 * R10 : une apparition sur quatre est un passager, qui ne bloque pas sa voie.
 * L'intervalle entre deux apparitions est fourni par DifficultyCurve (R7).
 */
public class Spawner {

    private static final ObstacleType[] TYPES = ObstacleType.values();

    private final List<Obstacle> obstacles = new ArrayList<>();
    private final List<Passager> passagers = new ArrayList<>();
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

    /**
     * @param spawnInterval intervalle courant entre deux apparitions (R7).
     */
    public void update(float delta, float speed, float spawnInterval) {
        moveAndRemoveOffScreen(delta, speed);

        timer += delta;
        if (timer >= spawnInterval && trySpawn()) {
            timer = 0f;
        }
    }

    /** Fait descendre obstacles et passagers, puis libere ceux qui sont sortis. */
    private void moveAndRemoveOffScreen(float delta, float speed) {
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            obstacle.update(delta, speed);
            if (obstacle.isOffScreen()) {
                obstacles.remove(i);
            }
        }
        for (int i = passagers.size() - 1; i >= 0; i--) {
            Passager passager = passagers.get(i);
            passager.update(delta, speed);
            if (passager.isOffScreen()) {
                // R11 : un passager non ramasse est perdu, sans penalite.
                passagers.remove(i);
            }
        }
    }

    /** @return vrai si un obstacle ou un passager a ete cree. */
    private boolean trySpawn() {
        if (random.nextInt(GameConfig.PASSENGER_SPAWN_ONE_IN) == 0) {
            // R10 : un passager remplace un obstacle une fois sur quatre.
            Passager passager = new Passager();
            passager.init(random.nextInt(GameConfig.LANE_COUNT));
            passagers.add(passager);
            return true;
        }

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

    /**
     * Retire le passager touche par la moto.
     *
     * @return vrai si un passager a ete ramasse.
     */
    public boolean collectPassager(Rectangle motoBounds) {
        for (int i = passagers.size() - 1; i >= 0; i--) {
            if (CollisionChecker.overlaps(motoBounds, passagers.get(i).getBounds())) {
                passagers.remove(i);
                return true;
            }
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

    /** Liste vivante des passagers, pour le rendu et le ramassage. */
    public List<Passager> getPassagers() {
        return passagers;
    }

    public void reset() {
        obstacles.clear();
        passagers.clear();
        timer = 0f;
    }
}
