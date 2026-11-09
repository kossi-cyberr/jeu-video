package cm.motorush.logic;

import cm.motorush.config.GameConfig;

/**
 * Score de la partie (R10, R12) : points gagnes en survivant et en
 * ramassant des passagers. Sans dependance graphique.
 */
public class ScoreKeeper {

    private int score;
    /** Temps ecoule depuis le dernier point de survie. */
    private float secondTimer;

    /** Ajoute SCORE_PER_SECOND points par seconde entiere ecoulee (R12). */
    public void update(float delta) {
        secondTimer += delta;
        while (secondTimer >= 1f) {
            secondTimer -= 1f;
            score += GameConfig.SCORE_PER_SECOND;
        }
    }

    /** Un passager ramasse rapporte SCORE_PER_PASSENGER points (R10). */
    public void addPassenger() {
        score += GameConfig.SCORE_PER_PASSENGER;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
        secondTimer = 0f;
    }
}
