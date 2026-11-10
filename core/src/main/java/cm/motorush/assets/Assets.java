package cm.motorush.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * Ressources graphiques et sonores (etape 9), chargees une seule fois par
 * libGDX AssetManager puis liberees par dispose(). Voir ASSETS.md pour
 * l'origine et la licence de chaque fichier.
 */
public class Assets {

    private static final String BIKE = "sprites/bike.png";
    private static final String CAR = "sprites/car.png";
    private static final String TRUCK = "sprites/truck.png";
    private static final String POTHOLE = "sprites/pothole.png";
    private static final String PASSENGER = "sprites/passenger.png";
    private static final String HEART = "sprites/heart.png";
    private static final String PORTRAIT = "sprites/portrait.png";

    private static final String CRASH_SOUND = "sounds/crash.wav";
    private static final String PICKUP_SOUND = "sounds/pickup.wav";
    private static final String THEME_MUSIC = "music/theme.wav";

    private final AssetManager manager = new AssetManager();

    private final Texture bike;
    private final Texture car;
    private final Texture truck;
    private final Texture pothole;
    private final Texture passenger;
    private final Texture heart;
    private final Texture portrait;
    private final Sound crashSound;
    private final Sound pickupSound;
    private final Music themeMusic;

    public Assets() {
        manager.load(BIKE, Texture.class);
        manager.load(CAR, Texture.class);
        manager.load(TRUCK, Texture.class);
        manager.load(POTHOLE, Texture.class);
        manager.load(PASSENGER, Texture.class);
        manager.load(HEART, Texture.class);
        manager.load(PORTRAIT, Texture.class);
        manager.load(CRASH_SOUND, Sound.class);
        manager.load(PICKUP_SOUND, Sound.class);
        manager.load(THEME_MUSIC, Music.class);
        manager.finishLoading();

        bike = loadTexture(BIKE);
        car = loadTexture(CAR);
        truck = loadTexture(TRUCK);
        pothole = loadTexture(POTHOLE);
        passenger = loadTexture(PASSENGER);
        heart = loadTexture(HEART);
        portrait = loadTexture(PORTRAIT);
        crashSound = manager.get(CRASH_SOUND, Sound.class);
        pickupSound = manager.get(PICKUP_SOUND, Sound.class);
        themeMusic = manager.get(THEME_MUSIC, Music.class);
    }

    /** Filtrage lineaire : les sprites sont generes en haute resolution. */
    private Texture loadTexture(String path) {
        Texture texture = manager.get(path, Texture.class);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return texture;
    }

    public Texture getBikeTexture() {
        return bike;
    }

    public Texture getCarTexture() {
        return car;
    }

    public Texture getTruckTexture() {
        return truck;
    }

    public Texture getPotholeTexture() {
        return pothole;
    }

    public Texture getPassengerTexture() {
        return passenger;
    }

    public Texture getHeartTexture() {
        return heart;
    }

    /** Medaillon de l'auteur affiche sur le menu principal. */
    public Texture getPortraitTexture() {
        return portrait;
    }

    public Sound getCrashSound() {
        return crashSound;
    }

    public Sound getPickupSound() {
        return pickupSound;
    }

    public Music getThemeMusic() {
        return themeMusic;
    }

    public void dispose() {
        manager.dispose();
    }
}
