package io.mehow.luckystrike.card;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static io.mehow.luckystrike.card.Card.Rank.ACE;
import static io.mehow.luckystrike.card.Card.Rank.THREE;
import static io.mehow.luckystrike.card.Card.Rank.TWO;
import static io.mehow.luckystrike.card.Card.Suit.CLUBS;
import static io.mehow.luckystrike.card.Card.Suit.DIAMONDS;
import static io.mehow.luckystrike.card.Card.Suit.HEARTS;
import static io.mehow.luckystrike.card.Card.Suit.SPADES;
import static io.mehow.luckystrike.card.CardSequence.SUIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public final class SuitSequenceDetectorTest {
  static final Hand NO_SEQUENCE = new Hand(Arrays.asList(
      new Card(TWO, CLUBS, ""),
      new Card(TWO, DIAMONDS, ""),
      new Card(TWO, HEARTS, ""),
      new Card(TWO, SPADES, "")
  ));

  static final List<Hand> SEQUENCES_OF_TWO = Arrays.asList(
      new Hand(Arrays.asList(
          new Card(ACE, CLUBS, ""),
          new Card(TWO, DIAMONDS, ""),
          new Card(THREE, CLUBS, "")
      )),
      new Hand(Arrays.asList(
          new Card(TWO, DIAMONDS, ""),
          new Card(THREE, DIAMONDS, ""),
          new Card(TWO, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(TWO, CLUBS, ""),
          new Card(THREE, HEARTS, ""),
          new Card(THREE, HEARTS, "")
      )),
      new Hand(Arrays.asList(
          new Card(TWO, CLUBS, ""),
          new Card(THREE, SPADES, ""),
          new Card(THREE, SPADES, "")
      ))
  );

  static final Hand SEQUENCE_OF_THREE = new Hand(Arrays.asList(
      new Card(TWO, SPADES, ""),
      new Card(THREE, SPADES, ""),
      new Card(THREE, SPADES, "")
  ));

  @Test public void sufficientCountMustBePositive() {
    assertThatCode(() -> new SuitSequenceDetector(-1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasNoCause()
        .hasMessage("sufficientCount must be positive!");
  }

  @Test public void noSequence() {
    CardSequenceDetector detector = new SuitSequenceDetector(2);

    Hand result = detector.detectSequence(NO_SEQUENCE);

    assertThat(result.getSequences()).isEmpty();
  }

  @Test public void tooSmallSequence() {
    CardSequenceDetector detector = new SuitSequenceDetector(3);

    SEQUENCES_OF_TWO.forEach(hand -> {
      Hand result = detector.detectSequence(hand);

      assertThat(result.getSequences()).isEmpty();
    });
  }

  @Test public void validSequences() {
    CardSequenceDetector detector = new SuitSequenceDetector(2);

    SEQUENCES_OF_TWO.forEach(hand -> {
      Hand result = detector.detectSequence(hand);

      assertThat(result.getSequences()).containsOnly(SUIT);
    });

    Hand result = detector.detectSequence(SEQUENCE_OF_THREE);

    assertThat(result.getSequences()).containsOnly(SUIT);
  }
}
