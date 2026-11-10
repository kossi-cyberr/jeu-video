package cm.motorush;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import cm.motorush.assets.Assets;
import cm.motorush.audio.AudioManager;
import cm.motorush.screen.GameOverScreen;
import cm.motorush.screen.MenuScreen;
import cm.motorush.screen.PauseScreen;
import cm.motorush.screen.PlayScreen;
import cm.motorush.storage.HighScoreStore;

/**
 * Classe principale du jeu : cree les ecrans et gere les transitions.
 * Etape 9 : menu, partie, pause, game over, meilleur score et ressources.
 */
public class MotoRushGame extends Game {

    private Assets assets;
    private AudioManager audio;
    private HighScoreStore highScoreStore;
    private MenuScreen menuScreen;
    private PlayScreen playScreen;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen;

    @Override
    public void create() {
        assets = new Assets();
        audio = new AudioManager(assets);
        highScoreStore = new HighScoreStore();
        menuScreen = new MenuScreen(this);
        // Le bouton Retour doit etre recu par les ecrans (pause) au lieu de
        // fermer l'application. Il est mappe dans les ecrans concernes.
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        setScreen(menuScreen);
    }

    /**
     * Passage de l'application en arriere-plan (appel, bouton d'accueil) :
     * la partie en cours se met en pause plutot que de continuer a l'aveugle.
     */
    @Override
    public void pause() {
        super.pause();
        if (playScreen != null && getScreen() == playScreen) {
            pauseGame();
        }
    }

    public int getHighScore() {
        return highScoreStore.getHighScore();
    }

    public boolean isSoundEnabled() {
        return audio.isEnabled();
    }

    public void toggleSound() {
        audio.toggle();
    }

    /** Lance une partie neuve : aucun reste de la precedente. */
    public void startNewGame() {
        disposeGameOverScreen();
        disposePlayScreen();
        playScreen = new PlayScreen(this, assets, audio);
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

    /** Termine la partie (R13) et affiche le score final et le record. */
    public void endGame(int score) {
        disposePlayScreen();
        disposeGameOverScreen();
        highScoreStore.submit(score);
        gameOverScreen = new GameOverScreen(this, score, highScoreStore.getHighScore());
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
        if (audio != null) {
            audio.stop();
        }
        if (assets != null) {
            assets.dispose();
            assets = null;
        }
    }
}
