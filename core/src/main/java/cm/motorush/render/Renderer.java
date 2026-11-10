package cm.motorush.render;

import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import cm.motorush.assets.Assets;
import cm.motorush.config.GameConfig;
import cm.motorush.logic.Road;
import cm.motorush.model.Moto;
import cm.motorush.model.Obstacle;
import cm.motorush.model.ObstacleType;
import cm.motorush.model.Passager;

/**
 * Rendu du jeu.
 * Etape 9 : route defilante, obstacles, passagers, moto et vies en sprites,
 * score en texte.
 * Etape 12 : combo, alerte d'heure de pointe, tremblement d'ecran et
 * particules d'impact.
 * Etape 14 : rendu pseudo-3D. La route converge vers un point de fuite place
 * sur l'horizon, la camera est derriere la moto et les objets sont projetes
 * puis tries du plus loin au plus proche. Les positions de jeu restent les
 * memes qu'en 2D : seule la projection change.
 * Etape 16 : petites touches de decor (soleil couchant, fenetres allumees,
 * halos des lampadaires).
 */
public class Renderer {

    private static final float DASH_PERIOD = GameConfig.DASH_LENGTH + GameConfig.DASH_GAP;
    /** Nombre de segments des particules rondes. */
    private static final int PARTICLE_SEGMENTS = 8;
    /** Nombre de segments des lampadaires et du soleil. */
    private static final int LAMP_SEGMENTS = 12;
    private static final int SUN_SEGMENTS = 32;
    /** Marge laissee entre deux immeubles de l'horizon. */
    private static final float BUILDING_GAP = 4f;
    /** Profondeur minimale des grands plans (route, bords). */
    private static final float GROUND_NEAR_DEPTH = GameConfig.CAMERA_CULL_DEPTH;
    /** Taille des fenetres des immeubles, en pixels. */
    private static final float WINDOW_WIDTH = 6f;
    private static final float WINDOW_HEIGHT = 9f;

    private final Assets assets;
    private final ShapeRenderer shapes;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final GlyphLayout scoreLayout = new GlyphLayout();
    private final GlyphLayout rushLayout = new GlyphLayout();
    private final GlyphLayout comboLayout = new GlyphLayout();
    private final StringBuilder scoreText = new StringBuilder();
    private final StringBuilder comboText = new StringBuilder();
    /** Dernier score affiche : le texte n'est reconstruit que s'il change. */
    private int displayedScore = -1;
    /** Dernier combo affiche : idem. */
    private int displayedCombo = -1;
    /** Projection decalee pendant un tremblement d'ecran. */
    private final Matrix4 projection = new Matrix4();
    /** Camera virtuelle : suit la moto et projette la scene. */
    private final Perspective perspective = new Perspective();

    private float shakeTime;

    /** Particules en tableaux fixes : aucune allocation pendant le jeu. */
    private final float[] particleX = new float[GameConfig.PARTICLE_MAX];
    private final float[] particleY = new float[GameConfig.PARTICLE_MAX];
    private final float[] particleVX = new float[GameConfig.PARTICLE_MAX];
    private final float[] particleVY = new float[GameConfig.PARTICLE_MAX];
    private final float[] particleLife = new float[GameConfig.PARTICLE_MAX];
    private final float[] particleMaxLife = new float[GameConfig.PARTICLE_MAX];
    private final GameConfig.Rgb[] particleColors = new GameConfig.Rgb[GameConfig.PARTICLE_MAX];
    private int particleCount;

    public Renderer(Assets assets) {
        this.assets = assets;
        this.shapes = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(GameConfig.SCORE_FONT_SCALE);
        this.font.setColor(GameConfig.SCORE_COLOR.r, GameConfig.SCORE_COLOR.g,
                GameConfig.SCORE_COLOR.b, 1f);
        this.font.getData().setScale(GameConfig.RUSH_BANNER_FONT_SCALE);
        this.rushLayout.setText(font, GameConfig.RUSH_BANNER_TEXT);
        this.font.getData().setScale(GameConfig.SCORE_FONT_SCALE);
    }

