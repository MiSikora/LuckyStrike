package io.mehow.luckystrike.card;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static io.mehow.luckystrike.card.Card.Rank.FIVE;
import static io.mehow.luckystrike.card.Card.Rank.FOUR;
import static io.mehow.luckystrike.card.Card.Rank.SEVEN;
import static io.mehow.luckystrike.card.Card.Rank.SIX;
import static io.mehow.luckystrike.card.Card.Rank.THREE;
import static io.mehow.luckystrike.card.Card.Rank.TWO;
import static io.mehow.luckystrike.card.Card.Suit.CLUBS;
import static io.mehow.luckystrike.card.Card.Suit.DIAMONDS;
import static io.mehow.luckystrike.card.Card.Suit.HEARTS;
import static io.mehow.luckystrike.card.CardSequence.STAIR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public final class StairSequenceDetectorTest {
  static final Hand NO_SEQUENCE = new Hand(Arrays.asList(
      new Card(TWO, CLUBS, ""),
      new Card(FOUR, CLUBS, ""),
      new Card(SIX, CLUBS, ""),
      new Card(SIX, CLUBS, ""),
      new Card(FOUR, CLUBS, ""),
      new Card(TWO, CLUBS, "")
  ));

  static final List<Hand> SEQUENCES_OF_TWO = Arrays.asList(
      new Hand(Arrays.asList(
          new Card(TWO, CLUBS, ""),
          new Card(THREE, DIAMONDS, ""),
          new Card(FIVE, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(TWO, CLUBS, ""),
          new Card(FOUR, DIAMONDS, ""),
          new Card(FIVE, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(TWO, CLUBS, ""),
          new Card(FOUR, DIAMONDS, ""),
          new Card(FIVE, HEARTS, ""),
          new Card(SEVEN, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(FIVE, CLUBS, ""),
          new Card(FOUR, DIAMONDS, ""),
          new Card(TWO, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(FIVE, CLUBS, ""),
          new Card(THREE, DIAMONDS, ""),
          new Card(TWO, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(SEVEN, CLUBS, ""),
          new Card(FIVE, DIAMONDS, ""),
          new Card(FOUR, HEARTS, ""),
          new Card(TWO, HEARTS, "")
      ))
  );

  static final Hand SEQUENCE_OF_THREE = new Hand(Arrays.asList(
      new Card(TWO, CLUBS, ""),
      new Card(THREE, DIAMONDS, ""),
      new Card(FOUR, HEARTS, "")
  ));

  @Test public void sufficientCountMustBePositive() {
    assertThatCode(() -> new StairSequenceDetector(-1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasNoCause()
        .hasMessage("sufficientCount must be positive!");
  }

  @Test public void noSequence() {
    CardSequenceDetector detector = new StairSequenceDetector(2);

    Hand result = detector.detectSequence(NO_SEQUENCE);

    assertThat(result.getSequences()).isEmpty();
  }

  @Test public void tooSmallSequence() {
    CardSequenceDetector detector = new StairSequenceDetector(3);

    SEQUENCES_OF_TWO.forEach(hand -> {
      Hand result = detector.detectSequence(hand);

      assertThat(result.getSequences()).isEmpty();
    });
  }

  @Test public void validSequences() {
    CardSequenceDetector detector = new StairSequenceDetector(2);

    SEQUENCES_OF_TWO.forEach(hand -> {
      Hand result = detector.detectSequence(hand);

      assertThat(result.getSequences()).containsOnly(STAIR);
    });

    Hand result = detector.detectSequence(SEQUENCE_OF_THREE);

    assertThat(result.getSequences()).containsOnly(STAIR);
  }
}
