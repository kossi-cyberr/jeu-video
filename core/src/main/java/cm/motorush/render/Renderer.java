package cm.motorush.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cm.motorush.config.GameConfig;

/**
 * Rendu du jeu avec des formes colorees.
 * Etape 1 : uniquement le fond de la zone de jeu.
 */
public class Renderer {

    private final ShapeRenderer shapes;

    public Renderer() {
        this.shapes = new ShapeRenderer();
    }

    /**
     * Remplit la zone de jeu (480 x 800) avec le fond gris fonce.
     * Les zones hors viewport gardent la couleur du clear (bandes noires).
     */
    public void drawBackground(OrthographicCamera camera) {
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(GameConfig.BG_RED, GameConfig.BG_GREEN, GameConfig.BG_BLUE, 1f);
        shapes.rect(0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        shapes.end();
    }

    public void dispose() {
        shapes.dispose();
    }
}
