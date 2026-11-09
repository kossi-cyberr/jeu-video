package cm.motorush.render;

import java.util.List;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cm.motorush.config.GameConfig;
import cm.motorush.logic.Road;
import cm.motorush.model.Moto;
import cm.motorush.model.Obstacle;
import cm.motorush.model.ObstacleType;

/**
 * Rendu du jeu avec des formes colorees.
 * Etape 2 : route a trois voies defilante et moto du joueur.
 */
public class Renderer {

    private static final float DASH_PERIOD = GameConfig.DASH_LENGTH + GameConfig.DASH_GAP;

    private final ShapeRenderer shapes;

    public Renderer() {
        this.shapes = new ShapeRenderer();
    }

    public void draw(OrthographicCamera camera, Road road, List<Obstacle> obstacles, Moto moto) {
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        drawRoad();
        drawLaneDashes(road.getDashOffset());
        drawObstacles(obstacles);
        drawMoto(moto);

        shapes.end();
    }

    private void drawRoad() {
        setColor(GameConfig.ROAD_COLOR);
        shapes.rect(0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
    }

    /** Pointilles separant les voies, qui descendent avec le defilement. */
    private void drawLaneDashes(float dashOffset) {
        setColor(GameConfig.DASH_COLOR);
        float halfWidth = GameConfig.DASH_WIDTH / 2f;
        for (float separatorX = GameConfig.LANE_WIDTH;
             separatorX < GameConfig.WORLD_WIDTH;
             separatorX += GameConfig.LANE_WIDTH) {
            for (float y = -DASH_PERIOD + dashOffset;
                 y < GameConfig.WORLD_HEIGHT + DASH_PERIOD;
                 y += DASH_PERIOD) {
                shapes.rect(separatorX - halfWidth, y, GameConfig.DASH_WIDTH, GameConfig.DASH_LENGTH);
            }
        }
    }

    private void drawObstacles(List<Obstacle> obstacles) {
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            setColor(colorOf(obstacle.getType()));
            shapes.rect(obstacle.getBounds().x, obstacle.getBounds().y,
                    obstacle.getBounds().width, obstacle.getBounds().height);
        }
    }

    private static GameConfig.Rgb colorOf(ObstacleType type) {
        switch (type) {
            case CAMION:
                return GameConfig.TRUCK_COLOR;
            case NID_DE_POULE:
                return GameConfig.POTHOLE_COLOR;
            case VOITURE:
            default:
                return GameConfig.CAR_COLOR;
        }
    }

    private void drawMoto(Moto moto) {
        setColor(GameConfig.BIKE_COLOR);
        shapes.rect(moto.getX() - GameConfig.BIKE_WIDTH / 2f,
                moto.getY(),
                GameConfig.BIKE_WIDTH,
                GameConfig.BIKE_HEIGHT);
    }

    private void setColor(GameConfig.Rgb color) {
        shapes.setColor(color.r, color.g, color.b, 1f);
    }

    public void dispose() {
        shapes.dispose();
    }
}