    /**
     * Fait vieillir le tremblement et les particules, et recale la camera
     * derriere la moto.
     *
     * @param motoX abscisse monde de la moto, que la camera suit.
     */
    public void update(float delta, float motoX) {
        perspective.follow(motoX, delta);
        if (shakeTime > 0f) {
            shakeTime = Math.max(0f, shakeTime - delta);
        }
        for (int i = particleCount - 1; i >= 0; i--) {
            particleLife[i] -= delta;
            if (particleLife[i] <= 0f) {
                removeParticle(i);
                continue;
            }
            particleX[i] += particleVX[i] * delta;
            particleY[i] += particleVY[i] * delta;
            particleVY[i] -= GameConfig.PARTICLE_GRAVITY * delta;
        }
    }

    /** Declenche un tremblement d'ecran court (choc). */
    public void shake() {
        shakeTime = GameConfig.SHAKE_DURATION;
    }

    /**
     * Eclate une gerbe de particules a la position demandee. La position est
     * donnee dans le monde puis convertie a l'ecran : les particules vivent
     * ensuite dans le plan de l'image, ou elles retombent avec la gravite.
     *
     * @param color couleur des etincelles (choc ou ramassage).
     */
    public void burst(float x, float y, GameConfig.Rgb color) {
        float depth = Perspective.depth(y);
        float screenX = perspective.screenX(x, depth);
        float screenY = Perspective.screenYForDepth(y);
        for (int i = 0; i < GameConfig.PARTICLE_COUNT && particleCount < GameConfig.PARTICLE_MAX; i++) {
            int index = particleCount++;
            float angle = MathUtils.random(0f, MathUtils.PI2);
            float speed = MathUtils.random(GameConfig.PARTICLE_SPEED_MIN, GameConfig.PARTICLE_SPEED_MAX);
            particleX[index] = screenX;
            particleY[index] = screenY;
            particleVX[index] = MathUtils.cos(angle) * speed;
            // Legerement vers le haut, puis la gravite fait retomber la gerbe.
            particleVY[index] = MathUtils.sin(angle) * speed + GameConfig.PARTICLE_SPEED_MIN;
            float life = GameConfig.PARTICLE_LIFETIME * MathUtils.random(0.6f, 1f);
            particleLife[index] = life;
            particleMaxLife[index] = life;
            particleColors[index] = color;
        }
    }

    private void removeParticle(int index) {
        int last = --particleCount;
        if (index == last) {
            return;
        }
        particleX[index] = particleX[last];
        particleY[index] = particleY[last];
        particleVX[index] = particleVX[last];
        particleVY[index] = particleVY[last];
        particleLife[index] = particleLife[last];
        particleMaxLife[index] = particleMaxLife[last];
        particleColors[index] = particleColors[last];
    }

    public void draw(OrthographicCamera camera, Road road, List<Obstacle> obstacles,
                     List<Passager> passagers, Moto moto, int score, int combo,
                     boolean rushHour) {
        projection.set(camera.combined);
        if (shakeTime > 0f) {
            float strength = GameConfig.SHAKE_STRENGTH * (shakeTime / GameConfig.SHAKE_DURATION);
            projection.translate(MathUtils.random(-strength, strength),
                    MathUtils.random(-strength, strength), 0f);
        }
        shapes.setProjectionMatrix(projection);
        batch.setProjectionMatrix(projection);

        // Decor lointain, sol et route : les halos demandent la transparence.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        drawSky();
        drawSun();
        drawSkyline();
        drawGround();
        drawRoad();
        drawLaneDashes(road.getDashOffset());
        drawLamps(road.getScroll());
        shapes.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Objets : du plus loin au plus proche, la moto en dernier.
        batch.begin();
        drawObstacles(obstacles);
        drawPassagers(passagers);
        drawMoto(moto);
        batch.end();

        drawParticles();

        // HUD par-dessus la scene.
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        drawPauseButton();
        shapes.end();

        batch.begin();
        drawLives(moto.getLives());
        drawScore(score);
        drawCombo(combo);
        if (rushHour) {
            drawRushBanner();
        }
        batch.end();
    }

