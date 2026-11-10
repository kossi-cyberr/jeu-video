package cm.motorush.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import cm.motorush.MotoRushGame;

/**
 * Pause (etapes 7 et 9) : reprendre, revenir au menu ou couper le son.
 */
public class PauseScreen extends AbstractMenuScreen {

    private static final int OPTION_RESUME = 0;
    private static final int OPTION_MENU = 1;
    private static final int OPTION_SOUND = 2;

    public PauseScreen(MotoRushGame game) {
        super(game, "Pause", null, "Reprendre", "Menu", "Son : Oui");
    }

    @Override
    public void show() {
        super.show();
        refreshSoundOption();
    }

    /** Echap ou Retour quittent aussi la pause (controle du cahier). */
    @Override
    protected boolean handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.resumeGame();
            return true;
        }
        return super.handleInput();
    }

    private void refreshSoundOption() {
        setOptionLabel(OPTION_SOUND, game.isSoundEnabled() ? "Son : Oui" : "Son : Non");
    }

    @Override
    protected boolean onOptionSelected(int index) {
        switch (index) {
            case OPTION_RESUME:
                game.resumeGame();
                return true;
            case OPTION_MENU:
                game.showMenu();
                return true;
            case OPTION_SOUND:
            default:
                game.toggleSound();
                refreshSoundOption();
                return false;
        }
    }
}
