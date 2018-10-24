package io.mehow.luckystrike.card;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static io.mehow.luckystrike.card.Card.Rank.ACE;
import static io.mehow.luckystrike.card.Card.Rank.EIGHT;
import static io.mehow.luckystrike.card.Card.Rank.FIVE;
import static io.mehow.luckystrike.card.Card.Rank.FOUR;
import static io.mehow.luckystrike.card.Card.Rank.JACK;
import static io.mehow.luckystrike.card.Card.Rank.KING;
import static io.mehow.luckystrike.card.Card.Rank.NINE;
import static io.mehow.luckystrike.card.Card.Rank.QUEEN;
import static io.mehow.luckystrike.card.Card.Rank.SEVEN;
import static io.mehow.luckystrike.card.Card.Rank.SIX;
import static io.mehow.luckystrike.card.Card.Rank.TEN;
import static io.mehow.luckystrike.card.Card.Rank.THREE;
import static io.mehow.luckystrike.card.Card.Rank.TWO;
import static io.mehow.luckystrike.card.Card.Suit.CLUBS;
import static io.mehow.luckystrike.card.Card.Suit.DIAMONDS;
import static io.mehow.luckystrike.card.Card.Suit.HEARTS;
import static io.mehow.luckystrike.card.CardSequence.FIGURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public final class FigureSequenceDetectorTest {
  static final Hand NO_SEQUENCE = new Hand(Arrays.asList(
      new Card(ACE, CLUBS, ""),
      new Card(TWO, CLUBS, ""),
      new Card(TWO, CLUBS, ""),
      new Card(THREE, CLUBS, ""),
      new Card(THREE, CLUBS, ""),
      new Card(FOUR, CLUBS, ""),
      new Card(FOUR, CLUBS, ""),
      new Card(FIVE, CLUBS, ""),
      new Card(FIVE, CLUBS, ""),
      new Card(SIX, CLUBS, ""),
      new Card(SIX, CLUBS, ""),
      new Card(SEVEN, CLUBS, ""),
      new Card(SEVEN, CLUBS, ""),
      new Card(EIGHT, CLUBS, ""),
      new Card(EIGHT, CLUBS, ""),
      new Card(NINE, CLUBS, ""),
      new Card(NINE, CLUBS, ""),
      new Card(TEN, CLUBS, ""),
      new Card(TEN, CLUBS, "")
  ));

  static final List<Hand> SEQUENCES_OF_TWO = Arrays.asList(
      new Hand(Arrays.asList(
          new Card(ACE, CLUBS, ""),
          new Card(ACE, DIAMONDS, ""),
          new Card(THREE, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(ACE, CLUBS, ""),
          new Card(THREE, DIAMONDS, ""),
          new Card(JACK, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(TWO, CLUBS, ""),
          new Card(JACK, DIAMONDS, ""),
          new Card(QUEEN, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(TWO, CLUBS, ""),
          new Card(QUEEN, DIAMONDS, ""),
          new Card(KING, HEARTS, "")
      ))
  );

  @Test public void sufficientCountMustBePositive() {
    assertThatCode(() -> new FigureSequenceDetector(-1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasNoCause()
        .hasMessage("sufficientCount must be positive!");
  }

  @Test public void noSequence() {
    CardSequenceDetector detector = new FigureSequenceDetector(2);

    Hand result = detector.detectSequence(NO_SEQUENCE);

    assertThat(result.getSequences()).isEmpty();
  }

  @Test public void tooSmallSequence() {
    CardSequenceDetector detector = new FigureSequenceDetector(3);

    SEQUENCES_OF_TWO.forEach(hand -> {
      Hand result = detector.detectSequence(hand);

      assertThat(result.getSequences()).isEmpty();
    });
  }

  @Test public void validSequences() {
    CardSequenceDetector detector = new FigureSequenceDetector(2);

    SEQUENCES_OF_TWO.forEach(hand -> {
      Hand result = detector.detectSequence(hand);

      assertThat(result.getSequences()).containsOnly(FIGURE);
    });
  }
}