    /** Ciel en degrade, du bleu de nuit au-dessus a la lueur du couchant. */
    private void drawSky() {
        float horizon = GameConfig.HORIZON_Y;
        float height = GameConfig.WORLD_HEIGHT - horizon;
        float band = height / GameConfig.SKY_BANDS;
        for (int i = 0; i < GameConfig.SKY_BANDS; i++) {
            // i = 0 juste au-dessus de l'horizon, le plus clair.
            float t = i / (float) (GameConfig.SKY_BANDS - 1);
            shapes.setColor(
                    MathUtils.lerp(GameConfig.SKY_HORIZON_COLOR.r, GameConfig.SKY_TOP_COLOR.r, t),
                    MathUtils.lerp(GameConfig.SKY_HORIZON_COLOR.g, GameConfig.SKY_TOP_COLOR.g, t),
                    MathUtils.lerp(GameConfig.SKY_HORIZON_COLOR.b, GameConfig.SKY_TOP_COLOR.b, t),
                    1f);
            shapes.rect(0f, horizon + i * band, GameConfig.WORLD_WIDTH, band + 0.5f);
        }
    }

    /** Soleil couchant et son halo, juste au-dessus de l'horizon. */
    private void drawSun() {
        shapes.setColor(GameConfig.SUN_COLOR.r, GameConfig.SUN_COLOR.g,
                GameConfig.SUN_COLOR.b, 0.07f);
        shapes.circle(GameConfig.SUN_X, GameConfig.SUN_Y, GameConfig.SUN_HALO_RADIUS, SUN_SEGMENTS);
        shapes.setColor(GameConfig.SUN_COLOR.r, GameConfig.SUN_COLOR.g,
                GameConfig.SUN_COLOR.b, 0.11f);
        shapes.circle(GameConfig.SUN_X, GameConfig.SUN_Y,
                GameConfig.SUN_HALO_RADIUS * 0.75f, SUN_SEGMENTS);
        shapes.setColor(GameConfig.SUN_COLOR.r, GameConfig.SUN_COLOR.g,
                GameConfig.SUN_COLOR.b, 0.16f);
        shapes.circle(GameConfig.SUN_X, GameConfig.SUN_Y,
                GameConfig.SUN_HALO_RADIUS * 0.5f, SUN_SEGMENTS);
        shapes.setColor(GameConfig.SUN_COLOR.r, GameConfig.SUN_COLOR.g,
                GameConfig.SUN_COLOR.b, 1f);
        shapes.circle(GameConfig.SUN_X, GameConfig.SUN_Y, GameConfig.SUN_RADIUS, SUN_SEGMENTS);
    }

    /**
     * Silhouettes d'immeubles posees sur l'horizon, avec quelques fenetres
     * allumees et une legere parallaxe.
     */
    private void drawSkyline() {
        float slot = GameConfig.WORLD_WIDTH / (float) GameConfig.SKYLINE_COUNT;
        float offset = -perspective.getCameraX() * GameConfig.SKYLINE_PARALLAX;
        for (int i = -1; i <= GameConfig.SKYLINE_COUNT; i++) {
            float variation = 0.5f + 0.5f * MathUtils.sin(i * 1.7f);
            float height = GameConfig.SKYLINE_MIN_HEIGHT
                    + (GameConfig.SKYLINE_MAX_HEIGHT - GameConfig.SKYLINE_MIN_HEIGHT) * variation;
            float left = i * slot + offset + BUILDING_GAP / 2f;
            float width = slot - BUILDING_GAP;

            setColor(GameConfig.BUILDING_COLOR);
            shapes.rect(left, GameConfig.HORIZON_Y, width, height);

            drawBuildingWindows(i, left, width, height);
        }
    }

