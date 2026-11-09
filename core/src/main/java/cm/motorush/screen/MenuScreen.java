package cm.motorush.screen;

import cm.motorush.MotoRushGame;

/** Menu principal (etapes 7 et 8) : Jouer, Quitter et meilleur score. */
public class MenuScreen extends AbstractMenuScreen {

    public MenuScreen(MotoRushGame game) {
        super(game, "Moto-Taxi Rush", null, "Jouer", "Quitter");
    }

    @Override
    public void show() {
        super.show();
        setSubtitle("Meilleur : " + game.getHighScore());
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
