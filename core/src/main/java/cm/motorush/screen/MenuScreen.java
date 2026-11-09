package cm.motorush.screen;

import cm.motorush.MotoRushGame;

/** Menu principal (etape 7) : Jouer ou Quitter. */
public class MenuScreen extends AbstractMenuScreen {

    public MenuScreen(MotoRushGame game) {
        super(game, "Moto-Taxi Rush", null, "Jouer", "Quitter");
    }

    @Override
    protected void onOptionSelected(int index) {
        if (index == 0) {
            game.startNewGame();
        } else {
            game.quitGame();
        }
    }
}
