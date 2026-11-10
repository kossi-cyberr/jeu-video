package cm.motorush.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import cm.motorush.MotoRushGame;
import cm.motorush.assets.Assets;
import cm.motorush.audio.AudioManager;
import cm.motorush.config.GameConfig;
import cm.motorush.logic.CollisionChecker;
import cm.motorush.logic.DifficultyCurve;
import cm.motorush.logic.Road;
import cm.motorush.logic.ScoreKeeper;
import cm.motorush.logic.Spawner;
import cm.motorush.model.Moto;
import cm.motorush.render.Renderer;

/**
 * Ecran de jeu.
 * Etape 9 : route defilante, moto et objets en sprites, score, difficulte
 * progressive, pause (Echap), fin de partie (R13) et sons.
 */
public class PlayScreen implements Screen {

    private final MotoRushGame game;
    private final AudioManager audio;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Renderer renderer;
    private final Moto moto;
    private final Road road;
    private final Spawner spawner;
    private final ScoreKeeper scoreKeeper;
    private final DifficultyCurve difficulty;
    /** Point touche, converti en coordonnees du monde (reutilise). */
    private final Vector2 touchPoint = new Vector2();

    public PlayScreen(MotoRushGame game, Assets assets, AudioManager audio) {
        this.game = game;
        this.audio = audio;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        this.viewport.apply(true);
        this.renderer = new Renderer(assets);
        this.moto = new Moto();
        this.road = new Road();
        this.spawner = new Spawner();
        this.scoreKeeper = new ScoreKeeper();
        this.difficulty = new DifficultyCurve();
    }

    @Override
    public void show() {
        // La fenetre a pu changer de taille pendant une pause.
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(float delta) {
        if (handleInput()) {
            // La partie vient d'etre mise en pause : rien d'autre a faire.
            return;
        }

        difficulty.update(delta);
        // Les heures de pointe accelereent la route et densifient le trafic.
        float roadSpeed = difficulty.getRoadSpeed() * difficulty.getSpeedMultiplier();
        float spawnInterval = difficulty.getSpawnInterval() / difficulty.getSpawnMultiplier();

        moto.update(delta);
        // La camera pseudo-3D se recale en douceur derriere la moto.
        renderer.update(delta, moto.getX());
        road.update(delta, roadSpeed);
        spawner.update(delta, roadSpeed, spawnInterval);
        scoreKeeper.update(delta);

        if (spawner.collectPassager(moto.getBounds())) {
            scoreKeeper.addPassenger();
            audio.playPassenger(scoreKeeper.getCombo());
            renderer.burst(moto.getX(), moto.getY() + GameConfig.BIKE_HEIGHT * 0.6f,
                    GameConfig.PARTICLE_PICKUP_COLOR);
        }
        if (CollisionChecker.hitsAny(moto.getBounds(), spawner.getObstacles())) {
            if (moto.hit()) {
                audio.playCollision();
                scoreKeeper.breakCombo();
                renderer.shake();
                renderer.burst(moto.getX(), moto.getY() + GameConfig.BIKE_HEIGHT * 0.6f,
                        GameConfig.PARTICLE_CRASH_COLOR);
            }
        }
        if (moto.getLives() <= 0) {
            // R13 : la partie se termine, le PlayScreen est libere par le jeu.
            game.endGame(scoreKeeper.getScore());
            return;
        }

        ScreenUtils.clear(GameConfig.LETTERBOX_COLOR.r, GameConfig.LETTERBOX_COLOR.g,
                GameConfig.LETTERBOX_COLOR.b, 1f);
        viewport.apply();
        camera.update();

        renderer.draw(camera, road, spawner.getObstacles(), spawner.getPassagers(),
                moto, scoreKeeper.getScore(), scoreKeeper.getCombo(), difficulty.isRushHour());
    }

    /**
     * Echap ou Retour mettent en pause ; fleches ou A/Q a gauche, D a droite
     * (R3) ; sur mobile, un appui dans la moitie gauche ou droite de l'ecran.
     *
     * @return vrai si la partie a ete mise en pause.
     */
    private boolean handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.pauseGame();
            return true;
        }
        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPoint);
            if (GameConfig.isPauseButtonHit(touchPoint.x, touchPoint.y)) {
                game.pauseGame();
                return true;
            }
            moto.changeLane(Gdx.input.getX() < Gdx.graphics.getWidth() / 2 ? -1 : 1);
            return false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyJustPressed(Input.Keys.A)
                || Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            moto.changeLane(-1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            moto.changeLane(1);
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Rien a suspendre pour l'instant.
    }

    @Override
    public void resume() {
        // Rien a reprendre pour l'instant.
    }

    @Override
    public void hide() {
        // L'ecran est libere par dispose().
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
