package cm.motorush.logic;

import org.junit.jupiter.api.Test;
import cm.motorush.config.GameConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Etape 12 : les passagers ramasses d'affilee rapportent de plus en plus,
 * un choc casse la serie et le multiplicateur est plafonne.
 */
class ScoreKeeperTest {

    @Test
    void comboMultipliesPassengerPoints() {
        ScoreKeeper keeper = new ScoreKeeper();

        keeper.addPassenger();
        assertEquals(GameConfig.SCORE_PER_PASSENGER, keeper.getScore());
        assertEquals(1, keeper.getCombo());

        keeper.addPassenger();
        assertEquals(GameConfig.SCORE_PER_PASSENGER * 3, keeper.getScore());
        assertEquals(2, keeper.getCombo());

        keeper.addPassenger();
        assertEquals(GameConfig.SCORE_PER_PASSENGER * 6, keeper.getScore());
        assertEquals(3, keeper.getCombo());
    }

    @Test
    void comboStopsAtConfiguredMaximum() {
        ScoreKeeper keeper = new ScoreKeeper();
        for (int i = 0; i < GameConfig.PASSENGER_COMBO_MAX + 4; i++) {
            keeper.addPassenger();
        }

        int expected = 0;
        for (int rank = 1; rank <= GameConfig.PASSENGER_COMBO_MAX + 4; rank++) {
            expected += GameConfig.SCORE_PER_PASSENGER
                    * Math.min(rank, GameConfig.PASSENGER_COMBO_MAX);
        }
        assertEquals(expected, keeper.getScore());
        assertEquals(GameConfig.PASSENGER_COMBO_MAX + 4, keeper.getCombo());
    }

    @Test
    void crashBreaksTheCombo() {
        ScoreKeeper keeper = new ScoreKeeper();
        keeper.addPassenger();
        keeper.addPassenger();

        keeper.breakCombo();
        assertEquals(0, keeper.getCombo());

        keeper.addPassenger();
        assertEquals(GameConfig.SCORE_PER_PASSENGER * 3 + GameConfig.SCORE_PER_PASSENGER,
                keeper.getScore());
        assertEquals(1, keeper.getCombo());
    }

    @Test
    void resetClearsScoreAndCombo() {
        ScoreKeeper keeper = new ScoreKeeper();
        keeper.addPassenger();
        keeper.addPassenger();

        keeper.reset();

        assertEquals(0, keeper.getScore());
        assertEquals(0, keeper.getCombo());
    }
}
