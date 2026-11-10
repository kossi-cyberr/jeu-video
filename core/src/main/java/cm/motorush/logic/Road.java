package cm.motorush.logic;

import cm.motorush.config.GameConfig;

/**
 * Defilement de la route : decalage vertical des pointilles.
 * La vitesse est fournie par l'appelant (constante a l'etape 2,
 * DifficultyCurve a l'etape 6).
 */
public class Road {

    private static final float DASH_PERIOD = GameConfig.DASH_LENGTH + GameConfig.DASH_GAP;

    private float dashOffset;
    /** Distance parcourue depuis le debut, pour caler le decor (lampadaires). */
    private float scroll;

    /** Avance les pointilles vers le bas (vers le joueur). */
    public void update(float delta, float speed) {
        dashOffset = (dashOffset + speed * delta) % DASH_PERIOD;
        if (dashOffset < 0f) {
            dashOffset += DASH_PERIOD;
        }
        scroll += speed * delta;
    }

    /** Distance totale defilee depuis le debut de la partie. */
    public float getScroll() {
        return scroll;
    }

    /** Decalage courant, toujours dans [0, DASH_PERIOD). */
    public float getDashOffset() {
        return dashOffset;
    }

    public void reset() {
        dashOffset = 0f;
        scroll = 0f;
    }
}
