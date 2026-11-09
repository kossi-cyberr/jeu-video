package cm.motorush.render;

import java.util.List;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cm.motorush.config.GameConfig;
import cm.motorush.logic.Road;
import cm.motorush.model.Moto;
import cm.motorush.model.Obstacle;
import cm.motorush.model.ObstacleType;
import cm.motorush.model.Passager;

/**
 * Rendu du jeu avec des formes colorees.
 * Etape 5 : route, obstacles, passagers, moto et HUD (vies, score).
 */
public class Renderer {

    private static final float DASH_PERIOD = GameConfig.DASH_LENGTH + GameConfig.DASH_GAP;

    private final ShapeRenderer shapes;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final GlyphLayout scoreLayout = new GlyphLayout();
    private final StringBuilder scoreText = new StringBuilder();
    /** Dernier score affiche : le texte n'est reconstruit que s'il change. */
    private int displayedScore = -1;

    public Renderer() {
        this.shapes = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(GameConfig.SCORE_FONT_SCALE);
        this.font.setColor(GameConfig.SCORE_COLOR.r, GameConfig.SCORE_COLOR.g,
                GameConfig.SCORE_COLOR.b, 1f);
    }

    public void draw(OrthographicCamera camera, Road road, List<Obstacle> obstacles,
                     List<Passager> passagers, Moto moto, int score) {
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        drawRoad();
        drawLaneDashes(road.getDashOffset());
        drawObstacles(obstacles);
        drawPassagers(passagers);
        drawMoto(moto);
        drawLives(moto.getLives());

        shapes.end();

        drawScore(camera, score);
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

    private void drawPassagers(List<Passager> passagers) {
        setColor(GameConfig.PASSENGER_COLOR);
        for (int i = 0; i < passagers.size(); i++) {
            Passager passager = passagers.get(i);
            shapes.rect(passager.getBounds().x, passager.getBounds().y,
                    passager.getBounds().width, passager.getBounds().height);
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
        if (!moto.isBlinkOn()) {
            return;
        }
        setColor(GameConfig.BIKE_COLOR);
        shapes.rect(moto.getX() - GameConfig.BIKE_WIDTH / 2f,
                moto.getY(),
                GameConfig.BIKE_WIDTH,
                GameConfig.BIKE_HEIGHT);
    }

    /** Vies restantes, en icones alignees en haut a gauche (R9). */
    private void drawLives(int lives) {
        float top = GameConfig.WORLD_HEIGHT - GameConfig.LIFE_ICON_MARGIN - GameConfig.LIFE_ICON_HEIGHT;
        for (int i = 0; i < GameConfig.LIVES_START; i++) {
            setColor(i < lives ? GameConfig.LIFE_FULL_COLOR : GameConfig.LIFE_LOST_COLOR);
            float x = GameConfig.LIFE_ICON_MARGIN + i * (GameConfig.LIFE_ICON_WIDTH + GameConfig.LIFE_ICON_SPACING);
            shapes.rect(x, top, GameConfig.LIFE_ICON_WIDTH, GameConfig.LIFE_ICON_HEIGHT);
        }
    }

    /** Score en haut a droite, aligne sur la marge du HUD des vies. */
    private void drawScore(OrthographicCamera camera, int score) {
        if (score != displayedScore) {
            displayedScore = score;
            scoreText.setLength(0);
            scoreText.append("Score ").append(score);
            scoreLayout.setText(font, scoreText);
        }
        float x = GameConfig.WORLD_WIDTH - GameConfig.SCORE_MARGIN - scoreLayout.width;
        float y = GameConfig.WORLD_HEIGHT - GameConfig.SCORE_MARGIN;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, scoreLayout, x, y);
        batch.end();
    }

    private void setColor(GameConfig.Rgb color) {
        shapes.setColor(color.r, color.g, color.b, 1f);
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
        shapes.dispose();
    }
}
