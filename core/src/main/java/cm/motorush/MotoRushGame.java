package cm.motorush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import cm.motorush.screen.PlayScreen;

/**
 * Classe principale du jeu : cree les ecrans et gere les transitions.
 * Etape 1 : lance directement PlayScreen (le menu arrive a l'etape 7).
 */
public class MotoRushGame extends Game {

    @Override
    public void create() {
        setScreen(new PlayScreen(this));
    }

    /**
     * Game.dispose() n'appelle que Screen.hide() : on libere explicitement
     * l'ecran courant pour eviter toute fuite de ressources (R : dispose).
     */
    @Override
    public void dispose() {
        Screen current = getScreen();
        super.dispose();
        if (current != null) {
            current.dispose();
        }
    }
}