    /** Fenetres allumees : motif fixe pour que la ville ne clignote pas. */
    private void drawBuildingWindows(int index, float left, float width, float height) {
        float stepX = width / (GameConfig.BUILDING_WINDOW_COLUMNS + 1f);
        float stepY = height / (GameConfig.BUILDING_WINDOW_ROWS + 1f);
        for (int row = 0; row < GameConfig.BUILDING_WINDOW_ROWS; row++) {
            for (int column = 0; column < GameConfig.BUILDING_WINDOW_COLUMNS; column++) {
                if ((index * 7 + row * 3 + column) % 5 >= 2) {
                    continue;
                }
                float x = left + stepX * (column + 1) - WINDOW_WIDTH / 2f;
                float y = GameConfig.HORIZON_Y + stepY * (row + 1) - WINDOW_HEIGHT / 2f;
                if (x < left + 2f || x + WINDOW_WIDTH > left + width - 2f) {
                    continue;
                }
                shapes.setColor(GameConfig.BUILDING_WINDOW_COLOR.r,
                        GameConfig.BUILDING_WINDOW_COLOR.g,
                        GameConfig.BUILDING_WINDOW_COLOR.b, 0.85f);
                shapes.rect(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
            }
        }
    }

    /** Sol de chaque cote de la route, sous la ligne d'horizon. */
    private void drawGround() {
        setColor(GameConfig.GROUND_COLOR);
        shapes.rect(0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.HORIZON_Y);
    }

    /** Chaussee en trapeze, avec ses bandes blanches de bord. */
    private void drawRoad() {
        setColor(GameConfig.ROAD_COLOR);
        drawGroundQuad(0f, GameConfig.WORLD_WIDTH);

        setColor(GameConfig.ROAD_EDGE_COLOR);
        drawGroundQuad(0f, GameConfig.ROAD_EDGE_WIDTH);
        drawGroundQuad(GameConfig.WORLD_WIDTH - GameConfig.ROAD_EDGE_WIDTH,
                GameConfig.WORLD_WIDTH);
    }

    /**
     * Quadrilatere au sol, entre deux abscisses monde et de la camera jusqu'a
     * la distance d'affichage. Les deux triangles suivent la perspective.
     */
    private void drawGroundQuad(float worldXLeft, float worldXRight) {
        drawGroundQuad(worldXLeft, worldXRight, GROUND_NEAR_DEPTH, GameConfig.DRAW_DISTANCE);
    }

    private void drawGroundQuad(float worldXLeft, float worldXRight,
                                float nearDepth, float farDepth) {
        float nearY = Perspective.screenYForDepth(nearDepth);
        float farY = Perspective.screenYForDepth(farDepth);
        float leftNear = perspective.screenX(worldXLeft, nearDepth);
        float rightNear = perspective.screenX(worldXRight, nearDepth);
        float leftFar = perspective.screenX(worldXLeft, farDepth);
        float rightFar = perspective.screenX(worldXRight, farDepth);

        shapes.triangle(leftNear, nearY, rightNear, nearY, leftFar, farY);
        shapes.triangle(rightNear, nearY, rightFar, farY, leftFar, farY);
    }

    /** Pointilles separant les voies, qui descendent avec le defilement. */
    private void drawLaneDashes(float dashOffset) {
        setColor(GameConfig.DASH_COLOR);
        float halfWidth = GameConfig.DASH_WIDTH / 2f;
        float lastY = GameConfig.WORLD_HEIGHT + GameConfig.DRAW_DISTANCE;
        for (float separatorX = GameConfig.LANE_WIDTH;
             separatorX < GameConfig.WORLD_WIDTH;
             separatorX += GameConfig.LANE_WIDTH) {
            for (float y = -DASH_PERIOD + dashOffset; y < lastY; y += DASH_PERIOD) {
                float nearDepth = Perspective.depth(y + GameConfig.DASH_LENGTH);
                float farDepth = Perspective.depth(y);
                if (nearDepth <= GROUND_NEAR_DEPTH) {
                    continue;
                }
                if (farDepth > GameConfig.DRAW_DISTANCE) {
                    break;
                }
                drawGroundQuad(separatorX - halfWidth, separatorX + halfWidth, nearDepth, farDepth);
            }
        }
    }

    /** Lampadaires des deux bords, qui defilent avec la route. */
    private void drawLamps(float scroll) {
        float cameraWorldY = GameConfig.BIKE_Y - GameConfig.CAMERA_BEHIND;
        float phase = scroll % GameConfig.LAMP_SPACING;
        float limit = cameraWorldY + GameConfig.DRAW_DISTANCE;
        for (float y = cameraWorldY - phase; y < limit; y += GameConfig.LAMP_SPACING) {
            drawLamp(GameConfig.WORLD_WIDTH / 2f - GameConfig.LAMP_LATERAL, y);
            drawLamp(GameConfig.WORLD_WIDTH / 2f + GameConfig.LAMP_LATERAL, y);
        }
    }

    private void drawLamp(float worldX, float worldY) {
        float depth = Perspective.depth(worldY);
        if (depth < GameConfig.CAMERA_CULL_DEPTH * 2f || depth > GameConfig.DRAW_DISTANCE) {
            return;
        }
        float scale = Perspective.scaleForDepth(depth);
        float x = perspective.screenX(worldX, depth);
        float groundY = Perspective.screenYForDepth(depth);
        // L'axe vertical de l'ecran monte : le sommet du mat est plus haut.
        float topY = groundY + GameConfig.LAMP_HEIGHT * scale;

        setColor(GameConfig.LAMP_COLOR);
        shapes.rectLine(x, groundY, x, topY, Math.max(1f, GameConfig.LAMP_WIDTH * scale));

        // Halo tiede : la ville semble eclairee a la tombee du jour.
        shapes.setColor(GameConfig.LAMP_GLOW_COLOR.r, GameConfig.LAMP_GLOW_COLOR.g,
                GameConfig.LAMP_GLOW_COLOR.b, 0.12f);
        shapes.circle(x, topY, Math.max(3f, GameConfig.LAMP_GLOW_RADIUS * scale), LAMP_SEGMENTS);
        setColor(GameConfig.LAMP_GLOW_COLOR);
        shapes.circle(x, topY, Math.max(1.5f, GameConfig.LAMP_HEAD_RADIUS * scale), LAMP_SEGMENTS);
    }

    /** Bouton pause du HUD, centre en haut, utilisable au doigt. */
    private void drawPauseButton() {
        float x = GameConfig.pauseButtonX();
        float y = GameConfig.pauseButtonY();
        setColor(GameConfig.MENU_BACKGROUND_COLOR);
        shapes.rect(x, y, GameConfig.PAUSE_BUTTON_SIZE, GameConfig.PAUSE_BUTTON_SIZE);

        setColor(GameConfig.MENU_HIGHLIGHT_COLOR);
        float barY = y + (GameConfig.PAUSE_BUTTON_SIZE - GameConfig.PAUSE_BAR_HEIGHT) / 2f;
        float barsWidth = GameConfig.PAUSE_BAR_WIDTH * 2f + GameConfig.PAUSE_BAR_GAP;
        float barX = x + (GameConfig.PAUSE_BUTTON_SIZE - barsWidth) / 2f;
        shapes.rect(barX, barY, GameConfig.PAUSE_BAR_WIDTH, GameConfig.PAUSE_BAR_HEIGHT);
        shapes.rect(barX + GameConfig.PAUSE_BAR_WIDTH + GameConfig.PAUSE_BAR_GAP, barY,
                GameConfig.PAUSE_BAR_WIDTH, GameConfig.PAUSE_BAR_HEIGHT);
    }

    /** Nids-de-poule au sol, puis vehicules, tous du plus loin au plus proche. */
    private void drawObstacles(List<Obstacle> obstacles) {
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            if (obstacles.get(i).getType() == ObstacleType.NID_DE_POULE) {
                drawObstacle(obstacles.get(i));
            }
        }
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            if (obstacles.get(i).getType() != ObstacleType.NID_DE_POULE) {
                drawObstacle(obstacles.get(i));
            }
        }
    }

    private void drawObstacle(Obstacle obstacle) {
        Texture texture;
        float flatten = 1f;
        switch (obstacle.getType()) {
            case CAMION:
                texture = assets.getTruckTexture();
                break;
            case NID_DE_POULE:
                texture = assets.getPotholeTexture();
                flatten = GameConfig.POTHOLE_FLATTEN;
                break;
            case VOITURE:
            default:
                texture = assets.getCarTexture();
                break;
        }
        drawGroundSprite(texture,
                obstacle.getBounds().x + obstacle.getBounds().width / 2f,
                obstacle.getBounds().y,
                obstacle.getBounds().width,
                obstacle.getBounds().height,
                flatten);
    }

    private void drawPassagers(List<Passager> passagers) {
        for (int i = passagers.size() - 1; i >= 0; i--) {
            Passager passager = passagers.get(i);
            drawGroundSprite(assets.getPassengerTexture(),
                    passager.getBounds().x + passager.getBounds().width / 2f,
                    passager.getBounds().y,
                    passager.getBounds().width,
                    passager.getBounds().height,
                    1f);
        }
    }

    private void drawMoto(Moto moto) {
        if (!moto.isBlinkOn()) {
            return;
        }
        drawGroundSprite(assets.getBikeTexture(), moto.getX(), moto.getY(),
                GameConfig.BIKE_WIDTH, GameConfig.BIKE_HEIGHT, 1f);
    }

    /**
     * Dessine un sprite pose au sol a la position monde demandee, avec
     * l'echelle de sa profondeur. Les objets plats (nids-de-poule) sont
     * ecrases et centres sur leur point de contact.
     */
    private void drawGroundSprite(Texture texture, float worldX, float worldY,
                                  float worldWidth, float worldHeight, float flatten) {
        float depth = Perspective.depth(worldY);
        if (depth <= GameConfig.CAMERA_CULL_DEPTH) {
            return;
        }
        float scale = Perspective.scaleForDepth(depth);
        float x = perspective.screenX(worldX, depth);
        float y = Perspective.screenYForDepth(depth);
        float width = worldWidth * scale;
        float height = worldHeight * scale * flatten;
        float bottom = flatten < 1f ? y - height / 2f : y;
        batch.draw(texture, x - width / 2f, bottom, width, height);
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

    /** Combo de passagers, juste au-dessus de la moto. */
    private void drawCombo(int combo) {
        if (combo < 2) {
            return;
        }
        if (combo != displayedCombo) {
            displayedCombo = combo;
            comboText.setLength(0);
            comboText.append("Combo x").append(Math.min(combo, GameConfig.PASSENGER_COMBO_MAX));
            font.getData().setScale(GameConfig.COMBO_FONT_SCALE);
            comboLayout.setText(font, comboText);
        } else {
            font.getData().setScale(GameConfig.COMBO_FONT_SCALE);
        }
        font.setColor(GameConfig.COMBO_COLOR.r, GameConfig.COMBO_COLOR.g,
                GameConfig.COMBO_COLOR.b, 1f);
        font.draw(batch, comboLayout,
                (GameConfig.WORLD_WIDTH - comboLayout.width) / 2f, GameConfig.COMBO_Y);
        // Rend la couleur et l'echelle du score a la suite du rendu.
        font.getData().setScale(GameConfig.SCORE_FONT_SCALE);
        font.setColor(GameConfig.SCORE_COLOR.r, GameConfig.SCORE_COLOR.g,
                GameConfig.SCORE_COLOR.b, 1f);
    }

    /** Alerte d'heure de pointe, au-dessus de la route. */
    private void drawRushBanner() {
        float pulse = 0.65f + 0.35f * MathUtils.sin(
                (float) (System.currentTimeMillis() % 1000) / 1000f * MathUtils.PI2);
        font.getData().setScale(GameConfig.RUSH_BANNER_FONT_SCALE);
        font.setColor(GameConfig.RUSH_BANNER_COLOR.r, GameConfig.RUSH_BANNER_COLOR.g,
                GameConfig.RUSH_BANNER_COLOR.b, Math.max(0.4f, pulse));
        font.draw(batch, rushLayout,
                (GameConfig.WORLD_WIDTH - rushLayout.width) / 2f,
                GameConfig.RUSH_BANNER_Y);
        font.getData().setScale(GameConfig.SCORE_FONT_SCALE);
        font.setColor(GameConfig.SCORE_COLOR.r, GameConfig.SCORE_COLOR.g,
                GameConfig.SCORE_COLOR.b, 1f);
    }

    /** Etincelles dessinees par-dessus les sprites, avec transparence. */
    private void drawParticles() {
        if (particleCount == 0) {
            return;
        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapes.setProjectionMatrix(projection);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < particleCount; i++) {
            float alpha = particleLife[i] / particleMaxLife[i];
            GameConfig.Rgb color = particleColors[i];
            shapes.setColor(color.r, color.g, color.b, alpha);
            shapes.circle(particleX[i], particleY[i],
                    GameConfig.PARTICLE_SIZE * alpha + 1f, PARTICLE_SEGMENTS);
        }
        shapes.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
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
