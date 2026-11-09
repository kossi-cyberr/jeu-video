package cm.motorush.screen;

import cm.motorush.MotoRushGame;

/**
 * Fin de partie (R13, etapes 7 et 8) : score final, meilleur score,
 * rejouer ou revenir au menu.
 */
public class GameOverScreen extends AbstractMenuScreen {

    public GameOverScreen(MotoRushGame game, int score, int highScore) {
        super(game, "Partie terminee",
                "Score : " + score + "\nMeilleur : " + highScore,
                "Rejouer", "Menu");
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
