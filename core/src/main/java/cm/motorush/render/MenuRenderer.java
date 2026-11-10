package cm.motorush.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cm.motorush.config.GameConfig;

/**
 * Rendu commun des ecrans de menu (etape 7) : fond, titre, sous-titre
 * et options avec surbrillance. Les textes sont mis en page a la
 * construction, et le sous-titre seulement lorsqu'il change (etape 8).
 */
public class MenuRenderer {

    private final ShapeRenderer shapes;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final GlyphLayout titleLayout;
    private final GlyphLayout authorLayout;
    /** Nul quand l'ecran n'a pas de sous-titre. */
    private GlyphLayout subtitleLayout;
    private final GlyphLayout[] optionLayouts;
    /** Nul quand l'ecran n'affiche pas de medaillon photo. */
    private Texture portrait;

    public MenuRenderer(String title, String subtitle, String[] options) {
        this.shapes = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();

        font.getData().setScale(GameConfig.MENU_TITLE_FONT_SCALE);
        this.titleLayout = new GlyphLayout(font, title);

        font.getData().setScale(GameConfig.MENU_AUTHOR_FONT_SCALE);
        this.authorLayout = new GlyphLayout(font, "par " + GameConfig.AUTHOR_NAME);

        font.getData().setScale(GameConfig.MENU_FONT_SCALE);
        setSubtitle(subtitle);

        this.optionLayouts = new GlyphLayout[options.length];
        for (int i = 0; i < options.length; i++) {
            optionLayouts[i] = new GlyphLayout(font, options[i]);
        }
    }

    public void draw(OrthographicCamera camera, int selected) {
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        setColor(GameConfig.MENU_BACKGROUND_COLOR);
        shapes.rect(0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        setColor(GameConfig.MENU_HIGHLIGHT_COLOR);
        shapes.rect((GameConfig.WORLD_WIDTH - GameConfig.MENU_HIGHLIGHT_WIDTH) / 2f,
                optionCenterY(selected) - GameConfig.MENU_HIGHLIGHT_HEIGHT / 2f,
                GameConfig.MENU_HIGHLIGHT_WIDTH, GameConfig.MENU_HIGHLIGHT_HEIGHT);
        shapes.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (portrait != null) {
            batch.draw(portrait,
                    (GameConfig.WORLD_WIDTH - GameConfig.MENU_PORTRAIT_SIZE) / 2f,
                    GameConfig.MENU_PORTRAIT_Y - GameConfig.MENU_PORTRAIT_SIZE / 2f,
                    GameConfig.MENU_PORTRAIT_SIZE, GameConfig.MENU_PORTRAIT_SIZE);
        }

        font.getData().setScale(GameConfig.MENU_TITLE_FONT_SCALE);
        font.setColor(GameConfig.MENU_TITLE_COLOR.r, GameConfig.MENU_TITLE_COLOR.g,
                GameConfig.MENU_TITLE_COLOR.b, 1f);
        font.draw(batch, titleLayout, (GameConfig.WORLD_WIDTH - titleLayout.width) / 2f,
                GameConfig.MENU_TITLE_Y);

        font.getData().setScale(GameConfig.MENU_FONT_SCALE);
        if (subtitleLayout != null) {
            font.setColor(GameConfig.MENU_TEXT_COLOR.r, GameConfig.MENU_TEXT_COLOR.g,
                    GameConfig.MENU_TEXT_COLOR.b, 1f);
            font.draw(batch, subtitleLayout, (GameConfig.WORLD_WIDTH - subtitleLayout.width) / 2f,
                    GameConfig.MENU_SUBTITLE_Y);
        }

        for (int i = 0; i < optionLayouts.length; i++) {
            GameConfig.Rgb color = i == selected
                    ? GameConfig.MENU_SELECTED_TEXT_COLOR
                    : GameConfig.MENU_TEXT_COLOR;
            font.setColor(color.r, color.g, color.b, 1f);
            font.draw(batch, optionLayouts[i],
                    (GameConfig.WORLD_WIDTH - optionLayouts[i].width) / 2f,
                    optionCenterY(i) + optionLayouts[i].height / 2f);
        }

        font.getData().setScale(GameConfig.MENU_AUTHOR_FONT_SCALE);
        font.setColor(GameConfig.MENU_TEXT_COLOR.r, GameConfig.MENU_TEXT_COLOR.g,
                GameConfig.MENU_TEXT_COLOR.b, 1f);
        font.draw(batch, authorLayout, (GameConfig.WORLD_WIDTH - authorLayout.width) / 2f,
                GameConfig.MENU_AUTHOR_Y);

        batch.end();
    }

    /** Affiche un medaillon rond au-dessus du titre (menu principal). */
    public void setPortrait(Texture portrait) {
        this.portrait = portrait;
    }

    /** Remplace le libelle d'une option, par exemple l'etat du son. */
    public void setOption(int index, String text) {
        font.getData().setScale(GameConfig.MENU_FONT_SCALE);
        optionLayouts[index] = new GlyphLayout(font, text);
    }

    /** Remplace le sous-titre, par exemple pour le meilleur score a jour. */
    public void setSubtitle(String subtitle) {
        font.getData().setScale(GameConfig.MENU_FONT_SCALE);
        this.subtitleLayout = subtitle == null || subtitle.isEmpty()
                ? null
                : new GlyphLayout(font, subtitle);
    }

    private static float optionCenterY(int index) {
        return GameConfig.MENU_FIRST_OPTION_Y - index * GameConfig.MENU_OPTION_SPACING;
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
