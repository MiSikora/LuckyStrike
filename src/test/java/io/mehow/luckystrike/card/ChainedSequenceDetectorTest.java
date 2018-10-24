package io.mehow.luckystrike.card;

import java.util.Collections;
import org.junit.Test;

import static io.mehow.luckystrike.card.CardSequence.FIGURE;
import static io.mehow.luckystrike.card.CardSequence.GEMINI;
import static org.assertj.core.api.Assertions.assertThat;

public final class ChainedSequenceDetectorTest {

  @Test public void usesAllChains() {
    CardSequenceDetector detector = new ChainedSequenceDetector.Builder()
        .add(hand -> hand.addSequence(GEMINI))
        .add(hand -> hand.addSequence(FIGURE))
        .build();

    Hand result = detector.detectSequence(new Hand(Collections.emptyList()));

    assertThat(result.getSequences()).containsOnly(GEMINI, FIGURE);
  }
}
