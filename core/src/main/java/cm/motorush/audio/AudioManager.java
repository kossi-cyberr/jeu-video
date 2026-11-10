package cm.motorush.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import cm.motorush.assets.Assets;
import cm.motorush.config.GameConfig;

/**
 * Musique et bruitages (etape 9). Les ressources appartiennent a Assets :
 * seules la lecture et la coupure sont gerees ici.
 * Etape 13 : le choix Son : Oui / Non est memorise d'un lancement a l'autre.
 */
public class AudioManager {

    /** Meme fichier de preferences que le meilleur score. */
    private static final String PREFERENCES_NAME = "moto-taxi-rush";
    private static final String SOUND_ENABLED_KEY = "soundEnabled";

    private final Preferences preferences;
    private final Sound crashSound;
    private final Sound pickupSound;
    private final Music music;
    private boolean enabled;

    public AudioManager(Assets assets) {
        this.preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        this.enabled = preferences.getBoolean(SOUND_ENABLED_KEY, true);
        this.crashSound = assets.getCrashSound();
        this.pickupSound = assets.getPickupSound();
        this.music = assets.getThemeMusic();
        music.setLooping(true);
        music.setVolume(GameConfig.MUSIC_VOLUME);
        if (enabled) {
            music.play();
        }
    }

    public void playCollision() {
        if (enabled) {
            crashSound.play(GameConfig.SOUND_VOLUME);
        }
    }

    /**
     * Bruitage de ramassage, dont la hauteur monte avec le combo pour
     * souligner la serie de passagers.
     */
    public void playPassenger(int combo) {
        if (enabled) {
            float pitch = Math.min(GameConfig.PICKUP_PITCH_MAX,
                    1f + GameConfig.PICKUP_PITCH_STEP * (combo - 1));
            pickupSound.play(GameConfig.SOUND_VOLUME, pitch, 0f);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    /** Bouton Son des menus : coupe ou relance la musique et les bruitages. */
    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            music.play();
        } else {
            music.pause();
        }
        preferences.putBoolean(SOUND_ENABLED_KEY, enabled);
        preferences.flush();
    }

    /** Arrete la musique ; AssetManager libere ensuite le fichier. */
    public void stop() {
        music.stop();
    }
}
