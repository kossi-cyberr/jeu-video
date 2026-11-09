package cm.motorush.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import cm.motorush.assets.Assets;
import cm.motorush.config.GameConfig;

/**
 * Musique et bruitages (etape 9). Les ressources appartiennent a Assets :
 * seules la lecture et la coupure sont gerees ici.
 */
public class AudioManager {

    private final Sound crashSound;
    private final Sound pickupSound;
    private final Music music;
    private boolean enabled = true;

    public AudioManager(Assets assets) {
        this.crashSound = assets.getCrashSound();
        this.pickupSound = assets.getPickupSound();
        this.music = assets.getThemeMusic();
        music.setLooping(true);
        music.setVolume(GameConfig.MUSIC_VOLUME);
        music.play();
    }

    public void playCollision() {
        if (enabled) {
            crashSound.play(GameConfig.SOUND_VOLUME);
        }
    }

    public void playPassenger() {
        if (enabled) {
            pickupSound.play(GameConfig.SOUND_VOLUME);
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
    }

    /** Arrete la musique ; AssetManager libere ensuite le fichier. */
    public void stop() {
        music.stop();
    }
}
