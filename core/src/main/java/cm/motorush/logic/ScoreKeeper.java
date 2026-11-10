package cm.motorush.logic;

import cm.motorush.config.GameConfig;

/**
 * Score de la partie (R10, R12) : points gagnes en survivant et en
 * ramassant des passagers. Sans dependance graphique.
 * Un combo recompense les passagers ramasses d'affilee : le deuxieme
 * rapporte deux fois plus, le troisieme trois fois plus, etc. Un choc
 * casse la serie.
 */
public class ScoreKeeper {

    private int score;
    /** Passagers ramasses d'affilee depuis le dernier choc. */
    private int combo;
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

    /**
     * Un passager ramasse rapporte SCORE_PER_PASSENGER points multiplies
     * par le rang du combo, plafonne a PASSENGER_COMBO_MAX (R10).
     */
    public void addPassenger() {
        combo++;
        score += GameConfig.SCORE_PER_PASSENGER * Math.min(combo, GameConfig.PASSENGER_COMBO_MAX);
    }

    /** Un choc casse la serie de passagers. */
    public void breakCombo() {
        combo = 0;
    }

    public int getCombo() {
        return combo;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
        combo = 0;
        secondTimer = 0f;
    }
}
