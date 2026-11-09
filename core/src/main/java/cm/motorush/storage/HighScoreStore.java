package cm.motorush.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Meilleur score persistant (R13, etape 8), stocke avec libGDX Preferences.
 * Le fichier est propre a la plateforme : pas de base de donnees.
 */
public class HighScoreStore {

    private static final String PREFERENCES_NAME = "moto-taxi-rush";
    private static final String HIGH_SCORE_KEY = "highScore";

    private final Preferences preferences;

    public HighScoreStore() {
        this.preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    public int getHighScore() {
        return preferences.getInteger(HIGH_SCORE_KEY, 0);
    }

    /**
     * Enregistre le score s'il bat le record.
     *
     * @return vrai si le meilleur score a ete battu.
     */
    public boolean submit(int score) {
        if (score <= getHighScore()) {
            return false;
        }
        preferences.putInteger(HIGH_SCORE_KEY, score);
        preferences.flush();
        return true;
    }
}
