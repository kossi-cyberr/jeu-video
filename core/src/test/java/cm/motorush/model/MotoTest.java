package cm.motorush.model;

import org.junit.jupiter.api.Test;
import cm.motorush.config.GameConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Etape 10 (c) : l'invincibilite qui suit un choc empeche de perdre
 * une nouvelle vie (R9).
 */
class MotoTest {

    @Test
    void invincibilityPreventsLosingAnotherLife() {
        Moto moto = new Moto();
        int initialLives = moto.getLives();

        assertTrue(moto.hit(), "le premier choc doit couter une vie");
        assertEquals(initialLives - 1, moto.getLives());
        assertTrue(moto.isInvincible());

        assertFalse(moto.hit(), "un choc pendant l'invincibilite est ignore");
        assertEquals(initialLives - 1, moto.getLives());

        moto.update(GameConfig.INVINCIBILITY_DURATION + 0.01f);
        assertFalse(moto.isInvincible(), "l'invincibilite doit expirer");
        assertTrue(moto.hit(), "un choc apres l'invincibilite coute une vie");
        assertEquals(initialLives - 2, moto.getLives());
    }

    @Test
    void noLifeIsLostOnceAllLivesAreGone() {
        Moto moto = new Moto();
        while (moto.getLives() > 0) {
            if (moto.isInvincible()) {
                moto.update(GameConfig.INVINCIBILITY_DURATION + 0.01f);
            }
            moto.hit();
        }

        assertFalse(moto.hit());
        assertEquals(0, moto.getLives());
    }
}
