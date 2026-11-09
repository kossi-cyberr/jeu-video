package cm.motorush.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import cm.motorush.MotoRushGame;

/** Pause (etape 7) : reprendre la partie en cours ou revenir au menu. */
public class PauseScreen extends AbstractMenuScreen {

    public PauseScreen(MotoRushGame game) {
        super(game, "Pause", null, "Reprendre", "Menu");
    }

    /** Echap quitte aussi la pause (controle du cahier). */
    @Override
    protected boolean handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.resumeGame();
            return true;
        }
        return super.handleInput();
    }

    @Override
    protected void onOptionSelected(int index) {
        if (index == 0) {
            game.resumeGame();
        } else {
            game.showMenu();
        }
    }
}
