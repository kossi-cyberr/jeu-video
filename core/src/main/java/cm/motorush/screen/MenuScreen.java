package cm.motorush.screen;

import cm.motorush.MotoRushGame;

/**
 * Menu principal (etapes 7 a 9) : Jouer, couper le son, Quitter,
 * et meilleur score.
 */
public class MenuScreen extends AbstractMenuScreen {

    private static final int OPTION_PLAY = 0;
    private static final int OPTION_SOUND = 1;
    private static final int OPTION_QUIT = 2;

    public MenuScreen(MotoRushGame game) {
        super(game, "Moto-Taxi Rush", null, "Jouer", "Son : Oui", "Quitter");
        setPortrait(game.getAssets().getPortraitTexture());
    }

    @Override
    public void show() {
        super.show();
        setSubtitle("Meilleur : " + game.getHighScore());
        refreshSoundOption();
    }

    private void refreshSoundOption() {
        setOptionLabel(OPTION_SOUND, game.isSoundEnabled() ? "Son : Oui" : "Son : Non");
    }

    @Override
    protected boolean onOptionSelected(int index) {
        switch (index) {
            case OPTION_PLAY:
                game.startNewGame();
                return true;
            case OPTION_SOUND:
                game.toggleSound();
                refreshSoundOption();
                return false;
            case OPTION_QUIT:
            default:
                game.quitGame();
                return true;
        }
    }
}
