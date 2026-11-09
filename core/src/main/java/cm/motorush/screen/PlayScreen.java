package cm.motorush.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import cm.motorush.MotoRushGame;
import cm.motorush.config.GameConfig;
import cm.motorush.logic.Road;
import cm.motorush.logic.Spawner;
import cm.motorush.model.Moto;
import cm.motorush.render.Renderer;

/**
 * Ecran de jeu.
 * Etape 2 : route a trois voies defilante, moto et changement de voie (R2 a R4).
 */
public class PlayScreen implements Screen {

    private final MotoRushGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Renderer renderer;
    private final Moto moto;
    private final Road road;
    private final Spawner spawner;

    public PlayScreen(MotoRushGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        this.viewport.apply(true);
        this.renderer = new Renderer();
        this.moto = new Moto();
        this.road = new Road();
        this.spawner = new Spawner();
    }

    @Override
    public void show() {
        // Pas de ressource a charger : le rendu utilise des formes.
    }

    @Override
    public void render(float delta) {
        handleInput();

        float roadSpeed = GameConfig.ROAD_SPEED_INITIAL;
        moto.update(delta);
        road.update(delta, roadSpeed);
        spawner.update(delta, roadSpeed);

        ScreenUtils.clear(GameConfig.LETTERBOX_COLOR.r, GameConfig.LETTERBOX_COLOR.g,
                GameConfig.LETTERBOX_COLOR.b, 1f);
        viewport.apply();
        camera.update();

        renderer.draw(camera, road, spawner.getObstacles(), moto);
    }

    /** Fleches ou A/Q a gauche, D a droite : un appui = une voie (R3). */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyJustPressed(Input.Keys.A)
                || Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            moto.changeLane(-1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            moto.changeLane(1);
        }
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
