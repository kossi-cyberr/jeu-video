package cm.motorush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import cm.motorush.screen.GameOverScreen;
import cm.motorush.screen.MenuScreen;
import cm.motorush.screen.PauseScreen;
import cm.motorush.screen.PlayScreen;

/**
 * Classe principale du jeu : cree les ecrans et gere les transitions.
 * Etape 7 : menu, partie, pause et game over.
 */
public class MotoRushGame extends Game {

    private MenuScreen menuScreen;
    private PlayScreen playScreen;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen;

    @Override
    public void create() {
        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }

    /** Lance une partie neuve : aucun reste de la precedente. */
    public void startNewGame() {
        disposeGameOverScreen();
        disposePlayScreen();
        playScreen = new PlayScreen(this);
        setScreen(playScreen);
    }

    /** Met la partie en cours en pause ; son etat est conserve. */
    public void pauseGame() {
        if (playScreen == null) {
            return;
        }
        if (pauseScreen == null) {
            pauseScreen = new PauseScreen(this);
        }
        setScreen(pauseScreen);
    }

    /** Reprend la partie mise en pause. */
    public void resumeGame() {
        if (playScreen != null) {
            setScreen(playScreen);
        } else {
            showMenu();
        }
    }

    /** Termine la partie (R13) et affiche le score final. */
    public void endGame(int score) {
        disposePlayScreen();
        disposeGameOverScreen();
        gameOverScreen = new GameOverScreen(this, score);
        setScreen(gameOverScreen);
    }

    /** Revient au menu principal en abandonnant la partie en cours. */
    public void showMenu() {
        disposePlayScreen();
        disposeGameOverScreen();
        setScreen(menuScreen);
    }

    public void quitGame() {
        Gdx.app.exit();
    }

    private void disposePlayScreen() {
        if (playScreen != null) {
            playScreen.dispose();
            playScreen = null;
        }
    }

    private void disposeGameOverScreen() {
        if (gameOverScreen != null) {
            gameOverScreen.dispose();
            gameOverScreen = null;
        }
    }

    /**
     * Game.dispose() n'appelle que Screen.hide() : on libere explicitement
     * tous les ecrans, y compris ceux gardes en cache.
     */
    @Override
    public void dispose() {
        super.dispose();
        if (menuScreen != null) {
            menuScreen.dispose();
            menuScreen = null;
        }
        if (pauseScreen != null) {
            pauseScreen.dispose();
            pauseScreen = null;
        }
        disposePlayScreen();
        disposeGameOverScreen();
    }
}
