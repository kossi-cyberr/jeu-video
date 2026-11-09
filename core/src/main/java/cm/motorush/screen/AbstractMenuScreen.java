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
import cm.motorush.render.MenuRenderer;

/**
 * Base des ecrans de menu (etape 7) : navigation haut/bas et validation
 * par Entree. Les sous-classes decident de l'action de chaque option.
 */
public abstract class AbstractMenuScreen implements Screen {

    protected final MotoRushGame game;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport =
            new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
    private final MenuRenderer renderer;
    private final String[] options;

    private int selected;

    protected AbstractMenuScreen(MotoRushGame game, String title, String subtitle, String... options) {
        this.game = game;
        this.options = options;
        this.renderer = new MenuRenderer(title, subtitle, options);
        viewport.apply(true);
    }

    @Override
    public void show() {
        // La selection repart sur la premiere option a chaque visite.
        selected = 0;
        // La fenetre a pu changer de taille depuis la derniere visite.
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(float delta) {
        if (handleInput()) {
            // Une option a change d'ecran : ne pas dessiner cet ecran de plus.
            return;
        }
        ScreenUtils.clear(GameConfig.LETTERBOX_COLOR.r, GameConfig.LETTERBOX_COLOR.g,
                GameConfig.LETTERBOX_COLOR.b, 1f);
        viewport.apply();
        camera.update();
        renderer.draw(camera, selected);
    }

    /**
     * Deplace la selection et valide l'option courante.
     *
     * @return vrai si une option a change d'ecran.
     */
    protected boolean handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selected = (selected + options.length - 1) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selected = (selected + 1) % options.length;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER)) {
            return onOptionSelected(selected);
        }
        return false;
    }

    /**
     * Action de l'option choisie.
     *
     * @return vrai si l'ecran doit changer ; faux pour une action sur place
     *         comme le bouton Son.
     */
    protected abstract boolean onOptionSelected(int index);

    /** Met a jour le libelle d'une option, par exemple son etat (etape 9). */
    protected void setOptionLabel(int index, String label) {
        options[index] = label;
        renderer.setOption(index, label);
    }

    /** Met a jour le sous-titre, par exemple le meilleur score (etape 8). */
    protected void setSubtitle(String subtitle) {
        renderer.setSubtitle(subtitle);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Rien a suspendre.
    }

    @Override
    public void resume() {
        // Rien a reprendre.
    }

    @Override
    public void hide() {
        // L'ecran reste en cache, libere par MotoRushGame.dispose().
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
