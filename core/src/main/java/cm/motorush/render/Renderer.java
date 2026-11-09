package cm.motorush.render;

import java.util.List;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cm.motorush.assets.Assets;
import cm.motorush.config.GameConfig;
import cm.motorush.logic.Road;
import cm.motorush.model.Moto;
import cm.motorush.model.Obstacle;
import cm.motorush.model.ObstacleType;
import cm.motorush.model.Passager;

/**
 * Rendu du jeu (etape 9) : bitume et pointilles en formes, obstacles,
 * passagers, moto et vies en sprites, score en texte.
 */
public class Renderer {

    private static final float DASH_PERIOD = GameConfig.DASH_LENGTH + GameConfig.DASH_GAP;

    private final Assets assets;
    private final ShapeRenderer shapes;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final GlyphLayout scoreLayout = new GlyphLayout();
    private final StringBuilder scoreText = new StringBuilder();
    /** Dernier score affiche : le texte n'est reconstruit que s'il change. */
    private int displayedScore = -1;

    public Renderer(Assets assets) {
        this.assets = assets;
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
        shapes.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawObstacles(obstacles);
        drawPassagers(passagers);
        drawMoto(moto);
        drawLives(moto.getLives());
        drawScore(score);
        batch.end();
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

    /** Les nids-de-poule passent sous les vehicules. */
    private void drawObstacles(List<Obstacle> obstacles) {
        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).getType() == ObstacleType.NID_DE_POULE) {
                drawObstacle(obstacles.get(i));
            }
        }
        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).getType() != ObstacleType.NID_DE_POULE) {
                drawObstacle(obstacles.get(i));
            }
        }
    }

    private void drawObstacle(Obstacle obstacle) {
        Texture texture;
        switch (obstacle.getType()) {
            case CAMION:
                texture = assets.getTruckTexture();
                break;
            case NID_DE_POULE:
                texture = assets.getPotholeTexture();
                break;
            case VOITURE:
            default:
                texture = assets.getCarTexture();
                break;
        }
        batch.draw(texture, obstacle.getBounds().x, obstacle.getBounds().y,
                obstacle.getBounds().width, obstacle.getBounds().height);
    }

    private void drawPassagers(List<Passager> passagers) {
        for (int i = 0; i < passagers.size(); i++) {
            Passager passager = passagers.get(i);
            batch.draw(assets.getPassengerTexture(),
                    passager.getBounds().x, passager.getBounds().y,
                    passager.getBounds().width, passager.getBounds().height);
        }
    }

    private void drawMoto(Moto moto) {
        if (!moto.isBlinkOn()) {
            return;
        }
        batch.draw(assets.getBikeTexture(),
                moto.getX() - GameConfig.BIKE_WIDTH / 2f,
                moto.getY(),
                GameConfig.BIKE_WIDTH,
                GameConfig.BIKE_HEIGHT);
    }

    /** Vies restantes, en coeurs alignes en haut a gauche (R9). */
    private void drawLives(int lives) {
        float top = GameConfig.WORLD_HEIGHT - GameConfig.LIFE_ICON_MARGIN - GameConfig.LIFE_ICON_HEIGHT;
        for (int i = 0; i < GameConfig.LIVES_START; i++) {
            if (i < lives) {
                batch.setColor(1f, 1f, 1f, 1f);
            } else {
                batch.setColor(GameConfig.LIFE_LOST_COLOR.r, GameConfig.LIFE_LOST_COLOR.g,
                        GameConfig.LIFE_LOST_COLOR.b, 0.45f);
            }
            float x = GameConfig.LIFE_ICON_MARGIN + i * (GameConfig.LIFE_ICON_WIDTH + GameConfig.LIFE_ICON_SPACING);
            batch.draw(assets.getHeartTexture(), x, top,
                    GameConfig.LIFE_ICON_WIDTH, GameConfig.LIFE_ICON_HEIGHT);
        }
        // Le texte du score ne doit pas heriter de la teinte des coeurs perdus.
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /** Score en haut a droite, aligne sur la marge du HUD des vies. */
    private void drawScore(int score) {
        if (score != displayedScore) {
            displayedScore = score;
            scoreText.setLength(0);
            scoreText.append("Score ").append(score);
            scoreLayout.setText(font, scoreText);
        }
        float x = GameConfig.WORLD_WIDTH - GameConfig.SCORE_MARGIN - scoreLayout.width;
        float y = GameConfig.WORLD_HEIGHT - GameConfig.SCORE_MARGIN;
        font.draw(batch, scoreLayout, x, y);
    }

    private void setColor(GameConfig.Rgb color) {
        shapes.setColor(color.r, color.g, color.b, 1f);
    }

    public void dispose() {
        // Les textures, sons et musiques appartiennent a Assets.
        font.dispose();
        batch.dispose();
        shapes.dispose();
    }
}
