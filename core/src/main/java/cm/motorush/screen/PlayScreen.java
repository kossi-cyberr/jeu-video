package cm.motorush.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import cm.motorush.MotoRushGame;
import cm.motorush.config.GameConfig;
import cm.motorush.render.Renderer;

/**
 * Ecran de jeu.
 * Etape 1 : camera orthographique, FitViewport 480 x 800 (R1) et fond gris fonce.
 */
public class PlayScreen implements Screen {

    private final MotoRushGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Renderer renderer;

    public PlayScreen(MotoRushGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        this.viewport.apply(true);
        this.renderer = new Renderer();
    }

    @Override
    public void show() {
        // Pas de ressources a charger a l'etape 1.
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GameConfig.LETTERBOX_RED, GameConfig.LETTERBOX_GREEN, GameConfig.LETTERBOX_BLUE, 1f);

        viewport.apply();
        camera.update();

        renderer.drawBackground(camera);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Rien a suspendre a l'etape 1.
    }

    @Override
    public void resume() {
        // Rien a reprendre a l'etape 1.
    }

    @Override
    public void hide() {
        // L'ecran est detruit juste apres par dispose().
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
