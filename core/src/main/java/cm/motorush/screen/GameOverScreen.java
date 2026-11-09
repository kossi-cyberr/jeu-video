package cm.motorush.screen;

import cm.motorush.MotoRushGame;

/** Fin de partie (R13, etape 7) : score final, rejouer ou revenir au menu. */
public class GameOverScreen extends AbstractMenuScreen {

    public GameOverScreen(MotoRushGame game, int score) {
        super(game, "Partie terminee", "Score : " + score, "Rejouer", "Menu");
    }

    @Override
    protected void onOptionSelected(int index) {
        if (index == 0) {
            game.startNewGame();
        } else {
            game.showMenu();
        }
    }
}
