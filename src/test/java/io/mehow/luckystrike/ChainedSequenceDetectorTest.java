package io.mehow.luckystrike;

import java.util.Collections;
import org.junit.Test;

import static io.mehow.luckystrike.CardSequence.FIGURE;
import static io.mehow.luckystrike.CardSequence.GEMINI;
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
